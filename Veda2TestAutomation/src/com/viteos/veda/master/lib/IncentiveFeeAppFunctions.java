package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class IncentiveFeeAppFunctions {
	public static boolean bStatus;

	public static boolean doVerifyIncentiveFeeOnEditScreen(Map<String , String> mapIncentiveFeeDetails){
		boolean bValidateStatus = true;
		try {			
			String appendMsg = "";

			/*
			 * Add Search function to Edit the Incentive Fee Details
			 */

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4//p[contains(text(),'Incentive Fee')]"), 10);
			if(!bStatus){
				Messages.errorMsg = "Incentive Fee Lable is Not Visible";
				return false;
			}
			if(mapIncentiveFeeDetails.get("Client Name") != null)
			{
				bStatus = IncentiveFeeAppFunctions.verifyTextInDropDownForOnChageDependentDDLs("Client Name", mapIncentiveFeeDetails.get("Client Name").trim().toString());
				if(!bStatus){
					appendMsg = appendMsg+" [ "+Messages.errorMsg+" ] \n";
					bValidateStatus = false;
				}
			}
			if(mapIncentiveFeeDetails.get("Fund Family Name") != null){
				bStatus = IncentiveFeeAppFunctions.verifyTextInDropDownForOnChageDependentDDLs("Fund Family Name", mapIncentiveFeeDetails.get("Fund Family Name").trim().toString());
				if(!bStatus){
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ] \n";
					bValidateStatus = false;
				}
			}
			if(mapIncentiveFeeDetails.get("Legal Entity Name") != null){
				bStatus = IncentiveFeeAppFunctions.verifyTextInDropDownForOnChageDependentDDLs("Legal Entity Name", mapIncentiveFeeDetails.get("Legal Entity Name").trim().toString());
				if(!bStatus){
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ] \n";
					bValidateStatus = false;
				}
			}
			if(mapIncentiveFeeDetails.get("Class Name") != null)
			{
				bStatus = verifyTextInDropDownForIncentiveFeeDetails("Class Name", mapIncentiveFeeDetails.get("Class Name").trim().toString());
				if(!bStatus){
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ] \n";
					bValidateStatus = false;
				}
			}
			if(mapIncentiveFeeDetails.get("Incentive Fee On") != null)
			{
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Incentive Fee On", By.xpath("//div[@id='s2id_incentiveFeeOn']//span[contains(@id,'select2-chosen')]"), mapIncentiveFeeDetails.get("Incentive Fee On"), "Yes", false);						
				if(!bStatus){
					appendMsg = appendMsg+Messages.errorMsg;
					bValidateStatus = false;
				}
				if(mapIncentiveFeeDetails.get("Fee Calculation Method") != null && mapIncentiveFeeDetails.get("Incentive Fee On").equalsIgnoreCase("Customised P&L"))
				{
					bStatus = verifyTextInDropDownForIncentiveFeeDetails("Fee Calculation Method", mapIncentiveFeeDetails.get("Fee Calculation Method"));
					if(!bStatus){
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
						bValidateStatus = false;
					}
					if(mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Rule Based"))
					{
						if(mapIncentiveFeeDetails.get("Incentive Fee Rule") != null)
						{
							Map<String, String> mapExistingIncentiveFeeRule = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath, "FormulaTestData", mapIncentiveFeeDetails.get("Incentive Fee Rule").trim().toString());
							bStatus = verifyTextInDropDownForIncentiveFeeDetails("Rule", mapExistingIncentiveFeeRule.get("FeeRuleName").trim().toString());
							if(!bStatus){
								appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
								bValidateStatus = false;
							}
							if(mapExistingIncentiveFeeRule.get("Rule") != null)
							{
								String sExpectedRuleValue = mapExistingIncentiveFeeRule.get("Rule").replaceAll("," , "");
								String sActualRulevalue = Elements.getText(Global.driver, By.id("ruleFormula"));
								if(!sExpectedRuleValue.trim().toString().equalsIgnoreCase(sActualRulevalue.trim().toString())){
									bValidateStatus = false;
									appendMsg = appendMsg+"[ "+sActualRulevalue+" is actual Incentive Fee Rule not equal to Expected: "+sExpectedRuleValue+" ]\n";
								}
							}
						}												
					}
					if(mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Slab Based"))
					{						
						bStatus = verifyFeeCalculationMethod(mapIncentiveFeeDetails);
						if(!bStatus){
							appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
							bValidateStatus = false;
						}											
					}
				}
				if(mapIncentiveFeeDetails.get("Fee Calculation Method")!=null && mapIncentiveFeeDetails.get("Incentive Fee On").equalsIgnoreCase("All P&L"))
				{
					bStatus = verifyTextInDropDownForIncentiveFeeDetails("Fee Calculation Method", mapIncentiveFeeDetails.get("Fee Calculation Method"));
					if(!bStatus){
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
						bValidateStatus = false;
					}
					if(mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Fixed"))
					{
						if(mapIncentiveFeeDetails.get("Incentive Fee Percentage")!=null){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='feePercentage' and contains(@value,'"+mapIncentiveFeeDetails.get("Incentive Fee Percentage")+"')]"));
							if(!bStatus){
								appendMsg = appendMsg+"[ Actual Incentive Fee Percentage is not matching with the Expected Value "+mapIncentiveFeeDetails.get("Incentive Fee Percentage")+" ]\n";
								bValidateStatus = false;
							}
						}
						if(mapIncentiveFeeDetails.get("Spread Percentage")!=null){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='spreadPercentage' and contains(@value,'"+mapIncentiveFeeDetails.get("Spread Percentage")+"')]"));
							if(!bStatus){
								appendMsg = appendMsg+"[ Actual Spread Percentage is not matching with the Expected Value "+mapIncentiveFeeDetails.get("Spread Percentage")+" ]\n";
								bValidateStatus = false;
							}
						}
					}
					if(mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Rule Based"))
					{
						if(mapIncentiveFeeDetails.get("Incentive Fee Rule")!=null)
						{
							bStatus = verifyTextInDropDownForIncentiveFeeDetails("Rule", mapIncentiveFeeDetails.get("Incentive Fee Rule"));
							if(!bStatus){
								appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
								bValidateStatus = false;
							}
							Map<String , String> formulaSetupDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath, "FormulaTestData",mapIncentiveFeeDetails.get("Incentive Fee Rule"));
							if(formulaSetupDetails.get("Rule")!=null)
							{
								String sExpectedRuleValue = formulaSetupDetails.get("Rule").replaceAll("," , "");
								String sActualRulevalue = Elements.getText(Global.driver, By.id("ruleFormula"));
								if(!sExpectedRuleValue.equalsIgnoreCase(sActualRulevalue)){
									bValidateStatus = false;
									appendMsg = appendMsg+"[ "+sActualRulevalue+" is actual Incentive Fee Rule not equal to Expected: "+formulaSetupDetails.get("Rule")+" ]\n";
								}
							}
						}												
					}
					if(mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Slab Based") || mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Tier Based"))
					{
						bStatus = verifyFeeCalculationMethod(mapIncentiveFeeDetails);
						if(!bStatus){
							appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
							bValidateStatus = false;
						}
					}
				}
			}
			if(mapIncentiveFeeDetails.get("Loss Carry Forward Calculation based on")!=null){
				bStatus = verifyTextInDropDownForIncentiveFeeDetails("Loss Carry Forward Calculation based on", mapIncentiveFeeDetails.get("Loss Carry Forward Calculation based on"));
				if(!bStatus){
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
					bValidateStatus = false;
				}					
			}
			if(mapIncentiveFeeDetails.get("Year End c/f Loss Carry Forward Percentage")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Year End c/f Loss Carry Forward Percentage", mapIncentiveFeeDetails.get("Year End c/f Loss Carry Forward Percentage"));
				if(!bStatus){
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
					bValidateStatus = false;
				}					
			}
			if(mapIncentiveFeeDetails.get("Hurdle Rate")!=null){
				//bStatus = NewUICommonFunctions.verifySelectedRadioButton("Hurdle Rate", mapIncentiveFeeDetails.get("Hurdle Rate"));
				String sIsHurdleRate = "";
				sIsHurdleRate = Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='isHurdleRate' and @checked='checked']"), "id");
				if(sIsHurdleRate == null || !sIsHurdleRate.trim().contains(mapIncentiveFeeDetails.get("Hurdle Rate"))){
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
					bValidateStatus = false;
				}
				if(mapIncentiveFeeDetails.get("Hurdle Rate").equalsIgnoreCase("Yes"))
				{
					if(mapIncentiveFeeDetails.get("Days Convention") != null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Days Convention", mapIncentiveFeeDetails.get("Days Convention"));
						if (!bStatus) {
							appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
							bValidateStatus = false;
						}
					}
					if(mapIncentiveFeeDetails.get("Convention Period") != null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Convention Period", mapIncentiveFeeDetails.get("Convention Period"));
						if (!bStatus) {
							appendMsg = appendMsg+"[ ERROR:"+Messages.errorMsg+"]\n";
							bValidateStatus = false;	
						}
					}

					if(mapIncentiveFeeDetails.get("Hurdle Rate Method")!=null)
					{
						bStatus = verifyTextInDropDownForIncentiveFeeDetails("Hurdle Rate Method", mapIncentiveFeeDetails.get("Hurdle Rate Method"));
						if(!bStatus){
							appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
							bValidateStatus = false;
						}
						if(mapIncentiveFeeDetails.get("Hurdle Rate Method").equalsIgnoreCase("Compound") && mapIncentiveFeeDetails.get("Compounding Frequency") != null)
						{
							bStatus = verifyTextInDropDownForIncentiveFeeDetails("Compounding Frequency", mapIncentiveFeeDetails.get("Compounding Frequency"));
							if(!bStatus){
								appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
								bValidateStatus = false;
							}
						}
					}
					if(mapIncentiveFeeDetails.get("Hurdle Rate Method Type")!=null)
					{
						bStatus = verifyTextInDropDownForIncentiveFeeDetails("Hurdle Rate Method Type", mapIncentiveFeeDetails.get("Hurdle Rate Method Type"));
						if(!bStatus){
							appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
							bValidateStatus = false;
						}
					}
					if(mapIncentiveFeeDetails.get("Calculation Method")!=null)
					{
						bStatus = verifyTextInDropDownForIncentiveFeeDetails("Calculation Method", mapIncentiveFeeDetails.get("Calculation Method"));
						if(!bStatus){
							appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
							bValidateStatus = false;
						}
						if(mapIncentiveFeeDetails.get("Calculation Method").equalsIgnoreCase("Fixed"))
						{
							if(mapIncentiveFeeDetails.get("Hurdle Rate Percentage")!=null){
								bStatus = verifyNumericalValuesInTextBox("hurdleRatePercent", mapIncentiveFeeDetails.get("Hurdle Rate Percentage"), "Yes");
								if(!bStatus){
									appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
									bValidateStatus = false;
								}
							}
							if(mapIncentiveFeeDetails.get("Hurdle Rate Fixed And Index Spread Percentage")!=null){
								bStatus = verifyNumericalValuesInTextBox("hurdleSpreadPercentageIndex", mapIncentiveFeeDetails.get("Hurdle Rate Fixed And Index Spread Percentage"), "Yes");
								if(!bStatus){
									appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
									bValidateStatus = false;
								}
							}
						}
						if(mapIncentiveFeeDetails.get("Calculation Method").equalsIgnoreCase("Index"))
						{
							if(mapIncentiveFeeDetails.get("Index Calculation Method")!=null){
								bStatus = verifyTextInDropDownForIncentiveFeeDetails("Index Calculation Method", mapIncentiveFeeDetails.get("Index Calculation Method"));
								if(!bStatus){
									appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
									bValidateStatus = false;
								}
							}
							if(mapIncentiveFeeDetails.get("Index")!=null){
								bStatus = verifyTextInDropDownForIncentiveFeeDetails("Index", mapIncentiveFeeDetails.get("Index"));
								if(!bStatus){
									appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
									bValidateStatus = false;
								}
							}
							if(mapIncentiveFeeDetails.get("Hurdle Rate Fixed And Index Spread Percentage")!=null){
								bStatus = verifyNumericalValuesInTextBox("hurdleSpreadPercentageIndex", mapIncentiveFeeDetails.get("Hurdle Rate Fixed And Index Spread Percentage"), "Yes");
								if(!bStatus){
									appendMsg = appendMsg+"[ "+Messages.errorMsg+" ] \n";
									bValidateStatus = false;
								}
							}
						}
						if(mapIncentiveFeeDetails.get("Calculation Method").equalsIgnoreCase("Variable"))
						{							
							bStatus = verifyVariableHurdleChargesCalculationDetails(mapIncentiveFeeDetails);
							if(!bStatus){
								appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
								bValidateStatus = false;
							}
						}
					}
					if(mapIncentiveFeeDetails.get("Rule")!=null)
					{
						String sExpectedRuleValue = mapIncentiveFeeDetails.get("Rule").trim().toString().replaceAll("," , "");
						String sActualRulevalue = Elements.getText(Global.driver, By.name("hurdleAmountRuleView"));
						if(!sExpectedRuleValue.equalsIgnoreCase(sActualRulevalue.trim().toString())){							
							appendMsg = appendMsg+"[ "+sActualRulevalue+" is actual Incentive Fee Rule not equal to Expected: "+mapIncentiveFeeDetails.get("Rule")+" ]\n";
							bValidateStatus = false;
						}
					}
				}
			}
			if(mapIncentiveFeeDetails.get("Effective Start Date")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Effective Start Date", mapIncentiveFeeDetails.get("Effective Start Date"));
				if(!bStatus){
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
					bValidateStatus = false;
				}
			}
			if(mapIncentiveFeeDetails.get("Payment Frequency")!=null){
				bStatus = verifyTextInDropDownForIncentiveFeeDetails("Payment Frequency", mapIncentiveFeeDetails.get("Payment Frequency"));
				if(!bStatus){
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
					bValidateStatus = false;
				}
			}	
			Messages.errorMsg = appendMsg;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

	public static boolean verifyVariableHurdleChargesCalculationDetails(Map<String, String> mapIncentiveFeeDetails) {
		boolean bValidateStatus = true;
		String sAppendMessage = "";
		try {
			if (mapIncentiveFeeDetails.get("Variable Type") != null){
				if(mapIncentiveFeeDetails.get("Variable Type").equalsIgnoreCase("Based On Capital") && mapIncentiveFeeDetails.get("Based On Capital Type") != null) {
					bStatus = verifyTextInDropDownForIncentiveFeeDetails("Based On Capital Type", mapIncentiveFeeDetails.get("Based On Capital Type").trim().toString());
					if (!bStatus) {
						sAppendMessage = sAppendMessage + " [ "+Messages.errorMsg+" ] \n";
						bValidateStatus = false;
					}
				}				
				String sStartDate, sAmountFrom, sAmountTo, sHurdlePercent, sHurdleSpreadPercent;
				List<String> aStartDate = null, aAmountFrom = null, aAmountTo = null, aHurdlePercent = null, aHurdleSpreadPercent = null;

				if (mapIncentiveFeeDetails.get("Start Date") != null) {
					sStartDate = mapIncentiveFeeDetails.get("Start Date");
					aStartDate = Arrays.asList(sStartDate.split(","));
				}
				if (mapIncentiveFeeDetails.get("Amount From") != null) {
					sAmountFrom = mapIncentiveFeeDetails.get("Amount From");
					aAmountFrom = Arrays.asList(sAmountFrom.split(","));
				}
				if (mapIncentiveFeeDetails.get("Amount To") != null) {
					sAmountTo = mapIncentiveFeeDetails.get("Amount To");
					aAmountTo = Arrays.asList(sAmountTo.split(","));
				}
				if (mapIncentiveFeeDetails.get("Hurdle Variable Percentage") != null) {
					sHurdlePercent = mapIncentiveFeeDetails.get("Hurdle Variable Percentage");
					aHurdlePercent = Arrays.asList(sHurdlePercent.split(","));
				}
				if (mapIncentiveFeeDetails.get("Hurdle Variable Spread Percentage") != null) {
					sHurdleSpreadPercent = mapIncentiveFeeDetails.get("Hurdle Variable Spread Percentage");
					aHurdleSpreadPercent = Arrays.asList(sHurdleSpreadPercent.split(","));
				}

				for(int i=0;i<aHurdlePercent.size();i++){
					if (mapIncentiveFeeDetails.get("Variable Type").equalsIgnoreCase("Periodic") && mapIncentiveFeeDetails.get("Start Date") != null && !aStartDate.get(i).trim().toString().equalsIgnoreCase("None")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='feeHurdleRates"+i+".toDate' and contains(@value,'"+aStartDate.get(i).trim().toString()+"')]"));
						if (!bStatus) 
						{
							sAppendMessage = sAppendMessage + " [ ERROR : 'Start Date' field : feeHurdleRates"+i+".toDate , for variable Type 'Periodic' with value : "+aStartDate.get(i).trim().toString()+" wasn't visible. ] \n";
							bValidateStatus = false;							
						}						
					}
					if(mapIncentiveFeeDetails.get("Variable Type").equalsIgnoreCase("Based On Capital") && mapIncentiveFeeDetails.get("Amount From") != null && !aAmountFrom.get(i).trim().toString().equalsIgnoreCase("None")){							
						bStatus = verifyNumericalValuesInTextBox("feeHurdleRates"+i+".amountFrom", aAmountFrom.get(i).trim().toString(), "Yes");
						if (!bStatus) 
						{
							sAppendMessage = Messages.errorMsg + " [ ERROR : 'Amount From' text field : feeHurdleRates"+i+".amountFrom , for variable Type 'Based On Capital' with value : "+aAmountFrom.get(i).trim().toString()+" wasn't visible. ] \n";
							bValidateStatus = false;							
						}
					}
					if(mapIncentiveFeeDetails.get("Variable Type").equalsIgnoreCase("Based On Capital") && mapIncentiveFeeDetails.get("Amount To") != null && !aAmountTo.get(i).trim().toString().equalsIgnoreCase("None")){

						bStatus = verifyNumericalValuesInTextBox("feeHurdleRates"+i+".amountTo", aAmountTo.get(i).trim().toString(), "Yes");
						if (!bStatus) 
						{
							sAppendMessage = Messages.errorMsg  + " [ ERROR : 'Amount To' text field : feeHurdleRates"+i+".amountTo , for variable Type 'Based On Capital' with value : "+aAmountTo.get(i).trim().toString()+" wasn't visible. ] \n";
							bValidateStatus = false;							
						}
					}
					if(mapIncentiveFeeDetails.get("Hurdle Variable Percentage") != null && !aHurdlePercent.get(i).trim().toString().equalsIgnoreCase("None")){

						bStatus = verifyNumericalValuesInTextBox("feeHurdleRates"+i+".hurdlePercentage", aHurdlePercent.get(i).trim().toString(), "Yes");
						if (!bStatus) 
						{
							sAppendMessage = Messages.errorMsg  + " [ ERROR : Hurdle Variable Percentage field : feeHurdleRates"+i+".hurdlePercentage , wit value : "+aHurdlePercent.get(i).trim().toString()+" wasn't visible. ] \n";
							bValidateStatus = false;							
						}	
					}	
					if(mapIncentiveFeeDetails.get("Hurdle Variable Spread Percentage") != null && !aHurdleSpreadPercent.get(i).trim().toString().equalsIgnoreCase("None")){

						bStatus = verifyNumericalValuesInTextBox("feeHurdleRates"+i+".hurdleSpreadPercentage", aHurdleSpreadPercent.get(i).trim().toString(), "Yes");
						if (!bStatus) 
						{
							sAppendMessage = Messages.errorMsg  + " [ ERROR : Hurdle Variable Spread Percentage field : feeHurdleRates"+i+".hurdleSpreadPercentage , with value : "+aHurdleSpreadPercent.get(i).trim().toString()+" wasn't visible. ] \n";
							bValidateStatus = false;							
						}	
					}						
				}					
			}	
			Messages.errorMsg = sAppendMessage;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

	private static boolean verifyFeeCalculationMethod(Map<String, String> mapIncentiveFeeDetails) {
		boolean bValidateStatus = true;
		String sAppendMessage = "";
		try {			
			String sAmountFrom, sAmountTo, sPercentage, sSpreadPercentage;
			List<String> aAmountFrom = null, aAmountTo = null, aPercentage = null, aSpreadPercentage = null;
			if (mapIncentiveFeeDetails.get("Slab Or Tier Amount From") != null) {
				sAmountFrom = mapIncentiveFeeDetails.get("Slab Or Tier Amount From");
				aAmountFrom = Arrays.asList(sAmountFrom.split(","));
			}
			if (mapIncentiveFeeDetails.get("Slab Or Tier Amount To") != null) {
				sAmountTo = mapIncentiveFeeDetails.get("Slab Or Tier Amount To");
				aAmountTo = Arrays.asList(sAmountTo.split(","));
			}
			if (mapIncentiveFeeDetails.get("Slab Or Tier Percentage") != null) {
				sPercentage = mapIncentiveFeeDetails.get("Slab Or Tier Percentage");
				aPercentage = Arrays.asList(sPercentage.split(","));
			}
			if (mapIncentiveFeeDetails.get("Slab Or Tier Spread Percentage") != null) {
				sSpreadPercentage = mapIncentiveFeeDetails.get("Slab Or Tier Spread Percentage");
				aSpreadPercentage = Arrays.asList(sSpreadPercentage.split(","));
			}
			if (mapIncentiveFeeDetails.get("Slab Or Tier Amount From") != null) {				
				for(int i=0;i<aAmountFrom.size();i++){
					if(!aAmountFrom.get(i).equalsIgnoreCase("None")){
						bStatus = verifyNumericalValuesInTextBox("feeSlabDetails"+i+".amountFrom",aAmountFrom.get(i).toString().trim(), "Yes");
						if (!bStatus){
							sAppendMessage = sAppendMessage + " [ "+Messages.errorMsg+" ] \n";
							bValidateStatus = false;
						}
					}
					if (mapIncentiveFeeDetails.get("Slab Or Tier Amount To") != null) {
						if(!aAmountTo.get(i).equalsIgnoreCase("None")){
							bStatus = verifyNumericalValuesInTextBox("feeSlabDetails"+i+".amountTo",aAmountTo.get(i).toString().trim(), "Yes");
							if (!bStatus){
								sAppendMessage = sAppendMessage + " [ "+Messages.errorMsg+" ] \n";
								bValidateStatus = false;					
							}
						}
					}
					if (mapIncentiveFeeDetails.get("Slab Or Tier Percentage") != null) {
						if(!aPercentage.get(i).equalsIgnoreCase("None")){
							bStatus = verifyNumericalValuesInTextBox("feeSlabDetails"+i+".slabPercentage", aPercentage.get(i).toString().trim(), "Yes");
							if (!bStatus){
								sAppendMessage = sAppendMessage + " [ "+Messages.errorMsg+" ] \n";
								bValidateStatus = false;							
							}
						}
					}
					if (mapIncentiveFeeDetails.get("Slab Or Tier Spread Percentage") != null) {
						if(!aSpreadPercentage.get(i).equalsIgnoreCase("None")){
							bStatus = verifyNumericalValuesInTextBox("feeSlabDetails"+i+".slabSpreadPercentage", aSpreadPercentage.get(i).toString().trim(), "Yes");
							if (!bStatus) {
								sAppendMessage = sAppendMessage + " [ "+Messages.errorMsg+" ] \n";
								bValidateStatus = false;							
							}
						}	
					}
				}
			}
			Messages.errorMsg = sAppendMessage;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

	public static boolean createNewIncentiveFee(Map<String, String> mapIncentiveFeeDetails){
		try {			
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4//p[contains(text(),'Incentive Fee')]"), 10);
			NewUICommonFunctions.performJavascriptExecutorToScrollOnToElement(By.xpath("//h4//p[contains(text(),'Incentive Fee')]"));
			if(!bStatus){
				Messages.errorMsg = " ERROR : Incentive Fee Lable is Not Visible";
				return false;
			}
			if(mapIncentiveFeeDetails.get("Client Name")!=null)
			{
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", mapIncentiveFeeDetails.get("Client Name"));
				if(!bStatus){					
					return false;
				}
			}
			if(mapIncentiveFeeDetails.get("Fund Family Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", mapIncentiveFeeDetails.get("Fund Family Name"));
				if(!bStatus){					
					return false;
				}
				TimeUnit.SECONDS.sleep(2);
			}
			if(mapIncentiveFeeDetails.get("Legal Entity Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name", mapIncentiveFeeDetails.get("Legal Entity Name"));
				if(!bStatus){
					return false;
				}
				TimeUnit.SECONDS.sleep(3);
			}
			if(mapIncentiveFeeDetails.get("Class Name")!=null)
			{
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Class Name", mapIncentiveFeeDetails.get("Class Name"));
				if(!bStatus){
					return false;
				}
			}
			if(mapIncentiveFeeDetails.get("Incentive Fee On")!=null)
			{
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Incentive Fee On", mapIncentiveFeeDetails.get("Incentive Fee On"));
				if(!bStatus){
					return false;
				}
				if(mapIncentiveFeeDetails.get("Fee Calculation Method")!= null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fee Calculation Method", mapIncentiveFeeDetails.get("Fee Calculation Method"));
					if(!bStatus){
						return false;
					}
				}
				if( mapIncentiveFeeDetails.get("Incentive Fee On").equalsIgnoreCase("Customised P&L"))				
				{
					if(mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Rule Based"))
					{
						if(mapIncentiveFeeDetails.get("Incentive Fee Rule")!=null)
						{
							Map<String, String> mapExistingFormulaDetails = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath, "FormulaTestData", mapIncentiveFeeDetails.get("Incentive Fee Rule"));
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Rule", mapExistingFormulaDetails.get("FeeRuleName"));
							if(!bStatus){								
								return false;
							}							
						}												
					}
					if(mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Slab Based"))
					{
						bStatus = addIncentiveFeeSlabChargingDetails(mapIncentiveFeeDetails);
						if(!bStatus){
							return false;
						}
					}					
				}

				if(mapIncentiveFeeDetails.get("Fee Calculation Method")!=null && mapIncentiveFeeDetails.get("Incentive Fee On").equalsIgnoreCase("All P&L"))
				{
					if (mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Fixed")) {
						if(mapIncentiveFeeDetails.get("Incentive Fee Percentage") != null){
							bStatus = Elements.enterText(Global.driver, By.id("feePercentage"), mapIncentiveFeeDetails.get("Incentive Fee Percentage"));
							if (!bStatus) {
								return false;
							}

							if(mapIncentiveFeeDetails.get("Fixed Spread Percentage") != null){
								bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='spreadPercentage']"), mapIncentiveFeeDetails.get("Fixed Spread Percentage"));
								if (!bStatus) {
									return false;
								}						
							}
						}
					}
					if(mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Rule Based"))
					{
						if(mapIncentiveFeeDetails.get("Incentive Fee Rule")!=null)
						{
							Map<String, String> mapExistingIncentiveFeeRule = ExcelUtils.readDataABasedOnCellName(Global.sFormulaSetupTestDataFilePath, "FormulaTestData", mapIncentiveFeeDetails.get("Incentive Fee Rule").trim().toString());
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Rule", mapExistingIncentiveFeeRule.get("FeeRuleName"));
							if(!bStatus){								
								return false;
							}							
						}												
					}
					if(mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Slab Based") || mapIncentiveFeeDetails.get("Fee Calculation Method").equalsIgnoreCase("Tier Based"))
					{
						bStatus = addIncentiveFeeSlabChargingDetails(mapIncentiveFeeDetails);
						if(!bStatus){
							return false;
						}
					}					
				}

				if(mapIncentiveFeeDetails.get("Loss Carry Forward Calculation based on") != null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Loss Carry Forward Calculation based on", mapIncentiveFeeDetails.get("Loss Carry Forward Calculation based on"));
					if(!bStatus){
						return false;
					}					
				}
				if(mapIncentiveFeeDetails.get("Year End c/f Loss Carry Forward Percentage") != null){
					bStatus = Elements.enterText(Global.driver, By.id("yearEndCfLcfPer"), mapIncentiveFeeDetails.get("Year End c/f Loss Carry Forward Percentage").trim());
					if(!bStatus){
						return false;
					}					
				}

				if(mapIncentiveFeeDetails.get("Hurdle Rate") != null){
					if(mapIncentiveFeeDetails.get("Hurdle Rate").equalsIgnoreCase("Yes"))
					{
						bStatus = Elements.click(Global.driver, By.id("rdIsHurdleRateYes"));
						if (!bStatus) {
							return false;
						}
					}
					if(mapIncentiveFeeDetails.get("Hurdle Rate").equalsIgnoreCase("No")){
						bStatus = Elements.click(Global.driver, By.id("rdIsHurdleRateNo"));
						if (!bStatus) {
							return false;
						}
					}
					if(mapIncentiveFeeDetails.get("Hurdle Rate").equalsIgnoreCase("Yes"))
					{	

						//Days convention drop down
						if(mapIncentiveFeeDetails.get("Days Convention") != null){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Days Convention", mapIncentiveFeeDetails.get("Days Convention"));
							if (!bStatus) {
								Messages.errorMsg = " ERROR : Unable to select the 'Days Convention' : "+mapIncentiveFeeDetails.get("Days Convention") +"for given Reorting Period";
								return false;
							}
							//Select Convention Period
							if(mapIncentiveFeeDetails.get("Convention Period") != null){
								if(mapIncentiveFeeDetails.get("Days Convention").equalsIgnoreCase("Days")){
									bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Convention Period", mapIncentiveFeeDetails.get("Convention Period"));
									if (!bStatus) {
										Messages.errorMsg = " ERROR : Unable to select the 'Convention Period' : "+mapIncentiveFeeDetails.get("Convention Period") +"for given Hurdle Rate";
										return false;
									}
								}
								if(mapIncentiveFeeDetails.get("Days Convention").equalsIgnoreCase("Months")){
									bStatus = NewUICommonFunctions.verifyTextInDropDown("Convention Period", mapIncentiveFeeDetails.get("Convention Period"));
									if(!bStatus){
										Messages.errorMsg = "[ ERROR:"+Messages.errorMsg+"]\n";
										return false;
									}
								}
							}
						}	
						if(mapIncentiveFeeDetails.get("Hurdle Rate Method") != null)
						{
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Hurdle Rate Method", mapIncentiveFeeDetails.get("Hurdle Rate Method"));
							if(!bStatus){
								return false;
							}
							if(mapIncentiveFeeDetails.get("Hurdle Rate Method").equalsIgnoreCase("Compound") && mapIncentiveFeeDetails.get("Compounding Frequency")!=null)
							{
								bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Compounding Frequency", mapIncentiveFeeDetails.get("Compounding Frequency"));
								if(!bStatus){
									return false;
								}
							}
						}
						if(mapIncentiveFeeDetails.get("Hurdle Rate Method Type")!=null)
						{
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Hurdle Rate Method Type", mapIncentiveFeeDetails.get("Hurdle Rate Method Type"));
							if(!bStatus){
								return false;
							}
						}
						if(mapIncentiveFeeDetails.get("Calculation Method")!=null)
						{
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Calculation Method", mapIncentiveFeeDetails.get("Calculation Method"));
							if(!bStatus){
								return false;
							}
							if(mapIncentiveFeeDetails.get("Calculation Method").equalsIgnoreCase("Fixed") || mapIncentiveFeeDetails.get("Calculation Method").equalsIgnoreCase("Index")){
								if(mapIncentiveFeeDetails.get("Hurdle Rate Fixed And Index Spread Percentage") != null){
									bStatus = Elements.enterText(Global.driver, By.id("hurdleSpreadPercentageIndex"), mapIncentiveFeeDetails.get("Hurdle Rate Fixed And Index Spread Percentage"));
									if(!bStatus){
										return false;
									}
								}
							}

							if(mapIncentiveFeeDetails.get("Calculation Method").equalsIgnoreCase("Fixed"))
							{
								if(mapIncentiveFeeDetails.get("Hurdle Rate Percentage")!=null){
									bStatus = Elements.enterText(Global.driver, By.id("hurdleRatePercent"), mapIncentiveFeeDetails.get("Hurdle Rate Percentage"));
									if(!bStatus){
										return false;
									}
								}							
							}
							if(mapIncentiveFeeDetails.get("Calculation Method").equalsIgnoreCase("Index"))
							{
								if(mapIncentiveFeeDetails.get("Index Calculation Method") != null){
									bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Index Calculation Method", mapIncentiveFeeDetails.get("Index Calculation Method"));
									if(!bStatus){
										return false;
									}
								}
								if(mapIncentiveFeeDetails.get("Index") != null){
									bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Index", mapIncentiveFeeDetails.get("Index"));
									if(!bStatus){
										return false;
									}
								}
							}
							if(mapIncentiveFeeDetails.get("Calculation Method").equalsIgnoreCase("Variable"))
							{						
								bStatus = addVariableHurdleChargesCalculationDetails(mapIncentiveFeeDetails);
								if(!bStatus){
									return false;
								}
							}
						}
						NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),' Hurdle Rate')]"));
						if (mapIncentiveFeeDetails.get("Rule") != null) {	
							NewUICommonFunctions.performJavascriptExecutorToScrollOnToElement(By.xpath("//button[contains(@onclick,'callClear')]"));
							NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'callClear')]"));
							bStatus = NewUICommonFunctions.createNewRule(mapIncentiveFeeDetails.get("Rule").trim().toString());
							if (!bStatus) {
								Messages.errorMsg = " ERROR : Unable To create a new formula with the data : "+mapIncentiveFeeDetails.get("Rule");
								return false;
							}
						}
					}
				}			
				if(mapIncentiveFeeDetails.get("Effective Start Date")!=null){
					NewUICommonFunctions.performJavascriptExecutorToScrollOnToElement(By.id("effectiveStartDate"));
					bStatus = Elements.enterText(Global.driver, By.id("effectiveStartDate"), mapIncentiveFeeDetails.get("Effective Start Date"));
					if(!bStatus){
						Messages.errorMsg = " ERROR : Wasn't able to input Effective Start Date ";
						return false;
					}

					//To make calendar to disappear.
					NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));				
				}
				if(mapIncentiveFeeDetails.get("Payment Frequency")!=null){
					NewUICommonFunctions.performJavascriptExecutorToScrollOnToElement(By.id("s2id_fkPaymentFrequencyId"));				
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Payment Frequency", mapIncentiveFeeDetails.get("Payment Frequency"));
					if(!bStatus){
						return false;
					}
				}
				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Management Fee Master", mapIncentiveFeeDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}	
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static boolean addVariableHurdleChargesCalculationDetails(Map<String, String> mapIncentiveFeeDetails) {
		try {
			if (mapIncentiveFeeDetails.get("Variable Type") != null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Variable Type", mapIncentiveFeeDetails.get("Variable Type"));
				if (!bStatus) {
					return false;
				}
				if(mapIncentiveFeeDetails.get("Variable Type").equalsIgnoreCase("Based On Capital") && mapIncentiveFeeDetails.get("Based On Capital Type") != null) {

					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Based On Capital Type", mapIncentiveFeeDetails.get("Based On Capital Type"));
					if (!bStatus) {
						return false;
					}
				}
				int removeButtonsCount = 0;
				removeButtonsCount = Elements.getXpathCount(Global.driver, By.xpath("//button[@id='removeHurdleChargesList']"));
				NewUICommonFunctions.performJavascriptExecutorToScrollOnToElement(By.id("txtFormula"));				
				for (int i = removeButtonsCount; i >= 1; i--) {
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'removeHurdleCharges') and contains(@onclick,'"+i+"')]"));
					if (!bStatus) {
						Messages.errorMsg = " ERROR : Unable to click on Remove buttons for Hurdle rate details";
					}
				}
				String sStartDate, sAmountFrom, sAmountTo, sHurdlePercent, sHurdleSpreadPercent;
				List<String> aStartDate = null, aAmountFrom = null, aAmountTo = null, aHurdlePercent = null, aHurdleSpreadPercent = null;

				if (mapIncentiveFeeDetails.get("Start Date") != null) {
					sStartDate = mapIncentiveFeeDetails.get("Start Date");
					aStartDate = Arrays.asList(sStartDate.split(","));
				}
				if (mapIncentiveFeeDetails.get("Amount From") != null) {
					sAmountFrom = mapIncentiveFeeDetails.get("Amount From");
					aAmountFrom = Arrays.asList(sAmountFrom.split(","));
				}
				if (mapIncentiveFeeDetails.get("Amount To") != null) {
					sAmountTo = mapIncentiveFeeDetails.get("Amount To");
					aAmountTo = Arrays.asList(sAmountTo.split(","));
				}
				if (mapIncentiveFeeDetails.get("Hurdle Variable Percentage") != null) {
					sHurdlePercent = mapIncentiveFeeDetails.get("Hurdle Variable Percentage");
					aHurdlePercent = Arrays.asList(sHurdlePercent.split(","));
				}
				if (mapIncentiveFeeDetails.get("Hurdle Variable Spread Percentage") != null) {
					sHurdleSpreadPercent = mapIncentiveFeeDetails.get("Hurdle Variable Spread Percentage");
					aHurdleSpreadPercent = Arrays.asList(sHurdleSpreadPercent.split(","));
				}
				if(mapIncentiveFeeDetails.get("Hurdle Variable Percentage") != null){
					for(int i=0;i<aHurdlePercent.size();i++){
						if (mapIncentiveFeeDetails.get("Variable Type").equalsIgnoreCase("Periodic") && mapIncentiveFeeDetails.get("Start Date") != null && !aStartDate.get(i).trim().toString().equalsIgnoreCase("None")) {
							bStatus = Elements.enterText(Global.driver, By.id("feeHurdleRates"+i+".toDate"), aStartDate.get(i).trim().toString());
							if (!bStatus) 
							{
								Messages.errorMsg = " ERROR : 'Start Date' field : feeHurdleRates"+i+".toDate , for variable Type 'Periodic' wasn't visible.";
								return false;							
							}
							//To make calendar to disappear.
							NewUICommonFunctions.jsClick(By.xpath("//b[text()='Amount Calculated On']"));
						}
						if(mapIncentiveFeeDetails.get("Variable Type").equalsIgnoreCase("Based On Capital") && mapIncentiveFeeDetails.get("Amount From") != null && !aAmountFrom.get(i).trim().toString().equalsIgnoreCase("None")){							
							bStatus = Elements.enterText(Global.driver, By.id("feeHurdleRates"+i+".amountFrom"), aAmountFrom.get(i).trim().toString());
							if (!bStatus) 
							{
								Messages.errorMsg = " ERROR : 'Amount From' text field : feeHurdleRates"+i+".amountFrom , for variable Type 'Based On Capital' wasn't visible.";
								return false;							
							}
						}
						if(mapIncentiveFeeDetails.get("Variable Type").equalsIgnoreCase("Based On Capital") && mapIncentiveFeeDetails.get("Amount To") != null && !aAmountTo.get(i).trim().toString().equalsIgnoreCase("None")){

							bStatus = Elements.enterText(Global.driver, By.id("feeHurdleRates"+i+".amountTo"), aAmountTo.get(i).trim().toString());
							if (!bStatus) 
							{
								Messages.errorMsg = " ERROR : 'Amount To' text field : feeHurdleRates"+i+".amountTo , for variable Type 'Based On Capital' wasn't visible.";
								return false;							
							}
						}
						if(mapIncentiveFeeDetails.get("Hurdle Variable Percentage") != null && !aHurdlePercent.get(i).trim().toString().equalsIgnoreCase("None")){

							bStatus = Elements.enterText(Global.driver, By.id("feeHurdleRates"+i+".hurdlePercentage"), aHurdlePercent.get(i).trim().toString());
							if (!bStatus) 
							{
								Messages.errorMsg = " ERROR : Hurdle Variable Percentage field : feeHurdleRates"+i+".hurdlePercentage , wasn't visible.";
								return false;							
							}	
						}	
						if(mapIncentiveFeeDetails.get("Hurdle Variable Spread Percentage") != null && !aHurdleSpreadPercent.get(i).trim().toString().equalsIgnoreCase("None")){

							bStatus = Elements.enterText(Global.driver, By.id("feeHurdleRates"+i+".hurdleSpreadPercentage"), aHurdleSpreadPercent.get(i).trim().toString());
							if (!bStatus) 
							{
								Messages.errorMsg = " ERROR : Hurdle Variable Spread Percentage field : feeHurdleRates"+i+".hurdleSpreadPercentage , wasn't visible.";
								return false;							
							}	
						}	
						if(i<aHurdlePercent.size()-1){
							//Thread.sleep(3000);
							//bStatus = CommonAppFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@style,'display: block')]//button[contains(@onclick,'addHurdleCharges')]"));
							bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'addHurdleCharges')]"));
							if(!bStatus){
								Messages.errorMsg = " ERROR : Add More Button wasn't Clicked for Hurdle rate details.";
								return false;
							}					
						}
					}
				}					
			}			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean addIncentiveFeeSlabChargingDetails(Map<String,String> mapDetails){
		try {
			int additionalSlabs = 0;
			additionalSlabs = Elements.getXpathCount(Global.driver, By.id("removeSlabDetailsList"));
			for (int i = additionalSlabs; i >= 1; i--) {
				NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'removeSlabDetails') and contains(@onclick,'"+i+"')]"));
			}
			String sAmountFrom, sAmountTo, sPercentage, sSpreadPercentage;
			List<String> aAmountFrom = null, aAmountTo = null, aPercentage = null, aSpreadPercentage = null;
			if (mapDetails.get("Slab Or Tier Amount From") != null) {
				sAmountFrom = mapDetails.get("Slab Or Tier Amount From");
				aAmountFrom = Arrays.asList(sAmountFrom.split(","));
			}
			if (mapDetails.get("Slab Or Tier Amount To") != null) {
				sAmountTo = mapDetails.get("Slab Or Tier Amount To");
				aAmountTo = Arrays.asList(sAmountTo.split(","));
			}
			if (mapDetails.get("Slab Or Tier Percentage") != null) {
				sPercentage = mapDetails.get("Slab Or Tier Percentage");
				aPercentage = Arrays.asList(sPercentage.split(","));
			}
			if (mapDetails.get("Slab Or Tier Spread Percentage") != null) {
				sSpreadPercentage = mapDetails.get("Slab Or Tier Spread Percentage");
				aSpreadPercentage = Arrays.asList(sSpreadPercentage.split(","));
			}
			if (mapDetails.get("Slab Or Tier Amount From") != null) {				
				for(int i=0;i<aAmountFrom.size();i++){
					if(!aAmountFrom.get(i).equalsIgnoreCase("None")){						
						bStatus = Elements.enterText(Global.driver, By.id("feeSlabDetails"+i+".amountFrom"), aAmountFrom.get(i).toString().trim());
						if (!bStatus){
							return false;							
						}
					}
					if (mapDetails.get("Slab Or Tier Amount To") != null) {
						if(!aAmountTo.get(i).equalsIgnoreCase("None")){
							bStatus = Elements.enterText(Global.driver, By.id("feeSlabDetails"+i+".amountTo"), aAmountTo.get(i).toString().trim());
							if (!bStatus){
								return false;							
							}
						}
					}
					if (mapDetails.get("Slab Or Tier Percentage") != null) {
						if(!aPercentage.get(i).equalsIgnoreCase("None")){
							bStatus = Elements.enterText(Global.driver, By.id("feeSlabDetails"+i+".slabPercentage"), aPercentage.get(i).toString().trim());
							if (!bStatus){
								return false;							
							}
						}
					}
					if (mapDetails.get("Slab Or Tier Spread Percentage") != null) {
						if(!aSpreadPercentage.get(i).equalsIgnoreCase("None")){
							bStatus = Elements.enterText(Global.driver, By.id("feeSlabDetails"+i+".slabSpreadPercentage"), aSpreadPercentage.get(i).toString().trim());
							if (!bStatus) {
								return false;							
							}
						}	
					}
					if(i<aAmountFrom.size()-1){
						bStatus = Elements.click(Global.driver, By.id("addSlabDetailsList"));
						if(!bStatus){
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*public static boolean modifyIncentiveFeeDetails(Map<String, String> mapDetialsToModify) {
		try {
			Search And Edit the Management Fee to Modify the details.
			Then Do Operations on it.
			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(mapDetialsToModify.get("IncentiveFeeID"), "Legal Entity Name", mapDetialsToModify.get("Legal Entity Name"), "active", 8);
			if (bStatus) {
				NewUICommonFunctions.performJavascriptExecutorToScrollOnToElement(By.xpath("//input[@id='"+mapDetialsToModify.get("IncentiveFeeID").trim()+"']//..//following-sibling::td[normalize-space(text())='"+mapDetialsToModify.get("Legal Entity Name").trim()+"']//following-sibling::td//a[@data-original-title='Edit']"));
				bStatus = CommonAppFunctions.spinnerClick(Global.driver, By.xpath("//input[@id='"+mapDetialsToModify.get("IncentiveFeeID").trim()+"']//..//following-sibling::td[normalize-space(text())='"+mapDetialsToModify.get("Legal Entity Name").trim()+"']//following-sibling::td//a[@data-original-title='Edit']"));
				if (!bStatus) {
					return false;
				}
			}
			bStatus = IncentiveFeeAppFunctions.createNewIncentiveFee(mapDetialsToModify);
			if (!bStatus) {
				return false;
			}
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/

	public static boolean modifyReturnIncentiveFeeDetails(Map<String, String> mapDetialsToModify) {
		try {
			bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapDetialsToModify.get("IncentiveFeeID"), "");					
			if(!bStatus){
				return false;
			}
			bStatus = IncentiveFeeAppFunctions.createNewIncentiveFee(mapDetialsToModify);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bStatus;
	}

	/*public static boolean verifyIncentiveFeeOnEditScreen(Map<String, String> createdIncentiveFeeDetails) {
		try {
			boolean bValidateStatus;
			String sAppendMessage = "";
			// search for Incentive Fee and click on edit to verify the details.
			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(createdIncentiveFeeDetails.get("IncentiveFeeID"), "Legal Entity Name", createdIncentiveFeeDetails.get("Legal Entity Name"), "active", 10);
			if (bStatus) {
				NewUICommonFunctions.performJavascriptExecutorToScrollOnToElement(By.xpath("//input[@id='"+createdIncentiveFeeDetails.get("IncentiveFeeID").trim()+"']//..//following-sibling::td[normalize-space(text())='"+createdIncentiveFeeDetails.get("Legal Entity Name").trim()+"']//following-sibling::td//a[@data-original-title='Edit']"));
				bStatus = CommonAppFunctions.spinnerClick(Global.driver, By.xpath("//input[@id='"+createdIncentiveFeeDetails.get("IncentiveFeeID").trim()+"']//..//following-sibling::td[normalize-space(text())='"+createdIncentiveFeeDetails.get("Legal Entity Name").trim()+"']//following-sibling::td//a[@data-original-title='Edit']"));
				if (!bStatus) {
					return false;
				}
				bStatus = doVerifyIncentiveFeeOnEditScreen(createdIncentiveFeeDetails);
				bValidateStatus = bStatus;
				sAppendMessage = Messages.errorMsg;
				NewUICommonFunctions.performJavascriptExecutorToScrollOnToElement(By.xpath("//ul[@class='page-breadcrumb pull-right']"));
				CommonAppFunctions.spinnerClick(Global.driver, By.xpath("//a[@class='btn btn-sm grey-gallery loading-buffer']"));
				Wait.waitForElementVisibility(Global.driver, Locators.CommonLocators.MasterSearchLable.lblSearch, 6);
				if (!bValidateStatus) {
					Messages.errorMsg = sAppendMessage + Messages.errorMsg;
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}*/

	/*public static boolean deactivateIncentiveFee(String sSearchFieldLabelName, String sLabelValue, String Status, String ID){
		try {
			//search panel function to search the Management fee based on given details.
			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(ID, sSearchFieldLabelName, sLabelValue, Status, 8);
			if (bStatus) {
				NewUICommonFunctions.performJavascriptExecutorToScrollOnToElement(By.xpath("//input[@id='"+ID+"']//..//following-sibling::td[normalize-space(text())='"+sLabelValue+"']//following-sibling::td//a[@data-original-title='Deactivate']"));
				bStatus = CommonAppFunctions.spinnerClick(Global.driver, By.xpath("//input[@id='"+ID+"']//..//following-sibling::td[normalize-space(text())='"+sLabelValue+"']//following-sibling::td//a[@data-original-title='Deactivate']"));
				if (!bStatus) {
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, 10);
				if (!bStatus) {
					Messages.errorMsg = " [ ERROR : Successfully updated the Incentive Fee status message wasn't displayed. ] \n";
					return false;
				}				
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bStatus;
	}*/

	/*public static boolean activateIncentiveFee(String sSearchFieldLabelName, String sLabelValue, String Status, String ID) {
		try {
			//search panel function to search the Management fee based on given details.
			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(ID, sSearchFieldLabelName, sLabelValue, "inactive", 8);
			if (bStatus) {
				NewUICommonFunctions.performJavascriptExecutorToScrollOnToElement(By.xpath("//input[@id='"+ID+"']//..//following-sibling::td[normalize-space(text())='"+sLabelValue+"']//following-sibling::td//a[@data-original-title='Activate']"));
				bStatus = CommonAppFunctions.spinnerClick(Global.driver, By.xpath("//input[@id='"+ID+"']//..//following-sibling::td[normalize-space(text())='"+sLabelValue+"']//following-sibling::td//a[@data-original-title='Activate']"));
				if (!bStatus) {
					return false;
				}
				bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, 10);
				if (!bStatus) {
					Messages.errorMsg = " [ ERROR : Successfully updated the Incentive Fee status message wasn't displayed. ] \n";
					return false;
				}				
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}*/

	public static boolean verifyTextInDropDownForOnChageDependentDDLs(String LabelName,String sExpectedText){
		try{
			String sActualvalue = Elements.getText(Global.driver, By.xpath("//label[text()='"+LabelName+"' or normalize-space(text())='"+LabelName+"']/..//div//select//option[@selected]"));
			if (sActualvalue == null || !sActualvalue.trim().equalsIgnoreCase(sExpectedText) ) {
				Messages.errorMsg = sActualvalue+" : is actual value for Label: "+LabelName+" which is not matching with Expected value : "+sExpectedText;
				return false;
			}							
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyTextInDropDownForIncentiveFeeDetails(String LabelName,String sExpectedText){
		try{			
			String sActualvalue = Elements.getText(Global.driver, By.xpath("//label[normalize-space(text())='"+LabelName+"']//..//following-sibling::select//option[@selected]"));
			if (sActualvalue == null || !sActualvalue.trim().equalsIgnoreCase(sExpectedText) ) {
				Messages.errorMsg = sActualvalue+" : is actual value for Label: "+LabelName+" which is not matching with Expected value : "+sExpectedText;
				return false;
			}							
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyNumericalValuesInTextBox(String objTextBoxID,String sExpectedText, String sSendYesForByAttributeNoToPlaceHolderText){
		try{
			String sActualvalue = "";
			if (sSendYesForByAttributeNoToPlaceHolderText.equalsIgnoreCase("Yes")) {
				sActualvalue = Elements.getElementAttribute(Global.driver, By.id(objTextBoxID),"value");
			}
			if (sSendYesForByAttributeNoToPlaceHolderText.equalsIgnoreCase("No")){
				sActualvalue = Elements.getText(Global.driver, By.id(objTextBoxID)); 
			}			
			Verify.verifyElementVisible(Global.driver, By.id(objTextBoxID));

			bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(sActualvalue, sExpectedText);

			if(!bStatus){				
				Messages.errorMsg = " [ ERROR : "+sActualvalue+" is actual value for : "+objTextBoxID+" which is not matching with Expected value: "+sExpectedText;
				return false;
			}
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
