
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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
public class MetadatosImagen {
    private String nombre;
    private String path;
    private String lat;
    private String lon;
    private Date fecha;
    private int width;
    private int height;

    public MetadatosImagen() {
        this.nombre = "";
        this.lat = "";
        this.lon = "";
        this.fecha = new Date();
        this.width = 0;
        this.height = 0;
    }

    public MetadatosImagen(String lat, String lon, Date fecha, int width, int height) {
        this.lat = lat;
        this.lon = lon;
        this.fecha = fecha;
        this.width = width;
        this.height = height;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getCoords() {
        if (this.lat == "" || this.lat == null && this.lon == "" || this.lon == null)
            return "Ubicación Desconocida";
        return toDecimal(this.lat) + ", " + toDecimal(this.lon);
    }
    
    public String getInfo() {
        return getNombre() + " - " + getCoords();
    }
    
    public String getFechaMes() {
        String mes = this.fecha.getMonth() < 10 ? "0"+this.fecha.getMonth() : ""+this.fecha.getMonth();
        String anyo = this.fecha.toString().split(" ")[5];
        
        return anyo+""+mes;
    }
    
    @Override
    public String toString() {
        return "MetadatosImagen{" + "nombre=" + nombre + ", lat=" + toDecimal(lat) + ", lon=" + toDecimal(lon) + ", fecha=" + fecha + ", width=" + width + ", height=" + height + ", loc=" + getPoblacion() + "}";
    }

    public String toDecimal(String dato) {
        if (dato == "" || dato == null) {
            
            return "-0.0";
        } else {
            String[] latlng = dato.replaceAll("[^0-9.\\s-]", "").split(" ");
            String[] latlng2 = dato.replaceAll(",", " ").split(" ");

            double degrees = Double.parseDouble(latlng[0]);
            double minutes = Double.parseDouble(latlng[1]);
            double seconds = Double.parseDouble(latlng2[2]+"."+(latlng2[3]).replaceAll("\"", ""));
            double decimal = ((minutes * 60)+seconds) / (60*60);
            String answer = "" + (degrees + decimal);;
            if (degrees <= -1)
                answer = ""+(degrees-1 + decimal);
            if (degrees == 0)
                answer = "-"+(degrees + decimal);

            return answer;
        }
        
    }

    public String getPoblacion() {
        if (this.lat != "" && this.lon != "") {
            String lat = toDecimal(this.lat);
            String lon = toDecimal(this.lon);

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

                String xml = new String(b, "UTF-8");
                Document doc = convertStringToDocument(xml);
                String datos = doc.getElementsByTagName("town").item(0).getTextContent();
                //System.out.println(datos);
                return datos;
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return "Ubicación desconocida";
    }
    private static Document convertStringToDocument(String xmlStr) {
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
