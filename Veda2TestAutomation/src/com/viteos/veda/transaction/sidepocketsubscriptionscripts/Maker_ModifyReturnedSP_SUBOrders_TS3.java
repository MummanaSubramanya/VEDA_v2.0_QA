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

public class Maker_ModifyReturnedSP_SUBOrders_TS3 {
	static boolean bStatus;
	static String sSheetName = "SPSUBModifyTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Trade_SidePocket_SUB";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("SidePocketSUBMakerUserName"), Global.mapCredentials.get("SidePocketSUBMakerPassword"));
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
				if (mapSPSUBDetails.get("ExpectedResults") == null || !mapSPSUBDetails.get("ExpectedResults").equalsIgnoreCase("Pass") || mapSP_SUB_OrderDetailsXML == null || mapSP_SUB_OrderDetailsXML.isEmpty() || mapSP_SUB_OrderDetailsXML.get("TransactionID") == null || mapSP_SUB_OrderDetailsXML.get("MakerStatus") == null || mapSP_SUB_OrderDetailsXML.get("MakerStatus").equalsIgnoreCase("Fail") || mapSP_SUB_OrderDetailsXML.get("CheckerStatus") == null || !mapSP_SUB_OrderDetailsXML.get("CheckerStatus").equalsIgnoreCase("Pass")) {
					continue;
				}
				
				Reporting.Testcasename = mapSPSUBDetails.get("TestCaseName");
				
				//Navigate to MAKER Dash-board.
				bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Dashboard", "Dashboard Menu cannot be selected Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Dashboard", "Dashboard Menu selected succesfully");			
				TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
				
				if (mapSPSUBDetails.get("Transaction ID") != null) {
					String sReplacedTxnIDs = SidePocketRedemptionAppFunctions.getTheTransactionIdsFromXmlFiles(mapSPSUBDetails.get("Transaction ID"));
					mapSPSUBDetails.put("Transaction ID", sReplacedTxnIDs);
				}				
				bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Side Pocket Subscription",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapSP_SUB_OrderDetailsXML.get("TransactionID"), mapSP_SUB_OrderDetailsXML);
				if (!bStatus) {
					Reporting.logResults("Fail", "Maker Open the Checker Returned Side Pocket Subscription Order", "Maker Failed to Open the Checker Returned Side Pocket Subscription Order. "+Messages.errorMsg);
					continue;
				}
				bStatus = SidePocketSubscriptionAppFunctions.doPlaceSidePocketSubscriptionOrder(mapSPSUBDetails);			
				
				//Storing test case name, Txn id and newly created Account ID in xml file.
				Map<String, String> mapWritableSPSUBXMLMap =  new HashMap<String, String>();
				if(bStatus && mapSPSUBDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
					if(mapSPSUBDetails.get("OperationType").equalsIgnoreCase("Approve")){
						mapWritableSPSUBXMLMap.put("TestcaseName", mapSPSUBDetails.get("TestCaseName"));
						mapWritableSPSUBXMLMap.put("MakerStatus", "Pass");
						Map<String ,String> mapExistingXMLSPSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TradeTypeSPSUB", mapSPSUBDetails.get("TestCaseName"));
						if(mapExistingXMLSPSUBDetails != null && mapExistingXMLSPSUBDetails.get("TestcaseName") != null){
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "MakerStatus", mapWritableSPSUBXMLMap.get("MakerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPSUB");
						}		
						Reporting.logResults("Pass", "Perform TradeTypeSidePocket_SUB Maker Operation : '"+mapSPSUBDetails.get("OperationType")+"' on 'Checker Returned' Order", "Performed TradeTypeSidePocket_SUB Maker Operation '"+mapSPSUBDetails.get("OperationType")+"' on 'Checker Returned' Order Successfully with Transaction ID : "+mapSP_SUB_OrderDetailsXML.get("TransactionID"));
						continue;
					}
					if(mapSPSUBDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapSPSUBDetails.get("OperationType").equalsIgnoreCase("Clear") ){
						Reporting.logResults("Pass", "Perform TradeTypeSidePocket_SUB Maker Operation: '"+mapSPSUBDetails.get("OperationType")+"' on 'Checker Returned' Order", "Performed Maker Operation '"+mapSPSUBDetails.get("OperationType")+"' on TradeTypeSidePocket_SUB  for 'Checker Returned' Order Successfully with Transaction ID : "+mapSP_SUB_OrderDetailsXML.get("TransactionID"));
						continue;
					}				
				}		
				if(bStatus && mapSPSUBDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){				
					Reporting.logResults("Fail", "Perform TradeTypeSidePocket_SUB Maker Operation : '"+mapSPSUBDetails.get("OperationType")+"' on 'Checker Returned' Order", "Performed Maker Operation '"+mapSPSUBDetails.get("OperationType")+"' on TradeTypeSidePocket_SUB  on 'Checker Returned' Order with Negative Test Data");
					continue;				
				}			
				if(!bStatus && mapSPSUBDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
					
					Reporting.logResults("Fail", "Perform TradeTypeSidePocket_SUB Maker Operation : '"+mapSPSUBDetails.get("OperationType")+"' on 'Checker Returned' Order", "Failed to Perform TradeTypeSidePocket_SUB Maker Operation '"+mapSPSUBDetails.get("OperationType")+"' on 'Checker Returned' Order.ERROR : "+Messages.errorMsg);
					Map<String ,String> mapXMLSPSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TradeTypeSPSUB", mapSPSUBDetails.get("TestCaseName"));
					if(mapXMLSPSUBDetails != null){
						mapWritableSPSUBXMLMap.put("MakerStatus","Fail");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "MakerStatus", mapWritableSPSUBXMLMap.get("MakerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPSUB");
					}
					NewUICommonFunctions.handleAlert();
					continue;					
				}			
				if(!bStatus && mapSPSUBDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){				
					Reporting.logResults("Pass", "Perform TradeTypeSidePocket_SUB Maker Operation : '"+mapSPSUBDetails.get("OperationType")+"' on 'Checker Returned' Order", "Negative Test Data Cannot Perform TradeTypeSidePocket_SUB Maker Operation '"+mapSPSUBDetails.get("OperationType")+"' on 'Checker Returned' Order.ERROR : "+Messages.errorMsg);
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
