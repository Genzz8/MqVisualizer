package it.nexi.MqVisualizer.builders;

import java.io.UnsupportedEncodingException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;

import it.nexi.MqVisualizer.model.MqMessage;

public class MqMessageMapper {

	public static MqMessage mapFromMessage(Message message) throws UnsupportedEncodingException, JMSException {
		
		MqMessage msgOut = new MqMessage();
		msgOut.setCorrelationId(message.getJMSCorrelationID());
		
		if(message instanceof BytesMessage) {
			BytesMessage bMsg = (BytesMessage) message;
			Long byteSize = bMsg.getBodyLength();
			byte[] bMessage = new byte[byteSize.intValue()]; 
			bMsg.readBytes(bMessage);
			msgOut.setMessagePreview(new String(bMessage, "UTF-8"));
		}
		
		return msgOut;
	}
	
	
}
