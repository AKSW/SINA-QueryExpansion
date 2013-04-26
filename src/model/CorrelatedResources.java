package model;

import static model.SPARQLHelper.DBPEDIA_ENDPOINT;
import static model.SPARQLHelper.answerSet;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

import de.konrad.commons.sparql.PrefixHelper;

public class CorrelatedResources
{
	static final int LIMIT = 100;

	protected static Set<String> getResourcesUnion(final String[] queries, String var) throws InterruptedException
	{
		if(!(var.charAt(0)=='?')) {var='?'+var;}
		StringBuilder fullQuery = new StringBuilder("select "+var+" {");		
		for(String query: queries)
		{
			fullQuery.append("{");
			fullQuery.append(query);
			fullQuery.append("}UNION");
		}		
		return SPARQLHelper.answerSet(fullQuery.substring(0,fullQuery.length()-"UNION".length())+"}",SPARQLHelper.DBPEDIA_ENDPOINT);			
	}
	

	protected static Set<String> getResourcesMultiThreaded(String[] queries) throws InterruptedException
	{		
		ExecutorService executor = Executors.newFixedThreadPool(4);
		final Set<String> resources = Collections.newSetFromMap(new HashMap<String,Boolean>());
		final AtomicInteger i = new AtomicInteger(0);
		for(final String query: queries)
		{			
			executor.submit(new Runnable()
			{			
				@Override
				public void run()
				{					
					int threadNumber = i.incrementAndGet();
					
					System.out.println("executing query "+threadNumber+": "+query);					
					resources.addAll(answerSet(query,DBPEDIA_ENDPOINT));//					
					System.out.println("finished executing query "+threadNumber);
				}
			}); 
		}
		executor.shutdown();
		if(!executor.awaitTermination(2,TimeUnit.MINUTES)) System.out.println("timeout, x/y completed");
		return resources;		
	}
	
	public static Set<String> correlatedDBpediaResourceLabels(String uri) throws InterruptedException, TimeoutException
	{
		System.out.println("********Getting correlated resources for uri "+uri+"*********+");
		uri = PrefixHelper.expand(uri);

		Set<String> resources;
		int prefixEnd = 1+Math.max(uri.lastIndexOf('/'),uri.lastIndexOf('#'));
		String prefix = uri.substring(0,prefixEnd);
		String suffix = uri.substring(prefixEnd);
		switch(prefix)
		{
			case "http://dbpedia.org/property/":
			case "http://dbpedia.org/ontology/":
				resources=Character.isUpperCase(suffix.charAt(0))?correlatedResourcesForClass(uri):correlatedResourcesForProperty(uri); 				
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
		System.out.println("Getting labels for "+dbpediaResources.size()+" correlated resources of "+uri);
		for(final String resource : dbpediaResources)
		{
			
			Future f = executor.submit(new Runnable()
			{			
				@Override
				public void run()
				{
					QueryExecution qe = new QueryEngineHTTP(SPARQLHelper.DBPEDIA_ENDPOINT,"select ?l {<"+resource+"> rdfs:label ?l. filter(langmatches(lang(?l),'en'))}");
					ResultSet rs = qe.execSelect();					
					while(rs.hasNext()) {labels.add(rs.nextSolution().getLiteral("?l").getLexicalForm());}
				}
			}); 
		}
		executor.shutdown();
		if(!executor.awaitTermination(1,TimeUnit.MINUTES)) throw new TimeoutException("timeout when getting the labels of uri"+uri);
		return labels;
	}

	protected static Set<String> correlatedResourcesForClass(String clazz) throws InterruptedException
	{
		String[] queries = new String[]
				{
				"select ?c where {?c rdfs:subClassOf <"+clazz+">}",
				"select ?c where {<"+clazz+"> rdfs:subClassOf ?c}",
				"select ?c where {<"+clazz+"> skos:broader ?c}",
				"select ?c where {<"+clazz+"> skos:narrower ?c}",
				"select ?c where {?s a <"+clazz+">. ?s a ?c. filter (?c!=<"+clazz+">)}",
				"select ?c where {<"+clazz+"> rdfs:label ?l. ?c rdfs:label ?l. }"//filter (?c!=<"+clazz+">)    
				};				
		return getResourcesUnion(queries,"?c");
	}
	
	protected static Set<String> correlatedResourcesForProperty(String prop) throws InterruptedException
	{	
		String[] queries = new String[]
				{
				"select ?p where {?p rdfs:subPropertyOf <"+prop+"> } limit "+LIMIT,
				"select ?p where {<"+prop+"> rdfs:subPropertyOf ?p} limit "+LIMIT,
				"select ?p where {<"+prop+"> skos:mappingRelation ?p} limit "+LIMIT,
				"select ?p where {<"+prop+"> skos:closeMatch ?p} limit "+LIMIT,
				"select ?p where {<"+prop+"> skos:exactMatch ?p} limit "+LIMIT,
				"select ?p where {<"+prop+"> skos:broadMatch ?p} limit "+LIMIT,
				"select ?p where {<"+prop+"> skos:narrowMatch ?p} limit "+LIMIT,
				"select ?p where {<"+prop+"> skos:relatedMatch ?p} limit "+LIMIT,
				"select ?p where {?s <"+prop+"> ?o. ?s ?p ?o. filter (?p!=<"+prop+">)} limit "+LIMIT,
				"select ?p where {<"+prop+"> rdfs:label ?l. ?p rdfs:label ?l.} limit "+LIMIT
				
				};				
		return getResourcesUnion(queries,"?p");
	}
	
	
	
//	protected static Set<String> getResources(String[] queries) throws InterruptedException
//	{
//		ExecutorService executor = Executors.newFixedThreadPool(4);
//		final Set<String> resources = Collections.newSetFromMap(new HashMap<String,Boolean>());
//		final AtomicInteger i = new AtomicInteger(0);
//		for(final String query: queries)
//		{			
//			executor.submit(new Runnable()
//			{			
//				@Override
//				public void run()
//				{					
//					int threadNumber = i.incrementAndGet();
//					
//					System.out.println("executing query "+threadNumber+": "+query);
//					QueryExecution qe = new QueryEngineHTTP(ENDPOINT,query);
//					resources.addAll(answerSet(query));
////					ResultSet rs = qe.execSelect();					
////					while(rs.hasNext())
////					{												
////						resources.add(rs.next().getResource("p").toString());						
////					}
//					System.out.println("finished executing query "+threadNumber);
//				}
//			}); 
//		}
//		executor.shutdown();
//		if(!executor.awaitTermination(2,TimeUnit.MINUTES)) System.out.println("timeout, x/y completed");
//		return resources;		
//	}
	
	
}