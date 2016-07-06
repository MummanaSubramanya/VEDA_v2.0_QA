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
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;


public class InvestorMasterAppFunctions {

	static boolean bStatus ;
	public static String InvestorType ="Entity";
	public static boolean bTadingInvestorFlag = false;

	public static boolean isInvestorGeneralDetailsModifyFlag = false;

	public static boolean doFillGeneralDetails(Map<String, String> mapGeneralDetails){

		try{
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("General Details");
			if(!bStatus){
				return false;
			}

			if(mapGeneralDetails.get("InvestorType")!=null){
				InvestorType = mapGeneralDetails.get("InvestorType");
				//select the entity type from Invetor type
				if (isInvestorGeneralDetailsModifyFlag == false) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Investor Type",mapGeneralDetails.get("InvestorType"));
					if(!bStatus){
						return false;
					}
				}				
			}

			//fill details for the investor type Entity
			if(mapGeneralDetails.get("InvestorType")!=null && InvestorType.equals("Entity")){

				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[text()='Same Holder ']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "General Details is not displyed for the investor type: Enity";
					return false;
				}

				//check for same as holder
				if(mapGeneralDetails.get("SameHolder")!=null && isInvestorGeneralDetailsModifyFlag == false){
					if(mapGeneralDetails.get("SameHolder").equalsIgnoreCase("yes")){
						bStatus = Elements.click(Global.driver, By.xpath("//label[text()='Same Holder ']/following-sibling::div//label[normalize-space()='Yes']//input"));
						if(!bStatus){
							Messages.errorMsg = "Cannot able to select Yes option for the field Same holder";
							return false;
						}
					}

					if(mapGeneralDetails.get("SameHolder").equalsIgnoreCase("No")){
						bStatus = Elements.click(Global.driver, By.xpath("//label[text()='Same Holder ']/following-sibling::div//label[normalize-space()='No']//input"));
						if(!bStatus){
							Messages.errorMsg = "Cannot able to select Yes option for the field Same holder";
							return false;
						}
					}
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
					bStatus = Elements.enterText(Global.driver, By.id("dateOfIncorporation"), mapGeneralDetails.get("IncorporationDate"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Incorporation Date field";
						return false;
					}
					Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Investor Master')]"));
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
					bStatus = removeAllTaxIDs("Entity");
					if(!bStatus){
						return false;
					}
					//add tax details for entity
					bStatus = fillTaxesDetailsForInvestorType(mapGeneralDetails.get("TaxID"),mapGeneralDetails.get("TaxState"),mapGeneralDetails.get("TaxCountry"));
					if(!bStatus){
						return false;
					}
				}

				/*if(mapGeneralDetails.get("ExtID1")!=null){
					bStatus = Elements.enterText(Global.driver,Locators.ClientMaster.TextBox.txtExternalId1,mapGeneralDetails.get("ExtID1"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter the value in External ID1";
						return false;
					}
				}

				if(mapGeneralDetails.get("ExtID2")!=null){
					bStatus = Elements.enterText(Global.driver, Locators.ClientMaster.TextBox.txtExternalId2,mapGeneralDetails.get("ExtID2"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter the value in External ID2";
						return false;
					}
				}

				if(mapGeneralDetails.get("ExtID3")!=null){
					bStatus = Elements.enterText(Global.driver,Locators.ClientMaster.TextBox.txtExternalId3,mapGeneralDetails.get("ExtID3"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter the value in External ID3";
						return false;
					}
				}*/
			}

			if(mapGeneralDetails.get("InvestorType")!=null && InvestorType.equals("Individual")){

				//wait for salutation
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='s2id_fkSalutationIdPk']"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "General Details is not displyed for the investor type: Individual";
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
					bStatus = Elements.enterText(Global.driver, By.id("dateOfBirthNew"), mapGeneralDetails.get("DateOfBirth"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the DOB field";
						return false;
					}
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
				}
				Elements.click(Global.driver, By.xpath("//h4/p[contains(text(),'Investor Master')]"));

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
					bStatus = fillTaxesDetailsForInvestorType(mapGeneralDetails.get("TaxID"),mapGeneralDetails.get("TaxState"),mapGeneralDetails.get("TaxCountry"));
					if(!bStatus){
						return false;
					}
				}

				/*if(mapGeneralDetails.get("ExtID1")!=null){
					bStatus = Elements.enterText(Global.driver,Locators.ClientMaster.TextBox.txtExternalId1,mapGeneralDetails.get("ExtID1"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter the value in External ID1";
						return false;
					}
				}

				if(mapGeneralDetails.get("ExtID2")!=null){
					bStatus = Elements.enterText(Global.driver, Locators.ClientMaster.TextBox.txtExternalId2,mapGeneralDetails.get("ExtID2"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter the value in External ID2";
						return false;
					}
				}

				if(mapGeneralDetails.get("ExtID3")!=null){
					bStatus = Elements.enterText(Global.driver,Locators.ClientMaster.TextBox.txtExternalId3,mapGeneralDetails.get("ExtID3"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter the value in External ID3";
						return false;
					}
				}*/
			}

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean  doFillCorrespondenceAddressDetails(Map<String, String> mapCorrespondenceAddr){
		try{


			bStatus = NewUICommonFunctions.selectTheTab("Address of Correspondence");
			if(!bStatus){
				Messages.errorMsg = " Address of correspondance tab is not clicked";
				return false;
			}
			Wait.waitForElementVisibility(Global.driver, By.id("corBuildingArea"), 5);
			if(!InvestorType.equalsIgnoreCase("")){
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
					if(mapCorrespondenceAddr.get("Courier").equalsIgnoreCase("No")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Courier']/preceding-sibling::div[1]//input"));
						if(bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Courier']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Courier check box  is not Clicked ";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("Courier").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Courier']/preceding-sibling::div[1]//input"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Courier']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Courier check box  is not Clicked ";
								return false;
							}
						}
					}
				}

				if(mapCorrespondenceAddr.get("Email")!=null){
					if(mapCorrespondenceAddr.get("Email").equalsIgnoreCase("No")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Email']/preceding-sibling::div[1]//input"));
						if(bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Email']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Email check box  is not Clicked ";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("Email").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Email']/preceding-sibling::div[1]//input"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Email']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Email check box  is not Clicked ";
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
					if(mapCorrespondenceAddr.get("Fax Check").equalsIgnoreCase("No")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Fax']/preceding-sibling::div[1]//input"));
						if(bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Fax']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Fax check box  is not Clicked ";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("Fax Check").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Fax']/preceding-sibling::div[1]//input"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Fax']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Fax check box  is not Clicked ";
								return false;
							}
						}
					}
				}

				if(mapCorrespondenceAddr.get("Web")!=null){
					if(mapCorrespondenceAddr.get("Web").equalsIgnoreCase("No")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Web']/preceding-sibling::div[1]//input"));
						if(bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Web']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Web check box  is not Clicked ";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("Web").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Web']/preceding-sibling::div[1]//input"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Web']/preceding-sibling::div[1]//input"));
							if(!bStatus){
								Messages.errorMsg = "Web check box  is not Clicked ";
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

				if(InvestorType.equalsIgnoreCase("Entity") && mapCorrespondenceAddr.get("CorrespondenceAndRegisteredAddrSame")!=null){

					if(mapCorrespondenceAddr.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("No")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("copyAddress"));
						if(bStatus){
							bStatus = Elements.click(Global.driver,By.id("copyAddress"));
							if(!bStatus){
								Messages.errorMsg = "CorrespondenceAndRegisteredAddrSame check box  is not Clicked ";
								return false;
							}
						}
					}
					if(mapCorrespondenceAddr.get("CorrespondenceAndRegisteredAddrSame").equalsIgnoreCase("Yes")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.id("copyAddress"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver,By.id("copyAddress"));
							if(!bStatus){
								Messages.errorMsg = "CorrespondenceAndRegisteredAddrSame check box  is not Clicked ";
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
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			TimeUnit.SECONDS.sleep(3);
			if (!InvestorType.equalsIgnoreCase("Entity")) {
				return true;
			}
			bStatus = NewUICommonFunctions.selectTheTab("Registered Address Details");
			if(!bStatus){
				Messages.errorMsg = " Registered Address Details tab is not clicked";
				return false;
			}
			Wait.waitForElementVisibility(Global.driver, By.id("buildingArea"), 5);
			if(!InvestorType.equalsIgnoreCase("") && InvestorType.equalsIgnoreCase("Entity")){
				//enter the building name
				if(mapRegisteredAddressDetails.get("Building")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("buildingArea"), mapRegisteredAddressDetails.get("Building"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the building field";
						return false;
					}
				}

				//enter the Floor name
				if(mapRegisteredAddressDetails.get("Floor")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("floorSuiteApt"), mapRegisteredAddressDetails.get("Floor"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Floor field";
						return false;
					}
				}

				//enter the Strret number
				if(mapRegisteredAddressDetails.get("StreetNumber")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("streetNo"), mapRegisteredAddressDetails.get("StreetNumber"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Strret Number field";
						return false;
					}
				}

				//enter the Street name
				if(mapRegisteredAddressDetails.get("StreetName")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("streetName"), mapRegisteredAddressDetails.get("StreetName"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Strret Name field";
						return false;
					}
				}

				//enter the city name
				if(mapRegisteredAddressDetails.get("City")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("city"), mapRegisteredAddressDetails.get("City"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the CIty field";
						return false;
					}
				}

				//enter the state name
				if(mapRegisteredAddressDetails.get("State")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("state"), mapRegisteredAddressDetails.get("State"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the State field";
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
						Messages.errorMsg = "Cannot enter value in the Postal code field";
						return false;
					}
				}

				//enter the Email1 
				if(mapRegisteredAddressDetails.get("EmailAddr1")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("emailAddress1"), mapRegisteredAddressDetails.get("EmailAddr1"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Email Address1 field";
						return false;
					}
				}

				//enter the Email2
				if(mapRegisteredAddressDetails.get("EmailAddr2")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("emailAddress2"), mapRegisteredAddressDetails.get("EmailAddr2"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Email Address2 field";
						return false;
					}
				}

				//enter the phone1
				if(mapRegisteredAddressDetails.get("Phone1")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("phone1"), mapRegisteredAddressDetails.get("Phone1"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the phone 1 field";
						return false;
					}
				}

				//enter the phone2
				if(mapRegisteredAddressDetails.get("Phone2")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("phone2"), mapRegisteredAddressDetails.get("Phone2"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the phone 2 field";
						return false;
					}
				}

				//enter the Fax
				if(mapRegisteredAddressDetails.get("Fax")!=null){
					bStatus = Elements.enterText(Global.driver, By.id("fax"), mapRegisteredAddressDetails.get("Fax"));
					if(!bStatus){
						Messages.errorMsg = "Cannot enter value in the Fax field";
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

	public static boolean fillTaxesDetailsForInvestorType(String TaxIds,String States,String Countries){
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
					bStatus = Elements.enterText(Global.driver, By.id("taxId_"+iValue), arrTaxIDs[iValue]);
					if(!bStatus){
						Messages.errorMsg = " [ ERROR : For the tax text box index:"+iValue+", value cannot be entered. ] \n";
						return false;
					}
				}
				//enter the domicile state name
				if(!arrStates[iValue].equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//input[contains(@id,'fkInvestorTaxDetails"+iValue+"')]"), arrStates[iValue]);
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
					bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'addMoreTaxDetails')]"));
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


	public static boolean doFillInvestorDetails(Map<String, String> mapInvestorDetails){
		String isGPorIMorNA = "NA";
		try{
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("Investor Details");
			if(!bStatus){
				Messages.errorMsg = "Investor Deatils tab is not clicked";
				return false;
			}
			Wait.waitForElementVisibility(Global.driver, By.xpath("//label[@for='isErisaInvestor']"), 5);
			if(InvestorType.equalsIgnoreCase("Individual") ||InvestorType.equalsIgnoreCase("Entity")){
				if(mapInvestorDetails.get("ErisaInvestor")!=null){
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='"+mapInvestorDetails.get("ErisaInvestor")+"']//input[contains(@id,'isErisaInvestor')]"));
					if(!bStatus){
						Messages.errorMsg = mapInvestorDetails.get("ErisaInvestor")+" option cannot be selected for radio button field name Erisa Investor";
						return false;
					}
				}
				if(mapInvestorDetails.get("SidePocketAllowed")!=null){
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='"+mapInvestorDetails.get("SidePocketAllowed")+"']//input[contains(@id,'isSidePocketAllowed')]"));
					if(!bStatus){
						Messages.errorMsg = mapInvestorDetails.get("SidePocketAllowed")+" option cannot be selected for radio button field name Side pocket allowed";
						return false;
					}
				}
				if(InvestorType.equalsIgnoreCase("Individual")){
					if(mapInvestorDetails.get("InvestorSubType")!=null){
						bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='"+mapInvestorDetails.get("InvestorSubType")+"']//input[contains(@id,'investorSubType')]"));
						if(!bStatus){
							Messages.errorMsg = mapInvestorDetails.get("InvestorSubType")+" option cannot be selected for radio button field name Investor Sub Type";
							return false;
						}
					}
				}
				/*if(mapInvestorDetails.get("IsGeneralpartner") != null && mapInvestorDetails.get("IsGeneralpartner").trim().equalsIgnoreCase("Yes")){					
					isGPorIMorNA = "GP";
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isGpYes']"));
					if(!bStatus){
						Messages.errorMsg = mapInvestorDetails.get("IsGeneralpartner")+" option cannot be selected for radio button field name Is General partner";
						return false;
					}
					if (mapInvestorDetails.get("IsGeneralpartner").equalsIgnoreCase("Yes")) {
						bStatus = addMoreGeneralPartnerDetails(mapInvestorDetails);
						if (!bStatus) {
							return false;
						}
					}
				}*/
				if(mapInvestorDetails.get("isGPOrIMOrNA") != null){
					if(mapInvestorDetails.get("isGPOrIMOrNA").equalsIgnoreCase("GP")){
						isGPorIMorNA = "GP";
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isGpYes']"));
						if(!bStatus){
							Messages.errorMsg = mapInvestorDetails.get("IsGeneralpartner")+" option cannot be selected for radio button field name Is General partner";
							return false;
						}
					}
					if(mapInvestorDetails.get("isGPOrIMOrNA").equalsIgnoreCase("IM")){
						isGPorIMorNA = "IM";
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isIMYes']"));
						if(!bStatus){
							Messages.errorMsg = mapInvestorDetails.get("IsInvestmentManager")+" option cannot be selected for radio button field name Is Investment Manager";
							return false;
						}
					}
					if(mapInvestorDetails.get("isGPOrIMOrNA").equalsIgnoreCase("NA")){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='gpIMNA']"));
						if(!bStatus){
							Messages.errorMsg = mapInvestorDetails.get("IsInvestmentManager")+" option cannot be selected for radio button field name 'GP/IM NA'.";
							return false;
						}
					}
				}
				/*if(mapInvestorDetails.get("IsInvestmentManager") != null && mapInvestorDetails.get("IsInvestmentManager").trim().equalsIgnoreCase("Yes")){
					isGPorIMorNA = "IM";
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='isIMYes']"));
					if(!bStatus){
						Messages.errorMsg = mapInvestorDetails.get("IsInvestmentManager")+" option cannot be selected for radio button field name Is Investment Manager";
						return false;
					}
					if (mapInvestorDetails.get("IsInvestmentManager").equalsIgnoreCase("Yes")) {
						bStatus = addMoreInvestmentManagerDetails(mapInvestorDetails);
						if (!bStatus) {
							return false;
						}
					}
				}
				if (isGPorIMorNA.equalsIgnoreCase("NA")) {
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='gpIMNA']"));
					if(!bStatus){
						Messages.errorMsg = mapInvestorDetails.get("IsInvestmentManager")+" option cannot be selected for radio button field name 'GP/IM NA'.";
						return false;
					}
				}*/
				if (isGPorIMorNA.equalsIgnoreCase("GP") || isGPorIMorNA.equalsIgnoreCase("IM")) {
					bStatus = FeeDistributionAppFunction.doAddNewGPorIMDetails(mapInvestorDetails);
					if (!bStatus) {
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

	private static boolean addMoreGeneralPartnerDetails(Map<String, String> mapInvestorDetails) {
		try {
			String sClientNames = null, sFundFamilyNames = null, sLegalEntityNames = null, sFromDate = null, sToDate = null;
			List<String> aClientNames = null, aFundFamilyNames = null, aLegalEntityNames = null, aFromDate = null, aToDate = null;

			int noOfRemoveButtons = 0;
			noOfRemoveButtons = Elements.getXpathCount(Global.driver, By.xpath("//button[contains(@onclick,'removeGenpartnerList')]"));
			for (int i = noOfRemoveButtons; i >= 1; i--) {				
				String sLocator = "//button[@onclick=\"removeGenpartnerList('/veda2','"+i+"')\"]";
				bStatus = NewUICommonFunctions.jsClick(By.xpath(sLocator));
				//bStatus = CommonAppFunctions.spinnerClick(Global.driver, By.xpath(sLocator));
				if (!bStatus) {
					Messages.errorMsg = "Unable to remove additional General Partner details.";
				}
			}

			if (mapInvestorDetails.get("Client Name") != null) {
				sClientNames = mapInvestorDetails.get("Client Name");
				aClientNames = Arrays.asList(sClientNames.split(","));
			}
			if (mapInvestorDetails.get("Fund Family Name") != null) {
				sFundFamilyNames = mapInvestorDetails.get("Fund Family Name");
				aFundFamilyNames = Arrays.asList(sFundFamilyNames.split(","));
			}
			if (mapInvestorDetails.get("Legal Entity Name") != null) {
				sLegalEntityNames = mapInvestorDetails.get("Legal Entity Name");
				aLegalEntityNames = Arrays.asList(sLegalEntityNames.split(","));
			}
			if (mapInvestorDetails.get("From Date") != null) {
				sFromDate = mapInvestorDetails.get("From Date");
				aFromDate = Arrays.asList(sFromDate.split(","));
			}
			if (mapInvestorDetails.get("To Date") != null) {
				sToDate = mapInvestorDetails.get("To Date");
				aToDate = Arrays.asList(sToDate.split(","));
			}
			if (sClientNames != null ) {
				for (int i = 0; i < aClientNames.size(); i++) {
					if (!aClientNames.get(i).equalsIgnoreCase("None")) {
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aClientNames.get(i), By.xpath("//div[@id='s2id_clientid_"+i+"']//span[contains(@id,'select2-chosen')]"));
						if (!bStatus) {
							Messages.errorMsg = aClientNames.get(i)+" : option cannot be selected from the dropdown of Client for General Partner details at index : "+i+" .";
							return false;
						}
						TimeUnit.SECONDS.sleep(3);
					}
					if (!aFundFamilyNames.get(i).equalsIgnoreCase("None")) {
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aFundFamilyNames.get(i), By.xpath("//div[@id='s2id_cmbFundFamilyName"+i+"']//span[contains(@id,'select2-chosen')]"));
						if (!bStatus) {
							Messages.errorMsg = aFundFamilyNames.get(i)+" : option cannot be selected from the dropdown of Fund Family for General Partner details at index : "+i+" .";
							return false;
						}
						TimeUnit.SECONDS.sleep(4);
					}
					if (!aLegalEntityNames.get(i).equalsIgnoreCase("None")) {

						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aLegalEntityNames.get(i), By.xpath("//div[@id='s2id_cmbFundName"+i+"']//span[contains(@id,'select2-chosen')]"));
						if (!bStatus) {
							Messages.errorMsg = aLegalEntityNames.get(i)+" : option cannot be selected from the dropdown of Legal Entity for General Partner details at index : "+i+" .";
							return false;
						}
						/*boolean bAlertStatus = Wait.waitForAlert(Global.driver, 5);
						if (bAlertStatus) {
							Alerts.acceptAlert(Global.driver);
						}*/
					}
					//Entering From Date.
					if (!aFromDate.get(i).equalsIgnoreCase("None")) {
						//bStatus = Elements.enterText(Global.driver, By.id("fromDateRange"+i+""), aFromDate.get(i));
						By objLocator = By.id("fromDateRange"+i+"");
						bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(objLocator, aFromDate.get(i));
						if (!bStatus) {
							Messages.errorMsg = aFromDate.get(i)+" : From Date cannot be inputed for General Partner details at index : "+i+" .";
							return false;
						}
						Elements.click(Global.driver, By.xpath("//p[contains(text(),'Investor Master')]"));
						//NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//strong[contains(text(),'General Partner')]"));
					}

					//Entering To Date.					
					if (!aToDate.get(i).equalsIgnoreCase("None")) {
						//Elements.click(Global.driver, By.id("fromDateRange"+i+""));
						//bStatus = Elements.enterText(Global.driver, By.id("toDateRange"+i+""), aToDate.get(i));
						By objLocator = By.id("toDateRange"+i+"");
						bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(objLocator, aToDate.get(i));
						if (!bStatus) {
							Messages.errorMsg = aToDate.get(i)+" : To Date cannot be inputed for General Partner details at index : "+i+" .";
							return false;
						}						
						Elements.click(Global.driver, By.xpath("//p[contains(text(),'Investor Master')]"));
						NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//strong[contains(text(),'General Partner')]"));
					}
					if(i<aClientNames.size()-1){
						bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'addGenpartnerList')]") );
						if(!bStatus){
							Messages.errorMsg = "Add More Button Not Clicked to add more General Partner Details.";
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bStatus;
	}

	private static boolean addMoreInvestmentManagerDetails(Map<String, String> mapInvestorDetails) {
		try {
			String sIMClientNames = null, sIMFundFamilyNames = null, sIMLegalEntityNames = null, sIMFromDate = null, sIMToDate = null;
			List<String> aIMClientNames = null, aIMFundFamilyNames = null, aIMLegalEntityNames = null, aIMFromDate = null, aIMToDate = null;

			int noOfRemoveButtons = 0;
			noOfRemoveButtons = Elements.getXpathCount(Global.driver, By.xpath("//button[contains(@onclick,'removeInvManagerList')]"));
			for (int i = 1; i <= noOfRemoveButtons; i++) {				
				String sLocator = "//button[@onclick=concat('removeInvManagerList(',\"'\"'/veda2','"+i+"',\"')\"]";				
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(sLocator));
				if (!bStatus) {
					Messages.errorMsg = "Unable to remove additional Investment Manager details.";
				}
			}

			if (mapInvestorDetails.get("Client") != null) {
				sIMClientNames = mapInvestorDetails.get("Client");
				aIMClientNames = Arrays.asList(sIMClientNames.split(","));
			}
			if (mapInvestorDetails.get("FundFamily") != null) {
				sIMFundFamilyNames = mapInvestorDetails.get("FundFamily");
				aIMFundFamilyNames = Arrays.asList(sIMFundFamilyNames.split(","));
			}
			if (mapInvestorDetails.get("LegalEntity") != null) {
				sIMLegalEntityNames = mapInvestorDetails.get("LegalEntity");
				aIMLegalEntityNames = Arrays.asList(sIMLegalEntityNames.split(","));
			}
			if (mapInvestorDetails.get("FromDate") != null) {
				sIMFromDate = mapInvestorDetails.get("FromDate");
				aIMFromDate = Arrays.asList(sIMFromDate.split(","));
			}
			if (mapInvestorDetails.get("ToDate") != null) {
				sIMToDate = mapInvestorDetails.get("ToDate");
				aIMToDate = Arrays.asList(sIMToDate.split(","));
			}
			if (sIMClientNames != null ) {
				for (int i = 0; i < aIMClientNames.size(); i++) {
					if (!aIMClientNames.get(i).equalsIgnoreCase("None")) {
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aIMClientNames.get(i), By.xpath("//div[@id='s2id_clientidIm_"+i+"']//span[contains(@id,'select2-chosen')]"));
						if (!bStatus) {
							Messages.errorMsg = aIMClientNames.get(i)+" : option cannot be selected from the dropdown of Client for Investment Manager details at index : "+i+" .";
							return false;
						}
						TimeUnit.SECONDS.sleep(3);
					}
					if (!aIMFundFamilyNames.get(i).equalsIgnoreCase("None")) {
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aIMFundFamilyNames.get(i), By.xpath("//div[@id='s2id_cmbFundFamilyIm_"+i+"']//span[contains(@id,'select2-chosen')]"));
						if (!bStatus) {
							Messages.errorMsg = aIMFundFamilyNames.get(i)+" : option cannot be selected from the dropdown of Fund Family for Investment Manager details at index : "+i+" .";
							return false;
						}
						TimeUnit.SECONDS.sleep(4);

					}
					if (!aIMLegalEntityNames.get(i).equalsIgnoreCase("None")) {
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aIMLegalEntityNames.get(i), By.xpath("//div[contains(@id,'s2id_cmbFundIm_"+i+"')]//span[contains(@id,'select2-chosen')]"));
						if (!bStatus) {
							Messages.errorMsg = aIMLegalEntityNames.get(i)+" : option cannot be selected from the dropdown of Legal Entity for Investment Manager details at index : "+i+" .";
							return false;
						}
						/*boolean bAlertStatus = Wait.waitForAlert(Global.driver, 5);
						if (bAlertStatus) {
							Alerts.acceptAlert(Global.driver);
						}*/
					}
					if (!aIMFromDate.get(i).equalsIgnoreCase("None")) {
						By objLocator = By.id("fromDateRangeIm"+i+"");
						bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(objLocator, aIMFromDate.get(i));
						if (!bStatus) {
							Messages.errorMsg = aIMFromDate.get(i)+" : From Date cannot be inputed for Investment Manager details at index : "+i+" .";
							return false;
						}
						/*Elements.click(Global.driver, By.xpath("//p[contains(text(),'Investor Master')]"));
						NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//strong[contains(text(),'Investment Manager')]"));*/						
					}
					if (!aIMToDate.get(i).equalsIgnoreCase("None")) {
						By objLocator = By.id("toDateRangeIm"+i+"");
						bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(objLocator, aIMToDate.get(i));
						if (!bStatus) {
							Messages.errorMsg = aIMToDate.get(i)+" : To Date cannot be inputed for Investment Manager details at index : "+i+" .";
							return false;
						}
						/*Elements.click(Global.driver, By.xpath("//p[contains(text(),'Investor Master')]"));
						NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//strong[contains(text(),'Investment Manager')]"));*/
					}
					if(i<aIMClientNames.size()-1){
						bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'addInvManagerList')]") );
						if(!bStatus){
							Messages.errorMsg = "Add More Button Not Clicked to add more Investment Manager Details.";
							return false;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bStatus;
	}

	public static boolean doFillFatcaDetails(Map<String, String> mapFatcaDeatils){
		try{
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("FATCA Details");
			if(!bStatus){
				Messages.errorMsg = "Wasn't Navigated to FATCA Details Tab";
				return false;
			}
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(text(),'Power of Attorney to US person')]"), 6);

			if(!InvestorType.equalsIgnoreCase("")){

				if(mapFatcaDeatils.get("PowerAttorneyToUS")!=null){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'Power of Attorney to US person')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("PowerAttorneyToUS")+"']//input"));
					if(!bStatus){
						Messages.errorMsg = mapFatcaDeatils.get("PowerAttorneyToUS")+" option cannot be selected for radio button field name power of attorney to US person";
						return false;
					}
				}

				if(mapFatcaDeatils.get("SignatoryAuthorityPersonInUS")!=null){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'Signatory Authority to person in US')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("SignatoryAuthorityPersonInUS")+"']//input"));
					if(!bStatus){
						Messages.errorMsg = mapFatcaDeatils.get("SignatoryAuthorityPersonInUS")+" option cannot be selected for radio button field name Sign Authority to US person";
						return false;
					}
				}

				if(mapFatcaDeatils.get("StandingPaymentInstructionsToUSAccount")!=null){
					bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'Standing Payment Instructions to US account')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("StandingPaymentInstructionsToUSAccount")+"']//input"));
					if(!bStatus){
						Messages.errorMsg = mapFatcaDeatils.get("StandingPaymentInstructionsToUSAccount")+" option cannot be selected for radio button field name Standing payment instructions to US account";
						return false;
					}
				}

				if(mapFatcaDeatils.get("Level1Classification")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Level 1 Classification",mapFatcaDeatils.get("Level1Classification"), 5);
					if(!bStatus){
						return false;
					}
				}

				if(InvestorType.equalsIgnoreCase("Entity")){
					if(mapFatcaDeatils.get("UnderLyingBeneficialOwner")!=null){
						bStatus = Elements.enterText(Global.driver,By.id("investorFatcaDetails0.underlyingBeneficalOwner"),mapFatcaDeatils.get("UnderLyingBeneficialOwner"));
						if(!bStatus){
							Messages.errorMsg = "Text cannot be entered in Underlying Beneficial Owner";
							return false;
						}
					}
				}

				if(mapFatcaDeatils.get("Level2Classification")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Level 2 Classification",mapFatcaDeatils.get("Level2Classification"), 5 );
					if(!bStatus){
						return false;
					}
				}
				if(InvestorType.equalsIgnoreCase("Entity")){
					if(mapFatcaDeatils.get("OwnerShipPercentage")!=null){
						bStatus = Elements.enterText(Global.driver,By.id("investorFatcaDetails0.ownerPercentage"),mapFatcaDeatils.get("OwnerShipPercentage"));
						if(!bStatus){
							Messages.errorMsg = "Text cannot be entered in owner ship percentage";
							return false;
						}
					}
				}
				if(mapFatcaDeatils.get("FATCA Classification")!=null){
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("FATCA Classification",mapFatcaDeatils.get("FATCA Classification"), 5 );
					if(!bStatus){
						return false;
					}
				}

				if(InvestorType.equalsIgnoreCase("Individual")){
					if(mapFatcaDeatils.get("USPerson")!=null){
						bStatus = Elements.click(Global.driver, By.xpath("//label[contains(text(),'US Person')]/following-sibling::div//label[normalize-space()='"+mapFatcaDeatils.get("USPerson")+"']//input"));
						if(!bStatus){
							Messages.errorMsg = mapFatcaDeatils.get("USPerson")+" option cannot be selected for radio button field name US person";
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
						Messages.errorMsg = mapFatcaDeatils.get("FATCADocumentation")+" option cannot be selected for radio button field name FATCA documentation";
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

	public static boolean removeAllTaxIDs(String sInvestorType){
		try{
			String sRemoveInvestorTypeTaxCount = "";			
			if (sInvestorType.equalsIgnoreCase("Individual")) {
				sRemoveInvestorTypeTaxCount = "//button[contains(@onclick,'removeTaxDetailsIndv')]";
			}
			else if (sInvestorType.equalsIgnoreCase("Entity")) {
				sRemoveInvestorTypeTaxCount = "//button[contains(@onclick,'removeTaxDetailsEntity')]";
			}
			int iRemovebtnCount = Elements.getXpathCount(Global.driver,By.xpath(sRemoveInvestorTypeTaxCount));
			if (iRemovebtnCount == 0) {
				NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'addMoreTaxDetails')]"));
				if (sInvestorType.equalsIgnoreCase("Individual")) {
					NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'removeTaxDetailsIndv') and contains(@onclick,'1')]"));
				}
				else if (sInvestorType.equalsIgnoreCase("Entity")) {
					NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'removeTaxDetailsEntity') and contains(@onclick,'1')]"));
				}			
			}
			for (int i = iRemovebtnCount; i >= 1 ; i--) {
				if (sInvestorType.equalsIgnoreCase("Individual")) {
					NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'removeTaxDetailsIndv') and contains(@onclick,'"+i+"')]"));
				}else if (sInvestorType.equalsIgnoreCase("Entity")) {
					NewUICommonFunctions.jsClick(By.xpath("//button[contains(@onclick,'removeTaxDetailsEntity') and contains(@onclick,'"+i+"')]"));
				}				
			}
			iRemovebtnCount = Elements.getXpathCount(Global.driver,By.xpath(sRemoveInvestorTypeTaxCount));
			if(iRemovebtnCount >= 1){
				Messages.errorMsg = " [ ERROR : Additional Tax details wasn't removed when click on respective Remove buttons for Investor Type : "+sInvestorType+" .] \n";
				return false;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyGeneralDetailsScreen(Map<String, String> mapGeneralDetails){
		try{
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			String appendMsg = "";
			boolean verifyStatus= true;
			bStatus = NewUICommonFunctions.selectTheTab("General Details");
			if(!bStatus){
				verifyStatus = false;
			}
			InvestorType = "";
			if(mapGeneralDetails.get("InvestorType")!=null){
				InvestorType = mapGeneralDetails.get("InvestorType");

				//VeriFy the Investor type
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Investor Type", mapGeneralDetails.get("InvestorType"));
				if(!bStatus){
					verifyStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
				}
			}

			//Verify the details for the investor type Entity
			if(mapGeneralDetails.get("InvestorType")!=null && InvestorType.equals("Entity")){
				//Verify same as holder
				if(mapGeneralDetails.get("SameHolder")!=null){
					String sValue = Elements.getText(Global.driver, By.xpath("//label[text()='Same Holder ']/../div"));
					if(!sValue.equalsIgnoreCase(mapGeneralDetails.get("SameHolder"))){
						Messages.errorMsg = "Error: The actual value: "+sValue+" is not equal to the expected value: "+mapGeneralDetails.get("SameHolder")+" for the field same holder";
						appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}


				//Verify the Entity type
				if(mapGeneralDetails.get("EntityType")!=null){
					bStatus = NewUICommonFunctions.verifyTextInDropDown("Entity Type",mapGeneralDetails.get("EntityType") );
					if(!bStatus){
						appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}


				//Verify the Registration Name
				if(mapGeneralDetails.get("RegistrationName")!=null){
					bStatus = NewUICommonFunctions.verifyTextInTextBox("Registration Name",mapGeneralDetails.get("RegistrationName") );
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify  Date of incorporation
				if(mapGeneralDetails.get("IncorporationDate")!=null){
					bStatus = NewUICommonFunctions.verifyTextInTextBox("Date Of Incorporation",mapGeneralDetails.get("IncorporationDate") );
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Country
				if(mapGeneralDetails.get("IncorporationCountry")!=null){
					bStatus = NewUICommonFunctions.verifyTextInDropDown("Country Of Incorporation",mapGeneralDetails.get("IncorporationCountry") );
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}



				//Verify the External ID's
				/*
				if(mapGeneralDetails.get("ExtID1")!=null){
					bStatus = NewUICommonFunctions.verifyTextInTextBox("External Id1", mapGeneralDetails.get("ExtID1"));
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				if(mapGeneralDetails.get("ExtID2")!=null){
					bStatus = NewUICommonFunctions.verifyTextInTextBox("External Id2", mapGeneralDetails.get("ExtID2"));
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				if(mapGeneralDetails.get("ExtID3")!=null){
					bStatus = NewUICommonFunctions.verifyTextInTextBox("External Id3", mapGeneralDetails.get("ExtID3"));
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}*/
			}
			if (InvestorType.equalsIgnoreCase("Individual") || InvestorType.equalsIgnoreCase("Entity")) {
				//Verify tax details
				if(mapGeneralDetails.get("TaxID")!=null && mapGeneralDetails.get("TaxState")!=null && mapGeneralDetails.get("TaxCountry ")!=null ){					
					bStatus = verifyTaxesDetailsForInvestorType(mapGeneralDetails.get("TaxID"),mapGeneralDetails.get("TaxState"),mapGeneralDetails.get("TaxCountry"));
					if(!bStatus){
						appendMsg = appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
			}
			if (InvestorType.equalsIgnoreCase("Individual")) {
				if (mapGeneralDetails.get("Salutation") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkSalutationIdPk']//span[contains(@id,'select2-chosen')]"), mapGeneralDetails.get("Salutation"), "No");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("FirstName") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("individualFirstName"), mapGeneralDetails.get("FirstName"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("MiddleName") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("individualMiddleInitials"), mapGeneralDetails.get("MiddleName"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("LastName") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("individualLastName"), mapGeneralDetails.get("LastName"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("Sex") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkSexIdPk']//span[contains(@id,'select2-chosen')]"), mapGeneralDetails.get("Sex"), "No");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("DateOfBirth") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("dateOfBirthNew"), mapGeneralDetails.get("DateOfBirth"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("PlaceOfBirth") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("placeOfBirth"), mapGeneralDetails.get("PlaceOfBirth"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("ReportingCurrency") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkReportingCurrencyPreferenceIdPk']//span[contains(@id,'select2-chosen')]"), mapGeneralDetails.get("ReportingCurrency"), "No");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("PassPortNumber") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("passportNo"), mapGeneralDetails.get("PassPortNumber"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("PassportExpiry") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("passportExpiryDate"), mapGeneralDetails.get("PassportExpiry"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}
				if (mapGeneralDetails.get("RiskLevel") != null) {
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkRiskLevelIdPk']//span[contains(@id,'select2-chosen')]"), mapGeneralDetails.get("RiskLevel"), "No");
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

	private static boolean verifyTaxesDetailsForInvestorType(String TaxIds, String States, String Countries) {
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
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//input[contains(@name,'fkInvestorTaxDetails["+iValue+"]') and contains(@placeholder,'ID')]"), arrTaxIDs[iValue], "Yes");					
					if(!bStatus){
						sAppendMessage = sAppendMessage + Messages.errorMsg +" \n";
						bValidateStatus = false;
					}
				}
				//enter the domicile state name
				if(!arrStates[iValue].equalsIgnoreCase("None")){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//input[contains(@name,'fkInvestorTaxDetails["+iValue+"]') and contains(@placeholder,'State')]"), arrStates[iValue], "Yes");					
					if(!bStatus){
						sAppendMessage = sAppendMessage + Messages.errorMsg +" \n";
						bValidateStatus = false;
					}
				}
				//enter the domicile country name
				if(!arrCountries[iValue].equalsIgnoreCase("None")){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//div[contains(@id,'s2id_fkInvestorTaxDetails"+iValue+"')]//span[contains(@id,'select2-chosen')]"), arrCountries[iValue], "No");
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

	private static boolean verifyTextInTextBoxOrDropDown(By objLocator, String sExpectedText, String YesForAttrValNoForPlaceHolder) {
		try {
			String sReadAttrVal = "";
			if (YesForAttrValNoForPlaceHolder.equalsIgnoreCase("Yes")) {
				sReadAttrVal = Elements.getElementAttribute(Global.driver, objLocator, "value").trim();
				if (sReadAttrVal.equalsIgnoreCase("") || !sReadAttrVal.equalsIgnoreCase(sExpectedText)) {
					Messages.errorMsg = " [ ERROR : Expected value "+sExpectedText+" for : "+objLocator+" is not matching with actual : "+sReadAttrVal+" . ]";
					return false;
				}
				return true;
			}else if (YesForAttrValNoForPlaceHolder.equalsIgnoreCase("No")) {
				sReadAttrVal = Elements.getText(Global.driver, objLocator).trim();
				if (sReadAttrVal.equalsIgnoreCase("") || !sReadAttrVal.equalsIgnoreCase(sExpectedText)) {
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

	public static boolean doVerifyCorrespondenceAddressDetails(Map<String, String> mapCorrespondenceAddr){
		try{
			String appendMsg = "";
			boolean verifyStatus= true;

			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("Address of Correspondence");
			if(!bStatus){
				Messages.errorMsg = appendMsg+"[ Address of correspondance tab is not clicked ] ";
				verifyStatus = false;
			}

			Wait.waitForElementVisibility(Global.driver, By.id("corBuildingArea"), 5);
			if(!InvestorType.equalsIgnoreCase("")){

				//verify the building name
				if(mapCorrespondenceAddr.get("Building")!=null){					
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corBuildingArea"), mapCorrespondenceAddr.get("Building"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}

					//Verify the Floor name
					if(mapCorrespondenceAddr.get("Floor")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corFloorSuiteApt"), mapCorrespondenceAddr.get("Floor"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify  the Street no number
					if(mapCorrespondenceAddr.get("StreetNumber")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corStreetNo"), mapCorrespondenceAddr.get("StreetNumber"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify  the Street name
					if(mapCorrespondenceAddr.get("StreetName")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corStreetName"), mapCorrespondenceAddr.get("StreetName"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the city name
					if(mapCorrespondenceAddr.get("City")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corCity"), mapCorrespondenceAddr.get("City"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the state name
					if(mapCorrespondenceAddr.get("State")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corState"), mapCorrespondenceAddr.get("State"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the Country name
					if(mapCorrespondenceAddr.get("Country")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_corCountryIdPk']//span[contains(@id,'select2-chosen')]"), mapCorrespondenceAddr.get("Country"), "No");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the PIN code
					if(mapCorrespondenceAddr.get("PostalCode")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corPostalCode"), mapCorrespondenceAddr.get("PostalCode"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the Email1 
					if(mapCorrespondenceAddr.get("EmailAddr1")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corEmailAddress1"), mapCorrespondenceAddr.get("EmailAddr1"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the Email2
					if(mapCorrespondenceAddr.get("EmailAddr2")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corEmailAddress2"), mapCorrespondenceAddr.get("EmailAddr2"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}


					//Verify the phone1
					if(mapCorrespondenceAddr.get("Phone1")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corPhone1"), mapCorrespondenceAddr.get("Phone1"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the phone2
					if(mapCorrespondenceAddr.get("Phone2")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='tab2']//div[contains(@class,'col-md')][4]//input[@id='corPhone2']"), mapCorrespondenceAddr.get("Phone2"), "Yes");
						if(!bStatus){
							appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
							verifyStatus = false;
						}
					}

					//Verify the Fax
					if(mapCorrespondenceAddr.get("Fax")!=null){
						bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("corFax"), mapCorrespondenceAddr.get("Fax"), "Yes");
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
				if(InvestorType.equalsIgnoreCase("Entity") && mapCorrespondenceAddr.get("CorrespondenceAndRegisteredAddrSame")!=null){
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

	public static boolean doVerifyRegisteredAddressDetails(Map<String, String> mapRegisteredAddrDetails, Map<String, String> mapCorrespondenceAddrDetails){
		String appendMsg = "";
		boolean verifyStatus= true;
		Map<String, String> mapClubbedAddressDetails = new HashMap<>();
		try{
			if (!InvestorType.equalsIgnoreCase("Entity")) {
				return true;
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("Registered Address Details");
			if(!bStatus){
				appendMsg = appendMsg+" [ERROR: Registered Address Details  Tab is not Clicked ]\n"; 
				verifyStatus = false;
			}
			if(mapRegisteredAddrDetails== null && mapCorrespondenceAddrDetails == null){
				return true ;
			}
			if(!InvestorType.equalsIgnoreCase("") && InvestorType.equalsIgnoreCase("Entity")){
				//Getting Actual Correspondence Addr Details.
				Map<String, String> mapActualCorrespondenceAddrDetails = new HashMap<>();
				if (mapCorrespondenceAddrDetails != null) {
					mapActualCorrespondenceAddrDetails = ExcelUtils.readDataABasedOnCellName(Global.sInvestorTestDataFilePath, "AddressofCorrespondence", mapCorrespondenceAddrDetails.get("TestcaseNameRef"));
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
				}
				Wait.waitForElementVisibility(Global.driver, By.id("buildingArea"), 5);
				//verify the building name
				if(mapClubbedAddressDetails.get("Building")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("buildingArea"), mapClubbedAddressDetails.get("Building"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Floor name
				if(mapClubbedAddressDetails.get("Floor")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("floorSuiteApt"), mapClubbedAddressDetails.get("Floor"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify  the Street no number
				if(mapClubbedAddressDetails.get("StreetNumber")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("streetNo"), mapClubbedAddressDetails.get("StreetNumber"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify  the Street name
				if(mapClubbedAddressDetails.get("StreetName")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("streetName"), mapClubbedAddressDetails.get("StreetName"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the city name
				if(mapClubbedAddressDetails.get("City")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("city"), mapClubbedAddressDetails.get("City"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the state name
				if(mapClubbedAddressDetails.get("State")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("state"), mapClubbedAddressDetails.get("State"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Country name
				if(mapClubbedAddressDetails.get("Country")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='s2id_fkCountryIdPk']//span[contains(@id,'select2-chosen')]"), mapClubbedAddressDetails.get("Country"), "No");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the PIN code
				if(mapClubbedAddressDetails.get("PostalCode")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("postalCode"), mapClubbedAddressDetails.get("PostalCode"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Email1 
				if(mapClubbedAddressDetails.get("EmailAddr1")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("emailAddress1"), mapClubbedAddressDetails.get("EmailAddr1"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Email2
				if(mapClubbedAddressDetails.get("EmailAddr2")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("emailAddress2"), mapClubbedAddressDetails.get("EmailAddr2"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}


				//Verify the phone1
				if(mapClubbedAddressDetails.get("Phone1")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("phone1"), mapClubbedAddressDetails.get("Phone1"), "Yes");
					if (!bStatus) {
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}					
				}		


				//Verify the phone2
				if(mapClubbedAddressDetails.get("Phone2")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.xpath("//div[@id='tab3']//div[@class='row'][3]//div[contains(@class,'col-md-')][4]//input"), mapClubbedAddressDetails.get("Phone2"), "Yes");
					if(!bStatus){
						appendMsg =appendMsg+"[ "+Messages.errorMsg+" ]\n";
						verifyStatus = false;
					}
				}

				//Verify the Fax
				if(mapClubbedAddressDetails.get("Fax")!=null){
					bStatus = InvestorMasterAppFunctions.verifyTextInTextBoxOrDropDown(By.id("fax"), mapClubbedAddressDetails.get("Fax"), "Yes");
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
			Wait.waitForElementVisibility(Global.driver,By.xpath("//label[contains(text(),'Power of Attorney to US person')]"), 5);
			if(!InvestorType.equalsIgnoreCase("")){
				if (InvestorType.equalsIgnoreCase("Entity") || InvestorType.equalsIgnoreCase("Individual")) {
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

					if(mapFatcaDeatils.get("Level3Classification") != null){
						bStatus = NewUICommonFunctions.verifyTextInDropDown("Level 3 Classification", mapFatcaDeatils.get("Level3Classification"));
						if(!bStatus){
							sAppendMessage = sAppendMessage +"[ ERROR : "+ mapFatcaDeatils.get("Level2Classification") + " : value for selected Level 3 Classification wasn't visible. ] \n";
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

					if (InvestorType.equalsIgnoreCase("Entity")) {
						if(mapFatcaDeatils.get("OwnerShipPercentage") != null){
							bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//input[@id='investorFatcaDetails0.ownerPercentage' and contains(@value,'"+mapFatcaDeatils.get("OwnerShipPercentage")+"')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("OwnerShipPercentage")+" : value wasn't visible in owner ship percentage field. ] \n";
								bValidateStatus = false;
							}
						}

						if(mapFatcaDeatils.get("UnderLyingBeneficialOwner") != null){
							bStatus = Verify.verifyElementVisible(Global.driver,By.xpath("//input[@id='investorFatcaDetails0.underlyingBeneficalOwner' and contains(@value,'"+mapFatcaDeatils.get("UnderLyingBeneficialOwner")+"')]"));
							if(!bStatus){
								sAppendMessage = sAppendMessage +"[ ERROR : "+mapFatcaDeatils.get("UnderLyingBeneficialOwner")+" : value wasn't visible in Underlying Beneficial Owner field. ] \n";
								bValidateStatus = false;
							}
						}
					}

					if(InvestorType.equalsIgnoreCase("Individual")){
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

	public static boolean doVerifyInvestorDetails(Map<String, String> mapInvestorDetails){
		String sAppendMessage = "";
		boolean bValidateStatus = true ;
		String isGPorIMorNA = "NA";
		try{
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("Investor Details");
			if(!bStatus){
				sAppendMessage = sAppendMessage+" [ERROR: Registered Address Details  Tab is not Clicked ]\n"; 
				bValidateStatus = false;
			}
			Wait.waitForElementVisibility(Global.driver, By.xpath("//label[@for='isErisaInvestor']"), 5);

			if(InvestorType.equalsIgnoreCase("Individual") || InvestorType.equalsIgnoreCase("Entity")){
				if(mapInvestorDetails.get("ErisaInvestor")!=null){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[@for='isErisaInvestor']/following-sibling::div//label[normalize-space()='"+mapInvestorDetails.get("ErisaInvestor")+"']//input//parent::span[contains(@class,'checked')]"));
					if(!bStatus){
						sAppendMessage = sAppendMessage+" [ ERROR : Is ErisaInvestor Manager option wasn't checked for Value : "+ mapInvestorDetails.get("ErisaInvestor") +". ] \n";
						bValidateStatus = false;
					}
				}
				if(mapInvestorDetails.get("SidePocketAllowed")!=null){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[@for='isSidePocketAllowed']/following-sibling::div//label[normalize-space()='"+mapInvestorDetails.get("SidePocketAllowed")+"']//input//parent::span[contains(@class,'checked')]"));
					if(!bStatus){
						sAppendMessage = sAppendMessage+" [ ERROR : Is SidePocketAllowed Manager option wasn't checked for Value : "+ mapInvestorDetails.get("SidePocketAllowed") +". ] \n";
						bValidateStatus = false;
					}
				}
				if(mapInvestorDetails.get("isGPOrIMOrNA") != null){
					if(mapInvestorDetails.get("isGPOrIMOrNA").equalsIgnoreCase("GP")){
						isGPorIMorNA = "GP";
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='isGpYes']//parent::span[@class='checked']"), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = " ERROR : "+isGPorIMorNA+" option wasn't marked as selected for radio button field name Is General partner";
							return false;
						}
					}
					if(mapInvestorDetails.get("isGPOrIMOrNA").equalsIgnoreCase("IM")){
						isGPorIMorNA = "IM";
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='isIMYes']//parent::span[@class='checked']"), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = " ERROR : "+isGPorIMorNA+" option wasn't marked as selected for radio button field name Is Investment Manager";
							return false;
						}
					}
					if(mapInvestorDetails.get("isGPOrIMOrNA").equalsIgnoreCase("NA")){
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='gpIMNA']//parent::span[@class='checked']"), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = " ERROR : "+isGPorIMorNA+" option wasn't marked as selected for radio button field name 'GP/IM N/A'.";
							return false;
						}
					}
				}
			}
			if(InvestorType.equalsIgnoreCase("Individual")){
				if(mapInvestorDetails.get("InvestorSubType")!=null){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//label[@for='investorSubType']/following-sibling::div//label[normalize-space()='"+mapInvestorDetails.get("InvestorSubType")+"']//input//parent::span[contains(@class,'checked')]"));
					if(!bStatus){
						sAppendMessage = sAppendMessage+" [ ERROR : InvestorSubType option wasn't checked for Value : "+ mapInvestorDetails.get("InvestorSubType") +". ] \n";
						bValidateStatus = false;
					}
				}
			}
			if (isGPorIMorNA.equalsIgnoreCase("GP") || isGPorIMorNA.equalsIgnoreCase("IM")) {
				bStatus = FeeDistributionAppFunction.doVerifyGPorIMDetails(mapInvestorDetails);
				if (!bStatus) {
					return false;
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

	private static boolean verifyAddMoreInvestmentManagerDetails(Map<String, String> mapInvestorDetails) {
		String sAppendMessage = "";
		boolean bValidateStatus = true ;
		try {
			String sIMClientNames = null, sIMFundFamilyNames = null, sIMLegalEntityNames = null, sIMFromDate = null, sIMToDate = null;
			List<String> aIMClientNames = null, aIMFundFamilyNames = null, aIMLegalEntityNames = null, aIMFromDate = null, aIMToDate = null;

			if (mapInvestorDetails.get("Client") != null && mapInvestorDetails.get("FundFamily") != null && mapInvestorDetails.get("LegalEntity") != null && mapInvestorDetails.get("FromDate") != null && mapInvestorDetails.get("ToDate") != null) {
				sIMClientNames = mapInvestorDetails.get("Client");
				aIMClientNames = Arrays.asList(sIMClientNames.split(","));

				sIMFundFamilyNames = mapInvestorDetails.get("FundFamily");
				aIMFundFamilyNames = Arrays.asList(sIMFundFamilyNames.split(","));

				sIMLegalEntityNames = mapInvestorDetails.get("LegalEntity");
				aIMLegalEntityNames = Arrays.asList(sIMLegalEntityNames.split(","));

				sIMFromDate = mapInvestorDetails.get("FromDate");
				aIMFromDate = Arrays.asList(sIMFromDate.split(","));

				sIMToDate = mapInvestorDetails.get("ToDate");
				aIMToDate = Arrays.asList(sIMToDate.split(","));

				if (aIMClientNames.size() != aIMFundFamilyNames.size() || aIMFundFamilyNames.size() != aIMFromDate.size() || aIMLegalEntityNames.size() != aIMFromDate.size() || aIMToDate.size() != aIMFundFamilyNames.size() || aIMToDate.size() != aIMLegalEntityNames.size()) {
					Messages.errorMsg = " [ TEST DATA ISSUE : Add more Investment Manager Details test data wasn't given properly. ] \n";
					return false;
				}
			}
			else {
				Messages.errorMsg = " [ TEST DATA ISSUE : Add more Investment Manager Details test data wasn't given properly. ] \n";
				return false;
			}

			for (int i = 0; i < aIMClientNames.size(); i++) {
				if (!aIMClientNames.get(i).equalsIgnoreCase("None")) {
					bStatus = InvestorMasterAppFunctions.verifyTextInDropDownUsingCommonlyChagingDDLsIDs(By.xpath("//div[@id='s2id_clientidIm_"+i+"']//span[contains(@id,'select2-chosen')]"), aIMClientNames.get(i));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (!aIMFundFamilyNames.get(i).equalsIgnoreCase("None")) {
					bStatus = InvestorMasterAppFunctions.verifyTextInDropDownUsingCommonlyChagingDDLsIDs(By.xpath("//div[@id='s2id_cmbFundFamilyIm_"+i+"']//span[contains(@id,'select2-chosen')]"),aIMFundFamilyNames.get(i));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (!aIMLegalEntityNames.get(i).equalsIgnoreCase("None")) {
					bStatus = InvestorMasterAppFunctions.verifyTextInDropDownUsingCommonlyChagingDDLsIDs(By.xpath("//div[contains(@id,'s2id_cmbFundIm_"+i+"')]//span[contains(@id,'select2-chosen')]"), aIMLegalEntityNames.get(i));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (!aIMFromDate.get(i).equalsIgnoreCase("None")) {
					bStatus = InvestorMasterAppFunctions.verifyTextIntextBoxUsingCommonlyChagingIDs(By.xpath("//input[@id='fromDateRangeIm"+i+"']"), aIMFromDate.get(i));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (!aIMToDate.get(i).equalsIgnoreCase("None")) {
					bStatus = InvestorMasterAppFunctions.verifyTextIntextBoxUsingCommonlyChagingIDs(By.xpath("//input[@id='toDateRangeIm"+i+"']"), aIMToDate.get(i));						
					if (!bStatus) {
						sAppendMessage = sAppendMessage + Messages.errorMsg;
						bValidateStatus = false;
					}
				}				
				Messages.errorMsg = sAppendMessage;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bValidateStatus;
	}

	private static boolean verifyAddMoreGeneralPartnerDetails(Map<String, String> mapInvestorDetails) {
		String sAppendMessage = "";
		boolean bValidateStatus = true ;
		try {
			String sClientNames = null, sFundFamilyNames = null, sLegalEntityNames = null, sFromDate = null, sToDate = null;
			List<String> aClientNames = null, aFundFamilyNames = null, aLegalEntityNames = null, aFromDate = null, aToDate = null;

			if (mapInvestorDetails.get("Client Name") != null && mapInvestorDetails.get("Fund Family Name") != null && mapInvestorDetails.get("Legal Entity Name") != null && mapInvestorDetails.get("From Date") != null && mapInvestorDetails.get("To Date") != null) {
				sClientNames = mapInvestorDetails.get("Client Name");
				aClientNames = Arrays.asList(sClientNames.split(","));

				sFundFamilyNames = mapInvestorDetails.get("Fund Family Name");
				aFundFamilyNames = Arrays.asList(sFundFamilyNames.split(","));

				sLegalEntityNames = mapInvestorDetails.get("Legal Entity Name");
				aLegalEntityNames = Arrays.asList(sLegalEntityNames.split(","));

				sFromDate = mapInvestorDetails.get("From Date");
				aFromDate = Arrays.asList(sFromDate.split(","));

				sToDate = mapInvestorDetails.get("To Date");
				aToDate = Arrays.asList(sToDate.split(","));

				if (aClientNames.size() != aFundFamilyNames.size() || aLegalEntityNames.size() != aFundFamilyNames.size() || aClientNames.size() != aLegalEntityNames.size()) {
					Messages.errorMsg = " [ TEST DATA ISSUE :  Test data wasn't given properly please check the details given for Add more General Partner Details. ] \n";
					return false;
				}
			}
			else {
				Messages.errorMsg = " [ TEST DATA ISSUE : Test data wasn't given properly please check the details given for Add more General Partner Details. ] \n";
				return false;
			}

			for (int i = 0; i < aClientNames.size(); i++) {
				if (!aClientNames.get(i).equalsIgnoreCase("None")) {
					bStatus = InvestorMasterAppFunctions.verifyTextInDropDownUsingCommonlyChagingDDLsIDs(By.xpath("//div[@id='s2id_clientid_"+i+"']//span[contains(@id,'select2-chosen')]"), aClientNames.get(i));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (!aFundFamilyNames.get(i).equalsIgnoreCase("None")) {
					bStatus = InvestorMasterAppFunctions.verifyTextInDropDownUsingCommonlyChagingDDLsIDs(By.xpath("//div[@id='s2id_cmbFundFamilyName"+i+"']//span[contains(@id,'select2-chosen')]"), aFundFamilyNames.get(i));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (!aLegalEntityNames.get(i).equalsIgnoreCase("None")) {
					bStatus = InvestorMasterAppFunctions.verifyTextInDropDownUsingCommonlyChagingDDLsIDs(By.xpath("//div[@id='s2id_cmbFundName"+i+"']//span[contains(@id,'select2-chosen')]"), aLegalEntityNames.get(i));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (!aFromDate.get(i).equalsIgnoreCase("None")) {
					bStatus = InvestorMasterAppFunctions.verifyTextIntextBoxUsingCommonlyChagingIDs(By.xpath("//input[@id='fromDateRange"+i+"']"), aFromDate.get(i));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + Messages.errorMsg;
						bValidateStatus = false;
					}
				}
				if (!aToDate.get(i).equalsIgnoreCase("None")) {
					bStatus = InvestorMasterAppFunctions.verifyTextIntextBoxUsingCommonlyChagingIDs(By.xpath("//input[@id='toDateRange"+i+"']"), aToDate.get(i));
					if (!bStatus) {
						sAppendMessage = sAppendMessage + Messages.errorMsg;
						bValidateStatus = false;
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

	public static boolean verifyTextInDropDownUsingCommonlyChagingDDLsIDs(By ddlLocator,String sExpectedText){
		try{
			String sActualvalue = Elements.getText(Global.driver, ddlLocator);
			if (sActualvalue == null || !sActualvalue.trim().equalsIgnoreCase(sExpectedText) ) {
				Messages.errorMsg = " [ ERROR : "+sActualvalue+" : is actual value for Label: "+ddlLocator+" which is not matching with Expected value : "+sExpectedText+" . ] \n";
				return false;
			}							
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifyTextIntextBoxUsingCommonlyChagingIDs(By txtLocator, String sExpectedText){
		try{
			String sActualvalue = Elements.getElementAttribute(Global.driver, txtLocator, "value");
			if (sActualvalue == null || !sActualvalue.trim().equalsIgnoreCase(sExpectedText) ) {
				Messages.errorMsg = " [ ERROR : "+sActualvalue+" : is actual value for Label: "+txtLocator+" which is not matching with Expected value : "+sExpectedText+" . ] \n";
				return false;
			}							
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean createNewInvestor(Map<String, Map<String, String>> objInvestorCreationTabsMaps) {
		try {
			if (objInvestorCreationTabsMaps != null) {			
				if (objInvestorCreationTabsMaps.get("GeneralDetails") != null) {
					bStatus = doFillGeneralDetails(objInvestorCreationTabsMaps.get("GeneralDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "Filling out General Details Tab.", "Failed to fill the General Details Tab. "+Messages.errorMsg);
						return false;
					}								
				}	

				if (objInvestorCreationTabsMaps.get("AddressofCorrespondence") != null) {
					bStatus = doFillCorrespondenceAddressDetails(objInvestorCreationTabsMaps.get("AddressofCorrespondence"));
					if (!bStatus) {
						Reporting.logResults("Fail", "Filling out AddressofCorrespondence Tab.", "Failed to fill the AddressofCorrespondence Tab. "+Messages.errorMsg);
						return false;
					}								
				}
				if (objInvestorCreationTabsMaps.get("RegisteredAddressDetails") != null) {
					bStatus = doFillRegisteredAddressDetails(objInvestorCreationTabsMaps.get("RegisteredAddressDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "Filling out RegisteredAddressDetails Tab.", "Failed to fill the RegisteredAddressDetails Tab. "+Messages.errorMsg);
						return false;
					}								
				}
				if (objInvestorCreationTabsMaps.get("InvestorDetails") != null) {
					bStatus = doFillInvestorDetails(objInvestorCreationTabsMaps.get("InvestorDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "Filling out InvestorDetails Tab.", "Failed to fill the InvestorDetails Tab. "+Messages.errorMsg);
						return false;
					}								
				}

				if (objInvestorCreationTabsMaps.get("FatcaDetails") != null) {
					bStatus = doFillFatcaDetails(objInvestorCreationTabsMaps.get("FatcaDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "Filling out FatcaDetails Tab.", "Failed to fill the FatcaDetails Tab. "+Messages.errorMsg);
						return false;
					}								
				}

				if (objInvestorCreationTabsMaps.get("KYCDetails") != null) {
					bStatus = doFillKYCDetails(objInvestorCreationTabsMaps.get("KYCDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "Filling out KYCDetails Tab.", "Failed to fill the KYCDetails Tab. "+Messages.errorMsg);
						return false;
					}								
				}

				if(bTadingInvestorFlag){
					if (InvestorType.equalsIgnoreCase("Entity") && objInvestorCreationTabsMaps.get("GeneralDetails").get("OperationType") != null && objInvestorCreationTabsMaps.get("GeneralDetails").get("OperationType").equalsIgnoreCase("Save") && objInvestorCreationTabsMaps.get("GeneralDetails").get("ExpectedResults") != null && objInvestorCreationTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Pass") && isInvestorGeneralDetailsModifyFlag == false && (objInvestorCreationTabsMaps.get("GeneralDetails").get("SameHolder") == null || objInvestorCreationTabsMaps.get("GeneralDetails").get("SameHolder").equalsIgnoreCase("No"))) {

						bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterTypeAndNotCheckMessage("Investor Master", objInvestorCreationTabsMaps.get("GeneralDetails").get("OperationType"));
						if (!bStatus) {
							return false;
						}
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4//p[contains(text(),'Investor Holder')]"), 8);
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Wasn't navigated to Investor holder page when the Investor 'Same holder' is chosen as : 'NO']";
							return false;
						}
						bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterTypeAndNotCheckMessage("InvestorHolder", "Cancel");
						if (!bStatus) {						
							return false;
						}					
					}				
					else {
						bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterTypeAndNotCheckMessage("Investor Master", objInvestorCreationTabsMaps.get("GeneralDetails").get("OperationType"));
						if (!bStatus) {						
							return false;
						}
					}
					return true;
				}

				if (InvestorType.equalsIgnoreCase("Entity") && objInvestorCreationTabsMaps.get("GeneralDetails").get("OperationType") != null && objInvestorCreationTabsMaps.get("GeneralDetails").get("OperationType").equalsIgnoreCase("Save") && objInvestorCreationTabsMaps.get("GeneralDetails").get("ExpectedResults") != null && objInvestorCreationTabsMaps.get("GeneralDetails").get("ExpectedResults").equalsIgnoreCase("Pass") && isInvestorGeneralDetailsModifyFlag == false && (objInvestorCreationTabsMaps.get("GeneralDetails").get("SameHolder") == null || objInvestorCreationTabsMaps.get("GeneralDetails").get("SameHolder").equalsIgnoreCase("No"))) {

					bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterTypeAndNotCheckMessage("Investor Master", objInvestorCreationTabsMaps.get("GeneralDetails").get("OperationType"));
					if (!bStatus) {
						return false;
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4//p[contains(text(),'Investor Holder')]"), 8);
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Wasn't navigated to Investor holder page when the Investor 'Same holder' is chosen as : 'NO']";
						return false;
					}
					bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterTypeAndNotCheckMessage("InvestorHolder", "Cancel");
					if (!bStatus) {						
						return false;
					}					
				}				
				else {
					bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Investor Master", objInvestorCreationTabsMaps.get("GeneralDetails").get("OperationType"));
					if (!bStatus) {						
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


	public static boolean doFillKYCDetails(Map<String, String> mapKYCDetails) {
		try {
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4//p"));
			bStatus = NewUICommonFunctions.selectTheTab("KYC Details");
			if(!bStatus){
				return false;
			}
			Wait.waitForElementVisibility(Global.driver, By.xpath("//span[text()='Template Name']"), 5);
			if (mapKYCDetails.get("TemplateName") != null) {
				if (isInvestorGeneralDetailsModifyFlag == false) {
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
												
												try {
													Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::div[contains(@class,'col')]//input[@name='file']")).sendKeys(mapKYCDetails.get("ElectricityUploadFile"));
												} catch (Exception e) {
													e.printStackTrace();
													Messages.errorMsg = " ERROR Unable to input KYC file name for the Electricity Bill at indexes of Proof : '"+index+"', kycDoc : '"+docIndex+"'.";
													return false;
												}
												/*//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']"), mapKYCDetails.get("PassportUploadfile"));												

												//Applying Javascript executor functions to enable the file input field to upload file.
												JavascriptExecutor jse = (JavascriptExecutor)Global.driver;												
												jse.executeScript("document.getElementsByClassName('"+Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@placeholder='Upload File']"), "class")+"')[0].removeAttribute('readonly', '');");
												Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@type='text']")).sendKeys(mapKYCDetails.get("PassportUploadfile"));*/
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
												try {
													Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::div[contains(@class,'col')]//input[@name='file']")).sendKeys(mapKYCDetails.get("ElectricityUploadFile"));
												} catch (Exception e) {
													e.printStackTrace();
													Messages.errorMsg = " ERROR Unable to input KYC file name for the Electricity Bill at indexes of Proof : '"+index+"', kycDoc : '"+docIndex+"'.";
													return false;
												}												
												
												/*bStatus = Elements.enterText(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']"), mapKYCDetails.get("ElectricityUploadFile"));
												Applying Javascript executor functions to enable file name field to upload file. 
												JavascriptExecutor jse = (JavascriptExecutor)Global.driver;												
												jse.executeScript("document.getElementsByClassName('"+Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@placeholder='Upload File']"), "class")+"')[0].removeAttribute('readonly', '');");
												Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::input[@type='text']")).sendKeys(mapKYCDetails.get("ElectricityUploadFile"));												
												String readID = Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']//preceding-sibling::div[contains(@class,'col')]//input[@disabled='']"), "class");
												System.out.println(jse.executeScript("document.getElementsByClassName('"+readID+"');"));
												jse.executeScript("document.getElementsByClassName('"+readID+"')[0].removeAttribute('disabled','');");
												Elements.enterText(Global.driver, By.className(readID), mapKYCDetails.get("ElectricityUploadFile"));
												Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[4].fileName']")).sendKeys(mapKYCDetails.get("ElectricityUploadFile"));*/											
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
												
												try {
													Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].fileName']//preceding-sibling::div[contains(@class,'col')]//input[@name='file']")).sendKeys(mapKYCDetails.get("ElectricityUploadFile"));
												} catch (Exception e) {
													e.printStackTrace();
													Messages.errorMsg = " ERROR Unable to input KYC file name for the Electricity Bill at indexes of Proof : '"+index+"', kycDoc : '"+docIndex+"'.";
													return false;
												}
												
												/*
												//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].fileName']"), mapKYCDetails.get("MOAOperatingAgreementUploadfile"));												

												//Applying Javascript executor functions to enable file name field to upload file. 
												JavascriptExecutor jse = (JavascriptExecutor)Global.driver;																				
												jse.executeScript("document.getElementsByClassName('"+Elements.getElementAttribute(Global.driver, By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].fileName']//preceding-sibling::input[@placeholder='Upload File']"), "class")+"')[0].removeAttribute('readonly', '');");
												Global.driver.findElement(By.xpath("//input[@name='kycProofs["+index+"].kycDocuments["+docIndex+"].kycDocumentDetails[5].fileName']//preceding-sibling::input[@type='text']")).sendKeys(mapKYCDetails.get("MOAOperatingAgreementUploadfile"));*/
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

			Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='kycTemplateId']//parent::div[normalize-space(text())='"+mapKYCDetails.get("TemplateName")+"']"), 5);
			if (mapKYCDetails.get("TemplateName") != null) {
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='kycTemplateId']//parent::div[normalize-space(text())='"+mapKYCDetails.get("TemplateName")+"']"));
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

	public static boolean modifyReturnInvestorDetails(Map<String, Map<String, String>> objInvestorModificationTabsMaps, String sInvestorName) {
		try {
			bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, sInvestorName, "");
			if (!bStatus) {
				return false;
			}

			isInvestorGeneralDetailsModifyFlag = true;

			bStatus = createNewInvestor(objInvestorModificationTabsMaps);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean verifyAllTabsOfInvestorMaster(Map<String, Map<String, String>> verifyMasterDetails) {
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
				if (verifyMasterDetails.get("AddressofCorrespondence") != null && InvestorType.equalsIgnoreCase("Entity")) {					
					bStatus = doVerifyRegisteredAddressDetails(verifyMasterDetails.get("RegisteredAddressDetails"), verifyMasterDetails.get("AddressofCorrespondence"));					
					if (!bStatus) {
						Reporting.logResults("Fail", "verifying RegisteredAddressDetails Tab.", "RegisteredAddressDetails Details Tab populated fields data isn't matching with created."+Messages.errorMsg);
						bValidateStatus = false;
					}								
				}
				if (verifyMasterDetails.get("InvestorDetails") != null) {
					bStatus = doVerifyInvestorDetails(verifyMasterDetails.get("InvestorDetails"));
					if (!bStatus) {
						Reporting.logResults("Fail", "verifying InvestorDetails Tab.", "InvestorDetails Tab populated fields data isn't matching with created."+Messages.errorMsg);
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

}