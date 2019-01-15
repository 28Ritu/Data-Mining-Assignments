//Ritu Kumari 2016078
package mypackage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Clustering {
	
	static int K, dim, no_features, no_of_points, iteration, seed_type;
	static String filename;
	static float[][] dataset, reps, seedR;
	static ArrayList<float[]>[] clusters;
	
	public static void intialise(int no_clusters, String file, int dimension, int s, float[][] seedReps) throws IOException
	{
		K = no_clusters;
		dim = dimension;
		iteration = 0;
		filename = file;
		seed_type = s;
		seedR = seedReps;
		clusters = new ArrayList[K];
		for (int i = 0; i < K; i++)
			clusters[i] = new ArrayList<float[]>();
	}
	
	public static void retrieveData() throws IOException
	{
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line = br.readLine();
		String[] parts = line.split(",");
		no_features = parts.length;
		br.close();
		br = new BufferedReader(new FileReader(file));
		
		no_of_points = (int) br.lines().count();
		dataset = new float[no_of_points][no_features];
		
		br.close();
		br = new BufferedReader(new FileReader(file));

		int i = 0;
		while ((line = br.readLine()) != null)
		{
			parts = line.split(",");
			for (int j = 0; j < parts.length; j++)
			{
				dataset[i][j] = Float.parseFloat(parts[j]);
			}
			i++;
		}
		reps = new float[K][no_features];
	}
	
	public static void FirstRepsRandom()
	{
		float[][] min_max = new float[2][no_features];
		float min, max;
		for (int i = 0; i < no_features; i++)
		{
			min = dataset[0][i];
			max = dataset[0][i];
			for (int j = 0; j < no_of_points; j++)
			{
				if ((Float.compare(dataset[j][i], min) == -1) || (Float.compare(dataset[j][i], min) == 0))
				{
					min = dataset[j][i];
				}
				if ((Float.compare(dataset[j][i], max) == 1) || (Float.compare(dataset[j][i], max) == 0))
				{
					max = dataset[j][i];
				}
			}
			min_max[0][i] = min;
			min_max[1][i] = max;
		}
		Random random = new Random();
		for (int i = 0; i < K; i++)
		{
			System.out.print("Reps "+ i + " : ");
			for (int j = 0; j < no_features; j++)
			{
				float r = min_max[0][j] + (random.nextFloat() * (min_max[1][j] - min_max[0][j]));
				reps[i][j] = r;
				System.out.print(reps[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static void FirstRepsSeed()
	{
		reps = seedR;
	}
	
	public static void KMeansReps(int iterator)
	{
		if (iterator == 0)
		{
			if (seed_type == 1)
				FirstRepsRandom();
			else
				FirstRepsSeed();
			KMeansFormClusters();
		}
		else 
		{
			float[][] new_reps = new float[K][no_features];
			for (int i = 0; i < K; i++)
			{
				for (int j = 0; j < no_features; j++)
				{
					float avg = 0;
					for (int k = 0; k < clusters[i].size(); k++)
					{
						avg = avg + clusters[i].get(k)[j];
					}
					if (clusters[i].size() > 0)
						avg = avg/clusters[i].size();
					new_reps[i][j] = avg;
				}
			}
			boolean stable = true;
			for (int i = 0; i < K; i++)
			{
				System.out.print("New Reps "+ i + " : ");
				for (int j = 0; j < no_features; j++)
				{
					System.out.print(new_reps[i][j] + " ");
					if (reps[i][j] != new_reps[i][j])
						stable = false;
				}
				System.out.println();
			}
			if (stable == true)
				return;
			for (int i = 0; i < K; i++)
			{
				clusters[i].clear();
				for (int j = 0; j < no_features; j++)
				{
					reps[i][j] = new_reps[i][j];
				}
			}
			KMeansFormClusters();
		}
	}
	
	public static void KMeansFormClusters()
	{
		float[] distance = new float[K];
		for (int i = 0; i < no_of_points; i++)
		{
			for (int j = 0; j < K; j++)
			{
				float dist = EuclideanDistance(dataset[i], reps[j]);
				distance[j] = dist;
			}
			float min = distance[0];
			int ci = 0;
			for (int j = 0; j < K; j++)
			{
				if ((Float.compare(distance[j], min) == -1) || (Float.compare(distance[j], min) == 0))
				{
					min = distance[j];
					ci = j;
				}
			}
			clusters[ci].add(dataset[i]);
		}
		for (int i = 0; i < K; i++)
		{
			System.out.println("Cluster " + i + " : ");
			for (int j = 0; j < clusters[i].size(); j++)
			{
				for (int k = 0; k < no_features; k++)
				{
					System.out.print(clusters[i].get(j)[k] + " ");
				}
				System.out.println();
			}
		}
		KMeansReps(iteration + 1);
	}
	
	public static float EuclideanDistance(float[] datapoint, float[] reps)
	{
		double sum = 0;
		float dist = 0;
		for (int i = 0; i < no_features; i++)
		{
			sum = sum + (datapoint[i] - reps[i]) * (datapoint[i] - reps[i]);
		}
		dist = (float) Math.sqrt(sum);
		return dist;
	}
	
	public static void KMedReps(int iterator)
	{
		if (iterator == 0)
		{
			if (seed_type == 1)
				FirstRepsRandom();
			else
				FirstRepsSeed();
			KMedFormClusters();
		}
		else
		{
			float[][] new_reps = new float[K][no_features];
			for (int i = 0; i < K; i++)
			{
				for (int j = 0; j < no_features; j++)
				{
					ArrayList<Float> medians = new ArrayList<Float>();
					for (int k = 0; k < clusters[i].size(); k++)
					{
						medians.add(clusters[i].get(k)[j]);
					}
					Collections.sort(medians);
					float median = 0;
					if (medians.size() > 0)
					{
						if ((medians.size() % 2) != 0)
							median = medians.get(((medians.size() - 1) / 2));
						else
							median = (medians.get((medians.size() / 2)) + medians.get((medians.size() / 2) - 1)) / 2;
					}
					new_reps[i][j] = median;
				}
			}
			boolean stable = true;
			for (int i = 0; i < K; i++)
			{
				System.out.print("New Reps "+ i + " : ");
				for (int j = 0; j < no_features; j++)
				{
					System.out.print(new_reps[i][j] + " ");
					if (reps[i][j] != new_reps[i][j])
						stable = false;
				}
				System.out.println();
			}
			if (stable == true)
				return;
			for (int i = 0; i < K; i++)
			{
				clusters[i].clear();
				for (int j = 0; j < no_features; j++)
				{
					reps[i][j] = new_reps[i][j];
				}
			}
			KMedFormClusters();
		}
	}
	
	public static void KMedFormClusters()
	{
		float[] distance = new float[K];
		for (int i = 0; i < no_of_points; i++)
		{
			for (int j = 0; j < K; j++)
			{
				float dist = ManhattanDistance(dataset[i], reps[j]);
				distance[j] = dist;
			}
			float min = distance[0];
			int ci = 0;
			for (int j = 0; j < K; j++)
			{
				if ((Float.compare(distance[j], min) == -1) || (Float.compare(distance[j], min) == 0))
				{
					min = distance[j];
					ci = j;
				}
			}
			clusters[ci].add(dataset[i]);
		}
		for (int i = 0; i < K; i++)
		{
			System.out.println("Cluster " + i + " : ");
			for (int j = 0; j < clusters[i].size(); j++)
			{
				for (int k = 0; k < no_features; k++)
				{
					System.out.print(clusters[i].get(j)[k] + " ");
				}
				System.out.println();
			}
		}
		KMedReps(iteration + 1);
	}
	
	public static float ManhattanDistance(float[] datapoint, float[] reps)
	{
		float dist = 0;
		for (int i = 0; i < no_features; i++)
		{
			dist = dist + Math.abs(datapoint[i] - reps[i]);
		}
		return dist;
	}
}
