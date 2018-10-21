package it.nexi.MqVisualizer.dao;

import java.io.InputStream;
import java.util.List;

import it.nexi.MqVisualizer.model.MqMessage;
import it.nexi.MqVisualizer.model.Queues;

public interface MqDao {

	MqMessage getMqMessageByCorrelationId(String queue, String correlationID) ;
	MqMessage getLastMqMessage(String queue) ;
	MqMessage removeMessage(Queues queue, String correlationId);
	List<MqMessage> getMessages(Queues queues) ;
	void sendMessage(String queue, MqMessage mqMessage);
	int countMessageOnQueue(String queue);
	void sendByteMessage(String queue, MqMessage mqMessage, InputStream input);
	InputStream getDataMessageByCorrelationId(String queue, String correlationID);
}
