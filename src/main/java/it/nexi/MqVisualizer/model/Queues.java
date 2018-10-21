package it.nexi.MqVisualizer.model;

public class Queues {

	private String paramQueue;
	private String dataQueue;
	private String charset;
	
	public Queues() {
	}
	
	public Queues(String paramQueue, String dataQueue) {
		this.paramQueue = paramQueue;
		this.dataQueue = dataQueue;
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
	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}
	
}
