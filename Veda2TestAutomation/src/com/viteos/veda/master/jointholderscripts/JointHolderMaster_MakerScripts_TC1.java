package com.viteos.veda.master.jointholderscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.JointHolderAppFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class JointHolderMaster_MakerScripts_TC1 {
	static boolean bStatus;
	static String sRefrenceJointHolderSheetName = "JointHolderTestData";
	
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
	public static void addNewJointHolder(){
		Map<String ,Map<String,String>> mapAllJointHolderDetails = Utilities.readMultipleTestData(Global.sJointHolderTestDataFilePath, sRefrenceJointHolderSheetName, "Y");
		
		for(int index=1 ; index<=mapAllJointHolderDetails.size() ; index++){
			
			Map<String , String > mapJointHolderDetails = mapAllJointHolderDetails.get("Row"+index);
			Reporting.Testcasename = mapJointHolderDetails.get("TestcaseName");
			
			//Navigate To Investors Joint Holder Master
			
			bStatus = NewUICommonFunctions.selectMenu("Investor Setup", "Joint Holder");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate To Joint Holder Master", "Failed to Navigate to Joint Holder Master");
			}
			Reporting.logResults("Pass", "Navigate To Joint Holder Master", "Navigated to Joint Holder Master");
			
			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.ADDNEW);
			if(!bStatus){
				Reporting.logResults("Fail", "Click on AddNew Button", "Failed to Click on AddNew Button");
			}
			Reporting.logResults("Pass", "Click on AddNew Button", "AddNew Button Clicked Successfully");
			
			bStatus = JointHolderAppFunctions.addNewJointHolder(mapJointHolderDetails);
			if(bStatus && mapJointHolderDetails.get("ExpectedResults").equalsIgnoreCase("Pass"))
			{
				if(mapJointHolderDetails.get("OperationType").equalsIgnoreCase("Save")){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapJointHolderDetails.get("OperationType"), "Performed Maker Operation Successfull Joint Holder Created"+mapJointHolderDetails.get("First Name"));
					
				}
				if(mapJointHolderDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					Reporting.logResults("Pass", "Perform Maker Operation: "+mapJointHolderDetails.get("OperationType"), "Performed Maker Operation: "+mapJointHolderDetails.get("OperationType")+" Successfully");
				}
			}
			if(!bStatus && mapJointHolderDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				
				Reporting.logResults("Fail","Perform Maker Operation: "+mapJointHolderDetails.get("OperationType"), "Cannot perform Maker Operation. Error: "+Messages.errorMsg+" for Investor: "+mapJointHolderDetails.get("First Name"));
				continue;
			}

			if(!bStatus && mapJointHolderDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				
				Reporting.logResults("Pass","Perform Maker Operation: "+mapJointHolderDetails.get("OperationType"), "Negative testcase-Cannot perform Maker Action for Investor: "+mapJointHolderDetails.get("First Name"));
			}

			if(bStatus && mapJointHolderDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				
				Reporting.logResults("Fail","Perform Maker Operation: "+mapJointHolderDetails.get("OperationType"), "Performed Maker Action with negative testdata. Investor Name: "+mapJointHolderDetails.get("First Name"));
				continue;
			}			
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
