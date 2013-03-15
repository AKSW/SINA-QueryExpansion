package model;

import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class CorrelatedResources
{
	protected static Set<String> resources(String endpoint, String query, String var)
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
	
	@SuppressWarnings("rawtypes")
	public static Set<String> correlatedResourcesForClass(String endpoint, String clazz)
	{
		Set<String> subClasses		= resources(endpoint,"select ?sub where {?sub rdfs:subClassOf <"+clazz+">}","?sub");
		Set<String> superClasses	= resources(endpoint,"select ?sup where {<"+clazz+"> rdfs:subClassOf ?sup}","?sup");	
		Set<String> otherClasses	= resources(endpoint,"select ?c where {?s a <"+clazz+">. ?s a ?c. filter (?c!=<"+clazz+">)}","?c");
		Set<String> sameLabel 		= resources(endpoint,"select ?c where {<"+clazz+"> rdfs:label ?l. ?c rdfs:label ?l.}","?c");		
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
	public static Set<String> correlatedResourcesForProperty(String endpoint, String prop)
	{
		int LIMIT = 100;
		Set<String> subProperties		= resources(endpoint,"select ?sub where {?sub rdfs:subPropertyOf <"+prop+">}","?sub");
		Set<String> superProperties	= resources(endpoint,"select ?sup where {<"+prop+"> rdfs:subPropertyOf ?sup}","?sup");	
		Set<String> otherProperties	= resources(endpoint,"select ?p where {?s <"+prop+"> ?o. ?s ?p ?o. filter (?p!=<"+prop+">)} limit "+LIMIT+"","?p");
		Set<String> sameLabel 		= resources(endpoint,"select ?p where {<"+prop+"> rdfs:label ?l. ?p rdfs:label ?l.}","?p");		
		Set[] subSets = {subProperties,superProperties,otherProperties,sameLabel};
		Set<String> resources = new HashSet<String>();
		for(Set subSet : subSets) resources.addAll(subSet);
//		System.out.println(subClasses);
//		System.out.println(superClasses);
//		System.out.println(otherClasses);
//		System.out.println(sameLabel);		
		return resources;
	}
}