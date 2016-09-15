/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package roadsign;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.learning.BackPropagation;

/**
 *
 * @author Julia Kubieniec
 */
public class MyNeuralNetwork {
    
    public static final int INPUT_COUNT = 256;
    public static final int OUTPUT_COUNT = 15;
    
    private double currentIteration;
    private double previousEpochError;
    private double totalNetworkError;

    public double getCurrentIteration() {
        return currentIteration;
    }

    public double getPreviousEpochError() {
        return previousEpochError;
    }

    public double getTotalNetworkError() {
        return totalNetworkError;
    }
    
    public void train(SignDAO trainingList, double learningRate, int maxIteration, double maxError) {
        
        // create training set
        DataSet trainingSet = createTrainingSet(trainingList);
        
        //create neural network
        MultiLayerPerceptron network;
        BackPropagation train = new BackPropagation();
        
        //set learning parametres
        train.setLearningRate(learningRate);
        train.setMaxError(maxError);
        train.setMaxIterations(maxIteration);
        network = new MultiLayerPerceptron(TransferFunctionType.TANH, INPUT_COUNT,56,OUTPUT_COUNT);
        train.setNeuralNetwork(network);
        train.setTrainingSet(trainingSet);
        network.setLearningRule(train);

        //learn network
        Thread t = new Thread() {
            public void run() {
                long t = System.currentTimeMillis();
                train.learn(trainingSet);
                network.save("NeuralNetwork.nnet");
                t = System.currentTimeMillis()-t;
                System.out.println("Time : " + t + " ms");
            }
        };
        t.start();
        System.out.println("layers count: " + network.getLayersCount());
        System.out.println("Max iter: " + train.getMaxIterations());
        do {
            currentIteration = train.getCurrentIteration();
            previousEpochError = train.getPreviousEpochError();
            System.out.println("Current iteration: " + train.getCurrentIteration());
            System.out.println("Previous epoch error: " + train.getPreviousEpochError());
        } while (train.getCurrentIteration() < train.getMaxIterations());
        totalNetworkError = train.getTotalNetworkError();
        System.out.println("Total error: " + train.getTotalNetworkError());
     
    }
    
    public static NeuralNetwork getNeuralNetwork() throws FileNotFoundException {
        // open neural network file
       InputStream inputStream = new FileInputStream("NeuralNetwork.nnet");
        // load neural network
        NeuralNetwork nn = NeuralNetwork.load(inputStream);
        return nn;
    }
    
    public static List<double[]> test(NeuralNetwork nnet, DataSet dset) {

        List<double[]> result = new ArrayList<>();
        double[] outputRow;
        
        for (DataSetRow trainingElement : dset.getRows()) {
            nnet.setInput(trainingElement.getInput());
            nnet.calculate();          
            outputRow = (nnet.getOutput()).clone();
            
     //       System.out.print("Input: " + Arrays.toString(trainingElement.getInput()));
            System.out.print("Output: " + Arrays.toString(outputRow));
            System.out.println("Desired output: " + Arrays.toString(trainingElement.getDesiredOutput()));
            result.add(outputRow);
        }

        return result;
    }
    
    public DataSet createTrainingSet(SignDAO trainingList) {
        // create training set
        DataSet trainingSet = new DataSet(INPUT_COUNT,OUTPUT_COUNT);
        List<Sign> list = trainingList.getSignList();
        for(int i = 0; i < list.size(); i++) {
            trainingSet.addRow(list.get(i).getTestSet(),list.get(i).getResultSet());
        }
        return trainingSet;
    }
    
}
