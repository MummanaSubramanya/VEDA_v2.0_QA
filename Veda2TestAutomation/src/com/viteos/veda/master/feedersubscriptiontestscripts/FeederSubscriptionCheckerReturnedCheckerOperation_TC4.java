package com.viteos.veda.master.feedersubscriptiontestscripts;

import java.util.HashMap;
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
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class FeederSubscriptionCheckerReturnedCheckerOperation_TC4 {
	static boolean bStatus;
	static String sSheetName = "CheckerReturnedMakerData";

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

			for(int index = 0;index < mapAllFeederSUBDetails.size();index++){

				Map<String, String> mapFeederSUBDetails = mapAllFeederSUBDetails.get("Row"+index);

				Reporting.Testcasename = mapFeederSUBDetails.get("TestCaseName");
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederSUBAccountXMLFilePath, "FeederSUBAccount", mapFeederSUBDetails.get("TestCaseName"));
				if(mapXMLSubscriptionDetails == null || !mapXMLSubscriptionDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
					continue;
				}
				if(mapXMLSubscriptionDetails.get("SearchID") != null){
					mapFeederSUBDetails.put("SearchID", mapXMLSubscriptionDetails.get("SearchID"));
				}
				Map<String, String> mapFeederSUBMakerTestData = ExcelUtils.readDataABasedOnCellName(Global.sFeederSubscriptionTestDataFilePath, "FeederSubscriptionTestData", mapFeederSUBDetails.get("TestcaseName"));
				
				Map<String,String> mapClubbedDetails = new HashMap<String, String>();
				//Navigate to DashBoard
				if(mapFeederSUBDetails.get("Cash") != null){
					mapFeederSUBMakerTestData.remove("Expected Cash");
					mapFeederSUBMakerTestData.remove("Units");
				}

				if(mapFeederSUBDetails.get("Units") != null){
					mapFeederSUBMakerTestData.remove("Cash");
					mapFeederSUBMakerTestData.remove("Expected Units");
				}
				
				mapClubbedDetails.putAll(mapFeederSUBMakerTestData);
				mapClubbedDetails.putAll(mapFeederSUBDetails);	
				
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				//Perform Operation on Table
				bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Feeder Subscription",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapClubbedDetails.get("SearchID"), mapClubbedDetails);
				if (!bStatus) {
					Reporting.logResults("Fail", "Get the record from dashboard to verify and perform checker operations.", "Unable to Get the record from dashboard to verify and perform checker operations."+Messages.errorMsg);
					continue;
				}
				
				//Perform Checker Verification Operation
				bStatus = FeederSubscriptionAppFunctions.doVerifyFeederSubscriptionDetails(mapClubbedDetails);
				if (!bStatus) {
					Reporting.logResults("Fail", "Verification Of Created Feeder SubscriptionDetails.", "Verification Of Created Feeder Subscription Details are NOT matching with Expected ."+Messages.errorMsg);
					continue;
				}
				
				Reporting.logResults("Pass", "Verification Of Created Feeder SubscriptionDetails.", "Verification Of Created Feeder Subscription Details are matching with Expected .");
				
								
				//Perform Checker OPeration
				bStatus = FeederSubscriptionAppFunctions.doCheckerOperationsForFeederSubscription(mapClubbedDetails.get("CheckerOperations"));
				
				if(bStatus && mapClubbedDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){

					Reporting.logResults("Pass","Perform Checker Operation: "+mapClubbedDetails.get("CheckerOperations"), "Successfully Performed checker operations for transaction ID : "+mapClubbedDetails.get("SearchID"));
				}
				if(!bStatus && mapClubbedDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){

					Reporting.logResults("Fail","Perform Checker Operation: "+mapClubbedDetails.get("CheckerOperations"), "Cannot Perform Checker Operations for transaction ID : "+mapClubbedDetails.get("SearchID")+". Error: "+Messages.errorMsg);
					continue;
				}
				if(!bStatus && mapClubbedDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){

					Reporting.logResults("Pass","Perform Checker Operation: "+mapClubbedDetails.get("CheckerOperations"), "Negative testcase - Cannot Perform Checker Operations .Error: "+Messages.errorMsg);
				}

				if(bStatus && mapClubbedDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){

					Reporting.logResults("Fail","Perform Checker Operation: "+mapClubbedDetails.get("CheckerOperations"), "Peformed Checker operations with negative testdata. transaction ID : "+mapClubbedDetails.get("SearchID"));
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
