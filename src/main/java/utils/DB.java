package utils;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
	private static String connectionString = "jdbc:mysql://localhost/miniau?user=root&password=root";

	private Connection connection;

	private static DB db;

	private DB() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		Class<?> driverClass = Class.forName("com.mysql.cj.jdbc.Driver");
		DriverManager.registerDriver((Driver) driverClass.newInstance());
		connection = DriverManager.getConnection(connectionString);
	}

	public static DB getInstance()
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		if (null == db) {
			db = new DB();
		}
		return db;
	}

	public Connection getConnection() {
		return connection;
	}
}
