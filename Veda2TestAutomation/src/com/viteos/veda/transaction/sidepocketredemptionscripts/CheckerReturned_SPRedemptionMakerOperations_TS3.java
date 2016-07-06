package com.viteos.veda.transaction.sidepocketredemptionscripts;

import java.util.HashMap;
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
import com.viteos.veda.master.lib.SidePocketRedemptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class CheckerReturned_SPRedemptionMakerOperations_TS3 {
	static boolean bStatus;
	static String sSheetName = "SidePocketRedemptionTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Side Pocket Redemption";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("SidePocketREDMakerUserName"), Global.mapCredentials.get("SidePocketREDMakerPassword"));
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
			Map<String, String> mapSDPCKTRedemptiontionDetails = mapAllSidePCKTRedemptionDetails.get("Row"+index);
			Reporting.Testcasename = mapSDPCKTRedemptiontionDetails.get("TestCaseName");

			Map<String ,String> mapXMLSPRDDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "TradeTypeSPRED", mapSDPCKTRedemptiontionDetails.get("TestCaseName"));
			if(mapXMLSPRDDetails == null || !mapXMLSPRDDetails.get("CheckerStatus").equalsIgnoreCase("Pass")){
				continue;
			}
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
				Reporting.logResults("Fail", "Select the Checker Returned Side Pocket Redemption", "Failed to Select the Checker Returned Side Pocket Redemption. "+Messages.errorMsg);
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
			SidePocketRedemptionAppFunctions.isMakerOperationForCheckerReturned = true;

			bStatus = SidePocketRedemptionAppFunctions.doFillSidePocaketRedemprionDetails(mapSDPCKTRedemptiontionDetails);
			String makerStatus = "";
			if(bStatus && mapSDPCKTRedemptiontionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				if(mapSDPCKTRedemptiontionDetails.get("OperationType").equalsIgnoreCase("Save")){	
					makerStatus = "Pass";
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "MakerStatus", makerStatus, "TestcaseName", Reporting.Testcasename, "TradeTypeSPRED");
					Reporting.logResults("Pass", "Perform Side Pocket Redemption Maker Operation :'"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"'", "Performed Side Pocket Redemption Maker Operation '"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"' Successfully for Transaction ID : "+mapSDPCKTRedemptiontionDetails.get("Transaction ID"));
					continue;
				}
				if(mapSDPCKTRedemptiontionDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapSDPCKTRedemptiontionDetails.get("OperationType").equalsIgnoreCase("Clear") ){
					Reporting.logResults("Pass", "Perform Side Pocket Redemption Maker Operation: "+mapSDPCKTRedemptiontionDetails.get("OperationType")+"", "Performed Side Pocket Redemption Maker Operation '"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"' Successfully ");
					continue;
				}				
			}		
			if(bStatus && mapSDPCKTRedemptiontionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){				
				Reporting.logResults("Fail", "Perform Side Pocket Redemption Maker Operation :'"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"'", "Performed Side Pocket Redemption Maker Operation '"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"' with Negative Test Data");
				continue;				
			}			
			if(!bStatus && mapSDPCKTRedemptiontionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Side Pocket Redemption Maker Operation :'"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"'", "Failed to Perform Side Pocket Redemption Maker Operation '"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"'.ERROR : "+Messages.errorMsg);
				makerStatus = "Fail";
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "MakerStatus", makerStatus, "TestcaseName", Reporting.Testcasename, "TradeTypeSPRED");
				NewUICommonFunctions.handleAlert();
				continue;
			}			
			if(!bStatus && mapSDPCKTRedemptiontionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){				
				Reporting.logResults("Pass", "Perform Side Pocket Redemption Maker Operation :'"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"'", "Negative Test Data Cannot Perform Side Pocket Redemption Maker Operation '"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"' .ERROR : "+Messages.errorMsg);
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
