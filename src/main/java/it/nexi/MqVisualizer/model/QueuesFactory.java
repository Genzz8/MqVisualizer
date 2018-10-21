package it.nexi.MqVisualizer.model;

import java.util.ArrayList;
import java.util.List;

public class QueuesFactory {

	public static final List<Queues> queues;
	
	static {
		
		queues = new ArrayList<Queues>();
		Queues q1 = new Queues();
		q1.setParamQueue("PNCBI.LAYER.TESTLC02");
		q1.setDataQueue("PNCBI.LAYER.TESTLC01");
		q1.setCharset("UTF-8");
		queues.add(q1);
		
		Queues q2 = new Queues();
		q2.setParamQueue("FTAE.FROM.HOST.TO.LAYER.BCM.PARAM");
		q2.setDataQueue("FTAE.FROM.HOST.TO.LAYER.BCM.FILE");
		q2.setCharset("IBM1047");
		queues.add(q2);
		
		Queues q3 = new Queues();
		q3.setParamQueue("FTAE.FROM.HOST.TO.LAYER.POS.PARAM");
		q3.setDataQueue("FTAE.FROM.HOST.TO.LAYER.POS.FILE");
		q3.setCharset("IBM1047");
		queues.add(q3);
		
		Queues q4 = new Queues();
		q4.setParamQueue("FTAE.FROM.HOST.TO.LAYER.QUAD.PARAM");
		q4.setDataQueue("FTAE.FROM.HOST.TO.LAYER.QUAD.FILE");
		q4.setCharset("IBM1047");
		queues.add(q4);
		
	}
	
	
}
