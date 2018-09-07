package example.edu.client.gui.handler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

import example.edu.client.gui.LoginGUI;
import example.edu.client.gui.MainGUI;

public class SignupHandler implements ClickHandler
{
	TextBox idLabel;
	TextBox emailLabel;
	PasswordTextBox pwLabel;
	PasswordTextBox pw2Label;
//	LoginGUI gui;
	MainGUI gui;
	
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


	
//	public SignupHandler(LoginGUI gui, TextBox id, TextBox email, PasswordTextBox pw, PasswordTextBox pw2)
//	{
//		this.gui = gui;
//		this.idLabel = id;
//		this.emailLabel = email;
//		this.pwLabel = pw;
//		this.pw2Label = pw2;
//	
//	}
	
	public SignupHandler(MainGUI gui, TextBox id, TextBox email, PasswordTextBox pw, PasswordTextBox pw2)
	{
		this.gui = gui;
		this.idLabel = id;
		this.emailLabel = email;
		this.pwLabel = pw;
		this.pw2Label = pw2;
	
	}
	
	@Override
	public void onClick(ClickEvent event) 
	{
		gui.printConsole("entered in handler");
		
		String id = idLabel.getText();
		String email = emailLabel.getText();
		String pw = pwLabel.getText();
		String pw2 = pw2Label.getText();
		
		// id length check
		if(id.length() < 6)
		{
			gui.getStatusLabel().setText("ID need to be at least 6!");
			return;
		}
		
	    // EMAIL CHECK
		if(!email.matches(EMAIL_PATTERN))
		{
			gui.getStatusLabel().setText("Check email address!");
			return;
		}
		else
		{
			gui.getStatusLabel().setText("");
		}

		// PASSWORD CHECK
		if( !pw.equals(pw2) )
		{
			gui.getStatusLabel().setText("Check password!");
			return;
		}	
		else
		{
			gui.getStatusLabel().setText("");
		}
		
		// PASSWORD LENGTH
		if(pw.length() < 6)
		{
			gui.getStatusLabel().setText("Password need to be at least 6!");
			return;
		}
		else
		{
			gui.getStatusLabel().setText("");
		}
		
		gui.getServiceImpl().signupUser(id, email, pw);

	}
	
}