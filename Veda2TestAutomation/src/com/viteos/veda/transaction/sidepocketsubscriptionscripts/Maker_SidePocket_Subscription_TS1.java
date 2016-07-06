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

public class Maker_SidePocket_Subscription_TS1 {
	static boolean bStatus;
	static String sSheetName = "SidePocketSubscriptionTestData";

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

				Reporting.Testcasename = mapSPSUBDetails.get("TestCaseName");
				
				//Navigate to Client Master
				bStatus = NewUICommonFunctions.navigateToSidePocket("Side Pocket Subscription");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Side Pocket Subscription Trade", "Side Pocket Subscription Menu cannot be selected Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Side Pocket Subscription Trade", "Side Pocket Subscription Menu selected succesfully");			
				TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
				
				if (mapSPSUBDetails.get("Transaction ID") != null) {
					String sReplacedTxnIDs = SidePocketRedemptionAppFunctions.getTheTransactionIdsFromXmlFiles(mapSPSUBDetails.get("Transaction ID"));
					mapSPSUBDetails.put("Transaction ID", sReplacedTxnIDs);
				}				
				bStatus = SidePocketSubscriptionAppFunctions.doPlaceSidePocketSubscriptionOrder(mapSPSUBDetails);			
				
				//Storing test case name, Txn id and newly created Account ID in xml file.
				Map<String, String> mapWritableSPSUBXMLMap =  new HashMap<String, String>();
				if(bStatus && mapSPSUBDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
					if(mapSPSUBDetails.get("OperationType").equalsIgnoreCase("Save")){
						String transactionID = NewUICommonFunctions.getOrderIDFromSuccessMessage();
						if (transactionID == null) {
							Reporting.logResults("Fail", "Get transaction ID from success Message of Side Pocket Subscription Order creation.", "Transaction ID Wasn't Displayed in the Success Message.");
							continue;
						}					
						mapWritableSPSUBXMLMap.put("TestcaseName", mapSPSUBDetails.get("TestCaseName"));
						mapWritableSPSUBXMLMap.put("Transaction ID", transactionID);					
						mapWritableSPSUBXMLMap.put("MakerStatus", "Pass");
						mapWritableSPSUBXMLMap.put("CheckerStatus", "None");
						Map<String ,String> mapExistingXMLSPSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TradeTypeSPSUB", mapSPSUBDetails.get("TestCaseName"));
						if(mapExistingXMLSPSUBDetails != null && mapExistingXMLSPSUBDetails.get("TestcaseName") != null){
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "MakerStatus", mapWritableSPSUBXMLMap.get("MakerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPSUB");
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TransactionID", mapWritableSPSUBXMLMap.get("Transaction ID"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPSUB");
						}else{
							XMLLibrary.writeTradeTypeSPSUBDetailsToXML(mapWritableSPSUBXMLMap);
						}					
						Reporting.logResults("Pass", "Perform TradeTypeSidePocket_SUB Maker Operation : '"+mapSPSUBDetails.get("OperationType")+"'", "Performed TradeTypeSidePocket_SUB Maker Operation '"+mapSPSUBDetails.get("OperationType")+"' Successfully with Transaction ID : "+transactionID);
						continue;
					}
					if(mapSPSUBDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapSPSUBDetails.get("OperationType").equalsIgnoreCase("Clear") ){
						Reporting.logResults("Pass", "Perform TradeTypeSidePocket_SUB Maker Operation: "+mapSPSUBDetails.get("OperationType")+"", "Performed Maker Operation '"+mapSPSUBDetails.get("OperationType")+"' on TradeTypeSidePocket_SUB Successfully ");
						continue;
					}				
				}		
				if(bStatus && mapSPSUBDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){				
					Reporting.logResults("Fail", "Perform TradeTypeSidePocket_SUB Maker Operation : '"+mapSPSUBDetails.get("OperationType")+"'", "Performed Maker Operation '"+mapSPSUBDetails.get("OperationType")+"' on TradeTypeSidePocket_SUB with Negative Test Data");
					continue;				
				}			
				if(!bStatus && mapSPSUBDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
					
					Reporting.logResults("Fail", "Perform TradeTypeSidePocket_SUB Maker Operation : '"+mapSPSUBDetails.get("OperationType")+"'", "Failed to Perform TradeTypeSidePocket_SUB Maker Operation '"+mapSPSUBDetails.get("OperationType")+"'.ERROR : "+Messages.errorMsg);
					Map<String ,String> mapXMLSPSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TradeTypeSPSUB", mapSPSUBDetails.get("TestCaseName"));
					if(mapXMLSPSUBDetails != null){
						mapWritableSPSUBXMLMap.put("MakerStatus","Fail");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "MakerStatus", mapWritableSPSUBXMLMap.get("MakerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPSUB");
					}
					NewUICommonFunctions.handleAlert();
					continue;
					
				}			
				if(!bStatus && mapSPSUBDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){				
					Reporting.logResults("Pass", "Perform TradeTypeSidePocket_SUB Maker Operation : '"+mapSPSUBDetails.get("OperationType")+"'", "Negative Test Data Cannot Perform TradeTypeSidePocket_SUB Maker Operation '"+mapSPSUBDetails.get("OperationType")+"' .ERROR : "+Messages.errorMsg);
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
