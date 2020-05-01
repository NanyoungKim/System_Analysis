package Lab5_HW;
import simView.*;
import java.awt.*;

public class ef extends ViewableDigraph
{
	
	public ef()
	{
		this("ef", 15, 500);	//int_arr_t은 genr / observe_t는 transd
	}
	
	public ef(String name, double int_arr_t, double observe_t)	//이 함수 코드 외워야함!!!!!! (시험)
	{
		super(name);
		
		
		//ef전체에서의 in/out port 선언
	    addInport("in");
	    addOutport("out"); 
	    
	    //generator과 transducer 선언 (atoic 객체를 만들어준것. 
	    ViewableAtomic g = new genr("g",int_arr_t);
	    ViewableAtomic t = new transd("t", observe_t);
	    
        //simulation view 에서 나오게 하기
	    add(g);
	    add(t);
	    
	    
	    //coupling (서로 연결을 시켜줘야함)
	    //out에서 in 또는 out 으로 가는 방향성이 있음. 거꾸로 쓰면 코드 작동 안됨. 
	    
	    
	    addCoupling(g,"out", t, "ariv");
	    addCoupling(g,"out", this,"out");
	    
	    
	    addCoupling(t,"out",g,"in");
	    
	    addCoupling(this,"in",t,"solved");
	    
		
	}

    
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(463, 297);
        ((ViewableComponent)withName("t")).setPreferredLocation(new Point(144, 195));
        ((ViewableComponent)withName("g")).setPreferredLocation(new Point(36, 52));
    }
}
