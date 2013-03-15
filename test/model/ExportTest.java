package model;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import org.apache.commons.collections15.multimap.MultiHashMap;
import org.junit.Test;

public class ExportTest
{

	@Test
	public void testMultiMapToStringCsvTsv()
	{
		MultiHashMap<String,String> map = new MultiHashMap<String,String>();
		map.putAll("word",Arrays.asList(new String[]{"schmörd","börd"}));
		map.putAll("wort",Arrays.asList(new String[]{"schmört","bört"}));
				
		String s = Export.multiMapToStringCsvTsv(map);
		String t = "word	schmörd,börd\nwort	schmört,bört";	
		assertTrue(s.equals(t));
	}

}
