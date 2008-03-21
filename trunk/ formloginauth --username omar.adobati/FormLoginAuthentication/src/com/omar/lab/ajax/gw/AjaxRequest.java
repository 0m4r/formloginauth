package com.omar.lab.ajax.gw;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

public class AjaxRequest extends HttpClient
{
	
	public AjaxRequest()	{
		super();	
		
	}

	public String doGetRequest(String url, int port, boolean https, boolean getResponse) throws HttpException, IOException{
		String protocol = "http";
		if(https)
			protocol = "https";
		
		getHostConfiguration().setHost(url, port, protocol);
		String resp = null;
		GetMethod getReq = new GetMethod(url);

		this.executeMethod(getReq);
		if (getResponse){
			resp = getReq.getResponseBodyAsString().trim();
			System.out.println("[" + new java.util.Date() + "] GET: " + getReq.getURI());
		}
		getReq.releaseConnection();
		int statusCode = getReq.getStatusCode();
		System.out.println("doGetRequest: " + statusCode + " - " + getReq.getStatusText());
		return resp;
	}

	public String doPostRequest(String url, boolean https, boolean getResponse, String params) throws HttpException, IOException{
		String protocol = "http";
		if(https)
			protocol = "https";
		
		String resp = null;
		PostMethod postReq = new PostMethod(url);

		postReq.setRequestBody((NameValuePair[]) StringToNameValuePairs(params));

		System.out.println("execute method: ");
		this.executeMethod(postReq);
		System.out.println("executed method: ");

		if (getResponse){
			resp = postReq.getResponseBodyAsString().trim();
			System.out.println("[" + new java.util.Date() + "] POST: " + postReq.getURI());
		}
		postReq.releaseConnection();
		System.out.println("doGetRequest: " + postReq.getStatusCode() + " - " + postReq.getStatusText());		
		return resp;
	}

	private NameValuePair[] StringToNameValuePairs(String params){
		String[] parameters = params.split("&");
		NameValuePair[] nvp = new NameValuePair[parameters.length];		
		for(int i=0; i<parameters.length; i++){		
			String[] pair = parameters[i].split("=");
			String k = pair[0];
			String v = pair[1];
			System.out.println(k + "\t: " + v);
			NameValuePair tmpnvp = new NameValuePair(k, v);
			nvp[i] = tmpnvp;
		}
		return nvp;
	}

	/*
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
	*/

	public void setUpProxy(String proxyUsername, String proxyPassword, String proxyAddr, int port){
		this.getState().setProxyCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
				new UsernamePasswordCredentials(proxyUsername, proxyPassword));
		this.getHostConfiguration().setProxy(proxyAddr, port);
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
}
