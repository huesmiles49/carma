package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class addParkingSpot
 */
@WebServlet("/addParkingSpot")
public class addParkingSpot extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public addParkingSpot() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init( ServletConfig config ) throws ServletException
    {
        super.init( config );

        try
        {
            Class.forName( "com.mysql.jdbc.Driver" );
        }
        catch( ClassNotFoundException e )
        {
            throw new ServletException( e );
        }
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONParser parser = new JSONParser();
        
        String url = "jdbc:mysql://localhost/cs3337group3";
        String username = "cs3337";
        String password = "csula2017";
        
        response.setContentType("application/json");
		PrintWriter out = response.getWriter();
        
        int userID = 3, userCar = 3;
        String location = "";
        String timeSwap = "Now";
        String comment = "";
        String level = "";
        
        /* to do: add check for cookie to get user id */
        
        try {
			JSONObject data = (JSONObject) parser.parse(request.getReader());
			location = (String) data.get("location");
			level = (String) data.get("level");
			//timeSwap = (String) data.get("timeSwap");
			comment = (String) data.get("comment");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Connection c = null;
		PreparedStatement insertSpot = null;
	    try {
	        c = DriverManager
	                .getConnection( url, username, password );
	        
	        insertSpot = c.prepareStatement(
	                "insert into Spots(Lister_ID, Lister_Car, Location, Time_Listed, Time_Swap, Comment)  values(?,?,?,?,?,?)");
	        
	        insertSpot.setInt(1, userID);
	        insertSpot.setInt(2, userCar);
	        insertSpot.setString(3,  location + ", " + level);
	        insertSpot.setString(4, (new Date().toString()));
	        insertSpot.setString(5, timeSwap);
	        insertSpot.setString(5, comment);
	        
	        insertSpot.executeUpdate();
	        
	    }
	    catch( SQLException e )
	    {
	    	throw new ServletException( e );
	    } finally {
			//try { rs.close(); } catch (Exception e) { /* ignored */ }
			try { insertSpot.close(); } catch (Exception e) { /* ignored */ }
			try { c.close(); } catch (Exception e) { /* ignored */ }
		}
	}
}

