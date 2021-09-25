package example.edu.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.gwtopenmaps.openlayers.client.LonLat;
import org.gwtopenmaps.openlayers.client.Map;
import org.gwtopenmaps.openlayers.client.MapOptions;
import org.gwtopenmaps.openlayers.client.MapWidget;
import org.gwtopenmaps.openlayers.client.Marker;
import org.gwtopenmaps.openlayers.client.Projection;
import org.gwtopenmaps.openlayers.client.Style;
import org.gwtopenmaps.openlayers.client.control.LayerSwitcher;
import org.gwtopenmaps.openlayers.client.control.MousePosition;
import org.gwtopenmaps.openlayers.client.feature.VectorFeature;
import org.gwtopenmaps.openlayers.client.geometry.LineString;
import org.gwtopenmaps.openlayers.client.geometry.LinearRing;
import org.gwtopenmaps.openlayers.client.geometry.Point;
import org.gwtopenmaps.openlayers.client.geometry.Polygon;
import org.gwtopenmaps.openlayers.client.layer.Markers;
import org.gwtopenmaps.openlayers.client.layer.OSM;
import org.gwtopenmaps.openlayers.client.layer.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.CustomButton;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.gwt.earth.client.api.GEPlugin;
import com.nitrous.gwt.earth.client.api.GoogleEarthWidget;
import com.nitrous.gwt.earth.client.api.KmlAltitudeMode;
import com.nitrous.gwt.earth.client.api.KmlIcon;
import com.nitrous.gwt.earth.client.api.KmlLookAt;
import com.nitrous.gwt.earth.client.api.KmlObjectList;
import com.nitrous.gwt.earth.client.api.KmlPlacemark;
import com.nitrous.gwt.earth.client.api.KmlPoint;
import com.nitrous.gwt.earth.client.api.KmlStyle;

import example.edu.client.gui.Timers.DelayTimer;
import example.edu.client.gui.Timers.OSMPolyLineTimer;
import example.edu.client.gui.Timers.PolyLineTimer;
import example.edu.client.gui.Timers.PolygonTimer;
import example.edu.client.gui.handler.LoadFileButtonHandler;
import example.edu.client.gui.handler.LoginButtonHandler;
import example.edu.client.gui.handler.SaveAsButtonHandler;
import example.edu.client.gui.handler.SignUpButtonHandler;
import example.edu.client.gui.handler.SignupHandler;
import example.edu.client.gui.handler.SwitchToGoogleEarthHandler;
import example.edu.client.gui.handler.SwitchToOpenMapHandler;
import example.edu.client.service.ExampleServiceClientImpl;

public class MainGUI extends Composite {

	private ExampleServiceClientImpl serviceImpl;
	private SplitLayoutPanel mainSplitPanel;
	private SplitLayoutPanel mapSplitLP;
	private DeckLayoutPanel mapEarthPanel;
	// private SplitLayoutPanel programmingSplitPanel;

	private MapWidget mapWidget;
	private GoogleEarthWidget earthWidget;
	private int currentWidget;

	private DialogBox loginDialogBox;
	private DialogBox signupDialogBox;
	private TextBox signupId;
	private Label statusLabel;
	private TextBox id;
	private PasswordTextBox pw;
	
	private String loginID;
	private boolean tryToSave = false;


	// Buttons for map-earth switching
	private HorizontalPanel switchButtons;
	private CustomButton mapButton;
	private CustomButton mapButtonDown;
	private CustomButton earthButton;
	private CustomButton earthButtonDown;

	// Buttons runScript and clearScript
	private Button runScript;
	private Button clearScript;
	
//	private Button sample1;
//	private Button sample2;
//	private Button sample3;
//	private Button sample4;
//	private Button sample5;
//	private Button sample6;
//	private Button sample7;
//	private Button sample8;

	private HorizontalPanel titleHPanel;
	private MenuBar menu;
	private VerticalPanel programmingVPanel;
	private TextArea dashboardTextArea;
	private TextArea instructionTextArea;
	
	private ListBox samples;
	private ListBox multiBox;

	private String nodesFilename = "";
	private String edgesFilename = "";
	private String delim = "";
	
	private Label runningTextBox;

	String currentLabel;
	public static boolean jobDone;
	public static int numJob;
	public static int totalJob;

	// for flying
	private boolean animationRunning = false;
	private HandlerRegistration frameEndRegistration;

	// program textArea
	private TextArea programScript;
	private HorizontalPanel titleContents;
	private FormPanel loginForm;
	private HTML titleHTML;
	private ListBox programComboBox;
	private Button loadFileButton;
	private Button saveAsButton;
	private String username;
	private String currentProgramName = "";
	public String tempScript = "";
	

	public MainGUI(ExampleServiceClientImpl serviceImpl) {
		this.mainSplitPanel = new SplitLayoutPanel();

		initWidget(this.mainSplitPanel);
		this.serviceImpl = serviceImpl;
		
		///////////////////////////////////////////////////////////// anonymouse redirect
//		serviceImpl.anonymousRedirect();

		this.mainSplitPanel.setStyleName("splitLayoutPanel");
		this.mainSplitPanel.addNorth(createTitleHPanel(), 50);
		this.mainSplitPanel.addEast(createMapPanel(), 625);

		AbsolutePanel boundaryPanel = new AbsolutePanel();
		boundaryPanel.setSize("100%", "100%");
		this.mainSplitPanel.add(boundaryPanel);

		SplitLayoutPanel childSplitPanel = new SplitLayoutPanel();
		childSplitPanel.setSize("100%", "100%");
		boundaryPanel.add(childSplitPanel);

		childSplitPanel.add(createProgrammingVPanel());
		
		loginGUI();
		signupGUI();
//		loadTextAreaFormat();
		
	}

	public ExampleServiceClientImpl getServiceImpl() {
		return serviceImpl;
	}

	public double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	private double calcDistanceCommuted(List<Point> outputPoints) {
		double result = 0;
		int i;
		for (i = 0; i < outputPoints.size() - 1; i++) {
			result = result
					+ distFrom(outputPoints.get(i).getX(), outputPoints.get(i)
							.getY(), outputPoints.get(i + 1).getX(),
							outputPoints.get(i + 1).getY());
		}

		return round(result, 2);
	}

	private double distFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return earthRadius * c;
	}

	public void updateLabel(String lonlat) {

	}

	public void updateFileNames(String nodes, String edges, String delimiter) {
		this.nodesFilename = nodes;
		this.edgesFilename = edges;
		this.delim = delimiter;

	}

	public String getCurrentLabel() {
		return currentLabel;
	}

	public void setCurrentLabel(String currentLabel) {
		this.currentLabel = currentLabel;
	}

	public Point extractPoint(String strPoint) {

		String actualData = strPoint.substring(6, strPoint.length() - 1);
		System.out.println(actualData);

		String[] lonLatTokens = actualData.split(" ");

		System.out.println(lonLatTokens[0]);
		double lon = Double.valueOf(lonLatTokens[0]);
		double lat = Double.valueOf(lonLatTokens[1]);

		Point p = new Point(lon, lat);

		return p;

	}

	// public void processGetLocation(List<String> locationData){
	// String type = "";
	// String data = "";
	//
	// type = locationData.get(0);
	// data = locationData.get(1);
	//
	// if (type.equals("POLYGON")){
	// String actualData = "";
	// actualData = data.substring(15, data.length()-3);
	// System.out.println(actualData);
	//
	// String[] tokens = actualData.split(",");
	// Point[] points = new Point[tokens.length];
	//
	//
	// for (int i = 0; i < tokens.length; i++){
	// String[] lonLatTokens = tokens[i].split(" ");
	// double lon = Double.valueOf(lonLatTokens[0]);
	// double lat = Double.valueOf(lonLatTokens[1]);
	// points[i] = new Point(lon, lat);
	//
	// System.out.println(lon);
	// System.out.println(lat);
	//
	// }
	// addPolygon(points);
	// }
	//
	// }

	public void addPolygon(Point[] points) {

		if (currentWidget == Variables.OpenMap) {
			Vector vectorLayer = new Vector("Vector Layer");
			Map map = mapWidget.getMap();
			map.addLayer(vectorLayer);

			// transform
			for (int i = 0; i < points.length; i++) {
				points[i].transform(new Projection("EPSG:4326"),
						new Projection(map.getProjection()));
			}

			LinearRing[] components = new LinearRing[points.length];

			for (int i = 0; i < points.length; i++) {
				components[i] = new LinearRing(points);
			}

			Style st = new Style();
			st.setFill(false);
			st.setStrokeColor("#8A2908");
			st.setStrokeWidth(3);

			Polygon mipol = new Polygon(components);
			final VectorFeature mipointFeature = new VectorFeature(mipol, st);
			vectorLayer.addFeature(mipointFeature);
		}
		else if (currentWidget == Variables.GoogleEarth)
		{
			// The GEPlugin is the core class and is a great place to start
			// browsing the API
			GEPlugin ge = earthWidget.getGEPlugin();

			PolygonTimer pt = new PolygonTimer(points, ge);
			DelayTimer dt = new DelayTimer(pt);
			dt.schedule(Variables.delayTime);

			// // append points
			// ArrayList<LonLat> ll = new ArrayList<LonLat>();
			//
			// for(int i = 0; i < pointsList.size() - 1; i++)
			// {
			// ll.addAll(getChunkPoints(pointsList.get(i).lon(),
			// pointsList.get(i).lat(),
			// pointsList.get(i+1).lon(), pointsList.get(i+1).lat(),
			// Variables.numPolyChunk));
			// }
			//
			// System.out.println("Set");
			// for(LonLat l : ll)
			// System.out.println(l.lon() +" "+ l.lat());

		} else {
			System.out.println("Unknown Widget in addPolygon()");
		}
		
		numJob++;

	}

	/**
	 * @param lonLat
	 * 
	 *            Add marker on the open map (Variables.OpenMap) or google earth
	 *            (Variables.Googlemap)
	 * 
	 */
	public void addMarkerAt(LonLat lonLat) {
		if (currentWidget == Variables.OpenMap) {
			lonLat.transform("EPSG:4326", "EPSG:900913");

			Map map = mapWidget.getMap();
			Vector vectorLayer = new Vector("Vector Layer");
			map.addLayer(vectorLayer);

			Markers marker = new Markers("start");
			map.addLayer(marker);
			Marker m = new Marker(lonLat);
			marker.addMarker(m);
		} else if (currentWidget == Variables.GoogleEarth) {
			// plot. ge.features is going to have all children
			GEPlugin ge = earthWidget.getGEPlugin();
			KmlPlacemark placemark = ge.createPlacemark("");
			placemark.setName("");
			ge.getFeatures().appendChild(placemark);

			// icon
			KmlIcon icon = ge.createIcon("");
			icon.setHref("http://maps.google.com/mapfiles/kml/pushpin/blue-pushpin.png");
			KmlStyle style = ge.createStyle("");
			style.getIconStyle().setIcon(icon);
			placemark.setStyleSelector(style);

			// place
			KmlPoint point = ge.createPoint("");
			point.setLatitude(lonLat.lat());
			point.setLongitude(lonLat.lon());
			placemark.setGeometry(point);
		} else {
			System.out.println("Unknown Widget in addMarkerAt()");
		}
		
		numJob++;

	}

	/**
	 * @param p
	 *            point parameter
	 * 
	 *            Add a commuter on the open map (Variables.OpenMap) or google
	 *            earth (Variables.Googlemap)
	 */
	public void addCommuterAt(Point p) {
		if (currentWidget == Variables.OpenMap) {
			Map map = mapWidget.getMap();
			Vector vectorLayer = new Vector("Vector Layer");
			map.addLayer(vectorLayer);

			p.transform(new Projection("EPSG:4326"),
					new Projection(map.getProjection()));

			Style commuterStyle = new Style();
			commuterStyle.setFontSize("1em");
			commuterStyle
					.setExternalGraphic(getAbsolutePath("/images/arrow.png"));
			commuterStyle.setGraphicSize(15, 15);
			commuterStyle.setFillOpacity(1.0);

			final VectorFeature commuterFeature = new VectorFeature(p,
					commuterStyle);
			commuterFeature.setFeatureId("DEPO_DIGITECH");
			vectorLayer.addFeature(commuterFeature);
		} else if (currentWidget == Variables.GoogleEarth) {
			// plot. ge.features is going to have all children
			GEPlugin ge = earthWidget.getGEPlugin();
			KmlPlacemark placemark = ge.createPlacemark("");
			placemark.setName("");
			ge.getFeatures().appendChild(placemark);

			// icon
			KmlIcon icon = ge.createIcon("");
			icon.setHref("http://maps.google.com/mapfiles/kml/paddle/red-circle.png");
			KmlStyle style = ge.createStyle("");
			style.getIconStyle().setIcon(icon);
			placemark.setStyleSelector(style);

			// place
			KmlLookAt la = ge.createLookAt("");
			la.set(p.getY(), p.getX(), 0,
					KmlAltitudeMode.ALTITUDE_RELATIVE_TO_GROUND, 0, 0, 0);
			KmlPoint point = ge.createPoint("");
			point.setLatitude(la.getLatitude());
			point.setLongitude(la.getLongitude());
			placemark.setGeometry(point);
		} else {
			System.out.println("Unknown Widget in addCommuterAt()");
		}
	}

	public void addPolyLineBatch(List<LineString> lineStrList) {

		Map map = mapWidget.getMap();
		Vector vectorLayer = new Vector("Vector Layer");
		map.addLayer(vectorLayer);

		for (int i = 0; i < lineStrList.size(); i++) {

			VectorFeature lineFeature = new VectorFeature(lineStrList.get(i));
			Style st3 = new Style();
			st3.setStrokeColor("#FF0000");
			st3.setStrokeWidth(2);
			lineFeature.setStyle(st3);
			vectorLayer.addFeature(lineFeature);

		}

	}

	/**
	 * @param points
	 *            - list of points Draw line or polygon on the open map
	 *            (Variables.OpenMap) or google earth (Variables.Googlemap)
	 */
	public void addPolyLine(List<Point> points, boolean big) 
	{
//		System.out.println("in p : " + points.size());
//		System.out.println("-");
		if (currentWidget == Variables.OpenMap) 
		{

			Map map = mapWidget.getMap();
//			Vector vectorLayer = new Vector("Vector Layer");
//			map.addLayer(vectorLayer);

			// ArrayList<LonLat> ll = new ArrayList<LonLat>();
			//
			// for(int i = 0; i < points.size() - 1; i++)
			// {
			// //ll.addAll(getChunkPointsOSM(points.get(i).getY(),points.get(i).getX()
			// ,points.get(i+1).getY(),
			// //points.get(i+1).getX(),Variables.numLineChunk ));
			//
			// ll.addAll(getChunkPointsOSM(points.get(i).getY(),points.get(i).getX()
			// ,points.get(i+1).getY(),
			// points.get(i+1).getX(),1 ));
			//
			// }

			// Point[] linePoints = new Point[ll.size()];
			//
			// for (int i=0; i< linePoints.length; i++){
			//
			// linePoints[i] = new Point(ll.get(i).lon(), ll.get(i).lat());
			// linePoints[i].transform(new Projection("EPSG:4326"), new
			// Projection(map.getProjection()));
			//
			// }
			Point[] linePoints = new Point[points.size()];

			for (int i = 0; i < linePoints.length; i++) {

				linePoints[i] = new Point(points.get(i).getX(), points.get(i)
						.getY());
				linePoints[i].transform(new Projection("EPSG:4326"),
						new Projection(map.getProjection()));

			}

//			System.out.println("total p : " + linePoints.length);

			OSMPolyLineTimer plt = new OSMPolyLineTimer(linePoints, map, big, "#FF0000");
			DelayTimer dt = new DelayTimer(plt);
			dt.schedule(Variables.delayTimeOSM);

		} 
		else if (currentWidget == Variables.GoogleEarth)
		{
			GEPlugin ge = earthWidget.getGEPlugin();

			// append points
//			ArrayList<LonLat> ll = new ArrayList<LonLat>();

			// for(int i = 0; i < points.size() - 1; i++)
			// {
			// ll.addAll(getChunkPoints(points.get(i).getX(),
			// points.get(i).getY(),
			// points.get(i+1).getX(), points.get(i+1).getY(),
			// Variables.numLineChunk));
			// }

//			for (int i = 0; i < points.size(); i++) {
//				ll.add(new LonLat(points.get(i).getY(), points.get(i).getX()));
//			}

//			System.out.println("total p : " + ll.size());

			PolyLineTimer plt = new PolyLineTimer((ArrayList<Point>)(points), ge, big);
			DelayTimer dt = new DelayTimer(plt);
			dt.schedule(Variables.delayTime);
		} else {
			System.out.println("Unknown Widget in addPolyLine()");
		}

	}
	
	/**
	 * @param points
	 *            - list of points Draw line or polygon on the open map
	 *            (Variables.OpenMap) or google earth (Variables.Googlemap)
	 */
	public void addPolyLine2(List<Point> points, boolean big) 
	{
		if (currentWidget == Variables.OpenMap) 
		{

			Map map = mapWidget.getMap();

			Point[] linePoints = new Point[points.size()];

			for (int i = 0; i < linePoints.length; i++) {

				linePoints[i] = new Point(points.get(i).getX(), points.get(i)
						.getY());
				linePoints[i].transform(new Projection("EPSG:4326"),
						new Projection(map.getProjection()));

			}

			OSMPolyLineTimer plt = new OSMPolyLineTimer(linePoints, map, big, "#0000FF");
			DelayTimer dt = new DelayTimer(plt);
			dt.schedule(Variables.delayTimeOSM);

		} 
		else if (currentWidget == Variables.GoogleEarth)
		{
			GEPlugin ge = earthWidget.getGEPlugin();

			PolyLineTimer plt = new PolyLineTimer((ArrayList<Point>)(points), ge, big);
			DelayTimer dt = new DelayTimer(plt);
			dt.schedule(Variables.delayTime);
		} else {
			System.out.println("Unknown Widget in addPolyLine()");
		}

	}

	/**
	 * @param allPoints
	 *            - all longitude and latitude
	 * @param zoom
	 *            - integer value for zoom out from the ground
	 * 
	 *            Set the camera's view point to the appropriate position
	 */
	public void centerMapAt(ArrayList<Point> allPoints) {
		// get max, min bound for lon/lat

		double lonMax = -Double.MAX_VALUE;
		double lonMin = Double.MAX_VALUE;
		double latMax = -Double.MAX_VALUE;
		double latMin = Double.MAX_VALUE;
		double lon, lat;
		// Point lonMaxPoint, lonMinPoint, latMaxPoint, latMinPoint;

		for (Point P : allPoints) {
			lon = P.getX();
			lat = P.getY();

			if (lon > lonMax)
				lonMax = lon;

			if (lon < lonMin)
				lonMin = lon;

			if (lat > latMax)
				latMax = lat;

			if (lat < latMin)
				latMin = lat;
		}

//		System.out.println("numPoints : " + allPoints.size());

		// get distance lon
		int diagonalDistance = (int) distFrom(latMax, lonMin, latMin, lonMax);

//		System.out.println("Digonal Distance : " + diagonalDistance);

		if (diagonalDistance > 1000) 
		{
			
			ArrayList<Double> lonList = new ArrayList<Double>();
			ArrayList<Double> latList = new ArrayList<Double>();;
			
			for(Point P : allPoints)
			{
				lonList.add(P.getX());
				latList.add(P.getY());
			}
			
			
			
//			System.out.println("Initial List: " + lonList);

			Collections.sort(lonList, new Comparator() 
			{
//				@Override
//				public int compare(Integer i1, Integer i2) {
//
//					return (i2.intValue() > i1.intValue()) ? 1 : -1;
//
//				}

				@Override
				public int compare(Object o1, Object o2) {
					// TODO Auto-generated method stub
					return ((Double)o2 > (Double)o1) ? 1 : -1;
				}
			});

			Collections.sort(latList, new Comparator() 
			{
//				@Override
//				public int compare(Integer i1, Integer i2) {
//
//					return (i2.intValue() > i1.intValue()) ? 1 : -1;
//
//				}

				@Override
				public int compare(Object o1, Object o2) {
					// TODO Auto-generated method stub
					return ((Double)o2 > (Double)o1) ? 1 : -1;
				}
			});
//			System.out.println("Sorted List: " + lonList);
			
			
			
			// get ith values
			int th = (int) (allPoints.size() * 0.2);
			
//			System.out.println("lon max : " + lonMax + " lon min : " + lonMin + " lat max : " + latMax + " lat min " + latMin);
			
			lonMax = lonList.get(th);
			lonMin = lonList.get(lonList.size() - th - 1);
			latMax = latList.get(th);
			latMin = latList.get(latList.size() - th - 1);
			
//			System.out.println("lon max : " + lonMax + " lon min : " + lonMin + " lat max : " + latMax + " lat min " + latMin);
			

		}

		// get center Point
		lon = 0.0;
		lat = 0.0;
		double hyp = 0.0;
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;

		Point[] a = new Point[2];
		a[0] = new Point(lonMin, latMax);
		a[1] = new Point(lonMax, latMin);

		for (Point p : a) {
			x += Math.cos(p.getX() * Math.PI / 180)
					* Math.cos(p.getY() * Math.PI / 180);
			y += Math.cos(p.getY() * Math.PI / 180)
					* Math.sin(p.getX() * Math.PI / 180);
			z += Math.sin(p.getY() * Math.PI / 180);
		}

		x /= 2;
		y /= 2;
		z /= 2;

		lon = Math.atan2(y, x);
		hyp = Math.sqrt(x * x + y * y);
		lat = Math.atan2(z, hyp);

		LonLat lonLat = new LonLat(lon * 180 / Math.PI, lat * 180 / Math.PI);

		if (currentWidget == Variables.OpenMap) {
			Map map = mapWidget.getMap();
			lonLat.transform("EPSG:4326", "EPSG:900913");

//			System.out.println("Map center value: "
//					+ (int) Math.ceil(Math.log10(diagonalDistance)
//							/ Math.log10(2)));

			if (diagonalDistance != 0 && diagonalDistance > 10) {
				map.setCenter(
						lonLat,
						16 - (int) Math.ceil(Math.log10(diagonalDistance)
								/ Math.log10(2)));
			} else {
				if (diagonalDistance < 2)
					map.setCenter(lonLat, 15);
				else if (diagonalDistance < 5)
					map.setCenter(lonLat, 14);
				else if (diagonalDistance < 9)
					map.setCenter(lonLat, 13);
				else
					map.setCenter(lonLat, 12);
			}

		} else if (currentWidget == Variables.GoogleEarth) {
			GEPlugin ge = earthWidget.getGEPlugin();
			KmlLookAt la = ge.createLookAt("");

			if (diagonalDistance != 0 && 1 <= diagonalDistance && diagonalDistance <= 1000) 
			{
				la.set(lonLat.lat(), lonLat.lon(), 0,
						KmlAltitudeMode.ALTITUDE_RELATIVE_TO_GROUND,
						Variables.earthHeading, Variables.earthTilt,
						Variables.earthZoom * diagonalDistance * 1.5);
			}
			else if (diagonalDistance != 0 && 1 <= diagonalDistance && diagonalDistance > 1000) 
			{
				
				la.set(lonLat.lat(), lonLat.lon(), 0,
						KmlAltitudeMode.ALTITUDE_RELATIVE_TO_GROUND,
						Variables.earthHeading, Variables.earthTilt,
						Variables.earthZoom * diagonalDistance * 0.5);
			}
			else 
			{
				la.set(lonLat.lat(), lonLat.lon(), 0,
						KmlAltitudeMode.ALTITUDE_RELATIVE_TO_GROUND,
						Variables.earthHeading, Variables.earthTilt,
						Variables.earthZoom);
			}

			ge.getView().setAbstractView(la);
		} else
			System.out.println("Unknown Widget in centerMapAt");

	}

	public void updateDashboardTextArea(String output, boolean reset) 
	{
		if(reset)
		{
			programScript.setEnabled(true);
			runScript.setEnabled(true);
			clearScript.setEnabled(true);
			
			samples.setEnabled(true);
//			sample1.setEnabled(true);
//			sample2.setEnabled(true);
//			sample3.setEnabled(true);
//			sample4.setEnabled(true);
//			sample5.setEnabled(true);
//			sample6.setEnabled(true);
//			sample7.setEnabled(true);
//			sample8.setEnabled(true);
			
//			runningTextBox.setText("");
			hideLoading();
			
			this.dashboardTextArea.setText(output);
		}
		else
			this.dashboardTextArea.setText(output);
	}

	public void updateInterface(List<String> output) {
		if (output.get(0) != "") 
		{
			updateDashboardTextArea(output.get(0), false);
		}

		// visualize_v2(output.get(1));
		visualize(output.get(1));

	}

	public void visualize_v2(String data) {
		// System.out.println(data);
		hideLoading();
		
		clearLayer();

		Map map = mapWidget.getMap();
		Vector vectorLayer = new Vector("Vector Layer");
		map.addLayer(vectorLayer);

		String[] lines = data.split("\n");

		// allPoints is to get the center and zoom level of the map and earth

		ArrayList<Point> allPoints = new ArrayList<Point>();

		for (int i = 0; i < lines.length; i++) {

			String[] tokens = lines[i].split(",");

			if (tokens[0].equals("POLYLINE")) {

				Point[] points = new Point[tokens.length - 1];

				for (int j = 1; j < tokens.length; j++) {

					String[] geoloc = tokens[j].split(";");

					double lon = Double.valueOf(geoloc[0]);
					double lat = Double.valueOf(geoloc[1]);

					Point p = new Point(lon, lat);
					allPoints.add(new Point(lon, lat));

					p.transform(new Projection("EPSG:4326"),
							new Projection(map.getProjection()));

					points[j - 1] = p;

				}

				LineString lineStr = new LineString(points);
				VectorFeature lineFeature = new VectorFeature(lineStr);
				Style st3 = new Style();
				// st3.setStrokeColor("#2A0A0A");
				st3.setStrokeColor("#8181F7");
				st3.setStrokeWidth(2);
				lineFeature.setStyle(st3);
				vectorLayer.addFeature(lineFeature);

			}
		}

		this.centerMapAt(allPoints);
		
	

		System.out.println("Hello world");
		programScript.setEnabled(true);
		runScript.setEnabled(true);
//		runningTextBox.setText("");
		
		
	}

	// parse visualization instructions and perform them on map
	public void visualize(String data) {

		clearLayer();
		hideLoading();

		String[] lines = data.split("\n");

		// allPoints is to get the center and zoom level of the map and earth

		ArrayList<Point> allPoints = new ArrayList<Point>();

		boolean big = false;
		if(lines.length > 10)
			big = true;
		
		jobDone = false;
		totalJob = lines.length;
		numJob = 0;
		
//		System.out.println(jobDone + " " + numJob + "/" + totalJob);
		
		for (int i = 0; i < lines.length; i++) 
		{

			String[] tokens = lines[i].split(",");

			if (tokens[0].equals("MARKER")) {

				double lon = Double.valueOf(tokens[1]);
				double lat = Double.valueOf(tokens[2]);

				this.addMarkerAt(new LonLat(lon, lat));

				allPoints.add(new Point(lon, lat));

			} else if (tokens[0].equals("MSG")) {

				double lon = Double.valueOf(tokens[2]);
				double lat = Double.valueOf(tokens[3]);

				Point p = new Point(lon, lat);
				this.addMessageAt(p, tokens[1]);

				// LonLat l = new LonLat(lon,lat);
				allPoints.add(new Point(lon, lat));

			} else if (tokens[0].equals("POLYLINE")) {

				List<Point> points = new ArrayList<Point>();

				for (int j = 1; j < tokens.length; j++) {

					String[] geoloc = tokens[j].split(";");

					double lon = Double.valueOf(geoloc[0]);
					double lat = Double.valueOf(geoloc[1]);

					points.add(new Point(lon, lat));
					allPoints.add(new Point(lon, lat));
				}

				// this.addPolyLine(points);
//				System.out.println("1");
				this.addPolyLine(points, big);

			} else if (tokens[0].equals("POLYLINE2")) {

				List<Point> points = new ArrayList<Point>();

				for (int j = 1; j < tokens.length; j++) {

					String[] geoloc = tokens[j].split(";");

					double lon = Double.valueOf(geoloc[0]);
					double lat = Double.valueOf(geoloc[1]);

					points.add(new Point(lon, lat));
					allPoints.add(new Point(lon, lat));
				}

				// this.addPolyLine(points);
//				System.out.println("1");
				this.addPolyLine2(points, big);

			}


			else if (tokens[0].equals("POLYGON")) {

				Point[] points = new Point[tokens.length - 1];
				// List<LonLat> points = new ArrayList<LonLat>();

				for (int j = 1; j < tokens.length; j++) {

					String[] geoloc = tokens[j].split(";");

					double lon = Double.valueOf(geoloc[0]);
					double lat = Double.valueOf(geoloc[1]);

					points[j - 1] = new Point(lon, lat);
					allPoints.add(new Point(lon, lat));
				}

				this.addPolygon(points);

			}
			else
			{
				System.out.println("Unknown Direction");
				numJob++;
			}

		}

		if(allPoints.size() != 0)
			this.centerMapAt(allPoints);
		
		Blocker blk = new Blocker();
		blk.scheduleRepeating(300);
	}
	
	public class Blocker extends Timer
	{
		public Blocker()
		{
			
		}

		@Override
		public void run() 
		{
			if(!jobDone)
			{
				if(numJob == totalJob)
					jobDone = true;
				
//				System.out.println("not done- " + numJob + "/" + totalJob);
			}
			else
			{
//				System.out.println("done- " + numJob + "/" + totalJob);
				programScript.setEnabled(true);
				runScript.setEnabled(true);
				clearScript.setEnabled(true);
				
				samples.setEnabled(true);
//				sample1.setEnabled(true);
//				sample2.setEnabled(true);
//				sample3.setEnabled(true);
//				sample4.setEnabled(true);
//				sample5.setEnabled(true);
//				sample6.setEnabled(true);
//				sample7.setEnabled(true);
//				sample8.setEnabled(true);
				
//				runningTextBox.setText("");
				hideLoading();
				
				this.cancel();
			}
		}
		
		
	}

	public void addMessageAt(Point p, String message) {
//		System.out.println("Messege in " + message);

		if (currentWidget == Variables.OpenMap) {

			Map map = mapWidget.getMap();

			Vector vectorLayer = new Vector("Vector Layer");
			map.addLayer(vectorLayer);

			p.transform(new Projection("EPSG:4326"),
					new Projection(map.getProjection()));

			Style st = new Style();
			st.setLabel(message);
			st.setLabelAlign("lb");
			// st.setFontColor("#0000FF");
			st.setFontColor("#0000ff");
			st.setFillOpacity(1.0);
			st.setFontSize("1.0em");

			// Create a vector feature
			VectorFeature pointFeature = new VectorFeature(p, st);
			// pointFeature.setFeatureId("DEPO_DIGITECH");
			vectorLayer.addFeature(pointFeature);
		} else if (currentWidget == Variables.GoogleEarth) {

			GEPlugin ge = earthWidget.getGEPlugin();

			// Create a placemark
			KmlPlacemark placemark = ge.createPlacemark("");
			placemark.setName(message);
			ge.getFeatures().appendChild(placemark);

			// Create style map for placemark
			KmlIcon icon = ge.createIcon("");
			icon.setHref("http://maps.google.com/mapfiles/kml/paddle/red-circle.png");

			KmlStyle style = ge.createStyle("");

			style.getIconStyle().setIcon(icon);

			// style.getLabelStyle().getColor().set("ff230a8c");
			// style.getLabelStyle().getColor().set("ffff0000");
			style.getLabelStyle().getColor().set("ff0000ff");
			style.getLabelStyle().setScale(0.8f);
			// style.getLabelStyle().setColorMode(colorMode)

			placemark.setStyleSelector(style);

			// place
			KmlLookAt la = ge.createLookAt("");
			la.set(p.getY(), p.getX(), 0,
					KmlAltitudeMode.ALTITUDE_RELATIVE_TO_GROUND, 0, 0, 0);
			KmlPoint point = ge.createPoint("");
			point.setLatitude(la.getLatitude());
			point.setLongitude(la.getLongitude());
			placemark.setGeometry(point);

		} else
			System.out.println("Unknown Widget in centerMapAt");

		numJob++;
	}

	public void setCurrentLabelNull() {
		currentLabel = null;
	}
	
	private void loginGUI()
	{
		loginDialogBox = new DialogBox();
		loginDialogBox.setText("Login");
		loginDialogBox.setAnimationEnabled(true);
		loginDialogBox.setAutoHideEnabled(true);
		
		loginForm = new FormPanel();
		
		loginForm.setAction(getAbsolutePath("/j_spring_security_check"));
		loginForm.setMethod(FormPanel.METHOD_POST);
		
		FlexTable grid = new FlexTable();
//		grid.getFlexCellFormatter().setRowSpan(0, 0, 3);
		
		loginForm.setWidget(grid);
		
		Label idLabel = new Label();
		idLabel.setText("ID:");
		idLabel.setStyleName("loginLabel");
		
		id = new TextBox();
		id.setWidth("120px");
		id.setHeight("10px");
		id.setName("j_username");
		
		Label pwLabel = new Label();
		pwLabel.setText("Password:");
		pwLabel.setStyleName("loginLabel");
		
		pw = new PasswordTextBox();
		pw.setWidth("120px");
		pw.setHeight("10px");
		pw.setName("j_password");
		
		SubmitButton submitButton = new SubmitButton();
		submitButton.setText("Login");
		
		grid.setWidget(0, 0, idLabel);
		grid.setWidget(0, 1, id);
		grid.setWidget(1, 0, pwLabel);
		grid.setWidget(1, 1, pw);
		grid.setWidget(2, 1, submitButton);
		
		loginDialogBox.add(loginForm);
		
		
		loginDialogBox.setPopupPosition(Window.getClientWidth()/2 - 150,
				(int)(Window.getClientHeight() * 0.15));

		loginForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {

	        @Override
	        public void onSubmitComplete(SubmitCompleteEvent e) 
	        {
	        	serviceImpl.checkUserLogin();
//	        	printConsole("check2");
//	        	alert(getAbsolutePath("/GWTMaps.html"));
//	            redirect(getAbsolutePath("/GWTMaps.html"));
	        }
	    });
		

	}
	
	private void signupGUI()
	{
		// Create the popup dialog box
		signupDialogBox = new DialogBox();
		signupDialogBox.setText("Sign Up");
		signupDialogBox.setAnimationEnabled(true);
		signupDialogBox.setAutoHideEnabled(true);

		
		final Grid grid = new Grid(6,2);
		
		final Label idLabel = new Label();
		idLabel.setText("ID:");
		idLabel.setStyleName("loginLabel");
		
		signupId = new TextBox();
		signupId.setWidth("120px");
		signupId.setHeight("10px");
		
		
		final Label mailLabel = new Label();
		mailLabel.setText("Email:");
		mailLabel.setStyleName("loginLabel");
		
		final TextBox email = new TextBox();
		email.setWidth("120px");
		email.setHeight("10px");
		
		
		final Label pwLabel = new Label();
		pwLabel.setText("Password:");
		pwLabel.setStyleName("loginLabel");
		
		final PasswordTextBox pw = new PasswordTextBox();
		pw.setWidth("120px");
		pw.setHeight("10px");
		
		final Label pwLabel2 = new Label();
		pwLabel2.setText("Confirmation:");
		pwLabel2.setStyleName("loginLabel");
		
		final PasswordTextBox pw2 = new PasswordTextBox();
		pw2.setWidth("120px");
		pw2.setHeight("10px");
		
		
		
		
		statusLabel = new Label();
		statusLabel.setWidth("120px");
		statusLabel.setStyleName("statusLabel");
		
		
		final Button okButton = new Button("Register");

		final Button closeButton = new Button("Cancel");
		
		final HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100px");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		
		
		buttonPanel.add(okButton);
		Label empty = new Label();
		
		empty.setWidth("7px");
		buttonPanel.add(empty);
		buttonPanel.add(closeButton);
		
		okButton.addClickHandler(new SignupHandler(this, signupId, email, pw, pw2));
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				signupDialogBox.hide();
			}
		});
		
		
		
		grid.setWidget(0, 0, idLabel);
		grid.setWidget(0, 1, signupId);
		grid.setWidget(1, 0, mailLabel);
		grid.setWidget(1, 1, email);
		grid.setWidget(2, 0, pwLabel);
		grid.setWidget(2, 1, pw);
		grid.setWidget(3, 0, pwLabel2);
		grid.setWidget(3, 1, pw2);
		grid.setWidget(4, 1, statusLabel);
		grid.setWidget(5, 1, buttonPanel);
		grid.setStyleName("signupTable panel");
		
		
		signupDialogBox.add(grid);
		
		
		signupDialogBox.setPopupPosition(Window.getClientWidth()/2 - 150,
				(int)(Window.getClientHeight() * 0.15));
		
		EnterKeyUpHandler fireHandler = new EnterKeyUpHandler(okButton);
		signupId.addKeyUpHandler(fireHandler);
		email.addKeyUpHandler(fireHandler);
		pw.addKeyUpHandler(fireHandler);
		pw2.addKeyUpHandler(fireHandler);
		
	}
	
	private class EnterKeyUpHandler implements KeyUpHandler
	{
	    private Button fireButton;
	    public EnterKeyUpHandler(Button btn)
	    {
	    	this.fireButton = btn;
	    }
	    
	    public void onKeyUp(KeyUpEvent event) 
	    {
		     if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
		     {
//		    	 Window.alert("enter!");
		    	 fireButton.fireEvent( new GwtEvent<ClickHandler>() {
		    	        @Override
		    	        public com.google.gwt.event.shared.GwtEvent.Type<ClickHandler> getAssociatedType() {
		    	        return ClickEvent.getType();
		    	        }
		    	        @Override
		    	        protected void dispatch(ClickHandler handler) {
		    	            handler.onClick(null);
		    	        }
		    	   });
		     }
	    }
	}

	// GUI create methods: Programming scripting area
	private ScrollPanel createProgrammingVPanel() {
		this.programmingVPanel = new VerticalPanel();
		this.programmingVPanel.setWidth("100%");

		VerticalPanel container = new VerticalPanel();
		container.setWidth("100%");
		HTML htmlHeader = new HTML("Program Scripting Area");
		htmlHeader.setWidth("100%");
		htmlHeader.setHeight("40px");
		htmlHeader.setStyleName("gwt-HTML-Title2");
		container.add(htmlHeader);

		this.programmingVPanel.add(container);

		HTML htmlLabel = new HTML("Write your Python script here");
		htmlLabel.setStyleName("gwt-HTML-Title5");

		this.programmingVPanel.add(htmlLabel);

		this.programScript = new TextArea();
		this.programScript.setWidth("80%");
		this.programScript.setHeight("300px");
		this.programScript.setName("code");
		this.programScript.getElement().setId("code");
		// this.programScript

		this.programmingVPanel.add(programScript);

		this.runScript = new Button("Run Script");
		this.runScript.addClickHandler(new runScriptBtnHandler());

		this.clearScript = new Button("Clear");
		this.clearScript.addClickHandler(new clearScriptBtnHandler());
		
		this.saveAsButton = new Button("Save as");
		this.saveAsButton.addClickHandler(new SaveAsButtonHandler(this));
//		this.saveAsButton.addClickHandler(new SignUpButtonHandler(this));
		
		
		this.runningTextBox = new Label("");
		this.runningTextBox.setStyleName("running");

		HorizontalPanel buttons = new HorizontalPanel();
		buttons.add(this.runScript);
		buttons.add(this.clearScript);
		buttons.add(this.saveAsButton);
		buttons.add(this.runningTextBox);
		buttons.setStyleName("buttons");
		this.programmingVPanel.add(buttons);

		HorizontalPanel sampleBox = new HorizontalPanel();

		HorizontalPanel emptyBox = new HorizontalPanel();
		emptyBox.setStyleName("empty");
		emptyBox.add(new HTML("&nbsp;"));

		samples = new ListBox();
		samples.setWidth("200px");

		samples.addItem("Choose a sample script");
		samples.addItem("Sample 1");
		samples.addItem("Sample 2");
		samples.addItem("Sample 3");
		samples.addItem("Sample 4");
		samples.addItem("Sample 5");
		samples.addItem("Sample 6");
		samples.addItem("Sample 7");
		samples.addItem("Sample 8");
		samples.addItem("Sample 9");
		samples.addItem("Sample 10");
		samples.addItem("Sample 11");
		samples.addItem("Sample 12");
		samples.addItem("Sample 13");
        samples.addItem("Sample 14");
        samples.addItem("Sample 15");
		samples.addItem("Sample 16");
//		samples.addItem("Sample Elevation 1");
//		samples.addItem("Sample Elevation 2");
//		samples.addItem("Sample Elevation 3");
		
		samples.addChangeHandler(new ChangeHandler()
	    {

			@Override
			public void onChange(ChangeEvent e) 
			{
				int selectedIndex = samples.getSelectedIndex();
				
				if (selectedIndex == 0)
				{
					programScript.setText("");
				}
				else if (selectedIndex == 1)
				{
					setEditorValue("# Program 1: Hello World"
							+ "\n# This program prints Hello World at a given address."
							+ "\naddress = read_address(\"305 N University St\", \"West Lafayette\", \"IN\",  \"47907\")"
							+ "\ndisplay_message(\"Hello World\", address)");
				} else if (selectedIndex == 2) {
					setEditorValue("# Program 2: Sequential "
							+ "\n# This program moves a Commuter from one location to another showing the route traveled."
							+ "\naddress = read_address(\"1156 Hillcrest Rd\", \"West Lafayette\", \"IN\", \"47906\")"
							+ "\ndisplay_marker(address)"
							+ "\nstart_at(\"com1\", address,\"EAST\")"
							+ "\nmove_until(\"com1\", \"N Grant St\")"
							+ "\nturn_to(\"com1\", \"N Grant St\", \"right\")"
							+ "\nmove_distance(\"com1\", 0.6)"
							+ "\nturn_to(\"com1\", \"W Stadium Ave\", \"right\")"
							+ "\nmove_until(\"com1\", \"Russell St\")"
							+ "\nturn_to(\"com1\", \"Russell St\", \"left\")"
							+ "\nmove_distance(\"com1\", 0.3)"
							+ "\nturn_to(\"com1\", \"3rd St\", \"left\")"
							+ "\nmove_to_next_intersection(\"com1\")"
							+ "\nlast_location = get_current_point(\"com1\")"
							+ "\ndisplay_marker(last_location)"
							+ "\nshow_on_map(\"com1\")");
				} else if (selectedIndex == 3) {

					setEditorValue("# Program 3: conditional if-else "
							+ "\n# Extends Program 2, "
							+ "\n# First, the program moves a Commuter from one location to another showing the route traveled."
							+ "\n# Next, the program computes distance, and prints \"I will bike\" if distance is less than"
							+ "\n#  3 miles, otherwise, the program prints \"I will drive\""
							+ "\naddress = read_address(\"1156 Hillcrest Rd\", \"West Lafayette\", \"IN\", \"47906\")"
							+ "\ndisplay_marker(address)"
							+ "\nstart_at(\"com1\", address,\"EAST\")"
							+ "\nmove_until(\"com1\", \"N Grant St\")"
							+ "\nturn_to(\"com1\", \"N Grant St\", \"right\")"
							+ "\nmove_distance(\"com1\", 0.6)"
							+ "\nturn_to(\"com1\", \"W Stadium Ave\", \"right\")"
							+ "\nmove_until(\"com1\", \"Russell St\")"
							+ "\nturn_to(\"com1\", \"Russell St\", \"left\")"
							+ "\nmove_distance(\"com1\", 0.3)"
							+ "\nturn_to(\"com1\", \"3rd St\", \"left\")"
							+ "\nmove_to_next_intersection(\"com1\")"
							+ "\nlast_location = get_current_point(\"com1\")"
							+ "\ndisplay_marker(last_location)"
							+ "\ndistance = display_distance(\"com1\")"
							+ "\nif distance < 3:"
							+ "\n  print \"I will bike\"" + "\nelse:"
							+ "\n  print \"I will drive\"");

				} else if (selectedIndex == 4) {
					setEditorValue("# Program 4: Sequential"
							+ "\n# This program shows the route the Commuter travels to go from one point back to the same point."
							+ "\naddress = read_address(\"1100 Hillcrest Rd\", \"West Lafayette\", \"IN\", \"47906\")"
							+ "\ndisplay_message(\"START\", address)"
							+ "\nstart_at(\"com1\", address, \"east\")"
							+ "\nmove_until(\"com1\", \"Ravinia Rd\")"
							+ "\nturn_to(\"com1\", \"Ravinia Rd\", \"north\")"
							+ "\nmove_until(\"com1\", \"Woodland Ave\")"
							+ "\nturn_to(\"com1\", \"Woodland Ave\", \"left\")"
							+ "\nmove_distance (\"com1\", 0.09)"
							+ "\nturn_to(\"com1\", \"Elmwood Dr\", \"right\")"
							+ "\nmove_distance(\"com1\", 0.09)"
							+ "\nturn_to(\"com1\", \"Garden St\", \"right\")"
							+ "\nmove_until(\"com1\", \"Windsor Dr\")"
							+ "\nturn_to(\"com1\", \"Windsor Dr\", \"left\")"
							+ "\nmove_until(\"com1\", \"Northwestern Ave\")"
							+ "\nturn_to(\"com1\", \"Northwestern Ave\", \"left\")"
							+ "\nmove_until(\"com1\", \"Oakhurst Dr\")"
							+ "\nturn_to(\"com1\", \"Oakhurst Dr\", \"west\")"
							+ "\nmove_until(\"com1\", \"Western Dr\")"
							+ "\nturn_to(\"com1\", \"Western Dr\", \"left\")"
							+ "\nmove_until(\"com1\", \"Glenway St\")"
							+ "\nturn_to(\"com1\", \"Glenway St\", \"left\")"
							+ "\nmove_until(\"com1\", \"Sheridan Rd\")"
							+ "\nmove_until(\"com1\", \"Sheridan Rd\")"
							+ "\nturn_to(\"com1\", \"Sheridan Rd\", \"right\")"
							+ "\nmove_until(\"com1\", \"Hillcrest Rd\")"
							+ "\nshow_on_map(\"com1\")");
				} else if (selectedIndex == 5) {
					setEditorValue("# Program 5: Sequential "
							+ "\n# This program performs visualization task, identifies number of points of interests,"
							+ "\n# add marker, add message on each location then compute the distance between"
							+ "\n# pairs of points in a selected route"
							+ "\nimport math"
							+ "\n"
							+ "\nhome = read_address(\"Ellen Dr\", \"Indianapolis\", \"IN\", \"46224\")"
							+ "\nspeedway = read_address(\"4790 W 16th St\", \"Indianapolis\", \"IN\",  \"46222\")"
							+ "\nmuseum = read_address(\"3000 N Meridian St,\", \"Indianapolis\", \"IN\",  \"46208\")"
							+ "\nzoo = read_address(\"1200 W Washington St\", \"Indianapolis\", \"IN\",  \"46222\")"
							+ "\npark = read_address(\"7840 W 56th St\", \"Indianapolis\", \"IN\",  \"46278\")"
							+ "\n"
							+ "\nhome_mk = display_marker(home)"
							+ "\nspeedway_mk =display_marker(speedway)"
							+ "\nmuseum_mk = display_marker(museum)"
							+ "\nzoo_mk = display_marker(zoo)"
							+ "\npark_mk = display_marker(park)"
							+ "\n"
							+ "\ndisplay_message(\"Home\", home_mk)"
							+ "\ndisplay_message(\"Motor Speedway\", speedway_mk)"
							+ "\ndisplay_message(\"The Children's Museum\", museum_mk)"
							+ "\ndisplay_message(\"Zoo\", zoo_mk)"
							+ "\ndisplay_message(\"Eagle Creek Park\", park_mk)"
							+ "\n"
							+ "\nprint \"home ---> museum \" + str(round(compute_distance(home, museum_mk),1)) + \"mi\""
							+ "\nprint \"museum ---> zoo \" + str(round(compute_distance(museum_mk, zoo),1)) + \"mi\""
							+ "\nprint \"zoo ---> speedway \" + str(round(compute_distance(zoo,speedway),1)) + \"mi\""
							+ "\nprint \"speedway ---> park \" + str(round(compute_distance(speedway,park_mk),1)) + \"mi\""
							+ "\nprint \"park ---> home \" + str(round(compute_distance(park_mk,home),1)) + \"mi\"");
				} else if (selectedIndex == 6) {
					setEditorValue("# Program 6: while loop and conditional if"
							+ "\n# Similar to Program 5 but instead of writing sequential programs while loop is utilized "
							+ "\n# This program performs visualization task, identifies number of points of interests,"
							+ "\n# add marker, add message on each location then compute the distance between"
							+ "\n# pairs of points in a selected route"
							+ "\nhome = read_address(\"Ellen Dr\", \"Indianapolis\", \"IN\", \"46224\")"
							+ "\n"
							+ "\nPOI_list = [[\"museum\", [\"3000 N Meridian St,\", \"Indianapolis\", \"IN\",  \"46208\"]], "
							+ "\n            [\"zoo\", [\"1200 W Washington St\", \"Indianapolis\", \"IN\",  \"46222\"]], "
							+ "\n            [\"speedway\",[\"4790 W 16th St\", \"Indianapolis\", \"IN\",  \"46222\"]], "
							+ "\n            [\"park\" , [\"7840 W 56th St\", \"Indianapolis\", \"IN\",  \"46278\"]]]"
							+ "\n"
							+ "\nhome_mk = display_marker(home)"
							+ "\ndisplay_message(\"Home\", home_mk)"
							+ "\nmarkers = []"
							+ "\ni = 0"
							+ "\nlength =  len(POI_list)"
							+ "\nwhile i < length:"
							+ "\n  location = read_address(POI_list[i][1][0], POI_list[i][1][1], "
							+ "\n                          POI_list[i][1][2], POI_list[i][1][3])"
							+ "\n  display_marker(location)"
							+ "\n  markers.append(location)"
							+ "\n  display_message(POI_list[i][0],location)"
							+ "\n  if i == 0:"
							+ "\n    print \"Home\" + \" ---> \" + POI_list [i][0]+ \" \" + \\"
							+ "\n            str(round(compute_distance(markers[i], home_mk),1)) + \"mi\""
							+ "\n  else:"
							+ "\n    print POI_list[i-1][0] + \" ---> \" + POI_list [i][0]+ \" \" + \\"
							+ "\n            str(round(compute_distance(markers[i-1], markers[i]),1)) + \"mi\""
							+ "\n  i = i + 1"
							+ "\n"
							+ "\nprint POI_list [length-1][0] + \" ---> \" + \"Home\"+ \" \" + \\"
							+ "\n            str(round(compute_distance(home_mk,markers[length-1]),1)) + \"mi\"");

				} else if (selectedIndex == 7) {
					setEditorValue("# Program 7: while loop and conditional if"
							+ "\n# This program moves  the Commuter 1 mile showing the route taken by the Commuter."
							+ "\naddress = read_address(\"1100 Hillcrest Rd\", \"West Lafayette\", \"IN\", \"47906\")"
							+ "\ndisplay_message(\"Start\", address)"
							+ "\nmy_location = get_location(address)"
							+ "\nprevious_location = my_location "
							+ "\nstart_at(\"com1\", address,\"EAST\")"
							+ "\ndistance= 1"
							+ "\ndirection =[\"SOUTH\",\"WEST\",\"NORTH\",\"EAST\"]"
							+ "\nnext_direction = 0"
							+ "\n"
							+ "\nwhile( distance > 0.001):"
							+ "\n  move_distance(\"com1\", 0.01)"
							+ "\n  current_location = get_current_point(\"com1\")"
							+ "\n  if(current_location == previous_location ):"
							+ "\n    road  = get_road_names(current_location)"
							+ "\n    turn_to(\"com1\", road[0],direction[next_direction])"
							+ "\n    next_direction += 1"
							+ "\n    if(next_direction==4):"
							+ "\n      next_direction=0"
							+ "\n  distance = distance - compute_distance(previous_location , current_location)"
							+ "\n  previous_location = current_location"
							+ "\n"
							+ "\nshow_on_map(\"com1\")"
							+ "\nFinish_location =  get_current_point(\"com1\")"
							+ "\ndisplay_message(\"Finish\",Finish_location)"
							+ "\nprint \"Total distance traveled: \" +str(display_distance(\"com1\"))");
				}
				else if (selectedIndex == 8)
				{
					setEditorValue("# Program 8: get_all (returns geo-points list), for loop"
							+ "\n# This program marks all the states."
							+ "\nall_states = get_all(\"STATE\", \"POLYGON\")"
							+ "\nfor i in range(len(all_states)):"
							+ "\n  display_shape(all_states[i])"
                            + "\nairport_list = get_all_in_range(\"AIRPORT\",all_states)"
                            + "\ncount  = get_count(airport_list)"
                            + "\ndisplay_Count (all_states, count)");
				}
                else if (selectedIndex == 9)
				{
                	setEditorValue("# Program 9: get_all_in_range (returns geo-points list), for loop"
							+ "\n# Similar to Program 8 but instead of getting all States"
							+ "\n# it gets all states within 300 mile radius from a given address"
							+ "\naddress = read_address(\"1100 Hillcrest Rd\", \"West Lafayette\", \"IN\", \"47906\")"
							+ "\nmy_location = get_location(address)"
							+ "\nall_states = get_all_in_range(\"STATE\", my_location, 300)"
							+ "\nfor i in range(len(all_states)):"
							+ "\n display_marker(all_states[i])");
				}
				else if (selectedIndex == 10)
				{
					setEditorValue("# Program 10: get (returns polyline segments list), for loop"
							+ "\n# This program draws the Interstate 65."
							+ "\nInd_pol = get(\"Indiana\", \"STATE\", \"POLYGON\")"
							+ "\ndisplay_shape(Ind_pol)"
							+ "\nInd_cross_flag=0"
							+ "\nIowa_pol = get(\"Iowa\", \"STATE\", \"POLYGON\")"
							+ "\ndisplay_shape(Iowa_pol)"
							+ "\nIowa_cross_flag=0"
							+ "\nI65= get(\"I- 65\", \"PRIMARY-ROAD\", \"POLYLINE\")"
							+ "\nfor i in range(len(I65)):"
							+ "\n  display_shape(I65[i])"
							+ "\n  if(crosses(I65[i],Ind_pol)):"
 							+ "\n     Ind_cross_flag=1"
							+ "\n  if (crosses(I65[i],Iowa_pol)):"
							+ "\n     Iowa_cross_flag=1"
							+ "\nif(Ind_cross_flag==1):"
							+ "\n    print \"I65 crosses Indiana\""
							+ "\nelse:"
							+ "\n    print \"I65 does not cross Indiana\""
							+ "\n    "
							+ "\nif(Iowa_cross_flag==1):"
							+ "\n    print \"I65 crosses Iowa\""
							+ "\nelse:"
							+ "\n    print \"I65 does not cross Iowa\"");
				}
				else if (selectedIndex == 11)
				{
					setEditorValue("# Program 11: get (returns polygon), spatial operators"
							+ "\n# contains and intersects, and conditional if/else."
							+ "\n# This program displays the geometric shapes of cities West Lafayette and Lafayette."
							+ "\nWL_pol = get(\"West Lafayette\", \"CITY\", \"POLYGON\")"
							+ "\ndisplay_shape(WL_pol)"
							+ "\nLaf_pol = get(\"Lafayette\", \"CITY\", \"POLYGON\")"
							+ "\ndisplay_shape(Laf_pol)"
							+ "\nIndiana_pol= get(\"Indiana\", \"STATE\", \"POLYGON\")"
							+ "\n"
							+ "\n# Here it tests if Indiana contains West Lafayette and if Lafayette and West Lafayette intersect"
							+ "\nif(contains(Indiana_pol,WL_pol)):"
							+ "\n  print \"Indiana contains West Lafayette\""
							+ "\nelse:"
							+ "\n  print \"Indiana does not contain West Lafayette\""
							+ "\nif(intersects(Laf_pol,WL_pol)):"
							+ "\n  print \"Lafayette and West Lafayette intersect\""
							+ "\nelse:"
							+ "\n  print \"Lafayette and West Lafayette do not intersect\"");
				}
				else if (selectedIndex == 12)
				{
					setEditorValue("# Program 12: get (returns polygon), spatial operator: touches, and conditional if/else."
							+ "\n# This program displays the geometric shape and the center of Indiana, Ohio and"
							+ "\n#  Iowa States, then tests if Indiana touches Iowa or Ohio."
							+ "\nIndiana_pol= get(\"Indiana\", \"STATE\", \"POLYGON\")"
							+ "\ndisplay_shape(Indiana_pol)"
							+ "\nIndiana_point= get(\"Indiana\", \"STATE\", \"POINT\")"
							+ "\ndisplay_marker(Indiana_point)"
							+ "\nOhio_pol= get(\"Ohio\", \"STATE\", \"POLYGON\")"
							+ "\ndisplay_shape(Ohio_pol)"
							+ "\nOhio_point= get(\"Ohio\", \"STATE\", \"POINT\")"
							+ "\ndisplay_marker(Ohio_point)"
							+ "\nIowa_pol= get(\"Iowa\", \"STATE\", \"POLYGON\")"
							+ "\ndisplay_shape(Iowa_pol)"
							+ "\nIowa_point= get(\"Iowa\", \"STATE\", \"POINT\")"
							+ "\ndisplay_marker(Iowa_point)"
							+ "\n"
							+ "\nif(touches(Indiana_pol, Ohio_pol)):"
							+ "\n  print \"Indiana touches Ohio\""
							+ "\nif(touches(Indiana_pol, Iowa_pol)):"
							+ "\n  print \"Indiana touches Iowa\""
                            + "\nif(disjoint(Indiana_pol, Ohio_pol)):"
							+ "\n  print \"Indiana and Ohio are disjoint\""
							+ "\nif(disjoint(Indiana_pol, Iowa_pol)):"
							+ "\n  print \"Indiana and Iowa are disjoint\"");
				}
				else if (selectedIndex == 13)
				{
					setEditorValue("# Program 13: get_all (return geo_points list), for loop, sort utility"
							+ "\n# This Program finds the 5 nearest airports to home location"
							+ "\naddress = read_address(\"1100 Hillcrest Rd\", \"West Lafayette\", \"IN\", \"47906\")"
							+ "\nmy_location = get_location(address)"
							+ "\nairport_list = get_all(\"AIRPORT\", \"POINT\")"
							+ "\n"
							+ "\nfor i in range(len(airport_list)):"
							+ "\n  airport_loc = (airport_list[i][0], airport_list[i][1])"
							+ "\n  distance = calculate_distance(my_location, airport_loc)"
							+ "\n  airport_list[i].append(distance)"
							+ "\n"
							+ "\nairport_list = sorted(airport_list, key = lambda x : x[3])"
							+ "\n"
							+ "\nfor i in range(5):"
							+ "\n  airport_loc = (airport_list[i][0], airport_list[i][1])"
							+ "\n  airport_name = airport_list[i][2]"
							+ "\n  display_marker(airport_loc)"
							+ "\n  display_message(airport_name, airport_loc)"
							+ "\n  print airport_name , airport_list[i][3]");
				}
				else if (selectedIndex == 14) {
					setEditorValue("# Program 14: get_kNN (return geo_points list), for loop, and conditional if"
							+ "\n# This program displays the nearest 10 airport to an address."
							+ "\naddress = read_address(\"1100 Hillcrest Rd\", \"West Lafayette\", \"IN\", \"47906\")"
							+ "\nmy_location = get_location(address)"
							+ "\nairport_list = get_kNN(\"AIRPORT\",  my_location ,10)"
							+ "\nfor i in range(len(airport_list)):"
							+ "\n  airport_loc = (airport_list[i][0], airport_list[i][1])"
							+ "\n  airport_name = airport_list[i][2]"
							+ "\n  display_marker(airport_loc)"
							+ "\n  display_message(airport_name, airport_loc)"
							+ "\n  print airport_name , airport_list[i][3]"
							+ "\n"
							+ "\n# Here it outputs the number of airports within 10 miles of an address"
							+ "\ndist=10"
							+ "\nairport_list = get_all_in_range(\"AIRPORT\", my_location ,dist)"
							+ "\nif(airport_list is not \"NULL\") :"
							+ "\n  print \"Number of airports in 10 mile radius is \" + str(len(airport_list))"
							+ "\n  print \"Density is \" + str(len(airport_list) / get_area(dist)) + \" airport per mile\"");

				}else if (selectedIndex == 15) {
					setEditorValue("# Program 15: Density"
							+ "\n# This program calculates the density of schools in an 10 mile area around  1100 Hillcrest Rd West, The program also calculates density of schools in Indiana STATE"
							+ "\naddress = read_address(\"1100 Hillcrest Rd\", \"West Indiana\", \"IN\", \"47906\")"
							+ "\nmy_location = get_location(address)"
							+ "\ndist=10"
							+ "\nschool_list = get_all_in_range(\"school\", my_location ,dist)"
							+ "\nprint \"Density of schools within 10 miles of 1100 Hillcrest Rd West Indiana is \" + str(len(school_list) / get_area(dist))"
							+ "\n"
							+ "\nInd_pol = get(\"Indiana\", \"STATE\", \"POLYGON\")"
							+ "\ndisplay_shape(Ind_pol)"
							+ "\nschool_list = get_all_in_range(\"school\",Ind_pol)"
							+ "\nif(school_list is not \"NULL\") :"
							+ "\n  number_of_school = len(school_list)"
							+ "\n  area_of_Ind_in_m = get_area(\"Indiana\", \"STATE\")"
							+ "\n  area_of_Ind_in_km = get_area(\"Indiana\", \"STATE\")/1000000"
							+ "\n  print \"Number of schools in Indiana State\" + str(number_of_school)"
							+ "\n  print \"Area of Indiana State in meters square \" + str(area_of_Ind_in_m)"
							+ "\n  print \"Area of Indiana State in KM square \" + str(area_of_Ind_in_km) "
							+ "\n  print \"Density of schools in Indiana State\" + str( number_of_school/ area_of_Ind_in_km)"
							+ "\n  for i in range(len(school_list)):"
							+ "\n    school_loc = (school_list[i][0], school_list[i][1])"
							+ "\n    school_name = school_list[i][2]"
							+ "\n    display_marker(school_loc)"
							+ "\n    display_message(school_name, school_loc)");

				} else if (selectedIndex == 16) {
					setEditorValue("# Program 16: Count"
							+ "\n# This program calculates the count of each polygon in a list of polygons"
							+ "\nIndiana_cities = []"
							+ "\nWL_pol = get(\"West Lafayette\", \"CITY\", \"POLYGON\")"
							+ "\nIndiana_cities.append(WL_pol)"
							+ "\nLaf_pol = get(\"Lafayette\", \"CITY\", \"POLYGON\")"
							+ "\nIndiana_cities.append(Laf_pol)"
							+ "\nIndianapolis_pol = get(\"Indianapolis\", \"CITY\", \"POLYGON\")"
							+ "\nIndiana_cities.append(Indianapolis_pol)"
                            + "\nEV_pol = get(\"Evansville\", \"CITY\", \"POLYGON\")"
							+ "\nIndiana_cities.append(EV_pol)"
							+ "\nairport_list = get_all_in_range(\"AIRPORT\",Indiana_cities)"
							+ "\ncount  = get_count(airport_list)"
							+ "\ndisplay_Count (Indiana_cities, count)");

				} 
//				else if (selectedIndex == 17) {
//					setEditorValue("# Program Elevation 1: Prints elevation at given point"
//							+ "\nvar = elevation_at_point(-86.6854935, 40.3124275)"
//							+ "\nprint var");
//
//				}  else if (selectedIndex == 18) {
//					setEditorValue("# Program Elevation 2: Prints elevation at given address"
//							+ "\naddress = read_address(\"305 N University St\", \"West Lafayette\", \"IN\",  \"47906\")"
//							+ "\ndisplay_message(\"Lawson\", address)" 
//							+ "\n\nvar = elevation_at_address(address)"
//							+ "\nprint var");
//
//				} else if (selectedIndex == 19) {
//					setEditorValue("# Program Elevation 3: Largest Slope "
//							+ "\n# This program, similar to Program 2, will move a Commuter from one location to another showing the route traveled."
//							+ "\n# However, this time it will display the largest slope on the trip."
//							+ "\naddress = read_address(\"1156 Hillcrest Rd\", \"West Lafayette\", \"IN\", \"47906\")"
//							+ "\ndisplay_marker(address)"
//							+ "\nstart_at(\"com1\", address,\"EAST\")"
//							+ "\nmove_until(\"com1\", \"N Grant St\")"
//							+ "\nturn_to(\"com1\", \"N Grant St\", \"right\")"
//							+ "\nmove_distance(\"com1\", 0.6)"
//							+ "\nturn_to(\"com1\", \"W Stadium Ave\", \"right\")"
//							+ "\nmove_until(\"com1\", \"Russell St\")"
//							+ "\nturn_to(\"com1\", \"Russell St\", \"left\")"
//							+ "\nmove_distance(\"com1\", 0.3)"
//							+ "\nturn_to(\"com1\", \"3rd St\", \"left\")"
//							+ "\nmove_to_next_intersection(\"com1\")"
//							+ "\nlast_location = get_current_point(\"com1\")"
//							+ "\ndisplay_marker(last_location)"
//							+ "\nshow_on_map(\"com1\")"
//							+ "\nvar = find_largest_slope(\"com1\")" 
//							+ "\ndisplay_message(\"slope = \" + str(var[0]), var[1])"
//							+ "\nprint var");
//				}
			}
	    
	    });
		

		sampleBox.add(emptyBox);
		sampleBox.add(samples);

		this.programmingVPanel.add(sampleBox);
		samples.setStyleName("samplebuttons");

		HTML htmlOutput = new HTML("Program Output");
		htmlOutput.setStyleName("gwt-HTML-Title5");
		this.programmingVPanel.add(htmlOutput);

		this.dashboardTextArea = new TextArea();
		// this.dashboardTextArea.setStyleName("gwt-TextArea-readonly");
		this.dashboardTextArea.setWidth("95%");
		this.dashboardTextArea.setHeight("100px");
		this.dashboardTextArea.setReadOnly(true);
		

		this.programmingVPanel.add(this.dashboardTextArea);
		
		
		
		HorizontalPanel hPanel = new HorizontalPanel();
	    hPanel.setSpacing(20);
	    hPanel.setWidth("500px");
	    
	    multiBox = new ListBox(true);
	    
	    multiBox.ensureDebugId("cwListBox-multiBox");
	    multiBox.setStyleName("instructionBox");
	    multiBox.setWidth("150px");
	    multiBox.setVisibleItemCount(15);
	    multiBox.setMultipleSelect(false);
	    
	    multiBox.addItem("LIMO Functions");
	    
	    multiBox.addItem("read_address");
	    multiBox.addItem("start_at");
	  //  multiBox.addItem("orient_to");
	    
	    multiBox.addItem("move_distance");
	    multiBox.addItem("move_until");
	    multiBox.addItem("move_to_next_intersection");
	    
	    multiBox.addItem("turn_to");
	    multiBox.addItem("display_message");
	    multiBox.addItem("display_marker");
	    multiBox.addItem("display_shape");
//	    multiBox.addItem("display_[distance|time]");
	    
	    
	    multiBox.addItem("draw_line");
	    multiBox.addItem("show_on_map");
	    
	    multiBox.addItem("display_distance");
	    multiBox.addItem("compute_distance");
	    
	    multiBox.addItem("get_current_point");
	    multiBox.addItem("get_location");
	    multiBox.addItem("get");
	    multiBox.addItem("get_all");
	    multiBox.addItem("get_kNN");
	    multiBox.addItem("get_all_in_range");
	    
	    
	    multiBox.addItem("overlaps");
	    multiBox.addItem("touches");
	    multiBox.addItem("intersects");
	    multiBox.addItem("contains");
	    
	    
	    multiBox.addItem("get_road_names");
	    multiBox.addItem("get_road_names_in");
	    
	    // adding the uses for Elevation functions
	    multiBox.addItem("elevation_at_point");
	    multiBox.addItem("elevation_at_address");
	    multiBox.addItem("find_largest_slope");
	    
	    multiBox.addChangeHandler(new ChangeHandler()
	    {

			@Override
			public void onChange(ChangeEvent e) 
			{
				int selectedIndex = multiBox.getSelectedIndex();
				
				if (selectedIndex == 0)
					instructionTextArea.setText("");
				else if (selectedIndex == 1)
					instructionTextArea.setText("read_address(street, city, state, zipcode)"
							+ "\n\nReturns the specified address.");
				else if (selectedIndex == 2)
					instructionTextArea.setText("start_at(commuterName, address, direction)"
							+ "\n\nSets the commuter’s start location to the given address and directs the commuter toward a certain direction.");
//				else if (selectedIndex == 3)
//					instructionTextArea.setText("orient_to(commuterName, direction)"
//							+ "\n\nDirect Commuter towards a certain direction, e.g., East, West, North, or South.");
				else if (selectedIndex == 3)
					instructionTextArea.setText("move_distance(commuterName, distance)"
							+ "\n\nMoves the commuter with the given name for a specified amount of distance.");
				else if (selectedIndex == 4)
					instructionTextArea.setText("move_until(commuterName, street)"
							+ "\n\nMoves the commuter until a specified street.");
				else if (selectedIndex == 5)
					instructionTextArea.setText("move_to_next_intersection(commuterName)"
							+ "\n\nMoves the commuter until the next intersection in his/her path.");
				
				else if (selectedIndex == 6)
					instructionTextArea.setText("turn_to(commuterName, roadName, [direction|None])"
							+ "\n\nRe-orients the commuter toward a new direction, e.g., right or left when direction is not empty or toward a road when no direction is given.");
				else if (selectedIndex == 7)
					instructionTextArea.setText("display_message(message, address|location)"
							+ "\n\nPlaces a text message at the given address or geo-location.");
				else if (selectedIndex == 8)
					instructionTextArea.setText("display_marker(address|location)"
							+ "\n\nPlaces a marker on the map at the given address or geo-location.");
				else if (selectedIndex == 9)
					instructionTextArea.setText("display_shape(geometric shape)"
							+ "\n\nDisplays the specified geometric shape (e.g., lake boundary, state, etc.) on the map.");
				else if (selectedIndex == 10)
					instructionTextArea.setText("draw_line(location1, location2)"
							+ "\n\nDraws a straight line from location1 to location2.");
				else if (selectedIndex == 11)
					instructionTextArea.setText("show_on_map(commuterName)"
							+ "\n\nDraws the route traveled by the commuter so far.");
				else if (selectedIndex == 12)
					instructionTextArea.setText("display_distance(commuter)"
							+ "\n\nDisplays the total distance commuted so far.");
				else if (selectedIndex == 13)
					instructionTextArea.setText("compute_distance(address1|location1, address2|location2)"
							+ "\n\nReturns the distance between two addresses or between geo-locations.");
				else if (selectedIndex == 14)
					instructionTextArea.setText("get_current_point(commuterName)"
							+ "\n\nReturns the current geo-coordinate of the commuter with the given name.");
				else if (selectedIndex == 15)
					instructionTextArea.setText("get_location(address)"
							+ "\n\nReturns the current geo-coordinate of the given address.");
				else if (selectedIndex == 16)
					instructionTextArea.setText("get(name, description, geometric shape)"
							+ "\n\nReturns the location (as geometric shape) of the place that matches the given name and description.");
				else if (selectedIndex == 17)
					instructionTextArea.setText("get_all(description, geometric shape)"
							+ "\n\nReturns a list of locations (as geometric shape) for the points of interest specified by the description (e.g. AIRPORT, STATE).");
				else if (selectedIndex == 18)
					instructionTextArea.setText("get_kNN(description, location, k)"
							+ "\n\nReturns a list of k nearest neighbours for a given geo-location that match the description (e.g., AIRPORT, STATE). Each object in the list has a geo-coordinate, full name and distance from the geo-location.");
				else if (selectedIndex == 19)
					instructionTextArea.setText("get_all_in_range(description, location, range)"
							+ "\n\nReturns a list of all points of interest that match the description (e.g., AIRPORT, STATE) that are within the range from a specified geo-location. Each object in the list has a geo-coordinate and full name.");
				else if (selectedIndex == 20)
					instructionTextArea.setText("overlaps(shape1, shape2)"
							+ "\n\nBoolean operator that tests whether two shapes overlap one another.");
				else if (selectedIndex == 21)
					instructionTextArea.setText("touches(shape1, shape2)"
							+ "\n\nBoolean operator that tests whether two shapes touch one another.");
				else if (selectedIndex == 22)
					instructionTextArea.setText("intersects(shape1, shape2)"
							+ "\n\nBoolean operator that tests whether two shapes intersect one another.");
				else if (selectedIndex == 23)
					instructionTextArea.setText("contains(shape1, shape2)"
							+ "\n\nBoolean operator that tests whether two shapes contain one another.");
				else if (selectedIndex == 24)
					instructionTextArea.setText("get_road_names(location)"
							+ "\n\nReturns all road names near the specified location.");
				else if (selectedIndex == 25)
					instructionTextArea.setText("get_road_names_in(location1, location2, location3, location4)"
							+ "\n\nReturns all road names within the rectangle formed by the four given locations.");
				else if (selectedIndex == 26)
					instructionTextArea.setText("elevation_at_point(longitude, latitude)"
							+ "\n\nReturns elevation at the point. Parameters are in decimal degrees.");
				else if (selectedIndex == 27)
					instructionTextArea.setText("elevation_at_address(address)"
							+ "\n\nReturns elevation at the address.");
				else if (selectedIndex == 28)
					instructionTextArea.setText("find_largest_slope(commuterName)"
							+ "\n\nReturns the largest slope of the commuter's trip.");
				
					
			}
		});
	    
	    hPanel.add(multiBox);
	    
		this.instructionTextArea = new TextArea();
		this.instructionTextArea.setWidth("300px");
		this.instructionTextArea.setHeight("220px");
		this.instructionTextArea.setReadOnly(true);
		this.instructionTextArea.setStyleName("instructionBox");
		
		hPanel.add(this.instructionTextArea);
		
		
		this.programmingVPanel.add(hPanel);
		
		
		ScrollPanel scroll = new ScrollPanel(this.programmingVPanel);
		scroll.setSize("100%", "100%");
		
		return scroll;

//		return this.programmingVPanel;
	}
	
	

	// GUI create methods: Title area
	private HorizontalPanel createTitleHPanel() {
		this.titleHPanel = new HorizontalPanel();
		this.titleHPanel.setSize("100%", "100%");
		this.titleHPanel.setSpacing(5);
		this.titleHPanel.setStyleName("gwt-HorizontalPanel");
		this.titleHPanel
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		Image maplogo = new Image(getAbsolutePath("/images/maplogo.png"));

		titleHTML = new HTML("LIMO");

		titleHTML.setStyleName("gwt-HTML-Title");
		titleHTML.setHeight("50px");

		HorizontalPanel childTitle = new HorizontalPanel();
		childTitle.add(maplogo);
		childTitle.add(titleHTML);

		this.titleHPanel.add(childTitle);
		
//		printConsole("refresh");
		
		this.titleContents = new HorizontalPanel();
		this.titleContents.setSize("500px", "30px");
		this.titleContents.setStyleName("titleLayoutPanel");
		this.titleContents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		this.titleContents.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
//		this.titleContents.set
		this.titleHPanel.add(titleContents);
		
	
		// After LogIN
		
		serviceImpl.checkUserLogin();
			
		return this.titleHPanel;

	}
	
	public void loginCheck(String result)
	{
		if(this.titleContents.getWidgetCount() >= 1)
		{
			printConsole("remove entered");

			while(this.titleContents.getWidgetCount() >= 1)
			{
				this.titleContents.remove(0);
			}
		}
		printConsole("check!!!!!");
		printConsole(result);
		
		this.loginDialogBox.hide();
//		this.loginID = result;
		
		if(result != "anonymousUser")
		{
			printConsole("Logined: " + result);
			
			printConsole("" + this.titleContents.getWidgetCount());
			
			Label idLabel = new Label();
			idLabel.setText("Hello, " + result);
			idLabel.getElement().getStyle().setFontSize(14, Unit.PX);
			idLabel.setWidth("100px");

			this.username = result;
			this.programComboBox = new ListBox();
			this.programComboBox.setWidth("200px");
			this.programComboBox.setHeight("30px");
			
			this.programComboBox.addItem("File List..");
			this.programComboBox.addChangeHandler(new LoadFileButtonHandler(this));
			
//			this.loadFileButton = new Button("Load");
//			this.loadFileButton.addClickHandler(new LoadFileButtonHandler(this));
//			this.loadFileButton.setHeight("30px");
			
			Label emptyLabel = new Label();
			emptyLabel.setWidth("100px");
			emptyLabel.setHeight("30px");
			
			Anchor logoutLink = new Anchor("[ Logout ]");
			logoutLink.addClickHandler(new ClickHandler() {
			  @Override
			  public void onClick(ClickEvent event) {
			    Window.Location.assign("j_spring_security_logout");
			  }
			});
			
			this.titleContents.add(logoutLink);
			
			
			this.titleContents.add(idLabel);
			this.titleContents.add(programComboBox);
//			this.titleContents.add(loadFileButton);
//			this.titleContents.add(emptyLabel);
			this.titleContents.add(logoutLink);
			this.callLoadProgramList();
			
			if(tryToSave == true)
			{
				tryToSave = false;
				saveAsButton.click();
			}
			printConsole("done");
		}
		else
		{
			printConsole("Not Logined");
			
			printConsole("" + this.titleContents.getWidgetCount());
			
			this.username = "";
			
			Anchor loginLink = new Anchor("Login");
			loginLink.addClickHandler(new LoginButtonHandler(this));
			
			Anchor signupLink = new Anchor("Sign up");
			signupLink.addClickHandler(new SignUpButtonHandler(this));
			
//			loginLink.addClickHandler(new ClickHandler() {
//			  @Override
//			  public void onClick(ClickEvent event) {
//				
//			    Window.Location.assign(getAbsolutePath("/Login.html"));
//			  }
//			});
			
			this.titleContents.add(loginLink);
			this.titleContents.add(signupLink);
			
		}
	}
	
	public void loadProgramList(List<String> lists)
	{
		printConsole("loadProgramlist here");
//		printConsole("msg:");
		this.programComboBox.clear();
		this.programComboBox.addItem("File List..");
		
		int index = 0;
		int i = 0;
		
		for(String s: lists)
		{
			i++;
			this.programComboBox.addItem(s);
			
			if( s == currentProgramName)
				index = i;
		}
		
		if(index != 0)
			this.programComboBox.setSelectedIndex(index);
		
	}
	
	
	// GUI create methods: Map area
	private SplitLayoutPanel createMapPanel() {

		// mapEarthPanel contains map and earth panel.
		// this contains several widgets but just show one widget.
		this.mapEarthPanel = new DeckLayoutPanel();
		this.mapEarthPanel.setSize("100%", "100%");

		// for map
		MapOptions defaultMapOptions = new MapOptions();
		this.mapWidget = new MapWidget("100%", "100%", defaultMapOptions);

		OSM osm_2 = OSM.Mapnik("Mapnik");
		OSM osm_3 = OSM.CycleMap("CycleMap");
		osm_2.setIsBaseLayer(true);
		osm_3.setIsBaseLayer(true);
		Map map = mapWidget.getMap();
		map.addLayer(osm_2);
		map.addControl(new LayerSwitcher());
		map.addControl(new MousePosition());
		LonLat lonLat = new LonLat(-86.9143287383871, 40.4239744418587);
		lonLat.transform("EPSG:4326", "EPSG:900913");
		map.setCenter(lonLat, 16);

		this.mapSplitLP = new SplitLayoutPanel();
		this.mapSplitLP.setSize("100%", "100%");

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSize("100%", "100%");
		hPanel.setStyleName("gwt-HorizontalPanel2");
		this.mapSplitLP.addNorth(hPanel, 40);

		// Set of map and earth buttons. *Down is colored(focused) button
		this.switchButtons = new HorizontalPanel();
		this.switchButtons
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);

		this.mapButton = new PushButton(new Image(
				getAbsolutePath("/images/switchMap.png")), new Image(
				getAbsolutePath("/images/switchMapDown.png")));
		this.mapButton.addClickHandler(new SwitchToOpenMapHandler(this));

		this.mapButtonDown = new PushButton(new Image(
				getAbsolutePath("/images/switchMapDown.png")), new Image(
				getAbsolutePath("/images/switchMap.png")));
		this.mapButtonDown.addClickHandler(new SwitchToOpenMapHandler(this));

		this.earthButton = new PushButton(new Image(
				getAbsolutePath("/images/switchEarth.png")), new Image(
				getAbsolutePath("/images/switchEarthDown.png")));
		this.earthButton.addClickHandler(new SwitchToGoogleEarthHandler(this));

		this.earthButtonDown = new PushButton(new Image(
				getAbsolutePath("/images/switchEarthDown.png")), new Image(
				getAbsolutePath("/images/switchEarth.png")));
		this.earthButtonDown.addClickHandler(new SwitchToGoogleEarthHandler(
				this));

		this.switchButtons.add(mapButtonDown);
		this.switchButtons.add(earthButton);

		hPanel.add(switchButtons);
		hPanel.setCellHorizontalAlignment(switchButtons,
				HasHorizontalAlignment.ALIGN_LEFT);

		HorizontalPanel commands = new HorizontalPanel();
		commands.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

//		PushButton run = new PushButton(new Image(
//				getAbsolutePath("/images/run.png")));
//		run.addClickHandler(new runBtnHandler());

//		PushButton delete = new PushButton(new Image(
//				getAbsolutePath("/images/delete.png")));
//		delete.addClickHandler(new deleteBtnHandler());

//		commands.add(run);
//		commands.add(delete);

		hPanel.add(commands);
		hPanel.setCellHorizontalAlignment(commands,
				HasHorizontalAlignment.ALIGN_RIGHT);

		this.mapEarthPanel.add(mapWidget);
		this.mapSplitLP.add(mapEarthPanel);
		this.mapEarthPanel.showWidget(Variables.OpenMap);
		this.currentWidget = Variables.OpenMap;

		return mapSplitLP;
	}

	// GUI create methods: Menubar
	private MenuBar createMenu() {
		this.menu = new MenuBar();
		this.menu.setSize("16%", "30px");
		this.menu.addStyleName("gwt-MenuBar");

		Command cmd = new Command() {
			public void execute() {

				LoadNetworkDialogBox loadBox = new LoadNetworkDialogBox(
						MainGUI.this);
				loadBox.center();
				loadBox.show();

				// Window.alert("You selected a menu item!");
			}
		};

		Command cmd2 = new Command() {
			public void execute() {

				clearLayer();
			}
		};
		
		Command cmd3 = new Command() {
			public void execute() {

				printConsole(getEditorValue());
			}
		};

		MenuBar mapMenue = new MenuBar(true);
		mapMenue.addItem("clear", cmd2);
		mapMenue.addItem("load network", cmd);

		MenuBar fileMenu = new MenuBar(true);
		fileMenu.addItem("Save", cmd);

		MenuBar editMenu = new MenuBar(true);
		editMenu.addItem("item1", cmd);

		MenuBar tutorialMenu = new MenuBar(true);
		tutorialMenu.addItem("item1", cmd);

		MenuBar helpMenu = new MenuBar(true);
		helpMenu.addItem("About", cmd);
		helpMenu.addItem("Tutorial", cmd3);

		// Make a new menu bar, adding a few cascading menus to it.
//		this.menu.addItem("Map", mapMenue);
//		this.menu.addItem("Help", helpMenu);

		return menu;
	}

	private class runScriptBtnHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			String script = getEditorValue();
			serviceImpl.runScript(script);
			
			programScript.setEnabled(false);
			runScript.setEnabled(false);
			clearScript.setEnabled(false);
			dashboardTextArea.setText("");
			
			samples.setEnabled(false);
//			sample1.setEnabled(false);
//			sample2.setEnabled(false);
//			sample3.setEnabled(false);
//			sample4.setEnabled(false);
//			sample5.setEnabled(false);
//			sample6.setEnabled(false);
//			sample7.setEnabled(false);
//			sample8.setEnabled(false);

//			runningTextBox.setText("Now Running...");
			showLoading();
			
		}

	}


	
	public void callLoadProgramList()
	{
		this.serviceImpl.loadProgramList(this.username);
	}

	private class clearScriptBtnHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			programScript.setText("");

		}

	}

	private class deleteBtnHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {

			if (nodesFilename == "" || edgesFilename == "" || delim == "")
				Window.alert("Select a road network");
			else
				serviceImpl
						.readRoadNetwork(nodesFilename, edgesFilename, delim);

		}
	}
	
//	class BeeperControl 
//	{
//	    private final ScheduledExecutorService scheduler =
//	       Executors.newScheduledThreadPool(1);
//
//	    public void beepForAnHour() 
//	    {
//	        final Runnable beeper = new Runnable() 
//	        {
//                public void run() { System.out.println("beep"); }
//	        };
//	        
//	        final ScheduledFuture<?> beeperHandle =
//	        					scheduler.scheduleAtFixedRate(beeper, 10, 10, TimeUnit.SECONDS);
//	        
//	        scheduler.schedule(new Runnable() 
//			{
//	        	public void run() { beeperHandle.cancel(true); }
//	        }
//	        , 5 * 5, TimeUnit.SECONDS);
//	    }
//	 }

	private class runBtnHandler implements ClickHandler {

		public void onClick(ClickEvent event) {

//			testJS("Hello Jscript in GWT from " + GWT.getModuleName());

			// if (!animationRunning) {
			// if (currentWidget == Variables.GoogleEarth) {
			// GEPlugin ge = earthWidget.getGEPlugin();
			// ge.getOptions().setFlyToSpeed(Variables.flyingSpeed);
			// animationRunning = true;
			// frameEndRegistration = ge
			// .addFrameEndListener(new FrameEndListener() {
			// @Override
			// public void onFrameEnd() {
			// tickAnimation();
			// }
			//
			// });
			// // start it off
			// tickAnimation();
			// }
			// else
			// System.out.println("Add other handler");
			// }
		}

	}

	// private void tickAnimation()
	// {
	// // an example of some camera manipulation that's possible w/ the Earth
	// // API
	// GEPlugin ge = earthWidget.getGEPlugin();
	// KmlCamera camera =
	// ge.getView().copyAsCamera(KmlAltitudeMode.ALTITUDE_RELATIVE_TO_GROUND);
	// double[] dest = destination(
	// camera.getLatitude(),
	// camera.getLongitude(),
	// 10,
	// camera.getHeading());
	//
	// camera.setAltitude(Variables.animationAltitude);
	// camera.setLatitude(dest[0]);
	// camera.setLongitude(dest[1]);
	//
	// ge.getView().setAbstractView(camera);
	// }
	//
	// private double[] destination(double lat, double lng, double dist, double
	// heading) {
	// lat *= Math.PI / 180;
	// lng *= Math.PI / 180;
	// heading *= Math.PI / 180;
	// dist /= 6371000; // angular dist
	//
	// double lat2 = Math.asin(Math.sin(lat)
	// * Math.cos(dist) + Math.cos(lat)
	// * Math.sin(dist)
	// * Math.cos(heading));
	//
	// return new double[] {
	// 180 / Math.PI * lat2,
	// 180 / Math.PI * (lng + Math.atan2(Math.sin(heading) * Math.sin(dist) *
	// Math.cos(lat2),
	// Math.cos(dist) - Math.sin(lat) * Math.sin(lat2)))
	// };
	// }

	/**
	 * @param str
	 *            - relative path of file (png, ...)
	 * @return String, full absolute path to the file.
	 * 
	 *         - Sub-function - This is method for making absolute path for
	 *         image files - Port number 8080 means http client-server
	 *         connection. - Running in eclipse, app can access files without
	 *         absolute path)
	 */
	public String getAbsolutePath(String str) {
		String url = GWT.getHostPageBaseURL();
		url = url.substring(0, url.length() - 1);

		if (Location.getPort().equals("8181")) {
			return url + str;
		} else {
			return str;
		}

	}

	/**
	 * @return boolean value whether the earth widget is null or not
	 * 
	 *         - Sub-function to check existence of earth widget
	 */
	public boolean isEarthWidgetNull() {
		if (earthWidget == null)
			return true;
		else
			return false;
	}

	/**
	 * @return GoogleEarthWidget
	 * 
	 *         - getter(), returns google earth widget
	 */
	public GoogleEarthWidget getEarthWidget() {
		return earthWidget;
	}

	/**
	 * @param w
	 *            - GoogleEarthWidget for set new widget
	 * 
	 *            - Set new google earth widget. - This function is called only
	 *            once at the begging of earth loading.
	 */
	public void setEarthWidget(GoogleEarthWidget w) {
		this.earthWidget = w;
	}

	/**
	 * @param w
	 *            GoogleEarthWidget
	 * 
	 *            - setter(), set map-earth panel to the google earth widget. -
	 *            This function is called only once at the begging of earth
	 *            loading. - To change panel, use switchMapPanel()
	 * 
	 */
	public void insertGoogleEarthToPanel(Widget w) {
		this.mapEarthPanel.add(w);
		this.mapEarthPanel.showWidget(w);
		this.currentWidget = Variables.GoogleEarth;

	}

	/**
	 * @param w
	 *            - integer value for switch map-earth panel.
	 * 
	 *            - Function for switching map-earth panel. - Variables.OpenMap
	 *            means OpenMap - Variables.GoogleEarth means GoogleEarth
	 * 
	 */
	public void switchMapPanel(int w) {
		if (w == Variables.OpenMap) {
			mapEarthPanel.showWidget(Variables.OpenMap);
			currentWidget = Variables.OpenMap;
		} else if (w == Variables.GoogleEarth) {
			mapEarthPanel.showWidget(Variables.GoogleEarth);
			currentWidget = Variables.GoogleEarth;
		} else {
			System.out.println("Unknown Widget in switchMapPanel");
		}
	}

	/**
	 * @param w
	 *            - integer value for switch button color.
	 * 
	 *            - Function for changing button color.
	 */
	public void switchMapButton(int w) {
		if (w == Variables.OpenMap) {
			switchButtons.clear();
			switchButtons.add(mapButtonDown);
			switchButtons.add(earthButton);
		} else if (w == Variables.GoogleEarth) {
			switchButtons.clear();
			switchButtons.add(mapButton);
			switchButtons.add(earthButtonDown);
		} else {
			System.out.println("Unknown Widget in switchMapButton");
		}
	}

	/**
	 * Clear the map or earth's layer for re-Drawing
	 */
	public void clearLayer() {
		if (currentWidget == Variables.OpenMap) 
		{
			Map map = mapWidget.getMap();
			map.removeOverlayLayers();
		} 
		else if (currentWidget == Variables.GoogleEarth) 
		{
			GEPlugin ge = earthWidget.getGEPlugin();
			KmlObjectList l = ge.getFeatures().getChildNodes();

			int size = l.getLength();
			for (int i = 0; i < size; i++) {
				ge.getFeatures().removeChild(l.item(i));
			}
		} 
		else
			System.out.println("Unknow Widget in clearMapLayer");
	}

	/**
	 * @param fromLat
	 * @param fromLon
	 * @param toLat
	 * @param toLon
	 * @param distance
	 *            : mile
	 * @return ArrayList<LonLat> : get all points from the source to destination
	 * 
	 *         returns all points from source to destination points
	 */
	public ArrayList<LonLat> getChunkPoints(double fromLat, double fromLon,
			double toLat, double toLon, int numChunk) {
		ArrayList<LonLat> result = new ArrayList<LonLat>();

		double d = distFrom(fromLat, fromLon, toLat, toLon);
		d /= numChunk;

		double bearing = getBearing(fromLat, fromLon, toLat, toLon);

		result.add(new LonLat(fromLon, fromLat));

		for (int i = 1; i < numChunk; i++) {
			LonLat l = nextPoint(fromLat, fromLon, bearing, d * i);
			result.add(l);
		}
		result.add(new LonLat(toLon, toLat));

		return result;
	}

	/**
	 * @param fromLat
	 * @param fromLon
	 * @param toLat
	 * @param toLon
	 * @param distance
	 *            : mile
	 * @return ArrayList<LonLat> : get all points from the source to destination
	 * 
	 *         returns all points from source to destination points
	 */

	public ArrayList<LonLat> getChunkPointsOSM(double fromLat, double fromLon,
			double toLat, double toLon, int numChunk) {
		ArrayList<LonLat> result = new ArrayList<LonLat>();

		double d = distFrom(fromLat, fromLon, toLat, toLon);

		d /= numChunk;

		double bearing = getBearing(fromLat, fromLon, toLat, toLon);

		result.add(new LonLat(fromLon, fromLat));

		int i;
		for (i = 1; i < numChunk - 1; i++) {
			LonLat l = nextPoint(fromLat, fromLon, bearing, d * i);
			result.add(l);
		}
		result.add(new LonLat(toLon, toLat));

		return result;
	}

	/**
	 * @param latitude
	 * @param longitude
	 * @param bearing
	 *            - degree for the next point
	 * @param distance
	 *            - mile
	 * @return LonLat
	 * 
	 *         calculate the next destination by start lat/lon, bearing, and the
	 *         distance
	 * 
	 */
	public LonLat nextPoint(double latitude, double longitude, double bearing,
			double distance) {
		double radius = 3958.75;

		double lat1 = toRad(latitude);
		double lon1 = toRad(longitude);

		double brng = toRad(bearing);

		double lat2 = Math
				.asin(Math.sin(lat1) * Math.cos(distance / radius)
						+ Math.cos(lat1) * Math.sin(distance / radius)
						* Math.cos(brng));

		double lon2 = lon1
				+ Math.atan2(
						Math.sin(brng) * Math.sin(distance / radius)
								* Math.cos(lat1),
						Math.cos(distance / radius) - Math.sin(lat1)
								* Math.sin(lat2));

		lon2 = toDeg(lon2);
		lat2 = toDeg(lat2);

		return new LonLat(lon2, lat2);
	}

	/**
	 * @param latitude1
	 * @param longitude1
	 * @param latitude2
	 * @param longitude2
	 * @return bearing
	 * 
	 *         Calculate the bearing from two lat/lon points
	 */
	public double getBearing(double latitude1, double longitude1,
			double latitude2, double longitude2) {
		double lat1 = toRad(latitude1);
		double lat2 = toRad(latitude2);
		double dLon = toRad(longitude2 - longitude1);

		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(dLon);

		return toBrng(Math.atan2(y, x));
	}

	public double toRad(double d) {
		// convert degrees to radians
		return d * Math.PI / 180;
	}

	public double toDeg(double r) {
		// convert radians to degrees (signed)
		return r * 180 / Math.PI;
	}

	public double toBrng(double r) {
		// convert radians to degrees (as bearing: 0...360)
		return (toDeg(r) + 360) % 360;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public String getSelectedFileName()
	{
//		return this.programComboBox.getSelectedItemText();
		return this.programComboBox.getItemText(this.programComboBox.getSelectedIndex());
	}
	
	public void setCurrentProgramName(String name)
	{
		currentProgramName = name;
	}
	
	public String getCurrentProgramName()
	{
		return currentProgramName;
	}
	
	public void loadProgramScript(String script)
	{
		setEditorValue(script);
	}

	public static native void printConsole(String str)
	/*-{
		$wnd.console.log(str);
	}-*/;
	
	public static native String getEditorValue()
	/*-{
		return $wnd.getEditorValue();
	}-*/;
	
	public static native void setEditorValue(String contents)
	/*-{
		$wnd.setEditorValue(contents);
	}-*/;
	
	public static native void setEditorOption(String key, boolean value)
	/*-{
		$wnd.setEditorOption(key, value);
	}-*/;
	
	public static native void showLoading()
	/*-{
		$wnd.showLoading();
	}-*/;
	
	public static native void hideLoading()
	/*-{
		$wnd.hideLoading();
	}-*/;
	
	public static native void redirect(String url)/*-{
	$wnd.location = url;
	}-*/;
	public static native void alert(String s)/*-{
	$wnd.alert(s);
	}-*/;

	public Label getStatusLabel() {
		return statusLabel;
	}

	public void setStatusLabel(Label statusLabel) {
		this.statusLabel = statusLabel;
	}

	public TextBox getSignupIdTextBox() {
		return signupId;
	}

	public void setSignupId(TextBox signupId) {
		this.signupId = signupId;
	}

	public TextBox getIdTextBox() {
		return id;
	}

	public void setIdTextBOx(TextBox id) {
		this.id = id;
	}

	public PasswordTextBox getPwBox() {
		return pw;
	}

	public void setPw(PasswordTextBox pw) {
		this.pw = pw;
	}

	public DialogBox getSignupDialogBox() {
		return signupDialogBox;
	}

	public void setSignupDialogBox(DialogBox dialogBox) {
		this.signupDialogBox = dialogBox;
	}

	public DialogBox getLoginDialogBox() {
		return loginDialogBox;
	}

	public void setLoginDialogBox(DialogBox loginDialogBox) {
		this.loginDialogBox = loginDialogBox;
	}

	public boolean isTryToSave() {
		return tryToSave;
	}

	public void setTryToSave(boolean tryToSave) {
		this.tryToSave = tryToSave;
	}
	
}