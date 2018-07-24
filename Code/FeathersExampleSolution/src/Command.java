import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

abstract public class Command {

	protected static final byte[] header = {(byte) 0xde, (byte) 0xad};
	protected static final byte[] footer = {(byte) 0xde, (byte) 0xef};
	protected static final int SIZE_LENGTH = 1;
	protected static final int CMD_BYTE_LENGTH = 1;
	private List<String> fields;
	
	public Command() {
		fields = new ArrayList<String>();
	}

	protected void addField(String field)
	{
		fields.add(field);
	}
	
	protected void writeField(String field, OutputStream outputStream) throws IOException {
		outputStream.write(field.getBytes());
		outputStream.write(0x00);
	}

	
	protected abstract byte[] commandChar();

	protected void writeBody(OutputStream outputStream) throws IOException
	{
		for(String field: fields) {
			writeField(field, outputStream);
		}
		
	}
	protected int getBodySize()
	{
		int total = 0;
		for(String field: fields) {
			total += field.getBytes().length + 1;
		}
		return total;
	}
	
	public void write(OutputStream outputStream) throws IOException {
		outputStream.write(header);
		outputStream.write(getSize());
		outputStream.write(commandChar());
		writeBody(outputStream);
		outputStream.write(footer);
	}

	public int getSize() {
		return header.length + SIZE_LENGTH + CMD_BYTE_LENGTH + footer.length + getBodySize();
	}
}