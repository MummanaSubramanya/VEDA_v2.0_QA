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
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.XMLLibrary;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;


public class FeeDistributionMaster_CheckerOperations_TC2 {
	static boolean bStatus;
	static String sSheetName = "FeeDistributionTestData";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="Fee Distribution Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;
		
		//XMLLibrary.sFeeDistributionXMLFilePath = "XMLMessages//FeeDistribution02-11-2016132210.xml";
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

			
			Map<String, Map<String, String>> mapAllXMLStoredFeeDistributionDetails = XMLLibrary.getDetailsMap(XMLLibrary.sFeeDistributionXMLFilePath, "//FeeDistribution");
			if (mapAllXMLStoredFeeDistributionDetails == null) {
				return;
			}
			
			
			Map<String, Map<String, String>> verifyFeeDistribution = new HashMap<String, Map<String, String>>();			

			for(int index = 0;index < mapAllXMLStoredFeeDistributionDetails.size();index++){
				
				Map<String, String> innerMap = new HashMap<String, String>();
				
				Map<String, String> mapXMLFeeDistributionDetails = mapAllXMLStoredFeeDistributionDetails.get("Row"+index);
				Map<String, String> mapFeeDistributionDetails = ExcelUtils.readDataABasedOnCellName(Global.sFeeDistributionTestDataFilePath, sSheetName, mapXMLFeeDistributionDetails.get("TestcaseName"));
				mapFeeDistributionDetails.put("FeeDistributionID", mapXMLFeeDistributionDetails.get("FeeDistributionID"));
				
				if(mapFeeDistributionDetails.get("OperationType") == null || !mapFeeDistributionDetails.get("OperationType").equalsIgnoreCase("Save") || mapFeeDistributionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
					continue;
				}
				Reporting.Testcasename = mapFeeDistributionDetails.get("TestcaseName");

				//Navigate To Dash board.
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				//Performs Checker Operations for the Incentive fee test data.

				bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapFeeDistributionDetails.get("FeeDistributionID"), mapFeeDistributionDetails.get("CheckerOperations"));


				if(bStatus && mapFeeDistributionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapFeeDistributionDetails.get("CheckerOperations"));
				}

				if(!bStatus && mapFeeDistributionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapFeeDistributionDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					continue;
				}

				if(!bStatus && mapFeeDistributionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations : "+mapFeeDistributionDetails.get("CheckerOperations")+".");
				}

				if(bStatus && mapFeeDistributionDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Error: "+Messages.errorMsg+ " : "+mapFeeDistributionDetails);
					continue;
				}	

				if(mapFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){		
					innerMap.put("Status", "active");
					innerMap.put("Legal Entity Name", mapFeeDistributionDetails.get("Legal Entity Name"));
					innerMap.put("FeeDistributionID", mapFeeDistributionDetails.get("FeeDistributionID"));
					verifyFeeDistribution.put(Reporting.Testcasename,innerMap);
				}

				if(mapFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){				
					innerMap.put("Status", "inactive");
					innerMap.put("Legal Entity Name", mapFeeDistributionDetails.get("Legal Entity Name"));
					innerMap.put("FeeDistributionID", mapFeeDistributionDetails.get("FeeDistributionID"));
					verifyFeeDistribution.put(Reporting.Testcasename,innerMap);
				}			
			}
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
					Reporting.logResults("Fail", "Navigate To Incentive Fee","Cannot Navigate to Fee Distribution Master Page");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Incentive Fee", "Navigated to Fee Distribution page");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}
				//search function for FeeDistribution .
				bStatus = TempFunctions.verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument("Legal Entity Name", test.getValue().get("Legal Entity Name"), test.getValue().get("Status"), "Fee Distribution Id", test.getValue().get("FeeDistributionID"), "FeeDist", time);				
				
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Fee Distribution  should Not be Active in Search Grid.",test.getValue().get("FeeDistributionID")+" :Fee Distribution is Not InActive in Search Grid.");
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Fee Distribution should be Inactive in Search Grid.",test.getValue().get("FeeDistributionID")+" : Fee Distribution is  InActive in Search Grid.");
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "VerifyFee Distribution should not be Active in Search Grid.",test.getValue().get("FeeDistributionID")+" : Fee Distributionis Not-Active in Search Grid.");
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Fee Distribution should be Active in Search Grid.",test.getValue().get("FeeDistributionID")+" : Fee Distribution is Active in Search Grid.");
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
