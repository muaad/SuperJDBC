package am.muaad.models;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import am.muaad.controllers.SuperController;
import am.muaad.helpers.WordsHelper;

public class SuperModel {
	
	SuperController controller = new SuperController();
	WordsHelper words = new WordsHelper();
	
	public Map<String, String> find(String id) {
		return controller.show(words.tableName(this.getClass()), id);		
	}

	public Map<String, String> findBy(String field, String value) {
		return where(field + " = '" + value + "'").get(0);		
	}
	
	public List<Map<String, String>> where(String field, String value) {
		return controller.conditionalSelect(words.tableName(this.getClass()), field, value);		
	}
	
	public List<Map<String, String>> where(String conditions) {
		return controller.conditionalSelect(words.tableName(this.getClass()), conditions);		
	}

	public Map<String, String> create(TreeMap<String, String> params) {
		return controller.create(words.tableName(this.getClass()), params);		
	}

	public List<TreeMap<String, String>> between(String from, String to) {
		return controller.between(words.tableName(this.getClass()), from, to);		
	}

	public List<TreeMap<String, String>> showAll() {
		return controller.index(words.tableName(this.getClass()));		
	}

	public boolean update(Map<String, String> params, Map<String, String> conditions) {
		for(String field : conditions.keySet()) {
			controller.update(words.tableName(this.getClass()), params, field, conditions.get(field));
		}
		return false;		
	}

	public boolean delete(String id) {
		controller.delete(words.tableName(this.getClass()), id);
		return false;		
	}

	public boolean delete(String field, String value) {
		controller.deleteWhere(words.tableName(this.getClass()), field, value);
		return false;		
	}

	public boolean deleteAll() {
		controller.truncate(words.tableName(this.getClass()));
		return false;		
	}
	
	public boolean exists(String id, String value) {
		return controller.recordExists(words.tableName(this.getClass()), id, value);
	}
	
	public void addColumn(String column, String type, String after) {
		controller.addColumn(words.tableName(this.getClass()), column, type, after);
	}
	
	public Map<String, String> findOrCreateBy(String column, Map<String, String> params, boolean update) {
		Map<String, String> record = new TreeMap<String, String>();
		Map<String, String> conditions = new TreeMap<String, String>();
		conditions.put(column, params.get(column));
		if (exists(column, params.get(column))) {
			if (update) {
				update(params, conditions);
			}
			record = findBy(column, params.get(column));
		}
		else {
			record = create(new TreeMap<String, String>(params));
		}
		return record;
	}
	
	public List<String> columns() {
		return controller.columns(words.tableName(this.getClass()));
	}
	
	public void migrate() {
		controller.createTable(words.tableName(this.getClass()), words.dbFields(this.getClass()));
	}
	
	public void drop() {
		controller.dropTable(words.tableName(this.getClass()));
	}
}
