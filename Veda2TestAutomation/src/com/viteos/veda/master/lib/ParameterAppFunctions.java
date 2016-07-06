package com.viteos.veda.master.lib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;


public class ParameterAppFunctions {

	static boolean bStatus;

	public static boolean addNewParameter(Map<String, String> mapParameterDetails){
		try{

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//p[contains(text(),'Parameter')]"), Constants.lTimeOut);
			if(!bStatus){
				return false;
			}

			if(mapParameterDetails.get("ClientName")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name",mapParameterDetails.get("ClientName"));
				if(!bStatus){
					return false;
				}
			}


			if(mapParameterDetails.get("FundFamilyName")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name",mapParameterDetails.get("FundFamilyName"));
				if(!bStatus){
					return false;
				}
			}

			if(mapParameterDetails.get("LegalEntityName")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name",mapParameterDetails.get("LegalEntityName"));
				if(!bStatus){
					return false;
				}
			}


			if(mapParameterDetails.get("ClassName")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Class Name",mapParameterDetails.get("ClassName"));
				if(!bStatus){
					return false;
				}
			}

			if(mapParameterDetails.get("ParameterName")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Parameter Name",mapParameterDetails.get("ParameterName"));
				if(!bStatus){
					return false;
				}
			}

			//Defining Rules
			if(mapParameterDetails .get("Rule")!=null){

				//New Rule
				if(mapParameterDetails.get("Rule").equalsIgnoreCase("New Rule")){
					bStatus = Elements.click(Global.driver, Locators.ParameterMaster .RadioButton.rbtnNewRule);
					if(!bStatus ){
						Messages.errorMsg = "New Rule Radio button is not selected";
						return false;
					}
				}

				//Existing Rules
				if(mapParameterDetails.get("Rule").equalsIgnoreCase("Existing Rules")){
					bStatus = Elements.click(Global.driver, Locators.ParameterMaster .RadioButton .rbtnExistingRule);
					if(!bStatus ){
						Messages.errorMsg = "Existing Rule Radio button is not selected";
						return false;
					}	

					if(mapParameterDetails.get("Attributes")!=null){
						bStatus = verifyFormulaInExistingRules(mapParameterDetails.get("EffectiveStartDate"), mapParameterDetails.get("Attributes"));
						if(!bStatus){
							return false;
						}
						return true;
					}
					Messages.errorMsg = "Exisiting formula selected.Please chnage Rule type";
					return false;
				}

				//All Funds Rules
				if(mapParameterDetails.get("Rule").equalsIgnoreCase("All Fund Rules")){
					bStatus = Elements.click(Global.driver, Locators.ParameterMaster .RadioButton .rbtnAllFundsRule);
					if(!bStatus ){
						Messages.errorMsg = "All Fund Rule  Radio button is not selected";
						return false;
					}

					if(mapParameterDetails.get("Attributes")!=null){
						String attributes = mapParameterDetails.get("Attributes").replace(",", "");
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[text()='"+attributes+"']/input"), 10);
						if(!bStatus){
							Messages.errorMsg = attributes+" is not displayed in the all fund rules";
							return false;
						}
						bStatus = Elements.click(Global.driver, By.xpath("//label[text()='"+attributes+"']/input"));
						if(!bStatus ){
							Messages.errorMsg = "Existing rule button is not selected";
							return false;
						}
					}

					//effective start date
					if(mapParameterDetails.get("EffectiveStartDate")!=null){
						bStatus=Elements.enterText(Global.driver,By.id("edate2"),mapParameterDetails.get("EffectiveStartDate"));
						if(!bStatus ){
							Messages.errorMsg = "Effective Start Date button is not selected";
							return false;
						}
					}

					bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Parameter Master", mapParameterDetails.get("OperationType"));
					if(!bStatus){
						return false;
					}
					return true;
				}
			}

			Wait.waitForElementVisibility(Global.driver, Locators.ParameterMaster.TextBox.txtEffectiveStartDateNewRule, 10);
			if(mapParameterDetails.get("EffectiveStartDate")!=null){
				bStatus=Elements.enterText(Global.driver, Locators.ParameterMaster.TextBox.txtEffectiveStartDateNewRule,mapParameterDetails.get("EffectiveStartDate"));
				if(!bStatus ){
					bStatus=Elements.enterText(Global.driver, By.id("effectiveStartDate"),mapParameterDetails.get("EffectiveStartDate"));
					if(!bStatus ){
						Messages.errorMsg = "Effective Start Date button is not selected";
						return false;
					}
				}
			}

			Elements.click(Global.driver, By.xpath("//p[contains(text(),'Parameter')]"));


			//attributes
			if(mapParameterDetails.get("Attributes")!= null){
				bStatus = NewUICommonFunctions.createNewRule(mapParameterDetails.get("Attributes"));
				if(!bStatus ){
					Messages.errorMsg = "Cannot create formula.Error: "+Messages.errorMsg;
					return false;
				}
			}

			bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Parameter Master", mapParameterDetails.get("OperationType"));
			if(!bStatus){

				return false;
			}
			return true;	
		}

		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyFormulaInExistingRules(String startDate,String Rule){
		try{

			//boolean dateStatus = true;
			boolean RuleStatus = true;
			Rule = Rule.replace(",","");

			SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy");
			SimpleDateFormat format2 = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = format1.parse(startDate);
			startDate = format2.format(date);

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='divExistingRule']"),20);
			if(!bStatus){
				Messages.errorMsg = "Existing rule Table is not displayed";
				return false;
			}

			String RuleLocator = "//div[@id='divExistingRule']//table//tbody/tr//td[text()='sRule']/../td[text()='startDate']";
			RuleLocator = RuleLocator.replace("sRule", Rule).replace("startDate", startDate);

			RuleStatus = Verify.verifyElementVisible(Global.driver,By.xpath(RuleLocator));
			if(!RuleStatus){
				Messages.errorMsg = "for the date: "+startDate+" Rule is not available: "+Rule;
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*	public static boolean modifyParametersDetails(String createdID, String Createdparam,Map<String, String> ModifiedParameterDetails){
		try{

			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(createdID, "Parameter Name",Createdparam,"active", Constants.iSearchTableTimeout);
			if(!bStatus){
				Messages.errorMsg = "Parameter is Not active yet.Error: "+Messages.errorMsg;
				return false;
			}

			bStatus = Elements.click(Global.driver, By.xpath("//td[text()='"+createdID+"']/following-sibling::td[normalize-space(text())='"+Createdparam+"']/..//a[@data-original-title='Edit']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Edit button to navigate to Modify screen";
				return false;
			}

			bStatus = addNewParameter(ModifiedParameterDetails);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	public static boolean modifyReturnParametersDetails(String createdID,Map<String, String> ModifiedParameterDetails){
		try{

			bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,createdID,"");
			if(!bStatus){
				Messages.errorMsg = "Parameter is Not displayed in checker table .Error: "+Messages.errorMsg;
				return false;
			}

			bStatus = addNewParameter(ModifiedParameterDetails);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*	public static boolean deactivateParameter(String parameterID,String sParameterName) {
		try{
			//search parameter has to be implement  here please make sure
			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(parameterID, "Parameter Name",sParameterName,"active", Constants.iSearchTableTimeout);
			if(!bStatus){
				Messages.errorMsg = "Parameter is Not active yet.Error: " +Messages.errorMsg;
				return false;
			}

			bStatus = Elements.click(Global.driver, By.xpath("//td[text()='"+parameterID+"']/following-sibling::td[normalize-space(text())='"+sParameterName+"']/..//a[@data-original-title='Deactivate']"));
			if(!bStatus){
				Messages.errorMsg ="Deactivate buton is not clicked";
				return bStatus;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="parameter cannot be Deactivated and Success message is not displayed";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	 */
	/*	public static boolean activateParameter(String parameterID,String sParameterName) {
		try{
			//search parameter has to be implement here please make sure
			bStatus = NewUICommonFunctions.searchItemsinPanelbasedonIDGenerated(parameterID, "Parameter Name",sParameterName,"inactive", Constants.iSearchTableTimeout);
			if(!bStatus){
				Messages.errorMsg = "Parameter is Not in inactive yet.Error: "+Messages.errorMsg;
				return false;
			}

			bStatus = Elements.click(Global.driver, By.xpath("//td[text()='"+parameterID+"']/following-sibling::td[normalize-space(text())='"+sParameterName+"']/..//a[@data-original-title='Activate']"));
			if(!bStatus){
				Messages.errorMsg ="Activate buton is not clicked";
				return bStatus;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="Parameter cannot be Activated. Success message is not displayed";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	public static boolean verifyReturnParameterDetailsInEditScreen(Map<String, String> ParameterDetails){
		try{

			boolean validateStatus = true;

			NewUICommonFunctions.selectMenu("Dashboard", "None");

			bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,ParameterDetails.get("parameterID"),"");
			if(!bStatus){
				Messages.errorMsg = "Parameter is Not displayed in checker table .Error: "+Messages.errorMsg;
				return false;
			}

			String appendMsg="";

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//p[contains(text(),'Parameter')]"), Constants.lTimeOut);
			if(!bStatus){
				validateStatus = false;
				return false;
			}

			if(ParameterDetails .get("ClientName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Client Name", ParameterDetails.get("ClientName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\n";
				}
			}

			if(ParameterDetails .get("FundFamilyName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Fund Family Name", ParameterDetails.get("FundFamilyName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\n";
				}
			}

			if(ParameterDetails .get("LegalEntityName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Legal Entity Name", ParameterDetails.get("LegalEntityName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\t";
				}
			}

			if(ParameterDetails .get("ClassName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Class Name", ParameterDetails.get("ClassName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\t";
				}
			}

			if(ParameterDetails .get("ParameterName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Parameter Name", ParameterDetails.get("ParameterName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\n";
				}
			}

			if(ParameterDetails.get("EffectiveStartDate")!=null){
				bStatus = NewUICommonFunctions .verifyTextInTextBox("Effective Start Date",ParameterDetails.get("EffectiveStartDate"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\n";
				}
			}

			if(ParameterDetails.get("Attributes")!=null){
				String sExpectedRuleValue1 = ParameterDetails.get("Attributes").replaceAll(",", "");
				String sActualRulevalue = Elements.getText(Global.driver, By.id("txtFormula"));
				if(!sExpectedRuleValue1.equalsIgnoreCase(sActualRulevalue)){
					validateStatus = false;
					appendMsg = appendMsg+" "+sActualRulevalue+" is actual not equal to Expected: "+ParameterDetails.get("Attributes")+" \n";
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

	public static boolean verifyParameterDetailsInEditScreen(Map<String, String> ParameterDetails){
		try{

			boolean validateStatus = true;

			String appendMsg="";

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//p[contains(text(),'Parameter')]"), Constants.lTimeOut);
			if(!bStatus){
				validateStatus = false;
				return false;
			}

			if(ParameterDetails .get("ClientName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Client Name", ParameterDetails.get("ClientName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\n";
				}
			}

			if(ParameterDetails .get("FundFamilyName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Fund Family Name", ParameterDetails.get("FundFamilyName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\n";
				}
			}

			if(ParameterDetails .get("LegalEntityName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Legal Entity Name", ParameterDetails.get("LegalEntityName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\t";
				}
			}

			if(ParameterDetails .get("ClassName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Class Name", ParameterDetails.get("ClassName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\t";
				}
			}

			if(ParameterDetails .get("ParameterName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Parameter Name", ParameterDetails.get("ParameterName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\n";
				}
			}

			if(ParameterDetails.get("EffectiveStartDate")!=null){
				bStatus = NewUICommonFunctions .verifyTextInTextBox("Effective Start Date",ParameterDetails.get("EffectiveStartDate"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\n";
				}
			}

			if(ParameterDetails.get("Attributes")!=null){
				String sExpectedRuleValue1 = ParameterDetails.get("Attributes").replaceAll(",", "");
				String sActualRulevalue = Elements.getText(Global.driver, By.id("txtFormula"));
				if(!sExpectedRuleValue1.equalsIgnoreCase(sActualRulevalue)){
					validateStatus = false;
					appendMsg = appendMsg+" "+sActualRulevalue+" is actual not equal to Expected: "+ParameterDetails.get("Attributes")+" \n";
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

}