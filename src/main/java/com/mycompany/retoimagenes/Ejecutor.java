/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.retoimagenes;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListModel;

/**
 *
 * @author BIG
 */
public class Ejecutor {
    public static final int TERMINAL = 0;
    public static final int GRAFIC = 1;
    
    private ArrayList<MetadatosImagen> metadatos;
    private ArrayList<File> fotos;
    private String rutaInicial;
    private String rutaFinal;
    private MetadatosImagen mi;
    
    private JList model;
    private int mode;

    public Ejecutor(int mode) {
        this.model = null;
        this.mode = mode;
        metadatos = new ArrayList<MetadatosImagen>();
        fotos = new ArrayList<File>();
    }

    public void setRutaInicial(String rutaInicial) {
        this.rutaInicial = rutaInicial;
    }

    public void setRutaFinal(String rutaFinal) {
        this.rutaFinal = rutaFinal;
    }

    public String getRutaInicial() {
        return rutaInicial;
    }

    public String getRutaFinal() {
        return rutaFinal;
    }
    
    public void setModel(JList l) {
        this.model = l;
    }
    
    public void ejecutar() {
        if (!(new File(rutaFinal).exists())) {
            new File(rutaFinal).mkdir();
        }
        
        cargarFotos(new File(rutaInicial));
        
        for (int i = 0; i < fotos.size(); i++) {
            try {
                Metadata metadata = ImageMetadataReader.readMetadata(fotos.get(i));
                extractMetadata(metadata, fotos.get(i).getAbsolutePath());
            } catch (ImageProcessingException e) {
                System.err.println(e);
            } catch (IOException e) {
                System.err.println(e);
            }
        
        }
        
        DefaultListModel listModel = new DefaultListModel();
        
        for (int i = 0; i < metadatos.size(); i++) {
            MetadatosImagen m = metadatos.get(i);
            
            
            System.out.println(m.getInfo());
            //System.out.println(m.getWidth() + " " + m.getHeight());
            if (model != null) {
                listModel.addElement(m.getInfo());
                model.setModel(listModel);
            }
            
            String cadenaCarpetaFecha = rutaFinal+"\\"+m.getFechaMes();
            File carpetaFecha = new File(cadenaCarpetaFecha);
            if (!carpetaFecha.exists()) {
                carpetaFecha.mkdir();
            }
            String cadenaPoblacion = cadenaCarpetaFecha+"\\"+m.getPoblacion();
            File carpetaPoblacion = new File(cadenaPoblacion);
            if (!carpetaPoblacion.exists()) {
                carpetaPoblacion.mkdir();
            }
            File origen = new File(m.getPath());
            
            String destino = carpetaPoblacion+"\\"+m.getNombre();
            try {
                FileInputStream in = new FileInputStream(origen);
                FileOutputStream out = new FileOutputStream(destino);

                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (IOException ioe){
                // ERROR AL COPIAR LA IMAGEN
                // System.err.println("Error: al copiar la imagen");
            }

       
            try {
                new MarcaAgua().setMarca(destino);
            } catch (IOException ex) {
                // ERROR EN AL ESTABLECER LA MARCA DE AGUA (EN CASO DE NO PODER ESCRIBIRLA)
                // System.err.println("ERROR AL ESTABLECER MARCA DE AGUA");
            } catch (Exception ex) {
                // ERROR EN AL ESTABLECER LA MARCA DE AGUA (EN CASO DE NO PODER ESCRIBIRLA)
                // System.err.println("ERROR AL ESTABLECER MARCA DE AGUA");
            }
            
            try {
                new Reescalar(destino);
            } catch (Exception e) {
                // ERROR AL REALIZAR LA REESCALA (EN CASO DE NO PODER REESCALAR UNA IMAGEN)
                // System.out.println("ERROR: No se a podido reescalar la imagen con destino " + destino);
            }
            

        }
    }

    private void extractMetadata(Metadata metadata, String path) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:SS");
        mi = new MetadatosImagen();
        mi.setPath(path);
        //System.out.println("-----------------------------------------------------------------------------------------");
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
               
                //System.out.println(tag);
                switch (tag.getTagName()) {
                    case "File Name":
                        mi.setNombre(tag.getDescription());
                        break;
                    case "GPS Latitude":
                        mi.setLat(tag.getDescription());
                        break;
                    case "GPS Longitude":
                        mi.setLon(tag.getDescription());
                        break;
                    case "Height":
                    case "Image Height":
                        mi.setHeight(Integer.parseInt(tag.getDescription().split(" ")[0]));
                        break;
                    case "Width":
                    case "Image Width":
                        mi.setWidth(Integer.parseInt(tag.getDescription().split(" ")[0]));
                        break;
                    case "Date/Time Original":
                        try {
                            Date d = sdf.parse(tag.getDescription());
                            mi.setFecha(d);
                        } catch (ParseException ex) {
                            System.err.println(ex);
                        }
                        break;
                    case "File Modified Date":
                        //mi.setFecha(new Date(tag.getDescription()));
                        // dom oct 06 08:53:52 +02:00 2019
                        String[] datos = tag.getDescription().split(" ");
                        int mes = 1;
                        switch (datos[1]) {
                            case "gen":
                                mes = 1;
                                break;
                            case "feb":
                                mes = 2;
                                break;
                            case "mar":
                                mes = 3;
                                break;
                            case "apr":
                                mes = 4;
                                break;
                            case "may":
                                mes = 5;
                                break;
                            case "jun":
                                mes = 6;
                                break;
                            case "jul":
                                mes = 7;
                                break;
                            case "aug":
                                mes = 8;
                                break;
                            case "sep":
                                mes = 9;
                                break;
                            case "oct":
                                mes = 10;
                                break;
                            case "nov":
                                mes = 11;
                                break;
                            case "dec":
                                mes = 12;
                                break;
                        }
                        //System.out.println(Integer.parseInt(datos[5]));
                        Date d2 = new Date(Integer.parseInt(datos[5])-1900, mes, Integer.parseInt(datos[2]), Integer.parseInt((datos[3]).split(":")[0]), Integer.parseInt((datos[3]).split(":")[1]), Integer.parseInt((datos[3]).split(":")[2]));
                        mi.setFecha(d2);
                        break;
                    
                }
                
            }
            for (String error : directory.getErrors()) {
                System.err.println("ERROR: " + error);
            }
        }
        metadatos.add(mi);
    }
    
    public void cargarFotos(File dir) {
        File listFile[] = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    cargarFotos(listFile[i]);
                } else {
                    fotos.add(listFile[i]);
                }
            }
        }
    }
    
    
    
}
