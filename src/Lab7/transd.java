package Lab7;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class transd extends  ViewableAtomic
{
	
	protected Function arrived, solved;
	protected double clock, total_ta, observation_time;
	
	
	protected loss_msg loss_msg;
	protected int[] loss;			//loss[0] : 전체loss 개수, loss[k] : processor k에서 발생한 loss 개수    
	protected int NODE;
		

	public transd(String name, double Observation_time, int _node)
	{
		super(name);
		NODE = _node;
    
		addOutport("out");
		addInport("ariv");
		addInport("solved");
		addInport("loss");			//로스 정보 받아오기 위해 포트 생성 
    
		arrived = new Function();
		solved = new Function();
    
		observation_time = Observation_time;
	}
  
	public transd()
	{
		this("transd", 200, 3);
	}

	public void initialize()
	{	
		
		loss_msg = new loss_msg("none",0);
		loss = new int[NODE + 1];		//loss[0] 이 필요하기 때문에 (전체 loss 개수 저장)
		
		clock = 0;
		total_ta = 0;
    
		arrived = new Function();
		solved = new Function();
		
		holdIn("on", observation_time);
	}

	public void deltext(double e, message x)
	{
		clock = clock + e;
		Continue(e);
		entity val;
 
		if(phaseIs("on"))
		{
			for (int i = 0; i < x.size(); i++)
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
          
						double turn_around_time = clock - arrival_time;
          
						total_ta = total_ta + turn_around_time;
          
						solved.put(val, new doubleEnt(clock));
					}
				}
				
				if (messageOnPort(x, "loss", i))
				{
					loss_msg = (loss_msg)x.getValOnPort("loss", i);
					loss[loss_msg.proc_num]++;			//특정 번호의 processor의 loss 증가  
					loss[0]++; 							//processor 전체에서 1개 증가    
				}
				
				
			}
			show_state();
		}
	}

	public void deltint()
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
			avg_ta_time = ( (double) total_ta) / solved.size();
		}
		return avg_ta_time;
	}

  
	public String compute_Thru()
	{
		String thruput = "";
		if (clock > 0)
		{
			thruput = solved.size() + " / " + clock;
		}
		return thruput;
	}

	public void show_state()
	{
		System.out.println("state of  " + name + ": ");
		System.out.println("phase, sigma : " + phase + " " + sigma + " ");
		
		if (arrived != null && solved != null)
		{
			System.out.println("Total jobs arrived : "+ arrived.size());
			System.out.println("Total jobs solved : " + solved.size());
			System.out.println("AVG TA = " + compute_TA());
			System.out.println("THRUPUT = " + compute_Thru());
			
			
			System.out.println();
			System.out.println("=======job loss ========");
			for(int i = 1; i<=NODE; i++) {
				System.out.println("processor"+ i + ": " + loss[i]);
			}
			System.out.println("loss rate: " + loss[0] + "/" + arrived.size());
			System.out.println("==========================");
			
			
			
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
