package it.nexi.MqVisualizer.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import it.nexi.MqVisualizer.model.MqMessage;
import it.nexi.MqVisualizer.services.JmsService;

public class TestController {

	private static final Logger logger = Logger.getLogger(TestController.class);
	
	public static final String QUEUE1 = "DEV.QUEUE.1";
	public static final String QUEUE2 = "DEV.QUEUE.2";
	
	
	@Autowired
	private JmsService jmsService;
	
	@GetMapping("/getNudes")
	@ResponseBody
	public MqMessage returnNudes() {
		MqMessage nude = null;
		try {
			 nude = jmsService.getMessage(QUEUE1);
		} catch (JmsException e) {
			logger.error(e);
		}
		return nude;
	}

	
}
