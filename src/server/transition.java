package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class transition
 */
@WebServlet("/transition")
public class transition extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public transition() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * Will return the max id of the match table at this time
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//db connections
		String url = "jdbc:mysql://localhost/cs3337group3";
        String username = "cs3337";
        String password = "csula2017";
        
        //return stuff
        int maxMatch = -1;
        JSONObject resultJSON = new JSONObject();
        
        response.setContentType("application/json");
		PrintWriter out = response.getWriter();
        
		//db stuff
		Connection c = null;
		PreparedStatement findMaxMatch = null;
		ResultSet maxMatchResults = null;
	    try {
	        c = DriverManager
	                .getConnection( url, username, password );
	        
	        findMaxMatch = c.prepareStatement(
	                "select max(id) from Matches");
	               
	        
	        maxMatchResults = findMaxMatch.executeQuery();
	        
	        if(maxMatchResults.next()) {
	        	maxMatch = maxMatchResults.getInt("max(id)");
	        }
	    }
	    catch( SQLException e )
	    {
	    	throw new ServletException( e );
	    } finally {
	    	try { maxMatchResults.close(); } catch (Exception e) { /* ignored */ }
			try { findMaxMatch.close(); } catch (Exception e) { /* ignored */ }
			try { c.close(); } catch (Exception e) { /* ignored */ }
		}
	    
	    //on return store the maxMatch somehow to send it later asking if it won.
	    resultJSON.put("maxMatch", maxMatch);
		out.println(resultJSON.toJSONString());
	}

	/**
	 * Request input: the max id of the match table when the page loaded and the user id cookie
	 * will check if the user has a match that came after the max id above
	 * if it does return a success value so the page knows to transition to the match view
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//db connections
		String url = "jdbc:mysql://localhost/cs3337group3";
        String username = "cs3337";
        String password = "csula2017";
        
        //inputs needed from request
        int userID = -1, maxMatch = -2;
        
        
        //return stuff
        int winningMatchID = -1;
        JSONObject resultJSON = new JSONObject();
        
        response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		//get inputs from request
		// check cookie for user id and car id
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie current : cookies) {
				if (current.getName().equals("ID")) {
					userID = Integer.parseInt(current.getValue());
				} else if(current.getName().equals("MAXMATCH")) {
					maxMatch = Integer.parseInt(current.getValue());
				}
			}
		}
			
		//small error check, if missing input don't do the db stuff
        if(maxMatch != -2 && userID != -1) {
			//db stuff
			Connection c = null;
			PreparedStatement findWinningMatch = null;
			ResultSet winningMatchResults = null;
		    try {
		        c = DriverManager
		                .getConnection( url, username, password );
		        
		        findWinningMatch = c.prepareStatement(
		                "select id from Matches where id>? and (?=(select Lister_ID from Spots where Matches.Spot_ID=ID) or ?=(select Reserver_ID from Reservations where Matches.Reservations_ID=ID)) ");
		               
		        findWinningMatch.setInt(1, maxMatch);
		        findWinningMatch.setInt(2, userID);
		        findWinningMatch.setInt(3, userID);
		        
		        winningMatchResults = findWinningMatch.executeQuery();
		        
		        if(winningMatchResults.next()) {
		        	int temp = winningMatchResults.getInt("id");
		        	//small sanity check
		        	if(temp > maxMatch)
		        		winningMatchID = temp;
		        }
		    }
		    catch( SQLException e )
		    {
		    	throw new ServletException( e );
		    } finally {
		    	try { winningMatchResults.close(); } catch (Exception e) { /* ignored */ }
				try { findWinningMatch.close(); } catch (Exception e) { /* ignored */ }
				try { c.close(); } catch (Exception e) { /* ignored */ }
			}
        }
	    
	    //on return check if winningMatch > maxMatch (small sanity check) if so you won!
	    resultJSON.put("winningMatch", winningMatchID);
		out.println(resultJSON.toJSONString());
	}

}
