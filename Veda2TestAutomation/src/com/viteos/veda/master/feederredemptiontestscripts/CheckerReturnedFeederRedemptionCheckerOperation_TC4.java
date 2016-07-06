package com.viteos.veda.master.feederredemptiontestscripts;

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
import com.viteos.veda.master.lib.FeederRedemptionAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TradeTypeSubscriptionAppFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class CheckerReturnedFeederRedemptionCheckerOperation_TC4 {
	static boolean bStatus;
	static String sSheetName = "CheckerReturnedMakerData";

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


			for(int index = 0;index < mapAllXMLFeederREDDetails.size();index++){

				Map<String, String> mapFeederREDDetails = mapAllXMLFeederREDDetails.get("Row"+index);

				Reporting.Testcasename = mapFeederREDDetails.get("TestcaseName");

				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeederREDAccountXMLFilePath, "FeederREDAccount", mapFeederREDDetails.get("TestCaseName"));
				if(mapXMLSubscriptionDetails == null || !mapXMLSubscriptionDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
					continue;
				}
				
				Map<String,String> mapClubbedDetails = new HashMap<>();
				if(mapXMLSubscriptionDetails.get("SearchID") != null){
					mapFeederREDDetails.put("SearchID", mapXMLSubscriptionDetails.get("SearchID"));
				}

				Map<String, String> mapFeederREDMakerTestData = ExcelUtils.readDataABasedOnCellName(Global.sFeedeerRedemptionTestDataFilePath, "FeederRedemptionTestData", mapFeederREDDetails.get("TestcaseName"));

				if(mapFeederREDDetails.get("Cash") != null){
					mapFeederREDMakerTestData.remove("Expected Cash");
					mapFeederREDMakerTestData.remove("Units");
				}

				if(mapFeederREDDetails.get("Units") != null){
					mapFeederREDMakerTestData.remove("Cash");
					mapFeederREDMakerTestData.remove("Expected Units");
				}
				mapClubbedDetails.putAll(mapFeederREDMakerTestData);
				mapClubbedDetails.putAll(mapFeederREDDetails);				

				//Navigate to DashBoard
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				bStatus = TradeTypeSubscriptionAppFunctions.performOperationsOnTableWithTransactionID("Feeder Redemption",dashboardMainDropdownToSelect.TRANSACTIONS, dashboardSubDropdownToSelect.NEW, mapClubbedDetails.get("SearchID"), mapClubbedDetails);
				if (!bStatus) {
					Reporting.logResults("Fail", "Select the Feeder Redemption", "Failed to Select the Feeder Redemption. "+Messages.errorMsg);
					continue;
				}
				
				//Perform Checker Verification Operation.
				bStatus = FeederRedemptionAppFunctions.doVerifyFeederRedemptionDetails(mapClubbedDetails);
				if (!bStatus) {
					Reporting.logResults("Fail", "Verification Of Feeder Redemption Details.", "Verification Of Feeder Redemption Details are NOT matching with Expected ."+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Verification Of Feeder Redemption Details.", "Verification Of Feeder Redemption Details are matching with Expected .");	


				//Perform Checker OPeration
				bStatus = FeederRedemptionAppFunctions.doCheckerOperationsForFeederRedemption(mapClubbedDetails.get("CheckerOperations"));

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
