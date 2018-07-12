package com.welezo.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;

//Path: http://localhost/<appln-folder-name>/login
@Path("/api")
public class Login {
	// HTTP Get Method
	@GET
	// Path: http://localhost/<appln-folder-name>/login/dologin
	@Path("/dologin")
	// Produces JSON as response
	@Produces(MediaType.APPLICATION_JSON)
	// Query parameters are parameters:
	// http://localhost/<appln-folder-name>/api/dologin?username=abc&password=xyz
	public String doLogin(@QueryParam("username") String uname, @QueryParam("password") String pwd) {
		String response = "";
		if (checkCredentials(uname, pwd)) {
			response = Utitlity.constructJSON("login=Lohith", true,"Successfully Login");
		} else {
			response = Utitlity.constructJSON("login", false,
					"Incorrect UserName or Password");
		}
		return response;
	}

	/**
	 * Method to check whether the entered credential is valid
	 * 
	 * @param uname
	 * @param pwd
	 * @return
	 */
	private boolean checkCredentials(final String uname,final String pwd) {
		boolean result = false;
		if (Utitlity.isNotNull(uname) && Utitlity.isNotNull(pwd)) {
			try {
				result = DBConnection.checkLogin(uname, pwd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				result = false;
			}
		} else {
			result = false;
		}

		return result;
	}
	@GET
	@Path("/hospital")
	// Produces JSON as response
	@Produces(MediaType.APPLICATION_JSON)
	public String packages() throws Exception {
		String response = "";
		 JSONArray package1 = DBConnection.getHospitalList();
		response = Utitlity.constructJSONPack(true,package1);
		return response;
	}
	@GET
	@Path("/packages")
	// Produces JSON as response
	@Produces(MediaType.APPLICATION_JSON)
	public String hospitalList() throws Exception {
		String response = "";
		 JSONArray package1 = DBConnection.getPackage();
		response = Utitlity.constructJSONPack(true,package1);
		return response;
	}
}
