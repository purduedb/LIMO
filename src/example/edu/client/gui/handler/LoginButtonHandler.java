package example.edu.client.gui.handler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import example.edu.client.gui.MainGUI;

public class LoginButtonHandler implements ClickHandler {

	private MainGUI gui;
	
	
	public LoginButtonHandler(MainGUI gui)
	{
		this.gui = gui;
	}
	
	@Override
	public void onClick(ClickEvent event) 
	{
		
		gui.getLoginDialogBox().show();
		gui.getIdTextBox().setFocus(true);
	}
	
	
}