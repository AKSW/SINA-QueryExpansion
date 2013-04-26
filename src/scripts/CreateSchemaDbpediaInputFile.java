package scripts;

import static model.SPARQLHelper.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
public class CreateSchemaDbpediaInputFile
{
	static Model m;

	static ResultSet execSelect(String queryStr)
	{		
		Query q = new Query();
		q.setPrefixMapping(m);
		QueryFactory.parse(q,queryStr,null,Syntax.syntaxSPARQL);		
		QueryExecution qe = QueryExecutionFactory.create(q,m);
		return qe.execSelect();
	}
	
	static Map<String,String> getLabels(Collection<String> urls, String endpoint)
	{
		Map<String,String> urlToLabel = new HashMap<>();
		for(String url: urls)
		{
//			System.out.println("select ?l {<"+url+"> rdfs:label ?l} at "+endpoint);
			Set<String> labels = answerSet("select ?l {<"+url+"> rdfs:label ?l}",endpoint);
			if(!labels.isEmpty()) {urlToLabel.put(url,labels.iterator().next());}
		}
		return urlToLabel;
	}

	public static void main(String[] args) throws FileNotFoundException
	{
		// ********* read file ***********************
		File f = new File("input/source/dbpedia-schema.nt");
		m = ModelFactory.createMemModelMaker().createDefaultModel();
		m.read(new FileInputStream(f),"","TURTLE");
		m.setNsPrefix("dbo","http://dbpedia.org/ontology/");
		m.setNsPrefix("schema","http://schema.org/");

		ResultSet entrySet = execSelect("select ?dbpedia ?schema {?dbpedia ?p ?schema.}");

		Map<String,String> schemaToDbpedia = new HashMap<>();		
		while(entrySet.hasNext())
		{
			QuerySolution qs = entrySet.next();
			schemaToDbpedia.put(qs.get("?schema").toString(),qs.get("?dbpedia").toString());
		}
		// **** query dbpedia and schema.org for the labels****************************
		Map<String,String> dbpediaLabels = getLabels(schemaToDbpedia.values(),DBPEDIA_ENDPOINT);
		Map<String,String> schemaLabels = getLabels(schemaToDbpedia.keySet(),SCHEMA_ENDPOINT);
		//		Map<String,String> labelToDbpedia = new HashMap<>();

		new File("tmp").mkdir();
		try(PrintWriter out = new PrintWriter("tmp/schemadbpedia.tsv"))
		{
			for(String schemaUrl: schemaToDbpedia.keySet())
			{
				String schemaLabel = schemaLabels.get(schemaUrl);				
				if(schemaLabel==null||schemaLabel.isEmpty()) {continue;}			
				String dbpediaUrl = schemaToDbpedia.get(schemaUrl);
				String dbpediaLabel = dbpediaLabels.get(dbpediaUrl);
				if(dbpediaLabel!=null&&!dbpediaLabel.equalsIgnoreCase(schemaLabel))
				{out.println(schemaLabel+"\t"+dbpediaUrl.replace("http://dbpedia.org/ontology/","dbo:"));}
			}
		}
	}
}