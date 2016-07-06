package com.viteos.veda.master.lib;

import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.locators.Locators;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class JointHolderAppFunctions {

	public static boolean bStatus = true;
	
	public static boolean addNewJointHolder(Map<String , String> mapJointHolderDetails){
		try {
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//a[contains(text(),'General Details')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "General Details are Not Visible";
				return false;
			}

			if(mapJointHolderDetails.get("Investor Name")!=null){
				bStatus = NewUICommonFunctions.selectFromDropDownOfApplication("Investor Name", mapJointHolderDetails.get("Investor Name"));
				if(!bStatus){
					return false;
				}
			}
			if(mapJointHolderDetails.get("First Name")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.JointHolder.TextBox.objFirstName, mapJointHolderDetails.get("First Name"));
				if(!bStatus){
					Messages.errorMsg = "First Name Not Entered";
					return false;

				}
			}
			if(mapJointHolderDetails.get("Middle Name")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.JointHolder.TextBox.objMiddleName, mapJointHolderDetails.get("Middle Name"));
				if(!bStatus){
					Messages.errorMsg = "Middle Name Not Entered";
					return false;
				}
			}
			if(mapJointHolderDetails.get("Last Name")!=null){
				bStatus = Elements.enterText(Global.driver, Locators.JointHolder.TextBox.objLastName, mapJointHolderDetails.get("Last Name"));
				if(!bStatus){
					Messages.errorMsg = "Last Name Not Entered";
					return false;
				}
			}
			if(mapJointHolderDetails.get("US Person")!=null){

				if(mapJointHolderDetails.get("US Person").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath("//div[contains(@id,'uniform-rdisUsPersonYes')]"));
					if(!bStatus){
						Messages.errorMsg = "US Person Yes Radio button Not clicked";
						return false;
					}
				}
				if(mapJointHolderDetails.get("US Person").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver,  By.xpath("//div[contains(@id,'uniform-rdisUsPersonNo')]"));
					if(!bStatus){
						Messages.errorMsg = "US Person No Radio Button Not Clicked";
						return false;
					}
				}		
			}
			if(mapJointHolderDetails.get("Investor Sub Type")!=null){
				if(mapJointHolderDetails.get("Investor Sub Type").equalsIgnoreCase("Accredited Investor")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.JointHolder.RadioButton.objInvestorSubTypeAccreditedInvestor);
					if(!bStatus){
						Messages.errorMsg = "Accredited Investor Radio Button Not Selected";
						return false;
					}
				}
				if(mapJointHolderDetails.get("Investor Sub Type").equalsIgnoreCase("Qualified Purchaser")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.JointHolder.RadioButton.objInvestorTypeQualilfiedPurchaser);
					if(!bStatus){
						Messages.errorMsg = "Qualified Purchaser Radio Button Not Selected";
						return false;
					}
				}
				if(mapJointHolderDetails.get("Investor Sub Type").equalsIgnoreCase("NA")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.JointHolder.RadioButton.objInvestorTypeNA);
					if(!bStatus){
						Messages.errorMsg = "Investor Sub Type NA Radio Button Not Selected";
						return false;
					}
				}			
			}
			if(mapJointHolderDetails.get("Erisa Investor")!=null){
				if(mapJointHolderDetails.get("Erisa Investor").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.JointHolder.RadioButton.objErisaInvestorYes);
					if(!bStatus){
						Messages.errorMsg = "Erisa Investor Yes Radio Button Not Selected";
						return false;
					}
				}
				if(mapJointHolderDetails.get("Erisa Investor").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.spinnerClick(Global.driver, Locators.JointHolder.RadioButton.objErisaInvestorNo);
					if(!bStatus){
						Messages.errorMsg = "Erisa Investor No Radio Button Not Selected";
						return false;
					}
				}
			}
			if(mapJointHolderDetails.get("OperationType")!=null){
				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Joint Holder", mapJointHolderDetails.get("OperationType"));
			}
			return bStatus;



		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}

	}

	public static boolean verifyJointHolderDetailsInEditScreen(Map<String , String> mapJointHolderDetails){
		try {
			boolean validateStatus = true;
			String appendMsg="";
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//a[contains(text(),'General Details')]"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "General Details are Not Visible";
				return false;
			}

			// Verify the Investor Name
			if(mapJointHolderDetails.get("Investor Name")!=null){
				bStatus=NewUICommonFunctions.verifyTextInDropDown("Investor Name", mapJointHolderDetails.get("Investor Name"));
				if(!bStatus){
					validateStatus = false;
					appendMsg = appendMsg+"[ "+Messages.errorMsg+" for Investor Name]\n";
				}
			}

			//Verify the First Name
			if(mapJointHolderDetails .get("First Name") !=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("First Name", mapJointHolderDetails.get("First Name"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for First Name ]\n";
				}
			}

			//Verify the Middle Name
			if(mapJointHolderDetails.get("Middle Name")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Middle Name", mapJointHolderDetails.get("Middle Name"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg +" for Middle Name ]\n";
				}
			}
			
			//Verify the Last Name
			if(mapJointHolderDetails.get("Last Name")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Last Name", mapJointHolderDetails.get("Last Name"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg +" for Last Name ]\n";
				}
			}
			
			//Verify the Us Person radio button
			if((mapJointHolderDetails.get("US Person")!=null)){
				bStatus=NewUICommonFunctions.verifySelectedRadioButton("US Person",mapJointHolderDetails.get("US Person"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" for US Person ]\n";
				}	
			}
			
			//Verify Investor Sub type radio button
			if(mapJointHolderDetails.get("Investor Sub Type")!=null){
				bStatus=NewUICommonFunctions.verifySelectedRadioButton("Investor Sub Type",mapJointHolderDetails.get("Investor Sub Type"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" Investor Sub Type ]\n";
				}	
			}
			
			// Verify Erisa Investor Radio button
			if(mapJointHolderDetails.get("Erisa Investor")!=null){
				bStatus=NewUICommonFunctions.verifySelectedRadioButton("Erisa Investor",mapJointHolderDetails.get("Erisa Investor"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" Erisa Investor ]\n";
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
 
	public static boolean modifyReturnJointHolder(String searchName,Map<String, String> ModifiedJointHolder){
		try{
			bStatus  = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,searchName,"");
			if(!bStatus){
				return false;
			}

			bStatus = addNewJointHolder(ModifiedJointHolder);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
