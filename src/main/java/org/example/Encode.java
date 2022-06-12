package org.example;

import me.tongfei.progressbar.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Encode {
    private int nTiles;
    private int gop;
    private int quality;
    private ArrayList<EncodedImages> encodedListedImages;

    public Encode(int tiles, int gop, int quality){
        this.nTiles = tiles;
        this.gop = gop;
        this.quality = quality;
        this.encodedListedImages = new ArrayList<>();
    }

    public void encode() throws IOException {
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/ReproducedImages";
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

        int tesselWidhtSize = (int) (w/(Math.sqrt(nTiles)));
        int tesselHeightSize = (int) (h/(Math.sqrt(nTiles)));

        int heightPointer = 0;
        int widthPointer = 0;

        EncodedImages encodedImage;
        ProgressBar pb = new ProgressBar("Encoding files", 100); // name, initial max
        pb.start();
        for(File filesListed:files) {
            int[][][] tesselsList = new int[nTiles][tesselWidhtSize][tesselHeightSize];
            encodedImage = new EncodedImages(nTiles,tesselWidhtSize,tesselHeightSize);
            pb.step();
            try {
                img = ImageIO.read(new File(filesListed.getAbsolutePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0;i<nTiles;i++){
                for (int j = widthPointer; j<(tesselWidhtSize+widthPointer);j++){
                    for(int l = heightPointer; l<(tesselHeightSize+heightPointer);l++) {
                        tesselsList[i][j-widthPointer][l-heightPointer]=img.getRGB(j,l);
                    }
                }

                widthPointer += tesselWidhtSize;
                if (widthPointer== w){
                    widthPointer = 0;
                    heightPointer+=tesselHeightSize;
                }

            }
            widthPointer = 0;
            heightPointer = 0;
            encodedImage.setTesselsList(tesselsList);
            encodedListedImages.add(encodedImage);
        }

        pb.stop();

        compareTessels(h, w, tesselWidhtSize, tesselHeightSize, this.encodedListedImages);

        saveEncodedImages(w, h, this.encodedListedImages, tesselWidhtSize, tesselHeightSize);
    }

    public void compareTessels(int h, int w, int tesselWidhtSize, int tesselHeightSize, ArrayList<EncodedImages> encodedListedImages) throws IOException {

        FileWriter myWriter = new FileWriter("encode_information.txt");

        for(int index = 0; index < encodedListedImages.size(); index++){
            if(index % this.gop != 0){
                EncodedImages imageToCompare = encodedListedImages.get(index - (index % this.gop));
                EncodedImages imageComparable = encodedListedImages.get(index);

                int[][][] tesselsToCompare = imageToCompare.getTesselsList();
                int[][][] tesselsComparable = imageComparable.getTesselsList();

                long diff = 0;
                for (int i = 0; i < nTiles; i++){
                    for (int j = 0; j < tesselWidhtSize; j++){
                        for(int l = 0; l < tesselHeightSize; l++) {
                            int val1 = tesselsToCompare[i][j][l];
                            int val2 = tesselsComparable[i][j][l];
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
                    if(percentage>3){
                        imageComparable.setEliminatedTessels(i);
                        //(0,0,1,1)
                        myWriter.write(index + " "+ i +"\n");
                    }
                }
                imageComparable.averageTessel(tesselWidhtSize,tesselHeightSize);
            }
        }

        myWriter.close();

    }


    public void saveEncodedImages(int w, int h, ArrayList<EncodedImages> encodedListedImages, int tesselWidhtSize, int tesselHeightSize){
        Path currentRelativePath = Paths.get("");
        int heightPointer = 0;
        int widthPointer = 0;
        for (int x = 0; x < encodedListedImages.size(); x++){
            BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            int[][][] tesselsList = encodedListedImages.get(x).getTesselsList();
            for(int t = 0; t<nTiles;t++) {
                for (int j = widthPointer; j < (tesselWidhtSize + widthPointer);j++){
                    for(int l = heightPointer; l < (tesselHeightSize + heightPointer);l++) {
                        bufferedImage.setRGB(j, l, tesselsList[t][j - widthPointer][l - heightPointer] );
                    }
                }

                widthPointer += tesselWidhtSize;

                if (widthPointer>= w){
                    widthPointer = 0;
                    heightPointer+= tesselHeightSize;
                }
            }

            File file = new File(currentRelativePath.toAbsolutePath()+"/EncodedImages/Cubo"+String.format("%02d", x)+".jpg");
            try {
                ImageIO.write(bufferedImage, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            widthPointer = 0;
            heightPointer = 0;
        }

    }

}
