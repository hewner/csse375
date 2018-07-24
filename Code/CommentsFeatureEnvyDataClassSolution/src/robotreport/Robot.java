package robotreport;

import java.io.IOException;
import java.io.Writer;

public class Robot {
    Machine location;
    String bin;

    public Robot() {}

    public Machine location() {return location;}
    public void moveTo(Machine location) {this.location = location;}

    public void pick() {this.bin = location.take();}
    public String bin() {return bin;}

    public void release() {
        location.put(bin);
        bin = null;
    }

	void outputForReport(Writer out)
			throws IOException {
		out.write("Robot");
	    if (location() != null)
	        out.write(" location=" + location().name());
	
	    if (bin() != null)
	        out.write(" bin=" + bin());
	
	    out.write("\n");
	}
}
