package com.viteos.veda.master.lib;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import com.viteos.veda.master.lib.XMLLibrary;

public class NewReporting {

	public static String Functionality = "";
	public static String Testcasename = "";
	public static int Iterator = 0;
	public static String resultFileName = "";
	
	private static String sPathSeparatorChar = java.io.File.separator;
	private static String sTestResultFolderPath = "NewLogForExcelCompare";
	private static Document doc;
	public static String sResultXMLFilePath = "";
	private static String sXMLCurrentScriptName;
	private static int sXMLCurrentIterator;
	private static Element rootElement;
	private static Element IteratorElement;
	private static Element tsElement;
	private static Element FuncElement;
	public static boolean bCreateFile = false;
	private static String sXMLCurrentFunctionality;
	private static int testCasesCount = 0;
	private static int testcasesFailCount = 0;
	private static int testcasesPassCount = 0;
	private static DocumentBuilderFactory docFactory;
	private static DocumentBuilder docBuilder;
	private static FileInputStream file;

	public static void logResults(String sStatus, String sColumnName,String newValue,String oldValue,String difference) {
		sXMLCurrentFunctionality = Functionality;
		sXMLCurrentScriptName = Testcasename;
		sXMLCurrentIterator = Iterator;

		// Check log file was created
		createResultfolder();

		// Check XML file was created and open the XMl file
		openXMLFile();

		// update functionality node
		addOrUpdateFunctionalityNode();

		// update testscript node
		addOrUpdateTestScriptNode();

		// update iterator node
		addOrUpdateIteratorNode();

		// update step node
		addOrUpdateStepNode(sStatus, sColumnName,newValue,oldValue,difference);

		// update status as "Fail" if the step is failed
		if (sStatus.equalsIgnoreCase("Fail")) {
			updateFailStatus();
		}

		testCasesCount = getTotalTestCaseCount();
		testcasesFailCount = getFailTestCaseCount();
		testcasesPassCount = getPassTestCaseCount();

		rootElement.setAttribute("TP", "" + testcasesPassCount);
		rootElement.setAttribute("TF", "" + testcasesFailCount);
		rootElement.setAttribute("TotalTestCases", "" + testCasesCount);

		// write the content into xml file
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void createResultfolder() {
		if ((new File(sTestResultFolderPath)).exists()) {
			return;
		}
		(new File(sTestResultFolderPath)).mkdir();
	}

	private static void openXMLFile() {
		if (!bCreateFile) {
			bCreateFile = true;
			String sResultFileName = "Results"+ now() + ".xml";
			sResultFileName = sResultFileName.replace(":", "");
			sResultFileName = sResultFileName.replace(" ", "");
			sResultXMLFilePath = sTestResultFolderPath + sPathSeparatorChar;
			sResultXMLFilePath = sResultXMLFilePath + sResultFileName;
			createXMLFile();
		}
	}

	private static void createXMLFile() {
		try {
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();

			Document xmlDocument = docBuilder.newDocument();
			xmlDocument.insertBefore(xmlDocument.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"NewResultForExcelCompare.xsl\""),
					xmlDocument.getDocumentElement());


			rootElement = xmlDocument.createElement("TestSuite");
			xmlDocument.appendChild(rootElement);

			// setting attributes
			rootElement.setAttribute("StartTime", now());
			rootElement.setAttribute("EndTime", now());
			rootElement.setAttribute("TotalTestCases", "" + testCasesCount);

			rootElement.setAttribute("TF", "" + testcasesFailCount);
			rootElement.setAttribute("TP", "" + testcasesPassCount);

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(xmlDocument);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));
			transformer.transform(source, result);
			changeToEditMode();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
		return sdf.format(cal.getTime());
	}

	private static void changeToEditMode() {
		try {
			file = new FileInputStream(new File(sResultXMLFilePath));
			doc = docBuilder.parse(file);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite";
			NodeList nodeList_ele = (NodeList) xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			rootElement = (Element) nodeList_ele;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void addOrUpdateFunctionalityNode() {
		if (!checkFunctionalNodeAvailable()) {
			testcasesFailCount = 0;
			testcasesPassCount = 0;
			Element ele1 = doc.createElement("Functionality");
			rootElement.appendChild(ele1);
			ele1.setAttribute("name", sXMLCurrentFunctionality);
			FuncElement = ele1;
		}

	}

	private static void addOrUpdateTestScriptNode() {
		if (!checkTestScriptNodeAvailable()) {
			Element ele = doc.createElement("TestScript");
			FuncElement.appendChild(ele);
			ele.setAttribute("name", sXMLCurrentScriptName);
			ele.setAttribute("StartTime", now());
			ele.setAttribute("EndTime", now());
			ele.setAttribute("TC_Status", "Pass");
			tsElement = ele;
		}
	}

	private static void addOrUpdateIteratorNode() {
		if (!checkIteratorNodeAvailable()) {
			Element ele = doc.createElement("Iterator");
			tsElement.appendChild(ele);
			ele.setAttribute("no", Integer.toString(Iterator));
			IteratorElement = ele;
		}
	}

	private static void addOrUpdateStepNode(String sStatus, String sColumnName,String newValue,String oldValue,String difference) {
		NodeList nl = IteratorElement.getChildNodes();
		int iStepNo = nl.getLength();

		Element el = doc.createElement("step");
		IteratorElement.appendChild(el);
		el.setAttribute("no", Integer.toString((iStepNo + 1)));
		Node eStep = IteratorElement.getLastChild();

		el = doc.createElement("status");
		Text txt = doc.createTextNode(sStatus);
		el.appendChild(txt);
		eStep.appendChild(el);

		el = doc.createElement("stepname");
		txt = doc.createTextNode(sColumnName);
		el.appendChild(txt);
		eStep.appendChild(el);

		el = doc.createElement("VEDA2.0");
		txt = doc.createTextNode("" + newValue);
		el.appendChild(txt);
		eStep.appendChild(el);

		el = doc.createElement("VEDA1.5");
		txt = doc.createTextNode("" + oldValue);
		el.appendChild(txt);
		eStep.appendChild(el);

		el = doc.createElement("Difference");
		txt = doc.createTextNode("" + difference);
		el.appendChild(txt);
		eStep.appendChild(el);

		el = doc.createElement("timestamp");
		txt = doc.createTextNode(now());
		el.appendChild(txt);
		eStep.appendChild(el);

		tsElement.setAttribute("EndTime", now());
		rootElement.setAttribute("EndTime", now());

		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean checkFunctionalNodeAvailable() {
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality[@name='"
					+ sXMLCurrentFunctionality + "']";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			NodeList nodeListElement = (NodeList) xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			if (nodeList.getLength() > 0) {
				FuncElement = (Element) nodeListElement;
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	private static boolean checkTestScriptNodeAvailable() {
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//Functionality[@name='"
					+ sXMLCurrentFunctionality + "']/TestScript[@name='"
					+ sXMLCurrentScriptName + "']";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			NodeList nodeListElement = (NodeList) xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			if (nodeList.getLength() > 0) {
				tsElement = (Element) nodeListElement;
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	private static boolean checkIteratorNodeAvailable() {
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//Functionality[@name='"
					+ sXMLCurrentFunctionality + "']/TestScript[@name='"
					+ sXMLCurrentScriptName + "']/Iterator[@no='"
					+ sXMLCurrentIterator + "']";
			NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
					doc, XPathConstants.NODESET);
			NodeList nodeListElement = (NodeList) xPath.compile(expression)
					.evaluate(doc, XPathConstants.NODE);
			if (nodeList.getLength() > 0) {
				IteratorElement = (Element) nodeListElement;
				return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}

	private static int getFailTestCaseCount() {
		NodeList nodeList = null;
		int iSum = 0;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality";
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc,
					XPathConstants.NODESET);
			for (int iCount = 1; iCount <= nodeList.getLength(); iCount++) {
				String sFailExpression = "//TestSuite/Functionality[" + iCount
						+ "]/TestScript[@TC_Status='Fail']";
				NodeList TCFailnodeList = (NodeList) xPath.compile(
						sFailExpression).evaluate(doc, XPathConstants.NODESET);
				iSum = iSum + TCFailnodeList.getLength();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return iSum;
	}

	private static int getPassTestCaseCount() {
		NodeList nodeList = null;
		int iSum = 0;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality";
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc,
					XPathConstants.NODESET);
			for (int iCount = 1; iCount <= nodeList.getLength(); iCount++) {
				String sPassExpression = "//TestSuite/Functionality[" + iCount
						+ "]/TestScript[@TC_Status='Pass']";
				NodeList TCPassNodeList = (NodeList) xPath.compile(
						sPassExpression).evaluate(doc, XPathConstants.NODESET);
				iSum = iSum + TCPassNodeList.getLength();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return iSum;
	}

	private static int getTotalTestCaseCount() {
		NodeList nodeList = null;
		int iSum = 0;
		try {
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//TestSuite/Functionality";
			nodeList = (NodeList) xPath.compile(expression).evaluate(doc,
					XPathConstants.NODESET);
			for (int iCount = 1; iCount <= nodeList.getLength(); iCount++) {
				String sTotalExpression = "//TestSuite/Functionality[" + iCount
						+ "]/TestScript";
				NodeList TCnodeList = (NodeList) xPath
						.compile(sTotalExpression).evaluate(doc,
								XPathConstants.NODESET);
				iSum = iSum + TCnodeList.getLength();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return iSum;
	}

	private static void updateFailStatus() {
		tsElement.setAttribute("TC_Status", "Fail");
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();

			Transformer transformer = transformerFactory.newTransformer();

			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(sResultXMLFilePath));

			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public static void test(){

		String filPath = "NewLogForExcelCompare//ResultFile05-02-2016201246.xml";
		Functionality = "Investor 1";
		Testcasename = "ClickToViewResults";
		logResults("Pass", "ColumnName1", "New Value", "Old Value", "Difference");
		logResults("Pass", "ColumnName2", "New Value", "Old Value", "Difference");
		logResults("Pass", "ColumnName3", "New Value", "Old Value", "Difference");
		Functionality = "Investor 2";
		Testcasename = "ClickToViewResults";
		logResults("Fail", "ColumnName2", "New Value", "Old Value", "Difference");
		logResults("Pass", "ColumnName3", "New Value", "Old Value", "Difference");
		logResults("Fail", "ColumnName5", "New Value", "Old Value", "Difference");
	}

	public static Map<String,Map<String,Map<String,String>>> getXMLDetailsMap(String sXMLFilePath){

		NodeList nodeList;
		Map<String,Map<String,Map<String,String>>> mapInvestorData = new HashMap<>();
		try {
			FileInputStream file = new FileInputStream(new File(sXMLFilePath));
			
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document xmlDocument = builder.parse(file);

			NodeList functionalityNodeList = xmlDocument.getElementsByTagName("Functionality");
			int fnCount = functionalityNodeList.getLength();
			for(int fnIndex = 0 ; fnIndex <fnCount ;fnIndex++)
			{
				Element fnEle = (Element)functionalityNodeList.item(fnIndex);				
				//Get the Investor Name
				String investorName = fnEle.getAttribute("name");		
				XPath xPath = XPathFactory.newInstance().newXPath();
				
				//Xpath for getting the Number of Steps for the particular investor 
				String expression = "//Functionality[@name='"+investorName+"']//step";
				nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDocument, XPathConstants.NODESET);
				Map<String,Map<String,String>> objMainMap = new HashMap<String,Map<String,String>>();
				
				//Loop the condition for Number of steps in that Steps
				for(int iOutLoopCounter =0;iOutLoopCounter<nodeList.getLength();iOutLoopCounter++)
				{	
					//Get the Child Nodes of the Steps
					NodeList childNodeList = nodeList.item(iOutLoopCounter).getChildNodes();
					//Initializing a Map to store the Child Nodes and it's values
					Map<String,String> objMsgDts = new HashMap<String,String>();
					//Putting the Investor name in the Map
					objMsgDts.put("Investor Name", investorName);					
					//Looping with the Child Node count 
					for(int iInnerLoopCounter =0; iInnerLoopCounter< childNodeList.getLength();iInnerLoopCounter++)
					{
						String sKey = childNodeList.item(iInnerLoopCounter).getNodeName();
						String sValue = childNodeList.item(iInnerLoopCounter).getTextContent();
						if(!sKey.contains("text")){
							objMsgDts.put(sKey, sValue);
						}						
					}					
					//Put the Steps(Columns Data) Map in the Row count basis for the Investor because Investor Can have multiple columns Data
					objMainMap.put("Row" + iOutLoopCounter,objMsgDts);
				}				
				//put the Total Columns data for a particular Investor based on the InvestorRow count
				mapInvestorData.put("InvestorRow"+fnIndex,objMainMap);
			}
			
			return mapInvestorData;
		}	
		
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}	
	}

}
