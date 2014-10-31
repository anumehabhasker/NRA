import java.io.*;
import java.util.*;
// create a class to define each object wrt it's attribute values, W and B.
class ObjectSeen
{
	long key;
	double W,B;
	double[] attr = new double[5];
	
	ObjectSeen()
	{
		this.W = 0.0;
		this.B = 0.0;
		int i;
		for(i=0;i<5;i++)
			this.attr[i] = 0.0;
	}
	public void setKey(long k) 
	{
		//sets the value of the object's key
       	        this.key = k;
       	}
       	public long getKey() 
	{
		//sets the value of the object's key
       	        return key;
       	}

	public void addW(double v)
	{
		//updates the value of the object's W
		this.W = this.W + v;
	}
	public void setW(double v)
	{
		//sets the value of the object's W
		this.W = v;
	}
	public void addB(double v)
	{
		//updates the value of the object's B
		this.B = this.B + v;
	}
	public void setB(double v)
	{
		//sets the value of the object's B
		this.B = v;
	}
	public double getW()
	{
		//returns the value of the object's W
		return W;
	}

	public double getB()
	{
		//returns the value of the object's B
		return B;
	}

	public void updateAttr(double v, int i)
	{
		//sets the value of the object's attribute# i
		this.attr[i] = v;
	}

	public double getAttr(int i)
	{
		//returns the value of the object's attribute# i
		return attr[i];
	}


}

public class NRA
{
	// function to perform Quick Sort on an array.
	public static void quick_srt_W(ObjectSeen array[],int low,int n)
	{	
		int lo = low;
		int hi = n;
		double mid = array[(lo + hi) / 2].getW();
		while (lo <= hi) 
		{
			while (array[lo].getW() > mid) 
			{
				lo++;
			}
			while (array[hi].getW() < mid)
			{
				hi--;
  			}
  			if (lo <= hi) 
			{
  				long T = array[lo].getKey();
  				array[lo].setKey(array[hi].getKey());
  				array[hi].setKey(T);
				double W = array[lo].getW();
  				array[lo].setW(array[hi].getW());
  				array[hi].setW(W);
				double B = array[lo].getB();
  				array[lo].setB(array[hi].getB());
  				array[hi].setB(B);
				lo++;
				hi--;
  			}
  		}
		if (low < hi)
  			quick_srt_W(array, low, hi);
 		if(lo < n )
    			quick_srt_W(array, lo, n);
	}
	
	//main function starts here
  	public static void main(String args[])
	{
		//Storing the time the program Starts running to compute Running Time
		long startTime = System.currentTimeMillis();

		//Reading dataset entries from the input file
		String filename = "input",strLine;
		FileInputStream fstream;
		DataInputStream input;
		BufferedReader br;

		//Creating an array of objects of the class ObjectSeen
		ObjectSeen obj[] = new ObjectSeen[160000];
		//Creating another array of objects of the class ObjectSeen to store sorted elements
		ObjectSeen objListY[] = new ObjectSeen[100];

		int i, j, count = 0; //counter for the ObjectSeen obj[]
		int found = 0; // flag to indicate that object matched
		int n, read = 1,k=0; 
		int seen_k = 0,top_k;
		int line = 1;
		double diff;
		int flag;
		int check;
		Scanner in = new Scanner(System.in);

		System.out.println("Please enter the value for k in top_k:");
		top_k = in.nextInt(); //input value for K to find top-K entries.

		// the List Y, containing potential top-k elements at all point of time
		int[] ListY = new int[top_k];
		// a variable to correspond to the index of ListY containing the obj with least val of W
		int minListY = 0;
		// a variable to keep track of the current object updated within the loop to follow
		int index_updated = 0;
		// array to store the min val of all attributes--used in calculation of B
		double[] min_attr_value = new double[5];
		//initializing to 0
		for(i=0;i<5;i++)
			min_attr_value[i] = 0.0;
		//int ln=1;
		//variable to keep track of the maximum value of B in among objects NOT in ListY-- used in algorithm-termination condition.
		double max_B_UnListY = 0.0;
		try
		{
		fstream = new FileInputStream(filename);
		input = new DataInputStream(fstream);
		br = new BufferedReader(new InputStreamReader(input)); //i/p file opened
		
		System.out.println("\nProcessing the input file now..." + "\n");
		System.out.println("\nThis will take some time. Please bear with us!" + "\n");
		//Reading file line by line
		while ((strLine = br.readLine()) != null && read == 1)
		{	//System.out.println("Reading line " + ln);
			//ln++;
			//splitting line elements on tabs; the split gives 5 (key,val) pairs
			String Splitline[] = strLine.split("\t");
			//a loop to look at each Key in the line read
			for(n=0; n<10; n=n+2)
			{
				if(line == 1)
				{
					// for first line, set all min_attr to the first val of an attr seen
					min_attr_value[n/2] = Double.parseDouble(Splitline[n+1]);
					if(n/2 == 4)
						line++;
				}
				else
				{	//Handling for min_attr_val updation on a new line read.
					if(min_attr_value[n/2] > Double.parseDouble(Splitline[n+1]))
					{
						diff = Double.parseDouble(Splitline[n+1]) - min_attr_value[n/2];
						for(i=0;i<count;i++)
						{
							if(obj[i].getAttr(n/2) == 0.0)
								obj[i].addB(diff);
						}
								
						min_attr_value[n/2] = Double.parseDouble(Splitline[n+1]);
					}	
				}

				//Check whether the new Key read already has an object created
				for(i=0;i<count;i++)
				{
					if(obj[i].getKey() == Long.parseLong(Splitline[n]))
					{
						if(Double.parseDouble(Splitline[n+1]) > min_attr_value[n/2])
						{
							diff = 	Double.parseDouble(Splitline[n+1]) - min_attr_value[n/2];
							obj[i].addB(diff);
						}
						obj[i].updateAttr(Double.parseDouble(Splitline[n+1]),n/2);
						obj[i].addW(Double.parseDouble(Splitline[n+1]));
						found = 1;
						index_updated = i;
						break;
					}
				}
				//If object has not yet been created, create it now.
				if(found == 0)
				{
					obj[count] = new ObjectSeen();
					obj[count].setKey(Long.parseLong(Splitline[n]));
					obj[count].updateAttr(Double.parseDouble(Splitline[n+1]),n/2);
					obj[count].addW(Double.parseDouble(Splitline[n+1]));
					obj[count].addB(Double.parseDouble(Splitline[n+1]));
					for(i=0;i<5;i++)
						if(i != n/2)
							obj[count].addB(min_attr_value[i]);
					index_updated = count;
					count++;
				}	//end of if(found == 0)
				else
					found = 0;

				//ListY generation starts here
				// if the objects seen so far are less than topk

				if(count <= top_k && count > seen_k)
				{
					flag = 0;
					//check if index_updated was already in the list
					for(i=0;i<seen_k;i++)
					{
						if(ListY[i] == index_updated)
						{
							flag = 1;
							break;
						}
					}
					//if the index_updated was not already in ListY, add it
					if(flag == 0)
					{
						ListY[seen_k] = index_updated;
								
						if(obj[ListY[minListY]].getW() > obj[index_updated].getW())
							minListY = seen_k;
						else if(obj[ListY[minListY]].getW() == obj[index_updated].getW())
						{
							//update value of minListY based on B in case of tie
							if(obj[ListY[minListY]].getB() > obj[index_updated].getB())
								minListY = seen_k;
						}
						seen_k++;// count of how many objects have been seen so far
					}
				}
				//if the list is full in capacity and a new object is seen
				else
				{
					for(i=0;i<top_k;i++)
						if(i != minListY && obj[ListY[minListY]].getW() > obj[ListY[i]].getW())
							minListY = i;
					for(i=0;i<count;i++)
					{	flag = 0;
						if(obj[i].getW() > obj[ListY[minListY]].getW())
						{
							for(j=0;j<top_k;j++)
								if(ListY[j] == i)
								{
									flag = 1;
									break;
								}
							if(flag == 0)
							{
								ListY[minListY] = i;
								for(j=0;j<top_k;j++)
									if(j != minListY && obj[ListY[minListY]].getW() > obj[ListY[j]].getW())
										minListY = j;
							}
						}
					}
				}	//else ends here
			}	//end of for loop on strLine
			//check for termination condition
			if(count > top_k)
			{
				//check only if ListY is full.
				for(i=0;i<count;i++)
				{	flag = 0;
					for(j=0;j<top_k;j++)
					{
						if(i == ListY[j])
						{
							flag = 1;
							break;
						}
					}
					if(flag == 0)
					{
						//find the max val of B amongst objs not in ListY	
						if(max_B_UnListY < obj[i].getB())
							max_B_UnListY = obj[i].getB();
					}
				}
				//if max value of B among objs not in List Y is less than min val of W in list, it is time to terminate
				if(max_B_UnListY <= obj[ListY[minListY]].getW())
					read = 0;
			}
		}	//end of While loop
		br.close();
		//use a new object to hold all keys in ListY
		for(i=0;i<top_k;i++)
		{
			objListY[i] = new ObjectSeen();
			objListY[i].setKey(obj[ListY[i]].getKey());
			objListY[i].setB(obj[ListY[i]].getB());
			objListY[i].setW(obj[ListY[i]].getW());
		}
		System.out.println("Processing complete..." + "\n" + "Calling sort" + "\n");
		// Sort these on the basis of value of W
		quick_srt_W(objListY,0,top_k-1);

		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		//Write information to file
		System.out.println("Writing the top_k to file output.txt in your local drive");
		FileWriter fw = new FileWriter("output.txt",true);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("Top_k = " + top_k +"\n");
		bw.write("Top_k key-values are:\n");
		for(i=0; i<top_k; i++)
		{
			bw.write("Key-> "+ objListY[i].getKey() + "\t" + " W-> " + objListY[i].getW() + " B-> " + objListY[i].getB() + "\n");
		}
		bw.write("Total Key-value pairs processed -> " + count + "\n");
		bw.write("Total time taken to process -> " + totalTime + " milliseconds\n\n\n");
		bw.close();
		System.out.println("Writing complete");


		}	//end of Try block
		catch (Exception e)
		{       //Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}	//end of Catch block
	} //end of main
} //end of class NRA

