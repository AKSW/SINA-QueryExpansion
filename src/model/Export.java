package model;

import org.apache.commons.collections15.MultiMap;

public class Export
{
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