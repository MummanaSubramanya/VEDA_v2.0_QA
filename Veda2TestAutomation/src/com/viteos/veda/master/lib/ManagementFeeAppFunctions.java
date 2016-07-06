package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.checkerActionTypes;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class ManagementFeeAppFunctions {
	public static boolean bStatus;

	public static boolean createNewManagementFee(Map<String, String> managementFeeDetailsMap){
		try {			 
			Map<String, String> mapExistingFormulaDetails = new HashMap<String, String>();

			//Check for new rule and get existing formula details if not new rule.
			if((managementFeeDetailsMap.get("New Rule") == null || !managementFeeDetailsMap.get("New Rule").equalsIgnoreCase("Yes")) && managementFeeDetailsMap.get("Management Fee On") != null){
				mapExistingFormulaDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath, "FormulaTestData", managementFeeDetailsMap.get("Management Fee On"));
			}
			if (managementFeeDetailsMap.get("Client Name") != null) {				
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", managementFeeDetailsMap.get("Client Name"));
				Thread.sleep(1000);
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Client Name : "+managementFeeDetailsMap.get("Client Name")+" from drop down. client name wasn't visible.";
					return false;
				}
			}
			if (managementFeeDetailsMap.get("Fund Family Name") != null) {				
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", managementFeeDetailsMap.get("Fund Family Name"));
				Thread.sleep(2000);
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Fund Family Name : "+managementFeeDetailsMap.get("Fund Family Name")+" from drop down. Fund Family Name wasn't visible.";
					return false;
				}
			}
			if (managementFeeDetailsMap.get("Legal Entity Name") != null) {
				Thread.sleep(1000);
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name", managementFeeDetailsMap.get("Legal Entity Name"), 10);
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Legal Entity Name : "+managementFeeDetailsMap.get("Legal Entity Name")+" from drop down. Legal Entity Name wasn't visible.";
					return false;
				}
			}
			if (managementFeeDetailsMap.get("Class Name") != null) {
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//select[@id='cmbClassName']//option[normalize-space()=\""+managementFeeDetailsMap.get("Class Name")+"\"]"), Constants.lTimeOut);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Class Name Drop Down value : '"+managementFeeDetailsMap.get("Class Name")+"', wasn't loaded after waiting : "+Constants.lTimeOut+" time.]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Class Name", managementFeeDetailsMap.get("Class Name"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Class Name : "+managementFeeDetailsMap.get("Class Name")+" from drop down. Class Name wasn't visible.";
					return false;
				}
			}			
			if ((managementFeeDetailsMap.get("New Rule") == null || !managementFeeDetailsMap.get("New Rule").equalsIgnoreCase("Yes")) && managementFeeDetailsMap.get("Management Fee On") != null && mapExistingFormulaDetails.get("FeeRuleName") != null) {
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//select[@id='managementFeeRule']//option[normalize-space()=\""+mapExistingFormulaDetails.get("FeeRuleName")+"\"]"), Constants.lTimeOut);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Management Fee On(Rule) Drop Down value : '"+mapExistingFormulaDetails.get("FeeRuleName")+"', wasn't loaded after waiting : "+Constants.lTimeOut+" time.]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Management Fee On(Rule)", mapExistingFormulaDetails.get("FeeRuleName"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Management Fee On (Rule) : "+mapExistingFormulaDetails.get("FeeRuleName")+" from drop down. Management Fee On (Rule) wasn't visible.";
					return false;
				}
			}
			if (managementFeeDetailsMap.get("New Rule") != null && managementFeeDetailsMap.get("New Rule").equalsIgnoreCase("Yes")) {
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.ManagementFee.Links.lnkNewRule);
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to Click the link to create a new (Rule) from Management Fee ";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Formula')]"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Formula creation form wasn't displayed after clicking on 'New Rule' link ";
					return false;
				}				
				bStatus = NewUICommonFunctions.addNewFormulasForManagementOrIncentiveFee(managementFeeDetailsMap, "Save");				
				if (!bStatus) {
					return false;
				}
			}

			if (managementFeeDetailsMap.get("Effective Start Date") != null) {		
				bStatus = Elements.enterText(Global.driver, Locators.ManagementFee.TextBox.txtEffectiveStartDate, managementFeeDetailsMap.get("Effective Start Date"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to enter Effective Start Date : "+managementFeeDetailsMap.get("Effective Start Date")+" for Management Fee Rule.";
					return false;
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(text(),'Management Fee')]"));
			}
			if (managementFeeDetailsMap.get("Fee Accrual Type") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fee Accrual Type", managementFeeDetailsMap.get("Fee Accrual Type"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Fee Accrual Type : "+managementFeeDetailsMap.get("Fee Accrual Type")+" for Management Fee Rule.";
					return false;
				}				
			}
			if (managementFeeDetailsMap.get("Fee Periodicity") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fee Periodicity", managementFeeDetailsMap.get("Fee Periodicity"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Fee Periodicity : "+managementFeeDetailsMap.get("Fee Periodicity")+" for Management Fee Rule.";
					return false;
				}				
			}
			if (managementFeeDetailsMap.get("Payment Frequency") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Payment Frequency", managementFeeDetailsMap.get("Payment Frequency"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Payment Frequency : "+managementFeeDetailsMap.get("Payment Frequency")+" for Management Fee Rule.";
					return false;
				}				
			}
			if (managementFeeDetailsMap.get("No of days for Calculation") != null) {

				//No of days calculation by 'Reporting Period'
				if (managementFeeDetailsMap.get("No of days for Calculation").equalsIgnoreCase("Reporting Period")) {
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isCalcBaseOnRepoPeriodYes']//.."));
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Unable to select the Radio button for 'No of days for Calculation' : "+managementFeeDetailsMap.get("No of days for Calculation");
						return false;
					}
					//Days convention drop down
					if(managementFeeDetailsMap.get("Days Convention") != null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Days Convention", managementFeeDetailsMap.get("Days Convention"));
						if (!bStatus) {
							Messages.errorMsg = " ERROR : Unable to select the 'Days Convention' : "+managementFeeDetailsMap.get("Days Convention") +"for given Reorting Period";
							return false;
						}
						//Select Convention Period
						if(managementFeeDetailsMap.get("Convention Period") != null){
							if(managementFeeDetailsMap.get("Days Convention").equalsIgnoreCase("Days")){
								bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Convention Period", managementFeeDetailsMap.get("Convention Period"));
								if (!bStatus) {
									Messages.errorMsg = " ERROR : Unable to select the 'Convention Period' : "+managementFeeDetailsMap.get("Convention Period") +"for given Reorting Period";
									return false;
								}
							}
							if(managementFeeDetailsMap.get("Days Convention").equalsIgnoreCase("Months")){
								bStatus = NewUICommonFunctions.verifyTextInDropDown("Convention Period", managementFeeDetailsMap.get("Convention Period"));
								if(!bStatus){
									Messages.errorMsg = "[ ERROR:"+Messages.errorMsg+"]\n";
									return false;
								}
							}
						}
					}
				}
				if (managementFeeDetailsMap.get("No of days for Calculation").equalsIgnoreCase("Fee Period")) {
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isCalcBaseOnRepoPeriodNo']//.."));
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Unable to select the Radio button for No of days for Calculation : "+managementFeeDetailsMap.get("No of days for Calculation");
						return false;
					}
					if (managementFeeDetailsMap.get("Fee Period Time Calculation") != null) {
						if (managementFeeDetailsMap.get("Fee Period Time Calculation").equalsIgnoreCase("Evenly")) {
							bStatus = Elements.click(Global.driver, By.id(Locators.ManagementFee.RadioButtons.sRadioFeePeriodTimeCalculation.replace("ReplaceIndex", "1")));
							if (!bStatus) {
								Messages.errorMsg = " ERROR : Unable to select the Radio button for Fee Period Time Calculation : "+managementFeeDetailsMap.get("Fee Period Time Calculation");
								return false;
							}
						}
						if (managementFeeDetailsMap.get("Fee Period Time Calculation").equalsIgnoreCase("Based On No Of Days")) {
							bStatus = Elements.click(Global.driver, By.id(Locators.ManagementFee.RadioButtons.sRadioFeePeriodTimeCalculation.replace("ReplaceIndex", "2")));
							if (!bStatus) {
								Messages.errorMsg = " ERROR : Unable to select the Radio button for Fee Period Time Calculation : "+managementFeeDetailsMap.get("Fee Period Time Calculation");
								return false;
							}
						}
					}
				}				
			}
			
			if (managementFeeDetailsMap.get("For New Subscription") != null) {
				if (managementFeeDetailsMap.get("For New Subscription").equalsIgnoreCase("Reporting Period")) {
					bStatus = Elements.click(Global.driver, By.id(Locators.ManagementFee.RadioButtons.sRadioIsNewSubscriptionEffectiveDate.replace("ReplaceIndex", "1")));
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Unable to select the Radio button For New Subscription : "+managementFeeDetailsMap.get("For New Subscription");
						return false;
					}
				}
				if (managementFeeDetailsMap.get("For New Subscription").equalsIgnoreCase("Subscription Effective Date")){
					bStatus = Elements.click(Global.driver, By.id(Locators.ManagementFee.RadioButtons.sRadioIsNewSubscriptionEffectiveDate.replace("ReplaceIndex", "2")));
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Unable to select the Radio button For New Subscription : "+managementFeeDetailsMap.get("For New Subscription");
						return false;
					}
				}
			}
			
			if (managementFeeDetailsMap.get("Apply Minimum Fee") != null) {
				if (managementFeeDetailsMap.get("Apply Minimum Fee").equalsIgnoreCase("No")) {
					bStatus = Elements.click(Global.driver, By.id(Locators.ManagementFee.RadioButtons.sRadioIsApplyMinimumFee.replace("ReplaceIndex", "2")));
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Unable to select the Radio button Apply Minimum Fee : "+managementFeeDetailsMap.get("Apply Minimum Fee");
						return false;
					}
				}
				if (managementFeeDetailsMap.get("Apply Minimum Fee").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.id(Locators.ManagementFee.RadioButtons.sRadioIsApplyMinimumFee.replace("ReplaceIndex", "1")));
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Unable to select the Radio button Apply Minimum Fee : "+managementFeeDetailsMap.get("Apply Minimum Fee");
						return false;
					}
					if (managementFeeDetailsMap.get("Management Fee") != null && managementFeeDetailsMap.get("Applied Value") != null && managementFeeDetailsMap.get("Minimum Fee Effective Start Date") != null && managementFeeDetailsMap.get("Minimum Fee Effective End Date") != null) {
						bStatus = addMinimumFeeAppliedData(managementFeeDetailsMap);
					}
				}
			}
			
			if (managementFeeDetailsMap.get("Fee Calculation Method") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fee Calculation Method", managementFeeDetailsMap.get("Fee Calculation Method"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select the Fee Calculation Method from dropdown for Type : "+managementFeeDetailsMap.get("Fee Calculation Method");
					return false;
				}
				if (managementFeeDetailsMap.get("Fee Calculation Method").equalsIgnoreCase("Fixed")) {
					if(managementFeeDetailsMap.get("Fee Percentage") != null){
						bStatus = Elements.enterText(Global.driver, Locators.ManagementFee.TextBox.txtFeePercentage, managementFeeDetailsMap.get("Fee Percentage"));
						if (!bStatus) {
							Messages.errorMsg = " ERROR : Unable to enter the Fee Percentage for Fixed Fee Calculation Method.";
							return false;
						}
					}
					if(managementFeeDetailsMap.get("Fixed Spread Percentage") != null){
						bStatus = Elements.enterText(Global.driver, Locators.ManagementFee.TextBox.txtSpreadPercentage, managementFeeDetailsMap.get("Fixed Spread Percentage"));
						if (!bStatus) {
							Messages.errorMsg = " ERROR : Unable to enter the Spread Percentage for Fixed Fee Calculation Method.";
							return false;
						}
					}
				}
				if (managementFeeDetailsMap.get("Fee Calculation Method").equalsIgnoreCase("Slab Based") || managementFeeDetailsMap.get("Fee Calculation Method").equalsIgnoreCase("Tier Based")) {
					if(managementFeeDetailsMap.get("Amount From") !=null && managementFeeDetailsMap.get("Amount To")!=null && managementFeeDetailsMap.get("Percentage")!=null){
						bStatus = NewUICommonFunctions.addManagementOrIncentiveFeeChargesForSlabAndTier(managementFeeDetailsMap, "Management Fee");
						if (!bStatus) {
							Messages.errorMsg = " ERROR : Slabs Or Tiered Structured details Not Entered into respective fields.";
							return false;
						}
					}

				}
			}
			bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Management Fee Master", managementFeeDetailsMap.get("OperationType"));
			if(!bStatus){
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean addMinimumFeeAppliedData(Map<String, String> managementFeeDetailsMap) {
		try {
			String managementFeeValueLocater = "managementMinimumFeeDetailsReplaceIndexValue.managementFeeValue";
			String appliedValueLocater = "managementMinimumFeeDetailsReplaceIndexValue.appliedValue";
			String effectiveStartDateLocater = "//input[@id='managementMinimumFeeDetailsReplaceIndexValue.effectiveStartDate']";
			String effectiveEndDateLocater = "//input[@id='managementMinimumFeeDetailsReplaceIndexValue.effectiveEndDate']";
			By objAddMoreLocater = By.xpath("//button[contains(@onclick,'addMinimumFee')]");
			
			String mgmtFeeValue, appliedValue, effStartDate, effEndDate;
			List<String> mgmtFeeValueArray = null, appliedValueArray = null, effStartDateArray = null, effEndDateArray = null;
			
			if(managementFeeDetailsMap.get("Management Fee") != null) {
				mgmtFeeValue = managementFeeDetailsMap.get("Management Fee");
				mgmtFeeValueArray = Arrays.asList(mgmtFeeValue.split(","));
			}
			if(managementFeeDetailsMap.get("Applied Value") != null) {
				appliedValue = managementFeeDetailsMap.get("Applied Value");
				appliedValueArray = Arrays.asList(appliedValue.split(","));
			}
			if(managementFeeDetailsMap.get("Minimum Fee Effective Start Date") != null) {
				effStartDate = managementFeeDetailsMap.get("Minimum Fee Effective Start Date");
				effStartDateArray = Arrays.asList(effStartDate.split(","));
			}
			if(managementFeeDetailsMap.get("Minimum Fee Effective End Date") != null) {
				effEndDate = managementFeeDetailsMap.get("Minimum Fee Effective End Date");
				effEndDateArray = Arrays.asList(effEndDate.split(","));
			}
			
			for (int i=0 ; i < mgmtFeeValueArray.size() ; i++) {
				if(managementFeeDetailsMap.get("Management Fee") != null && !mgmtFeeValueArray.get(i).equalsIgnoreCase("none")) {
					String mgmtFeeLoc = managementFeeValueLocater.replace("ReplaceIndexValue", String.valueOf(i));
					bStatus = Elements.enterText(Global.driver, By.id(mgmtFeeLoc), mgmtFeeValueArray.get(i).toString());
					if(!bStatus){
						Messages.errorMsg = "Mgmt Fee Value Not Entered in "+i+" index";
						return false;
					}
				}
				if(managementFeeDetailsMap.get("Applied Value") != null && !appliedValueArray.get(i).equalsIgnoreCase("none")) {
					String appValLoc = appliedValueLocater.replace("ReplaceIndexValue", String.valueOf(i));
					bStatus = Elements.enterText(Global.driver, By.id(appValLoc), appliedValueArray.get(i).toString());
					if(!bStatus){
						Messages.errorMsg = "Applied Value Not Entered in "+i+" index";
						return false;
					}
				}
				if(managementFeeDetailsMap.get("Minimum Fee Effective Start Date") != null && !effStartDateArray.get(i).equalsIgnoreCase("none")) {
					String startDateLoc = effectiveStartDateLocater.replace("ReplaceIndexValue", String.valueOf(i));
					bStatus = NewUICommonFunctions.doPickDateFromCalender(effStartDateArray.get(i).toString(), startDateLoc);//Elements.enterText(Global.driver, By.id(startDateLoc), effStartDateArray.get(i).toString());
					if(!bStatus){
						Messages.errorMsg = "Effective Start Date Not Entered in "+i+" index";
						return false;
					}
				}
				if(managementFeeDetailsMap.get("Minimum Fee Effective End Date") != null && !effEndDateArray.get(i).equalsIgnoreCase("none")) {
					String endDateLoc = effectiveEndDateLocater.replace("ReplaceIndexValue", String.valueOf(i));
					bStatus = NewUICommonFunctions.doPickDateFromCalender(effEndDateArray.get(i).toString(), endDateLoc);;//Elements.enterText(Global.driver, By.id(endDateLoc), effEndDateArray.get(i).toString());
					if(!bStatus){
						Messages.errorMsg = "Effective End Date Not Entered in "+i+" index";
						return false;
					}
				}
				if(i < mgmtFeeValueArray.size()-1){
					bStatus = Elements.click(Global.driver, objAddMoreLocater );
					if(!bStatus){
						Messages.errorMsg = "Add More Button Not Clicked";
						return false;
					}
				}
			}
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public static boolean doVerifyManagementFeeOnEditScreen(Map<String , String> mapManagementFeeDetails){
		try {
			/*
			 * Need to Implement the Search Function
			 * 
			NewUICommonFunctions.SearchinSearchPanel(mapManagementFeeDetails);
			Search and Click on Edit for the Created Management Fee 

			 */
			Map<String , String> formulaSetupDetails = new HashMap<String , String>();
			if(mapManagementFeeDetails.get("Management Fee On")!=null){
				formulaSetupDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath, "FormulaTestData", mapManagementFeeDetails.get("Management Fee On"));
			}

			String appendMsg = "";
			boolean bValidStatus = true;
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='fkTblFundFamily.tblClientDefinition.clientName']"), 10);
			if(!bStatus){
				Messages.errorMsg = "Client Name Field is not visisble";
				return false;
			}
			if(mapManagementFeeDetails.get("Client Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Client Name", mapManagementFeeDetails.get("Client Name"));
				if(!bStatus){
					bValidStatus = false;
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
				}
			}
			if(mapManagementFeeDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Fund Family Name", mapManagementFeeDetails.get("Fund Family Name"));
				if(!bStatus){
					bValidStatus = false;
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
				}
			}
			if(mapManagementFeeDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Legal Entity Name", mapManagementFeeDetails.get("Legal Entity Name"));
				if(!bStatus){
					bValidStatus = false;
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
				}
			}
			if(mapManagementFeeDetails.get("Class Name")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Class Name", mapManagementFeeDetails.get("Class Name"));
				if(!bStatus){
					bValidStatus = false;
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
				}
			}
			if(mapManagementFeeDetails.get("Management Fee On")!=null)
			{
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Management Fee On(Rule)", mapManagementFeeDetails.get("Management Fee On")); 
				if(!bStatus){
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
					bValidStatus = false;
				}		
				if(formulaSetupDetails.get("Rule")!=null)
				{
					String sExpectedRuleValue = formulaSetupDetails.get("Rule").replaceAll("," , "");
					String sActualRulevalue = Elements.getText(Global.driver, By.id("ruleFormula"));
					if(!sExpectedRuleValue.equalsIgnoreCase(sActualRulevalue)){
						bValidStatus = false;
						appendMsg = appendMsg+"[ "+sActualRulevalue+" is actual Management Fee Rule not equal to Expected: "+formulaSetupDetails.get("Rule")+" ]\n";
					}
				}
			}
			if(mapManagementFeeDetails.get("New Rule")!=null && mapManagementFeeDetails.get("New Rule").equalsIgnoreCase("Yes"))
			{
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Management Fee On(Rule)", mapManagementFeeDetails.get("FeeRuleName"));
				if(!bStatus){
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
					bValidStatus = false;
				}
				if(mapManagementFeeDetails.get("Rule")!=null)
				{
					String sExpectedRuleValue = mapManagementFeeDetails.get("Rule").replaceAll("," , "");
					String sActualRulevalue = Elements.getText(Global.driver, By.id("ruleFormula"));
					if(!sExpectedRuleValue.equalsIgnoreCase(sActualRulevalue)){
						bValidStatus = false;
						appendMsg = appendMsg+"[ "+sActualRulevalue+" is actual Management Fee Rule not equal to Expected: "+mapManagementFeeDetails.get("Rule")+" ]\n";
					}
				}
			}
			if(mapManagementFeeDetails.get("Effective Start Date")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Effective Start Date", mapManagementFeeDetails.get("Effective Start Date"));
				if(!bStatus){
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
					bValidStatus = false;
				}
			}
			if(mapManagementFeeDetails.get("Fee Accrual Type")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Fee Accrual Type", mapManagementFeeDetails.get("Fee Accrual Type"));
				if(!bStatus){
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
					bValidStatus = false;
				}
			}
			if(mapManagementFeeDetails.get("Fee Periodicity")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Fee Periodicity", mapManagementFeeDetails.get("Fee Periodicity"));
				if(!bStatus){
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
					bValidStatus = false;
				}
			}
			if(mapManagementFeeDetails.get("Payment Frequency")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Payment Frequency", mapManagementFeeDetails.get("Payment Frequency"));
				if(!bStatus){
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
					bValidStatus = false;
				}
			}
			if(mapManagementFeeDetails.get("No of days for Calculation")!=null)
			{
				bStatus = NewUICommonFunctions.verifySelectedRadioButton("No of days for Calculation", mapManagementFeeDetails.get("No of days for Calculation"));
				if(!bStatus){
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
					bValidStatus = false;
				}
				//verify Reporting Period
				if(mapManagementFeeDetails.get("No of days for Calculation").equalsIgnoreCase("Reporting Period")){
					if(mapManagementFeeDetails.get("Days Convention") != null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Days Convention", mapManagementFeeDetails.get("Days Convention"));
						if (!bStatus) {
							appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
							bValidStatus = false;
						}
					}
					if(mapManagementFeeDetails.get("Convention Period") != null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Convention Period", mapManagementFeeDetails.get("Convention Period"));
						if (!bStatus) {
							appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
							bValidStatus = false;	
						}
					}
				}
				//Verifying Fee Period
				if(mapManagementFeeDetails.get("No of days for Calculation").equalsIgnoreCase("Fee Period") && mapManagementFeeDetails.get("Fee Period Time Calculation")!=null){
					bStatus = NewUICommonFunctions.verifySelectedRadioButton("Fee Period Time Calculation", mapManagementFeeDetails.get("Fee Period Time Calculation"));
					if(!bStatus){
						appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
						bValidStatus = false;
					}
				}
			}
			if(mapManagementFeeDetails.get("Fee Calculation Method")!=null)
			{
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Fee Calculation Method", mapManagementFeeDetails.get("Fee Calculation Method"));
				if(!bStatus){
					appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
					bValidStatus = false;
				}
				if(mapManagementFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Fixed"))
				{
					if(mapManagementFeeDetails.get("Fee Percentage")!=null){
						String actualValue = Elements.getElementAttribute(Global.driver, By.id("feePercentage"), "value");
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualValue, mapManagementFeeDetails.get("Fee Percentage"));
						if(!bStatus){
							appendMsg = appendMsg+"[ ERROR: Fixed Fee Percentage actual value "+actualValue+"is not matching with the Expected "+mapManagementFeeDetails.get("Fee Percentage")+" ]\n";
							bValidStatus = false;
						}
					}
					if(mapManagementFeeDetails.get("Fixed Spread Percentage")!=null){
						String actualValue = Elements.getElementAttribute(Global.driver, By.id("spreadPercentage"), "value");
						bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualValue, mapManagementFeeDetails.get("Fixed Spread Percentage"));
						if(!bStatus){
							appendMsg = appendMsg+"[ ERROR: Fixed Spread Percentage actual Value is Not matching with the Expected ]\n";
							bValidStatus = false;
						}
					}
				}
				if(mapManagementFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Slab Based") || mapManagementFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Tier Based"))
				{
					if(mapManagementFeeDetails.get("Amount From")!=null && mapManagementFeeDetails.get("Amount To")!=null && mapManagementFeeDetails.get("Percentage")!=null)
					{
						String sAmountFromValues = mapManagementFeeDetails.get("Amount From");
						String sAmountToValues = mapManagementFeeDetails.get("Amount To");
						String sPercentageValues = mapManagementFeeDetails.get("Percentage");
						bStatus = verifyFeeCalculationMethod(sAmountFromValues , sAmountToValues , sPercentageValues);
						if(!bStatus){
							appendMsg = appendMsg+Messages.errorMsg+"\n";
							bValidStatus = false;
						}
					}

				}		
			}	
			Messages.errorMsg = appendMsg+"\n";
			return bValidStatus; 
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyFeeCalculationMethod(String sAmountFromValues,String sAmountToValues, String sPercentageValues) {
		try {
			boolean bValidStatus = true;
			String appendMsg = "";
			List<String> aAmountFromValue = Arrays.asList(sAmountFromValues.split(","));
			List<String> aAmontToValue = Arrays.asList(sAmountToValues.split(","));
			List<String> aPercentageValue = Arrays.asList(sPercentageValues.split(","));
			int count = Math.max(aAmountFromValue.size(), Math.max(aAmontToValue.size(), aPercentageValue.size()));
			for(int i=0; i<count; i++)
			{
				if(aAmountFromValue.get(i)!=null && !aAmountFromValue.get(i).equalsIgnoreCase("None")){
					String actualValue = Elements.getElementAttribute(Global.driver, By.id("feeSlabDetails"+i+".amountFrom"), "value");
					bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualValue, aAmountFromValue.get(i));
					if(!bStatus){
						appendMsg = appendMsg+"[ ERROR: "+aAmountFromValue.get(i)+" value Not Matching with the Actual Slab or Tiered Amount From Value ]\n";
						bValidStatus = false;
					}
				}
				if(aAmontToValue.get(i)!=null && !aAmontToValue.get(i).equalsIgnoreCase("None")){
					String actualValue = Elements.getElementAttribute(Global.driver, By.id("feeSlabDetails"+i+".amountTo"), "value");
					bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualValue, aAmontToValue.get(i));
					//bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='feeSlabDetails"+i+".amountTo' and contains(@value,'"+aAmontToValue.get(i)+"')]"));
					if(!bStatus){
						appendMsg = appendMsg+"[ ERROR: "+aAmontToValue.get(i)+" value Not Matching with the Actual Slab or Tiered Amount To Value ]\n";
						bValidStatus = false;
					}
				}
				if(aPercentageValue.get(i)!=null && !aPercentageValue.get(i).equalsIgnoreCase("None")){
					String actualValue = Elements.getElementAttribute(Global.driver, By.id("feeSlabDetails"+i+".slabPercentage"), "value");
					bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualValue, aPercentageValue.get(i));
					//bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='feeSlabDetails"+i+".slabPercentage' and contains(@value,'"+aPercentageValue.get(i)+"')]"));
					if(!bStatus){
						appendMsg = appendMsg+"[ ERROR: "+aPercentageValue.get(i)+" value Not Matching with the Actual Slab or Tiered Percentage Value ]\n";
						bValidStatus = false;
					}
				}
			}
			Messages.errorMsg = appendMsg+"\n";
			return bValidStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	public static boolean performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect menu , dashboardSubDropdownToSelect subMenu ,String ID,String sFormulaActionType, String sActionType){
		try{

			for (int i = 0; i < 5; i++) {
				bStatus = NewUICommonFunctions.searchValueinMastersAndTransactionTableWithTransactionID(menu,subMenu, ID);
				if(!bStatus){
					Global.driver.navigate().refresh();
					continue;					
				}
				if(bStatus){
					break;
				}				
			}
			if (!bStatus) {
				Messages.errorMsg = " ERROR : The Expected values : "+ID+" is not visible in the serach filter.";
				return false;
			}

			Elements.click(Global.driver, By.xpath("//div[1]/div[1]//a[text()='"+ID+"']"));



			//Performs Action Type for New Formula Created from management fee.
			if (sFormulaActionType != null) {
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//a[contains(text(),'Activate Rule')]"), 10);
				if (!bStatus) {
					Messages.errorMsg = " ERROR : The Formula Approval Link wasn't visible.";
					return false;
				}

				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[contains(text(),'Activate Rule')]"));


				if(sFormulaActionType.equalsIgnoreCase("Approve")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@class,'form-actions')]//button[1]"));
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Unable to click on Management Fee New Formula approve button.";
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ManagementFee.Lables.lblManagementFee, Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Management Fee Tab Wasn't Visible after approving the Formula.";
						return false;
					}
				}
				if(sFormulaActionType.equalsIgnoreCase("Reject")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@class,'form-actions')]//button[3]"));
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Unable to click on Management Fee New Formula Reject button.";
						return false;
					}
					Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
					Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Comments By QA");
					Elements.click(Global.driver, By.xpath("//div[@class='modal-footer']/button[1]"));					
					bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//label[contains(text(),'Checker Rejected')]"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = " ERROR : On Management Fee Tab it Wasn't displayed saying Checker Rejected.";
						return false;
					}
				}

				if(sFormulaActionType.equalsIgnoreCase("Return")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@class,'form-actions')]//button[2]"));
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Unable to click on Management Fee New Formula Return button.";
						return false;
					}
					Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
					Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Comments By QA");
					Elements.click(Global.driver, By.xpath("//div[@class='modal-footer']/button[1]"));
					bStatus = Wait.waitForElementVisibility(Global.driver,By.xpath("//label[contains(text(),'Checker Returned')]"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = " ERROR : On Management Fee Tab it Wasn't displayed saying Checker Returned.";
						return false;
					}
				}

			}	

			if(sActionType == null || sActionType.equalsIgnoreCase("")){

				return true;
			}
			//Performs Action Type for Fee.
			if (sActionType != null && sFormulaActionType!=null && sFormulaActionType.equalsIgnoreCase("Approve")) {
				if(sActionType.equalsIgnoreCase("Approve")){
					NewUICommonFunctions.doCheckerActionType(checkerActionTypes.APPROVE);
					bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Approved Successfully Message not visible";
					}
					return bStatus;
				}
				if(sActionType.equalsIgnoreCase("Reject")){
					NewUICommonFunctions.doCheckerActionType(checkerActionTypes.REJECT);
					Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
					Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Comments By QA");
					Elements.click(Global.driver, By.xpath("//div[@class='modal-footer']/button[3]"));
					bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Rejcted Successfully Message not visible";
					}
					return bStatus;
				}

				if(sActionType.equalsIgnoreCase("Return")){
					NewUICommonFunctions.doCheckerActionType(checkerActionTypes.RETURN);
					Wait.waitForElementVisibility(Global.driver, By.xpath("//textarea[@id='comments']"), Constants.iCommentsBox);
					Elements.enterText(Global.driver, By.xpath("//textarea[@id='comments']"),"Comments By QA");
					Elements.click(Global.driver, By.xpath("//div[@class='modal-footer']/button[1]"));
					bStatus = Wait.waitForElementVisibility(Global.driver,Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Returned Successfully Message not visible";
					}
					return bStatus;
				}
			}		

			return true; 
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*	public static boolean modifyManagementFeeDetails(String sManagementFeeID, String sLegalEntityName, Map<String, String> mapModifyManagementFeeDetails) {
		try {
			Search And Edit the Management Fee to Modify the details.
			Then Do Operations on it.
			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(sManagementFeeID, "Legal Entity Name", sLegalEntityName, "active", 8);
			if (bStatus){
				By checkElement = By.xpath("//td[normalize-space(text())='"+sManagementFeeID+"']//..//following-sibling::td[normalize-space(text())='"+sLegalEntityName+"']//following-sibling::td//a[@data-original-title='Edit']");
				bStatus = CommonAppFunctions.jsClick(checkElement);
				if (!bStatus) {
					Messages.errorMsg = "Management Fee Not Selected from the Search Table";
					return false;
				}
			}
			bStatus = ManagementFeeAppFunctions.createNewManagementFee(mapModifyManagementFeeDetails);
			if (!bStatus) {
				return false;
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/

	public static boolean modifyReturnManagementFeeDetails(Map<String, String> mapModifyManagementFeeDetails) {
		try {
			bStatus = performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapModifyManagementFeeDetails.get("ManagementFeeID"), null, "");
			if(!bStatus){
				return false;
			}
			bStatus = ManagementFeeAppFunctions.createNewManagementFee(mapModifyManagementFeeDetails);
			if (!bStatus) {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bStatus;
	}

	/*	public static boolean verifyManagementFeeDetailsInEditScreen(Map<String, String> createdManagementFeeDetails , Map<String, String> formulaSetupDetails) {
		try {
			//have to add search for Management Fee and click on edit to verify the details.
			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(createdManagementFeeDetails.get("ManagementFeeID"), "Legal Entity Name", createdManagementFeeDetails.get("Legal Entity Name"), "active", 8);
			if (bStatus) {
				By selectManagementFee = By.xpath("//td[normalize-space(text())='"+createdManagementFeeDetails.get("ManagementFeeID").trim()+"']//following-sibling::td[normalize-space(text())='"+createdManagementFeeDetails.get("Legal Entity Name").trim()+"']//following-sibling::td//a[@data-original-title='Edit']");
				bStatus = CommonAppFunctions.jsClick(selectManagementFee);
				if (!bStatus) {
					Messages.errorMsg = "Management Fee Not Selected From Search Table";
					return false;
				}		
				bStatus = doVerifyManagementFeeOnEditScreen(createdManagementFeeDetails , formulaSetupDetails);
				if (!bStatus) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bStatus;
	}*/

	public static boolean verifyReturnedManagementFeeDetails(Map<String,String> createdManagementFeeDetails){
		try{
			bStatus = performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, createdManagementFeeDetails.get("ManagementFeeID"), null, "");
			if(!bStatus){
				return false;
			}	
			bStatus = doVerifyManagementFeeOnEditScreen(createdManagementFeeDetails);
			if (!bStatus) {
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*	public static boolean deactivateManagementFee(Map<String, String> mapManagementFeeSearchDetails) {
		try {
			//search panel function to search the Management fee based on given details.
			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(mapManagementFeeSearchDetails.get("ManagementFeeID"), "Legal Entity Name", mapManagementFeeSearchDetails.get("Legal Entity Name"), "active", 8);
			if (bStatus) {
				By clickElement = By.xpath("//td[normalize-space(text())='"+mapManagementFeeSearchDetails.get("ManagementFeeID").trim()+"']//following-sibling::td[normalize-space(text())='"+mapManagementFeeSearchDetails.get("Legal Entity Name").trim()+"']//following-sibling::td//a[@data-original-title='Deactivate']");
				bStatus = CommonAppFunctions.jsClick(clickElement);
				if (!bStatus) {
					Messages.errorMsg = "Management Fee Not Selected From Search Table";
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, 10);
				if (!bStatus) {
					Messages.errorMsg = " [ ERROR : Successfully updated the Management Fee status message wasn't displayed. ] \n";
					return false;
				}				
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}*/

	/*	public static boolean activateManagementFee(Map<String, String> mapManagementFeeSearchDetails) {
		try {
			//search panel function to search the Management fee based on given details.
			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(mapManagementFeeSearchDetails.get("ManagementFeeID"), "Legal Entity Name", mapManagementFeeSearchDetails.get("Legal Entity Name"), "inactive", 8);
			if (bStatus) {
				By clickElement = By.xpath("//td[normalize-space(text())='"+mapManagementFeeSearchDetails.get("ManagementFeeID").trim()+"']//following-sibling::td[normalize-space(text())='"+mapManagementFeeSearchDetails.get("Legal Entity Name").trim()+"']//following-sibling::td//a[@data-original-title='Activate']");
				bStatus = CommonAppFunctions.jsClick(clickElement);
				if (!bStatus) {
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, 10);
				if (!bStatus) {
					Messages.errorMsg = " [ ERROR : Successfully updated the Management Fee status message wasn't displayed. ] \n";
					return false;
				}				
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}*/

}
