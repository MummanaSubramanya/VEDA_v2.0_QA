package com.viteos.veda.master.lib;

import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.masterOperationTypes;

public class FormulaSetupAppFunctions {

	static boolean bStatus;

/*	public static boolean searchFormulas(String sFeeType, String sFeeRuleName, String sStatus,int iTime){
		try{

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.CommonLocators.MasterSearchLable.lblSearch);
			if(!bStatus){
				Messages.errorMsg= "Spinner is displayed for a long time";
				return false;
			}

			if(!sFeeType.equalsIgnoreCase("")){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fee Type",sFeeType);
				if(!bStatus)
					return false;
			}

			bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Status",sStatus);
			if(!bStatus)
				return false;

			bStatus = NewUICommonFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
			if(!bStatus){
				return false;
			}

			Wait.waitForElementVisibility(Global.driver, By.xpath("//b[text()='Search Result ']/../div/a"),30);

			for (int i = 0; i < iTime; i++) {
				bStatus = searchFormulas(sFeeType, sFeeRuleName,sStatus);
				if(!bStatus){
					Global.driver.navigate().refresh();
					continue;
				}
				return true;
			}

			Messages.errorMsg = "Formula is not displayed on the page";
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

/*	public static boolean searchFormulas(String sFeeType, String sFeeRuleName, String sStatus){
		try{

			NewUICommonFunctions.selectNumberOfRecordsPerPageInSearchGrid("All", 5);

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//td[text()='"+sFeeRuleName+"']"), 15);
			if(!bStatus){
				Messages.errorMsg = sFeeRuleName+" : is not displayed in the formula search table";
				return false;
			}
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/


	public static boolean addNewFormulas(Map<String, String> newFormulaDetailsMap){
		try{

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//p[contains(text(),'Formula')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Rule computation page is not displayed";
				return false;
			}


			if(newFormulaDetailsMap.get("FeeType")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fee Type",newFormulaDetailsMap.get("FeeType"));
				if(!bStatus){
					
					return false;
				}
			}

			if(newFormulaDetailsMap.get("FeeRuleName")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='feeMethod']"),newFormulaDetailsMap.get("FeeRuleName"));
				if(!bStatus){
					Messages.errorMsg = newFormulaDetailsMap.get("FeeRuleName") + " FeeRuleName  was Not Enterd";
					return false;
				}
			}
			if(newFormulaDetailsMap.get("Rule")!=null){
				bStatus = NewUICommonFunctions.createNewRule(newFormulaDetailsMap.get("Rule"));
				if(!bStatus){
					Messages.errorMsg = newFormulaDetailsMap.get("Rule").replace(",","") + " Rule  was Not Enterd";
					return false;
				}
			}
			if(newFormulaDetailsMap.get("OperationType")!=null){
				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Formula", newFormulaDetailsMap.get("OperationType"));
			}
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

/*	public static boolean modifyFormulaDetails(String sFeeType,String sFormulaName, Map<String, String> modifiedFormulaDetails){
		try{
			bStatus = FormulaSetupAppFunctions.searchFormulas(sFeeType, sFormulaName,"active",10); 
			if(!bStatus){
				Messages.errorMsg = "Formula is not in active";
				return false;
			}

			bStatus = NewUICommonFunctions.jsClick(By.xpath("//td[text()='"+sFormulaName+"']/..//a[@data-original-title='Edit']"));
			if(!bStatus){
				return bStatus;
			}

			bStatus = addNewFormulas(modifiedFormulaDetails);
			if(!bStatus){
				return bStatus;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/


	public static boolean modifyReturnFormulaDetails(String sFormulaName, Map<String, String> modifiedFormulaDetails){
		try{
			bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sFormulaName, "");
			if(!bStatus){
				return false;
			}
			bStatus = addNewFormulas(modifiedFormulaDetails);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean verifyReturnFormulaDetails(String sFormulaName, Map<String, String> FormulaDetails){
		try{
			bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sFormulaName, "");
			if(!bStatus){
				return false;
			}
			boolean validateStatus = true;
			String appendMsg="";
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//p[contains(text(),'Formula')]"), Constants.lTimeOut);
			if(!bStatus){
				return false;
			}

			if(FormulaDetails.get("FeeType")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Fee Type",FormulaDetails.get("FeeType"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = "Fee type actual is not equal to Expected: "+FormulaDetails.get("FeeType")+" \n";
				}
			}

			if(FormulaDetails.get("FeeRuleName")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Fee Rule Name",FormulaDetails.get("FeeRuleName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+" Fee Rule Name actual is not equal to Expected: "+FormulaDetails.get("FeeRuleName")+" \n";
				}
			}

			if(FormulaDetails.get("Rule")!=null){

				String sExpectedRuleValue = FormulaDetails.get("Rule").replaceAll(",", "");
				String sActualRulevalue = Elements.getText(Global.driver, By.id("txtFormula"));
				if(!sExpectedRuleValue.equalsIgnoreCase(sActualRulevalue)){
					validateStatus = false;
					appendMsg = appendMsg+" "+sActualRulevalue+" is actual not equal to Expected: "+FormulaDetails.get("Rule")+" \n";
				}
			}
			Messages.errorMsg = appendMsg;
			return validateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifycreatedFormulaDetailsInEditScreen(String sFeeType,Map<String, String> FormulaDetails){
		try{
			boolean validateStatus = true;
			String appendMsg="";

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//p[contains(text(),'Formula')]"), Constants.lTimeOut);
			if(!bStatus){
				return false;
			}

			if(FormulaDetails.get("FeeType")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Fee Type",FormulaDetails.get("FeeType"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = "Fee type actual is not equal to Expected: "+FormulaDetails.get("FeeType")+" \n";
				}
			}

			if(FormulaDetails.get("FeeRuleName")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Fee Rule Name",FormulaDetails.get("FeeRuleName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+" Fee Rule Name actual is not equal to Expected: "+FormulaDetails.get("FeeRuleName")+" \n";
				}
			}

			if(FormulaDetails.get("Rule")!=null){

				String sExpectedRuleValue = FormulaDetails.get("Rule").replaceAll(",", "");
				String sActualRulevalue = Elements.getText(Global.driver, By.id("txtFormula"));
				if(!sExpectedRuleValue.equalsIgnoreCase(sActualRulevalue)){
					validateStatus = false;
					appendMsg = appendMsg+" "+sActualRulevalue+" is actual not equal to Expected: "+FormulaDetails.get("Rule")+" \n";
				}
			}
			Messages.errorMsg = appendMsg;
			return validateStatus;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

/*	public static boolean deactivateFormula(String sFeeType, String sFeeRuleName) {
		try{
			bStatus = FormulaSetupAppFunctions.searchFormulas(sFeeType, sFeeRuleName,"active",10); 
			if(!bStatus){
				Messages.errorMsg = "Formula is not in active";
				return false;
			}

			bStatus = NewUICommonFunctions.jsClick(By.xpath("//td[text()='"+sFeeRuleName+"']/..//a[@data-original-title='Deactivate']"));
			if(!bStatus){
				return bStatus;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

/*	public static boolean activateFormula(String sFeeType, String sFeeRuleName) {
		try{
			bStatus = FormulaSetupAppFunctions.searchFormulas("", sFeeRuleName,"inactive",10); 
			if(!bStatus){
				return false;
			}

			bStatus = NewUICommonFunctions.jsClick(By.xpath("//td[text()='"+sFeeRuleName+"']/..//a[@data-original-title='Activate']"));
			if(!bStatus){
				return bStatus;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/
}
