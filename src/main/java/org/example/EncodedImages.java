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
        int[][][] auxTexelsList = this.tesselsList.clone();
        ArrayList<Integer> pixelList = new ArrayList<>();
        for (int t = 0; t<eliminatedTessels.length;t++){
            if (eliminatedTessels[t] == 1){
                for (int i = 1; i<w;i++){
                    for(int j = 1; j<h;j++){
                        pixelList.add(auxTexelsList[t][i-1][j-1]);
                        pixelList.add(auxTexelsList[t][i-1][j]);
                        pixelList.add(auxTexelsList[t][i-1][j+1]);
                        pixelList.add(auxTexelsList[t][i][j-1]);
                        pixelList.add(auxTexelsList[t][i][j]);
                        pixelList.add(auxTexelsList[t][i][j+1]);
                        pixelList.add(auxTexelsList[t][i+1][j-1]);
                        pixelList.add(auxTexelsList[t][i+1][j]);
                        pixelList.add(auxTexelsList[t][i+1][j+1]);

                        Collections.sort(pixelList);
                        tesselsList[t][i][j] = (pixelList.get(pixelList.size()/2) + pixelList.get(pixelList.size()/2 - 1))/2;
                        pixelList.clear();
                    }
                }
            }
        }
    }
}
