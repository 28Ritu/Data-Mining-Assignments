import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

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
public class NaiveBayes {
	static int no_of_features, no_of_points;
	static float accuracy = 0;
	static double[][][] classifier;
	static TreeMap<String, ArrayList<Integer>> classLabels;
	static String[] classes;
	
	public static void trainNB(double[][] featureMatrix, String[] labels)
	{
		classLabels = new TreeMap<>();
		for (int i = 0; i < labels.length; i++)
		{
			if (!classLabels.containsKey(labels[i]))
			{
				ArrayList<Integer> index = new ArrayList<Integer>();
				index.add(i);
				classLabels.put(labels[i], index);
			}
			else {
				classLabels.get(labels[i]).add(i);
			}
		}
		classes = new String[classLabels.keySet().size()];
		classLabels.keySet().toArray(classes);
		
		classifier = new double[no_of_features][classLabels.size()][2];  // mean & variance
		for (int i = 0; i < no_of_features; i++)
		{
			int k = 0;
			for (Map.Entry<String, ArrayList<Integer>> entry : classLabels.entrySet())
			{
				ArrayList<Integer> index = entry.getValue();
				double mean = 0, variance = 0;
				for (int j = 0; j < index.size(); j++)
				{
					 mean += featureMatrix[index.get(j)][i];
				}
				mean = mean / index.size();
				for (int j = 0; j < index.size(); j++)
				{
					variance += Math.pow((featureMatrix[index.get(j)][i] - mean), 2);
				}
				variance = variance / index.size();
				classifier[i][k][0] = mean;
				if (Double.compare(variance, 0) == 0)
				{
					classifier[i][k][1] = 1;
				}
				else
					classifier[i][k][1] = variance;
				k++;
			}
		}
	}
	
	public static String classifyNB(double[] testPoint)
	{
		double[] posterior = new double[classLabels.size()];
		int maxIndex = 0;
		double max = 0;
		for (int i = 0; i < posterior.length; i++)
		{
			double numerator = (double) 1 / classLabels.size();
			for (int j = 0; j < testPoint.length; j++)
			{
				 numerator *= Math.exp((-1) * Math.pow((testPoint[j] - classifier[j][i][0]), 2)) / Math.sqrt(2 * Math.PI * classifier[j][i][1]);
			}
			posterior[i] = numerator;
			max = Math.max(posterior[i], max);
			if (Double.compare(max, posterior[i]) == 0)
				maxIndex = i;
		}
		return classes[maxIndex];
	}
	
	public static void Partition(List<String> dataset, int t)
	{
		String[] parts;
		double[][] feature = new double[dataset.size()][no_of_features];
		String[] label = new String[dataset.size()];
		for (int i = 0; i < dataset.size(); i++)
		{
			parts = dataset.get(i).split(",");
			int j;
			for (j = 0; j < parts.length - 1; j++)
			{
				feature[i][j] = Double.parseDouble(parts[j]);
			}
			label[i] = parts[j];
		}
		if (t == 0)
			trainNB(feature, label);
		else 
		{
			int correct = 0;
			for (int i = 0; i < feature.length; i++)
			{
				if (classifyNB(feature[i]).equals(label[i]))
				{
					correct += 1;
				}
			}
			accuracy += (float) correct / label.length;
			System.out.println(((float) correct / label.length) * 100 + "%");
			if (t == 1)
			{
				accuracy /= 10;
				System.out.println("Overall Accuracy: " + accuracy * 100 + "%");
			}
		}
	}
	
	public static void Predict(List<String> dataset) throws IOException
	{
		String[] parts;
		double[][] feature = new double[dataset.size()][no_of_features];
		for (int i = 0; i < dataset.size(); i++)
		{
			parts = dataset.get(i).split(",");
			int j;
			for (j = 0; j < parts.length; j++)
			{
				feature[i][j] = Double.parseDouble(parts[j]);
			}
		}
		File outFile = new File("/home/ritu/Downloads/Assignment_3/labels.txt");
		FileWriter fr = new FileWriter(outFile);
		String result;
		int i, d = 0, r = 0;
		for (i = 0; i < feature.length - 1; i++)
		{
			result = classifyNB(feature[i]);
			if (result.equals("D"))
				d++;
			else if (result.equals("R"))
				r++;
			result += "\n";
			fr.write(result);
		}
		result = classifyNB(feature[i]);
		if (result.equals("D"))
			d++;
		else if (result.equals("R"))
			r++;
		fr.write(result);
		fr.close();
		System.out.println("D's = " + d + ", R's = " + r);
	}
	
	public static void main(String[] args) throws IOException 
	{
		InReader.init(System.in);
		System.out.println(".....Naive Bayes Classification.....");
		System.out.print("Enter the filename (training data): ");
		String filename = InReader.next();
		File file = new File(filename);
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line = br.readLine();
		String[] parts = line.split(",");
		no_of_features = parts.length - 1;
		br.close();
		br = new BufferedReader(new FileReader(file));
		no_of_points = (int) br.lines().count();
		br.close();
		br = new BufferedReader(new FileReader(file));
		
		List<String> dataset = new ArrayList<String>();
		while ((line = br.readLine()) != null)
		{
			dataset.add(line);
		}
		
		int ctr = 9, L = 0;
		ArrayList<ArrayList<String>> folds = new ArrayList<>();	
		for (int i = 0; i < 10; i++)
		{
			folds.add(new ArrayList<String>());
			for (int j = 0; j < dataset.size()/10; j++)
			{
				folds.get(i).add(dataset.get(j + L));
			}
			L = dataset.size()/10;
		}

		while (ctr >= 0)
		{
			List<String> training = new ArrayList<String>();
			List<String> test = new ArrayList<String>();
			
			for (int i = 0; i < 10; i++)
			{
				if (i != ctr)
					training.addAll(folds.get(i));
			}
			test.addAll(folds.get(ctr));
			System.out.print("Model " + (10-ctr) + " - ");
			Partition(training, 0);
			Partition(test, ctr+1);
			ctr--;
		}		
		br.close();
		
		System.out.print("Enter the filename (testing data): ");
		filename = InReader.next();
		file = new File(filename);
		br = new BufferedReader(new FileReader(file));
		
		line = br.readLine();
		parts = line.split(",");
		no_of_features = parts.length;
		br.close();
		br = new BufferedReader(new FileReader(file));
		no_of_points = (int) br.lines().count();
		br.close();
		br = new BufferedReader(new FileReader(file));
		
		List<String> dataset2 = new ArrayList<String>();
		while ((line = br.readLine()) != null)
		{
			dataset2.add(line);
		}
		Predict(dataset2);
	}
}
