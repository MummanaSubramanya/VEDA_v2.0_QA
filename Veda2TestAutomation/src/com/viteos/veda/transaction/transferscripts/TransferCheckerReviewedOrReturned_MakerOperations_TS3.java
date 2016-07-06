package com.viteos.veda.transaction.transferscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeTransferAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class TransferCheckerReviewedOrReturned_MakerOperations_TS3 {
	static boolean bStatus;
	static String sSheetName = "TestDataForCheckerReviewd";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="TransferTrade";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("TransferMakerUserName"), Global.mapCredentials.get("TransferMakerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}
	@org.testng.annotations.Test
	public static void test(){
		Map<String, Map<String, String>> mapAllTransferDetails = Utilities.readMultipleTestData(Global.sTransferTestData,sSheetName,"Y");

		for(int index = 1;index <= mapAllTransferDetails.size();index++){
			Map<String, String> mapTransferDetails = mapAllTransferDetails.get("Row"+index);

			Reporting.Testcasename = mapTransferDetails.get("TestCaseName");
			
			Map<String ,String> mapXMLTransferDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "TradeTypeTRAN", mapTransferDetails.get("TestCaseName"));
			
			if(mapXMLTransferDetails == null || !mapXMLTransferDetails.get("CheckerStatus").equalsIgnoreCase("Pass")){
				continue;
			}
			if(mapXMLTransferDetails.get("TransactionID") != null){
				mapTransferDetails.put("Transaction ID", mapXMLTransferDetails.get("TransactionID"));
			}
			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard", "Successfully Navigate to Dashboard");

			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Transfer",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapTransferDetails.get("Transaction ID"), mapTransferDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Select the Created Transfer", "Failed to Select the Created Transfer. "+Messages.errorMsg);
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapTransferDetails.get("TestCaseName"), "TradeTypeTRAN");
				continue;
			}
			
			TradeTypeTransferAppFunctions.isTransferForCheckerReviewedScreen = true;
			bStatus = TradeTypeTransferAppFunctions.doMakerFillCheckerReviewedTransferTrade(mapTransferDetails);

			if(bStatus && mapTransferDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				if(mapTransferDetails.get("OperationType").equalsIgnoreCase("Save")){				
					if(mapTransferDetails.get("NewAccount") != null && mapTransferDetails.get("NewAccount").equalsIgnoreCase("Yes")){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "AccountID", TradeTypeTransferAppFunctions.newAccountID, "TestcaseName", Reporting.Testcasename, "TradeTypeTRAN");
					}
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "MakerStatus", "Pass", "TestcaseName", mapTransferDetails.get("TestCaseName"), "TradeTypeTRAN");
					
					Reporting.logResults("Pass", "Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"'", "Performed Transfer Maker Operation '"+mapTransferDetails.get("OperationType")+"' Successfully ");
					continue;
				}
				if(mapTransferDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapTransferDetails.get("OperationType").equalsIgnoreCase("Clear") ){
					Reporting.logResults("Pass", "Perform Transfer Maker Operation: "+mapTransferDetails.get("OperationType")+"", "Performed Maker Operation '"+mapTransferDetails.get("OperationType")+"' Successfully ");
					continue;
				}				
			}			
			if(bStatus && mapTransferDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"'", "Performed Transfer Maker Operation '"+mapTransferDetails.get("OperationType")+"' with Negative Test Data");
				continue;
			}			
			if(!bStatus && mapTransferDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"'", "Failed to Perform Transfer Maker Operation '"+mapTransferDetails.get("OperationType")+"'.ERROR : "+Messages.errorMsg);
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapTransferDetails.get("TestCaseName"), "TradeTypeTRAN");
				NewUICommonFunctions.handleAlert();
				continue;
			}			
			if(!bStatus && mapTransferDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"'", "Negative Test Data Cannot Perform Transfer Maker Operation '"+mapTransferDetails.get("OperationType")+"' .ERROR : "+Messages.errorMsg);
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
