package com.viteos.veda.master.openingbalancetestscripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.XMLLibrary;

public class OpeningBalanceMaster_CheckerOperations_TS2 {
	static boolean bStatus;
	static String sSheetName = "OpeningBalanceTestData";

	@BeforeMethod
	public static void setUp(){
		
		//XMLLibrary.sOpeningBalanceDetailsXMLFilePath = "XMLMessages//OpeningBalanceDetails02-25-2016202743.xml";

		Reporting.Functionality ="Opening Balance Master Setup";
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
			Map<String, Map<String, String>> mapAllXMLOpeningBalanceDetails = XMLLibrary.getDetailsMap(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "//OpeningBalanceDetails/OpeningBalance");			
			Map<String, Map<String, String>> verifyOpeningBalance = new HashMap<>();
			for(int index = 0;index < mapAllXMLOpeningBalanceDetails.size();index++){
				Map<String, String> innerMap = new HashMap<String, String>();
				Map<String, String> mapOpeningBalanceXMLDetails = mapAllXMLOpeningBalanceDetails.get("Row"+index);
				
				
				Map<String, String> mapOpeningBalanceTestData = ExcelUtils.readDataABasedOnCellName(Global.sOpeningBalanceTestDataFilePath, sSheetName, mapOpeningBalanceXMLDetails.get("TestcaseName"));

				if (mapOpeningBalanceTestData == null || mapOpeningBalanceTestData.isEmpty() || !mapOpeningBalanceTestData.get("ExecutionStatus").equalsIgnoreCase("Y") || mapOpeningBalanceTestData.get("CheckerOperations") == null || mapOpeningBalanceTestData.get("OperationType") == null || mapOpeningBalanceTestData.get("OperationType").equalsIgnoreCase("Cancel")) {
					continue;
				}
				Reporting.Testcasename = mapOpeningBalanceXMLDetails.get("TestcaseName");
				if(!mapOpeningBalanceXMLDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
					continue;
				}
				
				//Navigate to DashBoard

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}				
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				//Perform Checker Operation.
				bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapOpeningBalanceXMLDetails.get("SearchID"), mapOpeningBalanceTestData.get("CheckerOperations"));
										
				if(bStatus && mapOpeningBalanceTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "CheckerStatus", "Pass", "TestcaseName", mapOpeningBalanceTestData.get("TestCaseName"), "OpeningBalance");
					Reporting.logResults("Pass","Perform Checker Operation: "+mapOpeningBalanceTestData.get("CheckerOperations"), "Successfully Performed checker operations for transaction ID : "+mapOpeningBalanceXMLDetails.get("SearchID"));
				}
				if(!bStatus && mapOpeningBalanceTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sOpeningBalanceDetailsXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", mapOpeningBalanceTestData.get("TestCaseName"), "OpeningBalance");
					Reporting.logResults("Fail","Perform Checker Operation: "+mapOpeningBalanceTestData.get("CheckerOperations"), "Cannot Perform Checker Operations for transaction ID : "+mapOpeningBalanceXMLDetails.get("SearchID")+". Error: "+Messages.errorMsg);
					continue;
				}
				if(!bStatus && mapOpeningBalanceTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation: "+mapOpeningBalanceTestData.get("CheckerOperations"), "Negative testcase - Cannot Perform Checker Operations .Error: "+Messages.errorMsg);
				}
				if(bStatus && mapOpeningBalanceTestData.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation: "+mapOpeningBalanceTestData.get("CheckerOperations"), "Peformed Checker operations with negative testdata. transaction ID : "+mapOpeningBalanceXMLDetails.get("SearchID"));
					continue;
				}
				if(mapOpeningBalanceTestData.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
					innerMap.put("Status", "active");
					innerMap.put("SearchID", mapOpeningBalanceXMLDetails.get("SearchID"));
					verifyOpeningBalance.put(Reporting.Testcasename,innerMap);
				}
				if(mapOpeningBalanceTestData.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){				
					innerMap.put("Status", "inactive");
					innerMap.put("SearchID", mapOpeningBalanceXMLDetails.get("SearchID"));
					verifyOpeningBalance.put(Reporting.Testcasename,innerMap);
				}	
			}
			//Verify Checker Operations are taken place.
			verifyValuesinSearchPanel(verifyOpeningBalance);
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
	
	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyOpeningBalance) {
		try{
			for (Entry<String, Map<String, String>> test : verifyOpeningBalance.entrySet()) {				

				Reporting.Testcasename = test.getKey();

				bStatus = NewUICommonFunctions.selectMenu("TRADES", "Opening Balance");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Opening Balance","Cannot Navigate to Opening Balance Page");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Opening Balance", "Navigated to Opening Balance page");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}
				//search function for Incentive Fee.
				bStatus = NewUICommonFunctions.verifyTheRecordInSearchPanelWithStatusAndTxnIDAlongWithOperation(test.getValue().get("Status"), "Opening balance Id", test.getValue().get("SearchID"), "OpeningBalance", time, "");			
				
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Checker : Verify Opening Balance should Not be Active in Search Grid.", "Opening Balance with TXN ID : "+test.getValue().get("SearchID")+" ,is Not-Active in Search Grid.");
					continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Checker : Verify Opening Balance should be Inactive in Search Grid.", "Opening Balance with TXN ID : "+test.getValue().get("SearchID")+" , is Active in Search Grid.");
					continue;
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Checker : Verify Opening Balance should be Active in Search Grid.", "Opening Balance with TXN ID : "+test.getValue().get("SearchID")+" , is Not-Active in Search Grid.");
					continue;
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Checker : Verify Opening Balance should be Active in Search Grid.", "Opening Balance with TXN ID : "+test.getValue().get("SearchID")+" , is Active in Search Grid.");
					continue;
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
