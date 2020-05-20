package Lab7_HW;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class processor extends ViewableAtomic
{
	
	protected Queue q;
	protected entity job;
	protected double processing_time;
	
	protected int size;	//큐사이즈
	protected int proc_num;	//로스가 발생한 프로세스 번호
	protected loss_msg loss_msg;
	protected double temp_time; 	//q size = 2, processor1에 큐가 꽉 차있다고 생각해보자. 이때 jobk가 들어오면 Loss임. 
									//jobk 가 들어온 시점에서 processor는 일을 하고 있고 남은 프로세싱 타임이 150시그마라고 하면 loss발생 후에
									//150시그마만큼 일을 해야한다.temp_time은 이 150에 대한 것이다. 
	
	public processor()
	{
		this("processor", 20, 2);
	}

	public processor(String name, double Processing_time, int _size)
	{
		super(name);
		
		size = _size;
    
		addInport("in");
		addOutport("out1");
		addOutport("out2");		//이건 로스메세지를 보내는 포트 
		
		//시험에서는 아래 이 코드 외울 필요 없음 .
		proc_num = Integer.parseInt(name.substring(9));		//name이 processor1이면 substring을 통해 processor 글자(9글자) 날려버리고 남은 1은 int형으로 바꾼다.
		
		
		processing_time = Processing_time;
	}
	
	public void initialize()
	{
		temp_time = 0;
		loss_msg = new loss_msg("none", 0);
		
		
		q = new Queue();
		job = new entity("");
		
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
					if(q.size() < size) {		//큐사이즈가 설계된 큐사이즈보다 작아야 일을 함 -> 설계 q : size, 실제 할당된 q.size()
						entity temp = x.getValOnPort("in", i);
						q.add(temp);
						
					}
					else {						//일 처리 못했을 때  
						temp_time = sigma;
						holdIn("loss",0);
						
					}
					
					
					
				}
			}
		}
	}
	
	public void deltint()			//로스상태일 때 추가 
	{
		
		
		if(phaseIs("loss")) {				//processor time이 300인데 100만큼 일을 한 상황에서 loss가 발생했다고 하자. 이때 temp_time=200
			holdIn("busy", temp_time);	//로스가 발생했을 때 남아있는 일을 해야하므로  
		}
		else if (phaseIs("busy"))			
		{
			if (!q.isEmpty())
			{
				job = (entity) q.removeFirst();
				
				holdIn("busy", processing_time);
			}
			else										//큐 비어있으면 passive 	
			{
				job = new entity("");
				loss_msg = new loss_msg("loss", 0);		//큐에 일 없으면 로스메세지 초기화   
				holdIn("passive", INFINITY);
			}
		}
	}

	public message out()
	{
		message m = new message();
		
		if (phaseIs("busy"))
		{
			m.add(makeContent("out1", job));		//일 완료  
		}
		else if (phaseIs("loss")) {
			loss_msg = new loss_msg ("processor"+proc_num+ " :lost a job", proc_num);		//일 로스  
			
			m.add(makeContent("out2", loss_msg));
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



