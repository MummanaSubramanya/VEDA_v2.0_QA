package com.viteos.veda.master.lib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.tenx.framework.lib.Elements;
import com.tenx.framework.lib.Messages;
import com.tenx.framework.lib.Wait;

public class RoleAppFunctions {

	static boolean bStatus;

	public static boolean createaNewRole(Map<String, String> mapRoleDetails){
		try{

			Thread.sleep(3000);
			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//li[text()='Role']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Role portal is not visible on clicking on expand button";
				return false;
			}

			if(mapRoleDetails.get("RoleCode")!=null){
				bStatus = Elements.enterText(Global.driver, By.id("roleCode"),mapRoleDetails.get("RoleCode"));
				if(!bStatus){
					Messages.errorMsg = "Cannot entertext in the rolecode box";
					return false;
				}
			}

			if(mapRoleDetails.get("RoleDescription")!=null){
				bStatus = Elements.enterText(Global.driver, By.id("roleDescription"),mapRoleDetails.get("RoleDescription"));
				if(!bStatus){
					Messages.errorMsg = "Cannot entertext in the Role Description box";
					return false;
				}
			}

			if(mapRoleDetails.get("DashBoard")!=null){
				bStatus = selectRoleActivityForParents("DashBoard", mapRoleDetails.get("DashBoard"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Client")!=null){
				bStatus = selectRoleActivityForParents("Client", mapRoleDetails.get("Client"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Fund Family")!=null){
				bStatus = selectRoleActivityForParents("Fund Family", mapRoleDetails.get("Fund Family"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Legal Entity")!=null){
				bStatus = selectRoleActivityForParents("Legal Entity", mapRoleDetails.get("Legal Entity"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Class")!=null){
				bStatus = selectRoleActivityForParents("Class", mapRoleDetails.get("Class"));
				if(!bStatus){
					return false;
				}
			}
			
			if(mapRoleDetails.get("Formula")!=null){
				bStatus = selectRoleActivityForParents("Formula", mapRoleDetails.get("Formula"));
				if(!bStatus){
					return false;
				}
			}
			
			if(mapRoleDetails.get("Parameter")!=null){
				bStatus = selectRoleActivityForParents("Parameter", mapRoleDetails.get("Parameter"));
				if(!bStatus){
					return false;
				}
			}
			
			if(mapRoleDetails.get("Investor")!=null){
				bStatus = selectRoleActivityForParents("Investor", mapRoleDetails.get("Investor"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Investor Holder")!=null){
				bStatus = selectRoleActivityForParents("Investor Holder", mapRoleDetails.get("Investor Holder"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Investor Account")!=null){
				bStatus = selectRoleActivityForParents("Investor Account", mapRoleDetails.get("Investor Account"));
				if(!bStatus){
					return false;
				}
			}
			
			if(mapRoleDetails.get("Subscription")!=null){
				bStatus = selectRoleActivityForParents("Subscription", mapRoleDetails.get("Subscription"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Redemption")!=null){
				bStatus = selectRoleActivityForParents("Redemption", mapRoleDetails.get("Redemption"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Transfer")!=null){
				bStatus = selectRoleActivityForParents("Transfer", mapRoleDetails.get("Transfer"));
				if(!bStatus){
					return false;
				}
			}
			
			if(mapRoleDetails.get("Fee Distribution")!=null){
				bStatus = selectRoleActivityForParents("Fee Distribution", mapRoleDetails.get("Fee Distribution"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Switch")!=null){
				bStatus = selectRoleActivityForParents("Switch", mapRoleDetails.get("Switch"));
				if(!bStatus){
					return false;
				}
			}
			
			
			if(mapRoleDetails.get("Exchange")!=null){
				bStatus = selectRoleActivityForParents("Exchange", mapRoleDetails.get("Exchange"));
				if(!bStatus){
					return false;
				}
			}
			
			if(mapRoleDetails.get("Allocation")!=null){
				bStatus = selectRoleActivityForParents("Allocation", mapRoleDetails.get("Allocation"));
				if(!bStatus){
					return false;
				}
			}
			
			if(mapRoleDetails.get("Management Fee")!=null){
				bStatus = selectRoleActivityForParents("Management Fee", mapRoleDetails.get("Management Fee"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Incentive Fee")!=null){
				bStatus = selectRoleActivityForParents("Incentive Fee", mapRoleDetails.get("Incentive Fee"));
				if(!bStatus){
					return false;
				}
			}


			if(mapRoleDetails.get("Bank Master")!=null){
				bStatus = selectRoleActivityForParents("Bank Master", mapRoleDetails.get("Bank Master"));
				if(!bStatus){
					return false;
				}
			}
			

			if(mapRoleDetails.get("Trade")!=null){
				bStatus = selectRoleActivityForParents("Trade", mapRoleDetails.get("Trade"));
				if(!bStatus){
					return false;
				}
			}

			if(mapRoleDetails.get("Feeder Subscription")!=null){
				bStatus = selectRoleActivityForParents("Feeder Subscription", mapRoleDetails.get("Feeder Subscription"));
				if(!bStatus){
					return false;
				}
			}			

			if(mapRoleDetails.get("Feeder Redemption")!=null){
				bStatus = selectRoleActivityForParents("Feeder Redemption", mapRoleDetails.get("Feeder Redemption"));
				if(!bStatus){
					return false;
				}
			}
			
			if(mapRoleDetails.get("Series")!=null){
				bStatus = selectRoleActivityForParents("Series", mapRoleDetails.get("Series"));
				if(!bStatus){
					return false;
				}
			}
			
			if(mapRoleDetails.get("OperationType")!=null){
				bStatus = NewUICommonFunctions.doOperationOnUserManagementSubMasters("roleButtonDiv", mapRoleDetails.get("OperationType"));
			}




			return bStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean selectRoleActivityForParents(String sParentname,String ActivitiesList){
		try{
			int iParentIndex = getRowCountNumberBasedonParentName(sParentname);

			bStatus = removeAllCheckedActivities(sParentname , iParentIndex);
			if(!bStatus){
				return false;
			}

			//int iParentIndex = getRowCountNumberBasedonParentName(sParentname);

			String[] arrActivitiesArray =  ActivitiesList.split(",");
			String RowLocator = "//div[@id='roleAddDiv']/div[@id='dataDiv']/table/tbody/tr["+iParentIndex+"]";
			bStatus = moveToElementUsingActions(By.xpath(RowLocator)); 
			
			bStatus = NewUICommonFunctions.spinnerClick(Global.driver, By.xpath(RowLocator));
			if(!bStatus){
				return false;

			}
			for (int iActivityCounter = 0; iActivityCounter < arrActivitiesArray.length; iActivityCounter++) {



				//checking the search box
				if(arrActivitiesArray[iActivityCounter].equalsIgnoreCase("Search")){
					By searchLocator = By.xpath(RowLocator+"/td[2]//input");
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
					if(!bStatus){
						bStatus = Elements.click(Global.driver, searchLocator);
						//bStatus = NewUICommonFunctions.jsClick(searchLocator);
						if(bStatus){
							String sAttribute = Elements.getElementAttribute(Global.driver, searchLocator, "disabled");
							if(sAttribute!=null && sAttribute.equalsIgnoreCase("true")){
								Messages.errorMsg = "Cannot click on the "+sParentname+" Search checkbox as it in disabled mode";
								return false;
							}				
							continue;
						}
						Messages.errorMsg = "Cannot click on the "+sParentname+" Search checkbox";
						return false;					
					}
				}

				//checking the add check box
				if(arrActivitiesArray[iActivityCounter].equalsIgnoreCase("Add")){
					By searchLocator = By.xpath(RowLocator+"/td[3]//input");
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
					if(!bStatus){
						bStatus = Elements.click(Global.driver, searchLocator);
						//bStatus = NewUICommonFunctions.jsClick(searchLocator);
						if(bStatus){
							String sAttribute = Elements.getElementAttribute(Global.driver, searchLocator, "disabled");
							if(sAttribute!=null && sAttribute.equalsIgnoreCase("true")){
								Messages.errorMsg = "Cannot click on the "+sParentname+" Add checkbox as it in disabled mode";
								return false;
							}			
							continue;
						}
						Messages.errorMsg = "Cannot click on the "+sParentname+" Add checkbox";
						return false;
					}
				}

				if(arrActivitiesArray[iActivityCounter].equalsIgnoreCase("View")){
					By searchLocator = By.xpath(RowLocator+"/td[4]//input");
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
					if(!bStatus){
						bStatus = Elements.click(Global.driver, searchLocator);
						//bStatus = NewUICommonFunctions.jsClick(searchLocator);
						if(bStatus){
							String sAttribute = Elements.getElementAttribute(Global.driver, searchLocator, "disabled");
							if(sAttribute!=null && sAttribute.equalsIgnoreCase("true")){
								Messages.errorMsg = "Cannot click on the "+sParentname+" View checkbox as it in disabled mode";
								return false;
							}				
							continue;
						}
						Messages.errorMsg = "Cannot click on the "+sParentname+" View checkbox";
						return false;
					}
				}

				if(arrActivitiesArray[iActivityCounter].equalsIgnoreCase("Edit")){
					By searchLocator = By.xpath(RowLocator+"/td[5]//input");
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
					if(!bStatus){
						bStatus = Elements.click(Global.driver, searchLocator);
						//bStatus = NewUICommonFunctions.jsClick(searchLocator);
						if(bStatus){
							String sAttribute = Elements.getElementAttribute(Global.driver, searchLocator, "disabled");
							if(sAttribute!=null && sAttribute.equalsIgnoreCase("true")){
								Messages.errorMsg = "Cannot click on the "+sParentname+" Edit checkbox as it in disabled mode";
								return false;
							}
							continue;						
						}
						Messages.errorMsg = "Cannot click on "+sParentname+" the Edit checkbox";
						return false;
					}
				}

				if(arrActivitiesArray[iActivityCounter].equalsIgnoreCase("Approve")){
					By searchLocator = By.xpath(RowLocator+"/td[6]//input");
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
					if(!bStatus){
						bStatus = Elements.click(Global.driver, searchLocator);
						//bStatus = NewUICommonFunctions.jsClick(searchLocator);
						if(bStatus){
							String sAttribute = Elements.getElementAttribute(Global.driver, searchLocator, "disabled");
							if(sAttribute!=null && sAttribute.equalsIgnoreCase("true")){
								Messages.errorMsg = "Cannot click on the "+sParentname+" Approve checkbox as it in disabled mode";
								return false;
							}
							continue;
						}
						Messages.errorMsg = "Cannot click on the "+sParentname+" Approve checkbox";
						return false;
					}			
				}

				if(arrActivitiesArray[iActivityCounter].equalsIgnoreCase("Activate")){
					By searchLocator = By.xpath(RowLocator+"/td[7]//input");
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
					if(!bStatus){
						bStatus = Elements.click(Global.driver, searchLocator);
						//bStatus = NewUICommonFunctions.jsClick(searchLocator);
						if(bStatus){
							String sAttribute = Elements.getElementAttribute(Global.driver, searchLocator, "disabled");
							if(sAttribute!=null && sAttribute.equalsIgnoreCase("true")){
								Messages.errorMsg = "Cannot click on the "+sParentname+" Activate checkbox as it in disabled mode";
								return false;
							}
							continue;
						}
						Messages.errorMsg = "Cannot click on the "+sParentname+" Activate checkbox";
						return false;
					}			
				}

				if(arrActivitiesArray[iActivityCounter].equalsIgnoreCase("Deactivate")){
					By searchLocator = By.xpath(RowLocator+"/td[8]//input");
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
					if(!bStatus){
						bStatus = Elements.click(Global.driver, searchLocator);
						//bStatus = NewUICommonFunctions.jsClick(searchLocator);
						if(bStatus){
							String sAttribute = Elements.getElementAttribute(Global.driver, searchLocator, "disabled");
							if(sAttribute!=null && sAttribute.equalsIgnoreCase("true")){
								Messages.errorMsg = "Cannot click on the "+sParentname+" Deactivate checkbox as it in disabled mode";
								return false;
							}
							continue;
						}
						Messages.errorMsg = "Cannot click on the "+sParentname+" Deactivate checkbox";
						return false;
					}
				}
				if(arrActivitiesArray[iActivityCounter].equalsIgnoreCase("Delete")){
					By searchLocator = By.xpath(RowLocator+"/td[9]//input");
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
					if(!bStatus){
						bStatus = Elements.click(Global.driver, searchLocator);
						//bStatus = NewUICommonFunctions.jsClick(searchLocator);
						if(bStatus){
							String sAttribute = Elements.getElementAttribute(Global.driver, searchLocator, "disabled");
							if(sAttribute!=null && sAttribute.equalsIgnoreCase("true")){
								Messages.errorMsg = "Cannot click on the "+sParentname+" Delete checkbox as it in disabled mode";
								return false;
							}
							continue;
						}
						Messages.errorMsg = "Cannot click on the "+sParentname+" Delete checkbox";
						return false;
					}
				}

				if(arrActivitiesArray[iActivityCounter].equalsIgnoreCase("Cancel")){
					By searchLocator = By.xpath(RowLocator+"/td[10]//input");
					bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
					if(!bStatus){
						bStatus = Elements.click(Global.driver, searchLocator);
						//bStatus = NewUICommonFunctions.jsClick(searchLocator);
						if(bStatus){
							String sAttribute = Elements.getElementAttribute(Global.driver, searchLocator, "disabled");
							if(sAttribute!=null && sAttribute.equalsIgnoreCase("true")){
								Messages.errorMsg = "Cannot click on the "+sParentname+" Cancel checkbox as it in disabled mode";
								return false;
							}
							continue;
						}
						Messages.errorMsg = "Cannot click on the "+sParentname+" Cancel checkbox";
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

	private static int getRowCountNumberBasedonParentName(String sParentname) {
		try{

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='roleAddDiv']/div[@id='dataDiv']/table/tbody/tr"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Table is Not visible";
				return 0;
			}

			int iRowCount = Elements.getXpathCount(Global.driver,By.xpath("//div[@id='roleAddDiv']/div[@id='dataDiv']/table/tbody/tr"));
			if(iRowCount == 0){
				Messages.errorMsg = "No Parents are available";
				return 0;
			}

			for (int iCount = 0; iCount < iRowCount; iCount++) {
				String actParentName = Elements.getText(Global.driver,By.xpath("//div[@id='roleAddDiv']/div[@id='dataDiv']/table/tbody/tr["+iCount+"]/td[1]"));
				if(actParentName!=null){
					actParentName = actParentName.trim();
					if(actParentName.equalsIgnoreCase(sParentname)){
						return iCount;
					}
				}
			}
			return 0;
		}
		catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}

	private static boolean removeAllCheckedActivities(String sParentName , int iParentIndex){
		try{

			//int iParentIndex = getRowCountNumberBasedonParentName(sParentName);
			if(iParentIndex==0){
				return false;
			}


			String RowLocator = "//div[@id='roleAddDiv']/div[@id='dataDiv']/table/tbody/tr["+iParentIndex+"]";

			//unchecking the search box
			By searchLocator = By.xpath(RowLocator+"/td[2]//input");
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
			if(bStatus){
				bStatus = Elements.click(Global.driver, searchLocator);
				if(!bStatus){
					Messages.errorMsg = "Cannot click on the search checkbox";
				}
			}


			//unchecking the add check box
			By addLocator = By.xpath(RowLocator+"/td[3]//input");
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver,addLocator);
			if(bStatus){
				bStatus = Elements.click(Global.driver, addLocator);
				if(!bStatus){
					String sAttribute = Elements.getElementAttribute(Global.driver, addLocator, "disabled");
					if(sAttribute!=null && sAttribute.equalsIgnoreCase("disabled")){
						return true;
					}
					Messages.errorMsg = "Cannot click on the Add checkbox";
					return false;
				}
			}

			//unchecking view
			By ViewLocator = By.xpath(RowLocator+"/td[4]//input");
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver,ViewLocator);
			if(bStatus){
				bStatus = Elements.click(Global.driver, ViewLocator);
				if(!bStatus){
					String sAttribute = Elements.getElementAttribute(Global.driver, ViewLocator, "disabled");
					if(sAttribute!=null && sAttribute.equalsIgnoreCase("disabled")){
						return true;
					}
					Messages.errorMsg = "Cannot click on the View checkbox";
					return false;
				}
			}

			//unchecking the edit
			By EditLocator = By.xpath(RowLocator+"/td[5]//input");
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver,EditLocator);
			if(bStatus){
				bStatus = Elements.click(Global.driver, EditLocator);
				if(!bStatus){
					String sAttribute = Elements.getElementAttribute(Global.driver, EditLocator, "disabled");
					if(sAttribute!=null && sAttribute.equalsIgnoreCase("disabled")){
						return true;
					}
					Messages.errorMsg = "Cannot click on the Edit checkbox";
					return false;
				}
			}

			//uncheck the approve
			By ApproveLocator = By.xpath(RowLocator+"/td[6]//input");
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver,ApproveLocator);
			if(bStatus){
				bStatus = Elements.click(Global.driver, ApproveLocator);
				if(!bStatus){
					String sAttribute = Elements.getElementAttribute(Global.driver, ApproveLocator, "disabled");
					if(sAttribute!=null && sAttribute.equalsIgnoreCase("disabled")){
						return true;
					}
					Messages.errorMsg = "Cannot click on the Approve checkbox";
					return false;
				}
			}

			//uncheck the activate
			By ActivateLocator = By.xpath(RowLocator+"/td[7]//input");
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver,ActivateLocator);
			if(bStatus){
				bStatus = Elements.click(Global.driver, ActivateLocator);
				if(!bStatus){
					String sAttribute = Elements.getElementAttribute(Global.driver, ActivateLocator, "disabled");
					if(sAttribute!=null && sAttribute.equalsIgnoreCase("disabled")){
						return true;
					}
					Messages.errorMsg = "Cannot click on the Activate checkbox";
					return false;
				}
			}


			// uncheck the deactivate
			By deactivateLocator = By.xpath(RowLocator+"/td[8]//input");
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver,deactivateLocator);
			if(bStatus){
				bStatus = Elements.click(Global.driver, deactivateLocator);
				if(!bStatus){
					String sAttribute = Elements.getElementAttribute(Global.driver, deactivateLocator, "disabled");
					if(sAttribute!=null && sAttribute.equalsIgnoreCase("disabled")){
						return true;
					}
					Messages.errorMsg = "Cannot click on the Deactivate checkbox";
					return false;
				}
			}

			// uncheck the deactivate
			By deleteLocator = By.xpath(RowLocator+"/td[9]//input");
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver,deleteLocator);
			if(bStatus){
				bStatus = Elements.click(Global.driver, deleteLocator);
				if(!bStatus){
					String sAttribute = Elements.getElementAttribute(Global.driver, deleteLocator, "disabled");
					if(sAttribute!=null && sAttribute.equalsIgnoreCase("disabled")){
						return true;
					}
					Messages.errorMsg = "Cannot click on the delete checkbox";
					return false;
				}
			}

			// uncheck the deactivate
			By cancelLocator = By.xpath(RowLocator+"/td[10]//input");
			bStatus = NewUICommonFunctions.verifyChecked(Global.driver,cancelLocator);
			if(bStatus){
				bStatus = Elements.click(Global.driver, cancelLocator);
				if(!bStatus){
					String sAttribute = Elements.getElementAttribute(Global.driver, cancelLocator, "disabled");
					if(sAttribute!=null && sAttribute.equalsIgnoreCase("disabled")){
						return true;
					}
					Messages.errorMsg = "Cannot click on the Cancel checkbox";
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

	public static boolean verifyTheCreatedRole(Map<String, String> mapRoleDetails){
		try{
			String ErrorMsg = "";
			boolean bVerifyStatus = true;

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//li[text()='Role']"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Role portal is not visible on clicking on Edit button";
				return false;
			}

			if(mapRoleDetails.get("RoleCode")!=null){

				String actualValue = NewUICommonFunctions.jsGetElementAttribute("roleCode");
				if(!actualValue.equalsIgnoreCase(mapRoleDetails.get("RoleCode"))){
					ErrorMsg= ErrorMsg+"[ Role code actual value "+actualValue+" is not matching with the Expected "+mapRoleDetails.get("RoleCode")+"]";
					bVerifyStatus =  false;
				}
			}

			if(mapRoleDetails.get("RoleDescription")!=null){
				String actualValue = NewUICommonFunctions.jsGetElementAttribute("roleDescription");
				if(!actualValue.equalsIgnoreCase(mapRoleDetails.get("RoleDescription"))){
					ErrorMsg= ErrorMsg+"[ Role Description actual value "+actualValue+" is not matching with the Expected "+mapRoleDetails.get("RoleDescription")+"]";
					bVerifyStatus =  false;
				}
			} 

			Map<String, Map<String, String>> actualCheckedMap = getAllCheckedBoxesFromRoleModifyScreen();


			for (Map.Entry<String, Map<String, String>> entry : actualCheckedMap.entrySet()) {

				String sParentName = entry.getKey()	;		
				String ExpActivitiesList = mapRoleDetails.get(sParentName);
				if(ExpActivitiesList==null){
					ExpActivitiesList = "";
				}

				String actActivitiesList = getActivitiesInStringFormatIfChecked(entry.getValue());

				boolean condition = ExpActivitiesList.equals("") && actActivitiesList.equals("");
				if(condition){
					continue;
				}

				if(!sParentName.equalsIgnoreCase("Feeder Subscription") && !sParentName.equalsIgnoreCase("Feeder Redemption") && !sParentName.equalsIgnoreCase("Subscription") && !sParentName.equalsIgnoreCase("Redemption") && !sParentName.equalsIgnoreCase("Transfer") && !sParentName.equalsIgnoreCase("Switch") && !sParentName.equalsIgnoreCase("Exchange") ){
					String tempExpActivitiesList = ExpActivitiesList;
					if(ExpActivitiesList.contains("Add")||ExpActivitiesList.contains("Edit")||ExpActivitiesList.contains("Activate")||ExpActivitiesList.contains("Deactivate")||ExpActivitiesList.contains("Approve") || ExpActivitiesList.contains("Delete") || ExpActivitiesList.contains("Cancel")){
						tempExpActivitiesList = tempExpActivitiesList.replace("Search", "").replace("View", "");
						tempExpActivitiesList = tempExpActivitiesList.replaceFirst("^,","");
						tempExpActivitiesList = tempExpActivitiesList.replace(",,",",");
						tempExpActivitiesList = tempExpActivitiesList.replaceAll(",$", "");
						tempExpActivitiesList = "Search,View,"+tempExpActivitiesList;
						tempExpActivitiesList = removeUnwantedCommas(tempExpActivitiesList);
					}

					ExpActivitiesList = tempExpActivitiesList;
				}


				if(!condition && ExpActivitiesList.length()==actActivitiesList.length()){
					List<String> listExpActivities = Arrays.asList(ExpActivitiesList.split(",")); 

					String[] actActivity = actActivitiesList.split(",");
					for (int i = 0; i < actActivity.length; i++) {
						if(!listExpActivities.contains(actActivity[i])){
							ErrorMsg = ErrorMsg+"[ In Menu Name: "+sParentName+", "+actActivity[i]+" is checked but expected is it should not be checked ]";
							bVerifyStatus = false;
						}
					}
					continue;
				}

				if(ExpActivitiesList.length() > actActivitiesList.length()){
					List<String> actListActivities = Arrays.asList(actActivitiesList.split(",")); 

					String[] ExpActivity = ExpActivitiesList.split(",");
					for (int i = 0; i < ExpActivity.length; i++) {
						if(!actListActivities.contains(ExpActivity[i])){
							ErrorMsg = ErrorMsg+"[ In Menu Name: "+sParentName+", "+ExpActivity[i]+" is not checked but expected is checked ]";
							bVerifyStatus = false;
						}
					}
					continue;
				}

				if(ExpActivitiesList.length() < actActivitiesList.length()){
					List<String> listExpActivities = Arrays.asList(ExpActivitiesList.split(",")); 

					String[] actActivity = actActivitiesList.split(",");
					for (int i = 0; i < actActivity.length; i++) {
						if(!listExpActivities.contains(actActivity[i])){
							ErrorMsg = ErrorMsg+"[ In Menu Name: "+sParentName+", "+actActivity[i]+" is checked but expected is it should not be checked ]";
							bVerifyStatus = false;
						}
					}
					continue;
				}
			}
			Messages.errorMsg = ErrorMsg;
			return bVerifyStatus;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}


	public static String getActivitiesInStringFormatIfChecked(Map<String, String> activitiesMap){

		String returnString = "";
		for (Map.Entry<String,String> entry : activitiesMap.entrySet()) {
			String ActivityName = entry.getKey();
			String ActivityStatus = entry.getValue();

			if(ActivityStatus.equalsIgnoreCase("Yes")){
				returnString = returnString+","+ActivityName;

			}
		}
		returnString = returnString.replaceFirst("^,","");
		return returnString;

	}

	public static Map<String, Map<String, String>> getAllCheckedBoxesFromRoleModifyScreen(){
		try{

			Map<String, Map<String, String>> actActivitiesMap = new HashMap<>();

			bStatus = Wait.waitForElementVisibility(Global.driver, By.xpath("//div[@id='roleAddDiv']/div[@id='dataDiv']/table/tbody/tr"), Constants.lTimeOut);
			if(!bStatus){
				Messages.errorMsg = "Table is Not visible";
				return actActivitiesMap;
			}

			int iRowCount = Elements.getXpathCount(Global.driver,By.xpath("//div[@id='roleAddDiv']/div[@id='dataDiv']/table/tbody/tr"));
			if(iRowCount == 0){
				Messages.errorMsg = "No Parents are available";
				return actActivitiesMap;
			}

			for (int iCount = 1; iCount <= iRowCount; iCount++) {		
				Map<String, String> activityMap = new HashMap<String, String>();
				String iColLocator = "//div[@id='roleAddDiv']/div[@id='dataDiv']/table/tbody/tr["+iCount+"]";

				//check for search
				By searchLocator = By.xpath(iColLocator+"/td[2]//input");
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,searchLocator);
				if(!bStatus){
					activityMap.put("Search","No");
				}
				else{
					activityMap.put("Search","Yes");
				}

				//check for add
				By addLocator = By.xpath(iColLocator+"/td[3]//input");
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,addLocator);
				if(!bStatus){
					activityMap.put("Add","No");
				}
				else{
					activityMap.put("Add","Yes");
				}

				//check for view
				By viewLocator = By.xpath(iColLocator+"/td[4]//input");
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,viewLocator);
				if(!bStatus){
					activityMap.put("View","No");
				}
				else{
					activityMap.put("View","Yes");
				}

				//check for Edit
				By editLocator = By.xpath(iColLocator+"/td[5]//input");
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,editLocator);
				if(!bStatus){
					activityMap.put("Edit","No");
				}
				else{
					activityMap.put("Edit","Yes");
				}

				//check for Approve
				By approveLocator = By.xpath(iColLocator+"/td[6]//input");
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,approveLocator);
				if(!bStatus){
					activityMap.put("Approve","No");
				}
				else{
					activityMap.put("Approve","Yes");
				}

				//check for Activate
				By activateLocator = By.xpath(iColLocator+"/td[7]//input");
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,activateLocator);
				if(!bStatus){
					activityMap.put("Activate","No");
				}
				else{
					activityMap.put("Activate","Yes");
				}

				//check for Deactivate
				By deactivateLocator = By.xpath(iColLocator+"/td[8]//input");
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,deactivateLocator);
				if(!bStatus){
					activityMap.put("Deactivate","No");
				}
				else{
					activityMap.put("Deactivate","Yes");
				}

				//check for Deactivate
				By deleteLocator = By.xpath(iColLocator+"/td[9]//input");
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,deleteLocator);
				if(!bStatus){
					activityMap.put("Delete","No");
				}
				else{
					activityMap.put("Delete","Yes");
				}

				//check for Deactivate
				By cancelLocator = By.xpath(iColLocator+"/td[10]//input");
				bStatus = NewUICommonFunctions.verifyChecked(Global.driver,cancelLocator);
				if(!bStatus){
					activityMap.put("Cancel","No");
				}
				else{
					activityMap.put("Cancel","Yes");
				}

				String actParentName = Elements.getText(Global.driver,By.xpath("//div[@id='roleAddDiv']/div[@id='dataDiv']/table/tbody/tr["+iCount+"]/td[1]"));

				actActivitiesMap.put(actParentName.trim(), activityMap);
			}

			return actActivitiesMap;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static boolean moveToElementUsingActions(By locator){
		try {

			JavascriptExecutor js = (JavascriptExecutor) Global.driver;
			js.executeScript("scroll(250, 0);");

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}

	public static String removeUnwantedCommas(String valueToRemove){
		try {
			valueToRemove = valueToRemove.replace(",,",",");
			valueToRemove = valueToRemove.replaceFirst("^,","");			
			valueToRemove = valueToRemove.replaceAll(",$", "");

			return valueToRemove;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}

}

