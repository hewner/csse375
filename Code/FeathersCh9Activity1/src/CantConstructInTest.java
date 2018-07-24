
public class CantConstructInTest {
	boolean var;
	public CantConstructInTest(boolean var) {
		this.var = var;
		throw new RuntimeException("You can't construct this class!");
	}
	
	public boolean isTrue() {
		return var;
	}
}
