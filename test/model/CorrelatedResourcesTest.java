package model;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class CorrelatedResourcesTest
{
	@Test
	public void testCorrelatedDBpediaResourceLabels() throws InterruptedException
	{
		System.out.println(CorrelatedResources.correlatedDBpediaResourceLabels("http://dbpedia.org/ontology/Country"));
	}

	@Test	
	public void testResources()
	{
		assertTrue(
				CorrelatedResources.answerSet("http://dbpedia.org/sparql","select distinct ?s where {?s a dbpedia-owl:Country. ?s rdfs:label ?l. ?l bif:contains 'Germany' }","?s")
				.contains("http://dbpedia.org/resource/Germany")
				);
	}

	@Test
	public void testCorrelatedResourcesForClass()
	{
		Set<String> resources = CorrelatedResources.correlatedResourcesForClass("http://dbpedia.org/sparql","http://dbpedia.org/ontology/Country");
		/*sed "s|, |\",\n\"|g" /tmp/tmp > /tmp/tmp2
		 *sort -r /tmp/tmp2 | head -20*/
		Set<String> subSet = new HashSet<>(Arrays.asList(new String[] {"http://xmlns.com/foaf/0.1/Person",			
				"http://dbpedia.org/resource/State_(polity)",
				"http://dbpedia.org/property/country",
				"http://dbpedia.org/ontology/PopulatedPlace",
				"http://dbpedia.org/ontology/Place",
				"http://dbpedia.org/ontology/Country",
				"http://dbpedia.org/ontology/country",
				"http://dbpedia.org/class/yago/YagoGeoEntity"}));
		subSet.removeAll(resources);
		assertTrue(subSet.size()<2);		
	}
	
	@Test
	public void testCorrelatedResourcesForProperty()
	{
		Set<String> resources = CorrelatedResources.correlatedResourcesForProperty("http://dbpedia.org/sparql","http://dbpedia.org/property/birthPlace");
		Set<String> subSet = new HashSet<>(Arrays.asList(new String[] {
				"http://dbpedia.org/property/termStart", "http://dbpedia.org/property/dateOfBirth", "http://dbpedia.org/property/dateOfDeath", "http://dbpedia.org/property/deathPlace", "http://dbpedia.org/property/termEnd", "http://dbpedia.org/property/rows", "http://dbpedia.org/property/placeOfBirth"
		}));
		subSet.removeAll(resources);
		assertTrue(subSet.size()<5);		
	}
}