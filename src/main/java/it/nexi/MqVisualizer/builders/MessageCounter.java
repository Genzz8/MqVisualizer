package it.nexi.MqVisualizer.builders;

import java.util.Collections;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.springframework.jms.core.BrowserCallback;

public class MessageCounter implements BrowserCallback<Integer>{

	
	private static final MessageCounter instance = new MessageCounter();
	
	private MessageCounter() {
	}
	
	public static MessageCounter build() {
		return instance;
	}
	
	@Override
	public Integer doInJms(Session session, QueueBrowser browser) throws JMSException {
		@SuppressWarnings("unchecked")
		Integer counted = Collections.list(browser.getEnumeration()).size();
		return counted;		
	}

}
