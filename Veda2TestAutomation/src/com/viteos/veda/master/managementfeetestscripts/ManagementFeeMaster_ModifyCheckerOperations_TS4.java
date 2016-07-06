package com.viteos.veda.master.managementfeetestscripts;

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
import com.viteos.veda.master.lib.ManagementFeeAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class ManagementFeeMaster_ModifyCheckerOperations_TS4 {
	static boolean bStatus;
	static String sManagementFeeSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Management Fee Master";
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

			Map<String, Map<String, String>> mapAllManagementFeeDetails = Utilities.readMultipleTestData(Global.sManagementFeeTestDataFilePath,sManagementFeeSheetName,"Y");
			Map<String, Map<String, String>> verifyManagementFee = new HashMap<String, Map<String, String>>();


			for(int index = 1;index <= mapAllManagementFeeDetails.size();index++){
				Map<String, String> innerMap = new HashMap<String, String>();
				//Map<String , String> formulaSetupDetails = new HashMap<String, String>();
				Map<String, String> mapModifiyManagementFeeDetails = mapAllManagementFeeDetails.get("Row"+index);
				Map<String, String> createdManagementFeeDetails =  ExcelUtils.readDataABasedOnCellName(Global.sManagementFeeTestDataFilePath, "ManagementFeeDetails", mapModifiyManagementFeeDetails.get("TestcaseNameRef"));
				Reporting.Testcasename = mapModifiyManagementFeeDetails.get("TestcaseNameRef");
				if(mapModifiyManagementFeeDetails.get("Operation") == null || !mapModifiyManagementFeeDetails.get("Operation").equalsIgnoreCase("Modify")){
					continue;
				}
				if(mapModifiyManagementFeeDetails.get("OperationType").equalsIgnoreCase("Cancel") || mapModifiyManagementFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail") ){
					continue;
				}
				Map<String, String> createdXMLManagementFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sManagementXMLFilePath, "ManagementFee", mapModifiyManagementFeeDetails.get("TestcaseNameRef")) ;
				if(createdXMLManagementFeeDetails == null || !createdXMLManagementFeeDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
					continue;
				}				

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");				


				Map<String, String> mapDetialsToModify = new HashMap<String, String>();

				mapModifiyManagementFeeDetails.put("ManagementFeeID", createdXMLManagementFeeDetails.get("ManagementFeeID"));
				mapDetialsToModify.putAll(createdManagementFeeDetails);
				mapDetialsToModify.putAll(mapModifiyManagementFeeDetails);
								
				//Performs Checker Operations for the New Rule in management fee test data.Multiple Checker Operations
				if (mapModifiyManagementFeeDetails.get("New Rule") != null && mapModifiyManagementFeeDetails.get("New Rule").equalsIgnoreCase("Yes")) 
				{
					//if checker operation for formula is Returned or Rejected than there is no Checker Operation for Management fee.
					if(mapModifiyManagementFeeDetails.get("CheckerOperationsOnFormula")!=null && !mapModifiyManagementFeeDetails.get("CheckerOperationsOnFormula").equalsIgnoreCase("Approve"))
					{
						bStatus = ManagementFeeAppFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,createdXMLManagementFeeDetails.get("ManagementFeeID"), mapModifiyManagementFeeDetails.get("CheckerOperationsOnFormula"),null);
						if(mapModifiyManagementFeeDetails.get("CheckerOperations") == null || mapModifiyManagementFeeDetails.get("ExpectedResultsAfterCheckerOperations") == null){
							continue;
						}
					}
					
					//if Checker for New Rule  is Approve
					if(mapModifiyManagementFeeDetails.get("CheckerOperationsOnFormula")!=null && mapModifiyManagementFeeDetails.get("CheckerOperationsOnFormula").equalsIgnoreCase("Approve"))
					{

						bStatus = ManagementFeeAppFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, createdXMLManagementFeeDetails.get("ManagementFeeID"), mapModifiyManagementFeeDetails.get("CheckerOperationsOnFormula"), mapModifiyManagementFeeDetails.get("CheckerOperations"));
						if(mapModifiyManagementFeeDetails.get("CheckerOperationsOnFormula").equalsIgnoreCase("Approve") && mapModifiyManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus)
						{
							NewUICommonFunctions.selectMenu("Fund Setup","Management Fee");
							mapDetialsToModify.put("Management Fee On", null);
							boolean bTestStatus = TempFunctions.verifyFeeTypeDetailsScreen("Legal Entity Name",mapDetialsToModify.get("Legal Entity Name"), "Management Fee Id",mapDetialsToModify.get("ManagementFeeID"), "ManagementFee", mapDetialsToModify);							
							if(!bTestStatus){
								Reporting.logResults("Fail", "Verify Modify Details", "Details After Modification are not saved for management fee with management fee with a new rule. Error: "+Messages.errorMsg);
								continue;
							}
							innerMap.put("Status", "active");
							innerMap.put("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"));
							innerMap.put("ManagementFeeID", mapDetialsToModify.get("ManagementFeeID"));
							verifyManagementFee.put(Reporting.Testcasename, innerMap);
						}
						if((mapModifiyManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Reject") || mapModifiyManagementFeeDetails.get("CheckerOperationsOnFormula").equalsIgnoreCase("Reject")) && bStatus)
						{				
							innerMap.put("Status", "inactive");
							innerMap.put("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"));
							innerMap.put("ManagementFeeID", mapDetialsToModify.get("ManagementFeeID"));
							verifyManagementFee.put(Reporting.Testcasename,innerMap);
						}
					}
				}
				
				//Performs Checker Operations for the Created Rule in management fee test data.Single Checker Operation
				if (mapModifiyManagementFeeDetails.get("New Rule") == null || !mapModifiyManagementFeeDetails.get("New Rule").equalsIgnoreCase("Yes"))
				{

					bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, createdXMLManagementFeeDetails.get("ManagementFeeID"), mapModifiyManagementFeeDetails.get("CheckerOperations"));
					if(bStatus && mapModifiyManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve"))
					{
						NewUICommonFunctions.selectMenu("Fund Setup","Management Fee");
						mapDetialsToModify.put("New Rule", null);
						boolean bTestStatus = TempFunctions.verifyFeeTypeDetailsScreen("Legal Entity Name",mapDetialsToModify.get("Legal Entity Name"), "Management Fee Id",mapDetialsToModify.get("ManagementFeeID"), "ManagementFee", mapDetialsToModify);
								//ManagementFeeAppFunctions.verifyManagementFeeDetailsInEditScreen(mapDetialsToModify , formulaSetupDetails);
						if(!bTestStatus){
							Reporting.logResults("Fail", "Verify Modify Details", "Details After Modification are not saved for management fee management fee with an existing rule. Error: "+Messages.errorMsg);
							continue;
						}
						innerMap.put("Status", "active");
						innerMap.put("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"));
						innerMap.put("ManagementFeeID", mapDetialsToModify.get("ManagementFeeID"));
						verifyManagementFee.put(Reporting.Testcasename, innerMap);
					}
					if(mapModifiyManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus)
					{			
						innerMap.put("Status", "inactive");
						innerMap.put("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"));
						innerMap.put("ManagementFeeID", mapDetialsToModify.get("ManagementFeeID"));
						verifyManagementFee.put(Reporting.Testcasename,innerMap);
					}
				}

				if(bStatus && mapModifiyManagementFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapModifiyManagementFeeDetails.get("CheckerOperations"));
				}

				if(!bStatus && mapModifiyManagementFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapModifiyManagementFeeDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					continue;
				}

				if(!bStatus && mapModifiyManagementFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations: "+mapModifiyManagementFeeDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
				}

				if(bStatus && mapModifiyManagementFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Formula Name: "+mapModifiyManagementFeeDetails.get("FeeRuleName"));
					continue;
				}	
			}
			//search for the ManagementFee
			verifyValuesinSearchPanel(verifyManagementFee);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyManagementFee) {
		try{
			for (Entry<String, Map<String, String>> test : verifyManagementFee.entrySet()) {				

				Reporting.Testcasename = test.getKey();

				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Management Fee");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Management Fee","Cannot Navigate to Management Fee Page");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Management Fee", "Navigated to Management Fee page");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}
				//search function for Management Fee.
				bStatus = TempFunctions.verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument("Legal Entity Name", test.getValue().get("Legal Entity Name"), test.getValue().get("Status"), "Management Fee Id", test.getValue().get("ManagementFeeID"), "ManagementFee", time);				
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Management Fee shouldnot be Inactive in Search Grid.",test.getValue().get("ManagementFeeID")+" : Inactive Management Fee is Not visible in Search Grid.");						
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Management Fee should be Inactive in Search Grid.",test.getValue().get("ManagementFeeID")+" :Inactive Management Fee is Visible in in Search Grid.");					
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Management Fee should be Active in Search Grid.",test.getValue().get("ManagementFeeID")+" : Active Management Fee is Not-Visible in Search Grid.");						
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Management Fee should be Active in Search Grid.",test.getValue().get("ManagementFeeID")+" : Active Management Fee is visible in Search Grid.");
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
