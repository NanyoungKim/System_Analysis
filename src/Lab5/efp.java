package Lab5;
import java.awt.*;
import simView.*;

public class efp extends ViewableDigraph 
{

	public efp()
	{
		super("efp");
		
		
		//atiomic 모델 선언 여기서는 프로세스 하나 밖에 없음 
		ViewableAtomic p = new proc("p",35);
		
		//다이그래프 선언
		ViewableDigraph ef = new ef("ef",40,1000);	//ef.java 에서 숫자를 변경해도 efp 가 더 상위개념이라 영향 없음. 
		
		//simulation view 에서 나오게 하기 , add() 는 atomic, digraph에 모두 적용되므로 용법 같음. 
	    add(p);
	    add(ef); 
		
		//ef의 out과 proc의 in 연결 
	    addCoupling(ef,"out",p, "in");
	    
	    //proc의 out과 ef 의 in 연결 
	    addCoupling(p,"out",ef, "in");

		
	}
	
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(980, 644);
        ((ViewableComponent)withName("ef")).setPreferredLocation(new Point(105, 55));
        ((ViewableComponent)withName("p")).setPreferredLocation(new Point(593, 353));
    }
 }
