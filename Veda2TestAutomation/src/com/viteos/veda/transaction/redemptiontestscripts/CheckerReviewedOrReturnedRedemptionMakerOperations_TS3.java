package com.viteos.veda.transaction.redemptiontestscripts;

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
import com.viteos.veda.master.lib.TradeTypeRedemptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSwitchAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class CheckerReviewedOrReturnedRedemptionMakerOperations_TS3 {
	static boolean bStatus;
	static String sSheetName = "TestDataForCheckerReviewd";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Redemption";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("RedemptionMakerUserName"), Global.mapCredentials.get("RedemptionMakerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}
	@org.testng.annotations.Test
	public static void test(){
		Map<String, Map<String, String>> mapAllRedemptionDetails = Utilities.readMultipleTestData(Global.sRedemptionTestData,sSheetName,"Y");

		for(int index = 1;index <= mapAllRedemptionDetails.size();index++){
			Map<String, String> mapRedemptionDetails = mapAllRedemptionDetails.get("Row"+index);

			Reporting.Testcasename = mapRedemptionDetails.get("TestCaseName");
			
			Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "TradeTypeRED", mapRedemptionDetails.get("TestCaseName"));
			if(mapXMLSubscriptionDetails == null || !mapXMLSubscriptionDetails.get("CheckerStatus").equalsIgnoreCase("Pass")){
				continue;
			}
			if(mapXMLSubscriptionDetails.get("TransactionID")!=null){
				mapRedemptionDetails.put("Transaction ID", mapXMLSubscriptionDetails.get("TransactionID"));
				
			}
			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard", "Successfully Navigate to Dashboard");
			
			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Redemption",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapRedemptionDetails.get("Transaction ID"), mapRedemptionDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Select the Created Redemption", "Failed to Select the Created Redemption. "+Messages.errorMsg);
				continue;
			}
			TradeTypeRedemptionAppFunctions.isRedemptionForCheckerReviewedCase = true;
			
			if(mapRedemptionDetails.get("Account ID") != null){
				String accountId = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapRedemptionDetails.get("Account ID"));
				if(accountId != null){
					mapRedemptionDetails.put("Account ID", accountId);
				}
			}	
			
			bStatus = TradeTypeRedemptionAppFunctions.doFillCheckerReviewdTransactios(mapRedemptionDetails);
			
			if(bStatus && mapRedemptionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				if(mapRedemptionDetails.get("OperationType").equalsIgnoreCase("Save") || mapRedemptionDetails.get("OperationType").equalsIgnoreCase("Send For Review")){
					Reporting.logResults("Pass", "Perform Maker Operation :'"+mapRedemptionDetails.get("OperationType")+"'", "Performed Redemption Maker Operation '"+mapRedemptionDetails.get("OperationType")+"' Successfully ");
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "MakerStatus", "Pass", "TestcaseName", mapRedemptionDetails.get("TestCaseName"), "TradeTypeRED");
					continue;
				}
				if(mapRedemptionDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapRedemptionDetails.get("OperationType").equalsIgnoreCase("Clear") ){
					Reporting.logResults("Pass", "Perform Redemption Maker Operation: "+mapRedemptionDetails.get("OperationType")+"", "Performed Maker Operation '"+mapRedemptionDetails.get("OperationType")+"' Successfully ");
					continue;
				}
				
			}
			
			if(bStatus && mapRedemptionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Maker Operation :'"+mapRedemptionDetails.get("OperationType")+"'", "Performed Redemption Maker Operation '"+mapRedemptionDetails.get("OperationType")+"' with Negative Test Data");
				continue;
			}
			
			if(!bStatus && mapRedemptionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Maker Operation :'"+mapRedemptionDetails.get("OperationType")+"'", "Failed to Perform Redemption Maker Operation '"+mapRedemptionDetails.get("OperationType")+"'.ERROR : "+Messages.errorMsg);
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapRedemptionDetails.get("TestCaseName"), "TradeTypeRED");
				NewUICommonFunctions.handleAlert();
				continue;
			}
			
			if(!bStatus && mapRedemptionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Maker Operation :'"+mapRedemptionDetails.get("OperationType")+"'", "Negative Test Data Cannot Perform Redemption Maker Operation '"+mapRedemptionDetails.get("OperationType")+"' .ERROR : "+Messages.errorMsg);
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
