
import java.util.TreeMap;

public class CohortEntity {

	int MRN;
	String diseaseCode1,diseaseCode2,diseaseCode3,diseaseCode4,diseaseCode5;
	String ageRange;
	String disease_category;
	String segment;
	String chronic;
	String policy;
	String CIN;
	int age;
	int disease_rank;
	int num_of_visits;
	int ASTH_no;
	int CAD_no;
	int CKD_no;
	int COPD_no;
	int MH_no;
	int HF_no;
	int DM_no;
	int HTN_no;
	int LD_no;
	int SA_no;
	int SMOKER_no;
	int OBESITY_no;
	int ASTH;
	int CAD;
	int CKD;
	int COPD;
	int MH;
	int HF;
	int DM;
	int HTN;
	int LD;
	int SA;
	int SMOKER;
	int OBESITY;
	String relativeRisk;
	
	public String getRelativeRisk() {
		return relativeRisk;
	}

	public void setRelativeRisk(String relativeRisk) {
		this.relativeRisk = relativeRisk;
	}

	public String getCIN() {
		return CIN;
	}

	public void setCIN(String cIN) {
		CIN = cIN;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}
	
	public String getDiseaseCode1() {
		return diseaseCode1;
	}

	public void setDiseaseCode1(String diseaseCode1) {
		this.diseaseCode1 = diseaseCode1;
	}

	public String getDiseaseCode2() {
		return diseaseCode2;
	}

	public void setDiseaseCode2(String diseaseCode2) {
		this.diseaseCode2 = diseaseCode2;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}

	public String getDisease_category() {
		return disease_category;
	}

	public void setDisease_category(String disease_category) {
		this.disease_category = disease_category;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getChronic() {
		return chronic;
	}

	public void setChronic(String chronic) {
		this.chronic = chronic;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getDisease_rank() {
		return disease_rank;
	}

	public void setDisease_rank(int disease_rank) {
		this.disease_rank = disease_rank;
	}

	public int getNum_of_visits() {
		return num_of_visits;
	}

	public void setNum_of_visits(int num_of_visits) {
		this.num_of_visits = num_of_visits;
	}

	public int getASTH_no() {
		return ASTH_no;
	}

	public void setASTH_no(int aSTH_no) {
		ASTH_no = aSTH_no;
	}

	public int getCAD_no() {
		return CAD_no;
	}

	public void setCAD_no(int cAD_no) {
		CAD_no = cAD_no;
	}

	public int getCKD_no() {
		return CKD_no;
	}

	public void setCKD_no(int cKD_no) {
		CKD_no = cKD_no;
	}

	public int getCOPD_no() {
		return COPD_no;
	}

	public void setCOPD_no(int cOPD_no) {
		COPD_no = cOPD_no;
	}

	public int getMH_no() {
		return MH_no;
	}

	public void setMH_no(int mH_no) {
		MH_no = mH_no;
	}

	public int getHF_no() {
		return HF_no;
	}

	public void setHF_no(int hF_no) {
		HF_no = hF_no;
	}

	public int getDM_no() {
		return DM_no;
	}

	public void setDM_no(int dM_no) {
		DM_no = dM_no;
	}

	public int getHTN_no() {
		return HTN_no;
	}

	public void setHTN_no(int hTN_no) {
		HTN_no = hTN_no;
	}

	public int getLD_no() {
		return LD_no;
	}

	public void setLD_no(int lD_no) {
		LD_no = lD_no;
	}

	public int getSA_no() {
		return SA_no;
	}

	public void setSA_no(int sA_no) {
		SA_no = sA_no;
	}

	public int getSMOKER_no() {
		return SMOKER_no;
	}

	public void setSMOKER_no(int sMOKER_no) {
		SMOKER_no = sMOKER_no;
	}

	public int getOBESITY_no() {
		return OBESITY_no;
	}

	public void setOBESITY_no(int oBESITY_no) {
		OBESITY_no = oBESITY_no;
	}

	public int getASTH() {
		return ASTH;
	}

	public void setASTH(int aSTH) {
		ASTH = aSTH;
	}

	public int getCAD() {
		return CAD;
	}

	public void setCAD(int cAD) {
		CAD = cAD;
	}

	public int getCKD() {
		return CKD;
	}

	public void setCKD(int cKD) {
		CKD = cKD;
	}

	public int getCOPD() {
		return COPD;
	}

	public void setCOPD(int cOPD) {
		COPD = cOPD;
	}

	public int getMH() {
		return MH;
	}

	public void setMH(int mH) {
		MH = mH;
	}

	public int getHF() {
		return HF;
	}

	public void setHF(int hF) {
		HF = hF;
	}

	public int getDM() {
		return DM;
	}

	public void setDM(int dM) {
		DM = dM;
	}

	public int getHTN() {
		return HTN;
	}

	public void setHTN(int hTN) {
		HTN = hTN;
	}

	public int getLD() {
		return LD;
	}

	public void setLD(int lD) {
		LD = lD;
	}

	public int getSA() {
		return SA;
	}

	public void setSA(int sA) {
		SA = sA;
	}

	public int getSMOKER() {
		return SMOKER;
	}

	public void setSMOKER(int sMOKER) {
		SMOKER = sMOKER;
	}

	public int getOBESITY() {
		return OBESITY;
	}

	public void setOBESITY(int oBESITY) {
		OBESITY = oBESITY;
	}

	public TreeMap<String, Integer> getD1Map() {
		return d1Map;
	}

	public TreeMap<String, Integer> getD2Map() {
		return d2Map;
	}
	

	
	public TreeMap<String, Integer> d1Map=new TreeMap<String, Integer>();
	public TreeMap<String, Integer> d2Map=new TreeMap<String, Integer>();
	public TreeMap<String, Integer> d3Map=new TreeMap<String, Integer>();
	public TreeMap<String, Integer> d4Map=new TreeMap<String, Integer>();
	public TreeMap<String, Integer> d5Map=new TreeMap<String, Integer>();
	
	public TreeMap<String, Integer> TotalDiseases=new TreeMap<String, Integer>();
	public int count=0;
	public TreeMap<String, Integer> getTotalDiseases() {
		return TotalDiseases;
	}

	public void setTotalDiseases(TreeMap<String, Integer> totalDiseases) {
		TotalDiseases = totalDiseases;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public CohortEntity()
	{
		
	}
	public CohortEntity(String diseaseCode1,String diseaseCode2)
	{
		this.diseaseCode1=diseaseCode1;
		this.diseaseCode2=diseaseCode2;
		if(diseaseCode1==null)
			;
		else if(diseaseCode1.equalsIgnoreCase(""))
			;
		else
		  d1Map.put(diseaseCode1, 1);
		
		if(diseaseCode2==null)
			;
		else if(diseaseCode2.equalsIgnoreCase(""))
			;
		else
		  d2Map.put(diseaseCode2, 1);
		count++;
	}
	
	public CohortEntity(String diseaseCode1,String diseaseCode2,String diseaseCode3,String diseaseCode4,String diseaseCode5)
	{
		this.diseaseCode1=diseaseCode1;
		this.diseaseCode2=diseaseCode2;
		this.diseaseCode3=diseaseCode3;
		this.diseaseCode4=diseaseCode4;
		this.diseaseCode5=diseaseCode5;
		
		d1Map.put(diseaseCode1, 1);
		d2Map.put(diseaseCode2, 1);
		d3Map.put(diseaseCode3, 1);
		d4Map.put(diseaseCode4, 1);
		d5Map.put(diseaseCode5, 1);
		
		count++;
	}
	
	public String getDiseaseCode3() {
		return diseaseCode3;
	}

	public void setDiseaseCode3(String diseaseCode3) {
		this.diseaseCode3 = diseaseCode3;
	}

	public String getDiseaseCode4() {
		return diseaseCode4;
	}

	public void setDiseaseCode4(String diseaseCode4) {
		this.diseaseCode4 = diseaseCode4;
	}

	public String getDiseaseCode5() {
		return diseaseCode5;
	}

	public void setDiseaseCode5(String diseaseCode5) {
		this.diseaseCode5 = diseaseCode5;
	}

	public TreeMap<String, Integer> getD3Map() {
		return d3Map;
	}

	public void setD3Map(TreeMap<String, Integer> d3Map) {
		this.d3Map = d3Map;
	}

	public TreeMap<String, Integer> getD4Map() {
		return d4Map;
	}

	public void setD4Map(TreeMap<String, Integer> d4Map) {
		this.d4Map = d4Map;
	}

	public TreeMap<String, Integer> getD5Map() {
		return d5Map;
	}

	public void setD5Map(TreeMap<String, Integer> d5Map) {
		this.d5Map = d5Map;
	}

	public int getMRN() {
		return MRN;
	}
	public void setMRN(int mRN) {
		MRN = mRN;
	}
	public TreeMap<String, Integer> getDx1Map() {
		return d1Map;
	}
	public void setD1Map(TreeMap<String, Integer> dx1Map) {
		this.d1Map = dx1Map;
	}
	public TreeMap<String, Integer> getDx2Map() {
		return d2Map;
	}
	public void setD2Map(TreeMap<String, Integer> dx2Map) {
		this.d2Map = d2Map;
	}
	
	public void checkD1Map(String diseaseCode1)
	{
		if(d1Map.containsKey(diseaseCode1))
		{
			int count=d1Map.get(diseaseCode1);
			count++;
			d1Map.put(diseaseCode1, count);
		}
		else
			d1Map.put(diseaseCode1, 1);
	}
	
	public void checkD2Map(String diseaseCode2)
	{
		if(d2Map.containsKey(diseaseCode2))
		{
			int count=d2Map.get(diseaseCode2);
			count++;
			d2Map.put(diseaseCode2, count);
		}
		else
			d2Map.put(diseaseCode2, 1);
	}
	public void checkD3Map(String diseaseCode3)
	{
		if(d3Map.containsKey(diseaseCode3))
		{
			int count=d3Map.get(diseaseCode3);
			count++;
			d3Map.put(diseaseCode3, count);
		}
		else
			d3Map.put(diseaseCode3, 1);
	}
	
	public void checkD4Map(String diseaseCode4)
	{
		if(d4Map.containsKey(diseaseCode4))
		{
			int count=d4Map.get(diseaseCode4);
			count++;
			d4Map.put(diseaseCode4, count);
		}
		else
			d4Map.put(diseaseCode4, 1);
	}
	public void checkD5Map(String diseaseCode5)
	{
		if(d5Map.containsKey(diseaseCode5))
		{
			int count=d5Map.get(diseaseCode5);
			count++;
			d5Map.put(diseaseCode5, count);
		}
		else
			d5Map.put(diseaseCode5, 1);
	}
	
	
	
}
