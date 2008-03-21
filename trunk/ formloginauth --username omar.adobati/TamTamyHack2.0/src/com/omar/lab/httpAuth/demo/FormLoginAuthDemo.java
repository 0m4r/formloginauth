package com.omar.lab.httpAuth.demo;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang.StringEscapeUtils;

import com.omar.lab.httpAuth.FormLoginAuth;
import com.omar.lab.httpAuth.properties.Props;


public class FormLoginAuthDemo {
	
	public static void main(String[] args){		
		Props p = new Props("authDemo.properties");
		//p.list(System.out);
		FormLoginAuth fla = new FormLoginAuth(p);
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(p.getUsername_form_field(), p.getUsername());
			params.put(p.getPassword_form_field(), p.getPassword());
			fla.doPostRequest(fla.getLoginPage(), false, params);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			System.out.println();
			System.out.println(StringEscapeUtils.unescapeXml(fla.doGetRequest(fla.getFeedPage(), true)));			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
