package adaboost;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.StringTokenizer;


public class Dataset {
	
	private ArrayList<Data> allData;
	private ArrayList<Data> trainingData;
    private ArrayList<Data> testingData;           
    
	public Dataset(){
		allData=new ArrayList<Data>();
		trainingData=new ArrayList<Data>();
	    testingData=new ArrayList<Data>();	    
	}
		
	public int readData(String filename) {
    	int count_attr = 0;
		try {
			BufferedReader br=new BufferedReader(new FileReader(new File(filename)));
			while(true) {
				String s = br.readLine();
				if(s == null)
					break;
				StringTokenizer tokens=new StringTokenizer(s, ",");
				count_attr = tokens.countTokens();
				Data row = new Data(count_attr);
				for (int i = 0; i < count_attr; i++) {
					String str = tokens.nextToken();
					row.setAttribute(i, Integer.parseInt(str));
				}
				allData.add(row);
			}
			br.close();			
		}
		catch(Exception e) {
			System.out.println("Exception : " + e);
		}
		return count_attr;
    }	
	
	public void divideData() {
	  	Collections.shuffle(allData);
    	int train_count=(int)Math.ceil(allData.size()*0.8);     	
        for(int i=0;i<train_count;i++) {        	
        	trainingData.add(allData.get(i));        	
        }
        for(int i=train_count;i<allData.size();i++) {        	
            testingData.add(allData.get(i));
        }        
    }		
	
	public feature countFeatures(){		
		int numFeatures = allData.get(0).size();
		feature f = new feature();
		for(int i = 0; i < numFeatures - 1; i++){
			Hashtable<Integer, Integer> uniq = new Hashtable<Integer,Integer>();			
			for(int j = 0; j < allData.size(); j++) {
				Data d = (Data)allData.get(j);				
				int feat = d.getAttribute(i);							
				if(uniq.containsKey(feat)){
					uniq.put(feat, uniq.get(feat)+1);					
				}
				else
					uniq.put(feat, 1);									
			}			
			f.addFeature(uniq.size());
			uniq.clear();
		}												
		return f;
	}
		
	public void setEnsembleData(ensemble e){
		for(int i = 0; i < this.trainingData.size(); i++)
			e.insertData(this.trainingData.get(i));
	}
	
	public Result testBoost(ensemble e){
		Result rs = new Result();
		int TP = 0, TN = 0, FP = 0, FN = 0;
		for(int i = 0; i < this.testingData.size(); i++){
			Data d = this.testingData.get(i);
			int p = e.predict(d);
			if(d.getLabel() == 1 && p == 1)
				TP++;
			else if(d.getLabel() == 1 && p == 0)
				FN++;
			else if(d.getLabel() == 0 && p == 1)
				FP++;
			else if(d.getLabel() == 0 && p == 0)
				TN++;
		}
		rs.performance_evaluate(TP, FP, TN, FN);
		return rs;
	}
	public void clearData() {
        trainingData.clear();
        testingData.clear(); 
        this.allData.clear();
    }         

}


