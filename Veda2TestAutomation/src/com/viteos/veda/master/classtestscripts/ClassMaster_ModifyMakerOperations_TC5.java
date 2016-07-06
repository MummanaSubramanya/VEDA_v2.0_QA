package com.viteos.veda.master.classtestscripts;

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
import com.viteos.veda.master.lib.ClassAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;

public class ClassMaster_ModifyMakerOperations_TC5 {
	static boolean bStatus;
	static String sSheetName = "ModifyTestData";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Class Master";
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
			Map<String, Map<String, String>> mapAllClassDetails = Utilities.readMultipleTestData(Global.sModifyClassTestDataFilePath, sSheetName,"Y");

			for(int index = 1;index <= mapAllClassDetails.size();index++){

				Map<String, Map<String, String>> objClassModifyTabsMaps = new HashMap<String, Map<String,String>>();
				Map<String, Map<String, String>> objClassCreationTabsMaps = new HashMap<String, Map<String,String>>();

				Map<String, String> mapClassSearchDetails = mapAllClassDetails.get("Row"+index);

				if(mapClassSearchDetails.get("Operation").equalsIgnoreCase("Activate") || mapClassSearchDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					continue;
				}

				Map<String, String> ClassDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassDetailsTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
				mapClassSearchDetails.put("Search Class Name", ClassDetails.get("Class Name"));
				Reporting.Testcasename = mapClassSearchDetails.get("TestcaseNameRef");
				if(mapClassSearchDetails.get("Operation").equalsIgnoreCase("Modify"))
				{
					Map<String , String> mapModifyClassDetails = Utilities.readTestData(Global.sModifyClassTestDataFilePath,"ModifyClassDetails", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifyGeneralDetails = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassGeneralDetails", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifySubscriptionDetials = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassSubscription", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifyOtherDetials = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassOtherDetails", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifyRedemption = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassRedemption", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifyTransfer = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassTransfer", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifySwitch = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassSwitch", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapModifyExchange = Utilities.readTestData(Global.sModifyClassTestDataFilePath, "ModifyClassExchange", mapClassSearchDetails.get("TestcaseNameRef"));

					objClassModifyTabsMaps.put("ClassDetails", mapModifyClassDetails);
					objClassModifyTabsMaps.put("GeneralDetails", mapModifyGeneralDetails);
					objClassModifyTabsMaps.put("SubscriptionDetails", mapModifySubscriptionDetials);
					objClassModifyTabsMaps.put("OtherDetails", mapModifyOtherDetials);
					objClassModifyTabsMaps.put("RedemptionDetails", mapModifyRedemption);
					objClassModifyTabsMaps.put("TransferDetails", mapModifyTransfer);
					objClassModifyTabsMaps.put("SwitchDetails", mapModifySwitch);
					objClassModifyTabsMaps.put("ExchangeDetails", mapModifyExchange);
				}
				if(mapClassSearchDetails.get("Operation").equalsIgnoreCase("Validate"))
				{

					Map<String , String> mapClassDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath,"ClassDetailsTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapGeneralDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassGeneralDetailsTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapSubscriptionDetials = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassSubscriptionTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapOtherDetials = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassOtherDetailsTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapRedemption = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassRedemptionTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapTransfer = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassTransferTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapSwitch = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassSwitchTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
					Map<String , String> mapExchange = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassExchangeTabTestData", mapClassSearchDetails.get("TestcaseNameRef"));
					if(mapClassDetails.get("Clone").equalsIgnoreCase("Yes"))
					{	

						Map<String , String> mapClonedClassDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassDetailsTabTestData", mapClassDetails.get("Class Name for Clone"));
						Map<String ,String> NEwClassmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedClassDetails,mapClassDetails);
						if(mapClassDetails.get("Class Short Name")==null){
							NEwClassmap.remove("Class Short Name");
						}else{
							NEwClassmap.put("Class Short Name", mapClassDetails.get("Class Short Name"));
						}

						Map<String , String> mapClonedGenralDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassGeneralDetailsTabTestData", mapClonedClassDetails.get("TestcaseName"));
						Map<String ,String> NEwGDmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedGenralDetailsDetails,mapGeneralDetails);

						Map<String , String> mapClonedSubscriptionDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassSubscriptionTabTestData", mapClonedClassDetails.get("TestcaseName"));
						Map<String ,String> NEwSubscriptionmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedSubscriptionDetailsDetails,mapSubscriptionDetials);

						Map<String , String> mapClonedOtherDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassOtherDetailsTabTestData", mapClonedClassDetails.get("TestcaseName"));
						Map<String ,String> NEwOtherDetailsmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedOtherDetailsDetails,mapOtherDetials);



						Map<String , String> mapClonedRedemptionDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassRedemptionTabTestData", mapClonedClassDetails.get("TestcaseName"));
						Map<String ,String> NEwRedemptionmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedRedemptionDetailsDetails,mapRedemption);


						Map<String , String> mapClonedTransferDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassTransferTabTestData", mapClonedClassDetails.get("TestcaseName"));
						Map<String ,String> NEwTransfermap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedTransferDetailsDetails,mapTransfer);


						Map<String , String> mapClonedSwitchDetailsDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassSwitchTabTestData", mapClonedClassDetails.get("TestcaseName"));
						Map<String ,String> NEwmapSwitchmap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedSwitchDetailsDetails,mapSwitch);


						Map<String , String> mapClonedExchangeDetails = ExcelUtils.readDataABasedOnCellName(Global.sClassTestDataFilePath, "ClassExchangeTabTestData", mapClonedClassDetails.get("TestcaseName"));
						Map<String ,String> NEwmapExchangemap =  NewUICommonFunctions.cloneExisitngMapDetailsWithNewMap(mapClonedExchangeDetails,mapExchange);

						objClassCreationTabsMaps.put("ClassDetails", NEwClassmap);											
						objClassCreationTabsMaps.put("GeneralDetails", NEwGDmap);
						objClassCreationTabsMaps.put("SubscriptionDetails", NEwSubscriptionmap);
						objClassCreationTabsMaps.put("OtherDetails", NEwOtherDetailsmap);
						objClassCreationTabsMaps.put("RedemptionDetails", NEwRedemptionmap);
						objClassCreationTabsMaps.put("TransferDetails", NEwTransfermap);
						objClassCreationTabsMaps.put("SwitchDetails", NEwmapSwitchmap);
						objClassCreationTabsMaps.put("ExchangeDetails", NEwmapExchangemap);


					}
					else
					{
						objClassCreationTabsMaps.put("ClassDetails", mapClassDetails);
						objClassCreationTabsMaps.put("GeneralDetails", mapGeneralDetails);
						objClassCreationTabsMaps.put("SubscriptionDetails", mapSubscriptionDetials);
						objClassCreationTabsMaps.put("OtherDetails", mapOtherDetials);
						objClassCreationTabsMaps.put("RedemptionDetails", mapRedemption);
						objClassCreationTabsMaps.put("TransferDetails", mapTransfer);
						objClassCreationTabsMaps.put("SwitchDetails", mapSwitch);
						objClassCreationTabsMaps.put("ExchangeDetails", mapExchange);
					}	



				}
				NewUICommonFunctions.refreshThePage();

				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Class");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate to Class Mater Under Fund Setup", "Menu cannot be selected. Error: "+Messages.errorMsg);
					continue;
				}
				Reporting.logResults("Pass", "Navigate to Class Mater Under Fund Setup", "Class Menu selected succesfully");

				if(mapClassSearchDetails.get("Operation").equalsIgnoreCase("Modify")){
					if(ClassDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						bStatus = NewUICommonFunctions.modifyMasterDetails("Class Name", mapClassSearchDetails.get("Search Class Name"), "Class", objClassModifyTabsMaps);
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Class Data", "Class cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Class Data", "Class modified with the Tesdata: "+mapClassSearchDetails);
					}

					if(ClassDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						NewUICommonFunctions.selectMenu("Dashboard", "none");
						bStatus = ClassAppFunctions.modifyReturnClassDetails(objClassModifyTabsMaps, mapClassSearchDetails.get("Search Class Name").trim());
						if(!bStatus){
							Reporting.logResults("Fail", "Modify The Class Data", "Class cannot be modified. Error: "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass", "Modify The Class Data", "Class modified with the Tesdata: "+mapClassSearchDetails);
					}
				}

				if(mapClassSearchDetails.get("Operation").equalsIgnoreCase("Validate")){
					if(ClassDetails.get("CheckerOperations").equalsIgnoreCase("Approve")){
						bStatus = NewUICommonFunctions.verifyMasterDetails("Class Name", mapClassSearchDetails.get("Search Class Name"), "Class", objClassCreationTabsMaps);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Class Details in the modify screen", "Validating The Details Of Actual With Expected Is FAILED And The results are NOT matching with EXPECTED. "+Messages.errorMsg);
							continue;
						}
						Reporting.logResults("Pass","Validate Class Details in the modify screen", "Validating The Details Of Actual With Expected Is SUCCESSFULL And The results matching with EXPECTED.");
					}
					if(ClassDetails.get("CheckerOperations").equalsIgnoreCase("Return")){
						NewUICommonFunctions.selectMenu("Dashboard", "none");
						bStatus = ClassAppFunctions.verifyReturnDataInClassEditScreen(objClassCreationTabsMaps);
						if(!bStatus){
							Reporting.logResults("Fail","Validate Class Details in the modify screen", "Validating The Details Of Actual With Expected Is FAILED And The results are NOT matching with EXPECTED.");
							continue;
						}
						Reporting.logResults("Pass","Validate Class Details in the modify screen", "Validating The Details Of Actual With Expected Is SUCCESSFULL And The results matching with EXPECTED.");
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
		NewUICommonFunctions.refreshThePage();
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error "+Messages.errorMsg);
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
