package com.viteos.veda.transaction.switchtestscripts;

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
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSwitchAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class CheckerReviewedOrReturnedCheckerOperations_TC4 {
	static boolean bStatus;
	static String sSheetName = "CRCheckerDetailsToVerify";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Switch Trade";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("SwitchCheckerUserName"), Global.mapCredentials.get("SwitchCheckerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Checker Login into application", "Checker Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Checker Login into application ", "Checker Login into application successfull");
	}
	
	@Test
	public static void vefifyCheckerOperations(){
		Map<String, Map<String, String>> mapAllSwitchDetails = Utilities.readMultipleTestData(Global.sSwitchTestData,sSheetName,"Y");
		
		for (int index = 1; index <= mapAllSwitchDetails.size() ; index++){
			Map<String, String> mapSwitchDetails = mapAllSwitchDetails.get("Row"+index);
			Reporting.Testcasename = mapSwitchDetails.get("TestCaseName");

			//get the investor map details if available
			if(mapSwitchDetails.get("ExpectedResults").equalsIgnoreCase("Fail") || (!mapSwitchDetails.get("OperationType").equalsIgnoreCase("Save") && !mapSwitchDetails.get("OperationType").equalsIgnoreCase("Send For Review"))){
				continue;
			}
			if(mapSwitchDetails.get("CheckerOperations").equalsIgnoreCase("None") || mapSwitchDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("None")){
				continue;
			}
			Map<String ,String> mapXMLSwitchDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "TradeTypeSWITCH", mapSwitchDetails.get("TestCaseName"));
			if(mapXMLSwitchDetails == null || !mapXMLSwitchDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
				continue;
			}
			if(mapXMLSwitchDetails.get("TransactionID")!=null){
				mapSwitchDetails.put("Transaction ID", mapXMLSwitchDetails.get("TransactionID"));
				//mapSubscriptionDetails.put("Transaction ID", "TR00000031");
			}
			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard", "Successfully Navigate to Dashboard");

			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Switch",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapSwitchDetails.get("Transaction ID"), mapSwitchDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Select the Created Switch", "Failed to Select the Created Switch. "+Messages.errorMsg);
				continue;
			}
			//Overriding Checker side verification values of Available Balances and pending Trades with the new column values.
			
			mapSwitchDetails.put("ExpectedActualBalance", mapSwitchDetails.get("CheckerExpectedActulaBalance"));
			mapSwitchDetails.put("ExpectedTotalActualBalance", mapSwitchDetails.get("CheckerExpectedTotalActulaBalance"));
			mapSwitchDetails.put("ExpectedActualShares", mapSwitchDetails.get("CheckerExpectedActualShare"));
			mapSwitchDetails.put("ExpectedTotalActualShares", mapSwitchDetails.get("CheckerExpectedTotalActualShare"));			
			mapSwitchDetails.put("ExpectedAvailableShares", mapSwitchDetails.get("CheckerExpectedAvailableShares"));
			mapSwitchDetails.put("ExpectedAvailableAmount", mapSwitchDetails.get("CheckerExpectedAvailableAmount"));
			mapSwitchDetails.put("ExpectedTotalAvailableShares", mapSwitchDetails.get("CheckerExpectedTotalAvailableShares"));
			mapSwitchDetails.put("ExpectedTotalAvailableAmount", mapSwitchDetails.get("CheckerExpectedTotalAvailableAmount"));
			mapSwitchDetails.put("ExpectedPendingRequestAmount", mapSwitchDetails.get("CheckerExpectedPendingReqAmount"));
			mapSwitchDetails.put("ExpectedPendingRequestedShares", mapSwitchDetails.get("CheckerExpectedPendingReqShares"));
			
			bStatus = TradeTypeSwitchAppFunctions.doTriggerCheckerValidationtionFunctions(mapSwitchDetails);			
			if(!bStatus){
				Reporting.logResults("Fail", "Verify Switch Details in Checker", "Verification of Switch Details Failed in Checker.ERROR : "+Messages.errorMsg);
				continue;
			}

			bStatus = TradeTypeSwitchAppFunctions.performCheckerOperationOnTrade(mapSwitchDetails);			
			if(!bStatus  && mapSwitchDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Switch Checker Operation :'"+mapSwitchDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapSwitchDetails.get("CheckerOperations")+"' Failed .ERROR : "+Messages.errorMsg);
				continue;
			}
			if(bStatus && mapSwitchDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				Reporting.logResults("Pass", "Perform Switch Checker Operation :'"+mapSwitchDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapSwitchDetails.get("CheckerOperations")+"' Peroformed Successfully ");
				continue;
			}
			if(bStatus && mapSwitchDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Switch Checker Operation :'"+mapSwitchDetails.get("CheckerOperations")+"'", "Performed Checker Operation :'"+mapSwitchDetails.get("CheckerOperations")+"' with Negative Test Data");
				continue;
			}
			if(!bStatus && mapSwitchDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Switch Checker Operation :'"+mapSwitchDetails.get("CheckerOperations")+"'", "Negative Test Case cannot Perform Checker Operation :'"+mapSwitchDetails.get("CheckerOperations")+"' ERROR: "+Messages.errorMsg);
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
