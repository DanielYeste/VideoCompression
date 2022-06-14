package org.example;

import me.tongfei.progressbar.ProgressBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class Decode {
    private int nTiles;
    private int gop;
    private ArrayList<EncodedImages> encodedListedImages;
    private EncodingData encodingData;

    /**
     * Intializes the decoder.
     */
    public Decode(){
        this.encodedListedImages = new ArrayList<>();
    }

    /**
     * Main process of the decoder.
     * @throws IOException
     */
    public void decode() throws IOException, ClassNotFoundException {
        Path currentRelativePath = Paths.get("");

        String destDir = currentRelativePath.toAbsolutePath()+"/EncodedImages";

        File f = new File(destDir);

        File[] files = f.listFiles();

        // Opens the file where we saved the information about tessel substitution.
        FileInputStream fin = new FileInputStream(destDir + "/encodingData");
        ObjectInputStream ois = new ObjectInputStream(fin);
        this.encodingData= (EncodingData) ois.readObject();
        ois.close();


        // Reads the first line to get the number of tessels and GOP for the decoding
        this.nTiles = encodingData.getnTiles();
        this.gop = encodingData.getGop();

        // From the coded images, we create a list with the tesselated images.
        for(File image: files) {
            if(image.getName().contains(".")){
                EncodedImages encodedImage = new EncodedImages(nTiles, image);
                encodedListedImages.add(encodedImage);
            }
        }

        // Rebuild the images.
        rebuildImages();

        // Save the rebuilded images in a file.
        saveDecodedImages();
    }

    /**
     * Rebuilds the images from the encoded tesselated images and our data file with the information.
     * @throws FileNotFoundException
     */
    public void rebuildImages() throws IOException, ClassNotFoundException {
        ProgressBar pb = new ProgressBar("Decoding files", 100); // name, initial max
        pb.start();

        // Iterem per la llista de dades amb la info de cada tesela.
        for(int[] data: encodingData.getData()){
            int numImage = data[0];
            int row = data[1];
            int col = data[2];
            int prevRow = data[3];
            int prevCol = data[4];

            // Agafem la tesela corresponent al número de tesela que hem de recuperar de la imatge model
            // i la posem a la imatge a reconstruir.
            for(int i = 0; i < encodedListedImages.get(numImage).getTesselsList()[row][col].length; i++){
                for(int j = 0; j < encodedListedImages.get(numImage).getTesselsList()[row][col][i].length; j++){
                    encodedListedImages.get(numImage).getTesselsList()[row][col][i][j] = encodedListedImages.get(numImage - 1).getTesselsList()[prevRow][prevCol][i][j];
                }
            }

            pb.step();
        }

        pb.stop();
    }

    /**
     * Take the tesselated images, convert them to jpg and saves them in a file.
     * @throws IOException
     */
    public void saveDecodedImages() throws IOException {
        Path currentRelativePath = Paths.get("");

        Path path = Paths.get(currentRelativePath.toAbsolutePath()+"/DecodedImages");
        Files.createDirectories(path);

        // Iterem sobre la llista d'imatges teselades
        for (int x = 0; x < encodedListedImages.size(); x++){

            // Obtenim un BI a partir d'una imatge teselada.
            BufferedImage bufferedImage = encodedListedImages.get(x).buildBufferedImage();
            File file = new File("");
            file = new File(currentRelativePath.toAbsolutePath()+"/DecodedImages/Cubo"+String.format("%02d", x)+".jpg");


            try {
                ImageIO.write(bufferedImage, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
