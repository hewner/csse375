import java.util.*;
import java.sql.*;

public class Schedule {
    String name;
    int credits = 0;
    static final int minCredits = 12;
    static final int maxCredits = 18;
    boolean overloadAuthorized = false;
    ArrayList schedule = new ArrayList();

	public static void deleteAll() throws SQLException {
		DBCommands.doDBUpdate("DELETE FROM schedule");
	}

	public static Schedule create(String name) throws SQLException {

		DBCommands.doDBUpdate("DELETE FROM schedule WHERE name = '" + name
				+ "'");
		return new Schedule(name);
	}

	public static Schedule find(String name) {
		try {
			String[] rowNames = { "OfferingId" };
			String sql = "SELECT * FROM schedule WHERE Name= '" + name + "'";

			Schedule schedule = new Schedule(name);

			for (String[] row : DBCommands.doDBQuery(sql, rowNames)) {
				int offeringId = Integer.parseInt(row[0]);
				Offering offering = Offering.find(offeringId);
				schedule.add(offering);
			}
			return schedule;
		} catch (SQLException ex) {
			return null;
		}

	}

	public static Collection<Schedule> all() throws SQLException {
		String[] rowNames = { "Name" };
		String sql = "SELECT DISTINCT Name FROM schedule";
		ArrayList<Schedule> result = new ArrayList<Schedule>();

		for (String[] row : DBCommands.doDBQuery(sql, rowNames)) {
			result.add(Schedule.find(row[0]));

		}

		return result;
	}

	public void update() throws SQLException {

		String[] commands = new String[schedule.size() + 1];
		commands[0] = "DELETE FROM schedule WHERE name = '" + name + "'";

		for (int i = 0; i < schedule.size(); i++) {
			Offering offering = (Offering) schedule.get(i);
			commands[i + 1] = "INSERT INTO schedule VALUES('" + name + "',"
					+ offering.getId() + ")";
		}
		DBCommands.doDBUpdate(commands);
	}

    public Schedule(String name) {
        this.name = name;
    }

    public void add(Offering offering) {
        credits += offering.getCourse().getCredits();
        schedule.add(offering);
    }

    public void authorizeOverload(boolean authorized) {
        overloadAuthorized = authorized;
    }

    public List analysis() {
        ArrayList result = new ArrayList();

        if (credits < minCredits)
            result.add("Too few credits");

        if (credits > maxCredits && !overloadAuthorized)
            result.add("Too many credits");

        checkDuplicateCourses(result);

        checkOverlap(result);

        return result;
    }

    public void checkDuplicateCourses(ArrayList analysis) {
        HashSet courses = new HashSet();
        for (int i = 0; i < schedule.size(); i++) {
            Course course = ((Offering) schedule.get(i)).getCourse();
            if (courses.contains(course))
               analysis.add("Same course twice - " + course.getName());
            courses.add(course);
        }
    }

    public void checkOverlap(ArrayList analysis) {
      HashSet times = new HashSet();

      for (Iterator iterator = schedule.iterator(); iterator.hasNext();) 
      {
          Offering offering = (Offering) iterator.next();
          String daysTimes = offering.getDaysTimes();
          StringTokenizer tokens = new StringTokenizer(daysTimes, ",");
          while (tokens.hasMoreTokens()) {
              String dayTime = tokens.nextToken();
              if (times.contains(dayTime))
                  analysis.add("Course overlap - " + dayTime);
              times.add(dayTime);
          }
      }
    }

    public String toString() {
        return "Schedule " + name + ": " + schedule;
    }
}
