package midterm;
import GenCol.*;

public class check_fire extends entity
{   
	
	boolean isFire;
	int count;
	int sensorLocation;
	

	public check_fire(String name, int _count, boolean _isFire, int _sensorLocation)
	{  
		super(name);  
		

		isFire = _isFire;
		count = _count;
		sensorLocation = _sensorLocation;
		
		
		
	}
	
}
