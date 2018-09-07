package example.edu.client.gui.Timers;

import com.google.gwt.user.client.Timer;

import example.edu.client.gui.Variables;

public class DelayTimer extends Timer  
{
	private Timer t;
	
	public DelayTimer(Timer t)
	{
		this.t = t;
	}

	@Override
	public void run() 
	{
		t.scheduleRepeating(Variables.repeatTime);
	}
	
	
}
