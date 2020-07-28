package Lab8_HW;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class scheduler extends ViewableAtomic	//스케쥴
{
  
	protected entity job;
	protected double scheduling_time;
	
	
	
	protected int NODE;		//스케줄러에서 다루는 프로세서 개수
	protected int outport_num;	//몇번 포트로 나갈 것인지
	protected double[] q_size_list;  //프로세스 각각의 큐사이즈를 알아야 일을 얼마나 진행하고 있는지 (work load)를 알 수 있다.
	
	
	protected double[] workload_list;		//각 프로세서에 할당된 일의 수를 나타내는 배열 
	protected double[] load_rate_list;
	protected double min_workload;
	

	public scheduler()
	{
		this("scheduler", 20, 4, new int[5]);
	}

	public scheduler(String name, double Scheduling_time, int _node, int[] _q_size_list)
	{
		super(name);
    
		addInport("in");
		addOutport("out_loss");
		
		addInport("in1");
		addInport("in2");
		addInport("in3");
		addInport("in4");
		addInport("in5");
		addInport("in6");

		
		addOutport("out1");
		addOutport("out2");
		addOutport("out3");
		addOutport("out4");
		addOutport("out5");
		addOutport("out6");
		
//		for(int i = 1; i<=NODE; i++) {
//			addInport("in" + i);
//			addOutport("out" + i);
//		}
		
		
	
		
		scheduling_time = Scheduling_time;
		
		
		NODE = _node;
		q_size_list = new double[NODE+1];		//리스트는 0부터 시작하는데 우리는 프로세서 1번부터 시작하므로 +1 
		for(int i = 1; i<=NODE; i++) {
			q_size_list[i] = _q_size_list[i];
		}
	
	
	}
  
	public void initialize()
	{
		
		outport_num = 1;	//최초의 프로세서에게 보낸다.
		
		job = new entity("");
		
		holdIn("passive", INFINITY);
		
		workload_list = new double[NODE+1];
		load_rate_list = new double[NODE+1];
	}

	
	//시험에서는 통째로 외울 필요는 없음. 
	public void deltext(double e, message x)		//inourt로 들어오는 일 process에서 다 끝내고 들어오는 일 구분 
	{
		Continue(e);
		if (phaseIs("passive"))
		{
			
			//	generator에게  받음  
			for (int i = 0; i < x.getLength(); i++) {		//workload 쌓
				for(int j = 1; j<=NODE; j++){
			
					if (messageOnPort(x, "in"+j, i)) {
					
						workload_list[j]--;
					}
					
				}
			}
			
			
			
			
			
			
			//스케쥴링에서 loss phase 다루고... 
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					job = x.getValOnPort("in", i);
					
					System.out.println("Workload - ");
					for(int j = 1; j<=NODE; j++) {
						
						load_rate_list[j] = (workload_list[j]/q_size_list[j]) * 100;
						System.out.println("\t processor" + j + ": " + load_rate_list[j] + "%");
						
					
					}
					
					outport_num = 1;
					min_workload = load_rate_list[1];
					
					for(int j = 1; j<=NODE; j++) {
						
						if(min_workload > load_rate_list[j]) {
							min_workload = load_rate_list[j];
							outport_num = j;
						}
					}
					if(load_rate_list[outport_num] == 100) {	//minimum한 load_rate가 100
						System.out.println("!!! Can't schedule " + job.getName());
						holdIn("loss",e);
					}
					else {
						System.out.println("*processor " + outport_num + "* has minimum workload!");
						holdIn("busy", scheduling_time);
					}
					System.out.println();
				}
			}
		}
	}
  
	public void deltint()	//지난주 수업과 다른 부분 
	{
		if (phaseIs("busy"))
		{
			workload_list[outport_num]++;	//프로세서에게 일을 할당했을 때 스케쥴러 입장에서 workload 늘어난 것 
			
			job = new entity("");
			
			holdIn("passive", INFINITY);
		}
		
		else if(phaseIs("loss")) {
			holdIn("passive", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("busy"))
		{
			m.add(makeContent("out"+outport_num, job));	//load_rate가 가장 낮은 프로세서에 할당 
		}
			
		else if(phaseIs("loss")) {		//loss 발생시 transducer에 보냄 
			m.add(makeContent("out_loss", job));
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

