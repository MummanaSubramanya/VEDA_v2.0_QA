package com.viteos.veda.transaction.exchangetestscripts;

import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeTypeExchangeAppFunction;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class CheckerReviewedOrReturnedExchangeCheckerOperations_TS4 {
	static boolean bStatus;
	static String sSheetName = "TestDataCheckerReviewdAtChecker";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Exchange";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("ExchangeCheckerUserName"), Global.mapCredentials.get("ExchangeCheckerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void verifyCheckerOperations(){
		Map<String, Map<String, String>> mapAllExchangeDetails = Utilities.readMultipleTestData(Global.sExchangeTestData,sSheetName,"Y");

		for (int index = 1; index <= mapAllExchangeDetails.size() ; index++){
			Map<String, String> mapExchangeDetails = mapAllExchangeDetails.get("Row"+index);
			Reporting.Testcasename = mapExchangeDetails.get("TestCaseName");
			
			//get the investor map details if available
			if(mapExchangeDetails.get("ExpectedResults").equalsIgnoreCase("Fail") || (!mapExchangeDetails.get("OperationType").equalsIgnoreCase("Save") && !mapExchangeDetails.get("OperationType").equalsIgnoreCase("Send For Review"))){
				continue;
			}
			if(mapExchangeDetails.get("CheckerOperations").equalsIgnoreCase("None") || mapExchangeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("None")){
				continue;
			}
			
			Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "TradeTypeEXCN", mapExchangeDetails.get("TestCaseName"));
			if(mapXMLExchangeDetails == null || !mapXMLExchangeDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
				continue;
			}

			//Overriding Checker side verification values of Available Balances and pending Trades with the new column values.
			mapExchangeDetails.put("ExcpectedAvailableShares", mapExchangeDetails.get("CheckerExpectedAvailableShares"));
			mapExchangeDetails.put("ExcpectedAvailableAmount", mapExchangeDetails.get("CheckerExpectedAvailableAmount"));
			mapExchangeDetails.put("ExcpectedTotalAvailableShares", mapExchangeDetails.get("CheckerExpectedTotalAvailableShares"));
			mapExchangeDetails.put("ExcpectedTotalAvailableAmount", mapExchangeDetails.get("CheckerExpectedTotalAvailableAmount"));
			mapExchangeDetails.put("ExpectedPendingRequestAmount", mapExchangeDetails.get("CheckerExpectedPendingReqAmount"));
			mapExchangeDetails.put("ExpectedPendingRequestedShares", mapExchangeDetails.get("CheckerExpectedPendingReqShares"));
			mapExchangeDetails.put("ExpectedActualShares", mapExchangeDetails.get("CheckerExpectedActualShares"));
			mapExchangeDetails.put("ExpectedTotalActualShares", mapExchangeDetails.get("CheckerExpectedTotalActualShares"));
			mapExchangeDetails.put("ExpetedActualBalance", mapExchangeDetails.get("CheckerExpectedActualBalance"));
			mapExchangeDetails.put("ExpectedTotalActualBalance", mapExchangeDetails.get("CheckerExpectedTotalActualBalance"));
			
			
			if(mapXMLExchangeDetails.get("TransactionID")!=null){
				mapExchangeDetails.put("Transaction ID", mapXMLExchangeDetails.get("TransactionID"));
				//mapSubscriptionDetails.put("Transaction ID", "TR00000031");
			}

			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();

			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard", "Successfully Navigate to Dashboard");


			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Exchange",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapExchangeDetails.get("Transaction ID"), mapExchangeDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Select the Created Exchange", "Failed to Select the Created Exchange. "+Messages.errorMsg);
				continue;
			}
			
			bStatus = TradeTypeExchangeAppFunction.doVerifyCheckerReviewedExchangeTradeAtChecker(mapExchangeDetails);
			if(!bStatus){
				Reporting.logResults("Fail", "Verify Exchange Details in Checker", "Verification of Exchange Details Failed in Checker.ERROR : "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Verify Exchange Details in Checker", "Exchange details verified successfully in Checker");


			bStatus = TradeTypeExchangeAppFunction.doCheckerOperationsForExchangeTrade(mapExchangeDetails);
			if(!bStatus  && mapExchangeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Checker Operation :'"+mapExchangeDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapExchangeDetails.get("CheckerOperations")+"' Failed .ERROR : "+Messages.errorMsg);
				continue;
			}
			if(bStatus && mapExchangeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				Reporting.logResults("Pass", "Perform Checker Operation :'"+mapExchangeDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapExchangeDetails.get("CheckerOperations")+"' Peroformed Successfully ");
				continue;
			}
			if(bStatus && mapExchangeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Checker Operation :'"+mapExchangeDetails.get("CheckerOperations")+"'", "Performed Checker Operation :'"+mapExchangeDetails.get("CheckerOperations")+"' with Negative Test Data");
				continue;
			}
			if(!bStatus && mapExchangeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Checker Operation :'"+mapExchangeDetails.get("CheckerOperations")+"'", "Negative Test Case cannot Perform Checker Operation :'"+mapExchangeDetails.get("CheckerOperations")+"' ERROR: "+Messages.errorMsg);
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
