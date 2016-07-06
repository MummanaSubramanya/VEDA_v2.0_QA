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
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.IncentiveFeeAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;
import com.viteos.veda.master.lib.XMLLibrary;

public class IncentiveFeeMaster_MakerOperations_TS1 {
	static boolean bStatus;
	static String sSheetName = "IncentiveFeeDetails";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Incentive Fee Master";
		Reporting.Testcasename = "Open Application";

		bStatus = NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}
	@Test
	public static void testMaker(){
		try{
			String sIncentiveFeeID = "";
			Map<String, Map<String, String>> mapAllIncentiveFeeDetails = Utilities.readMultipleTestData(Global.sIncentiveFeeTestDataFilePath,sSheetName,"Y");
			for(int index = 1;index <= mapAllIncentiveFeeDetails.size();index++){			

				Map<String, String> incentiveFeeDetailsMap = mapAllIncentiveFeeDetails.get("Row"+index);

				Reporting.Testcasename = incentiveFeeDetailsMap.get("TestcaseName");
				Global.driver.navigate().refresh();
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Incentive Fee");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Incentive Fee Master Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Incentive Fee Master Under Fund Setup", "Incentive Fee Menu selected succesfully");

				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					Reporting.logResults("Fail", "Click on Add New button to add new Incentive Fee", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Click on Add New button to add new Fund Incentive Fee", "Add New Button clicked succesfully");

				bStatus = IncentiveFeeAppFunctions.createNewIncentiveFee(incentiveFeeDetailsMap);

				if(incentiveFeeDetailsMap.get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus){
					//export to xml file to save the ID
					if (incentiveFeeDetailsMap.get("OperationType") != null && incentiveFeeDetailsMap.get("OperationType").equalsIgnoreCase("Save")) {
						sIncentiveFeeID = NewUICommonFunctions.getIDFromSuccessMessage();
						if (sIncentiveFeeID == null) {
							Reporting.logResults("Fail", "Get transaction ID from success Message of Incentive Fee creation.", "Transaction ID Wasn't Displayed after creating Incentive Fee.");
							continue;
						}
						Map<String, String> incentiveFeeDetailsXMLMap =  new HashMap<String, String>();
						incentiveFeeDetailsXMLMap.put("IncentiveFeeID", sIncentiveFeeID);
						incentiveFeeDetailsXMLMap.put("MakerStatus", "Pass");
						incentiveFeeDetailsXMLMap.put("CheckerStatus", "None");
						incentiveFeeDetailsXMLMap.put("TestcaseName", incentiveFeeDetailsMap.get("TestcaseName"));
						Map<String, String> mapXMLIncentiveFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sIncentiveXMLFilePath, "IncentiveFee", Reporting.Testcasename);
						if(mapXMLIncentiveFeeDetails != null){
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "IncentiveFeeID", sIncentiveFeeID, "TestcaseName", Reporting.Testcasename, "IncentiveFee");
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "MakerStatus", "Pass", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "CheckerStatus", "None", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
						}else{
							XMLLibrary.writeIncentiveFeeDetailsToXML(incentiveFeeDetailsXMLMap);
						}
						
						Reporting.logResults("Pass", "Perform Maker Operation: "+incentiveFeeDetailsMap.get("OperationType"), "Performed maker Operation successfull for the scenario with test data " + incentiveFeeDetailsMap);
						continue;
					}
				}

				if(incentiveFeeDetailsMap.get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){
					Reporting.logResults("Fail", "Perform Maker Operation: "+incentiveFeeDetailsMap.get("OperationType"), "Cannot perform maker Operations. Failed for the scenario with test data " + Messages.errorMsg);
					Map<String, String> mapXMLIncentiveFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sIncentiveXMLFilePath, "IncentiveFee", Reporting.Testcasename);
					if(mapXMLIncentiveFeeDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "MakerStatus", "Fail", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sIncentiveXMLFilePath, "CheckerStatus", "None", "TestcaseName", Reporting.Testcasename, "IncentiveFee");
					}
					continue;
				}

				if(incentiveFeeDetailsMap.get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
					Reporting.logResults("Pass", "Perform Maker Operation: "+incentiveFeeDetailsMap.get("OperationType"), "Negative Testcase - Cannot perform maker operations for the scenario with test data " + incentiveFeeDetailsMap);
				}

				if(incentiveFeeDetailsMap.get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){
					Reporting.logResults("Fail", "Perform Maker Operation: "+incentiveFeeDetailsMap.get("OperationType"), "performed maker operations for the scenario with Negative test data : "+Messages.errorMsg +" ] " + incentiveFeeDetailsMap);
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
