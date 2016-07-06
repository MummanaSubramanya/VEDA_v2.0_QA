package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.james.mime4j.message.Message;
import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Verify;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;


public class FeeDistributionAppFunction {
	static boolean bStatus;

	public static boolean addFeeDistribution(Map<String, String> mapFeeDistribution){
		try{
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(text(),'Client Name')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = " Fee Distribution Master page is not displayed";
				return false;
			}
			//Select  Client Name From drop down

			if (mapFeeDistribution.get("Client Name") != null) {				
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", mapFeeDistribution.get("Client Name"),2);
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Client Name : "+mapFeeDistribution.get("Client Name")+" from drop down. client name wasn't visible.";
					return false;
				}
			}
			//Select FundFamily From drop down
			if (mapFeeDistribution.get("Fund Family Name") != null) {				
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", mapFeeDistribution.get("Fund Family Name"),2);

				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Fund Family Name : "+mapFeeDistribution.get("Fund Family Name")+" from drop down. Fund Family Name wasn't visible.";
					return false;
				}
			}
			//Select Legal Entity from drop down
			if (mapFeeDistribution.get("Legal Entity Name") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name", mapFeeDistribution.get("Legal Entity Name"), 2);
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Legal Entity Name : "+mapFeeDistribution.get("Legal Entity Name")+" from drop down. Legal Entity Name wasn't visible.";
					return false;
				}
			}
			//Select Fee Type
			if (mapFeeDistribution.get("Fee Type") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fee Type", mapFeeDistribution.get("Fee Type"), 2);
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select Fee Type Name : "+mapFeeDistribution.get("Fee Type")+" from drop down. Fee Type Name wasn't visible.";
					return false;
				}
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"));
			if (!bStatus) {
				Messages.errorMsg = " ERROR : General Partner form wasn't displayed after Selecting Fee Type  ";
				return false;
			}
			//Add General Partner Details
			bStatus = FeeDistributionAppFunction.addGenaeralPartner(mapFeeDistribution);
			if(!bStatus){
				Messages.errorMsg = " ERROR : General Partner are not Enter for  "+mapFeeDistribution.get("Fee Type") +" ";
				return false;
			}
			NewUICommonFunctions.scrollToView(By.xpath("//a[normalize-space()='Home']"));
			if(mapFeeDistribution.get("OperationType")!=null){
				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Fee Distribution", mapFeeDistribution.get("OperationType"));
				if(!bStatus){
					return false;
				}
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	public static boolean addGenaeralPartner(Map<String,String>mapGeneralPartner){
		try{
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "ERROR : General Partner form wasn't displayed ";
				return false;
			}

			//Select GP Account
			if(mapGeneralPartner.get("GP Account") != null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("GP Account", mapGeneralPartner.get("GP Account"), 2);
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to select GP Account : "+mapGeneralPartner.get("GP Account")+" from drop down. GP Account wasn't visible.";
					return false;
				}
			}

			//Enter the Fee Percentage
			if(mapGeneralPartner.get("Fee Percentage") != null){
				bStatus = Elements.enterText(Global.driver, By.id("feeDistribution0.feeDistributionDetails0.percentage"), mapGeneralPartner.get("Fee Percentage"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to Enter Fee Percentage for General Partner";
					return false;
				}
			}

			//Enter From Date and to Date
			if(mapGeneralPartner.get("DateFrom") != null){

				bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("fromDateRange_0"), mapGeneralPartner.get("DateFrom"));
				//bStatus = Elements.enterText(Global.driver, By.id("fromDateRange_0"), mapGeneralPartner.get("Date From"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to Enter From Date for General Partner";
					return false;
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"));
			Thread.sleep(2);
			if(mapGeneralPartner.get("DateTo") != null){
				bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("toDateRange_0"), mapGeneralPartner.get("DateTo"));
				//bStatus = Elements.enterText(Global.driver, By.id("toDateRange_0"), mapGeneralPartner.get("Date To"));
				if (!bStatus) {
					Messages.errorMsg = " ERROR : Unable to Enter To Date  for General Partner";
					return false;
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"));

			//Select Calculate Fee Radio button
			if(mapGeneralPartner.get("Calculate Fee") !=null){
				if(mapGeneralPartner.get("Calculate Fee").equalsIgnoreCase("Yes")){
					bStatus= Elements.click(Global.driver,By.xpath("//div[contains(@id,'isFeeCalculation1')]"));
					if(!bStatus){
						Messages.errorMsg = "ERROR :  Calculate Fee Radio button Yes is not Clicked ";
						return false;
					}
				}
				if(mapGeneralPartner.get("Calculate Fee").equalsIgnoreCase("No")){
					bStatus= Elements.click(Global.driver, By.xpath("//div[contains(@id,'isFeeCalculation2')]"));
					if(!bStatus){
						Messages.errorMsg = "ERROR :  Calculate Fee Radio button No is not Clicked ";
						return false;
					}
				}
			}

			//Select Provision For Deferment Radio button
			if(mapGeneralPartner.get("Provision for Deferment")!=null){
				if(mapGeneralPartner.get("Provision for Deferment").equalsIgnoreCase("Yes")){
					bStatus= Elements.click(Global.driver,By.xpath("//div[contains(@id,'isDeferment_provisionForDefermentYes')]"));
					if(!bStatus){
						Messages.errorMsg = "ERROR :  Provision for Deferment Radio button Yes is not Clicked ";
						return false;
					}
					// Add Deferment Details
					if(mapGeneralPartner.get("DefermentAllowed") != null){
						bStatus = FeeDistributionAppFunction.removeDistribution();
						if(!bStatus){
							return false;
						}
						bStatus = FeeDistributionAppFunction.removeDefermnetDates();
						if(!bStatus){
							return false;
						}
						bStatus = FeeDistributionAppFunction.addDefermentDetails(mapGeneralPartner);
						if(!bStatus){
							Messages.errorMsg = "ERROR : Deferment Details are not Entered";
						}
					}
				}
				if(mapGeneralPartner.get("Provision for Deferment").equalsIgnoreCase("No")){
					bStatus= Elements.click(Global.driver, By.xpath("//div[contains(@id,'isDeferment_provisionForDefermentNo')]"));
					if(!bStatus){
						Messages.errorMsg = "ERROR : Provision for Deferment  button No is not Clicked ";
						return false;
					}
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"));


			return true;	
		}
		catch(Exception e){
			return false;
		}
	}

	public static boolean addDefermentDetails(Map<String,String> mapDefermentDetails){
		try{
			String sDefermentAllowed = mapDefermentDetails.get("DefermentAllowed");
			List<String> aDefermentAllowed= Arrays.asList(sDefermentAllowed.split(","));
			String sSidePocketAllowed = mapDefermentDetails.get("Side PocketAllowed");
			List<String> aSidePocketAllowed = Arrays.asList(sSidePocketAllowed.split(","));
			String sDateFrom = mapDefermentDetails.get("Date From");
			List<String> aDateFrom= Arrays.asList(sDateFrom.split(","));
			String sDateTo = mapDefermentDetails.get("Date To");
			List<String> aDateTo= Arrays.asList(sDateTo.split(","));
			String sDefermentPL =mapDefermentDetails.get("Deferment Participient PL");
			List<String> aDefermentPL = Arrays.asList(sDefermentPL.split(","));


			for(int i=0;i<aDefermentAllowed.size();i++){

				// Enter the Deferment Percentage Allowed
				if(mapDefermentDetails.get("DefermentAllowed")!=null){
					if(!aDefermentAllowed.get(i).equalsIgnoreCase("None")){
						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercent']"), aDefermentAllowed.get(i));
						if(!bStatus){
							Messages.errorMsg = " ERROR : Deferment % is Not Entered";
							return false;
						}
					}			
				}

				//Enter the Side pocket Percentage Allowed

				if(mapDefermentDetails.get("Side PocketAllowed")!= null){
					if(!aSidePocketAllowed.get(i).equalsIgnoreCase("None")){

						bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".sidePocketPercent']"), aSidePocketAllowed.get(i));
						if(!bStatus){
							Messages.errorMsg = " ERROR : Side Pocket  % is Not Entered";
							return false;
						}
					}
				}

				//Enter the From Date
				if(mapDefermentDetails.get("Date From") != null){
					if(!aDateFrom.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".startDate']"), aDateFrom.get(i));
						//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".startDate']"), aDateFrom.get(i));
						if(!bStatus){
							Messages.errorMsg = " ERROR : From Date is Not Entered";
							return false;
						}

					}
				}

				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"));
				Thread.sleep(2);
				//Enter TO Date	
				if(mapDefermentDetails.get("Date To") != null){
					if(!aDateTo.get(i).equalsIgnoreCase("None")){
						bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".endDate']"), aDateTo.get(i));
						//bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".endDate']"), aDateTo.get(i));
						if(!bStatus){
							Messages.errorMsg = " ERROR : From Date is Not Entered";
							return false;
						}

					}
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"));
				// Select Deferment P & L Radio buttons
				if(mapDefermentDetails.get("Deferment Participient PL") != null){
					if(!aDefermentPL.get(i).equalsIgnoreCase("None")){
						if(aDefermentPL.get(i).equalsIgnoreCase("Yes")){
							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings0.isDefermentPnl1']"));
							if(!bStatus){
								bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-uniform-feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings0.isDefermentPnl1']"));
								if(!bStatus){
									Messages.errorMsg = " ERROR : Deferment Participient P&L YES radio button not selected ";
									return false;
								}
							}

							if(mapDefermentDetails.get("FromDate") != null  && mapDefermentDetails.get("ToDate")!= null ){
								bStatus = FeeDistributionAppFunction.addMoreDefermentDates(mapDefermentDetails, i);
								if(!bStatus){
									Messages.errorMsg = "Deferment Participient PL Dates Not Entered";
									return false;
								}

							}

						}
						if(aDefermentPL.get(i).equalsIgnoreCase("No")){
							bStatus = Elements.click(Global.driver, By.xpath("//div[@id='uniform-feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings0.isDefermentPnl2']"));
							if(!bStatus){
								Messages.errorMsg = " ERROR : Deferment Participient P&L NO radio button not selected ";
								return false;
							}

						}
					}
				}


				// Click on Add More Deferment Button
				if(i<aDefermentAllowed.size()-1){
					bStatus = Elements.click(Global.driver, By.xpath("//button[contains(@onclick,'addMoreDeferment')]"));
					if(!bStatus){
						Messages.errorMsg = "Add More Deferment Button Not Clicked ";
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

	public static boolean addMoreDefermentDates(Map<String,String>mapDefermentDates ,int i){
		try{
			String sFromDate = mapDefermentDates.get("FromDate");
			List<String> aFromDate= Arrays.asList(sFromDate.split(":"));
			String sToDate = mapDefermentDates.get("ToDate");
			List<String> aToDate= Arrays.asList(sToDate.split(":"));
			List<String> arrIndidualFromDate = Arrays.asList(aFromDate.get(i).split(","));
			List<String> arrIndidualToDate = Arrays.asList(aToDate.get(i).split(","));

			for(int k=0;k<arrIndidualFromDate.size();k++){
				if(!arrIndidualFromDate.get(k).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings"+k+".fromDate']"), arrIndidualFromDate.get(k));
					//Elements.enterText(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings"+k+".fromDate']"), arrIndidualFromDate.get(k));
				}
				Thread.sleep(2);
				if(!arrIndidualToDate.get(k).equalsIgnoreCase("None")){
					bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings"+k+".toDate']"), arrIndidualToDate.get(k));
					//Elements.enterText(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings"+k+".toDate']"), arrIndidualToDate.get(k));
				}				
				if(k<arrIndidualFromDate.size()-1){
					String objAddMoreDate = "//button[@onclick=\"addDefermentPnL('0','0','"+k+"')\"]";

					bStatus = Elements.click(Global.driver, By.xpath(objAddMoreDate));
					if(!bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//button[@name='addMoreFeeDefermentDetails']"));
						if(!bStatus){
							Messages.appErrorMsg = "Add more Deferment P&L is not clicked ";
						}
					}
				}
			}
			return true;	
		}
		catch(Exception e){
			return false;
		}
	}

	public static boolean modifyReturnFeeDistributionDetails(Map<String, String> mapDetialsToModify) {
		try {
			bStatus = NewUICommonFunctions.performOperationsOnTableWithTransactionID(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW, mapDetialsToModify.get("FeeDistributionID"), "");					
			if(!bStatus){
				return false;
			}
			bStatus = FeeDistributionAppFunction.addFeeDistribution(mapDetialsToModify);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bStatus;
	}

	public static boolean removeDistribution(){
		try {	
			int xpathCount = 1;
			xpathCount = Global.driver.findElements(By.xpath("//div[@id='defermentPnl_0_0_1']/following::button/em[@class='fa fa-close']")).size();

			for(int i=xpathCount ; i>=1 ; i--){
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='defermentPnl_0_0_"+i+"']/following::button/em[@class='fa fa-close']"));
				if(!bStatus){
					Messages.errorMsg = "Deferment are Not Cleared";
					return false;
				}
			}
			xpathCount = Global.driver.findElements(By.xpath("//div[@id='defermentPnl_0_0_1']/following::button/em[@class='fa fa-close']")).size();
			if(xpathCount>0){
				Messages.errorMsg = "Deferment are  Not Removed";
				return false;
			}

			return true;


		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean removeDefermnetDates(){
		try {	
			int xpathCount = 0;
			xpathCount = Global.driver.findElements(By.xpath("//div[@id='defermentPnlStyle_0_0_0_0']/following::button/em[@class='fa fa-close']")).size();

			for(int i=xpathCount-1; i>=0 ; i--){
				bStatus = NewUICommonFunctions.jsClick(By.xpath("//div[@id='defermentPnlStyle_0_0_0_"+i+"']/following::button/em[@class='fa fa-close']"));
				if(!bStatus){
					Messages.errorMsg = "Date are Not Cleared";
					return false;
				}
			}
			xpathCount = Global.driver.findElements(By.xpath("//div[@id='defermentPnlStyle_0_0_0_0']/following::button/em[@class='fa fa-close']")).size();
			if(xpathCount>0){
				Messages.errorMsg = "Dates are  Not Removed";
				return false;
			}

			return true;


		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean addMorePercentages(Map<String,String>mapAddmorePercentage){
		try{
			if(mapAddmorePercentage.get("Fee Percentage")!= null){

				int sPercentage = Elements.getXpathCount(Global.driver, By.xpath("//label[normalize-space(text())='Fee Percentage']"));
				for(int l=0;l< sPercentage;l++){
					bStatus = Elements.enterText(Global.driver, By.xpath("//label[normalize-space(text())='Fee Percentage']/following::div/input[@id='feeDistribution"+l+".feeDistributionDetails0.percentage']"), mapAddmorePercentage.get("Fee Percentage"));
					if(!bStatus){
						Messages.errorMsg = " Fee Percentage is Not Entered";
					}
					Thread.sleep(1);
				}
			}


			return true;	
		}
		catch(Exception e){
			return false;
		}
	}

	public static boolean addmoreInvestorFD(Map<String,String>mapAddmoreInvestorFD){
		try{
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(text(),'Client Name')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ERROR : Fee Distribution Master page is not displayed ] \n";
				return false;
			}

			//Select  Client Name From drop down

			if (mapAddmoreInvestorFD.get("Client Name") != null) {				
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name", mapAddmoreInvestorFD.get("Client Name"),2);
				if (!bStatus) {
					Messages.errorMsg = " [ERROR : Unable to select Client Name : "+mapAddmoreInvestorFD.get("Client Name")+" from drop down. client name wasn't visible.]" ;
					return false;
				}
			}

			//Select FundFamily From drop down
			if (mapAddmoreInvestorFD.get("Fund Family Name") != null) {				
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name", mapAddmoreInvestorFD.get("Fund Family Name"),2);

				if (!bStatus) {
					Messages.errorMsg = " [ERROR : Unable to select Fund Family Name : "+mapAddmoreInvestorFD.get("Fund Family Name")+" from drop down. Fund Family Name wasn't visible.]";
					return false;
				}
			}

			//Select Legal Entity from drop down
			if (mapAddmoreInvestorFD.get("Legal Entity Name") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name", mapAddmoreInvestorFD.get("Legal Entity Name"), 2);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to select Legal Entity Name : "+mapAddmoreInvestorFD.get("Legal Entity Name")+" from drop down. Legal Entity Name wasn't visible.]";
					return false;
				}
			}

			//Select Fee Type
			if (mapAddmoreInvestorFD.get("Fee Type") != null) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fee Type", mapAddmoreInvestorFD.get("Fee Type"), 2);
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to select Fee Type Name : "+mapAddmoreInvestorFD.get("Fee Type")+" from drop down. Fee Type Name wasn't visible.]";
					return false;
				}
			}

			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : General Partner form wasn't displayed after Selecting Fee Type ] ";
				return false;
			}	


			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ERROR : General Partner form wasn't displayed ]";
				return false;
			}

			//Select GP Account
			if(mapAddmoreInvestorFD.get("GP Account") != null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("GP Account", mapAddmoreInvestorFD.get("GP Account"), 2);
				if (!bStatus) {
					Messages.errorMsg = " [ERROR : Unable to select GP Account : "+mapAddmoreInvestorFD.get("GP Account")+" from drop down. GP Account wasn't visible.]";
					return false;
				}
			}

			//Enter the Fee Percentage
			if(mapAddmoreInvestorFD.get("Fee Percentage") != null){
				bStatus = Elements.enterText(Global.driver, By.id("feeDistribution0.feeDistributionDetails0.percentage"), mapAddmoreInvestorFD.get("Fee Percentage"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Enter Fee Percentage for General Partner ]";
					return false;
				}
			}

			//Enter From Date and to Date
			if(mapAddmoreInvestorFD.get("DateFrom") != null){

				bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("fromDateRange_0"), mapAddmoreInvestorFD.get("DateFrom"));
				//bStatus = Elements.enterText(Global.driver, By.id("fromDateRange_0"), mapGeneralPartner.get("Date From"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Enter From Date for General Partner ]";
					return false;
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"));
			Thread.sleep(2);
			if(mapAddmoreInvestorFD.get("DateTo") != null){
				bStatus = NewUICommonFunctions.typeCharactersIntoTextBox(By.id("toDateRange_0"), mapAddmoreInvestorFD.get("DateTo"));
				//bStatus = Elements.enterText(Global.driver, By.id("toDateRange_0"), mapGeneralPartner.get("Date To"));
				if (!bStatus) {
					Messages.errorMsg = " [ERROR : Unable to Enter To Date  for General Partner ]";
					return false;
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//label[contains(text(),'General Partner')]"));

			//Select Calculate Fee Radio button
			if(mapAddmoreInvestorFD.get("Calculate Fee") !=null){
				if(mapAddmoreInvestorFD.get("Calculate Fee").equalsIgnoreCase("Yes")){
					bStatus= Elements.click(Global.driver,By.xpath("//div[contains(@id,'isFeeCalculation1')]"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR :  Calculate Fee Radio button Yes is not Clicked ]";
						return false;
					}
				}
				if(mapAddmoreInvestorFD.get("Calculate Fee").equalsIgnoreCase("No")){
					bStatus= Elements.click(Global.driver, By.xpath("//div[contains(@id,'isFeeCalculation2')]"));
					if(!bStatus){
						Messages.errorMsg = "[ ERROR :  Calculate Fee Radio button No is not Clicked ]";
						return false;
					}
				}
			}

			bStatus = FeeDistributionAppFunction.addMorePercentages(mapAddmoreInvestorFD);
			if(!bStatus){
				Messages.appErrorMsg = "[ ERROR Percentage is not Entered ]";
			}


			return true;
		}
		catch(Exception e){
			return false;
		}
	}

	public static boolean doVerifyFeeDistributioneOnEditScreen(Map<String , String> mapFeeDistributionDetails){
		boolean bValidateStatus = true;
		try {			
			String appendMsg = "";

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//label[contains(text(),'Client Name')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Fee Distribution label page  is Not Visible] \n";
				return false;
			}

			//Verify Client Name
			if(mapFeeDistributionDetails.get("Client Name") != null)
			{
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Client Name", mapFeeDistributionDetails.get("Client Name"));
				if(!bStatus){
					appendMsg = "[ERROR : Client Name is Not Matched with Expected  "+mapFeeDistributionDetails.get("Client Name")+" ]";
					bValidateStatus = false;
				}
			}

			//Verify the Fund Family Name
			if(mapFeeDistributionDetails.get("Fund Family Name") != null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Fund Family Name", mapFeeDistributionDetails.get("Fund Family Name"));
				if(!bStatus){
					appendMsg = appendMsg+"[ERROR : Fund Family Name is not matched with Expected "+mapFeeDistributionDetails.get("Fund Family Name")+" ] \n";
					bValidateStatus = false;
				}
			}

			//Verify the Legal Entity Name
			if(mapFeeDistributionDetails.get("Legal Entity Name") != null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Legal Entity Name", mapFeeDistributionDetails.get("Legal Entity Name"));
				if(!bStatus){
					appendMsg = appendMsg+"[ ERROR : Legal Entity Name is not Matched with Expected "+mapFeeDistributionDetails.get("Legal Entity Name")+" ] \n";
					bValidateStatus = false;
				}
			}

			//Verify the General Parter details

			//verify GP Account
			if(mapFeeDistributionDetails.get("GP Account")!=null){
				bStatus =NewUICommonFunctions.verifyTextInDropDown("GP Account", mapFeeDistributionDetails.get("GP Account"));
				if(!bStatus){
					appendMsg =appendMsg+"[Error : GP Account is not Matched with Expected "+mapFeeDistributionDetails.get("GP Account")+" ] \n";
					bValidateStatus = false;
				}
			}

			//Verify Fee Percentage

			if(mapFeeDistributionDetails.get("Fee Percentage") !=null ){
				String sValue= Elements.getElementAttribute(Global.driver, By.id("feeDistribution0.feeDistributionDetails0.percentage"), "value");
				if (sValue == null || sValue.trim().equalsIgnoreCase("") || Float.parseFloat(sValue.trim()) != Float.parseFloat(mapFeeDistributionDetails.get("Fee Percentage"))) {
					appendMsg =appendMsg+"[ERROR : Actual Value : "+sValue+" for 'Fee Percentage' is not matched with Expected value : "+mapFeeDistributionDetails.get("Fee Percentage")+" ] \n";
					bValidateStatus = false;
				}
			}

			//Verify From Date  and To date in General Details
			if(mapFeeDistributionDetails.get("DateFrom") != null){

				String sValue= Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='fromDateRange_0']"), "value");
				if (sValue == null || sValue.trim().equalsIgnoreCase("") ||!sValue.equalsIgnoreCase(mapFeeDistributionDetails.get("DateFrom")))
				{
					appendMsg =appendMsg+"[ ERROR : 'Date From' is Not Matched with Expected "+mapFeeDistributionDetails.get("DateFrom") +"] \n";
					bValidateStatus = false;
				}
			}
			if(mapFeeDistributionDetails.get("DateTo")!=null){

				String sValue= Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='toDateRange_0']"), "value");
				if (sValue == null || sValue.trim().equalsIgnoreCase("") ||!sValue.equalsIgnoreCase( mapFeeDistributionDetails.get("DateTo")))
				{
					appendMsg =appendMsg+"[ ERRor : 'Date To'  is Not Matched with Expected "+mapFeeDistributionDetails.get("DateTo")+" ] \n";
					bValidateStatus = false;
				}
			}

			//Verify  Calculate Fee Details Radio button
			if(mapFeeDistributionDetails.get("Calculate Fee") != null){
				bStatus = NewUICommonFunctions.verifySelectedRadioButton("Calculate Fee", mapFeeDistributionDetails.get("Calculate Fee"));
				if(!bStatus){
					appendMsg =appendMsg+"[ERROR : Calculate Fee Radio button is Not checked as EXpected "+mapFeeDistributionDetails.get("Calculate Fee")+" ] \n";
					bValidateStatus = false;
				}
			}

			//Verify Provision Deferment radio button
			if(mapFeeDistributionDetails.get("Provision for Deferment")!=null){
				bStatus = NewUICommonFunctions.verifySelectedRadioButton("Provision for Deferment", mapFeeDistributionDetails.get("Provision for Deferment"));
				if(!bStatus){
					appendMsg =appendMsg+"[ERROR : Provision for Defermnet is Not Checked for Expected "+mapFeeDistributionDetails.get("Provision for Deferment")+"] \n";
					bValidateStatus = false;
				}

				//Verify more Deferment Details
				if(mapFeeDistributionDetails.get("Provision for Deferment").equalsIgnoreCase("Yes")){

					bStatus = FeeDistributionAppFunction.doVerifyMoreDefermentDetails(mapFeeDistributionDetails);
					if(!bStatus){
						appendMsg =appendMsg+"["+Messages.errorMsg+" ] \n";
						bValidateStatus = false;
					}
				}
			}
			Messages.errorMsg = appendMsg;
			return bValidateStatus;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyMoreDefermentDetails(Map<String,String>mapVerifyMoreDefermentDetails){
		try{
			String appendMsg = "";
			boolean bValidateStatus = true;

			String sDefermentAllowed = mapVerifyMoreDefermentDetails.get("DefermentAllowed");
			List<String> aDefermentAllowed= Arrays.asList(sDefermentAllowed.split(","));
			String sSidePocketAllowed = mapVerifyMoreDefermentDetails.get("Side PocketAllowed");
			List<String> aSidePocketAllowed = Arrays.asList(sSidePocketAllowed.split(","));
			String sDateFrom = mapVerifyMoreDefermentDetails.get("Date From");
			List<String> aDateFrom= Arrays.asList(sDateFrom.split(","));
			String sDateTo = mapVerifyMoreDefermentDetails.get("Date To");
			List<String> aDateTo= Arrays.asList(sDateTo.split(","));
			String sDefermentPL =mapVerifyMoreDefermentDetails.get("Deferment Participient PL");
			List<String> aDefermentPL = Arrays.asList(sDefermentPL.split(","));

			int x = Elements.getXpathCount(Global.driver, By.xpath("//div[contains(@id,'defermentStyle')]/div"));
			for(int i=0 ; i<x ;i++){

				//Verify Deferment % Allowed
				if(mapVerifyMoreDefermentDetails.get("DefermentAllowed")!=null){
					if(!aDefermentAllowed.get(i).equalsIgnoreCase("None")){

						String sValue =Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercent']"), "value");
						if (sValue == null || sValue.trim().equalsIgnoreCase("") || Float.parseFloat(sValue.trim()) != Float.parseFloat(aDefermentAllowed.get(i))) {
							appendMsg =appendMsg+"[ERROR : Actual Value : "+sValue+" for 'Deferment % Allowed ' is not matched with Expected value : "+aDefermentAllowed.get(i)+" ] \n";
							bValidateStatus = false;
						}

					}
				}
				//Verify Side Pocket % Allowed
				if(mapVerifyMoreDefermentDetails.get("Side PocketAllowed")!=null){
					if(!aSidePocketAllowed.get(i).equalsIgnoreCase("None")){
						String sValue =Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".sidePocketPercent']"), "value");
						if (sValue == null || sValue.trim().equalsIgnoreCase("") || Float.parseFloat(sValue.trim()) != Float.parseFloat(aSidePocketAllowed.get(i))) {
							appendMsg =appendMsg+"[ERROR : Actual Value : "+sValue+" for 'Side Pocket % Allowed  ' is not matched with Expected value : "+aSidePocketAllowed.get(i)+" ] \n";
							bValidateStatus = false;
						}
					}
				}

				//Verify From date 
				if(mapVerifyMoreDefermentDetails.get("Date From") != null){
					if(!aDateFrom.get(i).equalsIgnoreCase("None")){

						String sValue= Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".startDate']"), "value");
						if (sValue == null || sValue.trim().equalsIgnoreCase("") ||!sValue.equalsIgnoreCase(aDateFrom.get(i))){
							appendMsg =  appendMsg+"[ERROR : Actual Date From is  "+sValue+"  not Matched with Expected "+aDateFrom.get(i)+" ] \n";
							bValidateStatus = false;
						}			
					}
				}

				//Verify To Date
				if(mapVerifyMoreDefermentDetails.get("Date To") !=null){
					if(!aDateTo.get(i).equalsIgnoreCase("None")){
						String sValue= Elements.getElementAttribute(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".endDate']"), "value");
						if (sValue == null || sValue.trim().equalsIgnoreCase("") ||!sValue.equalsIgnoreCase(aDateTo.get(i))){
							appendMsg=  appendMsg+"[ERROR : Actual 'Date To'  "+sValue+" is not Matched with Expected "+aDateTo.get(i)+" ] \n";
							bValidateStatus = false;
						}
					}
				}

				//Verify Deferment P & L Radio button

				if(mapVerifyMoreDefermentDetails.get("Deferment Participient PL")!=null){
					if(!aDefermentPL.get(i).equalsIgnoreCase("None")){
						if(aDefermentPL.get(i).equalsIgnoreCase("Yes")){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings0.isDefermentPnl1']/span[@class='checked']"));
							if(!bStatus){
								appendMsg =  appendMsg+"[ERROR :Deferment Participient PL Radio Button 'Yes' is Not Checked ] \n";
								bValidateStatus = false;
							}
							//Verify Deferment P & L Dates
							if(mapVerifyMoreDefermentDetails.get("FromDate") !=null && mapVerifyMoreDefermentDetails.get("FromDate") !=null ){
								bStatus = FeeDistributionAppFunction.doVerifyMoreDefermentDates(mapVerifyMoreDefermentDetails, i);
								if(!bStatus){
									appendMsg =  appendMsg+""+Messages.errorMsg+"";
									bValidateStatus = false;
								}
							}
						}
						if(aDefermentPL.get(i).equalsIgnoreCase("No")){
							bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//div[@id='uniform-feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings0.isDefermentPnl2']/span[@class='checked']"));
							if(!bStatus){
								appendMsg =  appendMsg+"[ERROR : Deferment Participient PL Radio Button 'NO' is Not Checked ] \n";
								bValidateStatus = false;
							}
						}
					}
				}
			}
			Messages.errorMsg = appendMsg;
			return bValidateStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyMoreDefermentDates(Map<String,String>mapVerifyDefermentMoreDtaes , int i){
		try{
			String appendMsg = "";
			boolean bValidateStatus = true;
			String sFromDate = mapVerifyDefermentMoreDtaes.get("FromDate");
			List<String> aFromDate= Arrays.asList(sFromDate.split(":"));
			String sToDate = mapVerifyDefermentMoreDtaes.get("ToDate");
			List<String> aToDate= Arrays.asList(sToDate.split(":"));
			List<String> arrIndidualFromDate = Arrays.asList(aFromDate.get(i).split(","));
			List<String> arrIndidualToDate = Arrays.asList(aToDate.get(i).split(","));

			for(int k=0;k<arrIndidualFromDate.size();k++){
				if(!arrIndidualFromDate.get(k).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings"+k+".fromDate' and contains(@value,'"+arrIndidualFromDate.get(k)+"')]"));
					if(!bStatus){
						appendMsg = appendMsg+ "[ ERROR : Deferment Participient PL 'From Date' is Not matched with Expected "+arrIndidualFromDate.get(k)+" ] \n";
						bValidateStatus = false;
					}
				}
				if(!arrIndidualToDate.get(k).equalsIgnoreCase("None")){
					bStatus = Verify.verifyElementVisible(Global.driver, By.xpath("//input[@id='feeDistribution0.feeDistributionDetails0.defermentPercentageDetails"+i+".defermentPercentageDetailsMappings"+k+".toDate' and contains(@value,'"+arrIndidualToDate.get(k)+"')]"));
					if(!bStatus){
						appendMsg =  appendMsg+"[ERROR : Deferment Participient PL 'To Date' is Not matched with Expected "+arrIndidualFromDate.get(k)+" ] \n";
						bValidateStatus = false;
					}
				}
			}
			Messages.errorMsg = appendMsg ;
			return bValidateStatus;
		}
		catch(Exception e){
			return false;
		}
	}

	/****************************** New Functions For Fee Distribution Through Investor *************************/

	public static boolean doAddNewGPorIMDetails(Map<String, String> mapFeeDistribution){
		try {
			String sInvestorsTestDataFilePath = Global.sInvestorTestDataFilePath;
			String sSheetName = "FeeDistributionData";
			if (InvestorMasterAppFunctions.isInvestorGeneralDetailsModifyFlag == true) {
				sInvestorsTestDataFilePath = Global.sInvestorModifyTestDataFilePath;
				sSheetName = "ModifyFeeDistributionData";
			}
			bStatus = doRemoveOrAddMoreGPorIMDetails("Remove");
			if (!bStatus) {
				return false;
			}
			String feeDistRemoveLocator = "//button[contains(@id,'_removeFeeDist_')]";
			bStatus = removeAllDetails(feeDistRemoveLocator,"Fee Distribution");
			if(!bStatus){
				return false;
			}

			String differRemoveButton = "//button[contains(@onclick,'removeDeferment(')]";
			bStatus = removeAllDetails(differRemoveButton,"Deferment");
			if(!bStatus){
				return false;
			}

			String differPandLRemoveButton = "//button[contains(@onclick,'removeDefermentPnL')]";
			bStatus = removeAllDetails(differPandLRemoveButton,"Deferment P&L");
			if(!bStatus){
				return false;
			}

			int feeDistrCount = Elements.getXpathCount(Global.driver, By.xpath("//button[contains(@id,'_removeFeeDist_')]"));
			for (int i = 0; i < feeDistrCount; i++) {
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@id,'_removeFeeDist_')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to click on the Remove Fee distributions left for GP/IM index : '0'.]\n";
					return false;
				}
			}
			if (mapFeeDistribution != null && !mapFeeDistribution.isEmpty() && mapFeeDistribution.get("GPorIMReferenceTC") != null && !mapFeeDistribution.get("GPorIMReferenceTC").equalsIgnoreCase("None")) {
				List<String> sTCListOfNoOfGPOrIMs = Arrays.asList(mapFeeDistribution.get("GPorIMReferenceTC").split(","));
				for (int gpOrIMIndex = 0; gpOrIMIndex < sTCListOfNoOfGPOrIMs.size(); gpOrIMIndex++) {
					if (gpOrIMIndex > 0) {
						bStatus = doRemoveOrAddMoreGPorIMDetails("Add");
						if (!bStatus) {
							return false;
						}
					}
					Map<String, String> mapGPorIMDetails = ExcelUtils.readDataABasedOnCellName(sInvestorsTestDataFilePath, sSheetName, sTCListOfNoOfGPOrIMs.get(gpOrIMIndex).trim());
					bStatus = doSelectFundDetailsForRespectiveGPorIM(mapGPorIMDetails, gpOrIMIndex);
					if (!bStatus) {
						return false;
					}				
					List<String> sFeePercentagesListOfEachDistribution = null;
					List<String> sFromDatesListOfEachDistribution = null;
					List<String> sToDatesListOfEachDistribution = null;
					if (mapGPorIMDetails.get("Fee Percentage") != null && mapGPorIMDetails.get("DateFrom") != null && mapGPorIMDetails.get("DateTo") != null) {
						sFeePercentagesListOfEachDistribution = Arrays.asList(mapGPorIMDetails.get("Fee Percentage").split(":"));
						sFromDatesListOfEachDistribution = Arrays.asList(mapGPorIMDetails.get("DateFrom").split(":"));
						sToDatesListOfEachDistribution = Arrays.asList(mapGPorIMDetails.get("DateTo").split(":"));
					}

					List<String> sProvisionForDiffermentList = Arrays.asList(mapGPorIMDetails.get("Provision for Deferment").split(":"));
					List<String> sCalculateFeeList = Arrays.asList(mapGPorIMDetails.get("Calculate Fee").split(":"));

					for (int DistributionIndex = 0; DistributionIndex < sProvisionForDiffermentList.size(); DistributionIndex++) {
						if (DistributionIndex > 0) {
							bStatus = doClickOnAddMoreFeeDistribution(gpOrIMIndex);
							if (!bStatus) {
								return false;
							}
						}						
						bStatus = doFillRespectiveIndexGPorIMFeeDistributionDetails(sFeePercentagesListOfEachDistribution, sFromDatesListOfEachDistribution, sToDatesListOfEachDistribution, gpOrIMIndex, DistributionIndex);
						if (!bStatus) {
							return false;
						}
						if (sProvisionForDiffermentList.get(DistributionIndex) != null){
							if(sProvisionForDiffermentList.get(DistributionIndex).equalsIgnoreCase("Yes")) {
								bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.defermentPercentageDetails0.isDeferment1')]//span"));
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Unable to click on 'Provision For Differement' => YES for GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' .]\n";
									return false;
								}
								List<String> sDefermentAllowedLists = Arrays.asList(mapGPorIMDetails.get("DefermentAllowed").split(":"));
								List<String> sSidePocketAllowedLists = Arrays.asList(mapGPorIMDetails.get("SidePocketAllowed").split(":"));
								List<String> sDefermentDateFromLists = Arrays.asList(mapGPorIMDetails.get("Deferment Date From").split(":"));
								List<String> sDefermentDateToLists = Arrays.asList(mapGPorIMDetails.get("Deferment Date To").split(":"));
								List<String> sDefermentPartPnLLists = Arrays.asList(mapGPorIMDetails.get("Deferment Participient PL").split(":"));

								List<String> sDefermentAllowedSubList = Arrays.asList(sDefermentAllowedLists.get(DistributionIndex).split(","));
								List<String> sSidePocketAllowedSubList = Arrays.asList(sSidePocketAllowedLists.get(DistributionIndex).split(","));
								List<String> sDefermentDateFromSubList = Arrays.asList(sDefermentDateFromLists.get(DistributionIndex).split(","));
								List<String> sDefermentDateToSubList = Arrays.asList(sDefermentDateToLists.get(DistributionIndex).split(","));
								List<String> sDefermentPartPnLSubList = Arrays.asList(sDefermentPartPnLLists.get(DistributionIndex).split(","));
								int iAddMoreDefermentButtonIndexCounter = 1;
								for (int DefermentIndex = 0; DefermentIndex < sDefermentAllowedSubList.size(); DefermentIndex++) {
									if (DefermentIndex > 0) {										
										bStatus = doClickAddMoreDefermentForRespectiveDistributionOfGPorIMIndex(gpOrIMIndex, DistributionIndex, iAddMoreDefermentButtonIndexCounter);
										if (!bStatus) {
											return false;
										}
										iAddMoreDefermentButtonIndexCounter++;
									}									
									bStatus = doFillDefermentDetailsOfDistributionWithRespectiveIndexes(mapGPorIMDetails, sDefermentAllowedSubList.get(DefermentIndex), sSidePocketAllowedSubList.get(DefermentIndex), sDefermentDateFromSubList.get(DefermentIndex), sDefermentDateToSubList.get(DefermentIndex), sDefermentPartPnLSubList.get(DefermentIndex), gpOrIMIndex, DistributionIndex, DefermentIndex);
									if (!bStatus) {
										return false;
									}
								}
							}
							if (sProvisionForDiffermentList.get(DistributionIndex).equalsIgnoreCase("No")) {
								bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.defermentPercentageDetails0.isDeferment2')]//span"));
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Unable to click on 'Provision For Differement' => NO for GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' .]\n";
									return false;
								}
							}
							if (sCalculateFeeList.get(DistributionIndex).equalsIgnoreCase("Yes")) {
								bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.isFeeCalculation1')]//span"));
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Unable to click on 'Calculate Fee' => NO for GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' .]\n";
									return false;
								}
							}
							if (sCalculateFeeList.get(DistributionIndex).equalsIgnoreCase("No")) {
								bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.isFeeCalculation2')]//span"));
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Unable to click on 'Calculate Fee' => NO for GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' .]\n";
									return false;
								}
							}
						}
					}
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Messages.errorMsg = Messages.errorMsg + "[ ERROR : Something went wrong it might be a TEST DATA issue please do cross check Fee Distribution Data given properly or not.]";
			return false;
		}
	}

	private static boolean removeAllDetails(String feeDistRemoveLocator,String Label) {
		try {

			int feeDistrCount = Elements.getXpathCount(Global.driver, By.xpath(feeDistRemoveLocator));
			if(feeDistrCount == 0){
				return true;
			}
			for (int i = 0; i < feeDistrCount; i++) {
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(feeDistRemoveLocator));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to click on the Remove Button of "+Label+".]\n";
					return false;
				}
			}

			int feeDistrCountAfterRemoving = Elements.getXpathCount(Global.driver, By.xpath(feeDistRemoveLocator));

			if(feeDistrCount != feeDistrCountAfterRemoving){
				Messages.errorMsg = Label+" Remove buttons are not functioning unable to remove";
				return false;
			}

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean doFillDefermentDetailsOfDistributionWithRespectiveIndexes(Map<String, String> mapGPorIMDetails, String sDefermentAllowedVal,	String sSidePocketAllowedVal,	String sDefermentDateFromVal,	String sDefermentDateToVal, String sDefermentPartPnLVal, int iGPorIMIndex, int iDistributionIndex, int iDefermentIndex) {
		try {
			if (sDefermentAllowedVal != null && !sDefermentAllowedVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercent']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to scroll on to the field 'Deferment % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					return false;
				}
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercent']"), sDefermentAllowedVal.trim());
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to enter value : '"+sDefermentAllowedVal.trim()+"', into the field 'Deferment % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					return false;
				}
			}
			if (sSidePocketAllowedVal != null && !sSidePocketAllowedVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".sidePocketPercent']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to scroll on to the field 'Side Pocket % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					return false;
				}
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".sidePocketPercent']"), sSidePocketAllowedVal.trim());
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to enter value : '"+sSidePocketAllowedVal.trim()+"', into the field 'Side Pocket % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					return false;
				}
			}
			if (sDefermentDateFromVal != null && !sDefermentDateFromVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_feeDistribution"+iDistributionIndex+"feeDistributionDetails0defermentPercentageDetails"+iDefermentIndex+"startDate']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to scroll on to the field 'Deferment Date From' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.doPickDateFromCalender(sDefermentDateFromVal.trim(), "//input[@id='gpList"+iGPorIMIndex+"_feeDistribution"+iDistributionIndex+"feeDistributionDetails0defermentPercentageDetails"+iDefermentIndex+"startDate']");
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to enter value : '"+sDefermentDateFromVal.trim()+"', into the field 'Deferment Date From' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			}
			if (sDefermentDateToVal != null && !sDefermentDateToVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_feeDistribution"+iDistributionIndex+"feeDistributionDetails0defermentPercentageDetails"+iDefermentIndex+"endDate']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to scroll on to the field 'Deferment Date To' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.doPickDateFromCalender(sDefermentDateToVal.trim(), "//input[@id='gpList"+iGPorIMIndex+"_feeDistribution"+iDistributionIndex+"feeDistributionDetails0defermentPercentageDetails"+iDefermentIndex+"endDate']");
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to enter value : '"+sDefermentDateToVal.trim()+"', into the field 'Deferment Date To' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//h4/p"));
			}
			if (sDefermentPartPnLVal != null && !sDefermentPartPnLVal.trim().equalsIgnoreCase("None")) {
				if (sDefermentPartPnLVal.trim().equalsIgnoreCase("Yes")) {
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercentageDetailsMappings0.isDefermentPnl1')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Wasn't able to click on 'Deferment Participient P&L' => YES button for the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' .]\n";
						return false;
					}
					if (mapGPorIMDetails != null && !mapGPorIMDetails.isEmpty() && mapGPorIMDetails.get("Participient FromDate") != null && mapGPorIMDetails.get("Participient ToDate") != null) {
						List<String> sGPorIMParticipientFromDateLists = Arrays.asList(mapGPorIMDetails.get("Participient FromDate").split(":"));
						List<String> sGPorIMParticipientToDateLists = Arrays.asList(mapGPorIMDetails.get("Participient ToDate").split(":"));

						List<String> sDistributionParticipientFromDateLists = Arrays.asList(sGPorIMParticipientFromDateLists.get(iDistributionIndex).split(";"));
						List<String> sDistributionParticipientToDateLists = Arrays.asList(sGPorIMParticipientToDateLists.get(iDistributionIndex).split(";"));

						List<String> sParticipientFromDateList = Arrays.asList(sDistributionParticipientFromDateLists.get(iDefermentIndex).split(","));
						List<String> sParticipientToDateList = Arrays.asList(sDistributionParticipientToDateLists.get(iDefermentIndex).split(","));

						for (int iParticipientIndex = 0; iParticipientIndex < sParticipientFromDateList.size(); iParticipientIndex++) {
							if (iParticipientIndex > 0) {										
								bStatus = doClickAddMoreDefermentParticipientPnLForRespectiveDistributionOfGPorIMIndex(iGPorIMIndex, iDistributionIndex, iDefermentIndex);
								if (!bStatus) {
									return false;
								}
							}
							bStatus = doFillDefermentPnLDatesOfRespectiveIndexes(sParticipientFromDateList.get(iParticipientIndex),sParticipientToDateList.get(iParticipientIndex), iGPorIMIndex, iDistributionIndex, iDefermentIndex, iParticipientIndex);
							if (!bStatus) {
								return false;
							}
						}						
					}					
				}
				if (sDefermentPartPnLVal.trim().equalsIgnoreCase("No")) {
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercentageDetailsMappings0.isDefermentPnl2')]"));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Wasn't able to click on 'Deferment Participient P&L' => NO button for the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' .]\n";
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

	public static boolean doFillDefermentPnLDatesOfRespectiveIndexes(String sDefPnLFromDate, String sDefPnLToDate, int iGPorIMIndex, int iDistributionIndex, int iDefermentIndex, int iDefermentPnLIndex) {
		try {
			if (sDefPnLFromDate != null && !sDefPnLFromDate.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLFromDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to scroll on to the 'From Date' field at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' .]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.doPickDateFromCalender(sDefPnLFromDate.trim(), "//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLFromDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']");
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to input Value : '"+sDefPnLFromDate.trim()+"' for the 'From Date' field at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' .]\n";
					return false;
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'Investor Master')]"));
			}
			if (sDefPnLToDate != null && !sDefPnLToDate.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLToDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to scroll on to the 'To Date' field at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' .]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.doPickDateFromCalender(sDefPnLToDate.trim(), "//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLToDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']");
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to input Value : '"+sDefPnLToDate.trim()+"' for the 'To Date' field at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' .]\n";
					return false;
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'Investor Master')]"));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doClickAddMoreDefermentParticipientPnLForRespectiveDistributionOfGPorIMIndex(int iGPorIMIndex, int iDistributionIndex, int iDefermentIndex) {
		try {
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//button[contains(@onclick,\"addDefermentPnL('"+iGPorIMIndex+"', '"+iDistributionIndex+"','0','"+iDefermentIndex+"')\")]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Scroll on to the 'Add More Deferment P&L' button for the respective Indexes of => GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,\"addDefermentPnL('"+iGPorIMIndex+"', '"+iDistributionIndex+"','0','"+iDefermentIndex+"')\")]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to click on 'Add More Deferment P&L' button for the respective Indexes of => GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doClickAddMoreDefermentForRespectiveDistributionOfGPorIMIndex(int gpOrIMIndex, int distributionIndex,	int iAddMoreDefermentButtonIndexCounter) {
		try {
			bStatus = NewUICommonFunctions.scrollToView(By.xpath("//button[contains(@onclick,\"addMoreDeferment('"+gpOrIMIndex+"', '"+distributionIndex+"','0','"+iAddMoreDefermentButtonIndexCounter+"')\")]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Scroll on to the 'Add More Deferment' button at GP/IM , Distribution Indexes : '"+gpOrIMIndex+"','"+distributionIndex+"' respectively for the Deferment iteration index : '"+iAddMoreDefermentButtonIndexCounter+"'.]\n";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//button[contains(@onclick,\"addMoreDeferment('"+gpOrIMIndex+"', '"+distributionIndex+"','0','"+iAddMoreDefermentButtonIndexCounter+"')\")]"));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Click on 'Add More Deferment' button at GP/IM , Distribution Indexes : '"+gpOrIMIndex+"','"+distributionIndex+"' respectively for the Deferment iteration index : '"+iAddMoreDefermentButtonIndexCounter+"'.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doSelectFundDetailsForRespectiveGPorIM(Map<String, String> mapGPorIMDetails, int indexOfGPorIM){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@id,'s2id_clientid_')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "[ ERROR : Fee Distribution Details are not displayed in Investor Tab when selected GP/IM option.]\n";
				return false;
			}
			//Select  Client Name From drop down
			if (mapGPorIMDetails.get("Client Name") != null && !mapGPorIMDetails.get("Client Name").equalsIgnoreCase("None")) {				
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGPorIMDetails.get("Client Name"), By.xpath("//div[@id='s2id_clientid_"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to select Client Name : "+mapGPorIMDetails.get("Client Name")+" from drop down for GP / IM at index : '"+indexOfGPorIM+"'.]\n";
					return false;
				}
			}
			//Select FundFamily From drop down
			if (mapGPorIMDetails.get("Fund Family Name") != null && !mapGPorIMDetails.get("Fund Family Name").equalsIgnoreCase("None")) {
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//select[@id='cmbFundFamilyName"+indexOfGPorIM+"']/option[contains(normalize-space(),\""+mapGPorIMDetails.get("Fund Family Name")+"\")]"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Fund Family "+mapGPorIMDetails.get("Fund Family Name")+" is not available in Drop down index"+indexOfGPorIM+"]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGPorIMDetails.get("Fund Family Name"), By.xpath("//div[@id='s2id_cmbFundFamilyName"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to select Fund Family Name : "+mapGPorIMDetails.get("Fund Family Name")+" from drop down for GP / IM at index : '"+indexOfGPorIM+"'.]\n";
					return false;
				}
			}
			//Select Legal Entity from drop down
			if (mapGPorIMDetails.get("Legal Entity Name") != null && !mapGPorIMDetails.get("Legal Entity Name").equalsIgnoreCase("None")) {
				bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//select[@id='cmbFundName"+indexOfGPorIM+"']/option[contains(normalize-space(),\""+mapGPorIMDetails.get("Legal Entity Name")+"\")]"), Constants.lTimeOut);
				if(!bStatus){
					Messages.errorMsg = "[ ERROR : Legal Entity "+mapGPorIMDetails.get("Legal Entity Name")+" is not available in Drop down index"+indexOfGPorIM+"]\n";
					return false;
				}
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGPorIMDetails.get("Legal Entity Name"), By.xpath("//div[@id='s2id_cmbFundName"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to select Legal Entity Name : "+mapGPorIMDetails.get("Legal Entity Name")+" from drop down for GP / IM at index : '"+indexOfGPorIM+"'.]\n";
					return false;
				}
			}
			//Select Fee Type
			if (mapGPorIMDetails.get("Fee Type") != null && !mapGPorIMDetails.get("Fee Type").equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication(mapGPorIMDetails.get("Fee Type"), By.xpath("//div[@id='s2id_feeType_"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to select Fee Type Name : "+mapGPorIMDetails.get("Fee Type")+" from drop down for GP / IM at index : '"+indexOfGPorIM+"'.]\n";
					return false;
				}
			}
			//Select From Date of GP/IM.
			if (mapGPorIMDetails.get("GPorIMFromDate") != null && !mapGPorIMDetails.get("GPorIMFromDate").equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.doPickDateFromCalender(mapGPorIMDetails.get("GPorIMFromDate"), "//input[@id='fromDateRange"+indexOfGPorIM+"']");
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to type fromDateRange : "+mapGPorIMDetails.get("GPorIMFromDate")+" into the text box at GP/IM index : '"+indexOfGPorIM+"'.]\n";
					return false;
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'Investor Master')]"));
			}
			//Select To Date of GP/IM.
			if (mapGPorIMDetails.get("GPorIMToDate") != null && !mapGPorIMDetails.get("GPorIMToDate").equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.doPickDateFromCalender(mapGPorIMDetails.get("GPorIMToDate"), "//input[@id='toDateRange"+indexOfGPorIM+"']");
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to type toDateRange : "+mapGPorIMDetails.get("GPorIMToDate")+" into the text box at GP/IM index : '"+indexOfGPorIM+"'.]\n";
					return false;
				}
				NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//p[contains(normalize-space(),'Investor Master')]"));
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doRemoveOrAddMoreGPorIMDetails(String sAddOrRemove){
		try {
			if (sAddOrRemove.equalsIgnoreCase("Remove")) {
				int noOfRemoveButtons = Elements.getXpathCount(Global.driver, By.xpath("//button[contains(@onclick,'removeGenpartnerList')]"));
				for (int indexOfRemoveButton = 1; indexOfRemoveButton <= noOfRemoveButtons; indexOfRemoveButton++) {
					String xPath = "//button[contains(@onclick,\"removeGenpartnerList\") and contains(@onclick,\"a2','"+indexOfRemoveButton+"\")]";
					bStatus = NewUICommonFunctions.scrollToView(By.xpath(xPath));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to scroll the view on to the Remove button of respective GP or IM at index : '"+indexOfRemoveButton+"'.]\n";
						return false;					
					}
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(xPath));
					if (!bStatus) {
						Messages.errorMsg = "[ ERROR : Unable to Click on Remove button of respective GP or IM at index : '"+indexOfRemoveButton+"'.]\n";
						return false;
					}
				}
			}
			if (sAddOrRemove.equalsIgnoreCase("Add")) {
				String xPath = "//button[contains(@onclick,'addGenpartnerList')]";
				bStatus = NewUICommonFunctions.scrollToView(By.xpath(xPath));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to scroll the view on to the 'Add More' button of respective GP or IM .]\n";
					return false;					
				}
				bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(xPath));
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to Click on 'Add More' button of respective GP or IM.]\n";
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doClickOnAddMoreFeeDistribution(int index){
		try {
			String xPath = "//button[contains(@onclick,\"addMoreFeeDistribution1('"+index+"');\") or contains(@onclick,\"addMoreFeeDistribution1("+index+");\")]";
			bStatus = NewUICommonFunctions.scrollToView(By.xpath(xPath));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to scroll on to the view of 'Add More Fee Distribution' button for GP/IM  index : '"+index+"'.]\n";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(xPath));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Click on 'Add More Fee Distribution' button for GP/IM  index : '"+index+"'.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doClickOnRemoveFeeDistribution(int GPorIMIndex, int FeeDistributionIndex){
		try {
			String xPath = "//button[@id='gpList"+GPorIMIndex+"_removeFeeDist_"+FeeDistributionIndex+"']";
			bStatus = NewUICommonFunctions.scrollToView(By.xpath(xPath));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to scroll on to the view of 'Remove Fee Distribution' button for GP/IM  index : '"+GPorIMIndex+"' and Fee distribution Index : '"+FeeDistributionIndex+"'.]\n";
				return false;
			}
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(xPath));
			if (!bStatus) {
				Messages.errorMsg = "[ ERROR : Unable to Click on 'Remove Fee Distribution' button for GP/IM  index : '"+GPorIMIndex+"' and Fee distribution Index : '"+FeeDistributionIndex+"'.]\n";
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doFillRespectiveIndexGPorIMFeeDistributionDetails(List<String> sFeePercentagesListOfEachDistribution,	List<String> sFromDatesListOfEachDistribution, List<String> sToDatesListOfEachDistribution, int iGporIMIndex, int iRespectiveDistributionIndex){
		try {
			if (sFeePercentagesListOfEachDistribution.get(iRespectiveDistributionIndex) != null && !sFeePercentagesListOfEachDistribution.get(iRespectiveDistributionIndex).trim().equalsIgnoreCase("None")) {
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@id='gpList"+iGporIMIndex+"_feeDistribution"+iRespectiveDistributionIndex+".feeDistributionDetails0.percentage']"), sFeePercentagesListOfEachDistribution.get(iRespectiveDistributionIndex).trim());
				if (!bStatus) {
					Messages.errorMsg = "[ ERROR : Unable to input 'Fee Percentage' : '"+sFeePercentagesListOfEachDistribution.get(iRespectiveDistributionIndex).trim()+"' of the GP/IM index : '"+iGporIMIndex+"' and of 'fee Distribution' Index : '"+iRespectiveDistributionIndex+"'.]\n";
					return false;
				}
			}
			if (sFromDatesListOfEachDistribution.get(iRespectiveDistributionIndex) != null && !sFromDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.doPickDateFromCalender(sFromDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim(), "//input[@id='gpList"+iGporIMIndex+"_fromDateRange_"+iRespectiveDistributionIndex+"']");
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to input 'From Date' : '"+sFromDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim()+"' of the GP/IM index : '"+iGporIMIndex+"' and of 'fee Distribution' Index : '"+iRespectiveDistributionIndex+"'.]\n";
					return false;
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//input[@id='gpList"+iGporIMIndex+"_investorAccountMasterAccountIdPk_"+iRespectiveDistributionIndex+"_sysId']"));
			if (sToDatesListOfEachDistribution.get(iRespectiveDistributionIndex) != null && !sToDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.doPickDateFromCalender(sToDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim(), "//input[@id='gpList"+iGporIMIndex+"_toDateRange_"+iRespectiveDistributionIndex+"']");
				if (!bStatus) {
					Messages.errorMsg = Messages.errorMsg + "[ ERROR : Unable to input 'To Date' : '"+sToDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim()+"' of the GP/IM index : '"+iGporIMIndex+"' and of 'fee Distribution' Index : '"+iRespectiveDistributionIndex+"'.]\n";
					return false;
				}
			}
			NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//input[@id='gpList"+iGporIMIndex+"_investorAccountMasterAccountIdPk_"+iRespectiveDistributionIndex+"_sysId']"));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/****************************** Fee Distribution Verification Functions Through Investor *********************/

	public static boolean doVerifyGPorIMDetails(Map<String, String> mapFeeDistribution){
		try {
			String appendErrMsg ="";
			boolean bValidStatus = true;
					
			String sInvestorsTestDataFilePath = Global.sInvestorTestDataFilePath;
			String sSheetName = "FeeDistributionData";
			if (InvestorMasterAppFunctions.isInvestorGeneralDetailsModifyFlag == true) {
				sInvestorsTestDataFilePath = Global.sInvestorModifyTestDataFilePath;
				sSheetName = "ModifyFeeDistributionData";
			}
			if (mapFeeDistribution != null && !mapFeeDistribution.isEmpty() && mapFeeDistribution.get("GPorIMReferenceTC") != null && !mapFeeDistribution.get("GPorIMReferenceTC").equalsIgnoreCase("None")) {
				List<String> sTCListOfNoOfGPOrIMs = Arrays.asList(mapFeeDistribution.get("GPorIMReferenceTC").split(","));
				for (int gpOrIMIndex = 0; gpOrIMIndex < sTCListOfNoOfGPOrIMs.size(); gpOrIMIndex++) {					
					Map<String, String> mapGPorIMDetails = ExcelUtils.readDataABasedOnCellName(sInvestorsTestDataFilePath, sSheetName, sTCListOfNoOfGPOrIMs.get(gpOrIMIndex).trim());
					bStatus = doVerifySelectedFundDetailsForRespectiveGPorIM(mapGPorIMDetails, gpOrIMIndex);
					if (!bStatus) {
						appendErrMsg = appendErrMsg+Messages.errorMsg;
						bValidStatus = false;
					}				
					List<String> sFeePercentagesListOfEachDistribution = null;
					List<String> sFromDatesListOfEachDistribution = null;
					List<String> sToDatesListOfEachDistribution = null;
					if (mapGPorIMDetails.get("Fee Percentage") != null && mapGPorIMDetails.get("DateFrom") != null && mapGPorIMDetails.get("DateTo") != null) {
						sFeePercentagesListOfEachDistribution = Arrays.asList(mapGPorIMDetails.get("Fee Percentage").split(":"));
						sFromDatesListOfEachDistribution = Arrays.asList(mapGPorIMDetails.get("DateFrom").split(":"));
						sToDatesListOfEachDistribution = Arrays.asList(mapGPorIMDetails.get("DateTo").split(":"));
					}

					List<String> sProvisionForDiffermentList = Arrays.asList(mapGPorIMDetails.get("Provision for Deferment").split(":"));
					List<String> sCalculateFeeList = Arrays.asList(mapGPorIMDetails.get("Calculate Fee").split(":"));

					for (int DistributionIndex = 0; DistributionIndex < sProvisionForDiffermentList.size(); DistributionIndex++) {						
						bStatus = doVerifyRespectiveIndexGPorIMFeeDistributionDetails(sFeePercentagesListOfEachDistribution, sFromDatesListOfEachDistribution, sToDatesListOfEachDistribution, gpOrIMIndex, DistributionIndex);
						if (!bStatus) {
							appendErrMsg = appendErrMsg+Messages.errorMsg;
							bValidStatus = false;
						}
						if (sProvisionForDiffermentList.get(DistributionIndex) != null){
							if(sProvisionForDiffermentList.get(DistributionIndex).equalsIgnoreCase("Yes")) {
								bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.defermentPercentageDetails0.isDeferment1')]//span[@class='checked']"), Constants.lTimeOut);
								if (!bStatus) {
									appendErrMsg = appendErrMsg+"[ ERROR : Radio button for 'Provision For Differement' => 'YES' of GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' wasn't marked as selected.]\n";
									bValidStatus = false;
								}
								List<String> sDefermentAllowedLists = Arrays.asList(mapGPorIMDetails.get("DefermentAllowed").split(":"));
								List<String> sSidePocketAllowedLists = Arrays.asList(mapGPorIMDetails.get("SidePocketAllowed").split(":"));
								List<String> sDefermentDateFromLists = Arrays.asList(mapGPorIMDetails.get("Deferment Date From").split(":"));
								List<String> sDefermentDateToLists = Arrays.asList(mapGPorIMDetails.get("Deferment Date To").split(":"));
								List<String> sDefermentPartPnLLists = Arrays.asList(mapGPorIMDetails.get("Deferment Participient PL").split(":"));

								List<String> sDefermentAllowedSubList = Arrays.asList(sDefermentAllowedLists.get(DistributionIndex).split(","));
								List<String> sSidePocketAllowedSubList = Arrays.asList(sSidePocketAllowedLists.get(DistributionIndex).split(","));
								List<String> sDefermentDateFromSubList = Arrays.asList(sDefermentDateFromLists.get(DistributionIndex).split(","));
								List<String> sDefermentDateToSubList = Arrays.asList(sDefermentDateToLists.get(DistributionIndex).split(","));
								List<String> sDefermentPartPnLSubList = Arrays.asList(sDefermentPartPnLLists.get(DistributionIndex).split(","));

								for (int DefermentIndex = 0; DefermentIndex < sDefermentAllowedSubList.size(); DefermentIndex++) {									
									bStatus = doVerifyDefermentDetailsOfDistributionWithRespectiveIndexes(mapGPorIMDetails, sDefermentAllowedSubList.get(DefermentIndex), sSidePocketAllowedSubList.get(DefermentIndex), sDefermentDateFromSubList.get(DefermentIndex), sDefermentDateToSubList.get(DefermentIndex), sDefermentPartPnLSubList.get(DefermentIndex), gpOrIMIndex, DistributionIndex, DefermentIndex);
									if (!bStatus) {
										return false;
									}
								}
							}
							if (sProvisionForDiffermentList.get(DistributionIndex).equalsIgnoreCase("No")) {
								bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.defermentPercentageDetails0.isDeferment2')]//span[@class='checked']"), Constants.lTimeOut);
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Radio button for 'Provision For Differement' => 'NO' of GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' wasn't marked as selected.]\n";
									return false;
								}
							}
							if (sCalculateFeeList.get(DistributionIndex).equalsIgnoreCase("Yes")) {
								bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.isFeeCalculation1')]//span[@class='checked']"), Constants.lTimeOut);
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Radio button 'Calculate Fee' => 'Yes' for GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' wasn't marked as selected.]\n";
									return false;
								}
							}
							if (sCalculateFeeList.get(DistributionIndex).equalsIgnoreCase("No")) {
								bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@id,'gpList"+gpOrIMIndex+"_feeDistribution"+DistributionIndex+".feeDistributionDetails0.isFeeCalculation2')]//span[@class='checked']"), Constants.lTimeOut);
								if (!bStatus) {
									Messages.errorMsg = "[ ERROR : Radio button 'Calculate Fee' => 'NO' for GP/IM index : '"+gpOrIMIndex+"' and 'Distribution' index : '"+DistributionIndex+"' wasn't marked as selected.]\n";
									return false;
								}
							}
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

	private static boolean doVerifyDefermentDetailsOfDistributionWithRespectiveIndexes(Map<String, String> mapGPorIMDetails, String sDefermentAllowedVal,	String sSidePocketAllowedVal,	String sDefermentDateFromVal,	String sDefermentDateToVal, String sDefermentPartPnLVal, int iGPorIMIndex, int iDistributionIndex, int iDefermentIndex) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if (sDefermentAllowedVal != null && !sDefermentAllowedVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercent']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the field 'Deferment % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";					
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Deferment % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' ", By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercent']"), sDefermentAllowedVal.trim(), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sSidePocketAllowedVal != null && !sSidePocketAllowedVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".sidePocketPercent']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the field 'Side Pocket % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Side Pocket % Allowed' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' ", By.xpath("//input[@id='feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".sidePocketPercent']"), sSidePocketAllowedVal.trim(), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sDefermentDateFromVal != null && !sDefermentDateFromVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_feeDistribution"+iDistributionIndex+"feeDistributionDetails0defermentPercentageDetails"+iDefermentIndex+"startDate']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the field 'Deferment Date From' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Deferment Date From' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' ", By.xpath("//input[@id='gpList"+iGPorIMIndex+"_feeDistribution"+iDistributionIndex+"feeDistributionDetails0defermentPercentageDetails"+iDefermentIndex+"startDate']"), sDefermentDateFromVal.trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sDefermentDateToVal != null && !sDefermentDateToVal.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_feeDistribution"+iDistributionIndex+"feeDistributionDetails0defermentPercentageDetails"+iDefermentIndex+"endDate']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the field 'Deferment Date To' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' .]\n";
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Deferment Date To' at Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"' and Deferment : '"+iDefermentIndex+"' ", By.xpath("//input[@id='gpList"+iGPorIMIndex+"_feeDistribution"+iDistributionIndex+"feeDistributionDetails0defermentPercentageDetails"+iDefermentIndex+"endDate']"), sDefermentDateToVal.trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}				
			}
			if (sDefermentPartPnLVal != null && !sDefermentPartPnLVal.trim().equalsIgnoreCase("None")) {
				if (sDefermentPartPnLVal.trim().equalsIgnoreCase("Yes")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[contains(@id,'feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercentageDetailsMappings0.isDefermentPnl1')]//span[@class='checked']"), Constants.lTimeOut);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : 'Deferment Participient P&L' => 'YES' button for the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' wasn't marked as selected.]\n";
						Messages.errorMsg = sAppendErrorMsg;
						return bValidateStatus = false;
					}
					if (mapGPorIMDetails != null && !mapGPorIMDetails.isEmpty() && mapGPorIMDetails.get("Participient FromDate") != null && mapGPorIMDetails.get("Participient ToDate") != null) {
						List<String> sGPorIMParticipientFromDateLists = Arrays.asList(mapGPorIMDetails.get("Participient FromDate").split(":"));
						List<String> sGPorIMParticipientToDateLists = Arrays.asList(mapGPorIMDetails.get("Participient ToDate").split(":"));

						List<String> sDistributionParticipientFromDateLists = Arrays.asList(sGPorIMParticipientFromDateLists.get(iDistributionIndex).split(";"));
						List<String> sDistributionParticipientToDateLists = Arrays.asList(sGPorIMParticipientToDateLists.get(iDistributionIndex).split(";"));

						List<String> sParticipientFromDateList = Arrays.asList(sDistributionParticipientFromDateLists.get(iDefermentIndex).split(","));
						List<String> sParticipientToDateList = Arrays.asList(sDistributionParticipientToDateLists.get(iDefermentIndex).split(","));

						for (int iParticipientIndex = 0; iParticipientIndex < sParticipientFromDateList.size(); iParticipientIndex++) {
							bStatus = doVerifyDefermentPnLDatesOfRespectiveIndexes(sParticipientFromDateList.get(iParticipientIndex),sParticipientToDateList.get(iParticipientIndex), iGPorIMIndex, iDistributionIndex, iDefermentIndex, iParticipientIndex);
							if (!bStatus) {
								return false;
							}
						}						
					}					
				}
				if (sDefermentPartPnLVal.trim().equalsIgnoreCase("No")) {
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//div[contains(@id,'feeDistributionList"+iGPorIMIndex+".feeDistribution"+iDistributionIndex+".feeDistributionDetails0.defermentPercentageDetails"+iDefermentIndex+".defermentPercentageDetailsMappings0.isDefermentPnl2')]//span[@class='checked']"), Constants.lTimeOut);
					if (!bStatus) {
						sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : 'Deferment Participient P&L' => 'NO' button for the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' wasn't marked as selected.]\n";
						Messages.errorMsg = sAppendErrorMsg;
						return bValidateStatus = false;
					}
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyDefermentPnLDatesOfRespectiveIndexes(String sDefPnLFromDate, String sDefPnLToDate, int iGPorIMIndex, int iDistributionIndex, int iDefermentIndex, int iDefermentPnLIndex) {
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if (sDefPnLFromDate != null && !sDefPnLFromDate.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLFromDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the 'From Date' field at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' .]\n";
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'From Date' at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' ", By.xpath("//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLFromDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']"), sDefPnLFromDate.trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sDefPnLToDate != null && !sDefPnLToDate.trim().equalsIgnoreCase("None")) {
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLToDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']"));
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + "[ ERROR : Unable to scroll on to the 'To Date' field at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' .]\n";
					bValidateStatus = false;
				}
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'To Date' at the Indexes of GP/IM : '"+iGPorIMIndex+"', Distribution : '"+iDistributionIndex+"', Deferment : '"+iDefermentIndex+"' and Deferment P&L : '"+iDefermentPnLIndex+"' ", By.xpath("//input[@id='gpList"+iGPorIMIndex+"_defermentParticipientInPnLToDate_"+iDistributionIndex+"_0_"+iDefermentIndex+"_"+iDefermentPnLIndex+"']"), sDefPnLToDate.trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifySelectedFundDetailsForRespectiveGPorIM(Map<String, String> mapGPorIMDetails, int indexOfGPorIM){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[contains(@id,'s2id_clientid_')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = " ERROR : Fee Distribution Details are not displayed in Investor Tab when selected GP/IM option.";
				return false;
			}
			//Select  Client Name From drop down
			if (mapGPorIMDetails.get("Client Name") != null && !mapGPorIMDetails.get("Client Name").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Client Name in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//div[@id='s2id_clientid_"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"), mapGPorIMDetails.get("Client Name"), "Yes", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select FundFamily From drop down
			if (mapGPorIMDetails.get("Fund Family Name") != null && !mapGPorIMDetails.get("Fund Family Name").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Fund Family Name in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//div[@id='s2id_cmbFundFamilyName"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"), mapGPorIMDetails.get("Fund Family Name"), "Yes", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Legal Entity from drop down
			if (mapGPorIMDetails.get("Legal Entity Name") != null && !mapGPorIMDetails.get("Legal Entity Name").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Legal Entity Name in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//div[@id='s2id_cmbFundName"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"), mapGPorIMDetails.get("Legal Entity Name"), "Yes", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select Fee Type
			if (mapGPorIMDetails.get("Fee Type") != null && !mapGPorIMDetails.get("Fee Type").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("Fee Type in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//div[@id='s2id_feeType_"+indexOfGPorIM+"']//span[contains(@id,'select2-chosen')]"), mapGPorIMDetails.get("Fee Type"), "Yes", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select From Date of GP/IM.
			if (mapGPorIMDetails.get("GPorIMFromDate") != null && !mapGPorIMDetails.get("GPorIMFromDate").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("GPorIMFromDate in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//input[@id='fromDateRange"+indexOfGPorIM+"']"), mapGPorIMDetails.get("GPorIMFromDate"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			//Select To Date of GP/IM.
			if (mapGPorIMDetails.get("GPorIMToDate") != null && !mapGPorIMDetails.get("GPorIMToDate").equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("GPorIMToDate in drop down for GP / IM at index : '"+indexOfGPorIM+"'", By.xpath("//input[@id='toDateRange"+indexOfGPorIM+"']"), mapGPorIMDetails.get("GPorIMToDate"), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean doVerifyRespectiveIndexGPorIMFeeDistributionDetails(List<String> sFeePercentagesListOfEachDistribution,	List<String> sFromDatesListOfEachDistribution, List<String> sToDatesListOfEachDistribution, int iGporIMIndex, int iRespectiveDistributionIndex){
		boolean bValidateStatus = true;
		String sAppendErrorMsg = "";
		try {
			if (sFeePercentagesListOfEachDistribution.get(iRespectiveDistributionIndex) != null && !sFeePercentagesListOfEachDistribution.get(iRespectiveDistributionIndex).trim().equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'Fee Percentage' for the GP/IM index : '"+iGporIMIndex+"' and of 'fee Distribution' Index : '"+iRespectiveDistributionIndex+"'", By.xpath("//input[@id='gpList"+iGporIMIndex+"_feeDistribution"+iRespectiveDistributionIndex+".feeDistributionDetails0.percentage']"), sFeePercentagesListOfEachDistribution.get(iRespectiveDistributionIndex).trim(), "No", true);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sFromDatesListOfEachDistribution.get(iRespectiveDistributionIndex) != null && !sFromDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim().equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'From Date' for the GP/IM index : '"+iGporIMIndex+"' and of 'fee Distribution' Index : '"+iRespectiveDistributionIndex+"'", By.xpath("//input[@id='gpList"+iGporIMIndex+"_fromDateRange_"+iRespectiveDistributionIndex+"']"), sFromDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			if (sToDatesListOfEachDistribution.get(iRespectiveDistributionIndex) != null && !sToDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim().equalsIgnoreCase("None")) {
				bStatus = OpeningBalanceMasterAppFunctions.verifyTextIntextBoxOrDropDown("'To Date' for the GP/IM index : '"+iGporIMIndex+"' and of 'fee Distribution' Index : '"+iRespectiveDistributionIndex+"'", By.xpath("//input[@id='gpList"+iGporIMIndex+"_toDateRange_"+iRespectiveDistributionIndex+"']"), sToDatesListOfEachDistribution.get(iRespectiveDistributionIndex).trim(), "No", false);
				if (!bStatus) {
					sAppendErrorMsg = sAppendErrorMsg + Messages.errorMsg;
					bValidateStatus = false;
				}
			}
			Messages.errorMsg = sAppendErrorMsg;
			return bValidateStatus;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


}
