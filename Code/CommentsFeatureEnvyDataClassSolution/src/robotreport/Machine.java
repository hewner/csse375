package robotreport;

import java.io.IOException;
import java.io.Writer;

public class Machine {
    String name;
    String location;
    String bin;

    public Machine(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String take() {
        String result = bin;
        bin = null;
        return result;
    }

    public String bin() {
        return bin;
    }

    public void put(String bin) {
        this.bin = bin;
    }

    public String name() {return name;}

	void outputForReport(Writer out)
			throws IOException {
		out.write("Machine " + name());
	
		if (bin() != null)
		    out.write(" bin=" + bin());
	
		out.write("\n");
	}
}