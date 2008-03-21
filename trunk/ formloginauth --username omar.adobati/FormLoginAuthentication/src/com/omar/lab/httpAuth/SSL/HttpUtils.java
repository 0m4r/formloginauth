/*
 * Created on 10-gen-2006
 */
package com.omar.lab.httpAuth.SSL;

import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.HttpVersion;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.contrib.ssl.EasySSLProtocolSocketFactory;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;


/**
 * @author VitaliV
 */
public class HttpUtils
{
  private static HttpUtils instance = null;
  private HttpClient myHttpClient = null;


  public static HttpUtils getInstance()
  {
    if (instance == null)
    {
      instance = new HttpUtils();
      
      Protocol easyhttps = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
      Protocol.registerProtocol("https", easyhttps);
    }
    
    return instance;
  }
  
  public Hashtable<String,String> doPostRequest(URL strURL, String postdata, Hashtable<String,String> headers, int times, int connectionTimeout, int replyTimeout)
  {
    Hashtable<String,String> response = new Hashtable<String,String>();

    response.put("success", "false");

    if (times > 3)
    {
      System.err.println("Too many redirect");
      return response;
    }

    int resultCode = 0;
    String docContent = null;
    PostMethod myMethod = null;

    try
    {
      // Initialize the HttpClient Object
      //
      if (myHttpClient == null)
      {
        HttpClientParams clientparams = new HttpClientParams();
        HttpConnectionManagerParams connectionmanagerparams = new HttpConnectionManagerParams();
        MultiThreadedHttpConnectionManager mtconnmanager = new MultiThreadedHttpConnectionManager();

        connectionmanagerparams.setConnectionTimeout(connectionTimeout); // one second
        connectionmanagerparams.setSoTimeout(replyTimeout); // reply timeout

        clientparams.setVersion(HttpVersion.HTTP_1_1);
        mtconnmanager.setParams(connectionmanagerparams);

        myHttpClient = new HttpClient(clientparams, mtconnmanager);
        if (System.getProperty("http.proxyHost") != null && System.getProperty("http.proxyPort") != null)
        {
          HostConfiguration hostconfig = new HostConfiguration();
          hostconfig.setProxy(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")));
          myHttpClient.setHostConfiguration(hostconfig);
        }
      }

      myMethod = new PostMethod(strURL.toExternalForm());

      // Request content will be retrieved directly
      // from the input stream
      RequestEntity entity = new StringRequestEntity(postdata);
      myMethod.setRequestEntity(entity);

      // HTTP headers
      Iterator i = headers.keySet().iterator();
      while (i.hasNext())
      {
        String item = (String) i.next();
        String value = (String) headers.get(item);

        myMethod.addRequestHeader(item, value);
      }

      System.out.println("verySimplePOST : Downloading " + strURL);
      resultCode = myHttpClient.executeMethod(myMethod);
      System.out.println("verySimplePOST : Request result code is " + resultCode);

      switch (resultCode)
      {
      case HttpStatus.SC_NOT_FOUND:
        System.out.println("HttpStatus.SC_NOT_FOUND");
        break;
      case HttpStatus.SC_MOVED_PERMANENTLY:
      case HttpStatus.SC_TEMPORARY_REDIRECT:
      case HttpStatus.SC_MOVED_TEMPORARILY:
        System.out.println("verySimplePOST : Redirection detected");

        String redirectLocation;
        Header locationHeader = myMethod.getResponseHeader("location");

        if (locationHeader != null)
        {
          redirectLocation = locationHeader.getValue();
          System.out.println("verySimplePOST : Redirected to [" + redirectLocation + "]");
          return doPostRequest(new URL(redirectLocation), postdata, headers, ++times, connectionTimeout, replyTimeout);
        }
        else
        {
          // The response is invalid and did not provide the new location for
          // the resource. Report an error or possibly handle the response
          // like a 404 Not Found error.
          System.err.println("verySimplePOST : Redirection without New location specified");
          return response;
        }
      // break;
      }

      docContent = myMethod.getResponseBodyAsString();
      if (docContent == null)
      {
        System.err.println("verySimplePOST : Unable to get response from " + strURL.toString());
        return response;
      }
      
      response.put("content", docContent);
      response.put("success", "true");
    }
    catch (Exception e)
    {
      System.err.println("Exception [" + strURL + "]: " + e.getMessage());
    }
    finally
    {
      if (myMethod != null)
      {
        myMethod.releaseConnection();
      }
    }

    return response;
  }

  public Hashtable<String,String> doGetRequest(URL strURL, Hashtable<String,String> headers, int times, int connectionTimeout, int replyTimeout)
  {
    Hashtable<String,String> response = new Hashtable<String,String>();

    response.put("success", "false");

    if (times > 3)
    {
      System.err.println("Too many redirect");
      return response;
    }

    int resultCode = 0;
    String docContent = null;
    GetMethod myMethod = null;

    try
    {
      // Initialize the HttpClient Object
      //
      if (myHttpClient == null)
      {
        HttpClientParams clientparams = new HttpClientParams();
        HttpConnectionManagerParams connectionmanagerparams = new HttpConnectionManagerParams();
        MultiThreadedHttpConnectionManager mtconnmanager = new MultiThreadedHttpConnectionManager();

        connectionmanagerparams.setConnectionTimeout(connectionTimeout); // one second
        connectionmanagerparams.setSoTimeout(replyTimeout); // reply timeout

        clientparams.setVersion(HttpVersion.HTTP_1_1);

        mtconnmanager.setParams(connectionmanagerparams);

        myHttpClient = new HttpClient(clientparams, mtconnmanager);
        if (System.getProperty("http.proxyHost") != null && System.getProperty("http.proxyPort") != null)
        {
          HostConfiguration hostconfig = new HostConfiguration();
          hostconfig.setProxy(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")));
          myHttpClient.setHostConfiguration(hostconfig);
        }
      }

      myMethod = new GetMethod(strURL.toExternalForm());

      // HTTP headers
      Iterator i = headers.keySet().iterator();
      while (i.hasNext())
      {
        String item = (String) i.next();
        String value = (String) headers.get(item);

        myMethod.addRequestHeader(item, value);
      }

      System.out.println("verySimplePOST : Downloading " + strURL);
      resultCode = myHttpClient.executeMethod(myMethod);
      System.out.println("verySimplePOST : Request result code is " + resultCode);

      switch (resultCode)
      {
      case HttpStatus.SC_NOT_FOUND:
        System.out.println("HttpStatus.SC_NOT_FOUND");
        break;
      case HttpStatus.SC_MOVED_PERMANENTLY:
      case HttpStatus.SC_TEMPORARY_REDIRECT:
      case HttpStatus.SC_MOVED_TEMPORARILY:
        System.out.println("verySimplePOST : Redirection detected");

        String redirectLocation;
        Header locationHeader = myMethod.getResponseHeader("location");

        if (locationHeader != null)
        {
          redirectLocation = locationHeader.getValue();
          System.out.println("verySimplePOST : Redirected to [" + redirectLocation + "]");
          return doGetRequest(new URL(redirectLocation), headers, ++times, connectionTimeout, replyTimeout);
        }
        else
        {
          // The response is invalid and did not provide the new location for
          // the resource. Report an error or possibly handle the response
          // like a 404 Not Found error.
          System.err.println("verySimplePOST : Redirection without New location specified");
          return response;
        }
      // break;
      }

      docContent = myMethod.getResponseBodyAsString();
      if (docContent == null)
      {
        System.err.println(" verySimplePOST : Unable to get response from " + strURL.toString());
        return response;
      }
      
      response.put("content", docContent);
      response.put("success", "true");
    }
    catch (Exception e)
    {
      System.err.println("Exception [" + strURL + "]: " + e.getMessage());
    }
    finally
    {
      if (myMethod != null)
      {
        myMethod.releaseConnection();
      }
    }

    return response;
  }

}
