package probabilistic_hunt;

import java.util.Random;

public class Improved {

	
	Environment agent1; 
	int terrain[][];
	double belief[][];
	double probability[][];
	int dim;
	int search; 
	int searched[][];
	int search_needed[][];
	
	public Improved(int dim) {
		agent1 = new Environment(dim);
		probability = new double[dim][dim];
		terrain = agent1.table;
		belief = agent1.belief;
		this.dim = dim;
		search = 0;
		searched = new int[dim][dim];
		search_needed = new int[dim][dim];
		for(int i =0; i<dim; i++)
		{
			for(int j=0; j<dim; j++)
			{
				if(terrain[i][j] == 0)
				{
					search_needed[i][j] = 2;
				}
				else if(terrain[i][j] == 1)
				{
					search_needed[i][j] = 4;
				}
				else if(terrain[i][j] == 2)
				{
					search_needed[i][j] = 8;
				}
				else if(terrain[i][j] == 3)
				{
					search_needed[i][j] = 10;
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
				if(belief[i][j] > max) 
				{
					max = belief[i][j];
				}
					
			}
		}
		for(int i =0; i<dim; i++)
		{
			for(int j =0; j<dim; j++)
			{
				if(belief[i][j] == max && searched[i][j] < search_needed[i][j])
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
					if(belief[i][j] == max && searched[i][j] <
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
	
	public int[] search1(int row,int col)
	{
		System.out.println("Search: ( " + row + " , " + col + " )" );
		searched[row][col]++;
		if(searched[row][col] == search_needed[row][col])
		{
			belief[row][col] = 0;
		}
			
		int coordinates[] = new int[2];
		
	//	System.out.println("Search1 -> Search At: ( " + row + " , " + col + " )");
		double res[] = agent1.search(row, col); 
		search++;
	//	System.out.println("Search1 -> Search Res: ( " + res[0] + " , " + res[1] + " )");
		double temp;
		if(res[0] == 1.0)
		{
			System.out.println("TARGET FOUND AT: ( " + row + " , " + col + " )" + " Searches: " + search);
			coordinates[0] = row;
			coordinates[1] = col;
			return coordinates;
		}
		else
		{
		//	System.out.println("if ");
		//	System.out.println("Belief: " + belief[row][col]);
			if(agent1.table[row][col] == 0)
			{
			//	System.out.println("if 0");
				belief[row][col] *= 0.1;
				
			}
			else if(agent1.table[row][col] == 1)
			{
		//		System.out.println("if 1");
				belief[row][col] *= 0.3;
			}
			else if(agent1.table[row][col] == 2)
			{
		//		System.out.println("if 2");
				belief[row][col] *= 0.7;
			}
			else if(agent1.table[row][col] == 3)
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
		//		System.out.println();
			}
			
			coordinates = find_max_probability(row,col);
			
			
			
			search1(coordinates[0],coordinates[1]);
			
		}

		return coordinates;
	}
	 
	public void solve()
	{
		
		Random rand = new Random();
		int row = rand.nextInt(dim);
		int col = rand.nextInt(dim);
		System.out.println("RANDOM: ( " + row + " , " + col + " )");
		int res[] = search1(row,col);
		
	}
}
