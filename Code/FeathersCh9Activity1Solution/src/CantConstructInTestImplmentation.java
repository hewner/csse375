
public class CantConstructInTestImplmentation implements CantConstructInTest {
	boolean var;
	public CantConstructInTestImplmentation(boolean var) {
		this.var = var;
		throw new RuntimeException("You can't construct this class!");
	}
	
	/* (non-Javadoc)
	 * @see CantConstructInTestInterface#isTrue()
	 */
	@Override
	public boolean isTrue() {
		return var;
	}
}
