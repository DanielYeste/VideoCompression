package org.example;


public class DataHandler {
    private String inputFilePath;
    private String outputFilePath;
    private int fps;
    private int binarization;
    private boolean negative;
    private int averaging;
    private boolean encode;
    private boolean decode;

    private int nTiles;
    private int gop;
    private int quality;


    private boolean batch;
    
    /**
     * Initializes all the paramaters given form the argument line that we need to execute
     * the whole program and holds it to share it between the different objects.
     * Known as data class.
     * @param inputFilePath
     * @param outputFilePath
     * @param fps
     * @param binarization
     * @param negative
     * @param averaging
     * @param encode
     * @param decode
     * @param nTiles
     * @param gop
     * @param quality
     * @param batch
     */

    public DataHandler(String inputFilePath, String outputFilePath, int fps, int binarization, boolean negative, int averaging, boolean encode, boolean decode, int nTiles, int gop, int quality, boolean batch){
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.fps = fps;
        this.binarization = binarization;
        this.negative = negative;
        this.averaging = averaging;
        this.encode = encode;
        this.decode = decode;
        this.nTiles = nTiles;
        this.gop = gop;
        this.quality = quality;
        this.batch = batch;
    }


    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public double getFps() {return fps;}

    public int getBinarization() {
        return binarization;
    }

    public boolean getNegative() {
        return negative;
    }
    public int getAveraging(){
        return averaging;
    }
    public boolean getEncode(){ return encode;}
    public boolean getDecode(){ return decode;}
    public int getGop(){return gop;}
    public int getnTiles() {return nTiles;}

    public int getQuality() {return quality;}

    public boolean isBatch() {
        return batch;
    }



}
