package com.viteos.veda.master.lib;
import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardMainDropdownToSelect;
import com.viteos.veda.master.lib.NewUICommonFunctions.dashboardSubDropdownToSelect;

public class ClientAppFunctions {

	static boolean bStatus;


	public static boolean addNewClient(Map<String, String> mapClientDetails){
		try{

			bStatus = Wait.waitForElementVisibility(Global.driver, By.id("clientName"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Client Master page is not displayed";
				return false;
			}
			//Enter Client Name
			if(mapClientDetails.get("ClientName")!=null){
				NewUICommonFunctions.spinnerClick(Global.driver, By.id("clientName"));
				bStatus = Elements.enterText(Global.driver,By.id("clientName"),mapClientDetails.get("ClientName"));
				if(!bStatus){
					Messages.errorMsg = mapClientDetails.get("ClientName") + " Client Name  was Not Entered";
					return false;
				}
			}
			
			if(mapClientDetails.get("ClientCode") != null){
				bStatus = Elements.enterText(Global.driver, By.name("clientCode"), mapClientDetails.get("ClientCode"));
				if(!bStatus){
					Messages.errorMsg = "Client Code cannot be Entered";
					return false;
				}
			}

			//Switch Allowed Between Fund Families
			if(mapClientDetails.get("SwitchFundFamilies")!=null){
				if(mapClientDetails.get("SwitchFundFamilies").equalsIgnoreCase("Yes")){
					bStatus= Elements.click(Global.driver,By.id("uniform-rdIsSwithAllowedYes"));
					if(!bStatus){
						Messages.errorMsg = "SwitchFundFamilies Radio button Yes is not Clicked ";
						return false;
					}
				}
				if(mapClientDetails.get("SwitchFundFamilies").equalsIgnoreCase("No")){
					bStatus= Elements.click(Global.driver,  By.id("uniform-rdIsSwithAllowedNo"));
					if(!bStatus){
						Messages.errorMsg = "SwitchFundFamilies Radio button No is not Clicked ";
						return false;
					}
				}
			}

			//Enter Previous Administrator
			if(mapClientDetails.get("PreviousAdmin")!=null){
				bStatus = Elements.enterText(Global.driver,By.xpath("//input[@id='entity']"),mapClientDetails.get("PreviousAdmin"));
				if(!bStatus){
					Messages.errorMsg = mapClientDetails.get("SwitchFundFamilies") + " Previous Admin  was Not Enterd";
					return false;
				}
			}

			//  Order Ack check box.
			if(mapClientDetails.get("OA")!=null){
				if(mapClientDetails.get("OA").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='OA']//div//input"));
					if(bStatus){
						bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='OA']//div//input"));
						if(!bStatus){
							Messages.errorMsg = "OA check box  is not Clicked ";
							return false;
						}
					}
				}
				if(mapClientDetails.get("OA").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='OA']//div//input"));
					if(!bStatus){
						bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='OA']//div//input"));
						if(!bStatus){
							Messages.errorMsg = "OA check box  is not Clicked ";
							return false;
						}
					}
				}
			}

			//ICN check box
			if(mapClientDetails.get("ICN")!=null){
				if(mapClientDetails.get("ICN").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='ICN']//div//input"));
					if(!bStatus){
						bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='ICN']//div//input"));
						if(!bStatus){
							Messages.errorMsg = "ICN  check box  is not Clicked ";
							return false;
						}
					}
				}

				if(mapClientDetails.get("ICN").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='ICN']//div//input"));
					if(bStatus){
						bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='ICN']//div//input"));
						if(!bStatus){
							Messages.errorMsg = "ICN  check box  is not Clicked ";
							return false;
						}
					}
				}
			}

			//FCN Check box
			if(mapClientDetails.get("FCN")!=null){
				if( mapClientDetails.get("FCN").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='FCN']//div//input"));
					if(bStatus){
						bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='FCN']//div//input"));
						if(!bStatus){
							Messages.errorMsg = "FinalContract check box  is not Clicked ";
							return false;
						}
					}
				}

				if(mapClientDetails.get("FCN").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='FCN']//div//input"));
					if(!bStatus){
						bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='FCN']//div//input"));
						if(!bStatus){
							Messages.errorMsg = "FCN check box  is not Clicked ";
							return false;
						}
					}
				}
			}

			//Statement check box
			if(mapClientDetails.get("Statement")!=null){
				if(mapClientDetails.get("Statement").equalsIgnoreCase("No")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Statement']//div//input"));
					if(bStatus){
						bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Statement']//div//input"));
						if(!bStatus){
							Messages.errorMsg = "Statement check box  is not Clicked ";
							return false;
						}
					}
				}

				if(mapClientDetails.get("Statement").equalsIgnoreCase("Yes")){
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//label[normalize-space()='Statement']//div//input"));
					if(!bStatus){
						bStatus = Elements.click(Global.driver,By.xpath("//label[normalize-space()='Statement']//div//input"));
						if(!bStatus){
							Messages.errorMsg = "Statement check box  is not Clicked ";
							return false;
						}
					}
				}
			}

			//External Identifier

			if(mapClientDetails.get("ExtId1")!=null){
				bStatus = Elements.enterText(Global.driver,By.id("externalId1"),mapClientDetails.get("ExtId1"));
				if(!bStatus){
					Messages.errorMsg = mapClientDetails.get("ExtId1") + " External Id1  was Not Enterd";
					return false;
				}
			}

			if(mapClientDetails.get("ExtId2")!=null){
				bStatus = Elements.enterText(Global.driver,By.id("externalId2"),mapClientDetails.get("ExtId2"));
				if(!bStatus){
					Messages.errorMsg = mapClientDetails.get("ExtId2") + " External Id2  was Not Enterd";
					return false;
				}

			}

			if(mapClientDetails.get("ExtId3")!=null){
				bStatus = Elements.enterText(Global.driver,By.id("externalId3"),mapClientDetails.get("ExtId3"));
				if(!bStatus){
					Messages.errorMsg = mapClientDetails.get("ExtId3") + " External Id3  was Not Enterd";
					return false;
				}
			}

			if(mapClientDetails.get("OperationType")!=null){
				bStatus = NewUICommonFunctions.doSubOperationsUnderAddNewMasterType("Client", mapClientDetails.get("OperationType"));
				if(mapClientDetails.get("OperationType").equalsIgnoreCase("Cancel")){
					bStatus = Wait.waitForElementPresence(Global.driver, By.xpath("//input[@name='clientCode']"), 2);
					if(bStatus){
						Messages.errorMsg = "Cancel Operation Failed to Perform. Page is Still in Client Screen";
						return false;
					}
					return true;
				}
				if(!bStatus){
					return false;
				}
				return true;
			}
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

/*	public static boolean modifyMasterDetails(String sFieldName,String sName,String sTableName,Map<String, String> ModifiedClient){
		try{
			bStatus = NewUICommonFunctions.searchInSearchPanel(sFieldName,sName,"active",sTableName, 15);
			if(!bStatus){
				return false;
			}

			bStatus = Elements.click(Global.driver, By.xpath("//em[@class='fa fa-pencil']"));
			if(!bStatus){
				Messages.errorMsg = "Cannot click on Modify operation";
				return false;
			}

			bStatus = addNewClient(ModifiedClient);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}*/

	public static boolean verifyClientDetailsInEditScreen(Map<String, String> ClientDetails){
		try{

			boolean validateStatus = true;

			String appendMsg="";

			bStatus = Wait.waitForElementVisibility(Global.driver, By.id("clientName"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Client Master page is not displayed";
				return false;
			}
			//Verify the Client Name
			if(ClientDetails .get("ClientName")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Client Name", ClientDetails.get("ClientName"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
				}
			}
			
			if(ClientDetails .get("ClientCode")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Client Code", ClientDetails.get("ClientCode"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
				}
			}

			// Verify Switch Allowed between Fund families.
			if((ClientDetails.get("SwitchFundFamilies")!=null)){
				bStatus=NewUICommonFunctions.verifySelectedRadioButton("Switch Allowed Between Fund Families",ClientDetails.get("SwitchFundFamilies"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+"]\n";
				}	
			}

			//Verify Previous Administrator
			if(ClientDetails .get("PreviousAdmin")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("Previous Administrator", ClientDetails.get("PreviousAdmin"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
				}
			}

			//Verify Order ACK

			if(ClientDetails.get("OA")!=null){
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,By.xpath("//label[normalize-space()='OA']//div//input"));
				if(!bStatus && ClientDetails.get("OA").equalsIgnoreCase("Yes") ){
					validateStatus = false;
					appendMsg = appendMsg+" [ERROR: OA is Not checked but expected should be checked]\n"; 
				}

				if(bStatus && ClientDetails.get("OA").equalsIgnoreCase("No") ){
					validateStatus = false;
					appendMsg = appendMsg+" [ERROR: OA is checked but expected should be unchecked]\n"; 
				}
			}
			//Verify ICN
			if(ClientDetails.get("ICN")!=null){
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,By.xpath("//label[normalize-space()='ICN']//div//input"));
				if(!bStatus && ClientDetails.get("ICN").equalsIgnoreCase("Yes") ){
					validateStatus = false;
					appendMsg = appendMsg+" [ERROR: ICN is Not checked but expected should be checked]\n"; 
				}

				if(bStatus && ClientDetails.get("ICN").equalsIgnoreCase("No") ){
					validateStatus = false;
					appendMsg = appendMsg+" [ERROR: ICN is checked but expected should be unchecked]\n"; 
				}
			}
			//Verify FCN
			if(ClientDetails.get("FCN")!=null){
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,By.xpath("//label[normalize-space()='FCN']//div//input"));
				if(!bStatus && ClientDetails.get("FCN").equalsIgnoreCase("Yes") ){
					validateStatus = false;
					appendMsg = appendMsg+" [ERROR: FCN is Not checked but expected should be checked]\n"; 
				}

				if(bStatus && ClientDetails.get("FCN").equalsIgnoreCase("No") ){
					validateStatus = false;
					appendMsg = appendMsg+" [ERROR: FCN is checked but expected should be unchecked]\n"; 
				}
			}
			//Verify Statement
			if(ClientDetails.get("IS")!=null){
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,By.xpath("//label[normalize-space()='IS']//div//input"));
				if(!bStatus && ClientDetails.get("IS").equalsIgnoreCase("Yes") ){
					validateStatus = false;
					appendMsg = appendMsg+" [ERROR: IS is Not checked but expected should be checked]\n"; 
				}

				if(bStatus && ClientDetails.get("IS").equalsIgnoreCase("No") ){
					validateStatus = false;
					appendMsg = appendMsg+" [ERROR: IS is checked but expected should be unchecked]\n";  
				}
			}

			//verify External Identifier

			if(ClientDetails.get("ExtId1")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("External Id1", ClientDetails.get("ExtId1"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
				}

			}
			if(ClientDetails.get("ExtId2")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("External Id2", ClientDetails.get("ExtId2"));
				if(!bStatus){
					validateStatus = false;
					appendMsg =  appendMsg+"[ "+Messages.errorMsg+" ]\n";
				}

			}
			if(ClientDetails.get("ExtId3")!=null){
				bStatus=NewUICommonFunctions.verifyTextInTextBox("External Id3", ClientDetails.get("ExtId3"));
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

/*	public static boolean activateClient(String sClientName){
		try{
			bStatus = NewUICommonFunctions.searchInSearchPanel("" , sClientName, "inactive",10);
			if(!bStatus){
				Messages.errorMsg = "There are no Clients in inactive state yet";
				return false;
			}

			NewUICommonFunctions.selectNumberOfRecordsPerPageInSearchGrid("All",10);

			NewUICommonFunctions.sortTableByColumnName("sample_2", "Client ID", "descending");

			Elements.click(Global.driver, By.xpath("//td[normalize-space(text())='"+sClientName+"']/../td[5]/a[@data-original-title='Activate']"));

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="Client cannot be activated";
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
/*	public static boolean deactivateClient(String sClientName){
		try{
			bStatus = NewUICommonFunctions.searchInSearchPanel("Client Name" , sClientName, "active",10);
			if(!bStatus){
				Messages.errorMsg = "Client is Not in active state yet";
				return false;
			}
			Elements.click(Global.driver, By.xpath("//a[@data-original-title='Deactivate']"));

			bStatus = Wait.waitForElementVisibility(Global.driver, Locators.ClientMaster.Label.lblSuccessfullyMsg, Constants.iSaveTime);
			if(!bStatus){
				Messages.errorMsg ="Client cannot be deactivated";
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
	public static boolean modifyReturnClient(String searchName,Map<String, String> ModifiedClient){
		try{
			bStatus  = NewUICommonFunctions.performOperationsOnTable(dashboardMainDropdownToSelect.MASTERS, dashboardSubDropdownToSelect.NEW,searchName,"");
			if(!bStatus){
				return false;
			}

			bStatus = addNewClient(ModifiedClient);
			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

}
