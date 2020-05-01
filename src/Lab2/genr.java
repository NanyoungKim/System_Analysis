package Lab2;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class genr extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
  
	public genr() 	
	{
			this("genr",30); 	//객체 생성시 이름맞추기, 30마다 일을 만들어서 보내므로 30으로 설정.
	}
  
	public genr(String name, double Int_arr_time)	
	{  
		super(name);
   
		addOutport("out");
		addInport("in");
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		count = 0;	//카운트는 0부터 
		
		holdIn("active",int_arr_time);	//phase 설정 .holdIn 은 자바 자체에 있는 것이 아니라 DEVS에 라이브러리로 저장해놓은것.
	}
  
	public void deltext(double e, message x)	//외부에서 트리거가 없으므로 작동 안함. 지워도 상관 없음. 
	{
		Continue(e);
		if (phaseIs("active"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "in", i))
				{
					//holdIn("stop", INFINITY); => 지워 준다. 
				} 
			}
		}
	}

	public void deltint()		//internal transition function
	{
		if (phaseIs("active"))
		{
			count++;
			
			holdIn("active",int_arr_time);
		}
	}

	public message out()	//메세지 만듦 
	{	
		
		
		message m = new message();
		m.add(makeContent("out", new entity("job" + count )));	//이것도 DEVS 라이브러리 안에 있는 용법. 메세지를 보낸다.  
		return m;
	}
  
	public String getTooltipText()	//시험에 안 나옴. 크게 신경 쓸 부붑ㄴ은 아님. 
	{
		return
        super.getTooltipText()
        + "\n" + " int_arr_time: " + int_arr_time
        + "\n" + " count: " + count;
	}

}