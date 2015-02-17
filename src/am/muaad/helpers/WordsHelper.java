package am.muaad.helpers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.atteo.evo.inflector.English;

import am.muaad.annotations.DBField;

public class WordsHelper {
	public Map<String, Object> dbFields(Class className) {
		List<String> foreignKeys = new ArrayList<String>();
		Map<String, Object> fields = new HashMap<String, Object>();
		for(Field f : className.getFields()) {
			if (f.getAnnotation(DBField.class) != null) {
				String fieldName = f.getName();
				fields.put(fieldName, convertDataTypes(f.getType().getSimpleName()));
				if (f.getAnnotation(DBField.class).reference()) {
					foreignKeys.add("FOREIGN KEY(" + fieldName + ") REFERENCES " + fieldName.substring(0, fieldName.length() - 3) + "(id)");
				}
			}
		}
		fields.put("foreign keys", foreignKeys);
		return fields;		
	}
	
	public String tableName(Class className) {
		String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        return English.plural(className.getSimpleName().replaceAll(regex, replacement).toLowerCase());
	}
	
	private String convertDataTypes(String dataType) {
		/**
		 *  VARCHAR	java.lang.String	setString	updateString
			CHAR	java.lang.String	setString	updateString
			LONGVARCHAR	java.lang.String	setString	updateString
			BIT	boolean	setBoolean	updateBoolean
			NUMERIC	java.math.BigDecimal	setBigDecimal	updateBigDecimal
			TINYINT	byte	setByte	updateByte
			SMALLINT	short	setShort	updateShort
			INTEGER	int	setInt	updateInt
			BIGINT	long	setLong	updateLong
			REAL	float	setFloat	updateFloat
			FLOAT	float	setFloat	updateFloat
			DOUBLE	double	setDouble	updateDouble
			VARBINARY	byte[ ]	setBytes	updateBytes
			BINARY	byte[ ]	setBytes	updateBytes
			DATE	java.sql.Date	setDate	updateDate
			TIME	java.sql.Time	setTime	updateTime
			TIMESTAMP	java.sql.Timestamp	setTimestamp	updateTimestamp
			CLOB	java.sql.Clob	setClob	updateClob
			BLOB	java.sql.Blob	setBlob	updateBlob
			ARRAY	java.sql.Array	setARRAY	updateARRAY
			REF	java.sql.Ref	SetRef	updateRef
			STRUCT	java.sql.Struct	SetStruct	updateStruct
		 */
		Map<String, String> types = new HashMap<String, String>();
		types.put("String", "VARCHAR(45)");
		types.put("int", "INT(10)");
		types.put("boolean", "BIT");
		types.put("float", "FLOAT(10)");
		types.put("double", "FLOAT(10)");
		types.put("byte[]", "BINARY");
		types.put("byte", "TINYINT");
		types.put("short", "SMALLINT");
		types.put("long", "BIGINT");
		types.put("Date", "DATE");
		types.put("Timestamp", "TIMESTAMP");
		
		if (types.get(dataType) != null) {
			return types.get(dataType);
		}
		else {
			return dataType;
		}
	}
	
	public static TreeMap<String, String> constructParamsMap(String... params) {
		TreeMap<String, String> map = new TreeMap<String, String>();
		if (params.length % 2 == 0) {
			for (int i = 0; i < params.length; i+=2) {
				map.put(params[i], params[i + 1]);
			}
		}
		else {
			for (int i = 0; i < params.length - 1; i+=2) {
				map.put(params[i], params[i + 1]);
			}
		}
		
		return map;
	}
}
