package model;

import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;


public class SPARQLHelper
{
	public static final String DBPEDIA_ENDPOINT = "http://139.18.2.96:8910/sparql";	
	public static final String SCHEMA_ENDPOINT = "http://lod.openlinksw.com/sparql";
	/** 
	 * gets a set of the first bindings of each row of the result of a sparql query. */
	public static Set<String> answerSet(String query, String endpoint)
	{
		Set<String> resources = new HashSet<String>();
		QueryExecution qe = new QueryEngineHTTP(endpoint,query);
		ResultSet rs = qe.execSelect();
		while(rs.hasNext())
		{
			QuerySolution qs = rs.nextSolution();
			RDFNode node = qs.get(qs.varNames().next());
			resources.add((node.isLiteral()?node.asLiteral().getLexicalForm():node.asResource()).toString());
		}
		return resources;
	}
	
}
