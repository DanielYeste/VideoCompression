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

    public EncodedImages(int nTiles, File file){
        this.eliminatedTessels = new int[nTiles];
        this.nTiles = nTiles;
        tesselateImage(file);
    }

    private void tesselateImage(File file){
        int widthPointer = 0;
        int heightPointer = 0;

        BufferedImage img;

        try {
            img = ImageIO.read(new File(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.tesselWidthSize = (int) (img.getWidth() / (Math.sqrt(nTiles)));
        this.tesselHeightSize = (int) (img.getHeight() / (Math.sqrt(nTiles)));

        this.tesselsList = new int[nTiles][tesselWidthSize][tesselHeightSize];

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

    public int[][][] getTesselsList() {
        return tesselsList;
    }

    public void setTesselsList(int[][][] tesselsList) {
        this.tesselsList = tesselsList;
    }

    public int[] getEliminatedTessels() {
        return eliminatedTessels;
    }

    public void setEliminatedTessels(int eliminatedTessels) {
        this.eliminatedTessels[eliminatedTessels] = 1;
    }

    public void setEliminatedTessels(int[] eliminatedTessels) {
        this.eliminatedTessels = eliminatedTessels;
    }

    public int getTesselWidthSize() {
        return tesselWidthSize;
    }

    public void setTesselWidthSize(int tesselWidthSize) {
        this.tesselWidthSize = tesselWidthSize;
    }

    public int getTesselHeightSize() {
        return tesselHeightSize;
    }

    public void setTesselHeightSize(int tesselHeightSize) {
        this.tesselHeightSize = tesselHeightSize;
    }

    public int getnTiles() {
        return nTiles;
    }

    public void setnTiles(int nTiles) {
        this.nTiles = nTiles;
    }

    public int getImageWidth(){
        return this.tesselWidthSize * ( (int) Math.sqrt(nTiles));
    }

    public int getImageHeight(){
        return this.tesselHeightSize * ( (int) Math.sqrt(nTiles));
    }

    public void averageTessel(){
        int[] tesselsAvg = new int[eliminatedTessels.length];

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
            String imageAvg = String.valueOf(redAvg) + String.valueOf(greenAvg) + String.valueOf(blueAvg);
            tesselsAvg[t] = Integer.valueOf(imageAvg);
        }

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

    public BufferedImage buildBufferedImage(){
        int w = this.tesselWidthSize * ( (int) Math.sqrt(nTiles));
        int h = this.tesselHeightSize * ( (int) Math.sqrt(nTiles));
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int heightPointer = 0;
        int widthPointer = 0;
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
