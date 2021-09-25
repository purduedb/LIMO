package example.edu.client.service;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import example.edu.client.gui.MainGUI;

public class ExampleServiceClientImpl implements ExampleServiceClientInt{
private ExampleServiceAsync service;
private MainGUI maingui;
	
	//url location of servlet
	public ExampleServiceClientImpl(String url){
		System.out.println(url);
		this.service = GWT.create(ExampleService.class);
		ServiceDefTarget endpoint = (ServiceDefTarget) this.service;
		endpoint.setServiceEntryPoint(url);

		this.maingui = new MainGUI(this);
	}

	@Override
	public void sayHello(String name) {
		this.service.sayHello(name, new DefaultCallback());		
	}

	@Override
	public void addTwoNumbers(int num1, int num2) {
		this.service.addTwoNumbers(num1, num2, new DefaultCallback());		
	}
	public MainGUI getMainGUI(){
		return this.maingui;
	}
	private class DefaultCallback implements AsyncCallback<List<String>>{

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
			if (caught.getCause()!=null)
				System.out.println("Error");
			
			
			maingui.updateDashboardTextArea(caught.getLocalizedMessage(), true);
		}

		@Override
		public void onSuccess(List<String> result) {
			
			System.out.println("Response received");
			
			if (result != null)
			{
				maingui.updateInterface(result);
			}

		}
	}
	
	@Override
	public void runScript(String script) {
		// TODO Auto-generated method stub
		this.service.runScript(script, new DefaultCallback());
		
	}
	
	@Override
	public void readRoadNetwork(String nodeFileName, String edgesfile, String delim) {
		// TODO Auto-generated method stub
		this.service.readRoadNetwork(nodeFileName, edgesfile, delim, new DefaultCallback());
		
	}
	
	private class LoginCallback implements AsyncCallback<String>{

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
			if (caught.getCause()!=null)
				System.out.println("Error");
			
			
			maingui.updateDashboardTextArea(caught.getLocalizedMessage(), true);
		}

		@Override
		public void onSuccess(String result) {
			
			System.out.println("Response received");
			
			if (result != null)
			{
				maingui.loginCheck(result);
			}

		}
	}
	
	
	
	@Override
	public void checkUserLogin() 
	{
		this.service.checkUserLogin(new LoginCallback());
	}
	
	private class AnonyCallback implements AsyncCallback<String>{

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
			if (caught.getCause()!=null)
				System.out.println("Error");
			
			
			maingui.updateDashboardTextArea(caught.getLocalizedMessage(), true);
		}

		@Override
		public void onSuccess(String result) {
			
			System.out.println("Response received");
			
			if (result != null)
			{
				if (result.equals("TO_LOGIN"))
				{
					maingui.redirect(maingui.getAbsolutePath("/Login.html"));
				}
			}

		}
	}
	
	
	@Override
	public void anonymousRedirect()
	{
		
		this.service.anonymousRedirect(new AnonyCallback());
	}
	private class LoadProgramListCallback implements AsyncCallback<List<String>>{

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
			if (caught.getCause()!=null)
				System.out.println("Error");
			
			
			maingui.updateDashboardTextArea(caught.getLocalizedMessage(), true);
		}


		@Override
		public void onSuccess(List<String> result) {
			
			System.out.println("Response received");
			
			if (result != null)
			{
				maingui.loadProgramList(result);
			}
		}
	}
	
	@Override
	public void loadProgramList(String userName) {
		this.service.loadProgramList(userName, new LoadProgramListCallback());
		
	}
	
	private class SaveProgramCallback implements AsyncCallback<String>{

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
			if (caught.getCause()!=null)
				System.out.println("Error");
			
			
			maingui.updateDashboardTextArea(caught.getLocalizedMessage(), true);
		}


		@Override
		public void onSuccess(String result) {
			
			System.out.println("Response received");
			
			if (result != null)
			{
//				maingui.loadProgramList(result);
				if(result.equals("maxPrograms")) {
					maingui.updateDashboardTextArea("ERROR: Maximum saving programs used, overwrite existing one.", true);
				}
				else {
					maingui.updateDashboardTextArea("Saved: " + result, true);
					maingui.printConsole("saved:" + result);
					maingui.callLoadProgramList();
				}
				
			}
		}
	}
	
	@Override
	public void saveProgram(String username, String filename, String script)
	{
		this.service.saveProgram(username, filename, script, new SaveProgramCallback());
	}
	
	private class LoadProgramCallback implements AsyncCallback<String>{

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
			if (caught.getCause()!=null)
				System.out.println("Error");
			
			
			maingui.updateDashboardTextArea(caught.getLocalizedMessage(), true);
		}


		@Override
		public void onSuccess(String result) {
			
			System.out.println("Response received");
			
			if (result != null)
			{
				maingui.loadProgramScript(result);
				maingui.printConsole("loaded:" + result);
			}
		}
	}
	
	@Override
	public void loadProgram(String username, String filename)
	{
		this.service.loadProgram(username, filename, new LoadProgramCallback());
	}
	
	private class SignupCallback implements AsyncCallback<String>{

		@Override
		public void onFailure(Throwable caught) {
			
			System.out.println(caught.getMessage());
			if (caught.getCause()!=null)
				System.out.println("Error");
			
			
//			maingui.updateDashboardTextArea(caught.getLocalizedMessage(), true);
		}


		@Override
		public void onSuccess(String result) {
			
			System.out.println("Response received");
			
			if(result != null)
			{
				if(result.equals("maxIDs")) {
					maingui.getStatusLabel().setText("Registration closed!");
				}
				else if(result.equals("hasID"))
				{
					maingui.getStatusLabel().setText("ID already exists!");
				}
				else if(result.equals("hasEmail"))
				{
					maingui.getStatusLabel().setText("Email already exists!");
				}
				else if(result.equals("ok"))
				{
					maingui.printConsole("register done");
					maingui.getSignupDialogBox().hide();
					maingui.getLoginDialogBox().show();
					maingui.getIdTextBox().setText(maingui.getSignupIdTextBox().getText());
					maingui.getPwBox().setFocus(true);
				}
				else
				{
					maingui.getStatusLabel().setText("Error occured on server!");
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
