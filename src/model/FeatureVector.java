package model;

import java.util.EnumSet;
import java.util.Iterator;

public class FeatureVector
{
	public enum Feature {
		SYNONYM,
		HYPONYM,
		HYPERNYM,
		SAMEAS,
		SEEALSO,
		EQUIVALENT,
		SUPERRESOURCE,
		SUBRESOURCE,
		SKOSBROADER,
		SKOSNARROWER,
		SKOSRELATEDPROPERTY,}

	public EnumSet<Feature> values = EnumSet.noneOf(Feature.class);

	public boolean add(Feature f)		{return values.add(f);}
	public boolean remove(Feature f)	{return values.remove(f);}			

	@Override public String toString()			
	{
		StringBuilder sb = new StringBuilder();
		for(Feature f: Feature.values())
		{
			sb.append((values.contains(f)?"1":"0")+",");
		}				
		return sb.substring(0,sb.length()-1);
	}
}