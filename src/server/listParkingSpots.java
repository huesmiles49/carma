package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet("/listParkingSpots")
public class listParkingSpots extends HttpServlet {
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

		ResultSet SpotsResults = null;
		PreparedStatement getSpots = null;
		Connection c = null;

		try {
			String url = "jdbc:mysql://localhost/cs3337group3";
			String username = "cs3337";
			String password = "csula2017";

			response.setContentType("application/json");
			PrintWriter out = response.getWriter();

			JSONArray arrayJson = new JSONArray();

			c = DriverManager.getConnection(url, username, password);

			getSpots = c.prepareStatement("select Lister_ID,Location,Time_Swap,Comment from Spots");

			SpotsResults = getSpots.executeQuery();

			while (SpotsResults.next()) {
				String currentLocation = SpotsResults.getString("Location");
				String currentTimeSwap = SpotsResults.getString("Time_Swap");
				String currentComment = SpotsResults.getString("Comment");
				int currentListerID = SpotsResults.getInt("Lister_ID");

				JSONObject currentLine = new JSONObject();

				currentLine.put("id", currentListerID);
				currentLine.put("timeSwap", currentTimeSwap);
				currentLine.put("location", currentLocation);
				currentLine.put("comment", currentComment);

				arrayJson.add(currentLine);

			}

			out.println(arrayJson.toJSONString());
		} catch (SQLException e) {
			throw new ServletException(e);
		} finally {
			try { SpotsResults.close(); } catch (Exception e) { /* ignored */ }
			try { getSpots.close(); } catch (Exception e) { /* ignored */ }
			try { c.close(); } catch (Exception e) { /* ignored */ }
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Connection c = null;
		PreparedStatement insertReservation = null;

		try {
			String url = "jdbc:mysql://localhost/cs3337group3";
			String username = "cs3337";
			String password = "csula2017";
			
			int userID = 0, userCar = 0;
			int spotID = 0;
			
			//check cookie for user id and car id
			Cookie[] cookies = request.getCookies();
			if(cookies!=null) {
				for(Cookie current: cookies) {
					if(current.getName().equals("ID")) {
						userID = Integer.parseInt(current.getValue());
					} else if(current.getName().equals("CARID")) {
						userCar = Integer.parseInt(current.getValue());
					}
				}
			}
			//check paramaters for parking spot id
			String spotParamter;
			if((spotParamter = request.getParameter("id")) != null) {
				spotID = Integer.parseInt(spotParamter);
			}
	
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			
			c = DriverManager.getConnection(url, username, password);
	
			insertReservation = c.prepareStatement(
					"insert into Reservations(Spot_ID,Reserver_ID,Reserver_Car) values (?, ?, ?)");
			System.out.println(userID);
			System.out.println(userCar);
			System.out.println(spotID);
			
			insertReservation.setInt(1, spotID);
		    insertReservation.setInt(2, userID);
		    insertReservation.setInt(3, userCar);
		    
		    insertReservation.executeUpdate();
			
		} catch (SQLException e) {
			throw new ServletException(e);
		} finally {
			//try { SpotsResults.close(); } catch (Exception e) { /* ignored */ }
			try { insertReservation.close(); } catch (Exception e) { /* ignored */ }
			try { c.close(); } catch (Exception e) { /* ignored */ }
		}
	}

}
