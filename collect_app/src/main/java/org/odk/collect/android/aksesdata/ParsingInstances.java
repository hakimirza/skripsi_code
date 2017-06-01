package org.odk.collect.android.aksesdata;

import android.util.Log;

import org.odk.collect.android.augmentedreality.BangunanSensus;
import org.odk.collect.android.augmentedreality.formisian.BangunanSensusOnMaps;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Septiawan Aji Pradan on 5/31/2017.
 */

public class ParsingInstances {
    private XmlPullParserFactory xmlPullParserFactory;

    public BangunanSensus parseXml(String directory) throws IOException{
        BangunanSensus bangunanSensus = new BangunanSensus();
        try{
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParserFactory.setNamespaceAware(false);
            XmlPullParser parser = xmlPullParserFactory.newPullParser();

            File file = new File(directory);
            FileInputStream is = new FileInputStream(file);
            parser.setInput(is,null);
            bangunanSensus = getLoadedCmlValues(parser);
        }catch (XmlPullParserException e){
            e.printStackTrace();
        }
        return bangunanSensus;
    }

    private BangunanSensus getLoadedCmlValues(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        String name = null;
        BangunanSensus bangunanSensus = new BangunanSensus();
        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG){
                name = parser.getName();
                //no_sensus,no_fisik,sls,foto_bangunan,location
                if(name.equals("no_sensus")){
                    bangunanSensus.setNoSensus(parser.nextText());
                }else if(name.equals("no_fisik")){
                    bangunanSensus.setNoFisik(parser.nextText());
                }else if(name.equals("sls")){
                    bangunanSensus.setSls(parser.nextText());
                }else if(name.equals("location")){
                    String[] poin = parser.nextText().split(" ");
                    bangunanSensus.setLat(Double.parseDouble(poin[0]));
                    bangunanSensus.setLon(Double.parseDouble(poin[1]));
                }else if(name.equals("foto_bangunan")){
                    bangunanSensus.setPathFoto(parser.nextText());
                }
            }
            eventType = parser.next();
        }
        return bangunanSensus;
    }
}
