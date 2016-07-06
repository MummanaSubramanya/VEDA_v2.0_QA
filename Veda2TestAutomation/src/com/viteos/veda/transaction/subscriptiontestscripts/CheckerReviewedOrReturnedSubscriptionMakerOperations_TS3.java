package com.viteos.veda.transaction.subscriptiontestscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class CheckerReviewedOrReturnedSubscriptionMakerOperations_TS3 {
	static boolean bStatus;
	static String sSheetName = "CheckerReviewedTestData";
	

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

	@Test
	public static void vefifyCheckerOperations(){
		Map<String,  Map<String, String>> mapAllSubscriptionDetails = Utilities.readMultipleTestData(Global.sSubscriptionTestData, sSheetName , "Y");

		for (int index = 1; index <= mapAllSubscriptionDetails.size() ; index++){
			Map<String, String> mapSubscriptionDetails = mapAllSubscriptionDetails.get("Row"+index);
			Reporting.Testcasename = mapSubscriptionDetails.get("TestCaseName");

			Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapSubscriptionDetails.get("TestCaseName"));
			if(mapXMLSubscriptionDetails == null || !mapXMLSubscriptionDetails.get("CheckerStatus").equalsIgnoreCase("Pass")){
				continue;
			}
			TradeTypeSubscriptionAppFunctions.isInkindFromCheckerReviewedScreen = true;
			if(mapXMLSubscriptionDetails.get("TransactionID")!=null){
				mapSubscriptionDetails.put("Transaction ID", mapXMLSubscriptionDetails.get("TransactionID"));
				//mapSubscriptionDetails.put("Transaction ID", "TR00000031");
			}

			//get the details of respective map and put to subscription map

			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();

			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard", "Successfully Navigate to Dashboard");

			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Subscription",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapSubscriptionDetails.get("Transaction ID"), mapSubscriptionDetails);
			if (!bStatus) {
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
				Reporting.logResults("Fail", "Select the Checker Reviewed Subscription", "Failed to Select the Checker Reviewed Subscription. "+Messages.errorMsg);
				continue;
			}

			bStatus = TradeTypeSubscriptionAppFunctions.doFillSubscriptionDetails(mapSubscriptionDetails);

			if(bStatus && mapSubscriptionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				if(mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Save") || mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Send For Review")){
					if(mapSubscriptionDetails.get("NewAccount") != null && mapSubscriptionDetails.get("NewAccount").equalsIgnoreCase("Yes")){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "MakerStatus", "Pass", "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "AccountID", TradeTypeSubscriptionAppFunctions.newAccountID, "TestcaseName", Reporting.Testcasename, "TradeTypeSUB");
					}
					Reporting.logResults("Pass", "Perform Subscription Maker Operation: "+mapSubscriptionDetails.get("OperationType")+"", "Performed Maker Operation '"+mapSubscriptionDetails.get("OperationType")+"' Successfully with Transaction ID : "+mapSubscriptionDetails.get("Transaction ID"));
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
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
				Reporting.logResults("Fail", "Perform Maker Operation for Subscription: "+mapSubscriptionDetails.get("OperationType")+"", "Failed to Perform Maker Operation '"+mapSubscriptionDetails.get("OperationType")+"' for Subscrition.ERROR : "+Messages.errorMsg);
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
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error ");
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}

}
