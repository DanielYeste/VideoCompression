package org.example;

import java.io.IOException;

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


    public void executionHandler() throws IOException {
        boolean executionOn = true;
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
                    this.encode.encode();
                }

            }else if (state == 3) {
                System.out.println("Ready for activate the video player");
                videoPlayer = new VideoPlayer(this.dh);
                try {
                    videoPlayer.displayImage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (state == 4){
                System.out.println("You're done with the video");
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
                executionOn = false;
            }
            state++;
        }
    }

}
