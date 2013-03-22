package model;

public class FeatureVector {
	
	private boolean synonym 		= false;
	private boolean hyponym 		= false;
	private boolean hypernym 		= false;
	private boolean sameAs 			= false;
	private boolean seeAlso 		= false;
	private boolean equivalent  	= false;
    private boolean superresource 	= false;
	private boolean subresource 	= false;
	private boolean skosbroader     = false;
	private boolean skosnarrower     = false;
	private boolean skosrelatedproperty     = false;
	
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
	
	public void setsameAs (boolean s)
	{
		sameAs = s;
	}
		
	public void setseeAlso (boolean s)
	{
		seeAlso = s;
	}
	
	public void setequivalent (boolean s)
	{
		equivalent = s;
	}
	
	public void setsuperresource (boolean s)
	{
		superresource = s;
	}
	
	public void setsubresource (boolean s)
	{
		subresource = s;
	}
	
	public void setskosbroader (boolean s)
	{
		skosbroader = s;
	}
	
	public void setskosnarrower (boolean s)
	{
		skosnarrower = s;
	}
	
	public void setskosrelatedproperty (boolean s)
	{
		skosrelatedproperty = s;
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
			
			public boolean getequivalent ()
			{
				return equivalent;
			}
			
			public boolean getsuperresource ()
			{
				return superresource;
			}
			
			public boolean getsubresource ()
			{
				return subresource;
			}
			
			public boolean getskosbroader ()
			{
				return skosbroader;
			}
			
			public boolean getskosnarrower ()
			{
				return skosnarrower;
			}
			
			public boolean getskosrelatedproperty ()
			{
				return skosrelatedproperty;
			}
			
			
	/// %%%%%%%%%%%%%%%%%%%%%%%%%%% printing function %%%%%%%%%%%%%%%%%
			
			public String printFeatureVector()
			
			{
				String pfv="";
				if(this.getsynonym())
				{
					pfv = pfv + "1,";
				}
				else
				{
				    pfv = pfv + "0,";
				}
				//---------------
				if(this.gethyponym())
				{
					pfv = pfv + "1,";
				}
				else
				{
				    pfv = pfv + "0,";
				}
				//---------------
				if(this.gethypernym())
				{
					pfv = pfv + "1,";
				}
				else
				{
				    pfv = pfv + "0,";
				}
				//---------------
				
				if(this.getsameAs())
				{
					pfv = pfv + "1,";
				}
				else
				{
				    pfv = pfv + "0,";
				}
				//---------------
				
				if(this.getseeAlso())
				{
					pfv = pfv + "1,";
				}
				else
				{
				    pfv = pfv + "0,";
				}
				//---------------
				if(this.getequivalent())
				{
					pfv = pfv + "1,";
				}
				else
				{
				    pfv = pfv + "0,";
				}
				//---------------
				if(this.getsuperresource())
				{
					pfv = pfv + "1,";
				}
				else
				{
				    pfv = pfv + "0,";
				}
				//---------------
				if(this.getsubresource())
				{
					pfv = pfv + "1,";
				}
				else
				{
				    pfv = pfv + "0,";
				}
				//---------------
				if(this.getskosbroader())
				{
					pfv = pfv + "1,";
				}
				else
				{
				    pfv = pfv + "0,";
				}
				//---------------
				
				if(this.getskosnarrower())
				{
					pfv = pfv + "1,";
				}
				else
				{
				    pfv = pfv + "0,";
				}
				//---------------
				if(this.getskosrelatedproperty())
				{
					pfv = pfv + "1";
				}
				else
				{
				    pfv = pfv + "0";
				}
				//---------------
				
				return pfv;
			}
			
			
			

}
