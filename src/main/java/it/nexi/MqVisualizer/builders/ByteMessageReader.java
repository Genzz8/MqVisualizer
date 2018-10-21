package it.nexi.MqVisualizer.builders;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Enumeration;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.springframework.jms.core.BrowserCallback;

public class ByteMessageReader implements BrowserCallback<InputStream> {

	@Override
	public InputStream doInJms(Session session, QueueBrowser browser) throws JMSException {
		
		InputStream is = null;
		Enumeration<Message> messagesEnum = browser.getEnumeration();
		if(!messagesEnum.hasMoreElements()) {
			return null;
		} else {
			while (messagesEnum.hasMoreElements()) {
				
				Message message = (Message) messagesEnum.nextElement();
				
				
				BytesMessage bMsg = (BytesMessage) message;
				Long bMsgLenght = bMsg.getBodyLength();
				byte[] bytes = new byte[bMsgLenght.intValue()];
				bMsg.readBytes(bytes);
				
				is = new ByteArrayInputStream(bytes);
			}
		}
		
		return is;
	}

}
