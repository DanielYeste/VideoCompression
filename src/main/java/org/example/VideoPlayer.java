package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPlayer {
    DataHandler dh;
    Timer timer;
    private double fps;
    public VideoPlayer(DataHandler dataHandler){

        this.dh = dataHandler;
        this.fps = 1000/dh.getFps();
    }

    public void displayImage() throws IOException
    {
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/UnzippedImages";
        BufferedImage img;
        File f = new File(destDir);
        File[] files = f.listFiles();
        Arrays.sort(files);
        JFrame frame= new JFrame();
        ImageIcon icon;
        JLabel lbl=new JLabel();
        timer = new Timer();
        timer.schedule(new RemindTask(),1000,(int)this.fps);
        /*final boolean[] playerRunning = {true};
        while(playerRunning[0]) {
            for (File imageFiles : files) {
                img = ImageIO.read(new File(imageFiles.getAbsolutePath()));
                if (frame.isVisible()) {
                    icon = new ImageIcon(img);
                    lbl.setIcon(icon);
                    lbl.repaint();
                    if(!playerRunning[0]){
                        break;
                    }
                } else {
                    icon = new ImageIcon(img);
                    frame.setLayout(new FlowLayout());
                    frame.setSize(200, 300);
                    lbl.setIcon(icon);
                    frame.add(lbl);
                    frame.setVisible(true);
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                                playerRunning[0] = false;
                        }
                    });
                }

            }
        }
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));*/
    }

    class RemindTask extends TimerTask {
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/UnzippedImages";
        BufferedImage img;
        File f = new File(destDir);
        File[] files = f.listFiles();
        JFrame frame= new JFrame();
        ImageIcon icon;
        JLabel lbl=new JLabel();
        boolean started = false;
        int reproductionNumber= 0;
        int imgCounter = 0;
        public void run(){
            if(!started){
                Arrays.sort(files);
                try {
                    img = ImageIO.read(new File(files[imgCounter].getAbsolutePath()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                icon = new ImageIcon(img);
                frame.setLayout(new FlowLayout());
                frame.setSize(320, 260);
                lbl.setIcon(icon);
                frame.add(lbl);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                started = true;
            }else{
                try {
                    img = ImageIO.read(new File(files[imgCounter].getAbsolutePath()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                icon = new ImageIcon(img);
                lbl.setIcon(icon);
                lbl.repaint();
                imgCounter++;
                if(imgCounter>99){
                    imgCounter=0;
                    reproductionNumber++;
                }
            }

            if (reproductionNumber == 3){
                frame.setVisible(false);
                frame.dispose();
                timer.cancel();

            }
        }
    }


}
