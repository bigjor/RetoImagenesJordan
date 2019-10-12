
package com.mycompany.retoimagenes;

import static com.mycompany.retoimagenes.Reescalar.MAX_HEIGHT;
import static com.mycompany.retoimagenes.Reescalar.MAX_WIDTH;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MarcaAgua {
    static final int PADDING = 45;

    public void setMarca(String imagen) throws IOException {
        
        
        BufferedImage biUno= loadImage(imagen); 
        BufferedImage biDos = ImageIO.read(new File("MarcaAgua.png"));
        
        BufferedImage biResultado = new BufferedImage(biUno.getWidth(), biUno.getHeight(), BufferedImage.TYPE_INT_RGB); //suponiendo tamaño de imagen  y ARGB que soporta trasparencia

        Graphics g = biResultado.getGraphics();
        g.drawImage(biUno, 0, 0, null); //se rellena con imagen uno
        g.drawImage(biDos, (biUno.getWidth()-542)-PADDING, (biUno.getHeight()-519)-PADDING, null); //se rellena con imagen dos con  un supuesto margen de 5 (habria que ver cual es el real)

       
        //System.out.println(imagen.getName().split(".")[0]);
        //System.out.println(file_carpetapob.getAbsolutePath()+"\\"+[0]+".png");
        saveImage(biResultado, imagen);
        //ImageIO.write(biResultado, "PNG", new File(imagen)));
    }
    
     
    /*
    Este método se utiliza para cargar la imagen de disco
    */
    public BufferedImage loadImage(String pathName) {
        BufferedImage bimage = null;
        try {
            bimage = ImageIO.read(new File(pathName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bimage;
    }
 
    /*
    Este método se utiliza para almacenar la imagen en disco
    */
    public void saveImage(BufferedImage bufferedImage, String pathName) {
        try {
            String format = (pathName.endsWith(".png")) ? "png" : "jpg";
            File file =new File(pathName);
            file.getParentFile().mkdirs();
            ImageIO.write(bufferedImage, format, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
     
}

