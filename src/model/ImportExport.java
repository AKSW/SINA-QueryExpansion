package model;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;
import org.apache.commons.io.FileUtils;
// TODO: tidy up the class a bit
public class ImportExport
{	
	/** Generates a string of the following format:
	 *<pre>key1(tab)resource(tab)value11,value12,...value1m
	 *...
	 *keyn(tab)valuen1,valuen2,...valuenm</pre>
	 *To save it to a file use FileUtils.writeStringToFile(File,String)*/
	public static String multiMapToStringCsvTsv(Map<String,String> keywordToResource , MultiMap<String,String> keywordToLabel)
	{

		StringBuffer sb = new StringBuffer();
		for(String keyword: keywordToLabel.keySet())
		{				
			String labels = keywordToLabel.get(keyword).toString();				
			String resource = "";
			if(keywordToResource!=null&&keywordToResource.containsKey(keyword)) resource=keywordToResource.get(keyword);
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
	
	static Map<String,String> qald2 = new HashMap<String,String>();
	static {
		for(String[] qald2Entry : qald2Array)
		{
			qald2.put(qald2Entry[0],qald2Entry[1]);
		}
	}

	public static void main(String[] args) throws IOException
	{
		MultiMap<String,String> keywordToLabel = new MultiHashMap<>();
		for(String keyword: qald2.keySet())
		{
			String resource = qald2.get(keyword);			
			keywordToLabel.putAll(keyword,CorrelatedResources.correlatedDBpediaResourceLabels(resource));			
		}
		String s = multiMapToStringCsvTsv(qald2,keywordToLabel);
		FileUtils.writeStringToFile(new File("qald.csv"),s);
	}
	

}