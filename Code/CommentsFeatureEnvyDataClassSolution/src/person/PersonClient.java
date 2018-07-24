package person;
// The clients are in one file for convenience; 
// imagine them as non-test methods in separate client classes.

import junit.framework.TestCase;

import java.io.*;

/**************
 * Like usual, the tricky part here was determining what was duplication
 * and what was different.  Once I knew that, I knew what functions
 * my "data class" ought to have
 */

public class PersonClient extends TestCase {
    public PersonClient(String name) {super(name);}

    public void client1(Writer out, Person person) throws IOException {
        out.write(person.firstMiddleLast());
    }

    public String client2(Person person) {
        return person.lastFirstMiddle();
    }

    public void client3(Writer out, Person person) throws IOException {
        out.write(person.lastFirstMiddle());
    }

    public String client4(Person person) {
        return person.lastFirstMiddle();
    }

    public void testClients() throws IOException {
        Person bobSmith = new Person("Smith", "Bob", null);
        Person jennyJJones = new Person("Jones", "Jenny", "J");

        StringWriter out = new StringWriter();
        client1(out, bobSmith);
        assertEquals("Bob Smith", out.toString());

        out = new StringWriter();
        client1(out, jennyJJones);
        assertEquals("Jenny J Jones", out.toString());

        assertEquals("Smith, Bob", client2(bobSmith));
        assertEquals("Jones, Jenny J", client2(jennyJJones));

        out = new StringWriter();
        client3(out, bobSmith);
        assertEquals("Smith, Bob", out.toString());

        out = new StringWriter();
        client3(out, jennyJJones);
        assertEquals("Jones, Jenny J", out.toString());

        assertEquals("Smith, Bob", client4(bobSmith));
        assertEquals("Jones, Jenny J", client4(jennyJJones));
    }
}
