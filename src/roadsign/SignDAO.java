/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roadsign;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 *
 * @author Julia
 */
public class SignDAO {
    
    private List<Sign> signList;
    
    public SignDAO() {
        signList = new ArrayList<>();
    }
    
    public SignDAO(Sign sign) {
        signList = new ArrayList<>();
        signList.add(sign);
    }
    
    public List<Sign> getSignList() {
        return signList;
    }
    
    public void addFileToList(File[] filesToAdd) throws IOException {
        if((filesToAdd != null) && (filesToAdd.length > 0)) {
          for(File file : filesToAdd) {
              Sign sign = new Sign(file.getAbsolutePath(),getCategoryNameFromPath(file.getAbsolutePath()),
              getImage(file), createTrainingElement(seeBMPImage(file.getAbsolutePath())),
              getCategoryTrainingCode(getCategoryNameFromPath(file.getAbsolutePath())));
             if(!signList.contains(sign)) {
                signList.add(sign);                 
             }
          }
       }
    }

    private String getCategoryNameFromPath(String path) {
        int dashPos = path.indexOf("-");
        int floorPos = path.indexOf("_");
        String tmp = path.substring(dashPos + 1, floorPos);
        switch(tmp) {
            case Dictionary.NE: return Dictionary.NE;
            case Dictionary.RC: return Dictionary.RC;
            case Dictionary.SL30: return Dictionary.SL30;
            case Dictionary.SL50: return Dictionary.SL50;
            case Dictionary.SL60: return Dictionary.SL60;
            case Dictionary.SL70: return Dictionary.SL70;
            case Dictionary.SL80: return Dictionary.SL80;
            case Dictionary.SL90: return Dictionary.SL90;
            case Dictionary.SL100: return Dictionary.SL100;
            case Dictionary.SL110: return Dictionary.SL110;
            case Dictionary.SL120: return Dictionary.SL120;
            case Dictionary.STP: return Dictionary.STP;
            case Dictionary.WAR: return Dictionary.WAR;
            case Dictionary.YLD: return Dictionary.YLD;
            default: return Dictionary.ERR;
        }
    }
    
    private Image getImage(File file) throws IOException {
        Image image = ImageIO.read(file);
        return image;
    }
    
    private int[][] seeBMPImage(String BMPFileName) throws IOException {
    
        BufferedImage image = ImageIO.read(new File(BMPFileName));

        int[][] array2D = new int[image.getWidth()][image.getHeight()];
        
        for (int xPixel = 0; xPixel < image.getWidth(); xPixel++) {
            for (int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
                int color = image.getRGB(xPixel, yPixel);
                if (color == Color.BLACK.getRGB()) {
                    array2D[xPixel][yPixel] = 1;
                } else {
                    array2D[xPixel][yPixel] = 0; 
                }
            }
        }
        
        return array2D;        
    }
       
    private double[] createTrainingElement(int[][] array2D) {   
        
        double[] bmp = new double[MyNeuralNetwork.INPUT_COUNT];
        int i = 0;
        
        for(int x = 0; x < array2D.length; x++) {
            for(int y = 0; y < array2D[x].length; y++) {
                if(i < MyNeuralNetwork.INPUT_COUNT) {
                    bmp[i] = (byte)array2D[x][y];
                    i++;
                }
            }
        }
        
        return bmp;
    }
    
         //dla każdego casa jeden znak
    public double[] getCategoryTrainingCode(String categoryName) {
        switch(categoryName) {
            case Dictionary.NE: return new double[]{1,0,0,0,0,0,0,0,0,0,0,0,0,0,0}; //1
            case Dictionary.RC: return new double[]{0,1,0,0,0,0,0,0,0,0,0,0,0,0,0}; //2
            case Dictionary.SL30: return new double[]{0,0,1,0,0,0,0,0,0,0,0,0,0,0,0}; //3
            case Dictionary.SL50: return new double[]{0,0,0,1,0,0,0,0,0,0,0,0,0,0,0};   //4
            case Dictionary.SL60: return new double[]{0,0,0,0,1,0,0,0,0,0,0,0,0,0,0};   //5
            case Dictionary.SL70: return new double[]{0,0,0,0,0,1,0,0,0,0,0,0,0,0,0};   //6
            case Dictionary.SL80: return new double[]{0,0,0,0,0,0,1,0,0,0,0,0,0,0,0};   //7
            case Dictionary.SL90: return new double[]{0,0,0,0,0,0,0,1,0,0,0,0,0,0,0};   //8
            case Dictionary.SL100: return new double[]{0,0,0,0,0,0,0,0,1,0,0,0,0,0,0};  //9
            case Dictionary.SL110: return new double[]{0,0,0,0,0,0,0,0,0,1,0,0,0,0,0};  //10
            case Dictionary.SL120: return new double[]{0,0,0,0,0,0,0,0,0,0,1,0,0,0,0};  //11
            case Dictionary.STP: return new double[]{0,0,0,0,0,0,0,0,0,0,0,1,0,0,0};    //12
            case Dictionary.WAR: return new double[]{0,0,0,0,0,0,0,0,0,0,0,0,1,0,0};    //13
            case Dictionary.YLD: return new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,1,0};    //14
            default : return new double[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,1};   //15
        }
    }
    
}
