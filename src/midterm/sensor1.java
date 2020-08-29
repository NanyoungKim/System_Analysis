package midterm;
import genDevs.modeling.*;
import GenCol.*;
import Lab6_HW.result_msg;
import simView.*;

public class sensor1 extends ViewableAtomic
{
	
	protected Queue q;
	protected entity job;
	protected double processing_time;
	
	
	protected check_fire c_f;
	
	protected int result;
	protected result_msg res_msg;
	protected boolean isOn;
	
	
	
	public sensor1()
	{
		this("sensor1", 20);
	}

	public sensor1(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		q = new Queue();
		job = new entity("");
		
		
		c_f = new check_fire("",1, false, 1);
		result = 0;
		res_msg = new result_msg("none",0);
		isOn = false;
		
		holdIn("passive", INFINITY);
		
		
		
		
		
	}

	public void deltext(double e, message x)

	{
		
		
		
		
		Continue(e);
		if (phaseIs("passive"))		//큐에 일이 없었으면 
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					//job = x.getValOnPort("in", i);
					c_f = (check_fire)x.getValOnPort("in",i);
						
					isOn = false;	//초기화 
					
					
					
					
					if(c_f.sensorLocation == 1) {	//sensor1로 들어온거
						
						if(c_f.isFire){	//불이 난거면 	
							
							isOn = true;
							result = c_f.count;		//몇번째 알람 발생했는지 
							processing_time = 20;
							holdIn("busy", processing_time);
						}
						
						else {	//알람은 있었는데 불은 안 난거면 
							isOn = false;
							result = 0;
							processing_time = 5;
							holdIn("busy", processing_time);
							
						}
								
						
						
					}
					else {		//알람이 다른 센서로 간거면 
						holdIn("passive", INFINITY);
					}
					
			
				}
			}
		}
		else if (phaseIs("busy"))	//큐에 이미 할 일이 있으면 
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					//entity temp = x.getValOnPort("in", i);
					c_f = (check_fire)x.getValOnPort("in", i);
					q.add(c_f);
				}
			}
		}
	}
	
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			if (!q.isEmpty())	//큐에 할 일이 있으면 
			{
				
				c_f = (check_fire) q.removeFirst();
				isOn = false;	//초기화 
				if(c_f.sensorLocation == 1) {	//sensor1로 들어온거
					
					if(c_f.isFire){	//불이 난거면 	
						
						isOn = true;
						result = c_f.count;		//몇번째 알람 발생했는지 
						processing_time = 20;
						holdIn("busy", processing_time);
					}
					
					else {	//알람은 있었는데 불은 안 난거면 
						isOn = false;
						result = 0;
						processing_time = 5;
						holdIn("busy", processing_time);
						
					}
				
					
				}
				else {		//알람이 다른 센서로 간거면 
					holdIn("passive", INFINITY);
				}
				
			}
			else
			{
				//job = new entity("");
				
				result = 0;
				c_f = new check_fire("",1, false, 1);
				res_msg = new result_msg("none",0);
				
				holdIn("passive", INFINITY);
			}
		}
	}

	public message out()
	
	{
		
		
		
		
		message m = new message();
		
		if (phaseIs("busy"))
		{
			if(isOn) {	//sensor1 로 알람이 들어오고 불이 난 거면 
				res_msg = new result_msg("FireAlarm"+result, result);
				m.add(makeContent("out", res_msg));
				
			}
			else {	//sensor1로 알람은 왔는데 불이 안 났으면 
				res_msg = new result_msg("There was no fire", result);
				m.add(makeContent("out", res_msg));
				
				
			}
		}
		
		return m;
		
	}	
	
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + "queue length: " + q.size()
        + "\n" + "queue itself: " + q.toString();
	}

}



