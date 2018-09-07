package example.edu.client.gui.handler;

import org.gwtopenmaps.openlayers.client.LonLat;

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

public class SwitchToGoogleEarthHandler implements ClickHandler 
{
	private MainGUI gui;
	private GoogleEarthWidget earthWidget;
	
	public SwitchToGoogleEarthHandler(MainGUI gui)
	{
		this.gui = gui;
	}
	
	@Override
	public void onClick(ClickEvent event) 
	{		
		if(gui.isEarthWidgetNull())
		{
			earthWidget = new GoogleEarthWidget();
			
			GoogleEarth.loadApi(new Runnable(){
				@Override
				public void run() {
					// start the application
					onApiLoaded();				
				}    		
			});
		}
		else
		{
			gui.switchMapPanel(Variables.GoogleEarth);
			gui.switchMapButton(Variables.GoogleEarth);
		}
		

	}
	
	/**
	 * This is called only once at the beginning of google earth plug-in loading.
	 */
	private void onApiLoaded()
	{
        // register a listener to be notified when the earth plug-in has loaded
		earthWidget.addPluginReadyListener(new GEPluginReadyListener() 
		{
            	public void pluginReady(GEPlugin ge) 
            	{
	            	// show map content once the plugin has loaded
	                loadInitMapContent();
            	}

            	public void pluginInitFailure() 
            	{
	                // failure!
	                Window.alert("Failed to initialize Google Earth Plug-in");
            	}
        });
		
		gui.insertGoogleEarthToPanel(earthWidget);
		earthWidget.init();
		gui.setEarthWidget(earthWidget);
		gui.switchMapButton(Variables.GoogleEarth);
	}
	
	/**
     * Set initial position.	
     */
    private void loadInitMapContent() 
    {
        // The GEPlugin is the core class and is a great place to start browsing the API
        GEPlugin ge = earthWidget.getGEPlugin();
        ge.getWindow().setVisibility(true);
        
        // add a navigation control
        ge.getNavigationControl().setVisibility(GEVisibility.VISIBILITY_AUTO);
  
        // add some layers
        ge.enableLayer(GELayerId.LAYER_BORDERS, true);
        ge.enableLayer(GELayerId.LAYER_ROADS, true);
        ge.enableLayer(GELayerId.LAYER_BUILDINGS, true);
        
        
        // Fly to the West Lafayette
		KmlLookAt la = ge.createLookAt("");
		la.set(40.4239744418587, -86.9143287383871, 0, KmlAltitudeMode.ALTITUDE_RELATIVE_TO_GROUND, 0, 30, 1000);
		ge.getView().setAbstractView(la);
        
    }

	
	
}
