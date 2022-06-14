package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class EncodedImages {
    private int[][][] tesselsList;
    private int[] eliminatedTessels;
    private int tesselWidthSize;
    private int tesselHeightSize;
    private int nTiles;

    /**
     * Initializes a tesselated image.
     * @param nTiles Quantitat de teseles en les que es vol dividir la imatge.
     * @param file Arxiu amb la imatge que volem teselar.
     */
    public EncodedImages(int nTiles, File file){
        this.eliminatedTessels = new int[nTiles];
        this.nTiles = nTiles;

        // Teselem la imatge.
        tesselateImage(file);
    }

    /**
     * Tesselate an image given a file.
     * @param file File with the image to tessel.
     */
    private void tesselateImage(File file){
        int widthPointer = 0;
        int heightPointer = 0;

        BufferedImage img;

        try {
            img = ImageIO.read(new File(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Obtenim l'amplada i l'alçada de les teseles.
        this.tesselWidthSize = (int) (img.getWidth() / (Math.sqrt(nTiles)));
        this.tesselHeightSize = (int) (img.getHeight() / (Math.sqrt(nTiles)));

        this.tesselsList = new int[nTiles][tesselWidthSize][tesselHeightSize];

        // Guardem a cada tesela una matriu width*height amb els píxels corresponents.
        for (int i = 0; i < nTiles; i++){
            for (int j = widthPointer; j < (tesselWidthSize + widthPointer); j++){
                for(int l = heightPointer; l < (tesselHeightSize + heightPointer); l++) {
                    tesselsList[i][j - widthPointer][l - heightPointer] = img.getRGB(j, l);
                }
            }

            widthPointer += tesselWidthSize;
            if (widthPointer >= img.getWidth()){
                widthPointer = 0;
                heightPointer += tesselHeightSize;
            }
        }
    }

    /**
     * Returns the list of tessels.
     * @return tessels list.
     */
    public int[][][] getTesselsList() {
        return tesselsList;
    }

    /**
     * Guarda el número de tesela que s'ha eliminat.
     * @param eliminatedTessels Número de tesela que s'elimina
     */
    public void setEliminatedTessels(int eliminatedTessels) {
        this.eliminatedTessels[eliminatedTessels] = 1;
    }

    /**
     * Return tessels width.
     * @return Amplada de les teseles.
     */
    public int getTesselWidthSize() {
        return tesselWidthSize;
    }

    /**
     * Return tessels height.
     * @return Alçada de les teseles.
     */
    public int getTesselHeightSize() {
        return tesselHeightSize;
    }

    /**
     * Calculate average color of every tessel and paints it.
     */
    public void averageTessel(){
        int[] tesselsAvg = new int[eliminatedTessels.length];

        // Obté el color mig de cada tesela i el guarda en una llista.
        for(int t = 0; t < eliminatedTessels.length - 1; t++){
            int pixelCounter = 0;
            int redAvg = 0;
            int blueAvg = 0;
            int greenAvg = 0;

            for (int i = 0; i < tesselWidthSize; i++){
                for(int j = 0; j < tesselHeightSize; j++){
                    redAvg += (0x00ff0000 & tesselsList[t][i][j]) >> 16;
                    greenAvg += (0x0000ff00 & tesselsList[t][i][j]) >> 8;
                    blueAvg += (0x000000ff & tesselsList[t][i][j]);
                    pixelCounter += 1;
                }
            }
            redAvg = redAvg / pixelCounter;
            greenAvg = greenAvg / pixelCounter;
            blueAvg = blueAvg / pixelCounter;

            int p = (redAvg << 16) | (greenAvg << 8) | blueAvg;
            tesselsAvg[t] = Integer.valueOf(p);
        }

        // Pinta cada tesela eliminada del seu color mig.
        for (int t = 0; t < eliminatedTessels.length; t++){
            if (eliminatedTessels[t] == 1){
                for (int i = 0; i < tesselWidthSize; i++){
                    for(int j = 0; j < tesselHeightSize; j++){
                        tesselsList[t][i][j] = tesselsAvg[t];
                    }
                }
            }
        }
    }

    /**
     * Builds BI from the tessels list.
     * @return BI from the image.
     */
    public BufferedImage buildBufferedImage(){
        int w = this.tesselWidthSize * ( (int) Math.sqrt(nTiles));
        int h = this.tesselHeightSize * ( (int) Math.sqrt(nTiles));

        // Crea una BI buida.
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int heightPointer = 0;
        int widthPointer = 0;

        // Itera a través de la llista de teseles assignant cada píxel corresponent a la BI.
        for(int t = 0; t < nTiles;t++) {
            for (int j = widthPointer; j < (tesselWidthSize + widthPointer); j++){
                for(int l = heightPointer; l < (tesselHeightSize + heightPointer); l++) {
                    bufferedImage.setRGB(j, l, tesselsList[t][j - widthPointer][l - heightPointer] );
                }
            }

            widthPointer += tesselWidthSize;

            if (widthPointer>= w){
                widthPointer = 0;
                heightPointer+= tesselHeightSize;
            }
        }

        return bufferedImage;
    }
}
