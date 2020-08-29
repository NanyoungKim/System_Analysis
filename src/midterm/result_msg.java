package midterm;
import GenCol.*;

public class result_msg extends entity
{   
	
	int result;
	
	public result_msg(String name, int _result) // 파라미터 String name 은 툴이 고정되어 있어서 수정불가.
	{  
		super(name);  
		
		result = _result;
		
	}
	
}
