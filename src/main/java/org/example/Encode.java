package org.example;

import me.tongfei.progressbar.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Encode {
    private int nTiles;
    private int gop;
    private int quality;
    private int seekRange;
    private ArrayList<EncodedImages> encodedListedImages;
    private EncodingData encodingData;

   /**
     * Encode initializer that takes all the data needed for encoding our images.
     * Initializes our array list of images
     * @param tiles
     * @param gop
     * @param quality
     */
    public Encode(int tiles, int gop, int quality, int seekRange){
        this.nTiles = tiles;
        this.gop = gop;
        this.quality = quality;
        this.seekRange = seekRange;
        this.encodedListedImages = new ArrayList<>();
        this.encodingData = new EncodingData(nTiles, gop);
    }

    /**
     * Main encoding thread. Saves all image objects in an array, and calls
     * the subsequent methods for the tesselation and saving of the images.
     * @throws IOException
     */
    public void encode() throws IOException {
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/ReproducedImages";
        File f = new File(destDir);
        File[] files = f.listFiles();

        for(File filesListed:files) {
            EncodedImages encodedImage = new EncodedImages(nTiles, filesListed);
            encodedListedImages.add(encodedImage);
        }

        compareTessels();

        saveEncodedImages();
    }

    /**
     * Compare the tessels between the images as needed. Saves the information
     * in a file to decode the images if needed.
     * @throws IOException
     */
    public void compareTessels() throws IOException {

        ProgressBar pb = new ProgressBar("Encoding files", 100); // name, initial max
        pb.start();

        int tesselWidthSize = encodedListedImages.get(0).getTesselWidthSize();
        int tesselHeightSize = encodedListedImages.get(0).getTesselHeightSize();

        for(int index = 0; index < encodedListedImages.size(); index++){
            if(index % this.gop != 0){
                EncodedImages imageToCompare = encodedListedImages.get(index - 1);
                EncodedImages imageComparable = encodedListedImages.get(index);

                int[][][][] tesselsToCompare = imageToCompare.getTesselsList();
                int[][][][] tesselsComparable = imageComparable.getTesselsList();

                for(int k = 0; k < Math.sqrt(nTiles); k++){
                    for (int i = 0; i < Math.sqrt(nTiles); i++){
                        long diff = 0;
                        long minDiff = (long) Double.POSITIVE_INFINITY;
                        int prefi = 0;
                        int prefk = 0;
                        for(int kseek = k - seekRange; kseek < 1 + k + seekRange; kseek++){
                            for(int iseek = i - seekRange; iseek < 1 + i + seekRange; iseek++){
                                if(kseek >= 0 && kseek < Math.sqrt(nTiles) && iseek >= 0 && iseek < Math.sqrt(nTiles)){
                                    diff = 0;
                                    for (int j = 0; j < tesselWidthSize; j++){
                                        for(int l = 0; l < tesselHeightSize; l++) {
                                            int val1 = tesselsToCompare[kseek][iseek][j][l];
                                            int val2 = tesselsComparable[k][i][j][l];
                                            int r1 = (0x00ff0000 & val1) >> 16;
                                            int g1 = (0x0000ff00 & val1) >> 8;
                                            int b1 = (0x000000ff & val1);
                                            int r2 = (0x00ff0000 & val2) >> 16;
                                            int g2 = (0x0000ff00 & val2) >> 8;
                                            int b2 = (0x000000ff & val2);
                                            long data = Math.abs(r2 - r1) + Math.abs(g2 - g1) + Math.abs(b2 - b1);
                                            diff = diff + data;
                                        }
                                    }

                                    if(diff < minDiff){
                                        minDiff = diff;
                                        prefi = iseek;
                                        prefk = kseek;
                                    }
                                }
                            }
                        }

                        double avg = minDiff / (tesselWidthSize * tesselHeightSize * 3);
                        double percentage = (avg / 255) * 100;

                        if(percentage < quality){
                            imageComparable.setEliminatedTessels(k, i);
                            encodingData.addData(index, k, i, prefk, prefi);
                        }
                    }
                }

                imageComparable.averageTessel();
            }
            pb.step();
        }

        pb.stop();

    }

    /**
     * Saves encoded images in the specified path /EncodedImages
     * @throws IOException
     */
    public void saveEncodedImages() throws IOException {
        Path currentRelativePath = Paths.get("");

        Path path = Paths.get(currentRelativePath.toAbsolutePath()+"/EncodedImages");
        Files.createDirectories(path);

        for (int x = 0; x < encodedListedImages.size(); x++){

            BufferedImage bufferedImage = encodedListedImages.get(x).buildBufferedImage();

            File file = new File("");
            file = new File(currentRelativePath.toAbsolutePath()+"/EncodedImages/Cubo"+String.format("%02d", x)+".jpg");


            try {
                ImageIO.write(bufferedImage, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        FileOutputStream fos = new FileOutputStream(currentRelativePath.toAbsolutePath() + "/EncodedImages/encodingData");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(encodingData);
        oos.close();

    }

}
