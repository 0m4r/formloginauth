package com.omar.lab.httpAuth.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

public class Props extends Properties {
	
	public Props(){
		super();			
		loadPropertiesFile("auth,properties");	
	}
	
	public Props(String fileName){
		super();			
		loadPropertiesFile(fileName);	
	}

	private void loadPropertiesFile(String fileName){
		try {	    	
			InputStream resourceAsStream = this.getClass().getResourceAsStream (fileName);	
			load(resourceAsStream);
		} catch (Exception e) {
			System.err.println("Cannot find properties file");
			e.printStackTrace();
		}	
	}	

	public String getContentPage() {
		return this.getProperty("contentPage");
	}	

	public String getLoginPage() {
		return this.getProperty("loginPage");
	}

	public String getLogonSite() {
		return this.getProperty("logonSite");
	}

	public String getPassword() {
		return this.getProperty("password");
	}

	public String getPassword_form_field() {
		return this.getProperty("password_form_field");
	}

	public String getUsername() {
		return this.getProperty("username");
	}

	public String getUsername_form_field() {
		return this.getProperty("username_form_field");
	}

	public int getPort() {
		int port = 0;
		String portS = this.getProperty("port");
		if(portS!=null && !portS.equals(""))
			port = Integer.parseInt(portS);
		return port;
	}

	public String getProtocol() {
		return this.getProperty("protocol");
	}
	
	public String getCustomProperty(String propName){
		return this.getProperty(propName);
	}		
}
