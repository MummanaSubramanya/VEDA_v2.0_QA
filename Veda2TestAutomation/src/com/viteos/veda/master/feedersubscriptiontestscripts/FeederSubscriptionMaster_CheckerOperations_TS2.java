package com.viteos.veda.master.feedersubscriptiontestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.FeederSubscriptionAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.checkerActionTypes;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.XMLLibrary;

public class FeederSubscriptionMaster_CheckerOperations_TS2 {

	static boolean bStatus;
	static String sSheetName = "FeederSubscriptionTestData";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="FeederSubscription Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false; 

		bStatus = NewUICommonFunctions.loginToApplication(Global.sCheckerUserName, Global.sCheckerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void testCheckerOperations(){
		try{
			Map<String, Map<String, String>> mapAllFeederSUBDetails = Utilities.readMultipleTestData(Global.sFeederSubscriptionTestDataFilePath, sSheetName, "Y");

			for(int index = 1;index <=mapAllFeederSUBDetails.size();index++){

				Map<String, String> mapFeederSUBTestData = mapAllFeederSUBDetails.get("Row"+index);

				Reporting.Testcasename = mapFeederSUBTestData.get("TestCaseName");
				
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederSUBAccountXMLFilePath, "FeederSUBAccount", mapFeederSUBTestData.get("TestCaseName"));
				if(mapXMLSubscriptionDetails == null || !mapXMLSubscriptionDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
					continue;
				}
				//Navigate to DashBoard
				
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");
				
				bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionIDAndMasterType(dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapXMLSubscriptionDetails.get("SearchID"), "Feeder SUB", "");
				if (!bStatus) {								
					Reporting.logResults("Fail", "Get the created record from dashboard to verify and perform checker operations.", "Unable to Get the created record from dashboard to verify and perform checker operations."+Messages.errorMsg);
					continue;
				}
				
				//Perform Checker Verification Operation.
				bStatus = FeederSubscriptionAppFunctions.doVerifyFeederSubscriptionDetails(mapFeederSUBTestData);
				if (!bStatus) {
					Reporting.logResults("Fail", "Verification Of Created Feeder SubscriptionDetails.", "Verification Of Created Feeder Subscription Details are NOT matching with Expected ."+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", mapFeederSUBTestData.get("TestCaseName"), "FeederSUBAccount");
					continue;
				}
				Reporting.logResults("Pass", "Verification Of Created Feeder SubscriptionDetails.", "Verification Of Created Feeder Subscription Details are matching with Expected .");

				//Perform Checker OPeration
				bStatus = FeederSubscriptionAppFunctions.doCheckerOperationsForFeederSubscription(mapFeederSUBTestData.get("CheckerOperations"));

				if(bStatus && mapFeederSUBTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapFeederSUBTestData.get("CheckerOperations"), "Successfully Performed checker operations for transaction ID : "+mapXMLSubscriptionDetails.get("SearchID"));
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "CheckerStatus", "Pass", "TestcaseName", mapFeederSUBTestData.get("TestCaseName"), "FeederSUBAccount");
				}
				
				if(!bStatus && mapFeederSUBTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapFeederSUBTestData.get("CheckerOperations"), "Cannot Perform Checker Operations for transaction ID : "+mapXMLSubscriptionDetails.get("SearchID")+". Error: "+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", mapFeederSUBTestData.get("TestCaseName"), "FeederSUBAccount");
					continue;
				}
				
				if(!bStatus && mapFeederSUBTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapFeederSUBTestData.get("CheckerOperations"), "Negative testcase - Cannot Perform Checker Operations .Error: "+Messages.errorMsg);
				}

				if(bStatus && mapFeederSUBTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapFeederSUBTestData.get("CheckerOperations"), "Peformed Checker operations with negative testdata. transaction ID : "+mapXMLSubscriptionDetails.get("SearchID"));
					continue;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
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
