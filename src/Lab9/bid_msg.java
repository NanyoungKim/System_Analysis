package Lab9;
import simView.*;
import GenCol.*;

public class bid_msg extends entity			//Customer 와 Autioneer 사이에서 주고받는 메세지 
{   
	
	int customer_id;		//몇번째 customer 의 가격이제일 높았는지 
	int bidding_price;		//전 라운드에서 가격이 최대로 얼마가 책정되었는지 
	
	public bid_msg(String name, int _customer_id, int _bidding_price)
	{  
		super(name);  
		
		customer_id = _customer_id;
		bidding_price = _bidding_price;
	}
	
}
