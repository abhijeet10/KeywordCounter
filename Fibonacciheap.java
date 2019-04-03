
import java.util.*;
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
// Creates the Fibonacci heap
	public class Fibonacciheap {
	private int num_nodes;//Number of Nodes 
	public Node max_node;// Max Node in the heap
    
	 //Insert a new node in the heap
	  public void insert(Node node)
	  {
	      // When the Max Node is not null
	      if (max_node != null) {
	    	  
	          node.left = max_node; //Add new node to the right of max node
	          node.right = max_node.right;
	          max_node.right = node;

	          
	          if ( node.right!=null) {   //to check if the node to the right is not null                            
	              node.right.left = node;
	          }
	          
	        //if node to the right is indeed null
	          if ( node.right==null)
	          {
	              node.right = max_node;
	              max_node.left = node;
	          }
	          
	          //Assigning new Max Node
	          if (node.value > max_node.value) {
	        	  max_node = node;
	          }
	      } else {
	    	  max_node = node;

	      }

	      num_nodes++;
	  }
	// Function to find max node in the heap 
	  void find_max(Node max) 
	  { 
	  System.out.println( "max of heap is: " +  max.value); 
	  } 
	  
	//Cut a node c from another node p and add it to the root list.
	  public void cut(Node c, Node p)
	  {
	      // Adjust c and p pointers to remove c as p's child 
	      c.left.right = c.right;
	      c.right.left = c.left;
	      p.degree--;

	      // reset y.child if necessary
	      if (p.child == c) {
	          p.child = c.right;
	      }
	      //Check for the degree of p
	      if (p.degree == 0) {
	          p.child = null;
	      }

	      // add c to root list of heap
	      c.left = max_node;
	      c.right = max_node.right;
	      max_node.right = c;
	      c.right.left = c;

	      // set parent of x to nil
	      c.parent = null;

	      // set mark to false
	      c.mark = false;
	  }

	
	//Recursive cascade cutting on a given node  
	  public void cascadeCut(Node y)
	  {
	      Node x = y.parent;

	      //if there is a parent
	      if (x != null) {
	          // if y is unmarked, set it marked
	          if (!y.mark) {
	              y.mark = true;
	          } else {
	              // it's marked, cut it from parent
	              cut(y, x);

	              // cut its parent as well
	              cascadeCut(x);
	          }
	      }
	  }
	  
	  
	//Increase the value of 'value' for the given node in heap
	  public void increaseKey(Node x, int k)
	  {
	      if (k < x.value) {
	      }

	      x.value = k;

	      Node y = x.parent;

	      if ((y != null) && (x.value > y.value)) {
	          cut(x, y);
	          cascadeCut(y);
	      }

	      if (x.value > max_node.value) {
	    	  max_node = x;
	      }
	  }
	  
	  
	  
	//Extract the maximum from the heap
	  public Node extractMax()
	  {
	      Node z = max_node;
	      if (z != null) {
	          int num_children = z.degree;
	          Node x = z.child;
	          Node temp;

	          //while there exists child of max
	          while (num_children > 0) {
	        	  temp = x.right;

	              // remove x from child list
	              x.left.right = x.right;
	              x.right.left = x.left;

	              // add x to root list of heap
	              x.left = max_node;
	              x.right = max_node.right;
	              max_node.right = x;
	              x.right.left = x;

	              // set parent to null
	              x.parent = null;
	              x = temp;
	              //decrease no. of children of max
	              num_children--;

	          }

	       // remove z from root list of heap
	          z.left.right = z.right;
	          z.right.left = z.left;

	          if (z == z.right) {
	        	  max_node = null;

	          } else {
	        	  max_node = z.right;
	             consolidate();
	         }
	          num_nodes--;
	         return z;
	     }
	      return null;
	  }
	  
	  
	//Makes y a child of node x, incrementing the degree of x
	  public void makeChild(Node y, Node x)
	  {
	      // remove y from root list of heap
	      y.left.right = y.right;
	      y.right.left = y.left;

	      // make y a child of x
	      y.parent = x;

	      if (x.child == null) {
	          x.child = y;
	          y.right = y;
	          y.left = y;
	      } else {
	          y.left = x.child;
	          y.right = x.child.right;
	          x.child.right = y;
	          y.right.left = y;
	      }

	      // increase degree of x by 1
	      x.degree++;

	      // make mark of y as false
	      y.mark = false;
	  }  
	  
     //Consolidate the heap (Merge two trees if their degrees are same): Reference CLRS 3rd edition-page 515
	 public void consolidate()
	 {
	      //Reference: Section 19.4, page 523,CLRS, Internet sources say 45 is an optimized value
	      int length_DegreeArray =45;

	      List<Node> degreeArray =
	      new ArrayList<Node>(length_DegreeArray);

	      // Initialize degree Array (CLRS, page 516)
	      for (int i = 0; i < length_DegreeArray; i++) {
	          degreeArray.add(null);
	      }
	                  
	      //Number of nodes in the root list of heap H.
	      int num_roots = 0;
	      Node n = max_node;

	      if (n != null) {
	    	  num_roots++;
	          n = n.right;                     

	          while (n != max_node) {
	        	  num_roots++;
	              n = n.right;
	          }
	      }

	      // For each node in root list of Heap H
	      while (num_roots > 0) {

	          int d = n.degree;
	          Node nextNode = n.right;

	          // Merge two nodes with same degree, Add if degree doesn't exist in table.
	          Iterator<Node> iterator = degreeArray.iterator();
	          while (iterator.hasNext()) {
	              Node y = degreeArray.get(d);//another node with same degree as n
	              if (y == null) {
	                  break;
	              }

	              //compare the Key values
	              if (n.value < y.value) {
	                  Node temp = y;
	                  y = n;
	                  n = temp;
	              }

	              //make y the child of n as y.value is now greater than n.value
	              makeChild(y, n);   

	              //set its to null as the degree of x has been incremented
	              degreeArray.set(d, null);
	              d++;
	          }

	          //Keep new n=n+y here
	          degreeArray.set(d, n);

	          // Move towards right in the degreeArray.
	          n = nextNode;
	          num_roots--;
	      }

	    //Set 
	     max_node = null;

	      // combine entries of the degree table
	      for (int i = 0; i < length_DegreeArray; i++) {
	          Node y = degreeArray.get(i);
	          if (y == null) {
	              continue;
	          }

	         //till max node is not null
	         if (max_node != null) {

	           // First remove node from root list.
	            y.left.right = y.right;
	            y.right.left = y.left;

	            // Now add to root list, again.
	            y.left = max_node;
	            y.right = max_node.right;
	            max_node.right = y;
	            y.right.left = y;

	              // Check if this is a new maximum
	              if (y.value > max_node.value) {
	            	  max_node = y;
	              }
	          } else {
	        	  max_node = y;
	          }
	      }
	  }	  
}

