package example.edu.client.service;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface ExampleServiceClientInt {

	void sayHello(String name);
	void addTwoNumbers(int num1, int num2);
	void runScript(String script);
	void readRoadNetwork(String nodeFileName, String edgesfile, String delim);
	void anonymousRedirect();
	void checkUserLogin();
	void loadProgramList(String result);
	void saveProgram(String username, String filename, String script);
	void loadProgram(String username, String filename);
	void signupUser(String id, String email, String pw);

}
