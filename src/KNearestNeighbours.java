import java.util.ArrayList;

public class KNearestNeighbours {
	private ArrayList<Person> training = new ArrayList<Person>();
	private int neighbours; //as in K nearest neighbours
	
	public KNearestNeighbours(ArrayList<Person> training, int neighbours) {
		this.training = training;
		this.neighbours = neighbours;
	}
	
	public Outcome Test(Person test) {
		ArrayList<Person> nearestNeighbours = new ArrayList<Person>();
	
		int i;
		int j = 0; //index of furthest neighbour
		double furthest = 0; //distance of furthest neighbour
		for(i = 0; i < neighbours; i++) {
			nearestNeighbours.add(training.get(i));
			if (getDistance(test, training.get(i)) > furthest) {
				j = i;
				furthest = getDistance(test, training.get(i));
			}
		}
		
		for(i = neighbours; i< training.size(); i++) { //find the furthest
			if(getDistance(test, training.get(i)) < furthest) {
				nearestNeighbours.remove(j);
				nearestNeighbours.add(training.get(i));
				j = 0;
				furthest = 0;
				for(int k = 0; k < neighbours; k++) { //find the new furthest
					if (getDistance(test, nearestNeighbours.get(k)) > furthest) {
						j = k; //index of the furthest neighbour
						furthest = getDistance(test, nearestNeighbours.get(k));
					}
				}	
			}
		}
		
		
		int yesCount = 0;
		int noCount = 0;
		for(Person p : nearestNeighbours) {
			if(p.getOutcome() == Outcome.yes) {
				yesCount++;
			}
			else {
				noCount++;
			}
		}
		
		if(yesCount < noCount) {
			return Outcome.no;
		}
		else {
			return Outcome.yes;
		}	
	}
	
	//Returns the Euclidean distance between two People
	private double getDistance(Person example, Person test) {
		double exampleAttributes[] = example.getAttributes();
		double testAttributes[] = test.getAttributes();
		double attributeDelta[] = new double[testAttributes.length];
		for(int i = 0; i < attributeDelta.length; i++) {
			attributeDelta[i] = exampleAttributes[i] - testAttributes[i];
		}
		double sumOfSquares = 0;
	
		for(int i = 0; i < attributeDelta.length; i++) {
			sumOfSquares += Math.pow(attributeDelta[i], 2);
		}
		double euclidean = Math.sqrt(sumOfSquares);
		return  euclidean;
	}
}
