package example.edu.client.gui.handler;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import example.edu.client.gui.MainGUI;

public class SaveAsButtonHandler implements ClickHandler {

	private MainGUI gui;
	
	public SaveAsButtonHandler(MainGUI gui)
	{
		this.gui = gui;
	}
	
	@Override
	public void onClick(ClickEvent event) {
		
		
		String username = gui.getUsername();
		
		if(username.equals(""))
		{
			gui.getLoginDialogBox().show();
			gui.getIdTextBox().setFocus(true);
			gui.setTryToSave(true);
			return;
		}
		
		
		String script = gui.getEditorValue();
//		String filename = "noname7";
		
		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Save as..");
		dialogBox.setAnimationEnabled(true);
		dialogBox.setWidth("200px");
		dialogBox.setAutoHideEnabled(true);
		final Button okButton = new Button("Save");
//		okButton.setHeight("20px");
		final Button closeButton = new Button("Cancel");
//		closeButton.setHeight("20px");
		// We can set the id of a widget by accessing its Element
//		closeButton.getElement().setId("closeButton");
		final TextBox fileName = new TextBox();
		fileName.setHeight("13px");
		
		if (gui.getSelectedFileName() != "File List..")
		{
			fileName.setText(gui.getSelectedFileName());
		}
		else
		{
			fileName.setText("noname");
		}
		
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>File name: </b>"));
		dialogVPanel.add(fileName);
//		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
//		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setWidth("100px");
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		
		buttonPanel.add(okButton);
		buttonPanel.add(closeButton);
		
		
		dialogVPanel.add(buttonPanel);
		
		
		dialogBox.setWidget(dialogVPanel);

		
		okButton.addClickHandler(new FileNameClickHandler(username, fileName, script, dialogBox));
		
		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		
		dialogBox.center();
		dialogBox.setPopupPosition(Window.getClientWidth()/2 - 150,
				(int)(Window.getClientHeight() * 0.15));
		
	}
	
	private class FileNameClickHandler implements ClickHandler
	{
		TextBox filenameBox;
		String username;
		String script;
		DialogBox dialogbox;
		public FileNameClickHandler(String username, TextBox filenameBox, String script, DialogBox dialogbox)
		{
			this.filenameBox = filenameBox;
			this.username = username;
			this.script = script;
			this.dialogbox = dialogbox;
		}
		
		@Override
		public void onClick(ClickEvent event) {
			
			this.dialogbox.hide();
			gui.setCurrentProgramName(filenameBox.getText());
			gui.getServiceImpl().saveProgram(username, filenameBox.getText(), script);
			
		}
		
	}

}
