package model;

import org.apache.commons.collections15.MultiMap;
import org.apache.commons.io.FileUtils;

public class Export
{	
	/** Generates a string of the following format:
	 *<pre>key1(tab)value11,value12,...value1m
	 *...
	 *keyn(tab)valuen1,valuen2,...valuenm</pre>
	 *To save it to a file use FileUtils.writeStringToFile(File,String)*/
	public static String multiMapToStringCsvTsv(MultiMap<String,String> keywordToLabel)
	{
		
		StringBuffer sb = new StringBuffer();
		for(String keyword: keywordToLabel.keySet())
		{				
			String labels = keywordToLabel.get(keyword).toString();				
			sb.append(keyword+'\t'+labels.substring(1,labels.length()-1).replace(", ",",")+"\n");				
		}
		return sb.substring(0,sb.length()-1);
	}
}