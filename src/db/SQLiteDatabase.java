package db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import log.Log;

public class SQLiteDatabase {
	
	private static String name;
	private static String location;
	
	private static Connection connection;
	
	public static void init(String name, String location) {
		//Check for JDBC class
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			Log.error("JDBC class was not found.");
			e.printStackTrace();
		}
		
		SQLiteDatabase.name = name;
		SQLiteDatabase.location = location;
	}
	
	public static void openConnection() {
		//Open connection to the database
		try {
			if (connection == null) {
				connection = DriverManager.getConnection("jdbc:sqlite:" + location + "/" + name);
			} else if (connection.isClosed()) {
				connection = DriverManager.getConnection("jdbc:sqlite:" + location + "/" + name);
			}
		} catch (SQLException e) {
			Log.error("Error getting SQL database connection.");
			e.printStackTrace();
		}
	}
	
	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			Log.error("Error closing SQL database connection.");
			e.printStackTrace();
		}
	} 
	
	public static ResultSet executeQuery(String sql) {
		ResultSet result = null;
		
		try {
			Statement statement = connection.createStatement();
			result = statement.executeQuery(sql);
			
		} catch (SQLException e) {
			Log.error("Error executing query: " + sql);
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void executeUpdate(String sql) {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			Log.error("Error executing update: " + sql);
			e.printStackTrace();
		}
	}
	
	
	public static void createTable(String tableName, String[] columnNames, String[] columnTypes, String[] columnRules) {
		try {
			if (columnNames.length != columnTypes.length || columnNames.length != columnRules.length) throw new IllegalArgumentException();
			
			Statement statement = connection.createStatement();
			
			//Delete the table from the database
			statement.executeUpdate("drop table if exists " + tableName + ";");
			
			//Build the table creation string
			StringBuilder builder = new StringBuilder();
			builder.append("create table " + tableName + " (");
			for (int i = 0; i < columnNames.length; i++) {
				builder.append(columnNames[i] + " " + columnTypes[i] + " " + columnRules[i] + ",");
			}
			//Get rid of the extra comma
			builder.deleteCharAt(builder.length() - 1);
			builder.append(");");
			
			//Create the table in the database
			statement.executeUpdate(builder.toString());
		} catch (SQLException e) {
			Log.error("Error creating new table " + tableName);
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			Log.error("Lengths of column names, types, and rules arrays are not equal");
			e.printStackTrace();
		}
	}
	
	
	public static boolean hasTable(String tableName) {
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet rs = metadata.getTables(null, null, "%", null);
			
			while (rs.next()) {
				if (rs.getString("TABLE_NAME").equals(tableName)) {
					rs.close();
					return true;
				}
			}
			
			rs.close();
		} catch (SQLException e) {
			Log.error("Error checking if table " + tableName + " exists in database.");
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void dropAllTables() {
		try {
			DatabaseMetaData metadata = connection.getMetaData();
			ResultSet rs = metadata.getTables(null, null, "%", null);
			
			ArrayList<String> tableNames = new ArrayList<String>();
			while (rs.next()) {
				tableNames.add(rs.getString("TABLE_NAME"));
			}
			rs.close();
			
			Statement statement = connection.createStatement();
			for (String tableName : tableNames) {
				statement.executeUpdate("drop table if exists " + tableName + ";");
			}
			
		} catch (SQLException e) {
			Log.error("Error dropping all tables.");
			e.printStackTrace();
		}
	}
	
}

