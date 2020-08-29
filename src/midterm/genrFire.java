package midterm;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class genrFire extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
	
	//변수 추가 	
	protected boolean isFire; 			//불이 났는지 안 났는지 확인하는 값 
	protected int scaleFire;			//불이 난 정도 
	protected int sensorLocation;		//센서 위치 
	
	protected check_fire c_f;
	protected result_msg res_msg;
	
	
  
	public genrFire() 
	{
		this("genrFire", 15);
	}
  
	public genrFire(String name, double Int_arr_time)
	{
		super(name);
   
		
		
		addOutport("out");
		addInport("in");
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		count = 1;
		
		
		c_f = new check_fire("none", 1, false, 1);
		res_msg = new result_msg("none",0);
		
		isFire = false;
		scaleFire = 0;
		sensorLocation = 1;
		
		
		
		
		
		
		
		
		
		
		holdIn("active", int_arr_time);
	}
  
	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("active"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					//holdIn("stop", INFINITY);
				}
			}
		}
	}

	public void deltint()
	{
		if (phaseIs("active"))
		{
			count = count + 1;
			

		
			scaleFire = (int)(Math.random()*100)+1;
			
			if((0 <= scaleFire) && (scaleFire <=20)) {	//0~20이면 불 안 난것임. 
				isFire = false;
			}
			else {	// 21 <= num <= 100     =>   20넘으면 불 난 것임 .		
				isFire = true;
			}

			sensorLocation  =(int)(Math.random()*3)+1;		//1,2,3, 중 하나 
			

			
			holdIn("active", int_arr_time);
		}
	}

	public message out()
	{
		message m = new message();
		
		c_f = new check_fire("FireAlarm"+count,count, isFire, sensorLocation);
		
		if (phaseIs("active")) {
			
			m.add(makeContent("out", c_f));
		
		}
		
		
		
		//m.add(makeContent("out", new entity("sensor" + count)));
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count;
	}

}
