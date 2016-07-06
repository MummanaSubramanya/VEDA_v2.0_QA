package com.viteos.veda.transaction.sidepocketredemptionscripts;

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
import com.viteos.veda.master.lib.SidePocketRedemptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class CheckerReturnedCheckerOPerations_TS4 {
	static boolean bStatus;
	static String sSheetName = "CheckerReturnedMaker";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Side Pocket Redemption";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("SidePocketREDCheckerUserName"), Global.mapCredentials.get("SidePocketREDCheckerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}
	
	@Test
	public static void vefifyCheckerOperations(){
		Map<String, Map<String, String>> mapAllSidePCKTRedemptionDetails = Utilities.readMultipleTestData(Global.sSidePocketREDTestData,sSheetName,"Y");
		
		for (int index = 1; index <= mapAllSidePCKTRedemptionDetails.size() ; index++){
			Map<String, String> mapSPRedemptionCheckerReturnedData = mapAllSidePCKTRedemptionDetails.get("Row"+index);
			Reporting.Testcasename = mapSPRedemptionCheckerReturnedData.get("TestCaseName");

			//get the investor map details if available
			if(mapSPRedemptionCheckerReturnedData.get("ExpectedResults").equalsIgnoreCase("Fail") || (!mapSPRedemptionCheckerReturnedData.get("OperationType").equalsIgnoreCase("Save") && !mapSPRedemptionCheckerReturnedData.get("OperationType").equalsIgnoreCase("Send For Review"))){
				continue;
			}
			if(mapSPRedemptionCheckerReturnedData.get("CheckerOperations").equalsIgnoreCase("None") || mapSPRedemptionCheckerReturnedData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("None")){
				continue;
			}
			Map<String ,String> mapXMLSPRDDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "TradeTypeSPRED", mapSPRedemptionCheckerReturnedData.get("TestCaseName"));
			if(mapXMLSPRDDetails == null || !mapXMLSPRDDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
				continue;
			}
			
			Map<String ,String> mapSDPCKTRedemptiontionDetails = ExcelUtils.readDataABasedOnCellName(Global.sSidePocketREDTestData,"SidePocketRedemptionTestData",Reporting.Testcasename);
			mapSDPCKTRedemptiontionDetails.putAll(mapSPRedemptionCheckerReturnedData);
			if(mapXMLSPRDDetails.get("TransactionID")!=null){
				mapSDPCKTRedemptiontionDetails.put("Transaction ID", mapXMLSPRDDetails.get("TransactionID"));
				//mapSubscriptionDetails.put("Transaction ID", "TR00000031");
			}
			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard", "Successfully Navigate to Dashboard");

			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Side Pocket Redemption",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapSDPCKTRedemptiontionDetails.get("Transaction ID"), mapSDPCKTRedemptiontionDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Select the Created Side Pocket Redemption", "Failed to Select the Created Side Pocket Redemption. "+Messages.errorMsg);
				continue;
			}
			
			if(mapSDPCKTRedemptiontionDetails.get("TaxSlotTransactionID") != null){
				String taxSlotTransactionID = SidePocketRedemptionAppFunctions.getTheTransactionIdsFromXmlFiles(mapSDPCKTRedemptiontionDetails.get("TaxSlotTransactionID"));
				if(taxSlotTransactionID != null){
					mapSDPCKTRedemptiontionDetails.put("TaxSlotTransactionID", taxSlotTransactionID);
				}
			}			
			
			if(mapSDPCKTRedemptiontionDetails.get("AccountIDs") != null){
				String accountIDs = SidePocketRedemptionAppFunctions.getTheAccountIdListFromXMLFiles(mapSDPCKTRedemptiontionDetails.get("AccountIDs"));
				if(accountIDs != null){
					mapSDPCKTRedemptiontionDetails.put("AccountIDs", accountIDs);
				}
			}
			
			String checkerStatus = "";
			bStatus = SidePocketRedemptionAppFunctions.doVerifySidePocketRedemptionAtChecker(mapSDPCKTRedemptiontionDetails);
			
			if(!bStatus){
				Reporting.logResults("Fail", "Verify Side Pocket Redemption Details in Checker", "Verification of Side Pocket Redemption Details Failed in Checker.ERROR : "+Messages.errorMsg);
				checkerStatus = "Fail";
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "CheckerStatus", checkerStatus, "TestcaseName", Reporting.Testcasename, "TradeTypeSPRED");
				continue;
			}
			
			bStatus = SidePocketRedemptionAppFunctions.performCheckerOperationsOnSidePocketTrade(mapSDPCKTRedemptiontionDetails.get("CheckerOperations"));
			
			if(!bStatus  && mapSDPCKTRedemptiontionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				checkerStatus = "Fail";
				Reporting.logResults("Fail", "Perform Side Pocket Redemption Checker Operation :'"+mapSDPCKTRedemptiontionDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapSDPCKTRedemptiontionDetails.get("CheckerOperations")+"' Failed .ERROR : "+Messages.errorMsg);
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "CheckerStatus", checkerStatus, "TestcaseName", Reporting.Testcasename, "TradeTypeSPRED");
				continue;
			}
			if(bStatus && mapSDPCKTRedemptiontionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				Reporting.logResults("Pass", "Perform Side Pocket Redemption Checker Operation :'"+mapSDPCKTRedemptiontionDetails.get("CheckerOperations")+"'", "Checker Operation :'"+mapSDPCKTRedemptiontionDetails.get("CheckerOperations")+"' Peroformed Successfully ");
				checkerStatus = "Pass";
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "CheckerStatus", checkerStatus, "TestcaseName", Reporting.Testcasename, "TradeTypeSPRED");
				continue;
			}
			if(bStatus && mapSDPCKTRedemptiontionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Side Pocket Redemption Checker Operation :'"+mapSDPCKTRedemptiontionDetails.get("CheckerOperations")+"'", "Performed Checker Operation :'"+mapSDPCKTRedemptiontionDetails.get("CheckerOperations")+"' with Negative Test Data");
				continue;
			}
			if(!bStatus && mapSDPCKTRedemptiontionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Side Pocket Redemption Checker Operation :'"+mapSDPCKTRedemptiontionDetails.get("CheckerOperations")+"'", "Negative Test Case cannot Perform Checker Operation :'"+mapSDPCKTRedemptiontionDetails.get("CheckerOperations")+"' ERROR: "+Messages.errorMsg);
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
