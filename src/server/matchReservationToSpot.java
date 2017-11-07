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

		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			String url = "jdbc:mysql://localhost/cs3337group3";
			String username = "cs3337";
			String password = "csula2017";
			
			c = DriverManager.getConnection(url, username, password);
			
			
			/*Modified*/
			getReserverLocation = c.prepareStatement("select ID, Reservations.GPS_Lat, Reservations.GPS_Long, Spots.GPS_Lat, Spots.GPS_Long from Reservations inner join Spots on Reservations.ID = Spots.ID");
			//getListerLocation = c.prepareStatement("select ID, GPS_Lat, GPS_Long from Spots");
			/* * * * * */
			
			getActiveSpots = c.prepareStatement(
					"select ID, Time_Listed from Spots where ID not in (select Spot_ID from Matches)");

			findWinner = c.prepareStatement(
					"select max(Carma),ID from Users where ID In (Select Reserver_ID from Reservations where Spot_ID = ?)");
			
			insertSwap = c.prepareStatement(
					"Insert into Matches(Spot_ID, Reservations_ID) values (?, (select ID from Reservations where Spot_ID = ? && Reserver_ID = ?))");
			
			activeSpotsResults = getActiveSpots.executeQuery();

			while (activeSpotsResults.next()) {
				int spotID = activeSpotsResults.getInt("ID");
				//***
				int reserverID = activeSpotsResults.getInt("ID");
				int lat = activeSpotsResults.getInt("ID");
				int lng = activeSpotsResults.getInt("ID");
				//***
				
				
				//sanity check result of 0 == sql null (very bad design imo)
				if(spotID == 0)
					continue;
				
				LocalDateTime spotTime = LocalDateTime.parse(activeSpotsResults.getString("Time_Listed"));
				LocalDateTime currentTime = LocalDateTime.now();
				
				if(ChronoUnit.SECONDS.between(spotTime, currentTime) >= 60) {
					//findWinner.setInt(1, spotID);
					//winnerResults = findWinner.executeQuery();
					findWinner.setInt(1, spotID);
					winnerResults = getReserverLocation.executeQuery(); 
					
					//find distance
					DistanceBetweenUsers distance = new DistanceBetweenUsers();
					double miles = distance.distanceInMiles(
							winnerResults.getString("Reservations.GPS_Long"), 
							winnerResults.getString("Reservations.GPS_Lat"), 
							winnerResults.getString("Spots.GPS_Long"), 
							winnerResults.getString("Spots.GPS_Lat"));
					

					if(winnerResults.next()) {
						int winnerID = winnerResults.getInt("ID");
						
						//sanity check result of 0 == sql null (very bad design imo)
						if(winnerID == 0)
							continue;
						
						insertSwap.setInt(1, spotID);
						insertSwap.setInt(2, spotID);
						insertSwap.setInt(3, winnerID);
						
						insertSwap.executeUpdate();
					}
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
