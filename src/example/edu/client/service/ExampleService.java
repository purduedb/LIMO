package example.edu.client.service;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import example.edu.client.gui.LimoPythonException;

//shortcut name for servlet
@RemoteServiceRelativePath("exampleservice")

public interface ExampleService extends RemoteService{
	String sayHello(String name);
	int addTwoNumbers(int num1, int num2);
	// runs the python script
	// returns a list where list[0] contains the output and list[1] contains the error and list[2] contains
	// the visualization instructions e.g., add marker, display a message, etc.
	List<String> runScript(String script) throws IOException, LimoPythonException;
	List<String> readRoadNetwork(String nodesfile, String edgesfile, String delim) throws IOException;
	String checkUserLogin();
	String anonymousRedirect();
	List<String> loadProgramList(String userName);
	String saveProgram(String username, String filename, String script);
	String loadProgram(String username, String filename);
	String signupUser(String id, String email, String pw);

}
