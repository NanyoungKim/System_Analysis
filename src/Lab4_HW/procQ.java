package Lab4_HW;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class procQ extends ViewableAtomic
{
  
	protected entity job;
	protected double processing_time;
	protected Queue q;			//큐 추가 

	public procQ()
	{
		this("procQ", 20);
	}

	public procQ(String name, double Processing_time)
	{
		super(name);
    
		addInport("in");
		addOutport("out");
		
		processing_time = Processing_time;
	}
  
	public void initialize()
	{
		job = new entity("");
		q = new Queue();	//큐 초기화 
		
		holdIn("passive", INFINITY);
	}

	public void deltext(double e, message x)	//이 부분이 중요. normal processor랑은 busy상태일 때의 처리 방식이 다른 것이므로 
	{
		Continue(e);
		if (phaseIs("passive"))			//프로세서가 일을 하지 않을 
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))			//import에 들어오는 일을 처리하는 기
				{
					job = x.getValOnPort("in", i);
					
					holdIn("busy", processing_time);
				}
			}
		}
		
		
		else if(phaseIs("busy"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i)) 				//메세지가 import에 들어오는가? -> queue와 상관없이 들어옴.
				{
					entity temp = x.getValOnPort("in", i);
					q.add(temp);
					
					
					//holdIn("busy", processing_time); -> 외부에서 들어오는 일을 추가만 해줘야 하므로 없어야함.
				}
			}
		}
	}
  
	public void deltint()			//큐사이즈가 0인가 아닌가에 따라 나눠야함. (큐사이즈가 0 이면 일 끝냄)
	{
		if (phaseIs("busy"))
		{  
			if(q.isEmpty()) {			//큐에 더 이상 일이 없다. 
				
				job = new entity("");	//일 초기화. 필수적인 건 아니지만 초기화 해주는 단계에서는 데이터사이언스에서는 의미가 있는 코드.
				
				holdIn("passive", INFINITY);
				
			}
			
			else if(!q.isEmpty()) {		//큐에 할 일이 있다. 
				
				job = (entity)q.removeFirst();		//처리할 일을 받고 
				
				holdIn("busy", processing_time);
				
				
			}
		}
			
	}
		

	public message out()			//external 에서 바로 out으로 감. 이 코드는 바꿀 필요가 없음. (일을 다 처리한 것을 메세지로 보내주는 것이기 때문)
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
		+ "\n" + "job: " + job.getName();
	}

}

