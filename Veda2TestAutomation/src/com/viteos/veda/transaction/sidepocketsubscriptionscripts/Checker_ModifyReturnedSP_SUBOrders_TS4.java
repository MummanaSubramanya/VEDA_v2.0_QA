package com.viteos.veda.transaction.sidepocketsubscriptionscripts;

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
import com.viteos.veda.master.lib.SidePocketRedemptionAppFunctions;
import com.viteos.veda.master.lib.SidePocketSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class Checker_ModifyReturnedSP_SUBOrders_TS4 {
	static boolean bStatus;
	static String sSheetName = "SPSUBModifyTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Trade_SidePocket_SUB";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("SidePocketSUBCheckerUserName"), Global.mapCredentials.get("SidePocketSUBCheckerPassword"));
		//bStatus = NewUICommonFunctions.loginToApplication(Global.sCheckerUserName, Global.sCheckerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfull");
	}

	@org.testng.annotations.Test
	public static void test(){
		try {
			Map<String, Map<String, String>> mapAllSPSUBDetails = Utilities.readMultipleTestData(Global.sSidePocketSUBTestData, sSheetName, "Y");

			for(int index = 1;index <= mapAllSPSUBDetails.size();index++){
				Map<String, String> mapSPSUBDetails = mapAllSPSUBDetails.get("Row"+index);

				Map<String ,String> mapSP_SUB_OrderDetailsXML = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TradeTypeSPSUB", mapSPSUBDetails.get("TestCaseName"));
				if (mapSPSUBDetails.get("ExpectedResults") == null || !mapSPSUBDetails.get("ExpectedResults").equalsIgnoreCase("Pass") || mapSP_SUB_OrderDetailsXML == null || mapSP_SUB_OrderDetailsXML.isEmpty() || mapSP_SUB_OrderDetailsXML.get("TransactionID") == null || mapSP_SUB_OrderDetailsXML.get("MakerStatus") == null || mapSP_SUB_OrderDetailsXML.get("MakerStatus").equalsIgnoreCase("Fail")) {
					continue;
				}

				Reporting.Testcasename = mapSPSUBDetails.get("TestCaseName");

				//Navigate to Dash-board.
				bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate Dashboard", "Dashboard Menu cannot be selected Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Dashboard Trade", "Dashboard Menu selected succesfully");			
				TradeTypeSubscriptionAppFunctions.goToCurrentUrl();			

				bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Side Pocket Subscription",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapSP_SUB_OrderDetailsXML.get("TransactionID"), mapSP_SUB_OrderDetailsXML);
				if (!bStatus) {
					Reporting.logResults("Fail", "Checker Open the 'Checker Return - Maker Modified' Side Pocket Subscription Order", "Checker Failed to Open the 'Checker Return - Maker Modified' Side Pocket Subscription Order. "+Messages.errorMsg);
					continue;
				}

				if (mapSPSUBDetails.get("Transaction ID") != null) {
					String sReplacedTxnIDs = SidePocketRedemptionAppFunctions.getTheTransactionIdsFromXmlFiles(mapSPSUBDetails.get("Transaction ID"));
					mapSPSUBDetails.put("Transaction ID", sReplacedTxnIDs);
				}

				bStatus = SidePocketSubscriptionAppFunctions.doCheckerPerformOperationsOnSidePocketSubscriptionOrder(mapSPSUBDetails);		

				//Storing test case name, Txn id and newly created Account ID in xml file.
				Map<String, String> mapWritableSPSUBXMLMap =  new HashMap<String, String>();
				if(bStatus && mapSPSUBDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					mapWritableSPSUBXMLMap.put("TestcaseName", mapSPSUBDetails.get("TestCaseName"));
					mapWritableSPSUBXMLMap.put("CheckerStatus", "Pass");
					Map<String ,String> mapExistingXMLSPSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TradeTypeSPSUB", mapSPSUBDetails.get("TestCaseName"));
					if(mapExistingXMLSPSUBDetails != null && mapExistingXMLSPSUBDetails.get("TestcaseName") != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "CheckerStatus", mapWritableSPSUBXMLMap.get("CheckerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPSUB");
					}	
					Reporting.logResults("Pass", "Perform TradeTypeSidePocket_SUB Checker Operation : '"+mapSPSUBDetails.get("CheckerOperations")+"' on  'Checker Return - Maker Modified' Order", "Performed TradeTypeSidePocket_SUB Checker Operation '"+mapSPSUBDetails.get("CheckerOperations")+"' Successfully on 'Checker Return - Maker Modified' Order with Transaction ID : "+mapSP_SUB_OrderDetailsXML.get("TransactionID"));
					continue;	
				}		
				if(bStatus && mapSPSUBDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){				
					Reporting.logResults("Fail", "Perform TradeTypeSidePocket_SUB Checker Operation : '"+mapSPSUBDetails.get("CheckerOperations")+"' on  'Checker Return - Maker Modified' Order", "Performed Checker Operation '"+mapSPSUBDetails.get("CheckerOperations")+"' on TradeTypeSidePocket_SUB on  'Checker Return - Maker Modified' Order with Negative Test Data");
					continue;				
				}			
				if(!bStatus && mapSPSUBDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){

					Reporting.logResults("Fail", "Perform TradeTypeSidePocket_SUB Checker Operation : '"+mapSPSUBDetails.get("CheckerOperations")+"' on  'Checker Return - Maker Modified' Order", "Failed to Perform TradeTypeSidePocket_SUB Checker Operation '"+mapSPSUBDetails.get("CheckerOperations")+"' on  'Checker Return - Maker Modified' Order.ERROR : "+Messages.errorMsg);
					Map<String ,String> mapXMLSPSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TradeTypeSPSUB", mapSPSUBDetails.get("TestCaseName"));
					if(mapXMLSPSUBDetails != null){
						mapWritableSPSUBXMLMap.put("CheckerStatus","Fail");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "CheckerStatus", mapWritableSPSUBXMLMap.get("CheckerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPSUB");
					}
					NewUICommonFunctions.handleAlert();
					continue;
				}			
				if(!bStatus && mapSPSUBDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){				
					Reporting.logResults("Pass", "Perform TradeTypeSidePocket_SUB Checker Operation : '"+mapSPSUBDetails.get("CheckerOperations")+"' on  'Checker Return - Maker Modified' Order", "Negative Test Data Cannot Perform TradeTypeSidePocket_SUB Checker Operation '"+mapSPSUBDetails.get("CheckerOperations")+"' on  'Checker Return - Maker Modified' Order.ERROR : "+Messages.errorMsg);
					NewUICommonFunctions.handleAlert();
					continue;				
				}			
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
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
