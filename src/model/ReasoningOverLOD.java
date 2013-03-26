package model;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;


public class ReasoningOverLOD {
	
	
// ####################################################################################	
///  This function retrieves all labels connected via sameAs links to the anchor points	
// ####################################################################################
	
public Set<String> getLabelsViaSameAsOverLOD(String Pattern) {
		
	    Set<String> SameAsLabelSet				=			new TreeSet<String>();	
	    String querystring;
		 
		 String option="";
		 querystring = " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		  " SELECT distinct *  " +
		  " WHERE { ?iri rdfs:label ?label.   "  + 
		 // " ?label  bif:contains  '" + Pattern  + "'  ." +
		  "filter (?label=  '" + Pattern  + "')." +
		  " ?iri owl:sameAs ?iriSameAs. " +
		  " ?iriSameAs rdfs:label ?labelSameAs. " +
		  " FILTER( langMatches(lang(?labelSameAs), \"en\")). }"; 
		
		System.out.println("-------retrieved query is:" +querystring);
		String urlsever = ExpansionConstants.LODendpoint;
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
			
			SameAsLabelSet.add(Label);
	            	       	
	    }//end of second while
	 
		}
		catch(Exception e) {
			
			System.out.println("finish by exeption" +e.getMessage());
			
		}
		finally{
		}
		
		
		
		return SameAsLabelSet;
		
	}

//####################################################################################	
///  This function retrieves all labels connected via seeAlso links to the anchor points	
//####################################################################################
	
public Set<String> getLabelsViaSeeAlsoOverLOD(String Pattern) {
		
	    Set<String> SeeAlsoLabelSet				=			new TreeSet<String>();	
	    String querystring;
		 
		 String option="";
		 querystring = " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		  " SELECT distinct *  " +
		  " WHERE { ?iri rdfs:label ?label.   " + 
		 // " ?label  bif:contains  '" + Pattern  + "'  ." +
		  "filter (?label=  '" + Pattern  + "')."+
		  " ?iri rdfs:seeAlso ?iriSeeAlso. " +
		  " ?iriSeeAlso rdfs:label ?labelSeeAlso. " +
		  " FILTER( langMatches(lang(?labelSeeAlso), \"en\")). }"; 
		
		System.out.println("-------retrieved query is:" +querystring);
		String urlsever = ExpansionConstants.LODendpoint;
		//Query query=QueryFactory.create(querystring);
		QueryEngineHTTP qexec = new QueryEngineHTTP(urlsever, querystring);	
		//QueryExecution qexec = QueryExecutionFactory.sparqlService(urlsever,query);
		
		Set<String> setiri=new TreeSet<String>();
						
		try{
	    ResultSet results = qexec.execSelect();
	    while(results.hasNext()){
	    	
	    	QuerySolution  solution=results.nextSolution();
	    	
	    	
	    	
			String  Label = null, iri=null;
			iri = solution.get("iriSeeAlso").toString();
			Label = solution.get("labelSeeAlso").toString();	
			
			if (Label.endsWith("@en")) {
			Label = Label.replace("@en", "");}
			
			if (Label.endsWith("\"")) {
			Label = Label.replace("\"", "");}
				
			if (Label.startsWith("\"")) {
			Label = Label.replace("\"", "");}
			
			SeeAlsoLabelSet.add(Label);
	            	       	
	    }//end of second while
	 
		}
		catch(Exception e) {
			
			System.out.println("finish by exeption" +e.getMessage());
			
		}
		finally{
		}
		
		
		
		return SeeAlsoLabelSet;
		
	}

//####################################################################################	
///  This function retrieves all labels connected via equivalent links to the anchor points	
//####################################################################################
	
public Set<String> getLabelsViaEquivalentOverLOD(String Pattern) {
		
	    Set<String> SameAsLabelSet				=			new TreeSet<String>();	
	    String querystring;
		 
		 String option="";
		 querystring = " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>  " +
				 " SELECT distinct *  " + 
				 " WHERE { ?iri rdfs:label ?label. " +
				 "filter (?label=  '" + Pattern  + "')."+
				 " optional {?iri owl:equivalentClass ?equivalentiri. }" + 
				 " optional {?iri owl:equivalentProperty ?equivalentiri. } " +
				 " ?equivalentiri rdfs:label ?labelEquivalent.  " + 
				 " FILTER( langMatches(lang(?labelEquivalent),  \"en\")). }";  
		
		System.out.println("-------retrieved query is:" +querystring);
		String urlsever = ExpansionConstants.LODendpoint;
		//Query query=QueryFactory.create(querystring);
		QueryEngineHTTP qexec = new QueryEngineHTTP(urlsever, querystring);	
		//QueryExecution qexec = QueryExecutionFactory.sparqlService(urlsever,query);
		
		Set<String> setiri=new TreeSet<String>();
						
		try{
	    ResultSet results = qexec.execSelect();
	    while(results.hasNext()){
	    	
	    	QuerySolution  solution=results.nextSolution();
	    	
	    	
	    	
			String  Label = null, iri=null;
			iri = solution.get("equivalentiri").toString();
			Label = solution.get("labelEquivalent").toString();	
			
			if (Label.endsWith("@en")) {
			Label = Label.replace("@en", "");}
			
			if (Label.endsWith("\"")) {
			Label = Label.replace("\"", "");}
				
			if (Label.startsWith("\"")) {
			Label = Label.replace("\"", "");}
			
			SameAsLabelSet.add(Label);
	            	       	
	    }//end of second while
	 
		}
		catch(Exception e) {
			
			System.out.println("finish by exeption" +e.getMessage());
			
		}
		finally{
		}
		
		
		
		return SameAsLabelSet;
		
	}


//####################################################################################	
///  This function retrieves all labels connected via superclass/ Super property links to the anchor points	
//####################################################################################
	
public Set<String> getLabelsViaSuperOverLOD(String Pattern) {
		
	    Set<String> SameAsLabelSet				=			new TreeSet<String>();	
	    String querystring;
		 
		 String option="";
		 querystring = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " + 
				 		" SELECT distinct * " +  
				 		" WHERE { ?iri rdfs:label ?label. " +  
				 		 "filter (?label=  '" + Pattern  + "')."+
				 		" optional {?iri rdfs:subClassOf ?superiri. } " +
				 		" optional {?iri rdfs:subPropertyOf  ?superiri. } " +
				 		" ?superiri rdfs:label ?labelsuperiri. " +
				 		" FILTER( langMatches(lang(?labelsuperiri),  \"en\")). }";  
		
		System.out.println("-------retrieved query is:" +querystring);
		String urlsever = ExpansionConstants.LODendpoint;
		//Query query=QueryFactory.create(querystring);
		QueryEngineHTTP qexec = new QueryEngineHTTP(urlsever, querystring);	
		//QueryExecution qexec = QueryExecutionFactory.sparqlService(urlsever,query);
		
		Set<String> setiri=new TreeSet<String>();
						
		try{
	    ResultSet results = qexec.execSelect();
	    while(results.hasNext()){
	    	
	    	QuerySolution  solution=results.nextSolution();
	    	
	    	
	    	
			String  Label = null, iri=null;
			iri = solution.get("superiri").toString();
			Label = solution.get("labelsuperiri").toString();	
			
			if (Label.endsWith("@en")) {
			Label = Label.replace("@en", "");}
			
			if (Label.endsWith("\"")) {
			Label = Label.replace("\"", "");}
				
			if (Label.startsWith("\"")) {
			Label = Label.replace("\"", "");}
			
			SameAsLabelSet.add(Label);
	            	       	
	    }//end of second while
	 
		}
		catch(Exception e) {
			
			System.out.println("finish by exeption" +e.getMessage());
			
		}
		finally{
		}
		
		
		
		return SameAsLabelSet;
		
	}


//####################################################################################	
///  This function retrieves all labels connected via subclass/ Sub property links to the anchor points	
//####################################################################################
	
public Set<String> getLabelsViaSubOverLOD(String Pattern) {
		
	    Set<String> SameAsLabelSet				=			new TreeSet<String>();	
	    String querystring;
		 
		 String option="";
		 querystring = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>  " +
				 		" SELECT distinct *  WHERE { ?iri rdfs:label ?label.  " +
				 		 "filter (?label=  '" + Pattern  + "')."+
				 		" optional {?subiri rdfs:subClassOf ?iri . } " + 
				 		" optional {?subiri  rdfs:subPropertyOf ?iri . } " +   
				 		" ?subiri rdfs:label ?labelsubiri. " +  
				 		" FILTER( langMatches(lang(?labelsubiri),  \"en\")). }"; 
 
		
		System.out.println("-------retrieved query is:" +querystring);
		String urlsever = ExpansionConstants.LODendpoint;
		//Query query=QueryFactory.create(querystring);
		QueryEngineHTTP qexec = new QueryEngineHTTP(urlsever, querystring);	
		//QueryExecution qexec = QueryExecutionFactory.sparqlService(urlsever,query);
		
		Set<String> setiri=new TreeSet<String>();
						
		try{
	    ResultSet results = qexec.execSelect();
	    while(results.hasNext()){
	    	
	    	QuerySolution  solution=results.nextSolution();
	    	
	    	
	    	
			String  Label = null, iri=null;
			iri = solution.get("subiri").toString();
			Label = solution.get("labelsubiri").toString();	
			
			if (Label.endsWith("@en")) {
			Label = Label.replace("@en", "");}
			
			if (Label.endsWith("\"")) {
			Label = Label.replace("\"", "");}
				
			if (Label.startsWith("\"")) {
			Label = Label.replace("\"", "");}
			
			SameAsLabelSet.add(Label);
	            	       	
	    }//end of second while
	 
		}
		catch(Exception e) {
			
			System.out.println("finish by exeption" +e.getMessage());
			
		}
		finally{
		}
		
		
		
		return SameAsLabelSet;
		
	}


//####################################################################################	
///  This function retrieves all labels connected via skos:broader links to the anchor points	
//####################################################################################
	
public Set<String> getLabelsViaSkosbroaderOverLOD(String Pattern) {
		
	    Set<String> SameAsLabelSet				=			new TreeSet<String>();	
	    String querystring;
		 
		 String option="";
		 querystring = 
		" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		" prefix skos: <http://www.w3.org/2004/02/skos/core#> " +
		" SELECT distinct *  WHERE { ?iri rdfs:label ?label. " +
		"filter (?label=  '" + Pattern  + "')." + 
		" optional {?iri skos:broader ?broaderiri. } " + 
		" optional {?iri skos:broadMatch  ?broaderiri. } " +
		"  ?broaderiri rdfs:label ?labelbroader. " + 
		" FILTER( langMatches(lang(?labelbroader), \"en\")). }"; 
		
		System.out.println("-------retrieved query is:" +querystring);
		String urlsever = ExpansionConstants.LODendpoint;
		//Query query=QueryFactory.create(querystring);
		QueryEngineHTTP qexec = new QueryEngineHTTP(urlsever, querystring);	
		//QueryExecution qexec = QueryExecutionFactory.sparqlService(urlsever,query);
		
		Set<String> setiri=new TreeSet<String>();
						
		try{
	    ResultSet results = qexec.execSelect();
	    while(results.hasNext()){
	    	
	    	QuerySolution  solution=results.nextSolution();
	    	
	    	
	    	
			String  Label = null, iri=null;
			iri = solution.get("labelbroader").toString();
			Label = solution.get("labelbroader").toString();	
			
			if (Label.endsWith("@en")) {
			Label = Label.replace("@en", "");}
			
			if (Label.endsWith("\"")) {
			Label = Label.replace("\"", "");}
				
			if (Label.startsWith("\"")) {
			Label = Label.replace("\"", "");}
			
			SameAsLabelSet.add(Label);
	            	       	
	    }//end of second while
	 
		}
		catch(Exception e) {
			
			System.out.println("finish by exeption" +e.getMessage());
			
		}
		finally{
		}
		
		
		
		return SameAsLabelSet;
		
	}

//####################################################################################	
///  This function retrieves all labels connected via skos:narrower links to the anchor points	
//####################################################################################
	
public Set<String> getLabelsViaSkosnarrowerOverLOD(String Pattern) {
		
	    Set<String> SameAsLabelSet				=			new TreeSet<String>();	
	    String querystring;
		 
		 String option="";
		 querystring = 
		" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		" prefix skos: <http://www.w3.org/2004/02/skos/core#> " +
		" SELECT distinct *  WHERE { ?iri rdfs:label ?label. " +
		"filter (?label=  '" + Pattern  + "')." + 
		" optional {?iri skos:narrower ?narroweriri. } " + 
		" optional {?iri skos:narrowMatch  ?narroweriri. } " +
		"  ?narroweriri rdfs:label ?labelnarrower. " + 
		" FILTER( langMatches(lang(?labelnarrower), \"en\")). }"; 
		
		System.out.println("-------retrieved query is:" +querystring);
		String urlsever = ExpansionConstants.LODendpoint;
		//Query query=QueryFactory.create(querystring);
		QueryEngineHTTP qexec = new QueryEngineHTTP(urlsever, querystring);	
		//QueryExecution qexec = QueryExecutionFactory.sparqlService(urlsever,query);
		
		Set<String> setiri=new TreeSet<String>();
						
		try{
	    ResultSet results = qexec.execSelect();
	    while(results.hasNext()){
	    	
	    	QuerySolution  solution=results.nextSolution();
	    	
	    	
	    	
			String  Label = null, iri=null;
			iri = solution.get("narroweriri").toString();
			Label = solution.get("labelnarrower").toString();	
			
			if (Label.endsWith("@en")) {
			Label = Label.replace("@en", "");}
			
			if (Label.endsWith("\"")) {
			Label = Label.replace("\"", "");}
				
			if (Label.startsWith("\"")) {
			Label = Label.replace("\"", "");}
			
			SameAsLabelSet.add(Label);
	            	       	
	    }//end of second while
	 
		}
		catch(Exception e) {
			
			System.out.println("finish by exeption" +e.getMessage());
			
		}
		finally{
		}
		
		
		
		return SameAsLabelSet;
		
	}

//####################################################################################	
///  This function retrieves all labels connected via skos related properties links to the anchor points	
//####################################################################################
	
public Set<String> getLabelsViaSkosPropertyMatichOverLOD(String Pattern) {
		
	    Set<String> SameAsLabelSet				=			new TreeSet<String>();	
	    String querystring;
		 
		 String option="";
		 querystring =  
		" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> " +
		" prefix skos: <http://www.w3.org/2004/02/skos/core#>  " +
		" SELECT distinct *  WHERE {  ?iri rdfs:label ?label. "+
		" filter (?label=  '" + Pattern  + "')." + 
		" {?iri skos:relatedMatch ?skosriri.} " +
		" union {?iri skos:closeMatch ?skosriri.} " + 
		" union {?iri skos:mappingRelation ?skosriri.} " + 
		" union {?iri skos:exactMatch ?skosriri.} " +
		" ?skosriri rdfs:label ?skoslabel. " +
		" FILTER( langMatches(lang(?skoslabel),  \"en\")). }"; 
		 
		
		System.out.println("-------retrieved query is:" +querystring);
		String urlsever = ExpansionConstants.LODendpoint;
		//Query query=QueryFactory.create(querystring);
		QueryEngineHTTP qexec = new QueryEngineHTTP(urlsever, querystring);	
		//QueryExecution qexec = QueryExecutionFactory.sparqlService(urlsever,query);
		
		Set<String> setiri=new TreeSet<String>();
						
		try{
	    ResultSet results = qexec.execSelect();
	    while(results.hasNext()){
	    	
	    	QuerySolution  solution=results.nextSolution();
	    	
	    	
	    	
			String  Label = null, iri=null;
			iri = solution.get("skosriri").toString();
			Label = solution.get("skoslabel").toString();	
			
			if (Label.endsWith("@en")) {
			Label = Label.replace("@en", "");}
			
			if (Label.endsWith("\"")) {
			Label = Label.replace("\"", "");}
				
			if (Label.startsWith("\"")) {
			Label = Label.replace("\"", "");}
			
			SameAsLabelSet.add(Label);
	            	       	
	    }//end of second while
	 
		}
		catch(Exception e) {
			
			System.out.println("finish by exeption" +e.getMessage());
			
		}
		finally{
		}
		
		
		
		return SameAsLabelSet;
		
	}

}
