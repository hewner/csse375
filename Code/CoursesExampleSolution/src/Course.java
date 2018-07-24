import java.sql.*;

public class Course {
	private String name;
	private int credits;

	
	public static Course create(String name, int credits) throws Exception {

		String[] commands = { 
				"DELETE FROM course WHERE name = '" + name + "'",
				"INSERT INTO course VALUES ('" + name + "', " + credits + ")" 
				};
		DBCommands.doDBUpdate(commands);
		return new Course(name, credits);
	}

	public static Course find(String name) {
		try {
			String[] results = DBCommands.doDBQueryRow("SELECT * FROM course WHERE Name = '"
				+ name + "'", new String[] {"Credits"});
			return new Course(name, Integer.parseInt(results[0]));
		} catch (SQLException ignored) {
			return null;
		}
	}

	public void update() throws Exception {
		String[] commands = { 
				"DELETE FROM COURSE WHERE name = '" + name + "'",
				"INSERT INTO course VALUES('" + name + "'," + credits + ")" 
				};
		DBCommands.doDBUpdate(commands);
	}

	Course(String name, int credits) {
		this.name = name;
		this.credits = credits;
	}

	public int getCredits() {
		return credits;
	}

	public String getName() {
		return name;
	}
}
