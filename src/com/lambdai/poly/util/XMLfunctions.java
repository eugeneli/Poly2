package com.lambdai.poly.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

//Modified from: http://p-xr.com/android-tutorial-how-to-parseread-xml-data-into-android-listview/
public class XMLfunctions {
	
	public final static Document XMLfromString(String xml){
		
		Document doc = null;
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
        	
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml.replaceAll(" & "," &amp; ")));
	        doc = db.parse(is); 
	        
		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
            return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}
		       
        return doc;
        
	}
	
	/** Returns element value
	  * @param elem element (it is XML tag)
	  * @return Element value otherwise empty String
	  */
	 public final static String getElementValue( Node elem ) {
	     Node kid;
	     if( elem != null){
	         if (elem.hasChildNodes()){
	             for( kid = elem.getFirstChild(); kid != null; kid = kid.getNextSibling() ){
	                 if( kid.getNodeType() == Node.TEXT_NODE  ){
	                     return kid.getNodeValue();
	                 }
	             }
	         }
	     }
	     return "";
	 }
	 
		 
	 public static String getXML(String type){	 
			String line = null;

			try {
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = null;
				
				if(type.equals("events"))
				{
					httpPost = new HttpPost("http://www.poly.edu/poly_pages/api/events_month");
				}
				else if(type.equals("news"))
				{
					httpPost = new HttpPost("http://www.poly.edu/feed/news");
				}
				else if(type.equals("press"))
				{
					httpPost = new HttpPost("http://www.poly.edu/feed/press-releases");
				}
				else
				{
					throw new MalformedURLException();
				}
						
					
				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				line = EntityUtils.toString(httpEntity);
				
			} catch (UnsupportedEncodingException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (MalformedURLException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			} catch (IOException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			}catch (NullPointerException e) {
				line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			}

			return line;

	}
	 
	 public static int numResults(Document doc){		
		 //Node results = doc.getDocumentElement();
		 int res = -1;
	     try{
			 res = 10; //Get only 10 items since poly xml has no counter
			//res = Integer.valueOf(results.getAttributes().getNamedItem("count").getNodeValue());
			}catch(Exception e ){
			res = -1;
		}
			
		return res;
	}

	public static String getValue(Element item, String str) {		
		NodeList n = item.getElementsByTagName(str);	
		return XMLfunctions.getElementValue(n.item(0));
	}
}
