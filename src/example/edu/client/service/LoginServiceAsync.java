package example.edu.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {
	void signupUser(String id, String email, String pw, AsyncCallback signupUserCallback);
}
