package com.viteos.veda.transaction.subscriptiontestscripts;

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
import com.viteos.veda.master.lib.XMLLibrary;

public class CreateSubscription_TestScript_TS1 {
	static boolean bStatus;
	static String sSheetName = "SubscriptionTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Subscription";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("SubscriptionMakerUserName"), Global.mapCredentials.get("SubscriptionMakerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@org.testng.annotations.Test
	public static void test(){
		Map<String, Map<String, String>> mapAllSubscriptionDetails = Utilities.readMultipleTestData(Global.sSubscriptionTestData,sSheetName,"Y");

		for(int index = 1;index <= mapAllSubscriptionDetails.size();index++){
			Map<String, String> mapSubscriptionDetails = mapAllSubscriptionDetails.get("Row"+index);

			Reporting.Testcasename = mapSubscriptionDetails.get("TestCaseName");
			
			NewUICommonFunctions.refreshThePage();
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("TRADES","Subscription");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Subscription Trade", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Subscription Trade", "Subscription Menu selected succesfully");

			bStatus = TradeTypeSubscriptionAppFunctions.doFillSubscriptionDetails(mapSubscriptionDetails);
			String makerStatus = "";
			if(bStatus && mapSubscriptionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				if(mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Save") || mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Send For Review")){
					makerStatus = "Pass";
					String transactionID = NewUICommonFunctions.getIDFromSuccessMessage();
					if (transactionID == null) {
						Reporting.logResults("Fail", "Get transaction ID from success Message of Subscription creation.", "Transaction ID Wasn't Displayed in the Success Message.");
						continue;
					}
					//adding testcase name and id to xml library
					Map<String, String> mapSubscriptionXMLMap =  new HashMap<String, String>();
					mapSubscriptionXMLMap.put("TestcaseName", mapSubscriptionDetails.get("TestCaseName"));
					mapSubscriptionXMLMap.put("Transaction ID", transactionID);	
					mapSubscriptionXMLMap.put("MakerStatus", makerStatus);	
					mapSubscriptionXMLMap.put("CheckerStatus", "None");
					if(mapSubscriptionDetails.get("NewAccount")!=null && mapSubscriptionDetails.get("NewAccount").equalsIgnoreCase("Yes") && !TradeTypeSubscriptionAppFunctions.newAccountID.equalsIgnoreCase("")){

						mapSubscriptionXMLMap.put("Account ID", TradeTypeSubscriptionAppFunctions.newAccountID);
					}
					
					Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapSubscriptionDetails.get("TestCaseName"));
					if(mapXMLSubscriptionDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "TransactionID", transactionID, "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "MakerStatus", makerStatus, "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "CheckerStatus", "None", "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "AccountID", TradeTypeSubscriptionAppFunctions.newAccountID, "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
					}else{
						XMLLibrary.writeTradeTypeSUBDetailsToXML(mapSubscriptionXMLMap);
					}
					
					Reporting.logResults("Pass", "Perform Subscription Maker Operation: "+mapSubscriptionDetails.get("OperationType")+"", "Performed Maker Operation '"+mapSubscriptionDetails.get("OperationType")+"' Successfully with Transaction ID : "+transactionID);
					continue;
				}
				if(mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Clear") ){
					Reporting.logResults("Pass", "Perform Subscription Maker Operation: "+mapSubscriptionDetails.get("OperationType")+"", "Performed Maker Operation '"+mapSubscriptionDetails.get("OperationType")+"' Successfully ");
					continue;
				}
				
			}
			
			if(bStatus && mapSubscriptionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Maker Operation for Subscription: "+mapSubscriptionDetails.get("OperationType")+"", "Peroformed Maker Operation for Subscription: "+mapSubscriptionDetails.get("OperationType")+" with Negative Test Data");
				continue;
			}
			
			if(!bStatus && mapSubscriptionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Maker Operation for Subscription: "+mapSubscriptionDetails.get("OperationType")+"", "Failed to Perform Maker Operation '"+mapSubscriptionDetails.get("OperationType")+"' for Subscrition.ERROR : "+Messages.errorMsg);
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapSubscriptionDetails.get("TestCaseName"));
				if(mapXMLSubscriptionDetails != null){
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
				}				
				NewUICommonFunctions.handleAlert();
				continue;
			}
			
			if(!bStatus && mapSubscriptionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Maker Operation for Subscription: "+mapSubscriptionDetails.get("OperationType")+"", "Negative Test Data Cannot Perform Maker Operation '"+mapSubscriptionDetails.get("OperationType")+"' for Subscription .ERROR : "+Messages.errorMsg);
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
