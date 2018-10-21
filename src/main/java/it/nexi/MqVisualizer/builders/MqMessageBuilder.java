package it.nexi.MqVisualizer.builders;

import java.io.UnsupportedEncodingException;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.commons.codec.binary.Hex;
import org.springframework.jms.core.MessageCreator;

import it.nexi.MqVisualizer.model.MqMessage;

public class MqMessageBuilder implements MessageCreator {

	private MqMessage message;
	
	private MqMessageBuilder(MqMessage message) {
		this.message = message;
	}
	
	public static MqMessageBuilder build(MqMessage message) {
		return new MqMessageBuilder(message);
	}
	
	@Override
	public Message createMessage(Session session) throws JMSException {
		
		BytesMessage bMsg = session.createBytesMessage();
		
		byte[] corrIdAsBytes;
		try {
			corrIdAsBytes = message.getCorrelationId().getBytes("IBM-1047");
			bMsg.setJMSCorrelationIDAsBytes(corrIdAsBytes);
			
			byte[] messageEbcdic = message.getMessagePreview().getBytes("IBM-1047");
			bMsg.writeBytes(messageEbcdic);
			
		} catch (UnsupportedEncodingException e) {
			throw new JMSException("Impossibile convertire il messaggio");
		}
		
		return bMsg;
		
	}

}
