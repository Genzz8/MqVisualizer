package it.nexi.MqVisualizer.builders;

import java.io.IOException;
import java.io.InputStream;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

import it.nexi.MqVisualizer.model.MqMessage;

public class MqByteMessageBuilder implements MessageCreator {

	private InputStream inputStream;
	private MqMessage message;
	
	public MqByteMessageBuilder(InputStream inputStream, MqMessage message) {
		this.inputStream = inputStream;
		this.message = message;
	}
	
	@Override
	public Message createMessage(Session session) throws JMSException {
		
	BytesMessage bMsg = session.createBytesMessage();
		
		byte[] corrIdAsBytes;
		try {
			corrIdAsBytes = message.getCorrelationId().getBytes("IBM-1047");
			bMsg.setJMSCorrelationIDAsBytes(corrIdAsBytes);
			
			byte[] byteMessage = new byte[inputStream.available()];
			inputStream.read(byteMessage);
			bMsg.writeBytes(byteMessage);
			
		} catch (IOException e) {
			throw new JMSException("Impossibile leggere l'input");
		} finally {
			if(inputStream!=null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new JMSException("Impossibile chiudere lo streaming");
				}
			}
		}
		
		return bMsg;
		
	}

}
