package adaboost;

import java.util.ArrayList;

public class decision_stump {
		
	private ArrayList<Data> node_data;	
	private int split_attr;	//attribute used to split data set	
    private feature numValues; 
    private ArrayList<Integer>result;
	
	public decision_stump(feature numVal){
		node_data = new ArrayList<Data>();
		numValues = numVal;	    
	    result = new ArrayList<Integer>();
	}
	
	public void insert(Data d) {
		this.node_data.add(d);
	}
	
	public void buildStump(int count_attr){
		if(this.node_data.size()>0){
			this.createStump(count_attr);
			this.set_result();
			this.clearMem();
		}		
	}
		
	private void createStump(int count_attr) {
        boolean selected=false;
        double bestGain=0;
        double entropy = calEntropy(this.node_data);
        for(int i = 0; i < count_attr - 1; i++) {
            double avgEntropy=0;
            for(int j = 0; j < this.numValues.getFeature(i); j++) {
                ArrayList<Data> subset = getSubset(i, j + 1);
                if(subset.isEmpty())
                	continue;                
				double temp_entropy=calEntropy(subset);
				subset.clear();
                avgEntropy+=temp_entropy*subset.size();
            }
            avgEntropy=avgEntropy/node_data.size();
            double gain = entropy-avgEntropy;
            if (selected == false){
                selected = true;
                bestGain = gain;
                this.split_attr = i;
            }
            else {
                if (gain > bestGain) {
                    selected = true;
                    bestGain = gain;
                    this.split_attr = i;
                }
            }
        }                
    }
	
	private ArrayList<Data> getSubset(int attr,int value) {
        ArrayList<Data> subset = new ArrayList<Data>();
        for(int i = 0; i < node_data.size();i++) {
        	Data d=(Data)node_data.get(i);
        	if(d.getAttribute(attr)==value)
        		subset.add(d);
        }
        return subset;
    }        
	
	private double calEntropy(ArrayList<Data> eData) {
        if(eData.size() == 0)
    		return 0;
        int count1 = 0, count0 = 0;
       	for(int j = 0; j < eData.size(); j++) {
       		Data d = (Data) eData.get(j);
       		if(d.getLabel() == 1)
       			count1++;
       		else
       			count0++;
       	}
       	double prob1 = (double) count1 / eData.size();
       	double prob0 = (double) count0 / eData.size();
       	double pos = 0;
       	double neg = 0;
       	if(count1 > 0)
       		pos = -prob1 * (Math.log10(prob1) / Math.log10(2));
       	if(count0 > 0)
       		neg = -prob0*(Math.log10(prob0) / Math.log10(2));
       	double entropy = pos + neg;
        return entropy;
    }
	
	private void set_result() {
        int pos = 0, neg = 0;
        for(int i = 0; i < node_data.size();i++) {
            Data d = (Data) node_data.get(i);
            if(d.getLabel() == 1)
                pos++;
            else
                neg++;
        }
        int default_res;
    	if(pos > neg)
    		default_res = 1;
    	else
    		default_res = 0;
    	        
        for(int j = 0; j < this.numValues.getFeature(this.split_attr); j++) {
            int class_max1 = 0, class_max0 = 0;
            ArrayList<Data>subset = getSubset(this.split_attr, j + 1);
            if(subset.isEmpty())
                result.add(default_res);
            for(int i = 0; i < subset.size(); i++) {
                Data d=(Data)subset.get(i);
                if(d.getLabel() == 1)
                    class_max1++;
                else
                    class_max0++;
            }
            subset.clear();
            if(class_max1 > class_max0)
                result.add(1);
            else
                result.add(0);
        }
    }	
	
	public int getResult(Data d){		
		int index = d.getAttribute(this.split_attr);
		return this.result.get(index-1);
	}
	
	private void clearMem(){
		node_data.clear();		
	}

}
