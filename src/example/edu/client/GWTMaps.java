package example.edu.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import example.edu.client.service.ExampleServiceClientImpl;

public class GWTMaps implements EntryPoint 
{

	public void onModuleLoad() 
	{
		List<String> injectList = new ArrayList<String>();
        injectList.add("js/gwt-openlayers/util.js");
        inject(injectList);

        ExampleServiceClientImpl clientImpl = new ExampleServiceClientImpl(GWT.getModuleBaseURL()+"exampleservice");
		
		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(clientImpl.getMainGUI());
		loadTextAreaFormat();
	}
	
	public static native void loadTextAreaFormat()
	/*-{
		$wnd.loadTextAreaFormat();
	}-*/;
	
	private void inject(final List<String> p_jsList)
    {
        final String js =
            GWT.getModuleBaseForStaticFiles() + p_jsList.remove(0);
        
        ScriptInjector.fromUrl(js).setCallback(new Callback<Void, Exception>()
        {
            @Override
            public void onFailure(Exception e)
            {
                System.out.println("inject " + js + " failure " + e);
            }

            @Override
            public void onSuccess(Void ok)
            {
                if (!p_jsList.isEmpty())
                    inject(p_jsList);
//                else if (!Browser.isTouch())
//                    Browser.setTooltips(BUISupport.initJQueryTooltip());
            }
        }).setWindow(ScriptInjector.TOP_WINDOW).inject();
    }
    
}
