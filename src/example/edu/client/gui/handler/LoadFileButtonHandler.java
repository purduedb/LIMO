package example.edu.client.gui.handler;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;

import example.edu.client.gui.MainGUI;

public class LoadFileButtonHandler implements ChangeHandler {

	private MainGUI gui;
	
	public LoadFileButtonHandler(MainGUI gui)
	{
		this.gui = gui;
	}
	
	@Override
	public void onChange(ChangeEvent event) {
		// TODO Auto-generated method stub
		if(!gui.getSelectedFileName().equals("File List.."))
		{
			gui.setCurrentProgramName(gui.getSelectedFileName());
			gui.getServiceImpl().loadProgram(gui.getUsername(), gui.getSelectedFileName());
		}
	}

}
