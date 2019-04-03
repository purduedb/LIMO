package example.edu.server;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.python.core.Py;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import example.edu.client.gui.LimoPythonException;
import example.edu.client.service.ExampleService;
 
public class ExampleServiceImpl extends RemoteServiceServlet implements ExampleService{
 
    private final String url = "jdbc:postgresql://ibnkhaldun.cs.purdue.edu:5439/gisdb2";
    //private final String url = "jdbc:postgresql://localhost:5432/gisDBTest";
    //private final String url = "jdbc:postgresql://192.168.126.147:5432/gisDBTest";
     
    public static String resultString = "";
     
    @Override
	public String sayHello(String name) {
        double lon=0;
        double lat =0;
        String out="";
         
        java.sql.Connection conn; 
        try { 
            Class.forName("org.postgresql.Driver"); 
            //String url = "jdbc:postgresql://localhost:5432/gisDBTest";
             
            conn = DriverManager.getConnection(url, "postgres", ""); 
             
            Statement s = conn.createStatement(); 
            ResultSet r = s.executeQuery("SELECT ST_X(geomout), ST_Y(geomout) FROM geocode_intersection( 'ELIZABETH ST','14TH', 'IN', 'LAFAYETTE', '47904',1)");
            //ResultSet r = s.executeQuery("SELECT ST_X(g.geomout) As lon, ST_Y(g.geomout) As lat FROM geocode('305 N. University Street West Lafayette, IN  47907',1) As g");
           // ResultSet r = s.executeQuery("SELECT ST_X(g.geomout) As lon, ST_Y(g.geomout) As lat FROM geocode('610 Purdue Mall, IN  47907',1) As g");
 
            if (r.next())
            {
                lon = r.getDouble(1);
                lat = r.getDouble(2);
                         
                System.out.println(lon);
                System.out.println(lat);
            }
 
            s.close(); 
            conn.close(); 
          } 
        catch( Exception e ) { 
          e.printStackTrace(); 
          } 
         
        out = String.valueOf(lon)+"$"+String.valueOf(lat);
        System.out.println(out);
        return out;
         
    }
     @Override
    public int addTwoNumbers(int num1, int num2) {
        int y = num1 + num2;
        return y;
    }
       /**
     * @param scripy - program script
     * 
     * -executes user's script
     * return script output, error, and visualization instructions 
     * @throws IOException 
     * @throws LimoPythonException 
     */
    public List<String> runScript(String script) throws IOException, LimoPythonException {
         
    	
        List<String> output = new ArrayList<String>();
        // append script on the end of the content of limp.py
         
        String limoscript;
        int lineCnt = 1;
        //BufferedReader br = new BufferedReader(new FileReader("limo.py"));
        BufferedReader br = new BufferedReader(new FileReader(this.getThreadLocalRequest().getSession().getServletContext()
		        .getRealPath("/limo2.31.py").toString()));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
				lineCnt++;
			}
			limoscript = sb.toString();
		} finally {
			br.close();
		}

		limoscript += "\n";

		// add program script
		limoscript += script;
		limoscript += "\n";
		
		
//		limoscript += "def mainFunctionCall():\n";
//		
//		String[] lines = script.split("\n");
//		for(String line : lines)
//		{
//			limoscript += " " + line + "\n";
//		}
//		
//		
//		limoscript += "\n";
//		
//		limoscript += "try:\n"
//					+ "    with time_limit(1):\n"
//					+ "        mainFunctionCall()\n"
//					+ "    except TimeoutException, msg:\n"
//					+ "        print \"Timed out!\"\n";

		// Run the script
		PythonInterpreter interp = new PythonInterpreter(null,
				new PySystemState());

		
		PySystemState sys = Py.getSystemState();
        sys.path.append(new PyString(this.getThreadLocalRequest().getSession().getServletContext()
		        .getRealPath("/WEB-INF/lib/").toString()));
        
        sys.path.append(new PyString(this.getThreadLocalRequest().getSession().getServletContext()
		        .getRealPath("/WEB-INF/classes/example/edu/server/").toString()));
        
        
		StringWriter scriptTextOutput = new StringWriter();

		interp.setOut(scriptTextOutput);

		StringWriter scriptTextError = new StringWriter();

		interp.setErr(scriptTextError);

		// create a text file to append the visualization instructions
		

//		FileWriter fw = new FileWriter("visualize.txt", false);
//		fw.close();
		
		ExampleServiceImpl.resultString = "";

		try 
		{
			interp.exec(limoscript);
			
		} catch (Exception e) 
		{
			e.printStackTrace();
			LimoPythonException lpe = new LimoPythonException(e, scriptTextOutput.toString(), scriptTextError.toString(), lineCnt);
			throw lpe;
			
		}

		// System.out.println("Python code is called on server");

		String outputStr = scriptTextOutput.toString();

		// System.out.println(outputStr);

		// String outputStrErr = scriptTextError.toString();

		// System.out.println(outputStrErr);

		output.add(outputStr);
		// output.add(outputStrErr);

//		String visScript;
//
//		BufferedReader b = new BufferedReader(new FileReader("visualize.txt"));
//		try 
//		{
//			StringBuilder sb = new StringBuilder();
//			String line = b.readLine();
//
//			while (line != null) 
//			{
//				sb.append(line);
//				sb.append(System.lineSeparator());
//				line = b.readLine();
//			}
//			
//			visScript = sb.toString();
//		} 
//		finally 
//		{
//			b.close();
//		}
//                 
//	    //System.out.println(visScript);
//	    output.add(visScript);
		output.add(ExampleServiceImpl.resultString);
		
        return output;
    }
    
    public List<String> readRoadNetwork(String nodesfile, String edgesfile, String delim) throws IOException{
    	
    	List<String> output = new ArrayList<String>();
    	output.add("");
    	
    	In edgesInput;
    	In nodesInput;

    	Graph Gr;

    	try{
    		
    		ST<Long, Pair<Double,Double>> st = new ST<Long, Pair<Double,Double>>();
    		
    		nodesInput = new In(nodesfile);
    		String[] lines = nodesInput.readAllLines();
    		for (int i=1; i< lines.length; i++){
    			String[] nodeData = lines[i].split(delim);
    			st.put(Long.valueOf(nodeData[0]), new Pair<Double, Double>(Double.valueOf(nodeData[1]),Double.valueOf(nodeData[2])));
    			
    		}
    		
    		edgesInput = new In(edgesfile);
    		Gr = new Graph(edgesInput, delim);
    		String outputStr = "";
    		for (Long v : Gr.vertices()) {
               //v
                for (Long w : Gr.adjacentTo(v)) {
                	//w
                	Pair<Double,Double> vLonLat = st.get(v);
                	Pair<Double,Double> wLonLat = st.get(w);
                	outputStr += "POLYLINE,"+ vLonLat.getValue1() +";" +vLonLat.getValue0()+"," + wLonLat.getValue1()  +";" + wLonLat.getValue0() + "\n";
                    
                }
               
            } 
    		
    		output.add(outputStr);
    	}
    	catch (Exception e) {
    		System.out.println();
    		System.out.println(e); }

    	return output;
    	
    }
    
    public static void setResultString(String s)
    {
    	ExampleServiceImpl.resultString += s;
//    	System.out.println(">>" + ExampleServiceImpl.resultString + "<<");
    }
    
    public static String getResultString()
    {
    	return ExampleServiceImpl.resultString;
    }
    
    @Override
    public String anonymousRedirect()
    {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean r = false;		
		for (GrantedAuthority a : auth.getAuthorities())
		{
//			if (a.getAuthority().equals("ROLE_ADMIN"))
//			else 
			if (a.getAuthority().equals("ROLE_USER"))
				r = true;
                
		}
		
		if(!r)
			return "TO_LOGIN";
		else
			return "";
    }
    
	@Override
	public String checkUserLogin() 
	{

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		return auth.getName();
		
	}
	@Override
	public List<String> loadProgramList(String userName) {
		
		List<String> results = new ArrayList<String>();
		
		java.sql.Connection conn; 
        try { 
            Class.forName("org.postgresql.Driver"); 
            //String url = "jdbc:postgresql://localhost:5432/gisDBTest";
             
            conn = DriverManager.getConnection(url, "limo", "limo"); 
             
            Statement s = conn.createStatement(); 
            ResultSet r = s.executeQuery("SELECT filename FROM login.programs WHERE username='"+userName+"'");
            
            
            while (r.next())
            {
                System.err.println(r.getString(1));
                results.add(r.getString(1));
            }
            r.close();
            s.close(); 
            conn.close(); 
          } 
        catch( Exception e ) { 
          e.printStackTrace(); 
          } 
         
//        out = String.valueOf(lon)+"$"+String.valueOf(lat);
//        System.out.println(out);
		
		return results;
	}
	
	
	@Override
	public String saveProgram(String username, String filename, String script)
	{
		java.sql.Connection conn; 
        try { 
            Class.forName("org.postgresql.Driver"); 
            //String url = "jdbc:postgresql://localhost:5432/gisDBTest";
             
            conn = DriverManager.getConnection(url, "limo", "limo"); 
             
            Statement s = conn.createStatement();
            
            ResultSet r = s.executeQuery("SELECT contents FROM login.programs WHERE username = '"
            			+ username +"' AND filename = '" + filename + "'");
            
            if (!r.next()) 
            {//then there are no rows.
            	s.executeUpdate("INSERT INTO login.programs (username, filename, contents) VALUES('"
						+ username + "','" + filename + "','" + script + "')");
			}
			else 
			{
				s.executeUpdate("UPDATE login.programs SET contents = '"+ script +"'WHERE username = '"
						+ username + "' AND filename = '" + filename + "'");

			}
            
            s.close(); 
            conn.close(); 
          } 
        catch( Exception e ) { 
          e.printStackTrace(); 
          } 
         
//        out = String.valueOf(lon)+"$"+String.valueOf(lat);
//        System.out.println(out);
		
		return "ok";
	}
	
	@Override
	public String loadProgram(String username, String filename)
	{
		String result = "";
		java.sql.Connection conn; 
        try { 
            Class.forName("org.postgresql.Driver"); 
            //String url = "jdbc:postgresql://localhost:5432/gisDBTest";
             
            conn = DriverManager.getConnection(url, "limo", "limo"); 
             
            Statement s = conn.createStatement(); 
            
            ResultSet r = s.executeQuery("SELECT contents FROM login.programs WHERE username = '"
            							+ username + "' and filename = '" + filename + "'");
            
            if (r.next())
            {
//                System.err.println(r.getString(1));
                result = r.getString(1);
            }
            r.close();
            s.close(); 
            conn.close(); 
          } 
        catch( Exception e ) { 
          e.printStackTrace(); 
          } 
         
		return result;
	}

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