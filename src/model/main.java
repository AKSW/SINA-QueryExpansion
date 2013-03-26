package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.collections15.MultiMap;
import org.apache.commons.collections15.multimap.MultiHashMap;

import model.FeatureVector.Feature;
import net.didion.jwnl.JWNLException;

public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		
		ReasoningOverLOD ro = new ReasoningOverLOD();
		WordNetManagement wm = new WordNetManagement();
		CorrelatedResources cr = new CorrelatedResources();
		ImportExport IE = new ImportExport();
		
		String pattern="battle";
		
		Map<String,FeatureVector> Comprehensivemap 		    =           new HashMap<String, FeatureVector>();
		Set<String> ComprehensiveSet				    	=			new TreeSet<String>();
		Set<String> SameAsLabelSet						    =			new TreeSet<String>();	
		Set<String> SeeAlsoLabelSet							=			new TreeSet<String>();	
		Set<String> EquivalentLabeSet						=			new TreeSet<String>();
		Set<String> SuperResourceLabelSet					=			new TreeSet<String>();
		Set<String> SubResourceLabelsub						=			new TreeSet<String>();
		Set<String> BroaderLabelSet						    =			new TreeSet<String>();
		Set<String> NarrowerLabelSet						=			new TreeSet<String>();
		Set<String> RelatedPropertyLabelSet					=			new TreeSet<String>();
		SortedSet<String> synSet 							= 			new TreeSet<String>();
		SortedSet<String> hyponymSet					    = 			new TreeSet<String>();
		SortedSet<String> hyperSet 							= 			new TreeSet<String>();
		Set<String> CorrespondingSet 						= 			new TreeSet<String>();
		MultiMap<String,String> RelatedPatterns		    = new MultiHashMap<>();
		
		File f =new File("output/qald.tsv");
		try {
			RelatedPatterns.putAll(IE.readTsvCsvToMultiMap(f));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Iterator<Entry<String, Collection<String>>> iter = RelatedPatterns.entrySet().iterator();
		while (iter.hasNext()) {
		Entry<String, Collection<String>> entry = iter.next();
	    String word =entry.getKey();
	    Collection<String> FV = entry.getValue();
	    String vector = FV.toString(); 
	     System.out.println(word + "   " + vector);
	        
		}
		
	
		System.out.println(" size of the correlated resource" + RelatedPatterns.size());
		
		/*SameAsLabelSet.addAll(ro.getLabelsViaSameAsOverLOD(pattern));
		System.out.println(" %%%%%%%%%%%%%% the list of SameAs labels %%%%%%%%%%%%%%");
		for (String s:SameAsLabelSet)
		{
			System.out.println(s);
		}
		
		ComprehensiveSet.addAll(SameAsLabelSet);
			
		
		SeeAlsoLabelSet.addAll(ro.getLabelsViaSeeAlsoOverLOD(pattern));
		System.out.println(" %%%%%%%%%%%%%% the list of labels SeeAlso %%%%%%%%%%%%%% ");
		for (String s:SeeAlsoLabelSet)
		{
			System.out.println(s);
		}
		ComprehensiveSet.addAll(SeeAlsoLabelSet);
		
		
		EquivalentLabeSet.addAll(ro.getLabelsViaEquivalentOverLOD(pattern));
		System.out.println(" %%%%%%%%%%%%%% the list of labels equivalent %%%%%%%%%%%%%");
		for (String s:EquivalentLabeSet)
		{
			System.out.println(s);
		}
		ComprehensiveSet.addAll(EquivalentLabeSet);
		
		
		
		SuperResourceLabelSet.addAll(ro.getLabelsViaSuperOverLOD(pattern));
		System.out.println(" %%%%%%%%%%%%%% the list of labels super  %%%%%%%%%%%%%");
		for (String s:SuperResourceLabelSet)
		{
			System.out.println(s);
		}
		ComprehensiveSet.addAll(SuperResourceLabelSet);
		
		
		SubResourceLabelsub.addAll(ro.getLabelsViaSubOverLOD(pattern));
		System.out.println(" %%%%%%%%%%%%%% the list of labels sub  %%%%%%%%%%%%%");
		for (String s:SubResourceLabelsub)
		{
			System.out.println(s);
		}
		ComprehensiveSet.addAll(SubResourceLabelsub);
		
		// --------------------------------------------------------------------------------------
		try {
			synSet.addAll(wm.getSynonyms(pattern));
		} catch (FileNotFoundException | JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(" %%%%%%%%%%%%%% the list of labels Synset %%%%%%%%%%%%%%");
		for (String s:synSet)
		{
			System.out.println(s);
		}
		ComprehensiveSet.addAll(synSet);
		//-----------------------------------  Hyponyms  ----------------------------------------------
				
		try {
			hyponymSet.addAll(wm.getHyponyms(pattern));
		} catch (FileNotFoundException | JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(" the list of labels hyponyms");
		for (String s:hyponymSet)
		{
			System.out.println(s);
		}
		
		ComprehensiveSet.addAll(hyponymSet);
		//-------------------------------  list of hypernyms ------------------------------------------   
		try {
			hyperSet.addAll(wm.getHypernyms(pattern));
		} catch (FileNotFoundException | JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(" the list of labels hypenyms");
		for (String s:hyperSet)
		{
			System.out.println(s);
		}
		ComprehensiveSet.addAll(hyperSet);
		
		//-------------------------------  list of broader concepts ------------------------------------------   
				
		BroaderLabelSet.addAll(ro.getLabelsViaSkosbroaderOverLOD(pattern));
		System.out.println(" the list of labels broader");
		for (String s:BroaderLabelSet)
		{
			System.out.println(s);
			}
		ComprehensiveSet.addAll(BroaderLabelSet);
		
		//-------------------------------  list of narrower concepts ------------------------------------------   
		
		NarrowerLabelSet.addAll(ro.getLabelsViaSkosnarrowerOverLOD(pattern));
		System.out.println(" the list of narrower labels");
		for (String s:NarrowerLabelSet)
		 {
			System.out.println(s);
		 }
		ComprehensiveSet.addAll(NarrowerLabelSet);
				
		
		//-------------------------------  list of related properties ------------------------------------------   
		
		RelatedPropertyLabelSet.addAll(ro.getLabelsViaSkosPropertyMatichOverLOD(pattern));
				System.out.println(" the list of related properties Labels");
				for (String s:RelatedPropertyLabelSet)
				 {
					System.out.println(s);
				 }
				ComprehensiveSet.addAll(RelatedPropertyLabelSet);
		//-----------------------------------------------------------------------------------------------
		System.out.println(" size of comprehensive set is:" + ComprehensiveSet.size());
		

	*/
	
/*	for (String s:ComprehensiveSet)
	{
		FeatureVector fv = new FeatureVector();
		List<Set> sets = Arrays.asList(
				new Set[] {synSet,hyponymSet,hyperSet,SameAsLabelSet,SeeAlsoLabelSet,EquivalentLabeSet,SuperResourceLabelSet,SubResourceLabelsub,
						BroaderLabelSet,NarrowerLabelSet,RelatedPropertyLabelSet});		
		for(int i=0;i<Feature.values().length;i++) {if(sets.get(i).contains(s)) fv.add(Feature.values()[i]);}
		
		Comprehensivemap.put(s, fv);
	}
	Iterator<Entry<String, FeatureVector>> iter = Comprehensivemap.entrySet().iterator();
	while (iter.hasNext()) {
	Entry<String, FeatureVector> entry = iter.next();
    String word =entry.getKey();
    FeatureVector FV = entry.getValue();
    String vector = FV.toString(); 
     System.out.println(word + "   " + vector);
        
	}
	
*/	
	
	
	}
}
