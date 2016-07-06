package com.viteos.veda.master.feedersubscriptiontestscripts;


import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.FeederSubscriptionAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class FeederSubscriptionCheckerReturnedMakerOperations_TC3 {
	static boolean bStatus;
	static String sSheetName = "CheckerReturnedMakerData";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="FeederSubscription Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false; 

		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void testMakerOperations(){
		try{
			Map<String, Map<String, String>> mapAllFeederSUBDetailsTestData = Utilities.readMultipleTestData(Global.sFeederSubscriptionTestDataFilePath, sSheetName, "Y");

			for(int index = 1;index <= mapAllFeederSUBDetailsTestData.size();index++){

				Map<String, String> mapFeederSUBDetailsTestData = mapAllFeederSUBDetailsTestData.get("Row"+index);

				Reporting.Testcasename = mapFeederSUBDetailsTestData.get("TestCaseName");
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederSUBAccountXMLFilePath, "FeederSUBAccount", mapFeederSUBDetailsTestData.get("TestCaseName"));
				if(mapXMLSubscriptionDetails == null || !mapXMLSubscriptionDetails.get("CheckerStatus").equalsIgnoreCase("Pass")){
					continue;
				}
				
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"), "FeederSUBAccount");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");				
				
				//Perform Checker Verification Operation.
				
				bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Feeder Subscription",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapXMLSubscriptionDetails.get("SearchID"), mapFeederSUBDetailsTestData);
				if (!bStatus) {
					Reporting.logResults("Fail", "Select the Checker Returned Feeder Subscription", "Failed to Select the Checker Returned Feeder Subscription. "+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"), "FeederSUBAccount");
					continue;
				}
				//Adding new feeder subscription.
				bStatus = FeederSubscriptionAppFunctions.AddFeederSubscriptionDetails(mapFeederSUBDetailsTestData);

				if(bStatus && mapFeederSUBDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass")){
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "MakerStatus", "Pass", "TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"), "FeederSUBAccount");
					Reporting.logResults("Pass","Perform Maker Operation: "+mapFeederSUBDetailsTestData.get("OperationType"), "Successfully Performed Maker operations.");
					continue;
				}
				if(!bStatus && mapFeederSUBDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass")){
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederSUBAccountXMLFilePath, "MakerStatus", "Fail", "TestcaseName", mapFeederSUBDetailsTestData.get("TestCaseName"), "FeederSUBAccount");
					Reporting.logResults("Fail","Perform Maker Operation: "+mapFeederSUBDetailsTestData.get("OperationType"), "Cannot Perform Maker operations : "+Messages.errorMsg);
					continue;
				}
				if(!bStatus && mapFeederSUBDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Fail")){

					Reporting.logResults("Pass","Perform Maker Operation: "+mapFeederSUBDetailsTestData.get("OperationType"), "Negative testcase - Cannot Perform Maker operations : "+Messages.errorMsg);
					continue;
				}

				if(bStatus && mapFeederSUBDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Fail")){

					Reporting.logResults("Fail","Perform Maker Operation: "+mapFeederSUBDetailsTestData.get("OperationType"), "Able to Peformed Maker operations with negative testdata.");
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
