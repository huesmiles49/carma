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

/**
 * Servlet implementation class match
 */
@WebServlet("/match")
public class match extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public match() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new ServletException(e);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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
		String parkingSpotGPSLocation = "";

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

//			spotData = c.prepareStatement("select Lister_ID, Lister_Car, Location, GPS_Location from Spots where ID=?");
			spotData = c.prepareStatement("select Lister_ID, Lister_Car, Location from Spots where ID=?");

			spotData.setInt(1, parkingspotid);

			rsSpot = spotData.executeQuery();

			if (rsSpot.next()) {
//				parkingSpotGPSLocation = rsSpot.getString("GPS_Location");
				parkingSpotLocation = rsSpot.getString("Location");

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
		results.put("parkingSpotLocation", parkingSpotLocation);
//		results.put("parkingSpotGPSLocation", parkingSpotGPSLocation);

		// and finally send response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();

		out.println(results.toJSONString());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
