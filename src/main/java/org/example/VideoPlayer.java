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
    Timer timer2;
    private double fps;
    boolean decoding;
    
    /**
     * Initializes our video player with the datahandler to get all the information
     * that he needs.
     * @param dataHandler
     */
    public VideoPlayer(int fps, boolean decoding){
        this.fps = 1000/fps;
        this.decoding = decoding;
    }
    
    /**
     * Controls the image display. Initializes one or two threads if required, both
     * initializes a JFrame that reproduces our video.
     * @throws IOException
     */
    public void displayImage() throws IOException
    {
        timer = new Timer();
        timer.schedule(new RemindTask(),1000,(int)this.fps);
        if(decoding){
            timer2 = new Timer();

            TimerTask timerTask = new TimerTask() {
                Path currentRelativePath = Paths.get("");
                String destDir = currentRelativePath.toAbsolutePath()+"/DecodedImages";
                BufferedImage img;
                File f = new File(destDir);
                File[] files = f.listFiles();
                JFrame frame= new JFrame();
                ImageIcon icon;
                JLabel lbl=new JLabel();
                boolean started = false;
                int reproductionNumber= 0;
                int imgCounter = 0;
                @Override
                public void run() {
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
                        frame.setLocationRelativeTo(null);
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
                        timer2.cancel();

                    }
                }
            };

            timer2.schedule(timerTask,1000,(int)this.fps);
        }
    }

    class RemindTask extends TimerTask {
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/ReproducedImages";
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
                // Arrays.sort(files);
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
