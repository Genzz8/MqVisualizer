package it.nexi.MqVisualizer.services;

import java.io.InputStream;
import java.util.List;

import it.nexi.MqVisualizer.model.MqMessage;
import it.nexi.MqVisualizer.model.Queues;

public interface JmsService {

	
	MqMessage getMessage(String queue);
	
	void sendMessage(MqMessage message);
	
	List<MqMessage> getMessages(Queues queues);

	void removeMessage(Queues queue, String correlationId);

	MqMessage readMessageByCorrId(Queues queue, String corrid);

	void sendMessageInBytes(MqMessage message, InputStream input);

	InputStream getDataMessage(MqMessage message);
	
	
}
