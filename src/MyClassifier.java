import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MyClassifier {

	public static void main (String[] args) throws IOException 
	{
		ArrayList<Person> training = new ArrayList<Person>();
		ArrayList<Person> testing = new ArrayList<Person>();
		String trainingFile = args[0];
		String testingFile = args[1];
		String algorithm = args[2];
		String line;
		
		BufferedReader reader = new BufferedReader(new FileReader(trainingFile));
		
		if(testingFile.equals("FOLD")) { //used for testing 10-fold stratification;
			int errors[] = new int[10];
			
			for (int i = 1; i <= 10 ; i++) { //run the test 10 times
				reader = new BufferedReader(new FileReader(trainingFile));
				while((line = reader.readLine()) != null) { 	
					if(line.equals("fold" + i)) { //use fold i as testing data
						while((line = reader.readLine()) != null) {
							if(line.length() > 6) {
								testing.add(new Person(line,0));
							}	
							else {
								break;
							}
						}
					}
					else if(line.length() > 6) { //use other folds as training data
						training.add(new Person(line,0));
					}	
				}	
			
				if(algorithm.equals("NB")) {
					NaiveBayes search = new NaiveBayes(training); //initalise Naive Bayes classifier, i.e evaluate means, and standard deviations
					for(Person p : testing) { //Run the test cases
						if(search.Test(p) != p.getOutcome()) {
							errors[i-1] += 1;
						}
					}
				}
				else { //i.e algorithm is K Nearest Neighbours
					Scanner scan = new Scanner(args[2]).useDelimiter("\\D+");
					int k = scan.nextInt();
					KNearestNeighbours search = new KNearestNeighbours(training, k);
					for(Person p : testing) { //Run the test cases
						if(search.Test(p) != p.getOutcome()) {
							errors[i-1] += 1;
						}
					}
				}
				training.clear();
				testing.clear();
				reader.close();			
			}
			double avg = 0;
			for(int i = 0; i < 10; i++) {
				System.out.println("errors for for fold[" +i+  "] = " + errors[i]);
				avg += errors[i];
			}
			avg = avg/10;
			System.out.println("Average error rate is: " + avg + "/" + 76.8 ); //76.8 is average size of a fold
			System.out.println("Accurcy is: " + (1 - avg/76.8)); //accuracy of the classifier
			return;
		}
		
		
		while((line = reader.readLine()) != null) { //parse the csv file for the training examples, assuming each example has 8 attributes and an assigned class		
			training.add(new Person(line, 0));
		}
		reader.close();

		reader = new BufferedReader(new FileReader(testingFile)); //parse the csv file for the testing examples, assuming each example has 8 attributes but no assigned class
		while((line = reader.readLine()) != null) {
			testing.add(new Person(line, 1));
		}
		reader.close();
		
		if(algorithm.equals("NB")) {
			NaiveBayes search = new NaiveBayes(training); //initalise Naive Bayes classifier, i.e evaluate means, and standard deviations
			for(Person p : testing) { //Run the test cases
				if(search.Test(p) == Outcome.yes) {
					System.out.println("yes");
				}
				else {
					System.out.println("no");
				}
			}
		}
		
		else { //i.e algorithm is K Nearest Neighbours
			Scanner scan = new Scanner(args[2]).useDelimiter("\\D+");
			int k = scan.nextInt();
			KNearestNeighbours search = new KNearestNeighbours(training, k);
			for(Person p : testing) { //Run the test cases
				if(search.Test(p) == Outcome.yes) {
					System.out.println("yes");
				}
				else {
					System.out.println("no");
				}
			}
		}
		return;
	}
}
