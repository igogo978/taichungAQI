/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.igogo.taichungaqi;

/**
 *
 * @author igogo
 */
public class SiteAQI {
    private String siteName;
    private String aqiLevel;
    private String aqiValue;
    private String mp3;
    private String publishTime;

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }
    

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getAqiLevel() {
        return aqiLevel;
    }

    public void setAqiLevel(String aqiLevel) {
        this.aqiLevel = aqiLevel;
    }

    public String getAqiValue() {
        return aqiValue;
    }

    public void setAqiValue(String aqiValue) {
        this.aqiValue = aqiValue;
    }

    
}
