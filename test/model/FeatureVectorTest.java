package model;

import model.FeatureVector.Feature;
import org.junit.Test;

public class FeatureVectorTest
{

	@Test
	public void testToString()
	{
		FeatureVector f = new FeatureVector();
		f.add(Feature.EQUIVALENT);
		f.add(Feature.SYNONYM);
		System.out.println(f);
	}

}