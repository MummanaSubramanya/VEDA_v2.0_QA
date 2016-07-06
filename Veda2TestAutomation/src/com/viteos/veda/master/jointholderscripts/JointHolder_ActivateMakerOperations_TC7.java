package com.viteos.veda.master.jointholderscripts;
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

public class JointHolder_ActivateMakerOperations_TC7 {

	static boolean bStatus;
	static String sSheetName = "ModifyTestData";
	
	@BeforeMethod
	public static void setup(){

		Reporting.Functionality ="JointHolder Setup";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;

		bStatus=NewUICommonFunctions.loginToApplication(Global.sMakerUserName, Global.sMakerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application as Checker", "failed");
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application as Checker", "Passed");
	}

	@Test
	public static void activateMakerOperations(){
		try{
			Map<String, Map<String, String>> mapAllJointHolderDetails = Utilities.readMultipleTestData(Global.sJointHolderTestDataFilePath,sSheetName,"Y");

			for(int index = 1;index <= mapAllJointHolderDetails.size();index++){

				Map<String, String> mapJointHolderDetails = mapAllJointHolderDetails.get("Row"+index);

				if(mapJointHolderDetails.get("Operation").equalsIgnoreCase("Validate") || mapJointHolderDetails.get("Operation").equalsIgnoreCase("Modify") || mapJointHolderDetails.get("Operation").equalsIgnoreCase("DeActivate")){
					continue;
				}

				Reporting.Testcasename = mapJointHolderDetails.get("TestcaseNameRef");
				//Navigate to Joint Holder Module
				bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Joint Holder");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Joint Holder Master", "Failed to Navigate to Joint Holder Master");
				}
				Reporting.logResults("Pass", "Navigate To Joint Holder Master", "Navigated to Joint Holder Master");

				if(mapJointHolderDetails.get("Operation").equalsIgnoreCase("Activate")){

					//Read Test data
					Map<String, String> createdJointHolderDetails = ExcelUtils.readDataABasedOnCellName(Global.sJointHolderTestDataFilePath,"JointHolderTestData", mapJointHolderDetails.get("TestcaseNameRef"));
					boolean bNameModifyStatus = false;
					Map<String, String> modifiedJointHolderDetails = ExcelUtils.readDataABasedOnCellName(Global.sJointHolderTestDataFilePath,"ModifyTestData", mapJointHolderDetails.get("TestcaseNameRef"));
					if(modifiedJointHolderDetails!=null && modifiedJointHolderDetails.get("Operation").equalsIgnoreCase("Modify")){
						if(modifiedJointHolderDetails.containsKey("First Name") || modifiedJointHolderDetails.containsKey("Middle Name") || modifiedJointHolderDetails.containsKey("Last Name")  ){
							bNameModifyStatus = true;
							//mapJointHolderDetails.put("First Name", modifiedJointHolderDetails.get("First Name"));
							String Name = "";
							if( modifiedJointHolderDetails.get("First Name")!= null){
								Name = modifiedJointHolderDetails.get("First Name");
								}
								if(modifiedJointHolderDetails.get("First Name")== null){
									Name = createdJointHolderDetails.get("First Name");
								}
								if(modifiedJointHolderDetails.get("Middle Name") != null){
									Name = Name+" "+ modifiedJointHolderDetails.get("Middle Name");
								}
								if(modifiedJointHolderDetails.get("Middle Name")== null && createdJointHolderDetails.get("Middle Name")!= null) {
									Name = Name+" "+createdJointHolderDetails.get("Middle Name");
								}
								if(modifiedJointHolderDetails.get("Last Name") != null){
									Name = Name+" "+ modifiedJointHolderDetails.get("Last Name");
								}
								if(modifiedJointHolderDetails.get("Last Name")== null && createdJointHolderDetails.get("Last Name")!= null){
									Name = Name+" "+createdJointHolderDetails.get("Last Name");
								}

							mapJointHolderDetails.put("Name", Name);
						}
					}
					if(!bNameModifyStatus){
						//mapJointHolderDetails.put("First Name", createdJointHolderDetails.get("First Name"));
						String Name = "";
						Name = createdJointHolderDetails.get("First Name").trim();

						if(createdJointHolderDetails.get("Middle Name") != null){
							Name = Name+" "+ createdJointHolderDetails.get("Middle Name").trim();
						}

						if(createdJointHolderDetails.get("Last Name") != null){
							Name = Name+" "+ createdJointHolderDetails.get("Last Name").trim();
						}

						mapJointHolderDetails.put("Name", Name);
						//mapJointHolderDetails.put("First Name", createdJointHolderDetails.get("First Name"));
					}
					
					bStatus = NewUICommonFunctions.activateMaster("Investor Joint Holder Name",mapJointHolderDetails.get("Name"),"JointHolder");
					if(!bStatus){
						Reporting.logResults("Fail","Validate Joint Holder has not be Activated.  "+ mapJointHolderDetails.get("Name"), "Joint Holder has not be Activated. Error: "+Messages.errorMsg);
						continue;
					}
					Reporting.logResults("Pass","Validate Joint Holder  has been Activated "+mapJointHolderDetails.get("Name"), "Joint Holder has been Activated and send to Approval");
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

