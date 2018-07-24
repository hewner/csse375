
public class BadGlobal implements BadGlobaInterface {
	private static BadGlobaInterface instance = null;
	private BadGlobal(String config) {
		if(config.equals("connectToDB"))
			throw new RuntimeException("Can't connect to DB in tests");
	}
	
	public static BadGlobaInterface getInstance() {
		if(instance == null) {
			instance = new BadGlobal("connectToDB");
		}
		return instance;
	}
	
	public static void TEST_ONLY_setInstance(BadGlobaInterface testGlobal) {
		instance = testGlobal;
	}
	
	/* (non-Javadoc)
	 * @see BadGlobaInterface#doThing(java.lang.String)
	 */
	@Override
	public void doThing(String s) { }
}
