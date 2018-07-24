package matcher;
public class Matcher {

	public Matcher() {}

    public boolean match(int[] expected, int[] actual, 
        int clipLimit, int delta) 
    {
    	
    	/*******
    	 * There are many ways you could divide this up.
    	 * All are at least OK.
    	 * 
    	 * Here's mine:
    	 * 
    	 */

        limitArrayToMax(actual, clipLimit);

        /*****
         * Some code in my opinion is clear without ornimentation
         */
        if (actual.length != expected.length)
            return false;

        
        /*****
         * Of course I could have made a function for this whole
         * block too.  But I thought it's name would collide with
         * this name, and the iteration speaks for itself.
         */        
        for (int i = 0; i < actual.length; i++)
            if (!isWithinDeltaOf(expected[i], actual[i], delta))
                return false;

        return true;
    }
    
	private void limitArrayToMax(int[] actual, int max) {
		for (int i = 0; i < actual.length; i++)
            if (actual[i] > max)
                actual[i] = max;
	}
	
	private boolean isWithinDeltaOf(int a, int b, int delta) 
	{
		return Math.abs(a - b) <= delta;
	}
    

}