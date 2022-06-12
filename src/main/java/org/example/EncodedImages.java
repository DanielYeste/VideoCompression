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

    public EncodedImages(int nTiles,int tesseWidthImage,int tesselHeightImage){
        this.tesselsList = new int[nTiles][tesseWidthImage][tesselHeightImage];
        this.eliminatedTessels = new int[nTiles];
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

    public void averageTessel(int w,int h){
        int imageMedian = 0;
        int pixelCounter = 0;
        for(int t = 0;t<eliminatedTessels.length;t++){
            for (int i = 1; i<w;i++){
                for(int j = 1; j<h;j++){
                    imageMedian+= tesselsList[t][i][j];
                    pixelCounter+=1;
                }
            }
        }
        imageMedian = imageMedian/pixelCounter;

        for (int t = 0; t<eliminatedTessels.length;t++){
            if (eliminatedTessels[t] == 1){
                for (int i = 1; i<w;i++){
                    for(int j = 1; j<h;j++){
                        tesselsList[t][i][j] = imageMedian;
                    }
                }
            }
        }

    }
}
