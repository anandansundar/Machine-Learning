import java.util.ArrayList;
import java.util.List;

public class Tweet {
	
	private long tweetID;
	private int clusterID;
	private String tweetContent;
	List<String> wordList = new ArrayList<String>();
	double jaccardDistance;
	public static List<String> stopWords = new ArrayList<String>();
	
	Tweet(){
		this.tweetID = 0;
		this.clusterID = -1;
		this.tweetContent = "NA";
		this.jaccardDistance = 0.0;
	}
	
	Tweet(long tweetID, String tweetStr){
		this.tweetID = tweetID;
		this.tweetContent = tweetStr;
		this.clusterID = -1;
		this.jaccardDistance = 0.0;
	}
	
	void setTweetContent(String tweet){
		tweet = tweet.replaceAll("http+://.+?\\s+", "");
		tweet = tweet.replaceAll("@\\p{L}+", "");
		this.tweetContent = tweet.replaceAll("[!.+,:()\"\'_#@]", "");
		
		String[] lines = this.tweetContent.split("\n");
		for(String line : lines){
			String[] wordArr = line.split(" ");
			for(String word : wordArr){	
				this.wordList.add(word);
			}
		}
		setStopWords();
		wordList.removeAll(stopWords);
	}
	
	void setTweet(long tweetID, String tweet){
		setTweetID(tweetID);
		setTweetContent(tweet);
	}
	
	@SuppressWarnings("unchecked")
	public static double getCommonWordCount(Tweet t1, Tweet t2){
		List<String> firstTweet = t1.wordList;
		ArrayList<String> realCopyOfTweetStringTwo = (ArrayList<String>) t2.wordList;		
		ArrayList<String> secondTweet = (ArrayList<String>) realCopyOfTweetStringTwo.clone();
		int intersectionCount = 0;
		for(int i = 0; i < firstTweet.size() ; i++){
			for(int j = 0; j < secondTweet.size() ; j++){
				 if(firstTweet.get(i).equalsIgnoreCase(secondTweet.get(j))){
					 secondTweet.remove(j);
					 intersectionCount++;
					 break;
				 }
			}
		}		
		return intersectionCount;
	}
	
	public static double getTotalWordCount(Tweet t1, Tweet t2){
		return (t1.wordList.size() + t2.wordList.size() - getCommonWordCount(t1,t2));
	}
	
	public static double getJaccardianDistance(Tweet centroidTweet, Tweet otherTweet){
		return( 1 -(getCommonWordCount(centroidTweet, otherTweet) / getTotalWordCount(centroidTweet, otherTweet)));
	}
	
	long getTweetID(){
		return this.tweetID;
	}
	
	List<String> getWords(){
		return this.wordList;
	}
	
	int getClusterID(){
		return this.clusterID;
	}
	
	void setClusterID(int clustID){
		this.clusterID = clustID;
	}
	
	void setJaccardDist(double dist){
		this.jaccardDistance = dist;
	}
	
	void setTweetID(long tweetID){
		this.tweetID = tweetID;
	}
	
	public void setStopWords(){
		stopWords.add(" ");
		stopWords.add(",");
		stopWords.add("_");
		stopWords.add("");
		stopWords.add("-");
		stopWords.add("|");
		stopWords.add("#");
		stopWords.add(":");
		stopWords.add("\n");
		stopWords.add("@");
		stopWords.add("RT");
		stopWords.add("-CC");
	}
}
