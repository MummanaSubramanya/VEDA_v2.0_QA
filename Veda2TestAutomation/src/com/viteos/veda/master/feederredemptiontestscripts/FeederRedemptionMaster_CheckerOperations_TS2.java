package com.viteos.veda.master.feederredemptiontestscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.FeederRedemptionAppFunctions;
import com.viteos.veda.master.lib.FeederSubscriptionAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.XMLLibrary;

public class FeederRedemptionMaster_CheckerOperations_TS2 {

	static boolean bStatus;
	static String sSheetName = "FeederRedemptionTestData";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="FeederRedemption Master";
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
			Map<String, Map<String, String>> mapAllXMLFeederREDDetails = Utilities.readMultipleTestData(Global.sFeedeerRedemptionTestDataFilePath, sSheetName, "Y");
			
			for(int index = 1;index <=mapAllXMLFeederREDDetails.size();index++){

				Map<String, String> mapFeederREDTestData = mapAllXMLFeederREDDetails.get("Row"+index);

				Reporting.Testcasename = mapFeederREDTestData.get("TestCaseName");
				
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederREDAccountXMLFilePath, "FeederREDAccount", mapFeederREDTestData.get("TestCaseName"));
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
					
				bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionIDAndMasterType(dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapXMLSubscriptionDetails.get("SearchID"), "Feeder RED", "");			
				if (!bStatus) {
					Reporting.logResults("Fail", "Get the created record from dashboard to verify and perform checker operations.", "Unable to Get the created record from dashboard to verify and perform checker operations."+Messages.errorMsg);
					continue;
				}
				//Perform Checker Verification Operation.
				bStatus = FeederRedemptionAppFunctions.doVerifyFeederRedemptionDetails(mapFeederREDTestData);
				if (!bStatus) {
					Reporting.logResults("Fail", "Verification Of Created Feeder Redemption Details.", "Verification Of Created Feeder Redemption Details are NOT matching with Expected ."+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "CheckerStatus", "Fail" , "TestcaseName", mapFeederREDTestData.get("TestCaseName"), "FeederREDAccount");
					continue;
				}				
				Reporting.logResults("Pass", "Verification Of Created Feeder Redemption Details.", "Verification Of Created Feeder Redemption Details are matching with Expected .");		
				
				//Perform Checker OPeration
				bStatus = FeederRedemptionAppFunctions.doCheckerOperationsForFeederRedemption(mapFeederREDTestData.get("CheckerOperations"));
				
				if(bStatus && mapFeederREDTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapFeederREDTestData.get("CheckerOperations"), "Successfully Performed checker operations for transaction ID : "+mapXMLSubscriptionDetails.get("SearchID"));
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "CheckerStatus", "Pass" , "TestcaseName", mapFeederREDTestData.get("TestCaseName"), "FeederREDAccount");
					continue;
				}
				if(!bStatus && mapFeederREDTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapFeederREDTestData.get("CheckerOperations"), "Cannot Perform Checker Operations for transaction ID : "+mapXMLSubscriptionDetails.get("SearchID")+". Error: "+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sFeederREDAccountXMLFilePath, "CheckerStatus", "Fail" , "TestcaseName", mapFeederREDTestData.get("TestCaseName"), "FeederREDAccount");
					continue;
				}
				if(!bStatus && mapFeederREDTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapFeederREDTestData.get("CheckerOperations"), "Negative testcase - Cannot Perform Checker Operations .Error: "+Messages.errorMsg);
					continue;
				}

				if(bStatus && mapFeederREDTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapFeederREDTestData.get("CheckerOperations"), "Peformed Checker operations with negative testdata. transaction ID : "+mapXMLSubscriptionDetails.get("SearchID"));
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
