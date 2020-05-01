package Lab2_HW;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class proc extends ViewableAtomic
{
  
	protected entity job;
	protected double processing_time;

	public proc()
	{
		this("proc", 20);			
	}

	public proc(String name, double Processing_time)
	{ 
		super(name);
    
		addInport("in");
		addOutport("out");


		processing_time = Processing_time;

	}
  
	public void initialize()
	{
		job = new entity("");		//job을 디폴트 값으로 설정. 제너레이터로 job을 받아올 때 덮어씌움.
		
		holdIn("passive", INFINITY);	//passive 라는 이름을 우리가 지은 것. INFINITHY는 특별한 것이 없으면 멈추겠다는 의미. 
										//시간이 가도 현 상태를 유지한다는 뜻.(특별한 개입 없으면 INFINITY에서 움직이지 않는다.)
										//원래는 initialize() 후에 out() 으로 가야하는데 여기서는 INFINIT동안 움직이지 않음(아직은 
										//deltin()와 out() 으로 넘어가지 않음. 

	}

	public void deltext(double e, message x)		//initialize -> external
	{
		Continue(e);
		if (phaseIs("passive"))			//busy 상태일 때 일이 들어오면 받지 않고 pass. 
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				job = x.getValOnPort("in",i);	//in 포트로 들어오는 job을 받아옴. 
				
				holdIn("busy", processing_time);	//processing time 동안 busy 상태로 잡아둔다. 이 아토믹 모델에서는 20만큼임(line14)

			}
		}
	}
  
	public void deltint()	//initialize -> external -> out -> internal 로 넘어옴.  
	{

		if(phaseIs("busy")) {
			job = new entity();		//job을 받을 수 있게함 (이거 굳이 안써도 받을 수는 있는데 컴퓨터 사이언스 측면에서 초기
			
			holdIn("passive", INFINITY);		//일 다 끝내고 passive 상태로 
		}
				


	}

	public message out()		//initialize -> external -> out 으로 넘어옴.
	{
		message m = new message();
		if(phaseIs("busy")) {
			m.add(makeContent("out",job));	//out port 로 job 을 보낸다. 여기서는 가공하지 않고 메세지만 보낸다.
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