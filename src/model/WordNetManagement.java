package model;

import static model.WordNetManagement.DictionaryGetter.getDict;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.PointerUtils;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.data.list.PointerTargetNode;
import net.didion.jwnl.data.list.PointerTargetNodeList;
import net.didion.jwnl.dictionary.Dictionary;

public class WordNetManagement
{
	// 	prevent direct access to the dictionary field because it could be uninitialized
	static class DictionaryGetter
	{
		private static Dictionary dict = null;		
		public static Dictionary getDict() throws FileNotFoundException, JWNLException
		{		
			if(dict==null)
			{
				JWNL.initialize(new FileInputStream("file_properties.xml"));
				dict=Dictionary.getInstance();
			}
			return dict;
		}
	}	

	static SortedSet<String> getSynsetLemmas(Synset synset)
	{
		TreeSet<String> lemmas = new TreeSet<String>();
		for(Word w: synset.getWords()) {lemmas.add(w.getLemma().replace('_',' '));}
		return lemmas;		
	}

	@SuppressWarnings("rawtypes")
	static
	SortedSet<String> getPointerTargetNodeListLemmas(PointerTargetNodeList list)
	{
		TreeSet<String> lemmas = new TreeSet<String>();
		for(Iterator it = list.iterator(); it.hasNext();)
		{
			PointerTargetNode node = (PointerTargetNode) it.next();
			lemmas.addAll(getSynsetLemmas(node.getSynset()));
		}

		return lemmas;		
	}

	static Set<Synset> getSynsets(String word) throws JWNLException, FileNotFoundException
	{
		Set<Synset> synsets = new HashSet<Synset>();
		for(POS pos  : new POS[] {POS.ADJECTIVE,POS.ADVERB,POS.NOUN,POS.VERB})
		{
			IndexWord indexWord = getDict().getIndexWord(pos,word);
			if(indexWord!=null) synsets.addAll(Arrays.asList(indexWord.getSenses()));
		}
		return synsets;
	}

	/** Returns all lemmas of all hypernyms (more general words) of a word, e.g.  Tea -> {Drink,Plant}.**/
	static public SortedSet<String> getHypernyms(String word) throws JWNLException, FileNotFoundException
	{		
		TreeSet<String> lemmas = new TreeSet<String>();
		for(Synset synset : getSynsets(word))
		{
			lemmas.addAll(getPointerTargetNodeListLemmas(PointerUtils.getInstance().getDirectHypernyms(synset)));
		}
		return lemmas;
	}

	/** Returns all lemmas of all hyponyms (more specialized words) of a word, e.g.  Drink -> {Water,Tea}.**/
	static public SortedSet<String> getHyponyms(String word) throws JWNLException, FileNotFoundException
	{		
		TreeSet<String> lemmas = new TreeSet<String>();
		for(Synset synset : getSynsets(word))
		{
			lemmas.addAll(getPointerTargetNodeListLemmas(PointerUtils.getInstance().getDirectHyponyms(synset)));
		}
		return lemmas;
	}

	/** Returns all lemmas of all synonyms of a word, e.g. Waste Bin -> {Waste Basket}.**/
	static public SortedSet<String> getSynonyms(String word) throws JWNLException, FileNotFoundException
	{		
		TreeSet<String> lemmas = new TreeSet<String>();
		for(Synset synset : getSynsets(word)) {lemmas.addAll(getSynsetLemmas(synset));}
		return lemmas;
	}
	

	public static void main(String[] args) throws FileNotFoundException, JWNLException
	{
		String[] words = {"observe","talk","live","die"};
		//		System.out.println(getDict().getMorphologicalProcessor().lookupAllBaseForms(POS.VERB,"eating"));
		for(String word : words)
		{

			Set<Synset> synsets = getSynsets(word);
			SortedSet<String> synonyms = new TreeSet<String>();

//			SortedSet<String> crossPosLemmas = new TreeSet<String>();
//			for(Synset synset: synsets)
//			{
//				Pointer[] pointers = synset.getPointers();
//				for(Pointer pointer : pointers)
//				{					
//					if(pointer.getType()!=PointerType.NOMINALIZATION) continue;					
//					for(Pointer pointerPointer: pointer.getTarget().getPointers())
//					{
//						for(Word w : pointerPointer.getTargetSynset().getWords()) 
//							crossPosLemmas.add(w.getLemma());
//					}
//				}
//				synonyms.addAll(getSynsetLemmas(synset));
//			}
			//				System.out.println(getPointerTargetNodeListLemmas(PointerUtils.getInstance().getParticipleOf(synset)));
			//				System.out.println(getPointerTargetNodeListLemmas(PointerUtils.getInstance().getDerived(synset)));



			System.out.println("Synonyms: "+getSynonyms(word));
			System.out.println("Hypernyms: "+getHypernyms(word));
			System.out.println("Hyponyms: "+getHyponyms(word));
//			System.out.println("crossPosLemmas: "+crossPosLemmas);
			break;
		}
	}	
}