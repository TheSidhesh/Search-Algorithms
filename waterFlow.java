import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
 
public class waterFlow
{
	static class Pipe
	{
		String destination;
		int costtodestination,totalofftimes;
		ArrayList<Integer> starttime = new ArrayList<Integer>();
		ArrayList<Integer> endtime = new ArrayList<Integer>();
	}
	
	static class Node
	{
		String label;
		int cost;
	}
	
	static HashMap<String,LinkedList<String>> adjacencyList ;
	static HashMap<String,LinkedList<Pipe>> adjacencyListUCS;
	static LinkedList<String> explored;
	static String[] dest;
	static String finalnode;
	static int currenttime, noofpipes, depth,nopath;
	
	//-----------------------------------------//
	
	static void makeEdge(String src , String destination, String algo)
	{
		if(adjacencyList.containsKey(src)!=true)
		{
			LinkedList<String> srclist = new LinkedList<String>();
			srclist.add(destination);
		    adjacencyList.put(src, srclist);
			
		}
		else
		{
			LinkedList<String> srclist = new LinkedList<String>();
			srclist=adjacencyList.get(src);
		    srclist.add(destination);
		    if(algo.equals("BFS"))
		    {
		    	Collections.sort(srclist);
		    }
		    else
		    {
		    	Comparator<String> cmp = Collections.reverseOrder();
			    Collections.sort(srclist, cmp);
		    }
		    

		}
		   
	}
	//------------------------------------------//
	
	static void makeAdjacencyList(String src , String destination, int costtodest, int totalofftimes, String[] offtimes)
	{
		int i=0;
		LinkedList<Pipe> srclist = new LinkedList<Pipe>();
		int starttime=0,endtime=0;
		String[] words= new String[50];
		Pipe pipe = new Pipe();
		pipe.destination = destination;
		pipe.costtodestination = costtodest;
		pipe.totalofftimes=totalofftimes;
		for(i=0;i<totalofftimes;i++ )
		{
			words=offtimes[i].split("-");
			starttime=Integer.parseInt(words[0]);
			endtime=Integer.parseInt(words[1]);
			pipe.starttime.add(starttime);
			pipe.endtime.add(endtime);
		}
		
		if(adjacencyListUCS.containsKey(src)!=true)
		{	
			srclist.add(pipe);
			adjacencyListUCS.put(src, srclist);
		}
		else
		{
			srclist=adjacencyListUCS.get(src);
		    srclist.add(pipe);
		    Collections.sort(srclist,new Comparator<Pipe>()
		    {
		    	public int compare(Pipe p1, Pipe p2) 
		    	{
		    		if(p1.costtodestination>p2.costtodestination)
		    		{
		                return 1;
		    		} 
		    		else 
		    		{
		                return -1;
		            }
		        }	
		    });
		    
		}
		   
	}
	
	//-----------------------------------------//
	
	static void bfs(String src, String[] dest)
	{
		String current;
		depth=0;
		int timetodepthincrease=1,elementstodepthincrease=0,flag=1;
		Queue<String> frontier = new LinkedList<String>();
		frontier.add(src);
		explored.add(src);
		while(!frontier.isEmpty())
		{
			current = frontier.remove();	
			if(Arrays.asList(dest).contains(current))
			{
				
				finalnode=current;
				currenttime+=depth;
				nopath=1;
				break;
			}
			
			LinkedList<String> currentlist = new LinkedList<String>();
			if(!adjacencyList.containsKey(current))
			{
				flag=0;
			}
			else
			{
				currentlist.addAll(adjacencyList.get(current));
			}
			timetodepthincrease--;
			if(flag==1)
			{
				while( !currentlist.isEmpty())
				{
					 current=currentlist.poll();
					 if(!explored.contains(current))
					 {
						 frontier.add(current);	
						 explored.add(current);
						 elementstodepthincrease++;
					 }
				}
				
			}
			else
				flag=1;	
			if(timetodepthincrease==0)
			{
				depth++;
				timetodepthincrease=elementstodepthincrease;
				elementstodepthincrease=0;
			}
			
		}	
	}
	
	//-----------------------------------------//
	
	static void dfs(String src, String[] dest)
	{
		String current;
		depth=-1;
		Stack<String> frontier = new Stack<String>();
		frontier.push(src);
		while(!frontier.isEmpty())
		{
			current = frontier.peek();
			if(Arrays.asList(dest).contains(current))
			{
				depth++;
				finalnode=current;
				currenttime+=depth;
				nopath=1;
				break;
			}
			if(explored.contains(current))
			{
				
				frontier.pop();
				depth--;
				continue;
			}
			else
				explored.add(current);
			LinkedList<String> currentlist = new LinkedList<String>();
			
			if(!adjacencyList.containsKey(current))
			{
				frontier.pop();
			}
			else
			{
				currentlist.addAll(adjacencyList.get(current));
				depth++;
				while(currentlist.peekFirst()!=null)
				{
					current=currentlist.poll();
					if(!explored.contains(current))
					{
						frontier.push(current);
					}
				}	
				
			}		
		}
	}
	
	//-----------------------------------------//
	static class StringComparator implements Comparator<Node>
	{
	    public int compare(Node x, Node y)
	    {
	     
	    	int flag=0;
	    	flag=x.cost-y.cost;
	    	if(flag==0)
	    	{
	    		if(x.label.compareTo(y.label) < 0)
		    	{
		    		return -1;
		    	}
	    		else
		        {
		            return 1;
		        }
	    	}
	    	
	    	return flag;   
	    }
		
	}
	
	//-----------------------------------------//
	
	static int checkQueue(LinkedList<Node> frontier, String s)
	{
		int i,x=0;
		for(i=0; i<frontier.size();i++)
		{
			x=frontier.get(i).label.compareTo(s);
			if(x==0)
			{
				return i;
			}
				
		}
		return -1;
	}
	
	//-----------------------------------------//
	
	
	static int checkPipeWorking(String src, Pipe currentpipe, int currtime)
	{
		int i=0;
		currtime = currtime % 24;
		
		if(currentpipe.totalofftimes==0)
			return 1;
		for(i=0;i<currentpipe.totalofftimes;i++)
		{
			if(currtime>=currentpipe.starttime.get(i) && currtime<=currentpipe.endtime.get(i))
			{
				return -1;
			}
				
		}	
		return 1;
	}
	
	//-----------------------------------------//
	
	static void ucs(String src, String[] dest)
	{
		Comparator<Node> comparator = new StringComparator();
		LinkedList<Node> frontier = new LinkedList<Node>();
		Node node = new Node();
		int timenow;
		node.label=src;
		node.cost=currenttime;
		frontier.add(node);
		while(!frontier.isEmpty())
		{
			node = frontier.remove();
			timenow=node.cost;
			if(Arrays.asList(dest).contains(node.label))
			{
				nopath=1;
				finalnode=node.label;
				currenttime=node.cost;
				break;
				
			}
			LinkedList<Pipe> currentlist = new LinkedList<Pipe>();
			if(adjacencyListUCS.containsKey(node.label))
			{
				currentlist.addAll(adjacencyListUCS.get(node.label)) ;
				while( !currentlist.isEmpty())
				{
					Pipe currentpipe = new Pipe();
					currentpipe=currentlist.poll();
					int checkpipe = checkPipeWorking(node.label,currentpipe,timenow);
					if(checkpipe==1)
					{
						int index= checkQueue(frontier,currentpipe.destination);
						if(index!=-1)
						{
							Node comparenode = new Node();
							comparenode = frontier.get(index);
							if( comparenode.cost > currentpipe.costtodestination+node.cost)
							{
								Node newnode = new Node();
								frontier.remove(index);
								newnode.label=currentpipe.destination;
								newnode.cost=currentpipe.costtodestination+node.cost;
								frontier.add(newnode);
								frontier.sort(comparator);
							}
						}
						else
						{	
							if(!explored.contains(currentpipe.destination))
							{
								Node newnode = new Node();
								newnode.label=currentpipe.destination;
								newnode.cost= currentpipe.costtodestination+node.cost;
								frontier.add(newnode);
								frontier.sort(comparator);
							
							}
						}
					}		
				}	
			}	
			explored.add(node.label);
		}
	}
	
	//-----------------------------------------//
	
	public static void main(String[] args)
	{
		String line = null;
		int noofcases; 
		int x=0,i=0, j=0, caselinecount=0, costtodest=0,totalofftimes=0;
		String algo=null,src,tempsrc,tempdest;
		ArrayList<String> input;
	
		try 
		{
			File file = new File("output.txt");
			FileReader fileReader = new FileReader(args[1]);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			line = bufferedReader.readLine();
			noofcases = Integer.parseInt(line);
			
			while(i < noofcases)
			{
				adjacencyList = new HashMap<String, LinkedList<String>>();
				adjacencyListUCS = new HashMap<String, LinkedList<Pipe>>();
				input = new ArrayList<String>();
				explored = new LinkedList<String>();
				currenttime = depth = noofpipes = nopath =  0;
				caselinecount=0;
				finalnode=null;
				line = bufferedReader.readLine();
				while(true)
	            {
					if( line==null || (line.equals("")==true &&  caselinecount>6))
    					break;
					input.add(caselinecount, line);	
            	    caselinecount++;
            	    line = bufferedReader.readLine();
            	    
	            }
				i++;
				
				algo = input.get(0);
				src = input.get(1);
				dest = input.get(2).split(" ");
				noofpipes = Integer.parseInt(input.get(4));
				int temp = 5 + noofpipes;
				currenttime = Integer.parseInt(input.get(temp));
				
				if(algo.equals("BFS") || algo.equals("DFS"))
				{
					for(j=5 ; j<noofpipes+5 ; j++)
					{
						String[] words = input.get(j).split(" ");
						tempsrc=words[0];
						tempdest=words[1];
						makeEdge(tempsrc,tempdest,algo);
					}
				}
				else
				{
					for(j=5 ; j<noofpipes+5 ; j++)
					{
						String[] words = input.get(j).split(" ");
						tempsrc=words[0];
						tempdest=words[1];
						costtodest=Integer.parseInt(words[2]);
						totalofftimes=Integer.parseInt(words[3]);
						String[] offtimes= new String[words.length];
						for(x=0;x<totalofftimes;x++)
						{
							offtimes[x]= words[x+4];
						}
						makeAdjacencyList(tempsrc,tempdest,costtodest, totalofftimes, offtimes);
					}
				}
				if(algo.equals("BFS"))
				{
					try
					{
						bfs(src,dest);
					}
					catch(Exception e)
					{
						nopath=0;
					}
				}
				else if(algo.equals("DFS"))
				{
					try
					{
						dfs(src,dest);
					}
					catch(Exception e)
					{
						nopath=0;
					}	
				}
				else
				{
					try
					{
						ucs(src,dest);
					}
					catch(Exception e)
					{
						nopath=0;
					}
				}
				if(nopath==1)
				{
					currenttime = currenttime % 24;
					FileWriter fw = new FileWriter(file,true);
					BufferedWriter bufferedWriter = new BufferedWriter(fw);
					bufferedWriter.write(finalnode + " " + currenttime);
					bufferedWriter.newLine();
					bufferedWriter.flush();
					bufferedWriter.close();
				}
				else
				{
					FileWriter fw = new FileWriter(file,true);
					BufferedWriter bufferedWriter = new BufferedWriter(fw);
					bufferedWriter.write("None");
					bufferedWriter.newLine();
					bufferedWriter.flush();
					bufferedWriter.close();
				}				
			}
			bufferedReader.close();
			
	    }
        catch(FileNotFoundException ex)
		{
            System.out.println("Unable to open file ");                
        }
        catch(IOException ex) 
		{
            System.out.println("Error reading file");                           
        }
	}
}
