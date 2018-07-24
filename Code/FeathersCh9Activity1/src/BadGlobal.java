
public class BadGlobal {
	private static BadGlobal instance = null;
	private BadGlobal(String config) {
		if(config.equals("connectToDB"))
			throw new RuntimeException("Can't connect to DB in tests");
	}
	
	public static BadGlobal getInstance() {
		if(instance == null) {
			instance = new BadGlobal("connectToDB");
		}
		return instance;
	}
	
	public void doThing(String s) { }
}
