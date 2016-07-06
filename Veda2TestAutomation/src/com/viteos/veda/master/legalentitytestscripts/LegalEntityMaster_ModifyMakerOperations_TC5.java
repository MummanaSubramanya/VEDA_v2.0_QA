package com.viteos.veda.master.legalentitytestscripts;

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
import com.viteos.veda.master.lib.LegalEntityAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;

public class LegalEntityMaster_ModifyMakerOperations_TC5 {
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
			Map<String, Map<String, String>> mapAllLegalEntityDetails = Utilities.readMultipleTestData(Global.sModifyLegalEntityTestDataFilePath, sSheetName,"Y");

			for(int index = 1;index <= mapAllLegalEntityDetails.size();index++){

				Map<String, Map<String, String>> objLEModifyTabsMaps = new HashMap<String, Map<String,String>>();
				Map<String, Map<String, String>> objLECreationTabsMaps = new HashMap<String, Map<String,String>>();

				Map<String, String> mapLeagalEntitySearchDetails = mapAllLegalEntityDetails.get("Row"+index);
				Map<String, String> LegalEntityDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "LegalEntityDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));

				if(mapLeagalEntitySearchDetails.get("Operation").equalsIgnoreCase("Activate") || mapLeagalEntitySearchDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					continue;
				}

				//Map<String , String> mapModifyLegalEntityDetails = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath,"ModifyLEDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
				/*				if(mapModifyLegalEntityDetails.get("Legal Entity Name")==null){

				}*/
				mapLeagalEntitySearchDetails.put("Search Legal Entity Name", LegalEntityDetails.get("Legal Entity Name"));
				Reporting.Testcasename = mapLeagalEntitySearchDetails.get("TestcaseNameRef");
				if(mapLeagalEntitySearchDetails.get("Operation").equalsIgnoreCase("Modify"))
				{
					Map<String , String> mapModifyLegalEntityDetails = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath,"ModifyLEDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifyGeneralDetails = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifyGeneralDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifySubscriptionDetials = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifySubscriptionTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifyOtherDetials = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifyOtherDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifyRedemption = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifyRedemptionTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifyTransfer = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifyTransferTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifySwitch = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifySwitchTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifyExchange = Utilities.readTestData(Global.sModifyLegalEntityTestDataFilePath, "ModifyExchangeTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));


					objLEModifyTabsMaps.put("LegalEntityDetails", mapModifyLegalEntityDetails);
					objLEModifyTabsMaps.put("GeneralDetails", mapModifyGeneralDetails);
					objLEModifyTabsMaps.put("SubscriptionDetails", mapModifySubscriptionDetials);
					objLEModifyTabsMaps.put("OtherDetails", mapModifyOtherDetials);
					objLEModifyTabsMaps.put("RedemptionDetails", mapModifyRedemption);
					objLEModifyTabsMaps.put("TransferDetails", mapModifyTransfer);
					objLEModifyTabsMaps.put("SwitchDetails", mapModifySwitch);
					objLEModifyTabsMaps.put("ExchangeDetails", mapModifyExchange);
				}
				if(mapLeagalEntitySearchDetails.get("Operation").equalsIgnoreCase("Validate"))
				{


					Map<String , String> mapLegalEntityDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath,"LegalEntityDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "GeneralDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapSubscriptionDetials = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "SubscriptionTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapOtherDetials = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "OtherDetailsTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapRedemption = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "RedemptionTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapTransfer = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "TransferTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapSwitch = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "SwitchTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapExchange = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "ExchangeTestData", mapLeagalEntitySearchDetails.get("TestcaseNameRef"));


					if(mapLegalEntityDetails.get("Clone").equalsIgnoreCase("Yes"))
					{
						Map<String , String> mapClonedLegalEntityDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "LegalEntityDetailsTestData", mapLegalEntityDetails.get("Legal Entity Name for Clone"));
						Map<String ,String> NEwlemap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedLegalEntityDetails,mapLegalEntityDetails);


						Map<String , String> mapClonedGenralDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "GeneralDetailsTestData", mapClonedLegalEntityDetails.get("TestcaseName"));
						Map<String ,String> NEwGDmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedGenralDetailsDetails,mapGeneralDetails);

						Map<String , String> mapClonedSubscriptionDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "SubscriptionTestData", mapClonedLegalEntityDetails.get("TestcaseName"));
						Map<String ,String> NEwSubscriptionmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedSubscriptionDetailsDetails,mapSubscriptionDetials);

						Map<String , String> mapClonedOtherDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "OtherDetailsTestData", mapClonedLegalEntityDetails.get("TestcaseName"));
						Map<String ,String> NEwOtherDetailsmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedOtherDetailsDetails,mapOtherDetials);



						Map<String , String> mapClonedRedemptionDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "RedemptionTestData", mapClonedLegalEntityDetails.get("TestcaseName"));
						Map<String ,String> NEwRedemptionmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedRedemptionDetailsDetails,mapRedemption);


						Map<String , String> mapClonedTransferDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "TransferTestData", mapClonedLegalEntityDetails.get("TestcaseName"));
						Map<String ,String> NEwTransfermap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedTransferDetailsDetails,mapTransfer);


						Map<String , String> mapClonedSwitchDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "SwitchTestData", mapClonedLegalEntityDetails.get("TestcaseName"));
						Map<String ,String> NEwmapSwitchmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedSwitchDetailsDetails,mapSwitch);


						Map<String , String> mapClonedExchangeDetails = ExcelUtils.readDataABasedOnCellName(Global.sLegalEntityTestDataFilePath, "ExchangeTestData", mapClonedLegalEntityDetails.get("TestcaseName"));
						Map<String ,String> NEwmapExchangemap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedExchangeDetails,mapExchange);

						objLECreationTabsMaps.put("LegalEntityDetails", NEwlemap);											
						objLECreationTabsMaps.put("GeneralDetails", NEwGDmap);
						objLECreationTabsMaps.put("SubscriptionDetails", NEwSubscriptionmap);
						objLECreationTabsMaps.put("OtherDetails", NEwOtherDetailsmap);
						objLECreationTabsMaps.put("RedemptionDetails", NEwRedemptionmap);
						objLECreationTabsMaps.put("TransferDetails", NEwTransfermap);
						objLECreationTabsMaps.put("SwitchDetails", NEwmapSwitchmap);
						objLECreationTabsMaps.put("ExchangeDetails", NEwmapExchangemap);





						//objLECreationTabsMaps.put("LegalEntityDetails", mapClonedLegalEntityDetails);

					}
					else{
						objLECreationTabsMaps.put("LegalEntityDetails", mapLegalEntityDetails);											
						objLECreationTabsMaps.put("GeneralDetails", mapGeneralDetails);
						objLECreationTabsMaps.put("SubscriptionDetails", mapSubscriptionDetials);
						objLECreationTabsMaps.put("OtherDetails", mapOtherDetials);
						objLECreationTabsMaps.put("RedemptionDetails", mapRedemption);
						objLECreationTabsMaps.put("TransferDetails", mapTransfer);
						objLECreationTabsMaps.put("SwitchDetails", mapSwitch);
						objLECreationTabsMaps.put("ExchangeDetails", mapExchange);
					}

				}

				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Legal Entity");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Legal Entity Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Legal Entity Mater Under Fund Setup", "Legal Entity Menu selected succesfully");

				if(mapLeagalEntitySearchDetails.get("Operation").equalsIgnoreCase("Modify")){
					if(LegalEntityDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						bStatus = NewUICommonFunctions.modifyMasterDetails("Legal Entity Name",mapLeagalEntitySearchDetails.get("Search Legal Entity Name") ,"LegalEntity", objLEModifyTabsMaps);/*(objLEModifyTabsMaps, mapLeagalEntitySearchDetails.get("Search Legal Entity Name"));*/
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Legal Entity Data",Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Legal Entity Data", "Legal Entity modified with the Tesdata: "+mapLeagalEntitySearchDetails);
					}

					if(LegalEntityDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						NewUICommonFunctions.selectMenu("Dashboard", "none");
						bStatus = LegalEntityAppFunctions.modifyReturnLegalEntityDetails(objLEModifyTabsMaps, mapLeagalEntitySearchDetails.get("Search Legal Entity Name"));
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Legal Entity Data", Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Legal Entity Data", "Legal Entity modified with the Tesdata: "+mapLeagalEntitySearchDetails);
					}
				}

				if(mapLeagalEntitySearchDetails.get("Operation").equalsIgnoreCase("Validate")){
					if(LegalEntityDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						
						bStatus = NewUICommonFunctions.verifyMasterDetails("Legal Entity Name", mapLeagalEntitySearchDetails.get("Search Legal Entity Name"), "LegalEntity", objLECreationTabsMaps);
						//bStatus = LegalEntityAppFunctions.verifyDataInLegalEntityScreen(objLECreationTabsMaps);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Legal Entity Details in the modify screen", "Validating The Details Of Actual With Expected Is FAILED And The results are NOT matching with EXPECTED. ");
							continue;
						}
						Reporting.logResults("Pass","Validate Legal Entity Details in the modify screen", "Validating The Details Of Actual With Expected Is SUCCESSFULL And The results matching with EXPECTED.");
					}
					if(LegalEntityDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						NewUICommonFunctions.selectMenu("Dashboard", "none");
						bStatus = LegalEntityAppFunctions.verifyReturnLegalEntityDetails(objLECreationTabsMaps, objLECreationTabsMaps.get("LegalEntityDetails").get("Legal Entity Name"));
						if(!bStatus){
							Reporting.logResults("Fail","Validate Legal Entity Details in the modify screen", "Validating The Details Of Actual With Expected Is FAILED And The results are NOT matching with EXPECTED. ");
							continue;
						}
						Reporting.logResults("Pass","Validate Legal Entity Details in the modify screen", "Validating The Details Of Actual With Expected Is SUCCESSFULL And The results matching with EXPECTED.");
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
