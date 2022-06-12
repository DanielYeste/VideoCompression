package org.example;

import me.tongfei.progressbar.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Decode {
    private int nTiles;
    private int gop;
    private ArrayList<EncodedImages> encodedListedImages;

    public Decode(int tiles, int gop){
        this.nTiles = tiles;
        this.gop = gop;
        this.encodedListedImages = new ArrayList<>();
    }

    public void decode() throws FileNotFoundException {
        Path currentRelativePath = Paths.get("");

        String destDir = currentRelativePath.toAbsolutePath()+"/EncodedImages";

        File f = new File(destDir);

        File[] files = f.listFiles();

        for(File image: files) {
            EncodedImages encodedImage = new EncodedImages(nTiles, image);
            encodedListedImages.add(encodedImage);
        }

        rebuildImages();

        saveDecodedImages();
    }

    public void rebuildImages() throws FileNotFoundException {
        ProgressBar pb = new ProgressBar("Decoding files", 100); // name, initial max
        pb.start();

        File file = new File("encode_information.txt");
        Scanner sc = new Scanner(file);

        while(sc.hasNextLine()){
            String line = sc.nextLine();
            int numImage = Integer.valueOf(line.split(" ")[0]);
            int numTessel = Integer.valueOf(line.split(" ")[1]);

            for(int i = 0; i < encodedListedImages.get(numImage).getTesselsList()[numTessel].length; i++){
                for(int j = 0; j < encodedListedImages.get(numImage).getTesselsList()[numTessel][i].length; j++){
                    encodedListedImages.get(numImage).getTesselsList()[numTessel][i][j] = encodedListedImages.get(numImage - (numImage % gop)).getTesselsList()[numTessel][i][j];
                }
            }

            pb.step();
        }

        pb.stop();
    }

    public void saveDecodedImages(){
        Path currentRelativePath = Paths.get("");

        for (int x = 0; x < encodedListedImages.size(); x++){

            BufferedImage bufferedImage = encodedListedImages.get(x).buildBufferedImage();
            File file = new File("");
            if(x/10 < 1){
                file = new File(currentRelativePath.toAbsolutePath()+"/DecodedImages/MyImage0"+x+".jpg");
            }else{
                file = new File(currentRelativePath.toAbsolutePath()+"/DecodedImages/MyImage"+x+".jpg");
            }

            try {
                ImageIO.write(bufferedImage, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
