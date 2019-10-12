/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main {
    static  Ejecutor e;
    public static void main(String[] args) {        
        boolean paramCorrectos = true;
        if (args.length == 0) {
            Ventana v = new Ventana();
            v.setVisible(true);
            v.setResizable(false);
        } else {
            if (args.length == 2) {
                e = new Ejecutor(Ejecutor.TERMINAL);
                e.setRutaInicial(args[0]);
                e.setRutaFinal(args[1]);
//                e.setRutaInicial("C:\\Users\\BIG\\Desktop\\fotos");
//                e.setRutaFinal("C:\\Users\\BIG\\Desktop\\final");
                if (!(new File(e.getRutaInicial())).exists())
                    paramCorrectos = false;
                
                if (!(new File(e.getRutaFinal())).exists())
                    paramCorrectos = false;
                
                if (paramCorrectos) 
                    e.ejecutar();
                
            } else {
                System.err.println("Los argumentos no son correctos");
            }
        }
        
        


    }
    
    
    

}
