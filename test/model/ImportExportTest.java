package model;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.TreeSet;

import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;
import org.junit.Test;

public class ImportExportTest
{
	@Test
	public void testReadTsvCsvToMultiMap() throws FileNotFoundException
	{
		MultiMap<String,String> map = ImportExport.readTsvCsvToMultiMap(new File("output/qald.tsv"));
		assertTrue(map.keySet().size()>10);
		assertTrue(new TreeSet<String>(map.get("spokenin")).toString().equals("[Language, Languages, OfficialLang, language, official language, official languages, region, spoken in]"));
		assertTrue(new TreeSet<String>(map.get("produce")).toString().equals("[Caption, Name, designer, designer company, manufacturer, model, origin, related, sequence, service, title]"));		
	}

	@Test
	public void testMultiMapToStringCsvTsv()
	{
		MultiHashMap<String,String> map = new MultiHashMap<String,String>();
		map.putAll("word",Arrays.asList(new String[]{"schmörd","börd"}));
		map.putAll("wort",Arrays.asList(new String[]{"schmört","bört"}));
				
		String s = ImportExport.multiMapToStringTsvCsv(null,map);
		String t = "word		schmörd,börd\nwort		schmört,bört";	
		assertTrue(s.equals(t));
	}

}