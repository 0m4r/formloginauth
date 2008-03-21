package com.omar.lab.httpAuth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import com.omar.lab.httpAuth.properties.Props;

public class FormLoginAuth extends HttpClient{
  private String logonSite = null;
  private String loginUrl = null;
  private int logonPort = 0;
  private String contentPage = null;
  private String protocol = null;

  private String username = null;
  private String password = null;

  public FormLoginAuth(Props p){
	this.username = p.getUsername(); 
	this.password = p.getPassword();			
	this.protocol = p.getProtocol();
	this.logonPort = p.getPort();		
	this.logonSite = p.getLogonSite();		
	this.loginUrl = (this.protocol + "://" + this.logonSite + p.getLoginPage()).trim();
	this.contentPage = p.getContentPage();
	if(this.contentPage.startsWith("http"))
	  this.contentPage = p.getContentPage();
	else	
	  this.contentPage = (this.protocol + "://" + this.logonSite + p.getContentPage()).trim();					
	
	System.out.println("Connecting to " + this.logonSite + " via " + this.protocol + " using port " + this.logonPort);
	getHostConfiguration().setHost(this.logonSite, this.logonPort, this.protocol);
  }

  public FormLoginAuth(String username, String password, String logonSite, String contentPage, String loginUrl, String protocol, int port){
	super();											
	this.username = username; 
	this.password = password;			
	this.protocol = protocol;
	this.logonPort = port;		
	this.logonSite = logonSite;		
	this.loginUrl = (this.protocol + "://" + this.logonSite + loginUrl).trim();
	if(contentPage.startsWith("http"))
	  this.contentPage = contentPage;
	else	
	  this.contentPage = (this.protocol + "://" + this.logonSite + contentPage).trim();		
	
	System.out.println("Connecting to " + this.logonSite + " via " + this.protocol + " using port " + this.logonPort);
	getHostConfiguration().setHost(this.logonSite, this.logonPort, this.protocol);
  }

  public String doGetRequest(boolean getResponse) throws HttpException, IOException{
	return doGetRequest(this.contentPage, getResponse);
  }

  public String doGetRequest(String url, boolean getResponse) throws HttpException, IOException{
	String resp = null;
	GetMethod getReq = new GetMethod(url);
	this.executeMethod(getReq);
	if (getResponse){
	  resp = getReq.getResponseBodyAsString().trim();
	  //System.out.println("[" + new java.util.Date() + "] GET: " + getReq.getURI());			
	}
	getReq.releaseConnection();		
	System.out.println("doGetRequest: " + getReq.getStatusCode() + " - " + getReq.getStatusText());
	Header header = getReq.getResponseHeader("location");
	this.manageRequest(getReq.getStatusCode(), header);
	return resp;
  }

  public String doPostRequest(boolean getResponse, HashMap params) throws HttpException, IOException{
	return doPostRequest(this.loginUrl, getResponse, params);
  }

  public String doPostRequest(String url, boolean getResponse, HashMap params) throws HttpException, IOException{
	String resp = null;
	PostMethod postReq = new PostMethod(url);
	postReq.setRequestBody(hashMapToNameValuePairs(params));
	this.executeMethod(postReq);
	if (getResponse){
	  resp = postReq.getResponseBodyAsString().trim();
	  //System.out.println("[" + new java.util.Date() + "] POST: " + postReq.getURI());
	}
	postReq.releaseConnection();
	int statusCode = postReq.getStatusCode();
	System.out.println("doPostRequest: " + url + " - " + postReq.getStatusCode() + " - " + postReq.getStatusText());
	Header header = postReq.getResponseHeader("location");
	this.manageRequest(statusCode, header);
	return resp;
  }

  private NameValuePair[] hashMapToNameValuePairs(HashMap params){
	Iterator keys = params.keySet().iterator();
	NameValuePair[] nvp = new NameValuePair[params.size()];
	int i = 0;
	while (keys.hasNext()){
	  String k = (String) keys.next();
	  String v = (String) params.get(k);
	  NameValuePair tmpnvp = new NameValuePair(k, v);
	  nvp[i] = tmpnvp;
	  i++;
	}
	return nvp;
  }

  private void manageRequest(int statusCode, Header header) throws HttpException, IOException	{
	if ((statusCode == HttpStatus.SC_MOVED_TEMPORARILY) || (statusCode == HttpStatus.SC_MOVED_PERMANENTLY)
		|| (statusCode == HttpStatus.SC_SEE_OTHER) || (statusCode == HttpStatus.SC_TEMPORARY_REDIRECT)){
	  if (header != null){
		String newUri = header.getValue();
		if ((newUri == null) || (newUri.equals(""))){
		  newUri = "/";
		}
		this.doGetRequest(newUri, true);
	  }
	}
  }

  public static void enableLogging(boolean useLogging){
	System.out.println(useLogging);
	if (useLogging)
	{
	  System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
	  System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
	  System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");
	  System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");
	}
  }

  /*
	public String getLogonSite(){
		return logonSite;
	}

	public void setLogonSite(String logonSite){
		this.logonSite = logonSite;
	}

	public int getLogonPort(){
		return logonPort;
	}

	public void setLogonPort(int logonPort){
		this.logonPort = logonPort;
	}

	public String getLoginPage(){
		return loginPage;
	}

	public void setLoginPage(String loginPage){
		this.loginPage = loginPage;
	}

	public String getFeedPage(){
		return contentPage;
	}

	public void setFeedPage(String feedPage){
		this.contentPage = feedPage;
	}

	public String getUsername(){
		return username;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}
   */
}
