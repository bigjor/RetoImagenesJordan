/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.retoimagenes;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 *
 * @author BIG
 */
public class Location {
    private String latitude;
    private String longitude;

    public Location(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public void getLocation() {
        String lat = "38.9908444";
        String lon = "-0.5305654";
        try {
            URL url = new URL("https://nominatim.openstreetmap.org/reverse.xml?format=xml&lat="+lat+"&lon="+lon+"&zoom=18&addressdetails=1");
            URLConnection urlc = url.openConnection();
            urlc.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            
            BufferedInputStream buffer = new BufferedInputStream(urlc.getInputStream());
            ArrayList<Byte> list = new ArrayList<Byte>();
            int byteRead;
            while ((byteRead = buffer.read()) != -1) {
                list.add((byte) byteRead);
            }
            buffer.close();

            byte[] b = new byte[list.size()];
            for (int i = 0; i < list.size(); i++) {
                b[i] = list.get(i);
            }
            
            String xml = new String(b);
            Document doc = convertStringToDocument(xml);
            String datos = doc.getElementsByTagName("town").item(0).getTextContent();
            System.out.println(datos);
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
    }
    private Document convertStringToDocument(String xmlStr) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
        return null;
    }
}
