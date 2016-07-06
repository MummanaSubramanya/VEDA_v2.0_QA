package com.viteos.veda.transaction.exchangetestscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeTypeExchangeAppFunction;
import com.viteos.veda.master.lib.XMLLibrary;

public class CreatingMakerExchange_TS1 {
	static boolean bStatus;
	static String sSheetName = "ExchangeTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Exchange";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("ExchangeMakerUserName"), Global.mapCredentials.get("ExchangeMakerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}
	
	@org.testng.annotations.Test
	public static void test(){
		Map<String, Map<String, String>> mapAllExchangeDetails = Utilities.readMultipleTestData(Global.sExchangeTestData,sSheetName,"Y");

		for(int index = 1;index <= mapAllExchangeDetails.size();index++){
			Map<String, String> mapExchangeDetails = mapAllExchangeDetails.get("Row"+index);

			Reporting.Testcasename = mapExchangeDetails.get("TestCaseName");
			
			NewUICommonFunctions.refreshThePage();
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("TRADES","Exchange");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Exchange Trade", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Exchange Trade", "Exchange Menu selected succesfully");
			
			bStatus = TradeTypeExchangeAppFunction.doFillExchangeTradeDetails(mapExchangeDetails);
			
			if(bStatus && mapExchangeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				if(mapExchangeDetails.get("OperationType").equalsIgnoreCase("Save") || mapExchangeDetails.get("OperationType").equalsIgnoreCase("Send For Review") ){
					String transactionID = NewUICommonFunctions.getIDFromSuccessMessage();
					if (transactionID == null) {
						Reporting.logResults("Fail", "Get transaction ID from success Message of Exchange creation.", "Transaction ID Wasn't Displayed in the Success Message.");
						continue;
					}
					//adding testcase name and id to xml library
					Map<String, String> mapExchangeXMLMap =  new HashMap<String, String>();
					mapExchangeXMLMap.put("TestcaseName", mapExchangeDetails.get("TestCaseName"));
					mapExchangeXMLMap.put("Transaction ID", transactionID);
					mapExchangeXMLMap.put("MakerStatus", "Pass");
					mapExchangeXMLMap.put("CheckerStatus", "None");
					Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "TradeTypeEXCN", mapExchangeDetails.get("TestCaseName"));
					if(mapXMLExchangeDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "TransactionID", transactionID, "TestcaseName", mapExchangeDetails.get("TestCaseName"), "TradeTypeEXCN");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "MakerStatus", "Pass", "TestcaseName", mapExchangeDetails.get("TestCaseName"), "TradeTypeEXCN");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "CheckerStatus", "None", "TestcaseName", mapExchangeDetails.get("TestCaseName"), "TradeTypeEXCN");
					}else{
						XMLLibrary.writeTradeTypeEXCNDetailsToXML(mapExchangeXMLMap);
					}					
					Reporting.logResults("Pass", "Perform Maker Operation :'"+mapExchangeDetails.get("OperationType")+"'", "Performed Exchange Maker Operation '"+mapExchangeDetails.get("OperationType")+"' Successfully with Transaction ID : "+transactionID);
					continue;
				}
				if(mapExchangeDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapExchangeDetails.get("OperationType").equalsIgnoreCase("Clear") ){
					Reporting.logResults("Pass", "Perform Exchange Maker Operation: "+mapExchangeDetails.get("OperationType")+"", "Performed Maker Operation '"+mapExchangeDetails.get("OperationType")+"' Successfully ");
					continue;
				}	
				
			}
			
			if(bStatus && mapExchangeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Maker Operation :'"+mapExchangeDetails.get("OperationType")+"'", "Performed Exchange Maker Operation '"+mapExchangeDetails.get("OperationType")+"' with Negative Test Data");
				continue;
			}
			
			if(!bStatus && mapExchangeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Maker Operation :'"+mapExchangeDetails.get("OperationType")+"'", "Failed to Perform Exchange Maker Operation '"+mapExchangeDetails.get("OperationType")+"'.ERROR : "+Messages.errorMsg);
				Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "TradeTypeEXCN", mapExchangeDetails.get("TestCaseName"));
				if(mapXMLExchangeDetails != null){
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapExchangeDetails.get("TestCaseName"), "TradeTypeEXCN");
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "CheckerStatus", "None", "TestcaseName", mapExchangeDetails.get("TestCaseName"), "TradeTypeEXCN");
				}
				NewUICommonFunctions.handleAlert();
				continue;
			}
			
			if(!bStatus && mapExchangeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Maker Operation :'"+mapExchangeDetails.get("OperationType")+"'", "Negative Test Data Cannot Perform Exchange Maker Operation '"+mapExchangeDetails.get("OperationType")+"' .ERROR : "+Messages.errorMsg);
				NewUICommonFunctions.handleAlert();
				continue;
			}
			
		}
	}
	
	@AfterMethod
	public static void tearDown(){
		
		Reporting.Testcasename = "Close Application";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
