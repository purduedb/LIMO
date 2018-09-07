package example.edu.client.service;

import java.io.IOException;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


public interface ExampleServiceAsync {
	void sayHello(String name, AsyncCallback callback);
	void addTwoNumbers(int num1, int num2, AsyncCallback callback);
	void runScript(String script, AsyncCallback callback);
	void readRoadNetwork(String filename, String edgesfile, String delim, AsyncCallback callback);
	void checkUserLogin(AsyncCallback callback);
	void anonymousRedirect(AsyncCallback callback);
	void loadProgramList(String userName, AsyncCallback callback);
	void saveProgram(String username, String filename, String script, AsyncCallback saveProgramCallback);
	void loadProgram(String username, String filename, AsyncCallback loadProgramCallback);
	void signupUser(String id, String email, String pw, AsyncCallback signupUserCallback);

}
