package example.edu.client.gui;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoadNetworkDialogBox extends DialogBox {
	
	private Button okButton;
	private Button cancelButton;
	private MainGUI parent;
	private VerticalPanel vPanel;
	private ListBox dropBox;
	
	public LoadNetworkDialogBox(MainGUI mainGUI)
    {
        super();

        setGlassEnabled(true);
        setAnimationEnabled(true);
        this.parent = mainGUI;
        this.vPanel = new VerticalPanel();
        
        init();
        okButton.addClickHandler(new OKBtnHandler()); 
        cancelButton.addClickHandler(new cancelBtnHandler());
        
        setWidget(vPanel);
        
        
    }
	
	public void init(){
    	this.okButton = new Button("OK");
        this.cancelButton = new Button("Cancel");
        this.dropBox = new ListBox();
        this.dropBox.addItem("WEST LAF");
        this.dropBox.addItem("LAF small");
        this.dropBox.addItem("Greater LAF");
        
        Label dataset = new Label("Data set");
        
        HorizontalPanel hPanel = new HorizontalPanel();
        hPanel.add(dataset);
        hPanel.add(this.dropBox);
        hPanel.setCellHorizontalAlignment(dropBox,HasHorizontalAlignment.ALIGN_RIGHT);
        vPanel.add(hPanel);
        
        HorizontalPanel buttons = new HorizontalPanel();
        buttons.add(okButton);
        buttons.add(cancelButton);
        this.vPanel.add(buttons);
        this.vPanel.setCellHorizontalAlignment(buttons, HasHorizontalAlignment.ALIGN_CENTER);
	}
	
	private class OKBtnHandler implements ClickHandler{

		public void onClick(ClickEvent event) {
			hide();
			int index = dropBox.getSelectedIndex();
			
			if(index == 0){
				parent.updateFileNames("node-test3.txt", "edge-test3.txt", ",");
			}
			else if (index == 1 )
	      	{
	      		parent.updateFileNames("node-test1.txt", "edge-test1.txt", ",");
	      		
	      	}
	      	else if (index == 2){
	      		parent.updateFileNames("node-test2.txt", "edge-test2.txt", ",");
	      		
	      	}
			
		}
	}
	
    private class cancelBtnHandler implements ClickHandler{

		public void onClick(ClickEvent event) {
			hide();
		}
    }

}
