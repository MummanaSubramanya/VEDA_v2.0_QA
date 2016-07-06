package com.viteos.veda.master.feedistribution;

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
import com.viteos.veda.master.lib.FeeDistributionAppFunction;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class FeeDistributionMaster_ModifyMakerOperations_TC3 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Fee Distribution Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		XMLLibrary.sFeeDistributionXMLFilePath = "XMLMessages//FeeDistribution02-10-2016234219.xml";

		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void modifyMakerOperations(){
		try{
			Map<String, Map<String, String>> mapAllFeeDistributionDetails = Utilities.readMultipleTestData(Global.sFeeDistributionTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllFeeDistributionDetails.size();index++){
				Map<String, String> mapModifyFeeDistributionDetails = mapAllFeeDistributionDetails.get("Row"+index);

				if(mapModifyFeeDistributionDetails.get("Operation") == null || mapModifyFeeDistributionDetails.get("Operation").equalsIgnoreCase("Activate") || mapModifyFeeDistributionDetails.get("Operation").equalsIgnoreCase("Deactivate")){
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

				//navigate to Fee Distribution Module
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Fee Distribution");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Fee Distribution Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Fee Distribution Under Fund Setup", "Fee Distribution selected successfully");

				Map<String, String> mapDetialsToModify = new HashMap<String, String>();
				mapDetialsToModify.putAll(createdFeeDistributionDetails);
				//get operation type to be done to modify
				if(mapModifyFeeDistributionDetails.get("Operation").equalsIgnoreCase("Modify")){					
					// Putting all the non-modifiable values to null.

					mapDetialsToModify.putAll(mapModifyFeeDistributionDetails);

					//do modify for the approved Fee Distribution 
					if(createdFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){						

						bStatus = TempFunctions.modifyFeeTypeDetailsScreen("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"), "Fee Distribution Id", mapDetialsToModify.get("FeeDistributionID"), "FeeDist", mapModifyFeeDistributionDetails);	

						if(!bStatus && mapModifyFeeDistributionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Fee Distribution  Data", "Fee Distribution  cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}

						if(!bStatus && mapModifyFeeDistributionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Fee Distribution  Data", "Negative testdata - Modification of Fee Distribution  Failed");
							continue;
						}

						if(bStatus && mapModifyFeeDistributionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify The Fee Distribution  Data", "Fee Distribution modified with the Tesdata: "+mapModifyFeeDistributionDetails);
							continue;
						}

						if(bStatus && mapModifyFeeDistributionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify The Fee Distribution  Data", "Modification of Fee Distribution  got created with negative testdata");
							continue;
						}												
					}

					//do modify for Returned Fee Distribution
					if(createdFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						//select the dash board.
						NewUICommonFunctions.selectMenu("Dashboard", "None");

						bStatus = FeeDistributionAppFunction.modifyReturnFeeDistributionDetails(mapDetialsToModify);


						if(!bStatus && mapModifyFeeDistributionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Returned Fee Distribution  should get saved.", "Returned Fee Distribution  didn't get saved with test data. Error: "+Messages.errorMsg +" ] "+ mapModifyFeeDistributionDetails);
							continue;
						}

						if(!bStatus && mapModifyFeeDistributionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Returned Fee Distribution  Data should not get saved", "Modification of Returned Fee Distribution  did not saved with negative test data : "+mapModifyFeeDistributionDetails);
							continue;
						}

						if(bStatus && mapModifyFeeDistributionDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify Returned Fee Distribution  should get saved.", "Returned Fee Distribution  modified with the Tesdata: "+mapModifyFeeDistributionDetails);
							continue;
						}

						if(bStatus && mapModifyFeeDistributionDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify Returned Fee Distribution  should not get saved with Negative Data.", "Modification of Returned Fee Distribution  got saved with negative testdata"+mapModifyFeeDistributionDetails);
							continue;
						}			
					}
				}

				//do validate the data when clicked on modify the data
				if(mapModifyFeeDistributionDetails.get("Operation").equalsIgnoreCase("Validate")){
					bStatus = TempFunctions.verifyFeeTypeDetailsScreen("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"), "Fee Distribution Id", mapDetialsToModify.get("FeeDistributionID"), "FeeDist", createdFeeDistributionDetails);
					if(!bStatus){
						Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
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
		Reporting.Testcasename = "Close app";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
