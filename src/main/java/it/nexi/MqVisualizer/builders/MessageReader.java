package it.nexi.MqVisualizer.builders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.QueueBrowser;
import javax.jms.Session;

import org.apache.log4j.Logger;
import org.springframework.jms.core.BrowserCallback;

import it.nexi.MqVisualizer.model.MqMessage;
import it.nexi.MqVisualizer.model.Queues;

public class MessageReader implements BrowserCallback<List<MqMessage>>{

	private static final Logger logger = Logger.getLogger(MessageReader.class);
	
	private int messageNumber = 20;
	private Queues queues;
	
	private MessageReader(int messageNumber) {
		this.messageNumber = messageNumber;
	}
	
	private MessageReader(int messageNumber, Queues queues) {
		this.messageNumber = messageNumber;
		this.queues = queues;
	}
	
	public static MessageReader build(int messageNumber) {
		return new MessageReader(messageNumber);
	}
	
	public static MessageReader build(int messageNumber, Queues queues) {
		return new MessageReader(messageNumber, queues);
	}
	
	
	
	@Override
	public List<MqMessage> doInJms(Session session, QueueBrowser browser) throws JMSException {
		
		List<MqMessage> messages = new ArrayList<>();
		Enumeration<Message> messagesEnum = browser.getEnumeration();
		
		//TODO pensare a un modo per decidere da che messaggio partire
		
		if(!messagesEnum.hasMoreElements()) {
			return messages;
		} else {
			int count = 0;
			while (messagesEnum.hasMoreElements() && count < messageNumber) {
				Message message = (Message) messagesEnum.nextElement();
				
				MqMessage mqMessage = new MqMessage();
				
				try {
					logger.info("Correlation Id " + message.getJMSCorrelationID());
					String converteCorrId = new String(message.getJMSCorrelationIDAsBytes(), "IBM1047").trim();
					logger.info("Converted Correlation Id: " + converteCorrId);
					mqMessage.setCorrelationId(converteCorrId);
				} catch (UnsupportedEncodingException e) {
					logger.error("Invalid Correlation Id decoding", e);
					mqMessage.setCorrelationId(message.getJMSCorrelationID());
				}
				
				BytesMessage bMsg = (BytesMessage) message;
				Long bMsgLenght = bMsg.getBodyLength();
				byte[] bytes = new byte[bMsgLenght.intValue()];
				bMsg.readBytes(bytes);
				
				try {
					mqMessage.setMessagePreview(new String(bytes, "IBM1047"));
				} catch (UnsupportedEncodingException e) {
					logger.error("Invalid Decoding ", e);
				}
				if(this.queues!=null) {
					mqMessage.setParamQueue(queues.getParamQueue());
					mqMessage.setDataQueue(queues.getDataQueue());
				}
				messages.add(mqMessage);
				count++;
			}
		}
		
		return messages;
	}

}
