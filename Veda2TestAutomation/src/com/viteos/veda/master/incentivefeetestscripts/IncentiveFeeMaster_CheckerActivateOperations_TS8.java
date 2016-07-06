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

public class IncentiveFeeMaster_CheckerActivateOperations_TS8 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality = "Incentive Fee Master";
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
	public static void testSearchFunctions(){
		Map<String, Map<String, String>> mapAllIncentiveFeeDetails = Utilities.readMultipleTestData(Global.sIncentiveFeeTestDataFilePath,sSheetName,"Y");
		Map<String, Map<String, String>> verifyIncentiveFeeMap = new HashMap<String, Map<String, String>>();
		Map<String, String> innerMap = new HashMap<String, String>();
		for(int index = 1;index <= mapAllIncentiveFeeDetails.size();index++){
			Map<String, String> mapModifyIncentiveFeeDetails = mapAllIncentiveFeeDetails.get("Row"+index);

			if(mapModifyIncentiveFeeDetails.get("Operation") == null || !mapModifyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("Activate")){
				continue;
			}

			Reporting.Testcasename = mapModifyIncentiveFeeDetails.get("TestcaseNameRef");

			//read testdata
			Map<String, String> createdXMLIncentiveFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sIncentiveXMLFilePath, "IncentiveFee",mapModifyIncentiveFeeDetails.get("TestcaseNameRef"));
			Map<String, String> createdIncentiveFeeDetails =  ExcelUtils.readDataABasedOnCellName(Global.sIncentiveFeeTestDataFilePath, "IncentiveFeeDetails", mapModifyIncentiveFeeDetails.get("TestcaseNameRef"));
			if (createdXMLIncentiveFeeDetails == null || createdXMLIncentiveFeeDetails.get("IncentiveFeeID").equalsIgnoreCase("")) {
				continue;
			}
			mapModifyIncentiveFeeDetails.put("IncentiveFeeID", createdXMLIncentiveFeeDetails.get("IncentiveFeeID"));
			createdIncentiveFeeDetails.put("IncentiveFeeID", createdXMLIncentiveFeeDetails.get("IncentiveFeeID"));
			createdIncentiveFeeDetails.putAll(mapModifyIncentiveFeeDetails);

			//navigate to Client Module
			bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");

			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Dashboard", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Dashboard", "Navigated To Dashboard succesfully");
			if (mapModifyIncentiveFeeDetails.get("CheckerOperations") == null) {
				continue;
			}
			bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, createdIncentiveFeeDetails.get("IncentiveFeeID"), createdIncentiveFeeDetails.get("CheckerOperations"));

			if(mapModifyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("Activate")){								
				if(mapModifyIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){						
					if(!bStatus){
						Reporting.logResults("Fail","Checker : Validate Incentive Fee Activation request Approved.", " Checker : Incentive Fee activation request cannot be Approved : "+Messages.errorMsg);
						continue;
					}	
					innerMap.put("Status", "active");
					innerMap.put("IncentiveFeeID", createdIncentiveFeeDetails.get("IncentiveFeeID"));
					innerMap.put("Legal Entity Name", createdIncentiveFeeDetails.get("Legal Entity Name"));
					verifyIncentiveFeeMap.put(Reporting.Testcasename, innerMap);
				}	
				if (mapModifyIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Reject") || mapModifyIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Return")) {
					innerMap.put("Status", "inactive");
					innerMap.put("IncentiveFeeID", createdIncentiveFeeDetails.get("IncentiveFeeID"));
					innerMap.put("Legal Entity Name", createdIncentiveFeeDetails.get("Legal Entity Name"));
					verifyIncentiveFeeMap.put(Reporting.Testcasename, innerMap);					
				}
			}
		}		
		verifyValuesinSearchPanel(verifyIncentiveFeeMap);
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

	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyIncentiveFeeMap) {
		try{
			for (Entry<String, Map<String, String>> test : verifyIncentiveFeeMap.entrySet()) {				

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
}
