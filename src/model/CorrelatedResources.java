package model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

import de.konrad.commons.sparql.PrefixHelper;

public class CorrelatedResources
{
	protected static Set<String> answerSet(String endpoint, String query, String var)
	{		
		Set<String> resources = new HashSet<String>();
		QueryExecution qe = new QueryEngineHTTP(endpoint,query);
		ResultSet rs = qe.execSelect();
		while(rs.hasNext())
		{
			QuerySolution qs = rs.nextSolution();
			resources.add(qs.getResource(var).toString());
		}
		return resources;
	}

	public static Set<String> correlatedDBpediaResourceLabels(String uri) throws InterruptedException
	{
		System.out.println("getting labels for uri"+uri+"...");
		uri = PrefixHelper.expand(uri);
		final String ENDPOINT = "http://live.dbpedia.org/sparql";
		Set<String> resources;
		int prefixEnd = 1+Math.max(uri.lastIndexOf('/'),uri.lastIndexOf('#'));
		String prefix = uri.substring(0,prefixEnd);
		String suffix = uri.substring(prefixEnd);
		switch(prefix)
		{
			case "http://dbpedia.org/property/":
			case "http://dbpedia.org/ontology/":
				resources=Character.isUpperCase(suffix.charAt(0))?correlatedResourcesForClass(ENDPOINT,uri):correlatedResourcesForProperty(ENDPOINT,uri); 				
				break;
			default: return Collections.<String>emptySet();
			//throw new IllegalArgumentException("url seems to neither be a dbpedia class nor dbpedia property");
		}
		Set<String> dbpediaResources = new HashSet<String>();
		for(String resource : resources)
		{
			if(resource.startsWith("http://dbpedia.org/"))	dbpediaResources.add(resource);
		}
		final Set<String> labels = new HashSet<String>();
		// TODO: optimize speed (multithreading and/or union or some library like from Claus)
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for(final String resource : dbpediaResources)
		{
			Future f = executor.submit(new Runnable()
			{			
				@Override
				public void run()
				{
					QueryExecution qe = new QueryEngineHTTP(ENDPOINT,"select ?l {<"+resource+"> rdfs:label ?l. filter(langmatches(lang(?l),'en'))}");
					ResultSet rs = qe.execSelect();
					if(rs.hasNext()) {labels.add(qe.execSelect().next().getLiteral("?l").getLexicalForm());}
				}
			}); 
		}
		if(!executor.awaitTermination(1,TimeUnit.MINUTES)) throw new RuntimeException("timeout when getting the labels of uri"+uri);
		return labels;
	}

	@SuppressWarnings("rawtypes")
	protected static Set<String> correlatedResourcesForClass(String endpoint, String clazz)
	{
		//		Set<String> subClasses		= answerSet(endpoint,"select distinct ?l where {?sub rdfs:subClassOf <"+clazz+">. ?sub rdfs:label ?l. filter(langmatches(lang(?l),'en'))}","?l");
		//		Set<String> superClasses	= answerSet(endpoint,"select distinct ?l where {<"+clazz+"> rdfs:subClassOf ?sup.?sub rdfs:label ?l. filter(langmatches(lang(?l),'en'))}","?l");	
		//		Set<String> otherClasses	= answerSet(endpoint,"select distinct ?l where {?s a <"+clazz+">. ?s a ?c. filter (?c!=<"+clazz+">).?c rdfs:label ?l. filter(langmatches(lang(?l),'en'))}","?l");
		//		Set<String> sameLabel 		= answerSet(endpoint,"select distinct ?l where {<"+clazz+"> rdfs:label ?l. ?c rdfs:label ?l.?c rdfs:label ?l. filter(langmatches(lang(?l),'en'))}","?l");		
		
		Set<String> subClasses		= answerSet(endpoint,"select ?sub where {?sub rdfs:subClassOf <"+clazz+">}","?sup");
		Set<String> superClasses	= answerSet(endpoint,"select ?sup where {<"+clazz+"> rdfs:subClassOf ?sup}","?sup");	
		Set<String> otherClasses	= answerSet(endpoint,"select ?c where {?s a <"+clazz+">. ?s a ?c. filter (?c!=<"+clazz+">)}","?c");
		Set<String> sameLabel 		= answerSet(endpoint,"select ?c where {<"+clazz+"> rdfs:label ?l. ?c rdfs:label ?l. }","?c");//filter (?c!=<"+clazz+">)		
		Set[] subSets = {subClasses,superClasses,otherClasses,sameLabel};
		Set<String> resources = new HashSet<String>();
		for(Set<String> subSet : subSets) resources.addAll(subSet);
		//		System.out.println(subClasses);
		//		System.out.println(superClasses);
		//		System.out.println(otherClasses);
		//		System.out.println(sameLabel);		
		return resources;
	}

	@SuppressWarnings("rawtypes")
	protected static Set<String> correlatedResourcesForProperty(String endpoint, String prop)
	{
		int LIMIT = 100;
		System.out.println("getting sub properties...");
		Set<String> subProperties	= answerSet(endpoint,"select ?sub where {?sub rdfs:subPropertyOf <"+prop+"> } limit "+LIMIT,"?sub");
		System.out.println("getting super properties...");
		Set<String> superProperties	= answerSet(endpoint,"select ?sup where {<"+prop+"> rdfs:subPropertyOf ?sup} limit "+LIMIT,"?sup");	
		System.out.println("getting other properties...");
		Set<String> otherProperties	= answerSet(endpoint,"select ?p where {?s <"+prop+"> ?o. ?s ?p ?o. filter (?p!=<"+prop+">)} limit "+LIMIT+"","?p");
		System.out.println("getting same label properties...");
		Set<String> sameLabel 		= answerSet(endpoint,"select ?p where {<"+prop+"> rdfs:label ?l. ?p rdfs:label ?l.} limit "+LIMIT,"?p");		
		Set[] subSets = {subProperties,superProperties,otherProperties,sameLabel};
		Set<String> resources = new HashSet<String>();
		for(Set<String> subSet : subSets) resources.addAll(subSet);
		//		System.out.println(subClasses);
		//		System.out.println(superClasses);
		//		System.out.println(otherClasses);
		//		System.out.println(sameLabel);		
		return resources;
	}
}