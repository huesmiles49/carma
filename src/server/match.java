package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class match
 */
@WebServlet("/match")
public class match extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "jdbc:mysql://localhost/cs3337group3";
		String username = "cs3337";
		String password = "csula2017";
		int userID = 0, userCar = 0;

		// return variables
		int matchID = 0;
		String otherUserFirstName = "";
		String otherUserCar = "";
		String parkingSpotLocation = "";
		String parkingSpotGPSLat = "";
		String parkingSpotGPSLong = "";
		String parkingSpotComment= "";

		// check cookie for user id and car id
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie current : cookies) {
				if (current.getName().equals("ID")) {
					userID = Integer.parseInt(current.getValue());
				} else if (current.getName().equals("CARID")) {
					userCar = Integer.parseInt(current.getValue());
				}
			}
		}

		Connection c = null;
		PreparedStatement matchData = null, reservationData = null, spotData = null, otherUserInfo = null;
		ResultSet rsMatch = null, rsReservation = null, rsSpot = null, rsOtherUser = null;
		try {
			int parkingspotid = 0, reservationid = 0;
			int otheruserid = 0, otherusercar = 0;
			c = DriverManager.getConnection(url, username, password);

			matchData = c.prepareStatement(
					"Select max(ID), Spot_ID, Reservations_ID from matches where (Spot_ID in (select ID from spots where Lister_ID=?)) or (Reservations_ID in (select ID from reservations where Reserver_ID=?))");

			matchData.setInt(1, userID);
			matchData.setInt(2, userID);

			rsMatch = matchData.executeQuery();

			if (rsMatch.next()) {
				matchID = rsMatch.getInt("max(ID)");
				parkingspotid = rsMatch.getInt("Spot_ID");
				reservationid = rsMatch.getInt("Reservations_ID");
			}

			reservationData = c.prepareStatement("select Reserver_ID, Reserver_Car from Reservations where ID=?");

			reservationData.setInt(1, reservationid);

			rsReservation = reservationData.executeQuery();

			if (rsReservation.next()) {
				otheruserid = rsReservation.getInt("Reserver_ID");
				otherusercar = rsReservation.getInt("Reserver_Car");
			}

			spotData = c.prepareStatement("select Lister_ID, Lister_Car, Location, Comment, GPS_Lat, GPS_Long from Spots where ID=?");

			spotData.setInt(1, parkingspotid);

			rsSpot = spotData.executeQuery();

			// result variables
			if (rsSpot.next()) {
				parkingSpotGPSLat = rsSpot.getString("GPS_Lat");
				parkingSpotGPSLong = rsSpot.getString("GPS_Long");
				parkingSpotLocation = rsSpot.getString("Location");
				parkingSpotComment = rsSpot.getString("Comment");

				// if the current user was the reserver then the other user was the lister
				if (otheruserid == userID) {
					otheruserid = rsSpot.getInt("Lister_ID");
					otherusercar = rsSpot.getInt("Lister_Car");
				}
			}

			otherUserInfo = c.prepareStatement(
					"select Users.FName, Users_Cars.Make, Users_Cars.Model, Users_Cars.Color from Users inner join Users_Cars on Users.ID = Users_Cars.User_ID where Users.ID=? and Users_Cars.ID=?");

			otherUserInfo.setInt(1, otheruserid);
			otherUserInfo.setInt(2, otherusercar);

			rsOtherUser = otherUserInfo.executeQuery();

			if (rsOtherUser.next()) {
				otherUserFirstName = rsOtherUser.getString("FName");
				otherUserCar = rsOtherUser.getString("Color") + " " + rsOtherUser.getString("Make") + " "
						+ rsOtherUser.getString("Model");
			}

		} catch (SQLException e) {
			throw new ServletException(e);
		} finally {
			try {
				rsMatch.close();
			} catch (Exception e) {
				/* ignored */ }
			try {
				rsReservation.close();
			} catch (Exception e) {
				/* ignored */ }
			try {
				rsSpot.close();
			} catch (Exception e) {
				/* ignored */ }
			try {
				rsOtherUser.close();
			} catch (Exception e) {
				/* ignored */ }
			try {
				matchData.close();
			} catch (Exception e) {
				/* ignored */ }
			try {
				reservationData.close();
			} catch (Exception e) {
				/* ignored */ }
			try {
				spotData.close();
			} catch (Exception e) {
				/* ignored */ }
			try {
				otherUserInfo.close();
			} catch (Exception e) {
				/* ignored */ }
			try {
				c.close();
			} catch (Exception e) {
				/* ignored */ }
		}

		// create the JSON for the response
		JSONObject results = new JSONObject();
		results.put("matchID", matchID);
		results.put("otherUserName", otherUserFirstName);
		results.put("otherUserCar", otherUserCar);
		results.put("parkingSpotComment", parkingSpotComment);
		results.put("parkingSpotLocation", parkingSpotLocation);
		results.put("parkingSpotGPSLat", parkingSpotGPSLat);
		results.put("parkingSpotGPSLong", parkingSpotGPSLong);

		// and finally send response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		out.println(results.toJSONString());
	}

	/**
	 * Post handles sending and receiving the current GPS location for the users
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//db connection 
		String url = "jdbc:mysql://localhost/cs3337group3";
		String username = "cs3337";
		String password = "csula2017";
		
		//json handler
		JSONParser parser = new JSONParser();
		
		//input variables
		int userID = 0, userCar = 0, matchID = 0;
		String currentGPSLat = "", currentGPSLong = "";
		
		//return variables
		String otherUserGPSLat ="",otherUserGPSLong = "";
		
		//check cookie for user id and car id and matchid
		Cookie[] cookies = request.getCookies();
		if(cookies!=null) {
			for(Cookie current: cookies) {
				if(current.getName().equals("ID")) {
					userID = Integer.parseInt(current.getValue());
				} else if(current.getName().equals("CARID")) {
					userCar = Integer.parseInt(current.getValue());
					
					//TODO: make sure client side sets the same cookie for match id
				} else if(current.getName().equals("MATCHID")) {
					matchID = Integer.parseInt(current.getValue());
				}
			}
		}
		
		try {
			JSONObject data = (JSONObject) parser.parse(request.getReader());
			currentGPSLat = (String) data.get("latitude");
			currentGPSLong = (String) data.get("longitude");
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		//have all inputs now do SQL stuff and get return values
		Connection c = null;
		PreparedStatement insertUpdate = null, findotheruserlocation = null;
		ResultSet otheruserlocation = null;
	    try {
	    	c = DriverManager
	                .getConnection( url, username, password );
	    	
	    	//updating current user location
	    	insertUpdate = c.prepareStatement(
	    			"Insert into MatchGPS(User_ID, Matches_ID, GPS_Lat, GPS_Long) values (?,?,?,?) on duplicate key update GPS_Lat=?, GPS_Long=?");
	    	
	    	insertUpdate.setInt(1, userID);
	    	insertUpdate.setInt(2, matchID);
	    	insertUpdate.setString(3, currentGPSLat);
	    	insertUpdate.setString(4, currentGPSLong);
	    	insertUpdate.setString(5, currentGPSLat);
	    	insertUpdate.setString(6, currentGPSLong);
	    	
	    	insertUpdate.executeUpdate();
	    	
	    	//find other users location
	    	findotheruserlocation = c.prepareStatement(
	    			"select GPS_Lat, GPS_Long from MatchGPS where Matches_ID=? and User_ID!=?");
	    	
	    	findotheruserlocation.setInt(1, matchID);
	    	findotheruserlocation.setInt(2, userID);
	    	
	    	otheruserlocation = findotheruserlocation.executeQuery();
	    	
	    	if(otheruserlocation.next()) {
	    		otherUserGPSLat = otheruserlocation.getString("GPS_Lat");
	    		otherUserGPSLong = otheruserlocation.getString("GPS_Long");
	    	}
	    	
	    } catch( SQLException e ) {
	    	throw new ServletException( e );
	    } finally {
			try { otheruserlocation.close(); } catch (Exception e) { /* ignored */ }
			try { findotheruserlocation.close(); } catch (Exception e) { /* ignored */ }
			try { insertUpdate.close(); } catch (Exception e) { /* ignored */ }
			try { c.close(); } catch (Exception e) { /* ignored */ }
		}
		
	    //generate JSon response
	    JSONObject results = new JSONObject();
	    //TODO: client side check for this json on return
	    results.put("latitude", otherUserGPSLat);
	    results.put("longitude", otherUserGPSLong);
	    
	    //and finally send response
	    response.setContentType("application/json");
		PrintWriter out = response.getWriter();

	    out.println(results.toJSONString());
	    
	}

}
