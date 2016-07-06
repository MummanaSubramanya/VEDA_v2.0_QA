package com.viteos.veda.transaction.switchtestscripts;

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
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSwitchAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class Switch_MakerOperations_TS1 {
	static boolean bStatus;
	static String sSheetName = "SwitchTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Switch";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("SwitchMakerUserName"), Global.mapCredentials.get("SwitchMakerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Maker Login into application", "Maker Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Maker Login into application ", "Maker Login into application is successfull");
	}
	
	@org.testng.annotations.Test
	public static void test(){
		Map<String, Map<String, String>> mapAllSwitchDetails = Utilities.readMultipleTestData(Global.sSwitchTestData,sSheetName,"Y");

		for(int index = 1;index <= mapAllSwitchDetails.size();index++){
			Map<String, String> mapSwitchDetails = mapAllSwitchDetails.get("Row"+index);

			Reporting.Testcasename = mapSwitchDetails.get("TestCaseName");
			
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("TRADES","Switch");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Switch Trade", "Switch Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Switch Trade", "Switch Menu selected succesfully");
			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
			bStatus = TradeTypeSwitchAppFunctions.doTriggerMakerFillAndVerificationFunctions(mapSwitchDetails);
			
			//Storing test case name, Txn id and newly created Account ID in xml file.
			Map<String, String> mapSwitchXMLMap =  new HashMap<String, String>();
			if(bStatus && mapSwitchDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				if(mapSwitchDetails.get("OperationType").equalsIgnoreCase("Save") || mapSwitchDetails.get("OperationType").equalsIgnoreCase("Send For Review") ){
					String transactionID = NewUICommonFunctions.getIDFromSuccessMessage();
					if (transactionID == null) {
						Reporting.logResults("Fail", "Get transaction ID from success Message of Switch creation.", "Transaction ID Wasn't Displayed in the Success Message.");
						continue;
					}					
					mapSwitchXMLMap.put("TestcaseName", mapSwitchDetails.get("TestCaseName"));
					mapSwitchXMLMap.put("Transaction ID", transactionID);					
					mapSwitchXMLMap.put("MakerStatus", "Pass");
					mapSwitchXMLMap.put("CheckerStatus", "None");
					mapSwitchXMLMap.put("Account ID", "");
					if(mapSwitchDetails.get("NewAccount")!=null && mapSwitchDetails.get("NewAccount").equalsIgnoreCase("Yes") && !TradeTypeSwitchAppFunctions.newAccountID.equalsIgnoreCase("")){

						mapSwitchXMLMap.put("Account ID", TradeTypeSwitchAppFunctions.newAccountID);
					}
					Map<String ,String> mapXMLSwitchDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "TradeTypeSWITCH", mapSwitchDetails.get("TestCaseName"));
					if(mapXMLSwitchDetails != null && mapXMLSwitchDetails.get("TestcaseName") != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "AccountID", mapSwitchXMLMap.get("Account ID"), "TestcaseName", Reporting.Testcasename, "TradeTypeSWITCH");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "MakerStatus", mapSwitchXMLMap.get("MakerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSWITCH");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "CheckerStatus", mapSwitchXMLMap.get("CheckerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSWITCH");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "TransactionID", mapSwitchXMLMap.get("Transaction ID"), "TestcaseName", Reporting.Testcasename, "TradeTypeSWITCH");
					}else{
						XMLLibrary.writeTradeTypeSWITCHDetailsToXML(mapSwitchXMLMap);
					}					
					Reporting.logResults("Pass", "Perform Switch Maker Operation :'"+mapSwitchDetails.get("OperationType")+"'", "Performed Switch Maker Operation '"+mapSwitchDetails.get("OperationType")+"' Successfully with Transaction ID : "+transactionID);
					continue;
				}
				if(mapSwitchDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapSwitchDetails.get("OperationType").equalsIgnoreCase("Clear") ){
					Reporting.logResults("Pass", "Perform Switch Maker Operation: "+mapSwitchDetails.get("OperationType")+"", "Performed Switch Maker Operation '"+mapSwitchDetails.get("OperationType")+"' Successfully ");
					continue;
				}				
			}		
			if(bStatus && mapSwitchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){				
				Reporting.logResults("Fail", "Perform Switch Maker Operation :'"+mapSwitchDetails.get("OperationType")+"'", "Performed Switch Maker Operation '"+mapSwitchDetails.get("OperationType")+"' with Negative Test Data");
				continue;				
			}			
			if(!bStatus && mapSwitchDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				
				Reporting.logResults("Fail", "Perform Switch Maker Operation :'"+mapSwitchDetails.get("OperationType")+"'", "Failed to Perform Switch Maker Operation '"+mapSwitchDetails.get("OperationType")+"'.ERROR : "+Messages.errorMsg);
				Map<String ,String> mapXMLSwitchDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "TradeTypeSWITCH", mapSwitchDetails.get("TestCaseName"));
				if(mapXMLSwitchDetails != null){
					mapSwitchXMLMap.put("MakerStatus","Fail");
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "MakerStatus", mapSwitchXMLMap.get("MakerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSWITCH");
				}
				NewUICommonFunctions.handleAlert();
				continue;
				
			}			
			if(!bStatus && mapSwitchDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){				
				Reporting.logResults("Pass", "Perform Switch Maker Operation :'"+mapSwitchDetails.get("OperationType")+"'", "Negative Test Data Cannot Perform Switch Maker Operation '"+mapSwitchDetails.get("OperationType")+"' .ERROR : "+Messages.errorMsg);
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
