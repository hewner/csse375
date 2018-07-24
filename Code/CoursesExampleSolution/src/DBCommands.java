import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;


public class DBCommands {
	static String url = "jdbc:derby:courses";

	static {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
		} catch (Exception ignored) {
		}
	}

	public static void doDBUpdate(String[] commands) throws SQLException {
		Connection conn = null;

		try {
			conn = DriverManager.getConnection(url, "", "");
			Statement statement = conn.createStatement();
			for (String command : commands) {
				statement.executeUpdate(command);
			}
		} finally {
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}
	
	public static void doDBUpdate(String command) throws SQLException {
		doDBUpdate(new String[] {command});
	}

	public static String[] doDBQueryRow(String querySQL, String[] columns) throws SQLException {
		List<String[]> results = doDBQuery(querySQL,columns);
		if(results.size() != 1) {
			throw new SQLException("Expected 1 row.  Got " + results.size());
		}
		return results.get(0);
	}
	
	public static List<String[]> doDBQuery(String querySQL, String[] columns) throws SQLException {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, "", "");
			Statement statement = conn.createStatement();
			ResultSet rows = statement
					.executeQuery(querySQL);
			List<String[]> results = new LinkedList<String[]>();
			while(rows.next()) {
				String[] rowResult = new String[columns.length];
				for(int i = 0; i < rowResult.length; i++) {
					rowResult[i] = rows.getString(columns[i]);
				}
				results.add(rowResult);
			}
			return results;
		} finally {
			try {
				conn.close();
			} catch (Exception ignored) {
			}
		}
	}
	
    public static int getMaxOfferingId() 
            throws SQLException 
        {
            Connection conn = null;

            try {
                conn = DriverManager.getConnection(url, "", "");
                Statement statement = conn.createStatement();

                ResultSet result = statement.executeQuery("SELECT MAX(ID) FROM offering");
                result.next();
                return result.getInt(1);

            } finally {
                try { conn.close(); } catch (Exception ignored) {}
            }
        }
}
