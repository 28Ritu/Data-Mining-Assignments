//Ritu Kumari 2016078
package mypackage;
import java.io.IOException;

public class KMedians {
	public static void main(String[] args) throws IOException
	{
		InReader.init(System.in);
		System.out.println("...K-Medians Clustering...");
		System.out.print("Enter K: ");
		int K = InReader.nextInt();
		System.out.print("Enter filename: ");
		String filename = InReader.next();
		System.out.print("Enter seed type (1-Random, 2-Provide seed): ");
		int seed = InReader.nextInt();
		if (seed == 1)
		{
			Clustering.intialise(K, filename, 0, seed, null);
		}
		else if (seed == 2)
		{
			float[][] seedReps = new float[K][];
			for (int i = 0; i < K; i++)
			{
				String line = InReader.next();
				String[] parts = line.split(",");
				int no_of_features = parts.length;
				seedReps[i] = new float[no_of_features];
				for (int j = 0; j < no_of_features; j++)
					seedReps[i][j] = Float.parseFloat(parts[j]);
			}
			Clustering.intialise(K, filename, 0, seed, seedReps);
		}
		Clustering.retrieveData();
		Clustering.KMedReps(0);
	}
}
