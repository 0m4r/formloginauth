<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%
  response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
  response.setHeader("Pragma","no-cache"); //HTTP 1.0
  response.setDateHeader ("Expires", 0); //prevents caching at the proxy server  
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head> 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="icon" href="img/favicon.gif" type="image/gif" />
<title>TamTamy2Reader</title>
<script type="text/javascript" src="js/prototype.js"></script>
<script type="text/javascript" src="http://www.google.com/jsapi?key=ABQIAAAAAuUAboMvbl-lAwwgzcnSmxSBVm6dSI_DqfaWu_KqoK73iyRwExSrrIkemSfQCesof1E7tbp5jKLx6g"></script>
<script type="text/javascript">
 
    google.load("feeds", "1");
 	
 	
    function grab() {
      var feed = new google.feeds.Feed("http://jlabs.adobati.it/FormLoginAuthentication/FormAuthLogin?https=true&usr="+$('usr').value+"&pwd="+$('pwd').value+"&feedurl="+$('rss').value+"&op=");                             
      feed.setResultFormat(google.feeds.Feed.XML_FORMAT);      
      feed.setNumEntries($('results').value);
      
      feed.load(function(result) {
        doFeedLinks();                
        if (!result.error) {                     
          var container = $('feed');      
          var table = document.createElement("table");
          var tHead = document.createElement("thead");
          table.appendChild(tHead);
		  var tBody = document.createElement("tBody");
		  table.appendChild(tBody);
		  var items = result.xmlDocument.getElementsByTagName("item");		  		                             
          for (var i = 0; i < items.length; i++) {
            var entry = items[i];            
            var tr = createRow(entry, i);
            tBody.appendChild(tr);                                  
          }          
          container.update(table);  
        }else{
          var container = $('feed');           
          container.update(result.error.message + ' ' + result.error.code);
        }
      });
    }    
        
    function createRow(entry, i){
      var tr = document.createElement("tr");
      var lnk = entry.getElementsByTagName("link")[0];
      var lnkValue = lnk.firstChild.nodeValue;
      var tdLink = createRSSLink(lnkValue, i);
      var tdTitle = createTD(entry, i);      
      tr.appendChild(tdLink);
      tr.appendChild(tdTitle);     
      return tr;
    }
    
    function createTD(entry){
      var title = entry.getElementsByTagName("title")[0];
      var titleValue = title.firstChild.nodeValue;
      
      var desc = entry.getElementsByTagName("description")[0];
      var descValue = "";
      try{
        descValue = desc.firstChild.nodeValue;
      }catch(e){}  
      
      var readSpan = document.createElement("span");           
      readSpan.appendChild(document.createTextNode(" [read this entry]"));
      
      var link = entry.getElementsByTagName("link")[0];
      var linkValue = link.firstChild.nodeValue;   
                           
      var lnk = document.createElement("a");
      lnk.setAttribute("href", linkValue);
      lnk.setAttribute("target", "_new");      
      lnk.setAttribute("title", titleValue);
      lnk.appendChild(readSpan);
      
      try{
        var pubDate = entry.getElementsByTagName("pubDate")[0];
        var pubDateValue = pubDate.firstChild.nodeValue;   
      }catch(e){
      }  
            
      var td = document.createElement("td");      
      //td.appendChild(document.createTextNode(pubDateValue.unescapeHTML()));
	  td.appendChild(document.createTextNode(titleValue.unescapeHTML()));
	  //td.appendChild(document.createTextNode(titleValue));
      td.appendChild(lnk);
      
      return td;
    }        
    
    function createLink(value){
      var link = document.createElement("a");
      link.href = value;
    }
    
    function createRSSLink(value, i){   
      var td = document.createElement("td");                   
      var img = document.createElement("img");
      img.src="img/TamTamyReader_ico.gif";         
      img.border=0;
      img.title=(i+1);
      var lnk = document.createElement("a");
      lnk.setAttribute("href", getLink(value));
      lnk.setAttribute("target", "_new");      
      lnk.appendChild(img);         
      td.appendChild(lnk);   
      return td;
    }
    
    function getLink(value){
      if(value.indexOf("blog/")>0){
        var bn = getName(value, "blog/");
        return "http://tamtamy.reply.it/blog/" + bn + "/feed";
      }else if(value.indexOf("content/")>0){
        var cn = getName(value, "content/");
        return "http://tamtamy.reply.it/tamtamy/content/" + cn;
      }else{
        return "#";
      }
    }
    
    function getName(value, pattern){      
      var idx = value.indexOf(pattern);
      var patternL = pattern.length;      
      var tmp = value.substring(idx+patternL);
      
      var slash = tmp.indexOf("/");
      if(slash>0){
        var blogName = tmp.substring(0, slash);      
        return blogName;
      }else{
        return tmp;
      }       
    }
    
    function doFeedLinks(){
      var url = "TamTamyRSS?https=true&";//?op=1";
   	    
      var feedLinks = "";
	  var feeds = [{link:'/blog/?wpmu-feed=posts', value:'RSS Posts'},
	   		       {link:'/blog/?wpmu-feed=comments', value:'RSS Comments'},
	      		   {link:'/blog/?wpmu-feed=pages', value:'RSS Blog Pages'},
	      	       {link:'/tamtamy/rss/rss.action', value:'RSS TamTamy'},
	      		   {link:'/tamtamy/rss/comments.action', value:'RSS TamTamy Comments'},
	      		   {link:'/blog/lifecoding/feed', value:'TamTamyHack Blog'}];
      for(var k=0; k<feeds.length; k++){
   	    params = '&usr='+$('usr').value+'&pwd='+$('pwd').value+'&feedurl='+feeds[k].link;   	    
   	    feedLinks += '<a href="'+url+params+'" target="_new" title="Subscribe to '+feeds[k].value+'"">'+feeds[k].value+'</a><img src="img/TamTamyReader_ico.gif"/>  |  ';
   	  }     	    
	  $('feedLinks').update(feedLinks);	  	  	 
	}
</script>

<script src="http://www.google-analytics.com/urchin.js" type="text/javascript">
</script>
<script type="text/javascript">
_uacct = "UA-2988083-1";
urchinTracker();
</script>

<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
  <div>
    <img src="img/TamTamyReader.gif">
  </div>
  <div>
  Inserisci il tuo <i>username</i> e la tua <i>password</i> per conoscere gli ultimi interventi pubblicati su TamTamy:
  </div>    
  <fieldset>
    <legend>.: TamTamy2GoogleReader :.</legend> 
    <span>
      <label for="usr">NomeUtente</label>&nbsp; 
      <input type="text" name="username" id="usr" value="" /> 
    </span> 
    <span> 
      <label for="pwd">Password</label>
	  <input type="password" name="password" id="pwd" value="" /> 
	</span> 
	<span>
	  <select id="results">
        <option value="10" selected="selected">10</option>
        <option value="20">20</option>
        <option value="30">30</option>
        <option value="40">40</option>
      </select>
	</span>  
	<span>
	  <label for="rss">RSS: </label>
	  <select id="rss">
	    <optgroup label="Blog">
	      <option value="http://tamtamy.reply.it/blog/?wpmu-feed=posts" selected="selected">RSS Blog Posts</option>
	      <option value="http://tamtamy.reply.it/blog/?wpmu-feed=comments">RSS Blog Comments</option>
	      <option value="http://tamtamy.reply.it/blog/?wpmu-feed=pages">RSS Blog Pages</option>
	    </optgroup>
	    <optgroup label="TamTamy">
	      <option value="http://tamtamy.reply.it/tamtamy/rss/rss.action">RSS TamTamy</option>
	      <option value="http://tamtamy.reply.it/tamtamy/rss/comments.action">RSS TamTamy Comments</option>
	    </optgroup>	    
	  </select>
	</span>
	<span> 	  
	   
	  <input type="button" name="grabFeed" id="grab" value="grab" onclick="grab()" /> 
	   
 	  <!-- input type="button" name="grabFeed" id="grab" value="grab" onclick="doFeedLinks()" /--> 
	</span>
  </fieldset>
  <br />
  <div id="feedLinks"></div>
  <br />
  <div id="tinyFeedLinks"></div>
  <br />
  <div id="feed"></div>
  <br/>
  <div id="log"></div>
</body>
</html>