package com.omar.tamtamy;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.lang.StringEscapeUtils;

import com.omar.lab.httpAuth.FormLoginAuth;
import com.omar.lab.httpAuth.Error.RSSErrorHandler;

import com.omar.lab.httpAuth.SSL.SSLUtilities;
import com.omar.lab.httpAuth.properties.Props;

public class FormAuthLogin extends javax.servlet.http.HttpServlet
{
	static final long serialVersionUID = 1L;
	private PrintWriter out = null;
	private String username = null;
	private String password = null;
	private String contentPage = null;
	private String loginUrl = null;
	private String logonSite = null;
	private String username_form_field = null;
	private String password_form_field = null;
	private String protocol = null;
	private int port = 0;
	private boolean httpsAuth = false;


	private Props p = null;

	public void init()  {
		FormLoginAuth.enableLogging(false); 
		p = new Props();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");  
		out = response.getWriter();				

		if(validate(request)){    
			if(protocol.equals("https") && port==443){
				httpsAuth = true;
				enableSSLStuff();
			}    					
			doOutput(execute());
		}  
	}

	private boolean validate(HttpServletRequest request)  {				
		contentPage = request.getParameter("feedurl");
		if(contentPage==null)
		  contentPage = p.getContentPage();
		
		logonSite = request.getParameter("logonSite");
		if(logonSite==null)
		  logonSite = p.getLogonSite();
		
		loginUrl = request.getParameter("loginUrl");
		if(loginUrl==null)
		  loginUrl = p.getLoginPage();
		
		username_form_field = request.getParameter("username_form_field");		
		if(username_form_field==null)
		  username_form_field = p.getUsername_form_field();
		
		password_form_field = request.getParameter("username_form_field");		
		if(password_form_field==null)
		  password_form_field = p.getPassword_form_field();
		
		username = request.getParameter("usr");		
		if(username==null)
		  username = p.getUsername();			
		
		
		password = request.getParameter("pwd");
		if(password==null)
		  password = p.getPassword();	
		
		protocol = request.getParameter("protocol");		
		if(protocol==null)
		  protocol= p.getProtocol();		
		
		String portS = request.getParameter("port");		
		if(portS==null)
		  port = p.getPort();
		else
		  port = Integer.parseInt(portS);	
		
		System.out.println(username + " " + password);
		System.out.println(contentPage + " " + logonSite + " " + loginUrl + " " + loginUrl + " " + password_form_field + " " + port + " " + protocol);
		
		if(username==null || password==null){			
			doOutput(RSSErrorHandler.getError(RSSErrorHandler.LOGIN_ERROR).toString());
			return false;
		}
		if((contentPage==null || contentPage.equals("")) || logonSite==null || loginUrl==null || password_form_field==null || username_form_field==null || port<=0 || protocol==null){
			doOutput(RSSErrorHandler.getError(RSSErrorHandler.GENERIC_ERROR).toString());
			return false;
		}  
		
		return true;
	}

	private String execute(){		
		FormLoginAuth fla = new FormLoginAuth(username, password, logonSite, contentPage, loginUrl, protocol, port);
		String s = null;
		try{
			HashMap<String, String> params = new HashMap<String, String>();
			params.put(username_form_field, username);
			params.put(password_form_field, password);
			fla.doPostRequest(false, params);
		}catch (Exception e){
			e.printStackTrace();
			s = RSSErrorHandler.getError(RSSErrorHandler.LOGIN_ERROR).toString();
		}

		try{
			String tmp = fla.doGetRequest(true);
			String toReturn = StringEscapeUtils.unescapeXml(tmp);      
			return toReturn;
		}catch (Exception e){
			e.printStackTrace();
			s = RSSErrorHandler.getError(RSSErrorHandler.FEED_ERROR).toString();
		}

		if (s == null)
			s = RSSErrorHandler.getError(RSSErrorHandler.GENERIC_ERROR).toString();
		return s;
	}

	private void enableSSLStuff(){
		SSLUtilities.trustAllHostnames();
		SSLUtilities.trustAllHttpsCertificates();
		Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);    
		Protocol.registerProtocol("https", easyhttps);
	}

	private void doOutput(String output){
		out.println(output);
		out.close();
	}

}
