package probabilistic_hunt;

import java.util.Random;

public class Agent2 {

	int search;
	Environment maze;
	double probability[][];
	int terrain[][];
	int searched[][];
	int search_needed[][];
	double belief[][];
	int dim;
	boolean[][] isSearched;
	int distance_travelled;
	public Agent2(int dim) {
		isSearched = new boolean[dim][dim];
		distance_travelled = 0;
		search_needed = new int[dim][dim];
		maze = new Environment(dim);
		probability = new double[dim][dim];
		terrain = maze.table;
		searched = new int[dim][dim];
		belief = maze.belief;
		search = 0;
		this.dim = dim;
		for(int i =0; i<dim; i++)
		{
			for(int j=0; j<dim; j++)
			{
				if(terrain[i][j] == 0)
				{
					search_needed[i][j] = 2;
					probability[i][j] = 0.9 * belief[i][j];
				}
				else if(terrain[i][j] == 1)
				{
					search_needed[i][j] = 4;
					probability[i][j] = 0.7 * belief[i][j];
				}
				else if(terrain[i][j] == 2)
				{
					search_needed[i][j] = 8;
					probability[i][j] = 0.3 * belief[i][j];
				}
				else if(terrain[i][j] == 3)
				{
					search_needed[i][j] = 10;
					probability[i][j] = 0.1 * belief[i][j];
				}
				
			}
		}
	}
	public int man_dist(int x1, int y1, int x2, int y2)
	{
		int distance;
		distance = Math.abs(x1-x2) + Math.abs(y1-y2);
		return distance;
	}
	
	public int[] find_min_distance(int row, int col, int[] rows, int[] cols)
	{
		int coordinates[] = {rows[0], cols[0]};
		int min = man_dist(row,col,rows[0],cols[0]);
		int count = 0;
		for(int i=1; i<rows.length; i++)
		{
			if(man_dist(row, col, rows[i],cols[i]) < min)
			{
				min = man_dist(row, col, rows[i],cols[i]);
			}
		}
		
		for(int i=0; i<rows.length; i++)
		{
			for(int j=0; j<cols.length; j++)
			{
				if(min == man_dist(row,col,rows[i],cols[i]))
				{
					coordinates[0] = rows[i];
					coordinates[1] = cols[j];
					count++;
				}
			}
		}
		if(count > 1)
		{
			int[] temp_row = new int[count];
		
			int[] temp_col = new int[count];
			count = 0;
			for(int i=0; i<rows.length; i++)
			{
				if(min == man_dist(row,col,rows[i],cols[i]))
				{
					temp_row[count] = rows[i];
					temp_col[count] = cols[i];
					count++;
				}
			
			}
			Random rand = new Random();
			int c = rand.nextInt(count);
			coordinates[0] = temp_row[c];
			coordinates[1] = temp_col[c];
		}
		
		
		return coordinates;
	}
	public int[] find_max_probability(int row,int col)
	{
		int coordinates[] = new int[2];
		int count = 0;
		double max = 0;
		for(int i =0; i<dim; i++)
		{
			for(int j =0; j<dim; j++)
			{
				if(probability[i][j] > max) 
				{
					max = probability[i][j];
				}
					
			}
		}
		for(int i =0; i<dim; i++)
		{
			for(int j =0; j<dim; j++)
			{
				if(probability[i][j] == max && searched[i][j] < search_needed[i][j])
				{
					coordinates[0] = i;
					coordinates[1] = j;
					count++;
				}
					
			}
		}
		if(count>1)
		{
			int[] temp_row = new int[count];
			int[] temp_col = new int[count];
			count = 0;
			for(int i =0; i<dim; i++)
			{
				for(int j =0; j<dim; j++)
				{
					if(probability[i][j] == max && searched[i][j] <
							search_needed[i][j])
					{
						temp_row[count] = i;
						temp_col[count] = j;
						count++;
						
					}
						
				}
			}
			return find_min_distance(row,col,temp_row,temp_col);
		}
		return coordinates;
	}
	
	public int[] search2(int row,int col)
	{
		isSearched[row][col] = true;
		System.out.println("Search: ( " + row + " , " + col + " )" );
		searched[row][col]++;
		if(searched[row][col] == search_needed[row][col])
		{
			probability[row][col] = 0;
		}
			
		int coordinates[] = new int[2];
		
	//	System.out.println("Search1 -> Search At: ( " + row + " , " + col + " )");
		double res[] = maze.search(row, col); 
		search++;
	//	System.out.println("Search1 -> Search Res: ( " + res[0] + " , " + res[1] + " )");
		double temp;
		if(res[0] == 1.0)
		{
			System.out.println("TARGET FOUND AT: ( " + row + " , " + col + " )" + " Searches: " + search);
			System.out.println("TARGET Type: ( " + maze.table[row][col]);
			coordinates[0] = row;
			coordinates[1] = col;
			return coordinates;
		}
		else
		{
		//	System.out.println("if ");
		//	System.out.println("Belief: " + belief[row][col]);
			if(maze.table[row][col] == 0)
			{
			//	System.out.println("if 0");
				belief[row][col] *= 0.1;
				
			}
			else if(maze.table[row][col] == 1)
			{
		//		System.out.println("if 1");
				belief[row][col] *= 0.3;
			}
			else if(maze.table[row][col] == 2)
			{
		//		System.out.println("if 2");
				belief[row][col] *= 0.7;
			}
			else if(maze.table[row][col] == 3)
			{
		//		System.out.println("if 3");
				belief[row][col] *= 0.9;
			}
			
			temp = belief[row][col] + (dim-1)*(dim-1)/dim;
			
			for(int i =0; i<dim; i++)
			{
				for(int j=0; j<dim; j++)
				{
					belief[i][j] = (belief[i][j]) / temp;
				//	System.out.print(belief[i][j] + "\t");
				}
		
			}
			
			if(maze.table[row][col] == 0)
			{
			
				probability[row][col] = 0.9 * belief[row][col];
				
			}
			else if(maze.table[row][col] == 1)
			{
	
				probability[row][col] = 0.7 * belief[row][col];
			}
			else if(maze.table[row][col] == 2)
			{
	
				probability[row][col] = 0.3 * belief[row][col];
			}
			else if(maze.table[row][col] == 3)
			{
		
				probability[row][col] = 0.1 * belief[row][col];
			}
			
			coordinates = find_max_probability(row,col);
			
			
			if(!isSearched[coordinates[0]][coordinates[1]])
			{
				distance_travelled += man_dist(row,col,coordinates[0],coordinates[1]); 
			}
			search2(coordinates[0],coordinates[1]);
			
		}

		return coordinates;
	}
	public void solve()
	{
		
		Random rand = new Random();
		int row = rand.nextInt(dim);
		int col = rand.nextInt(dim);
		System.out.println("RANDOM: ( " + row + " , " + col + " )");
		int res[] = search2(row,col);
		
	}
	public static void main(String[] args)
	{
		int dim = 10;
		Agent2 agent2 = new Agent2(dim);
		agent2.solve();
		System.out.println("Distance Travelled:" + agent2.distance_travelled);
		
	}
}
