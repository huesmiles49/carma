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
//		System.out.println("Background Match running");
		Connection c = null;

		//test*******
		PreparedStatement getListerLocation = null;
		PreparedStatement getReserverLocation = null;
		//***********
		PreparedStatement getActiveSpots = null;
		PreparedStatement findWinner = null;
		PreparedStatement insertSwap = null;
		ResultSet activeSpotsResults = null;
		ResultSet winnerResults = null;
		ResultSet reserverLocations = null;

		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://localhost/cs3337group3";
			String username = "cs3337";
			String password = "csula2017";
			
			c = DriverManager.getConnection(url, username, password);
			
			
			getReserverLocation = c.prepareStatement("select GPS_Lat, GPS_Long from Reservations where Spot_ID = ? and Reserver_ID = ?");
			
			getActiveSpots = c.prepareStatement(
					"select ID, Time_Listed, GPS_Lat, GPS_Long from Spots where ID not in (select Spot_ID from Matches)");
			
			findWinner = c.prepareStatement(
					"select Carma, ID from Users where ID In (Select Reserver_ID from Reservations where Spot_ID = ?)");
			
			insertSwap = c.prepareStatement(
					"Insert into Matches(Spot_ID, Reservations_ID) values (?, (select ID from Reservations where Spot_ID = ? && Reserver_ID = ?))");
			
			activeSpotsResults = getActiveSpots.executeQuery();

			while (activeSpotsResults.next()) {
				int spotID = activeSpotsResults.getInt("ID");
				String spotLat = activeSpotsResults.getString("GPS_Lat");
				String spotLong = activeSpotsResults.getString("GPS_Long");
				
				//sanity check result of 0 == sql null (very bad design imo)
				if(spotID == 0)
					continue;
				
				LocalDateTime spotTime = LocalDateTime.parse(activeSpotsResults.getString("Time_Listed"));
				LocalDateTime currentTime = LocalDateTime.now();
				
				if(ChronoUnit.SECONDS.between(spotTime, currentTime) >= 60) {
					//findWinner.setInt(1, spotID);
					//winnerResults = findWinner.executeQuery();
					
					findWinner.setInt(1, spotID);
					
					winnerResults = findWinner.executeQuery(); 
					double miles = 0;
					double maxRank = 0;
					int maxUser = -1;
					
					while(winnerResults.next()) {
						
						int reserverID = winnerResults.getInt("ID");
						
						if(reserverID == 0) {
							continue;
						}
						
						getReserverLocation.setInt(1, spotID);
						getReserverLocation.setInt(2, reserverID);
						reserverLocations = getReserverLocation.executeQuery();
						
						if(reserverLocations.next()) {
						
							//find distance
							DistanceBetweenUsers distance = new DistanceBetweenUsers();
							miles = distance.distanceInMiles(
									reserverLocations.getString("GPS_Long"), 
									reserverLocations.getString("GPS_Lat"), 
									spotLong, 
									spotLat);
							System.out.println("miles: " + miles);

							int carmaOfCurrentUser = winnerResults.getInt("Carma");
							
							//determine winner based on rank
							double rank = carmaOfCurrentUser / miles;
							System.out.println("rank: " + rank);
							
							if(rank > maxRank) {
								maxRank = rank;
								maxUser = winnerResults.getInt("ID");
							}
							
						}
						
					}
					
					if(maxUser == -1) {
						continue;
					}
					
					insertSwap.setInt(1, spotID);
					insertSwap.setInt(2, spotID);
					insertSwap.setInt(3, maxUser);
					
					insertSwap.executeUpdate();
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try { winnerResults.close(); } catch (Exception e) { /* ignored */ }
			try { activeSpotsResults.close(); } catch (Exception e) { /* ignored */ }
			try { insertSwap.close(); } catch (Exception e) { /* ignored */ }
			try { findWinner.close(); } catch (Exception e) { /* ignored */ }
			try { getActiveSpots.close(); } catch (Exception e) { /* ignored */ }
			try { c.close(); } catch (Exception e) { /* ignored */ }
		}

	}

}
