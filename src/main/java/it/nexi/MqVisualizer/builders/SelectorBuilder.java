package it.nexi.MqVisualizer.builders;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;

public class SelectorBuilder {

	
	
	public static String buildSelectorFromEBCDIC(String corrId) {
		
		try {
			String corrIdEbcidc = new String(corrId.getBytes(), "IBM1047");
			String selector = Hex.encodeHexString(corrIdEbcidc.getBytes());
			return corrIdEbcidc;
			
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		
		
	}
	
	
}
