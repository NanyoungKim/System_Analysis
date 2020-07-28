package Lab9;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;
import java.util.Random;			//랜덤값 

public class customer extends ViewableAtomic
{
  
	protected int bid_price;			//책정가격  
	protected int price_elasticity;		//변동폭 
	protected int max_bid_price;		//한계
	protected int posted_price;
	
	protected int customer_id;
	
	protected auction_msg auction_msg;
	protected bid_msg bid_msg;
	Random r = new Random();			//랜덤 값이랑 변동 폭이랑 곱해져서 추가 가격이 정해짐 
										//r 은 0~1 사이의 값. 
	
	
	public customer()		
	{
		this("customer", 20, 0, 0);		//변동폭, 한계치, id 
	}

	public customer(String name, int _price_elasticity,
			int _max_bid_price, int _customer_id)
	{
		super(name);

		price_elasticity = _price_elasticity;
		max_bid_price = _max_bid_price;
		customer_id = _customer_id;
		
		addInport("start");		//최초 가격 
		addInport("bid_in");	//이전에 얼마에 제시가 되었는가 알려주는 포트 
		addInport("result_in");	//최종적으로 얼마에 팔렸는지 알려줌. 

		
		addOutport("bid_out");	//이만큼 돈을 내겠다! 하고 제시하는 가격 
		
	}
  
	public void initialize()
	{
		posted_price = 0;		//그냥 초기화 해주기 
		bid_price = 0;
		
		auction_msg = new auction_msg("none",0);
		
		holdIn("wait_1", INFINITY);	//wait_1 : Customer에 물품이 들어오기 전 가격을 모르는 상태 .initial 가격도 안 들어온 상태 
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait_1"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "start", i))				//wait_1 일 때 start 포트로 들어오는 메세지 받음 
				{
					auction_msg =(auction_msg) x.getValOnPort("start", i);
					
					posted_price = auction_msg.post_price;
					bid_price = posted_price + (r.nextInt(price_elasticity)+1);		//r*변동값 한 것을 정수로 만들어줌. 
					
					if(posted_price > max_bid_price) {
						bid_price = 0;						//한계치 넘으면 안쓸래 
					}
					
					holdIn("bidding", 0);			//가격 보냈어 하는 상태 바꿔줌 
				}
			}
		}
		else if (phaseIs("wait_2"))			//wait_2 : 가격을 받아서 처리해서 보낸 후 기다리는 상태 
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "bid_in", i))			//bid_in 포트로 들어오는 것 받음 
				{
					
					auction_msg =(auction_msg) x.getValOnPort("bid_in", i);		
					
					posted_price = auction_msg.post_price;
					bid_price = posted_price +(r.nextInt(price_elasticity)+1);
					
					if(posted_price > max_bid_price) {
						bid_price = 0;
					}
					
					holdIn("bidding", 0);
					
					
				}
				if(messageOnPort(x, "result_in", i))		//최종적으로끝났을 때는 
				{
					holdIn("wait_1", INFINITY);		//다시 wait_1으로 상태 돌려놓음 
				}
			}
		}
	}
	public void deltint()
	{
		if (phaseIs("bidding"))			//메세지를 보내고 기다리게 상태 바꿔줌 
		{		
			holdIn("wait_2", INFINITY);
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("bidding"))			//bidding 상태일때 bid_out으로 메세지 보냄 
		{
			m.add(makeContent("bid_out", new bid_msg("bid_msg #"
					+ customer_id, customer_id, bid_price)));		
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText()
		+"\n" + "pe: " + price_elasticity
		+"\n" + "pe: " + max_bid_price
		+"\n" + "pe: " + bid_price;
	}

}