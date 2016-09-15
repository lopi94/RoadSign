/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roadsign;

import java.awt.Image;


/**
 *
 * @author Julia Kubieniec
 */
public class Sign {
    
    private String path;
    private String categoryName;
    private Image image;
    private double[] testSet;
    private double[] resultSet;

    public Sign(String path, String categoryName, Image image, double[] testSet, double[] resultSet) {
        this.path = path;
        this.categoryName = categoryName;
        this.image = image;
        this.testSet = testSet;
        this.resultSet = resultSet;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public double[] getTestSet() {
        return testSet;
    }

    public void setTestSet(double[] testSet) {
        this.testSet = testSet;
    }
    
    public double[] getResultSet() {
        return resultSet;
    }

    public void setResultSet(double[] resultSet) {
        this.resultSet = resultSet;
    }

}
