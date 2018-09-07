package example.edu.client.gui.Timers;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.geometry.Point;

import com.google.gwt.user.client.Timer;
import com.nitrous.gwt.earth.client.api.GEPlugin;
import com.nitrous.gwt.earth.client.api.KmlCoordArray;
import com.nitrous.gwt.earth.client.api.KmlLineStyle;
import com.nitrous.gwt.earth.client.api.KmlLinearRing;
import com.nitrous.gwt.earth.client.api.KmlPlacemark;
import com.nitrous.gwt.earth.client.api.KmlPolyStyle;
import com.nitrous.gwt.earth.client.api.KmlPolygon;
import com.nitrous.gwt.earth.client.api.KmlStyle;

import example.edu.client.gui.Variables;

public class PolygonTimer extends Timer 
{
	private int cnt;
	private int size;
	private Point[] pp;
	
	private KmlPlacemark polygonPlacemark;
	private KmlPolygon polygon;
	private KmlLinearRing boundary;
	private KmlStyle style;
	private KmlLineStyle lineStyle;
	private KmlPolyStyle polyStyle;
	private KmlCoordArray coords; 
	
	public PolygonTimer(Point[] pp, GEPlugin ge)
	{
		this.pp = pp;
		cnt = 0;
		size = pp.length;
		
//		System.out.println("Polygon Size " + size);
		
		// Make polygon placemark
        polygonPlacemark = ge.createPlacemark("");
        polygon = ge.createPolygon("");
        
        polygonPlacemark.setGeometry(polygon);
        boundary = ge.createLinearRing("");
        polygon.setOuterBoundary(boundary);
        
        // Style the polygon
	    if (polygonPlacemark.getStyleSelector() == null)
	    {
	        polygonPlacemark.setStyleSelector(ge.createStyle(""));
	    }
        style = (KmlStyle)polygonPlacemark.getStyleSelector();
        
        lineStyle = style.getLineStyle();
	    lineStyle.setWidth(lineStyle.getWidth() + Variables.earthLineWidth);
	    lineStyle.getColor().set(Variables.earthLineTransparent);
 
	    polyStyle = style.getPolyStyle();
	    polyStyle.getColor().set(Variables.earthLineTransparent);
	    
        ge.getFeatures().appendChild(polygonPlacemark);
        
        do
        {
	        boundary.getCoordinates().pushLatLngAlt(pp[cnt].getY(), pp[cnt].getX(), Variables.defaultAltitude);
	    	cnt++;
        }while(cnt < size);
	    
	}

	@Override
	public void run() 
	{
		if(cnt == size)
		{
			 polyStyle.getColor().set(Variables.earthPolyColor);
			 cnt++;
        }
		else
		{
//			System.out.println("Timer Stopped");
			this.cancel();
		}
		
				
	}

}
