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
		try {
			String url = "jdbc:mysql://localhost/cs3337group3";
			String username = "cs3337";
			String password = "csula2017";

			response.setContentType("application/json");
			PrintWriter out = response.getWriter();

			JSONArray arrayJson = new JSONArray();

			Connection c = DriverManager.getConnection(url, username, password);

			PreparedStatement getSpots = c.prepareStatement("select Lister_ID,Location,Time_Swap,Comment from Spots");

			ResultSet SpotsResults = getSpots.executeQuery();

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
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			String url = "jdbc:mysql://localhost/cs3337group3";
			String username = "cs3337";
			String password = "csula2017";
			
			int userID = 0, userCar = 0;
			int spotID = 0;
	
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			
			Connection c = DriverManager.getConnection(url, username, password);
	
			PreparedStatement insertReservation = c.prepareStatement(
					"insert into Reservations(Spot_ID,Reserver_ID,Reserver_Car) values (?, ?, ?)");
		    
			/* TODO: get spotID from parameter? and user info from cookie */
			
			insertReservation.setInt(1, spotID);
		    insertReservation.setInt(2, userID);
		    insertReservation.setInt(3, userCar);
		    
		    insertReservation.executeUpdate();
			
		} catch (SQLException e) {
			throw new ServletException(e);
		}
	}

}