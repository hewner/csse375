import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;

public class CohortTableData {

	public void CohortTableDataProcessing(String datafile) throws IOException, ClassNotFoundException, SQLException
	{
		TreeMap<Integer, CohortEntity> uniqueCohort=new TreeMap<Integer, CohortEntity>();
		ICD9toStd instance1;
		ICD9toStd instance2;
		CohortEntity cohort;
		
		//creating diseaseHashMap
		String Line,lineSplit[];
		TreeMap<String, String> diagCodeMap=new TreeMap<String, String>();
		BufferedReader formattedFile=new BufferedReader(new FileReader("FormattedDiag_cd_list3.3.txt"));
		formattedFile.readLine();
    	while((Line=formattedFile.readLine())!=null)
    	{
    		String tempCode1,tempCode2;
    		lineSplit=Line.split(",");  
    		//System.out.println(lineSplit[0]+"-----"+lineSplit[lineSplit.length-1]);
    		//System.out.println("lineSplit length:"+lineSplit.length);
    		diagCodeMap.put(lineSplit[0],lineSplit[lineSplit.length-1]);
    	}
		
   	
        int i=0,MRN=0;
        
        formattedFile=new BufferedReader(new FileReader(datafile));
		formattedFile.readLine();
    	while((Line=formattedFile.readLine())!=null)
        {
        	int count=0;
        	//System.out.println("coming here:"+i++);
        	String diseaseCode1,diseaseCode2;
        	lineSplit=Line.split(",");
        	MRN=Integer.parseInt(lineSplit[0]);
        	instance1=ICD9toStd.getInstance();
        	if(uniqueCohort.containsKey(MRN))
        	{
        		//System.out.println("Contains MRN");
        		
        		diseaseCode1=instance1.formatICD9Code(lineSplit[1]);
        		diseaseCode1=diagCodeMap.get(diseaseCode1);
        		diseaseCode2=instance1.formatICD9Code(lineSplit[2]);
        		diseaseCode2=diagCodeMap.get(diseaseCode2);
        		CohortEntity extractedCohort=uniqueCohort.get(MRN);
        		extractedCohort.setCount((extractedCohort.getCount())+1);
        		if(diseaseCode1!=null)
        		    extractedCohort.checkD1Map(diseaseCode1);
        		if(diseaseCode2!=null)
        		    extractedCohort.checkD2Map(diseaseCode2);
        	}
        	else
        	{
        		//System.out.println("Doesn't contain MRN:");
        		instance2=ICD9toStd.getInstance();
        		diseaseCode1=instance1.formatICD9Code(lineSplit[1]);
        		if(diseaseCode1!=null)
        		   diseaseCode1=diagCodeMap.get(diseaseCode1);
        		else 
        		   diseaseCode1="";
        		diseaseCode2=instance1.formatICD9Code(lineSplit[2]);
        		if(diseaseCode2!=null)
        		   diseaseCode2=diagCodeMap.get(diseaseCode2);
        		else
        			diseaseCode2="";
        		cohort=new CohortEntity(diseaseCode1, diseaseCode2);
        		uniqueCohort.put(MRN, cohort);
        	}
        }
        
        int uniqueRecords=0;
        //Trying to print the final Disease Map
        for(Entry<Integer, CohortEntity> entry:uniqueCohort.entrySet())
        {
        	//System.out.println("Key:"+entry.getKey());
        	CohortEntity obj=entry.getValue();
        	TreeMap<String,Integer> finalDiseaseMap=new TreeMap<String, Integer>();
        	
        	for(Map.Entry<String, Integer> disease1Map:obj.d1Map.entrySet())
        	{
        		if(!finalDiseaseMap.containsKey(disease1Map.getKey()))
        		{
        			finalDiseaseMap.put(disease1Map.getKey(),disease1Map.getValue());
        		}
        		else
        		{
        			finalDiseaseMap.put(disease1Map.getKey(),disease1Map.getValue()+1);
        		}
        			
        	}
        	

        	for(Map.Entry<String, Integer> disease2Map:obj.d2Map.entrySet())
        	{
        		if(!finalDiseaseMap.containsKey(disease2Map.getKey()))
        		{
        			finalDiseaseMap.put(disease2Map.getKey(),disease2Map.getValue());
        		}
        		else
        		{
        			int disease2Value=finalDiseaseMap.get(disease2Map.getKey())+disease2Map.getValue();
        			finalDiseaseMap.put(disease2Map.getKey(),disease2Value);
        		}
        			
        	}
        	obj.setTotalDiseases(finalDiseaseMap);
        	uniqueRecords++;
        }
        System.out.println("Unique Records:"+uniqueRecords);
        
        
        System.out.println("Lets try to print the data:");
        for(Entry<Integer, CohortEntity> finalEntry:uniqueCohort.entrySet())
        {
        	//ComprehensiveEntity compEntity=new ComprehensiveEntity();
        	System.out.println("MRN:"+finalEntry.getKey());
        	CohortEntity obj=finalEntry.getValue();
        	obj.setNum_of_visits(obj.count);
        	System.out.println("Count of this MRN:"+obj.count);
        	System.out.println("Disease for this guy:");
        	for(Map.Entry<String, Integer> finalDiseases:obj.getTotalDiseases().entrySet())
        	{
        		if(finalDiseases.getKey()!=null)
        		{
//        			if(finalDiseases.getKey().equalsIgnoreCase("DM"))
//         			{
//         				obj.setDM_no(finalDiseases.getValue());
//         				obj.setDM(1);
//         			}
        			if(finalDiseases.getKey().equalsIgnoreCase("asth"))
        			{
        				obj.setASTH_no(finalDiseases.getValue());
        				obj.setASTH(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("CAD"))
        			{
        				obj.setCAD_no(finalDiseases.getValue());
        				obj.setCAD(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("CKD"))
        			{
        				obj.setCKD_no(finalDiseases.getValue());
        				obj.setCKD(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("COPD"))
        			{
        				obj.setCOPD_no(finalDiseases.getValue());
        				obj.setCOPD(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("MH"))
        			{
        				obj.setMH_no(finalDiseases.getValue());
        				obj.setMH(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("DM"))
        			{
        				obj.setDM_no(finalDiseases.getValue());
        				obj.setDM(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("HF"))
        			{
        				obj.setHF_no(finalDiseases.getValue());
        				obj.setHF(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("HTN"))
        			{
        				obj.setHTN_no(finalDiseases.getValue());
        				obj.setHTN(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("LD"))
        			{
        				obj.setLD_no(finalDiseases.getValue());
        				obj.setLD(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("SA"))
        			{
        				obj.setSA_no(finalDiseases.getValue());
        				obj.setSA(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("Smoker"))
        			{
        				obj.setSMOKER_no(finalDiseases.getValue());
        				obj.setSMOKER(1);
        			}
        			else if(finalDiseases.getKey().equalsIgnoreCase("Obesity"))
        			{
        				obj.setOBESITY_no(finalDiseases.getValue());
        				obj.setOBESITY(1);
        			}
        		}
        		  System.out.println("Disease Code:"+finalDiseases.getKey()+" count of disease:"+finalDiseases.getValue());
        	}
        }
             	
	}
	
	public static void main(String[] args) throws ClassNotFoundException, IOException, SQLException {
		CohortTableData data = new CohortTableData();
		
		//if you would like to redirect the output, uncomment these lines
		//PrintStream ps = new PrintStream("output.txt");
		//System.setOut(ps);
		data.CohortTableDataProcessing("sampledata_1.txt");
	}
}
