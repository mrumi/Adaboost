package adaboost;

import java.util.ArrayList;

public class feature {
	
    private ArrayList<Integer>numValues; 

    public feature(){
    	numValues = new ArrayList<Integer>();
    }
    
    public void addFeature(int feat){
    	numValues.add(feat);
    }
    
    public int getFeature(int pos){
    	return numValues.get(pos);
    }

}
