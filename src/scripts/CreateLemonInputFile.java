package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class CreateLemonInputFile
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

	public static void main(String[] args) throws FileNotFoundException
	{
		File f = new File("input/source/dbpedia_train_lexicon_en.ttl");

		//		new FileManager().readModel(m,f.getAbsolutePath(),"TURTLE");
		m = ModelFactory.createMemModelMaker().createDefaultModel();
		m.read(new FileInputStream(f),"","TURTLE");
//		

		ResultSet entrySet = execSelect("select ?s ?l ?d {?s a lemon:LexicalEntry. ?s lemon:canonicalForm ?c." +
				"?c lemon:writtenRep ?l. ?s lemon:sense ?sense. ?sense lemon:reference ?d.}");

		MultiMap<String,String> wordToSense = new MultiHashMap<>();
		while(entrySet.hasNext())
		{
			QuerySolution qs = entrySet.next();
			String uri = qs.get("?s").toString();
			//			System.out.println(uri);
			String suffix = uri.substring(uri.lastIndexOf('#')+1);
			//			System.out.println(suffix);
			String label = qs.get("?l").asLiteral().getLexicalForm();
			//			System.out.println(label);
			String sense = qs.get("?d").toString();
			String senseSuffix = sense.substring(sense.lastIndexOf('/')+1);
			
			if(!suffix.equals(senseSuffix))
			{
//				System.out.println(suffix+"!="+senseSuffix);
				wordToSense.put(label,sense.replace("http://dbpedia.org/ontology/","dbo:"));
			}
		}
		new File("tmp").mkdir();
		try(PrintWriter out = new PrintWriter("tmp/lemon.tsv"))
		{
			for(String word: wordToSense.keySet())
			{
				out.println(word+"\t"+wordToSense.get(word).toString().replace(", ","\t").replace("[","").replace("]",""));
			}
		}
	}
}