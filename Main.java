package adaboost;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Dataset ds = new Dataset(); 
        int numAttr = ds.readData("data.csv");                
        ds.divideData();
        feature feat = ds.countFeatures();
        ensemble es = new ensemble(feat);
        ds.setEnsembleData(es);
        es.AdaBoost(50, numAttr);
        Result rs = ds.testBoost(es);
        showResult(rs);
        es.clearData();	                   
        ds.clearData();

	}
	
	public static void showResult(Result adb){
		System.out.println("Accuracy :" + adb.getAccuracy());
		System.out.println("Precision: " + adb.getPrecision()); 
		System.out.println("Recall: " + adb.getRecall());
		System.out.println("F_measure: " + adb.getF_Measure());
		System.out.println("G_mean :" + adb.getG_Mean());  
	}
}
