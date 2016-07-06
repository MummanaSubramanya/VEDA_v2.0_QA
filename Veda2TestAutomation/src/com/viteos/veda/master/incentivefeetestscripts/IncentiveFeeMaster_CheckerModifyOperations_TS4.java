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

public class IncentiveFeeMaster_CheckerModifyOperations_TS4 {
	static boolean bStatus;
	static String sIncentiveFeeSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Incentive Fee Master";
		Reporting.Testcasename = "Open Application";

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
			Map<String, Map<String, String>> verifyIncentiveFee = new HashMap<String, Map<String, String>>();
			Map<String, String> innerMap = new HashMap<String, String>();

			for(int index = 1;index <= mapAllIncentiveFeeDetails.size();index++){

				Map<String, String> mapModifiyIncentiveFeeDetails = mapAllIncentiveFeeDetails.get("Row"+index);
				Map<String, String> createdIncentiveFeeDetails =  ExcelUtils.readDataABasedOnCellName(Global.sIncentiveFeeTestDataFilePath, "IncentiveFeeDetails", mapModifiyIncentiveFeeDetails.get("TestcaseNameRef"));
				Reporting.Testcasename = mapModifiyIncentiveFeeDetails.get("TestcaseNameRef");

				if(mapModifiyIncentiveFeeDetails.get("Operation") == null || !mapModifiyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("Modify")){
					continue;
				}

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");				

				//Map<String, String> createdManagementFeeDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath,"FormulaTestData", mapModifiyIncentiveFeeDetails.get("TestcaseNameRef"));
				Map<String, String> createdXMLIncentiveFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sIncentiveXMLFilePath, "IncentiveFee", mapModifiyIncentiveFeeDetails.get("TestcaseNameRef")) ;
				Map<String, String> mapDetialsToModify = new HashMap<String, String>();
				
				if (createdXMLIncentiveFeeDetails == null || !createdXMLIncentiveFeeDetails.get("MakerStatus").equalsIgnoreCase("Pass")) {
					continue;
				}
				
				mapModifiyIncentiveFeeDetails.put("IncentiveFeeID", createdXMLIncentiveFeeDetails.get("IncentiveFeeID"));
				mapDetialsToModify.putAll(createdIncentiveFeeDetails);
				mapDetialsToModify.putAll(mapModifiyIncentiveFeeDetails);

				bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, createdXMLIncentiveFeeDetails.get("IncentiveFeeID"), mapModifiyIncentiveFeeDetails.get("CheckerOperations"));
				
				if(bStatus && mapModifiyIncentiveFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapModifiyIncentiveFeeDetails.get("CheckerOperations"));
				}

				if(!bStatus && mapModifiyIncentiveFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapModifiyIncentiveFeeDetails.get("CheckerOperations")+" \n.[ Error: "+Messages.errorMsg);					
				}

				if(!bStatus && mapModifiyIncentiveFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations: [ Error: "+Messages.errorMsg);
				}

				if(bStatus && mapModifiyIncentiveFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata for : " +mapDetialsToModify.get("IncentiveFeeID")+" with DETAILS : "+mapModifiyIncentiveFeeDetails);					
				}	

				if(mapModifiyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("Modify") && mapModifiyIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){					
					if(bStatus){
						NewUICommonFunctions.selectMenu("Fund Setup","Incentive Fee");
						boolean	bTestStatus = TempFunctions.verifyFeeTypeDetailsScreen("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"), "Incentive Fee Id", mapDetialsToModify.get("IncentiveFeeID"), "IncentiveFee", mapDetialsToModify);
						//boolean bTestStatus = IncentiveFeeAppFunctions.verifyIncentiveFeeOnEditScreen(mapDetialsToModify);						
						if(!bTestStatus){
							Reporting.logResults("Fail", "Verify Modify Details", "Details After Modification are not saved for Incentive fee : "+mapDetialsToModify.get("IncentiveFeeID")+" Error: "+Messages.errorMsg);
							continue;
						}
					}					
				}

				if(mapModifiyIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
					innerMap.put("Status", "active");
					innerMap.put("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"));
					innerMap.put("IncentiveFeeID", mapDetialsToModify.get("IncentiveFeeID"));
					verifyIncentiveFee.put(Reporting.Testcasename, innerMap);
				}
				if((mapModifiyIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Reject") || mapModifiyIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Return")) && bStatus){		
					innerMap.put("Status", "inactive");
					innerMap.put("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"));
					innerMap.put("IncentiveFeeID", mapDetialsToModify.get("IncentiveFeeID"));
					verifyIncentiveFee.put(Reporting.Testcasename, innerMap);
				}
			}
			//search for the ManagementFee
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
				//search function for Management Fee.
				bStatus = TempFunctions.verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument("Legal Entity Name", test.getValue().get("Legal Entity Name"), test.getValue().get("Status"), "Incentive Fee Id", test.getValue().get("IncentiveFeeID"), "IncentiveFee", time);
				
				//bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(test.getValue().get("IncentiveFeeID"), "Legal Entity Name", test.getValue().get("Legal Entity Name"), test.getValue().get("Status"), time);				
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Inactive Incentive Fee should be there in Search Grid.",test.getValue().get("IncentiveFeeID")+" : Inactive Incentive Fee is not present in Search Grid.");						
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Inactive Incentive Fee should be there in Search Grid.",test.getValue().get("IncentiveFeeID")+" : Inactive Incentive Fee is present in Search Grid.");					
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Active Incentive Fee should be there in Search Grid.",test.getValue().get("IncentiveFeeID")+" : Active Incentive Fee is Not present in Search Grid.");						
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Active Incentive Fee should be there in Search Grid.",test.getValue().get("IncentiveFeeID")+" : Active Incentive Fee is present in Search Grid.");
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

