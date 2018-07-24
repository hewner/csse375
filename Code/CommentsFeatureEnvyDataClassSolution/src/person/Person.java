package person;
public class Person {
    public String last;
    public String first;
    public String middle;

    public Person(String last, String first, String middle) {
        this.last = last;
        this.first = first;
        this.middle = middle;
    }
    
    private String maybeMiddle() {
    	if(middle != null)
    		return " " + middle;
    	return "";
    }
    
    public String firstMiddleLast() {
    	return first + maybeMiddle() + " " + last;
    }
    
    public String lastFirstMiddle() {
    	return last + ", " + first + maybeMiddle();
    }
}