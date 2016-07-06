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

public class FeeDistribution_CheckerActivateOperations_TC8 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality = "Fee Distribution Master";
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
	public static void testSearchFunctions(){
		
		Map<String, Map<String, String>> mapAllFeeDistributionDetails = Utilities.readMultipleTestData(Global.sFeeDistributionTestDataFilePath,sSheetName,"Y");
		Map<String, Map<String, String>> verifyFeeDistributionMap = new HashMap<String, Map<String, String>>();
		Map<String, String> innerMap = new HashMap<String, String>();
		for(int index = 1;index <= mapAllFeeDistributionDetails.size();index++){
			Map<String, String> mapModifyFeeDistributionDetails = mapAllFeeDistributionDetails.get("Row"+index);

			if(mapModifyFeeDistributionDetails.get("Operation") == null || !mapModifyFeeDistributionDetails.get("Operation").equalsIgnoreCase("Activate")){
				continue;
			}

			Reporting.Testcasename = mapModifyFeeDistributionDetails.get("TestcaseNameRef");


			//read testdata
			Map<String, String> createdXMLFeeDistributionFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sFeeDistributionXMLFilePath, "FeeDistribution",mapModifyFeeDistributionDetails.get("TestcaseNameRef")) ;
			Map<String, String> createdFeeDistributionDetails =  ExcelUtils.readDataABasedOnCellName(Global.sFeeDistributionTestDataFilePath, "FeeDistributionTestData", mapModifyFeeDistributionDetails.get("TestcaseNameRef"));
			if (createdXMLFeeDistributionFeeDetails == null || createdXMLFeeDistributionFeeDetails.get("FeeDistributionID") == null || createdXMLFeeDistributionFeeDetails.get("FeeDistributionID").equalsIgnoreCase("")) {
				continue;
			}

			

			mapModifyFeeDistributionDetails.put("FeeDistributionID", createdXMLFeeDistributionFeeDetails.get("FeeDistributionID"));
			createdFeeDistributionDetails.put("FeeDistributionID", createdXMLFeeDistributionFeeDetails.get("FeeDistributionID"));
			createdFeeDistributionDetails.putAll(mapModifyFeeDistributionDetails);
			
			if (mapModifyFeeDistributionDetails.get("CheckerOperations") == null) {
				continue;
			}
			bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, createdFeeDistributionDetails.get("FeeDistributionID"), createdFeeDistributionDetails.get("CheckerOperations"));

			if(mapModifyFeeDistributionDetails.get("Operation").equalsIgnoreCase("Activate")){								
				if(mapModifyFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){						
					if(!bStatus){
						Reporting.logResults("Fail","Checker : Validate Fee Distribution  Activation request Approved.", " Checker : Fee Distribution activation request cannot be Approved : "+Messages.errorMsg);
						continue;
					}	
					innerMap.put("Status", "active");
					innerMap.put("FeeDistributionID", createdFeeDistributionDetails.get("FeeDistributionID"));
					innerMap.put("Legal Entity Name", createdFeeDistributionDetails.get("Legal Entity Name"));
					verifyFeeDistributionMap.put(Reporting.Testcasename, innerMap);
				}	
				if (mapModifyFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Reject") || mapModifyFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Return")) {
					innerMap.put("Status", "inactive");
					innerMap.put("FeeDistributionID", createdFeeDistributionDetails.get("FeeDistributionID"));
					innerMap.put("Legal Entity Name", createdFeeDistributionDetails.get("Legal Entity Name"));
					verifyFeeDistributionMap.put(Reporting.Testcasename, innerMap);					
				}
			}
		}		
		verifyValuesinSearchPanel(verifyFeeDistributionMap);
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

				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Fee Distribution");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Fee Distribution","Cannot Navigate to Fee Distribution Page");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Fee Distribution", "Navigated to Fee Distribution");

				int time =10 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}
				//search function for FeeDistribution.
				bStatus = TempFunctions.verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument("Legal Entity Name", test.getValue().get("Legal Entity Name"), test.getValue().get("Status"), "FeeDistributionID", test.getValue().get("FeeDistributionID"), "FeeDist", time);				
							
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Inactive Fee Distribution should be there in Search Grid.",test.getValue().get("FeeDistributionID")+" : Inactive Fee Distribution is not present in Search Grid.");						
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
}
