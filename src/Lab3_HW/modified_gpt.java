package Lab3_HW;
import java.awt.*;
import simView.*;

public class modified_gpt extends ViewableDigraph
{

	public modified_gpt()
	{
		super("modified_gpt");
    	
		ViewableAtomic g = new genr("g", 30);		//제너레이터는 30마다 (job2부터 즉 count3부터는 65)
		ViewableAtomic p = new proc("p", 60);		//processing time = 60
		ViewableAtomic t = new transd("t", 1000);	//oberservation time
    	
		add(g);
		add(p);
		add(t);
  
		addCoupling(g, "out", p, "in");
		addCoupling(g, "out", t, "ariv");
		
		addCoupling(p, "out", t, "solved");
     
		addCoupling(t, "out", g, "in");
	}

    
    /**
     * Automatically generated by the SimView program.
     * Do not edit this manually, as such changes will get overwritten.
     */
    public void layoutForSimView()
    {
        preferredSize = new Dimension(988, 646);
        ((ViewableComponent)withName("t")).setPreferredLocation(new Point(639, 117));
        ((ViewableComponent)withName("p")).setPreferredLocation(new Point(465, 476));
        ((ViewableComponent)withName("g")).setPreferredLocation(new Point(59, 148));
    }
}
