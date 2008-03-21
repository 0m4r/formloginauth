package com.omar.lab.ajax.gw;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class for Servlet: AjaxGateway
 *
 */
 public class AjaxGateway extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
   static final long serialVersionUID = 1L;
   private PrintWriter out = null;
   
    /* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#HttpServlet()
	 */
	public AjaxGateway() {
		super();
	}   	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/xml");
		response.setCharacterEncoding("UTF-8");  
	    out = response.getWriter();
	    AjaxRequest ar = new AjaxRequest();
	    String url = request.getParameter("url");
	    System.out.println("doGet():" + url);
	    String returnValue = ar.doGetRequest(url, 80, false, true);
	    System.out.println(returnValue);
	    out.print(returnValue);	    
	}  	
	
	/* (non-Java-doc)
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}   	  	    
}