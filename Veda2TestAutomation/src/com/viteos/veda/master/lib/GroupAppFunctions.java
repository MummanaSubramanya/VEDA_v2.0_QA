package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;

public class GroupAppFunctions {

	static boolean bStatus; 
	public static boolean createAGroup(Map<String, String> mapGroupDetails){
		try{

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//li[text()='Group']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Group portal is not visible on clicking on expand button";
				return false;
			}

			if(mapGroupDetails.get("Group Name")!=null){
				bStatus = Elements.enterText(Global.driver, By.xpath("//input[@name='groupName']"),mapGroupDetails.get("Group Name") );
				if(!bStatus){
					Messages.errorMsg = "Text Cannot be entered in GroupName";
					return false;
				}
			}

			if(mapGroupDetails.get("Assign Legal Entity")!=null && mapGroupDetails.get("Role")!=null){
				bStatus = removeGroupDetails();
				if(!bStatus){
					return false;
				}
				bStatus = enterGroupDetails(mapGroupDetails);
				if(!bStatus){
					return false;
				}
			}

			if(mapGroupDetails.get("OperationType")!=null){
				bStatus = NewUICommonFunctions.doOperationOnUserManagementSubMasters("groupButtonDiv", mapGroupDetails.get("OperationType"));
				if(!bStatus){
					return false;
				}
			}

			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}


	private static boolean removeGroupDetails() {
		try {
			By removeButtonXpath = By.xpath("//em[@class='fa fa-close']//parent::button");
			int removeCount = Elements.getXpathCount(Global.driver, removeButtonXpath);
			if(removeCount==0){
				return true;
			}
			for(int i=removeCount ;i>=1;i--){
				bStatus = Elements.click(Global.driver, removeButtonXpath);
				if(!bStatus){
					Messages.errorMsg = "Remove Button Failed to click";
					return false;
				}
			}

			int removeCountafterRemove = Elements.getXpathCount(Global.driver, removeButtonXpath);
			if(removeCountafterRemove!=0){
				Messages.errorMsg = "Remove Button Not Removed";
				return false;
			}


			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	private static boolean enterGroupDetails(Map<String, String> mapGroupDetails) {
		try {

			List<String> assignLEList = Arrays.asList(mapGroupDetails.get("Assign Legal Entity").split(":"));

			List<String> aroleList = Arrays.asList(mapGroupDetails.get("Role").split(":"));
			for(int i=0 ; i<assignLEList.size() ; i++){


				//Select Assign Legal Entity Yes Radio Button

				if(assignLEList.get(i).equalsIgnoreCase("Yes"))
				{
					bStatus = NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space()='Yes']//input[@name='leFlag"+i+"']"));
					if(!bStatus){
						Messages.errorMsg = "Page  is not Scrolled to Assign Legal Entity Button";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='Yes']//input[@name='leFlag"+i+"']"));
					if(!bStatus){
						Messages.errorMsg = "Assign Legal Entity Yes Radio Button Not Selected";
						return false;
					}
					//Enter Client,Fund Family and Legal Entity Detail

					if(mapGroupDetails.get("Client")!=null && mapGroupDetails.get("Fund Family")!=null && mapGroupDetails.get("Legal Entity")!=null){
						bStatus = enterFundSetupDetails(mapGroupDetails ,i);
						if(!bStatus){
							return false;
						}
					}

				}

				//Select Assign Legal Entity No Radio Button
				if(assignLEList.get(i).equalsIgnoreCase("No"))
				{
					bStatus = NewUICommonFunctions.scrollToView(By.xpath("//label[normalize-space()='No']//input[@name='leFlag"+i+"']"));
					if(!bStatus){
						Messages.errorMsg = "Page  is not Scrolled to Assign Legal Entity Button";
						return false;
					}

					bStatus = Elements.click(Global.driver, By.xpath("//label[normalize-space()='No']//input[@name='leFlag"+i+"']"));
					if(!bStatus){
						Messages.errorMsg = "Assign Legal Entity No Radio Button Not Selected";
						return false;

					}						
				}

				//Enter Role Details

				if(!aroleList.get(i).equalsIgnoreCase("None")){
					bStatus = removeSelectedListBoxValues("selectAllRoleId"+i+"");
					if(!bStatus){
						return false;
					}
					if(aroleList.get(i).equalsIgnoreCase("All")){
						bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//div[@id='uniform-selectAllRoleId"+i+"']//input"));
						if(!bStatus){
							bStatus = Elements.click(Global.driver, By.xpath("//input[@id='selectAllRoleId"+i+"']"));
							if(!bStatus){
								Messages.errorMsg = "Role SelectAll Checkbox Not Selected";
								return false;
							}
						}
					}else{

						bStatus = NewUICommonFunctions.selectMultipleValuesInSelectBox("Role",aroleList.get(i).split(","),"role"+i);
						if(!bStatus){
							return false;
						}
					}

				}
				//Click on AddMOre Button
				if(i<assignLEList.size()-1){
					bStatus = NewUICommonFunctions.scrollToView(By.xpath("//em[@class='fa fa-plus']//parent::button"));
					if(!bStatus){
						Messages.errorMsg = "Page  is not Scrolled to Add More Button";
						return false;
					}
					bStatus = Elements.click(Global.driver, By.xpath("//div[@id='leRoleListHtml']//em[@class='fa fa-plus']//parent::button"));
					if(!bStatus){
						Messages.errorMsg = "Failed To Click on AddMore Button";
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


	private static boolean removeSelectedListBoxValues(String checkBoxId) {
		try {
			By selectCheckBoxLocator = By.xpath("//input[@id='"+checkBoxId+"']");
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver, selectCheckBoxLocator);
			if(!bStatus){
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='"+checkBoxId+"']"));
				if(!bStatus){
					Messages.errorMsg = "Page  is not Scrolled to "+checkBoxId;
					return false;
				}
				bStatus = Elements.click(Global.driver, selectCheckBoxLocator);
				if(!bStatus){
					Messages.errorMsg = "Select All Check box Not Checked";
					return false;
				}
			}

			bStatus = NewUICommonFunctions.verifyChecked(Global.driver, selectCheckBoxLocator);
			if(bStatus){
				bStatus = NewUICommonFunctions.scrollToView(By.xpath("//input[@id='"+checkBoxId+"']"));
				if(!bStatus){
					Messages.errorMsg = "Page  is not Scrolled to "+checkBoxId;
					return false;
				}
				bStatus = Elements.click(Global.driver, selectCheckBoxLocator);
				if(!bStatus){
					Messages.errorMsg = "Select All Check box Not Unchecked";
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


	private static boolean enterFundSetupDetails(Map<String, String> mapGroupDetails , int i) {
		try {
			List<String> aClientList = Arrays.asList(mapGroupDetails.get("Client").split(":"));
			List<String> aFundFamilyList = Arrays.asList(mapGroupDetails.get("Fund Family").split(":"));
			List<String> aLegalEntity = Arrays.asList(mapGroupDetails.get("Legal Entity").split(":"));
			String selectValueXpath = "//ul[contains(@id,'select2-results')]//li//div[text()='selectDropDownValue']";

			if(!aClientList.get(i).equalsIgnoreCase("None")){
				By clickLocator = By.xpath("//div[@id='s2id_client"+i+"']//span[contains(@id,'select2-chosen')]");		
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(aClientList.get(i), clickLocator, selectValueXpath);
				if(!bStatus){
					Messages.errorMsg = "Client Dropdown Not Selected";
					return false;
				}
			}

			if(!aFundFamilyList.get(i).equalsIgnoreCase("None")){
				By clickLocatorFF = By.xpath("//div[@id='s2id_fundFamily"+i+"']//span[contains(@id,'select2-chosen')]");
				bStatus = NewUICommonFunctions.selectDropDownOfApplication(aFundFamilyList.get(i), clickLocatorFF, selectValueXpath);
				if(!bStatus){
					Messages.errorMsg = aFundFamilyList.get(i)+"Fund Family Dropdown Not Selected";
					return false;
				}
			}

			if(!aLegalEntity.get(i).equalsIgnoreCase("None")){

				bStatus = removeSelectedListBoxValues("selectAllLegalEntityId"+i+"");
				if(!bStatus){
					return false;
				}


				if(aLegalEntity.get(i).equalsIgnoreCase("All"))
				{
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//div[@id='uniform-selectAllLegalEntityId"+i+"']//input"));
					if(!bStatus){
						bStatus = Elements.click(Global.driver, By.xpath("//input[@id='selectAllLegalEntityId"+i+"']"));
						if(!bStatus){
							Messages.errorMsg = "Legal Entity SeleAll Check box not selected";
							return false;
						}
					}
				}
				else
				{
					bStatus = NewUICommonFunctions.selectMultipleValuesInSelectBox("Legal Entity",aLegalEntity.get(i).split(","),"legalEntity"+i);
					if(!bStatus){
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


	public static boolean verifyTheCreatedGroup(Map<String, String> mapGroupDetails) {
		try {
			String ErrorMsg = "";
			boolean bVerifyStatus = true;

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//li[text()='Group']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Group portal is not visible on clicking on expand button";
				return false;
			}

			if(mapGroupDetails.get("Group Name")!=null){

				String actualValue = NewUICommonFunctions.jsGetElementAttribute("Name" ,"groupName");
				if(!actualValue.equalsIgnoreCase(mapGroupDetails.get("Group Name"))){
					ErrorMsg= ErrorMsg+"[ Group Name actual value "+actualValue+" is not matching with the Expected "+mapGroupDetails.get("Group Name")+"]";
					bVerifyStatus =  false;
				}
			}

			if(mapGroupDetails.get("Assign Legal Entity")!=null && mapGroupDetails.get("Role")!=null){

				bStatus = verifyGroupDetails(mapGroupDetails);
				if(!bStatus){
					return false;
				}

			}
			return bVerifyStatus;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	private static boolean verifyGroupDetails(Map<String, String> mapGroupDetails) {
		try {
			boolean bValidStatus = true;
			List<String> assignLEList = Arrays.asList(mapGroupDetails.get("Assign Legal Entity").split(":"));
			Map<String ,String> actualMapFromApplication = new HashMap<>();

			//get the Double Map from Excel by index 0,1,2.. as ExpectedMapFromExcel.get(0,firstGridValues)
			Map<String ,Map<String ,String>> ExpectedMapFromExcel =  getIndividualMapFromExcel(mapGroupDetails);

			for(int i=0 ; i<assignLEList.size() ; i++){

				//get the actual values from application 
				actualMapFromApplication = getMapFromApplication(i);

				//compare the map from application with the Excel map
				for(int j=0;j<assignLEList.size();j++){
					Map<String ,String> ExpectedIndividualMapFromExcel = new HashMap<>();
					ExpectedIndividualMapFromExcel = ExpectedMapFromExcel.get(j+"");
					bStatus = compareMaps(actualMapFromApplication, ExpectedIndividualMapFromExcel, i);
					//bStatus = ExpectedIndividualMapFromExcel.equals(actualMapFromApplication);
					if(!bStatus){
						continue;
					}
					break;

				}
				if(!bStatus){
					Messages.errorMsg = "Expected Group Values  are Not Matching with the Actual Group Value "+Messages.errorMsg;
					return false;		
				}

			}
			return bValidStatus;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}


	private static Map<String, Map<String, String>> getIndividualMapFromExcel(Map<String, String> mapGroupDetails) {
		try {

			Map<String ,Map<String ,String>> IndividulaExcelMap = new HashMap<>();	
			List<String> AssignLEmap = Arrays.asList(mapGroupDetails.get("Assign Legal Entity").split(":"));
			List<String> aroleList = Arrays.asList(mapGroupDetails.get("Role").split(":"));
			List<String> aClientList = Arrays.asList(mapGroupDetails.get("Client").split(":"));
			List<String> aFundFamilyList = Arrays.asList(mapGroupDetails.get("Fund Family").split(":"));
			List<String> aLegalEntity = Arrays.asList(mapGroupDetails.get("Legal Entity").split(":"));

			for(int i=0 ; i<AssignLEmap.size() ; i++){
				Map<String , String> individualMap = new HashMap<>();
				individualMap.put("Assign Legal Entity", AssignLEmap.get(i).trim());

				if(!aClientList.get(i).equalsIgnoreCase("None")){

					individualMap.put("Client" , aClientList.get(i).trim());
				}

				if(!aFundFamilyList.get(i).equalsIgnoreCase("None")){

					individualMap.put("Fund Family", aFundFamilyList.get(i));
				}

				if(!aLegalEntity.get(i).equalsIgnoreCase("None")){

					List<String> aLegalEntitysubList = Arrays.asList(aLegalEntity.get(i).split(","));

					String LegalEnityStringList = "";
					for(int j =0 ; j<aLegalEntitysubList.size() ; j++){

						String LegalEntityDetails = aLegalEntitysubList.get(j).trim();
						LegalEnityStringList = LegalEnityStringList+LegalEntityDetails+",";
					}
					LegalEnityStringList = LegalEnityStringList.replaceAll(",$", "");

					individualMap.put("Legal Entity", LegalEnityStringList);

				}

				if(!aroleList.get(i).equalsIgnoreCase("None")){

					List<String> aRoleSubList = Arrays.asList(aroleList.get(i).split(","));

					String roleStringList = "";
					for(int k =0 ; k<aRoleSubList.size() ; k++){

						String roleDetails = aRoleSubList.get(k).trim();
						roleStringList = roleStringList+roleDetails+",";
					}

					roleStringList = roleStringList.replaceAll(",$", "");
					individualMap.put("Role", roleStringList);
				}

				IndividulaExcelMap.put(i+"", individualMap);


			}

			return IndividulaExcelMap;

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}


	private static Map<String, String> getMapFromApplication(int i) {
		try {
			Map<String ,String> CreatedGroupMapDetails = new HashMap<String, String>();

			bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@name='leFlag"+i+"' and @value='0']"));
			if(bStatus){
				CreatedGroupMapDetails.put("Assign Legal Entity", "No");
			}		
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver, By.xpath("//input[@name='leFlag"+i+"' and @value='1']"));
			if(bStatus){
				CreatedGroupMapDetails.put("Assign Legal Entity", "Yes");
			}


			String clientName = Elements.getText(Global.driver, By.xpath("//div[@id='s2id_client"+i+"']//span[contains(@id,'select2-chosen')]"));
			if(clientName!=null){
				CreatedGroupMapDetails.put("Client", clientName.trim());
			}


			String fundFamilyName = Elements.getText(Global.driver, By.xpath("//div[@id='s2id_fundFamily"+i+"']//span[contains(@id,'select2-chosen')]"));
			if(fundFamilyName!=null){
				CreatedGroupMapDetails.put("Fund Family", fundFamilyName.trim());
			}


			int legalEntityXpathCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='legalEntity"+i+"']//option[@selected='selected']"));
			if(legalEntityXpathCount>0){
				int totlaLegalEntityCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='legalEntity"+i+"']/option"));
				if(legalEntityXpathCount==totlaLegalEntityCount){
					CreatedGroupMapDetails.put("Legal Entity", "All");
				}
				else
				{

					String legalEntities = "";
					for(int j=1 ; j<=legalEntityXpathCount ; j++){

						String LeName = Elements.getText(Global.driver, By.xpath("//select[@id='legalEntity"+i+"']//option[@selected='selected']["+j+"]"));
						if(LeName==null){
							continue;
						}
						legalEntities = legalEntities+LeName.trim()+",";
					}
					legalEntities = legalEntities.replaceAll(",$", "");
					CreatedGroupMapDetails.put("Legal Entity", legalEntities);
				}			

			}


			int roleDetailsCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='role"+i+"']//option[@selected='selected']"));
			if(roleDetailsCount>0){
				int totalroleDetailsCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='role"+i+"']/option"));
				if(roleDetailsCount==totalroleDetailsCount){
					CreatedGroupMapDetails.put("Role", "All");
				}
				else
				{
					String RoleDetails = "";
					for(int k=1 ; k<=roleDetailsCount ; k++){
						String roleDetails = Elements.getText(Global.driver, By.xpath("//select[@id='role"+i+"']//option[@selected='selected']["+k+"]"));
						RoleDetails = RoleDetails+roleDetails.trim()+",";

					}
					RoleDetails = RoleDetails.replaceAll(",$", "");
					CreatedGroupMapDetails.put("Role", RoleDetails);
				}							
			}

			return CreatedGroupMapDetails;


		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}


	public static boolean compareMaps(Map<String , String> actualMap , Map<String , String> expectedMap ,int index){
		try {

			for (String k : actualMap.keySet())
			{
				//Verifying the actual key value with the Expected Key value k=key(Client Name) and actualMap.get(k)="Viteos" verifying the Client and Funs family
				if (!actualMap.get(k).equalsIgnoreCase(expectedMap.get(k))) 
				{
					if(k.equalsIgnoreCase("Legal Entity") || k.equalsIgnoreCase("Role"))
					{
						//For Legal Entity and Role there are Multiple values so checking the every value in Map as if the values are swapped == condition is not working
						List<String> subListExpected = Arrays.asList(expectedMap.get(k).split(","));

						List<String> subListActual = Arrays.asList(actualMap.get(k).split(","));

						//if both subList are Not equal in size than no need to check for individual values
						if(subListActual.size()!=subListExpected.size())
						{
							Messages.errorMsg = k+" actual Values '["+subListActual+"]' Count "+subListActual.size()+" are Not Matching with the Expected Values '["+subListExpected+"]' Count "+subListExpected.size()+" Count ";
							//If return false than Legal Entity or Role values are not equal in size thann it won't check for All condition Refer the below two line comment
							//return false;
						}

						//Compare the sublist
						for(int i=0;i<subListExpected.size();i++)
						{
							if(!subListActual.contains(subListExpected.get(i)))
							{
								/*if there is only one or Two Legal Entities for selected fund family than in Excel we may give as individual Names but actual map get that value
								 * as "All" from Application so compare will fail
								 */

								if(k.equalsIgnoreCase("Legal Entity") && subListActual.get(i).equalsIgnoreCase("All"))
								{
									int legalEntityXpathCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='legalEntity"+index+"']//option[@selected='selected']"));
									if(legalEntityXpathCount==subListExpected.size()){
										break;
									}
								}

								if(k.equalsIgnoreCase("Role") && subListActual.get(i).equalsIgnoreCase("All"))
								{
									int roleDetailsCount = Elements.getXpathCount(Global.driver, By.xpath("//select[@id='role"+index+"']//option[@selected='selected']"));
									if(roleDetailsCount==subListExpected.size()){
										break;
									}
								}

								Messages.errorMsg = k+" Expected Value "+subListExpected.get(i)+" is Not there in the List box";
								return false;
							}
						}


						continue;
					}

					Messages.errorMsg = k+" actual value "+actualMap.get(k)+" is Not matching with the Expected value "+expectedMap.get(k);
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



}
