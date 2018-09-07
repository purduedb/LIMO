package example.edu.client.gui;



import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
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
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import example.edu.client.gui.handler.SignUpButtonHandler;
import example.edu.client.gui.handler.SignupHandler;
import example.edu.client.service.LoginServiceClientImpl;

public class LoginGUI extends Composite 
{
	private LoginServiceClientImpl serviceImpl;
	private SplitLayoutPanel mainSplitPanel;
	private FormPanel loginForm;
	private DialogBox dialogBox;
	private Label statusLabel;
	private TextBox signupId;
	private TextBox id;
	private PasswordTextBox pw;
	
	public LoginGUI(LoginServiceClientImpl serviceImpl) 
	{
		this.mainSplitPanel = new SplitLayoutPanel();

		initWidget(this.mainSplitPanel);
		this.serviceImpl = serviceImpl;

		this.mainSplitPanel.setStyleName("loginLayoutPanel");
//		this.mainSplitPanel.addNorth(createTitleHPanel(), 50);
//		this.mainSplitPanel.addEast(createMapPanel(), 625);

		AbsolutePanel boundaryPanel = new AbsolutePanel();
		boundaryPanel.setSize("100%", "100%");
		this.mainSplitPanel.add(boundaryPanel);

		SplitLayoutPanel childSplitPanel = new SplitLayoutPanel();
		childSplitPanel.setSize("100%", "100%");
		boundaryPanel.add(childSplitPanel);
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		VerticalPanel vp = new VerticalPanel();
		vp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		vp.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		
		loginForm = new FormPanel();
		
		loginForm.setAction(getAbsolutePath("/j_spring_security_check"));
		loginForm.setMethod(FormPanel.METHOD_POST);
		
		Image maplogo = new Image(getAbsolutePath("/images/maplogo.png"));
		maplogo.setSize("100px", "100px");
		
		HTML titleHTML = new HTML("LIMO");
		titleHTML.setStyleName("loginTitle");
		titleHTML.setHeight("30px");
		
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
//		submitButton.removeStyleName("gwt-SubmitButton");
//		submitButton.addStyleName("gwt-Button");
		
		Anchor guestButton = new Anchor("Guest");
		guestButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				redirect(getAbsolutePath("/GWTMaps.html"));
			}
		});
		
		Anchor signupButton = new Anchor("Sign Up");
//		signupButton.addClickHandler(new SignUpButtonHandler(this));
		
		grid.setWidget(0, 0, idLabel);
		grid.setWidget(0, 1, id);
		grid.setWidget(1, 0, pwLabel);
		grid.setWidget(1, 1, pw);
		grid.setWidget(2, 1, submitButton);
		grid.setWidget(3, 1, signupButton);
		grid.setWidget(3, 3, guestButton);
		
		hp.setStyleName("loginTable");
		
		vp.add(maplogo);
		vp.add(titleHTML);
		
		hp.add(vp);
		hp.add(loginForm);
		
		
		childSplitPanel.add(hp);
		
		
		loginForm.addSubmitCompleteHandler(new SubmitCompleteHandler() {

	        @Override
	        public void onSubmitComplete(SubmitCompleteEvent e) 
	        {
//	        	alert(getAbsolutePath("/GWTMaps.html"));
	            redirect(getAbsolutePath("/GWTMaps.html"));
	        }
	    });
		
		
		
		signupGUI();
	
	}
	
	private void signupGUI()
	{
		// Create the popup dialog box
		dialogBox = new DialogBox();
		dialogBox.setText("Sign Up");
		dialogBox.setAnimationEnabled(true);

		
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
		
//		okButton.addClickHandler(new SignupHandler(this, signupId, email, pw, pw2));
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
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
		
		
		dialogBox.add(grid);
		
		
		dialogBox.setPopupPosition(Window.getClientWidth()/2 - 150,
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
	public LoginServiceClientImpl getServiceImpl() {
		return serviceImpl;
	}
	
	public Label getStatusLabel()
	{
		return statusLabel;
	}
	
	public DialogBox getDialogBox()
	{
		return dialogBox;
	}
	
	public TextBox getSignupIdTextBox()
	{
		return signupId;
	}
	
	public TextBox getIdTextBox()
	{
		return id;
	}
	
	public PasswordTextBox getPwBox()
	{
		return pw;
	}
	
	public String getAbsolutePath(String str) {
		String url = GWT.getHostPageBaseURL();
		url = url.substring(0, url.length() - 1);

		if (Location.getPort().equals("8181")) {
			return url + str;
		} else {
			return str;
		}

	}

	public static native void alert(String s)/*-{
		$wnd.alert(s);
	}-*/;
	
	public static native void redirect(String url)/*-{
    	$wnd.location = url;
  	}-*/;
	
	public static native void printConsole(String str)
	/*-{
		$wnd.console.log(str);
	}-*/;
	
}
