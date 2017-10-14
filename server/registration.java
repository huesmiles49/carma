package server;

import java.io.BufferedReader;
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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 * Servlet implementation class registration
 */
@WebServlet("/registration")
public class registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public registration() {
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

        try
        {
            String url = "jdbc:mysql://localhost/cs3337group3";
            String username = "cs3337";
            String password = "csula2017";
            
            String testFName = "John", testLName = "Doe", testEmail = "John.Doe@nowhere.com", testPass = "password";
            String testMake = "Honda", testModel="Civic", testColor = "Green", testLicense = "TooCool", testState = "CA";
            int Carma = 1000;
            
            Connection c = DriverManager
                    .getConnection( url, username, password );
            
            PreparedStatement insertUsers = c.prepareStatement(
                    "insert into Users(FName, LName, Email, Pass, Carma)  values(?,?,?,?,?)");
            
            insertUsers.setString(1, testFName);
            insertUsers.setString(2, testLName);
            insertUsers.setString(3,  testEmail);
            insertUsers.setString(4, testPass);
            insertUsers.setInt(5, Carma);
            
            insertUsers.executeUpdate();
            
            PreparedStatement insertCars = c.prepareStatement(
            		"insert into Users_Cars(User_ID, Make, Model, Color, License_Plate, Plate_State) "
            		           + "values((select ID from Users where Email = ?), ?, ?, ?, ?, ?)");
            		           
            insertCars.setString(1, testEmail);
            insertCars.setString(2, testMake);
            insertCars.setString(3, testModel);
            insertCars.setString(4, testColor);
            insertCars.setString(5, testLicense);
            insertCars.setString(6, testState);
            
            insertCars.executeUpdate();
        }
        catch( SQLException e )
        {
            throw new ServletException( e );
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONParser parser = new JSONParser();
        JSONObject returnValues = new JSONObject();
        
        String url = "jdbc:mysql://localhost/cs3337group3";
        String username = "cs3337";
        String password = "csula2017";
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        String FName = "", LName = "", Email = "", Pass = "";
        String Make = "", Model = "", Color ="", License = "", State ="";
        int Carma = 1000;
        
        try {
			JSONObject data = (JSONObject) parser.parse(request.getReader());
			FName = (String) data.get("firstName");
			LName = (String) data.get("lastName");
			Email = (String) data.get("email");
			Pass = (String) data.get("password");
			Make = (String) data.get("make");
			Model = (String) data.get("model");
			Color = (String) data.get("color");
			License = (String) data.get("licensePlateNumber");
			State = (String) data.get("licensePlateState");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
	        Connection c = DriverManager
	                .getConnection( url, username, password );
	        
	        PreparedStatement insertUsers = c.prepareStatement(
	                "insert into Users(FName, LName, Email, Pass, Carma)  values(?,?,?,?,?)");
	        
	        insertUsers.setString(1, FName);
	        insertUsers.setString(2, LName);
	        insertUsers.setString(3,  Email);
	        insertUsers.setString(4, Pass);
	        insertUsers.setInt(5, Carma);
	
	        insertUsers.executeUpdate();
	        
	        PreparedStatement insertCars = c.prepareStatement(
	        		"insert into Users_Cars(User_ID, Make, Model, Color, License_Plate, Plate_State) "
	        		           + "values((select ID from Users where Email = ?), ?, ?, ?, ?, ?)");
	        		           
	        insertCars.setString(1, Email);
	        insertCars.setString(2, Make);
	        insertCars.setString(3, Model);
	        insertCars.setString(4, Color);
	        insertCars.setString(5, License);
	        insertCars.setString(6, State);
	        
	        insertCars.executeUpdate();
	        
	        PreparedStatement getUserID = c.prepareStatement("select ID from Users where Email=?");
	        getUserID.setString(1, Email);
	        
	        PreparedStatement getUserCar = c.prepareStatement("select ID from Users_Cars where User_ID=(select ID from Users where Email=?)");
	        getUserCar.setString(1, Email);
	        
	        ResultSet UserResults = getUserID.executeQuery();
	        
	        if(UserResults.next()) {
	        	returnValues.put("id", UserResults.getString("ID"));
	        }
	        
	        ResultSet CarResults = getUserCar.executeQuery();
	        
	        if(CarResults.next()) {
	        	returnValues.put("car", CarResults.getString("ID"));
	        }
	        
	        out.println(returnValues.toJSONString());

	    }
	    catch( SQLException e )
	    {
	    	throw new ServletException( e );
	    }
	}
}

