package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import model.FeatureVector.Feature;

import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;
import org.apache.commons.io.FileUtils;
// TODO: tidy up the class a bit
public class ImportExport
{	
	/** Generates a tab separated (tsv) String with keyword, resource, and correlated resource label columns.
	 * Multiple correlated resource labels are separated by commas. 
	 *To save it to a file use FileUtils.writeStringToFile(File,String)*/
	public static String multiMapToStringTsvCsv(MultiMap<String,String> keywordToResource , MultiMap<String,String> keywordToLabel)
	{
		StringBuffer sb = new StringBuffer();
		for(String keyword: keywordToLabel.keySet())
		{				
			String labels = keywordToLabel.get(keyword).toString();				
			String resource = "";
			if(keywordToResource!=null&&keywordToResource.containsKey(keyword))
			{				
				resource = keywordToResource.get(keyword).toString(); 
				resource=resource.substring(1,resource.length()-1).replace(", ",",");
			}
			sb.append(keyword+'\t'+resource+'\t'+labels.substring(1,labels.length()-1).replace(", ",",")+"\n");				
		}
		return sb.substring(0,sb.length()-1);
	}

	static final String[][] qald2Array =
		{
		{"daughter",    "dbo:child"},
		{"married to",  "dbp:spouse"},
		{"die",         ":deathPlace"},
		{"governed",    "dbp:rulingParty"},
		{"tall",        "dbo:height"},
		{"war",         "dbo:battle"},
		{"join the EU", "dbp:accessioneudate"},
		{"when",        "dbo:date"},
		{"mayor",       "dbo:leaderName "},
		{"alive",       "dbo:deathDate"},
		{"die from ",   "dbo:deathCause"},
		{"wife",        "dbo:spouse"},
		{"website",     "foaf:homepage"},
		{"first season","dbo:seasonNumber "},
		{"first name",  "foaf:givenName"},
		{"higher",      "dbp:elevationM"},
		{"created",     "dbo:author"},
		{"spoken",      "dbo:language "},
		{"own",         "dbo:keyPerson"},
		{"written ",    "dbo:author"},
		{"movie",       "dbo:starring"},
		{"wrote",       "dbo:author"},
		{"highest",     "dbo:elevation"},
		{"born",        "dbo:birthPlace"}
		};



	static MultiMap<String,String> qald2 = new MultiHashMap<String,String>();
	static {
		for(String[] qald2Entry : qald2Array)
		{
			qald2.put(qald2Entry[0],qald2Entry[1]);
		}
	}

	/** @see multiMapToStringTsvCsv. */
	public static MultiMap<String,String> readTsvCsvToMultiMap(File f) throws FileNotFoundException
	{
		MultiMap<String,String> keywordToLabel = new MultiHashMap<>();
		try(Scanner in = new Scanner(f))
		{
			while(in.hasNextLine())
			{
				String line = in.nextLine();
				if(line==null) {continue;}				
				line=line.trim();
				if(line.isEmpty()) {continue;}
				String[] tokens = line.split("\t");
				String[] labels = tokens[2].split(",");
				keywordToLabel.putAll(tokens[0],Arrays.asList(labels));
			}
		}
		return keywordToLabel;
	}
	

public static void writeArff(File f, Collection<FeatureVector> vectors) throws IOException
{
	try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f))))
	{
		out.println("@RELATION qald");
		{				
			for(Feature feature: Feature.values())
			{
				out.println("@ATTRIBUTE "+feature+" NUMERIC");				
			}
		}
		out.println("@DATA");
		for(FeatureVector v: vectors)
		{
			out.println(v);
		}
	}
}

/** Calculate the correlated resources for the qald2 data and write them to files.
 * TODO?: Resources are randomly sorted in their processing order and written each to their own file if it doesn't exist yet because the method takes really long.   
 */
public static void main(String[] args) throws IOException, InterruptedException
{
	MultiMap<String,String> keywordToLabel = new MultiHashMap<>();		
	MultiMap<String,String> keywordToResource = new MultiHashMap<>();
	final File outputFile = new File("qald.tsv");
	try(Scanner in = new Scanner(new File("input/qald2012train2.tsv")))
	{
		int i=0;
		while(in.hasNextLine())
		{
			i++;
			String[] tokens = in.nextLine().trim().split("\t");
			String keyword = tokens[0];
			System.out.println("processing keyword "+i+": "+keyword);
			Set<String> resources = new HashSet<>(); 
			for(int j=1;j<tokens.length;j++)
			{
				String resource=tokens[j];
				resources.add(resource);
				try
				{
					Set<String> correlatedResources=CorrelatedResources.correlatedDBpediaResourceLabels(resource);
					keywordToLabel.putAll(keyword,correlatedResources);
				}
				catch(TimeoutException e)
				{
					keywordToLabel.put(keyword,"TIMEOUT");
				}
			}
			keywordToResource.putAll(keyword,resources);
		}
	}
	//		int i=0;
	//		for(String keyword: qald2.keySet())
	//		{
	//			i++;
	//			String resource = qald2.get(keyword);
	//			System.out.println("processing keyword "+i+"/"+qald2.keySet().size()+": "+keyword);
	//			Set<String> resources;
	//			try
	//			{
	//				resources=CorrelatedResources.correlatedDBpediaResourceLabels(resource);
	//				keywordToLabel.putAll(keyword,resources);
	//			}
	//			catch(TimeoutException e)
	//			{
	//				keywordToLabel.put(keyword,"TIMEOUT");
	//			}			
	//		}
	String s = multiMapToStringTsvCsv(keywordToResource,keywordToLabel);
	FileUtils.writeStringToFile(outputFile,s);
}

}