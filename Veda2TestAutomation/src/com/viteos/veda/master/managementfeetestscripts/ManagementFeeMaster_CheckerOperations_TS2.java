package com.viteos.veda.master.managementfeetestscripts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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
import com.viteos.veda.master.lib.TempFunctions;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.XMLLibrary;

public class ManagementFeeMaster_CheckerOperations_TS2 {

	static boolean bStatus;
	static String sManagementFeeSheetName = "ManagementFeeDetails";

	@BeforeMethod
	public static void setUp(){
		Reporting.Functionality ="Management Fee Master";
		Reporting.Testcasename = "Open Application";
		Utilities.bscreenShot = false;
		//XMLLibrary.sManagementXMLFilePath = "XMLMessages//Management03-10-2016165314.xml";

		bStatus = NewUICommonFunctions.loginToApplication(Global.sCheckerUserName, Global.sCheckerPassword);
		if(!bStatus){
			Reporting.logResults("Fail", "Login into application", "Login to application Failed.Error: "+Messages.errorMsg);
			Assert.fail(Messages.errorMsg);
		}
		Reporting.logResults("Pass", "Login into application ", "Login into application successfully");
	}

	@Test
	public static void testCheckerOperations(){
		try{
			Map<String, Map<String, String>>  mapAllManagementFeeDetails = Utilities.readMultipleTestData(Global.sManagementFeeTestDataFilePath, sManagementFeeSheetName, "Y");
			Map<String, Map<String, String>> verifyManagementFee = new HashMap<String, Map<String, String>>();

			for(int index = 1;index <= mapAllManagementFeeDetails.size();index++){
				
				Map<String, String> innerMap = new HashMap<String, String>();
				Map<String, String> mapManagementFeeDetails = mapAllManagementFeeDetails.get("Row"+index);
				if(!mapManagementFeeDetails.get("OperationType").equalsIgnoreCase("Save") || mapManagementFeeDetails.get("ExpectedResults").equalsIgnoreCase("Fail") ){
					continue;
				}
				Map<String, String> mapXMLStoredManagementFeeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sManagementXMLFilePath, "ManagementFee", mapManagementFeeDetails.get("TestcaseName"));
				if(mapXMLStoredManagementFeeDetails == null || !mapXMLStoredManagementFeeDetails.get("MakerStatus").equalsIgnoreCase("Pass")){
					continue;
				}
				
				mapManagementFeeDetails.put("ManagementFeeID", mapXMLStoredManagementFeeDetails.get("ManagementFeeID"));
				
				Reporting.Testcasename = mapManagementFeeDetails.get("TestcaseName");

				//Navigate To Dash board.
				bStatus = NewUICommonFunctions.selectMenu("Dashboard","None");
				if(!bStatus){
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", Reporting.Testcasename, "ManagementFee");
					Reporting.logResults("Fail", "Navigate To DashBoard","Cannot Navigate to DashBoard");
					continue;
				}
				Reporting.logResults("Pass","Navigate To DashBoard", "Navigated to DashBoard");

				//Performs Checker Operations for the New Rule in management fee test data.
				if (mapManagementFeeDetails.get("New Rule") != null && mapManagementFeeDetails.get("New Rule").equalsIgnoreCase("Yes")) 
				{
					if(mapManagementFeeDetails.get("CheckerOperationsOnFormula")!=null && !mapManagementFeeDetails.get("CheckerOperationsOnFormula").equalsIgnoreCase("Approve"))
					{
						bStatus = ManagementFeeAppFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,mapXMLStoredManagementFeeDetails.get("ManagementFeeID"), mapManagementFeeDetails.get("CheckerOperationsOnFormula"),null);
						if(bStatus || mapManagementFeeDetails.get("CheckerOperations") == null || mapManagementFeeDetails.get("ExpectedResultsAfterCheckerOperations") == null){
							continue;
						}
					}
					if(mapManagementFeeDetails.get("CheckerOperationsOnFormula")!=null && mapManagementFeeDetails.get("CheckerOperationsOnFormula").equalsIgnoreCase("Approve"))
					{
						bStatus = ManagementFeeAppFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,mapXMLStoredManagementFeeDetails.get("ManagementFeeID"), mapManagementFeeDetails.get("CheckerOperationsOnFormula"),mapManagementFeeDetails.get("CheckerOperations"));								
						if(mapManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && mapManagementFeeDetails.get("CheckerOperationsOnFormula").equalsIgnoreCase("Approve") && bStatus){		
							innerMap.put("Status", "active");
							innerMap.put("Legal Entity Name", mapManagementFeeDetails.get("Legal Entity Name"));
							innerMap.put("ManagementFeeID", mapManagementFeeDetails.get("ManagementFeeID"));					
							verifyManagementFee.put(Reporting.Testcasename,innerMap);
						}

						if((mapManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Reject") || mapManagementFeeDetails.get("CheckerOperationsOnFormula").equalsIgnoreCase("Reject")) && bStatus){				
							innerMap.put("Status", "inactive");
							innerMap.put("Legal Entity Name", mapManagementFeeDetails.get("Legal Entity Name"));
							innerMap.put("ManagementFeeID", mapManagementFeeDetails.get("ManagementFeeID"));
							verifyManagementFee.put(Reporting.Testcasename,innerMap);
						}
					}
					
				}

				//Performs Checker Operations for the Created Rule in management fee test data.
				if(mapManagementFeeDetails.get("New Rule") == null || mapManagementFeeDetails.get("New Rule").equalsIgnoreCase("No"))
				{
					
					bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,mapManagementFeeDetails.get("ManagementFeeID"),mapManagementFeeDetails.get("CheckerOperations"));
					if(mapManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Approve") && bStatus){	
						innerMap.put("Status", "active");
						innerMap.put("Legal Entity Name", mapManagementFeeDetails.get("Legal Entity Name"));
						innerMap.put("ManagementFeeID", mapManagementFeeDetails.get("ManagementFeeID"));					
						verifyManagementFee.put(Reporting.Testcasename,innerMap);
					}

					if(mapManagementFeeDetails.get("CheckerOperations").equalsIgnoreCase("Reject") && bStatus){			
						innerMap.put("Status", "inactive");
						innerMap.put("Legal Entity Name", mapManagementFeeDetails.get("Legal Entity Name"));
						innerMap.put("ManagementFeeID", mapManagementFeeDetails.get("ManagementFeeID"));
						verifyManagementFee.put(Reporting.Testcasename,innerMap);
					}
				}	

				if(bStatus && mapManagementFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Pass","Perform Checker Operation", "Successfully Performed checker operations: "+mapManagementFeeDetails.get("CheckerOperations"));
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "CheckerStatus", "Pass", "TestcaseName", Reporting.Testcasename, "ManagementFee");
				}

				if(!bStatus && mapManagementFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Pass")){
					Reporting.logResults("Fail","Perform Checker Operation", "Cannot Perform Checker Operations: "+mapManagementFeeDetails.get("CheckerOperations")+". Error: "+Messages.errorMsg);
					XMLLibrary.updatedAttributeValueInCreatedXMLFile(XMLLibrary.sManagementXMLFilePath, "CheckerStatus", "Fail", "TestcaseName", Reporting.Testcasename, "ManagementFee");
					continue;
				}

				if(!bStatus && mapManagementFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Pass","Perform Checker Operation", "Negative testcase - Cannot Perform Checker Operations : "+mapManagementFeeDetails.get("CheckerOperations")+".");
				}

				if(bStatus && mapManagementFeeDetails.get("ExpectedResultsAfterCheckerOperations").equalsIgnoreCase("Fail")){
					Reporting.logResults("Fail","Perform Checker Operation", "Peformed Checker operations with negative testdata. Error: "+Messages.errorMsg);
					continue;
				}	

				
			}			
			//search for the Management Fee in Search Grid.
			verifyValuesinSearchPanel(verifyManagementFee);
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
	
	
	private static void verifyValuesinSearchPanel(Map<String, Map<String, String>> verifyManagementFee) {
		try{
			for (Entry<String, Map<String, String>> test : verifyManagementFee.entrySet()) {				

				Reporting.Testcasename = test.getKey();

				bStatus = NewUICommonFunctions.selectMenu("Fund Setup","Management Fee");
				if(!bStatus){
					Reporting.logResults("Fail", "Navigate To Management Fee","Cannot Navigate to Management Fee Page");
					continue;
				}
				Reporting.logResults("Pass","Navigate To Management Fee", "Navigated to Management Fee page");

				int time =15 ;
				if(test.getValue().get("Status").equalsIgnoreCase("inactive")){
					time = 4;
				}
				
				//search function for Management Fee.
				bStatus = TempFunctions.verifyRecordIsDisplayedInTheGridTableUsingIDOrAdditionalColumnArgument("Legal Entity Name",test.getValue().get("Legal Entity Name"),test.getValue().get("Status"), "Management Fee Id", test.getValue().get("ManagementFeeID"),"ManagementFee", time);				
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Fail", "Verify Management Fee shouldnot be Inactive in Search Grid.",test.getValue().get("ManagementFeeID")+" : Inactive Management Fee is Not visible in Search Grid.");						
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("inactive")){
					Reporting.logResults("Pass", "Verify Management Fee should be Inactive in Search Grid.",test.getValue().get("ManagementFeeID")+" :Inactive Management Fee is Visible in in Search Grid.");					
				}
				if(!bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Fail", "Verify Management Fee should be Active in Search Grid.",test.getValue().get("ManagementFeeID")+" : Active Management Fee is Not-Visible in Search Grid.");						
				}
				if(bStatus && test.getValue().get("Status").equalsIgnoreCase("active")){
					Reporting.logResults("Pass", "Verify Management Fee should be Active in Search Grid.",test.getValue().get("ManagementFeeID")+" : Active Management Fee is visible in Search Grid.");
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}
