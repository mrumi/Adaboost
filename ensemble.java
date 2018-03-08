package adaboost;

import java.util.*;

/**
 *
 * @author Monjura
 */
public class ensemble {
	
	ArrayList<Boolean> prediction;
    ArrayList<Double> weights;
    ArrayList<Double> prob;
    ArrayList<decision_stump> classifier;
    ArrayList<Double> beta; 
    feature fSet;    
    ArrayList<Data>trainDataset;
    
    public ensemble(feature f){
    	prediction = new ArrayList<Boolean>();
    	weights = new ArrayList<Double>();
    	prob = new ArrayList<Double>(); 
    	beta = new ArrayList<Double>();
    	classifier = new ArrayList<decision_stump>();
    	trainDataset = new ArrayList<Data>(); 
    	fSet = f;
    }
    
    public void insertData(Data d){
    	trainDataset.add(d);
    }
    
    private void initParam(){    	
        int trainNum = this.trainDataset.size();        
        for(int i = 0; i < trainNum; i++){
        	this.weights.add(1.0/trainNum);
        	this.prob.add(1.0/trainNum);
        }                     	    	    	    		    
    }
        
    private void updateParam(double beta) {
    	double sum = 0;
        for(int i = 0; i < this.weights.size(); i++) {
            boolean check=prediction.get(i);
            if(check){
                weights.set(i,weights.get(i)*beta);
            }
            sum += this.weights.get(i);
        }
        this.prob.clear();
        for(int i = 0; i < this.weights.size(); i++){
        	this.prob.add(this.weights.get(i)/sum);
        }
    }    
    
    private void dataSelection(decision_stump stump){
    	int numData = this.trainDataset.size();
    	double running_prob[]=new double [numData];
        double prob_sum = 0;
        for(int i = 0; i < numData; i++) {
        	prob_sum += (double) prob.get(i);
        
        	running_prob[i] = prob_sum;
        }                               	    	 
    	for(int j = 0; j < numData; j++) {
            Random generator = new Random();
            double random = generator.nextDouble();
            int index = find(random, running_prob);
            if(index >= numData) {                 
                j = j - 1;
                continue;
            }
            stump.insert(this.trainDataset.get(index));            
    	}    	 
    }
     
    private int find(double query, double[] data) {
        int index = -1;
        int max = data.length;
        int min = 0;
        while (min <= max && index == -1) {
        	int mid = (max + min) / 2;
            if (mid == 0 && query < data[mid])
                index = mid;
            else if (mid == (data.length - 1) && query <= data[mid])
                 index = mid;
             else if (data[mid] <= query && data[mid + 1]>query)
             	index = mid;
             else if (data[mid] > query)
                 max = mid;
             else
                 min = mid;
         }
         return index;
     }   

    public int predict(Data row) {
        double pos = 0, neg = 0;        
        for(int i = 0; i < classifier.size(); i++) {
        	decision_stump t = classifier.get(i);
        	int pr = t.getResult(row);            
            double b = (double)beta.get(i);
            double vote = Math.log(1.0/b);
            if(pr == 1)
                pos += vote;
            else
                neg += vote;
        }
        if(pos > neg)
            return 1;
        else
            return 0;
    }

    private double check_error(decision_stump stump) {
    	prediction.clear();
        double error_sum=0;        
        for(int i = 0; i < this.trainDataset.size();i++) {	    
	        Data d = (Data)trainDataset.get(i);
            int actual = d.getLabel();            
            int predicted = stump.getResult(d);
            if(actual == predicted)
                prediction.add(true);
            else {            
                error_sum+=(double)prob.get(i);
                prediction.add(false);
            }
        }        
        return error_sum;
    }

    public void AdaBoost(int rounds, int numAttr) { 
    	this.initParam();
        for(int r = 0; r < rounds; r++) {  
        	System.out.println(r);
        	decision_stump stump = new decision_stump(this.fSet);
            dataSelection(stump); 
            stump.buildStump(numAttr);
            double error = check_error(stump);
            if(error > 0.6) {
            	r = r - 1;
                continue;
            }
            double beta_val = error / (1 - error);
            classifier.add(stump);
            beta.add(beta_val);
            this.updateParam(beta_val);
        }        
    }           
        
    public void clearData(){        
        classifier.clear();        
        prediction.clear();
        weights.clear();
        prob.clear();
        beta.clear();
    }    
}