package Lab9;
import simView.*;
import genDevs.modeling.*;
import GenCol.*;
import simView.*;

public class auctioneer extends ViewableAtomic
{

	protected int [] bid_list;			//customer 들의 제시가 
	protected int customer_number;		//customer id
	protected int customer_counter;		//customer 몇명?
	protected int count;				//라운드 수 (autioneer가 customer에게 가격뿌리고 돌려받는게 라운드 1개)
	
	protected String max_bidder;		//매 라운드마다 최종 입찰자 id들을 string 화 한것 
	protected int max_price;			//customer 들의 가격 중 최대 금액 
	
	protected String end_bidder;		//최종 낙찰한 customer 의 id 
	protected int end_price;			//최종 낙찰 가 
	
	protected auction_msg auction_msg;
	protected bid_msg bid_msg;
	
	
	public auctioneer()
	{
		this("autioneer", 5);		//customer 의 수 총 5명 
	}

	public auctioneer(String name, int _customer_number)
	{
		super(name);
    
		customer_number = _customer_number;
		
		addInport("product_in");		//supplier 가 처음에 얼마로 줬는지 
		addInport("bid_in");			//각 라운드에서 들어오는 금액 
		
		addOutport("result_out");		//최종 낙찰가를 customer들에게 알려줌 
		addOutport("bid_out");
		addOutport("auction_init");		//맨 처음 supplier 한테 받은 초기가격을 customer 들에게 보냄 
		
		
	}
  
	public void initialize()
	{
		bid_list = new int[customer_number +1];
		customer_counter =0;
		count = 0;
		max_bidder = "0";
		max_price = 0;
		end_bidder = "0";
		
		auction_msg = new auction_msg("none", 0);
		bid_msg = new bid_msg("none",0,0);
		holdIn("wait", INFINITY);				//supplier의 물품 기다리는 상태 
	}

	public void deltext(double e, message x)
	{
		Continue(e);
		if (phaseIs("wait"))						//wait 상태에서 물품이 들어오면 
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "product_in", i))
				{
					auction_msg = (auction_msg)x.getValOnPort("product_in", i);	//메세지 받아서 출력. 
					System.out.println(auction_msg.getName()+ ": init_price " + auction_msg.post_price);
					holdIn("start", 0);
				}
			}
		}
		
		else if (phaseIs("bidding"))
		{
			for (int i = 0; i < x.getLength(); i++)
			{
				if (messageOnPort(x, "bid_in", i))
				{
					
					bid_msg = (bid_msg)x.getValOnPort("bid_in", i);
					
					bid_list[bid_msg.customer_id] = bid_msg.bidding_price; //리스트에 customer 가 제시한 가격을 받아옴 
					
					System.out.println("customer#" + bid_msg.customer_id + ": " + bid_msg.bidding_price);
					
					customer_counter++;
					
					if(customer_counter == customer_number)		//라운드 하나 끝났으면 , 즉 5명한테 다 받았으면 
					{
						customer_counter = 0;					//초기화 
						max_price = bid_list[1];				//customer 는 1번부터 있으므로 일단 초기화 
						max_bidder ="1";
						for(int j =2; j< customer_number +1 ; j++)		
						{
							if(max_price<bid_list[j]) {			
								max_price = bid_list[j];
								max_bidder = Integer.toString(j);
							}
						}
						auction_msg.post_price = max_price;		// 예를들어 1라운드에서 3번 customer 가 가장 비싸게 제시했으면 그 가격으로 바꿔줌 
						
						count++;		//라운드 수하나 증가 
						
						System.out.println("\nstage" + count);				//몇번째 라운드인지 출력 
						System.out.println("customer# " + max_bidder + "is winner");	//해당 라운드에서 몇번째 customer 가 가장 비싸게 샀는지 
						
						if(count == 5) 		//최종 라운드이면 
						{
							count = 0;
							holdIn("result", 0);	//결과 나왔으니까 
						}
						
						else 
						{
							holdIn("sent", 0);	//최종 라운드 아니면 계속 라운드 진행 
						}	
					}
				}
			}
		}
	}
  
	public void deltint()
	{
		if (phaseIs("start"))				//초기에 : start 일때 customer에게 가격 보내고 bidding 으로 바꿔줌 
		{
			holdIn("bidding", INFINITY);
		}
		else if (phaseIs("sent"))			//중간에 : customer 에게 가격 보냈으면 bidding 으로 바꿔줌 
		{
			holdIn("bidding", INFINITY);
		}
		else if (phaseIs("result"))			//최종적으로 끝났을 때 
		{
			end_bidder = max_bidder;		//누가 얼마에 가장 비싸게 사갔는지에 대해서 
			end_price = max_price;
			
			System.out.println(": winner is customer#" + end_bidder + ", result price is " + end_price);
			
			holdIn("wait", INFINITY);		//다시 경매 준비 
		}
	}

	public message out()
	{
		message m = new message();
		if (phaseIs("start"))			//초기 가격 알려줌 
		{
			m.add(makeContent("auction_init", new auction_msg(
					"auction_msg #a(start)", auction_msg.post_price)));
		}
		else if (phaseIs("sent"))		//
		{
			m.add(makeContent("bid_out", new auction_msg(
					"auction_msg #a(sent)", auction_msg.post_price)));		//얼마에 가장 비싸게 팔렸는지 customer 에게 알려줌 
		}
		else if (phaseIs("result"))     //최종 결과 보냄 
		{
			m.add(makeContent("result_out", new auction_msg(
					"auction_msg #a(result)", max_price)));
		}
		return m;
	}

	public String getTooltipText()
	{
		return
		super.getTooltipText();
	}

}

