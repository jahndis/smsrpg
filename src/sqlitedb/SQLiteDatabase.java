package sqlitedb;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import log.Log;


public final class SQLiteDatabase {
	
	private static String name;
	private static String location;
	private static Connection connection;
	
	public static void init(String name, String location) {
		SQLiteDatabase.name = name;
		SQLiteDatabase.location = location;
		
		checkForJDBCClass();
		createDatabaseFile();
	}
	
	public static void openConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				connection = DriverManager.getConnection("jdbc:sqlite:" + location + "/" + name);
			}
			
			if (connection == null) {
				throw new SQLException("Unable to create connection to database");
			}
		} catch (SQLException e) {
			Log.error("Error getting SQL database connection");
			e.printStackTrace();
		}
	}
	
	public static void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			Log.error("Error closing SQL database connection");
			e.printStackTrace();
		}
	} 
	
	public static boolean isConnected() {
		try {
			if (connection != null) {
				return !connection.isClosed();
			}
		} catch (SQLException e) {
			Log.error("Error testing for database connection");
			e.printStackTrace();
		}
		return false;
	}
	
	public static DatabaseResult executeQuery(String statement, Object[] values) {
		PreparedStatement preparedStatement = null;
		DatabaseResult result = null;

    try {
    	if (!isConnected()) {
				throw new SQLException("Database connection is not open");
			}
    	
    	preparedStatement = connection.prepareStatement(statement);   
    	
      for (int i = 0; i < values.length; i++) {
      	preparedStatement.setObject(i+1, values[i]);
      }
   
      result = new DatabaseResult(preparedStatement.executeQuery());
    } catch (SQLException e) {
    	Log.error("Error executing query");
      e.printStackTrace();
    } finally {
      closePreparedStatement(preparedStatement);
    }
    
    return result;
	}
	
	public static DatabaseResult executeQuery(String statement) {
    return executeQuery(statement, new Object[] { });
	}
	
	public static void executeUpdate(String statement, Object[] values) {
		PreparedStatement preparedStatement = null;
		
    try {
    	if (!isConnected()) {
				throw new SQLException("Database connection is not open");
			}
    	
      preparedStatement = connection.prepareStatement(statement);
      
      for (int i = 0; i < values.length; i++) {
      	preparedStatement.setObject(i+1, values[i]);
      }
      
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
    	Log.error("Error executing update");
      e.printStackTrace();
    } finally {
    	closePreparedStatement(preparedStatement);
    }
	}
	
	public static void executeUpdate(String statement) {
    executeUpdate(statement, new Object[] { });
	}
	
	public static void createTable(TableDefinition tableDefinition) {
		try {
			if (hasTable(tableDefinition.getName())) {
				throw new SQLException("Table " + tableDefinition.getName() + " has already been created");
			}
			
			Table table = new Table(tableDefinition);
			table.create();
		} catch (SQLException e) {
			Log.error("Error creating table in database");
			e.printStackTrace();
		}
	}
	
	public static boolean hasTable(String tableName) {
		ResultSet rs = null;
		boolean hasTable = false;
		
		try {
			if (!isConnected()) {
				throw new SQLException("Database connection is not open");
			}
			
			DatabaseMetaData metadata = connection.getMetaData();
			rs = metadata.getTables(null, null, "%", null);
			
			while (rs.next()) {
				if (rs.getString("TABLE_NAME").equals(tableName)) {
					hasTable = true;
					break;
				}
			}
		} catch (SQLException e) {
			Log.error("Error checking if table " + tableName + " exists in database");
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
		}
		
		return hasTable;
	}
	
	public static void dropTable(TableDefinition tableDefinition) {
		try {
			if (!hasTable(tableDefinition.getName())) {
				throw new SQLException("Table " + tableDefinition.getName() + " does not exist");
			}
			
			Table table = new Table(tableDefinition);
			table.drop();
		} catch (SQLException e) {
			Log.error("Error dropping table in database");
			e.printStackTrace();
		}
	}
	
	public static void dropAllTables() {
		ResultSet rs = null;
		
		try {
			if (!isConnected()) {
				throw new SQLException("Database connection is not open");
			}
			
			DatabaseMetaData metadata = connection.getMetaData();
			rs = metadata.getTables(null, null, "%", null);
			
			ArrayList<String> tableNames = new ArrayList<String>();
			while (rs.next()) {
				tableNames.add(rs.getString("TABLE_NAME"));
			}
			
			Statement statement = connection.createStatement();
			for (String tableName : tableNames) {
				statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
			}
			
		} catch (SQLException e) {
			Log.error("Error dropping all tables");
			e.printStackTrace();
		} finally {
			closeResultSet(rs);
		}
	}
	
	
	/* Private Methods */
	
	private static void checkForJDBCClass() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			Log.error("JDBC class was not found");
			e.printStackTrace();
		}
	}
	
	private static void createDatabaseFile() {
		String filePath = location + "/" + name;
		
		File file = new File(filePath); 
		if (!file.exists()) { 
		  try { 
		    file.createNewFile(); 
		    Log.log("Creating database file: " + filePath);
		  } catch(IOException e) { 
		  	Log.error("Error creating database file " + filePath);
		    e.printStackTrace(); 
		  } 
		}
	}
	
	private static void closeResultSet(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				Log.error("Error closing result set");
				e.printStackTrace();
			}
		}
	}
	
	private static void closePreparedStatement(PreparedStatement statement) {
		if (statement != null) {
    	try {
    		statement.close();
			} catch (SQLException e) {
				Log.error("Error closing prepared statement");
				e.printStackTrace();
			}
    }
	}
	
}

