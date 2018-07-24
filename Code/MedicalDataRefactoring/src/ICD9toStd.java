
public class ICD9toStd {
	private static ICD9toStd INSTANCE;
	 
	 public static ICD9toStd getInstance() {
	        if (INSTANCE == null) {
	            INSTANCE = new ICD9toStd();
	        }
	        return INSTANCE;
	    }

	 public static String formatICD9Code(String ICD9Code)
		{
			String tempCode1,tempCode2,tempCode3;
			if(ICD9Code==null)
				return null;
			if(!ICD9Code.contains(".") && !ICD9Code.contains("V"))
			{
				if(ICD9Code.length()==3)
				{
					if(ICD9Code.charAt(0)=='0')
						ICD9Code="V"+ICD9Code.substring(1)+".00";	
					else 
					    ICD9Code=ICD9Code+".00";
				}
				else if(ICD9Code.length()==2)
				{
					ICD9Code="V"+ICD9Code+".00";
				}
				else if(ICD9Code.length()==4)
				{
					if(ICD9Code.charAt(0)=='0')
					{
						tempCode1=ICD9Code.substring(1, 3);
						tempCode2=ICD9Code.substring(3);
						ICD9Code="V"+tempCode1+"."+tempCode2+"0";
					}
					else
					{
						tempCode1=ICD9Code.substring(0, 3);
						tempCode2=ICD9Code.substring(3);
						ICD9Code=tempCode1+"."+tempCode2+"0";
					}
				}
				else if(ICD9Code.length()==5)
				{
					if(ICD9Code.charAt(0)=='0')
					{
						tempCode1=ICD9Code.substring(1, 3);
						tempCode2=ICD9Code.substring(3);
						ICD9Code="V"+tempCode1+"."+tempCode2;
					}
					else	
					{
					    tempCode1=ICD9Code.substring(0, 3);
					    tempCode2=ICD9Code.substring(3);
					    ICD9Code=tempCode1+"."+tempCode2;
					}
				}
			}
			else if(!ICD9Code.contains(".") && ICD9Code.contains("V"))
			{
				if(ICD9Code.length()==3)
				{
					ICD9Code=ICD9Code+".00";
				}
				else if(ICD9Code.length()==4)
				{
					tempCode1=ICD9Code.substring(0, 4);
					tempCode2=ICD9Code.substring(4);
					ICD9Code=tempCode1+"."+tempCode2+"0";
				}
				else if(ICD9Code.length()==5)
				{
					tempCode1=ICD9Code.substring(0, 3);
					tempCode2=ICD9Code.substring(3);
					ICD9Code=tempCode1+"."+tempCode2;
				}
			}
			else if(ICD9Code.contains(".") && !ICD9Code.contains("V"))
			{
				if(ICD9Code.length()==6)
				{
					if(ICD9Code.charAt(0)=='0')
					{
						tempCode1=ICD9Code.substring(1, 3);
						tempCode2=ICD9Code.substring(3);
						ICD9Code="V"+tempCode1+tempCode2;
					}
					else
					{
						tempCode1=ICD9Code.substring(0, 3);
						tempCode2=ICD9Code.substring(3);
						ICD9Code=tempCode1+tempCode2;
					}
				}
				if(ICD9Code.length()==5)
				{
					if(ICD9Code.charAt(0)=='0')
					{
						tempCode1=ICD9Code.substring(1, 3);
						tempCode2=ICD9Code.substring(3);
						ICD9Code="V"+tempCode1+tempCode2+"0";
					}
					else
					{
						tempCode1=ICD9Code.substring(0, 3);
						tempCode2=ICD9Code.substring(3);
						ICD9Code=tempCode1+tempCode2+"0";
					}
				}
			}
			else if(ICD9Code.contains(".") && ICD9Code.contains("V"))
			{
			  if(ICD9Code.length()==5)
			  {
				  tempCode1=ICD9Code.substring(0, 3);
				  tempCode2=ICD9Code.substring(3);
				  ICD9Code=tempCode1+tempCode2+"0";
			  }
			  else if(ICD9Code.length()==6)
			  {
				  tempCode1=ICD9Code.substring(0, 3);
				  tempCode2=ICD9Code.substring(3);
				  ICD9Code=tempCode1+tempCode2;
			  }
				  
			}
			
				
			return ICD9Code;
		}
}
