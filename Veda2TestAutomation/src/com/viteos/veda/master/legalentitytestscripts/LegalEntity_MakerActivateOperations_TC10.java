package com.viteos.veda.master.legalentitytestscripts;

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

public class LegalEntity_MakerActivateOperations_TC10 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Legal Entity Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

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
			Map<String, Map<String, String>> mapAllModifyLegalEntityDetails = Utilities.readMultipleTestData(Global.sModifyLegalEntityTestDataFilePath, sSheetName,"Y");

			for(int index = 1;index <= mapAllModifyLegalEntityDetails.size();index++){

				Map<String, String> mapLeagalEntitySearchDetails = mapAllModifyLegalEntityDetails.get("Row"+index);				
						
				if (mapLeagalEntitySearchDetails.get("Operation") == null || !mapLeagalEntitySearchDetails.get("Operation").equalsIgnoreCase("Activate")) {
					continue;
				}				
				Reporting.Testcasename = mapLeagalEntitySearchDetails.get("TestcaseNameRef");
				//select Legal Entity From Fund Setup.
				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Legal Entity");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Legal Entity Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Legal Entity Mater Under Fund Setup", "Legal Entity Menu selected succesfully");
				//Read test data based on Activate operation with Reference To "TCID".				
				boolean bNameStatus = false;
				Map<String, String> mapActualLEDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "LegalEntityDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedLEDetailsTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyLegalEntityTestDataFilePath, "ModifyLEDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
				Map<String, String> mapModifiedMainTabData = ExcelUtils.readDataABasedOnCellName(Global.sModifyLegalEntityTestDataFilePath, "ModifyTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
				if(mapLeagalEntitySearchDetails.get("Operation").equalsIgnoreCase("Activate")){
					if (mapModifiedLEDetailsTabData.get("Legal Entity Name") != null && mapModifiedMainTabData.get("Operation").equalsIgnoreCase("Modify")) {
						if (mapModifiedLEDetailsTabData.containsKey("Legal Entity Name")) {
							bNameStatus = true;
							//set LE Name from modified LE details if respective LE name modified.
							mapLeagalEntitySearchDetails.put("Legal Entity Name", mapModifiedLEDetailsTabData.get("Legal Entity Name"));							
						}
					}
					if(!bNameStatus)  {
						//set LE Name from Actual LE details if respective LE name NOT-Modified.
						mapLeagalEntitySearchDetails.put("Legal Entity Name", mapActualLEDetailsTabData.get("Legal Entity Name"));						
					}
					// LE Activate Request by MAKER.
					bStatus = NewUICommonFunctions.activateMaster("Legal Entity Name", mapLeagalEntitySearchDetails.get("Legal Entity Name"), "LegalEntity");
					if(!bStatus){
						Reporting.logResults("Fail","Maker : Activating Legal Entity", "Maker : Legal Entity : "+mapLeagalEntitySearchDetails.get("Legal Entity Name")+" has not been Activated");
						continue;
					}
					Reporting.logResults("Pass","Maker : Validate Legal Entity has been Activated", "Maker : Legal Entity : "+mapLeagalEntitySearchDetails.get("Legal Entity Name")+" Has been Activated");
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
