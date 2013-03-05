package com.Layout;

import java.util.List;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.vaadin.data.util.IndexedContainer;


public class RunSparqlQueries {
	

	
	//private String defaultGraphString = "http://dbpedia.org";
	private IndexedContainer ResultContainer;
	private String urlsever = "http://139.18.2.96:8910/sparql";//"http://dbpedia.org/sparql";
	
	/**
	 * 
	 * @param keyword
	 * @return
	 */
	public IndexedContainer getResultContainer()
	{
		return ResultContainer;
	}
	
	
	public void runSparqlQuery(String SPARQLquery) {
		
		//List<ResourceInfo> foundRessources = new ArrayList<ResourceInfo>();
		
		
		//String urlsever = "jdbc:virtuoso://139.18.2.96:1130/";
		
		
		SPARQLquery=SPARQLquery + " limit 10 ";
		System.out.println("-------retrieved query is:"+ SPARQLquery);
		Query query=QueryFactory.create(SPARQLquery);
		
		//QueryEngineHTTP qexec = new QueryEngineHTTP("http://db0.aksw.org:8895/sparql", query);	
		//QueryEngineHTTP qexec = new QueryEngineHTTP(urlsever, query);
		QueryEngineHTTP qexec = new QueryEngineHTTP(urlsever, SPARQLquery);	
		//QueryExecution qexec = QueryExecutionFactory.sparqlService("http://pubmed.bio2rdf.org/sparql",query);
		//qexec.addDefaultGraph(defaultGraphString);			
	try{
    ResultSet results = qexec.execSelect();
    ResultContainer = createDummyData(results);
    System.out.println("size of container   " + ResultContainer.size());
    
	}
	catch(Exception e) {
		
		System.out.println("finish by exeption 1" +e.getMessage());
		//break;
	}
	finally{//qexec.close();
	}
	
}// end of the method

	//#################################################
    //  This method make a container for using  	
	//#################################################
	private static IndexedContainer createDummyData(ResultSet results) {
					       
			        IndexedContainer ic = new IndexedContainer();
		
			        List<String> headers = results.getResultVars();
			        String[] fields =new String[headers.size()];
			        int counter=0;
			        for (String p : headers) {
			        	fields[counter]=p;
			        	counter++;
			        }
			        for (String p : fields) {
			            ic.addContainerProperty(p, String.class, "");
			            System.out.println("fields  :" + p);
			        }
			        while(results.hasNext()){
			        	Object id = ic.addItem();
			        	QuerySolution  solution=results.nextSolution();
			        	
			        	for(String p:fields)
			        	{
			        	ic.getContainerProperty(id, p).setValue(
			        			solution.get(p).toString());
			        	System.out.println(" value ...." + solution.get(p).toString());
			        	
			        	}
			        	        	       	
			        }

			        return ic;
			        
	}
	
//______________________________________________________________________
	
}
