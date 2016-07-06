package com.viteos.veda.transaction.exchangetestscripts;

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
import com.viteos.veda.master.lib.TradeTypeExchangeAppFunction;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class CheckerReviewedOrReturnedExchangeMakerOperation_TS3 {
	static boolean bStatus;
	static String sSheetName = "TestDataForCheckerReviewed";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Exchange";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("ExchangeMakerUserName"), Global.mapCredentials.get("ExchangeMakerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void vefifyCheckerOperations(){
		Map<String, Map<String, String>> mapAllExchangeDetails = Utilities.readMultipleTestData(Global.sExchangeTestData,sSheetName,"Y");

		for (int index = 1; index <= mapAllExchangeDetails.size() ; index++){
			Map<String, String> mapExchangeDetails = mapAllExchangeDetails.get("Row"+index);
			Reporting.Testcasename = mapExchangeDetails.get("TestCaseName");			
			
			Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "TradeTypeEXCN", mapExchangeDetails.get("TestCaseName"));
			if(mapXMLExchangeDetails == null || !mapXMLExchangeDetails.get("CheckerStatus").equalsIgnoreCase("Pass")){
				continue;
			}

			if(mapXMLExchangeDetails.get("TransactionID")!=null){
				mapExchangeDetails.put("Transaction ID", mapXMLExchangeDetails.get("TransactionID"));
			}
			//TradeTypeExchangeAppFunction.isExchangeForChekerReviewedMaker = true;
			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();

			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard", "Unable to Navigate to Dashboard");
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapExchangeDetails.get("TestCaseName"), "TradeTypeEXCN");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard", "Successfully Navigate to Dashboard");


			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Exchange",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapExchangeDetails.get("Transaction ID"), mapExchangeDetails);
			if (!bStatus) {
				Reporting.logResults("Fail", "Select the Created Exchange", "Failed to Select the Created Exchange. "+Messages.errorMsg);
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapExchangeDetails.get("TestCaseName"), "TradeTypeEXCN");
				continue;
			}
			TradeTypeExchangeAppFunction.isExchangeFromChekerReviewed = true;
			bStatus = TradeTypeExchangeAppFunction.doFillExchangeCheckerReviewedTradeDetails(mapExchangeDetails);
			if(!bStatus  && mapExchangeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail", "Perform Maker Operation :'"+mapExchangeDetails.get("OperationType")+"'", "Maker Operation :'"+mapExchangeDetails.get("OperationType")+"' Failed .ERROR : "+Messages.errorMsg);
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapExchangeDetails.get("TestCaseName"), "TradeTypeEXCN");
				continue;
			}
			if(bStatus && mapExchangeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Pass", "Perform Maker Operation :'"+mapExchangeDetails.get("OperationType")+"'", "Maker Operation :'"+mapExchangeDetails.get("OperationType")+"' Peroformed Successfully ");
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "MakerStatus", "Pass", "TestcaseName", mapExchangeDetails.get("TestCaseName"), "TradeTypeEXCN");
				continue;
			}
			if(bStatus && mapExchangeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Maker Operation :'"+mapExchangeDetails.get("OperationType")+"'", "Performed Maker Operation :'"+mapExchangeDetails.get("OperationType")+"' with Negative Test Data");
				continue;
			}
			if(!bStatus && mapExchangeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Maker Operation :'"+mapExchangeDetails.get("OperationType")+"'", "Negative Test Case cannot Perform Maker Operation :'"+mapExchangeDetails.get("OperationType")+"' ERROR: "+Messages.errorMsg);
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
