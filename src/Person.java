
public class Person {
	private double attributes[];
	private Outcome assigned; //yes or no
	
	public Person(String line, int mode) {
		String split[] = line.split(",");
		if(mode == 0) { //training data
			attributes = new double[split.length - 1];
			for(int i = 0; i < split.length - 1; i++) {
				attributes[i] = Double.parseDouble(split[i]);
			}
			if(split[split.length-1].equals("yes")) {
				assigned = Outcome.yes;
			}
			else {
				assigned = Outcome.no;
			}
		}
		else {//test data
			attributes = new double[split.length];
			for(int i = 0; i < split.length; i++) {
				attributes[i] = Double.parseDouble(split[i]);
			}
			assigned = Outcome.unknown;
		}
	}
	
	public double[] getAttributes() {
		return attributes;
	}
	
	public Outcome getOutcome() {
		return assigned;
	}
	
}
