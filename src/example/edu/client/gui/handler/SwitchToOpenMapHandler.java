package example.edu.client.gui.handler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.nitrous.gwt.earth.client.api.GELayerId;
import com.nitrous.gwt.earth.client.api.GEPlugin;
import com.nitrous.gwt.earth.client.api.GEPluginReadyListener;
import com.nitrous.gwt.earth.client.api.GEVisibility;
import com.nitrous.gwt.earth.client.api.GoogleEarth;
import com.nitrous.gwt.earth.client.api.GoogleEarthWidget;
import com.nitrous.gwt.earth.client.api.KmlAltitudeMode;
import com.nitrous.gwt.earth.client.api.KmlLookAt;

import example.edu.client.gui.MainGUI;
import example.edu.client.gui.Variables;

public class SwitchToOpenMapHandler implements ClickHandler 
{
	private MainGUI gui;
	
	public SwitchToOpenMapHandler(MainGUI gui)
	{
		this.gui = gui;
	}
	
	@Override
	public void onClick(ClickEvent event) 
	{		
		gui.switchMapPanel(Variables.OpenMap);
		gui.switchMapButton(Variables.OpenMap);		
	}
}
