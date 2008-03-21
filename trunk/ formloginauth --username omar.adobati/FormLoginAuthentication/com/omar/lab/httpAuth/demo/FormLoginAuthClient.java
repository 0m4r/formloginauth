package com.omar.lab.httpAuth.demo;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringEscapeUtils;

import com.omar.lab.httpAuth.FormLoginAuth;
import com.omar.lab.httpAuth.SSL.SSLUtilities;
import com.omar.lab.httpAuth.properties.Props;


public class FormLoginAuthClient {

  public static void main(String[] args){		
	Props p = new Props("authDemo.properties");
	p.list(System.out);

	if(p.getProtocol().equals("https") && p.getPort()==443){		
	  SSLUtilities.trustAllHostnames();
	  SSLUtilities.trustAllHttpsCertificates();
	  Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);    
	  Protocol.registerProtocol("https", easyhttps);
	}

	FormLoginAuth fla = new FormLoginAuth(p);
	try {
	  HashMap<String, String> params = new HashMap<String, String>();
	  params.put(p.getUsername_form_field(), p.getUsername());
	  params.put(p.getPassword_form_field(), p.getPassword());
	  fla.doPostRequest(false, params);
	} catch (HttpException e) {
	  e.printStackTrace();
	} catch (IOException e) {
	  e.printStackTrace();
	}

	try {
	  System.out.println();
	  System.out.println(StringEscapeUtils.unescapeXml(fla.doGetRequest(true)));			
	} catch (HttpException e) {
	  e.printStackTrace();
	} catch (IOException e) {
	  e.printStackTrace();
	}
  }

}
