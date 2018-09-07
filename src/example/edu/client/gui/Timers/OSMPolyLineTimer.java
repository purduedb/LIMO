package example.edu.client.gui.Timers;



import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.LineString;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.user.client.Timer;

import example.edu.client.gui.MainGUI;



public class OSMPolyLineTimer extends Timer {
	
	private int cnt;
	private int size;
	private Vector vectorLayer;
	Point[] linePoints;
	private boolean big;
	private String color;
	
	public OSMPolyLineTimer(Point[] linePoints, Map map, boolean big, String color)
	{
		
		this.cnt = 0;
		this.linePoints = linePoints;
		this.size = this.linePoints.length;
		this.big = big;
		this.vectorLayer = new Vector("Vector Layer");
		this.color = color;
		
		map.addLayer(vectorLayer);
		
//		System.out.println("size : " + size);
        	
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		if(big)
		{
//			System.out.println("b");
			LineString line = new LineString(linePoints);
            
            VectorFeature lineFeature = new VectorFeature(line);
            Style st3 = new Style();
            st3.setStrokeColor(color);
            st3.setStrokeWidth(2);
            lineFeature.setStyle(st3);
            vectorLayer.addFeature(lineFeature);
            MainGUI.numJob++;
            this.cancel();
            
            
		}
		else
		{
			if(this.cnt < this.size-1)
			{
		        Point[] linePoints = {this.linePoints[cnt], this.linePoints[cnt+1]};
		        this.cnt++;
		        
		        LineString line = new LineString(linePoints);
	            
	            VectorFeature lineFeature = new VectorFeature(line);
	            Style st3 = new Style();
	            st3.setStrokeColor(color);
	            st3.setStrokeWidth(2);
	            lineFeature.setStyle(st3);
	            vectorLayer.addFeature(lineFeature);
	            
			    
	        }
			else
			{
	//			System.out.println("Timer Stopped");
//				System.out.println("2");
				MainGUI.numJob++;
				this.cancel();
				
			}
		}
	}

}
