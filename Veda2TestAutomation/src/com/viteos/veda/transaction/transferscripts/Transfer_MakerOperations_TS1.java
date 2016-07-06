package com.viteos.veda.transaction.transferscripts;

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
import com.viteos.veda.master.lib.TradeTypeTransferAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class Transfer_MakerOperations_TS1 {
	static boolean bStatus;
	static String sSheetName = "TransferTestData";

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
			
			NewUICommonFunctions.refreshThePage();
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("TRADES","Transfer");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Transfer Trade", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Transfer Trade", "Transfer Menu selected succesfully");

			bStatus= TradeTypeTransferAppFunctions.doFillTransferMasterDetails(mapTransferDetails);
			if(bStatus && mapTransferDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				if(mapTransferDetails.get("OperationType").equalsIgnoreCase("Save") || mapTransferDetails.get("OperationType").equalsIgnoreCase("Send For Review") ){
					String transactionID = NewUICommonFunctions.getIDFromSuccessMessage();
					if (transactionID == null) {
						Reporting.logResults("Fail", "Get transaction ID from success Message of Transfer creation.", "Transaction ID Wasn't Displayed in the Success Message.");
						continue;
					}
					//adding testcase name and id to xml library
					Map<String, String> mapTransferXMLMap =  new HashMap<String, String>();
					mapTransferXMLMap.put("TestcaseName", mapTransferDetails.get("TestCaseName"));
					mapTransferXMLMap.put("Transaction ID", transactionID);
					mapTransferXMLMap.put("MakerStatus", "Pass");
					mapTransferXMLMap.put("CheckerStatus", "None");
					if(mapTransferDetails.get("NewAccount")!=null && mapTransferDetails.get("NewAccount").equalsIgnoreCase("Yes") && !TradeTypeTransferAppFunctions.newAccountID.equalsIgnoreCase("")){

						mapTransferXMLMap.put("Account ID", TradeTypeTransferAppFunctions.newAccountID);
					}
					Map<String ,String> mapXMLTransferDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "TradeTypeTRAN", mapTransferDetails.get("TestCaseName"));
					if(mapXMLTransferDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "TransactionID", transactionID, "TestcaseName", mapTransferDetails.get("TestCaseName"), "TradeTypeTRAN");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "AccountID", TradeTypeTransferAppFunctions.newAccountID, "TestcaseName", mapTransferDetails.get("TestCaseName"), "TradeTypeTRAN");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "MakerStatus", "Pass", "TestcaseName", mapTransferDetails.get("TestCaseName"), "TradeTypeTRAN");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "CheckerStatus", "None", "TestcaseName", mapTransferDetails.get("TestCaseName"), "TradeTypeTRAN");
					}else{
						XMLLibrary.writeTradeTypeTRANDetailsToXML(mapTransferXMLMap);
					}					
					Reporting.logResults("Pass", "Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"'", "Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"' Successfully with Transaction ID : "+transactionID);
					continue;
				}			
				if(mapTransferDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapTransferDetails.get("OperationType").equalsIgnoreCase("Clear") ){
					Reporting.logResults("Pass", "Perform Transfer Maker Operation: "+mapTransferDetails.get("OperationType")+"", "Performed Maker Operation '"+mapTransferDetails.get("OperationType")+"' Successfully ");
					continue;
				}			
			}
			
			if(bStatus && mapTransferDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"'", "Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"' with Negative Test Data");
				continue;
			}
			
			if(!bStatus && mapTransferDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"'", "Failed to Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"'.ERROR : "+Messages.errorMsg);
				Map<String ,String> mapXMLTransferDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "TradeTypeTRAN", mapTransferDetails.get("TestCaseName"));
				if(mapXMLTransferDetails != null){
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapTransferDetails.get("TestCaseName"), "TradeTypeTRAN");
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "CheckerStatus", "None", "TestcaseName", mapTransferDetails.get("TestCaseName"), "TradeTypeTRAN");
				}
				NewUICommonFunctions.handleAlert();
				continue;
			}
			
			if(!bStatus && mapTransferDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"'", "Negative Test Data Cannot Perform Maker Operation :'"+mapTransferDetails.get("OperationType")+"'.ERROR : "+Messages.errorMsg);
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
