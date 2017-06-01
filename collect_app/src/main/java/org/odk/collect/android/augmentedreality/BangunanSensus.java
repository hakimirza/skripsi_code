package org.odk.collect.android.augmentedreality;

/**
 * Created by Septiawan Aji Pradan on 3/14/2017.
 */

public class BangunanSensus {
    private int id;
    private String namaKRT;
    private Double lat;
    private Double lon;
    private String pathFoto;
    private String noSensus;
    private String noFisik;
    private String sls;

    public BangunanSensus(){

    }
    public BangunanSensus(String namaKRT, Double lat, Double lon, String pathFoto){
        this.namaKRT = namaKRT;
        this.lat = lat;
        this.lon = lon;
        this.pathFoto = pathFoto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaKRT() {
        return namaKRT;
    }

    public void setNamaKRT(String namaKRT) {
        this.namaKRT = namaKRT;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public String getNoSensus() {
        return noSensus;
    }

    public void setNoSensus(String noSensus) {
        this.noSensus = noSensus;
    }

    public String getNoFisik() {
        return noFisik;
    }

    public void setNoFisik(String noFisik) {
        this.noFisik = noFisik;
    }

    public String getSls() {
        return sls;
    }

    public void setSls(String sls) {
        this.sls = sls;
    }
}
