//Ritu Kumari 2016078
package mypackage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.stream.Stream;

class InReader {
    static BufferedReader reader;
    static StringTokenizer tokenizer;
    
    /** call this method to initialize reader for InputStream */
    static void init(InputStream input) {
        reader = new BufferedReader(
                     new InputStreamReader(input) );
        tokenizer = new StringTokenizer("");
    }

    /** get next word */
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                   reader.readLine() );
        }
        return tokenizer.nextToken();
    }
    
    static String nextLine() throws IOException
    {
    	return reader.readLine();
    }
    static int nextInt() throws IOException {
        return Integer.parseInt( next() );
    }
    
    static double nextDouble() throws IOException {
        return Double.parseDouble( next() );
    }
    
    static float nextFloat() throws IOException {
    	return Float.parseFloat( next() );
    }
}
public class KMeans {
	public static void main(String[] args) throws IOException
	{
		InReader.init(System.in);
		System.out.println("...K-Means Clustering...");
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
		Clustering.KMeansReps(0);
	}
}
