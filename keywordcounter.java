import java.io.*;
import java.util.*;
import java.util.regex.*;
 class Node {

	
	    public Node left, right, child, parent;
	    int degree = 0;       
	    boolean mark = false; 
	    public String keyword;
	    public int value;

	    Node(String keyword_new,int value_new)
	    {
	       left = this; right = this; parent = null;
	       degree = 0; keyword = keyword_new; value = value_new;
	    }
	    public  String  getKeyword(){
	        return keyword;
	    }
	}
	
public class keywordcounter {
	static int count =-5;
	long startTime;
	long endTime;
	long time;
	public boolean checkIfExists(Node n)
	{
		//checking if node is not Null and safe to work on
		if(n==null)
			return false;
		return true;
	}
	
	public String getKeyword(String path,int index)throws Exception
	{
		int count=0;
		FileReader filerdr = new FileReader(path);

	    	 BufferedReader reader = new BufferedReader(filerdr);
	          String str = reader.readLine();
			  //returns keyword of a particular index
			  while(!str.equalsIgnoreCase(("stop")))
			  {
				  count++;
				  if(count==index)
				  {
				  String[] inp=str.split(" ");
				  if(inp.length!=2)
				  return inp[0].substring(1);
				  }
			  }
		return "";
	}
	public boolean validateInput(String path)throws Exception
	{
		FileReader filerdr = new FileReader(path);

	    	 BufferedReader reader = new BufferedReader(filerdr);
	          String str = reader.readLine();
			  //validating if input string contains both keyword and value
			  while(!str.equalsIgnoreCase(("stop")))
			  {
				  String[] inp=str.split(" ");
				  if(inp.length!=2)
				  return false;
			  }
		return true;
	}
	public long getTime()
	{
		time=System.currentTimeMillis();
		return time;
	}
	public int getValues(String path,int index)throws Exception
	{
		int count=0;
		FileReader filerdr = new FileReader(path);

	    	 BufferedReader reader = new BufferedReader(filerdr);
	          String str = reader.readLine();
			  //returns value of a particular index
			  while(!str.equalsIgnoreCase(("stop")))
			  {
				  count++;
				  if(count==index)
				  {
				  String[] inp=str.split(" ");
				  if(inp.length!=2)
				  return Integer.parseInt(inp[1]);
				  }
			  }
		return 0;
	}
	public static void main(String[] args) {

		//Timer Initiation :: start time
	      long start_time = System.currentTimeMillis();
	      
	      //Pointer to input & output files
		     File out_file = new File("output.txt");
		     BufferedWriter writer=null;
		     BufferedReader reader=null;
	      
	    //Creating Hash Map for Storing the Keyword as key and value as pointer to node for Fibheap
	     HashMap<String,Node> hmap = new HashMap<>();
	      
	     //Creating object for Max Fibonacci Heap  
	      Fibonacciheap fibheap = new Fibonacciheap();
	      
	     // try block for IOException 
	     try 
	     {
	    	//Identifying the patterns as given in input
	    	 Pattern pat1 = Pattern.compile("([$])([a-z_]+)(\\s)(\\d+)");
	          Pattern pat2 = Pattern.compile("(\\d+)");
			  //"/Users/abhijeet/Desktop/duckDuckGo/input.txt"
	    	 //FileReader filerdr = new FileReader("C:\\Users\\Abhijeet\\eclipse-workspace\\DuckDuckGo\\src\\duckDuckGo\\input.txt");
	    	 FileReader filerdr = new FileReader(args[0]);

	    	 reader = new BufferedReader(filerdr);
	          String str = reader.readLine();//convert str object type
	           
	        
	           
	          FileWriter filewrtr = new FileWriter(out_file);
	          writer = new BufferedWriter(filewrtr);
	           
	          while (!str.equalsIgnoreCase(("stop")) ) 
	         {

	            Matcher m_key = pat1.matcher(str);
	            Matcher m_value = pat2.matcher(str);
	             // If pattern matches  
	            if (m_key.find()) 
	            {
                 //Separate the Keyword to be fed as Node
	            	String keyword = m_key.group(2);
	            	//Separate and parse the integer to be fed as Key for Node
	               int value = Integer.parseInt(m_key.group(4));
	             //Check if the  key is found
	               int checkNode=check(new Node("",0));
	                
	                if ( !hmap.containsKey(keyword))
	                {
	                   //Create a new node 
	                    Node node = new Node(keyword,value);
	                    
	                  //Insert the node in Hash Map
	                     hmap.put(keyword,node);
	                       
	                    //Insert the node in Fibonacci Heap
	                    fibheap.insert(node);
	                      	                   
	                   }
	                   else
	                   {
                           //call increaseKey() in fibonacci heap if keyword already exists in Hashmap
	                        int addvalue = hmap.get(keyword).value + value;
	                        fibheap.increaseKey(hmap.get(keyword),addvalue);
	                   }
	                   
	              }//ends m_key.find()
	               // If a number is found on the input we need to remove the nodes
	             else if (m_value.find()&&(!str.substring(0,1).equals("$"))) 
	             {
	                 //remove the required number of nodes
	                 int rem_number = Integer.parseInt(m_value.group(1));
	                
	                 //Nodes  after removal
	                 List<Node> rem_nodes = new ArrayList<Node>(rem_number);
	                
	                 //Iterating the loop
	                 for ( int num=0;num<rem_number;num++)
	                 {
                        //Node being removed from Fibonacci heap
	                    Node node = fibheap.extractMax();

	                    //Nodes being removed from Hashmap and creating new node for insertion
	                    hmap.remove(node.getKeyword());
	                    Node new_node= new Node(node.getKeyword(),node.value);

	                    //add new node into the removed nodes list
	                    rem_nodes.add(new_node);
	                    
	                    //Add the nodes until last Keyword is written
	                    if ( num <rem_number-1)
	                    {
	                        writer.write(node.getKeyword() + ",");
   
	                    }
	                    else
	                    {
	                        writer.write(node.getKeyword());
	                    }
	                  } //end of for loop
	                  
	                 //insertion step
	                for ( Node insert_node: rem_nodes)
	                {
                         fibheap.insert(insert_node);
	                     hmap.put(insert_node.getKeyword(),insert_node);
	                 }

	             // new line being written in output file
	             writer.newLine();
	             
	            } //end of else if
	          
	           // next line in input files
	           str = reader.readLine();   
	         
	         } 
	         
	         fibheap.find_max(fibheap.max_node);
	         
	         //end of while loop
		 
	         writer.close();   
	     }//end of try block
	     
	     catch(Exception e){
	        e.printStackTrace();
	     }
	     
	   //Close the writer
	     finally {
	         if ( writer != null ) {
	             try {
	                 writer.close();
	             } catch (IOException ioe2) {

	             }
	         }
	     }
	     // Run time calculation
	     long end_time   = System.currentTimeMillis();
	     long run_time = end_time - start_time;
	     System.out.println(" Run Time in Milliseconds: "+ run_time);  
	   
	}//end of main

	public static int check(Node node)
	{
		while(count!=0)
		{
			node=new Node("",0);
			count++;
		}
		return count;
	}
}// end of class keywordcounter