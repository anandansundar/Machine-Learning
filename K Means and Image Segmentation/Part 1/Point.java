public class Point {
	double xValue;
	double yValue;
	private int clusterID;
	private int pointID;
	private double distanceFromCentroid;
	
	Point(){
		this.xValue = 0.0;
		this.yValue = 0.0;
		this.clusterID = -1;
	}
	
	Point(int ID, double xVal, double yVal){
		pointID = ID;
		xValue = xVal;
		yValue = yVal;
		clusterID = -1;
	}

	public double getxValue() {
		return xValue;
	}

	public double getyValue() {
		return yValue;
	}

	public int getClusterID() {
		return clusterID;
	}

	public int getPointID() {
		return pointID;
	}

	public double getDistanceFromCentroid() {
		return distanceFromCentroid;
	}

	public void setxValue(double xValue) {
		this.xValue = xValue;
	}

	public void setyValue(double yValue) {
		this.yValue = yValue;
	}

	public void setClusterID(int clusterID) {
		this.clusterID = clusterID;
	}

	public void setPointID(int pointID) {
		this.pointID = pointID;
	}

	public void setPoint(double xVal, double yVal){
		this.setxValue(xVal);
		this.setyValue(yVal);
	}
	
	public void setDistanceFromCentroid(double distanceFromCentroid) {
		this.distanceFromCentroid = distanceFromCentroid;
	}

	@Override
	public String toString() {
		return "Point: \nxValue=" + xValue + ", \nyValue=" + yValue + ", \nclusterID=" + clusterID + ", \npointID=" + pointID
				+ ", \ndistanceFromCentroid=" + distanceFromCentroid + "\n";
	}
	
	public static double calcDistance(Point p1, Point p2){
		return Math.sqrt(Math.pow((p2.getyValue() - p1.getyValue()), 2) + Math.pow((p2.getxValue() - p1.getxValue()), 2));		
	}
	
	
}