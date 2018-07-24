import java.io.IOException;
import java.io.OutputStream;

public class AddEmployeeCmd extends Command{
	String name;
	String address;
	String city;
	String state;
	String yearlySalary;
	
	public AddEmployeeCmd(String name, String address, String city, String state, int yearlySalary) 
	{
		addField(name);
		addField(address);
		addField(city);
		addField(state);
		addField(Integer.toString(yearlySalary));
	}
	


	@Override
	protected byte[] commandChar() {
		return new byte[] {0x02};
	}
	
}
