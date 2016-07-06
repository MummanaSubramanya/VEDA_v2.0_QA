package com.viteos.veda.transaction.transferscripts;

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
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeTransferAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class TransferCheckerReviewedOrReturned_CheckerOperations_TS4 {
	static boolean bStatus;
	static String sSheetName = "TestDataCheckerReviewdAtChecker";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="TransferTrade";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("TransferCheckerUserName"), Global.mapCredentials.get("TransferCheckerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void testCheckerOperations(){
		try {
			Map<String,  Map<String, String>> mapAllTransferDetails = Utilities.readMultipleTestData(Global.sTransferTestData, sSheetName , "Y");

			for (int index = 1; index <= mapAllTransferDetails.size() ; index++){
				Map<String, String> mapTransferDetails = mapAllTransferDetails.get("Row"+index);
				Reporting.Testcasename = mapTransferDetails.get("TestCaseName");
						
				if(mapTransferDetails.get("ExpectedResults").equalsIgnoreCase("Fail") || (!mapTransferDetails.get("OperationType").equalsIgnoreCase("Save") && !mapTransferDetails.get("OperationType").equalsIgnoreCase("Send For Review"))){
					continue;
				}
				if(mapTransferDetails.get("CheckerOperations").equalsIgnoreCase("None") || mapTransferDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("None")){
					continue;
				}
				Map<String ,String> mapXMLTransferDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "TradeTypeTRAN", mapTransferDetails.get("TestCaseName"));
				if(mapXMLTransferDetails == null){
					continue;
				}
				if (mapTransferDetails.get("New Investor TestCaseName") != null) {
					String ToInvestorNameCheckerReviewed = TradeTypeTransferAppFunctions.getInvestorNameFromTheInvestorTestData(mapTransferDetails);
					mapTransferDetails.put("To Investor Name",ToInvestorNameCheckerReviewed);
				}
				
				if(mapTransferDetails.get("New Holder")!=null && mapTransferDetails.get("New Holder TestCaseName")!=null){
					String ToHolderNameCheckerReviewedSheet = TradeTypeTransferAppFunctions.getHolderNameFromHolderTestData(mapTransferDetails);
					mapTransferDetails.put("To Holder Name",ToHolderNameCheckerReviewedSheet);
				}
					
					if(mapXMLTransferDetails.get("TransactionID")!=null){
					mapTransferDetails.put("TransactionID", mapXMLTransferDetails.get("TransactionID"));
				}
				
				TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
				bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
				if (!bStatus) {
					Reporting.logResults("Fail", "Navigation to the Dashboard in Transfer Module", "Unable to Navigate to Dashboard in Transfer Module");
					continue;
				}
				Reporting.logResults("Pass", "Navigation to the Dashboard in Transfer Module", "Successfully Navigate to Dashboard in Transfer Module");
				
				//Overriding Checker side verification values of Available Balances and pending Trades with the new column values.
			
				bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Transfer",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapTransferDetails.get("TransactionID"), mapTransferDetails);
				if (!bStatus) {
					Reporting.logResults("Fail", "Select the Created Transfer", "Failed to Select the Created Transfer. "+Messages.errorMsg);
					continue;
				}
											
				mapTransferDetails.put("ExpectedAvailableAmount",mapTransferDetails.get("CheckerExpectedAvailableAmount"));
				mapTransferDetails.put("ExpectedTotalAvailableAmount",mapTransferDetails.get("CheckerExpectedTotalAvailableAmount"));
				mapTransferDetails.put("ExpectedAvailableShares",mapTransferDetails.get("CheckerExpectedAvailableShares"));
				mapTransferDetails.put("ExpectedTotalAvailableShares",mapTransferDetails.get("CheckerExpectedTotalAvailableShares"));
				mapTransferDetails.put("ExpectedPendingRequestAmount",mapTransferDetails.get("CheckerExpectedPendingReqAmount"));
				mapTransferDetails.put("ExpectedPendingRequestedShares",mapTransferDetails.get("CheckerExpectedPendingReqShares"));
				mapTransferDetails.put("ExpectedActualBalance",mapTransferDetails.get("CheckerExpectedActualBalance"));
				mapTransferDetails.put("ExpectedTotalActualBalance",mapTransferDetails.get("CheckerExpectedTotalActualBalance"));
				mapTransferDetails.put("ExpectedActualShares",mapTransferDetails.get("CheckerExpectedActualShares"));
				mapTransferDetails.put("ExpectedTotalActualShares",mapTransferDetails.get("CheckerExpectedTotalActualShares"));
				
				
				bStatus = TradeTypeTransferAppFunctions.doVerifyTransferDetailsInChecker(mapTransferDetails, mapXMLTransferDetails);
				if(!bStatus){
					Reporting.logResults("Fail", "Verifying Trade Type Transfer Details in Checker", "Verification of Trade Type Transfer Details Failed in Checker.ERROR : "+Messages.errorMsg);
					continue;
				}
				
				Reporting.logResults("Pass", "Verifying Trade Type Transfer Details in Checker", "Verification of Trade Type Transfer details verified successfully in Checker");
				
				bStatus= TradeTypeTransferAppFunctions.doCheckerActionTypesOnTradeTypeTransfer(mapTransferDetails);						
				if(!bStatus  && mapTransferDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail", "Perform Checker Operation :'"+mapTransferDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapTransferDetails.get("CheckerOperations")+"' Failed .ERROR : "+Messages.errorMsg);
					continue;
				}
				if(bStatus && mapTransferDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass", "Perform Checker Operation :'"+mapTransferDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapTransferDetails.get("CheckerOperations")+"' Performed Successfully ");
					continue;
				}
				if(bStatus && mapTransferDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail", "Perform Checker Operation :'"+mapTransferDetails.get("CheckerOperations")+"'", "Performed Checker Operation :'"+mapTransferDetails.get("CheckerOperations")+"' with Negative Test Data");
					continue;
				}
				if(!bStatus && mapTransferDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass", "Perform Checker Operation :'"+mapTransferDetails.get("CheckerOperations")+"'", "Negative Test Case cannot Perform Checker Operation :'"+mapTransferDetails.get("CheckerOperations")+"' ERROR: "+Messages.errorMsg);
					continue;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
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