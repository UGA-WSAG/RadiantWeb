/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uga.cs.lumina.discovery.util;

import edu.uga.cs.wstool.parser.sawsdl.MessageOBJ;
import edu.uga.cs.wstool.parser.sawsdl.SimpleTypeOBJ;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;

import edu.uga.radiant.ontology.OntologyManager;
import edu.uga.radiant.util.DataBaseConnection;
import edu.uga.radiant.util.QueryManager;
import static java.util.Arrays.deepToString;

/**
 *
 * @author Yung-Long Li
 */
public class WsdlManager {

	private static boolean DEBUG = false;
	private OntologyManager discoveryMgr;
	private String  owlURI;
	private DataBaseConnection dbcn = new DataBaseConnection();
	private Connection connection = dbcn.getConnection();
	
	public WsdlManager(OntologyManager _discoveryMgr, String _owlURI){
		discoveryMgr = _discoveryMgr;
		owlURI = _owlURI;
	} // constructor
		
	public WsOpReader createRequest(String _ontologyName, String _serviceName, String _serviceDoc, String _serviceURI, String _operationName, String _functionalityDes, String _functionality,
			MessageOBJ _input, MessageOBJ _output) {
		WsOpReader request = null;
		request = new WsOpReader(_serviceDoc, _operationName, _functionalityDes, _functionality, _input, _output);
		return request;
	}

	public double getOpSemanticSimilarity(WsOpReader request, WsOpReader service) throws Exception {
		return SawsdlSimilarity.oPSim(request, service, owlURI);
	}
	
	public double getOpSyntaticSimilarity(WsOpReader request, WsOpReader service) throws Exception {
		return WsdlSimilarity.oPSim(request, service);
	}

	public HashMap<String, HashMap<String, Double>> compareSAWSDLServices(WsOpReader request, double threshold, String filePath) throws Exception {
		
		// get sawsdl list
		ArrayList<String> sawsdlfileNames = QueryManager.getSAWSDLList(connection);
		
		if (DEBUG) System.out.println();
		
		// read the SAWSDL or SAWADL file
		HashMap<String, HashMap<String, Double>> Result = new HashMap<String, HashMap<String, Double>>();
		
		if ((sawsdlfileNames != null) && (sawsdlfileNames.size() != 0)){
			for (String name : sawsdlfileNames) {
				//if (file.endsWith(".sawsdl")) {    // check if it is sawsdl
					List<WsOpReader> WsOpearations = new ArrayList<WsOpReader>();
					try {
						if (DEBUG) System.out.println("reading the " + name + "...");
						WsOpearations = WsOpReader.getInstance(connection, discoveryMgr, name, "sawsdl");
					} catch (Exception ex) {
						if (DEBUG) System.out.println("\n Invalid WSDL : " + name + "\n\n");
						ex.printStackTrace();
						continue;
					}
					if (DEBUG) System.out.println("");
					
					// get Semantic Similarity score
					if (DEBUG) System.out.println("Starting to compare the concept ..."); 
					HashMap<String, Double> semanticResult = new HashMap<String, Double>();
					for (int i = 0; i < WsOpearations.size(); i++) {
						WsOpReader current_service = WsOpearations.get(i);
						if (DEBUG) System.out.println("   the " + (i + 1) + "th service :" + current_service.getOperationName());
						if (DEBUG) System.out.println("      get similarity score ...");
						double score = getOpSemanticSimilarity(request, current_service);
						if(score >= threshold) semanticResult.put(current_service.getOperationName(), new Double(score));
						if (DEBUG) System.out.println("      Score : " + score + "\n");
					}
					
					HashMap<String, HashMap<String, Double>> semantic = new HashMap<String, HashMap<String, Double>>();
					semantic.put(name, semanticResult);
					Result.putAll(semantic);
					
				//}
				//else if (file.endsWith(".sawadl")) {   // read SAWADL file
				//	String wadlURL = basedir + file;
				//	List<WsOpReader> adv = null;
				//	try {
				//		adv = WsOpReader.readSAWADL(discoveryMgr, wadlURL);
				//	} catch (Exception ex) {
				//		System.out.println("\n Invalid WADL : "+file+"\n\n");
				//		continue;
				//		//System.out.println(ex.getMessage());
				//		//return null;
				//	}
				//
				//	HashMap<String, Double> semanticResult = new HashMap<String, Double>();
				//	for (int i = 0; i < adv.size(); i++) {
				//		WsOpReader current_service = adv.get(i);
				//		double score = getOpSemanticSimilarity(request, current_service);
				//		if(score >= threshold)
				//			semanticResult.put(current_service.getOperationName(), new Double(score));
				//		//System.out.println("Score : " + score);
				//	}
				//	HashMap<String, HashMap<String, Double>> semantic = new HashMap<String, HashMap<String, Double>>();
				//	semantic.put(file, semanticResult);
				//	Result.putAll(semantic);
				//}
			}
		}
		
		return Result;
	}
	
	public HashMap<String, HashMap<String, Double>> compareWSDLServices(WsOpReader request, double threshold, String filePath) throws Exception {
		
		// get wsdl list
		ArrayList<String> wsdlfileNames = QueryManager.getWSDLList(connection);
		
		if (DEBUG) System.out.println();
		
		// read the WSDL file
		HashMap<String, HashMap<String, Double>> Result = new HashMap<String, HashMap<String, Double>>();
		
		if ((wsdlfileNames != null) && (wsdlfileNames.size() != 0)){
			for (String name : wsdlfileNames) {
				//if (file.endsWith(".wsdl")) {    // check if it is sawsdl
					List<WsOpReader> WsOpearations = null;
					try {
						if (DEBUG) System.out.println("reading the " + name + "...");
						WsOpearations = WsOpReader.getInstance(connection, discoveryMgr, name, "wsdl");
					} catch (Exception ex) {
						if (DEBUG) System.out.println("\n Invalid WSDL : " + name + "\n\n");
						ex.printStackTrace();
						continue;
					}
					
					// get Syntactic Similarity score
					if (DEBUG) System.out.println("Starting to compare the text ..."); 
					HashMap<String, Double> syntaticResult = new HashMap<String, Double>();
					for (int i = 0; i < WsOpearations.size(); i++) {
						WsOpReader current_service = WsOpearations.get(i);
						if (DEBUG) System.out.println("   the " + (i + 1) + "th service :" + current_service.getOperationName());
						if (DEBUG) System.out.println("      get similarity score ...");
						double score = getOpSyntaticSimilarity(request, current_service);
						if(score >= threshold) syntaticResult.put(current_service.getOperationName(), new Double(score));
						if (DEBUG) System.out.println("      Score : " + score + "\n");
					}
					
					HashMap<String, HashMap<String, Double>> syntatic = new HashMap<String, HashMap<String, Double>>();
					syntatic.put(name, syntaticResult);
					Result.putAll(syntatic);
					
				//}
				//else if (file.endsWith(".wadl")) {   // read SAWADL file
				//	String wadlURL = basedir + file;
				//	List<WsOpReader> adv = null;
				//	try {
				//		adv = WsOpReader.readSAWADL(discoveryMgr, wadlURL);
				//	} catch (Exception ex) {
				//		System.out.println("\n Invalid WADL : "+file+"\n\n");
				//		continue;
				//		//System.out.println(ex.getMessage());
				//		//return null;
				//	}
				//
				//	HashMap<String, Double> semanticResult = new HashMap<String, Double>();
				//	for (int i = 0; i < adv.size(); i++) {
				//		WsOpReader current_service = adv.get(i);
				//		double score = getOpSemanticSimilarity(request, current_service);
				//		if(score >= threshold)
				//			semanticResult.put(current_service.getOperationName(), new Double(score));
				//		//System.out.println("Score : " + score);
				//	}
				//	HashMap<String, HashMap<String, Double>> semantic = new HashMap<String, HashMap<String, Double>>();
				//	semantic.put(file, semanticResult);
				//	Result.putAll(semantic);
				//}
			}
		}
		
		return Result;
	}
	
	public static void main(String args[]) throws Exception {

		/*
		// save WSDL file to existDB
		String filePath1 = "D:/workspace/Java/Discovery/lib/downloadedWSDL/WSDbfetchDoclit.wsdl";
		String filePath2 = "D:/workspace/Java/Discovery/lib/downloadedWSDL/wublast.wsdl";
        String[] files = {filePath1};
		saveToDatabase(files);

		// delete WSDL file from existDB
		String fileName = "WSDbfetchDoclit.wsdl";
        deleteFromDatabase(fileName);
		*/
		
		// define ontologyName
		String _ontologyName = "edam_v12.owl";
		if (DEBUG) System.out.println("Accessing the " + _ontologyName + " ontology\n");

		// create discovery manager
		OntologyManager discoveryMgr = null;
		discoveryMgr = new OntologyManager(_ontologyName);

		
		// create mwsdi model
		WsdlManager wsdlmgr = new WsdlManager(discoveryMgr, _ontologyName);
		
		
		// define request Name
		String _requestName = "test";
		if (DEBUG) System.out.println("request service name = " + _requestName + "\n");
		
		String _requestDoc = "";
		
		String _requestURI = null;
		
		
		// define operation Name
		String _operationName = "getStatus";
        if (DEBUG) System.out.println("Operation Name = " + _operationName + "\n");
        
        
        // define functionality
		String _functionality = "http://purl.org/obo/owl/EDAM#EDAM_0000493";
		if (DEBUG) System.out.println("functionality = " + _functionality + "\n");
		
		// define functionality
		String _functionalityDes = "";
		
		// define input Parameter in MessageOBJ 
		String[] _inParameter = {"run"};
		if (DEBUG) System.out.println("inParameter:" + deepToString(_inParameter));
		String[] _inputs = {"http://purl.org/obo/owl/EDAM#EDAM_0001676"};
		if (DEBUG) System.out.println("inputs" + deepToString(_inputs));
		String[] _inputsDes = {""};
		String[] _inputTypes = {"sequence"};
		if (DEBUG) System.out.println("input Types:" + deepToString(_inputTypes) + "\n");
		MessageOBJ in = new MessageOBJ();
		List<SimpleTypeOBJ> simples = new ArrayList<SimpleTypeOBJ>();
		for (int i = 0; i < _inParameter.length; i++){
			SimpleTypeOBJ obj = new SimpleTypeOBJ();
			obj.setName(_inParameter[i]);
			obj.setModelReference(IRI.create(_inputs[i]));
			obj.setDescription(_inputsDes[i]);
			obj.setType(_inputTypes[i]);
			simples.add(obj);
		}
		in.setSimpletype(simples);
		
		
		//define output Parameter  in MessageOBJ 
		String[] _outParameter = {"runResponse"};
		if (DEBUG) System.out.println("outParameter:" + deepToString(_outParameter));
		String[] _outputs  = {"http://purl.org/obo/owl/EDAM#EDAM_0001675"};
		if (DEBUG) System.out.println("outputs :" + deepToString(_outputs) );
		String[] _outputsDes = {""};
		String[] _outputTypes = {"sequence"};
		if (DEBUG) System.out.println("_outputTypes:" + deepToString(_outputTypes) + "\n");
		MessageOBJ out = new MessageOBJ();
		simples = new ArrayList<SimpleTypeOBJ>();
		for (int i = 0; i < _inParameter.length; i++){
			SimpleTypeOBJ obj = new SimpleTypeOBJ();
			obj.setName(_outParameter[i]);
			obj.setModelReference(IRI.create(_outputs[i]));
			obj.setDescription(_outputsDes[i]);
			obj.setType(_outputTypes[i]);
			simples.add(obj);
		}
		out.setSimpletype(simples);
		
		
		// create request template
		if (DEBUG) System.out.println("create request template ...");
		WsOpReader request = wsdlmgr.createRequest(_ontologyName, _requestName, _requestDoc, _requestURI, _operationName, _functionalityDes, _functionality, in, out);
		if (DEBUG) System.out.println();
		
		// find the services whose score is more than threshold
		if (DEBUG) System.out.println("Starting to Search Web Service in database ...");
		HashMap<String, HashMap<String, Double>> Result = wsdlmgr.compareSAWSDLServices(request, 0.0, "");
			
		// get the service name and score
		Iterator<String> iter = Result.get(0).keySet().iterator();
		if (DEBUG) System.out.println("result:");
		while (iter.hasNext()) {
			String service = iter.next();
			if (DEBUG) System.out.println("service name = " + service);
			HashMap<String, Double> opResult = Result.get(service);
			Iterator<String> opIter = opResult.keySet().iterator();
			while (opIter.hasNext()) {
				String operation = opIter.next();
				double score = opResult.get(operation);
				if (DEBUG) System.out.println("   " + operation + ": " + score);
			}
			if (DEBUG) System.out.println();
		}
		if (DEBUG) System.out.println("Done");
	}
}
