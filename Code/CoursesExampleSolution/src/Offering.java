import java.sql.*;

public class Offering {
    private int id;
    private Course course;
    private String daysTimes;

    public static Offering create(Course course, String daysTimesCsv) 
        throws SQLException 
    {
    	int newId = DBCommands.getMaxOfferingId() + 1;

		DBCommands.doDBUpdate("INSERT INTO offering VALUES ("
                + newId + ",'" + course.getName() 
                + "','" + daysTimesCsv + "')");
		return new Offering(newId, course, daysTimesCsv);
    }

    public static Offering find(int id) {
    	try {
    		String[] results = DBCommands.doDBQueryRow("SELECT * FROM offering WHERE ID =" + id, 
    				new String[] {"Course","DateTime"});
    		String courseName = results[0]; 
    		Course course = Course.find(courseName); 
    		String dateTime = results[1];
    		return new Offering(id, course, dateTime);
		} catch (SQLException ignored) {
			return null;
		}
    }

    public void update() throws SQLException{
		String[] commands = { 
				"DELETE FROM Offering WHERE ID=" + id,
				"INSERT INTO Offering VALUES(" + id + ",'" +
	                      course.getName() + "','" + daysTimes + "')"
				};
		DBCommands.doDBUpdate(commands);
    }

    public Offering(int id, Course course, String daysTimesCsv) {
        this.id = id;
        this.course = course;
        this.daysTimes = daysTimesCsv;
    }

    public int getId() {
        return id;
    }

    public Course getCourse() {
        return course;
    }

    public String getDaysTimes() {
        return daysTimes;
    }

    public String toString() {
        return "Offering " + getId() + ": " 
		+ getCourse() + " meeting " + getDaysTimes();
    }
}
