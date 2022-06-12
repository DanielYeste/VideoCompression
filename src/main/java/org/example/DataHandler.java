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

    public DataHandler(String inputFilePath, String outputFilePath, int fps, int binarization, boolean negative, int averaging, boolean encode, boolean decode, int nTiles, int gop, int quality){
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

}
