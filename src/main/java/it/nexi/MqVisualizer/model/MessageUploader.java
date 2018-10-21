package it.nexi.MqVisualizer.model;

import java.io.Serializable;
import java.util.List;

public class MessageUploader implements Serializable {

	private static final long serialVersionUID = -6429960094605812604L;
	
	private List<MqMessage> list;

	public List<MqMessage> getList() {
		return list;
	}

	public void setList(List<MqMessage> list) {
		this.list = list;
	}
	
	
	
}
