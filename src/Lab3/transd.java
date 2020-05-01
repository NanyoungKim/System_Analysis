package Lab3;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class transd extends  ViewableAtomic
{
	
	protected Function arrived, solved;
	protected double clock, total_ta, observation_time;

	public transd(String name, double Observation_time)	//우리가 이렇게 하는걸 얼마만큼 볼 것인가? ->데드라인이 정해
	{
		super(name);
    
		addOutport("out");
		addInport("ariv");
		addInport("solved");
    
		arrived = new Function(); //DEVS java eclipse에서 자체적 라이브리러에서 구현. arrived는 제너레이터가 만든 일을 받음.
		solved = new Function();	//solved는 프로세서로부터 받는 일을 아마 리스트로 저장.
    
		observation_time = Observation_time;
	}
  
	public transd()
	{
		this("transd", 200);	//어짜피 gpt에서 설정하면 되는 부분.
	}

	public void initialize()
	{	
		clock = 0;	//시간대별로 구분하기 위해 씀. 
		total_ta = 0; 
    
		arrived = new Function();	//job을 받아서 잘 해결했는지 확인하기 위함. 
		solved = new Function();
		
		holdIn("on", observation_time);	//관찰하는 동안에는 on 상태.
	}

	public void deltext(double e, message x)
	{
		clock = clock + e;
		Continue(e);
		entity val;
 
		if(phaseIs("on"))
		{
			for (int i = 0; i < x.size(); i++)					//개념적인 부분만 이해할것. 이렇게 코드 짜라고 안냄.
			{
				if (messageOnPort(x, "ariv", i))
				{
					val = x.getValOnPort("ariv", i);
					arrived.put(val.getName(), new doubleEnt(clock));
				}
				if (messageOnPort(x, "solved", i))
				{
					val = x.getValOnPort("solved", i);
					if (arrived.containsKey(val.getName()))
					{
						entity ent = (entity) arrived.assoc(val.getName());
					
						doubleEnt num = (doubleEnt) ent;
						double arrival_time = num.getv();
          
						//delext함수 시작부터 이 라인까지 구현 자세히 볼 필요 없음.
						
						
						//우리는 ta time and tta위주로 볼 것임.
						
						//genr = 20, processor = 30 -> 20에서 일을 받고 50에서 일을 해결했다고 하자.
						double turn_around_time = clock - arrival_time;	//clock은 50시점에서 클락. arrival_time은 20임.(genr가 언제 일을 보내줬느냐) 
          
						total_ta = total_ta + turn_around_time;
          
						solved.put(val, new doubleEnt(clock));
					}
				}
			}
			show_state();
		}
	}

	public void deltint()	//보여주는 것에서 인터페이스 적인 부분이라 볼 필요 없음.
	{
		if (phaseIs("on"))
		{
			clock = clock + sigma;
			System.out.println("--------------------------------------------------------");
	   		show_state();
	   		System.out.println("--------------------------------------------------------");
	   		
	   		holdIn("off", 0);
		}
	}
  
	public message out()
	{
		message m = new message();
		
		if (phaseIs("on"))
		{
			m.add(makeContent("out", new entity("TA: " + compute_TA())));
		}
		
		return m;
	}

	public double compute_TA()
	{
		double avg_ta_time = 0;
		if (!solved.isEmpty())
		{
			avg_ta_time = ( (double) total_ta) / solved.size();		//ta는 일 처리하는데에 얼마나 걸리느냐임. 총 걸린 시간을 해결된 문제수로 나눈다.시험 출제 가능성 있음.  
		}
		return avg_ta_time;
	}

  
	public String compute_Thru()
	{
		String thruput = "";
		if (clock > 0)
		{	
			thruput = solved.size() + " / " + clock;	//전체 시간동안 일을 몇개 끝냈느냐. 시험 출제 가능성 있음. 
		}
		return thruput;
	}

	public void show_state()	//출력해서 보여주는 것을 구현.
	{
		System.out.println("state of  " + name + ": ");
		System.out.println("phase, sigma : " + phase + " " + sigma + " ");
		
		if (arrived != null && solved != null)
		{
			System.out.println("Total jobs arrived : "+ arrived.size());
			System.out.println("Total jobs solved : " + solved.size());
			System.out.println("AVG TA = " + compute_TA());
			System.out.println("THRUPUT = " + compute_Thru());
		}
	}	
  
	public String getTooltipText()
	{
		String s = "";
		if (arrived != null && solved != null)
		{
			s = "\n" + "jobs arrived :" + arrived.size()
			+ "\n" + "jobs solved :" + solved.size()
			+ "\n" + "AVG TA = " + compute_TA()
			+ "\n" + "THRUPUT = " + compute_Thru();
		}
		return super.getTooltipText() + s;
	}

}
