package model;

public class FeatureVector {
	
	private boolean synonym = false;
	private boolean hyponym = false;
	private boolean hypernym = false;
	private boolean sameAs = false;
	private boolean seeAlso = false;
	//private boolean synonym = false;
	//private boolean synonym = false;
	//private boolean synonym = false;
	//private boolean synonym = false;
	
	/// ####################  SET FUNCTIONS ############################
	
	public void setsynonym (boolean s)
	{
		synonym = s;
	}
	
	public void sethyponym (boolean s)
	{
		hyponym = s;
	}
	
	
	public void sethypernym (boolean s)
	{
		hypernym = s;
	}
	
	
	public void setseeAlso (boolean s)
	{
		seeAlso = s;
	}
	
			public void setsameAs (boolean s)
	{
		sameAs = s;
	}
	
	
	
/// ####################  GET FUNCTIONS ############################
	
			public boolean getsynonym ()
			{
				return synonym;
			}
			
			public boolean gethyponym ()
			{
				return hyponym ;
			}
			
			
			public boolean gethypernym()
			{
				return hypernym;
			}
			
			
			public boolean getseeAlso()
			{
				return seeAlso;
			}
			
			public boolean getsameAs ()
			{
				return sameAs;
			}

}
