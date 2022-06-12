package org.example;

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

    public ExecutionLogic(DataHandler dh){
        this.dh = dh;
        this.fileUnzipper = new FileUnzipper(dh.getInputFilePath());
        this.fileZipper = new FileZipper(dh.getOutputFilePath());
        this.imageFilters = new ImageFilters(dh.getBinarization(),dh.getNegative(),dh.getAveraging());
        this.encode = new Encode(dh.getnTiles(),dh.getGop(),dh.getQuality());
    }


    public void executionHandler(){
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
                System.out.println("Checking encoding/decoding selection...");
                if(this.dh.getEncode()){
                    this.encode.tesselateImages();
                }

            }else if (state == 3) {
                videoPlayer = new VideoPlayer(this.dh);
                try {
                    videoPlayer.displayImage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (state == 4){
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

            }else if (state == 5){
                filesComparation();
                long endTime = System.nanoTime();
                long timeElapsed = endTime - startTime;
                System.out.println("Total execution time in milliseconds: " + timeElapsed / 1000000);
            }else{
                executionOn = false;
            }
            state++;
        }
    }

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
        System.out.println("Compression ratio is: " + (unEncodedlength/encodedlength)/100.0f);
    }

}
