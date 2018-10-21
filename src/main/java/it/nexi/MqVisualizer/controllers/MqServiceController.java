package it.nexi.MqVisualizer.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.nexi.MqVisualizer.model.MessageUploader;
import it.nexi.MqVisualizer.model.MqMessage;
import it.nexi.MqVisualizer.model.Queues;
import it.nexi.MqVisualizer.model.QueuesFactory;
import it.nexi.MqVisualizer.services.JmsService;

@Controller
public class MqServiceController {

	private static final Logger logger = Logger.getLogger(MqServiceController.class);
	
	@Autowired
	private JmsService jmsService;
	
	
	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "welcome";
	}
	
	@RequestMapping("/sendMessage")
	public String sendMessage(Map<String, Object> model) {
		model.put("mqMessage", new MqMessage());
		model.put("defQueues", QueuesFactory.queues);
		return "sendMessage";
	}
	
	@RequestMapping("/jmsMessages")
	public String jmsMessagesQ(Map<String, Object> model) {
		model.put("queues", new Queues());
		model.put("defQueues", QueuesFactory.queues);
		return "jmsMessage/jmsMessages";
	}
	
	@PostMapping("/jmsMessages")
	public String getMessageOnQueue(@ModelAttribute Queues queues, Map<String, Object> model) {
		List<MqMessage> messages = jmsService.getMessages(queues);
		model.clear();
		model.put("messages", messages);
		model.put("queues", queues);
		MqMessage nextM = new MqMessage();
		model.put("nextM", nextM);
		return "jmsMessages";
	}
	
	
	@RequestMapping(value="removeMessage")
	public String removeMessage(@ModelAttribute MqMessage mqMessage, final ModelMap model) {
		logger.info("Removing Message with CorrelationId " + mqMessage.getCorrelationId());
		Queues queue = new Queues(mqMessage.getParamQueue(), mqMessage.getDataQueue());
		jmsService.removeMessage(queue, mqMessage.getCorrelationId());
		model.clear();
		return "redirect:/jmsMessages";
	} 
	
	
	@RequestMapping(value="/sendMessageStart")
	public String sendMqMessage(@ModelAttribute MqMessage mqMessage, final BindingResult bindingResult, final ModelMap model) {
	    if (bindingResult.hasErrors()) {
	        return "jmsMessages";
	    }
		Queues queues = new Queues(mqMessage.getParamQueue(), mqMessage.getDataQueue());
	    jmsService.sendMessage(mqMessage);
	    model.clear();
	    model.put("queues", queues);
	    return "redirect:/jmsMessages";
	}
	
	@RequestMapping(value="/downloadData")
	public ResponseEntity<ByteArrayResource> downloadMessage(@ModelAttribute MqMessage mqMessage, HttpServletResponse response) {
	
		String mineType = "text/plain";
		MediaType mediaType = MediaType.parseMediaType(mineType);
		
		InputStream is = jmsService.getDataMessage(mqMessage);
		
		
		try {
			byte[] data = new byte[is.available()];
			is.read(data);
			
			ByteArrayResource resourceIS = new ByteArrayResource(data);
			logger.info("Is readable " + resourceIS.isReadable());
			return ResponseEntity.ok()
				        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + mqMessage.getCorrelationId())
				        .contentType(mediaType)
				        .contentLength(resourceIS.contentLength()) //
				        .body(resourceIS);
		} catch (IOException e) {
			logger.error("No Data", e);
		}
		return null;
		
	}
	
	@RequestMapping("/uploadMessage")
	public String uploadView(Map<String, Object> model) {
		return "uploadMessage";
	}
	
	@PostMapping("/uploadMessage")
    public String handleFileUpload(@RequestParam("fileParam") MultipartFile file, 
    		@RequestParam("fileDati") MultipartFile fileDati,
            RedirectAttributes redirectAttributes) {

		ObjectMapper mapper = new ObjectMapper();
		
		try {
			
			MessageUploader uploader = mapper.readValue(file.getInputStream(), MessageUploader.class);
			
			for(MqMessage mqMessage : uploader.getList()) {
				if(!fileDati.isEmpty()) {
					jmsService.sendMessageInBytes(mqMessage, fileDati.getInputStream());
				} else {
					jmsService.sendMessage(mqMessage);
				}
			}
			
			logger.info("Lista messaggi: " + uploader.getList().size());
		} catch (IOException e) {
			logger.error("Unparsable", e);
		}
		
		logger.info("inside upload");
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/";
    }
	
	@GetMapping("jmsMessages/getMessageTest")
	@ResponseBody
	public List<Queues> getMessagesTest() {
		return QueuesFactory.queues;
	}
	
	@PostMapping("jmsMessages/getMessageTest")
	@ResponseBody
	public List<MqMessage> getMessages(@RequestBody Queues queues) {
		
		List<MqMessage> messages = jmsService.getMessages(queues);
		return messages;
	}
	
	@PostMapping("/jmsMessages/getMessage")
	@ResponseBody
	public MqMessage getMessage(@RequestBody MqMessage rmessage) {
		Queues queues = new Queues(rmessage.getParamQueue(), rmessage.getDataQueue());
		MqMessage message = jmsService.readMessageByCorrId(queues, rmessage.getCorrelationId());
		return message;
	}
	
	@DeleteMapping("/jmsMessages/getMessage")
	public void deleteMessage(@RequestBody MqMessage rmessage) {
		logger.info("Removing Message with CorrelationId " + rmessage.getCorrelationId());
		Queues queue = new Queues(rmessage.getParamQueue(), rmessage.getDataQueue());
		jmsService.removeMessage(queue, rmessage.getCorrelationId());
	}
	
}
