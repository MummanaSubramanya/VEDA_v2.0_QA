package com.viteos.veda.testsuites;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestSuite2 {


	public static void main(String[] args) {

		String clientStatus = args[0];
		String ffStatus = args[1];
		String leStatus = args[2];
		String classStatus = args[3];
		String seriesStatus = args[4];
		String formulaStatus = args[5];
		String parameterStatus = args[6];

		XmlSuite suite = new XmlSuite();
		suite.setName("ViteosTestSuite");
		
		
		XmlSuite suite1 = new XmlSuite();
		suite1.setName("ClientSuite");
		XmlTest test1 = new XmlTest(suite);
		test1.setName("ClientTests");
		List<XmlClass> classes1 = new ArrayList<XmlClass>();
		classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_MakerOperations_TC1"));
		classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_CheckerOperations_TC2"));
		classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ModifyMakerOperations_TC3"));
		classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ModifyCheckerOperations_TC4"));
		classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_DeactivateMakerOperations_TC5"));
		classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_DeactivateCheckerOperations_TC6"));
		classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ActivateMakerOperations_TC7"));
		classes1.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ActivateCheckerOperations_TC8"));
		test1.setXmlClasses(classes1);
		
		
		XmlSuite suite2 = new XmlSuite();
		suite2.setName("FundFamilySuite");
		XmlTest test2 = new XmlTest(suite);
		test2.setName("FundFamilyTests");
		List<XmlClass> classes2 = new ArrayList<XmlClass>();
		classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_MakerOperations_TC1"));
		classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_CheckerOperations_TC2"));
		classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ModifyMakerOperations_TC3"));
		classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ModifyCheckerOperations_TC4"));
		classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_DeactivateMakerOperations_TC5"));
		classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_DeactivateCheckerOperations_TC6"));
		classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ActivateMakerOperations_TC7"));
		classes2.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ActivateCheckerOperations_TC8"));
		test2.setXmlClasses(classes2);
		
		//set of testsuites to be run
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		if(clientStatus.equalsIgnoreCase("YES"))
			suites.add(suite);
		if(ffStatus.equalsIgnoreCase("YES"))
			suites.add(suite1);
		
		if(suites.size() == 0){
			System.out.println("*****************************************************************");
			System.out.println("     No Suites selected!! No scripts to Run     ");
			System.out.println("*****************************************************************");
			return;
		}
		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);
		tng.run();
		
		
		
/*		XmlSuite suite3 = new XmlSuite();
		suite3.setName("LegalEntitySuite");
		XmlTest test3 = new XmlTest(suite);
		test3.setName("LegalEntityTests");
		List<XmlClass> classes3 = new ArrayList<XmlClass>();		
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_MakerOperations_TC1"));
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_CheckerOperations_TC2"));
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityFeeder_MakerOperations_TC3"));
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityFeeder_CheckerOperations_TC4"));
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_ModifyMakerOperations_TC5"));
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_ModifyCheckerOperations_TC6"));
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_VerifyClonedValues_TC7"));
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_MakerDeactivateOperations_TC8"));
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_CheckerDeactivateOperations_TC9"));
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_MakerActivateOperations_TC10"));
		classes3.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_CheckerActivateOperations_TC11"));
		test3.setXmlClasses(classes3);

		XmlTest test4 = new XmlTest(suite);
		test4.setName("ClassTests");
		List<XmlClass> classes4 = new ArrayList<XmlClass>();		
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_MakerOperations_TC1"));
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_CheckerOperations_TC2"));
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassClone_MakerOperations_TC3"));
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassClone_CheckerOperations_TC4"));
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_ModifyMakerOperations_TC5"));
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_ModifyCheckerOperations_TC6"));
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_VerifyClonedAndLEDataForClass_TC7"));
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_MakerDeactivateOperations_TC8"));
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_CheckerDeactivateOperations_TC9"));
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_MakerActivateOperations_TC10"));
		classes4.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_CheckerActivateOperations_TC11"));
		test4.setXmlClasses(classes4);

		XmlTest test5 = new XmlTest(suite);
		test5.setName("SeriesSetupTests");
		List<XmlClass> classes5 = new ArrayList<XmlClass>();
		classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_MakerOperations_TC1"));
		classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_CheckerOperations_TC2"));
		classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ModifyMakerOperations_TC3"));
		classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ModifyCheckerOperations_TC4"));
		classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_DeactivateMakerOperations_TC5"));
		classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_DeactivateCheckerOperaations_TC6"));
		classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ActivateMakerOperations_TC7"));
		classes5.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ActivateCheckerOperations_TC8"));
		test5.setXmlClasses(classes5);

		XmlTest test6 = new XmlTest(suite);
		test6.setName("FormulaSetupTests");
		List<XmlClass> classes6 = new ArrayList<XmlClass>();
		classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_MakerOperations_TC1"));
		classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_CheckerOperations_TC2"));
		classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ModifyMakerOperations_TC3"));
		classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ModifyCheckerOperations_TC4"));
		classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_DeActivateMakerOperations_TC5"));
		classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_DeActivateCheckerOperations_TC6"));
		classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ActivateMakerOperations_TC7"));
		classes6.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ActivateCheckerOperations_TC8"));
		test6.setXmlClasses(classes6);

		XmlTest test7 = new XmlTest(suite);
		test7.setName("ParametersTests");
		List<XmlClass> classes7 = new ArrayList<XmlClass>();
		classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_MakerOperations_TS1"));
		classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_CheckerOperations_TS2"));
		classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_MakerOperations_TS3"));
		classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_CheckerOperations_TS4"));
		classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_ModifyMakerOperations_TS5"));
		classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_ModifyCheckerOperations_TC6"));
		classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_DeactivateMakerOperations_TS7"));
		classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_DeActivateCheckerOperations_TC8"));
		classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_ActivateMakerOperations_TS9"));
		classes7.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_ActivateCheckerOperations_TC10"));
		test7.setXmlClasses(classes7);


		//set of testsuites to be run
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		if(clientStatus.equalsIgnoreCase("YES"))
			suites.add(suite);
		if(ffStatus.equalsIgnoreCase("YES"))
			suites.add(suite1);
		if(bSanityRTGSIn)
			suites.add(suite2);
		if(bSanityRTGSOUT)
			suites.add(suite3);
		if(bSanityBranchChannel)
			suites.add(suite4);
		if(bSanityCorpChannel)
			suites.add(suite5);

			


		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_CheckerOperations_TS2"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_MakerOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_CheckerOperations_TS4"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_ModifyMakerOperations_TS5"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_ModifyCheckerOperations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_DeactivateMakerOperations_TS7"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_DeActivateCheckerOperations_TC8"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_ActivateMakerOperations_TS9"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_ActivateCheckerOperations_TC10"));


		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_CheckerOperations_TS2"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_ModifyMakerOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_ModifyCheckerOperations_TS4"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_MakerDeactivateOperations_TS5"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_CheckerDeactivateOperations_TS6"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_MakerActivateOperations_TS7"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_CheckerActivateOperations_TS8"));


		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerOperations_TS2"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerModifyOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerModifyOperations_TS4"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerDeactivateOperations_TS5"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerDeactivateOperations_TS6"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerActivateOperations_TS7"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerActivateOperations_TS8"));

		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolderMaster_MakerScripts_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_CheckerOperations_TC_2"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ModifyMakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ModifyCheckerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_DeactivateMakerOperations_TC5"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_DeactivateCheckerOperations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ActivateMakerOperations_TC7"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ActivateCheckerOperations_TC8"));

		classes.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleCreationScript_TC1"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleModifyorVerifyScript_TC2"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleDeActivateScript_TC3"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleActivateScript_TC4"));



		test.setXmlClasses(classes);

		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);

		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);
		tng.run();

		//int test  = Float.valueOf("8.0").intValue();
		 */	}
}
