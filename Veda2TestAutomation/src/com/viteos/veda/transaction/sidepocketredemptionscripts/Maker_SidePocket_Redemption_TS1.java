package com.viteos.veda.transaction.sidepocketredemptionscripts;

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
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.TradeTypeSwitchAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class Maker_SidePocket_Redemption_TS1 {
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
	
	@org.testng.annotations.Test
	public static void test(){
		Map<String, Map<String, String>> mapAllSidePCKTRedemptionDetails = Utilities.readMultipleTestData(Global.sSidePocketREDTestData,sSheetName,"Y");

		for(int index = 1;index <= mapAllSidePCKTRedemptionDetails.size();index++){
			Map<String, String> mapSDPCKTRedemptiontionDetails = mapAllSidePCKTRedemptionDetails.get("Row"+index);

			Reporting.Testcasename = mapSDPCKTRedemptiontionDetails.get("TestCaseName");
			
			NewUICommonFunctions.refreshThePage();
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.navigateToSidePocket("Side Pocket Redemption");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Side Pocket Redemption Trade", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Side Pocket Redemption Trade", "Subscription Menu selected succesfully");
			
			if(mapSDPCKTRedemptiontionDetails.get("TaxSlotTransactionID") != null){
				String taxSlotTransactionID = SidePocketRedemptionAppFunctions.getTheTransactionIdFromSPSubscriptionXMLFile(mapSDPCKTRedemptiontionDetails.get("TaxSlotTransactionID"));
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
			
			bStatus = SidePocketRedemptionAppFunctions.doFillSidePocaketRedemprionDetails(mapSDPCKTRedemptiontionDetails);
			
			//Storing test case name, Txn id and newly created Account ID in xml file.
			Map<String, String> mapSDPCKTRedemptiontionXMLMap =  new HashMap<String, String>();
			if(bStatus && mapSDPCKTRedemptiontionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				if(mapSDPCKTRedemptiontionDetails.get("OperationType").equalsIgnoreCase("Save")){
					String transactionID = NewUICommonFunctions.getIDFromSuccessMessage();
					if (transactionID == null) {
						Reporting.logResults("Fail", "Get transaction ID from success Message of Side Pocket Redemption creation.", "Transaction ID Wasn't Displayed in the Success Message.");
						continue;
					}					
					mapSDPCKTRedemptiontionXMLMap.put("TestcaseName", mapSDPCKTRedemptiontionDetails.get("TestCaseName"));
					mapSDPCKTRedemptiontionXMLMap.put("Transaction ID", transactionID);					
					mapSDPCKTRedemptiontionXMLMap.put("MakerStatus", "Pass");
					mapSDPCKTRedemptiontionXMLMap.put("CheckerStatus", "None");
					Map<String ,String> mapXMLSPRDDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "TradeTypeSPRED", mapSDPCKTRedemptiontionDetails.get("TestCaseName"));
					if(mapXMLSPRDDetails != null && mapXMLSPRDDetails.get("TestcaseName") != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "MakerStatus", mapSDPCKTRedemptiontionXMLMap.get("MakerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPRED");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "CheckerStatus", mapSDPCKTRedemptiontionXMLMap.get("CheckerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPRED");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "TransactionID", mapSDPCKTRedemptiontionXMLMap.get("Transaction ID"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPRED");
					}else{
						XMLLibrary.writeTradeTypeSPREDDetailsToXML(mapSDPCKTRedemptiontionXMLMap);
					}					
					Reporting.logResults("Pass", "Perform Side Pocket Redemption Maker Operation :'"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"'", "Performed Side Pocket Redemption Maker Operation '"+mapSDPCKTRedemptiontionDetails.get("OperationType")+"' Successfully with Transaction ID : "+transactionID);
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
				Map<String ,String> mapXMLSwitchDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "TradeTypeSPRED", mapSDPCKTRedemptiontionDetails.get("TestCaseName"));
				if(mapXMLSwitchDetails != null){
					mapSDPCKTRedemptiontionXMLMap.put("MakerStatus","Fail");
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSPREDDetailsXMLFilePath, "MakerStatus", mapSDPCKTRedemptiontionXMLMap.get("MakerStatus"), "TestcaseName", Reporting.Testcasename, "TradeTypeSPRED");
				}
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
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
