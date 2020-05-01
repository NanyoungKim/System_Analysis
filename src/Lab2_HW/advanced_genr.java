package Lab2_HW;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class advanced_genr extends ViewableAtomic
{
	
	protected double int_arr_time;
	protected int count;
  
	public advanced_genr() 	
	{
			this("advanced_genr",20); 	//초기값 20으로 설정. 
	}
  
	public advanced_genr(String name, double Int_arr_time)	
	{
		super(name);
   
		addOutport("out");
		addInport("in");
    
		int_arr_time = Int_arr_time;
	}
  
	public void initialize()
	{
		count = 1;    //job 1 부터 시작하니까 count 초기값은 1로 설정.
		
		//오류나서 추가한 코드 -> restart 해도 잘 작동함.
		int_arr_time = 20;
		
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
					//holdIn("stop", INFINITY);
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
		
		if(int_arr_time == 20) {		//int_arr_time 이 초기값일때  
			int_arr_time = 50;			//job2 가 70에 나가므로 70-20 = 50으로 설정. 
		}
		else if(int_arr_time == 50) {	//그 이후로는 간격이 40과 50을 번갈아가면서 실행된다. 
			int_arr_time = 40;
		}
		else {//int_arr_time ==40
			int_arr_time = 50;
		}
		
		
		
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