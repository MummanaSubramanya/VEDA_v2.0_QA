package com.viteos.veda.master.investoraccountmastertestscripts;
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

public class InvestorAccountMaster_ActivateMakerOperations_TC4 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="InvestorAccount Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;
		//XMLLibrary.sInvestorAccountXMLFilePath = "XMLMessages//InvestorAccount01-23-2016011553.xml";

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
			Map<String, Map<String, String>> mapAllInvestorAccountDetails = Utilities.readMultipleTestData(Global.sInvestorAccountTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllInvestorAccountDetails.size();index++){
				Map<String, String> mapModifyInvestoraccountDetails = mapAllInvestorAccountDetails.get("Row"+index);
				//read testdata
				Map<String, String> createdXMLInvestorAccountDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sInvestorAccountXMLFilePath,"InvestorAccount", mapModifyInvestoraccountDetails.get("TestcaseNameRef")) ;
				Map<String, String> createdExcelInvestorDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sInvestorAccountTestDataFilePath,"AccountTestData", mapModifyInvestoraccountDetails.get("TestcaseNameRef"));

				if(createdXMLInvestorAccountDetails == null){
					continue ;
				}

				createdExcelInvestorDetailsDetails.putAll(createdXMLInvestorAccountDetails);


				if(mapModifyInvestoraccountDetails.get("Operation").equalsIgnoreCase("Validate") || mapModifyInvestoraccountDetails.get("Operation").equalsIgnoreCase("Modify")||mapModifyInvestoraccountDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					continue;
				}

				Reporting.Testcasename = mapModifyInvestoraccountDetails.get("TestcaseNameRef");

				//Navigate to Investor Account 

				bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Account");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to InvestorAccount  Mater Under Investor Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to InvestorAccount Mater Under Investor Setup", "Account Menu selected succesfully");


				Map<String, String> ModifiedExcelInvestorDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sInvestorAccountTestDataFilePath,sSheetName, mapModifyInvestoraccountDetails.get("TestcaseNameRef"));
				createdExcelInvestorDetailsDetails.putAll(ModifiedExcelInvestorDetailsDetails);

				if(mapModifyInvestoraccountDetails.get("Operation").equalsIgnoreCase("Activate")){

					bStatus = TempFunctions.activateFeeTye("Investor Holder", createdExcelInvestorDetailsDetails.get("Investor Holder"), "Investor Account Id", createdExcelInvestorDetailsDetails.get("AccountID"), "InvestorAccount");
					if(!bStatus){
						Reporting.logResults("Fail","Validate InvestorAccount  has been Activated", "InvestorAccount  has not be Deactivated. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Validate InvestorAccount  has been Activated", "InvestorAccount  has been Deactivated");
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


