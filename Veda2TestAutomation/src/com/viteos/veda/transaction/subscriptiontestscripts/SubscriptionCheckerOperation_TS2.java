package com.viteos.veda.transaction.subscriptiontestscripts;

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
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.TradeTypeSwitchAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class SubscriptionCheckerOperation_TS2 {
	static boolean bStatus;
	static String sSheetName = "SubscriptionTestData";
	static String sInvestorSheetName = "GeneralDetails";
	static String sHolderSheetName = "GeneralDetails";
	static String sSeriesTestDataSheetName = "SeriesTestData";

	@BeforeMethod
	public static void setup(){
		Reporting.Functionality ="Subscription";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("SubscriptionCheckerUserName"), Global.mapCredentials.get("SubscriptionCheckerPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void vefifyCheckerOperations(){
		Map<String,  Map<String, String>> mapAllSubscriptionDetails = Utilities.readMultipleTestData(Global.sSubscriptionTestData, sSheetName , "Y");

		for (int index = 1; index <= mapAllSubscriptionDetails.size() ; index++){
			Map<String, String> mapSubscriptionDetails = mapAllSubscriptionDetails.get("Row"+index);
			Reporting.Testcasename = mapSubscriptionDetails.get("TestCaseName");


			//get the investor map details if available
			if(mapSubscriptionDetails.get("ExpectedResults").equalsIgnoreCase("Fail") || (!mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Save") && !mapSubscriptionDetails.get("OperationType").equalsIgnoreCase("Send For Review"))){
				continue;
			}
			if(mapSubscriptionDetails.get("CheckerOperations").equalsIgnoreCase("None") || mapSubscriptionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("None")){
				continue;
			}
			
			Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSUBXMLFilePath, "TradeTypeSUB", mapSubscriptionDetails.get("TestCaseName"));
			if(mapXMLSubscriptionDetails == null || !mapXMLSubscriptionDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
				continue;
			}
			
			if(mapSubscriptionDetails.get("New Investor")!=null && mapSubscriptionDetails.get("New Investor TestCaseName")!=null)
			{
				//Get the Investor Name from the Investor Test data put that in Investor Name Column
				String investorName = getTheInvestorNameFromInvestorTestData(mapSubscriptionDetails);
				mapSubscriptionDetails.put("Investor Name",investorName);
			}

			//get the holder map
			if(mapSubscriptionDetails.get("New Holder")!=null && mapSubscriptionDetails.get("New Holder TestCaseName")!=null)
			{	
				//Get the Holder Name from the Holder Test data put that in Investor Name Column
				String hoderName = getTheHolderNameFromHolderTestData(mapSubscriptionDetails);
				mapSubscriptionDetails.put("Holder Name",hoderName);
			}

			//get the series map
			if(mapSubscriptionDetails.get("New Series")!=null && mapSubscriptionDetails.get("New Series TestCaseName")!=null){
				Map<String,  String> mapSeriesDetails = ExcelUtils.readDataABasedOnCellName(Global.sSeriesTestDataFilePath, sSeriesTestDataSheetName ,mapSubscriptionDetails.get("New Series TestCaseName"));
				if(mapSeriesDetails.get("SeriesName")!=null){
					mapSubscriptionDetails.put("Series Name", mapSeriesDetails.get("SeriesName"));
				}
			}

			//get the bank map
			if(mapSubscriptionDetails.get("New Bank TestCaseName")!=null){
				String BankNames = TradeTypeSubscriptionAppFunctions.getBankNameForCheckerVerify(mapSubscriptionDetails);
				if(BankNames != null && !BankNames.equalsIgnoreCase("")){
					mapSubscriptionDetails.put("Bank Name", BankNames);
				}
			}

			//get the xml map
			if(mapSubscriptionDetails.get("NewAccount")!=null && mapXMLSubscriptionDetails.get("AccountID")!=null){
				mapSubscriptionDetails.put("Account ID", mapXMLSubscriptionDetails.get("AccountID"));
			}
			if (mapSubscriptionDetails.get("NewAccount") == null && mapSubscriptionDetails.get("Account ID") != null) {
				//Get the Account id from the Created XML files
				String accountId = TradeTypeSwitchAppFunctions.getTheAccountIDFromCreatedXMLFilesBasedOnTestCaseName(mapSubscriptionDetails.get("Account ID"));
				mapSubscriptionDetails.put("Account ID",accountId);	
			}
			if(mapXMLSubscriptionDetails.get("TransactionID")!=null){
				mapSubscriptionDetails.put("Transaction ID", mapXMLSubscriptionDetails.get("TransactionID"));
			}

			//get the details of respective map and put to subscription map

			TradeTypeSubscriptionAppFunctions.goToCurrentUrl();

			bStatus = NewUICommonFunctions.selectMenu("Dashboard", "None");
			if (!bStatus) {
				Reporting.logResults("Fail", "Navigation to the Dashboard in Subscription Module", "Unable to Navigate to Dashboard in Subscription Module");
				continue;
			}
			Reporting.logResults("Pass", "Navigation to the Dashboard in Subscription Module", "Successfully Navigate to Dashboard in Subscription Module");


			bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Subscription",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapSubscriptionDetails.get("Transaction ID"), mapSubscriptionDetails);
			if (!bStatus) {
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
				Reporting.logResults("Fail", "Select the Created Subscription", "Failed to Select the Created Subscription. "+Messages.errorMsg);
				continue;
			}

			bStatus = TradeTypeSubscriptionAppFunctions.doVerifySubscriptionDetailsInChecker(mapSubscriptionDetails);
			if(!bStatus){
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
				Reporting.logResults("Fail", "Verify Subscription Details in Checker", "Verification of Subscription Details Failed in Checker.ERROR : "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Verify Subscription Details in Checker", "Subscription details verified successfully in Checker");

			bStatus = TradeTypeSubscriptionAppFunctions.doCheckerActionTypesOnTrade(mapSubscriptionDetails);
			if(!bStatus  && mapSubscriptionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
				Reporting.logResults("Fail", "Perform Checker Operation: '"+mapSubscriptionDetails.get("CheckerOperations")+"'", "Checker Operation Failed .ERROR : "+Messages.errorMsg);
				continue;
			}
			if(bStatus && mapSubscriptionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
				XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sTradeTypeSUBXMLFilePath, "CheckerStatus", "Pass", "TestcaseName", mapSubscriptionDetails.get("TestCaseName"), "TradeTypeSUB");
				Reporting.logResults("Pass", "Perform Checker Operation: '"+mapSubscriptionDetails.get("CheckerOperations")+"'", "Checker Operation '"+mapSubscriptionDetails.get("CheckerOperations")+"' Peroformed Successfully ");
				continue;
			}
			if(bStatus && mapSubscriptionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail", "Perform Checker Operation: '"+mapSubscriptionDetails.get("CheckerOperations")+"'", "Performed Checker Operation: '"+mapSubscriptionDetails.get("CheckerOperations")+"' for Negative Test Case");
				continue;
			}
			if(!bStatus && mapSubscriptionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass", "Perform Checker Operation: '"+mapSubscriptionDetails.get("CheckerOperations")+"'", "Negative Test Case cannot Perform Checker Operation: '"+mapSubscriptionDetails.get("CheckerOperations")+"' "+Messages.errorMsg);
				continue;
			}

		}

	}

	public static String getTheHolderNameFromHolderTestData(Map<String, String> mapSubscriptionDetails) {
		try {
			Map<String, String> mapHolderGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sHolderTestDataFilePath, sHolderSheetName , mapSubscriptionDetails.get("New Holder TestCaseName"));

			String sHolderFirstName = "";
			String sHolderLastName = "";
			String sHolderMiddleName = "";
			String sHolderName = "";								

			if (mapHolderGeneralDetails.get("HolderType") != null) 
			{
				if (mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Entity") && mapHolderGeneralDetails.get("RegistrationName") != null) 
				{
					sHolderName = mapHolderGeneralDetails.get("RegistrationName");
				}	
				//if Holder Type is Individual Holder Name should be Combination of First,Middle,Last
				if (mapHolderGeneralDetails.get("HolderType").equalsIgnoreCase("Individual")) 
				{
					if (mapHolderGeneralDetails.get("FirstName") != null) {
						sHolderFirstName = mapHolderGeneralDetails.get("FirstName");
						sHolderName = sHolderFirstName;
					}
					if (mapHolderGeneralDetails.get("MiddleName") != null) {
						sHolderMiddleName = mapHolderGeneralDetails.get("MiddleName");
						sHolderName = sHolderName+" "+sHolderMiddleName;
					}
					if (mapHolderGeneralDetails.get("LastName") != null) {
						sHolderLastName = mapHolderGeneralDetails.get("LastName");
						sHolderName = sHolderName+" "+sHolderLastName;
					}
					sHolderName = sHolderName.trim();
				}
				mapSubscriptionDetails.put("Holder Name",sHolderName);
			}
			
			return sHolderName;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	public static String getTheInvestorNameFromInvestorTestData(Map<String, String> mapSubscriptionDetails) {
		try {
			
			String sInvestorFirstName = "";
			String sInvestorLastName = "";
			String sInvestorMiddleName = "";
			String sInvestorName = "";	
			Map<String, String> mapInvestorGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sInvestorTestDataFilePath, sInvestorSheetName , mapSubscriptionDetails.get("New Investor TestCaseName"));

			if (mapInvestorGeneralDetails.get("InvestorType") != null) 
			{
				if (mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Entity") && mapInvestorGeneralDetails.get("RegistrationName") != null) {
					sInvestorName = mapInvestorGeneralDetails.get("RegistrationName");
				}	
				//if Investor Type is Individual Holder Name should be Combination of First,Middle,Last
				if (mapInvestorGeneralDetails.get("InvestorType").equalsIgnoreCase("Individual")) 
				{
					if (mapInvestorGeneralDetails.get("FirstName") != null) {
						sInvestorFirstName = mapInvestorGeneralDetails.get("FirstName");
						sInvestorName = sInvestorFirstName;
					}
					if (mapInvestorGeneralDetails.get("MiddleName") != null) {
						sInvestorMiddleName = mapInvestorGeneralDetails.get("MiddleName");
						sInvestorName = sInvestorName+" "+sInvestorMiddleName;
					}
					if (mapInvestorGeneralDetails.get("LastName") != null) {
						sInvestorLastName = mapInvestorGeneralDetails.get("LastName");
						sInvestorName = sInvestorName+" "+sInvestorLastName;
					}
					sInvestorName = sInvestorName.trim();
				}
				//put the Investor Name From Investor Test data in Subscription Map
				mapSubscriptionDetails.put("Investor Name",sInvestorName);
			}
			
			return sInvestorName;			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
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
