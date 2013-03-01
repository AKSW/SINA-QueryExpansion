package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import HMMQuerySegmentation.Functionality;
import HMMQuerySegmentation.PreProcessing;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;


public class ReasoningOverLOD {
	
	
public Set<String> getLabelsViaSameAsOverLOD(String Pattern, ArrayList Segment) {
		
	    private  Set<String> LabelSet				=			new TreeSet<String>();	
	    String querystring;
		 
		 String option="";
		 querystring = " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		  " SELECT distinct *  " +
		  "  WHERE { ?iri rdfs:label ?label.   " + 
		  "?label  bif:contains  '" + Pattern  + "'  ." +
		  "  ?iri owl:sameAs ?iriSameAs. " +
		  " iriSameAs rdfs:label ?labelSameAs. " +
			" FILTER( langMatches(lang(?labelSameAs), \"en\")). }"; //  //" FILTER(REGEX(?label,\"" + Pattern  + "\")). " ;  + option +  "   }  "; //limit 10 " FILTER regex(str(?iri), \"http://dbpedia.org/resource/\")
		
		System.out.println("-------retrieved query is:" +querystring);
		String urlsever = Expansion_Constants.LODendpoint;
		//Query query=QueryFactory.create(querystring);
		QueryEngineHTTP qexec = new QueryEngineHTTP(urlsever, querystring);	
		//QueryExecution qexec = QueryExecutionFactory.sparqlService(urlsever,query);
		
		Set<String> setiri=new TreeSet<String>();
						
		try{
	    ResultSet results = qexec.execSelect();
	    while(results.hasNext()){
	    	
	    	QuerySolution  solution=results.nextSolution();
	    	
	    	
	    	
			String  Label = null, iri=null;
			iri = solution.get("iriSameAs").toString();
			Label = solution.get("labelSameAs").toString();	
			
			if (Label.endsWith("@en")) {
			Label = Label.replace("@en", "");}
			
			if (Label.endsWith("\"")) {
			Label = Label.replace("\"", "");}
				
			if (Label.startsWith("\"")) {
			Label = Label.replace("\"", "");}
			
			
	            	       	
	    }//end of second while
	 
		}
		catch(Exception e) {
			
			System.out.println("finish by exeption" +e.getMessage());
			
		}
		finally{
		}
		
		
		Functionality f = new Functionality();
		PreProcessing p = new PreProcessing();
		
		ArrayList LabelList;
		System.out.println("saeedeh feting finished");
		for(ResourceInfo resourceInfo: foundRessources) 
		{
			if(resourceInfo.getType() == Constants.TYPE_INSTANCE){
			//LabelList = p.removeStopWordswithStemming(resourceInfo.getLabel());
				LabelList = p.removeStopWordswithlem(resourceInfo.getLabel());
				resourceInfo.setStringSimilarityScore(f.JaccardLevenstienSimilarity(Segment, LabelList , resourceInfo.getLabel()));
			}
			else 
			{
				LabelList = p.removeStopWordswithlem(resourceInfo.getLabel());
				resourceInfo.setStringSimilarityScore(f.JaccardLevenstienSimilarity(Segment, LabelList , resourceInfo.getLabel()));
					
			}
			
			//resourceInfo.setStringSimilarityScore( f.SimilarityString(Term.trim() , resourceInfo.getLabel().trim(), minimumlength, maximumlength) );
			//System.out.println("  resourceInfo  " + resourceInfo.getLabel()  + " score " + resourceInfo.getStringSimilarityScore());
			
		} 
		
		
		
		
		for(ResourceInfo resourceInfo: foundRessources) 
		{
			if(resourceInfo.getStringSimilarityScore() >=  thresholdtheta )
			{
				finalfoundResources.add(resourceInfo);
			}
		
		}
		
		
		// sort the final list
		//Collections.sort(finalfoundResources, new StringSimilarityComparator());
		// cut the final list if it is so big
		/*if (finalfoundResources.size() > Constants.LimitOfList)
		{
			finalfoundResources=finalfoundResources.subList(0, Constants.LimitOfList);
		}*/
		
		
		// set the associating class of final resources
		  for(ResourceInfo resourceInfo: finalfoundResources) 
			{
			 if(resourceInfo.getType() == Constants.TYPE_INSTANCE)
			   resourceInfo.setClassSet(getClassofInstance(resourceInfo.getUri()));
			} 
		
			
		
		System.out.println("size of finalfoundResources " + finalfoundResources.size());
		return finalfoundResources;
		//return foundRessources;
	}

	

}
