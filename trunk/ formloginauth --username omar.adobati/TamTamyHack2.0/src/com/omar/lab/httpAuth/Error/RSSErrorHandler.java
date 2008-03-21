package com.omar.lab.httpAuth.Error;

public class RSSErrorHandler {

	public final static int USR_OR_PWD_ERROR = 0;
	public final static int LOGIN_ERROR = 1;
	public final static int FEED_ERROR = 2;
	public final static int GENERIC_ERROR = 1000;

	public static StringBuffer getError(int error){
		switch (error) {
		case 0:
			return usrPwdError();			
		case 1:
			return loginError();
		case 2:
			return feedError();
		default:
			return generalError();
		}
	}

	private static StringBuffer generalError(){
		StringBuffer s = new StringBuffer();
		s.append(RSSErrorHandler.rssHeader());
		s.append("  <item>");
		s.append("    <title> GENERAL ERROR </title>");		
		s.append("    <link> </link>");		
		s.append("    <pubDate>"+new java.util.Date()+"</pubDate>");								
		s.append("    <description> THERE WAS A GENERAL ERROR!</description>");
		s.append("  </item>");
		s.append(RSSErrorHandler.rssFooter());
		return s;
	}

	private static StringBuffer feedError() {
		StringBuffer s = new StringBuffer();
		s.append(RSSErrorHandler.rssHeader());
		s.append("  <item>");
		s.append("    <title> ERROR READING FEED PAGE</title>");		
		s.append("    <link> </link>");		
		s.append("    <pubDate>"+new java.util.Date()+"</pubDate>");								
		s.append("    <description> ERROR READING FEED PAGE </description>");
		s.append("  </item>");
		s.append(RSSErrorHandler.rssFooter());
		return s;
	}

	private static StringBuffer loginError() {
		StringBuffer s = new StringBuffer();
		s.append(RSSErrorHandler.rssHeader());
		s.append("  <item>");
		s.append("    <title> LOGIN ERROR </title>");		
		s.append("    <link> </link>");		
		s.append("    <pubDate>"+new java.util.Date()+"</pubDate>");								
		s.append("    <description> ERROR DURING THE LOGIN PROCESS </description>");
		s.append("  </item>");
		s.append(RSSErrorHandler.rssFooter());
		return s;
	}

	private static StringBuffer usrPwdError() {
		StringBuffer s = new StringBuffer();
		s.append(RSSErrorHandler.rssHeader());
		s.append("  <item>");
		s.append("    <title> INVALID CREDENTIAL </title>");		
		s.append("    <link> </link>");		
		s.append("    <pubDate>"+new java.util.Date()+"</pubDate>");								
		s.append("    <description> INVALID USERNAME OR PASSWORD </description>");
		s.append("  </item>");
		s.append(RSSErrorHandler.rssFooter());
		System.out.println(s);
		return s;
	}

	private static StringBuffer rssHeader(){
		StringBuffer s = new StringBuffer();
		s.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		s.append("<rss version=\"2.0\" xmlns:content=\"http://purl.org/rss/1.0/modules/content/\">");
		s.append("<channel>");
		s.append("  <title>TamTamy2GoogleReader ERROR</title>");
		s.append("  <link> </link>");
		s.append("  <description>TamTamy2GoogleReader ERROR: </description>");
		s.append("  <pubDate>"+new java.util.Date()+"</pubDate>");
		s.append("  <generator>TamTamy2GoogleReader</generator>");
		s.append("  <language>en</language>");
		return s;
	}

	private static StringBuffer rssFooter(){
		StringBuffer s = new StringBuffer();	
		s.append("  </channel>");
		s.append("</rss>");
		return s;
	}
}