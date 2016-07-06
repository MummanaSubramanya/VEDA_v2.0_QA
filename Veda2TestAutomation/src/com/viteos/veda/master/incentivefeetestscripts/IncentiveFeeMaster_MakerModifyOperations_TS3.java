package com.viteos.veda.master.incentivefeetestscripts;

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
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.IncentiveFeeAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class IncentiveFeeMaster_MakerModifyOperations_TS3 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){		
		Reporting.Functionality = "Incentive Fee Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;		
		//XMLLibrary.sIncentiveXMLFilePath = "XMLMessages//IncentiveFee02-11-2016213520.xml";
		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void testSearchFunctions(){
		try{
			Map<String, Map<String, String>> mapAllIncentiveFeeDetails = Utilities.readMultipleTestData(Global.sIncentiveFeeTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllIncentiveFeeDetails.size();index++){
				Map<String, String> mapModifyIncentiveFeeDetails = mapAllIncentiveFeeDetails.get("Row"+index);

				if(mapModifyIncentiveFeeDetails.get("Operation") == null || mapModifyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("Activate") || mapModifyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("Deactivate")){
					continue;
				}

				Reporting.Testcasename = mapModifyIncentiveFeeDetails.get("TestcaseNameRef");
				Map<String, String> createdIncentiveFeeDetails =  ExcelUtils.readDataABasedOnCellName(Global.sIncentiveFeeTestDataFilePath, "IncentiveFeeDetails", mapModifyIncentiveFeeDetails.get("TestcaseNameRef"));
				//read testdata
				Map<String, String> createdXMLIncentiveFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sIncentiveXMLFilePath, "IncentiveFee",Reporting.Testcasename) ;
				
				if (createdXMLIncentiveFeeDetails == null || !createdXMLIncentiveFeeDetails.get("CheckerStatus").equalsIgnoreCase("Pass")) {
					continue;
				}
				mapModifyIncentiveFeeDetails.put("IncentiveFeeID", createdXMLIncentiveFeeDetails.get("IncentiveFeeID"));
				createdIncentiveFeeDetails.put("IncentiveFeeID", createdXMLIncentiveFeeDetails.get("IncentiveFeeID"));

				//navigate to Client Module
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Incentive Fee");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to IncentiveFee Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "MakerStatus", "Fail", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
					continue;
				}
				Reporting.logResults("Pass", "Navigate to IncentiveFee Under Fund Setup", "IncentiveFee selected succesfully");

				Map<String, String> mapDetialsToModify = new HashMap<String, String>();

				//get operation type to be done to modify
				if(mapModifyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("Modify")){					
					// Putting all the non-modifiable values to null.

					mapDetialsToModify.putAll(createdIncentiveFeeDetails);
					mapDetialsToModify.putAll(mapModifyIncentiveFeeDetails);
					
					//do modify for the approved formula and Fee
					if(createdIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){						
						bStatus = TempFunctions.modifyFeeTypeDetailsScreen("Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"), "Incentive Fee Id", mapDetialsToModify.get("IncentiveFeeID"), "IncentiveFee", mapDetialsToModify);
								//IncentiveFeeAppFunctions.modifyIncentiveFeeDetails(mapDetialsToModify);
						
						if(!bStatus && mapModifyIncentiveFeeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The IncentiveFee Data", "IncentiveFee cannot be modified. Error: "+Messages.errorMsg);
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "MakerStatus", "Fail", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
							continue;
						}

						if(!bStatus && mapModifyIncentiveFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The IncentiveFee Data", "Negative testdata - Modification of IncentiveFee Failed");
							continue;
						}

						if(bStatus && mapModifyIncentiveFeeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify The IncentiveFee Data", "IncentiveFee modified with the Tesdata: "+mapModifyIncentiveFeeDetails);
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "MakerStatus", "Pass", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
							continue;
						}

						if(bStatus && mapModifyIncentiveFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify The IncentiveFee Data", "Modification of IncentiveFee got created with negative testdata");
							continue;
						}												
					}

					//do modify for the Approved Formula and returned Incentive Fee.
					if(createdIncentiveFeeDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						//select the dash board.
						NewUICommonFunctions.selectMenu("Dashboard", "None");
						
						bStatus = IncentiveFeeAppFunctions.modifyReturnIncentiveFeeDetails(mapDetialsToModify);

						if(!bStatus && mapModifyIncentiveFeeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Returned IncentiveFee should get saved.", "Returned IncentiveFee didn't get saved with test data. Error: "+Messages.errorMsg +" ] "+ mapModifyIncentiveFeeDetails);
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "MakerStatus", "Fail", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
							continue;
						}

						if(!bStatus && mapModifyIncentiveFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Returned IncentiveFee Data should not get saved", "Modification of Returned IncentiveFee did not saved with negative test data : "+mapModifyIncentiveFeeDetails);
							continue;
						}

						if(bStatus && mapModifyIncentiveFeeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify Returned IncentiveFee should get saved.", "Returned IncentiveFee modified with the Tesdata: "+mapModifyIncentiveFeeDetails);
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "MakerStatus", "Pass", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
							continue;
						}

						if(bStatus && mapModifyIncentiveFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify Returned IncentiveFee should not get saved with Negative Data.", "Modification of Returned IncentiveFee got saved with negative testdata"+mapModifyIncentiveFeeDetails);
							continue;
						}			
					}
				}

				//do validate the data when clicked on modify the data
				if(mapModifyIncentiveFeeDetails.get("Operation").equalsIgnoreCase("Validate")){
					bStatus = TempFunctions.verifyFeeTypeDetailsScreen("Legal Entity Name", createdIncentiveFeeDetails.get("Legal Entity Name"), "Incentive Fee Id", createdIncentiveFeeDetails.get("IncentiveFeeID"), "IncentiveFee", createdIncentiveFeeDetails);
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
