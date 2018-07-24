package robotreport;

import java.util.*;
import java.io.*;

public class Report {
    public static void report(Writer out, List<Machine> machines, Robot robot)
            throws IOException 
    {
    	/****************
    	 * I was able to this whole refactoring almost
    	 * with automated tools alone.
    	 */
        out.write("FACTORY REPORT\n");
        
        for(Machine machine : machines) {

        	machine.outputForReport(out);
        }
        out.write("\n");

        robot.outputForReport(out);

        out.write("========\n");
    }
}