package com.viteos.veda.transaction.redemptiontestscripts;

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
import com.viteos.veda.master.lib.TradeTypeRedemptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSwitchAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class CheckerReviewedOrReturnedRedemptionCheckerOperation_TS4 {
	static boolean bStatus;
	static String sSheetName = "TestDataCheckerReviewdAtChecker";

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
		Map<String, Map<String, String>> mapAllCheckerReviewdDetails = Utilities.readMultipleTestData(Global.sRedemptionTestData,sSheetName,"Y");

		for (int index = 1; index <= mapAllCheckerReviewdDetails.size() ; index++){
			Map<String, String> mapCheckerReviewdDetails = mapAllCheckerReviewdDetails.get("Row"+index);
			Reporting.Testcasename = mapCheckerReviewdDetails.get("TestCaseName");
			
			
			if(mapCheckerReviewdDetails.get("ExpectedResults").equalsIgnoreCase("Fail") ||  !mapCheckerReviewdDetails.get("OperationType").equalsIgnoreCase("Save")){
				continue;
			}
			if(mapCheckerReviewdDetails.get("CheckerOperations").equalsIgnoreCase("None") || mapCheckerReviewdDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("None")){
				continue;
			}
			Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "TradeTypeRED", mapCheckerReviewdDetails.get("TestCaseName"));
			if(mapXMLSubscriptionDetails == null || !mapXMLSubscriptionDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
				continue;
			}
			
			if (mapCheckerReviewdDetails.get("Account ID") != null) {
				String accountId = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapCheckerReviewdDetails.get("Account ID"));
				if(accountId != null){
					mapCheckerReviewdDetails.put("Account ID", accountId);
				}
			}
			if(mapXMLSubscriptionDetails.get("TransactionID")!=null){
				mapCheckerReviewdDetails.put("Transaction ID", mapXMLSubscriptionDetails.get("TransactionID"));
			}
			
			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
			
			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard ", "Successfully Navigate to Dashboard");
			
			
			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Redemption",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapCheckerReviewdDetails.get("Transaction ID"), mapCheckerReviewdDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Select the Created Redemption", "Failed to Select the Created Redemption. "+Messages.errorMsg);
				continue;
			}
			//Overriding Checker side verification values of Available Balances and pending Trades with the new column values.
			mapCheckerReviewdDetails.put("ExcpectedActualBalance", mapCheckerReviewdDetails.get("CheckerExpectedActualBalance"));
			mapCheckerReviewdDetails.put("ExcpectedTotalActualBalance", mapCheckerReviewdDetails.get("CheckerExpectedTotalActualBalance"));
			mapCheckerReviewdDetails.put("ExcpectedActualShares", mapCheckerReviewdDetails.get("CheckerExpectedActualShares"));
			mapCheckerReviewdDetails.put("ExcpectedTotalActualShares", mapCheckerReviewdDetails.get("CheckerExpectedTotalActualShare"));
			mapCheckerReviewdDetails.put("ExcpectedAvailableShares", mapCheckerReviewdDetails.get("CheckerExpectedAvailableShares"));
			mapCheckerReviewdDetails.put("ExcpectedAvailableAmount", mapCheckerReviewdDetails.get("CheckerExpectedAvailableAmount"));
			mapCheckerReviewdDetails.put("ExcpectedTotalAvailableShares", mapCheckerReviewdDetails.get("CheckerExpectedTotalAvailableShares"));
			mapCheckerReviewdDetails.put("ExcpectedTotalAvailableAmount", mapCheckerReviewdDetails.get("CheckerExpectedTotalAvailableAmount"));
			mapCheckerReviewdDetails.put("ExpectedPendingRequestAmount", mapCheckerReviewdDetails.get("CheckerExpectedPendingReqAmount"));
			mapCheckerReviewdDetails.put("ExpectedPendingRequestedShares", mapCheckerReviewdDetails.get("CheckerExpectedPendingReqShares"));
			mapCheckerReviewdDetails.put("ExcpectedIsUnderLockupStatus", mapCheckerReviewdDetails.get("CheckerExpectedUnderLockUpStatus"));

			
			bStatus = TradeTypeRedemptionAppFunctions.doVerifyRedemptionInCheckerScreen(mapCheckerReviewdDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Verify Redemption Details in Checker", "Verification of Redemption Details Failed in Checker.ERROR : "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Verify Redemption Details in Checker", "Redemption details verified successfully in Checker");
			
			
			bStatus = TradeTypeRedemptionAppFunctions.doCheckerActionTypesOnTradeRedemption(mapCheckerReviewdDetails);
			if(!bStatus  && mapCheckerReviewdDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Checker Operation :'"+mapCheckerReviewdDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapCheckerReviewdDetails.get("CheckerOperations")+"' Failed .ERROR : "+Messages.errorMsg);
				continue;
			}
			if(bStatus && mapCheckerReviewdDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				Reporting.logResults("Pass", "Perform Checker Operation :'"+mapCheckerReviewdDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapCheckerReviewdDetails.get("CheckerOperations")+"' Peroformed Successfully ");
				continue;
			}
			if(bStatus && mapCheckerReviewdDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Checker Operation :'"+mapCheckerReviewdDetails.get("CheckerOperations")+"'", "Performed Checker Operation :'"+mapCheckerReviewdDetails.get("CheckerOperations")+"' with Negative Test Data");
				continue;
			}
			if(!bStatus && mapCheckerReviewdDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Checker Operation :'"+mapCheckerReviewdDetails.get("CheckerOperations")+"'", "Negative Test Case cannot Perform Checker Operation :'"+mapCheckerReviewdDetails.get("CheckerOperations")+"' ERROR: "+Messages.errorMsg);
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
