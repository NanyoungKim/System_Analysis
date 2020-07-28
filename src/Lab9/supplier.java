package Lab9;
import simView.*;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;

public class supplier extends ViewableAtomic
{
	
	protected int initial_price;
  
	public supplier() 
	{
		this("supplier", 2000);
	}
  
	public supplier(String name, int _initial_price)
	{
		super(name);
   
		addOutport("product_out");
		addInport("product_in");
    
		initial_price = _initial_price;
	}
  
	public void initialize()
	{

		holdIn("start", 0);	//바로 메세지 보내기 
	}
  
	public void deltext(double e, message x)  
	{//최종으로 Autioneer가 aution 다 끝낸 이후에 보내주는 메세지가 들어옴.
		Continue(e);
		if (phaseIs("active"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "product_in", i))	//product in 포트로 들어옴. 
				{
					holdIn("finished", INFINITY);		//경매 완료! 
				}
			}
		}
	}

	public void deltint()	    // 초기 가격 보내고 최종 가격이 들어올때까지 기다림 
	{
		if (phaseIs("start"))	//처음 start 상태에서 	
		{
			holdIn("wait", INFINITY);	//경매가 완료되기 전 까지 멈춤. 
		}
	}

	public message out()   //초기 가겨이 얼마인지 알려줌 	
	{
		message m = new message();
		
		//알려줄 정보는 start 했다! 라는 것과 초기 가격 
		m.add(makeContent("product_out", new auction_msg("start", initial_price)));	
		return m;
	}
  
	public String getTooltipText()
	{
		return
        super.getTooltipText();
	}

}
