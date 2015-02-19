package am.muaad.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import am.muaad.helpers.DBHelpers;
import am.muaad.helpers.PropertiesHelper;

public class SuperController {
	PreparedStatement psmt;
	static ResultSet rst = null;
	ResultSetMetaData rmd = null;
	private DBHelpers helper = new DBHelpers();
	boolean connected = false;
	public Connection connection = connect();
	
//	public String regex = "([a-z])([A-Z])";
//	public String replacement = "$1_$2";
//	public String className = English.plural(this.getClass().getSimpleName().replaceAll(regex, replacement).toLowerCase(), 2);
	
	public Connection connect() {
		String url = PropertiesHelper.get("db.properties").get("url");
		String db = PropertiesHelper.get("db.properties").get("database");
		String user = PropertiesHelper.get("db.properties").get("user");
		String pass = PropertiesHelper.get("db.properties").get("password");
		Connection c = null;
		try {
			c = DriverManager.getConnection("jdbc:mysql://"+ url +"/" + db, user, pass); // "muaad", "muaad"
			connected = true;
		} catch (SQLException e) {
			e.printStackTrace();
			connected = false;
		}
		return c;
	}
	
	public Connection sqliteConnection() {
		Connection c = null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      c = DriverManager.getConnection("jdbc:sqlite:kpos.db");
	    } catch ( Exception e ) {
	      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.exit(0);
	    }
	    System.out.println("Opened database successfully");
	    return c;
	}
	
	public void closeConnection() {
		try {
			connect().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static Connection connectInitial() {
		String url = PropertiesHelper.get("db.properties").get("url");
		String user = PropertiesHelper.get("db.properties").get("user");
		String pass = PropertiesHelper.get("db.properties").get("password");
		Connection c = null;
		try {
			c = DriverManager.getConnection("jdbc:mysql://"+ url +"/", user, pass); // "muaad", "muaad"
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return c;
	}
	
	public static void setUp() {
		String db = PropertiesHelper.get("db.properties").get("database");
		try {
			connectInitial().createStatement().executeUpdate("create database if not exists " + db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<String> columns(String table) {
		List<String> cols = new ArrayList<String>();
		try {
			rst = connect().createStatement().executeQuery("select * from " + table + " limit 1");
			rmd = rst.getMetaData();
			
			for (int j = 1; j <= rmd.getColumnCount(); j++) {
				cols.add(rmd.getColumnName(j));
			}
		} catch (SQLException e) {}
		closeConnection();
		return cols;
	}
	
//	public static void runMigrations() {
//		try {
//			connectInitial().createStatement().executeUpdate("create database if not exists KPos");
//		} catch (SQLException e) {}
//		Category.migrate();
//		Expense.migrate();
//		ExpenseTransaction.migrate();
//		Sale.migrate();
//		SalesTransaction.migrate();
//		SubCategory.migrate();
//		Stock.migrate();
//		StockReserve.migrate();
//		User.migrate();
//		Payroll.migrate();
//		Session.migrate();
//		Business.migrate();
//		Customer.migrate();
//		CustomerTransaction.migrate();
//		Supplier.migrate();
//		StockTransaction.migrate();
//	}
//
//	public static void resetDB() {
//		Category.deleteAll();
//		Expense.deleteAll();
//		ExpenseTransaction.deleteAll();
//		Sale.deleteAll();
//		SalesTransaction.deleteAll();
//		SubCategory.deleteAll();
//		Stock.deleteAll();
//		StockReserve.deleteAll();
//		User.deleteAll();
//		Payroll.deleteAll();
//		Session.deleteAll();
//		Business.deleteAll();
//		Customer.deleteAll();
//		CustomerTransaction.deleteAll();
//		Supplier.deleteAll();
//		StockTransaction.deleteAll();
//	}
	
//	public boolean setUp() {
//		boolean setUp = false;
//		String [] tables = {"businesses", "categories", "customer_transactions", "customers",
//				"expense_transactions", "expenses", "payrolls", "sales", "sales_transactions", "sessions",
//				"stock_reserves", "stock_transactions", "stocks", "suppliers", "sub_categories", "users" };
//		List<String> tbls = Arrays.asList(tables);
//		Collections.sort(tbls);
//		connect();
//		if (connected) {
//			try {
//				rst = connect().createStatement().executeQuery("show tables");
//				int i = 0;
//				while (rst.next()) {
//					setUp = tbls.get(i).trim().equalsIgnoreCase(rst.getString(1).trim());
//					i++;
//				}
//			} catch (SQLException e) {
//				setUp = false;
//			}
//		}
//		return setUp && connected;		
//	}

	public boolean recordExists(String table, String field, String value) {
		boolean exists = false;
		String query = "select "+ field +" from " + table + " where " + field + " = '" + value + "'";
		try {
			rst = connect().createStatement().executeQuery(query);
			exists = rst.first();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(query);
		closeConnection();
		return exists;
	}

	public List<TreeMap<String, String>> index(String table) {
		List<TreeMap<String, String>> all = new ArrayList<TreeMap<String, String>>();
		TreeMap<String, String> record;
		String query = "select * from " + table;
		try {
			rst = connect().createStatement().executeQuery(query);
			rmd = rst.getMetaData();
			int n = 1;
			while (rst.next()) {
				record = new TreeMap<String, String>();
				for (int j = 1; j <= rmd.getColumnCount(); j++) {
					record.put(rmd.getColumnName(j), rst.getString(j));
					n++;
				}
				all.add(record);
			}
		} catch (SQLException e) {}
		System.out.println(query);
		closeConnection();
		return all;
	}
	
	public List<TreeMap<String, String>> between(String table, String from, String to) {
		List<TreeMap<String, String>> all = new ArrayList<TreeMap<String, String>>();
		TreeMap<String, String> record;
		String query = "select * from " + table +" where created_at between '" + from + "' and '" + to + "'";
		try {
			rst = connect().createStatement().executeQuery(query);
			rmd = rst.getMetaData();
			
			while (rst.next()) {
				record = new TreeMap<String, String>();
				for (int j = 1; j <= rmd.getColumnCount(); j++) {
					record.put(rmd.getColumnName(j), rst.getString(j));
				}
				all.add(record);
			}
		} catch (SQLException e) {}
		System.out.println(query);
		closeConnection();
		return all;
	}
//	TODO: Use prepared statements instead: http://www.javalobby.org/articles/activeobjects/
	public List<Map<String, String>> conditionalSelect(String table, String field, String value) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		Map<String, String> record;
		String query = "select * from " +table +" where " + field + " = '" + value + "'";
		try {
			rst = connect().createStatement().executeQuery(query);
			rmd = rst.getMetaData();
			
			while (rst.next()) {
				record = new HashMap<String, String>();
				for (int j = 1; j <= rmd.getColumnCount(); j++) {
					record.put(rmd.getColumnName(j), rst.getString(j));
				}
				all.add(record);
			}
		} catch (SQLException e) {}
//		System.out.println(query);
		closeConnection();
		return all;
	}
	
	public List<Map<String, String>> conditionalSelect(String table, String conditions) {
		List<Map<String, String>> all = new ArrayList<Map<String, String>>();
		Map<String, String> record;
		String query = "select * from " + table + " where " + conditions;
		try {
			rst = connect().createStatement().executeQuery(query);
			rmd = rst.getMetaData();
			
			while (rst.next()) {
				record = new HashMap<String, String>();
				for (int j = 1; j <= rmd.getColumnCount(); j++) {
					record.put(rmd.getColumnName(j), rst.getString(j));
				}
				all.add(record);
			}
		} catch (SQLException e) {}
		System.out.println(query);
		closeConnection();
		return all;
	}

	public Map<String, String> show(String table, String id) {
		Map<String, String> record = new HashMap<String, String>();
		String query = "select * from " + table + " where id = '"+ id + "'";
		try {
			rst = connect().createStatement().executeQuery(query);
			rmd = rst.getMetaData();
			
			while (rst.next()) {
				for (int j = 1; j <= rmd.getColumnCount(); j++) {
					record.put(rmd.getColumnName(j), rst.getString(j));
				}
			}
		} catch (SQLException e) {}
		System.out.println(query);
		closeConnection();
		return record;
	}

	public boolean update(String table, Map<String, String> params, String id, String value) {
		List<String> keys = new ArrayList<String>();
		for(String key : params.keySet()) {
			keys.add(key);
		}
		String query;
		query = helper.generateUpdateQuery(table , params, id, value);
		int n = 1;
		try {
			psmt = connect().prepareStatement(query);
		
			for (String l : params.keySet()) {
				psmt.setString(n, params.get(l));
				n++;
			}
			psmt.executeUpdate();
		} catch (SQLException e1) {e1.printStackTrace();}
		System.out.println(query);
		closeConnection();
		return true;
	}

	public boolean delete(String table, String id) {
		String query = "delete from " + table + " where id = ?";
		try {
			psmt = connect().prepareStatement(query);
			psmt.setString(1, id);
			psmt.executeUpdate();
		} catch (SQLException e) {}
		System.out.println(query);
		closeConnection();
		return false;
	}

	public boolean deleteWhere(String table, String field, String value) {
		String query = "delete from " + table + " where " + field + "= ?";
		try {
			psmt = connect().prepareStatement(query);
			psmt.setString(1, value);
			psmt.executeUpdate();
		} catch (SQLException e) {}
		System.out.println(query);
		closeConnection();
		return false;
	}

	public boolean truncate(String table) {
		try {
			connect().createStatement().executeQuery("truncate " + table );
		} catch (SQLException e) {}
		closeConnection();
		return false;
	}

	public Map<String, String> create(String table, TreeMap<String, String> params) {
		List<String> keys = new ArrayList<String>();
		for(String key : params.keySet()) {
			keys.add(key);
		}
		String query;
		String id = null;
		query = helper.generateCreateQuery(table , params);
		int n = 1;
		try {
			psmt = connect().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
			for (String l : params.keySet()) {
				if (l.equalsIgnoreCase("due_date")) {
					DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
					java.util.Date myDate = formatter.parse(params.get(l));
					java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
					psmt.setDate(n, sqlDate);
				}
				else {
					psmt.setString(n, params.get(l));
				}
				n++;
			}
			psmt.executeUpdate();
		} catch (SQLException e1) {e1.printStackTrace(); } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try (ResultSet generatedKeys = psmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                id = String.valueOf(generatedKeys.getLong(1));
            }
            else {
                throw new SQLException("Creating record failed, no ID obtained.");
            }
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(query);
		closeConnection();
		return show(table, id);
	}
	
	public void createTable(String tableName, Map<String, Object> fields) {
		System.out.println(helper.generateCreateTableQuery(tableName, fields));
		try {
			connection.createStatement().executeUpdate(helper.generateCreateTableQuery(tableName, fields));
		} catch (SQLException e) {
//			if any of the keys in the fields map is not in the table's columns, add new column 
			fields.keySet().removeAll(columns(tableName));
			for(String f : fields.keySet()) {
				addColumn(tableName, f, (String) fields.get(f), columns(tableName).get(columns(tableName).size() - 3));
			}
//			e.printStackTrace();
		}
	}
	
	public void dropTable(String tableName) {
		try {
			connection.createStatement().executeUpdate("drop table if exists " + tableName);
		} catch (SQLException e) {
			e.printStackTrace(); 
		}
	}
	
	public void addColumn(String table, String column, String type, String after) {
		String query = "ALTER TABLE " + table + " ADD COLUMN " + column + " " + type + " AFTER " + after;
		try {
			connect().createStatement().executeUpdate(query);
		} catch (SQLException e) {
//			e.printStackTrace();
		}
		System.out.println(query);
		closeConnection();
	}
}
