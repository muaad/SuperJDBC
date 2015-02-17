package am.muaad.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import am.muaad.annotations.DBField;

public class DBHelpers {
	PreparedStatement psmt;
	ResultSet rst = null;
	ResultSetMetaData rmd = null;
	
	@DBField(reference = true)
	public String a;
	
	@DBField
	public java.sql.Array b;
	
	public String c;
	
	
	public String generateCreateQuery(String table, TreeMap<String, String> params) {
		String query = "insert into " + table + " (";
		for(String l : params.keySet()) {
			query += l + ", ";
		}
		query = query.substring(0, query.length() - 2);
		query += ", created_at) values (";
		for(String l : params.keySet()) {
			query += "?, ";
		}
		query = query.substring(0, query.length() - 2);
		query += ", now())";
		System.out.println(query);
		return query;
	}

	public String generateUpdateQuery(String table, Map<String, String> params, String id, String value) {
		String query = "update " + table + " set ";
		for(String l : params.keySet()) {
			query += l + " = ?, ";
		}
		query += "updated_at = now()";
		query += " where " + id + " = '" + value + "'";
		return query;
	}

	public String generateCreateTableQuery(String table, Map<String, Object> fields) {
		String query = "CREATE TABLE " + table + " (id int(10) UNSIGNED AUTO_INCREMENT, ";
		for(String l : fields.keySet()) {
			query += l + " " + fields.get(l) + ", ";
		}
		if (fields.containsKey("foreign keys")) {
			@SuppressWarnings("unchecked")
			List<String> queriesList = (List<String>) fields.get("foreign keys");
			for(String q : queriesList) {
				query += q;
			}
		}
		query += "created_at datetime, updated_at datetime, primary key(id))ENGINE = InnoDB";
		return query;
	}
	
	public Set<String> columnDataTypes(String table, Connection con) {
		Set<String> types = new HashSet<String>();
		try {
			rst = con.createStatement().executeQuery("select * from " + table + " limit 1");
			rmd = rst.getMetaData();
			
			for (int j = 1; j <= rmd.getColumnCount(); j++) {
				types.add(rmd.getColumnTypeName(j));
			}
		} catch (SQLException e) {}
		return types;
	}
	
	public static void main(String[] args) {
		DBHelpers h = new DBHelpers();
		WordsHelper wh = new WordsHelper();
//		System.out.println(wh.dbFields(h.getClass()));
//		System.out.println(wh.tableName(h.getClass()));
//		System.out.println(h.generateCreateTableQuery(wh.tableName(h.getClass()), wh.dbFields(h.getClass())));
//		System.out.println(h.getClass().getFields()[1].getAnnotation(DBField.class).reference());
	}
}
