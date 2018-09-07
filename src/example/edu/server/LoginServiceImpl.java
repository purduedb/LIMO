package example.edu.server;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import example.edu.client.service.LoginService;

public class LoginServiceImpl extends RemoteServiceServlet implements LoginService
{
	private final String url = "jdbc:postgresql://ibnkhaldun.cs.purdue.edu:5439/gisdb2";

	@Override
	public String signupUser(String id, String email, String pw) 
	{
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		pw = passwordEncoder.encode(pw);
		
		java.sql.Connection conn; 
        try 
        { 
            Class.forName("org.postgresql.Driver"); 
            //String url = "jdbc:postgresql://localhost:5432/gisDBTest";
             
            conn = DriverManager.getConnection(url, "limo", "limo"); 
             
            Statement s = conn.createStatement();
            
            // check id exsist
            ResultSet r1 = s.executeQuery("SELECT username FROM login.users WHERE username = '" + id + "'");
            if (r1.next())
            {
            	r1.close();
            	return "hasID";
            }
            // check email exsist
            ResultSet r2 = s.executeQuery("SELECT email FROM login.users WHERE email = '" + email + "'");
            if (r2.next())
            {
            	r2.close();
            	return "hasEmail";
            }
        	//then there are no rows.
            
        	s.executeUpdate("INSERT INTO login.users (username, email, password, enabled) "
        			+ "VALUES('" + id + "','" + email + "','" + pw + "','T')");
        	
        	s.executeUpdate("INSERT INTO login.user_roles (username, authority) "
        			+ "VALUES('" + id + "','ROLE_USER')");

        	r1.close();
            r2.close();
            
            s.close(); 
            conn.close(); 
        } 
        catch( Exception e ) 
        { 
        	e.printStackTrace(); 
        	return "error";
        } 
        
        return "ok";
	}

}
