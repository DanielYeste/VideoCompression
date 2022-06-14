package org.example;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import java.util.Scanner;

public class Main {
    /**
     * Main method that starts the execution. Gets the arguments and initializes the main thread
     * ExecutionLogic.
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        CommandController commandController = new CommandController();
        JCommander jComm;

        System.out.println("Initializing...");

        try {
            jComm = new JCommander(commandController, args);
            jComm.setProgramName("F1.jar");

            if (commandController.help) {
                commandController.helpText();
            }else{
                DataHandler dh = new DataHandler(commandController.inputFilePath,
                        commandController.outputFilePath, commandController.fps,
                        commandController.binarization,
                        commandController.negative,
                        commandController.averaging,
                        commandController.encode,
                        commandController.decode,
                        commandController.nTiles,
                        commandController.gop,
                        commandController.quality,
                        commandController.batch,
                        commandController.seekRange);
                ExecutionLogic logic = new ExecutionLogic(dh);
                System.out.println("Ready for the execution");
                logic.executionHandler();
            }
        } catch (ParameterException pex) {
            System.err.println(pex.getMessage());
            System.err.print("Try --help or -h for help. \n");
        }

    }

}
