package com.viteos.veda.master.lib;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;




public class XMLLibrary {

	private static boolean bCreateFile;
	private static boolean bMgCreateFile;
	private static boolean bInceFeeCreateFile;
	private static boolean bIvAccountCreateFile;	
	private static boolean bTradeTypeSUBCreateFile;
	private static boolean bFeeDistributionCreateFile;
	private static boolean bOpeningBalanceDetailsCreateFile;
	private static boolean bFeederSUBAccountCreateFile;
	private static boolean bFeederREDAccountCreateFile;
	private static boolean bTradeTypeREDDetailsCreateFile;
	private static boolean bTradeTypeTRANDetailsCreateFile;
	private static boolean bTradeTypeEXCNDetailsCreateFile;
	private static boolean bTradeTypeSWITCHDetailsCreateFile;
	private static boolean bTradeTypeSPSUBDetailsCreateFile;
	private static boolean bTradeTypeSPREDDetailsCreateFile;
	private static String sPathSeparatorChar = java.io.File.separator;

	public static String sFundSetupXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"Parameters.xml";
	public static String sManagementXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"Management.xml";
	public static String sIncentiveXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"IncentiveFee.xml";
	public static String sInvestorAccountXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"InvestorAccount.xml";
	public static String sTradeTypeSUBXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"TradeTypeSUB.xml";
	public static String sFeeDistributionXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"FeeDistribution.xml";
	public static String sFeederSUBAccountXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"FeederSUBAccount.xml";
	public static String sFeederREDAccountXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"FeederREDAccount.xml";
	public static String sOpeningBalanceDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"OpeningBalanceDetails.xml";
	public static String sTradeTypeREDDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"TradeTypeREDDetails.xml";
	public static String sTradeTypeTRANDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"TradeTypeTRANDetails.xml";
	public static String sTradeTypeEXCNDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"TradeTypeEXCNDetails.xml";
	public static String sTradeTypeSWITCHDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"TradeTypeSWITCHDetails.xml";
	public static String sTradeTypeSPSUBDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"TradeTypeSPSUBDetails.xml";
	public static String sTradeTypeSPREDDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar +"TradeTypeSPREDDetails.xml";

	private static Document doc;
	private static Document Mgdoc;
	private static Document Incedoc;
	private static Document IvAccountdoc;
	private static Document TradeTypeSUBdoc;
	private static Document FeeDistributiondoc;
	private static Document FeederSUBAccountdoc;
	private static Document FeederREDAccountdoc;
	private static Document OpeningBalanceDetailsdoc;
	private static Document TradeTypeREDDetailsdoc;
	private static Document TradeTypeTRANDetailsdoc;
	private static Document TradeTypeEXCNDetailsdoc;
	private static Document TradeTypeSWITCHDetailsdoc;
	private static Document TradeTypeSPSUBDetailsdoc;
	private static Document TradeTypeSPREDDetailsdoc;


	private static Element rootElement = null;
	private static Element MgrootElement = null;
	private static Element IncerootElement = null;
	private static Element INAcountrootElement = null;
	private static Element TradeTypeSUBrootElement = null;
	private static Element FeeDistributionrootElement = null;
	private static Element FeederSUBAccountrootElement = null;
	private static Element FeederREDAccountrootElement = null;
	private static Element OpeningBalanceDetailsrootElement = null;
	private static Element TradeTypeREDDetailsrootElement = null;
	private static Element TradeTypeTRANDetailsrootElement = null;
	private static Element TradeTypeEXCNDetailsrootElement = null;
	private static Element TradeTypeSWITCHDetailsrootElement = null;
	private static Element TradeTypeSPSUBDetailsrootElement = null;
	private static Element TradeTypeSPREDDetailsrootElement = null;

	private static Element parameterElement;
	private static Element ManagementElement;
	private static Element IncentiveElement;
	private static Element InvestorAccountElement;
	private static Element TradeTypeSUBElement;
	private static Element FeeDistributionElement;
	private static Element FeederSUBAccountElement;
	private static Element FeederREDAccountElement;
	private static Element OpeningBalanceDetailsElement;
	private static Element TradeTypeREDDetailsElement;
	private static Element TradeTypeTRANDetailsElement;
	private static Element TradeTypeEXCNDetailsElement;
	private static Element TradeTypeSWITCHDetailsElement;
	private static Element TradeTypeSPSUBDetailsElement;
	private static Element TradeTypeSPREDDetailsElement;

	//private static Element clientElement;
	//private static Element fundFamilyElement;
	//private static Element legalEntityElement;
	//private static Element SeriesElement;


	public static boolean writeParametersToXML(Map<String, String> parameterDetails){
		try{
			createResultfolder(sFundSetupXMLFilePath);
			openXMLFile();
			addParameterDetails(parameterDetails,sFundSetupXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	//Xml for InvestorAccount
	public static boolean writeInvestorToXML(Map<String, String> investorAccountDetails){
		try{
			createResultfolder(sInvestorAccountXMLFilePath);
			openInvestorAccountXMLFile();
			addInvestorAccountDetails(investorAccountDetails,sInvestorAccountXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	//xml for FeeDistribution	
	public static boolean writeFeeDistributionToXML(Map<String, String> feeDistributionDetails){
		try{
			createResultfolder(sFeeDistributionXMLFilePath);
			openFeeDistributionXMLFile();
			addFeeDistributionDetails(feeDistributionDetails,sFeeDistributionXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}	

	//xml for management
	public static boolean writeManagementDetailsToXML(Map<String, String> ManagementDetails){
		try{
			createResultfolder(sManagementXMLFilePath);
			openManagementXMLFile();
			addManagementDetails(ManagementDetails,sManagementXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	} 

	private static void createResultfolder(String sFilePath) {
		setFilePropertyToWritable(sFilePath);
		if ((new File(Global.sXMLTestFolderPath)).exists()) {
			return;
		}
		(new File(Global.sXMLTestFolderPath)).mkdir();
	}

	private static void openXMLFile() {
		if (!bCreateFile) {
			bCreateFile = true;
			String sMessageFileName =  "Parameters.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sFundSetupXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sFundSetupXMLFilePath = sFundSetupXMLFilePath + sMessageFileName;
			createXMLFile(sFundSetupXMLFilePath);
		}
	}

	//Open xml file for InvestorAccout Master
	private static void openInvestorAccountXMLFile() {
		if (!bIvAccountCreateFile) {
			bIvAccountCreateFile = true;
			String sMessageFileName =  "InvestorAccount.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sInvestorAccountXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sInvestorAccountXMLFilePath = sInvestorAccountXMLFilePath + sMessageFileName;
			createInvestorAccoutXMLFile(sInvestorAccountXMLFilePath);
		}
	}

	//Open xnl file for FeeDistribution
	private static void openFeeDistributionXMLFile() {
		if (!bFeeDistributionCreateFile) {
			bFeeDistributionCreateFile = true;
			String sMessageFileName =  "FeeDistribution.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sFeeDistributionXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sFeeDistributionXMLFilePath = sFeeDistributionXMLFilePath + sMessageFileName;
			createFeeDistributionXMLFile(sFeeDistributionXMLFilePath);
		}
	}	

	private static void openManagementXMLFile() {
		if (!bMgCreateFile) {
			bMgCreateFile = true;
			String sMessageFileName =  "Management.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sManagementXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sManagementXMLFilePath = sManagementXMLFilePath + sMessageFileName;
			createManangementXMLFile(sManagementXMLFilePath);
		}
	}

	private static void createXMLFile(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			doc = docBuilder.newDocument();
			rootElement = doc.createElement("parameters");
			doc.appendChild(rootElement);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sfile));
			transformer.transform(source, result);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void createInvestorAccoutXMLFile(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sInvestorAccountXMLFilePath).exists()){
				IvAccountdoc = docBuilder.parse(sInvestorAccountXMLFilePath);
				INAcountrootElement = IvAccountdoc.getDocumentElement();
			}else{
				IvAccountdoc = docBuilder.newDocument();
				INAcountrootElement = IvAccountdoc.createElement("Accounts");
				IvAccountdoc.appendChild(INAcountrootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(IvAccountdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	//Create FeeDistribution Xml File	
	private static void createFeeDistributionXMLFile(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			FeeDistributiondoc = docBuilder.newDocument();
			FeeDistributionrootElement = FeeDistributiondoc.createElement("FeeDistribution");
			FeeDistributiondoc.appendChild(FeeDistributionrootElement);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(FeeDistributiondoc);
			StreamResult result = new StreamResult(new File(sfile));
			transformer.transform(source, result);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}	

	private static void createManangementXMLFile(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			if(new File(sManagementXMLFilePath).exists()){
				Mgdoc = docBuilder.parse(sManagementXMLFilePath);
				MgrootElement = Mgdoc.getDocumentElement();
			}else{
				Mgdoc = docBuilder.newDocument();
				MgrootElement = Mgdoc.createElement("ManagementFeeDetails");
				Mgdoc.appendChild(MgrootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(Mgdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void addParameterDetails(Map<String, String> objParameterMap,String sXMLFilePath) {
		String wrongvalue ="";
		try {
			parameterElement = doc.createElement("parameter");
			rootElement.appendChild(parameterElement);


			for (Entry<String, String> test1 : objParameterMap.entrySet()) {
				// setting attributes
				if(test1.getKey().equalsIgnoreCase("Comments")){
					continue;
				}
				try{
					wrongvalue = test1.getKey();
					parameterElement.setAttribute(test1.getKey(),test1.getValue());
				}
				catch(Exception e){
					System.out.println("check theses "+wrongvalue);
				}
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//Add InvestorAccountDetails
	private static void addInvestorAccountDetails(Map<String, String> objParameterMap,String sXMLFilePath) {
		try {
			InvestorAccountElement = IvAccountdoc.createElement("InvestorAccount");
			INAcountrootElement.appendChild(InvestorAccountElement);

			InvestorAccountElement.setAttribute("TestcaseName", objParameterMap.get("TestcaseName"));
			InvestorAccountElement.setAttribute("AccountID", objParameterMap.get("AccountID"));

			/*for (Entry<String, String> test1 : objParameterMap.entrySet()) {
				// setting attributes
				if(test1.getKey().equalsIgnoreCase("Comments")){
					continue;
				}
				try{
					wrongvalue = test1.getKey();
					InvestorAccountElement.setAttribute(test1.getKey(),test1.getValue());
				}
				catch(Exception e){
					System.out.println("check theses "+wrongvalue);
				}
			}*/

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(IvAccountdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	//Add FeeDistribution Details
	private static void addFeeDistributionDetails(Map<String, String> objParameterMap,String sXMLFilePath) {
		try {
			FeeDistributionElement = FeeDistributiondoc.createElement("FeeDistribution");
			FeeDistributionrootElement.appendChild(FeeDistributionElement);

			FeeDistributionElement.setAttribute("TestcaseName", objParameterMap.get("TestcaseName"));
			FeeDistributionElement.setAttribute("FeeDistributionID", objParameterMap.get("FeeDistributionID"));


			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(FeeDistributiondoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	private static void addManagementDetails(Map<String, String> objParameterMap,String sXMLFilePath) {
		try {

			ManagementElement = Mgdoc.createElement("ManagementFee");
			MgrootElement.appendChild(ManagementElement);

			ManagementElement.setAttribute("TestcaseName", objParameterMap.get("TestcaseName"));
			ManagementElement.setAttribute("ManagementFeeID", objParameterMap.get("ManagementFeeID"));
			ManagementElement.setAttribute("MakerStatus", objParameterMap.get("MakerStatus"));
			ManagementElement.setAttribute("CheckerStatus", objParameterMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(Mgdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*	public static Map<String, String> getParameterDataFromXML(String sXMLFilePath,String sTestcaseName){	
		String sXpath = "//parameter[@TestcaseName='testcasename']";
		sXpath = sXpath.replace("testcasename", sTestcaseName);
		Map<String,Map<String,String>> mapDetails = getDetailsMap(sXMLFilePath,sXpath);
		if(mapDetails.size()==0){
			return null;
		}
		return  mapDetails.get("Row0");
	}
	 */

	public static Map<String,Map<String,String>> getDetailsMap(String sXMLFilePath,String sExpression){

		NodeList nodeList;
		Map<String,Map<String,String>> objMainMap = new HashMap<String,Map<String,String>>();
		try {
			FileInputStream file = new FileInputStream(new File(sXMLFilePath));

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(file);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = sExpression;
			nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
			for(int iOutLoopCounter =0;iOutLoopCounter<nodeList.getLength();iOutLoopCounter++){	
				NamedNodeMap nm = nodeList.item(iOutLoopCounter).getAttributes();
				Map<String,String> objMsgDts = new HashMap<String,String>();
				for(int iInnerLoopCounter =0; iInnerLoopCounter< nm.getLength();iInnerLoopCounter++){
					String sKey = nm.item(iInnerLoopCounter).getNodeName();
					String sValue = nm.item(iInnerLoopCounter).getNodeValue();
					objMsgDts.put(sKey, sValue);
				}
				objMainMap.put("Row" + iOutLoopCounter,objMsgDts);
			}			
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
		return objMainMap;
	}

	public static boolean writeParametersForCreatedRulesToXML(Map<String, String> FundFamilyDetails){
		try{
			String Xpath = "//parameters";
			movePointerToElement(sFundSetupXMLFilePath,Xpath);
			addParameterDetails(FundFamilyDetails,sFundSetupXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	//To Read the XML Details from each master based on tag name.
	private static void movePointerToElement(String sXMLFilePath,String Xpath) {
		try {
			FileInputStream file = new FileInputStream(new File(sXMLFilePath));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			//Document document = builder.parse(file);
			//docBuilder = docFactory.newDocumentBuilder();
			//docBuilder.p
			doc = builder.parse(file);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = Xpath;
			NodeList nodeList_ele = (NodeList) xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			rootElement = (Element) nodeList_ele;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> getCreatedMasterDataFromXML(String sXMLFilePath, String sMasterName ,String sTestcaseName){	
		String sXpath = "//"+sMasterName+"[@TestcaseName='"+sTestcaseName+"']";
		System.out.println(sXpath);
		Map<String,Map<String,String>> mapDetails = getDetailsMap(sXMLFilePath,sXpath);
		if(mapDetails.size()==0){
			return null;
		}
		return  mapDetails.get("Row0");
	}

	//IncentiveFee Details.
	public static boolean writeIncentiveFeeDetailsToXML(Map<String, String> IncentiveFeeDetails){
		try{
			createResultfolder(sIncentiveXMLFilePath);
			openIncentiveFeeXMLFile();
			addIncentiveFeeDetails(IncentiveFeeDetails,sIncentiveXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	} 

	private static void openIncentiveFeeXMLFile() {
		if (!bInceFeeCreateFile) {
			bInceFeeCreateFile = true;
			String sMessageFileName =  "IncentiveFee.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sIncentiveXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sIncentiveXMLFilePath = sIncentiveXMLFilePath + sMessageFileName;
			createIncentiveFeeXMLFile(sIncentiveXMLFilePath);
		}
	}

	private static void createIncentiveFeeXMLFile(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			if(new File(sIncentiveXMLFilePath).exists()){
				Incedoc = docBuilder.parse(sIncentiveXMLFilePath);
				IncerootElement = Incedoc.getDocumentElement();
			}else{
				Incedoc = docBuilder.newDocument();
				IncerootElement = Incedoc.createElement("IncentiveFeeDetails");
				Incedoc.appendChild(IncerootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(Incedoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);

			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void addIncentiveFeeDetails(Map<String, String> objIncentiveFeeMap,String sXMLFilePath) {
		try {

			IncentiveElement = Incedoc.createElement("IncentiveFee");
			IncerootElement.appendChild(IncentiveElement);

			IncentiveElement.setAttribute("TestcaseName", objIncentiveFeeMap.get("TestcaseName"));
			IncentiveElement.setAttribute("IncentiveFeeID", objIncentiveFeeMap.get("IncentiveFeeID"));
			IncentiveElement.setAttribute("MakerStatus", objIncentiveFeeMap.get("MakerStatus"));
			IncentiveElement.setAttribute("CheckerStatus", objIncentiveFeeMap.get("CheckerStatus"));
			

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(Incedoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//XML File creation for Trade Type SUBSCRIPTION and putting in details of created.
	public static boolean writeTradeTypeSUBDetailsToXML(Map<String, String> objTradeTypeSUBMap){
		try{
			createResultfolder(sTradeTypeSUBXMLFilePath);
			openTradeTypeSUBXMLFile();
			addTradeTypeSUBDetailsToXML(objTradeTypeSUBMap,sTradeTypeSUBXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void openTradeTypeSUBXMLFile() {
		if (!bTradeTypeSUBCreateFile) {
			bTradeTypeSUBCreateFile = true;
			String sMessageFileName =  "TradeTypeSUB.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sTradeTypeSUBXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sTradeTypeSUBXMLFilePath = sTradeTypeSUBXMLFilePath + sMessageFileName;
			createXMLFileForTradeTypeSUB(sTradeTypeSUBXMLFilePath);
		}
	}

	private static void addTradeTypeSUBDetailsToXML(Map<String, String> objTradeTypeSUBMap,String sXMLFilePath) {
		try {

			TradeTypeSUBElement = TradeTypeSUBdoc.createElement("TradeTypeSUB");
			TradeTypeSUBrootElement.appendChild(TradeTypeSUBElement);

			TradeTypeSUBElement.setAttribute("TestcaseName", objTradeTypeSUBMap.get("TestcaseName"));
			TradeTypeSUBElement.setAttribute("TransactionID", objTradeTypeSUBMap.get("Transaction ID"));
			TradeTypeSUBElement.setAttribute("AccountID", objTradeTypeSUBMap.get("Account ID"));
			TradeTypeSUBElement.setAttribute("MakerStatus", objTradeTypeSUBMap.get("MakerStatus"));
			TradeTypeSUBElement.setAttribute("CheckerStatus", objTradeTypeSUBMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(TradeTypeSUBdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	private static void createXMLFileForTradeTypeSUB(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sTradeTypeSUBXMLFilePath).exists()){
				TradeTypeSUBdoc = docBuilder.parse(sTradeTypeSUBXMLFilePath);
				TradeTypeSUBrootElement = TradeTypeSUBdoc.getDocumentElement();
			}else{
				TradeTypeSUBdoc = docBuilder.newDocument();
				TradeTypeSUBrootElement = TradeTypeSUBdoc.createElement("TradeTypeSUBDetails");
				TradeTypeSUBdoc.appendChild(TradeTypeSUBrootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(TradeTypeSUBdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	//Feeder Subscription Account details.
	public static boolean writeFeederSUBAccountDetailsToXML(Map<String, String> objFeederSUBAccountDetailsMap){
		try{
			createResultfolder(sFeederSUBAccountXMLFilePath);
			openFeederSUBAccountXMLFile();
			addFeederSUBAccountDetailsToXML(objFeederSUBAccountDetailsMap,sFeederSUBAccountXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addFeederSUBAccountDetailsToXML(Map<String, String> objFeederSUBAccountDetailsMap, String sXMLFilePath) {
		try {
			FeederSUBAccountElement = FeederSUBAccountdoc.createElement("FeederSUBAccount");
			FeederSUBAccountrootElement.appendChild(FeederSUBAccountElement);

			FeederSUBAccountElement.setAttribute("TestcaseName", objFeederSUBAccountDetailsMap.get("TestcaseName"));
			FeederSUBAccountElement.setAttribute("AccountID", objFeederSUBAccountDetailsMap.get("AccountID"));
			FeederSUBAccountElement.setAttribute("SearchID", objFeederSUBAccountDetailsMap.get("SearchID"));
			FeederSUBAccountElement.setAttribute("MakerStatus", objFeederSUBAccountDetailsMap.get("MakerStatus"));
			FeederSUBAccountElement.setAttribute("CheckerStatus", objFeederSUBAccountDetailsMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(FeederSUBAccountdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openFeederSUBAccountXMLFile() {
		if (!bFeederSUBAccountCreateFile) {
			bFeederSUBAccountCreateFile = true;
			String sMessageFileName =  "FeederSUBAccount.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sFeederSUBAccountXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sFeederSUBAccountXMLFilePath = sFeederSUBAccountXMLFilePath + sMessageFileName;
			createXMLFileForFeederSUBAccount(sFeederSUBAccountXMLFilePath);
		}
	}

	private static void createXMLFileForFeederSUBAccount(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sFeederSUBAccountXMLFilePath).exists()){
				FeederSUBAccountdoc = docBuilder.parse(sFeederSUBAccountXMLFilePath);
				FeederSUBAccountrootElement = FeederSUBAccountdoc.getDocumentElement();
			}else{
				//Create the New XML File if the File Not Exists
				FeederSUBAccountdoc = docBuilder.newDocument();
				FeederSUBAccountrootElement = FeederSUBAccountdoc.createElement("FeederSUBAccountDetails");
				FeederSUBAccountdoc.appendChild(FeederSUBAccountrootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(FeederSUBAccountdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}	

	//Feeder Redemption  details.
	public static boolean writeFeederREDAccountDetailsToXML(Map<String, String> objFeederREDAccountDetailsMap){
		try{
			createResultfolder(sFeederREDAccountXMLFilePath);
			openFeederREDAccountXMLFile();
			addFeederREDAccountDetailsToXML(objFeederREDAccountDetailsMap,sFeederREDAccountXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addFeederREDAccountDetailsToXML(Map<String, String> objFeederREDAccountDetailsMap, String sXMLFilePath) {
		try {
			FeederREDAccountElement = FeederREDAccountdoc.createElement("FeederREDAccount");
			FeederREDAccountrootElement.appendChild(FeederREDAccountElement);

			FeederREDAccountElement.setAttribute("TestcaseName", objFeederREDAccountDetailsMap.get("TestcaseName"));
			FeederREDAccountElement.setAttribute("SearchID", objFeederREDAccountDetailsMap.get("SearchID"));
			FeederREDAccountElement.setAttribute("MakerStatus", objFeederREDAccountDetailsMap.get("MakerStatus"));
			FeederREDAccountElement.setAttribute("CheckerStatus", objFeederREDAccountDetailsMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(FeederREDAccountdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openFeederREDAccountXMLFile() {
		if (!bFeederREDAccountCreateFile) {
			bFeederREDAccountCreateFile = true;
			String sMessageFileName =  "FeederREDAccount.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sFeederREDAccountXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sFeederREDAccountXMLFilePath = sFeederREDAccountXMLFilePath + sMessageFileName;
			createXMLFileForFeederREDAccount(sFeederREDAccountXMLFilePath);
		}
	}

	private static void createXMLFileForFeederREDAccount(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sFeederREDAccountXMLFilePath).exists()){
				FeederREDAccountdoc = docBuilder.parse(sFeederREDAccountXMLFilePath);
				FeederREDAccountrootElement = FeederREDAccountdoc.getDocumentElement();
			}else{
				FeederREDAccountdoc = docBuilder.newDocument();
				FeederREDAccountrootElement = FeederREDAccountdoc.createElement("FeederREDAccountDetails");
				FeederREDAccountdoc.appendChild(FeederREDAccountrootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(FeederREDAccountdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}

	//Opening Balance Details.
	public static boolean writeOpeningBalanceDetailsToXML(Map<String, String> objOpeningBalanceDetailsMap){
		try{
			createResultfolder(sOpeningBalanceDetailsXMLFilePath);
			openOpeningBalanceDetailsXMLFile();
			addOpeningBalanceDetailsToXML(objOpeningBalanceDetailsMap,sOpeningBalanceDetailsXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addOpeningBalanceDetailsToXML(Map<String, String> objOpeningBalanceDetailsMap, String sXMLFilePath) {
		try {
			OpeningBalanceDetailsElement = OpeningBalanceDetailsdoc.createElement("OpeningBalance");
			OpeningBalanceDetailsrootElement.appendChild(OpeningBalanceDetailsElement);

			OpeningBalanceDetailsElement.setAttribute("TestcaseName", objOpeningBalanceDetailsMap.get("TestcaseName"));
			OpeningBalanceDetailsElement.setAttribute("SearchID", objOpeningBalanceDetailsMap.get("SearchID"));
			OpeningBalanceDetailsElement.setAttribute("MakerStatus", objOpeningBalanceDetailsMap.get("MakerStatus"));
			OpeningBalanceDetailsElement.setAttribute("CheckerStatus", objOpeningBalanceDetailsMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(OpeningBalanceDetailsdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openOpeningBalanceDetailsXMLFile() {
		if (!bOpeningBalanceDetailsCreateFile) {
			bOpeningBalanceDetailsCreateFile = true;
			String sMessageFileName =  "OpeningBalanceDetails.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sOpeningBalanceDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sOpeningBalanceDetailsXMLFilePath = sOpeningBalanceDetailsXMLFilePath + sMessageFileName;
			createXMLFileForOpeningBalanceDetails(sOpeningBalanceDetailsXMLFilePath);
		}
	}

	private static void createXMLFileForOpeningBalanceDetails(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sOpeningBalanceDetailsXMLFilePath).exists()){
				OpeningBalanceDetailsdoc = docBuilder.parse(sOpeningBalanceDetailsXMLFilePath);
				OpeningBalanceDetailsrootElement = OpeningBalanceDetailsdoc.getDocumentElement();
			}
			//Create the New XML File if the File Not Exists
			else{
				OpeningBalanceDetailsdoc = docBuilder.newDocument();
				OpeningBalanceDetailsrootElement = OpeningBalanceDetailsdoc.createElement("OpeningBalanceDetails");
				OpeningBalanceDetailsdoc.appendChild(OpeningBalanceDetailsrootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(OpeningBalanceDetailsdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}

	//Trade Type REDEMPTION Details.
	public static boolean writeTradeTypeREDDetailsToXML(Map<String, String> objTradeTypeREDDetailsMap){
		try{
			createResultfolder(sTradeTypeREDDetailsXMLFilePath);
			openTradeTypeREDDetailsXMLFile();
			addTradeTypeREDDetailsToXML(objTradeTypeREDDetailsMap,sTradeTypeREDDetailsXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addTradeTypeREDDetailsToXML(Map<String, String> objTradeTypeREDDetailsMap, String sXMLFilePath) {
		try {
			TradeTypeREDDetailsElement = TradeTypeREDDetailsdoc.createElement("TradeTypeRED");
			TradeTypeREDDetailsrootElement.appendChild(TradeTypeREDDetailsElement);

			TradeTypeREDDetailsElement.setAttribute("TestcaseName", objTradeTypeREDDetailsMap.get("TestcaseName"));
			TradeTypeREDDetailsElement.setAttribute("TransactionID", objTradeTypeREDDetailsMap.get("Transaction ID"));
			TradeTypeREDDetailsElement.setAttribute("MakerStatus", objTradeTypeREDDetailsMap.get("MakerStatus"));
			TradeTypeREDDetailsElement.setAttribute("CheckerStatus", objTradeTypeREDDetailsMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(TradeTypeREDDetailsdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openTradeTypeREDDetailsXMLFile() {
		if (!bTradeTypeREDDetailsCreateFile) {
			bTradeTypeREDDetailsCreateFile = true;
			String sMessageFileName =  "TradeTypeREDDetails.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sTradeTypeREDDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sTradeTypeREDDetailsXMLFilePath = sTradeTypeREDDetailsXMLFilePath + sMessageFileName;
			createXMLFileForTradeTypeREDDetails(sTradeTypeREDDetailsXMLFilePath);
		}
	}

	private static void createXMLFileForTradeTypeREDDetails(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sTradeTypeREDDetailsXMLFilePath).exists()){
				TradeTypeREDDetailsdoc = docBuilder.parse(sTradeTypeREDDetailsXMLFilePath);
				TradeTypeREDDetailsrootElement = TradeTypeREDDetailsdoc.getDocumentElement();
			}else{
				//Create the New XML File if the File Not Exists
				TradeTypeREDDetailsdoc = docBuilder.newDocument();
				TradeTypeREDDetailsrootElement = TradeTypeREDDetailsdoc.createElement("TradeTypeREDDetails");
				TradeTypeREDDetailsdoc.appendChild(TradeTypeREDDetailsrootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(TradeTypeREDDetailsdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}		

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}

	//Trade Type Transfer Details.
	public static boolean writeTradeTypeTRANDetailsToXML(Map<String, String> objTradeTypeTRANDetailsMap){
		try{
			createResultfolder(sTradeTypeTRANDetailsXMLFilePath);
			openTradeTypeTRANDetailsXMLFile();
			addTradeTypeTRANDetailsToXML(objTradeTypeTRANDetailsMap,sTradeTypeTRANDetailsXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addTradeTypeTRANDetailsToXML(Map<String, String> objTradeTypeTRANDetailsMap, String sXMLFilePath) {
		try {
			TradeTypeTRANDetailsElement = TradeTypeTRANDetailsdoc.createElement("TradeTypeTRAN");
			TradeTypeTRANDetailsrootElement.appendChild(TradeTypeTRANDetailsElement);

			TradeTypeTRANDetailsElement.setAttribute("TestcaseName", objTradeTypeTRANDetailsMap.get("TestcaseName"));
			TradeTypeTRANDetailsElement.setAttribute("TransactionID", objTradeTypeTRANDetailsMap.get("Transaction ID"));
			TradeTypeTRANDetailsElement.setAttribute("AccountID", objTradeTypeTRANDetailsMap.get("Account ID"));
			TradeTypeTRANDetailsElement.setAttribute("MakerStatus", objTradeTypeTRANDetailsMap.get("MakerStatus"));
			TradeTypeTRANDetailsElement.setAttribute("CheckerStatus", objTradeTypeTRANDetailsMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(TradeTypeTRANDetailsdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openTradeTypeTRANDetailsXMLFile() {
		if (!bTradeTypeTRANDetailsCreateFile) {
			bTradeTypeTRANDetailsCreateFile = true;
			String sMessageFileName =  "TradeTypeTRANDetails.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sTradeTypeTRANDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sTradeTypeTRANDetailsXMLFilePath = sTradeTypeTRANDetailsXMLFilePath + sMessageFileName;
			createXMLFileForTradeTypeTRANDetails(sTradeTypeTRANDetailsXMLFilePath);
		}
	}

	private static void createXMLFileForTradeTypeTRANDetails(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sTradeTypeTRANDetailsXMLFilePath).exists()){
				TradeTypeTRANDetailsdoc = docBuilder.parse(sTradeTypeTRANDetailsXMLFilePath);
				TradeTypeTRANDetailsrootElement = TradeTypeTRANDetailsdoc.getDocumentElement();
			}else{
				//Create the New XML File if the File Not Exists
				TradeTypeTRANDetailsdoc = docBuilder.newDocument();
				TradeTypeTRANDetailsrootElement = TradeTypeTRANDetailsdoc.createElement("TradeTypeTRANDetails");
				TradeTypeTRANDetailsdoc.appendChild(TradeTypeTRANDetailsrootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(TradeTypeTRANDetailsdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}


	//Trade Type Exchange  Details.
	public static boolean writeTradeTypeEXCNDetailsToXML(Map<String, String> objTradeTypeEXCNDetailsMap){
		try{
			createResultfolder(sTradeTypeEXCNDetailsXMLFilePath);
			openTradeTypeEXCNDetailsXMLFile();
			addTradeTypeEXCNDetailsToXML(objTradeTypeEXCNDetailsMap,sTradeTypeEXCNDetailsXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addTradeTypeEXCNDetailsToXML(Map<String, String> objTradeTypeEXCNDetailsMap, String sXMLFilePath) {
		try {
			TradeTypeEXCNDetailsElement = TradeTypeEXCNDetailsdoc.createElement("TradeTypeEXCN");
			TradeTypeEXCNDetailsrootElement.appendChild(TradeTypeEXCNDetailsElement);

			TradeTypeEXCNDetailsElement.setAttribute("TestcaseName", objTradeTypeEXCNDetailsMap.get("TestcaseName"));
			TradeTypeEXCNDetailsElement.setAttribute("TransactionID", objTradeTypeEXCNDetailsMap.get("Transaction ID"));
			TradeTypeEXCNDetailsElement.setAttribute("MakerStatus", objTradeTypeEXCNDetailsMap.get("MakerStatus"));
			TradeTypeEXCNDetailsElement.setAttribute("CheckerStatus", objTradeTypeEXCNDetailsMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(TradeTypeEXCNDetailsdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openTradeTypeEXCNDetailsXMLFile() {
		if (!bTradeTypeEXCNDetailsCreateFile) {
			bTradeTypeEXCNDetailsCreateFile = true;
			String sMessageFileName =  "TradeTypeEXCNDetails.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sTradeTypeEXCNDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sTradeTypeEXCNDetailsXMLFilePath = sTradeTypeEXCNDetailsXMLFilePath + sMessageFileName;
			createXMLFileForTradeTypeEXCNDetails(sTradeTypeEXCNDetailsXMLFilePath);
		}
	}

	private static void createXMLFileForTradeTypeEXCNDetails(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sTradeTypeEXCNDetailsXMLFilePath).exists()){
				TradeTypeEXCNDetailsdoc = docBuilder.parse(sTradeTypeEXCNDetailsXMLFilePath);
				TradeTypeEXCNDetailsrootElement = TradeTypeEXCNDetailsdoc.getDocumentElement();
			}else{
				//Create the New XML File if the File Not Exists
				TradeTypeEXCNDetailsdoc = docBuilder.newDocument();
				TradeTypeEXCNDetailsrootElement = TradeTypeEXCNDetailsdoc.createElement("TradeTypeEXCNDetails");
				TradeTypeEXCNDetailsdoc.appendChild(TradeTypeEXCNDetailsrootElement);

				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(TradeTypeEXCNDetailsdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}

	//Trade Type SWITCH  Details.
	public static boolean writeTradeTypeSWITCHDetailsToXML(Map<String, String> objTradeTypeSWITCHDetailsMap){
		try{
			createResultfolder(sTradeTypeSWITCHDetailsXMLFilePath);
			openTradeTypeSWITCHDetailsXMLFile();
			addTradeTypeSWITCHDetailsToXML(objTradeTypeSWITCHDetailsMap,sTradeTypeSWITCHDetailsXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addTradeTypeSWITCHDetailsToXML(Map<String, String> objTradeTypeSWITCHDetailsMap, String sXMLFilePath) {
		try {
			TradeTypeSWITCHDetailsElement = TradeTypeSWITCHDetailsdoc.createElement("TradeTypeSWITCH");
			TradeTypeSWITCHDetailsrootElement.appendChild(TradeTypeSWITCHDetailsElement);

			TradeTypeSWITCHDetailsElement.setAttribute("TestcaseName", objTradeTypeSWITCHDetailsMap.get("TestcaseName"));
			TradeTypeSWITCHDetailsElement.setAttribute("TransactionID", objTradeTypeSWITCHDetailsMap.get("Transaction ID"));
			TradeTypeSWITCHDetailsElement.setAttribute("AccountID", objTradeTypeSWITCHDetailsMap.get("Account ID"));
			TradeTypeSWITCHDetailsElement.setAttribute("MakerStatus", objTradeTypeSWITCHDetailsMap.get("MakerStatus"));
			TradeTypeSWITCHDetailsElement.setAttribute("CheckerStatus", objTradeTypeSWITCHDetailsMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(TradeTypeSWITCHDetailsdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openTradeTypeSWITCHDetailsXMLFile() {
		if (!bTradeTypeSWITCHDetailsCreateFile) {
			bTradeTypeSWITCHDetailsCreateFile = true;
			String sMessageFileName =  "TradeTypeSWITCHDetails.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sTradeTypeSWITCHDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sTradeTypeSWITCHDetailsXMLFilePath = sTradeTypeSWITCHDetailsXMLFilePath + sMessageFileName;

			createXMLFileForTradeTypeSWITCHDetails(sTradeTypeSWITCHDetailsXMLFilePath);

		}
	}

	private static void createXMLFileForTradeTypeSWITCHDetails(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sTradeTypeSWITCHDetailsXMLFilePath).exists()){
				TradeTypeSWITCHDetailsdoc = docBuilder.parse(sTradeTypeSWITCHDetailsXMLFilePath);
				TradeTypeSWITCHDetailsrootElement = TradeTypeSWITCHDetailsdoc.getDocumentElement();
			}
			//Create the New XML File if the File Not Exists
			else{
				TradeTypeSWITCHDetailsdoc = docBuilder.newDocument();
				TradeTypeSWITCHDetailsrootElement = TradeTypeSWITCHDetailsdoc.createElement("TradeTypeSWITCHDetails");
				TradeTypeSWITCHDetailsdoc.appendChild(TradeTypeSWITCHDetailsrootElement);
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(TradeTypeSWITCHDetailsdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}

	public static void updatedAttributeValueInCreatedXMLFile(String filePath,String nameOfAttributeToUpdateItsValue,String newValueOfAttribute,String testCaseAttributeName,String testCaseNameToUpdate,String tagName){
		try {
			File inputFile = new File(filePath);
			setFilePropertyToWritable(filePath);
			DocumentBuilderFactory docFactory =	DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(inputFile);

			//Update Values
			NodeList tagNameToUpdate = doc.getElementsByTagName(tagName);
			Element emp = null;
			//loop for each employee
			for(int i=0; i<tagNameToUpdate.getLength();i++){
				emp = (Element) tagNameToUpdate.item(i);
				if(testCaseNameToUpdate.equalsIgnoreCase(emp.getAttribute(testCaseAttributeName))){
					emp.setAttribute(nameOfAttributeToUpdateItsValue, newValueOfAttribute);
				}
			}

			//Write to file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult consoleResult = new StreamResult(new File(filePath));
			transformer.transform(source, consoleResult);

			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	//Trade Type 'Side Pocket SUBSCRIPTION'  Details.
	public static boolean writeTradeTypeSPSUBDetailsToXML(Map<String, String> objTradeTypeSPSUBDetailsMap){
		try{
			createResultfolder(sTradeTypeSPSUBDetailsXMLFilePath);
			openTradeTypeSPSUBDetailsXMLFile();
			addTradeTypeSPSUBDetailsToXML(objTradeTypeSPSUBDetailsMap,sTradeTypeSPSUBDetailsXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addTradeTypeSPSUBDetailsToXML(Map<String, String> objTradeTypeSPSUBDetailsMap, String sXMLFilePath) {
		try {
			TradeTypeSPSUBDetailsElement = TradeTypeSPSUBDetailsdoc.createElement("TradeTypeSPSUB");
			TradeTypeSPSUBDetailsrootElement.appendChild(TradeTypeSPSUBDetailsElement);

			TradeTypeSPSUBDetailsElement.setAttribute("TestcaseName", objTradeTypeSPSUBDetailsMap.get("TestcaseName"));
			TradeTypeSPSUBDetailsElement.setAttribute("TransactionID", objTradeTypeSPSUBDetailsMap.get("Transaction ID"));
			TradeTypeSPSUBDetailsElement.setAttribute("MakerStatus", objTradeTypeSPSUBDetailsMap.get("MakerStatus"));
			TradeTypeSPSUBDetailsElement.setAttribute("CheckerStatus", objTradeTypeSPSUBDetailsMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(TradeTypeSPSUBDetailsdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openTradeTypeSPSUBDetailsXMLFile() {
		if (!bTradeTypeSPSUBDetailsCreateFile) {
			bTradeTypeSPSUBDetailsCreateFile = true;
			String sMessageFileName =  "TradeTypeSPSUBDetails.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sTradeTypeSPSUBDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sTradeTypeSPSUBDetailsXMLFilePath = sTradeTypeSPSUBDetailsXMLFilePath + sMessageFileName;

			createXMLFileForTradeTypeSPSUBDetails(sTradeTypeSPSUBDetailsXMLFilePath);

		}
	}

	private static void createXMLFileForTradeTypeSPSUBDetails(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sTradeTypeSPSUBDetailsXMLFilePath).exists()){
				TradeTypeSPSUBDetailsdoc = docBuilder.parse(sTradeTypeSPSUBDetailsXMLFilePath);
				TradeTypeSPSUBDetailsrootElement = TradeTypeSPSUBDetailsdoc.getDocumentElement();
			}
			//Create the New XML File if the File Not Exists
			else{
				TradeTypeSPSUBDetailsdoc = docBuilder.newDocument();
				TradeTypeSPSUBDetailsrootElement = TradeTypeSPSUBDetailsdoc.createElement("TradeTypeSPSUBDetails");
				TradeTypeSPSUBDetailsdoc.appendChild(TradeTypeSPSUBDetailsrootElement);
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(TradeTypeSPSUBDetailsdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}

	//Trade Type 'Side Pocket REDEMPTION'  Details.
	public static boolean writeTradeTypeSPREDDetailsToXML(Map<String, String> objTradeTypeSPREDDetailsMap){
		try{
			createResultfolder(sTradeTypeSPREDDetailsXMLFilePath);
			openTradeTypeSPREDDetailsXMLFile();
			addTradeTypeSPREDDetailsToXML(objTradeTypeSPREDDetailsMap,sTradeTypeSPREDDetailsXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addTradeTypeSPREDDetailsToXML(Map<String, String> objTradeTypeSPREDDetailsMap, String sXMLFilePath) {
		try {
			TradeTypeSPREDDetailsElement = TradeTypeSPREDDetailsdoc.createElement("TradeTypeSPRED");
			TradeTypeSPREDDetailsrootElement.appendChild(TradeTypeSPREDDetailsElement);

			TradeTypeSPREDDetailsElement.setAttribute("TestcaseName", objTradeTypeSPREDDetailsMap.get("TestcaseName"));
			TradeTypeSPREDDetailsElement.setAttribute("TransactionID", objTradeTypeSPREDDetailsMap.get("Transaction ID"));
			TradeTypeSPREDDetailsElement.setAttribute("MakerStatus", objTradeTypeSPREDDetailsMap.get("MakerStatus"));
			TradeTypeSPREDDetailsElement.setAttribute("CheckerStatus", objTradeTypeSPREDDetailsMap.get("CheckerStatus"));

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(TradeTypeSPREDDetailsdoc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void openTradeTypeSPREDDetailsXMLFile() {
		if (!bTradeTypeSPREDDetailsCreateFile) {
			bTradeTypeSPREDDetailsCreateFile = true;
			String sMessageFileName =  "TradeTypeSPREDDetails.xml";
			sMessageFileName = sMessageFileName.replace(":", "");
			sMessageFileName = sMessageFileName.replace(" ", "");

			sTradeTypeSPREDDetailsXMLFilePath = Global.sXMLTestFolderPath + sPathSeparatorChar;
			sTradeTypeSPREDDetailsXMLFilePath = sTradeTypeSPREDDetailsXMLFilePath + sMessageFileName;

			createXMLFileForTradeTypeSPREDDetails(sTradeTypeSPREDDetailsXMLFilePath);

		}
	}

	private static void createXMLFileForTradeTypeSPREDDetails(String sfile) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			//Append the Records if already File exists By parsing the file name to doc and assigning document Element to the Root element 
			if(new File(sTradeTypeSPREDDetailsXMLFilePath).exists()){
				TradeTypeSPREDDetailsdoc = docBuilder.parse(sTradeTypeSPREDDetailsXMLFilePath);
				TradeTypeSPREDDetailsrootElement = TradeTypeSPREDDetailsdoc.getDocumentElement();
			}
			//Create the New XML File if the File Not Exists
			else{
				TradeTypeSPREDDetailsdoc = docBuilder.newDocument();
				TradeTypeSPREDDetailsrootElement = TradeTypeSPREDDetailsdoc.createElement("TradeTypeSPREDDetails");
				TradeTypeSPREDDetailsdoc.appendChild(TradeTypeSPREDDetailsrootElement);
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(TradeTypeSPREDDetailsdoc);
				StreamResult result = new StreamResult(new File(sfile));
				transformer.transform(source, result);
			}			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}		
	}
	
	public static void setFilePropertyToWritable(String sFilePath){
		try {
			File sFile = new File(sFilePath);
			if(sFile.exists()){
				sFile.setWritable(true);
			}
			
			return;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return;
		}
	}

	/*	public static Map<String, String> getClientDataFromXML(String sXMLFilePath,String sTestcaseName,String Status){	
		String sXpath = "//ClientDetails[@TestcaseName='testcasename' and @Status='sStatus']";
		sXpath = sXpath.replace("testcasename", sTestcaseName).replace("sStatus", Status);
		Map<String,Map<String,String>> mapDetails = getDetailsMap(sXMLFilePath,sXpath);
		if(mapDetails.size()==0){
			return null;
		}
		return  mapDetails.get("Row0");
	}

	public static Map<String, String> getClientDataFromXML(String sXMLFilePath,String sTestcaseName){	
		String sXpath = "//ClientDetails[@TestcaseName='testcasename']";
		sXpath = sXpath.replace("testcasename", sTestcaseName);
		Map<String,Map<String,String>> mapDetails = getDetailsMap(sXMLFilePath,sXpath);
		if(mapDetails.size()==0){
			return null;
		}
		return  mapDetails.get("Row0");
	}

	public static Map<String, String> getFundFamilyDataFromXML(String sXMLFilePath,String sTestcaseName,String Status){	
		String sXpath = "//FundFamily[@TestcaseName='testcasename' and @Status='sStatus']";
		sXpath = sXpath.replace("testcasename", sTestcaseName).replace("sStatus", Status);
		Map<String,Map<String,String>> mapDetails = getDetailsMap(sXMLFilePath,sXpath);
		if(mapDetails.size()==0){
			return null;
		}
		return  mapDetails.get("Row0");
	}

	public static Map<String,Map<String,String>> getDetailsMap(String sXMLFilePath,String sExpression){

		NodeList nodeList;
		Map<String,Map<String,String>> objMainMap = new HashMap<String,Map<String,String>>();
		try {
			FileInputStream file = new FileInputStream(new File(sXMLFilePath));

			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(file);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = sExpression;
			nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
			for(int iOutLoopCounter =0;iOutLoopCounter<nodeList.getLength();iOutLoopCounter++){	
				NamedNodeMap nm = nodeList.item(iOutLoopCounter).getAttributes();
				Map<String,String> objMsgDts = new HashMap<String,String>();
				for(int iInnerLoopCounter =0; iInnerLoopCounter< nm.getLength();iInnerLoopCounter++){
					String sKey = nm.item(iInnerLoopCounter).getNodeName();
					String sValue = nm.item(iInnerLoopCounter).getNodeValue();
					objMsgDts.put(sKey, sValue);
				}
				objMainMap.put("Row" + iOutLoopCounter,objMsgDts);
			}			
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
		return objMainMap;
	}

	public static void updateClientStatus(String sXMLFilePath,String ClientName,String sNewStatus){
		String sXpath = "//ClientDetails[@ClientName='"+ClientName+"']";
		updateFlagStatus(sXMLFilePath,sXpath,"Status" ,sNewStatus);
	}

	private static void updateFlagStatus(String sXMLFilePath,String sXpath,String sAttribute,String sNewStatus) {
		NodeList nodeList;
		try {
			FileInputStream file = new FileInputStream(new File(sXMLFilePath));
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(file);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = sXpath;
			nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODE);
			Element element = (Element) nodeList;
			element.setAttribute(sAttribute,sNewStatus);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDocument);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} 
		catch (Exception e) {
			System.out.println("Raising exception while updating status for Xpth "+sXpath);
		}

	}

	private static void movePointerToElement(String sXMLFilePath,String Xpath) {
		try {
			FileInputStream file = new FileInputStream(new File(sXMLFilePath));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = factory.newDocumentBuilder();
			//Document document = builder.parse(file);
			//docBuilder = docFactory.newDocumentBuilder();
			//docBuilder.p
			doc = builder.parse(file);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = Xpath;
			NodeList nodeList_ele = (NodeList) xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			rootElement = (Element) nodeList_ele;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean writeFundFamilyToXML(Map<String, String> FundFamilyDetails){
		try{
			String Xpath = "//ClientDetails[@ClientName='"+FundFamilyDetails.get("ClientName")+"']";
			movePointerToElement(sFundSetupXMLFilePath,Xpath);
			addFundFamilyDetails(FundFamilyDetails,sFundSetupXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	} 

	private static void addFundFamilyDetails(Map<String, String> objECSFileTrailerMap,String sXMLFilePath) {

		try {
			fundFamilyElement = doc.createElement("FundFamily");
			rootElement.appendChild(fundFamilyElement);

			// setting attributes
			fundFamilyElement.setAttribute("TestcaseName", objECSFileTrailerMap.get("TestcaseName"));
			fundFamilyElement.setAttribute("ClientName", objECSFileTrailerMap.get("ClientName"));
			fundFamilyElement.setAttribute("FFName", objECSFileTrailerMap.get("FundFamilyName"));
			//fundFamilyElement.setAttribute("TCref", Reporting.Testcasename);
			fundFamilyElement.setAttribute("Status","MakerDone");

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateFFStatus(String sXMLFilePath,String FFName,String sNewStatus){
		String sXpath = "//FundFamily[@FFName='"+FFName+"']";
		updateFlagStatus(sXMLFilePath,sXpath,"Status" ,sNewStatus);
	}

	public static boolean writeLEDetailsToXML(Map<String, String> LegalEntityDetails){
		try{
			String Xpath = "//FundFamily[@FFName='"+LegalEntityDetails.get("FundFamilyName")+"']";
			movePointerToElement(sFundSetupXMLFilePath,Xpath);
			addLegalEntityDetails(LegalEntityDetails,sFundSetupXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addLegalEntityDetails(Map<String, String> objLEMap,String sXMLFilePath) {
		try {
			legalEntityElement = doc.createElement("LegalEntity");
			rootElement.appendChild(legalEntityElement);

			// setting attributes
			legalEntityElement.setAttribute("TestcaseName", objLEMap.get("TestcaseName"));
			legalEntityElement.setAttribute("ClientName", objLEMap.get("ClientName"));
			legalEntityElement.setAttribute("FFName", objLEMap.get("FundFamilyName"));
			legalEntityElement.setAttribute("LEName", objLEMap.get("LegalEntityName"));
			legalEntityElement.setAttribute("LEType", objLEMap.get("LegalEntityType"));
			legalEntityElement.setAttribute("Status","MakerDone");

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean writeLEFeederDetailsToXML(Map<String, String> LegalEntityDetails){
		try{
			String Xpath = "//LegalEntity[@LEName='"+LegalEntityDetails.get("FundFamilyName")+"' and @LEType='Master']";
			movePointerToElement(sFundSetupXMLFilePath,Xpath);
			addLegalEntityFeederDetails(LegalEntityDetails,sFundSetupXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addLegalEntityFeederDetails(Map<String, String> objLEMap,String sXMLFilePath) {
		try {
			legalEntityElement = doc.createElement("FeederLegalEntity");
			rootElement.appendChild(legalEntityElement);

			// setting attributes
			legalEntityElement.setAttribute("TestcaseName", objLEMap.get("TestcaseName"));
			legalEntityElement.setAttribute("ClientName", objLEMap.get("ClientName"));
			legalEntityElement.setAttribute("FFName", objLEMap.get("FundFamilyName"));
			legalEntityElement.setAttribute("LEName", objLEMap.get("LegalEntityName"));
			legalEntityElement.setAttribute("LEType", objLEMap.get("LegalEntityType"));
			legalEntityElement.setAttribute("Status","MakerDone");

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//class xml path will be getting appended

	//series xml path will be appended
	public static boolean writeSeriesDetailsToXML(Map<String, String> SeriesDetails){
		try{
			String Xpath = "//Class[@ClassName='"+SeriesDetails.get("ClassName")+"']";
			movePointerToElement(sFundSetupXMLFilePath,Xpath);
			addSeriesDetails(SeriesDetails,sFundSetupXMLFilePath);
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	private static void addSeriesDetails(Map<String, String> objLEMap,String sXMLFilePath) {
		try {
			SeriesElement = doc.createElement("Series");
			rootElement.appendChild(SeriesElement);

			// setting attributes
			SeriesElement.setAttribute("TestcaseName", objLEMap.get("TestcaseName"));
			SeriesElement.setAttribute("ClientName", objLEMap.get("ClientName"));
			SeriesElement.setAttribute("FFName", objLEMap.get("FundFamilyName"));
			SeriesElement.setAttribute("LEName", objLEMap.get("LegalEntityName"));
			SeriesElement.setAttribute("ClassName", objLEMap.get("ClassName"));
			SeriesElement.setAttribute("Status","MakerDone");

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sXMLFilePath));
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
