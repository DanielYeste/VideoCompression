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

        for(File filesListed:files) {
            EncodedImages encodedImage = new EncodedImages(nTiles, filesListed);
            encodedListedImages.add(encodedImage);
        }

        compareTessels();

        saveEncodedImages();
    }

    public void compareTessels() throws IOException {

        ProgressBar pb = new ProgressBar("Encoding files", 100); // name, initial max
        pb.start();

        FileWriter myWriter = new FileWriter("encode_information.txt");

        int tesselWidthSize = encodedListedImages.get(0).getTesselWidthSize();
        int tesselHeightSize = encodedListedImages.get(0).getTesselHeightSize();

        for(int index = 0; index < encodedListedImages.size(); index++){
            if(index % this.gop != 0){
                EncodedImages imageToCompare = encodedListedImages.get(index - (index % this.gop));
                EncodedImages imageComparable = encodedListedImages.get(index);

                int[][][] tesselsToCompare = imageToCompare.getTesselsList();
                int[][][] tesselsComparable = imageComparable.getTesselsList();

                for (int i = 0; i < nTiles; i++){
                    long diff = 0;
                    for (int j = 0; j < tesselWidthSize; j++){
                        for(int l = 0; l < tesselHeightSize; l++) {
                            int val1 = tesselsToCompare[i][j][l];
                            int val2 = tesselsComparable[i][j][l];
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

                    double avg = diff / (tesselWidthSize * tesselHeightSize * 3);
                    double percentage = (avg / 255) * 100;

                    if(percentage < quality){
                        imageComparable.setEliminatedTessels(i);
                        myWriter.write(index + " "+ i +"\n");
                    }
                }
                imageComparable.averageTessel();
            }
            pb.step();
        }

        myWriter.close();
        pb.stop();

    }


    public void saveEncodedImages(){
        Path currentRelativePath = Paths.get("");

        for (int x = 0; x < encodedListedImages.size(); x++){

            BufferedImage bufferedImage = encodedListedImages.get(x).buildBufferedImage();

            File file = new File("");
            if(x/10 < 1){
                file = new File(currentRelativePath.toAbsolutePath()+"/EncodedImages/MyImage0"+x+".jpg");
            }else{
                file = new File(currentRelativePath.toAbsolutePath()+"/EncodedImages/MyImage"+x+".jpg");
            }

            try {
                ImageIO.write(bufferedImage, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
