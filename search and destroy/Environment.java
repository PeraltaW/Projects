package probabilistic_hunt;
import java.util.*;
// This class creates the initial environment for the maze
public class Environment {
	int i,j,count,row,col; // Useful variables
	int[][] table; // This is the 2D array that will hold the maze
	double[][] belief; // Belief that our cell has the target
	boolean[][] isTarget; // Whether a cell contains the target or not (not provided to agents)
	Random rand = new Random();  // Invoking random class to get random numbers
	int dim;
	public Environment(int dim) {  // Constructor
		
		
		this.dim = dim;
		
		
		table = new int[dim][dim];  // The maze is supposed to be dimxdim
		belief = new double[dim][dim]; // belief for each dimxdim cells
		isTarget = new boolean[dim][dim]; // true or false value for all cells 
		// Assigning the properties to the 2D arrays:
		for(i=0; i<dim; i++)
		{
			for(j=0; j<dim; j++)
			{
				isTarget[i][j] = false;       // First assign every cell to not be a target
				belief[i][j] = 1.0000000000000000000000000000/(dim*dim);		// Initially, all cells are supposed to have a probability of 1/(dim*dim) to contain the target
				count = rand.nextInt(4);	// pick numbers from {0,1,2,3} uniformly at random
				table[i][j] = count;  		// Assign the terrain type : 0 = flat, 1 = hill, 2 = forest, 3 = cave
			// Each cell will be assigned a terrain with equal probability (0.25) as there are 4 terrains and 
			// and the random numbers from {0,1,2,3} all have 0.25 probability of being any one of them
			}
		}
		// Now assigning a random cell to be a target
		row = rand.nextInt(dim); // get a random row
		col = rand.nextInt(dim); // random column
		isTarget[row][col] = true; // assign it TARGET
		
	}
// Function to complete the search query:
	public double[] search(int row, int col) 
	// takes the row and column indices as parameters,
	// returns an array of 2 doubles : 1: whether or not it has target, 2: probability that its output is true										
	{
		double[] result = new double[2]; // Array of 2 doubles
		result[0] = 0; // Initially set it to false(0)
		result[1] = 1; // with probability(1)
		// If there is no target at the location to be searched,
		if(!isTarget[row][col])  
		{
			// return {0 (no target), 1 (with 100% accuracy)
			// because we do not do false positives
			return result;
		}
		// Otherwise, if the target is there,
		else 
		{
			int n = rand.nextInt(10); // Get a random number from 0 to 9 (inclusive)
			
	/* Basically, what we are doing from here is choosing one or more numbers from 0 to 9
	 * and comparing to what n is. Each number in range has 1/10 chance of being n, If we have 2 numbers, the probability will be 2/10,
	 * if 3, 3/10, and so on. So, we will tell the agent that the location contains the target with a false negative rate provided in the write-up.
	 * But we return the actual probability of the result being true, not the false negative rate: 1 - false negative probability. 	
	 */
			if(table[row][col] == 0) //FLAT
			{
				if(n==0)
				{
					result[0] = 0;
				}
				else
				{
					result[0] = 1;
				}
				
				result[1] = 0.9;
			}
			else if(table[row][col] == 1) //HILL
			{
			
				if(n==1 || n==2 || n==3)
				{
					result[0] = 0;
				}
				else
				{
					result[0] = 1;
				}
				result[1] = 0.7;
			}
			else if(table[row][col] == 2) //FOREST
			{
				if(n==3 || n==4 || n==5 || n==6 || n==7 || n==8 || n==9)
				{
					result[0] = 0;
				}
				else
				{
					result[0] = 1;
				}
				result[1] = 0.3;
			
			}
			else   //CAVE
			{
				if(n == 3)
				{
					result[0] = 1;
				}
				else
				{
					result[0] = 0;
				}
				result[1] = 0.1;
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		int dim = 5;
		Environment maze = new Environment(dim);
		int row = 0 ,col = 0;
		int flat = 0, hilly = 0, forest = 0, cave = 0;
		for(int i=0; i<maze.dim; i++)
		{
			for(int j = 0; j < maze.dim; j++)
			{
				if(maze.isTarget[i][j] == true)
				{
					row = i;
					col =j;
				}
				System.out.print(maze.table[i][j] + "\t");
				if(maze.table[i][j] == 0)
				{
					flat++;
				}
				else if(maze.table[i][j] == 1)
				{
					hilly++;
				}
				else if(maze.table[i][j] == 2)
				{
					forest++;
				}
				else
				{
					cave++;
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		for(int i=0; i<maze.dim; i++)
		{
			for(int j = 0; j < maze.dim; j++)
			{
				System.out.print(maze.belief[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		double[] res = maze.search(3, col);
		System.out.println("Flat = " + flat + ", Hilly = " + hilly + ", Forest = " + forest + ", Cave = " + cave);
		System.out.println("TARGET AT: ( " + row + ", " + col + " )" );
		System.out.println("TARGET TERRAIN: " + maze.table[row][col]);
		System.out.println("Search returned: " + res[0] + " with probability: " + res[1]);
	}

}
