/*
 * Copyright (C) 2012-13 Dobility, Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.odk.collect.android.downloadinstance;

import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.kxml2.kdom.Document;

import java.io.File;
import java.io.IOException;


/**
 * CTOSurvey contribution to address issues with Android 4.3 systems not
 * properly flushing OpenSSL CipherStreams.
 *
 * SCTO-789
 *
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 28/9/2013
 * Time: 9:57 μμ
 */
public final class BadXMLFixer {

    private static final String t= "BadXMLFixer";

    private static final String XML_HEADER = "<?xml version='1.0' ?>";
    private static final String ENCODING = "UTF-8";

    public static Document fixBadXML(File xmlFile) throws CannotFixXMLException {
        Log.d(t,"Trying to fix the submission " + xmlFile.getAbsolutePath());

        try {
            String originalXML = FileUtils.readFileToString(xmlFile, ENCODING);
            String fixedXML = fixXML(originalXML);
            File tempFile = File.createTempFile(xmlFile.getName(), ".fixed.xml");
            FileUtils.writeStringToFile(tempFile, fixedXML, ENCODING);
            return XmlManipulationUtils.parseXml(tempFile);
        } catch (IOException e) {
            Log.d(t,e.getMessage(), e);
            throw new CannotFixXMLException("Cannot fix " + xmlFile.getAbsolutePath(), e);
        } catch (ParsingException e) {
            Log.d(t,e.getMessage(), e);
            throw new CannotFixXMLException("Cannot fix " + xmlFile.getAbsolutePath(), e);
        } catch (FileSystemException e) {
            Log.d(t,e.getMessage(), e);
            throw new CannotFixXMLException("Cannot fix " + xmlFile.getAbsolutePath(), e);
        }
    }

    protected static String fixXML(String originalXML) throws CannotFixXMLException {
        // try to find the name of the root element, that is the formId
        int startIndex = XML_HEADER.length() + 1;
        int endIndex = originalXML.indexOf(" ", startIndex);
        String formId = originalXML.substring(startIndex, endIndex);

        Log.d(t,"Trying to fix a submission of the form: " + formId);

        // try to see if the last part is a part of the form id
        int idClosingTagIndex = originalXML.lastIndexOf("</");
        if (idClosingTagIndex == -1) {
            throw new CannotFixXMLException("Cannot find a single closing tag in this file!");
        }

        String lastPart = originalXML.substring(idClosingTagIndex + 2);
        if (lastPart.equals("") || (formId + ">").startsWith(lastPart)) {
            // this is the easy case just fill in the rest of the id.
            return originalXML.substring(0, idClosingTagIndex) + "</" + formId + ">";
        } else if (("meta></" + formId + ">").startsWith(lastPart)) {
            // that means that perhaps the entire closing tag is missing, let's give it a try
            return originalXML.substring(0, idClosingTagIndex) + "</meta></" + formId + ">";
        } else {
            throw new CannotFixXMLException("Cannot understand where this file was truncated");
        }
    }
}
