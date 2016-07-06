package com.viteos.veda.master.feedistribution;

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
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class FeedistributionMaster_MakerActivateOperations_TS7 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality = "Fee Distribution Master";
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
	public static void testSearchFunctions(){

		Map<String, Map<String, String>> mapAllFeeDistributionDetails = Utilities.readMultipleTestData(Global.sFeeDistributionTestDataFilePath,sSheetName,"Y");

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

			//navigate to Fee Distribution Master
			bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Fee Distribution");

			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Fee Distribution Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Fee Distribution Under Fund Setup", "Fee Distribution selected successfully");

			if(createdFeeDistributionDetails.get("Operation").equalsIgnoreCase("Activate")){
				if(createdFeeDistributionDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){

					bStatus = TempFunctions.activateFeeTye("Legal Entity Name", createdFeeDistributionDetails.get("Legal Entity Name"), "Fee Distribution Id", createdFeeDistributionDetails.get("FeeDistributionID"), "FeeDist");

					if(!bStatus){
						Reporting.logResults("Fail","Maker : Validate Fee Distribution has been requested for Activation.", " Maker : failed to request for Fee Distribution  Activation : "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Maker : Validate Fee Distribution  has been requested for Activation.", " Maker : successfully raised request to Activate Fee Distribution  with ID : "+createdFeeDistributionDetails.get("FeeDistributionID"));
					continue;
				}
			}
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
