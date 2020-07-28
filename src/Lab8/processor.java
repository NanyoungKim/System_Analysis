package Lab8;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class processor extends ViewableAtomic
{
	
	protected Queue q;
	protected entity job;
	protected double processing_time;
	
	
	protected double size;
	
	
	public processor()	//큐사이즈만 추가해주고 변하는 것 없음. 스케쥴러가 일 거의 다 하므로 
	{
		this("processor", 20, 3);
	}

	public processor(String name, double Processing_time, int _size)
	{
		super(name);
    
		addInport("in");
		addOutport("out");	//generator와 transd에 같은 output이 나가므로 그냥 둠 
		
		size = _size;
		
		processing_time = Processing_time;
	}
	
	
	public void initialize()
	{
		q = new Queue();
		job = new entity("job");	//큰 의미는 없음 
		
		holdIn("passive", INFINITY);
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("passive"))
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					job = x.getValOnPort("in", i);
					
					holdIn("busy", processing_time);
				}
			}
		}
		else if (phaseIs("busy"))
		{
			for (int i = 0; i < x.size(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					entity jb = x.getValOnPort("in", i);
					q.add(jb);
				}
			}
		}
	}
	
	public void deltint()
	{
		if (phaseIs("busy"))
		{
			if (!q.isEmpty())
			{
				job = (entity) q.removeFirst();
				
				holdIn("busy", processing_time);
			}
			else
			{
				job = new entity("");
				
				holdIn("passive", INFINITY);
			}
		}
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("busy"))
		{
			m.add(makeContent("out", job));
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



