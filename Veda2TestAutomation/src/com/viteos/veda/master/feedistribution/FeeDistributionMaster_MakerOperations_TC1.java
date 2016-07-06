package com.viteos.veda.master.feedistribution;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.FeeDistributionAppFunction;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;
import com.viteos.veda.master.lib.XMLLibrary;

public class FeeDistributionMaster_MakerOperations_TC1 {

	static boolean bStatus;
	static String sSheetName = "FeeDistributionTestData";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Fee Distribution Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;


		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@org.testng.annotations.Test
	public static void test(){
		try{
			String feeDistributionId = "";
			NewUICommonFunctions.DeleteFileIfExists(XMLLibrary.sFeeDistributionXMLFilePath);
			Map<String, Map<String, String>> mapFeeDistributionALLDetails = Utilities.readMultipleTestData(Global.sFeeDistributionTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapFeeDistributionALLDetails.size();index++){

				Map<String, String> mapFeeDistribution = mapFeeDistributionALLDetails.get("Row"+index);

				Reporting.Testcasename = mapFeeDistribution.get("TestcaseName");

				//Navigate to Fee Distribution
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Fee Distribution");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Fee Distribution Master", "Fee Distribution Menu cannot be selected Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Fee Distribution Master", "Fee Distribution Menu selected succesfully");

				//Click on Add Button
				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					Reporting.logResults("Fail", "Click on 'Add New' button for Fee Distribution creation", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Click on 'Add New' button for Fee Distribution creation", "Add New Button clicked succesfully");

				// Add New Fee Distribution Details
				
				bStatus = FeeDistributionAppFunction.addFeeDistribution(mapFeeDistribution);
				
				if(mapFeeDistribution .get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus){
					
					//Export to xml file to save the ID
					if(mapFeeDistribution .get("OperationType").equalsIgnoreCase("Save")){

						feeDistributionId = NewUICommonFunctions.getIDFromSuccessMessage();
						mapFeeDistribution .put("FeeDistributionID", feeDistributionId);
						XMLLibrary.writeFeeDistributionToXML(mapFeeDistribution);
					}
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapFeeDistribution .get("OperationType"), "Performed maker Operation successfull for the scenario with test data " + mapFeeDistribution );
				}

				if(mapFeeDistribution .get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){
					Reporting.logResults("Fail", "Perform Maker Operation: "+mapFeeDistribution .get("OperationType"), "Cannot perform maker Operations. Failed for the scenario with test data "     + mapFeeDistribution  +  " ERROR : "   + Messages.errorMsg);
					continue;
				}

				if(mapFeeDistribution .get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapFeeDistribution .get("OperationType"), "Negative Testcase - Cannot perform maker operations for the scenario with test data " + mapFeeDistribution  + " ERROR : "   + Messages.errorMsg);
					continue;
				}

				if(mapFeeDistribution .get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){
					Reporting.logResults("Fail", "Perform Maker Operation: "+mapFeeDistribution .get("OperationType"), "performed maker operations for the scenario with Negative test data " + mapFeeDistribution );
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

		Reporting.Testcasename = "Close Application";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error ");
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
