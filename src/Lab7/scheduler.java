package Lab7;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class scheduler extends ViewableAtomic
{
  
	protected entity job;
	protected double scheduling_time;
	
	
	protected int NODE; 		//프로세스 개수 
	protected int outport_num;	//scheduler 에서 out port 개수  

	public scheduler()
	{
		this("scheduler",0,3);		//여기서 지정해도 어짜피 RRSM 에서 최종으로 초기화 
	}

	public scheduler(String name, double Scheduling_time, int _node)
	{
		super(name);
		
		NODE = _node;
		
		addInport("in");
		for(int i=1; i<=NODE; i++) {
			
			addOutport("out"+i);
		}
    
		
		scheduling_time = Scheduling_time;
	}
  
	public void initialize()
	{
		outport_num = 1;
		job = new entity("");
		
		holdIn("passive", INFINITY);
	}

	public void deltext(double e, message x)				//여긴 바꿀 필요 없음. 
	{
		Continue(e);
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					job = x.getValOnPort("in", i);
					
					holdIn("busy", scheduling_time);
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			outport_num++;
			
			if(outport_num > NODE) {			//최대값보다 커지면 초기
				outport_num = 1; 
			}
			job = new entity("");
			
			holdIn("passive", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			m.add(makeContent("out"+outport_num, job));			// outport 에 맞게 보내줌. 
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+ "\n" + "job: " + job.getName();
	}

}

