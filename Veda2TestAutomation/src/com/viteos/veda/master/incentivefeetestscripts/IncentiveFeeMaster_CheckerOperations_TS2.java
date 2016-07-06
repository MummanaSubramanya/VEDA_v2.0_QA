package com.viteos.veda.master.incentivefeetestscripts;

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
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class IncentiveFeeMaster_CheckerOperations_TS2 {
	static boolean bStatus;
	static String sIncentiveFeeSheetName = "IncentiveFeeDetails";

	@BeforeMethod
	public static void setUp(){
		//XMLLibrary.sIncentiveXMLFilePath = "XMLMessages//IncentiveFee02-11-2016213520.xml";
		Reporting.Functionality ="Incentive Fee Master";
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
			Map<String, Map<String, String>> mapAllIncentiveFeeDetails = Utilities.readMultipleTestData(Global.sIncentiveFeeTestDataFilePath,sIncentiveFeeSheetName,"Y");
			//Map<String, Map<String, String>> mapAllXMLStoredIncentiveFeeDetails = XMLLibrary.getDetailsMap(XMLLibrary.sIncentiveXMLFilePath, "//IncentiveFeeDetails/IncentiveFee");
			/*if (mapAllXMLStoredIncentiveFeeDetails == null) {
				return;
			}*/
			Map<String, Map<String, String>> verifyIncentiveFee = new HashMap<String, Map<String, String>>();			

			for(int index = 1;index <= mapAllIncentiveFeeDetails.size();index++){
				
				Map<String, String> innerMap = new HashMap<String, String>();
				
				Map<String, String> mapIncentiveFeeDetails = mapAllIncentiveFeeDetails.get("Row"+index);
				Reporting.Testcasename = mapIncentiveFeeDetails.get("TestcaseName");
				if(mapIncentiveFeeDetails == null || mapIncentiveFeeDetails.isEmpty() || !mapIncentiveFeeDetails.get("OperationType").equalsIgnoreCase("Save") || mapIncentiveFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}
				
				Map<String, String> mapXMLIncentiveFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sIncentiveXMLFilePath, "IncentiveFee", Reporting.Testcasename);
				if(mapXMLIncentiveFeeDetails == null || !mapXMLIncentiveFeeDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
					continue;
				}
				mapIncentiveFeeDetails.put("IncentiveFeeID", mapXMLIncentiveFeeDetails.get("IncentiveFeeID"));

				//Navigate To Dash board.
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				//Performs Checker Operations for the Incentive fee test data.

				bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapIncentiveFeeDetails.get("IncentiveFeeID"), mapIncentiveFeeDetails.get("CheckerOperations"));


				if(bStatus && mapIncentiveFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapIncentiveFeeDetails.get("CheckerOperations"));
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "CheckerStatus", "Pass", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
				}

				if(!bStatus && mapIncentiveFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapIncentiveFeeDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
					continue;
				}

				if(!bStatus && mapIncentiveFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations : "+mapIncentiveFeeDetails.get("CheckerOperations")+".");
				}

				if(bStatus && mapIncentiveFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Error: "+Messages.errorMsg+ " : "+mapIncentiveFeeDetails);
					continue;
				}	

				if(mapIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
					innerMap.put("Status", "active");
					innerMap.put("Legal Entity Name", mapIncentiveFeeDetails.get("Legal Entity Name"));
					innerMap.put("IncentiveFeeID", mapIncentiveFeeDetails.get("IncentiveFeeID"));
					verifyIncentiveFee.put(Reporting.Testcasename,innerMap);
				}

				if(mapIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){				
					innerMap.put("Status", "inactive");
					innerMap.put("Legal Entity Name", mapIncentiveFeeDetails.get("Legal Entity Name"));
					innerMap.put("IncentiveFeeID", mapIncentiveFeeDetails.get("IncentiveFeeID"));
					verifyIncentiveFee.put(Reporting.Testcasename,innerMap);
				}			
			}
			verifyValuesinSearchPanel(verifyIncentiveFee);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyIncentiveFee) {
		try{
			for (Entry<String, Map<String, String>> test : verifyIncentiveFee.entrySet()) {				

				Reporting.Testcasename = test.getKey();

				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Incentive Fee");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Incentive Fee","Cannot Navigate to Incentive Fee Page");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Incentive Fee", "Navigated to Incentive Fee page");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}
				//search function for Incentive Fee.
				bStatus = TempFunctions.verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument("Legal Entity Name", test.getValue().get("Legal Entity Name"), test.getValue().get("Status"), "Incentive Fee Id", test.getValue().get("IncentiveFeeID"), "IncentiveFee", time);				
				
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Incentive Fee should Not be Active in Search Grid.",test.getValue().get("IncentiveFeeID")+" : Incentive Fee is Not-Active in Search Grid.");
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Incentive Fee should be Inactive in Search Grid.",test.getValue().get("IncentiveFeeID")+" : Incentive Fee is Active in Search Grid.");
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Incentive Fee should be Active in Search Grid.",test.getValue().get("IncentiveFeeID")+" : Incentive Fee is Not-Active in Search Grid.");
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Incentive Fee should be Active in Search Grid.",test.getValue().get("IncentiveFeeID")+" : Incentive Fee is Active in Search Grid.");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	@AfterMethod
	public static void tearDown(){
		Reporting.Testcasename = "Close app";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
