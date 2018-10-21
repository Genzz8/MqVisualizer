package it.nexi.MqVisualizer.dao;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Repository;

import it.nexi.MqVisualizer.builders.ByteMessageReader;
import it.nexi.MqVisualizer.builders.MessageCounter;
import it.nexi.MqVisualizer.builders.MessageReader;
import it.nexi.MqVisualizer.builders.MqByteMessageBuilder;
import it.nexi.MqVisualizer.builders.MqMessageBuilder;
import it.nexi.MqVisualizer.builders.MqMessageMapper;
import it.nexi.MqVisualizer.model.MqMessage;
import it.nexi.MqVisualizer.model.Queues;

@Repository
public class MqDaoImpl implements MqDao {

	private static final Logger logger = Logger.getLogger(MqDaoImpl.class);
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Override
	public MqMessage getMqMessageByCorrelationId(String queue, String correlationID) {
		StringBuilder selector;
		
		try {
			
			byte[] corrIdAsBytes = correlationID.getBytes("IBM-1047");
			String hexCorr = Hex.encodeHexString(corrIdAsBytes);
			logger.info("Hex Corelation Id:  "+ hexCorr);
			selector = new StringBuilder("JMSCorrelationID like 'ID:%").append(hexCorr.toLowerCase()).append("%'")
					.append(" OR JMSCorrelationID like 'ID:%").append(hexCorr.toUpperCase()).append("%'");
			List<MqMessage> messages = jmsTemplate.browseSelected(queue,selector.toString(), MessageReader.build(10));
			if(!messages.isEmpty()) {
				if(messages.size()>1) {
					logger.info("WARNING, found more then one message with the same correlation Id, returning the firs");
				}
				return messages.get(0);
			}
			
		} catch (UnsupportedEncodingException e) {
			logger.error("Error encoding ", e);
		}
		
		MqMessage mqMessage = new MqMessage();
		mqMessage.setCorrelationId("NOT FOUND");
		mqMessage.setMessagePreview("NOT FOUND");
		return mqMessage;
	}

	@Override
	public InputStream getDataMessageByCorrelationId(String queue, String correlationID) {
		StringBuilder selector;
		
		try {
			
			byte[] corrIdAsBytes = correlationID.getBytes("IBM-1047");
			String hexCorr = Hex.encodeHexString(corrIdAsBytes);
			logger.info("Hex Corelation Id:  "+ hexCorr);
			selector = new StringBuilder("JMSCorrelationID like 'ID:%").append(hexCorr.toLowerCase()).append("%'")
					.append(" OR JMSCorrelationID like 'ID:%").append(hexCorr.toUpperCase()).append("%'");
			
			InputStream is = jmsTemplate.browseSelected(queue,selector.toString(), new ByteMessageReader());
			return is;
			
		} catch (UnsupportedEncodingException e) {
			logger.error("Error encoding ", e);
			return null;
		}
	}
	
	@Override
	public MqMessage getLastMqMessage(String queue) {
		
		MqMessage  mqMessage=null;
		Message msg = jmsTemplate.receive(queue);
		try {
			mqMessage = MqMessageMapper.mapFromMessage(msg);
			
		} catch (JMSException e) {
			logger.error(e);
		} catch (UnsupportedEncodingException e) {
			logger.error(e);
		}
		return mqMessage;
		
	}

	@Override
	public List<MqMessage> getMessages(Queues queue) {
		List<MqMessage> messages = jmsTemplate.browse(queue.getParamQueue(), MessageReader.build(200, queue));
		return messages;
	}

	@Override
	public void sendMessage(String queue, MqMessage mqMessage) {

		MqMessageBuilder builder = MqMessageBuilder.build(mqMessage);
		jmsTemplate.send(queue, builder);
		
	}
	
	@Override
	public void sendByteMessage(String queue, MqMessage mqMessage, InputStream input) {

//		MqMessageBuilder builder = MqMessageBuilder.build(mqMessage);
		MqByteMessageBuilder builder = new MqByteMessageBuilder(input, mqMessage);
		jmsTemplate.send(queue, builder);
		
	}

	@Override
	public int countMessageOnQueue(String queue) {
		int counted = jmsTemplate.browse(queue, MessageCounter.build());
		return counted;
	}

	@Override
	public MqMessage removeMessage(Queues queue, String correlationId) {
		
		MqMessage message = null;
		byte[] corrIdAsBytes;
		try {
			corrIdAsBytes = correlationId.getBytes("IBM-1047");

			String hexCorr = Hex.encodeHexString(corrIdAsBytes);
			logger.info("Hex Corelation Id:  " + hexCorr);
			StringBuilder selector = new StringBuilder("JMSCorrelationID like 'ID:%").append(hexCorr.toLowerCase())
					.append("%'").append(" OR JMSCorrelationID like 'ID:%").append(hexCorr.toUpperCase()).append("%'");

			Message msgQ = jmsTemplate.receiveSelected(queue.getParamQueue(), selector.toString());
			
			message = MqMessageMapper.mapFromMessage(msgQ);
		} catch (UnsupportedEncodingException | JMSException e) {
			logger.error("Failed to remove Messabge ", e);
		}
		return message;
	}

}
