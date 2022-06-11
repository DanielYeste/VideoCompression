package org.example;

import me.tongfei.progressbar.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Encode {
    private int nTiles;
    private int gop;
    private int quality;
    private ArrayList<EncodedImages> encodedListedImages;
    public Encode(int tiles,int gop,int quality){
        this.nTiles = tiles;
        this.gop = gop;
        this.quality = quality;
        this.encodedListedImages = new ArrayList<>();
    }

    public void tesselateImages(){
        ProgressBar pb = new ProgressBar("Encoding files", 100); // name, initial max
        pb.start();
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/UnzippedImages";
        File f = new File(destDir);
        File[] files = f.listFiles();
        Arrays.sort(files);
        BufferedImage img;

        try {
            img = ImageIO.read(new File(files[0].getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int h = img.getHeight();
        int w = img.getWidth();

        int tesselWidhtSize = w/(nTiles/2);
        int tesselHeightSize = h/(nTiles/2);


        int heightPointer = 0;
        int widthPointer = 0;
        int auxHeightPointer = 0;
        int auxWidthPointer = 0;
        int hP = 0;
        EncodedImages encodedImage;
        for(File filesListed:files) {
            int[][][] tesselsList = new int[nTiles][tesselWidhtSize][tesselHeightSize];
            encodedImage = new EncodedImages(nTiles,tesselWidhtSize,tesselHeightSize);
            try {
                img = ImageIO.read(new File(filesListed.getAbsolutePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0;i<nTiles;i++){
                pb.step();
                for (int j = widthPointer; j<(tesselWidhtSize+widthPointer);j++){
                    for(int l = heightPointer; l<(tesselHeightSize+heightPointer);l++) {
                        tesselsList[i][j-widthPointer][l-heightPointer]=img.getRGB(j,l);
                        hP++;
                    }
                    if(j == 0){
                        auxHeightPointer = hP;
                    }
                    auxWidthPointer++;
                }

                widthPointer += auxWidthPointer;

                if (widthPointer>= w){
                    widthPointer = 0;
                    auxWidthPointer = 0;
                    heightPointer+= auxHeightPointer;
                }
                auxHeightPointer = 0;
                hP = 0;

            }
            encodedImage.setTesselsList(tesselsList);
            encodedListedImages.add(encodedImage);
        }


        pb.stop();
        compareTessels(h,w,tesselWidhtSize,tesselHeightSize,encodedListedImages);
        saveEncodedImages(w,h,encodedListedImages,tesselWidhtSize,tesselHeightSize);

    }

    public void compareTessels(int h, int w, int tesselWidhtSize, int tesselHeightSize, ArrayList<EncodedImages> encodedListedImages){
        EncodedImages image1 = encodedListedImages.get(0);
        EncodedImages image2 = encodedListedImages.get(70);
        int[][][] tesselsList1 = image1.getTesselsList();
        int[][][] tesselsList2 = image2.getTesselsList();
        long diff = 0;
        for (int i = 0;i<nTiles;i++){
            for (int j = 0; j<tesselWidhtSize;j++){
                for(int l = 0; l<tesselHeightSize;l++) {
                    int val1 = tesselsList1[i][j][l];
                    int val2 = tesselsList2[i][j][l];
                    int r1 = (0x00ff0000 & val1) >> 16;
                    int g1 = (0x0000ff00 & val1) >> 8;
                    int b1 = (0x000000ff & val1);
                    int r2 = (0x00ff0000 & val2) >> 16;
                    int g2 = (0x0000ff00 & val2) >> 8;
                    int b2 = (0x000000ff & val2);
                    long data = Math.abs(r1-r2)+Math.abs(g1-g2)+ Math.abs(b1-b2);
                    diff = diff+data;
                }
            }
            double avg = diff/(w*h*3);
            double percentage = (avg/255)*100;
           //System.out.println("Difference: "+percentage);
            if(percentage>2){
                image2.setEliminatedTessels(i);
                try {
                    FileWriter myWriter = new FileWriter("encode_information.txt");
                    myWriter.write("70 "+ i +"\n");
                    myWriter.close();
                    System.out.println("Successfully wrote to the file.");
                } catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }
            }
        }

    }


    public void saveEncodedImages(int w, int h, ArrayList<EncodedImages> encodedListedImages, int tesselWidhtSize, int tesselHeightSize){

    }

}




