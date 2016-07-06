package com.viteos.veda.transaction.redemptiontestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

public class RedemptionCheckerOperation_TS2 {
	static boolean bStatus;
	static String sSheetName = "RedemptionTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Redemption";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("RedemptionCheckerUserName"), Global.mapCredentials.get("RedemptionCheckerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void vefifyCheckerOperations(){
		Map<String, Map<String, String>> mapAllRedemptionDetails = Utilities.readMultipleTestData(Global.sRedemptionTestData,sSheetName,"Y");

		for (int index = 1; index <= mapAllRedemptionDetails.size() ; index++){
			Map<String, String> mapRedemptionDetails = mapAllRedemptionDetails.get("Row"+index);
			Reporting.Testcasename = mapRedemptionDetails.get("TestCaseName");
			
			
			//get the investor map details if available
			if(mapRedemptionDetails.get("ExpectedResults").equalsIgnoreCase("Fail") || (!mapRedemptionDetails.get("OperationType").equalsIgnoreCase("Save") && !mapRedemptionDetails.get("OperationType").equalsIgnoreCase("Send For Review"))){
				continue;
			}
			if(mapRedemptionDetails.get("CheckerOperations").equalsIgnoreCase("None") || mapRedemptionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("None")){
				continue;
			}
			//Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML("XMLMessages//TradeTypeREDDetails02-23-2016235608.xml", "TradeTypeRED", mapRedemptionDetails.get("TestCaseName"));
			Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "TradeTypeRED", mapRedemptionDetails.get("TestCaseName"));
			if(mapXMLSubscriptionDetails == null || !mapXMLSubscriptionDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
				continue;
			}
			
			if (mapRedemptionDetails.get("Account ID") != null) {
				String accountId = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapRedemptionDetails.get("Account ID"));
				if(accountId != null){
					mapRedemptionDetails.put("Account ID", accountId);
				}				
			}
			if(mapXMLSubscriptionDetails.get("TransactionID")!=null){
				mapRedemptionDetails.put("Transaction ID", mapXMLSubscriptionDetails.get("TransactionID"));
				//mapSubscriptionDetails.put("Transaction ID", "TR00000031");
			}
			
			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
			
			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard ", "Successfully Navigate to Dashboard");
			
			
			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Redemption",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapRedemptionDetails.get("Transaction ID"), mapRedemptionDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Select the Created Redemption", "Failed to Select the Created Redemption. "+Messages.errorMsg);
				continue;
			}
			//Overriding Checker side verification values of Actual balance ,Available Balances and pending Trades with the new column values.(Because we are using the same function for Maker and Checker Verify)
			mapRedemptionDetails.put("ExcpectedActualBalance", mapRedemptionDetails.get("CheckerExpectedActualBalance"));
			mapRedemptionDetails.put("ExcpectedTotalActualBalance", mapRedemptionDetails.get("CheckerExpectedTotalActualBalance"));
			mapRedemptionDetails.put("ExcpectedActualShares", mapRedemptionDetails.get("CheckerExpectedActualShares"));
			mapRedemptionDetails.put("ExcpectedTotalActualShares", mapRedemptionDetails.get("CheckerExpectedTotalActualShare"));			
			mapRedemptionDetails.put("ExcpectedAvailableShares", mapRedemptionDetails.get("CheckerExpectedAvailableShares"));
			mapRedemptionDetails.put("ExcpectedAvailableAmount", mapRedemptionDetails.get("CheckerExpectedAvailableAmount"));
			mapRedemptionDetails.put("ExcpectedTotalAvailableShares", mapRedemptionDetails.get("CheckerExpectedTotalAvailableShares"));
			mapRedemptionDetails.put("ExcpectedTotalAvailableAmount", mapRedemptionDetails.get("CheckerExpectedTotalAvailableAmount"));
			mapRedemptionDetails.put("ExpectedPendingRequestAmount", mapRedemptionDetails.get("CheckerExpectedPendingReqAmount"));
			mapRedemptionDetails.put("ExpectedPendingRequestedShares", mapRedemptionDetails.get("CheckerExpectedPendingReqShares"));
			mapRedemptionDetails.put("ExcpectedIsUnderLockupStatus", mapRedemptionDetails.get("CheckerExpectedUnderLockUpStatus"));
			
			bStatus = TradeTypeRedemptionAppFunctions.doVerifyRedemptionInCheckerScreen(mapRedemptionDetails);
			if(!bStatus){
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", mapRedemptionDetails.get("TestCaseName"), "TradeTypeRED");
				Reporting.logResults("Fail", "Verify Redemption Details in Checker", "Verification of Redemption Details Failed in Checker.ERROR : "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Verify Redemption Details in Checker", "Redemption details verified successfully in Checker");
			
			
			bStatus = TradeTypeRedemptionAppFunctions.doCheckerActionTypesOnTradeRedemption(mapRedemptionDetails);
			if(!bStatus  && mapRedemptionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Checker Operation :'"+mapRedemptionDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapRedemptionDetails.get("CheckerOperations")+"' Failed .ERROR : "+Messages.errorMsg);
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", mapRedemptionDetails.get("TestCaseName"), "TradeTypeRED");
				continue;
			}
			if(bStatus && mapRedemptionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				Reporting.logResults("Pass", "Perform Checker Operation :'"+mapRedemptionDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapRedemptionDetails.get("CheckerOperations")+"' Peroformed Successfully ");
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "CheckerStatus", "Pass", "TestcaseName", mapRedemptionDetails.get("TestCaseName"), "TradeTypeRED");
				continue;
			}
			if(bStatus && mapRedemptionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Checker Operation :'"+mapRedemptionDetails.get("CheckerOperations")+"'", "Performed Checker Operation :'"+mapRedemptionDetails.get("CheckerOperations")+"' with Negative Test Data");
				continue;
			}
			if(!bStatus && mapRedemptionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Checker Operation :'"+mapRedemptionDetails.get("CheckerOperations")+"'", "Negative Test Case cannot Perform Checker Operation :'"+mapRedemptionDetails.get("CheckerOperations")+"' ERROR: "+Messages.errorMsg);
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
