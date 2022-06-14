package org.example;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import me.tongfei.progressbar.ProgressBar;

import javax.imageio.ImageIO;


public class ImageFilters {
    private int binarization;
    
    private boolean negative;

    private int averaging;
    
    /**
     * Initialize our ImageFiltersHandler with the needed data to execute the filters.
     * @param b
     * @param n
     * @param averaging
     */
    public ImageFilters(int b, boolean n, int averaging){
        this.binarization = b;
        this.negative = n;
        this.averaging = averaging;
    }
    
    /**
     * Controls the call on which filter must call depending on the given arguments.
     */
    public void handleFilters(){
        if(binarization == 0 && !negative && averaging == 0){
            System.out.println("Filter non-applied");
        }else if(binarization>0){
            applyBinarization();
        }else if(negative){
            applyNegative();
        }else if(averaging>0){
            applyAveraging();
        }
    }
    
    /**
     * Applies a negative filter on all images.
     * This filter applies the maxValueOfPixel-valueOfPixel
     * on each channel so we get the inverse value of the RGB.
     */
    private void applyNegative() {
        ProgressBar pb = new ProgressBar("Negative filter", 100); // name, initial max
        pb.start();
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/ReproducedImages";
        File f = new File(destDir);
        File[] files = f.listFiles();
        Arrays.sort(files);
        BufferedImage img = null;
        int h;
        int w;
        int fileIterator = 0;
        for(File filesListed:files){
            pb.step();
            try {
                img = ImageIO.read(new File(filesListed.getAbsolutePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            h=img.getHeight();
            w=img.getWidth();
            BufferedImage bufferedImage = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i<w;i++){
                for(int j = 0; j<h;j++){

                    int p = img.getRGB(i, j);
                    int a = (p >> 24) & 0xff;
                    int r = (p >> 16) & 0xff;
                    int g = (p >> 8) & 0xff;
                    int b = p & 0xff;

                    // subtract RGB from 255
                    r = 255 - r;
                    g = 255 - g;
                    b = 255 - b;

                    // set new RGB value
                    p = (a << 24) | (r << 16) | (g << 8) | b;

                    // for light color it set white
                    bufferedImage.setRGB(i, j, p);
                }
            }
            File file = new File(currentRelativePath.toAbsolutePath()+"/ReproducedImages/Cubo"+String.format("%02d", fileIterator)+".jpg");
            try {
                ImageIO.write(bufferedImage, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileIterator++;
        }
        pb.stop();
    }

    /**
     * Applies a binaritzation filter on all images.
     * Gets the values of each RGB channel and determines if we change it to white(255) or black(0),
     * depending on the threshold.
     */
    private void applyBinarization() {
        ProgressBar pb = new ProgressBar("Binarizing images", 100); // name, initial max
        pb.start();
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/ReproducedImages";
        File f = new File(destDir);
        File[] files = f.listFiles();
        Arrays.sort(files);
        BufferedImage img = null;
        int h = 0;
        int w = 0;
        int fileIterator = 0;

        for(File filesListed:files){
            pb.step();
            try {
                img = ImageIO.read(new File(filesListed.getAbsolutePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            h=img.getHeight();
            w=img.getWidth();
            BufferedImage bufferedImage = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
            for (int i = 0; i<w;i++){
                for(int j = 0; j<h;j++){
                    int val = img.getRGB(i, j);
                    int r = (0x00ff0000 & val) >> 16;
                    int g = (0x0000ff00 & val) >> 8;
                    int b = (0x000000ff & val);
                    int m=(r+g+b);
                    if(m>=binarization)
                    {
                        // for light color it set white
                        bufferedImage.setRGB(i, j, Color.WHITE.getRGB());
                    }
                    else{
                        // for dark color it will set black
                        bufferedImage.setRGB(i, j, 0);
                    }

                }
            }
            File file = new File(currentRelativePath.toAbsolutePath()+"/ReproducedImages/Cubo"+String.format("%02d", fileIterator)+".jpg");
            try {
                ImageIO.write(bufferedImage, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileIterator++;
        }
        pb.stop();
    }
    
    /**
     * Applies an averaging filter on all images.
     * It takes every pixel and check their 'n' surrounding pixels and average the color,
     * setting that color in the pixel that we are iterating for.
     * 'n' is determined by the arguments given.
     */
    private void applyAveraging(){
        ProgressBar pb = new ProgressBar("Averaging images", 100); // name, initial max
        pb.start();
        Path currentRelativePath = Paths.get("");
        String destDir = currentRelativePath.toAbsolutePath()+"/ReproducedImages";
        File f = new File(destDir);
        File[] files = f.listFiles();
        Arrays.sort(files);
        BufferedImage img = null;
        int h = 0;
        int w = 0;
        ArrayList<Integer> pixelList = new ArrayList<>();
        int fileIterator = 0;
        for(File filesListed:files){
            pb.step();
            try {
                img = ImageIO.read(new File(filesListed.getAbsolutePath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            h=img.getHeight();
            w=img.getWidth();
            BufferedImage bufferedImage = new BufferedImage(w,h, BufferedImage.TYPE_INT_RGB);
            for (int i = averaging; i<w-averaging;i++){
                for(int j = averaging; j<h-averaging;j++){
                    for(int x = 0;x<averaging;x++){
                        pixelList.add(img.getRGB(i-averaging, j-averaging));
                        pixelList.add(img.getRGB(i-averaging,j));
                        pixelList.add(img.getRGB(i-averaging,j+averaging));
                        pixelList.add(img.getRGB(i,j-averaging));
                        pixelList.add(img.getRGB(i,j));
                        pixelList.add(img.getRGB(i,j+averaging));
                        pixelList.add(img.getRGB(i+averaging,j-averaging));
                        pixelList.add(img.getRGB(i+averaging,j));
                        pixelList.add(img.getRGB(i+averaging,j+averaging));
                    }
                    Collections.sort(pixelList);
                    bufferedImage.setRGB(i, j, (pixelList.get(pixelList.size()/2) + pixelList.get(pixelList.size()/2 - 1))/2);
                    pixelList.clear();
                }
            }
            File file = new File(currentRelativePath.toAbsolutePath()+"/ReproducedImages/Cubo"+String.format("%02d", fileIterator)+".jpg");
            try {
                ImageIO.write(bufferedImage, "jpg", file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            fileIterator++;
        }
        pb.stop();

    }
}
