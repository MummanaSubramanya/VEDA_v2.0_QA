package com.viteos.veda.master.managementfeetestscripts;

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
import com.viteos.veda.master.lib.ManagementFeeAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.XMLLibrary;

public class ManagementFeeMaster_ModifyMakerOperations_TS3 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";


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
	public static void testSearchFunctions(){
		try{
			Map<String, Map<String, String>> mapAllManagementFeeDetails = Utilities.readMultipleTestData(Global.sManagementFeeTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllManagementFeeDetails.size();index++){
				Map<String, String> mapModifyManagementFeeDetails = mapAllManagementFeeDetails.get("Row"+index);

				if(mapModifyManagementFeeDetails.get("Operation") == null || mapModifyManagementFeeDetails.get("Operation").equalsIgnoreCase("Deactivate") || mapModifyManagementFeeDetails.get("Operation").equalsIgnoreCase("Activate")){
					continue;
				}

				Reporting.Testcasename = mapModifyManagementFeeDetails.get("TestcaseNameRef");
				//Map<String , String> formulaSetupDetails = new HashMap<String, String>();
			
				//read testdata
				Map<String, String> createdXMLManagementFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sManagementXMLFilePath, "ManagementFee",mapModifyManagementFeeDetails.get("TestcaseNameRef")) ;
				
				
				/*if(createdManagementFeeDetails.get("Management Fee On")!=null){
					 formulaSetupDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath, "FormulaTestData",createdManagementFeeDetails.get("Management Fee On"));
				}*/
				
				if (createdXMLManagementFeeDetails == null || !createdXMLManagementFeeDetails.get("CheckerStatus").equalsIgnoreCase("Pass")) {
					continue;
				}
				
				Map<String, String> createdManagementFeeDetails =  ExcelUtils.readDataABasedOnCellName(Global.sManagementFeeTestDataFilePath, "ManagementFeeDetails", mapModifyManagementFeeDetails.get("TestcaseNameRef"));
				
				mapModifyManagementFeeDetails.put("ManagementFeeID", createdXMLManagementFeeDetails.get("ManagementFeeID"));
				createdManagementFeeDetails.put("ManagementFeeID", createdXMLManagementFeeDetails.get("ManagementFeeID"));

				//navigate to Client Module
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Management Fee");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to ManagementFee Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "MakerStatus" , "Fail" , "TestcaseName", Reporting.Testcasename, "ManagementFee");
					continue;
				}
				Reporting.logResults("Pass", "Navigate to ManagementFee Under Fund Setup", "ManagementFee selected succesfully");

				
				if(mapModifyManagementFeeDetails.get("Operation").equalsIgnoreCase("Modify")){					
					
					if (createdManagementFeeDetails.get("New Rule") != null && createdManagementFeeDetails.get("New Rule").equalsIgnoreCase("Yes")) {
						if (createdManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && createdManagementFeeDetails.get("CheckerOperationsOnFormula").equalsIgnoreCase("Approve")) 
						{
							bStatus = TempFunctions.modifyFeeTypeDetailsScreen("Legal Entity Name",createdManagementFeeDetails.get("Legal Entity Name"), "Management Fee Id",mapModifyManagementFeeDetails.get("ManagementFeeID"), "ManagementFee", mapModifyManagementFeeDetails);
						}
						if (createdManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Return") && createdManagementFeeDetails.get("CheckerOperationsOnFormula").equalsIgnoreCase("Approve"))
						{
							bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
							if(!bStatus){
								Reporting.logResults("Fail", "Navigate to Dashboard ", "Menu cannot be selected. Error: "+Messages.errorMsg);
								continue;
							}
							bStatus = ManagementFeeAppFunctions.modifyReturnManagementFeeDetails(mapModifyManagementFeeDetails);
						}

						if(!bStatus && mapModifyManagementFeeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The ManagementFee Data", "ManagementFee cannot be modified. Error: "+Messages.errorMsg);
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "MakerStatus" , "Fail" , "TestcaseName", Reporting.Testcasename, "ManagementFee");
							continue;
						}

						if(!bStatus && mapModifyManagementFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The ManagementFee Data", "Modification of ManagementFee Failed. "+Messages.errorMsg);
							continue;
						}

						if(bStatus && mapModifyManagementFeeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify The ManagementFee Data", "ManagementFee modified Successfully ");
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "MakerStatus" , "Pass" , "TestcaseName", Reporting.Testcasename, "ManagementFee");
							continue;
						}

						if(bStatus && mapModifyManagementFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify The ManagementFee Data", "Modification of ManagementFee got created with negative testdata");
							continue;
						}				
					}
					
					if (createdManagementFeeDetails.get("Management Fee On") != null) {
						if (createdManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
							
							bStatus = TempFunctions.modifyFeeTypeDetailsScreen("Legal Entity Name",createdManagementFeeDetails.get("Legal Entity Name"), "Management Fee Id",mapModifyManagementFeeDetails.get("ManagementFeeID"), "ManagementFee", mapModifyManagementFeeDetails);
						}
						if (createdManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("return")) {
							bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
							if(!bStatus){
								Reporting.logResults("Fail", "Navigate to Dashboard ", "Menu cannot be selected. Error: "+Messages.errorMsg);
								continue;
							}							
							bStatus = ManagementFeeAppFunctions.modifyReturnManagementFeeDetails(mapModifyManagementFeeDetails);
						}
						if(!bStatus && mapModifyManagementFeeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Fail", "Modify The Returned ManagementFee Data", " ManagementFee cannot be modified. Error: "+Messages.errorMsg);
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "MakerStatus" , "Fail" , "TestcaseName", Reporting.Testcasename, "ManagementFee");
							continue;
						}

						if(!bStatus && mapModifyManagementFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Pass", "Modify The Returned ManagementFee Data", "Modification of  ManagementFee Failed. "+Messages.errorMsg);
							continue;
						}

						if(bStatus && mapModifyManagementFeeDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
							Reporting.logResults("Pass", "Modify The Returned ManagementFee Data", "Management Fee modified in Maker Successfully ");
							XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "MakerStatus" , "Pass" , "TestcaseName", Reporting.Testcasename, "ManagementFee");
							continue;
						}

						if(bStatus && mapModifyManagementFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
							Reporting.logResults("Fail", "Modify The Returned ManagementFee Data", "ManagementFee got created with negative testdata");
							continue;
						}			
					}
				}
									

				//do validate the data when clicked n modify the data
				if(mapModifyManagementFeeDetails.get("Operation").equalsIgnoreCase("Validate")){
					if (createdManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						
						bStatus = TempFunctions.verifyFeeTypeDetailsScreen("Legal Entity Name",createdManagementFeeDetails.get("Legal Entity Name"), "Management Fee Id",mapModifyManagementFeeDetails.get("ManagementFeeID"), "ManagementFee", createdManagementFeeDetails);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
						continue;
					}
					if (createdManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("return")){
						bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
						if(!bStatus){
							Reporting.logResults("Fail", "Navigate to Dashboard ", "Menu cannot be selected. Error: "+Messages.errorMsg);
							continue;
						}	
						bStatus = ManagementFeeAppFunctions.verifyReturnedManagementFeeDetails(createdManagementFeeDetails);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Details in the modify screen", "Details Validation failed in the Edit screen. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Details in the modify screen", "Validated the details Actual Details equal to Expected");
						continue;
					}
					
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
