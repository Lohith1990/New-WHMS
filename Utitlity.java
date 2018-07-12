package com.welezo.api;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Utitlity {
	/**
	 * Null check Method
	 * 
	 * @param txt
	 * @return
	 */
	public static boolean isNotNull(String txt) {
		// System.out.println("Inside isNotNull");
		return txt != null && txt.trim().length() >= 0 ? true : false;
	}

	/**
	 * Method to construct JSON
	 * 
	 * @param tag
	 * @param status
	 * @return
	 */
	public static String constructJSON(String tag, boolean status) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("tag", tag);
			obj.put("status", new Boolean(status));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		return obj.toString();
	}

	/**
	 * Method to construct JSON with Error Msg
	 * @param tag
	 * @param status
	 * @param err_msg
	 * @return
	 */
	public static String constructJSON(String tag, boolean status,
			String err_msg) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("tag", tag);
			obj.put("status", new Boolean(status));
			obj.put("error_msg", err_msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		return obj.toString();
	}
	public static String constructJSONPack(boolean status,	JSONArray package1) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("status", new Boolean(status));
			obj.put("Response", package1);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		}
		return obj.toString();
	}
	public static JSONObject constructJsonFromHashMap(HashMap<String, String> map){
		JSONObject obj = new JSONObject();
		Set<String> keys = map.keySet();
		for(String key : keys){
			try{
				obj.put(key, map.get(key));
			}catch(JSONException e){
				// Do nothing
			}
		}
		return obj;
	}
	
	
	public static JSONArray constructJsonFromResultSetObject(ResultSet rs){
		JSONObject obj = new JSONObject();
		ArrayList<String> colNames = new ArrayList<String>();
		JSONArray objects = new JSONArray();
        try{
			ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int j = 1; j <= columnCount; j++) {
                 colNames.add(rsmd.getColumnName(j));
            }
            int rowCount = 1;
            String results="";
            while (rs.next()) {
                int i = 1;
                HashMap<String, String> map = new HashMap<String, String>();
                for (i = 1; i <= columnCount; i++) {
                	map.put(colNames.get(i-1), rs.getString(i));
                }
                objects.put(map);
                // break ; // single row object
                rowCount++;
          }
           //obj.put("details", objects);
		}catch(Exception e){
			
		}
		return objects;
	}
	
	public static JSONObject constructJsonFromResultSet(ResultSet rs){
		JSONObject obj = new JSONObject();
		ArrayList<String> colNames = new ArrayList<String>();
		try{
			ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int j = 1; j <= columnCount; j++) {
                 colNames.add(rsmd.getColumnName(j));
            }
            int rowCount = 1;
            String results="";
            JSONArray objects = new JSONArray();
            while (rs.next()) {
                int i = 1;
                HashMap<String, String> map = new HashMap<String, String>();
                for (i = 1; i <= columnCount; i++) {
                	map.put(colNames.get(i-1), rs.getString(i));
                }
                objects.put(map);
                // break ; // single row object
                rowCount++;
          }
           obj.put("details", objects);
		}catch(Exception e){
			
		}
		return obj;
	}
}
