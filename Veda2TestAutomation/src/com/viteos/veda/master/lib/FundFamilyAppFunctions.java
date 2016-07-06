package com.viteos.veda.master.lib;

import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class FundFamilyAppFunctions 
{
	static boolean bStatus;
	/*	public static boolean searchFundFamily(Map<String,String> mapSearchDetails)
	{
		try {

			bStatus = CommonAppFunctions.spinnerClick(Global.driver, By.xpath("//span[text()='Search panel']"));
			if(!bStatus){
				Reporting.logResults("Fail", "Spinner is displayed for a long time", Messages.errorMsg);
				return false;
			}

			if(mapSearchDetails.get("Client Name") != null)
			{
				bStatus = Elements.clickButton(Global.driver, Locators.CommonLocators.MasterCommonDropDowns.ddlClientName);
				if(!bStatus)
				{
					Messages.errorMsg = "Client Name Search dropdown Wasn't Clickable";
					return false;
				}
				String seletDropDownValueLocator = Locators.CommonLocators.MasterCommonDropDowns.ddlSelect.replace("selectDropDownValue", mapSearchDetails.get("Client Name"));
				By objDroDownOption = By.xpath(seletDropDownValueLocator);
				bStatus = Wait.waitForElementVisibility(Global.driver,objDroDownOption,Constants.lTimeOut);
				if(!bStatus)
				{
					Messages.errorMsg = "Client Name is not visible in DropDown menu";
					return false;
				}
				bStatus = Elements.clickButton(Global.driver, objDroDownOption);
				if(!bStatus)
				{
					Messages.errorMsg = "Client Name Wasn't selected in dropdown";
					return false;
				}
			}
			if(mapSearchDetails.get("Fund Family Name") != null)
			{
				bStatus = CommonAppFunctions.spinnerClick(Global.driver, Locators.CommonLocators.MasterSearchLable.lblSearch);
				if(!bStatus)
				{
					Messages.errorMsg = "Spinner is visible for long time unable to interact with page";
					return false;
				}
				bStatus = Elements.clickButton(Global.driver, Locators.CommonLocators.MasterCommonDropDowns.ddlFundFamilyName);
				if(!bStatus)
				{
					Messages.errorMsg = "Fund Family Name Search dropdown Wasn't Clickable";
					return false;
				}
				String seletDropDownValueLocator = Locators.CommonLocators.MasterCommonDropDowns.ddlSelect.replace("selectDropDownValue", mapSearchDetails.get("Fund Family Name"));
				By objDroDownOption = By.xpath(seletDropDownValueLocator);
				bStatus = Wait.waitForElementVisibility(Global.driver,objDroDownOption,Constants.lTimeOut);
				if(!bStatus)
				{
					Messages.errorMsg = "Fund Family Name is not visible in DropDown menu";
					return false;
				}
				bStatus = Elements.clickButton(Global.driver, objDroDownOption);
				if(!bStatus)
				{
					Messages.errorMsg = "Fund Family Name Wasn't selected in dropdown";
					return false;
				}			
			}
			if(mapSearchDetails.get("Status") != null)
			{
				bStatus = CommonAppFunctions.spinnerClick(Global.driver, Locators.CommonLocators.MasterSearchLable.lblSearch);
				if(!bStatus)
				{
					Messages.errorMsg = "Spinner is visible for long time unable to interact with page";
					return false;
				}
				bStatus = Elements.clickButton(Global.driver, Locators.CommonLocators.MasterCommonDropDowns.ddlStatus);
				if(!bStatus)
				{
					Messages.errorMsg = "Fund Family Name Search dropdown Wasn't Clickable";
					return false;
				}
				String seletDropDownValueLocator = Locators.CommonLocators.MasterCommonDropDowns.ddlSelect.replace("selectDropDownValue", mapSearchDetails.get("Status"));
				By objDroDownOption = By.xpath(seletDropDownValueLocator);
				bStatus = Wait.waitForElementVisibility(Global.driver,objDroDownOption,Constants.lTimeOut);
				if(!bStatus)
				{
					Messages.errorMsg = "Status Name is not visible in DropDown menu";
					return false;
				}
				bStatus = Elements.clickButton(Global.driver, objDroDownOption);
				if(!bStatus)
				{
					Messages.errorMsg = "Status Wasn't selected in dropdown";
					return false;
				}			
			}

			bStatus = CommonAppFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
			if(!bStatus)
			{
				Messages.errorMsg = "Search Operation Wasn't performed";
				return false;
			}		


			bStatus = CommonAppFunctions.isSerachTableDisplayed(Locators.CommonLocators.MasterSearchTable.tableMasterSearch);
			return bStatus;

		}
		catch (Exception e) 
		{
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}
	 */
	public static boolean addNewFundfamily(Map<String, String> mapFundFamilyDetails)
	{
		try
		{
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='fundFamilyName']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Fund Family Page Not visible";
				return false;
			}

			// Select Client Name
			if(mapFundFamilyDetails.get("ClientName")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name",mapFundFamilyDetails.get("ClientName"),2);
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("ClientName")+ " ClientName  was Not Selected";
					return false;
				}
			}

			//Enter  Fund Family Name

			if(mapFundFamilyDetails.get("FundFamilyName")!=null){
				bStatus = Elements.enterText(Global.driver , By.id("fundFamilyName"), mapFundFamilyDetails.get("FundFamilyName"));
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("FundFamilyName") + " Fund Family Name  was Not Entered";
					return false;
				}
			}

			if(mapFundFamilyDetails.get("Fund Family Code") != null){
				bStatus = Elements.enterText(Global.driver, By.name("fundFamilyCode"), mapFundFamilyDetails.get("Fund Family Code"));
				if(!bStatus){
					Messages.errorMsg = "Fund Family code cannot be entered";
					return false;
				}
			}
			//Select Fund Family TYpe

			if(mapFundFamilyDetails.get("FundFamilyType")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Type",mapFundFamilyDetails.get("FundFamilyType"),2);
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("FundFamilyType") + " Fund Family Type  was Not Selected";
					return false;
				}
			}

			//Enter Start Date
			if(mapFundFamilyDetails.get("StartDate")!=null){
				bStatus = Elements.enterText(Global.driver,By.id("initialOfferingDate"),mapFundFamilyDetails.get("StartDate"));
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("StartDate") + " Start Date  was Not Enterd";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Fund Family Master')]"));
				if(!bStatus){
					Messages.errorMsg = "Fund Family Master Lable Not Clicked after enterig the Start Date";
					return false;
				}
			}

			// Select Investment Strategy

			if(mapFundFamilyDetails.get("InvestmentStrategy")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Investment Strategy",mapFundFamilyDetails.get("InvestmentStrategy"));
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("InvestmentStrategy")+ " Investment Strategy  was Not Selected";
					return false;
				}
			}

			//Select Investment Region*

			if(mapFundFamilyDetails.get("Accounting System")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Accounting System",mapFundFamilyDetails.get("Accounting System"));
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("Accounting System")+" Accounting System was Not Selected";
					return false;
				}
			}
			// Select Version

			if(mapFundFamilyDetails.get("Version")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Version",mapFundFamilyDetails.get("Version"));
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("Version")+" Version was Not Selected";
					return false;
				}
			}

			//Select Accounting System
			if(mapFundFamilyDetails.get("InvestmentRegion")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Investment Region",mapFundFamilyDetails.get("InvestmentRegion"));
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("InvestmentRegion")+" Investment Region was Not Selected";
					return false;
				}
			}
			//Select Switch Allowed Between Legal Entities

			if(mapFundFamilyDetails.get("SwitchBetweenLegalEntity")!=null){
				if(mapFundFamilyDetails.get("SwitchBetweenLegalEntity").equalsIgnoreCase("Yes"))
				{
					bStatus= Elements.click(Global.driver,By.id("uniform-rdIsSwitchAllowedBetweenLegalEntitesYes"));
					if(!bStatus){
						Messages.errorMsg = "Switch Between LegalEntity Radio button Yes is not Clicked ";
						return false;
					}	
				}
				if(mapFundFamilyDetails.get("SwitchBetweenLegalEntity").equalsIgnoreCase("No")){
					bStatus= Elements.click(Global.driver,  By.id("uniform-rdIsSwitchAllowedBetweenLegalEntitesNo"));
					if(!bStatus){
						Messages.errorMsg = "Switch Between Legal Entity Radio button No is not Clicked ";
						return false;
					}
				}
			}

			//Select Administrator Method

			if(mapFundFamilyDetails.get("Administrator Method")!=null){
				if(mapFundFamilyDetails.get("Administrator Method").equalsIgnoreCase("FA"))
				{
					bStatus= Elements.click(Global.driver,  By.xpath("//label[normalize-space()='FA']//following-sibling::div"));
					if(!bStatus){
						Messages.errorMsg = "Administrator Method Radio button 'FA' is not Clicked ";
						return false;
					}	
				}
				if(mapFundFamilyDetails.get("Administrator Method").equalsIgnoreCase("IS")){
					bStatus= Elements.click(Global.driver,  By.xpath("//label[normalize-space()='IS']//following-sibling::div"));
					if(!bStatus){
						Messages.errorMsg = "Administrator Method Radio button 'IS' is not Clicked ";
						return false;
					}
				}

				if(mapFundFamilyDetails.get("Administrator Method").equalsIgnoreCase("Both")){
					bStatus= Elements.click(Global.driver,  By.xpath("//label[normalize-space()='Both']//following-sibling::div"));
					if(!bStatus){
						Messages.errorMsg = "Administrator Method Radio button'Both' is not Clicked ";
						return false;
					}
				}
			}




			// External Identifier

			if(mapFundFamilyDetails.get("ExtId1")!=null){
				bStatus = Elements.enterText(Global.driver,By.id("externalId1"),mapFundFamilyDetails.get("ExtId1"));
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("ExtId1") + "External Id1  was Not Enterd";
					return false;
				}
			}

			if(mapFundFamilyDetails.get("ExtId2")!=null){
				bStatus = Elements.enterText(Global.driver,By.id("externalId2"),mapFundFamilyDetails.get("ExtId2"));
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("ExtId2") + "  External Id2  was Not Enterd";
					return false;
				}

			}

			if(mapFundFamilyDetails.get("ExtId3")!=null){
				bStatus = Elements.enterText(Global.driver,By.id("externalId3"),mapFundFamilyDetails.get("ExtId3"));
				if(!bStatus){
					Messages.errorMsg = mapFundFamilyDetails.get("ExtId3") +"External Id3  was Not Enterd";
					return false;
				}
			}

			if(mapFundFamilyDetails.get("OperationType")!=null){
				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("FundFamily", mapFundFamilyDetails.get("OperationType"));
				if(mapFundFamilyDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//input[@id='fundFamilyName']"), 2);
					if(bStatus){
						Messages.errorMsg = "Cancel Operaion failed to perform,Fund Family Page is still available";
						return false;
					}
					return true;
				}
			}
			return bStatus;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/*	public static boolean modifyFundFamilyDetails(String sFundFamilyName,Map<String, String> ModifiedFundFamily){
		try{

			bStatus = FundFamilyAppFunctions.searchFundFamilyMaster(sFundFamilyName, "active",2); 
			if(!bStatus){
				Messages.errorMsg = "FundFamily is Not active yet";
				return false;
			}

			Elements.click(Global.driver, By.xpath("//a[@data-original-title='Edit']"));

			bStatus = addNewFundfamily(ModifiedFundFamily);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	/*	public static boolean searchFundFamilyMaster(String sFundFamilyName, String Status,int iTime){
		try{
			bStatus = Wait.waitForElementVisibility(Global.driver,Locators.FundFamilyMaster.Label.lblSearchPanel, Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Fund Family is not displayed.";
				return false;
			}

			bStatus = CommonAppFunctions.WaitUntilValueAvailableInDropDown(sFundFamilyName,Locators.FundFamilyMaster.Ddn.objFundFamilyTypeClick,iTime);
			if (!bStatus) {
				return false;
			}

			bStatus = CommonAppFunctions.spinnerClick(Global.driver, Locators.FundFamilyMaster.Label.lblSearchPanel);
			if (!bStatus) {
				return false;
			}

			bStatus = CommonAppFunctions.selectFromDropDownOfApplication(Status,Locators.FundFamilyMaster.Ddn.objStatusClick);
			if (!bStatus) {
				return false;
			}

			bStatus = CommonAppFunctions.doOperationsOnMasterPage(masterOperationTypes.SEARCH);
			if (!bStatus) {
				Messages.errorMsg = " Search opeartion Button Wasn't Visible";
				return false;
			}

			bStatus = CommonAppFunctions.isSerachTableDisplayed(Locators.CommonLocators.MasterSearchTable.tableMasterSearch);

			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	 */
	public static boolean verifyFundFamilyDetailsInEditScreen(Map<String, String> FundFamilyDetails){
		try{	

			boolean validateStatus = true;

			String appendMsg="";

			bStatus = Wait.waitForElementVisibility(Global.driver, By.name("fundFamilyCode"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Fund Family page is not visible";
				return false;
			}

			//Verify Client Name

			if(FundFamilyDetails .get("ClientName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInDropDown("Client Name", FundFamilyDetails.get("ClientName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Client Name]\n";
				}
			}

			//Verify Fund family name

			if(FundFamilyDetails .get("FundFamilyName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Fund Family Name", FundFamilyDetails.get("FundFamilyName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Fund Family Name]\n";
				}
			}

			if(FundFamilyDetails.get("Fund Family Code") != null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Fund Family Code", FundFamilyDetails.get("Fund Family Code"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Fund Family Code]\n";
				}
			}
			//Verify Fund Family Type

			if(FundFamilyDetails .get("FundFamilyType")!=null){
				bStatus=NewUICommonFunctions.verifyTextInDropDown("Fund Family Type", FundFamilyDetails.get("FundFamilyType"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Fund Family Type]\n";
				}
			}

			//Verify the Start Date

			if(FundFamilyDetails .get("StartDate")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Start Date", FundFamilyDetails.get("StartDate"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Start Date]\n";
				}
			}

			//Verify Investment Strategy

			if(FundFamilyDetails .get("InvestmentStrategy")!=null){
				bStatus=NewUICommonFunctions.verifyTextInDropDown("Investment Strategy", FundFamilyDetails.get("InvestmentStrategy"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Investment Strategy]\n";
				}
			}

			//Verify Investment Region

			if(FundFamilyDetails .get("InvestmentRegion")!=null){
				bStatus=NewUICommonFunctions.verifyTextInDropDown("Investment Region", FundFamilyDetails.get("InvestmentRegion"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Investment Region]\n";
				}
			}

			//Verify Accounting System
			if(FundFamilyDetails.get("Accounting System")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Accounting System",FundFamilyDetails.get("Accounting System"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Accounting System]\n";
				}
			}

			// Verify Version
			if(FundFamilyDetails.get("Version")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Version",FundFamilyDetails.get("Version"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Version]\n";
				}
			}

			//Verify Switch Allowed between Legal Entities

			if((FundFamilyDetails.get("SwitchBetweenLegalEntity")!=null)){
				bStatus=NewUICommonFunctions.verifySelectedRadioButton("Switch Allowed Between Legal Entities",FundFamilyDetails.get("SwitchBetweenLegalEntity"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Switch Allowed Between Legal Entities ]\n";
				}	
			}

			//Verify Administrator Method

			if((FundFamilyDetails.get("Administrator Method")!=null)){
				bStatus=NewUICommonFunctions.verifySelectedRadioButton("Administrator Method",FundFamilyDetails.get("Administrator Method"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Administrator Method ]\n";
				}	
			}
			//verify External Identifier

			if(FundFamilyDetails.get("ExtId1")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("External Id1", FundFamilyDetails.get("ExtId1"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for External Id1]\n";
				}

			}
			/* if(FundFamilyDetails.get("ExtId2")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("External Id2", FundFamilyDetails.get("ExtId2"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for External Id2]\n";
				}

			}
			if(FundFamilyDetails.get("ExtId3")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("External Id3", FundFamilyDetails.get("ExtId3"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for External Id3]\n";
				}
			}	
			 */


			if(FundFamilyDetails.get("ExtId2")!=null)
			{
				String sExt2 = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='externalId2']"),"value");
				if(sExt2==null || !sExt2.equalsIgnoreCase(FundFamilyDetails.get("ExtId2")))
				{
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for External Id2]\n";
				}
			}

			if(FundFamilyDetails.get("ExtId3")!=null)
			{
				String sExt3 = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='externalId3']"),"value");
				if(sExt3==null || !sExt3.equalsIgnoreCase(FundFamilyDetails.get("ExtId3")))
				{
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for External Id3]\n";
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

	/*	public static boolean deactivateFundFamily(String sFundFamilyName){
		try{
			bStatus = FundFamilyAppFunctions.searchFundFamilyMaster(sFundFamilyName, "active",5); 
			if(!bStatus){
				Messages.errorMsg = "Fund Fmaily is Not in active state yet : "+sFundFamilyName;
				return false;
			}
			Elements.click(Global.driver, By.xpath("//a[@data-original-title='Deactivate']"));

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="Fund Family cannot be deactivated";
				return false;
			}
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	public static boolean modifyReturnFundFamilyDetails(String searchName,Map<String, String> mapFundFamilyModifyDetails) {
		try{
			bStatus  = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,searchName,"");
			if(!bStatus){
				return false;
			}
			bStatus = addNewFundfamily(mapFundFamilyModifyDetails);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/*	public static boolean activateFundFamily(String sFundFamilyName){
		try{
			bStatus = NewUICommonFunctions.searchInSearchPanel("" , sFundFamilyName, "inactive",10);
			if(!bStatus){
				Messages.errorMsg = "There are no Clients in inactive state yet";
				return false;
			}

			NewUICommonFunctions.selectNumberOfRecordsPerPageInSearchGrid("All",10);

			NewUICommonFunctions.sortTableByColumnName("sample_2", "Fund Family ID", "descending");

			Elements.click(Global.driver, By.xpath("//td[normalize-space(text())='"+sFundFamilyName+"']/../td[6]/a[@data-original-title='Activate']"));

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="FundFamily Cannot be activated";
				return false;
			}
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

}
