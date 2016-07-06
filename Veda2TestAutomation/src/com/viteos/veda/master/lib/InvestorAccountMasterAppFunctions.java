package com.viteos.veda.master.lib;

import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;

public class InvestorAccountMasterAppFunctions {
	static boolean bStatus;
	public static boolean addNewAccount(Map<String, String> mapAccountDetails){
		try {
			if (mapAccountDetails.get("Account") != null) {
				String sAccountType = "";
				if (mapAccountDetails.get("Account").equalsIgnoreCase("Investor")) {
					sAccountType = "Investor";
				}else if (mapAccountDetails.get("Account").equalsIgnoreCase("Feeder")) {
					sAccountType = "Feeder";
				}
				bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='"+sAccountType+"']//input"));
				if (!bStatus) {
					Messages.errorMsg = "radio button wasn't selectable for Account Type : "+sAccountType+" ]";
					return false;
				}
			}
			if (mapAccountDetails.get("Account") != null && mapAccountDetails.get("Account").equalsIgnoreCase("Investor")) {
				if (mapAccountDetails.get("Investor Name") != null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Investor Name", mapAccountDetails.get("Investor Name"));
					if (!bStatus) {
						Messages.errorMsg = "Investor  Name was Not Selected From Drop Down";
						return false;

					}
				}
				if (mapAccountDetails.get("Investor Holder") != null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Investor Holder", mapAccountDetails.get("Investor Holder"));
					if (!bStatus) {
						Messages.errorMsg = "Investor  Holder  was Not Selected From Drop Down";
						return false;
					}
				}				
			}
			if (mapAccountDetails.get("Account") != null && mapAccountDetails.get("Account").equalsIgnoreCase("Feeder")) {
				if (mapAccountDetails.get("Client Name") != null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", mapAccountDetails.get("Client Name"));
					if (!bStatus) {
						Messages.errorMsg = "Client Name  was Not Selected From Drop Down";
						return false;
					}
				}
				if (mapAccountDetails.get("Fund Family Name") != null) {					
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", mapAccountDetails.get("Fund Family Name"));
					if (!bStatus) {
						Messages.errorMsg = "Fund Family Name  was Not Selected From Drop Down";
						return false;
					}
				}
				if (mapAccountDetails.get("Legal Entity Name From") != null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name(From)", mapAccountDetails.get("Legal Entity Name From"));
					if (!bStatus) {
						Messages.errorMsg = "Legal Entity Name From  was Not Selected From Drop Down";
						return false;
					}
				}
				if (mapAccountDetails.get("Legal Entity Name To") != null) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name(To)", mapAccountDetails.get("Legal Entity Name To"));
					if (!bStatus) {
						Messages.errorMsg = "Legal Entity Name To  was Not Selected From Drop Down";
						return false;
					}
				}
			}
			if (mapAccountDetails.get("External Id1") != null) {
				bStatus = Elements.enterText(Global.driver, By.id("externalId1"), mapAccountDetails.get("External Id1"));
				if (!bStatus) {
					Messages.errorMsg = "External Id1 was  not Entered";
					return false;
				}
			}
			if (mapAccountDetails.get("External Id2") != null) {
				bStatus = Elements.enterText(Global.driver, By.id("externalId2"), mapAccountDetails.get("External Id2"));
				if (!bStatus) {
					Messages.errorMsg = "External Id2 was  not Entered";
					return false;
				}
			}
			if (mapAccountDetails.get("External Id3") != null) {
				bStatus = Elements.enterText(Global.driver, By.id("externalId3"), mapAccountDetails.get("External Id3"));
				if (!bStatus) {
					Messages.errorMsg = "External Id3 was  not Entered";
					return false;
				}
			}

			bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("InvestorAccount Master", mapAccountDetails.get("OperationType"));
			if(!bStatus){
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bStatus;
	}

	public static boolean verifyInvestorAccountDetailsInEditScreen(Map<String, String> mapAccountDetails){
		try {
			boolean validateStatus = true;
			String appendMsg="";
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//a[contains(text(),'General Details')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "General Details are Not Visible";
				return false;
			}

			//verify the Account Type Radio button
			if(mapAccountDetails.get("Account")!=null){
				bStatus=NewUICommonFunctions.verifySelectedRadioButton("Account",mapAccountDetails.get("Account"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" Investor Sub Type ]\n";
				}	
			}

			//Verify the Investor Details

			//Verify the Investor Name
			if (mapAccountDetails.get("Account") != null && mapAccountDetails.get("Account").equalsIgnoreCase("Investor")) {
				if (mapAccountDetails.get("Investor Name") != null) {
					bStatus=NewUICommonFunctions.verifyTextInDropDown("Investor Name", mapAccountDetails.get("Investor Name"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" for Investor Name]\n";
					}
				}

				//Verify the Investor Holder
				if (mapAccountDetails.get("Investor Holder") != null) {
					bStatus=NewUICommonFunctions.verifyTextInDropDown("Investor Holder", mapAccountDetails.get("Investor Holder"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" for Investor Holder]\n";
					}
				}				
			}

			//Verify Feeder Details

			//Verify Client Name
			if (mapAccountDetails.get("Account") != null && mapAccountDetails.get("Account").equalsIgnoreCase("Feeder")) {
				if (mapAccountDetails.get("Client Name") != null) {
					bStatus=NewUICommonFunctions.verifyTextInDropDown("Client Name", mapAccountDetails.get("Client Name"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" for Client Name ]\n";
					}
				}


				//Verify Fund Family
				if (mapAccountDetails.get("Fund Family Name") != null) {	
					bStatus=NewUICommonFunctions.verifyTextInDropDown("Fund Family Name", mapAccountDetails.get("Fund Family Name"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" for Fund Family Name ]\n";
					}
				}


				//Verify Legal Entity Name From
				if (mapAccountDetails.get("Legal Entity Name From") != null) {
					bStatus=NewUICommonFunctions.verifyTextInDropDown("Legal Entity Name(From)", mapAccountDetails.get("Legal Entity Name From"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" for Legal Entity Name From ]\n";
					}
				}

				//Verify Legal Entity Name To
				if (mapAccountDetails.get("Legal Entity Name To") != null) {
					bStatus=NewUICommonFunctions.verifyTextInDropDown("Legal Entity Name(To)", mapAccountDetails.get("Legal Entity Name To"));
					if(!bStatus){
						validateStatus = false;
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" for Legal Entity Name To ]\n";
					}
				}
			}
			//Verify External ID's

			if (mapAccountDetails.get("External Id1") != null) {
				bStatus=NewUICommonFunctions.verifyTextInTextBox("External Id1", mapAccountDetails.get("External Id1"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" External Id1 ]\n";
				}
			}

			if (mapAccountDetails.get("External Id2") != null) {
				bStatus=NewUICommonFunctions.verifyTextInTextBox("External Id2", mapAccountDetails.get("External Id2"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" External Id2 ]\n";
				}
			}
			if (mapAccountDetails.get("External Id3") != null) {
				bStatus=NewUICommonFunctions.verifyTextInTextBox("External Id3", mapAccountDetails.get("External Id3"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" External Id3 ]\n";
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

