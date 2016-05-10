import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class TweetsKMeans {
	
	public static String inputFile;
	public static String outputFile;
	public static String seedTweetFileName;
	public static ArrayList<Long> centroidTweetIDList = new ArrayList<Long>();
	public static ArrayList<Tweet> prevCentroidTweetList = new ArrayList<Tweet>();
	public static ArrayList<Tweet> centroidTweetList = new ArrayList<Tweet>();
	public static ArrayList<Tweet> completeTweetList = new ArrayList<Tweet>();
	public static ArrayList<Double> SSE = new ArrayList<Double>();
	
	public static void setPrevCentroidTweetList(int noOfClusters, String seedFileName) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(seedFileName));
		String tweetID = null; 
		int count = 1;
		while((tweetID = br.readLine()) != null && count <= noOfClusters){
			String [] valueFromTxt = tweetID.split(",");
			centroidTweetIDList.add(Long.parseLong(valueFromTxt[0]));	
			count++;
		}
		for(int i = 0; i<centroidTweetIDList.size();i++){			
			for(int j = 0; j<completeTweetList.size(); j++){
				if(completeTweetList.get(j).getTweetID() == centroidTweetIDList.get(i) ){
					prevCentroidTweetList.add(completeTweetList.get(j));
					prevCentroidTweetList.get(i).setClusterID(i+1);
					break;
				}
			}
		}
		br.close();
	}
	
	public static void setInputTweetList(String inputFile) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(inputFile));
		String line;
		JsonReader jr;
		JsonObject tweetJsonObj;
		String tweetContentTemp;
		long tweetID;

		while((line = br.readLine()) != null){			 
			Tweet t1 = new Tweet();
			jr = Json.createReader(new StringReader(line));
			tweetJsonObj = jr.readObject();
			tweetID = Long.parseLong(tweetJsonObj.get("id").toString());
			tweetContentTemp = tweetJsonObj.get("text").toString();
			completeTweetList.add(t1);
			t1.setTweet(tweetID, tweetContentTemp);
		}
		br.close();
	}
	
	public static void firstIteration(int K, String seedFileName, String inputFile) throws IOException{
		if(0< K && K < 252){
			setInputTweetList(inputFile);
			setPrevCentroidTweetList(K,seedFileName);
			findMinJaccardDistance(completeTweetList, prevCentroidTweetList);
		}else{
				System.out.println("incorrect k value");
		}
	}
	
	public static Tweet findClosestTweet(ArrayList<Tweet> tweetClusters){
		Tweet minTweet = null;
		double minDistance = 999999999999.0;
		double tempDistance ;
		for(int i = 0; i < tweetClusters.size(); i++){
			tempDistance = 0;
			for(int j = 0 ; j < tweetClusters.size(); j++){
				tempDistance += Tweet.getJaccardianDistance(tweetClusters.get(i),tweetClusters.get(j));
			}
			if(tempDistance < minDistance){
				minDistance = tempDistance;
				minTweet = tweetClusters.get(i);
				minTweet.setJaccardDist(0.0);
			}
		}		
		return minTweet;
	}
	
	public static void findMinJaccardDistance(ArrayList<Tweet> completeTweetL,ArrayList<Tweet> previousCentroidTweetL){
		double maxDistance;	
		long centroidID,tempTweetID;		
		Tweet tempTweet,CenteroidTweet;
		boolean isCentroid = true;		
		int clusterID = -1;

		for(int i = 0; i < completeTweetL.size();i++){
			maxDistance = 9999999999.0;	
			isCentroid = false;
			tempTweet = completeTweetL.get(i);	
			
			for(int j = 0 ; j < previousCentroidTweetL.size(); j++){
				CenteroidTweet = previousCentroidTweetL.get(j);
				centroidID = CenteroidTweet.getTweetID();
				tempTweetID = tempTweet.getTweetID();
				
				if(tempTweetID == centroidID){
					isCentroid = true;
					break;
				}
				else if(tempTweetID != centroidID){
					double dist = Tweet.getJaccardianDistance(tempTweet, CenteroidTweet);
					if(maxDistance > dist){
						maxDistance = dist;
						clusterID = CenteroidTweet.getClusterID();
					}
				}
			}
			if(isCentroid == false){
				tempTweet.setClusterID(clusterID);
				tempTweet.setJaccardDist(maxDistance);
			}
		}
	}
	
	public static void updateCenteroid(ArrayList<Tweet> centroidTweetL,ArrayList<Tweet> completeList){		
		ArrayList<Tweet> tweetClusters = new ArrayList<Tweet>();
		
		centroidTweetList.clear();
		for(int i = 0; i < centroidTweetL.size(); i++){
			tweetClusters.clear();			
			
			for(int j = 0; j < completeList.size(); j++){
				if(completeList.get(j).getClusterID() == i+1){
					tweetClusters.add(completeList.get(j));
				}
			}

			centroidTweetList.add(findClosestTweet(tweetClusters));	
		}				
		findMinJaccardDistance(completeTweetList, centroidTweetList);		
	}
	
	public static boolean isSameCentroids(ArrayList<Tweet> oldCentroids , ArrayList<Tweet> newCentroids){
		double distance = 0.0;
		for(int i = 0 ; i < oldCentroids.size() ; i++){
			distance += Tweet.getJaccardianDistance(oldCentroids.get(i), newCentroids.get(i));
		}		
		if(distance > 0.0)
			return true;
		return false;
	}
	
	public static void printOutput(String filename) throws IOException{	
		double sseValue = 0.0;
		double distance = 0.0;
		StringBuffer sb = new StringBuffer();
		
		for(int i = 0 ; i < prevCentroidTweetList.size();i++){
			distance = 0.0;
			
			sb.append(prevCentroidTweetList.get(i).getClusterID());
			sb.append("\t");
			int count = 0;
			for(int j = 0 ; j < completeTweetList.size(); j++){
				if(i+1 == completeTweetList.get(j).getClusterID()){
					if(count == 0){
						sb.append(completeTweetList.get(j).getTweetID());
						count++;
					}else{
						sb.append(",");
						sb.append(completeTweetList.get(j).getTweetID());
					}
					distance += Tweet.getJaccardianDistance(prevCentroidTweetList.get(i),completeTweetList.get(j));
				}			
			}
			sb.append("\n");
			SSE.add(distance);
		}
		
		BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(filename)));
		bwr.write(sb.toString());
		bwr.flush();
		bwr.close();
		
		for(int i = 0 ; i < SSE.size(); i++){			
			sseValue += SSE.get(i);
		}
		
		System.out.println("SSE for the given tweets is :"+sseValue);
	}
	
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		int kValue = Integer.parseInt(args[0]);
		seedTweetFileName = args[1];
		inputFile = args[2];
		outputFile = args[3]; 				
		boolean loopFlag = true;
		
		firstIteration(kValue,seedTweetFileName,inputFile);		
		//printOutput(outputFile);
		while(loopFlag){
			updateCenteroid(prevCentroidTweetList,completeTweetList);			
			loopFlag = isSameCentroids(prevCentroidTweetList,centroidTweetList);
			prevCentroidTweetList=(ArrayList<Tweet>) centroidTweetList.clone();	
		}
		printOutput(outputFile);
	}
	
}
