package Lab9;
import simView.*;
import GenCol.*;

public class auction_msg extends entity		
{   //가격 정보만 있는 메세지 
	
	int post_price;
	
	public auction_msg(String name, int _post_price)
	{  
		super(name);  
		
		post_price = _post_price;
	}
	
}
