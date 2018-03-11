package app.androidparkapp.androidparkapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mustafaspc on 11-Mar-18.
 */

public class OtoparkUcretler {

    private String ucretId;
    private boolean Durum;
    private String giris;
    private String cikis;
    public String borc;



    public OtoparkUcretler(){

    }

    public OtoparkUcretler(String ucretId,boolean Durum,String giris,String cikis){
        this.ucretId=ucretId;
        this.Durum=Durum;
        this.giris=giris;
        this.cikis=cikis;
    }

    public String borcHesapla(String giris, String cikis)throws ParseException {



        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date date1 = sdf.parse(giris.substring(10,19));
            Date date2 = sdf.parse(cikis.substring(10,19));
            long diff=((date2.getTime()-date1.getTime())/1000);

            borc=Long.toString(diff);


        }catch (ParseException e){
            e.printStackTrace();
        }

        return borc;
    }



    public boolean isDurum() {
        return Durum;
    }

    public void setDurum(boolean durum) {
        Durum = durum;
    }

    public String getGiris() {
        return giris;
    }

    public void setGiris(String giris) {
        this.giris = giris;
    }

    public String getCikis() {
        return cikis;
    }

    public void setCikis(String cikis) {
        this.cikis = cikis;
    }
    public String getBorc() {
        return borc;
    }

    public void setBorc(String borc) {
        this.borc = borc;
    }

    public String getUcretId() {
        return ucretId;
    }

    public void setUcretId(String ucretId) {
        this.ucretId = ucretId;
    }
}
