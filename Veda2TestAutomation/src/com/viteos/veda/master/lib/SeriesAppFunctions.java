package com.viteos.veda.master.lib;

import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class SeriesAppFunctions {


	static boolean bStatus;
	

	//CHANGED ACCORDING TO NEW USER INTERFACE
	public static boolean addNewSeries(Map<String, String> mapSeriesDetails){
		try{
			bStatus = Wait.waitForElementVisibility(Global.driver, By.id("unitValue"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Series Master Page Not Visible : Unit Value field is Not Available"; 
				return false;
			}
			//Selecting the Client Name
			if(mapSeriesDetails.get("ClientName")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Client Name",mapSeriesDetails.get("ClientName"));
				if (!bStatus) {
					Messages.errorMsg = mapSeriesDetails.get("ClientName")+" is not available in the dropdown";
					return false;
				}
			}
			//Selecting the Fund Family Name
			if(mapSeriesDetails.get("FundFamilyName")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Fund Family Name",mapSeriesDetails.get("FundFamilyName"));
				if (!bStatus) {
					Messages.errorMsg = mapSeriesDetails.get("FundFamilyName")+" is not available in the dropdown";
					return false;
				}
			}
			//Selecting the Legal Entity Name
			if(mapSeriesDetails.get("LegalEntityName")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Legal Entity Name",mapSeriesDetails.get("LegalEntityName"));
				if (!bStatus) {
					Messages.errorMsg = mapSeriesDetails.get("LegalEntityName")+" is not available in the dropdown";
					return false;
				}
			}
			//Selecting the Class Name
			if(mapSeriesDetails.get("ClassName")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Class Name",mapSeriesDetails.get("ClassName"));
				if (!bStatus) {
					Messages.errorMsg = mapSeriesDetails.get("ClassName")+" is not available in the dropdown";
					return false;
				}
			}
			//Entering the Series Name
			if(mapSeriesDetails.get("SeriesName")!=null){
				bStatus=Elements.enterText(Global.driver, By.xpath("//input[@id='seriesName']"), mapSeriesDetails.get("SeriesName"));
				if(!bStatus){
					Messages.errorMsg = "Series Name was Not Enterd";
					return false;
				}
			}
			//Entering the Date Of First Issue
			if(mapSeriesDetails.get("FirstIssueDate")!=null){
				bStatus=Elements.enterText(Global.driver, By.xpath("//input[@id='dateOfFirstIssue']"), mapSeriesDetails.get("FirstIssueDate"));
				if(!bStatus){
					Messages.errorMsg = "Series Name was Not Enterd";
					return false;
				}
			}
			//Selecting the BenchMark Series Radio Button
			if(mapSeriesDetails.get("BenchmarkSeries")!=null){
				if(mapSeriesDetails.get("BenchmarkSeries").equalsIgnoreCase("Yes")){
					bStatus = Elements.click(Global.driver, By.id("uniform-benchmarkSeriesYes"));
					if(!bStatus){
						Messages.errorMsg = "BenchmarkSeries Yes Radio Button Not Selected";
						return false;
					}
				}
				if(mapSeriesDetails.get("BenchmarkSeries").equalsIgnoreCase("No")){
					bStatus = Elements.click(Global.driver, By.id("uniform-benchmarkSeriesNo"));
					if(!bStatus){
						Messages.errorMsg = "BenchmarkSeries No Radio Button Not Selected";
						return false;
					}
				}
			}
			//Entering the Unit Value
			if(mapSeriesDetails.get("UnitValue")!=null){
				bStatus=Elements.enterText(Global.driver,By.id("unitValue"),mapSeriesDetails.get("UnitValue"));
				if(!bStatus){
					Messages.errorMsg = "External ID1 Not Entered";
					return false;
				}
			}
			//Entering the External Id1
			if(mapSeriesDetails.get("ExtId1")!=null)
			{
				bStatus=Elements.enterText(Global.driver,Locators.FundFamilyMaster.TextBox.objExternalId1,mapSeriesDetails.get("ExtId1"));
				if(!bStatus){
					Messages.errorMsg = "External ID1 Not Entered";
					return false;
				}
			}
			//Entering the External Id2
			if(mapSeriesDetails.get("ExtId2")!=null)
			{
				bStatus=Elements.enterText(Global.driver, Locators.FundFamilyMaster.TextBox.objExternalId2,mapSeriesDetails.get("ExtId2"));
				if(!bStatus){
					Messages.errorMsg = "External ID2 Not Entered";
					return false;
				}
			}
			//Entering the External Id3
			if(mapSeriesDetails.get("ExtId3")!=null){
				Elements.enterText(Global.driver,Locators.FundFamilyMaster.TextBox.objExternalId3,mapSeriesDetails.get("ExtId3"));
				if(!bStatus){
					Messages.errorMsg = "External ID3 Not Entered";
					return false;
				}
			}


			//Selecting the Operation Type
			if(mapSeriesDetails.get("OperationType")!=null){
				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Series", mapSeriesDetails.get("OperationType"));
			}
			return bStatus;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}

	}

	/*	//changed according to new UI
	public static boolean modifySeriesDetails(String sSeriesName,Map<String, String> mapModifiedSeriesDetails){
		try{

			bStatus = NewUICommonFunctions.searchInSearchPanel("Series Name",sSeriesName, "active",10); 
			if(!bStatus){
				Messages.errorMsg = "Series is Not active yet";
				return false;
			}

			Elements.click(Global.driver, By.xpath("//a[@data-original-title='Edit']"));

			bStatus = addNewSeries(mapModifiedSeriesDetails);
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

	public static boolean modifyReturnSeriesDetails(String sSeriesName,Map<String, String> mapModifiedSeriesDetails){
		try{
			bStatus = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS,dashboardSubDropdownToSelect.NEW,sSeriesName,"");
			if(!bStatus){
				return false;
			}

			bStatus = addNewSeries(mapModifiedSeriesDetails);
			if(!bStatus){
				return bStatus;
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean verifySeriesDetailsInEditScreen(Map<String, String> SeriesDetails){
		try{

			boolean validateStatus = true;

			String appendMsg="";

			bStatus = Wait.waitForElementVisibility(Global.driver, By.id("unitValue"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Series Page not visible in Series Master";
				return false;
			}
			//Verify Selected Client Name
			if(SeriesDetails.get("ClientName")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Client Name", SeriesDetails.get("ClientName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Client name]\n";
				}
			}
			//Verify Selected Fund Family Name
			if((SeriesDetails.get("FundFamilyName")!=null)){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Fund Family Name", SeriesDetails.get("FundFamilyName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for fund family name]\n";
				}
			}
			//Verify Selected Legal Entity Name
			if(SeriesDetails.get("LegalEntityName")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Legal Entity Name", SeriesDetails.get("LegalEntityName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Legal Entity]\n";
				}
			}
			//Verify Selected Class Name
			if(SeriesDetails.get("ClassName")!=null){
				bStatus = NewUICommonFunctions.verifyTextInDropDown("Class Name", SeriesDetails.get("ClassName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Class Name]\n";
				}
			}
			//Verify Entered Series Name
			if(SeriesDetails.get("SeriesName")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Series Name",SeriesDetails.get("SeriesName") );
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for Series Name]\n";
				}
			}
			//Verify Entered FirstIssue Date
			if(SeriesDetails.get("FirstIssueDate")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("Date of First Issue",SeriesDetails.get("FirstIssueDate") );
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for First Issue Date ]\n";
				}
			}
			//Verify Selected Benchmark Series Radio Button 
			if(SeriesDetails.get("BenchmarkSeries")!=null){
				bStatus = NewUICommonFunctions.verifySelectedRadioButton("Benchmark Series",SeriesDetails.get("BenchmarkSeries"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+ "[ "+SeriesDetails.get("BenchmarkSeries")+" option is not Selected in Benchmark Series ]\n";
				}
			}
			//Verify Entered External ID1
			if(SeriesDetails.get("ExtId1")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("External Id1",SeriesDetails.get("ExtId1") );
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
				}
			}
			//Verify Entered Unit Value
			if(SeriesDetails.get("UnitValue")!=null){
				String actualValue = Elements.getElementAttribute(Global.driver, By.id("unitValue"), "value");
				bStatus = NewUICommonFunctions.isTheNumbersMatchingInFloatingPoint(actualValue, SeriesDetails.get("UnitValue"));
				//bStatus = NewUICommonFunctions.verifyTextInTextBox("Unit Value",SeriesDetails.get("UnitValue") );
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
				}
			}
			//Verify Entered External ID2
			if(SeriesDetails.get("ExtId2")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("External Id2",SeriesDetails.get("ExtId2") );
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
				}
			}
			//Verify Entered External ID2
			if(SeriesDetails.get("ExtId3")!=null){
				bStatus = NewUICommonFunctions.verifyTextInTextBox("External Id3",SeriesDetails.get("ExtId3") );
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
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

	/*	public static boolean deactivateSeries(String sSeriesName){
		try{
			bStatus = NewUICommonFunctions.searchInSearchPanel("Series Name" , sSeriesName, "active",10); 
			if(!bStatus){
				Messages.errorMsg = "Series is Not in active state yet";
				return false;
			}
			bStatus = Elements.click(Global.driver, By.xpath("//a[@data-original-title='Deactivate']"));
			if(!bStatus){
				Messages.errorMsg = "Deactivate Not Clicked ";
				return false;
			}

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="Success Message not displayed. Series cannot be deactivated";
				return false;
			}
			return true;

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean activateSeries(String sSeriesName){		
		try{
			bStatus = NewUICommonFunctions.searchInSearchPanel("", sSeriesName, "inactive",10);
			if(!bStatus){
				Messages.errorMsg = "There are no Clients in inactive state yet";
				return false;
			}

			NewUICommonFunctions.selectNumberOfRecordsPerPageInSearchGrid("All",10);

			NewUICommonFunctions.sortTableByColumnName("sample_2", "Series ID", "descending");

			Elements.click(Global.driver, By.xpath("//td[normalize-space(text())='"+sSeriesName+"']/../td[7]/a[@data-original-title='Activate']"));

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
