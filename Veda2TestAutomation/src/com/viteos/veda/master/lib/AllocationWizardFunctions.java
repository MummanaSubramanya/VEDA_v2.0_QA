package com.viteos.veda.master.lib;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;

public class AllocationWizardFunctions {
	public static boolean bStatus = true;
	static String sAllocationType = "";
	public static boolean bValidateStatus = true;
	public static String sAppendErrorMsg = "";
	public static String sSAAddGridClassColumn = "2";
	public static String sSAAddGridSeriesColumn = "3";
	public static String sSAAddGridInvestorColumn = "4";
	public static String sSAAddGridCurrentBalanceColumn = "5";
	public static String sSAAddGridUnevenAmountColumn = "6";

	//Main function that triggers Allocation Wizard functions.
	public static boolean doTriggerAllocationProcess(Map<String,String> mapAllocationDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//p[contains(normalize-space(),'Choose Portfolio')]") , Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Next Button is not visible in Choose Portfolio Screen.]\n";
				return false;
			}
			//Select the Portfolio  data
			bStatus = doFillPortfolioDetails(mapAllocationDetails);
			if(!bStatus){
				return false;
			}

			//Break Period Screen Functions
			bStatus = doVerifyBreakPeriodDetailsTabAndDefineBreakPeriods(mapAllocationDetails);
			if(!bStatus){
				return false;
			}
			if(mapAllocationDetails.get("isDeleteBreakPeriod") != null && mapAllocationDetails.get("isDeleteBreakPeriod").equalsIgnoreCase("Yes")){
				return true;
			}
			//Trial Balance Screen Functions
			bStatus = doVerifyAndFillTrialBalance(mapAllocationDetails);
			if(!bStatus){
				return false;
			}

			//Navigate to Next Screen based on the Alert Message
			bStatus = doNavigateToSpecialAllocationScreen(mapAllocationDetails);
			if(!bStatus){
				return false;
			}

			//Special Allocation Screen Functions
			bStatus = doVerifySpecialAllocationScreenDetails(mapAllocationDetails);
			if(!bStatus){
				return false;
			}
			bStatus = doAddNewSpecialAllocations(mapAllocationDetails);
			if (!bStatus) {
				return false;
			}
			bStatus = doVerifyExistingAllocationDetails(mapAllocationDetails);
			if (!bStatus) {
				return false;
			}			

			//Hot Issue Screen Functions 
			bStatus = doMapPnLHeadsForHotissueAllocationsForRespectiveLEs(mapAllocationDetails);
			if (!bStatus) {
				return false;
			}
			bStatus = doAllocateHotIssuesForLEs(mapAllocationDetails);
			if (!bStatus) {
				return false;
			}

			//Fee Adjustment Screen Functions
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 20);			
			bStatus = doFillFeeAdjustmentDetails(mapAllocationDetails);
			if(!bStatus){
				return false;
			}			

			//Process Allocation Screen Functions
			bStatus = doProcessAllocationForTBsUploadedLEs(mapAllocationDetails);
			if (!bStatus) {
				return false;
			}			

			//Nav Finalize Screen Functions
			bStatus = doPerformOperationsOnNAVFreezeScreen(mapAllocationDetails);
			if (!bStatus) {
				return false;
			}

			//series Roll up Screen functions
			bStatus =  doFillOrVerifySeriesRollupScreen(mapAllocationDetails);
			if(!bStatus){
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Navigation to Special Allocation Screen and verifying the TB upload exception pop-up.
	public static boolean doNavigateToSpecialAllocationScreen(Map<String, String> mapAllocationDetails){
		try {
			bStatus = doClickOnAllocationNavigationbuttons("Next");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Next Button Cannot be clicked in Trial Balance Screen.]\n";
				return false;
			}
			if (mapAllocationDetails.get("ProceedOrCancelTBUploadWarning") != null) {
				if (mapAllocationDetails.get("ProceedOrCancelTBUploadWarning").equalsIgnoreCase("Yes")) {
					NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 20);
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[@data-bb-handler='confirm']"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Expected TB upload warning alert is not appeared when TB's upoaded for only few Legal Entities.]\n";
						return false;
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[@data-bb-handler='confirm']"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to Click on 'OK' button of Expected TB upload warning when proceed further without uploading TB's for few Legal Entity's.]\n";
						return false;
					}
					NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 20);
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'Special Allocation')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Special Allocation screen wasn't displayed after confirming the warning alert.]\n";
						return false;
					}
				}
				if (mapAllocationDetails.get("ProceedOrCancelTBUploadWarning").equalsIgnoreCase("No")) {
					NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 20);
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[@data-bb-handler='cancel']"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Expected TB upload warning is not appeared when proceed further without uploading TB's for few Legal Entity's.]\n";
						return false;
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[@data-bb-handler='cancel']"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to Click on 'Cancel' button of Expected TB upload warning when proceed further without uploading TB's for few Legal Entity's.]\n";
						return false;
					}
					NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 20);
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//p[contains(normalize-space(),'Special Allocation')]"));
					if (bStatus) {
						Messages.errorMsg = "[ ERROR : Navigated to 'Special Allocation' screen when 'Canceled' the warning alert.]\n";
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

	//Verifying and filling out TB details.
	private static boolean doVerifyAndFillTrialBalance(Map<String, String> mapAllocationDetails) {
		try {
			if (mapAllocationDetails.get("Client Name") != null) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Client Name' in Trial Balance Screen ", By.id("clientName"), mapAllocationDetails.get("Client Name"), "No", false);
				if (!bStatus) {
					return false;
				}
			}
			if (mapAllocationDetails.get("Fund Family Name") != null) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Fund Family Name' in Trial Balance Screen ", By.id("fundFamilyName"), mapAllocationDetails.get("Fund Family Name"), "No", false);
				if (!bStatus) {
					return false;
				}
			}
			if(mapAllocationDetails.get("Break Period Date")!=null){
				//String sActualvalue = Elements.getElementAttribute(Global.driver, By.id("breakPeriodNavDate"), "value").trim();
				String sActualvalue = Elements.getText(Global.driver, By.xpath("//input[@id='breakPeriodDate']//parent::label"));
				sActualvalue = TradeTypeSubscriptionAppFunctions.formatDate(sActualvalue);
				if (sActualvalue == null || !sActualvalue.equalsIgnoreCase(mapAllocationDetails.get("Break Period Date")) ) {
					Messages.errorMsg = "[ ERROR : '"+sActualvalue+"' : is actual value for field : Break Period in Trial Balance Screen which is not matching with the Expected : '"+mapAllocationDetails.get("Break Period Date")+"']\n";
					return false;
				}
				//Verify To Date in Date From Row
				String sVal = NewUICommonFunctions.jsGetElementAttribute("toDate");
				if(!sVal.equalsIgnoreCase(mapAllocationDetails.get("Break Period Date"))){
					Messages.errorMsg = "[ ERROR : Break Period Date : '"+mapAllocationDetails.get("Break Period Date")+"' ,given in Break Period Screen is Not Matching with the 'To Date' in Trial Balance screen : '"+sVal+"']\n";
					return false;
				}
			}
			if(mapAllocationDetails.get("NAV Type") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'NAV Type' in Trial Balance Screen, ", By.xpath("//input[@id='navType']//parent::label"), mapAllocationDetails.get("NAV Type"), "Yes", false);
				if (!bStatus) {
					return false;
				}
			}
			if(mapAllocationDetails.get("Upload Type") != null){
				if(mapAllocationDetails.get("Upload Type").equalsIgnoreCase("Excel")){
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='trialBalancesObject.uploadTypeCode1']"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Upload Type 'Excel Radio' Radio button cannot be clicked.]\n";
						return false;
					}					
				}
				if(mapAllocationDetails.get("Upload Type").equalsIgnoreCase("Accounting System")){
					bStatus = Elements.click(Global.driver, By.xpath("//input[@id='trialBalancesObject.uploadTypeCode2']"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Upload Type 'Accounting System' Radio button cannot be clicked.]\n";
						return false;
					}					
				}
			}
			if(mapAllocationDetails.get("Trial Balance From Date") != null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='fromDate']"), mapAllocationDetails.get("Trial Balance From Date"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : From Date Cannot be entered in Trial Balance Screen.]\n";
					return false;
				}
			}
			if(mapAllocationDetails.get("TrailBalanceUploadForLegalEntities") != null && mapAllocationDetails.get("FilePaths") != null){
				List<String> aLeList = Arrays.asList(mapAllocationDetails.get("TrailBalanceUploadForLegalEntities").split(","));
				List<String> aFilePaths = Arrays.asList(mapAllocationDetails.get("FilePaths").split(","));
				for(int i=0; i<aLeList.size();i++){
					String loc = "//label[normalize-space()=\""+aLeList.get(i)+"\"]//parent::td//following-sibling::td";					
					Global.driver.findElement(By.xpath(loc+"//div//input[contains(@id,'file')]")).sendKeys(aFilePaths.get(i));
					bStatus = Elements.click(Global.driver, By.xpath(loc+"//button[contains(@id,'uploadBtnExcel')]"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Upload Button cannot be clicked for Legal Entity :"+aLeList.get(i);
						return false;
					}
					NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//th[contains(text(),'Status')]"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Spinner is still there after Uploading the File.]\n";
						return false;
					}
					//TimeUnit.SECONDS.sleep(2);
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(loc+"//span[contains(@id,'upExcelMstPndStatus_') and contains(text(),'Uploaded')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Uploaded Status is Not Populated after Uploading the File.]\n";
						return false;
					}
					if(mapAllocationDetails.get("Processed By") != null){
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(loc+"//label[contains(@id,'processedBy') and contains(normalize-space(),'"+mapAllocationDetails.get("Processed By")+"')]"), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Processed By "+mapAllocationDetails.get("Processed By")+" is Not Populated after Uploading the File.]\n";
							return false;
						}
					}					
				}
			}
			//setAttributeValue(element, "display:none");
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Filling portfolio details.
	public static boolean doFillPortfolioDetails(Map<String,String> mapAllocationDetails){
		try {
			if (mapAllocationDetails.get("Client Name") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapAllocationDetails.get("Client Name"), By.xpath("//div[@id='s2id_client']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Failed to select the Client Name : '"+mapAllocationDetails.get("Client Name")+"']\n";
					return false;
				}
				TimeUnit.SECONDS.sleep(2);
			}			
			if (mapAllocationDetails.get("Fund Family Name") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapAllocationDetails.get("Fund Family Name"), By.xpath("//div[@id='s2id_fundFamilyId']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Failed to select the Fund Family Name : '"+mapAllocationDetails.get("Fund Family Name")+"']\n";
					return false;
				}
			}

			bStatus = doClickOnAllocationNavigationbuttons("Next");
			if(!bStatus){
				Messages.errorMsg = " [ ERROR : Next Button Cannot be clicked in Choosse Portfolio Screen.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Verifying and Defining break periods.
	public static boolean doVerifyBreakPeriodDetailsTabAndDefineBreakPeriods(Map<String,String> mapAllocationDetails){
		try {
			if (mapAllocationDetails.get("Client Name") != null) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Client Name' in Break Period Screen ", By.id("clientName"), mapAllocationDetails.get("Client Name"), "No", false);
				if (!bStatus) {
					return false;
				}
			}
			if (mapAllocationDetails.get("Fund Family Name") != null) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Fund Family Name' in Break Period Screen ", By.id("fundFamilyName"), mapAllocationDetails.get("Fund Family Name"), "No", false);
				if (!bStatus) {
					return false;
				}
			}
			if (mapAllocationDetails.get("ExpectedLECount") != null) {
				int iNoOfLegalEntities = Elements.getXpathCount(Global.driver, By.xpath("//tbody//div[contains(@id,'uniform-chk')]"));
				if (iNoOfLegalEntities != Integer.parseInt(mapAllocationDetails.get("ExpectedLECount"))) {
					Messages.errorMsg = "[ ERROR : Expected Legal Entities count : '"+mapAllocationDetails.get("ExpectedLECount")+"' , is not matching with actual : '"+iNoOfLegalEntities+"']\n";
					return false;
				}
			}
			if (mapAllocationDetails.get("Legal Entities To Process") != null) {
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//input[@id='chkAll']//parent::span[@class='checked']"), 1000);
				if (mapAllocationDetails.get("Legal Entities To Process").equalsIgnoreCase("All")) {
					if (!bStatus) {
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='chkAll']//parent::span"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to click on check box to select 'All' Legal Entities .]\n";
							return false;
						}
					}
					bStatus = doFillBreakPeriodDetailsForAll(mapAllocationDetails);
					if(!bStatus){
						return false;
					}
				}
				if (!mapAllocationDetails.get("Legal Entities To Process").equalsIgnoreCase("All")){
					List<String> sLEList = Arrays.asList(mapAllocationDetails.get("Legal Entities To Process").split(","));
					int iLECount = sLEList.size();
					if (bStatus) {
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='chkAll']//parent::span[@class='checked']"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to click on check box to Un-select 'All' Legal Entities .]\n";
							return false;
						}
					}
					for (int i = 0; i < iLECount; i++) {
						//Select Particular Legal Entity Check box depending on Legal Entity Name
						bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='"+sLEList.get(i)+"']//parent::td//preceding-sibling::td//span//input"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to click on check box to select and process for Legal Entity : '"+sLEList.get(i)+"' .]\n";
							return false;
						}
					}
					bStatus = doFillBreakPeriodDetailsForSlectedLegalEntities(sLEList,mapAllocationDetails);
					if(!bStatus){
						return false;
					}
				}
			}
			if(mapAllocationDetails.get("isDeleteBreakPeriod") != null && mapAllocationDetails.get("isDeleteBreakPeriod").equalsIgnoreCase("Yes")){
				return true;
			}
			bStatus = doClickOnAllocationNavigationbuttons("Next");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Next Button Cannot be clicked on Break Period Screen.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Filling Break period details for selected LE's.
	private static boolean doFillBreakPeriodDetailsForSlectedLegalEntities(List<String> sLEList, Map<String, String> mapAllocationDetails) {
		try {		
			if(mapAllocationDetails.get("NAV Type") != null){
				bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()=\""+sLEList.get(0)+"\"]//parent::td//following-sibling::td//label[contains(normalize-space(),'"+mapAllocationDetails.get("NAV Type")+"')]//div[contains(@id,'uniform-NavType')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : NAV Type : '"+mapAllocationDetails.get("NAV Type")+"' ,Radio button Not Selected for Legal Entity : '"+sLEList.get(0)+"']\n";
					return false;
				}					
			}
			if(mapAllocationDetails.get("BreakPeriodNewOrExisting") != null){
				bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()=\""+sLEList.get(0)+"\"]//parent::td//following-sibling::td//label[contains(normalize-space(),'"+mapAllocationDetails.get("BreakPeriodNewOrExisting")+"')]//div[contains(@id,'uniform-brkPeriod')]"));
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : BreakPeriod New Or Existing : "+mapAllocationDetails.get("BreakPeriodNewOrExisting")+" ,Radio Button cannot be Selected in Break period screen.]\n";
					return false;
				}
				if(mapAllocationDetails.get("BreakPeriodNewOrExisting").equalsIgnoreCase("New")){
					if(mapAllocationDetails.get("Break Period Date") != null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space()=\""+sLEList.get(0)+"\"]//parent::td//following-sibling::td//div//input[contains(@id,'brkPeriodDate')]"), mapAllocationDetails.get("Break Period Date"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Break Period New Date : '"+mapAllocationDetails.get("Break Period Date")+"' ,Cannot be Entered.]\n";
							return false;
						}
						Elements.click(Global.driver, By.xpath("//th[contains(text(),'Break Period Date')]"));
					}							
				}
				if(mapAllocationDetails.get("BreakPeriodNewOrExisting").equalsIgnoreCase("Existing")){						
					if(mapAllocationDetails.get("Break Period Date") != null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapAllocationDetails.get("Break Period Date"), By.xpath("//label[normalize-space()=\""+sLEList.get(0)+"\"]//parent::td//following-sibling::td//div[contains(@id,'s2id_brkPeriodDateOpt')]//span[contains(@id,'select2-chosen')]"));
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Break Period : '"+mapAllocationDetails.get("Break Period Date")+"' ,for Existing Date cannot be selected from Dropdown.]\n";
							return false;
						}
						Elements.click(Global.driver, By.xpath("//th[contains(text(),'Break Period Date')]"));
						if(mapAllocationDetails.get("isDeleteBreakPeriod") != null && mapAllocationDetails.get("isDeleteBreakPeriod").equalsIgnoreCase("Yes")){
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//td[normalize-space()=\""+sLEList.get(0)+"\"]//following-sibling::td//a[contains(@id,'deletePeriodBtn')]"));
							if(!bStatus){
								Messages.errorMsg = "Delete Break Period Button Cannot be clicked for the Legal Entity: "+sLEList.get(0);
								return false;
							}
							bStatus = verifyAndClickOnOKButtonForBreakPeriodDeletion(mapAllocationDetails.get("Break Period Date"));
							if(!bStatus){
								return false;
							}
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapAllocationDetails.get("Break Period Date"), By.xpath("//label[normalize-space()=\""+sLEList.get(0)+"\"]//parent::td//following-sibling::td//div[contains(@id,'s2id_brkPeriodDateOpt')]//span[contains(@id,'select2-chosen')]"));
							if(bStatus){
								Messages.errorMsg = "[ ERROR : Break Period : '"+mapAllocationDetails.get("Break Period Date")+"' ,is still available in Dropdown After Deleting.]\n";
								return false;
							}
							NewUICommonFunctions.refreshThePage();
						}						
					}							
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Filling Break period details for All LE's.
	private static boolean doFillBreakPeriodDetailsForAll(Map<String, String> mapAllocationDetails) {
		try {
			if(mapAllocationDetails.get("NAV Type") != null){
				bStatus = Elements.click(Global.driver, By.xpath("//label[contains(normalize-space(),'"+mapAllocationDetails.get("NAV Type")+"')]//div[contains(@id,'uniform-NavType')]/span"));
				if(!bStatus){
					Messages.errorMsg = ""+mapAllocationDetails.get("NAV Type")+" Radio button Not Selected";
					return false;
				}					
			}
			if(mapAllocationDetails.get("BreakPeriodNewOrExisting") != null){
				bStatus = Elements.click(Global.driver, By.xpath("//label[contains(normalize-space(),'"+mapAllocationDetails.get("BreakPeriodNewOrExisting")+"')]//div[contains(@id,'uniform-brkPeriod')]/span"));
				if(!bStatus){
					Messages.errorMsg = mapAllocationDetails.get("BreakPeriodNewOrExisting")+" Radio Button cannot be Selected in Break period screen";
					return false;
				}
				if(mapAllocationDetails.get("BreakPeriodNewOrExisting").equalsIgnoreCase("New")){
					if(mapAllocationDetails.get("Break Period Date") != null){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='brkPeriodDate_0']"), mapAllocationDetails.get("Break Period Date"));
						if(!bStatus){
							Messages.errorMsg = "Break Period New Date Cannot be Entered";
							return false;
						}
						Elements.click(Global.driver, By.xpath("//th[contains(text(),'Break Period Date')]"));
					}							
				}
				if(mapAllocationDetails.get("BreakPeriodNewOrExisting").equalsIgnoreCase("Existing")){						
					if(mapAllocationDetails.get("Break Period Date") != null){
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapAllocationDetails.get("Break Period Date"), By.xpath("//div[@id='s2id_brkPeriodDateOpt_0']//span[contains(@id,'select2-chosen')]"));
						if(!bStatus){
							Messages.errorMsg = "Break Period Existing Dropdown cannot be selected";
							return false;
						}
						Elements.click(Global.driver, By.xpath("//th[contains(text(),'Break Period Date')]"));

						if(mapAllocationDetails.get("isDeleteBreakPeriod") != null && mapAllocationDetails.get("isDeleteBreakPeriod").equalsIgnoreCase("Yes")){
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//a[@id='deletePeriodBtn_0']"));
							if(!bStatus){
								Messages.errorMsg = "Cannot click on Delete button for the First Row Fund";
								return false;
							}							
							bStatus = verifyAndClickOnOKButtonForBreakPeriodDeletion(mapAllocationDetails.get("Break Period Date"));
							if(!bStatus){
								return false;
							}
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapAllocationDetails.get("Break Period Date"), By.xpath("//div[@id='s2id_brkPeriodDateOpt_0']//span[contains(@id,'select2-chosen')]"));
							if(bStatus){
								Messages.errorMsg = "[ ERROR : Break Period : '"+mapAllocationDetails.get("Break Period Date")+"' ,is still available in Existing NAv Date Dropdown After Deleting.]\n";
								return false;
							}
							NewUICommonFunctions.refreshThePage();
						}
					}							
				}
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean verifyAndClickOnOKButtonForBreakPeriodDeletion(String breakPeriodDate) {
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(normalize-space(),'Are you sure you want to delete break beriod for selected entites?')]//following-sibling::div//button[normalize-space()='OK']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Alert Message is not visible after click on the Delete Button for the Selected Break Period: "+breakPeriodDate;
				return false;
			}
			bStatus =Elements.click(Global.driver, By.xpath("//div[contains(normalize-space(),'Are you sure you want to delete break beriod for selected entites?')]//following-sibling::div//button[normalize-space()='OK']") );
			if(!bStatus){
				Messages.errorMsg = "Cannot click on the OK button of Delete Break Period Alert Message for the Break Period:"+breakPeriodDate;
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']//span[contains(normalize-space(),'BreakPeriod Deleted successfully')]"),Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Break Period Deleted Successfully Message is not visible";
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//To perform operations on Allocation wizard navigation buttons.
	public static boolean doClickOnAllocationNavigationbuttons(String sNavigationButtonName){
		try {
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//i[@class='fa fa-home']"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Scroll to the top of screen.]\n";
				return false;
			}
			bStatus = NewUICommonFunctions.jsClick(By.xpath("//button[normalize-space()='"+sNavigationButtonName+"']"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to click on the Allocation navigation button : '"+sNavigationButtonName+"'.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Setting up attribute value for LE row to Upload TB.
	public static void setAttributeValue(WebElement elem, String value){
		JavascriptExecutor js;
		js = (JavascriptExecutor) Global.driver; 

		String scriptSetAttrValue = "arguments[0].setAttribute(arguments[1],arguments[2])";

		js.executeScript(scriptSetAttrValue, elem, "value", value);

	}

	//Processing Special Allocation screen.
	public static boolean doVerifySpecialAllocationScreenDetails(Map<String,String> mapAllocationDetails){
		boolean bValidateStatus = true;
		String sAppenderrorMsg = "";
		String sValue = "";
		try {
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'Special Allocation')]"));
			bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Client Name' in Special Allocation screen, ", By.xpath("//input[@id='clientName']//parent::label"), mapAllocationDetails.get("Client Name"), "Yes", false);
			if (!bStatus) {
				sAppenderrorMsg = sAppenderrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Fund Family Name' in Special Allocation screen, ", By.xpath("//input[@id='fundFamilyName']//parent::label"), mapAllocationDetails.get("Fund Family Name"), "Yes", false);
			if (!bStatus) {
				sAppenderrorMsg = sAppenderrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			String sFormattedDate = "";
			sValue = Elements.getText(Global.driver, By.xpath("//label[contains(normalize-space(),'Break Period')]//parent::p//label[2]"));
			if (sValue != null && !sValue.equalsIgnoreCase("")) {
				sFormattedDate = TradeTypeSubscriptionAppFunctions.formatDate(sValue);
				if (!sFormattedDate.equalsIgnoreCase(mapAllocationDetails.get("Break Period Date"))) {
					sAppenderrorMsg = sAppenderrorMsg + "[ ERROR : In Special Allocation Screen Actual Break Period date : '"+sFormattedDate+"' ,is not matching with Expected : '"+mapAllocationDetails.get("Break Period Date")+"' .]\n";
					bValidateStatus = false;
				}
			}
			else {
				sAppenderrorMsg = sAppenderrorMsg + "[ ERROR : In Special Allocation Screen Actual Break Period date : '"+sValue+"' ,is not matching with Expected : '"+mapAllocationDetails.get("Break Period Date")+"' .]\n";
				bValidateStatus = false;
			}
			/*bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Break Period' in Special Allocation screen, ", By.xpath("//label[contains(normalize-space(),'Break Period')]//parent::p//label[2]"), mapAllocationDetails.get("Break Period Date"), "Yes", false);
			if (!bStatus) {
				sAppenderrorMsg = sAppenderrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}*/
			bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'NAV Type' in Special Allocation screen, ", By.xpath("//label[contains(normalize-space(),'NAV Type')]//following-sibling::label"), mapAllocationDetails.get("NAV Type"), "Yes", false);
			if (!bStatus) {
				sAppenderrorMsg = sAppenderrorMsg + Messages.errorMsg;
				bValidateStatus = false;
			}
			Messages.errorMsg = sAppenderrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}		
	}

	//Add New Special Allocations.
	public static boolean doAddNewSpecialAllocations(Map<String,String> mapAllocationDetails){
		List<String> sNewSpecialAllocationLEList = new ArrayList<String>();
		List<String> sNewPLHeadLists = new ArrayList<String>();
		List<String> sNewStrategyLists = new ArrayList<String>();
		List<String> sNewCustodianAccountLists = new ArrayList<String>();
		List<String> sNewAllocationTypeLists = new ArrayList<String>();
		List<String> sNewIsCombinationUsedLists = new ArrayList<String>();
		try {
			if (mapAllocationDetails.get("LEsToAddNewSA") != null) {
				sNewSpecialAllocationLEList = Arrays.asList(mapAllocationDetails.get("LEsToAddNewSA").split(":"));
				if (mapAllocationDetails.get("NewPLHead") != null && !mapAllocationDetails.get("NewPLHead").equalsIgnoreCase("")) {
					sNewPLHeadLists = Arrays.asList(mapAllocationDetails.get("NewPLHead").split(":"));
				}
				if (mapAllocationDetails.get("NewStrategy") != null && !mapAllocationDetails.get("NewStrategy").equalsIgnoreCase("")) {
					sNewStrategyLists = Arrays.asList(mapAllocationDetails.get("NewStrategy").split(":"));
				}
				if (mapAllocationDetails.get("NewCustodianAccount") != null && !mapAllocationDetails.get("NewCustodianAccount").equalsIgnoreCase("")) {
					sNewCustodianAccountLists = Arrays.asList(mapAllocationDetails.get("NewCustodianAccount").split(":"));
				}
				if (mapAllocationDetails.get("AllocationType") != null && !mapAllocationDetails.get("AllocationType").equalsIgnoreCase("")) {
					sNewAllocationTypeLists = Arrays.asList(mapAllocationDetails.get("AllocationType").split(":"));
				}
				if (mapAllocationDetails.get("IsAllocationProcessedForTheCombination") != null && !mapAllocationDetails.get("IsAllocationProcessedForTheCombination").equalsIgnoreCase("")) {
					sNewIsCombinationUsedLists = Arrays.asList(mapAllocationDetails.get("IsAllocationProcessedForTheCombination").split(":"));
				}
				for (int indexOfLE = 0; indexOfLE < sNewSpecialAllocationLEList.size(); indexOfLE++) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(sNewSpecialAllocationLEList.get(indexOfLE).trim(), By.xpath("//div[@id='s2id_legalEntityOpt']//span[contains(@id,'select2-chosen')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to select the Legal Entity : '"+sNewSpecialAllocationLEList.get(indexOfLE).trim()+"' ,from the drop down of special allocation screen. ]\n";
						return false;
					}
					if (sNewPLHeadLists.size() != sNewSpecialAllocationLEList.size()) {
						Messages.errorMsg = "[ ERROR : Test data wasn't given properly please cross check, No Of LE's : '"+sNewSpecialAllocationLEList.size()+"', are not matching with the given PLHeads : '"+sNewPLHeadLists.size()+"' values for each record.]\n";
						return false;	
					}
					List<String> sRespectiveLENewPLHeadList = Arrays.asList(sNewPLHeadLists.get(indexOfLE).split("\\*"));
					List<String> sRespectiveLENewStrategyList = Arrays.asList(sNewStrategyLists.get(indexOfLE).split("\\*"));
					List<String> sRespectiveLENewCustodianAccList = Arrays.asList(sNewCustodianAccountLists.get(indexOfLE).split("\\*"));
					List<String> sRespectiveLENewAllocationTypeList = Arrays.asList(sNewAllocationTypeLists.get(indexOfLE).split("\\*"));						
					List<String> sRespectiveLEIsNewCombinationUsedList = Arrays.asList(sNewIsCombinationUsedLists.get(indexOfLE).split("\\*"));
					for (int indexOfHeaders = 0; indexOfHeaders < sRespectiveLENewPLHeadList.size(); indexOfHeaders++) {						

						bStatus = doSelectTheHeadersCombinationAndGetInvestorsValues(sRespectiveLENewPLHeadList.get(indexOfHeaders), sRespectiveLENewStrategyList.get(indexOfHeaders), sRespectiveLENewCustodianAccList.get(indexOfHeaders), sRespectiveLENewAllocationTypeList.get(indexOfHeaders), sRespectiveLEIsNewCombinationUsedList.get(indexOfHeaders), sNewSpecialAllocationLEList.get(indexOfLE).trim());
						if (!bStatus) {
							return false;
						}				

						if (mapAllocationDetails.get("NewSAInvestors") != null && mapAllocationDetails.get("NewSAUnevenAmounts") != null && mapAllocationDetails.get("NewSACurrentBalance") != null && mapAllocationDetails.get("NewNoOfDecimalsToDisplay") != null && mapAllocationDetails.get("NewSAClassForInvestor") != null && mapAllocationDetails.get("NewSASeriesForInvestor") != null) {
							//Splitting the Details With respect to LE. 
							List<String> sInvestorsListsOfLE = Arrays.asList(mapAllocationDetails.get("NewSAInvestors").split(":"));
							List<String> sInvCurrentBalanceListOfLE = Arrays.asList(mapAllocationDetails.get("NewSACurrentBalance").split(":"));
							List<String> sAmountsListsOfLE = Arrays.asList(mapAllocationDetails.get("NewSAUnevenAmounts").split(":"));
							List<String> sNoOfDecimalsToDisplayOfLE = Arrays.asList(mapAllocationDetails.get("NewNoOfDecimalsToDisplay").split(":"));
							List<String> sClassListsOfLE = Arrays.asList(mapAllocationDetails.get("NewSAClassForInvestor").split(":"));
							List<String> sSeriesListsOfLE = Arrays.asList(mapAllocationDetails.get("NewSASeriesForInvestor").split(":"));

							//Splitting the Details With respect to LE and Headers.
							List<String> sInvestorsListOfNewSARecord = Arrays.asList(sInvestorsListsOfLE.get(indexOfLE).split("\\*"));
							List<String> sCurrentBalListOfNewSARecord = Arrays.asList(sInvCurrentBalanceListOfLE.get(indexOfLE).split("\\*"));
							List<String> sAmountsListOfNewSARecord = Arrays.asList(sAmountsListsOfLE.get(indexOfLE).split("\\*"));
							List<String> sNoOfDecListOfNewSARecord = Arrays.asList(sNoOfDecimalsToDisplayOfLE.get(indexOfLE).split("\\*"));
							List<String> sClassNewSARecord = Arrays.asList(sClassListsOfLE.get(indexOfLE).split("\\*"));
							List<String> sSeriesNewSARecord = Arrays.asList(sSeriesListsOfLE.get(indexOfLE).split("\\*"));

							//Splitting the Investors and other details With respect to LE.
							List<String> sInvestorsSubList = Arrays.asList(sInvestorsListOfNewSARecord.get(indexOfHeaders).split(";"));
							List<String> sCurrentBalSubList = Arrays.asList(sCurrentBalListOfNewSARecord.get(indexOfHeaders).split(";"));
							List<String> sAmountsSubList = Arrays.asList(sAmountsListOfNewSARecord.get(indexOfHeaders).split(";"));
							List<String> sNoOfDecSubList = Arrays.asList(sNoOfDecListOfNewSARecord.get(indexOfHeaders).split(";"));
							List<String> sClassSubList = Arrays.asList(sClassNewSARecord.get(indexOfHeaders).split(";"));
							List<String> sSeriesSubList = Arrays.asList(sSeriesNewSARecord.get(indexOfHeaders).split(";"));

							if (sAllocationType.equalsIgnoreCase("Auto")) {
								bStatus = doSelectRecordsOfTheInvestorsAndProcessSpecialAllocationForAutoMode(sInvestorsSubList, sAmountsSubList, sCurrentBalSubList, sNoOfDecSubList, sNewSpecialAllocationLEList.get(indexOfLE).trim(), sInvestorsSubList.size(),sClassSubList,sSeriesSubList);
								if (!bStatus) {
									return false;
									/*sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
									bValidateStatus = false;*/
								}
							}
							if (sAllocationType.equalsIgnoreCase("Manual")) {
								bStatus = doSelectRecordsOfTheInvestorsAndProcessSpecialAllocationForManualMode(sInvestorsSubList, sAmountsSubList, sCurrentBalSubList, sNoOfDecSubList, sNewSpecialAllocationLEList.get(indexOfLE).trim(), sInvestorsSubList.size(),sClassSubList,sSeriesSubList);
								if (!bStatus) {
									return false;
									/*sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
									bValidateStatus = false;*/
								}							
							}
						}
						bStatus = doVerifyAndBringHeadersDropdownsIntoVisibility();
						if (!bStatus) {
							/*Messages.errorMsg = Messages.errorMsg + sAppendErrorMsg;*/
							return false;
						}
					}					
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//verify the headers if they are visible else click on add new button then proceed.
	public static boolean doVerifyAndBringHeadersDropdownsIntoVisibility(){
		try {
			bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[contains(@id,'s2id_pnlHead')]"));
			boolean bStatus1 = Verify.verifyElementVisible(Global.driver, By.xpath("//button[@id='newAllocationBtn']"));
			if (bStatus == true && bStatus1 == true) {
				Messages.errorMsg = "[ ERROR : Headers dropdowns and Add New button both are visible at a time in Special Allocation Screen.]\n";
				return false;
			}
			if (bStatus == true && bStatus1 == false) {
				return true;
			}
			if (bStatus == false && bStatus1 == true) {
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[@id='newAllocationBtn']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to click on Add New button to add a new Special Allocation.]\n";
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

	//Select And Process Manual Special Allocation for selective records.
	public static boolean doSelectRecordsOfTheInvestorsAndProcessSpecialAllocationForManualMode(List<String> sInvestorsList, List<String> sAmountsList, List<String> sCurrentBalList, List<String> sNoOfDecimalsList, String sFundName, int noOfInvestors, List<String> sClassSubList, List<String> sSeriesSubList){
		try {
			for (int i = 0; i < noOfInvestors; i++) {

				//Filter the Grid with the InvestorName and Current Balance
				bStatus = doFilterInvestorGridWithInvestorNameAndCurrentBalance(sInvestorsList.get(i),sCurrentBalList.get(i),sFundName,"Manual",sClassSubList.get(i),sSeriesSubList.get(i));
				if(!bStatus){
					return false;
				}

				String checkBoxocator = "//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridManual')][1]//div[contains(@id,'contenttable')]//div[@role='row']//div[@role='gridcell']//div[normalize-space()=\""+sInvestorsList.get(i)+"\"]//..//parent::div//div[contains(@class,'checkbox-')]//div";
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(checkBoxocator), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Investor : '"+sInvestorsList.get(i).trim()+"' is Not visible for the Fund : '"+sFundName+"'.]\n";
					return false;
				}
				bStatus = Elements.click(Global.driver, By.xpath(checkBoxocator));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Identify and Select the record for the Investor : '"+sInvestorsList.get(i).trim()+"' and for Fund : '"+sFundName+"'.]\n";
					return false;
				}
				//verify Current Balance and No Of Decimal to display.
				if(!sCurrentBalList.get(i).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Current Balance for the Investor : '"+sInvestorsList.get(i).trim()+"' and Fund : '"+sFundName+"' ,", By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridManual')][1]//div[contains(@id,'contenttable')]//div[@role='row']//div[@role='gridcell']//div[normalize-space()=\""+sInvestorsList.get(i).trim()+"\"]//..//following-sibling::div[1]//div"), sCurrentBalList.get(i), "Yes", true, Integer.parseInt(sNoOfDecimalsList.get(i).trim()));
					if (!bStatus) {
						return false;
					}
				}
				//verifying the UnevenAmount and No Of Decimal to display.
				if(!sAmountsList.get(i).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridManual')][1]//div[contains(@id,'contenttable')]//div[@role='row']//div[@role='gridcell']//div[normalize-space()=\""+sInvestorsList.get(i)+"\"]//..//following-sibling::div[2]//input"), sAmountsList.get(i));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to Input Uneven Amount : '"+sAmountsList.get(i).trim()+"' ,on Manual Mode for Fund : '"+sFundName+"' and Investor : '"+sInvestorsList.get(i)+"' .]\n";
						return false;
					}
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//div//div[contains(normalize-space(),'Drag a column')]"));
			}
			//Elements.clearText(Global.driver, By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridManual')][1]//div[contains(@id,'filterrow')]//div[contains(@class,'jqx-grid-cell')][3]//input[@type='textarea']"));
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@id,'saveBtn') and contains(@onclick,'saveManualAllocation')]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Save Special Allocation for details opted for 'Manual' Mode and Fund : '"+sFundName+"' , Investors : "+sInvestorsList+" and Amounts : '"+sAmountsList+"' .]\n";
				return false;
			}
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@class,'alert-success') and contains(normalize-space(),'Special Allocation Completed')]"), Constants.lTimeOut);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Manual Special Allocation Successful message wasn't displayed for the combination of Fund : '"+sFundName+"', Investors : "+sInvestorsList+" and Amounts : '"+sAmountsList+"' ,when saved the details.]\n";
				return false;
			}
			bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridManual')][1]//div[contains(@id,'filterrow')]//div[contains(@class,'jqx-grid-cell')][3]//input[@type='textarea']"));
			if (bStatus) {
				Messages.errorMsg = "[ ERROR : After saving Special Allocation Manually for the combination of Fund : '"+sFundName+"', Investors : "+sInvestorsList+" and Amounts : '"+sAmountsList+"' ,the Grid did NOT went INVISIBLE.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Select And Process Auto Special Allocation for selective records.
	public static boolean doSelectRecordsOfTheInvestorsAndProcessSpecialAllocationForAutoMode(List<String> sInvestorsList, List<String> sAmountsList, List<String> sCurrentBalList, List<String> sNoOfDecimalsList, String sFundName, int noOfInvestors, List<String> sClassSubList, List<String> sSeriesSubList){
		try {
			for (int inedexOfInv = 0; inedexOfInv < noOfInvestors; inedexOfInv++) {
				//Filter the Grid with the InvestorName and Current Balance
				bStatus = doFilterInvestorGridWithInvestorNameAndCurrentBalance(sInvestorsList.get(inedexOfInv),sCurrentBalList.get(inedexOfInv),sFundName,"Auto",sClassSubList.get(inedexOfInv),sSeriesSubList.get(inedexOfInv));
				if(!bStatus){
					return false;
				}
				String checkBoxLocator = "//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridAuto')][1]//div[contains(@id,'contenttable')]//div[@role='row']//div[@role='gridcell']//div[normalize-space()=\""+sInvestorsList.get(inedexOfInv).trim()+"\"]//..//parent::div//div[contains(@class,'checkbox-')]//div";
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(checkBoxLocator), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Investor : '"+sInvestorsList.get(inedexOfInv)+"' is not visible in Grid for Fund : '"+sFundName+"'.]\n";
					return false;
				}

				bStatus = Elements.click(Global.driver, By.xpath(checkBoxLocator));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Identify and Mark the record for the Investor : '"+sInvestorsList.get(inedexOfInv)+"' in Grid for Fund : '"+sFundName+"'.]\n";
					return false;
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//div//div[contains(normalize-space(),'Drag a column')]"));
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'computeAllocation') and contains(normalize-space(),'Compute')]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to click on Compute button in Auto Mode on Special Allocation Screen for Fund : '"+sFundName+"'.]\n";
				return false;
			}
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
			for (int inedexOfInv = 0; inedexOfInv < noOfInvestors; inedexOfInv++) {
				//Filter the Grid with the Investor Name and Current Balance
				bStatus = doFilterInvestorGridWithInvestorNameAndCurrentBalance(sInvestorsList.get(inedexOfInv),sCurrentBalList.get(inedexOfInv),sFundName,"Auto",sClassSubList.get(inedexOfInv),sSeriesSubList.get(inedexOfInv));
				if(!bStatus){
					return false;
				}	

				String checkBoxLocator = "//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridAuto')][1]//div[contains(@id,'contenttable')]//div[@role='row']//div[@role='gridcell']//div[normalize-space()=\""+sInvestorsList.get(inedexOfInv)+"\"]//..//parent::div//div[contains(@class,'checkbox-')]//div";
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(checkBoxLocator), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Investor : '"+sInvestorsList.get(inedexOfInv)+"' is not visible in Grid for Fund : '"+sFundName+"'.]\n";
					return false;
				}
				//verify Current Balance and No Of Decimal to display.
				if(!sCurrentBalList.get(inedexOfInv).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Current Balance for the Investor : '"+sInvestorsList.get(inedexOfInv).trim()+"' and Fund : '"+sFundName+"' ,", By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridAuto')][1]//div[contains(@id,'contenttable')]//div[@role='row']//div[@role='gridcell']//div[normalize-space()=\""+sInvestorsList.get(inedexOfInv).trim()+"\"]//..//following-sibling::div[1]//div"), sCurrentBalList.get(inedexOfInv).trim(), "Yes", true, Integer.parseInt(sNoOfDecimalsList.get(inedexOfInv).trim()));
					if (!bStatus) {
						return false;
					}
				}
				//verifying the UnevenAmount and No Of Decimal to display.
				if(!sAmountsList.get(inedexOfInv).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.verifyTextIntextBoxOrDropDownAndVerifyDecimalsToDisplay("Uneven Amount for the Investor : '"+sInvestorsList.get(inedexOfInv).trim()+"' and Fund : '"+sFundName+"' ,", By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridAuto')][1]//div[contains(@id,'contenttable')]//div[@role='row']//div[@role='gridcell']//div[normalize-space()=\""+sInvestorsList.get(inedexOfInv).trim()+"\"]//..//following-sibling::div[2]//div"), sAmountsList.get(inedexOfInv).trim(), "Yes", true, Integer.parseInt(sNoOfDecimalsList.get(inedexOfInv).trim()));
					if (!bStatus) {
						return false;
					}
				}
			}
			//Elements.clearText(Global.driver, By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridAuto')][1]//div[contains(@id,'filterrow')]//div[contains(@class,'jqx-grid-cell')][3]//input[@type='textarea']"));
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@id,'saveBtn') and contains(@onclick,'computeAllocation')]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Save Special Allocation for details opted for 'Auto' Mode and Fund : '"+sFundName+"' , Investors : "+sInvestorsList+" and Amounts : '"+sAmountsList+"' .]\n";
				return false;
			}
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@class,'alert-success') and contains(normalize-space(),'Special Allocation Completed')]"), Constants.lTimeOut);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Auto Special Allocation Successful message wasn't displayed for the combination of Fund : '"+sFundName+"', Investors : "+sInvestorsList+" and Amounts : '"+sAmountsList+"' ,when saved the details.]\n";
				return false;
			}
			bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGridAuto')][1]//div[contains(@id,'filterrow')]//div[contains(@class,'jqx-grid-cell')][3]//input[@type='textarea']"));
			if (bStatus) {
				Messages.errorMsg = "[ ERROR : After saving Auto Special Allocation for the combination of Fund : '"+sFundName+"' and Investors : "+sInvestorsList+" and Amounts : '"+sAmountsList+"' ,the Grid did NOT went INVISIBLE.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doFilterInvestorGridWithInvestorNameAndCurrentBalance(String sInvestorName, String sCurrentBalance,String sFundName,String mode, String sClassName, String sSeriesName) {
		try {
			String gridLocator = "//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGrid"+mode+"')][1]//div[contains(@id,'filterrow')]";
			if(!sInvestorName.equalsIgnoreCase("None")){
				bStatus = Elements.enterText(Global.driver, By.xpath(gridLocator+"//div[contains(@class,'jqx-grid-cell')][5]//input[@type='textarea']"), sInvestorName);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Enter Investor Name : '"+sInvestorName+"' ,into Investor search filter in the Grid for Fund : '"+sFundName+"'.]\n";
					return false;
				}
			}

			if(!sClassName.equalsIgnoreCase("None")){
				bStatus = Elements.enterText(Global.driver, By.xpath(gridLocator+"//div[contains(@class,'jqx-grid-cell')][3]//input[@type='textarea']"), sClassName);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Enter Class Name : '"+sClassName+"' ,into Class search filter in the Grid for Fund : '"+sFundName+"'.]\n";
					return false;
				}
			}

			if(!sSeriesName.equalsIgnoreCase("None")){
				bStatus = Elements.enterText(Global.driver, By.xpath(gridLocator+"//div[contains(@class,'jqx-grid-cell')][4]//input[@type='textarea']"), sSeriesName);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Enter Series Name Name : '"+sSeriesName+"' ,into Series Name search filter in the Grid for Fund : '"+sFundName+"'.]\n";
					return false;
				}
			}
			if(!sCurrentBalance.equalsIgnoreCase("None")){
				bStatus = Elements.enterText(Global.driver, By.xpath(gridLocator+"//div[contains(@class,'jqx-grid-cell')][6]//input[@type='textarea']"), sCurrentBalance);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Enter Current Balance Amount : '"+sCurrentBalance+"' ,into Current Balance search filter in the Grid for Fund : '"+sFundName+"'.]\n";
					return false;
				}
			}

			Thread.sleep(1500);

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;			
		}
	}

	//Selecting the Headers Combination and fetching the investors of entity tree.
	public static boolean doSelectTheHeadersCombinationAndGetInvestorsValues(String sNewPLHeadValue, String sNewStrategy, String sNewCustodianAccount, String sNewAllocationType, String isTheHeadersCombinationWasAlreadyUsed, String sFundName){
		try {
			if (sNewPLHeadValue != null && !sNewPLHeadValue.equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(sNewPLHeadValue, By.xpath("//div[contains(@id,'s2id_pnlHead')]//span[contains(@class,'select2-chosen')]"));
				if (!bStatus) {
					return false;
				}
			}
			if (sNewStrategy != null && !sNewStrategy.equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(sNewStrategy, By.xpath("//div[contains(@id,'s2id_strategy')]//span[contains(@class,'select2-chosen')]"));
				if (!bStatus) {
					return false;
				}
			}
			if (sNewCustodianAccount != null && !sNewCustodianAccount.equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(sNewCustodianAccount, By.xpath("//div[contains(@id,'s2id_custodian')]//span[contains(@class,'select2-chosen')]"));
				if (!bStatus) {
					return false;
				}
			}
			if (sNewAllocationType != null && !sNewAllocationType.equalsIgnoreCase("None")) {				
				if (sNewAllocationType.equalsIgnoreCase("Auto")) {
					sAllocationType = "Auto";
				}
				if (sNewAllocationType.equalsIgnoreCase("Manual")) {
					sAllocationType = "Manual";
				}				
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'"+sAllocationType.toLowerCase()+"')]//span"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to select the Allocation Type : '"+sAllocationType.toUpperCase()+"' .]\n";
					return false;
				}
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@id,'btn_get_value')]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to click on 'Get Value' button to fetch the investors of Entity Tree.]\n";
				return false;
			}
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
			if (isTheHeadersCombinationWasAlreadyUsed.equalsIgnoreCase("Yes")) {
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@class,'info') and contains(text(),'already processed')]"), Constants.lTimeOut);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Message wasn't displayed for the used combination of heads when trying to add a new Special Allocation with : '"+sNewPLHeadValue+"', '"+sNewStrategy+"', '"+sNewCustodianAccount+"' of Fund : '"+sFundName+"' .]\n";
					return false;
				}
				TradeTypeSubscriptionAppFunctions.goToCurrentUrl();
				NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
			}
			if (isTheHeadersCombinationWasAlreadyUsed.equalsIgnoreCase("No")) {
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@id,'groupsheadertreeGrid')]//following-sibling::div[contains(@id,'treeGrid"+sAllocationType+"')][1]"), Constants.lTimeOut);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : For Allocation Type : '"+sAllocationType+"' Grid wasn't displayed for the headers combination : '"+sNewPLHeadValue+"', '"+sNewStrategy+"', '"+sNewCustodianAccount+"' of Fund : '"+sFundName+"' .]";
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Expand Or Collapse Existing Allocation Details.
	public static boolean doExpandOrCollapseExistingAllocationDetailsView(String sExpandOrCollapse){
		try {
			boolean bStatus1 = Wait.waitForElementVisibility(Global.driver, By.xpath("//a[contains(@id,'extTableCollapseLink') and @class='expand']"), 1);
			boolean bStatus2 = Wait.waitForElementVisibility(Global.driver, By.xpath("//a[contains(@id,'extTableCollapseLink') and @class='collapse']"), 1);
			if (sExpandOrCollapse != null && sExpandOrCollapse.equalsIgnoreCase("expand") && bStatus1) {
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//a[contains(@id,'extTableCollapseLink') and @class='expand']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to 'Expand' the 'Existing Allocation Details' View.]\n";
					return false;
				}
			}
			else if (sExpandOrCollapse != null && sExpandOrCollapse.equalsIgnoreCase("collapse") && bStatus2) {
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//a[contains(@id,'extTableCollapseLink') and @class='collapse']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to 'Collapse' the 'Existing Allocation Details' View.]\n";
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Clear the opened grid view of Existing Allocation Details.
	/*public static boolean doClearOrAddSpecialAllocationDetails(boolean isAdd_TrueOrClear_False){
		try {
			if (isAdd_TrueOrClear_False) {
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.id("newAllocationBtn"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to click on 'Add New' Special Allocation button.]\n";
					return false;
				}
			}
			else {
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.id("cancelAllocateBtn"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to click on 'Add New' Special Allocation button.]\n";
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}*/

	//Verify Existing Allocation Details.
	public static boolean doVerifyExistingAllocationDetails(Map<String,String> mapAllocationDetails){
		List<String> sSpeciallyAllocatedLEList = new ArrayList<String>();
		List<String> sPLHeadLists = new ArrayList<String>();
		List<String> sStrategyLists = new ArrayList<String>();
		List<String> sCustodianAccountLists = new ArrayList<String>();
		List<String> sModeLists = new ArrayList<String>();
		List<String> sAllocatedLists = new ArrayList<String>();
		List<String> sActionLists = new ArrayList<String>();
		try {
			if (mapAllocationDetails.get("LEListForSpecialAllocationVerify") != null) {
				sSpeciallyAllocatedLEList = Arrays.asList(mapAllocationDetails.get("LEListForSpecialAllocationVerify").split(":"));
				if (mapAllocationDetails.get("PLHead") != null && !mapAllocationDetails.get("PLHead").equalsIgnoreCase("")) {
					sPLHeadLists = Arrays.asList(mapAllocationDetails.get("PLHead").split(":"));
				}
				if (mapAllocationDetails.get("Strategy") != null && !mapAllocationDetails.get("Strategy").equalsIgnoreCase("")) {
					sStrategyLists = Arrays.asList(mapAllocationDetails.get("Strategy").split(":"));
				}
				if (mapAllocationDetails.get("CustodianAccount") != null && !mapAllocationDetails.get("CustodianAccount").equalsIgnoreCase("")) {
					sCustodianAccountLists = Arrays.asList(mapAllocationDetails.get("CustodianAccount").split(":"));
				}
				if (mapAllocationDetails.get("Mode") != null && !mapAllocationDetails.get("Mode").equalsIgnoreCase("")) {
					sModeLists = Arrays.asList(mapAllocationDetails.get("Mode").split(":"));
				}
				if (mapAllocationDetails.get("Allocated") != null && !mapAllocationDetails.get("Allocated").equalsIgnoreCase("")) {
					sAllocatedLists = Arrays.asList(mapAllocationDetails.get("Allocated").split(":"));
				}
				if (mapAllocationDetails.get("Actions") != null && !mapAllocationDetails.get("Actions").equalsIgnoreCase("")) {
					sActionLists = Arrays.asList(mapAllocationDetails.get("Actions").split(":"));
				}
				for (int indexOfLE = 0; indexOfLE < sSpeciallyAllocatedLEList.size(); indexOfLE++) {
					bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(sSpeciallyAllocatedLEList.get(indexOfLE).trim(), By.xpath("//div[@id='s2id_legalEntityOpt']//span[contains(@id,'select2-chosen')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to select the Legal Entity : '"+sSpeciallyAllocatedLEList.get(indexOfLE).trim()+"' ,from the drop down of special allocation screen. ]\n";
						return false;
					}
					if (sPLHeadLists.size() != sSpeciallyAllocatedLEList.size()) {
						Messages.errorMsg = "[ ERROR : Test data wasn't given properly please cross check, No Of LE's : '"+sSpeciallyAllocatedLEList.size()+"', are not matching with the given PLHeads : '"+sPLHeadLists.size()+"' values for each record.]\n";
						return false;	
					}
					List<String> sRespectiveLEPLHeadList = Arrays.asList(sPLHeadLists.get(indexOfLE).split("\\*"));
					List<String> sRespectiveLEStrategyList = Arrays.asList(sStrategyLists.get(indexOfLE).split("\\*"));
					List<String> sRespectiveLECustodianAccList = Arrays.asList(sCustodianAccountLists.get(indexOfLE).split("\\*"));
					List<String> sRespectiveLEModeList = Arrays.asList(sModeLists.get(indexOfLE).split("\\*"));
					List<String> sRespectiveLEAllocatedList = Arrays.asList(sAllocatedLists.get(indexOfLE).split("\\*"));
					List<String> sRespectiveLEsActionsList = Arrays.asList(sActionLists.get(indexOfLE).split("\\*"));

					for (int indexOfHeaders = 0; indexOfHeaders < sRespectiveLEPLHeadList.size(); indexOfHeaders++) {

						bStatus = doPerformOperationsOnTheExistingAllocationRecord(sSpeciallyAllocatedLEList.get(indexOfLE).trim(), sRespectiveLEPLHeadList.get(indexOfHeaders).trim(), sRespectiveLEStrategyList.get(indexOfHeaders).trim(), sRespectiveLECustodianAccList.get(indexOfHeaders).trim(), sRespectiveLEModeList.get(indexOfHeaders).trim(), sRespectiveLEAllocatedList.get(indexOfHeaders).trim(), sRespectiveLEsActionsList.get(indexOfHeaders).trim());
						if (!bStatus) {
							return false;
						}
						if (sRespectiveLEsActionsList.get(indexOfHeaders).trim().equalsIgnoreCase("View")) {
							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='wrappertreeGridViewAllocation_grid']//div[@id='contenttreeGridViewAllocation_grid']//div[contains(@id,'filterrow')]//div[contains(@class,'row')][2]//input[@type='textarea']"), Constants.lTimeOut);
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Grid for respective special allocation combination record of Legal Entity : '"+sSpeciallyAllocatedLEList.get(indexOfLE).trim()+"' , with the combination of PLHead : '"+sRespectiveLEPLHeadList.get(indexOfHeaders).trim()+"', Strategy : '"+sRespectiveLEStrategyList.get(indexOfHeaders).trim()+"', CustodianAccount : '"+sRespectiveLECustodianAccList.get(indexOfHeaders).trim()+"', Mode : '"+sRespectiveLEModeList.get(indexOfHeaders).trim()+"', Allocated : '"+sRespectiveLEAllocatedList.get(indexOfHeaders).trim()+"' wasn't visible.]\n";
								return false;
							}
							if (mapAllocationDetails.get("Investors") != null && mapAllocationDetails.get("Amounts") != null) {
								List<String> sInvestorsListsOfLE = Arrays.asList(mapAllocationDetails.get("Investors").split(":"));
								List<String> sAmountsListsOfLE = Arrays.asList(mapAllocationDetails.get("Amounts").split(":"));
								List<String> sClassListOfLE = Arrays.asList(mapAllocationDetails.get("ClassForInvestor").split(":"));
								List<String> sSeriesListsOfLE = Arrays.asList(mapAllocationDetails.get("SeriesForInvestor").split(":"));

								List<String> sInvestorsListOfProcessedRecord = Arrays.asList(sInvestorsListsOfLE.get(indexOfLE).split("\\*"));
								List<String> sAmountsListOfProcessedRecord = Arrays.asList(sAmountsListsOfLE.get(indexOfLE).split("\\*"));
								List<String> sClassListOfProcessedRecord = Arrays.asList(sClassListOfLE.get(indexOfLE).split("\\*"));
								List<String> sSeriesListOfProcessedRecord = Arrays.asList(sSeriesListsOfLE.get(indexOfLE).split("\\*"));

								List<String> sInvestorsSubList = Arrays.asList(sInvestorsListOfProcessedRecord.get(indexOfHeaders).split(";"));
								List<String> sAmountsSubList = Arrays.asList(sAmountsListOfProcessedRecord.get(indexOfHeaders).split(";"));
								List<String> sClassSubList = Arrays.asList(sClassListOfProcessedRecord.get(indexOfHeaders).split(";"));
								List<String> sSeriesSubList = Arrays.asList(sSeriesListOfProcessedRecord.get(indexOfHeaders).split(";"));

								String sErrMsgAppendableString = "Legal Entity : '"+sSpeciallyAllocatedLEList.get(indexOfLE).trim()+"' , with the combination of PLHead : '"+sRespectiveLEPLHeadList.get(indexOfHeaders).trim()+"', Strategy : '"+sRespectiveLEStrategyList.get(indexOfHeaders).trim()+"', CustodianAccount : '"+sRespectiveLECustodianAccList.get(indexOfHeaders).trim()+"', Mode : '"+sRespectiveLEModeList.get(indexOfHeaders).trim()+"', Allocated : '"+sRespectiveLEAllocatedList.get(indexOfHeaders).trim()+"' ";

								bStatus = doVerifyInvestorsDetailsOfExistingAllocationRecord(sErrMsgAppendableString, sInvestorsSubList, sAmountsSubList,sClassSubList,sSeriesSubList);
								if (!bStatus) {
									return false;
								}
							}							
						}
						if (sRespectiveLEsActionsList.get(indexOfHeaders).trim().equalsIgnoreCase("Delete")) {
							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(normalize-space(),'Are you sure you want to delete allocation for selected combination')]//div//button[normalize-space()='OK']"), Constants.lTimeOut);
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR :  Modal dialogue popup is not visible for respective special allocation combination record of Legal Entity : '"+sSpeciallyAllocatedLEList.get(indexOfLE).trim()+"' , with the combination of PLHead : '"+sRespectiveLEPLHeadList.get(indexOfHeaders)+"', Strategy : '"+sRespectiveLEStrategyList.get(indexOfHeaders)+"', CustodianAccount : '"+sRespectiveLECustodianAccList.get(indexOfHeaders)+"', Mode : '"+sRespectiveLEModeList.get(indexOfHeaders)+"', Allocated : '"+sRespectiveLEAllocatedList.get(indexOfHeaders)+"'.]\n";
								return false;
							}

							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(normalize-space(),'Are you sure you want to delete allocation for selected combination')]//div//button[normalize-space()='OK']"));
							if(!bStatus){
								Messages.errorMsg = "Cannot Click on OK button of Alert Message for the Combination of PLHead : '"+sRespectiveLEPLHeadList.get(indexOfHeaders)+"', Strategy : '"+sRespectiveLEStrategyList.get(indexOfHeaders)+"', CustodianAccount : '"+sRespectiveLECustodianAccList.get(indexOfHeaders)+"', Mode : '"+sRespectiveLEModeList.get(indexOfHeaders)+"', Allocated : '"+sRespectiveLEAllocatedList.get(indexOfHeaders)+"' ";
								return false;
							}
							NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@class,'alert-success') and contains(normalize-space(),'Special Allocation Deletion Completed')]"), Constants.lTimeOut);
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Success message is not visible After Clicking on 'OK' button of Alert for deletion for the  Legal Entity : '"+sSpeciallyAllocatedLEList.get(indexOfLE).trim()+"' , with the combination of PLHead : '"+sRespectiveLEPLHeadList.get(indexOfHeaders)+"', Strategy : '"+sRespectiveLEStrategyList.get(indexOfHeaders)+"', CustodianAccount : '"+sRespectiveLECustodianAccList.get(indexOfHeaders)+"', Mode : '"+sRespectiveLEModeList.get(indexOfHeaders)+"', Allocated : '"+sRespectiveLEAllocatedList.get(indexOfHeaders)+"' .]\n";
								return false;
							}
							continue;
						}												
					}
				}
			}

			bStatus = doClickOnAllocationNavigationbuttons("Next");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Next Button Cannot be clicked on 'Special Allocation' Screen.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Selecting the Existing Allocation record with the combination of given headers.
	public static boolean doPerformOperationsOnTheExistingAllocationRecord(String sLEName, String sPLHead, String sStrategy, String sCustodianAccount, String sMode, String sAllocatedAmount, String sAction){
		bStatus = doExpandOrCollapseExistingAllocationDetailsView("expand");
		if (!bStatus) {
			return false;
		}
		String sTableLocator = "//tbody[contains(@id,'existingAllocationTabBody')]//tr";
		String sPLHeadLocator = "";
		String sStrategyLocator = "";
		String sCustodianAccountLocator = "";
		String sModeLocator = "";
		String sAllocatedAmountLocator = "";
		String sActiontype = "";
		try {
			if (sPLHead != null && !sPLHead.equalsIgnoreCase("") && !sPLHead.equalsIgnoreCase("None")) {
				sPLHeadLocator = "//following-sibling::td[normalize-space()=\""+sPLHead+"\"]";
			}
			if (sStrategy != null && !sStrategy.equalsIgnoreCase("") && !sStrategy.equalsIgnoreCase("None")) {
				sStrategyLocator = "//following-sibling::td[normalize-space()=\""+sStrategy+"\"]";
			}
			if (sCustodianAccount != null && !sCustodianAccount.equalsIgnoreCase("") && !sCustodianAccount.equalsIgnoreCase("None")) {
				sCustodianAccountLocator = "//following-sibling::td[normalize-space()=\""+sCustodianAccount+"\"]";
			}
			if (sMode != null && !sMode.equalsIgnoreCase("") && !sMode.equalsIgnoreCase("None")) {
				sModeLocator = "//following-sibling::td[normalize-space()=\""+sMode+"\"]";
			}
			if (sAllocatedAmount != null && !sAllocatedAmount.equalsIgnoreCase("") && !sAllocatedAmount.equalsIgnoreCase("None")) {
				sAllocatedAmountLocator = "//following-sibling::td[normalize-space()=\""+sAllocatedAmount+"\" or contains(normalize-space(),\""+sAllocatedAmount+"\")]";
			}			
			if (sAction != null && !sAction.equalsIgnoreCase("None")) {				
				if (sAction.trim().equalsIgnoreCase("View")) {
					sActiontype = "viewAllocation";
				}
				if (sAction.trim().equalsIgnoreCase("Delete")) {
					sActiontype = "deleteAllocationConfirmation";
				}
			}

			String sSAProcessedRecordViewButtonLocator = sTableLocator + sPLHeadLocator + sStrategyLocator + sCustodianAccountLocator + sModeLocator + sAllocatedAmountLocator + "//following-sibling::td//a[contains(@onclick,'"+sActiontype+"')]";			
			bStatus = NewUICommonFunctions.scrollToView(By.xpath(sSAProcessedRecordViewButtonLocator));

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(sSAProcessedRecordViewButtonLocator), Constants.lTimeOut);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : No Specially Allocated Record found for the Legal Entity : '"+sLEName+"' , with the combination of PLHead : '"+sPLHead+"', Strategy : '"+sStrategy+"', CustodianAccount : '"+sCustodianAccount+"', Mode : '"+sMode+"', Allocated : '"+sAllocatedAmount+"' .]\n";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(sSAProcessedRecordViewButtonLocator));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to click the 'VIEW' button to verify Specially Allocated Record found for the Legal Entity : '"+sLEName+"' , with the combination of PLHead : '"+sPLHead+"', Strategy : '"+sStrategy+"', CustodianAccount : '"+sCustodianAccount+"', Mode : '"+sMode+"', Allocated : '"+sAllocatedAmount+"' .]\n";
				return false;
			}			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Enter values into the grid search fields.
	public static boolean doVerifyInvestorsDetailsOfExistingAllocationRecord(String sAppendableErrString, List<String> sInvestorsSubList, List<String> sAmountsSubList, List<String> sClassSubList, List<String> sSeriesSubList){
		try {

			for (int indexOfInv = 0; indexOfInv < sInvestorsSubList.size(); indexOfInv++) {
				bStatus = doFilterTheGridForExistingSpecialAllocationDetails(sInvestorsSubList.get(indexOfInv),sClassSubList.get(indexOfInv),sSeriesSubList.get(indexOfInv),sAppendableErrString);
				if(!bStatus){
					return false;
				}

				NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 30);
				bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='contenttabletreeGridViewAllocation_grid']//div[contains(@class,'grid-cell-') and normalize-space()=\""+sInvestorsSubList.get(indexOfInv).trim()+"\"]//..//following::div[contains(@class,'grid-cell-') and normalize-space()='"+sAmountsSubList.get(indexOfInv).trim()+"' or contains(normalize-space(),'"+sAmountsSubList.get(indexOfInv).trim()+"')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Investor : '"+sInvestorsSubList.get(indexOfInv).trim()+"' ,record is not present with the Allocated Amount value : '"+sAmountsSubList.get(indexOfInv).trim()+"' in the Existing Allocation search Grid for the combination : '"+sAppendableErrString+"'.]\n";
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doFilterTheGridForExistingSpecialAllocationDetails(String sInvestorName, String sClassName, String sSeriesName, String sAppendableErrString) {
		try {

			String filterLocator = "//div[@id='contenttreeGridViewAllocation_grid']//div[contains(@id,'filterrow')]";

			bStatus = Elements.enterText(Global.driver,By.xpath(filterLocator+"//div[contains(@class,'row')][4]//input[@type='textarea']"), sInvestorName);
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Enter Investor Name : '"+sInvestorName+"' ,into Investor search filter in the Grid for the existing allocation record details : "+sAppendableErrString+".]\n";
				return false;
			}

			if(!sClassName.equalsIgnoreCase("None")){
				bStatus = Elements.enterText(Global.driver,By.xpath(filterLocator+"//div[contains(@class,'row')][2]//input[@type='textarea']"), sClassName);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Enter Class Name : '"+sClassName+"' ,into Class search filter in the Grid for the existing allocation record details : "+sAppendableErrString+".]\n";
					return false;
				} 
			}

			if(!sSeriesName.equalsIgnoreCase("None")){
				bStatus = Elements.enterText(Global.driver,By.xpath(filterLocator+"//div[contains(@class,'row')][3]//input[@type='textarea']"), sSeriesName);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Enter Series Name : '"+sSeriesName+"' ,into Series search filter in the Grid for the existing allocation record details : "+sAppendableErrString+".]\n";
					return false;
				} 
			}

			Thread.sleep(1500);

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Process Allocation for TB's Uploaded LE's
	public static boolean doProcessAllocationForTBsUploadedLEs(Map<String,String> mapAllocationDetails){
		try {
			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 20);
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'Process Allocation')]"));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Not Navigated to 'Process Allocation' screen after clicking on Next Buttonon on 'Fee Adjustment' Screen.]\n";
				return false;
			}
			if (mapAllocationDetails.get("FinalLEsToProcessAllocation") != null) {
				List<String> sEdsReportType = null;
				List<String> sEdsReportFileName = null;
				List<String> sOldEDSReportFilePath = null;
				List<String> sOldEDSReportSheetName = null;
				List<String> sExpectedStatusAfterLEsProcessed = null;
				List<String> sLEsToProcessAllocation = Arrays.asList(mapAllocationDetails.get("FinalLEsToProcessAllocation").split(":"));
				if (mapAllocationDetails.get("ExpectedStatusAfterProcessing") != null) {
					sExpectedStatusAfterLEsProcessed = Arrays.asList(mapAllocationDetails.get("ExpectedStatusAfterProcessing").split(":"));
				}
				
				if(mapAllocationDetails.get("NewEDSReportFileNameWithExtension") != null){					
					sEdsReportFileName = Arrays.asList(mapAllocationDetails.get("NewEDSReportFileNameWithExtension").split(","));					
				}
				if(mapAllocationDetails.get("EDSFileType") != null){
					sEdsReportType = Arrays.asList(mapAllocationDetails.get("EDSFileType").split(","));
				}
				if(mapAllocationDetails.get("OldEDSFilePathToCompare") != null && mapAllocationDetails.get("OldEDSFileSheetName") != null){
					sOldEDSReportFilePath = Arrays.asList(mapAllocationDetails.get("OldEDSFilePathToCompare").split(","));
					sOldEDSReportSheetName = Arrays.asList(mapAllocationDetails.get("OldEDSFileSheetName").split(","));
				}
				
				
				//Process the Allocation for the Provided Legal Entities
				if(sExpectedStatusAfterLEsProcessed != null){
					for (int indexOfLE = 0; indexOfLE < sLEsToProcessAllocation.size(); indexOfLE++) {
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[@id='excelUploadTrialBalDiv']//tbody//tr//td//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//button[contains(@onclick,'process')]"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to click Process Allocation for LE : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"']\n";
							return false;
						}
						TimeUnit.SECONDS.sleep(2);
						NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 25);
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-danger']"), 2);
						if(bStatus){
							Messages.errorMsg = "Failed To Process the Allocation";
							return false;
						}
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tr//td//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//span[normalize-space()='"+sExpectedStatusAfterLEsProcessed.get(indexOfLE).trim()+"']"), Constants.lTimeOut);
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Expected Status : '"+sExpectedStatusAfterLEsProcessed.get(indexOfLE).trim()+"' ,wasn't visible after processing Allocation for LE : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"']\n";
							return false;
						}
						if(mapAllocationDetails.get("Processed By") != null){
							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//td[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//following-sibling::td//label[contains(@id,'processedBy') and contains(normalize-space(),'"+mapAllocationDetails.get("Processed By")+"')]"), Constants.lTimeOut);
							if(!bStatus){
								Messages.errorMsg = "[ ERROR : Processed By "+mapAllocationDetails.get("Processed By")+" is Not Populated after Processing the Legal Entity :"+sLEsToProcessAllocation.get(indexOfLE).trim()+".]\n";
								return false;
							}
						}
					}
				}				
				
				//Post the Fee in Master if Button Available
				if(mapAllocationDetails.get("isFeeToBePostedInMasterAvailable") != null && mapAllocationDetails.get("isFeeToBePostedInMasterAvailable").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'processFeePostInMaster') and contains(normalize-space(),'Post Fee in Master')]"));
					if(!bStatus){
						Messages.errorMsg = " Unable to Click Post Fee in Master button";
						return false;
					}

					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Success Message is Not visible after click on the Post Fee in Master button";
						return false;
					}

				}
				
				//Download and Verify the Downloaded EDS File with the Provided old EDS file 
				for (int indexOfLE = 0; indexOfLE < sLEsToProcessAllocation.size(); indexOfLE++) {
					if(sEdsReportFileName != null && !sEdsReportFileName.get(indexOfLE).equalsIgnoreCase("None")){
						String newFilePath = Global.mapCredentials.get("EDS_ReportPathToDownload")+"\\"+sEdsReportFileName.get(indexOfLE);
						
						String legalEntiyAndBreakPeriod = sLEsToProcessAllocation.get(indexOfLE)+"_"+mapAllocationDetails.get("Break Period Date");
						legalEntiyAndBreakPeriod = legalEntiyAndBreakPeriod.replaceAll("/", "_").replaceAll("\\W", "");
						
						NewUICommonFunctions.DeleteFileIfExists(newFilePath);
						
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//td[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//following-sibling::td//a[contains(@onclick,'viewReport')]"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to click View button for Legal Entity : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+" in Process Screen']\n";
							return false;
						}
						Thread.sleep(3000);
						if(sEdsReportType != null && sOldEDSReportFilePath != null && !sOldEDSReportFilePath.get(indexOfLE).equalsIgnoreCase("None")){
							bStatus = verifyDownloadedEDSReportAndCompareWithOldFile(newFilePath, sOldEDSReportFilePath.get(indexOfLE), sOldEDSReportSheetName.get(indexOfLE), legalEntiyAndBreakPeriod, sEdsReportType.get(indexOfLE),mapAllocationDetails);
							if(!bStatus){
								return false;
							}							
						}
						
					}				
				}
			}
			
			//Proceed to Next from the Process Screen
			bStatus = doClickOnAllocationNavigationbuttons("Next");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Next Button Cannot be clicked on 'Process Allocation' Screen.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	private static boolean verifyDownloadedEDSReportAndCompareWithOldFile(String newFilePath, String sOldEDSReportFilePath,String oldSheetName,String legalEntiyAndBreakPeriod,String sType,Map<String,String> mapAllocationDetails) {
		try {
			Thread.sleep(3000);
			File file = new File(newFilePath);
			if(!file.exists()){
				Messages.errorMsg = "File Path :"+newFilePath+" is not Exists";
				return false;				
			}
			if(sType.equalsIgnoreCase("EDS")){
				if(mapAllocationDetails.get("RemoveExtraColumnsInOldFile") != null){
					ComparisonReportFunctions.removeExtraColumnsAndAddHolderAccountInOldEDSReport(sOldEDSReportFilePath);
				}
				bStatus = ComparisonReportFunctions.compareEDSReports(newFilePath, "EDS_Report", sOldEDSReportFilePath, oldSheetName, "ComparisonReportsResultsInExcelFormat", legalEntiyAndBreakPeriod, "Results");
				if(!bStatus){
					Messages.errorMsg = "Comparision of EDS Report Failed for Legal Entity with the Break Period "+legalEntiyAndBreakPeriod+":"+Messages.errorMsg;
					NewReporting.bCreateFile = false;
					return false;
				}
				NewReporting.bCreateFile = false;
			}
			if(sType.equalsIgnoreCase("Fund")){
				bStatus = ComparisonReportFunctions.compareEDSReportsBaasedonSeries(newFilePath, "EDS_Report", sOldEDSReportFilePath, oldSheetName, "ComparisonReportsResultsInExcelFormat", legalEntiyAndBreakPeriod, "Results");
				if(!bStatus){
					Messages.errorMsg = "Comparision of EDS Report Failed for Legal Entity with the Break Period "+legalEntiyAndBreakPeriod+":"+Messages.errorMsg;
					NewReporting.bCreateFile = false;
					return false;
				}
				NewReporting.bCreateFile = false;
			}
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	//Do Finalize / Undo on NAV Freeze screen.
	public static boolean doPerformOperationsOnNAVFreezeScreenOld(Map<String,String> mapAllocationDetails){
		try {
			if (mapAllocationDetails.get("LEsToNAVFreezeOrUndo") != null) {
				List<String> sLEsToProcessAllocation = Arrays.asList(mapAllocationDetails.get("LEsToNAVFreezeOrUndo").split(","));
				List<String> sOperationsOnLE = Arrays.asList(mapAllocationDetails.get("ActionFinalizeOrUndoNAV").split(","));
				List<String> sExpectedStatusAfterLEsOperations = Arrays.asList(mapAllocationDetails.get("NAVFreezeStatus").split(","));
				if (sExpectedStatusAfterLEsOperations.size() != sLEsToProcessAllocation.size()) {
					Messages.errorMsg = "[ ERROR : Test data wasn't given properly please do crosscheck the number of LE's given to process NAV Freeze actions and their respective Expected status's.]\n";
					return false;
				}
				for (int indexOfLE = 0; indexOfLE < sLEsToProcessAllocation.size(); indexOfLE++) {
					if (sOperationsOnLE.get(indexOfLE) != null && !sOperationsOnLE.get(indexOfLE).equalsIgnoreCase("None")) {
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//tbody//tr//td//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//button[contains(@onclick,'freeze') and contains(normalize-space(),'"+sOperationsOnLE.get(indexOfLE).trim()+"')]"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to perform Operation : '"+sOperationsOnLE.get(indexOfLE).trim()+"' , on LE : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"' .]\n";
							return false;
						}
						TimeUnit.SECONDS.sleep(2);
					}
					NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 25);
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tbody//tr//td//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//span[contains(@id,'allocationStatus') and normalize-space()='"+sExpectedStatusAfterLEsOperations.get(indexOfLE).trim()+"']"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Expected Status : '"+sExpectedStatusAfterLEsOperations.get(indexOfLE).trim()+"' ,for LE : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"' ,wasn't visible.]\n";
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

	//Mapping Hot Issue P&L Heads.
	public static boolean doMapPnLHeadsForHotissueAllocationsForRespectiveLEs(Map<String,String> mapAllocationDetails){
		try {

			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 20);
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'Hot Issue Allocation')]"));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Not Navigated to 'Hot Issue Allocation' screen after clicking on Next Buttonon on 'Special Allocation' Screen.]\n";
				return false;
			}

			if (mapAllocationDetails.get("LEsToMapHeadersForHotIssue") != null) {
				List<String> sLEsListForHotIssueMapping = Arrays.asList(mapAllocationDetails.get("LEsToMapHeadersForHotIssue").split(":"));				
				for (int iLECounter = 0; iLECounter < sLEsListForHotIssueMapping.size(); iLECounter++) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//label[normalize-space()=\""+sLEsListForHotIssueMapping.get(iLECounter)+"\"]//..//following-sibling::td//a[contains(@onclick,'editMappedHeads')]"), Constants.lTimeOut);
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : P&L Head Mapping 'EDIT' button with Legal Entity : '"+sLEsListForHotIssueMapping.get(iLECounter)+"' wasn't present in the table.]\n";
						return false;
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()=\""+sLEsListForHotIssueMapping.get(iLECounter)+"\"]//..//following-sibling::td//a[contains(@onclick,'editMappedHeads')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to click on P&L Head Mapping 'EDIT' button with Legal Entity : '"+sLEsListForHotIssueMapping.get(iLECounter)+"' .]\n";
						return false;
					}
					NewUICommonFunctions.waitUntilSpinnerGoInvisible(2, 30);
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'Head Mapping')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : User wasn't redirected to P&L Head Mapping screen when clicked on Edit button of respective leagal entity Head mapping.]\n";
						return false;
					}
					List<String> sLEsPnLHeadsLists = Arrays.asList(mapAllocationDetails.get("PnLHeadNames").split(":"));
					List<String> sLEsPnLTypesLists = Arrays.asList(mapAllocationDetails.get("PnLTypes").split(":"));					
					if (sLEsPnLHeadsLists != null && sLEsPnLTypesLists != null && !sLEsPnLHeadsLists.isEmpty() && !sLEsPnLTypesLists.isEmpty()) {
						if (sLEsPnLHeadsLists.size() != sLEsPnLTypesLists.size()) {
							Messages.errorMsg = "[ TEST DATA ERROR : Please check the 'P&L Headers Names' and 'Types' details for Hot issue allocation, both sizes are not matching : P&LHeadNames : "+sLEsPnLHeadsLists+" , P&LTypes : "+sLEsPnLTypesLists+" .]\n";
							return false;
						}
						List<String> sPnLHeadsSubLists = Arrays.asList(sLEsPnLHeadsLists.get(iLECounter).split(","));
						List<String> sPnLTypesSubLists = Arrays.asList(sLEsPnLTypesLists.get(iLECounter).split(","));	
						if (sLEsPnLHeadsLists.size() != sLEsPnLTypesLists.size()) {
							Messages.errorMsg = "[ TEST DATA ERROR : Please check the 'P&L Headers Names' and 'Types' details for Hot issue allocation, both sizes are not matching for LE : '"+sLEsListForHotIssueMapping.get(iLECounter)+"' respective Heads => P&LHeadNames : "+sPnLHeadsSubLists+" , P&LTypes : "+sPnLTypesSubLists+" .]\n";
							return false;
						}
						for (int i = 0; i < sPnLHeadsSubLists.size(); i++) {
							bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//td[normalize-space()=\""+sPnLTypesSubLists.get(i)+"\"]//preceding-sibling::td[normalize-space()=\""+sPnLHeadsSubLists.get(i)+"\"]//preceding-sibling::td//span"), Constants.lTimeOut);
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : P&L Head Name : '"+sPnLHeadsSubLists.get(i)+"' ,P&L Type : '"+sPnLTypesSubLists.get(i)+"' combination wasn't present in the table for the LE : '"+sLEsListForHotIssueMapping.get(iLECounter)+"'.]\n";
								return false;
							}
							bStatus = NewUICommonFunctions.scrollToView(By.xpath("//td[normalize-space()=\""+sPnLTypesSubLists.get(i)+"\"]//preceding-sibling::td[normalize-space()=\""+sPnLHeadsSubLists.get(i)+"\"]//preceding-sibling::td//span"));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : View wasn't scrolled to the record in table with the combination of P&L Head Name : '"+sPnLHeadsSubLists.get(i)+"' ,P&L Type : '"+sPnLTypesSubLists.get(i)+"' for the LE : '"+sLEsListForHotIssueMapping.get(iLECounter)+"'.]\n";
								return false;
							}
							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//td[normalize-space()=\""+sPnLTypesSubLists.get(i)+"\"]//preceding-sibling::td[normalize-space()=\""+sPnLHeadsSubLists.get(i)+"\"]//preceding-sibling::td//span[@class='checked']"), 2);
							if (!bStatus){
								bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//td[normalize-space()=\""+sPnLTypesSubLists.get(i)+"\"]//preceding-sibling::td[normalize-space()=\""+sPnLHeadsSubLists.get(i)+"\"]//preceding-sibling::td//span"));
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : unable to select the record in table with the combination of P&L Head Name : '"+sPnLHeadsSubLists.get(i)+"' ,P&L Type : '"+sPnLTypesSubLists.get(i)+"' for the LE : '"+sLEsListForHotIssueMapping.get(iLECounter)+"' in P&L Head Mapping screen.]\n";
									return false;
								}
							}
						}
						bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,'submintForm')]"));
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Unable to click on Save button of P&L Head Mapping selection for Hot issue allocations on.]\n";
							return false;
						}
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(""), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = "Success Message is not visible After Saving the P&L Head Mapping for Legal Entity :"+sLEsListForHotIssueMapping.get(iLECounter);
							return false;
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Process Hot Issue Allocations.
	public static boolean doAllocateHotIssuesForLEs(Map<String,String> mapAllocationDetails){
		try {
			if (mapAllocationDetails.get("LEsToAllocateHotIssues") != null) {
				List<String> sLEsToAllocateHotIssue = Arrays.asList(mapAllocationDetails.get("LEsToAllocateHotIssues").split(":"));
				for (int iLECounter = 0; iLECounter < sLEsToAllocateHotIssue.size(); iLECounter++) {
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()=\""+sLEsToAllocateHotIssue.get(iLECounter).trim()+"\"]//..//following-sibling::td//button[contains(@onclick,'allocateHotIssue')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to click on 'ALLOCATE' button for Legal Entity : '"+sLEsToAllocateHotIssue.get(iLECounter).trim()+"' on Hot Issue Allocation screen.]\n";
						return false;
					}
					NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 10);
					if(mapAllocationDetails.get("Processed By") != null){
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//td[normalize-space()=\""+sLEsToAllocateHotIssue.get(iLECounter).trim()+"\"]//following-sibling::td//label[contains(@id,'processedBy') and contains(normalize-space(),'"+mapAllocationDetails.get("Processed By")+"')]"), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = "[ ERROR : Processed By "+mapAllocationDetails.get("Processed By")+" is Not Populated after Allocating the Legal Entity :."+sLEsToAllocateHotIssue.get(iLECounter)+"]\n";
							return false;
						}
					}
				}

			}
			bStatus = doClickOnAllocationNavigationbuttons("Next");
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Next Button Cannot be clicked on 'Hot Issue Allocation' Screen.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//Do Finalize / Undo on NAV Freeze screen.
	public static boolean doPerformOperationsOnNAVFreezeScreen(Map<String,String> mapAllocationDetails){
		try {

			NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 20);
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'NAV Freeze')]"));
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Not Navigated to 'NAV Freeze' screen after clicking on Next Buttonon on 'Process Allocation' Screen.]\n";
				return false;
			}

			if (mapAllocationDetails.get("LEsToNAVFreezeOrUndo") != null) {
				List<String> sLEsToProcessAllocation = Arrays.asList(mapAllocationDetails.get("LEsToNAVFreezeOrUndo").split(":"));
				List<String> sOperationsOnLE = Arrays.asList(mapAllocationDetails.get("ActionFinalizeOrUndoNAV").split(":"));
				List<String> sExpectedStatusAfterLEsOperations = Arrays.asList(mapAllocationDetails.get("NAVFreezeStatus").split(":"));
				List<String> sIsNAVUpdateScreenAvialabl = null;
				if(mapAllocationDetails.get("isNAVUpdateScreenAvailable") != null){
					sIsNAVUpdateScreenAvialabl = Arrays.asList(mapAllocationDetails.get("isNAVUpdateScreenAvailable").split(":"));
				}

				List<String> aOrderIdToUpdateNAV = null;
				if(mapAllocationDetails.get("OrderIdToUpdateNAV") != null){
					aOrderIdToUpdateNAV = Arrays.asList(mapAllocationDetails.get("OrderIdToUpdateNAV").split(":"));
				}
				if (sExpectedStatusAfterLEsOperations.size() != sLEsToProcessAllocation.size()) {
					Messages.errorMsg = "[ ERROR : Test data wasn't given properly please do crosscheck the number of LE's given to process NAV Freeze actions and their respective Expected status's.]\n";
					return false;
				}
				for (int indexOfLE = 0; indexOfLE < sLEsToProcessAllocation.size(); indexOfLE++) {
					if (sOperationsOnLE != null && !sOperationsOnLE.get(indexOfLE).equalsIgnoreCase("None")) {

						if(sOperationsOnLE.get(indexOfLE).equalsIgnoreCase("Finalize")){							
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//button[contains(@onclick,'freeze') and contains(normalize-space(),'"+sOperationsOnLE.get(indexOfLE).trim()+"')]"));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to perform Operation : '"+sOperationsOnLE.get(indexOfLE).trim()+"' , on Legal Entity : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"' .]\n";
								return false;
							}

							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='modal-body' and contains(normalize-space(),'Are you sure you want to finalize this NAV?')]//following-sibling::div/button[normalize-space()='OK']"), Constants.lTimeOut);
							if(!bStatus){
								Messages.errorMsg = "Alert Message is Not Visible After click on the Finalize button in NAV Freez";
								return false;
							}
							bStatus = Elements.click(Global.driver, By.xpath("//div[@class='modal-body' and contains(normalize-space(),'Are you sure you want to finalize this NAV?')]//following-sibling::div/button[normalize-space()='OK']"));
							if(!bStatus){
								Messages.errorMsg = "Unable to select the OK button in Finalizing alert pop up for the Legal Entity ;"+sLEsToProcessAllocation.get(indexOfLE);
								return false;
							}

							//TimeUnit.SECONDS.sleep(2);

							//After finalizing the Fund screen Navigates to NAV Update Screen Calling the NAV Update Screen Functions
							if(aOrderIdToUpdateNAV != null && sIsNAVUpdateScreenAvialabl != null && sIsNAVUpdateScreenAvialabl.get(indexOfLE).equalsIgnoreCase("Yes")){
								bStatus = doSelectTheTardesToUpdateTheNAV(mapAllocationDetails, indexOfLE ,sLEsToProcessAllocation.get(indexOfLE),aOrderIdToUpdateNAV.get(indexOfLE));
								if(!bStatus){
									return false;
								}
							}

						}

						if(sOperationsOnLE.get(indexOfLE).equalsIgnoreCase("Update")){
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//button[contains(@onclick,'update') and contains(normalize-space(),'"+sOperationsOnLE.get(indexOfLE).trim()+"')]"));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to perform Operation : '"+sOperationsOnLE.get(indexOfLE).trim()+"' , on Legal Entity : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"' .]\n";
								return false;
							}
							//TimeUnit.SECONDS.sleep(2);
							if(aOrderIdToUpdateNAV != null && sIsNAVUpdateScreenAvialabl != null && sIsNAVUpdateScreenAvialabl.get(indexOfLE).equalsIgnoreCase("Yes")){
								bStatus = doSelectTheTardesToUpdateTheNAV(mapAllocationDetails, indexOfLE ,sLEsToProcessAllocation.get(indexOfLE),aOrderIdToUpdateNAV.get(indexOfLE));
								if(!bStatus){
									return false;
								}
							}

						}

						if(sOperationsOnLE.get(indexOfLE).equalsIgnoreCase("Undo")){
							bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//button[contains(@onclick,'freeze') and contains(normalize-space(),'"+sOperationsOnLE.get(indexOfLE).trim()+"')]"));
							if (!bStatus) {
								Messages.errorMsg = "[ ERROR : Unable to perform Operation : '"+sOperationsOnLE.get(indexOfLE).trim()+"' , on Legal Entity : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"' .]\n";
								return false;
							}
							//TimeUnit.SECONDS.sleep(2);
							bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='modal-body' and contains(normalize-space(),'Are you sure you want to undo this NAV?')]//following-sibling::div/button[normalize-space()='OK']"), Constants.lTimeOut);
							if(!bStatus){
								Messages.errorMsg = "Alert Message is not visible after click on the Undo Button for the Legal Entity: "+sLEsToProcessAllocation.get(indexOfLE);
								return false;
							}
							bStatus = Elements.click(Global.driver, By.xpath("//div[@class='modal-body' and contains(normalize-space(),'Are you sure you want to undo this NAV?')]//following-sibling::div/button[normalize-space()='OK']"));
							if(!bStatus){
								Messages.errorMsg = "Unable to select the OK button in Undo this NAV alert pop up for the Legal Entity :"+sLEsToProcessAllocation.get(indexOfLE);
								return false;
							}
						}
						NewUICommonFunctions.waitUntilSpinnerGoInvisible(1, 25);
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//tbody//tr//td//label[normalize-space()=\""+sLEsToProcessAllocation.get(indexOfLE).trim()+"\"]//parent::td//following-sibling::td//span[contains(@id,'allocationStatus') and normalize-space()='"+sExpectedStatusAfterLEsOperations.get(indexOfLE).trim()+"']"), Constants.lTimeOut);
						if (!bStatus) {
							Messages.errorMsg = "[ ERROR : Expected Status : '"+sExpectedStatusAfterLEsOperations.get(indexOfLE).trim()+"' ,for Legal Entity : '"+sLEsToProcessAllocation.get(indexOfLE).trim()+"' ,wasn't visible.]\n";
							return false;
						}
					}					
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	//NAV Update Screen 
	public static boolean doSelectTheTardesToUpdateTheNAV(Map<String,String> mapNAVUpdateDetails,int leIndex,String legalEntity,String aOrderIdList){
		try {

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//button[contains(@onclick,'redirectToNavFinalize') and contains(normalize-space(),'Back')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "NAV Update Screen is not Visible";
				return false;
			}
			if (mapNAVUpdateDetails.get("Client Name") != null) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Client Name' in NAV Update Screen ", By.id("clientName"), mapNAVUpdateDetails.get("Client Name"), "No", false);
				if (!bStatus) {
					return false;
				}
			}
			if (mapNAVUpdateDetails.get("Fund Family Name") != null) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Fund Family Name' in NAV Update Screen ", By.id("fundFamilyName"), mapNAVUpdateDetails.get("Fund Family Name"), "No", false);
				if (!bStatus) {
					return false;
				}
			}
			if(mapNAVUpdateDetails.get("Break Period Date")!=null){
				//String sActualvalue = Elements.getElementAttribute(Global.driver, By.id("breakPeriodNavDate"), "value").trim();
				String sActualvalue = Elements.getText(Global.driver, By.xpath("//input[@id='breakPeriodDate']//parent::label"));
				sActualvalue = TradeTypeSubscriptionAppFunctions.formatDate(sActualvalue);
				if (sActualvalue == null || !sActualvalue.equalsIgnoreCase(mapNAVUpdateDetails.get("Break Period Date")) ) {
					Messages.errorMsg = "[ ERROR : '"+sActualvalue+"' : is actual value for field : Break Period in NAV Update Screen which is not matching with the Expected : '"+mapNAVUpdateDetails.get("Break Period Date")+"']\n";
					return false;
				}
			}
			if(mapNAVUpdateDetails.get("NAV Type") != null){
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'NAV Type' in NAV Update Screen, ", By.xpath("//label[contains(normalize-space(),'NAV Type')]//following-sibling::label"), mapNAVUpdateDetails.get("NAV Type"), "Yes", false);
				if (!bStatus) {
					return false;
				}
			}

			//wait for the Series Roll up Page for that specific Legal Entity
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Legal Entity :']//following-sibling::label[normalize-space()='"+legalEntity+"']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Legal Entity Name :"+legalEntity+",is Not visible in NAV Update Screen";
				return false;
			}

			if(aOrderIdList != null && !aOrderIdList.equalsIgnoreCase("None") && !aOrderIdList.equalsIgnoreCase("Previous")){
				List<String> orderIdList = Arrays.asList(aOrderIdList.split(","));	
				bStatus = removeAllCheckedInNAVUpdateScreen();
				for(int i=0;i<orderIdList.size();i++){
					String orderIdLocator = "//tbody//tr//td/a[contains(normalize-space(),'"+getTheTransactionIDFromTradesXMlFiles(orderIdList.get(i))+"')]";
					String checkBoxLocator = "//parent::td//preceding-sibling::td//span/input";
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath(orderIdLocator+checkBoxLocator));
					if(!bStatus){						
						bStatus = Elements.click(Global.driver, By.xpath(orderIdLocator+checkBoxLocator));
						if(!bStatus){
							bStatus = NewUICommonFunctions.jsClick(By.xpath(orderIdLocator+checkBoxLocator));
							if(!bStatus){
								Messages.errorMsg = "Unable to select the Check box for Order ID: "+orderIdList.get(i);
								return false;
							}							
						}
					}
				}				
			}
			if(aOrderIdList != null && !aOrderIdList.equalsIgnoreCase("Previous")){
				String buttonLocator = "//button[contains(@onclick,'confirmUpdate') and contains(normalize-space(),'Update')]";
				bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath(buttonLocator), Constants.lTimeOut);
				if(bStatus){
					bStatus = NewUICommonFunctions.scrollToView(By.xpath(buttonLocator));

					bStatus = Elements.click(Global.driver, By.xpath(buttonLocator));
					if(!bStatus){
						bStatus = NewUICommonFunctions.jsClick(By.xpath(buttonLocator));
						if(!bStatus){
							Messages.errorMsg = "unable to click the Update in NAv Update Screen";
							return false;
						}					
					}
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(normalize-space(),'All selected trades will be updated to Final')]//button[contains(@data-bb-handler,'confirm') and normalize-space()='OK']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Alert Message is not Visible After click on the Update button in NAV Update screen for the Legal Entity: "+legalEntity;
						return false;
					}

					bStatus = Elements.click(Global.driver, By.xpath("//div[contains(normalize-space(),'All selected trades will be updated to Final')]//button[contains(@data-bb-handler,'confirm') and normalize-space()='OK']"));
					if(!bStatus){
						Messages.errorMsg = "Unable to click on OK button to Updated the NAV to Final";
						return false;
					}
				}else{
					Messages.errorMsg = "Update Button is not visible in NAV Update Screen";
					return false;
				}
			}

			if(aOrderIdList != null && aOrderIdList.equalsIgnoreCase("Previous")){
				String buttonLocator = "//button[contains(@onclick,'redirectToNavFinalize') and contains(normalize-space(),'Back')]";
				bStatus = NewUICommonFunctions.scrollToView(By.xpath(buttonLocator));
				bStatus = NewUICommonFunctions.jsClick(By.xpath(buttonLocator));
				if(!bStatus){
					Messages.errorMsg = "Back button cannot be clicked in NAV Update Screen";
					return false;
				}
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Remove All Check boxes in NAV Update Screen
	public static boolean removeAllCheckedInNAVUpdateScreen() {
		try {

			String checkBoxLocator = "//tbody//td//span";
			int xpathCount = Elements.getXpathCount(Global.driver, By.xpath(checkBoxLocator));

			for(int i=0;i<xpathCount;i++){
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath(checkBoxLocator+"/input[@id='checkTrade_"+i+"']"));
				if(bStatus){
					bStatus = Elements.click(Global.driver, By.xpath(checkBoxLocator+"/input[@id='checkTrade_"+i+"']"));
					if(!bStatus){
						bStatus = NewUICommonFunctions.jsClick(By.xpath(checkBoxLocator+"/input[@id='checkTrade_"+i+"']"));
						if(!bStatus){
							Messages.errorMsg = "Check box Cannot be Unchecked";
							return false;
						}						
					}
				}
			}


			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//Series Roll up Screen
	public static boolean doFillOrVerifySeriesRollupScreen(Map<String,String> mapSeriesRollupData){
		try {

			if(mapSeriesRollupData.get("LegalEntityToSelectRollUp") != null){
				List<String> aLeList = Arrays.asList(mapSeriesRollupData.get("LegalEntityToSelectRollUp").split(":"));
				List<String> aClassList = Arrays.asList(mapSeriesRollupData.get("ClassNameOfRollUpSeries").split(":"));
				List<String> aBenchMarkSeriesList = Arrays.asList(mapSeriesRollupData.get("BenchMarkSeriesToSelect").split(":"));
				List<String> aSeriesTobeRolledUpList = Arrays.asList(mapSeriesRollupData.get("SeriesTobeRollup").split(":"));
				List<String> aSRPOperationList = Arrays.asList(mapSeriesRollupData.get("SeriesRollupOperation").split(":"));

				for(int leIndex = 0;leIndex<aLeList.size();leIndex++){
					//Click the series Roll up button based on the Legal Entity Name
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='"+aLeList.get(leIndex)+"']//parent::td//following-sibling::td//button[contains(@onclick,'seriesRollup') and normalize-space()='Rollup']"));
					if(!bStatus){
						Messages.errorMsg = "Unable click the Series Rollup button for Legal Entity :"+aLeList.get(leIndex);
						return false;
					}

					//wait for the Series Roll up Page for that specific Legal Entity
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[normalize-space()='Legal Entity :']//following-sibling::label[normalize-space()='"+aLeList.get(leIndex)+"']"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Legal Entity Name :"+aLeList.get(leIndex)+",is Not visible in Series Rollup Screen";
						return false;
					}

					//Split the Class and Series based on the Legal Entity Index
					List<String> indvClassList = Arrays.asList(aClassList.get(leIndex).split(","));
					List<String> indvBMSeriesList = Arrays.asList(aBenchMarkSeriesList.get(leIndex).split(","));
					List<String> indvSeriesRupList = Arrays.asList(aSeriesTobeRolledUpList.get(leIndex).split(","));
					for(int classIndex = 0; classIndex<indvClassList.size() ; classIndex++){
						String seriesLabelLocator = "//div//label[normalize-space()='"+indvClassList.get(classIndex)+"']";
						String seriesDropdownLoc = seriesLabelLocator+"//parent::div//parent::div/following-sibling::div[1]//span[contains(@id,'select')]";

						//Select the Rollup Series Dropdown based on the Class Name
						NewUICommonFunctions.scrollToView(By.xpath(seriesDropdownLoc));
						bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(indvBMSeriesList.get(classIndex), By.xpath(seriesDropdownLoc));
						if(!bStatus){
							Messages.errorMsg = "Series "+indvBMSeriesList.get(classIndex)+" Not selected from Drop down :"+Messages.errorMsg;
							return false;
						}

						//Split the Series to be rolled up based on the Class index
						List<String> aSeriesToBeSelect = Arrays.asList(indvSeriesRupList.get(classIndex).split("\\|"));
						for(int rpSeriesIndex = 0; rpSeriesIndex < aSeriesToBeSelect.size(); rpSeriesIndex++){
							String checkBoxLocator = "//div[contains(@id,'contenttablejqxgrid')]//div/span[normalize-space()='"+aSeriesToBeSelect.get(rpSeriesIndex)+"']//parent::div//preceding-sibling::div";

							bStatus = Wait.waitForElementPresence(Global.driver, By.xpath(checkBoxLocator+"//span[contains(@class,'checked')]"), 2);
							//bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath(checkBoxLocator+"//span"));
							if(!bStatus){
								bStatus = Elements.click(Global.driver, By.xpath(checkBoxLocator+"//span//parent::div"));
								if(!bStatus){
									Messages.errorMsg = "Unable to Select the Check box for the Series :"+aSeriesToBeSelect.get(rpSeriesIndex);
									return false;
								}
							}
						}
					}

					NewUICommonFunctions.scrollToView(By.xpath("//h4"));
					if(aSRPOperationList.get(leIndex).equalsIgnoreCase("Submit")){
						bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'Series') and normalize-space()='Submit']"));
						if(!bStatus){
							Messages.errorMsg = "Unable to select the Submit Button for the Legal Entity: "+aLeList.get(leIndex);
							return false;
						}
					}
					if(aSRPOperationList.get(leIndex).equalsIgnoreCase("Previous")){
						bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'redirect') and normalize-space()='Previous']"));
						if(!bStatus){
							Messages.errorMsg = "Unable to select the Previous Button for the Legal Entity: "+aLeList.get(leIndex);
							return false;
						}
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4/p[contains(normalize-space(),'NAV Freeze')]"), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = "Page is not navigated to NAV Freeze Screen afeter clicking the Cancel Button for the Legal Entity"+aLeList.get(leIndex);
							return false;
						}
					}
					if(aSRPOperationList.get(leIndex).equalsIgnoreCase("Cancel")){
						bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'Home') and normalize-space()='Cancel']"));
						if(!bStatus){
							Messages.errorMsg = "Unable to select the Cancel Button for the Legal Entity: "+aLeList.get(leIndex);
							return false;
						}
						bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4/p[contains(normalize-space(),'Choose Portfolio')]"), Constants.lTimeOut);
						if(!bStatus){
							Messages.errorMsg = "Page is not navigated to Choose Portfolio Screen afeter clicking the Cancel Button for the Legal Entity"+aLeList.get(leIndex);
							return false;
						}
					}
				}
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	//getThe Transaction ID Based on the Test Case Id of the Trade
	public static String getTheTransactionIDFromTradesXMlFiles(String testCaseId){
		try {
			String sTransactionId = "";
			Map<String ,String> mapXMLSPSUBDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSPSUBDetailsXMLFilePath, "TradeTypeSPSUB", testCaseId);
			if(mapXMLSPSUBDetails != null){
				sTransactionId = mapXMLSPSUBDetails.get("TransactionID");
			}
			else
			{
				Map<String ,String> mapXMLSubscriptionDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeREDDetailsXMLFilePath, "TradeTypeRED", testCaseId);
				if(mapXMLSubscriptionDetails != null)
				{
					sTransactionId = mapXMLSubscriptionDetails.get("TransactionID");
				}
				else
				{
					Map<String ,String> mapXMLTransferDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeTRANDetailsXMLFilePath, "TradeTypeTRAN", testCaseId);
					if(mapXMLTransferDetails != null)
					{
						sTransactionId = mapXMLTransferDetails.get("TransactionID");
					}
					else
					{
						Map<String ,String> mapXMLExchangeDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeEXCNDetailsXMLFilePath, "TradeTypeEXCN", testCaseId);
						if(mapXMLExchangeDetails != null){
							sTransactionId = mapXMLExchangeDetails.get("TransactionID");
						}
						else
						{
							Map<String ,String> mapXMLSwitchDetails = XMLLibrary.getCreatedMasterDataFromXML(XMLLibrary.sTradeTypeSWITCHDetailsXMLFilePath, "TradeTypeSWITCH", testCaseId);
							if(mapXMLSwitchDetails != null){
								sTransactionId = mapXMLSwitchDetails.get("TransactionID");
							}else{
								sTransactionId = testCaseId;
							}
						}
					}
				}
			}
			return sTransactionId;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

	//Fee Adjustment Screen.	
	public static boolean doFillFeeAdjustmentDetails(Map<String,String> mapFeeAdjustmentDetails){
		try {

			if(mapFeeAdjustmentDetails.get("IsFeeAdjustMentScreenAvailable") != null){
				if(mapFeeAdjustmentDetails.get("IsFeeAdjustMentScreenAvailable").equalsIgnoreCase("Yes")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4/p[contains(normalize-space(),'Fee Adjustment')]"), Constants.lTimeOut);
					if(!bStatus){
						Messages.errorMsg = "Fee Adjustment Screen is not available";
						return false;
					}
					if(mapFeeAdjustmentDetails.get("LegalEntitiesToUpdateFee") != null){
						List<String> aLegalEntiyList = Arrays.asList(mapFeeAdjustmentDetails.get("LegalEntitiesToUpdateFee").split(":"));
						List<String> aFeeTypeList = Arrays.asList(mapFeeAdjustmentDetails.get("FeeTypeForTheLegalEntity").split(":"));
						List<String> aInvestorAccountList = Arrays.asList(mapFeeAdjustmentDetails.get("AccountIdsToUpdateFee").split(":"));
						List<String> aClassList = Arrays.asList(mapFeeAdjustmentDetails.get("ClassNameForTheGivenAccount").split(":"));
						List<String> aSeriesList = Arrays.asList(mapFeeAdjustmentDetails.get("SeriesNameForTheGivenAccount").split(":"));
						List<String> aFeeToEnterList = Arrays.asList(mapFeeAdjustmentDetails.get("FeeToEnterForTheInvestor").split(":"));

						if(aLegalEntiyList.size() != aFeeTypeList.size()){
							Messages.errorMsg = "Number of Legal Entity  given is not matching with the Fee Type for the Legal Entity Given";
							return false;
						}
						for(int i = 0 ;i<aLegalEntiyList.size(); i++){
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aLegalEntiyList.get(i), By.xpath("//div[@id='s2id_legalEntityOpt']//span[contains(@id,'select2')]"));
							if(!bStatus){
								Messages.errorMsg = aLegalEntiyList.get(i)+" Legal Entity From Drop down Not Selected in Fee Adjustment Screen "+Messages.errorMsg;
								return false;
							}
							bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(aFeeTypeList.get(i), By.xpath("//div[@id='s2id_feeType']//span[contains(@id,'select')]"));
							if(!bStatus){
								Messages.errorMsg = "Fee Type "+aFeeTypeList.get(i)+" Cannot be Selected form the Drop down "+Messages.errorMsg;
								return false;
							}

							bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'fetchInvestor') and contains(normalize-space(),'Search')]"));
							if(!bStatus){
								Messages.errorMsg = "Cannot click on Search button for the Legal Entity "+aLegalEntiyList.get(i)+" and Fee Type "+aFeeTypeList.get(i)+"";
								return false;
							}

							bStatus = EnterGridDetailsAndUpdateFee(aClassList,aSeriesList,aInvestorAccountList,aFeeToEnterList,i);
							if(!bStatus){
								return false;
							}

							bStatus = Elements.click(Global.driver, By.xpath("//h4/p"));

						}
					}

					bStatus = doClickOnAllocationNavigationbuttons("Next");
					if(!bStatus){
						Messages.errorMsg = "[ ERROR : Next Button Cannot be clicked on 'Fee Adjustment' Screen.]\n";
						return false;
					}

				}
				if(mapFeeAdjustmentDetails.get("IsFeeAdjustMentScreenAvailable").equalsIgnoreCase("No")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//h4/p[contains(normalize-space(),'Fee Adjustment')]"), 2);
					if(bStatus){
						Messages.errorMsg = "Fee Adjustment Screen is Available when Expected is No";
						return false;
					}					
				}
			}


			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean EnterGridDetailsAndUpdateFee(List<String> aClassList, List<String> aSeriesList,List<String> aInvestorAccountList, List<String> aFeeToEnterList,int i) {
		try {

			List<String> aClassIndvList = Arrays.asList(aClassList.get(i).split(","));
			List<String> aSeriesIndvList = Arrays.asList(aSeriesList.get(i).split(","));
			List<String> aAccountIndvList = Arrays.asList(aInvestorAccountList.get(i).split(","));
			List<String> aFeeToEnterIndvList = Arrays.asList(aFeeToEnterList.get(i).split(","));
			for(int index = 0 ; index<aClassIndvList.size() ; index++){
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
				/*	String rowLocator = "//div[contains(@id,'contenttabletreeGridManualGrid')]";
					String classLocator = "//div[contains(@id,'row')]//div[normalize-space()=\""+aClassIndvList.get(index)+"\"]";
					String seriesLocator = "//following-sibling::div[normalize-space()=\""+aSeriesIndvList.get(index)+"\"]";
					String accountLocator = "//following-sibling::div[normalize-space()= \""+aAccountIndvList.get(index)+"\"]";
					String checkBoxLocator = rowLocator+classLocator+seriesLocator+accountLocator+"//preceding-sibling::div//span";
					String feeLocator = rowLocator+classLocator+seriesLocator+accountLocator+"//following-sibling::div/input";*/

				if(!aClassIndvList.get(index).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//div[contains(@id,'filterrow')]//div//div[2]//input[@type='textarea']"), aClassIndvList.get(index));
					if(!bStatus){
						Messages.errorMsg = "Class Name "+aClassIndvList.get(index)+"cannot be Entered in filter box";
						return false;
					}
				}
				if(!aSeriesIndvList.get(index).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//div[contains(@id,'filterrow')]//div//div[3]//input[@type='textarea']"), aSeriesIndvList.get(index));
					if(!bStatus){
						Messages.errorMsg = "Series Name "+aSeriesIndvList.get(index)+"cannot be Entered in filter box";
						return false;
					}
				}
				if(!aAccountIndvList.get(index).equalsIgnoreCase("None")){
					bStatus = Elements.enterText(Global.driver, By.xpath("//div[contains(@id,'filterrow')]//div//div[5]//input[@type='textarea']"), aAccountIndvList.get(index));
					if(!bStatus){
						Messages.errorMsg = "Account Number "+aAccountIndvList.get(index)+"cannot be Entered in filter box";
						return false;
					}
				}
				Thread.sleep(2000);
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[contains(@id,'contenttabletreeGridManualGrid')]/div[1]//div[contains(@class,'state-pressed')]//span"), 2);
				if(!bStatus){
					bStatus = Elements.click(Global.driver, By.xpath("//div[contains(@id,'contenttabletreeGridManualGrid')]/div[1]//span//parent::div"));
					if(!bStatus){
						Messages.errorMsg = "Cannot Select the check box for the Investor Account ID "+aAccountIndvList.get(index);
						return false;
					}
				}


				//Enter Amount into Fee Text box
				if(!aFeeToEnterIndvList.get(index).equalsIgnoreCase("None")){
					bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@id,'contenttabletreeGridManualGrid')]/div[1]//div[7]/div"), 2);
					if(bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//div[contains(@id,'contenttabletreeGridManualGrid')]/div[1]//div[7]/div"));
						if(!bStatus){
							Messages.errorMsg = "Fee Amount "+aFeeToEnterIndvList.get(index)+" Cannot be Entered for the Investor Account "+aAccountIndvList.get(index)+" in Fee text box";
							return false;
						}
					}

					bStatus = Elements.enterText(Global.driver, By.xpath("//div[contains(@id,'contenttabletreeGridManualGrid')]/div//div/input"), aFeeToEnterIndvList.get(index));
					if(!bStatus){
						Messages.errorMsg = "Fee Amount "+aFeeToEnterIndvList.get(index)+" Cannot be Entered for the Investor Account "+aAccountIndvList.get(index)+" in Fee text box";
						return false;
					}
				}
			}
			bStatus = Elements.click(Global.driver , By.xpath("//button[contains(@onclick,'saveFeeAdjustment') and contains(normalize-space(),'Update Fee')]"));
			if(!bStatus){
				Messages.errorMsg = "Update Fee Button cannot be Clicked";
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@class='alert alert-success' and contains(normalize-space(),'Fee Adjustment Save')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Fee Adjustment Saved Successfull Message is Not Visible";
				return false;
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
}
