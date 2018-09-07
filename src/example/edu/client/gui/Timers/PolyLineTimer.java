package example.edu.client.gui.Timers;

import java.util.ArrayList;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.geometry.Point;

import com.google.gwt.user.client.Timer;
import com.nitrous.gwt.earth.client.api.GEPlugin;
import com.nitrous.gwt.earth.client.api.GoogleEarthWidget;
import com.nitrous.gwt.earth.client.api.KmlLineString;
import com.nitrous.gwt.earth.client.api.KmlLineStyle;
import com.nitrous.gwt.earth.client.api.KmlPlacemark;
import com.nitrous.gwt.earth.client.api.KmlStyle;

import example.edu.client.gui.MainGUI;
import example.edu.client.gui.Variables;

public class PolyLineTimer extends Timer 
{
	private int cnt;
	private int size;
	private boolean big;
	private ArrayList<Point> ll;
	private KmlPlacemark lineStringPlacemark;
	private KmlLineString lineString; 
	
	public PolyLineTimer(ArrayList<Point> ll, GEPlugin ge, boolean big)
	{
		this.ll = ll;
		this.big = big;
		cnt = 0;
		size = ll.size();
		
		// create the line string
		lineStringPlacemark = ge.createPlacemark("");
		lineString = ge.createLineString("");
		lineStringPlacemark.setGeometry(lineString);
        lineString.setTessellate(true);
        
        // Styling
        lineStringPlacemark.setStyleSelector(ge.createStyle(""));
        KmlStyle style = (KmlStyle)lineStringPlacemark.getStyleSelector();
        KmlLineStyle lineStyle = style.getLineStyle();
        lineStyle.setWidth(lineStyle.getWidth() + Variables.earthLineWidth);
        
        // aabbggrr color..
        lineStyle.getColor().set("641400DC");
        ge.getFeatures().appendChild(lineStringPlacemark);
        
//        System.out.println("size : " + size);
	}

	@Override
	public void run() 
	{
		if(big)
		{
//			System.out.println("b");
			if(size > 50)
			{
//				if(cnt < size)
//				{
//			        lineString.getCoordinates().pushLatLngAlt(ll.get(cnt).getY(), ll.get(cnt).getX(), Variables.defaultAltitude);
//					cnt += 1;
//		        }
				
				for(; cnt < size; cnt += 5)
				{
					lineString.getCoordinates().pushLatLngAlt(ll.get(cnt).getY(), ll.get(cnt).getX(), Variables.defaultAltitude);
				}
				
				if(cnt != size)
				{
//					System.out.println("good");
					lineString.getCoordinates().pushLatLngAlt(ll.get(size - 1).getY(), ll.get(size - 1).getX(), Variables.defaultAltitude);
				}
				MainGUI.numJob++;
				this.cancel();
				
			}
			else
			{
				for(; cnt < size; cnt += 1)
				{
					lineString.getCoordinates().pushLatLngAlt(ll.get(cnt).getY(), ll.get(cnt).getX(), Variables.defaultAltitude);
				}
				
				if(cnt != size)
				{
//					System.out.println("good");
					lineString.getCoordinates().pushLatLngAlt(ll.get(size - 1).getY(), ll.get(size - 1).getX(), Variables.defaultAltitude);
				}
				MainGUI.numJob++;
				this.cancel();
				
			}

		}
		else
		{
			if(cnt < size)
			{
//			    Point p = new Point(ll.get(cnt).lon(), ll.get(cnt).lat());
//		        lineString.getCoordinates().pushLatLngAlt(p.getX(), p.getY(), Variables.defaultAltitude);
//				cnt++;

				//				Point p = new Point(ll.get(cnt).lon(), ll.get(cnt).lat());
		        lineString.getCoordinates().pushLatLngAlt(ll.get(cnt).getY(), ll.get(cnt).getX(), Variables.defaultAltitude);
				cnt++;
	        }
			else
			{
	//			System.out.println("numChunks : " + cnt);
	//			System.out.println("Timer Stopped");
				MainGUI.numJob++;
				this.cancel();
			}
		}
	}

}
