//Ritu Kumari, 2016078
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.lang.Comparable;
import java.lang.reflect.Array;
import java.io.PrintWriter;
import java.io.FileReader;
import java.util.*;
import java.io.*;

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
public class Tweet {
	
	static int n = 20, s = 5;
	static int[] A = new int[n];
	static int[] sample;
	static ArrayList<Integer> sampleList;
	
	public static int[] getNextStream(int n)
	{
		ArrayList<Integer> B = new ArrayList<Integer>();
		for (int i = 0; i < n; i++)
		{
			B.add(i+1);				// creating a data stream of elements from 1 to 20
		}
		Collections.shuffle(B);		// random permutation of the stream is generated
		int[] C = new int[n];
		for (int i = 0; i < n; i++)
		{
			C[i] = B.get(i);
		}
		return C;
	}
	public static void updateSample(String streamItem, int itemNumber)
	{
		if (itemNumber <= s-1)
		{
			sampleList.add(Integer.valueOf(streamItem));
		}
		else {
			Random r = new Random();
			int prob = r.nextInt(itemNumber) + 1;
			if (prob < s)
			{
				sampleList.add(Integer.valueOf(streamItem));
				int index = r.nextInt(s);
				if (index < sampleList.size())
					sampleList.remove(index);
			}
		}
	}
	public static void createSample(int N)
	{
		int[] frequency = new int[n];
		for (int j = 0; j < N; j++)
		{
			for (int i = 0; i < n; i++)
			{
				updateSample(Integer.toString(A[i]), i);
			}
			for (int i = 0; i < s; i++)
			{
				sample[i] = sampleList.get(i);
			}
			for (int i = 0; i < s; i++)
			{
				for (int k = 0; k < n; k++)
				{
					if (sample[i] == A[k])
					{
						frequency[A[k]-1]++;
					}
				}
			}
		}
		System.out.println("N = " + N + " : ");
		for (int i = 0; i < n; i++)
		{
			System.out.println(frequency[i]);
		}
	}
	public static void main(String[] args) throws IOException
	{
		InReader.init(System.in);
		int N1 = 100, N2 = 500, N3 = 1000, N4 = 10000;
		A = getNextStream(n);
		sample = new int[s];
		sampleList = new ArrayList<Integer>();
		createSample(N1);
		createSample(N2);
		createSample(N3);
		createSample(N4);
	}
}
