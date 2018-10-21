package it.nexi.MqVisualizer.model;

import java.io.Serializable;

public class MqMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7401247414219060184L;
	private String correlationId;
	private String messagePreview;
	private String message;
	private String paramQueue;	
	private String dataQueue;
	private String charset;
	private String desc;
	
	
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getMessagePreview() {
		return messagePreview;
	}
	public void setMessagePreview(String messagePreview) {
		this.messagePreview = messagePreview;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCorrelationId() {
		return correlationId;
	}
	public void setCorrelationId(String correlationId) {
		this.correlationId = correlationId;
	}
	public String getParamQueue() {
		return paramQueue;
	}
	public void setParamQueue(String paramQueue) {
		this.paramQueue = paramQueue;
	}
	public String getDataQueue() {
		return dataQueue;
	}
	public void setDataQueue(String dataQueue) {
		this.dataQueue = dataQueue;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
