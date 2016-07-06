package com.viteos.veda.master.feedistribution;

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

public class FeeDistribution_CheckerModifyOperations_TC4 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Fee Distribution Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		XMLLibrary.sFeeDistributionXMLFilePath = "XMLMessages//FeeDistribution02-10-2016234219.xml";
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

			Map<String, Map<String, String>> mapFeeDistributionDetails = Utilities.readMultipleTestData(Global.sFeeDistributionTestDataFilePath,sSheetName,"Y");
			Map<String, Map<String, String>> verifyFeeDistribution = new HashMap<String, Map<String, String>>();
			Map<String, String> innerMap = new HashMap<String, String>();

			for(int index = 1;index <= mapFeeDistributionDetails.size();index++){

				Map<String, String> mapModifiyFeeDistributionDetails = mapFeeDistributionDetails.get("Row"+index);
				Map<String, String> createdFeeDistributionDetails =  ExcelUtils.readDataABasedOnCellName(Global.sFeeDistributionTestDataFilePath, "FeeDistributionTestData", mapModifiyFeeDistributionDetails.get("TestcaseNameRef"));
				Reporting.Testcasename = mapModifiyFeeDistributionDetails.get("TestcaseNameRef");

				if(mapModifiyFeeDistributionDetails.get("Operation") == null || !mapModifiyFeeDistributionDetails.get("Operation").equalsIgnoreCase("Modify")){
					continue;
				}

				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");				

				//Read Test data
				Map<String, String> createdXMLIncentiveFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeeDistributionXMLFilePath, "FeeDistribution", mapModifiyFeeDistributionDetails.get("TestcaseNameRef")) ;
				Map<String, String> mapDetialsToModify = new HashMap<String, String>();

				if (createdXMLIncentiveFeeDetails == null || createdXMLIncentiveFeeDetails.get("FeeDistributionID").equalsIgnoreCase(" ")) {
					continue;
				}

				mapModifiyFeeDistributionDetails.put("FeeDistributionID", createdXMLIncentiveFeeDetails.get("FeeDistributionID"));
				mapDetialsToModify.putAll(createdFeeDistributionDetails);
				mapDetialsToModify.putAll(mapModifiyFeeDistributionDetails);

				bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, createdXMLIncentiveFeeDetails.get("FeeDistributionID"), mapModifiyFeeDistributionDetails.get("CheckerOperations"));

				if(mapModifiyFeeDistributionDetails.get("Operation").equalsIgnoreCase("Modify") && mapModifiyFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){					
					if(bStatus){
						NewUICommonFunctions.selectMenu("Fund Setup","Fee Distribution");
						boolean	bTestStatus = TempFunctions.verifyFeeTypeDetailsScreen("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"), "Fee Distribution Id", mapDetialsToModify.get("FeeDistributionID"), "FeeDist", mapModifiyFeeDistributionDetails);					
						if(!bTestStatus){
							Reporting.logResults("Fail", "Verify Modify Details", "Details After Modification are not saved for Fee Distribution : "+mapDetialsToModify.get("FeeDistributionID")+" Error: "+Messages.errorMsg);
						}
					}
					if(bStatus && mapModifiyFeeDistributionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapModifiyFeeDistributionDetails.get("CheckerOperations"));
					}

					if(!bStatus && mapModifiyFeeDistributionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
						Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapModifiyFeeDistributionDetails.get("CheckerOperations")+" \n.[ Error: "+Messages.errorMsg);					
					}

					if(!bStatus && mapModifiyFeeDistributionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations: [ Error: "+Messages.errorMsg);
					}

					if(bStatus && mapModifiyFeeDistributionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
						Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata for : " +mapDetialsToModify.get("FeeDistributionID")+" with DETAILS : "+mapModifiyFeeDistributionDetails);					
					}	

					if(mapModifiyFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
						innerMap.put("Status", "active");
						innerMap.put("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"));
						innerMap.put("FeeDistributionID", mapDetialsToModify.get("FeeDistributionID"));
						verifyFeeDistribution.put(Reporting.Testcasename, innerMap);
					}
					if((mapModifiyFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Reject") || mapModifiyFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Return")) && bStatus){		
						innerMap.put("Status", "inactive");
						innerMap.put("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"));
						innerMap.put("FeeDistributionID", mapDetialsToModify.get("FeeDistributionID"));
						verifyFeeDistribution.put(Reporting.Testcasename, innerMap);
					}
				}
			}
			//search for the Fee Distribution in Grid Table
			verifyValuesinSearchPanel(verifyFeeDistribution);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyIncentiveFee) {
		try{
			for (Entry<String, Map<String, String>> test : verifyIncentiveFee.entrySet()) {				

				Reporting.Testcasename = test.getKey();

				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Fee Distribution");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Incentive Fee","Cannot Navigate to Fee Distribution Page");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Incentive Fee", "Fee Distribution page");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}
				//search function for Fee Distribution.
				bStatus = TempFunctions.verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument("Legal Entity Name", test.getValue().get("Legal Entity Name"), test.getValue().get("Status"), "Fee Distribution Id", test.getValue().get("FeeDistributionID"), "FeeDist", time);
				
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Inactive Fee Distribution should be there in Search Grid.",test.getValue().get("FeeDistributionID")+" : Inactive Fee Distributionis not present in Search Grid.");						
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Inactive Fee Distribution should be there in Search Grid.",test.getValue().get("FeeDistributionID")+" : Inactive Fee Distribution is present in Search Grid.");					
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Active Fee Distribution should be there in Search Grid.",test.getValue().get("FeeDistributionID")+" : Active Fee Distribution is Not present in Search Grid.");						
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Active Fee Distribution should be there in Search Grid.",test.getValue().get("FeeDistributionID")+" : Active Fee Distribution is present in Search Grid.");
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

