import java.io.IOException;
import java.io.OutputStream;


public class LoginCommand extends Command {
	private String userName;
	private String password;
	public LoginCommand(String userName, String passwd) {
		addField(userName);
		addField(passwd);
	}
		
	@Override
	protected byte[] commandChar() {
		return new byte[] {0x01};
	}
}
