import java.io.IOException;
import java.io.OutputStream;

public class AddEmployeeCmd {
	String name;
	String address;
	String city;
	String state;
	String yearlySalary;
	private static final byte[] header = {(byte) 0xde, (byte) 0xad};
	private static final byte[] commandChar = {0x02};
	private static final byte[] footer = {(byte) 0xde, (byte) 0xef};
	private static final int SIZE_LENGTH = 1;
	private static final int CMD_BYTE_LENGTH = 1;
	
	public int getSize() {
		return header.length +
				SIZE_LENGTH +
				CMD_BYTE_LENGTH +
				footer.length +
				name.getBytes().length + 1 +
				address.getBytes().length + 1 +
				city.getBytes().length + 1 +
				state.getBytes().length + 1 +
				yearlySalary.getBytes().length + 1;
	}
	
	public AddEmployeeCmd(String name, String address, String city, String state, int yearlySalary) 
	{
		this.name = name;
		this.address = address;
		this.city = city;
		this.state = state;
		this.yearlySalary = Integer.toString(yearlySalary);
	}
	
	public void write(OutputStream output) throws IOException
	{
		output.write(header);
		output.write(getSize());
		output.write(commandChar);
		output.write(name.getBytes());
		output.write(0x00);
		output.write(address.getBytes());
		output.write(0x00);
		output.write(city.getBytes());
		output.write(0x00);
		output.write(state.getBytes());
		output.write(0x00);
		output.write(yearlySalary.getBytes());
		output.write(0x00);
		output.write(footer);
	}
}
