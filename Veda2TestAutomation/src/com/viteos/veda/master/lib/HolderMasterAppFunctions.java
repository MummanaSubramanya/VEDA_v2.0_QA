package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.tenx.framework.reporting.Reporting;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class HolderMasterAppFunctions {

	static boolean bStatus ;
	public static String HolderType ="Entity";
	public static boolean isHolderGeneralDetailsModifyFlag = false;
	public static boolean bTradingSubscription = false;

	public static boolean doFillGeneralDetails(Map<String, String> mapGeneralDetails){
		try{

			if(HolderType.equalsIgnoreCase("") || HolderType == null){
				Messages.errorMsg = "Holder type not mentioned in the testdata file";
				return false;
			}
			bStatus = NewUICommonFunctions.selectTheTab("General Details");
			if(!bStatus){
				return false;
			}


			if(mapGeneralDetails.get("HolderType") != null){
				//select the HolderType from Holder.
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Holder Type",mapGeneralDetails.get("HolderType"));
				if(!bStatus && isHolderGeneralDetailsModifyFlag == false){
					return false;
				}
				if (bStatus && isHolderGeneralDetailsModifyFlag == true) {
					Messages.errorMsg = "[ ERROR : 'Holder Type' cannot be modified but user is able to modify the Holder Type . ] "+Messages.errorMsg;
					return false;
				}
			}

			if(mapGeneralDetails.get("Investor")!=null){
				//select the Investor if not a modification of holder flag set to false.
				if (isHolderGeneralDetailsModifyFlag == false) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Investor",mapGeneralDetails.get("Investor"));
					if(!bStatus){
						return false;
					}
				}				
			}

			//fill details for the HolderType Entity
			if(mapGeneralDetails.get("HolderType")!=null && HolderType.equalsIgnoreCase("Entity")){
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='registrationName']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "General Details is not displyed for the HolderType : Enity";
					return false;
				}

				//select for the Entitytype
				if(mapGeneralDetails.get("EntityType")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Entity Type",mapGeneralDetails.get("EntityType"));
					if(!bStatus){
						return false;
					}
				}

				//enter the Registration Name
				if(mapGeneralDetails.get("RegistrationName")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("registrationName"), mapGeneralDetails.get("RegistrationName"));
					if(!bStatus){
						Messages.errorMsg = "Canot enter value in the Registration Name field";
						return false;
					}
				}

				//enter the Date of incorporation
				if(mapGeneralDetails.get("IncorporationDate")!=null){
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("dateOfIncorporation"), mapGeneralDetails.get("IncorporationDate"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Incorporation Date field";
						return false;
					}
					//NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
				}

				//select Country
				if(mapGeneralDetails.get("IncorporationCountry")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Country Of Incorporation",mapGeneralDetails.get("IncorporationCountry"));
					if(!bStatus){
						return false;
					}
				}

				//fill tax details
				if(mapGeneralDetails.get("TaxID")!=null && mapGeneralDetails.get("TaxState")!=null && mapGeneralDetails.get("TaxCountry")!=null ){
					//remove all tax id if availble any
					bStatus = removeAllTaxIDs(HolderType);
					if(!bStatus){
						return false;
					}

					//add tax details for entity
					bStatus = fillTaxesDetailsForHolderType(mapGeneralDetails.get("TaxID"),mapGeneralDetails.get("TaxState"),mapGeneralDetails.get("TaxCountry"));
					if(!bStatus){
						return false;
					}
				}							
			}

			// fill common external Id's
			if(mapGeneralDetails.get("ExternalID1")!=null){
				bStatus = Elements.enterText(Global.driver,Locators.ClientMaster.TextBox.txtExternalId1,mapGeneralDetails.get("ExternalID1"));
				if(!bStatus){
					Messages.errorMsg = "Cannot enter the value in External ID1";
					return false;
				}
			}

			if(mapGeneralDetails.get("ExternalID2")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.ClientMaster.TextBox.txtExternalId2,mapGeneralDetails.get("ExternalID2"));
				if(!bStatus){
					Messages.errorMsg = "Cannot enter the value in External ID2";
					return false;
				}
			}

			if(mapGeneralDetails.get("ExternalID3")!=null){
				bStatus = Elements.enterText(Global.driver,Locators.ClientMaster.TextBox.txtExternalId3,mapGeneralDetails.get("ExternalID3"));
				if(!bStatus){
					Messages.errorMsg = "Cannot enter the value in External ID3";
					return false;
				}
			}

			//fill Individual Holder Type details.
			if(mapGeneralDetails.get("HolderType")!=null && HolderType.equalsIgnoreCase("Individual")){

				//wait for salutation
				bStatus = Wait.waitForElementVisibility(Global.driver, By.id("s2id_fkSalutationIdPk"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "General Details is not displyed for the Holder type: Individual";
					return false;
				}

				//select the salutation
				if(mapGeneralDetails.get("Salutation")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Salutation",mapGeneralDetails.get("Salutation"));
					if(!bStatus){
						return false;
					}
				}

				//select the First name
				if(mapGeneralDetails.get("FirstName")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("individualFirstName"), mapGeneralDetails.get("FirstName"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the First Name field";
						return false;
					}
				}

				//select the Middle name
				if(mapGeneralDetails.get("MiddleName")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("individualMiddleInitials"), mapGeneralDetails.get("MiddleName"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Middle Name field";
						return false;
					}
				}

				//select the Last name
				if(mapGeneralDetails.get("LastName")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("individualLastName"), mapGeneralDetails.get("LastName"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Last Name field";
						return false;
					}
				}

				//select the Sex
				if(mapGeneralDetails.get("Sex")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Sex",mapGeneralDetails.get("Sex"));
					if(!bStatus){
						return false;
					}
				}

				//enter the DOB
				if(mapGeneralDetails.get("DateOfBirth")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("dateOfBirth"), mapGeneralDetails.get("DateOfBirth"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the DOB field";
						return false;
					}
					NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
				}

				//enter the lace of birth
				if(mapGeneralDetails.get("PlaceOfBirth")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("placeOfBirth"), mapGeneralDetails.get("PlaceOfBirth"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the place of birth field";
						return false;
					}
				}

				//select the currency type
				if(mapGeneralDetails.get("ReportingCurrency")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Reporting Currency Preference",mapGeneralDetails.get("ReportingCurrency"));
					if(!bStatus){
						return false;
					}
				}

				//enter the pp number
				if(mapGeneralDetails.get("PassPortNumber")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("passportNo"), mapGeneralDetails.get("PassPortNumber"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the pp number field";
						return false;
					}
				}

				//enter the pp ExpiryDate
				if(mapGeneralDetails.get("PassportExpiry")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("passportExpiryDate"), mapGeneralDetails.get("PassportExpiry"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the pp expiry date field";
						return false;
					}
					NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
				}

				//enter the pp ExpiryDate
				if(mapGeneralDetails.get("RiskLevel")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Risk Level",mapGeneralDetails.get("RiskLevel"));
					if(!bStatus){
						return false;
					}
				}	
				if(mapGeneralDetails.get("TaxID")!=null && mapGeneralDetails.get("TaxState")!=null && mapGeneralDetails.get("TaxCountry")!=null){
					//remove all tax id if availble any
					bStatus = removeAllTaxIDs("Individual");
					if(!bStatus){
						return false;
					}
					//add tax details for entity
					bStatus = fillTaxesDetailsForHolderType(mapGeneralDetails.get("TaxID"),mapGeneralDetails.get("TaxState"),mapGeneralDetails.get("TaxCountry"));
					if(!bStatus){
						return false;
					}
				}
			}

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean removeAllTaxIDs(String sHolderType){
		try{
			String sRemoveHolderTypeTaxCount = "";
			String sAddHolderTypeTax = "";
			if (sHolderType.equalsIgnoreCase("Individual")) {
				sRemoveHolderTypeTaxCount = "//button[contains(@onclick,'removeIndvTaxHolder')]";
				sAddHolderTypeTax = "//button[contains(@onclick,'addIndvTaxHolder')]";
			}
			else if (sHolderType.equalsIgnoreCase("Entity")) {
				sRemoveHolderTypeTaxCount = "//button[contains(@onclick,'removeEntityTaxHolder')]";
				sAddHolderTypeTax = "//button[contains(@onclick,'addEntityTaxHolder')]";
			}
			int iRemovebtnCount = Elements.getXpathCount(Global.driver,By.xpath(sRemoveHolderTypeTaxCount));
			/*if (iRemovebtnCount == 0) {
				NewUICommonFunctions.jsClick(By.xpath(sAddHolderTypeTax));
				TimeUnit.SECONDS.sleep(2);
				if (sHolderType.equalsIgnoreCase("Individual")) {
					NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'removeIndvTaxHolder') and contains(@onclick,'1')]"));
				}
				else if (sHolderType.equalsIgnoreCase("Entity")) {
					NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'removeEntityTaxHolder') and contains(@onclick,'1')]"));
				}			
			}*/
			for (int i = iRemovebtnCount; i >= 1 ; i--) {
				if (sHolderType.equalsIgnoreCase("Individual")) {
					NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'removeIndvTaxHolder') and contains(@onclick,'"+i+"')]"));
				}else if (sHolderType.equalsIgnoreCase("Entity")) {
					NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'removeEntityTaxHolder') and contains(@onclick,'"+i+"')]"));
				}			
			}
			iRemovebtnCount = Elements.getXpathCount(Global.driver,By.xpath(sRemoveHolderTypeTaxCount));
			if(iRemovebtnCount >= 1){
				Messages.errorMsg = " [ ERROR : Additional Tax details wasn't removed when click on respective Remove buttons for sHolderType : "+sHolderType+" .] \n";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean fillTaxesDetailsForHolderType(String TaxIds,String States,String Countries){
		try{

			String[] arrTaxIDs = TaxIds.split(",");
			String[] arrStates = States.split(",");
			String[] arrCountries = Countries.split(",");
			/*String replaceValue="";

			if(investorType.equalsIgnoreCase("entity")){
				replaceValue = "Domicile";
			}

			if(investorType.equalsIgnoreCase("individual")){
				replaceValue = "Tax";
			}*/

			if(!((arrTaxIDs.length == arrStates.length) && (arrCountries.length == arrTaxIDs.length) && (arrStates.length == arrCountries.length))){
				Messages.errorMsg = " [ TEST DATA ERROR : Count mismatch with taxid, domicile state and domicile country. ] \n";
				return false;
			}

			for (int iValue = 0; iValue < arrCountries.length; iValue++) {
				//enter the tax value in the text box
				if(!arrTaxIDs[iValue].equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.id("taxHId_"+iValue), arrTaxIDs[iValue]);
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : For the tax text box index:"+iValue+", value cannot be entered. ] \n";
						return false;
					}
				}
				//enter the domicile state name
				if(!arrStates[iValue].equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[contains(@id,'fkInvestorTaxDetails"+iValue+"') and @type='text']"), arrStates[iValue]);
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : For the Domicile state text box index:"+iValue+", value cannot be entered. ] \n";
						return false;
					}
				}
				//enter the domicile country name
				if(!arrCountries[iValue].equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(arrCountries[iValue], By.xpath("//div[contains(@id,'s2id_fkInvestorTaxDetails"+iValue+"')]//span[contains(@id,'select2-chosen')]"));
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : For the Domicile state text box index:"+iValue+", value cannot be selected. ] \n";
						return false;
					}
				}
				if(iValue < arrCountries.length-1){
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'add') and contains(@id,'addMoreTax')]"));
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : Cannot click on Add More button. ] \n";
						return false;
					}
				}
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	public static boolean  doFillCorrespondenceAddressDetails(Map<String, String> mapCorrespondenceAddr){
		try{
			bStatus = NewUICommonFunctions.selectTheTab("Address of Correspondence");
			if(!bStatus){
				return false;
			}
			Wait.waitForElementVisibility(Global.driver, By.id("corBuildingArea"), 5);
			if(!HolderType.equalsIgnoreCase("")){
				//enter the building name
				if(mapCorrespondenceAddr.get("Building")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corBuildingArea"), mapCorrespondenceAddr.get("Building"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the building field";
						return false;
					}
				}

				//enter the Floor name
				if(mapCorrespondenceAddr.get("Floor")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corFloorSuiteApt"), mapCorrespondenceAddr.get("Floor"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Floor field";
						return false;
					}
				}

				//enter the Strret number
				if(mapCorrespondenceAddr.get("StreetNumber")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corStreetNo"), mapCorrespondenceAddr.get("StreetNumber"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Strret Number field";
						return false;
					}
				}

				//enter the Street name
				if(mapCorrespondenceAddr.get("StreetName")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corStreetName"), mapCorrespondenceAddr.get("StreetName"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Strret Name field";
						return false;
					}
				}

				//enter the city name
				if(mapCorrespondenceAddr.get("City")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corCity"), mapCorrespondenceAddr.get("City"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the CIty field";
						return false;
					}
				}

				//enter the state name
				if(mapCorrespondenceAddr.get("State")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corState"), mapCorrespondenceAddr.get("State"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the State field";
						return false;
					}
				}

				//Select the Country name
				if(mapCorrespondenceAddr.get("Country")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Country",mapCorrespondenceAddr.get("Country"));
					if(!bStatus){
						return false;
					}
				}

				//enter the PIN 
				if(mapCorrespondenceAddr.get("PostalCode")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corPostalCode"), mapCorrespondenceAddr.get("PostalCode"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Postal code field";
						return false;
					}
				}

				//enter the Email1 
				if(mapCorrespondenceAddr.get("EmailAddr1")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corEmailAddress1"), mapCorrespondenceAddr.get("EmailAddr1"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Email Address1 field";
						return false;
					}
				}

				//enter the Email2
				if(mapCorrespondenceAddr.get("EmailAddr2")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corEmailAddress2"), mapCorrespondenceAddr.get("EmailAddr2"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Email Address2 field";
						return false;
					}
				}

				//enter the phone1
				if(mapCorrespondenceAddr.get("Phone1")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corPhone1"), mapCorrespondenceAddr.get("Phone1"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the phone 1 field";
						return false;
					}
				}

				//enter the phone2
				if(mapCorrespondenceAddr.get("Phone2")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corPhone2"), mapCorrespondenceAddr.get("Phone2"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the phone 2 field";
						return false;
					}
				}

				//enter the Fax
				if(mapCorrespondenceAddr.get("Fax")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("corFax"), mapCorrespondenceAddr.get("Fax"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Fax field";
						return false;
					}
				}
				/*-------------------------------------------------------------------------------------------
      		//  Order Ack check box.
				if(mapCorrespondenceAddr.get("OA")!=null){
					if(mapCorrespondenceAddr.get("OA").equalsIgnoreCase("No")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='OA']/preceding-sibling::div[1]//input"));
						if(bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='OA']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "OA check box  is not Clicked ";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("OA").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='OA']/preceding-sibling::div[1]//input"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='OA']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "OA check box  is not Clicked ";
								return false;
							}
						}
					}
				}

				//  CN check box.
				if(mapCorrespondenceAddr.get("CN")!=null){
					if(mapCorrespondenceAddr.get("CN").equalsIgnoreCase("No")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='CN']/preceding-sibling::div[1]//input"));
						if(bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='CN']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "CN check box  is not Clicked ";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("CN").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='CN']/preceding-sibling::div[1]//input"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='CN']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "CN check box  is not Clicked ";
								return false;
							}
						}
					}
				}

				//  Statemt Ack check box.
				if(mapCorrespondenceAddr.get("Statement")!=null){
					if(mapCorrespondenceAddr.get("Statement").equalsIgnoreCase("No")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Statement']/preceding-sibling::div[1]//input"));
						if(bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Statement']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Statement check box  is not Clicked ";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("Statement").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Statement']/preceding-sibling::div[1]//input"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Statement']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Statement check box  is not Clicked ";
								return false;
							}
						}
					}
				}

				//  Statemt Ack check box.
				if(mapCorrespondenceAddr.get("CancelNote")!=null){
					if(mapCorrespondenceAddr.get("CancelNote").equalsIgnoreCase("No")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Cancel Note']/preceding-sibling::div[1]//input"));
						if(bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Cancel Note']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Cancel Note check box  is not Clicked ";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("CancelNote").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Cancel Note']/preceding-sibling::div[1]//input"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Cancel Note']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Cancel Note check box  is not Clicked ";
								return false;
							}
						}
					}
				}
				------------------------------------------------------------------------------------
				 */


				// Courier check box.

				if(mapCorrespondenceAddr.get("Courier")!=null){
					boolean bCheckStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[normalize-space()='Courier' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input//parent::span[@class='checked']"));
					if(mapCorrespondenceAddr.get("Courier").equalsIgnoreCase("No")){						
						if(bCheckStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Courier' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input"));
							if(!bStatus){
								Messages.errorMsg = " [ ERROR : Courier check box  is not Clicked to uncheck it. ]";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("Courier").equalsIgnoreCase("Yes")){
						if(!bCheckStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Courier' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input"));
							if(!bStatus){
								Messages.errorMsg = " [ ERROR : Courier check box  is not Clicked to check it. ]";
								return false;
							}
						}
					}
				}

				if(mapCorrespondenceAddr.get("Email")!=null){
					boolean bCheckStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[normalize-space()='Email' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input//parent::span[@class='checked']"));
					if(mapCorrespondenceAddr.get("Email").equalsIgnoreCase("No")){
						if(bCheckStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Email' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input"));
							if(!bStatus){
								Messages.errorMsg = " [ ERROR : Email check box  is not Clicked to uncheck it. ]";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("Email").equalsIgnoreCase("Yes")){
						if(!bCheckStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Email' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input"));
							if(!bStatus){
								Messages.errorMsg = " [ ERROR : Email check box  is not Clicked to check it. ] ";
								return false;
							}
						}
						if (mapCorrespondenceAddr.get("EmailForDistribution") != null) {
							if(mapCorrespondenceAddr.get("EmailForDistribution").equalsIgnoreCase("Both")){
								bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='Both']//input[contains(@id,'rdisEmail')]"));
							}
							if(mapCorrespondenceAddr.get("EmailForDistribution").equalsIgnoreCase("Email1")){
								bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='Email1']//input[contains(@id,'rdisEmail')]"));
							}
							if(mapCorrespondenceAddr.get("EmailForDistribution").equalsIgnoreCase("Email2")){
								bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='Email2']//input[contains(@id,'rdisEmail')]"));
							}
						}
					}					
				}

				if(mapCorrespondenceAddr.get("Fax Check")!=null){
					boolean bCheckStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[normalize-space()='Fax' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input//parent::span[@class='checked']"));
					if(mapCorrespondenceAddr.get("Fax Check").equalsIgnoreCase("No")){
						if(bCheckStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Fax' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input"));
							if(!bStatus){
								Messages.errorMsg = " [ ERROR : Fax check box  is not Clicked to uncheck it. ]";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("Fax Check").equalsIgnoreCase("Yes")){
						if(!bCheckStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Fax' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input"));
							if(!bStatus){
								Messages.errorMsg = " [ ERROR : Fax check box  is not Clicked to check it. ] ";
								return false;
							}
						}
					}
				}

				if(mapCorrespondenceAddr.get("Web")!=null){
					boolean bCheckStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[normalize-space()='Web' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input//parent::span[@class='checked']"));
					if(mapCorrespondenceAddr.get("Web").equalsIgnoreCase("No")){
						if(bCheckStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Web' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input"));
							if(!bStatus){
								Messages.errorMsg = " [ ERROR : Web check box  is not Clicked to uncheck it. ]";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("Web").equalsIgnoreCase("Yes")){
						if(!bCheckStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Web' and  contains(@for,'dist')]//preceding-sibling::div[1]//span//input"));
							if(!bStatus){
								Messages.errorMsg = " [ ERROR : Web check box  is not Clicked to check it . ]";
								return false;
							}
						}
					}
				}

				/*if(mapCorrespondenceAddr.get("Email")!=null && mapCorrespondenceAddr.get("EmailForDistribution")!=null){
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='"+mapCorrespondenceAddr.get("EmailForDistribution")+"']//input"));
					if(!bStatus){
						Messages.errorMsg = mapCorrespondenceAddr.get("EmailForDistribution")+" cannot check the radio button";
						return false;
					}
				}*/

				if(HolderType.equalsIgnoreCase("Entity") && mapCorrespondenceAddr.get("CorrespondenceAndRegisteredAddrSame")!=null){
					boolean bCheckStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='copyAddress']//parent::span[@class='checked']"));
					if(mapCorrespondenceAddr.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("No")){
						if(bCheckStatus){
							bStatus = Elements.click(Global.driver,By.id("copyAddress"));
							if(!bStatus){
								Messages.errorMsg = " [ ERROR : CorrespondenceAndRegisteredAddrSame check box  is not Clicked to uncheck it. ]";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes")){
						if(!bCheckStatus){
							bStatus = Elements.click(Global.driver,By.id("copyAddress"));
							if(!bStatus){
								Messages.errorMsg = " [ ERROR : CorrespondenceAndRegisteredAddrSame check box  is not Clicked to check it. ] ";
								return false;
							}
						}
					}
				}
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();return false;
		}
	}	

	public static boolean doFillRegisteredAddressDetails(Map<String, String> mapRegisteredAddressDetails){
		try{
			if (HolderType.equalsIgnoreCase("") || !HolderType.equalsIgnoreCase("Entity")) {
				return true;
			}
			bStatus = NewUICommonFunctions.selectTheTab("Registered Address Details");
			TimeUnit.SECONDS.sleep(3);
			if(!bStatus){
				return false;
			}
			if(!HolderType.equalsIgnoreCase("") && HolderType.equalsIgnoreCase("Entity")){
				//enter the building name
				if(mapRegisteredAddressDetails.get("Building")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("buildingArea"), mapRegisteredAddressDetails.get("Building"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the building field. ]";
						return false;
					}
				}

				//enter the Floor name
				if(mapRegisteredAddressDetails.get("Floor")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("floorSuiteApt"), mapRegisteredAddressDetails.get("Floor"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the Floor field . ]";
						return false;
					}
				}

				//enter the Strret number
				if(mapRegisteredAddressDetails.get("StreetNumber")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("streetNo"), mapRegisteredAddressDetails.get("StreetNumber"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the Strret Number field. ]";
						return false;
					}
				}

				//enter the Street name
				if(mapRegisteredAddressDetails.get("StreetName")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("streetName"), mapRegisteredAddressDetails.get("StreetName"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the Strret Name field. ]";
						return false;
					}
				}

				//enter the city name
				if(mapRegisteredAddressDetails.get("City")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("city"), mapRegisteredAddressDetails.get("City"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the CIty field. ]";
						return false;
					}
				}

				//enter the state name
				if(mapRegisteredAddressDetails.get("State")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("state"), mapRegisteredAddressDetails.get("State"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the State field. ]";
						return false;
					}
				}

				//Select the Country name
				if(mapRegisteredAddressDetails.get("Country")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapRegisteredAddressDetails.get("Country"), By.xpath("//div[@id='s2id_fkCountryIdPk']//span[contains(@id,'select2-chosen')]"));
					//bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Country",mapRegisteredAddressDetails.get("Country"));
					if(!bStatus){
						return false;
					}
				}

				//enter the PIN 
				if(mapRegisteredAddressDetails.get("PostalCode")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("postalCode"), mapRegisteredAddressDetails.get("PostalCode"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the Postal code field. ]";
						return false;
					}
				}

				//enter the Email1 
				if(mapRegisteredAddressDetails.get("EmailAddr1")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("emailAddress1"), mapRegisteredAddressDetails.get("EmailAddr1"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the Email Address1 field. ]";
						return false;
					}
				}

				//enter the Email2
				if(mapRegisteredAddressDetails.get("EmailAddr2")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("emailAddress2"), mapRegisteredAddressDetails.get("EmailAddr2"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the Email Address2 field. ]";
						return false;
					}
				}

				//enter the phone1
				if(mapRegisteredAddressDetails.get("Phone1")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("phone1"), mapRegisteredAddressDetails.get("Phone1"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the phone 1 field. ]";
						return false;
					}
				}

				//enter the phone2
				if(mapRegisteredAddressDetails.get("Phone2")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("phone2"), mapRegisteredAddressDetails.get("Phone2"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the phone 2 field. ]";
						return false;
					}
				}

				//enter the Fax
				if(mapRegisteredAddressDetails.get("Fax")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("fax"), mapRegisteredAddressDetails.get("Fax"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Cannot enter value in the Fax field. ]";
						return false;
					}
				}

				return true;
			}
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doFillHolderTabDetails(Map<String, String> mapHolderDetails){
		try {
			NewUICommonFunctions.selectTheTab("Holder Details");
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='tab4' and contains(@class,'active')]"), 5);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to navigate to Holder Details tab.]";
				return false;
			}
			if (mapHolderDetails.get("ErisaInvestor") != null) {
				if (mapHolderDetails.get("ErisaInvestor").equalsIgnoreCase("Yes")) {
					bStatus = Elements.click(Global.driver, By.id("isErisaInvestor1"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unablo to click the isErisaInvestor : Yes ] ";
						return false;
					}
				}
				if (mapHolderDetails.get("ErisaInvestor").equalsIgnoreCase("No")) {
					bStatus = Elements.click(Global.driver, By.id("isErisaInvestor2"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unablo to click the isErisaInvestor : No ] ";
						return false;
					}
				}
			}
			if (mapHolderDetails.get("SidePocketAllowed") != null) {
				if (mapHolderDetails.get("SidePocketAllowed").equalsIgnoreCase("Yes")) {
					bStatus = Elements.click(Global.driver, By.id("isSidePocketAllowed1"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unablo to click the SidePocketAllowed : Yes ] ";
						return false;
					}
				}
				if (mapHolderDetails.get("SidePocketAllowed").equalsIgnoreCase("No")) {
					bStatus = Elements.click(Global.driver, By.id("isSidePocketAllowed2"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unablo to click the SidePocketAllowed : No ] ";
						return false;
					}
				}
			}
			if (!HolderType.equalsIgnoreCase("") && HolderType.equalsIgnoreCase("Individual") && mapHolderDetails.get("InvestorSubType") != null) {
				if (mapHolderDetails.get("InvestorSubType").equalsIgnoreCase("Accredited Investor")) {
					bStatus = Elements.click(Global.driver, By.id("investorSubType1"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unablo to click the InvestorSubType : Accredited Investor ] ";
						return false;
					}
				}
				if (mapHolderDetails.get("InvestorSubType").equalsIgnoreCase("Qualified Purchaser")) {
					bStatus = Elements.click(Global.driver, By.id("investorSubType2"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unablo to click the InvestorSubType : Qualified Purchaser ] ";
						return false;
					}
				}
				if (mapHolderDetails.get("InvestorSubType").equalsIgnoreCase("NA")) {
					bStatus = Elements.click(Global.driver, By.id("investorSubType3"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unablo to click the InvestorSubType : NA ] ";
						return false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean doFillFatcaDetails(Map<String, String> mapFatcaDeatils){
		try{
			bStatus = NewUICommonFunctions.selectTheTab("FATCA Details");
			bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[@id='tab5' and contains(@class,'active')]"), 5);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to navigate to Fatca Details tab.]";
				return false;
			}
			if(!HolderType.equalsIgnoreCase("")){

				if(mapFatcaDeatils.get("PowerAttorneyToUS")!=null){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'Power of Attorney to US person')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("PowerAttorneyToUS")+"']//input"));
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : "+mapFatcaDeatils.get("PowerAttorneyToUS")+" : option cannot be selected for radio button field name 'power of attorney to US person' ]";
						return false;
					}
				}

				if(mapFatcaDeatils.get("SignatoryAuthorityPersonInUS")!=null){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'Signatory Authority to person in US')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("SignatoryAuthorityPersonInUS")+"']//input"));
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : "+mapFatcaDeatils.get("SignatoryAuthorityPersonInUS")+" : option cannot be selected for radio button field name 'Sign Authority to US person' ]";
						return false;
					}
				}

				if(mapFatcaDeatils.get("StandingPaymentInstructionsToUSAccount")!=null){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'Standing Payment Instructions to US account')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("StandingPaymentInstructionsToUSAccount")+"']//input"));
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : "+mapFatcaDeatils.get("StandingPaymentInstructionsToUSAccount")+" : option cannot be selected for radio button field name 'Standing payment instructions to US account' ]";
						return false;
					}
				}

				if(mapFatcaDeatils.get("Level1Classification")!=null){
					TimeUnit.SECONDS.sleep(2);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Level 1 Classification",mapFatcaDeatils.get("Level1Classification") );
					if(!bStatus){
						return false;
					}					
				}
				if(mapFatcaDeatils.get("Level2Classification")!=null){
					TimeUnit.SECONDS.sleep(3);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Level 2 Classification",mapFatcaDeatils.get("Level2Classification") );
					if(!bStatus){
						return false;
					}
				}

				if(mapFatcaDeatils.get("FATCAClassification")!=null){
					TimeUnit.SECONDS.sleep(3);
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("FATCA Classification",mapFatcaDeatils.get("FATCAClassification") );
					if(!bStatus){
						return false;
					}
				}

				if(HolderType.equalsIgnoreCase("Entity")){
					if(mapFatcaDeatils.get("UnderLyingBeneficialOwner")!=null){
						bStatus = Elements.enterText(Global.driver,By.id("investorFatcaDetails0.underlyingBeneficalOwner"),mapFatcaDeatils.get("UnderLyingBeneficialOwner"));
						if(!bStatus){
							Messages.errorMsg = " [ ERROR : Text cannot be entered in 'Underlying Beneficial Owner' field ]";
							return false;
						}
					}

					if(mapFatcaDeatils.get("OwnerShipPercentage")!=null){
						bStatus = Elements.enterText(Global.driver,By.id("benef_owner"),mapFatcaDeatils.get("OwnerShipPercentage"));
						if(!bStatus){
							Messages.errorMsg = " [ ERROR : Text cannot be entered in 'owner ship percentage' field ]";
							return false;
						}					
					}
				}

				if(HolderType.equalsIgnoreCase("Individual")){
					if(mapFatcaDeatils.get("USPerson")!=null){
						bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'US Person')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("USPerson")+"']//input"));
						if(!bStatus){
							Messages.errorMsg = " [ ERROR : "+mapFatcaDeatils.get("USPerson")+" option cannot be selected for radio button field name 'US person' ]";
							return false;
						}
					}

					if(mapFatcaDeatils.get("CountryOfBirth")!=null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Country Of Birth",mapFatcaDeatils.get("CountryOfBirth") );
						if(!bStatus){
							return false;
						}
					}

					if(mapFatcaDeatils.get("PresentNationality")!=null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Present Nationality",mapFatcaDeatils.get("PresentNationality") );
						if(!bStatus){
							return false;
						}
					}
				}

				if(mapFatcaDeatils.get("FATCADocumentation")!=null){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'FATCA Documentation')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("FATCADocumentation")+"']//input"));
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : "+mapFatcaDeatils.get("FATCADocumentation")+" option cannot be selected for radio button field name 'FATCA documentation' ]";
						return false;
					}
				}

				if(mapFatcaDeatils.get("DocumentsAvailable")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Documents Available",mapFatcaDeatils.get("DocumentsAvailable") );
					if(!bStatus){
						return false;
					}
				}

				if(mapFatcaDeatils.get("DocumentationExpiryDate")!=null){
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("investorFatcaDetails0.fatcaDocumentExpiryDate"),mapFatcaDeatils.get("DocumentationExpiryDate"));
					if(!bStatus){
						Messages.errorMsg = "Text cannot be entered in Documentation Expiry Date";
						return false;
					}
					NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
				}
				return true;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyKYCDetails(Map<String, String> mapKYCDetails){
		String sAppendMsg = "";
		boolean bValidateStatus = true;
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("KYC Details");
			if(!bStatus){
				sAppendMsg = sAppendMsg+" [ERROR: KYC Details Tab is not Clicked ]\n"; 
				bValidateStatus = false;
			}
			if (mapKYCDetails.get("TemplateName") != null) {
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='kycTemplateId']//parent::div[normalize-space(text())='"+mapKYCDetails.get("TemplateName")+"']"), 5);
				if (!bStatus) {
					sAppendMsg = sAppendMsg + " [ ERROR : KYC Details selected Template Name : "+mapKYCDetails.get("TemplateName")+", wasn't visible. ] \n";
					bValidateStatus = false;
				}
			}
			if (mapKYCDetails.get("ProofName") != null) {
				List<String> ProofNames = Arrays.asList(mapKYCDetails.get("ProofName").toLowerCase().split(","));	
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//input[@id='proofListSize']"), 5);
				if (!bStatus) {
					sAppendMsg = sAppendMsg + " [ ERROR : proofListSize wasn't present. ] ";
					bValidateStatus = false;
				}
				int proofCount = Integer.parseInt(Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='proofListSize']"), "value").trim());
				//Loop over PROOFS.
				for(int index = 0 ; index < proofCount; index++){
					//Dealing with PROOFS.
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='isProofSelected_"+index+"']//..//following::td//div[@id='document"+index+"']"));
					String attrProofName = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='isProofSelected_"+index+"']//following::input[contains(@name,'proofName')]"), "value").trim().toLowerCase();					
					if (attrProofName == null) {
						sAppendMsg = sAppendMsg + " [ ERROR : Unable to get the attribute to verify the selected Proof and respective doc's . ] ";
						bValidateStatus = false;
						continue;
					}
					if (ProofNames.contains(attrProofName.trim()) && !bStatus) {
						sAppendMsg = sAppendMsg + " [ ERROR : Proof Name : "+attrProofName+" , which was marked while creation of Investor isn't appeared to be marked in verification. ] \n";
						bValidateStatus = false;
						continue;
					}
					if (!ProofNames.contains(attrProofName.trim()) && bStatus) {
						if (!bStatus) {
							sAppendMsg = sAppendMsg + " [ ERROR : Proof Name : "+attrProofName+" , which wasn't marked while creation of Investor is appeared to be marked in verification. ] \n";
							bValidateStatus = false;
							continue;
						}
					}
					//dealing with DOC's.
					if(mapKYCDetails.get("DocumentName") != null){
						List<String> docIndexBasedInSheet = Arrays.asList(mapKYCDetails.get("DocumentName").toLowerCase().split(":"));
						List<String> DocumentNames = Arrays.asList(docIndexBasedInSheet.get(index).toLowerCase().split(","));
						int iDocIndexCount = Integer.parseInt(Elements.getElementAttribute(Global.driver, By.id("docListSize_"+index+""), "value").trim());
						//Loop over DOCUMENTS.
						for (int docIndex = 0; docIndex < iDocIndexCount; docIndex++) {
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='docDetails_"+index+"_"+docIndex+"']"));
							String attrDocName = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='isDocumentSelected_"+index+"_"+docIndex+"']//..//..//following-sibling::input[contains(@name,'documentName')]"), "value").trim().toLowerCase();					
							if (attrDocName == null) {
								sAppendMsg = sAppendMsg + " [ ERROR : Unable to get the attribute to verify the selected Doc : "+mapKYCDetails.get("DocumentName")+", and respective fields . ] \n";
								bValidateStatus = false;
								continue;
							}
							if (DocumentNames.contains(attrDocName.trim()) && !bStatus) {
								sAppendMsg = sAppendMsg + " [ ERROR : The Document : "+mapKYCDetails.get("DocumentName")+", which was selected during Adding of an Investor, isn't marked as selected when verifying the same. ] \n";
								bValidateStatus = false;
								continue;
							}
							if (!DocumentNames.contains(attrDocName.trim()) && bStatus) {
								sAppendMsg = sAppendMsg + " [ ERROR : The Document : "+mapKYCDetails.get("DocumentName")+", which was NOT selected during Adding of an Investor, is appeared to be marked as selected when verifying the same. ] \n";
								bValidateStatus = false;
								continue;
							}
							if (DocumentNames.contains(attrDocName.trim()) && bStatus) {
								if (mapKYCDetails.get("DocumentReceived") != null) {
									int isDocumentReceivedIndex = DocumentNames.indexOf(attrDocName.trim());
									List<String> isDocumentReceivedDocBasedValues = Arrays.asList(mapKYCDetails.get("DocumentReceived").split(":"));
									List<String> isDocumentReceivedValues = Arrays.asList(isDocumentReceivedDocBasedValues.get(index).split(","));
									if (isDocumentReceivedValues.get(isDocumentReceivedIndex) != null && isDocumentReceivedValues.get(isDocumentReceivedIndex).equalsIgnoreCase("Yes")) {
										bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].isDocumentReceived' and @value='1']//parent::span[@class='checked']"));
										if (!bStatus) {
											sAppendMsg = sAppendMsg + " [ ERROR : The Document Received status for : "+mapKYCDetails.get("DocumentName")+", which was NOT marked to YES when verifying the same. ] \n";
											bValidateStatus = false;
										}
										if (attrDocName.contains("passport") && bStatus) {
											if(mapKYCDetails.get("PassportRecieveDate") != null && !mapKYCDetails.get("PassportRecieveDate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[0].docDetailsValue']"), mapKYCDetails.get("PassportRecieveDate"), "Yes");
												//bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[0].docDetailsValue']"), mapKYCDetails.get("PassportRecieveDate"));
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("PassportDocumentDate") != null && !mapKYCDetails.get("PassportDocumentDate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[1].docDetailsValue']"), mapKYCDetails.get("PassportDocumentDate"), "Yes");
												//bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[1].docDetailsValue']"), mapKYCDetails.get("PassportDocumentDate"));
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("PassportExpiryDate") != null && !mapKYCDetails.get("PassportExpiryDate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[2].docDetailsValue']"), mapKYCDetails.get("PassportExpiryDate"), "Yes");
												//bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[2].docDetailsValue']"), mapKYCDetails.get("PassportExpiryDate"));
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("PassportIssueplace") != null && !mapKYCDetails.get("PassportIssueplace").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[3].docDetailsValue']"), mapKYCDetails.get("PassportIssueplace"), "Yes");
												//bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[3].docDetailsValue']"), mapKYCDetails.get("PassportIssueplace"));
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("PassportIssuedate") != null && !mapKYCDetails.get("PassportIssuedate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].docDetailsValue']"), mapKYCDetails.get("PassportIssuedate"), "Yes");
												//bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].docDetailsValue']"), mapKYCDetails.get("PassportIssuedate"));
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											/*if(mapKYCDetails.get("PassportUploadfile") != null){
												//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']"), mapKYCDetails.get("PassportUploadfile"));												

												//Applying Javascript executor functions to enable the file input field to upload file.
												JavascriptExecutor jse = (JavascriptExecutor)Global.driver;												
												jse.executeScript("document.getElementsByClassName('"+Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@placeholder='Upload File']"), "class")+"')[0].removeAttribute('readonly', '');");
												Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@type='text']")).sendKeys(mapKYCDetails.get("PassportUploadfile"));


												if (!bStatus) {
													return false;
												}
											}*/
										}
										if (attrDocName.contains("electricity") && bStatus) {
											if(mapKYCDetails.get("ElectricityRecieveDate") != null && !mapKYCDetails.get("ElectricityRecieveDate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[0].docDetailsValue']"), mapKYCDetails.get("ElectricityRecieveDate"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("ElectricityDocumentDate") != null && !mapKYCDetails.get("ElectricityDocumentDate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[1].docDetailsValue']"), mapKYCDetails.get("ElectricityDocumentDate"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("ElectricityExpiryDate") != null && !mapKYCDetails.get("ElectricityExpiryDate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[2].docDetailsValue']"), mapKYCDetails.get("ElectricityExpiryDate"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("ElectricityBillNumber") != null && !mapKYCDetails.get("ElectricityBillNumber").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[3].docDetailsValue']"), mapKYCDetails.get("ElectricityBillNumber"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("ElectricityProviderName") != null && !mapKYCDetails.get("ElectricityProviderName").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].docDetailsValue']"), mapKYCDetails.get("ElectricityProviderName"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											/*if(mapKYCDetails.get("ElectricityUploadFile") != null){
												//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']"), mapKYCDetails.get("ElectricityUploadFile"));

												//Applying Javascript executor functions to enable file name field to upload file. 
												JavascriptExecutor jse = (JavascriptExecutor)Global.driver;												
												jse.executeScript("document.getElementsByClassName('"+Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@placeholder='Upload File']"), "class")+"')[0].removeAttribute('readonly', '');");
												Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@type='text']")).sendKeys(mapKYCDetails.get("ElectricityUploadFile"));
												if (!bStatus) {
													return false;
												}
											}*/
										}
										if ((attrDocName.contains("moa") || attrDocName.contains("aoa") || attrDocName.contains("operating agreement")) && bStatus) {
											if(mapKYCDetails.get("MOAOperatingAgreementRecieveDate") != null && !mapKYCDetails.get("MOAOperatingAgreementRecieveDate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[0].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementRecieveDate"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("MOAOperatingAgreementDocumentDate") != null && !mapKYCDetails.get("MOAOperatingAgreementDocumentDate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[1].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementDocumentDate"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("MOAOperatingAgreementExpiryDate") != null && !mapKYCDetails.get("MOAOperatingAgreementExpiryDate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[2].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementExpiryDate"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("MOAOperatingAgreementAgreementDate") != null && !mapKYCDetails.get("MOAOperatingAgreementAgreementDate").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[3].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementAgreementDate"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("MOAOperatingAgreementPlace") != null && !mapKYCDetails.get("MOAOperatingAgreementPlace").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementPlace"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											if(mapKYCDetails.get("MOAOperatingAgreementSignedBy") != null && !mapKYCDetails.get("MOAOperatingAgreementSignedBy").equalsIgnoreCase("None")){
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementSignedBy"), "Yes");												
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
											/*if(mapKYCDetails.get("MOAOperatingAgreementUploadfile") != null){
												//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].fileName']"), mapKYCDetails.get("MOAOperatingAgreementUploadfile"));												

												//Applying Javascript executor functions to enable file name field to upload file. 
												JavascriptExecutor jse = (JavascriptExecutor)Global.driver;																				
												jse.executeScript("document.getElementsByClassName('"+Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].fileName']//preceding-sibling::input[@placeholder='Upload File']"), "class")+"')[0].removeAttribute('readonly', '');");
												Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].fileName']//preceding-sibling::input[@type='text']")).sendKeys(mapKYCDetails.get("MOAOperatingAgreementUploadfile"));

												if (!bStatus) {
													return false;
												}
											}*/
										}
									}

									if (isDocumentReceivedValues.get(isDocumentReceivedIndex) != null && isDocumentReceivedValues.get(isDocumentReceivedIndex).equalsIgnoreCase("No")) {
										bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].isDocumentReceived' and @value='0']//parent::span[@class='checked']"));
										if (!bStatus) {
											sAppendMsg = sAppendMsg + "[ ERROR : For the Document : "+attrDocName+", Radio button wasn't marked as NO . ] \n";
											bValidateStatus = false;
											continue;
										}
										if (attrDocName.contains("passport")) {
											if (mapKYCDetails.get("PassportDocumentDueDate") != null && !mapKYCDetails.get("PassportDocumentDueDate").equalsIgnoreCase("None")) {
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].docDueDate']"), mapKYCDetails.get("PassportDocumentDueDate"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
										}
										if (attrDocName.contains("electricity")) {
											if (mapKYCDetails.get("ElectricityDocumentDueDate") != null && !mapKYCDetails.get("ElectricityDocumentDueDate").equalsIgnoreCase("None")) {
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].docDueDate']"), mapKYCDetails.get("ElectricityDocumentDueDate"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
										}
										if (attrDocName.contains("moa") || attrDocName.contains("aoa") || attrDocName.contains("operating agreement")) {
											if (mapKYCDetails.get("MOAOperatingAgreementDocumentDueDate") != null && !mapKYCDetails.get("MOAOperatingAgreementDocumentDueDate").equalsIgnoreCase("None")) {
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].docDueDate']"), mapKYCDetails.get("MOAOperatingAgreementDocumentDueDate"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
										}
									}
									if (isDocumentReceivedValues.get(isDocumentReceivedIndex) != null && isDocumentReceivedValues.get(isDocumentReceivedIndex).equalsIgnoreCase("Waiver")) {
										bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].isDocumentReceived' and @value='2']//parent::span[@class='checked']"));
										if (!bStatus) {
											sAppendMsg = sAppendMsg + "[ ERROR : For the Document : "+attrDocName+", Radio button wasn't marked as 'WAIVER' . ] \n";
											bValidateStatus = false;
											continue;
										}
										if (attrDocName.contains("passport")) {
											if (mapKYCDetails.get("PassportWaiverReason") != null && !mapKYCDetails.get("PassportWaiverReason").equalsIgnoreCase("None")) {
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//textarea[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].waiveReason']"), mapKYCDetails.get("PassportWaiverReason"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
										}
										if (attrDocName.contains("electricity")) {
											if (mapKYCDetails.get("ElectricityWaiverReason") != null && !mapKYCDetails.get("ElectricityWaiverReason").equalsIgnoreCase("None")) {
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//textarea[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].waiveReason']"), mapKYCDetails.get("ElectricityWaiverReason"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
										}
										if (attrDocName.contains("moa") || attrDocName.contains("aoa") || attrDocName.contains("operating agreement")) {
											if (mapKYCDetails.get("MOAOperatingAgreementWaiverReason") != null && !mapKYCDetails.get("MOAOperatingAgreementWaiverReason").equalsIgnoreCase("None")) {
												bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//textarea[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].waiveReason']"), mapKYCDetails.get("MOAOperatingAgreementWaiverReason"), "Yes");
												if (!bStatus) {
													sAppendMsg = sAppendMsg + Messages.errorMsg +" \n";
													bValidateStatus = false;
												}
											}
										}
									}
								}
							}
							/*if (!DocumentNames.contains(attrDocName.trim()) && bStatus) {
								bStatus = Elements.click(Global.driver, By.id("isDocumentSelected_"+index+"_"+docIndex+""));
								if (!bStatus) {
									return false;
								}
							}*/
						}
					}
				}
			}
			Messages.errorMsg = sAppendMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

	private static boolean verifyTextInTextBoxOrDropDown(By objLocator, String sExpectedText, String YesForAttrValNoForPlaceHolder) {
		try {
			String sReadAttrVal = "";
			if (YesForAttrValNoForPlaceHolder.equalsIgnoreCase("Yes")) {
				for (int i = 0; i < 4; i++) {
					sReadAttrVal = Elements.getElementAttribute(Global.driver, objLocator, "value");
					if (sReadAttrVal != null) {
						break;
					}
				}
				if (sReadAttrVal == null || sReadAttrVal.equalsIgnoreCase("") || !sReadAttrVal.trim().equalsIgnoreCase(sExpectedText)) {
					Messages.errorMsg = " [ ERROR : Expected value "+sExpectedText+" for : "+objLocator+" is not matching with actual : "+sReadAttrVal+" . ]";
					return false;
				}
				return true;
			}else if (YesForAttrValNoForPlaceHolder.equalsIgnoreCase("No")) {
				for (int i = 0; i < 4; i++) {
					sReadAttrVal = Elements.getText(Global.driver, objLocator);
					if (sReadAttrVal != null) {
						break;
					}
				}				
				if (sReadAttrVal == null || sReadAttrVal.equalsIgnoreCase("") || !sReadAttrVal.trim().equalsIgnoreCase(sExpectedText)) {
					Messages.errorMsg = " [ ERROR : Expected value "+sExpectedText+" for : "+objLocator+" is not matching with actual : "+sReadAttrVal+" . ]";
					return false;
				}
				return true;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	public static boolean doFillKYCDetails(Map<String, String> mapKYCDetails) {
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("KYC Details");
			if(!bStatus){
				return false;
			}
			if (mapKYCDetails.get("TemplateName") != null) {
				if (isHolderGeneralDetailsModifyFlag == false) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Template Name", mapKYCDetails.get("TemplateName"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : KYC Details Template name wasn't selected from dropdown. ] ";
						return false;
					}
				}				
			}
			if (mapKYCDetails.get("ProofName") != null) {
				List<String> ProofNames = Arrays.asList(mapKYCDetails.get("ProofName").toLowerCase().split(","));	
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//input[@id='proofListSize']"), 5);
				if (!bStatus) {
					Messages.errorMsg = " [ ERROR : proofListSize wasn't visible. ] ";
					return false;
				}
				int proofCount = Integer.parseInt(Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='proofListSize']"), "value").trim());
				//Loop over PROOFS.
				for(int index = 0 ; index < proofCount; index++){
					//Dealing with PROOFS.
					TimeUnit.SECONDS.sleep(3);
					//add SE
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='isProofSelected_"+index+"']//..//following::td//div[@id='document"+index+"']"));
					String attrProofName = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='isProofSelected_"+index+"']//following::input[contains(@name,'proofName')]"), "value").trim().toLowerCase();					
					if (attrProofName == null) {
						Messages.errorMsg = " [ ERROR : Unable to get the attribute to verify the selected Proof and respective doc's . ] ";
						return false;
					}
					if (ProofNames.contains(attrProofName.trim()) && !bStatus) {
						bStatus = Elements.click(Global.driver, By.id("isProofSelected_"+index+""));
						if (!bStatus) {
							return false;
						}
					}
					if (!ProofNames.contains(attrProofName.trim()) && bStatus) {
						bStatus = Elements.click(Global.driver, By.id("isProofSelected_"+index+""));
						if (!bStatus) {
							return false;
						}
						continue;
					}
					if (!ProofNames.contains(attrProofName.trim()) && !bStatus) {
						continue;
					}
					//dealing with DOC's.
					if(mapKYCDetails.get("DocumentName") != null){
						List<String> docIndexBasedInSheet = Arrays.asList(mapKYCDetails.get("DocumentName").toLowerCase().trim().split(":"));
						List<String> DocumentNames = Arrays.asList(docIndexBasedInSheet.get(index).toLowerCase().trim().split(","));
						int iDocIndexCount = Integer.parseInt(Elements.getElementAttribute(Global.driver, By.id("docListSize_"+index+""), "value").trim());
						//Loop over DOCUMENTS.
						for (int docIndex = 0; docIndex < iDocIndexCount; docIndex++) {
							//add SE
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='docDetails_"+index+"_"+docIndex+"']"));
							String attrDocName = Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='isDocumentSelected_"+index+"_"+docIndex+"']//following::input[contains(@name,'documentName')]"), "value").trim().toLowerCase();					
							if (attrDocName == null) {
								Messages.errorMsg = " [ ERROR : Unable to get the attribute to verify the selected Doc and respective fields . ] ";
								return false;
							}
							if (DocumentNames.contains(attrDocName) && !bStatus) {								
								bStatus = Elements.click(Global.driver, By.id("isDocumentSelected_"+index+"_"+docIndex+""));
								if (!bStatus) {
									return false;
								}
							}
							if (DocumentNames.contains(attrDocName) && bStatus) {								
								if (mapKYCDetails.get("DocumentReceived") != null && !mapKYCDetails.get("DocumentReceived").equalsIgnoreCase("None")) {
									int isDocumentReceivedIndex = DocumentNames.indexOf(attrDocName);
									if (isDocumentReceivedIndex == -1) {
										continue;
									}
									List<String> isDocumentReceivedDocBasedValues = Arrays.asList(mapKYCDetails.get("DocumentReceived").split(":"));
									List<String> isDocumentReceivedValues = Arrays.asList(isDocumentReceivedDocBasedValues.get(index).split(","));
									if (isDocumentReceivedValues.get(isDocumentReceivedIndex) != null && isDocumentReceivedValues.get(isDocumentReceivedIndex).equalsIgnoreCase("Yes")) {
										bStatus = Elements.click(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].isDocumentReceived' and @value='1']"));
										if (!bStatus) {
											return false;
										}
										if (attrDocName.contains("passport")) {
											if(mapKYCDetails.get("PassportRecieveDate") != null && !mapKYCDetails.get("PassportRecieveDate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[0].docDetailsValue']"), mapKYCDetails.get("PassportRecieveDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("PassportDocumentDate") != null && !mapKYCDetails.get("PassportDocumentDate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[1].docDetailsValue']"), mapKYCDetails.get("PassportDocumentDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("PassportExpiryDate") != null && !mapKYCDetails.get("PassportExpiryDate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[2].docDetailsValue']"), mapKYCDetails.get("PassportExpiryDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("PassportIssueplace") != null && !mapKYCDetails.get("PassportIssueplace").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[3].docDetailsValue']"), mapKYCDetails.get("PassportIssueplace"));
												if (!bStatus) {
													return false;
												}
											}
											if(mapKYCDetails.get("PassportIssuedate") != null && !mapKYCDetails.get("PassportIssuedate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].docDetailsValue']"), mapKYCDetails.get("PassportIssuedate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("PassportUploadfile") != null && !mapKYCDetails.get("PassportUploadfile").equalsIgnoreCase("None")){
												//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']"), mapKYCDetails.get("PassportUploadfile"));												

												//Applying Javascript executor functions to enable the file input field to upload file.
												JavascriptExecutor jse = (JavascriptExecutor)Global.driver;												
												jse.executeScript("document.getElementsByClassName('"+Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@placeholder='Upload File']"), "class")+"')[0].removeAttribute('readonly', '');");
												Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@type='text']")).sendKeys(mapKYCDetails.get("PassportUploadfile"));
											}
										}
										if (attrDocName.contains("electricity")) {
											if(mapKYCDetails.get("ElectricityRecieveDate") != null && !mapKYCDetails.get("ElectricityRecieveDate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[0].docDetailsValue']"), mapKYCDetails.get("ElectricityRecieveDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("ElectricityDocumentDate") != null && !mapKYCDetails.get("ElectricityDocumentDate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[1].docDetailsValue']"), mapKYCDetails.get("ElectricityDocumentDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("ElectricityExpiryDate") != null && !mapKYCDetails.get("ElectricityExpiryDate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[2].docDetailsValue']"), mapKYCDetails.get("ElectricityExpiryDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("ElectricityBillNumber") != null && !mapKYCDetails.get("ElectricityBillNumber").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[3].docDetailsValue']"), mapKYCDetails.get("ElectricityBillNumber"));
												if (!bStatus) {
													return false;
												}
											}
											if(mapKYCDetails.get("ElectricityProviderName") != null && !mapKYCDetails.get("ElectricityProviderName").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].docDetailsValue']"), mapKYCDetails.get("ElectricityProviderName"));
												if (!bStatus) {
													return false;
												}
											}
											if(mapKYCDetails.get("ElectricityUploadFile") != null && !mapKYCDetails.get("ElectricityUploadFile").equalsIgnoreCase("None")){
												//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']"), mapKYCDetails.get("ElectricityUploadFile"));

												//Applying Javascript executor functions to enable file name field to upload file. 
												JavascriptExecutor jse = (JavascriptExecutor)Global.driver;												
												jse.executeScript("document.getElementsByClassName('"+Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@placeholder='Upload File']"), "class")+"')[0].removeAttribute('readonly', '');");
												Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@type='text']")).sendKeys(mapKYCDetails.get("ElectricityUploadFile"));
											}
										}
										if (attrDocName.contains("moa") || attrDocName.contains("aoa") || attrDocName.contains("operating agreement")) {
											if(mapKYCDetails.get("MOAOperatingAgreementRecieveDate") != null && !mapKYCDetails.get("MOAOperatingAgreementRecieveDate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[0].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementRecieveDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("MOAOperatingAgreementDocumentDate") != null && !mapKYCDetails.get("MOAOperatingAgreementDocumentDate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[1].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementDocumentDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("MOAOperatingAgreementExpiryDate") != null && !mapKYCDetails.get("MOAOperatingAgreementExpiryDate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[2].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementExpiryDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("MOAOperatingAgreementAgreementDate") != null && !mapKYCDetails.get("MOAOperatingAgreementAgreementDate").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[3].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementAgreementDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
											if(mapKYCDetails.get("MOAOperatingAgreementPlace") != null && !mapKYCDetails.get("MOAOperatingAgreementPlace").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementPlace"));
												if (!bStatus) {
													return false;
												}
											}
											if(mapKYCDetails.get("MOAOperatingAgreementSignedBy") != null && !mapKYCDetails.get("MOAOperatingAgreementSignedBy").equalsIgnoreCase("None")){
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].docDetailsValue']"), mapKYCDetails.get("MOAOperatingAgreementSignedBy"));												
												if (!bStatus) {
													return false;
												}
											}
											if(mapKYCDetails.get("MOAOperatingAgreementUploadfile") != null && !mapKYCDetails.get("MOAOperatingAgreementUploadfile").equalsIgnoreCase("None")){
												//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].fileName']"), mapKYCDetails.get("MOAOperatingAgreementUploadfile"));												

												//Applying Javascript executor functions to enable file name field to upload file. 
												JavascriptExecutor jse = (JavascriptExecutor)Global.driver;																				
												jse.executeScript("document.getElementsByClassName('"+Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].fileName']//preceding-sibling::input[@placeholder='Upload File']"), "class")+"')[0].removeAttribute('readonly', '');");
												Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].fileName']//preceding-sibling::input[@type='text']")).sendKeys(mapKYCDetails.get("MOAOperatingAgreementUploadfile"));
											}
										}
									}
									if (isDocumentReceivedValues.get(isDocumentReceivedIndex) != null && isDocumentReceivedValues.get(isDocumentReceivedIndex).equalsIgnoreCase("No")) {
										NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
										bStatus = Elements.click(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].isDocumentReceived' and @value='0']"));
										if (!bStatus) {
											return false;
										}
										if (attrDocName.contains("passport")) {
											if (mapKYCDetails.get("PassportDocumentDueDate") != null && !mapKYCDetails.get("PassportDocumentDueDate").equalsIgnoreCase("None")) {
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].docDueDate']"), mapKYCDetails.get("PassportDocumentDueDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
										}
										if (attrDocName.contains("electricity")) {
											if (mapKYCDetails.get("ElectricityDocumentDueDate") != null && !mapKYCDetails.get("ElectricityDocumentDueDate").equalsIgnoreCase("None")) {
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].docDueDate']"), mapKYCDetails.get("ElectricityDocumentDueDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
										}
										if (attrDocName.contains("moa") || attrDocName.contains("aoa") || attrDocName.contains("operating agreement")) {
											if (mapKYCDetails.get("MOAOperatingAgreementDocumentDueDate") != null && !mapKYCDetails.get("MOAOperatingAgreementDocumentDueDate").equalsIgnoreCase("None")) {
												bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].docDueDate']"), mapKYCDetails.get("MOAOperatingAgreementDocumentDueDate"));
												if (!bStatus) {
													return false;
												}
												NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
											}
										}
									}
									if (isDocumentReceivedValues.get(isDocumentReceivedIndex) != null && isDocumentReceivedValues.get(isDocumentReceivedIndex).equalsIgnoreCase("Waiver")) {
										bStatus = Elements.click(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].isDocumentReceived' and @value='2']"));
										if (!bStatus) {
											return false;
										}
										if (attrDocName.contains("passport")) {
											if (mapKYCDetails.get("PassportWaiverReason") != null && !mapKYCDetails.get("PassportWaiverReason").equalsIgnoreCase("None")) {
												bStatus = Elements.enterText(Global.driver, By.xpath("//textarea[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].waiveReason']"), mapKYCDetails.get("PassportWaiverReason"));
												if (!bStatus) {
													return false;
												}
											}
										}
										if (attrDocName.contains("electricity")) {
											if (mapKYCDetails.get("ElectricityWaiverReason") != null && !mapKYCDetails.get("ElectricityWaiverReason").equalsIgnoreCase("None")) {
												bStatus = Elements.enterText(Global.driver, By.xpath("//textarea[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].waiveReason']"), mapKYCDetails.get("ElectricityWaiverReason"));
												if (!bStatus) {
													return false;
												}
											}
										}
										if (attrDocName.contains("moa") || attrDocName.contains("aoa") || attrDocName.contains("operating agreement")) {
											if (mapKYCDetails.get("MOAOperatingAgreementWaiverReason") != null && !mapKYCDetails.get("MOAOperatingAgreementWaiverReason").equalsIgnoreCase("None")) {
												bStatus = Elements.enterText(Global.driver, By.xpath("//textarea[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].waiveReason']"), mapKYCDetails.get("MOAOperatingAgreementWaiverReason"));
												if (!bStatus) {
													return false;
												}
											}
										}
									}
								}
							}
							if (!DocumentNames.contains(attrDocName.trim()) && bStatus) {
								bStatus = Elements.click(Global.driver, By.id("isDocumentSelected_"+index+"_"+docIndex+""));
								if (!bStatus) {
									return false;
								}
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

	public static boolean createNewHolder(Map<String, Map<String, String>> objHolderCreationTabsMaps) {		
		try {
			if (objHolderCreationTabsMaps != null) {			
				if (objHolderCreationTabsMaps.get("GeneralDetails") != null) {
					bStatus = doFillGeneralDetails(objHolderCreationTabsMaps.get("GeneralDetails"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unable to fill the details of General Details Tab and go further. ] "+Messages.errorMsg;							
						return false;
					}								
				}	
				
				if (objHolderCreationTabsMaps.get("AddressofCorrespondence") != null) {
					bStatus = doFillCorrespondenceAddressDetails(objHolderCreationTabsMaps.get("AddressofCorrespondence"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unable to fill the details of AddressofCorrespondence Tab. ] "+Messages.errorMsg;
						return false;
					}								
				}

				if (objHolderCreationTabsMaps.get("RegisteredAddressDetails") != null) {
					bStatus = doFillRegisteredAddressDetails(objHolderCreationTabsMaps.get("RegisteredAddressDetails"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unable to fill the details of RegisteredAddressDetails Tab. ] "+Messages.errorMsg;
						return false;
					}							
				}

				if (objHolderCreationTabsMaps.get("HolderDetails") != null) {
					bStatus = doFillHolderTabDetails(objHolderCreationTabsMaps.get("HolderDetails"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unable to fill the details of InvestorDetails Tab. ] "+Messages.errorMsg;
						return false;
					}								
				}

				if (objHolderCreationTabsMaps.get("FatcaDetails") != null) {
					bStatus = doFillFatcaDetails(objHolderCreationTabsMaps.get("FatcaDetails"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unable to fill the details of FatcaDetails Tab. ] "+Messages.errorMsg;
						return false;
					}								
				}

				if (objHolderCreationTabsMaps.get("KYCDetails") != null) {
					bStatus = doFillKYCDetails(objHolderCreationTabsMaps.get("KYCDetails"));
					if (!bStatus) {
						Messages.errorMsg = " [ ERROR : Unable to fill the details of KYCDetails Tab. ] "+Messages.errorMsg;
						return false;
					}								
				}
				
				if(bTradingSubscription){
					bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterTypeAndNotCheckMessage("Investor-Holder", objHolderCreationTabsMaps.get("GeneralDetails").get("OperationType"));
					if (!bStatus) {
						return false;
					}
					return true;
				}

				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Investor-Holder", objHolderCreationTabsMaps.get("GeneralDetails").get("OperationType"));
				if (!bStatus) {
					return false;
				}
			}				
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean modifyReturnHolderDetails(Map<String, Map<String, String>> objHolderModificationTabsMaps, String sHolderName) {
		try {
			bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sHolderName, "");
			if (!bStatus) {
				return false;
			}
			isHolderGeneralDetailsModifyFlag = true;

			bStatus = createNewHolder(objHolderModificationTabsMaps);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;	
	}

	public static boolean verifyAllTabsOfHolderMaster(Map<String, Map<String, String>> verifyMasterDetails) {
		boolean bValidateStatus = true;
		try {
			if (verifyMasterDetails != null) {			
				if (verifyMasterDetails.get("GeneralDetails") != null) {
					bStatus = doVerifyGeneralDetailsScreen(verifyMasterDetails.get("GeneralDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "verifying General Details Tab.", "General Details Tab populated fields data isn't matching with created."+Messages.errorMsg);
						bValidateStatus = false;
					}								
				}	

				if (verifyMasterDetails.get("AddressofCorrespondence") != null) {
					bStatus = doVerifyCorrespondenceAddressDetails(verifyMasterDetails.get("AddressofCorrespondence"));
					if (!bStatus) {
						Reporting.logResults("Fail", "verifying AddressofCorrespondence Tab Details.", "AddressofCorrespondence Tab populated fields data isn't matching with created."+Messages.errorMsg);
						bValidateStatus = false;
					}								
				}

				if (verifyMasterDetails.get("RegisteredAddressDetails") != null && HolderType.equalsIgnoreCase("Entity")) {					
					//bStatus = doVerifyRegisteredAddressDetails(verifyMasterDetails.get("RegisteredAddressDetails"), verifyMasterDetails.get("AddressofCorrespondence"));					
					bStatus = doVerifyRegisteredAddressDetails(verifyMasterDetails.get("RegisteredAddressDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "verifying RegisteredAddressDetails Tab.", "RegisteredAddressDetails Details Tab populated fields data isn't matching with created."+Messages.errorMsg);
						bValidateStatus = false;
					}								
				}

				if (verifyMasterDetails.get("HolderDetails") != null) {
					bStatus = doVerifyHolderDetails(verifyMasterDetails.get("HolderDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "verifying HolderDetails Tab.", "HolderDetails Tab populated fields data isn't matching with created."+Messages.errorMsg);
						bValidateStatus = false;
					}								
				}

				if (verifyMasterDetails.get("FatcaDetails") != null) {
					bStatus = doVerifyFatcaDetails(verifyMasterDetails.get("FatcaDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "verifying FatcaDetails Tab.", "FatcaDetails Tab populated fields data isn't matching with created."+Messages.errorMsg);
						bValidateStatus = false;
					}								
				}

				if (verifyMasterDetails.get("KYCDetails") != null) {
					bStatus = doVerifyKYCDetails(verifyMasterDetails.get("KYCDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "verifying KYCDetails Tab.", "KYCDetails Tab populated fields data isn't matching with created."+Messages.errorMsg);
						bValidateStatus = false;
					}								
				}

				/*bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Investor Master", objInvestorCreationTabsMaps.get("GeneralDetails").get("OperationType"));
				if (!bStatus) {
					Reporting.logResults("Fail", "Performing Maker action on Add New Investor page.", "Failed to perform Maker Action : "+objInvestorCreationTabsMaps.get("GeneralDetails").get("Operation")+", on new Investor creation page");
					return false;
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}	

	private static boolean doVerifyHolderDetails(Map<String, String> mapHolderDetails) {
		String appendMsg = "";
		boolean verifyStatus = true;
		try {			
			bStatus = NewUICommonFunctions.selectTheTab("Holder Details");
			if(!bStatus){
				verifyStatus = false;
			}
			Wait.waitForElementVisibility(Global.driver, By.id("isErisaInvestor1"), 5);
			if (HolderType.equalsIgnoreCase("Entity") || HolderType.equalsIgnoreCase("Individual")) {
				if (mapHolderDetails.get("ErisaInvestor") != null) {
					if (mapHolderDetails.get("ErisaInvestor").equalsIgnoreCase("Yes")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='isErisaInvestor1']//parent::span[@class='checked']"));
						if (!bStatus) {
							appendMsg = appendMsg + "[ ERROR : isErisaInvestor radio button wasn't marked to : 'Yes' .] \n";
							verifyStatus = false;
						}
					}
					if (mapHolderDetails.get("ErisaInvestor").equalsIgnoreCase("No")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='isErisaInvestor2']//parent::span[@class='checked']"));
						if (!bStatus) {
							appendMsg = appendMsg + "[ ERROR : isErisaInvestor radio button wasn't marked to : 'No' .] \n";
							verifyStatus = false;
						}
					}
				}
				if (mapHolderDetails.get("SidePocketAllowed") != null) {
					if (mapHolderDetails.get("SidePocketAllowed").equalsIgnoreCase("Yes")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='isSidePocketAllowed1']//parent::span[@class='checked']"));
						if (!bStatus) {
							appendMsg = appendMsg + "[ ERROR : isSidePocketAllowed radio button wasn't marked to : 'Yes' .] \n";
							verifyStatus = false;
						}
					}
					if (mapHolderDetails.get("SidePocketAllowed").equalsIgnoreCase("No")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='isSidePocketAllowed2']//parent::span[@class='checked']"));
						if (!bStatus) {
							appendMsg = appendMsg + "[ ERROR : isSidePocketAllowed radio button wasn't marked to : 'No' .] \n";
							verifyStatus = false;
						}
					}
				}
			}
			if (HolderType.equalsIgnoreCase("Individual")) {
				if (mapHolderDetails.get("InvestorSubType") != null) {
					if (mapHolderDetails.get("InvestorSubType").equalsIgnoreCase("Accredited Investor")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='investorSubType1']//parent::span[@class='checked']"));
						if (!bStatus) {
							appendMsg = appendMsg + "[ ERROR : 'Accredited Investor' radio button wasn't marked for : 'Investor Sub Type' .] \n";
							verifyStatus = false;
						}
					}
					if (mapHolderDetails.get("InvestorSubType").equalsIgnoreCase("Qualified Purchaser")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='investorSubType2']//parent::span[@class='checked']"));
						if (!bStatus) {
							appendMsg = appendMsg + "[ ERROR : 'Qualified Purchaser' radio button wasn't marked for : 'Investor Sub Type' .] \n";
							verifyStatus = false;
						}
					}
					if (mapHolderDetails.get("InvestorSubType").equalsIgnoreCase("NA")) {
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='investorSubType2']//parent::span[@class='checked']"));
						if (!bStatus) {
							appendMsg = appendMsg + "[ ERROR : 'NA' radio button wasn't marked for : 'Investor Sub Type' .] \n";
							verifyStatus = false;
						}
					}
				}
			}
			Messages.errorMsg = appendMsg;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return verifyStatus;	
	}

	public static boolean doVerifyGeneralDetailsScreen(Map<String, String> mapGeneralDetails){
		try{
			String appendMsg = "";
			boolean verifyStatus= true;
			bStatus = NewUICommonFunctions.selectTheTab("General Details");
			if(!bStatus){
				verifyStatus = false;
			}
			HolderType = "";

			if(mapGeneralDetails.get("Investor")!=null){
				//VeriFy the Holder type
				bStatus = verifyTextInTextBoxOrDropDown(By.id("fkInvestorIdPkLabel"), mapGeneralDetails.get("Investor"), "Yes");
				if(!bStatus){
					verifyStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
				}
			}

			if(mapGeneralDetails.get("HolderType")!=null){
				HolderType = mapGeneralDetails.get("HolderType");
				bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkInvestorTypeIdPk']//span[contains(@id,'select2-chosen')]"), mapGeneralDetails.get("HolderType"), "No");
				if(!bStatus){
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
					verifyStatus = false;
				}
			}

			//Verify the details for the investor type Entity
			if(mapGeneralDetails.get("EntityType")!=null && HolderType.equals("Entity")){				
				//Verify the Entity type
				if(mapGeneralDetails.get("EntityType")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkEntityTypeIdPk']//span[contains(@id,'select2-chosen')]"), mapGeneralDetails.get("EntityType"), "No");
					if(!bStatus){
						appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Registration Name
				if(mapGeneralDetails.get("RegistrationName")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("registrationName"), mapGeneralDetails.get("RegistrationName"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify  Date of incorporation
				if(mapGeneralDetails.get("IncorporationCountry")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkCountryOfIncorporationIdPk']//span[contains(@id,'select2-chosen')]"),mapGeneralDetails.get("IncorporationCountry"), "No");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Country
				if(mapGeneralDetails.get("IncorporationDate")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("dateOfIncorporation"), mapGeneralDetails.get("IncorporationDate"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}				
			}

			if (HolderType.equalsIgnoreCase("Individual") || HolderType.equalsIgnoreCase("Entity")) {
				//Verify tax details
				if(mapGeneralDetails.get("TaxID")!=null && mapGeneralDetails.get("TaxState")!=null && mapGeneralDetails.get("TaxCountry ")!=null ){					
					bStatus = verifyTaxesDetailsForHolderType(mapGeneralDetails.get("TaxID"),mapGeneralDetails.get("TaxState"),mapGeneralDetails.get("TaxCountry"));
					if(!bStatus){
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				//Verify the External ID's				
				if(mapGeneralDetails.get("ExternalID1")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("externalId1"), mapGeneralDetails.get("ExternalID1"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				if(mapGeneralDetails.get("ExternalID2")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("externalId2"), mapGeneralDetails.get("ExternalID2"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				if(mapGeneralDetails.get("ExternalID3")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("externalId3"), mapGeneralDetails.get("ExternalID3"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
			}

			if (HolderType.equalsIgnoreCase("Individual")) {
				if (mapGeneralDetails.get("Salutation") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkSalutationIdPk']//span[contains(@id,'select2-chosen')]"), mapGeneralDetails.get("Salutation"), "No");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("FirstName") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.id("individualFirstName"), mapGeneralDetails.get("FirstName"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("MiddleName") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.id("individualMiddleInitials"), mapGeneralDetails.get("MiddleName"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("LastName") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.id("individualLastName"), mapGeneralDetails.get("LastName"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("Sex") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkSexIdPk']//span[contains(@id,'select2-chosen')]"), mapGeneralDetails.get("Sex"), "No");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("DateOfBirth") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.id("dateOfBirth"), mapGeneralDetails.get("DateOfBirth"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("PlaceOfBirth") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.id("placeOfBirth"), mapGeneralDetails.get("PlaceOfBirth"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("ReportingCurrency") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkReportingCurrencyPreferenceIdPk']//span[contains(@id,'select2-chosen')]"), mapGeneralDetails.get("ReportingCurrency"), "No");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("PassPortNumber") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.id("passportNo"), mapGeneralDetails.get("PassPortNumber"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("PassportExpiry") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.id("passportExpiryDate"), mapGeneralDetails.get("PassportExpiry"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("RiskLevel") != null) {
					bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkRiskLevelIdPk']//span[contains(@id,'select2-chosen')]"), mapGeneralDetails.get("RiskLevel"), "No");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
			}
			Messages.errorMsg = appendMsg;
			return verifyStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyTaxesDetailsForHolderType(String TaxIds, String States, String Countries) {
		try{
			String sAppendMessage = "";
			boolean bValidateStatus = true;
			String[] arrTaxIDs = TaxIds.split(",");
			String[] arrStates = States.split(",");
			String[] arrCountries = Countries.split(",");
			/*String replaceValue="";

			if(investorType.equalsIgnoreCase("entity")){
				replaceValue = "Domicile";
			}

			if(investorType.equalsIgnoreCase("individual")){
				replaceValue = "Tax";
			}*/

			if(!((arrTaxIDs.length == arrStates.length) && (arrCountries.length == arrTaxIDs.length) && (arrStates.length == arrCountries.length))){
				Messages.errorMsg = " [ TEST DATA ERROR : Count mismatch with taxid, domicile state and domicile country. ] \n";
				return false;
			}

			for (int iValue = 0; iValue < arrCountries.length; iValue++) {
				//enter the tax value in the text box
				if(!arrTaxIDs[iValue].equalsIgnoreCase("None")){
					bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[@id='taxHId_"+iValue+"']"), arrTaxIDs[iValue], "Yes");					
					if(!bStatus){
						sAppendMessage = sAppendMessage + Messages.errorMsg +" \n";
						bValidateStatus = false;
					}
				}
				//enter the domicile state name
				if(!arrStates[iValue].equalsIgnoreCase("None")){
					bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//input[contains(@id,'fkInvestorTaxDetails"+iValue+"') and @type='text']"), arrStates[iValue], "Yes");					
					if(!bStatus){
						sAppendMessage = sAppendMessage + Messages.errorMsg +" \n";
						bValidateStatus = false;
					}
				}
				//enter the domicile country name
				if(!arrCountries[iValue].equalsIgnoreCase("None")){
					bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[contains(@id,'s2id_fkInvestorTaxDetails"+iValue+"')]//span[contains(@id,'select2-chosen')]"), arrCountries[iValue], "No");
					if(!bStatus){
						sAppendMessage = sAppendMessage + Messages.errorMsg +" \n";
						bValidateStatus = false;
					}
				}
			}
			Messages.errorMsg = sAppendMessage;
			return bValidateStatus;
		}
		catch(Exception e){
			return false;
		}
	}

	public static boolean doVerifyCorrespondenceAddressDetails(Map<String, String> mapCorrespondenceAddr){
		try{
			String appendMsg = "";
			boolean verifyStatus= true;
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("Address of Correspondence");
			if(!bStatus){
				verifyStatus = false;
			}

			if(!HolderType.equalsIgnoreCase("")){

				//verify the building name
				if(mapCorrespondenceAddr.get("Building")!=null){					
					bStatus = verifyTextInTextBoxOrDropDown(By.id("corBuildingArea"), mapCorrespondenceAddr.get("Building"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}

					//Verify the Floor name
					if(mapCorrespondenceAddr.get("Floor")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.id("corFloorSuiteApt"), mapCorrespondenceAddr.get("Floor"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify  the Street no number
					if(mapCorrespondenceAddr.get("StreetNumber")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.id("corStreetNo"), mapCorrespondenceAddr.get("StreetNumber"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify  the Street name
					if(mapCorrespondenceAddr.get("StreetName")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.id("corStreetName"), mapCorrespondenceAddr.get("StreetName"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the city name
					if(mapCorrespondenceAddr.get("City")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.id("corCity"), mapCorrespondenceAddr.get("City"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the state name
					if(mapCorrespondenceAddr.get("State")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.id("corState"), mapCorrespondenceAddr.get("State"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the Country name
					if(mapCorrespondenceAddr.get("Country")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_corCountryIdPk']//span[contains(@id,'select2-chosen')]"), mapCorrespondenceAddr.get("Country"), "No");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the PIN code
					if(mapCorrespondenceAddr.get("PostalCode")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.id("corPostalCode"), mapCorrespondenceAddr.get("PostalCode"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the Email1 
					if(mapCorrespondenceAddr.get("EmailAddr1")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.id("corEmailAddress1"), mapCorrespondenceAddr.get("EmailAddr1"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the Email2
					if(mapCorrespondenceAddr.get("EmailAddr2")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.id("corEmailAddress2"), mapCorrespondenceAddr.get("EmailAddr2"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}


					//Verify the phone1
					if(mapCorrespondenceAddr.get("Phone1")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.id("corPhone1"), mapCorrespondenceAddr.get("Phone1"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the phone2
					if(mapCorrespondenceAddr.get("Phone2")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='tab2']//div[contains(@class,'col-md')][4]//input[@id='corPhone2']"), mapCorrespondenceAddr.get("Phone2"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the Fax
					if(mapCorrespondenceAddr.get("Fax")!=null){
						bStatus = verifyTextInTextBoxOrDropDown(By.id("corFax"), mapCorrespondenceAddr.get("Fax"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}
					/*
					// verify  Order Ack check box.
					if(mapCorrespondenceAddr.get("OA")!=null){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='OA']/preceding-sibling::div[1]//input"));
						if(!bStatus && mapCorrespondenceAddr.get("OA").equalsIgnoreCase("Yes") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ERROR: OA is Not checked but expected should be checked]\n"; 
						}

						if(bStatus && mapCorrespondenceAddr.get("OA").equalsIgnoreCase("No") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ERROR: OA is checked but expected should be unchecked]\n"; 
						}
					}
					//  verify CN check box.
					if(mapCorrespondenceAddr.get("CN")!=null){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='CN']/preceding-sibling::div[1]//input"));
						if(!bStatus && mapCorrespondenceAddr.get("CN").equalsIgnoreCase("Yes") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ERROR: CN is Not checked but expected should be checked]\n"; 
						}

						if(bStatus && mapCorrespondenceAddr.get("CN").equalsIgnoreCase("No") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ERROR: CN is checked but expected should be unchecked]\n"; 
						}
					}

					//  Statement Ack check box.
					if(mapCorrespondenceAddr.get("Statement")!=null){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Statement']/preceding-sibling::div[1]//input"));
					if(!bStatus && mapCorrespondenceAddr.get("Statement").equalsIgnoreCase("Yes") ){
						verifyStatus = false;
						appendMsg = appendMsg+" [ERROR: Statement is Not checked but expected should be checked]\n"; 
					}

					if(bStatus && mapCorrespondenceAddr.get("Statement").equalsIgnoreCase("No") ){
						verifyStatus = false;
						appendMsg = appendMsg+" [ERROR: Statement is checked but expected should be unchecked]\n"; 
					}
				  }


				//  Statemt Ack check box.
				if(mapCorrespondenceAddr.get("CancelNote")!=null){
					if(mapCorrespondenceAddr.get("CancelNote").equalsIgnoreCase("No")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Cancel Note']/preceding-sibling::div[1]//input"));
						if(bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Cancel Note']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Cancel Note check box  is not Clicked ";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("CancelNote").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Cancel Note']/preceding-sibling::div[1]//input"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Cancel Note']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Cancel Note check box  is not Clicked ";
								return false;
							}
						}
					}
				}*/

					// Verify in Distribution Mode
					// verify Courier check box.
					if(mapCorrespondenceAddr.get("Courier")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+Elements.getElementAttribute(Global.driver, By.xpath("//label[contains(text(),'Courier') and contains(@for,'dist')]"), "for").trim()+"']//parent::span[@class='checked']"));
						if(!bStatus && mapCorrespondenceAddr.get("Courier").equalsIgnoreCase("Yes") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ ERROR : Courier is Not checked but expected should be checked ]\n"; 
						}
						if(bStatus && mapCorrespondenceAddr.get("Courier").equalsIgnoreCase("No") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ ERROR : Courier is checked but expected should be unchecked ]\n";
						}
					}

					// verify Email check box
					if(mapCorrespondenceAddr.get("Email")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+Elements.getElementAttribute(Global.driver, By.xpath("//label[contains(text(),'Email') and contains(@for,'dist')]"), "for").trim()+"']//parent::span[@class='checked']"));
						if(!bStatus && mapCorrespondenceAddr.get("Email").equalsIgnoreCase("Yes") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ERROR: Email is Not checked but expected should be checked]\n"; 
						}
						if(bStatus && mapCorrespondenceAddr.get("Email").equalsIgnoreCase("No") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ERROR: Email is checked but expected should be unchecked]\n"; 
						}
					}

					//Verify Fax check box
					if(mapCorrespondenceAddr.get("Fax")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+Elements.getElementAttribute(Global.driver, By.xpath("//label[contains(text(),'Fax') and contains(@for,'dist')]"), "for").trim()+"']//parent::span[@class='checked']"));
						if(!bStatus && mapCorrespondenceAddr.get("Fax").equalsIgnoreCase("Yes") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ERROR: Fax is Not checked but expected should be checked]\n"; 
						}

						if(bStatus && mapCorrespondenceAddr.get("Fax").equalsIgnoreCase("No") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ERROR: Fax is checked but expected should be unchecked]\n"; 
						}
					}

					//Verify Web check box
					if(mapCorrespondenceAddr.get("Web")!=null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='"+Elements.getElementAttribute(Global.driver, By.xpath("//label[contains(text(),'Web') and contains(@for,'dist')]"), "for").trim()+"']//parent::span[@class='checked']"));
						if(!bStatus && mapCorrespondenceAddr.get("Web").equalsIgnoreCase("Yes") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ERROR: Web is Not checked but expected should be checked]\n"; 
						}
						if(bStatus && mapCorrespondenceAddr.get("Web").equalsIgnoreCase("No") ){
							verifyStatus = false;
							appendMsg = appendMsg+" [ERROR: Web is checked but expected should be unchecked]\n"; 
						}
					}
				}

				//Verify the Email for distribution
				if(mapCorrespondenceAddr.get("Email")!=null && mapCorrespondenceAddr.get("Email").equalsIgnoreCase("Yes") && mapCorrespondenceAddr.get("EmailForDistribution")!=null){
					bStatus=NewUICommonFunctions.verifySelectedRadioButton("Email for distribution",mapCorrespondenceAddr.get("EmailForDistribution"));
					if(!bStatus){
						verifyStatus = false;
						appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\n";
					}	
				}

				//Verify  Address of Correspondence
				if(HolderType.equalsIgnoreCase("Entity") && mapCorrespondenceAddr.get("CorrespondenceAndRegisteredAddrSame")!=null){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='copyAddress']//parent::span[@class='checked']"));
					if(!bStatus && mapCorrespondenceAddr.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes") ){
						verifyStatus = false;
						appendMsg = appendMsg+" [ERROR: CorrespondenceAndRegisteredAddrSame is Not checked but expected should be checked]\n"; 
					}
					if(bStatus && mapCorrespondenceAddr.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("No") ){
						verifyStatus = false;
						appendMsg = appendMsg+" [ERROR: CorrespondenceAndRegisteredAddrSame is checked but expected should be unchecked]\n"; 
					}
				}
			}
			Messages.errorMsg = appendMsg;
			return verifyStatus;
		}
		catch(Exception e){
			e.printStackTrace();return false;
		}
	}	

	public static boolean doVerifyRegisteredAddressDetails(Map<String, String> mapRegisteredAddrDetails){
		String appendMsg = "";
		boolean verifyStatus= true;
		Map<String, String> mapClubbedAddressDetails = new HashMap<>();
		try{
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			if (!HolderType.equalsIgnoreCase("Entity")) {
				return true;
			}
			bStatus = NewUICommonFunctions.selectTheTab("Registered Address Details");
			if(!bStatus){
				appendMsg = appendMsg+" [ERROR: Registered Address Details  Tab is not Clicked ]\n"; 
				verifyStatus = false;
			}

			if(mapRegisteredAddrDetails== null){
				return true ;
			}
			mapClubbedAddressDetails.putAll(mapRegisteredAddrDetails);

			if(!HolderType.equalsIgnoreCase("") && HolderType.equalsIgnoreCase("Entity")){
				/*//Getting Actual Correspondence Addr Details.
				Map<String, String> mapActualCorrespondenceAddrDetails = new HashMap<>();
				if (mapCorrespondenceAddrDetails != null) {
					mapActualCorrespondenceAddrDetails = ExcelUtils.readDataABasedOnCellName(Global.sHolderTestDataFilePath, "AddressofCorrespondence", mapCorrespondenceAddrDetails.get("TestcaseNameRef"));
				}
				if (mapRegisteredAddrDetails != null && mapCorrespondenceAddrDetails != null && mapCorrespondenceAddrDetails.get("CorrespondenceAndRegisteredAddrSame") != null && mapCorrespondenceAddrDetails.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes")) {
					mapClubbedAddressDetails.putAll(mapCorrespondenceAddrDetails);
					mapClubbedAddressDetails.putAll(mapRegisteredAddrDetails);
				}
				else if (mapRegisteredAddrDetails == null && mapCorrespondenceAddrDetails != null && mapCorrespondenceAddrDetails.get("CorrespondenceAndRegisteredAddrSame") != null && mapCorrespondenceAddrDetails.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes") ) {
					mapClubbedAddressDetails.putAll(mapCorrespondenceAddrDetails);
				}
				//Getting Actual Correspondence Addr Details and comparing whether the registered addr details are same as corresp addr details is marked as YES if so then putting all into the clubbed map to club actual corr details and the changed details and verify the modified registered addr details.
				else if (mapRegisteredAddrDetails == null && mapActualCorrespondenceAddrDetails != null && mapActualCorrespondenceAddrDetails.get("CorrespondenceAndRegisteredAddrSame") != null && mapActualCorrespondenceAddrDetails.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes")) {
					mapClubbedAddressDetails.putAll(mapActualCorrespondenceAddrDetails);
				}
				else if (mapRegisteredAddrDetails != null && mapActualCorrespondenceAddrDetails != null && mapActualCorrespondenceAddrDetails.get("CorrespondenceAndRegisteredAddrSame") != null && mapActualCorrespondenceAddrDetails.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes")) {
					mapClubbedAddressDetails.putAll(mapActualCorrespondenceAddrDetails);
					mapClubbedAddressDetails.putAll(mapRegisteredAddrDetails);
				}
				else if (mapRegisteredAddrDetails != null) {
					mapClubbedAddressDetails.putAll(mapRegisteredAddrDetails);
				}*/

				//verify the building name
				if(mapClubbedAddressDetails.get("Building")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("buildingArea"), mapClubbedAddressDetails.get("Building"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Floor name
				if(mapClubbedAddressDetails.get("Floor")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("floorSuiteApt"), mapClubbedAddressDetails.get("Floor"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify  the Street no number
				if(mapClubbedAddressDetails.get("StreetNumber")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("streetNo"), mapClubbedAddressDetails.get("StreetNumber"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify  the Street name
				if(mapClubbedAddressDetails.get("StreetName")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("streetName"), mapClubbedAddressDetails.get("StreetName"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the city name
				if(mapClubbedAddressDetails.get("City")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("city"), mapClubbedAddressDetails.get("City"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the state name
				if(mapClubbedAddressDetails.get("State")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("state"), mapClubbedAddressDetails.get("State"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Country name
				if(mapClubbedAddressDetails.get("Country")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkCountryIdPk']//span[contains(@id,'select2-chosen')]"), mapClubbedAddressDetails.get("Country"), "No");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the PIN code
				if(mapClubbedAddressDetails.get("PostalCode")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("postalCode"), mapClubbedAddressDetails.get("PostalCode"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Email1 
				if(mapClubbedAddressDetails.get("EmailAddr1")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("emailAddress1"), mapClubbedAddressDetails.get("EmailAddr1"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Email2
				if(mapClubbedAddressDetails.get("EmailAddr2")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("emailAddress2"), mapClubbedAddressDetails.get("EmailAddr2"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}


				//Verify the phone1
				if(mapClubbedAddressDetails.get("Phone1")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("phone1"), mapClubbedAddressDetails.get("Phone1"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}					
				}		


				//Verify the phone2
				if(mapClubbedAddressDetails.get("Phone2")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("phone2"), mapClubbedAddressDetails.get("Phone2"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Fax
				if(mapClubbedAddressDetails.get("Fax")!=null){
					bStatus = verifyTextInTextBoxOrDropDown(By.id("fax"), mapClubbedAddressDetails.get("Fax"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}				
			}
			Messages.errorMsg = appendMsg;
			return verifyStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}	

	public static boolean doVerifyFatcaDetails(Map<String, String> mapFatcaDeatils){
		String sAppendMessage = "";
		boolean bValidateStatus = true ;
		try{
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("FATCA Details");
			if(!bStatus){
				sAppendMessage = sAppendMessage+" [ERROR: Registered Address Details  Tab is not Clicked ]\n"; 
				bValidateStatus = false;
			}
			if(!HolderType.equalsIgnoreCase("")){
				//Common fields for both Entity types.
				if (HolderType.equalsIgnoreCase("Entity") || HolderType.equalsIgnoreCase("Individual")) {
					if(mapFatcaDeatils.get("PowerAttorneyToUS") != null){
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(text(),'Power of Attorney to US person')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("PowerAttorneyToUS")+"']//input//parent::span[contains(@class,'checked')]"), 5);
						if(!bStatus){
							sAppendMessage =  "[ ERROR : "+mapFatcaDeatils.get("PowerAttorneyToUS")+" : option wasn't selected for radio button field name power of attorney to US person. ] \n";
							bValidateStatus = false;
						}
					}

					if(mapFatcaDeatils.get("SignatoryAuthorityPersonInUS") != null){
						bStatus =Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(text(),'Signatory Authority to person in US')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("SignatoryAuthorityPersonInUS")+"']//input//parent::span[contains(@class,'checked')]"),5);
						if(!bStatus){
							sAppendMessage = sAppendMessage +"[ ERROR : "+ mapFatcaDeatils.get("SignatoryAuthorityPersonInUS")+" : option wasn't selected for radio button field name Sign Authority to US person. ] \n";
							bValidateStatus = false;
						}
					}

					if(mapFatcaDeatils.get("StandingPaymentInstructionsToUSAccount") != null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[contains(text(),'Standing Payment Instructions to US account')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("StandingPaymentInstructionsToUSAccount")+"']//input//parent::span[contains(@class,'checked')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("StandingPaymentInstructionsToUSAccount")+" : option cannot be selected for radio button field name Standing payment instructions to US account. ] \n";
							bValidateStatus = false;
						}
					}

					if(mapFatcaDeatils.get("FATCADocumentation") != null){
						bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[contains(text(),'FATCA Documentation')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("FATCADocumentation")+"']//input//parent::span[contains(@class,'checked')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("FATCADocumentation")+" : option cannot be selected for radio button field name FATCA documentation. ] \n";
							bValidateStatus = false;
						}
					}

					if(mapFatcaDeatils.get("Level1Classification") != null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Level 1 Classification", mapFatcaDeatils.get("Level1Classification"));
						if(!bStatus){
							sAppendMessage = sAppendMessage +"[ ERROR : "+ mapFatcaDeatils.get("Level1Classification") + " : value for selected Level 1 Classification wasn't visible. ] \n";
							bValidateStatus = false;
						}
					}

					if(mapFatcaDeatils.get("Level2Classification") != null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Level 2 Classification", mapFatcaDeatils.get("Level2Classification"));
						if(!bStatus){
							sAppendMessage = sAppendMessage +"[ ERROR : "+ mapFatcaDeatils.get("Level2Classification") + " : value for selected Level 2 Classification wasn't visible. ] \n";
							bValidateStatus = false;
						}
					}

					if(mapFatcaDeatils.get("FATCAClassification") != null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("FATCA Classification", mapFatcaDeatils.get("FATCAClassification"));
						if(!bStatus){
							sAppendMessage = sAppendMessage +"[ ERROR : "+ mapFatcaDeatils.get("FATCAClassification") + " : value for selected FATCAClassification wasn't visible. ] \n";
							bValidateStatus = false;
						}
					}

					if(mapFatcaDeatils.get("DocumentationExpiryDate") != null){
						bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//input[@id='investorFatcaDetails0.fatcaDocumentExpiryDate' and contains(@value,'"+mapFatcaDeatils.get("DocumentationExpiryDate")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("DocumentationExpiryDate")+" : value in Documentation Expiry Date wasn't visible. ] \n";
							bValidateStatus = false;
						}
					}

					if(mapFatcaDeatils.get("DocumentsAvailable") != null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Documents Available", mapFatcaDeatils.get("DocumentsAvailable"));
						if(!bStatus){
							sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("DocumentsAvailable")+" : value selected for DocumentsAvailable wasn't visible. ] \n";
							bValidateStatus = false;
						}
					}

					if(mapFatcaDeatils.get("OwnerShipPercentage") != null){
						bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//input[@id='benef_owner' and contains(@value,'"+mapFatcaDeatils.get("OwnerShipPercentage")+"')]"));
						if(!bStatus){
							sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("OwnerShipPercentage")+" : value wasn't visible in owner ship percentage field. ] \n";
							bValidateStatus = false;
						}
					}

					//Only for Entity type Holder.
					if (HolderType.equalsIgnoreCase("Entity")) {
						if(mapFatcaDeatils.get("UnderLyingBeneficialOwner") != null){
							bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//input[@id='investorFatcaDetails0.underlyingBeneficalOwner' and contains(@value,'"+mapFatcaDeatils.get("UnderLyingBeneficialOwner")+"')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("UnderLyingBeneficialOwner")+" : value wasn't visible in Underlying Beneficial Owner field. ] \n";
								bValidateStatus = false;
							}
						}
					}

					//Only for Individual type Holder.
					if(HolderType.equalsIgnoreCase("Individual")){
						if(mapFatcaDeatils.get("USPerson") != null){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[contains(text(),'US Person')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("USPerson")+"']//input//parent::span[contains(@class,'checked')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("USPerson")+" : radio wasn't selected for radio button field name US person. ] \n";
								bValidateStatus = false;
							}
						}

						if(mapFatcaDeatils.get("CountryOfBirth") != null){
							bStatus = NewUICommonFunctions.verifyTextInDropDown("Country Of Birth", mapFatcaDeatils.get("CountryOfBirth"));
							if(!bStatus){
								sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("CountryOfBirth")+" : Value selected for dropdown Country Of Birth wasn't visible. ] \n";
								bValidateStatus = false;
							}
						}

						if(mapFatcaDeatils.get("PresentNationality") != null){
							bStatus = NewUICommonFunctions.verifyTextInDropDown("Present Nationality",mapFatcaDeatils.get("PresentNationality") );
							if(!bStatus){
								sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("PresentNationality")+" : Value selected for dropdown Present Nationality wasn't visible. ] \n";
								bValidateStatus = false;
							}
						}
					}
				}				
			}
			Messages.errorMsg = sAppendMessage;			
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

}
