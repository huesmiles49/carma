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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Servlet implementation class login
 */
@WebServlet("/login")
public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public login() {
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
		String url = "jdbc:mysql://localhost/cs3337group3";
		String username = "cs3337";
		String password = "csula2017";

		String reqEmail = request.getParameter("email");
		String reqPassword = request.getParameter("password");

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		if(reqEmail == null  || reqPassword == null)
			return;


		try {
			Connection c = 
					DriverManager.getConnection( url, username, password );

			PreparedStatement selectPassword  = c.prepareStatement(
					"select Pass, ID from Users where Email=?");

			selectPassword.setString(1, reqEmail);

			ResultSet rsPassword = selectPassword.executeQuery();
			
			if(rsPassword.next()) {
				String comparePassword = rsPassword.getString(1);
				int id = rsPassword.getInt(2);
				if(comparePassword.equals(reqPassword)) {
					JSONObject json = new JSONObject();
					json.put("id", new Integer(id));
					
					out.println(json.toJSONString());
				}
						
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




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
		
		String reqEmail = null, reqPassword = null;
		
		try {
			JSONObject data = (JSONObject) parser.parse(request.getReader());
			reqEmail = (String) data.get("email");
			reqPassword = (String) data.get("pass");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(reqEmail == null  || reqPassword == null)
			return;


		try {
			Connection c = 
					DriverManager.getConnection( url, username, password );

			PreparedStatement selectPassword  = c.prepareStatement(
					"select Pass, ID from Users where Email=?");

			selectPassword.setString(1, reqEmail);

			ResultSet rsPassword = selectPassword.executeQuery();
			
			if(rsPassword.next()) {
				String comparePassword = rsPassword.getString(1);
				int id = rsPassword.getInt(2);
				if(comparePassword.equals(reqPassword)) {
					JSONObject json = new JSONObject();
					json.put("id", new Integer(id));
					
					out.println(json.toJSONString());
				}
						
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
