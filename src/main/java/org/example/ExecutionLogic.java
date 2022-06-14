package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ExecutionLogic {
    private DataHandler dh;
    private int state = 0;
    private FileUnzipper fileUnzipper;
    private VideoPlayer videoPlayer;
    private FileZipper fileZipper;
    private ImageFilters imageFilters;
    private Encode encode;
    private Decode decode;

    /**
     * ExecutionLogic initializer. Receives our DataHandler that contains the data that
     * we need to run the whole program.
     * @param dh
     * @throws IOException
     */
    public ExecutionLogic(DataHandler dh) throws IOException {
        this.dh = dh;
        this.fileUnzipper = new FileUnzipper(dh.getInputFilePath());
        this.fileZipper = new FileZipper(dh.getOutputFilePath());
        this.imageFilters = new ImageFilters(dh.getBinarization(),dh.getNegative(),dh.getAveraging());
        this.encode = new Encode(dh.getnTiles(),dh.getGop(),dh.getQuality(), dh.getSeekRange());
        this.decode = new Decode();
    }

    /**
     * Holds the main thread. Calls methods sequentially following a little
     * state machine similarity on execution.
     * @throws IOException
     */
    public void executionHandler() throws IOException, ClassNotFoundException {
        boolean executionOn = true;
        long startTime = System.nanoTime();
        while(executionOn) {
            if (state == 0) {
                System.out.println("Unzipping images wait...");
                this.fileUnzipper.unzip();
            } else if(state == 1){
                System.out.println("Unzipped files correctly");
                imageFilters.handleFilters();

            }else if (state == 2){
                System.out.println("Checking encoding selection...");
                if(this.dh.getEncode()){
                    System.out.println("Encoding files");
                    this.encode.encode();
                    filesComparation();
                }

            }else if (state == 3) {
                System.out.println("Checking decoding selection...");
                if(this.dh.getDecode()){
                    System.out.println("Decoding files");
                    this.decode.decode();
                    System.out.println("Calculating Peak-Noise-Ratio");
                    peakNoiseRatio();
                }
            }else if (state == 4) {
                if(!dh.isBatch()){
                    videoPlayer = new VideoPlayer((int) dh.getFps(), dh.getDecode());
                    try {
                        videoPlayer.displayImage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            } else if (state == 5){
                System.out.println(dh.getOutputFilePath());
                if(dh.getOutputFilePath().isEmpty()){
                    System.out.println("Non existent output path, can't zip the files");
                }else{
                    try {
                        System.out.println("Ready for zipping the files!");
                        System.out.println("Zipping images wait...");
                        fileZipper.zipper(dh.getOutputFilePath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }else{
                long endTime = System.nanoTime();
                long timeElapsed = endTime - startTime;
                System.out.println("Total execution time in milliseconds: " + timeElapsed / 1000000);
                executionOn = false;
            }
            state++;
        }
    }

    /**
     * Compares the size of files. The comparison its between original images and encoded images.
     */
    public void filesComparation(){
        long unEncodedlength = 0;
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/ReproducedImages";
        File f = new File(destDir);
        File[] files = f.listFiles();
        // listFiles() is used to list the
        // contents of the given folder
        int count = files.length;

        // loop for traversing the directory
        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                unEncodedlength += files[i].length();
            }
        }
        System.out.println("Initial files size is: " + unEncodedlength+"bytes");

        long encodedlength = 0;
        destDir = currentRelativePath.toAbsolutePath()+"/EncodedImages";
        f = new File(destDir);
        files = f.listFiles();
        count = files.length;

        // loop for traversing the directory
        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                encodedlength += files[i].length();
            }
        }
        System.out.println("Encoded files size is: " + encodedlength+"bytes");
        System.out.println("Compression ratio is: " + ((double) unEncodedlength/encodedlength));
    }

    /**
     * Calculates the peak noise ratio between the original images and the encoded images.
     * @throws IOException
     */
    public void peakNoiseRatio() throws IOException {
        PSNR psnr = new PSNR();
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/ReproducedImages";
        File fOriginal = new File(destDir);
        File[] filesOriginal = fOriginal.listFiles();
        destDir = currentRelativePath.toAbsolutePath()+"/EncodedImages";
        File fEncoded = new File(destDir);
        File[] filesEncoded = fEncoded.listFiles();
        BufferedImage originalImg;
        BufferedImage encodedImg;
        for(int i=0;i<filesOriginal.length;i++){
            originalImg = ImageIO.read(new File(filesOriginal[i].getAbsolutePath()));
            encodedImg = ImageIO.read(new File(filesEncoded[i].getAbsolutePath()));
           PSNR.calculate_PSNR(originalImg,encodedImg);


        }
    }

}
