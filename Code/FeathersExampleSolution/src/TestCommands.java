import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;


public class TestCommands {

	@Test
	public void testLoginGetSize() throws IOException {
		LoginCommand c = new LoginCommand("u","p");
		assertEquals(10, c.getSize());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		c.write(output);
		assertEquals(10, output.toByteArray().length);
	}

	@Test
	public void testLoginWrite() throws IOException {
		byte[] result = {(byte) 0xde, (byte) 0xad, 12, 0x01, 'u', 'n', 0, 'p', 'd', 0, (byte)0xde, (byte)0xef};
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		LoginCommand c = new LoginCommand("un","pd");
		c.write(output);
		assertArrayEquals(result, output.toByteArray());
	}
	
	@Test
	public void testAddGetSize() throws IOException {
		AddEmployeeCmd c = new AddEmployeeCmd("Joe","1 Foo Rd.","Terre Haute","IN",100);
		assertEquals(39, c.getSize());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		c.write(output);
		assertEquals(39, output.toByteArray().length);		
	}

	@Test
	public void testAddWrite() throws IOException {
		byte[] result = {(byte) 0xde, (byte) 0xad, 22, 0x02, 'J', 'o', 'e', 0, '1', 0, 't','h', 0, 'I', 'N', 0, '1','0','0',0, (byte)0xde, (byte)0xef};
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		AddEmployeeCmd c = new AddEmployeeCmd("Joe","1","th","IN",100);
		c.write(output);
		assertArrayEquals(result, output.toByteArray());
	}
	
}
