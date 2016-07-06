package com.viteos.veda.testsuites;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import com.viteos.veda.config.ExcelUtils;
import com.viteos.veda.master.lib.Global;

public class FinalSuiteConfiguration {	
	public static void main(String [] args) {
		try {			
			System.out.println("Entered into suites configuration file...........!!!");

			Map<String, String> mapForScriptsToExecute = ExcelUtils.readDataABasedOnCellName(Global.globalTestDataFilePath, "TestSuitesConfiguration", "Y");
			System.out.println("Scripts to map with suites for execution : **********   "+mapForScriptsToExecute+"   ******************");

			//set of test suites to be run
			List<XmlSuite> suites = new ArrayList<XmlSuite>();

			XmlSuite suite = new XmlSuite();
			suite.setName("ViteosTestSuite");

			suites.add(suite);
			
			// Adding All the suites based on their execution status updated in the Excel file : "GlobalTestData" and within the sheet : "TestSuitesConfiguration".
			if (mapForScriptsToExecute != null && !mapForScriptsToExecute.isEmpty()) {
				//Client Scripts Suite.
				XmlSuite suite1 = new XmlSuite();
				suite1.setName("ClientMasterSuite");
				XmlTest test1 = new XmlTest(suite);
				test1.setName("ClientMasterTests");
				List<XmlClass> classes1 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Client Add Script") != null && mapForScriptsToExecute.get("Client Add Script").equalsIgnoreCase("Yes")) {
					classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_MakerOperations_TC1"));
				}
				if (mapForScriptsToExecute.get("ClientAddCheckerOperation") != null && mapForScriptsToExecute.get("ClientAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_CheckerOperations_TC2"));
				}
				if (mapForScriptsToExecute.get("Client Modify Script") != null && mapForScriptsToExecute.get("Client Modify Script").equalsIgnoreCase("Yes")) {
					classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ModifyMakerOperations_TC3"));					
				}
				if (mapForScriptsToExecute.get("ClientModifyCheckerOperation") != null && mapForScriptsToExecute.get("ClientModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ModifyCheckerOperations_TC4"));					
				}
				if (mapForScriptsToExecute.get("Client Deactivate Script") != null && mapForScriptsToExecute.get("Client Deactivate Script").equalsIgnoreCase("Yes")) {
					classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_DeactivateMakerOperations_TC5"));
				}
				if (mapForScriptsToExecute.get("ClientDeActivateCheckerOperation") != null && mapForScriptsToExecute.get("ClientDeActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_DeactivateCheckerOperations_TC6"));
				}
				if (mapForScriptsToExecute.get("Client Activate Script") != null && mapForScriptsToExecute.get("Client Activate Script").equalsIgnoreCase("Yes")) {
					classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ActivateMakerOperations_TC7"));
				}
				if (mapForScriptsToExecute.get("ClientActivateCheckerOperation") != null && mapForScriptsToExecute.get("ClientActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ActivateCheckerOperations_TC8"));
				}
				if (!classes1.isEmpty()) {
					test1.setXmlClasses(classes1);
					suites.add(suite1);
				}

				//Fund Family Scripts Suite.
				XmlSuite suite2 = new XmlSuite();
				suite2.setName("FundFamilyMasterSuite");
				XmlTest test2 = new XmlTest(suite);
				test2.setName("FundFamilyMasterTests");
				List<XmlClass> classes2 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Fund Family Add Script") != null && mapForScriptsToExecute.get("Fund Family Add Script").equalsIgnoreCase("Yes")) {
					classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_MakerOperations_TC1"));
				}
				if (mapForScriptsToExecute.get("FundFamilyAddCheckerOperation") != null && mapForScriptsToExecute.get("FundFamilyAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_CheckerOperations_TC2"));
				}
				if (mapForScriptsToExecute.get("Fund Family Modify Script") != null && mapForScriptsToExecute.get("Fund Family Modify Script").equalsIgnoreCase("Yes")) {
					classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ModifyMakerOperations_TC3"));					
				}
				if (mapForScriptsToExecute.get("FundFamilyModifyCheckerOperation") != null && mapForScriptsToExecute.get("FundFamilyModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ModifyCheckerOperations_TC4"));					
				}
				if (mapForScriptsToExecute.get("Fund Family Deactivate Script") != null && mapForScriptsToExecute.get("Fund Family Deactivate Script").equalsIgnoreCase("Yes")) {
					classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_DeactivateMakerOperations_TC5"));
				}
				if (mapForScriptsToExecute.get("FundFamlyDeactivateCheckerOperation") != null && mapForScriptsToExecute.get("FundFamlyDeactivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_DeactivateCheckerOperations_TC6"));
				}
				if (mapForScriptsToExecute.get("Fund Family Activate Script") != null && mapForScriptsToExecute.get("Fund Family Activate Script").equalsIgnoreCase("Yes")) {
					classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ActivateMakerOperations_TC7"));
				}
				if (mapForScriptsToExecute.get("FundFamilyActivateCheckerOperation") != null && mapForScriptsToExecute.get("FundFamilyActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ActivateCheckerOperations_TC8"));
				}
				if (!classes2.isEmpty()) {
					test2.setXmlClasses(classes2);
					suites.add(suite2);
				}	

				//Legal Entity Scripts Suite.
				XmlSuite suite3 = new XmlSuite();
				suite3.setName("LegalEntityMasterSuite");
				XmlTest test3 = new XmlTest(suite);
				test3.setName("LegalEntityMasterTests");
				List<XmlClass> classes3 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("LE Master Add Script") != null && mapForScriptsToExecute.get("LE Master Add Script").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_MakerOperations_TC1"));
				}
				if (mapForScriptsToExecute.get("LEMasterAddCheckerOperation") != null && mapForScriptsToExecute.get("LEMasterAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_CheckerOperations_TC2"));
				}
				if (mapForScriptsToExecute.get("LE Feeder Add Script") != null && mapForScriptsToExecute.get("LE Feeder Add Script").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityFeeder_MakerOperations_TC3"));
				}
				if (mapForScriptsToExecute.get("LEFeederAddCheckerOperation") != null && mapForScriptsToExecute.get("LEFeederAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityFeeder_CheckerOperations_TC4"));
				}
				if (mapForScriptsToExecute.get("LE Modify Script") != null && mapForScriptsToExecute.get("LE Modify Script").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_ModifyMakerOperations_TC5"));
				}
				if (mapForScriptsToExecute.get("LEModifyCheckerOperation") != null && mapForScriptsToExecute.get("LEModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_ModifyCheckerOperations_TC6"));
				}
				if (mapForScriptsToExecute.get("LE Clone Verification Script") != null && mapForScriptsToExecute.get("LE Clone Verification Script").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_VerifyClonedValues_TC7"));
				}
				if (mapForScriptsToExecute.get("LE Deactivate Script") != null && mapForScriptsToExecute.get("LE Deactivate Script").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_MakerDeactivateOperations_TC8"));
				}
				if (mapForScriptsToExecute.get("LEDeactivateCheckerOperation") != null && mapForScriptsToExecute.get("LEDeactivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_CheckerDeactivateOperations_TC9"));
				}
				if (mapForScriptsToExecute.get("LE Aactivate Script") != null && mapForScriptsToExecute.get("LE Aactivate Script").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_MakerActivateOperations_TC10"));
				}
				if (mapForScriptsToExecute.get("LEActivateCheckerOperation") != null && mapForScriptsToExecute.get("LEActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_CheckerActivateOperations_TC11"));
				}
				if (!classes3.isEmpty()) {
					test3.setXmlClasses(classes3);
					suites.add(suite3);
				}


				//Class Scripts Suite.
				XmlSuite suite4 = new XmlSuite();
				suite4.setName("ClassMasterSuite");
				XmlTest test4 = new XmlTest(suite);
				test4.setName("ClassMasterTests");
				List<XmlClass> classes4 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Class Master Add Script") != null && mapForScriptsToExecute.get("Class Master Add Script").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_MakerOperations_TC1"));
				}
				if (mapForScriptsToExecute.get("ClassMasterAddCheckerOperation") != null && mapForScriptsToExecute.get("ClassMasterAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_CheckerOperations_TC2"));
				}
				if (mapForScriptsToExecute.get("Class Clone Add Script") != null && mapForScriptsToExecute.get("Class Clone Add Script").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassClone_MakerOperations_TC3"));
				}
				if (mapForScriptsToExecute.get("ClassCloneAddCheckerOperation") != null && mapForScriptsToExecute.get("ClassCloneAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassClone_CheckerOperations_TC4"));
				}
				if (mapForScriptsToExecute.get("Class Modify Script") != null && mapForScriptsToExecute.get("Class Modify Script").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_ModifyMakerOperations_TC5"));
				}
				if (mapForScriptsToExecute.get("ClassModifyCheckerOperation") != null && mapForScriptsToExecute.get("ClassModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_ModifyCheckerOperations_TC6"));
				}
				if (mapForScriptsToExecute.get("Class And LE Clone Verification Script") != null && mapForScriptsToExecute.get("Class And LE Clone Verification Script").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_VerifyClonedAndLEDataForClass_TC7"));
				}
				if (mapForScriptsToExecute.get("Class Deactivate Script") != null && mapForScriptsToExecute.get("Class Deactivate Script").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_MakerDeactivateOperations_TC8"));
				}
				if (mapForScriptsToExecute.get("ClassDeactivateCheckerOperation") != null && mapForScriptsToExecute.get("ClassDeactivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_CheckerDeactivateOperations_TC9"));
				}
				if (mapForScriptsToExecute.get("Class Activate Script") != null && mapForScriptsToExecute.get("Class Activate Script").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_MakerActivateOperations_TC10"));
				}
				if (mapForScriptsToExecute.get("ClassActivateCheckerOperation") != null && mapForScriptsToExecute.get("ClassActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_CheckerActivateOperations_TC11"));
				}
				if (!classes4.isEmpty()) {
					test4.setXmlClasses(classes4);
					suites.add(suite4);
				}


				//Series Scripts Suite.
				XmlSuite suite5 = new XmlSuite();
				suite5.setName("SeriesMasterSuite");
				XmlTest test5 = new XmlTest(suite);
				test5.setName("SeriesMasterTests");
				List<XmlClass> classes5 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Series Add Script") != null && mapForScriptsToExecute.get("Series Add Script").equalsIgnoreCase("Yes")) {
					classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_MakerOperations_TC1"));
				}
				if (mapForScriptsToExecute.get("SeriesAddCheckerOperation") != null && mapForScriptsToExecute.get("SeriesAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_CheckerOperations_TC2"));
				}
				if (mapForScriptsToExecute.get("Series Modify Script") != null && mapForScriptsToExecute.get("Series Modify Script").equalsIgnoreCase("Yes")) {
					classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ModifyMakerOperations_TC3"));					
				}
				if (mapForScriptsToExecute.get("SeriesModifyCheckerOperation") != null && mapForScriptsToExecute.get("SeriesModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ModifyCheckerOperations_TC4"));					
				}
				if (mapForScriptsToExecute.get("Series Deactivate Script") != null && mapForScriptsToExecute.get("Series Deactivate Script").equalsIgnoreCase("Yes")) {
					classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_DeactivateMakerOperations_TC5"));
				}
				if (mapForScriptsToExecute.get("SeriesDeactivateCheckerOperation") != null && mapForScriptsToExecute.get("SeriesDeactivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_DeactivateCheckerOperaations_TC6"));
				}
				if (mapForScriptsToExecute.get("Series Activate Script") != null && mapForScriptsToExecute.get("Series Activate Script").equalsIgnoreCase("Yes")) {
					classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ActivateMakerOperations_TC7"));
				}
				if (mapForScriptsToExecute.get("SeriesActivateCheckerOperation") != null && mapForScriptsToExecute.get("SeriesActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ActivateCheckerOperations_TC8"));
				}
				if (!classes5.isEmpty()) {
					test5.setXmlClasses(classes5);
					suites.add(suite5);
				}

				//Formula scripts suite.
				XmlSuite suite6 = new XmlSuite();
				suite6.setName("FormulaMasterSuite");
				XmlTest test6 = new XmlTest(suite);
				test6.setName("FormulaMasterTests");
				List<XmlClass> classes6 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Formula Add Script") != null && mapForScriptsToExecute.get("Formula Add Script").equalsIgnoreCase("Yes")) {
					classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_MakerOperations_TC1"));
				}
				if (mapForScriptsToExecute.get("FormulaAddCheckerOperation") != null && mapForScriptsToExecute.get("FormulaAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_CheckerOperations_TC2"));
				}
				if (mapForScriptsToExecute.get("Formula Modify Script") != null && mapForScriptsToExecute.get("Formula Modify Script").equalsIgnoreCase("Yes")) {
					classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ModifyMakerOperations_TC3"));					
				}
				if (mapForScriptsToExecute.get("FormulaModifyCheckerOperation") != null && mapForScriptsToExecute.get("FormulaModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ModifyCheckerOperations_TC4"));					
				}
				if (mapForScriptsToExecute.get("Formula Deactivate Script") != null && mapForScriptsToExecute.get("Formula Deactivate Script").equalsIgnoreCase("Yes")) {
					classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_DeActivateMakerOperations_TC5"));
				}
				if (mapForScriptsToExecute.get("FormulaDeacivateCheckerOperation") != null && mapForScriptsToExecute.get("FormulaDeacivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_DeActivateCheckerOperations_TC6"));
				}
				if (mapForScriptsToExecute.get("Formula Activate Script") != null && mapForScriptsToExecute.get("Formula Activate Script").equalsIgnoreCase("Yes")) {
					classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ActivateMakerOperations_TC7"));
				}
				if (mapForScriptsToExecute.get("FormulaActivateCheckerOperation") != null && mapForScriptsToExecute.get("FormulaActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ActivateCheckerOperations_TC8"));
				}
				if (!classes6.isEmpty()) {
					test6.setXmlClasses(classes6);
					suites.add(suite6);
				}

				//Parameter scripts suite.
				XmlSuite suite7 = new XmlSuite();
				suite7.setName("ParameterMasterSuite");
				XmlTest test7 = new XmlTest(suite);
				test7.setName("ParameterMasterTests");
				List<XmlClass> classes7 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Parameter New Rule Add Script") != null && mapForScriptsToExecute.get("Parameter New Rule Add Script").equalsIgnoreCase("Yes")) {
					classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_MakerOperations_TS1"));
				}
				if (mapForScriptsToExecute.get("ParameterNewRuleAddCheckerOperation") != null && mapForScriptsToExecute.get("ParameterNewRuleAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_CheckerOperations_TS2"));
				}
				if (mapForScriptsToExecute.get("Parameter All Fund Rule Add Script") != null && mapForScriptsToExecute.get("Parameter All Fund Rule Add Script").equalsIgnoreCase("Yes")) {
					classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_MakerOperations_TS3"));
				}
				if (mapForScriptsToExecute.get("ParameterAllFundRuleAddCheckerOperation") != null && mapForScriptsToExecute.get("ParameterAllFundRuleAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_CheckerOperations_TS4"));
				}
				if (mapForScriptsToExecute.get("Parameter Modify Script") != null && mapForScriptsToExecute.get("Parameter Modify Script").equalsIgnoreCase("Yes")) {
					classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_ModifyMakerOperations_TS5"));					
				}
				if (mapForScriptsToExecute.get("ParameterModifyCheckerOperation") != null && mapForScriptsToExecute.get("ParameterModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_ModifyCheckerOperations_TC6"));					
				}
				if (mapForScriptsToExecute.get("Parameter Deactivate Script") != null && mapForScriptsToExecute.get("Parameter Deactivate Script").equalsIgnoreCase("Yes")) {
					classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_DeactivateMakerOperations_TS7"));
				}
				if (mapForScriptsToExecute.get("ParameterDeactivateCheckerOperation") != null && mapForScriptsToExecute.get("ParameterDeactivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_DeActivateCheckerOperations_TC8"));
				}
				if (mapForScriptsToExecute.get("Parameter Activate Script") != null && mapForScriptsToExecute.get("Parameter Activate Script").equalsIgnoreCase("Yes")) {
					classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_ActivateMakerOperations_TS9"));
				}
				if (mapForScriptsToExecute.get("ParameterActivateCheckerOperation") != null && mapForScriptsToExecute.get("ParameterActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_ActivateCheckerOperations_TC10"));
				}
				if (!classes7.isEmpty()) {
					test7.setXmlClasses(classes7);
					suites.add(suite7);
				}


				//Management Fee scripts suite.
				XmlSuite suite8 = new XmlSuite();
				suite8.setName("ManagementFeeMasterSuite");
				XmlTest test8 = new XmlTest(suite);
				test8.setName("ManagementFeeMasterTests");
				List<XmlClass> classes8 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Management Fee Add Script") != null && mapForScriptsToExecute.get("Management Fee Add Script").equalsIgnoreCase("Yes")) {
					classes8.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_MakerOperations_TS1"));
				}
				if (mapForScriptsToExecute.get("ManagementFeeAddCheckerOperation") != null && mapForScriptsToExecute.get("ManagementFeeAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes8.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_CheckerOperations_TS2"));
				}
				if (mapForScriptsToExecute.get("Management Fee Modify Script") != null && mapForScriptsToExecute.get("Management Fee Modify Script").equalsIgnoreCase("Yes")) {
					classes8.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_ModifyMakerOperations_TS3"));					
				}
				if (mapForScriptsToExecute.get("ManagementFeeModifyCheckerOperation") != null && mapForScriptsToExecute.get("ManagementFeeModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes8.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_ModifyCheckerOperations_TS4"));					
				}
				if (mapForScriptsToExecute.get("Management Fee Deactivate Script") != null && mapForScriptsToExecute.get("Management Fee Deactivate Script").equalsIgnoreCase("Yes")) {
					classes8.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_MakerDeactivateOperations_TS5"));
				}
				if (mapForScriptsToExecute.get("ManagementFeeDeactivateCheckerOperation") != null && mapForScriptsToExecute.get("ManagementFeeDeactivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes8.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_CheckerDeactivateOperations_TS6"));
				}
				if (mapForScriptsToExecute.get("Management Fee Activate Script") != null && mapForScriptsToExecute.get("Management Fee Activate Script").equalsIgnoreCase("Yes")) {
					classes8.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_MakerActivateOperations_TS7"));
				}
				if (mapForScriptsToExecute.get("ManagementFeeActivateCheckerOperation") != null && mapForScriptsToExecute.get("ManagementFeeActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes8.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_CheckerActivateOperations_TS8"));
				}
				if (!classes8.isEmpty()) {
					test8.setXmlClasses(classes8);
					suites.add(suite8);
				}


				//Incentive Fee scripts suite.
				XmlSuite suite9 = new XmlSuite();
				suite9.setName("IncentiveFeeMasterSuite");
				XmlTest test9 = new XmlTest(suite);
				test9.setName("IncentiveFeeMasterTests");
				List<XmlClass> classes9 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Incentive Fee Add Script") != null && mapForScriptsToExecute.get("Incentive Fee Add Script").equalsIgnoreCase("Yes")) {
					classes9.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerOperations_TS1"));
				}
				if (mapForScriptsToExecute.get("IncentiveFeeModifyCheckerOperation") != null && mapForScriptsToExecute.get("IncentiveFeeModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes9.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerOperations_TS2"));
				}
				if (mapForScriptsToExecute.get("Incentive Fee Modify Script") != null && mapForScriptsToExecute.get("Incentive Fee Modify Script").equalsIgnoreCase("Yes")) {
					classes9.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerModifyOperations_TS3"));					
				}
				if (mapForScriptsToExecute.get("IncentiveFeeModifyCheckerOperation") != null && mapForScriptsToExecute.get("IncentiveFeeModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes9.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerModifyOperations_TS4"));					
				}
				if (mapForScriptsToExecute.get("Incentive Fee Deactivate Script") != null && mapForScriptsToExecute.get("Incentive Fee Deactivate Script").equalsIgnoreCase("Yes")) {
					classes9.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerDeactivateOperations_TS5"));
				}
				if (mapForScriptsToExecute.get("IncentiveFeeDeactivateCheckerOperation") != null && mapForScriptsToExecute.get("IncentiveFeeDeactivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes9.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerDeactivateOperations_TS6"));
				}
				if (mapForScriptsToExecute.get("Incentive Fee Activate Script") != null && mapForScriptsToExecute.get("Incentive Fee Activate Script").equalsIgnoreCase("Yes")) {
					classes9.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerActivateOperations_TS7"));
				}
				if (mapForScriptsToExecute.get("IncentiveFeeActivateCheckerOperation") != null && mapForScriptsToExecute.get("IncentiveFeeActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes9.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerActivateOperations_TS8"));
				}
				if (!classes9.isEmpty()) {
					test9.setXmlClasses(classes9);
					suites.add(suite9);
				}


				//Investor scripts suite.
				XmlSuite suite10 = new XmlSuite();
				suite10.setName("InvestorMasterSuite");
				XmlTest test10 = new XmlTest(suite);
				test10.setName("InvestorMasterTests");
				List<XmlClass> classes10 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Investor Add Script") != null && mapForScriptsToExecute.get("Investor Add Script").equalsIgnoreCase("Yes")) {
					classes10.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_MakerOperations_TS1"));
				}
				if (mapForScriptsToExecute.get("InvestorAddCheckerOperation") != null && mapForScriptsToExecute.get("InvestorAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes10.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_CheckerOperations_TS2"));
				}
				if (mapForScriptsToExecute.get("Investor Modify Script") != null && mapForScriptsToExecute.get("Investor Modify Script").equalsIgnoreCase("Yes")) {
					classes10.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_ModifyMakerOperations_TS3"));					
				}
				if (mapForScriptsToExecute.get("InvestorModifyCheckerOperation") != null && mapForScriptsToExecute.get("InvestorModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes10.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_ModifyCheckerOperations_TS4"));					
				}
				if (mapForScriptsToExecute.get("Investor Deactivate Script") != null && mapForScriptsToExecute.get("Investor Deactivate Script").equalsIgnoreCase("Yes")) {
					classes10.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_DeactivateMakerOperations_TS5"));
				}
				if (mapForScriptsToExecute.get("InvestorDeactivateCheckerOperation") != null && mapForScriptsToExecute.get("InvestorDeactivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes10.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_DeactivateCheckerOperations_TS6"));
				}
				if (mapForScriptsToExecute.get("Investor Activate Script") != null && mapForScriptsToExecute.get("Investor Activate Script").equalsIgnoreCase("Yes")) {
					classes10.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_ActivateMakerOperations_TS7"));
				}
				if (mapForScriptsToExecute.get("InvestorActivateCheckerOperation") != null && mapForScriptsToExecute.get("InvestorActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes10.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_ActivateCheckerOperations_TS8"));
				}
				if (!classes10.isEmpty()) {
					test10.setXmlClasses(classes10);
					suites.add(suite10);
				}

				//Holder scripts suite.
				XmlSuite suite11 = new XmlSuite();
				suite11.setName("HolderMasterSuite");
				XmlTest test11 = new XmlTest(suite);
				test11.setName("HolderMasterTests");
				List<XmlClass> classes11 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Holder Add Script") != null && mapForScriptsToExecute.get("Holder Add Script").equalsIgnoreCase("Yes")) {
					classes11.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_MakerOperations_TS1"));
				}
				if (mapForScriptsToExecute.get("HolderAddCheckerOperation") != null && mapForScriptsToExecute.get("HolderAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes11.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_CheckerOperations_TS2"));
				}
				if (mapForScriptsToExecute.get("Holder Modify Script") != null && mapForScriptsToExecute.get("Holder Modify Script").equalsIgnoreCase("Yes")) {
					classes11.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_ModifyMakerOperations_TS3"));					
				}
				if (mapForScriptsToExecute.get("HolderModifyCheckerOperation") != null && mapForScriptsToExecute.get("HolderModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes11.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_ModifyCheckerOperations_TS4"));					
				}
				if (mapForScriptsToExecute.get("Holder Deactivate Script") != null && mapForScriptsToExecute.get("Holder Deactivate Script").equalsIgnoreCase("Yes")) {
					classes11.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_DeactivateMakerOperations_TS5"));
				}
				if (mapForScriptsToExecute.get("HolderDeactivateCheckerOperation") != null && mapForScriptsToExecute.get("HolderDeactivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes11.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_DeactivateCheckerOperations_TS6"));
				}
				if (mapForScriptsToExecute.get("Holder Activate Script") != null && mapForScriptsToExecute.get("Holder Activate Script").equalsIgnoreCase("Yes")) {
					classes11.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_ActivateMakerOperations_TS7"));
				}
				if (mapForScriptsToExecute.get("HolderActivateCheckerOperation") != null && mapForScriptsToExecute.get("HolderActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes11.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_ActivateCheckerOperations_TS8"));
				}
				if (!classes11.isEmpty()) {
					test11.setXmlClasses(classes11);
					suites.add(suite11);
				}


				//Joint Holder scripts suite.
				XmlSuite suite12 = new XmlSuite();
				suite12.setName("JointHolderMasterSuite");
				XmlTest test12 = new XmlTest(suite);
				test12.setName("JointHolderMasterTests");
				List<XmlClass> classes12 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Joint Holder Add Script") != null && mapForScriptsToExecute.get("Joint Holder Add Script").equalsIgnoreCase("Yes")) {
					classes12.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolderMaster_MakerScripts_TC1"));
					classes12.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_CheckerOperations_TC_2"));
				}
				if (mapForScriptsToExecute.get("Joint Holder Modify Script") != null && mapForScriptsToExecute.get("Joint Holder Modify Script").equalsIgnoreCase("Yes")) {
					classes12.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ModifyMakerOperations_TC3"));
					classes12.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ModifyCheckerOperations_TC4"));					
				}
				if (mapForScriptsToExecute.get("Joint Holder Deactivate Script") != null && mapForScriptsToExecute.get("Joint Holder Deactivate Script").equalsIgnoreCase("Yes")) {
					classes12.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_DeactivateMakerOperations_TC5"));
					classes12.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_DeactivateCheckerOperations_TC6"));
				}
				if (mapForScriptsToExecute.get("Joint Holder Activate Script") != null && mapForScriptsToExecute.get("Joint Holder Activate Script").equalsIgnoreCase("Yes")) {
					classes12.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ActivateMakerOperations_TC7"));
					classes12.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ActivateCheckerOperations_TC8"));
				}
				if (!classes12.isEmpty()) {
					test12.setXmlClasses(classes12);
					suites.add(suite12);
				}


				//Account scripts suite.
				XmlSuite suite13 = new XmlSuite();
				suite13.setName("AccountMasterSuite");
				XmlTest test13 = new XmlTest(suite);
				test13.setName("AccountMasterTests");
				List<XmlClass> classes13 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Account Add Script") != null && mapForScriptsToExecute.get("Account Add Script").equalsIgnoreCase("Yes")) {
					classes13.add(new XmlClass("com.viteos.veda.master.investoraccountmastertestscripts.InvestorAccountMaster_MakerOperations_TC1"));
				}
				if (mapForScriptsToExecute.get("Account Modify Script") != null && mapForScriptsToExecute.get("Account Modify Script").equalsIgnoreCase("Yes")) {
					classes13.add(new XmlClass("com.viteos.veda.master.investoraccountmastertestscripts.InvestorAccountMaster_ModifyMakerOperations_TC2"));
				}
				if (mapForScriptsToExecute.get("Account Deactivate Script") != null && mapForScriptsToExecute.get("Account Deactivate Script").equalsIgnoreCase("Yes")) {
					classes13.add(new XmlClass("com.viteos.veda.master.investoraccountmastertestscripts.InvestorAccountMaster_DeactivateMakerOperations_TC3"));
				}
				if (mapForScriptsToExecute.get("Account Aactivate Script") != null && mapForScriptsToExecute.get("Account Aactivate Script").equalsIgnoreCase("Yes")) {
					classes13.add(new XmlClass("com.viteos.veda.master.investoraccountmastertestscripts.InvestorAccountMaster_ActivateMakerOperations_TC4"));
				}
				if (!classes13.isEmpty()) {
					test13.setXmlClasses(classes13);
					suites.add(suite13);
				}


				//Fee Distribution scripts suite.
				
				/*XmlSuite suite14 = new XmlSuite();
				suite14.setName("FeeDistributionMasterSuite");
				XmlTest test14 = new XmlTest(suite);
				test14.setName("FeeDistributionMasterTests");
				List<XmlClass> classes14 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Fee Distribution Add Script") != null && mapForScriptsToExecute.get("Fee Distribution Add Script").equalsIgnoreCase("Yes")) {
					classes14.add(new XmlClass("com.viteos.veda.master.feedistribution.FeeDistributionMaster_MakerOperations_TC1"));
					classes14.add(new XmlClass("com.viteos.veda.master.feedistribution.FeeDistributionMaster_CheckerOperations_TC2"));
				}
				if (mapForScriptsToExecute.get("Fee Distribution Modify Script") != null && mapForScriptsToExecute.get("Fee Distribution Modify Script").equalsIgnoreCase("Yes")) {
					classes14.add(new XmlClass("com.viteos.veda.master.feedistribution.FeeDistributionMaster_ModifyMakerOperations_TC3"));
					classes14.add(new XmlClass("com.viteos.veda.master.feedistribution.FeeDistribution_CheckerModifyOperations_TC4"));					
				}
				if (mapForScriptsToExecute.get("Fee Distribution Deactivate Script") != null && mapForScriptsToExecute.get("Fee Distribution Deactivate Script").equalsIgnoreCase("Yes")) {
					classes14.add(new XmlClass("com.viteos.veda.master.feedistribution.FeeDistributionMaster_MakerDeactivateOperations_TC5"));
					classes14.add(new XmlClass("com.viteos.veda.master.feedistribution.FeeDistributionMaster_CheckerDeactivateOperations_TC6"));
				}
				if (mapForScriptsToExecute.get("Fee Distribution Activate Script") != null && mapForScriptsToExecute.get("Fee Distribution Activate Script").equalsIgnoreCase("Yes")) {
					classes14.add(new XmlClass("com.viteos.veda.master.feedistribution.FeedistributionMaster_MakerActivateOperations_TS7"));
					classes14.add(new XmlClass("com.viteos.veda.master.feedistribution.FeeDistribution_CheckerActivateOperations_TC8"));
				}
				if (!classes14.isEmpty()) {
					test14.setXmlClasses(classes14);
					suites.add(suite14);
				}*/

				
				//Opening Balance scripts suite.
				XmlSuite suite15 = new XmlSuite();
				suite15.setName("OpeningBalanceSuite");
				XmlTest test15 = new XmlTest(suite);
				test15.setName("OpeningBalanceTests");
				List<XmlClass> classes15 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("OpeningBalance Add Script") != null && mapForScriptsToExecute.get("OpeningBalance Add Script").equalsIgnoreCase("Yes")) {
					classes15.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_MakerOperations_TS1"));
				}
				if (mapForScriptsToExecute.get("OpeningBalanceAddCheckerOperation") != null && mapForScriptsToExecute.get("OpeningBalanceAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes15.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_CheckerOperations_TS2"));
				}
				if (mapForScriptsToExecute.get("OpeningBalance Modify Script") != null && mapForScriptsToExecute.get("OpeningBalance Modify Script").equalsIgnoreCase("Yes")) {
					classes15.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ModifyMakerOperations_TS3"));					
				}
				if (mapForScriptsToExecute.get("OpeningBalanceModifyCheckerOperation") != null && mapForScriptsToExecute.get("OpeningBalanceModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes15.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ModifyCheckerOperations_TS4"));					
				}
				if (mapForScriptsToExecute.get("OpeningBalance Deactivate Script") != null && mapForScriptsToExecute.get("OpeningBalance Deactivate Script").equalsIgnoreCase("Yes")) {
					classes15.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_DeactivateMakerOperations_TS5"));
				}
				if (mapForScriptsToExecute.get("OpeningBalanceDeactivateCheckerOperation") != null && mapForScriptsToExecute.get("OpeningBalanceDeactivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes15.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_DeactivateCheckerOperations_TS6"));
				}
				if (mapForScriptsToExecute.get("OpeningBalance Activate Script") != null && mapForScriptsToExecute.get("OpeningBalance Activate Script").equalsIgnoreCase("Yes")) {
					classes15.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ActivateMakerOperations_TS7"));
				}
				if (mapForScriptsToExecute.get("OpeningBalanceActivateCheckerOperation") != null && mapForScriptsToExecute.get("OpeningBalanceActivateCheckerOperation").equalsIgnoreCase("Yes")) {
					classes15.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ActivateCheckerOperations_TS8"));
				}
				if (!classes15.isEmpty()) {
					test15.setXmlClasses(classes15);
					suites.add(suite15);
				}

				
				//Trade Type SUBSCRIPTION scripts suite.
				XmlSuite suite16 = new XmlSuite();
				suite16.setName("TradeTypeSUBSuite");
				XmlTest test16 = new XmlTest(suite);
				test16.setName("TradeTypeSUBTests");
				List<XmlClass> classes16 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Trade_Subscription Add Scripts") != null && mapForScriptsToExecute.get("Trade_Subscription Add Scripts").equalsIgnoreCase("Yes")) {
					classes16.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.CreateSubscription_TestScript_TS1"));
				}
				if (mapForScriptsToExecute.get("SubscriptionAddCheckerOperation") != null && mapForScriptsToExecute.get("SubscriptionAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes16.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.SubscriptionCheckerOperation_TS2"));
				}
				if (mapForScriptsToExecute.get("CheckerReviewedOrReturnedSUBModifyScript") != null && mapForScriptsToExecute.get("CheckerReviewedOrReturnedSUBModifyScript").equalsIgnoreCase("Yes")) {
					classes16.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.CheckerReviewedOrReturnedSubscriptionMakerOperations_TS3"));
				}
				if (mapForScriptsToExecute.get("CheckerReviewedOrReturnedSUBModifyCheckerOperation") != null && mapForScriptsToExecute.get("CheckerReviewedOrReturnedSUBModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes16.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.CheckerReviewedOrReturnedSubscriptionCheckerOperations_TS4"));
				}
				if (mapForScriptsToExecute.get("Trade_SUB ViewButtons Data Verify Scripts") != null && mapForScriptsToExecute.get("Trade_SUB ViewButtons Data Verify Scripts").equalsIgnoreCase("Yes")) {
					classes16.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.Subscription_VerifyViewButtons"));					
				}
				if (!classes16.isEmpty()) {
					test16.setXmlClasses(classes16);
					suites.add(suite16);
				}

				
				
				//Trade Type REDEMPTION scripts suite.
				XmlSuite suite17 = new XmlSuite();
				suite17.setName("TradeTypeREDSuite");
				XmlTest test17 = new XmlTest(suite);
				test17.setName("TradeTypeREDTests");
				List<XmlClass> classes17 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Trade_Redemption Add Scripts") != null && mapForScriptsToExecute.get("Trade_Redemption Add Scripts").equalsIgnoreCase("Yes")) {
					classes17.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.CreatingMakerRedemption_TS1"));
				}
				if (mapForScriptsToExecute.get("RedemptionAddCheckerOperation") != null && mapForScriptsToExecute.get("RedemptionAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes17.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.RedemptionCheckerOperation_TS2"));
				}
				if (mapForScriptsToExecute.get("CheckerReviewedOrReturnedREDModifyScript") != null && mapForScriptsToExecute.get("CheckerReviewedOrReturnedREDModifyScript").equalsIgnoreCase("Yes")) {
					classes17.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.CheckerReviewedOrReturnedRedemptionMakerOperations_TS3"));
				}
				if (mapForScriptsToExecute.get("CheckerReviewedOrReturnedREDModifyCheckerOperation") != null && mapForScriptsToExecute.get("CheckerReviewedOrReturnedREDModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes17.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.CheckerReviewedOrReturnedRedemptionCheckerOperation_TS4"));
				}
				if (mapForScriptsToExecute.get("Trade_RED ViewButtons Data Verify Scripts") != null && mapForScriptsToExecute.get("Trade_RED ViewButtons Data Verify Scripts").equalsIgnoreCase("Yes")) {
					classes17.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.Redemption_VerifyViewButtons"));					
				}
				if (!classes17.isEmpty()) {
					test17.setXmlClasses(classes17);
					suites.add(suite17);
				}


				//Trade Type TRANSFER scripts suite.
				XmlSuite suite18 = new XmlSuite();
				suite18.setName("TradeTypeTRASuite");
				XmlTest test18 = new XmlTest(suite);
				test18.setName("TradeTypeTRATests");
				List<XmlClass> classes18 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Trade_Transfer Add Scripts") != null && mapForScriptsToExecute.get("Trade_Transfer Add Scripts").equalsIgnoreCase("Yes")) {
					classes18.add(new XmlClass("com.viteos.veda.transaction.transferscripts.Transfer_MakerOperations_TS1"));
				}
				if (mapForScriptsToExecute.get("TransferAddCheckerOperation") != null && mapForScriptsToExecute.get("TransferAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes18.add(new XmlClass("com.viteos.veda.transaction.transferscripts.Transfer_CheckerOperations_TS2"));
				}
				if (mapForScriptsToExecute.get("CheckerReviewedOrReturnedTRAModifyScript") != null && mapForScriptsToExecute.get("CheckerReviewedOrReturnedTRAModifyScript").equalsIgnoreCase("Yes")) {
					classes18.add(new XmlClass("com.viteos.veda.transaction.transferscripts.TransferCheckerReviewedOrReturned_MakerOperations_TS3"));
				}
				if (mapForScriptsToExecute.get("CheckerReviewedOrReturnedTRAModifyCheckerOperation") != null && mapForScriptsToExecute.get("CheckerReviewedOrReturnedTRAModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes18.add(new XmlClass("com.viteos.veda.transaction.transferscripts.TransferCheckerReviewedOrReturned_CheckerOperations_TS4"));
				}
				if (mapForScriptsToExecute.get("Trade_TRA ViewButtons Data Verify Scripts") != null && mapForScriptsToExecute.get("Trade_TRA ViewButtons Data Verify Scripts").equalsIgnoreCase("Yes")) {
					classes18.add(new XmlClass("com.viteos.veda.transaction.transferscripts.Transfer_VerifyViewButtons"));					
				}
				if (!classes18.isEmpty()) {
					test18.setXmlClasses(classes18);
					suites.add(suite18);
				}

				//Trade Type EXCHANGE scripts suite.
				XmlSuite suite19 = new XmlSuite();
				suite19.setName("TradeTypeEXCSuite");
				XmlTest test19 = new XmlTest(suite);
				test19.setName("TradeTypeEXCTests");
				List<XmlClass> classes19 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Trade_Exchange Add Scripts") != null && mapForScriptsToExecute.get("Trade_Exchange Add Scripts").equalsIgnoreCase("Yes")) {
					classes19.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.CreatingMakerExchange_TS1"));
				}
				if (mapForScriptsToExecute.get("ExchangeAddCheckerOperation") != null && mapForScriptsToExecute.get("ExchangeAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes19.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.ExchangeCheckerOperation_TS2"));
				}
				if (mapForScriptsToExecute.get("CheckerReviewedOrReturnedEXCModifyScript") != null && mapForScriptsToExecute.get("CheckerReviewedOrReturnedEXCModifyScript").equalsIgnoreCase("Yes")) {
					classes19.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.CheckerReviewedOrReturnedExchangeMakerOperation_TS3"));
				}
				if (mapForScriptsToExecute.get("CheckerReviewedOrReturnedEXCModifyCheckerOperation") != null && mapForScriptsToExecute.get("CheckerReviewedOrReturnedEXCModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes19.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.CheckerReviewedOrReturnedExchangeCheckerOperations_TS4"));
				}
				if (mapForScriptsToExecute.get("Trade_EXC ViewButtons Data Verify Scripts") != null && mapForScriptsToExecute.get("Trade_EXC ViewButtons Data Verify Scripts").equalsIgnoreCase("Yes")) {
					classes19.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.Exchange_VerifyViewButtons"));					
				}
				if (!classes19.isEmpty()) {
					test19.setXmlClasses(classes19);
					suites.add(suite19);
				}

				//Trade Type SWITCH scripts suite.
				XmlSuite suite20 = new XmlSuite();
				suite20.setName("TradeTypeSWISuite");
				XmlTest test20 = new XmlTest(suite);
				test20.setName("TradeTypeSWITests");
				List<XmlClass> classes20 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("Trade_Switch Add Scripts") != null && mapForScriptsToExecute.get("Trade_Switch Add Scripts").equalsIgnoreCase("Yes")) {
					classes20.add(new XmlClass("com.viteos.veda.transaction.switchtestscripts.Switch_MakerOperations_TS1"));
				}
				if (mapForScriptsToExecute.get("SwitchAddCheckerOperation") != null && mapForScriptsToExecute.get("SwitchAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes20.add(new XmlClass("com.viteos.veda.transaction.switchtestscripts.Switch_CheckerOperations_TS2"));
				}
				if (mapForScriptsToExecute.get("CheckerReviewedOrReturnedSWIModifyScript") != null && mapForScriptsToExecute.get("CheckerReviewedOrReturnedSWIModifyScript").equalsIgnoreCase("Yes")) {
					classes20.add(new XmlClass("com.viteos.veda.transaction.switchtestscripts.CheckerReviewedOrReturnedMakerOperation_TC3"));
				}
				if (mapForScriptsToExecute.get("CheckerReviewedOrReturnedSWIModifyCheckerOperation") != null && mapForScriptsToExecute.get("CheckerReviewedOrReturnedSWIModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes20.add(new XmlClass("com.viteos.veda.transaction.switchtestscripts.CheckerReviewedOrReturnedCheckerOperations_TC4"));
				}
				if (mapForScriptsToExecute.get("Trade_SWI ViewButtons Data Verify Scripts") != null && mapForScriptsToExecute.get("Trade_SWI ViewButtons Data Verify Scripts").equalsIgnoreCase("Yes")) {
					classes20.add(new XmlClass("com.viteos.veda.transaction.switchtestscripts.Switch_VerifyViewButtons"));					
				}
				if (!classes20.isEmpty()) {
					test20.setXmlClasses(classes20);
					suites.add(suite20);
				}
				
				//Suite For Side Pocket Subscriptions.
				XmlSuite suite21 = new XmlSuite();
				suite21.setName("SidePocketSUBSuite");
				XmlTest test21 = new XmlTest(suite);
				test21.setName("SidePocketSUBTests");
				List<XmlClass> classes21 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("SidePocketSUB_Add Scripts") != null && mapForScriptsToExecute.get("SidePocketSUB_Add Scripts").equalsIgnoreCase("Yes")) {
					classes21.add(new XmlClass("com.viteos.veda.transaction.sidepocketsubscriptionscripts.Maker_SidePocket_Subscription_TS1"));
				}
				if (mapForScriptsToExecute.get("SidePocketSUBAddCheckerOperation") != null && mapForScriptsToExecute.get("SidePocketSUBAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes21.add(new XmlClass("com.viteos.veda.transaction.sidepocketsubscriptionscripts.Checker_SidePocket_Subscription_TS2"));
				}
				if (mapForScriptsToExecute.get("SidePocketSUB_Modify Scripts") != null && mapForScriptsToExecute.get("SidePocketSUB_Modify Scripts").equalsIgnoreCase("Yes")) {
					classes21.add(new XmlClass("com.viteos.veda.transaction.sidepocketsubscriptionscripts.Maker_ModifyReturnedSP_SUBOrders_TS3"));
				}
				if (mapForScriptsToExecute.get("SidePocketSUBModifyCheckerOperation") != null && mapForScriptsToExecute.get("SidePocketSUBModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes21.add(new XmlClass("com.viteos.veda.transaction.sidepocketsubscriptionscripts.Checker_ModifyReturnedSP_SUBOrders_TS4"));
				}
				if (!classes21.isEmpty()) {
					test21.setXmlClasses(classes21);
					suites.add(suite21);
				}
				
			
				
				//Suite For Side Pocket Redemptions.
				XmlSuite suite23 = new XmlSuite();
				suite23.setName("SidePocketREDSuite");
				XmlTest test23 = new XmlTest(suite);
				test23.setName("SidePocketREDTests");
				List<XmlClass> classes23 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("SidePocketRED_Add Scripts") != null && mapForScriptsToExecute.get("SidePocketRED_Add Scripts").equalsIgnoreCase("Yes")) {
					classes23.add(new XmlClass("com.viteos.veda.transaction.sidepocketredemptionscripts.Maker_SidePocket_Redemption_TS1"));
				}
				if (mapForScriptsToExecute.get("SidePocketREDAddCheckerOperation") != null && mapForScriptsToExecute.get("SidePocketREDAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes23.add(new XmlClass("com.viteos.veda.transaction.sidepocketredemptionscripts.Checker_SidePocket_Redemption_TS2"));
				}
				if (mapForScriptsToExecute.get("SidePocketRED_Modify Scripts") != null && mapForScriptsToExecute.get("SidePocketRED_Modify Scripts").equalsIgnoreCase("Yes")) {
					classes23.add(new XmlClass("com.viteos.veda.transaction.sidepocketredemptionscripts.CheckerReturned_SPRedemptionMakerOperations_TS3"));
				}
				if (mapForScriptsToExecute.get("SidePocketREDModifyCheckerOperation") != null && mapForScriptsToExecute.get("SidePocketREDModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes23.add(new XmlClass("com.viteos.veda.transaction.sidepocketredemptionscripts.CheckerReturnedCheckerOPerations_TS4"));
				}
				if (!classes23.isEmpty()) {
					test23.setXmlClasses(classes23);
					suites.add(suite23);
				}
				
				//Suite For Feeder Subscriptions.
				XmlSuite suite24 = new XmlSuite();
				suite24.setName("FeederSUBSuite");
				XmlTest test24 = new XmlTest(suite);
				test24.setName("FeederSUBTests");
				List<XmlClass> classes24 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("FeederSUB_Add Scripts") != null && mapForScriptsToExecute.get("FeederSUB_Add Scripts").equalsIgnoreCase("Yes")) {
					classes24.add(new XmlClass("com.viteos.veda.master.feedersubscriptiontestscripts.FeederSubscriptionMaster_MakerOperations_TS1"));
				}
				if (mapForScriptsToExecute.get("FeederSUB_AddCheckerOperation") != null && mapForScriptsToExecute.get("FeederSUB_AddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes24.add(new XmlClass("com.viteos.veda.master.feedersubscriptiontestscripts.FeederSubscriptionMaster_CheckerOperations_TS2"));
				}
				if (mapForScriptsToExecute.get("FeederSUB_Modify Scripts") != null && mapForScriptsToExecute.get("FeederSUB_Modify Scripts").equalsIgnoreCase("Yes")) {
					classes24.add(new XmlClass("com.viteos.veda.master.feedersubscriptiontestscripts.FeederSubscriptionCheckerReturnedMakerOperations_TC3"));
				}
				if (mapForScriptsToExecute.get("FeederSUB_ModifyCheckerOperation") != null && mapForScriptsToExecute.get("FeederSUB_ModifyCheckerOperation").equalsIgnoreCase("Yes")) {
					classes24.add(new XmlClass("com.viteos.veda.master.feedersubscriptiontestscripts.FeederSubscriptionCheckerReturnedCheckerOperation_TC4"));
				}
				if (!classes24.isEmpty()) {
					test24.setXmlClasses(classes24);
					suites.add(suite24);
				}
				
				//Suite For Feeder Redemptions.
				XmlSuite suite25 = new XmlSuite();
				suite25.setName("FeederREDSuite");
				XmlTest test25 = new XmlTest(suite);
				test25.setName("FeederREDTests");
				List<XmlClass> classes25 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("FeederRED_Add Scripts") != null && mapForScriptsToExecute.get("FeederRED_Add Scripts").equalsIgnoreCase("Yes")) {
					classes25.add(new XmlClass("com.viteos.veda.master.feederredemptiontestscripts.FeederRedemptionMaster_MakerOperations_TS1"));
				}
				if (mapForScriptsToExecute.get("FeederREDAddCheckerOperation") != null && mapForScriptsToExecute.get("FeederREDAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes25.add(new XmlClass("com.viteos.veda.master.feederredemptiontestscripts.FeederRedemptionMaster_CheckerOperations_TS2"));
				}
				if (mapForScriptsToExecute.get("FeederRED_Modify Scripts") != null && mapForScriptsToExecute.get("FeederRED_Modify Scripts").equalsIgnoreCase("Yes")) {
					classes25.add(new XmlClass("com.viteos.veda.master.feederredemptiontestscripts.FeederRedemptionCheckerReturnedMakerOperation_TC3"));
				}
				if (mapForScriptsToExecute.get("FeederModifyAddCheckerOperation") != null && mapForScriptsToExecute.get("FeederModifyAddCheckerOperation").equalsIgnoreCase("Yes")) {
					classes25.add(new XmlClass("com.viteos.veda.master.feederredemptiontestscripts.CheckerReturnedFeederRedemptionCheckerOperation_TC4"));
				}
				if (!classes25.isEmpty()) {
					test25.setXmlClasses(classes25);
					suites.add(suite25);
				}
				
				//Suite For Processing Allocations.
				XmlSuite suite22 = new XmlSuite();
				suite22.setName("AllocationProcessingWizardSuite");
				XmlTest test22 = new XmlTest(suite);
				test22.setName("AllocationProcessingWizardTests");
				List<XmlClass> classes22 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("TriggerAllocationWizardProcessing") != null && mapForScriptsToExecute.get("TriggerAllocationWizardProcessing").equalsIgnoreCase("Yes")) {
					classes22.add(new XmlClass("com.viteos.veda.allocationscripts.AllocationProcessTestScript_TC1"));
				}
				if (!classes22.isEmpty()) {
					test22.setXmlClasses(classes22);
					suites.add(suite22);
				}
				
				//Suite For Processing Allocations.
				XmlSuite suite29 = new XmlSuite();
				suite29.setName("AllocationComparisonSuite");
				XmlTest test29 = new XmlTest(suite);
				test29.setName("AllocationComparisonSuite");
				List<XmlClass> classes29 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("AllocationComparisonReport") != null && mapForScriptsToExecute.get("AllocationComparisonReport").equalsIgnoreCase("Yes")) {
					classes29.add(new XmlClass("com.viteos.veda.com.viteos.veda.allocationreports.AllocationComparisonReports_TS1"));
				}
				if (!classes29.isEmpty()) {
					test29.setXmlClasses(classes29);
					suites.add(suite29);
				}
				
				
				//Suite For Role test scripts of USER MANAGEMENT.
				XmlSuite suite26 = new XmlSuite();
				suite26.setName("UserManagementROLESuite");
				XmlTest test26 = new XmlTest(suite);
				test26.setName("UserManagementROLETests");
				List<XmlClass> classes26 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("ROLE_Add Script") != null && mapForScriptsToExecute.get("ROLE_Add Script").equalsIgnoreCase("Yes")) {
					classes26.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleCreationScript_TC1"));
				}
				if (mapForScriptsToExecute.get("Role Modify Script") != null && mapForScriptsToExecute.get("Role Modify Script").equalsIgnoreCase("Yes")) {
					classes26.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleModifyorVerifyScript_TC2"));
				}
				if (mapForScriptsToExecute.get("Role Deactivate Script") != null && mapForScriptsToExecute.get("Role Deactivate Script").equalsIgnoreCase("Yes")) {
					classes26.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleDeActivateScript_TC3"));
				}
				if (mapForScriptsToExecute.get("Role Activate Script") != null && mapForScriptsToExecute.get("Role Activate Script").equalsIgnoreCase("Yes")) {
					classes26.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleActivateScript_TC4"));
				}
				if (!classes26.isEmpty()) {
					test26.setXmlClasses(classes26);
					suites.add(suite26);
				}
				
				//Suite For GROUP test scripts of USER MANAGEMENT.
				XmlSuite suite27 = new XmlSuite();
				suite27.setName("UserManagementGROUPSuite");
				XmlTest test27 = new XmlTest(suite);
				test27.setName("UserManagementGROUPTests");
				List<XmlClass> classes27 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("GROUP_Add_Script") != null && mapForScriptsToExecute.get("GROUP_Add_Script").equalsIgnoreCase("Yes")) {
					classes27.add(new XmlClass("com.viteos.veda.usermanagement.grouptestscripts.GroupCreationTestScript_TC1"));
				}
				if (mapForScriptsToExecute.get("Group Modify Script") != null && mapForScriptsToExecute.get("Group Modify Script").equalsIgnoreCase("Yes")) {
					classes27.add(new XmlClass("com.viteos.veda.usermanagement.grouptestscripts.Group_ModifyOrVerifyTestScript_TC2"));
				}
				if (mapForScriptsToExecute.get("Group Deactivate Script") != null && mapForScriptsToExecute.get("Group Deactivate Script").equalsIgnoreCase("Yes")) {
					classes27.add(new XmlClass("com.viteos.veda.usermanagement.grouptestscripts.Group_DeActivation_TestScript_TC3"));
				}
				if (mapForScriptsToExecute.get("Group Activate Script") != null && mapForScriptsToExecute.get("Group Activate Script").equalsIgnoreCase("Yes")) {
					classes27.add(new XmlClass("com.viteos.veda.usermanagement.grouptestscripts.Group_Activate_TestScript_TC4"));
				}
				if (!classes27.isEmpty()) {
					test27.setXmlClasses(classes27);
					suites.add(suite27);
				}
				
				//Suite For USER test scripts of USER MANAGEMENT.
				XmlSuite suite28 = new XmlSuite();
				suite28.setName("UserManagementUSERSuite");
				XmlTest test28 = new XmlTest(suite);
				test28.setName("UserManagementUSERTests");
				List<XmlClass> classes28 = new ArrayList<XmlClass>();
				if (mapForScriptsToExecute.get("USER_UserManagement_TestScripts") != null && mapForScriptsToExecute.get("USER_UserManagement_TestScripts").equalsIgnoreCase("Yes")) {
					classes28.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.CreateUser_TestScript_TS1"));
					classes28.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserVerificationUser1_TetsScript_TC2"));
					classes28.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserVerificationUser2_TestScript_TC3"));
					classes28.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserVerificationUser3_TestScript_TC4"));
					classes28.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserModify_TestScript_TC5"));
					classes28.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserVerificationUser1_TetsScript_TC6"));
					classes28.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserModifiedRoleVerificationUser4_TetsScript_TC7"));
				}				
				if (!classes28.isEmpty()) {
					test28.setXmlClasses(classes28);
					suites.add(suite28);
				}
				/*
				TimeUnit.MINUTES.sleep(3);
				suites.add(suite);
				System.out.println("=====================================> 'VITEOS - VEDA' Test Suite with following modules with respective workflow test coverage scripts  =========================> " + suite);
				suites.add(suite1);
				System.out.println("=====================================> 'CLIENT' suite with following selective test scripts : "+suite1);
				suites.add(suite2);
				System.out.println("=====================================> 'FUND FAMILY' suite with following selective test scripts : "+suite2);
				suites.add(suite3);
				System.out.println("=====================================> 'LEGAL ENTITY' suite with following selective test scripts : "+suite3);
				suites.add(suite4);
				System.out.println("=====================================> 'CLASS' suite with following selective test scripts : "+suite4);
				suites.add(suite5);
				System.out.println("=====================================> 'SERIES' suite with following selective test scripts : "+suite5);
				suites.add(suite6);
				System.out.println("=====================================> 'FORMULA' suite with following selective test scripts : "+suite6);
				suites.add(suite7);
				System.out.println("=====================================> 'PARAMETER' suite with following selective test scripts : "+suite7);
				suites.add(suite8);
				System.out.println("=====================================> 'MANAGEMENT FEE' suite with following selective test scripts : "+suite8);
				suites.add(suite9);
				System.out.println("=====================================> 'INCENTIVE FEE' suite with following selective test scripts : "+suite9);
				suites.add(suite10);
				System.out.println("=====================================> 'INVESTOR' suite with following selective test scripts : "+suite10);
				suites.add(suite11);
				System.out.println("=====================================> 'HOLDER' suite with following selective test scripts : "+suite11);
				suites.add(suite12);
				System.out.println("=====================================> 'JOINT HOLDER' suite with following selective test scripts : "+suite12);
				suites.add(suite13);
				System.out.println("=====================================> 'ACCOUNT' suite with following selective test scripts : "+suite13);
				suites.add(suite14);
				System.out.println("=====================================> 'FEE DISTRIBUTION' suite with following selective test scripts : "+suite14);
				suites.add(suite15);
				System.out.println("=====================================> 'OPENING BALANCE' suite with following selective test scripts : "+suite15);
				suites.add(suite16);
				System.out.println("=====================================> 'TRADE SUBSCRIPTION' suite with following selective test scripts : "+suite16);
				suites.add(suite21);
				System.out.println("=====================================> 'SIDE POCKET SUBSCRIPTION' suite with following selective test scripts : "+suite21);
				suites.add(suite22);
				System.out.println("=====================================> 'ALLOCATION WIZARD PROCESSING' suite with following selective test scripts : "+suite22);
				suites.add(suite17);
				System.out.println("=====================================> 'TRADE REDEMPTION' suite with following selective test scripts : "+suite17);
				suites.add(suite18);
				System.out.println("=====================================> 'TRADE TRANSFER' suite with following selective test scripts : "+suite18);
				suites.add(suite19);
				System.out.println("=====================================> 'TRADE EXCHANGE' suite with following selective test scripts : "+suite19);
				suites.add(suite20);
				System.out.println("=====================================> 'TRADE SWITCH' suite with following selective test scripts : "+suite20);				
				suites.add(suite23);
				System.out.println("=====================================> 'SIDE POCKET REDEMPTION' suite with following selective test scripts : "+suite23);
				suites.add(suite24);
				System.out.println("=====================================> 'FEEDER SUBSCRIPTION' suite with following selective test scripts : "+suite24);
				suites.add(suite25);
				System.out.println("=====================================> 'FEEDER REDEMPTION' suite with following selective test scripts : "+suite25);
				suites.add(suite26);
				System.out.println("=====================================> 'USER MANAGEMENT - ROLE' suite with following selective test scripts : "+suite26);
				suites.add(suite27);
				System.out.println("=====================================> 'USER MANAGEMENT - GROUP' suite with following selective test scripts : "+suite27);
				suites.add(suite28);
				System.out.println("=====================================> 'USER MANAGEMENT - USER' suite with following selective test scripts : "+suite28);*/
				
				System.out.println("Executable suites with test scripts : ===============> "+suites+"  <=============================");
				
				if(suites.size() == 0){
					System.out.println("*****************************************************************");
					System.out.println("     No Suites selected!! No scripts to Run     ");
					System.out.println("*****************************************************************");
					return;
				}
				TestNG tng = new TestNG();
				tng.setXmlSuites(suites);
				tng.run();
			}						
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}	
	}
}
