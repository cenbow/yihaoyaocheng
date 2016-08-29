package com.yyw.yhyc.pay.tenpay.client;

import com.yyw.yhyc.utils.XMLUtil;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author miklchen
 *
 */
public class XMLClientResponseHandler extends ClientResponseHandler {

	protected void doParse() throws JDOMException, IOException {
		String xmlContent = this.getContent();
		
		Map m = XMLUtil.doXMLParse(xmlContent);
		
		Iterator it = m.keySet().iterator();
		while(it.hasNext()) {
			String k = (String) it.next();
			String v = (String) m.get(k);
			this.setParameter(k, v);
		}
		
	}
	
}