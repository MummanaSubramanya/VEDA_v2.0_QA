package com.viteos.veda.master.managementfeetestscripts;

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
import com.viteos.veda.master.lib.ManagementFeeAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;
import com.viteos.veda.master.lib.XMLLibrary;

public class ManagementFeeMaster_MakerOperations_TS1 {
	static boolean bStatus;
	static String sSheetName = "ManagementFeeDetails";

	@BeforeMethod
	public static void setUp(){

		Reporting.Functionality ="Management Fee Master";
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
			String sManagementFeeID = "";
			Map<String, Map<String, String>> mapAllManagementFeeDetails = Utilities.readMultipleTestData(Global.sManagementFeeTestDataFilePath,sSheetName,"Y");
			for(int index = 1;index <= mapAllManagementFeeDetails.size();index++){			

				Map<String, String> managementFeeDetailsMap = mapAllManagementFeeDetails.get("Row"+index);

				Reporting.Testcasename = managementFeeDetailsMap.get("TestcaseName");
				
				NewUICommonFunctions.refreshThePage();
				
				//navigate to management fee
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup", "Management Fee");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Management Fee Master Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Management Fee Master Under Fund Setup", "Management Fee Menu selected succesfully");
				
				//click on add new
				bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);	
				if(!bStatus){
					Reporting.logResults("Fail", "Click on Add New button to add new Management Fee", "Cannot click on 'Add New' Button. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Click on Add New button to add new Fund Management Fee", "Add New Button clicked succesfully");

				bStatus = ManagementFeeAppFunctions.createNewManagementFee(managementFeeDetailsMap);

				if(managementFeeDetailsMap.get("ExpectedResults").equalsIgnoreCase("Pass") && bStatus){
					//export to xml file to save the ID
					if (managementFeeDetailsMap.get("OperationType") != null && managementFeeDetailsMap.get("OperationType").equalsIgnoreCase("Save")) 
					{
						sManagementFeeID = NewUICommonFunctions.getIDFromSuccessMessage();
						if (sManagementFeeID == null) {
							Reporting.logResults("Fail", "Get transaction ID from success Message of Management Fee creation.", "Transaction ID Wasn't Displayed in the Success Message.");
							continue;
						}
						//adding testcase name and id to xml library
						Map<String, String> managementFeeDetailsXMLMap =  new HashMap<String, String>();
						managementFeeDetailsXMLMap.put("TestcaseName", managementFeeDetailsMap.get("TestcaseName"));
						managementFeeDetailsXMLMap.put("ManagementFeeID", sManagementFeeID);
						managementFeeDetailsXMLMap.put("MakerStatus", "Pass");
						managementFeeDetailsXMLMap.put("CheckerStatus", "None");
						Map<String, String> mapXMLStoredManagementFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sManagementXMLFilePath, "ManagementFee", Reporting.Testcasename);
						if(mapXMLStoredManagementFeeDetails != null){
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "ManagementFeeID", sManagementFeeID, "TestcaseName", Reporting.Testcasename, "ManagementFee");
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "MakerStatus" , "Pass" , "TestcaseName", Reporting.Testcasename, "ManagementFee");
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "CheckerStatus", "None", "TestcaseName", Reporting.Testcasename, "ManagementFee");
						}else{
							XMLLibrary.writeManagementDetailsToXML(managementFeeDetailsXMLMap);
						}
						
						Reporting.logResults("Pass", "Perform Maker Operation: "+managementFeeDetailsMap.get("OperationType"), "Performed maker Operation "+managementFeeDetailsMap.get("OperationType")+" successfully. New Management Fee Created.");
					}
					//log for cancel operation
					if(managementFeeDetailsMap.get("OperationType").equalsIgnoreCase("Cancel")){
						Reporting.logResults("Pass", "Perform Maker Operation: "+managementFeeDetailsMap.get("OperationType"), "Performed maker Operation "+managementFeeDetailsMap.get("OperationType")+" Successfully");
					}
				}

				if(managementFeeDetailsMap.get("ExpectedResults").equalsIgnoreCase("Pass") && !bStatus){
					Map<String, String> mapXMLStoredManagementFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sManagementXMLFilePath, "ManagementFee", managementFeeDetailsMap.get("TestcaseName"));
					if(mapXMLStoredManagementFeeDetails != null){
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "MakerStatus" , "Fail" , "TestcaseName", Reporting.Testcasename, "ManagementFee");
						XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "CheckerStatus", "None", "TestcaseName", Reporting.Testcasename, "ManagementFee");
					}
					Reporting.logResults("Fail", "Perform Maker Operation: "+managementFeeDetailsMap.get("OperationType"), "Cannot perform maker Operations. "+Messages.errorMsg);
					continue;
				}

				if(managementFeeDetailsMap.get("ExpectedResults").equalsIgnoreCase("Fail") && !bStatus){
					Reporting.logResults("Pass", "Perform Maker Operation: "+managementFeeDetailsMap.get("OperationType"), "Negative Testcase - Cannot perform maker operations. "+Messages.errorMsg);
				}

				if(managementFeeDetailsMap.get("ExpectedResults").equalsIgnoreCase("Fail") && bStatus){
					Reporting.logResults("Fail", "Perform Maker Operation: "+managementFeeDetailsMap.get("OperationType"), "performed maker operations  with Negative test data ");
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
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}

}
