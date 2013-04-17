package edu.uga.cs.lumina.discovery.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import edu.uga.radiant.util.DataManager;
import edu.uga.radiant.util.QueryManager;

public class WSDLSystem {
	
	private Connection conn;
	
	public WSDLSystem(Connection connection){
		conn = connection;
	}
	
	public ArrayList<String> search(String keyword) throws URISyntaxException, IOException, SQLException{
		
		ArrayList<String> result = QueryManager.search(conn, keyword);
		return result;
		
	}
	
	public String saveWSDL(URLlink link){
		
		String result = "";
		boolean error = false;
		
		try {
			
			// save it to a file:
            XMLOutputter outputer = new XMLOutputter();
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            // get xml document
            SAXBuilder builder = new SAXBuilder();
            URL wsURL = new URL(link.getWsLocation());
	        URLConnection ws = wsURL.openConnection();
	        ws.setReadTimeout(3000);
			Document document = (Document) builder.build(new InputStreamReader(ws.getInputStream()));
			
			// write to byte array
			outputer.output(document, out);
			
			// store in database
			String xml = new String(out.toByteArray(), "utf-8");
			String newMD5 = generateMD5(xml);
			
			long id = QueryManager.getWSDLID(conn, link.getName(), "wsdl");
			if (id != 0){
				boolean validate = QueryManager.checkValidate(conn, id);
				String md5 = QueryManager.getMD5(conn, id);
				if (!newMD5.equals(md5)){
					validate = DataManager.updateWSDL(conn, id, xml, newMD5);
					if (validate == false) result = result + "validate:" + validate;
				}else{
					result = "In database, validate:" + validate;
				}
			}else{
				boolean validate = DataManager.storeWSDL(conn, link.getName(), "wsdl", xml, newMD5);
				result = result + "validate:" + validate;
			}
			
			out.close();
			return result;
			
		} catch (Exception e) {// Catch exception if any
			error = true;
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}

		// use backup url
		if (error == true){
			
			try {
				
				// save it to a file:
	            XMLOutputter outputer = new XMLOutputter();

				ByteArrayOutputStream out = new ByteArrayOutputStream();
				
	            // get xml document
	            SAXBuilder builder = new SAXBuilder();
	            URL wsURL = new URL(link.getBackupWSDLURL());
		        URLConnection ws = wsURL.openConnection();
				Document document = (Document) builder.build(new InputStreamReader(ws.getInputStream()));
				
				// write to byte array
				outputer.output(document, out);
	            
				// store in database
				String xml = new String(out.toByteArray(), "utf-8");
				String newMD5 = generateMD5(xml);
				
				long id = QueryManager.getWSDLID(conn, link.getName(), "wsdl");
				if (id != 0){
					boolean validate = QueryManager.checkValidate(conn, id);
					String md5 = QueryManager.getMD5(conn, id);
					if (!newMD5.equals(md5)){
						validate = DataManager.updateWSDL(conn, id, xml, newMD5);
						if (validate == false) result = result + "validate:" + validate;
					}else{
						result = "In database, validate:" + validate;
					}
				}else{
					boolean validate = DataManager.storeWSDL(conn, link.getName(), "wsdl", xml, newMD5);
					result = result + "validate:" + validate;
				}
				
				out.close();
				return result;
				
			} catch (Exception e) {// Catch exception if any
				e.printStackTrace();
				System.err.println("Error in loading : " + e.getMessage());
				return "Error in loading : " + e.getMessage();
			}
			
		}
		
		return result;
	}
	

	public String saveSAWSDL(URLlink link){
		
		String result = "";
		try {
			
			// save it to a byte array:
            XMLOutputter outputer = new XMLOutputter();
            
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            // get xml document
            SAXBuilder builder = new SAXBuilder();
            URL wsURL = new URL(link.getSAWSDLURL());
	        URLConnection ws = wsURL.openConnection();
	        ws.setReadTimeout(5000);
	        Document document = (Document) builder.build(new InputStreamReader(ws.getInputStream()));
			
			// write to byte array
			outputer.output(document, out);
            
			// store in database
			String xml = new String(out.toByteArray(), "utf-8");
			String newMD5 = generateMD5(xml);
			
			long id = QueryManager.getWSDLID(conn, link.getName(), "sawsdl");
			if (id != 0){
				boolean validate = QueryManager.checkValidate(conn, id);
				String md5 = QueryManager.getMD5(conn, id);
				if (!newMD5.equals(md5)){
					validate = DataManager.updateWSDL(conn, id, xml, newMD5);
					if (validate == false) result = result + "validate:" + validate;
				}else{
					result = "In database, validate:" + validate;
				}
			}else{
				boolean validate = DataManager.storeWSDL(conn, link.getName(), "sawsdl", xml, newMD5);
				result = result + "validate:" + validate;
			}
			
			out.close();
			return result;
			
		} catch (Exception e) {// Catch exception if any
			e.printStackTrace();
			result = "Error in loading : " + e.getMessage();
			System.err.println("Error in loading : " + e.getMessage());
		}

		return result;
		
	}
	
	public static String generateMD5(String xml) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		
		byte[] defaultBytes = xml.getBytes("utf-8");
		MessageDigest algorithm = MessageDigest.getInstance("MD5");
		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < messageDigest.length; i++) {
			hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
		}
		return hexString.toString();
		
	}
	
	public static void main(String args[]) {
		
	}
}