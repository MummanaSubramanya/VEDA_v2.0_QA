package com.viteos.veda.master.feederredemptiontestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.FeederRedemptionAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class FeederRedemptionCheckerReturnedMakerOperation_TC3 {
	static boolean bStatus;
	static String sSheetName = "CheckerReturnedMakerData";
	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="FeederRedemption Master";
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
			
			Map<String, Map<String, String>> mapAllFeederREDMDetailsTestData = Utilities.readMultipleTestData(Global.sFeedeerRedemptionTestDataFilePath, sSheetName, "Y");

			for(int index = 1;index <= mapAllFeederREDMDetailsTestData.size();index++){

				Map<String, String> mapFeederREDMDetailsTestData = mapAllFeederREDMDetailsTestData.get("Row"+index);

				Reporting.Testcasename = mapFeederREDMDetailsTestData.get("TestCaseName");

				//Navigate to DashBoard

				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederREDAccountXMLFilePath, "FeederREDAccount", mapFeederREDMDetailsTestData.get("TestCaseName"));
				if(mapXMLSubscriptionDetails == null || !mapXMLSubscriptionDetails.get("CheckerStatus").equalsIgnoreCase("Pass")){
					continue;
				}

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "MakerStatus", "Fail" , "TestcaseName", mapFeederREDMDetailsTestData.get("TestCaseName"), "FeederREDAccount");Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				
				//Perform Operation on table to select the Transaction ID.
				
				bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Feeder Redemption",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapXMLSubscriptionDetails.get("SearchID"), mapFeederREDMDetailsTestData);
				if (!bStatus) {
					Reporting.logResults("Fail", "Select the Checker Returned Feeder Redemption", "Failed to Select the Checker Returned Feeder Redemption. "+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "MakerStatus", "Fail" , "TestcaseName", mapFeederREDMDetailsTestData.get("TestCaseName"), "FeederREDAccount");
					continue;
				}			

				//Editing feeder Redemption.
							
				bStatus =  FeederRedemptionAppFunctions.AddFeederRedemptionDetails(mapFeederREDMDetailsTestData);
				if(bStatus && mapFeederREDMDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Returned Maker Operation: "+mapFeederREDMDetailsTestData.get("OperationType"), "Successfully Performed Maker operations.");
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "MakerStatus", "Pass" , "TestcaseName", mapFeederREDMDetailsTestData.get("TestCaseName"), "FeederREDAccount");
					continue;
				}
				
				if(!bStatus && mapFeederREDMDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Returned Maker Operation: "+mapFeederREDMDetailsTestData.get("OperationType"), "Cannot Perform Maker operations : "+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "MakerStatus", "Fail" , "TestcaseName", mapFeederREDMDetailsTestData.get("TestCaseName"), "FeederREDAccount");
					continue;
				}
				if(!bStatus && mapFeederREDMDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Fail")){

					Reporting.logResults("Pass","Perform Checker Returned Maker Operation: "+mapFeederREDMDetailsTestData.get("OperationType"), "Negative testcase - Cannot Perform Maker operations : "+Messages.errorMsg);
					continue;
				}

				if(bStatus && mapFeederREDMDetailsTestData.get("ExpectedResults").equalsIgnoreCase("Fail")){

					Reporting.logResults("Fail","Perform Checker Returned Maker Operation: "+mapFeederREDMDetailsTestData.get("OperationType"), "Able to Peformed Maker operations with negative testdata.");
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
