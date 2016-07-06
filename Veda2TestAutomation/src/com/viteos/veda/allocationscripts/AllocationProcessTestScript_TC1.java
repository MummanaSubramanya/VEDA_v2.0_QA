package com.viteos.veda.allocationscripts;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Utilities;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.master.lib.AllocationWizardFunctions;
import com.viteos.veda.master.lib.ClientAppFunctions;
import com.viteos.veda.master.lib.Global;
import com.viteos.veda.master.lib.NewUICommonFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class AllocationProcessTestScript_TC1 {
	static boolean bStatus;
	static String sSheetName = "AllocationTestData";


	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Allocation Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;
		NewUICommonFunctions.isLoginFromAllocation = true;
		//Global.sVedaURL = "http://192.168.170.237:7070/veda2/";
		bStatus = NewUICommonFunctions.loginToApplication(Global.mapCredentials.get("AllocationUserName"), Global.mapCredentials.get("AllocationPassword"));
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}
	
	@org.testng.annotations.Test
	public static void test(){

		Map<String, Map<String, String>> mapAllAllocationDetails = Utilities.readMultipleTestData(Global.sAllocationTestDataPath,sSheetName,"Y");
		
		for(int index = 1;index <= mapAllAllocationDetails.size();index++){

			Map<String, String> mapAllocationDetails = mapAllAllocationDetails.get("Row"+index);
			
			Reporting.Testcasename = mapAllocationDetails.get("TestcaseName");
			
			//Navigate to Client Master
			bStatus = NewUICommonFunctions.selectMenu("ALLOCATION","Process");
			if(!bStatus){
				Reporting.logResults("Fail", "Navigate to Allocation Process", "Menu cannot be selected Error: "+Messages.errorMsg);
				continue;
			}
			Reporting.logResults("Pass", "Navigate to Allocation Process", "Allocation Process Menu selected succesfully");

			// Add New Client Details
			bStatus = AllocationWizardFunctions.doTriggerAllocationProcess(mapAllocationDetails);
			if(bStatus && mapAllocationDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){				
				Reporting.logResults("Pass", "Perform Allocation Process", "Performed Allocation Successfully ");
				
			}

			if(!bStatus && mapAllocationDetails.get("ExpectedResults").equalsIgnoreCase("Pass")){
				Reporting.logResults("Fail","Perform Allocation Process", "Cannot perform Allocation Process. Error: "+Messages.errorMsg);
				continue;
			}

			if(!bStatus && mapAllocationDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Pass","Perform Allocation Process", "Negative testcase-Cannot perform Allocation Process");
				continue;
			}

			if(bStatus && mapAllocationDetails.get("ExpectedResults").equalsIgnoreCase("Fail")){
				Reporting.logResults("Fail","Perform Allocation Process ", "Performed Allocation Process with negative testdata");
				continue;
			}	
		}
	}

	@AfterMethod
	public static void tearDown(){
		
		Reporting.Testcasename = "Close Application";
		bStatus = NewUICommonFunctions.logoutFromApplication();
		if(!bStatus){
			Reporting.logResults("Fail", "Close Application","Cannot logout from application. Error ");
			Assert.fail("Cannot logout from application. Error "+Messages.errorMsg);
		}
		Reporting.logResults("Pass","Logout from Application","Logged Out from application successfully");
	}
}
