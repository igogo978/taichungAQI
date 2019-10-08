/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.igogo.taichungaqi;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author igogo
 */
public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws IOException, JavaLayerException {
//        current working directory
        String cwd = System.getProperty("user.dir");
        SiteAQI siteAQI = new SiteAQI();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File("settings.json"));
        String site = root.path("siteName").asText();
        String aqiURL = root.path("taichungURL").asText();
        Integer alarm = root.path("alarm").asInt();

        siteAQI = parseHtml(aqiURL, site);
        //寫入log
        FileHandler fh = new FileHandler("aqiData.log");
        // configure the logger with handler and formatter 
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
        logger.addHandler(fh);

        logger.info(mapper.writeValueAsString(siteAQI));

        //是否播放到警戒值
        if (Integer.valueOf(siteAQI.getAqiValue()) >= alarm) {
            File audio = new File(cwd + "/mp3/" + siteAQI.getMp3());
            if (audio.exists()) {
                logger.info("play mp3:"+ siteAQI.getMp3());
                FileInputStream fis = new FileInputStream(audio);
                Player playMP3 = new Player(fis);
                playMP3.play();
            }

        }

//        是否播放  logger.info(root.path(siteAQI.getAqiLevel()).asText().toLowerCase());
//        String isPlay = root.path(siteAQI.getAqiLevel()).asText().toLowerCase();
//        if (isPlay.equals("play")) {
//            File audio = new File(cwd + "/mp3/" + siteAQI.getMp3());
//            if (audio.exists()) {
//                logger.info("play mp3 file");
//                FileInputStream fis = new FileInputStream(audio);
//                Player playMP3 = new Player(fis);
//                playMP3.play();
//            }
//
//        }

    }

    public static String aqiLevel(int aqi) {
        String aqiLevel = "";

        if (aqi > 0 && aqi <= 50) {
            aqiLevel = "green";
        }
        if (aqi >= 51 && aqi <= 100) {
            aqiLevel = "yellow";
        }
        if (aqi >= 101 && aqi <= 150) {
            aqiLevel = "orange";
        }
        if (aqi >= 151 && aqi <= 200) {
            aqiLevel = "red";
        }

        if (aqi >= 201 && aqi <= 300) {
            aqiLevel = "purple";
        }

        if (aqi >= 301 && aqi <= 500) {
            aqiLevel = "darkred";
        }

        return aqiLevel;
    }

    public static SiteAQI parseHtml(String aqiURL, String site) throws IOException {
        SiteAQI siteAQI = new SiteAQI();
        Document doc = Jsoup.connect(aqiURL).get();

        Element table = doc.select("table").get(0);
//        System.out.println(table.getElementById("Label2").text());
        siteAQI.setPublishTime(table.getElementById("Label2").text());

        Elements rows = table.select("tr");

        for (int i = 4; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");

            //表格切的不齊   找欄位都有16個的開始
            if (cols.size() >= 16) {

                // 创建 Pattern 对象
                Pattern r = Pattern.compile(site);
                // 现在创建 matcher 对象
                Matcher m = r.matcher(cols.get(0).text());

                if (m.find()) {
                    //站點名稱
                    siteAQI.setSiteName(cols.get(0).text());
                    // AQI 數值
                    siteAQI.setAqiValue(cols.get(2).text());
                    //AQI 等級, 因台中市與環保署描述不同 不良,不健康 無法比對 依EPA為主
                    siteAQI.setAqiLevel(aqiLevel(Integer.valueOf(cols.get(2).text())));
                    siteAQI.setMp3(aqiLevel(Integer.valueOf(cols.get(2).text())) + ".mp3");

                }

            }

        }

        return siteAQI;
    }

//    public static String getAQIData(String AQIURL, Logger logger) throws IOException {
//        String rawData = null;
//        URL url;
//        try {
//            url = new URL(AQIURL);
//            InputStreamReader isr = new InputStreamReader(url.openStream(), "UTF-8");
//            BufferedReader br = new BufferedReader(isr);
//            String line;
//            StringBuilder lines = new StringBuilder();
//            while ((line = br.readLine()) != null) {
//                lines.append(line);
//            }
//            rawData = lines.toString();
//
//        } catch (MalformedURLException e) {
//            logger.info(e.getMessage());
//        }
//        return rawData;
//    }
}
