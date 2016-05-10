import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KMeans_Algorithm {

	public static List<Point> points = new ArrayList<>();
	public static List<Point> centroids = new ArrayList<>();
	public static List<Point> prevCentroids = new ArrayList<>();
	public static ArrayList<Double> SSE = new ArrayList<>();
	public static String inputFile;
	public static String outputFile;


	private static void readFile(String inputFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line = br.readLine();
		while((line = br.readLine()) != null){
			String [] value = line.split("\t");
			Point p = new Point(Integer.parseInt(value[0]), Double.parseDouble(value[1]),Double.parseDouble(value[2]));
			points.add(p);
		}
		br.close();
	}

	private static void firstIteration(int kValue) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		//randomly initializing K centroids
		for (int i=1; i<100; i++)
			list.add(new Integer(i));

		Collections.shuffle(list);

		for (int i=0; i<kValue; ++i) {
			Point tempCentroid = points.get(list.get(i));
			Point p = new Point();
			tempCentroid.setClusterID(i+1);
			prevCentroids.add(p);
			centroids.add(tempCentroid);	
		}

		calculateDistanceFromCentroid();
	}

	private static boolean stationaryCentroids() {
		double distance = 0.0;
		for(int i = 0 ; i < centroids.size(); i++){
			Point oldPoint = prevCentroids.get(i);
			distance += Point.calcDistance(oldPoint, centroids.get(i));
		}		
		if(distance <= 0.0)
			return true;		
		return false;
	}

	private static void calculateDistanceFromCentroid() { 
		Point tempPoint, centroid = null; 
		double maxDistance; 
		int clusterID = -1; 
		boolean isCentroid = false;

		for(int j = 0; j < points.size();j++){			
			maxDistance = 9999999999.00; 
			isCentroid = false;
			clusterID = -1;
			tempPoint = points.get(j);

			for(int i = 0; i < centroids.size() ; ++i){				
				centroid = centroids.get(i);				
				if(tempPoint.getPointID() == centroid.getPointID()){
					isCentroid = true;
					break;
				}
				else {										
					double dist = Point.calcDistance(tempPoint, centroid);
					if(dist < maxDistance){
						maxDistance = dist;
						clusterID = centroid.getClusterID();
					}
				}				
			}			
			if(isCentroid == false){							
				tempPoint.setClusterID(clusterID);
				tempPoint.setDistanceFromCentroid(maxDistance);
			}
		}

	}

	private static void updateCentroid() {
		
		double tempPtXVal,tempPtYVal;
		int oldCentroidClusterID, clusterPointCount;
		for(int i = 0; i < centroids.size() ; i++){
			Point newCentroid = new Point();
			tempPtXVal = 0.0;
			tempPtYVal = 0.0;
			oldCentroidClusterID = centroids.get(i).getClusterID();			
			clusterPointCount = 0;
			for(int j = 0; j < points.size() ; j++){
				if(oldCentroidClusterID == points.get(j).getClusterID() ){					
					tempPtXVal += points.get(j).getxValue();
					tempPtYVal += points.get(j).getyValue();
					clusterPointCount++;
				}
			}
			newCentroid.setPoint((tempPtXVal/clusterPointCount), (tempPtYVal/clusterPointCount));
			newCentroid.setClusterID(oldCentroidClusterID);
			newCentroid.setPointID(points.size()+i+1);
			newCentroid.setDistanceFromCentroid(0.0);			
			prevCentroids.set(i, centroids.get(i));
			centroids.set(i, newCentroid);			
		}		
	}

	private static void printOutput(String filename) throws IOException {
		StringBuffer sb = new StringBuffer();
		double distance = 0.0;
		double sseValue = 0.0;
		for(int i = 0 ; i < centroids.size();i++){			
			sb.append(i+1);
			sb.append("\t");
			distance = 0.0;
			int count = 0;
			for(int j = 0 ; j < points.size(); j++){
				if(i+1 == points.get(j).getClusterID()){					
					//System.out.print(sb);
					if(count == 0){
						sb.append(points.get(j).getPointID());
						//System.out.print(points.get(j).getPointID());
						count++;
					}else{
						sb.append(",");
						sb.append(points.get(j).getPointID());
						//System.out.print("," + points.get(j).getPointID());
					}
					distance += Point.calcDistance(centroids.get(i), points.get(j));
				}
			}
			sb.append("\n");
			SSE.add(distance);
			//System.out.println();
		}
		for(int x = 0 ; x < SSE.size(); x++){			
			sseValue += SSE.get(x);
		}

		String str = "Goodness (SSE) for the clustering is : " + sseValue;

		System.out.println(str);
		sb.append("\n");
		sb.append(str);

		printCentroids();

		BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(filename)));
		bwr.write(sb.toString());
		bwr.flush();
		bwr.close();
	}

	public static void printCentroids(){

		System.out.println("Centroids while convergence are : ");

		for (Point point : centroids) {
			System.out.println(point.xValue + ", " + point.yValue);
		}
	}

	public static void main(String[] args) throws IOException {
		int kValue = Integer.parseInt(args[0]);
		inputFile = args[1];
		outputFile = args[2];

		readFile(inputFile);
		firstIteration(kValue);
		
		//We are limiting the number of maximum iterations to 25 as per requirement.
		int no_of_iterations = 1;
		
		while(!stationaryCentroids() && no_of_iterations <=25){
			updateCentroid();
			calculateDistanceFromCentroid();
			no_of_iterations++;
		}

		printOutput(outputFile);
	}

}