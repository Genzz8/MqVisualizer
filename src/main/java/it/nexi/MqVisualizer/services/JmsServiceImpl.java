package it.nexi.MqVisualizer.services;

import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.nexi.MqVisualizer.dao.MqDaoImpl;
import it.nexi.MqVisualizer.model.MqMessage;
import it.nexi.MqVisualizer.model.Queues;


@Service
public class JmsServiceImpl implements JmsService {
	
	private static final Logger logger = Logger.getLogger(JmsServiceImpl.class);
	
	@Autowired
	private MqDaoImpl mqDao;
	
	@Override
	public MqMessage getMessage(String queue) {

		if(mqDao.countMessageOnQueue(queue)==0) {
			return null;
		}
		MqMessage  mqMessag = mqDao.getLastMqMessage(queue);
		return mqMessag;
	}

	@Override
	public void removeMessage(Queues queue, String correlationId) {
		mqDao.removeMessage(queue, correlationId);
	}
	
	
	@Override
	public void sendMessage(MqMessage message) {
		mqDao.sendMessage(message.getParamQueue(), message);
		MqMessage messData = new MqMessage();
		messData.setCorrelationId(message.getCorrelationId() + "00010001");
		messData.setMessagePreview(message.getMessage());
		mqDao.sendMessage(message.getDataQueue(), messData);
	}

	@Override
	public void sendMessageInBytes(MqMessage message, InputStream input) {
		mqDao.sendMessage(message.getParamQueue(), message);
		MqMessage messData = new MqMessage();
		messData.setCorrelationId(message.getCorrelationId() + "00010001");
		messData.setMessagePreview(message.getMessage());
		mqDao.sendByteMessage(message.getDataQueue(), messData, input);
	}

	
	@Override
	public List<MqMessage> getMessages(Queues queues) {
		
		List<MqMessage> messages = mqDao.getMessages(queues);
		
		return messages;
		
	}
	

	@Override
	public MqMessage readMessageByCorrId(Queues queue, String corrid) {
		
		MqMessage mqParamMessage = mqDao.getMqMessageByCorrelationId(queue.getParamQueue(), corrid);
		StringBuilder corrIdBuilder = new StringBuilder(mqParamMessage.getCorrelationId().substring(0, 13)).append("0001");
		MqMessage mqDataMessage = mqDao.getMqMessageByCorrelationId(queue.getDataQueue(), corrIdBuilder.toString());
		if(mqDataMessage!=null) {
			mqParamMessage.setMessage(mqDataMessage.getMessagePreview());
		} else {
			logger.error("DATA NOT FOUND");
		}
		mqParamMessage.setParamQueue(queue.getParamQueue());
		mqParamMessage.setDataQueue(queue.getDataQueue());
		return mqParamMessage;
		
	}

	@Override
	public InputStream getDataMessage(MqMessage message) {
		
		StringBuilder corrIdBuilder = new StringBuilder(message.getCorrelationId().substring(0, 13)).append("0001");
		InputStream is = mqDao.getDataMessageByCorrelationId(message.getDataQueue(), corrIdBuilder.toString());
		return is;
	}

	

}
