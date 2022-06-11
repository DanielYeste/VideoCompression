package org.example;

import com.beust.jcommander.Parameter;

public class CommandController {
    @Parameter(names = {"--help", "-h"},
            description = "Usage description of F1 project", help = true)
    public boolean help;

    @Parameter(names = {"--input","-i"},
            description = "Input file path", required = true)
    public String inputFilePath;

    @Parameter(names = {"--output","-o"},
            description = "Output file path")
    public String outputFilePath = "";

    @Parameter(names = {"--fps"},
            description = "Frame rate value for the video reproduction")
    public int fps;

    @Parameter(names = {"--binarization"},
            description = "Enables binarization filter with the specified value as threshold")
    public int binarization;

    @Parameter(names = {"--negative"},
            description = "Enables negative filter")
    public boolean negative;

    @Parameter(names = {"--encode","-e"},
            description = "Encode the files")
    public boolean encode;

    @Parameter(names = {"--decode","-d"},
            description = "Decode the files")
    public boolean decode;

    @Parameter(names = {"--averaging"},
            description = "Average for filters")
    public int averaging;

    @Parameter(names = {"--nTiles"},
            description = "Number of tessels that we use to divide the image")
    public int nTiles;

    @Parameter(names = {"--seekRange"},
            description = "Maximum displacement of tessels")
    public int seekRange;

    @Parameter(names = {"--GOP"},
            description = "Number of images between two reference frames")
    public int gop;

    @Parameter(names = {"--quality"},
            description = "Quality factor that determines coincident tessels")
    public int quality;

    @Parameter(names = {"--batch","-b"},
            description = "Does not open the video reproducer")
    public boolean batch;

    public void helpText() {
        System.out.println(
                "OPTIONS\n" +
                        "• –h,--help                       Usage Help\n"+
                        "• –i, --input <path to file.zip>  Input file. Mandatory \n" +
                        "• –o, --output <path to file>     Output file name \n" +
                        "• --fps <value>                   Frames per second value of the played video\n" +
                        "• --binarization <value>          Binarization filter, value is used as threshold\n" +
                        "• --negative                      Negative filter for the video"
        );
    }
}
