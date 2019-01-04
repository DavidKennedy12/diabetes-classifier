import java.util.ArrayList;

public class NaiveBayes {
	//holds the mean values for each attribute in the yes class
	//holds the standard deviation values for each attribute in the yes class
	private double attributes[];
	private double yesMean[];
	private double yesStdDev[];
	private double noMean[];
	private double noStdDev[];
	
	private double pYes;
	private double pNo;
	
	
	public NaiveBayes(ArrayList<Person> training) {
		int size = training.get(0).getAttributes().length;
		yesMean = new double[size];
		noMean = new double[size];
		yesStdDev = new double[size];
		noStdDev = new double[size];
		double y = 0; //Number of yes examples in training set
		double n = 0; //Number of no examples in training set
		for(Person p : training) {
			attributes = p.getAttributes();
			if(p.getOutcome() == Outcome.yes) {
				y++;
				for(int i = 0; i < attributes.length; i++) {
					yesMean[i] += attributes[i];
				}
			}
			else {
				n++;
				for(int i = 0; i < attributes.length; i++) {
					noMean[i] += attributes[i];
				}
			}
		}
		
		for(int i = 0; i < attributes.length; i++) {
			yesMean[i] = yesMean[i] / y;
			noMean[i] = noMean[i] / n;
		}
		
		for(Person p : training) {
			attributes = p.getAttributes();
			if(p.getOutcome() == Outcome.yes) {
				for(int i = 0; i < attributes.length; i++) {
					yesStdDev[i] += Math.pow(attributes[i] - yesMean[i], 2);
				}
			}
			else {
				for(int i = 0; i < attributes.length; i++) {
					noStdDev[i] += Math.pow(attributes[i] - noMean[i], 2);
				}
			}
		}
		
		for(int i = 0; i < attributes.length; i++) {
			yesStdDev[i] = Math.sqrt((yesStdDev[i])/(y-1));
			noStdDev[i] = Math.sqrt((noStdDev[i])/(n-1));
		}
		pYes = y/(y+n); //probability of any given example being yes
		pNo = n/(n+y); //probability of any given example being no
	}
	
	public Outcome Test(Person test) {
		double pYesGivenE = 1.0;
		double pNoGivenE = 1.0;
		double attributes[] = test.getAttributes();
		for(int i = 0; i < attributes.length; i++) {
			pYesGivenE *= getProbability(attributes[i], yesMean[i], yesStdDev[i]);
			pNoGivenE *= getProbability(attributes[i], noMean[i], noStdDev[i]);
		}
		pYesGivenE *= pYes;
		pNoGivenE *= pNo;
		
		if(pNoGivenE > pYesGivenE) {
			return Outcome.no;
		}
		else {
			return Outcome.yes;
		}
	}
	
	private double getProbability(double x, double mean, double stdDev) { //using an assumed normal distribution
		double denominator = stdDev * Math.sqrt(2*Math.PI);
		double exponent = -1 * ( (Math.pow(x - mean, 2)) / (2*Math.pow(stdDev, 2)) );
		double probability = (1/denominator) * Math.pow(Math.E, exponent);
		return probability;
	}
}
