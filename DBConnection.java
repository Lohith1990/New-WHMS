package com.welezo.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

public class DBConnection {
	/**
	 * Method to create DB Connection
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	public static Connection createConnection() throws Exception {
		Connection con = null;
		try {
			Class.forName(Constants.dbClass);
			con = DriverManager.getConnection(Constants.dbUrl,Constants.dbUser, Constants.dbPwd);
		} catch (Exception e) {
			throw e;
		} finally {
			return con;
		}
	}

	/**
	 * Method to check whether uname and pwd combination are correct
	 * 
	 * @param uname
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public static boolean checkLogin(String uname, String pwd) throws Exception {
		boolean isUserAvailable = false;
		Connection dbConn = null;
		try {
			try {
				dbConn = DBConnection.createConnection();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Statement stmt = dbConn.createStatement();

			// String query = "Select * from product_master";
			String query = "SELECT * FROM usermaster WHERE (user_mail = '"+ uname + "' OR user_name = '"+ uname + "')   AND password='" + pwd + "'";
			ResultSet rs = stmt.executeQuery(query);
			// String jsonOp = Utitlity.constructJsonFromResultSet(rs);

			while (rs.next()) {
				isUserAvailable = true;
			}
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (dbConn != null) {
				dbConn.close();
			}
			throw e;
		} finally {
			if (dbConn != null) {
				dbConn.close();
			}
		}
		return isUserAvailable;
	}

	/**
	 * Method to insert uname and pwd in DB
	 * 
	 * @param name
	 * @param uname
	 * @param pwd
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	public static boolean insertUser(String name, String uname, String pwd)
			throws SQLException, Exception {
		boolean insertStatus = false;
		Connection dbConn = null;
		String string = null;
		try {
			try {
				dbConn = DBConnection.createConnection();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Statement stmt = dbConn.createStatement();
			String query1 = "SELECT * FROM usermaster WHERE user_name = '"
					+ name + "' AND password='" + pwd + "'";
			ResultSet rs1 = stmt.executeQuery(query1);
			while (rs1.next()) {
				string = rs1.getString(2);

			}
			if (string == null) {
				try {

					String query = "INSERT into usermaster(user_name, user_mail, password) values('"
							+ name + "'," + "'" + uname + "','" + pwd + "')";
					int records = stmt.executeUpdate(query);
					// When record is successfully inserted
					if (records > 0) {
						insertStatus = true;
					}
				} catch (SQLException sqle) {
					throw sqle;
				} catch (Exception e) {
					if (dbConn != null) {
						dbConn.close();
					}
				}
				// throw e;
			}
		} finally {
			if (dbConn != null) {
				dbConn.close();
			}
		}
		return insertStatus;
	}

	public static JSONArray getPackage() throws Exception {
		Connection dbConn = null;
		JSONObject constructJsonFromResultSet = null;
		 JSONArray objects = new JSONArray();
		try {
			try {
				dbConn = DBConnection.createConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Statement stmt = dbConn.createStatement();

			//String query = "Select * from product_master";
			String query = "SELECT product_id, product_name, product_price FROM product_master  WHERE CURRENT_DATE BETWEEN start_date AND valid_till";
			ResultSet rs = stmt.executeQuery(query);
           
            while(rs.next()){
            	String query1 = "SELECT ser.`service_name`,off.`quantity` FROM `product_offers` off "
						+ "LEFT JOIN `services`ser ON ser.`service_id` = off.`service_id` WHERE product_id = '"+rs.getInt(1)+"'";
				JSONObject innerQuery1 = new JSONObject(); // InnerQuery(query1);
				innerQuery1.put("product_name", rs.getString(2));
				innerQuery1.put("product_price", rs.getString(3));
				innerQuery1.put("prod_id", rs.getString(1));
				innerQuery1.put("services", InnerQuery(query1));
				objects.put(innerQuery1);
            }
			constructJsonFromResultSet = Utitlity.constructJsonFromResultSet(rs);
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (dbConn != null) {
				dbConn.close();
			}
			throw e;
		} finally {
			if (dbConn != null) {
				dbConn.close();
			}
		}
		return objects;
	}
	public static JSONArray InnerQuery(String query) throws Exception {
		Connection dbConn = null;
		JSONArray constructJsonFromResultSet = null;
		try{
			try {
				dbConn = DBConnection.createConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Statement stmt = dbConn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			  constructJsonFromResultSet = Utitlity.constructJsonFromResultSetObject(rs);
	} catch (SQLException sqle) {
		throw sqle;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		if (dbConn != null) {
			dbConn.close();
		}
		throw e;
	} finally {
		if (dbConn != null) {
			dbConn.close();
		}
	}
	return constructJsonFromResultSet;
}
	
	public static JSONArray getHospitalList() throws Exception {
		Connection dbConn = null;
		JSONObject constructJsonFromResultSet = null;
		 JSONArray objects = new JSONArray();
		try {
			try {
				dbConn = DBConnection.createConnection();
			} catch (Exception e) {
				e.printStackTrace();
			}
			Statement stmt = dbConn.createStatement();

			//String query = "Select * from product_master";
			String query = "SELECT * FROM `hospital_details`hosp LEFT JOIN `hospital_contact`cont ON  cont.`hospital_id`=hosp.`hospital_id`;";
			ResultSet rs = stmt.executeQuery(query);
           
            while(rs.next()){
            	String query1 = "SELECT ser.`service_id`,servc.`service_name`,servc.`category` FROM `hospital_service`ser "
            			+ "LEFT JOIN `services`servc ON servc.`service_id`= ser.`service_id` WHERE `hospital_id` = '"+rs.getInt(1)+"'";
				JSONObject innerQuery1 = new JSONObject(); // InnerQuery(query1);
				innerQuery1.put("Hospital_Id", rs.getString(1));
				innerQuery1.put("Hospital_Name", rs.getString(2));
				innerQuery1.put("Empanelled_Date", rs.getString(3));
				innerQuery1.put("Address", rs.getString(6));
				innerQuery1.put("City", rs.getString(7));
				innerQuery1.put("Latitude", rs.getString(9));
				innerQuery1.put("Longitude", rs.getString(10));
				innerQuery1.put("Appt_Booking", rs.getString(11));
				innerQuery1.put("Escalation_contact", rs.getString(12));
				innerQuery1.put("Email", rs.getString(13));
				innerQuery1.put("Auth_sign", rs.getString(14));
				innerQuery1.put("services", InnerQuery(query1));
				objects.put(innerQuery1);
            }
			constructJsonFromResultSet = Utitlity.constructJsonFromResultSet(rs);
		} catch (SQLException sqle) {
			throw sqle;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			if (dbConn != null) {
				dbConn.close();
			}
			throw e;
		} finally {
			if (dbConn != null) {
				dbConn.close();
			}
		}
		return objects;
	}
}
