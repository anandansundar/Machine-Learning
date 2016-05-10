import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public final class RandomSelectedTree {

	ArrayList<String[]> trainingSet = new ArrayList<>();

	LinkedHashMap<Integer, String> random_dtree=new LinkedHashMap<Integer, String>();
	String[] attributeList=null;
	String[] attributes=null;
	String[] attributeValues=null;
	ArrayList<String> parents=new ArrayList<String>();
	ArrayList<String> parentsIndex=new ArrayList<String>();

	boolean selected=true;
	int dataset=0;
	static int attrcount=0;

	@SuppressWarnings("unused")
	public LinkedHashMap<Integer, String> DesignRandomTree(ArrayList<String[]> tset,int lines,String class1,LinkedHashMap<Integer, String> dtree1,Integer traverse,String criteria, int prunecount){
		float ones=0,zeros=0,totalEntropy=0,zEntropy=0,oEntropy=0,z1 = 0,z2 = 0,o1 = 0,o2 = 0;
		String opt=null,attrVal=null;

		String[] row=null;

		random_dtree=dtree1;
		int index=0;
		trainingSet=tset;
		dataset=lines;
		String tgtAttr;
		int tgtattrIndex = 0;

		if(random_dtree.size()>2){
			int i=traverse;
			while(i>0){
				parents.add(random_dtree.get(i));
				parentsIndex.add(String.valueOf(i));
				i=((i-1)/2);
			}

			if(i==0){
				parents.add(random_dtree.get(i));
				parentsIndex.add(String.valueOf(i));
			}
		}

		if(random_dtree.size()!=0){
			tgtAttr=random_dtree.get(traverse);
			tgtattrIndex=get_attr_Index(tgtAttr);
		}

		String targetattr=null;
		int attributeindex=get_attr_Index(class1);
		float dValues[]=new float[attrcount];

		for(int i=0;i<dataset;i++){		
			row=trainingSet.get(i);
			opt=row[attributeindex];
			if(random_dtree.size()!=0){
				targetattr=row[tgtattrIndex];
			}
			if(criteria.equals("root") || criteria.equals("1") && is_example_Selected("1",i) && targetattr.equals("1")|| criteria.equals("0") && targetattr.equals("0") && is_example_Selected("0",i)){
				if(opt.equalsIgnoreCase("1")){
					ones++;
				}
				else if(opt.equalsIgnoreCase("0")){
					zeros++;
				}
			}
		}
		int leaf=-1;
		boolean isleaf=false;
		int leftprunecount, rightprunecount;
		leftprunecount = prunecount / 2;
		rightprunecount = (prunecount + 1)/2;

		if(ones==0 && zeros==0){
			isleaf=true;
		}

		else if(ones<=leftprunecount){
			leaf=0;
			isleaf=true;
			leftprunecount = 0;
		}

		else if(zeros<=rightprunecount){
			leaf=1;
			isleaf=true;
			rightprunecount = 0;
		}

		totalEntropy= new Random().nextFloat();

		if(isleaf==false){
			//choosing attribute
			for(int i=0;i<attrcount;i++){
				z1=0;o1=0;z2=0;o2=0;
				int counter=0;
				if(i!=attributeindex){
					for(int k=0;k<parents.size();k++){
						if(parents.get(k).equals(attributes[i])){
							counter++;
						}
					}

					if(counter==0){
						for(int j=0;j<dataset;j++){			
							row=trainingSet.get(j);
							attrVal=row[i];
							opt=row[attributeindex];
							targetattr=row[tgtattrIndex];
							if(criteria.equals("root") || criteria.equals("1") && is_example_Selected("1",j) && targetattr.equals("1")|| criteria.equals("0") && targetattr.equals("0") && is_example_Selected("0",j)){
								if(attrVal.equalsIgnoreCase("1")){
									if(opt.equalsIgnoreCase("0")){
										z1++;
									}
									else if(opt.equalsIgnoreCase("1")){
										o1++;
									}
								}
								else if(attrVal.equalsIgnoreCase("0")){
									if(opt.equalsIgnoreCase("0")){
										z2++;
									}
									else if(opt.equalsIgnoreCase("1")){
										o2++;
									}
								}
							}
						}

					}
					if(ones+zeros!=0){
						if(counter==0){
							dValues[index]= new Random().nextInt(100);
							index++;
						}
						else{
							dValues[index]=-1;
							index++;
						}
					}

				}}
		}
		if(criteria.equals("root")){
			if(isleaf){
				if(leaf==1)
					random_dtree.put(traverse, "1");
				else if(leaf==0)
					random_dtree.put(traverse, "0");
				else if(leaf==-1)
					random_dtree.put(traverse, "-1");
			}
			else{
				random_dtree.put(traverse, attributes[find_Root(dValues)]);
			}
		}
		else if(criteria.equals("0")){
			if(isleaf){
				if(leaf==1)
					random_dtree.put((traverse*2)+2, "1");
				else if(leaf==0)
					random_dtree.put((traverse*2)+2, "0");
				else if(leaf==-1)
					random_dtree.put((traverse*2)+2, "-1");
			}
			else{
				if(is_attr_Selected(attributes[find_Root(dValues)],traverse)){

					if(find_Root(dValues)==-1){
						random_dtree.put((traverse*2)+2, "-1");
					}
					else{
						random_dtree.put((traverse*2)+2, attributes[find_Root(dValues)]);
					}
				}
			}
		}
		else if(criteria.equals("1")){
			if(isleaf){
				if(leaf==1)
					random_dtree.put((traverse*2)+1, "1");
				else if(leaf==0)
					random_dtree.put((traverse*2)+1, "0");
				else if(leaf==-1)
					random_dtree.put((traverse*2)+1, "-1");
			}
			else{

				if(is_attr_Selected(attributes[find_Root(dValues)],traverse))
					if(find_Root(dValues)==-1){
						random_dtree.put((traverse*2)+1, "-1");
					}
					else{
						random_dtree.put((traverse*2)+1, attributes[find_Root(dValues)]);
					}
			}
		}

		return random_dtree;
	}



	public boolean is_example_Selected(String criteria,int index){
		boolean sel=true;
		String[] val=new String[attrcount];
		String[] row=null;
		int count=0;
		int j=0;
		row=trainingSet.get(index);
		if(parents.size()>0){

			val[0]=row[get_attr_Index(parents.get(j))];
			if(val[0].equals(criteria)){
				count++;
			}
		}
		for(j=1;j<parents.size();j++){
			val[j]=row[get_attr_Index(parents.get(j))];
			int vall=Integer.parseInt(parentsIndex.get(j-1))%2;
			String svall=String.valueOf(vall);
			if(val[j].equals(svall)){
				count++;
			}
		}
		if(count!=parents.size()){
			return false;
		}
		return sel;
	}

	public static int find_Root(float[] infoGain){
		float winAttr=infoGain[0];
		int winIndex=0,leave=0;;
		for(int i=1;i<attrcount;i++){
			if(infoGain[i]==-1){
				leave++;
			}
			if(infoGain[i]>winAttr){
				winAttr=infoGain[i];
				winIndex=i;
			}
		}
		if(leave>=infoGain.length){
			winIndex=-1;
		}
		return winIndex;
	}

	public boolean is_attr_Selected(String attribute,int traverse){
		int i=traverse;

		while(i>0){

			if(attribute.equals(random_dtree.get(i))){
				selected=false;
			}
			i=(i-1)/2;
		}
		if(attribute.equals(random_dtree.get(0))){
			selected=false;
		}


		return selected;

	}

	public boolean get_attr_Selected(){
		return selected;
	}
	public int get_attr_Index(String chooseAttribute){
		int attributeindex = 0;

		attributes=trainingSet.get(0);
		attrcount=attributes.length;
		for(int i=0;i<attrcount;i++){

			if(attributes[i].equalsIgnoreCase(chooseAttribute)){
				break;
			}
			else{
				attributeindex++;
			}
		}
		return attributeindex;
	}

	public String[] getAttributes(){
		return attributes;
	}

}