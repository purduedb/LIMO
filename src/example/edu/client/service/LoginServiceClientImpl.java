package example.edu.client.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import example.edu.client.gui.LoginGUI;


public class LoginServiceClientImpl implements LoginServiceClientInt{
	private LoginServiceAsync service;
	private LoginGUI logingui;
		
		//url location of servlet
		public LoginServiceClientImpl(String url){
			System.err.println(url);
			this.service = GWT.create(LoginService.class);
			ServiceDefTarget endpoint = (ServiceDefTarget) this.service;
			endpoint.setServiceEntryPoint(url);

			this.logingui = new LoginGUI(this);
		}

		public LoginGUI getLoginGUI(){
			return this.logingui;
		}
		
		private class SignupCallback implements AsyncCallback<String>{

			@Override
			public void onFailure(Throwable caught) {
				
				System.out.println(caught.getMessage());
				if (caught.getCause()!=null)
					System.out.println("Error");
				
				
//				maingui.updateDashboardTextArea(caught.getLocalizedMessage(), true);
			}


			@Override
			public void onSuccess(String result) {
				
				System.out.println("Response received");
				
				if(result != null)
				{
					if(result.equals("hasID"))
					{
						logingui.getStatusLabel().setText("ID already exists!");
					}
					else if(result.equals("hasEmail"))
					{
						logingui.getStatusLabel().setText("Email already exists!");
					}
					else if(result.equals("ok"))
					{
						logingui.printConsole("register done");
						logingui.getDialogBox().hide();
						logingui.getIdTextBox().setText(
								logingui.getSignupIdTextBox().getText());
						logingui.getPwBox().setFocus(true);
					}
					else
					{
						logingui.getStatusLabel().setText("Error occured on server!");
					}
				}
				
			}
		}
		
		@Override
		public void signupUser(String id, String email, String pw)
		{
			this.service.signupUser(id, email, pw, new SignupCallback());
		}
		
	}