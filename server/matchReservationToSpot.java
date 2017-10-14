package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class matchReservationToSpot implements Runnable {

	@Override
	public void run() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://localhost/cs3337group3";
			String username = "cs3337";
			String password = "csula2017";
			
			Connection c = DriverManager.getConnection(url, username, password);
			
			PreparedStatement getActiveSpots = c.prepareStatement(
					"select ID, Time_Listed from Spots where ID not in (select ID from Matches)");

			PreparedStatement findWinner = c.prepareStatement(
					"select max(Carma),ID from Users where ID In (Select Reserver_ID from Reservations where Spot_ID = ?)");
			
			PreparedStatement insertSwap = c.prepareStatement(
					"Insert into Matches(Spot_ID, Reservations_ID) values (?, (select ID from Reservations where Spot_ID = ? && Reserver_ID = ?))");
			
			ResultSet activeSpotsResults = getActiveSpots.executeQuery();

			while (activeSpotsResults.next()) {
				int spotID = activeSpotsResults.getInt("ID");
				
				LocalDateTime spotTime = LocalDateTime.parse(activeSpotsResults.getString("Time_Listed"));
				LocalDateTime currentTime = LocalDateTime.now();
				
				if(ChronoUnit.SECONDS.between(spotTime, currentTime) >= 60) {
					findWinner.setInt(1, spotID);
					ResultSet winnerResults = findWinner.executeQuery();
					
					if(winnerResults.next()) {
						int winnerID = winnerResults.getInt("ID");
						insertSwap.setInt(1, spotID);
						insertSwap.setInt(2, spotID);
						insertSwap.setInt(3, winnerID);
						
						insertSwap.executeUpdate();
					}
				}
				
			}
			
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

}
