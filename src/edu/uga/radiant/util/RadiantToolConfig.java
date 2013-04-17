/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uga.radiant.util;

import java.io.*;
import java.net.URI;
import java.util.Properties;

/**
 *
 * @author Chaitu
 */
public class RadiantToolConfig {

	private static String propertyFile;
	
    private static String dbDriver;

    private static String dbUrl;

    private String dbName;

    private String dbUserId;

    private String dbPassword;
    
    private String operationConcept;
    
    private String operationConcept2;

    private static String parameterConcept;

    private static String definitionURI;
    
    private static String descriptionURI;

    private static String wsdlUrl;
    
    private static String updatedWSDLLocation;
    
    private static String WADLLocation;
    
    private String updatedWADLLocation;
    
    private static String existConfiguration;
    
    private static String stopwordsPath;
    
    public String getStopWordsPath(){
        return stopwordsPath;
    }
    
    public String getDescriptionURI(){
        return descriptionURI;
    }
    
    public String getExistConfiguration(){
        return existConfiguration;
    }

    public String getWsdlUrl(){
        return wsdlUrl;
    }

    public void setWsdlUrl(String url){
        wsdlUrl=url;
    }

    public String getDefinitionURI(){
        return definitionURI;
    }

    public String getOperationConcept(){
    	return operationConcept;
    }

    public String getParameterConcept(){
        return parameterConcept;
    }

    public static String getDbDriver()
    {
        return dbDriver;
    }
    public static String getDbUrl()
    {
        return dbUrl;
    }
    public String getDbUserId()
    {
        return dbUserId;
    }
    public String getDbPassword()
    {
        return dbPassword;
    }
    public String getDbName()
    {
        return dbName;
    }
    
    public String getUpdateWSDLLocation(){
        return updatedWSDLLocation;
    }
    
    public void setExistConfiguration(String config){
        RadiantToolConfig.existConfiguration = config;
    }
            
            
    public void setUpdateWSDLLocation(String location){
        RadiantToolConfig.updatedWSDLLocation = location;
    }
    
    public void setOperationConcept(String operationConcept){
        this.operationConcept = operationConcept;
    }
    
    public void setParameterConcpet(String parameterConcept){
        this.operationConcept = parameterConcept;
    }
    
    public void setDbUrl(String dbUrl)
    {
        RadiantToolConfig.dbUrl = dbUrl;
    }
    
    public void setDbName(String _dbName)
    {
        dbName = _dbName;
    }
    
    public void setDbUserId(String _dbUserId)
    {
        dbUserId = _dbUserId;
    }
    
    public void setDbPassword(String _dbPassword)
    {
        dbPassword = _dbPassword;
    }
    
    public void setDbDriver(String dbDriver)
    {
        RadiantToolConfig.dbDriver = dbDriver;
    }
    
    public void setDefinitionURI(String definitionURI){
        RadiantToolConfig.definitionURI = definitionURI;
    }
    
    public void setDescriptionURI(String descriptionURI){
        RadiantToolConfig.descriptionURI = descriptionURI;
    }
    
    public void setUpdatedWADLLocation(String updatedWADLLocation) {
		this.updatedWADLLocation = updatedWADLLocation;
	}

	public String getUpdatedWADLLocation() {
		return updatedWADLLocation;
	}
	
	public void setWADLLocation(String wADLLocation) {
		WADLLocation = wADLLocation;
	}

	public String getWADLLocation() {
		return WADLLocation;
	}


    public RadiantToolConfig(){//String property){
        
    	try{
    		
    		String url = RadiantToolConfig.class.getResource("").toString();
    		propertyFile = url + "RadiantProperties.properties";
    		
    		File file = new File(new URI(propertyFile));
        	Properties prop = new Properties();
        	FileInputStream in = new FileInputStream(file);
        	prop.load(in);
        	
        	dbName = prop.getProperty("db.name");
        	dbUserId = prop.getProperty("db.userId");
        	dbPassword = prop.getProperty("db.password");
        	dbDriver = prop.getProperty("db.driver");
        	operationConcept = prop.getProperty("operation.concept");
        	operationConcept2 = prop.getProperty("operation.concept2");
        	parameterConcept = prop.getProperty("parameter.concept");
        	definitionURI = prop.getProperty("definition.uri");
        	wsdlUrl = prop.getProperty("wsdl.url");
        	updatedWSDLLocation = prop.getProperty("updatedwsdl.location");
        	WADLLocation = prop.getProperty("wadl.location");
        	setUpdatedWADLLocation(prop.getProperty("updatedwadl.location"));
        	existConfiguration=prop.getProperty("exist.configuraion");
        	descriptionURI=prop.getProperty("description.uri");
        	//stopwordsPath=prop.getProperty("stopword.path");
        	String fullFileBase = RadiantToolConfig.class.getResource("").toString();
        	stopwordsPath = fullFileBase + "stop.txt";
        	
        	in.close();
        	
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	System.out.println( "Property-Value pair could not be added\nPlease check if the .properties file exists at the said path..!!");
        }
    }
    
    //Sets the Property - value pair for the values passed to it
    //Returns Status: Successfully added / Not added
    public String setValuesToProperty(String property,String value){
    
    	try
        {
        	//Creating an object of java.util.Properties
        	Properties prop = new Properties();
        	//Loading the properties file as a Input file
        	FileInputStream in = new FileInputStream(propertyFile);
        	prop.load(in);
        	in.close();
        	//Uses SetProperty method defined in java.util.Properties to add property(key)-Value pair,
        	//both of them strings to the object of type Properties created.
        	prop.setProperty(property, value);
        	//Stores the modified object to the properties file
        	FileOutputStream out = new FileOutputStream(propertyFile);
        	prop.store(out, null);
        	out.close();
        	return "Property-Value pair successfully added..!!";
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	return "Property-Value pair could not be added\nPlease check if the .properties file exists at the said path..!!";
        }
    }

    public static void main(String args[]){
    	
    	RadiantToolConfig config = new RadiantToolConfig();
    	
    	System.out.println("wadl = " + config.getUpdatedWADLLocation());
    	System.out.println("wadl = " + config.getWADLLocation());
    	
    }

	public void setOperationConcept2(String operationConcept2) {
		this.operationConcept2 = operationConcept2;
	}

	public String getOperationConcept2() {
		return operationConcept2;
	}

	
	
    
}
