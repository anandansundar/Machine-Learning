import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Scanner;

public final class Driver {

	static int datacount;
	static int trainingdataset,trainingattrcount;
	static int testdataset,testattrcount;
	static int valdataset, valattrcount;
	static int attrLength=0;
	static ArrayList<String[]> trainingSet = new ArrayList<>();
	static ArrayList<String[]> testSet = new ArrayList<>();
	static ArrayList<String[]> valSet = new ArrayList<>();
	static LinkedHashMap<Integer, String> dtree = new LinkedHashMap<>();
	static String[] test_attributes=null;
	static String[] val_attributes=null;
	static String trainingset_path;
	static String testset_path;
	static String validationset_path;

	public static void main(String args[]){
		
		Scanner in=new Scanner(System.in);
		int prunecount = 0;

		System.out.println("Enter the no of nodes to be pruned: ");
		prunecount = Integer.parseInt(args[0]);
		/*prunecount = in.nextInt();*/

		System.out.println("Enter the Decision Tree You Want to use ");
		System.out.println("1. ID3 ");
		System.out.println("2. Random Selection ");
		int algo_select = Integer.parseInt(args[1]);

		/*int algo_select = in.nextInt();*/

		trainingset_path = args[2];
		validationset_path = args[3];
		testset_path = args[4];

		/*System.out.println("Path for training set data");
		trainingset_path=in.next();
		System.out.println("Path for test set data");
		testset_path=in.next();
		System.out.println("Path for validation set data");
		validationset_path=in.next();
		 */

		trainingSet=readTrainingset(trainingset_path);
		datacount=getDatacount();

		if (algo_select == 1 || algo_select == 2){

			System.out.println("Do you need to print the tree? (Enter 0 or 1)");
			int print = Integer.parseInt(args[5]);
			/*int print = in.nextInt();*/
			dtree = Info_Gain(prunecount, algo_select);
			int no_of_nodes = dtree.size();
			System.out.println("No of nodes in the tree: " +no_of_nodes);
			if (print == 1 )
				print_tree(dtree,0,0);

		}

		else
			System.out.println("You have given a wrong value for algorithm selection ");

		in.close();

	}

	public static LinkedHashMap<Integer, String> Info_Gain(int prunecount, int algo_select){

		ID3Tree id3gain=new ID3Tree();
		RandomSelectedTree randomselect = new RandomSelectedTree();
		int traverse=0;
		String[] attributes=null;
		LinkedHashMap<Integer, String> dtree = new LinkedHashMap<Integer, String>();

		if (algo_select == 1){
			dtree=id3gain.cal_Info_Gain(trainingSet,datacount,"Class",dtree,traverse,"root", 0);
			attributes=id3gain.getAttributes();
		}

		else {
			dtree=randomselect.DesignRandomTree(trainingSet,datacount,"Class",dtree,traverse,"root", 0);		
			attributes=randomselect.getAttributes();
		}

		attrLength=attributes.length;
		int count=1;
		Iterator<Integer> iterator = dtree.keySet().iterator() ;

		while(iterator.hasNext()){

			Integer key= iterator.next();

			if((dtree.get(key).equals("0") || dtree.get(key).equals("1") || dtree.get(key).equals("-1"))){
				iterator = dtree.keySet().iterator();
				for(int gg=0;gg<count;gg++){
					iterator.next();
				}
				count++;
				continue;
			}

			if (algo_select == 1){
				id3gain=new ID3Tree();
				dtree=id3gain.cal_Info_Gain(trainingSet,datacount,"Class",dtree,key,"1", 0);
				id3gain=new ID3Tree();
				dtree=id3gain.cal_Info_Gain(trainingSet,datacount,"Class",dtree,key,"0", prunecount);
			}

			else {
				randomselect = new RandomSelectedTree();
				dtree=randomselect.DesignRandomTree(trainingSet,datacount,"Class",dtree,key,"1", 0);	
				randomselect = new RandomSelectedTree();
				dtree=randomselect.DesignRandomTree(trainingSet,datacount,"Class",dtree,key,"0", prunecount);
			}

			iterator = dtree.keySet().iterator() ;

			for(int i=0;i<count;i++){
				traverse=iterator.next();
			}

			count++;

		}


		float test_val=0, validation_val = 0;
		/*System.out.println("Tree derived using ID3 with Training Set : ");
		System.out.println("D-Tree: " +dtree);
		 */

		validation_val = cal_accuracy_val(validationset_path, dtree);
		System.out.println("Accuracy using validation_set "+ validation_val);

		test_val=cal_accuracy_test(testset_path, dtree);
		System.out.println("Accuracy using test_set " + test_val);

		return dtree;

	}


	static int leaf_count=0;
	public static void print_tree(LinkedHashMap<Integer, String> dtree,int visited,int gap)
	{
		for(int i =0;i<gap;i++)
		{
			System.out.print("|\t");
		}

		if(dtree.get(visited)== "0")
			System.out.println(dtree.get(visited)+" : 0");
		else
			System.out.println(dtree.get(visited)+" : 1");

		if(dtree.get((visited*2)+1) != null)
		{
			gap++;
			print_tree(dtree,((visited*2)+1),gap);
			gap--;
		}
		else
		{
			gap--;
			return;
		}
		for(int i =0;i<gap;i++)
		{
			System.out.print("|\t");
		}

		if(dtree.get(visited)== "1")
			System.out.println(dtree.get(visited)+" : 1");
		else
			System.out.println(dtree.get(visited)+" : 0");

		if(dtree.get((visited*2)+2) != null)
		{
			gap++;
			print_tree(dtree,((visited*2)+2),gap);
			gap--;

		}
		else
		{
			gap--;
			return;
		}
	}

	public static float cal_accuracy_test(String path, LinkedHashMap<Integer, String> dtree1){
		float val=readTestSet(path,dtree1);
		return val;
	}

	public static float cal_accuracy_val(String path, LinkedHashMap<Integer, String> dtree1){
		float val=readValSet(path,dtree1);
		return val;
	}

	public static ArrayList<String[]> readTrainingset(String path){

		BufferedReader br = null;
		String str = "";
		String delimiter = ",";
		String[] attr=null;

		try{
			br = new BufferedReader(new FileReader(path));
			while ((str = br.readLine()) != null) {
				attr = str.split(delimiter);
				trainingattrcount=attr.length;		
				trainingSet.add(trainingdataset,attr);		
				trainingdataset++;		
			}

		}
		catch(Exception e){

		}
		return trainingSet;
	}


	public static float readTestSet(String path, LinkedHashMap<Integer, String> dtree1){

		BufferedReader br = null;
		String str = "";
		String delimiter = ",";
		String[] attr=null;

		try{
			br = new BufferedReader(new FileReader(path));
			while ((str = br.readLine()) != null) {
				attr = str.split(delimiter);
				testattrcount=attr.length;		
				testSet.add(testdataset,attr);		
				testdataset++;		
			}

		}
		catch(Exception e){

		}
		float val=0;
		val=test_traverse_tree(dtree1);
		return val;
	}

	public static float readValSet(String path, LinkedHashMap<Integer, String> dtree1){

		BufferedReader br = null;
		String str = "";
		String delimiter = ",";
		String[] attr=null;

		try{
			br = new BufferedReader(new FileReader(path));
			while ((str = br.readLine()) != null) {
				attr = str.split(delimiter);
				valattrcount=attr.length;		
				valSet.add(valdataset,attr);		
				valdataset++;		
			}

		}
		catch(Exception e){

		}
		float val=0;
		val=val_traverse_tree(dtree1);
		return val;
	}

	public static float test_traverse_tree(LinkedHashMap<Integer, String> dtree){

		String root;
		int index=0,win=0;
		String[] row=new String[testattrcount];
		for(int i=1;i<testdataset;i++){
			index=0;
			row=testSet.get(i);
			while(dtree.get(index)!= null && !dtree.get(index).equals("1") && !dtree.get(index).equals("0") && !dtree.get(index).equals("-1")){
				root=dtree.get(index);
				//System.out.println("root : " + root);
				if(row[getTestAttributeIndex(root)].equals("1")){
					index=(index*2)+1;
				}
				else if(row[getTestAttributeIndex(root)].equals("0")){
					index=(index*2)+2;
				}						
			}
			if(dtree.get(index).equals(row[testattrcount-1])){
				win++;
			}
		}	
		float accuracy = (float) (win*100)/(testdataset-1);
		return accuracy;
	}

	public static int getTestAttributeIndex(String chooseAttribute){
		int attributeindex = 0;
		test_attributes=testSet.get(0);
		testattrcount=test_attributes.length;
		for(int i=0;i<testattrcount;i++){

			if(test_attributes[i].equalsIgnoreCase(chooseAttribute)){
				break;
			}
			else{
				attributeindex++;
			}
		}
		return attributeindex;
	}

	public static float val_traverse_tree(LinkedHashMap<Integer, String> dtree){

		String root;
		int index=0,win=0;
		String[] row=new String[valattrcount];
		for(int i=1;i<valdataset;i++){
			index=0;
			row=valSet.get(i);
			while(dtree.get(index)!= null && !dtree.get(index).equals("1") && !dtree.get(index).equals("0") && !dtree.get(index).equals("-1")){
				root=dtree.get(index);
				if(row[getValAttributeIndex(root)].equals("1")){
					index=(index*2)+1;
				}
				else if(row[getValAttributeIndex(root)].equals("0")){
					index=(index*2)+2;
				}						
			}
			if(dtree.get(index).equals(row[valattrcount-1])){
				win++;
			}
		}	
		float accuracy = (float) (win*100)/(valdataset-1);
		return accuracy;
	}

	public static int getValAttributeIndex(String chooseAttribute){
		int attributeindex = 0;
		val_attributes=valSet.get(0);
		valattrcount=val_attributes.length;
		for(int i=0;i<valattrcount;i++){

			if(val_attributes[i].equalsIgnoreCase(chooseAttribute)){
				break;
			}
			else{
				attributeindex++;
			}
		}
		return attributeindex;
	}

	public static int getDatacount(){
		return trainingdataset;
	}

}