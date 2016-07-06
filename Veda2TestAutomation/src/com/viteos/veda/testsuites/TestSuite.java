package com.viteos.veda.testsuites;

import java.util.ArrayList;
import java.util.List;

import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class TestSuite {

	
	public static void main(String[] args) {
		XmlSuite suite = new XmlSuite();
		suite.setName("FundSetupSuite");
		XmlTest test = new XmlTest(suite);
		test.setName("SeriesTests");
		List<XmlClass> classes = new ArrayList<XmlClass>();
		
		/*classes.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_CheckerOperations_TC2"));
		classes.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ModifyMakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ModifyCheckerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_DeactivateMakerOperations_TC5"));
		classes.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_DeactivateCheckerOperations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ActivateMakerOperations_TC7"));
		classes.add(new XmlClass("com.viteos.veda.master.clienttestscripts.ClientMaster_ActivateCheckerOperations_TC8"));
		
		classes.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_CheckerOperations_TC2"));
		classes.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ModifyMakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ModifyCheckerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_DeactivateMakerOperations_TC5"));
		classes.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_DeactivateCheckerOperations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ActivateMakerOperations_TC7"));
		classes.add(new XmlClass("com.viteos.veda.master.fftestscripts.FundFamilyMaster_ActivateCheckerOperations_TC8"));*/
		
		/*classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_CheckerOperations_TC2"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityFeeder_MakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityFeeder_CheckerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_ModifyMakerOperations_TC5"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntityMaster_ModifyCheckerOperations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_VerifyClonedValues_TC7"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_MakerDeactivateOperations_TC8"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_CheckerDeactivateOperations_TC9"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_MakerActivateOperations_TC10"));
		classes.add(new XmlClass("com.viteos.veda.master.legalentitytestscripts.LegalEntity_CheckerActivateOperations_TC11"));
		
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_CheckerOperations_TC2"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassClone_MakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassClone_CheckerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_ModifyMakerOperations_TC5"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_ModifyCheckerOperations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_VerifyClonedAndLEDataForClass_TC7"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_MakerDeactivateOperations_TC8"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_CheckerDeactivateOperations_TC9"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_MakerActivateOperations_TC10"));
		classes.add(new XmlClass("com.viteos.veda.master.classtestscripts.ClassMaster_CheckerActivateOperations_TC11"));*/
		
		/*classes.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_CheckerOperations_TC2"));
		classes.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ModifyMakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ModifyCheckerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_DeActivateMakerOperations_TC5"));
		classes.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_DeActivateCheckerOperations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ActivateMakerOperations_TC7"));
		classes.add(new XmlClass("com.viteos.veda.master.formulatestscripts.FormulaSetupMaster_ActivateCheckerOperations_TC8"));*/
		
		/*classes.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_CheckerOperations_TC2"));
		classes.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ModifyMakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ModifyCheckerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_DeactivateMakerOperations_TC5"));
		classes.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_DeactivateCheckerOperaations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ActivateMakerOperations_TC7"));
		classes.add(new XmlClass("com.viteos.veda.master.seriestestscripts.SeriesMaster_ActivateCheckerOperations_TC8"));*/
		
		/*classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_CheckerOperations_TS2"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_MakerOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_CheckerOperations_TS4"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_ModifyMakerOperations_TS5"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_ModifyCheckerOperations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_DeactivateMakerOperations_TS7"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_DeActivateCheckerOperations_TC8"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParametersMaster_ActivateMakerOperations_TS9"));
		classes.add(new XmlClass("com.viteos.veda.master.parametertestscripts.ParameterMaster_ActivateCheckerOperations_TC10"));*/
		
		/*
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_CheckerOperations_TS2"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_ModifyMakerOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_ModifyCheckerOperations_TS4"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_MakerDeactivateOperations_TS5"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_CheckerDeactivateOperations_TS6"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_MakerActivateOperations_TS7"));
		classes.add(new XmlClass("com.viteos.veda.master.managementfeetestscripts.ManagementFeeMaster_CheckerActivateOperations_TS8"));*/
		
		
		/*classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerOperations_TS2"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerModifyOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerModifyOperations_TS4"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerDeactivateOperations_TS5"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerDeactivateOperations_TS6"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_MakerActivateOperations_TS7"));
		classes.add(new XmlClass("com.viteos.veda.master.incentivefeetestscripts.IncentiveFeeMaster_CheckerActivateOperations_TS8"));*/
		
		/*classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolderMaster_MakerScripts_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_CheckerOperations_TC_2"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ModifyMakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ModifyCheckerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_DeactivateMakerOperations_TC5"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_DeactivateCheckerOperations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ActivateMakerOperations_TC7"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ActivateCheckerOperations_TC8"));*/
		
		/*classes.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleCreationScript_TC1"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleModifyorVerifyScript_TC2"));
			classes.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleDeActivateScript_TC3"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.roletestscripts.RoleActivateScript_TC4"));*/
		
		//classes.add(new XmlClass("com.viteos.veda.usermanagement.grouptestscripts.GroupCreationTestScript_TC1"));
		/*classes.add(new XmlClass("com.viteos.veda.usermanagement.grouptestscripts.Group_ModifyOrVerifyTestScript_TC2"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.grouptestscripts.Group_DeActivation_TestScript_TC3"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.grouptestscripts.Group_Activate_TestScript_TC4"));*/
		
		/*classes.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserModify_TestScript_TC1"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserVerificationUser1_TetsScript_TC2"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserVerificationUser2_TestScript_TC3"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserVerificationUser3_TestScript_TC4"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserModify_TestScript_TC5"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserVerificationUser1_TetsScript_TC6"));
		classes.add(new XmlClass("com.viteos.veda.usermanagement.usertestscripts.UserModifiedRoleVerificationUser4_TetsScript_TC7"));*/
		
		/*classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolderMaster_MakerScripts_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_CheckerOperations_TC_2"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ModifyMakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ModifyCheckerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_DeactivateMakerOperations_TC5"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_DeactivateCheckerOperations_TC6"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ActivateMakerOperations_TC7"));
		classes.add(new XmlClass("com.viteos.veda.master.jointholderscripts.JointHolder_ActivateCheckerOperations_TC8"));
		
		classes.add(new XmlClass("com.viteos.veda.master.investoraccountmastertestscripts.InvestorAccountMaster_MakerOperations_TC1"));
		classes.add(new XmlClass("com.viteos.veda.master.investoraccountmastertestscripts.InvestorAccountMaster_ModifyMakerOperations_TC2"));
		classes.add(new XmlClass("com.viteos.veda.master.investoraccountmastertestscripts.InvestorAccountMaster_DeactivateMakerOperations_TC3"));
		classes.add(new XmlClass("com.viteos.veda.master.investoraccountmastertestscripts.InvestorAccountMaster_ActivateMakerOperations_TC4"));
		
		
		classes.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_CheckerOperations_TS2"));
		classes.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_ModifyMakerOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_ModifyCheckerOperations_TS4"));
		classes.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_DeactivateMakerOperations_TS5"));
		classes.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_DeactivateCheckerOperations_TS6"));
		classes.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_ActivateMakerOperations_TS7"));
		classes.add(new XmlClass("com.viteos.veda.master.investortestscripts.InvestorMaster_ActivateCheckerOperations_TS8"));
		
		
		classes.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_CheckerOperations_TS2"));
		classes.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_ModifyMakerOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_ModifyCheckerOperations_TS4"));
		classes.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_DeactivateMakerOperations_TS5"));
		classes.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_DeactivateCheckerOperations_TS6"));
		classes.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_ActivateMakerOperations_TS7"));
		classes.add(new XmlClass("com.viteos.veda.master.holdertestscripts.HolderMaster_ActivateCheckerOperations_TS8"));*/
		
		/*classes.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.CreateSubscription_TestScript_TC1"));
		classes.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.SubscriptionCheckerOperation_TC2"));
		classes.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.CheckerReviewdSubscriptionMakerOperations_TC4"));
		classes.add(new XmlClass("com.viteos.veda.transaction.subscriptiontestscripts.CheckerReviewedSubscriptionCheckerOperations_TC5"));
		

		/*classes.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.CreatingMakerRedemption_TC1"));
		classes.add(new XmlClass("com.viteos.veda.transaction.redemptiontestscripts.RedemptionCheckerOperation_TC2"));*/
		//classes.add(new XmlClass("com.viteos.veda.master.investoraccountmastertestscripts.InvestorAccountMaster_MakerOperations_TC1"));
		/*classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_MakerOperations_TS1"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_CheckerOperations_TS2"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ModifyMakerOperations_TS3"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ModifyCheckerOperations_TS4"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_DeactivateMakerOperations_TS5"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_DeactivateCheckerOperations_TS6"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ActivateMakerOperations_TS7"));
		classes.add(new XmlClass("com.viteos.veda.master.openingbalancetestscripts.OpeningBalanceMaster_ActivateCheckerOperations_TS8"));*/
		
		/*classes.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.CreatingMakerExchange_TC1"));
		classes.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.ExchangeCheckerOperation_TC2"));
		classes.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.CheckerReviewdExchangeMakerOperation_TC3"));
		classes.add(new XmlClass("com.viteos.veda.transaction.exchangetestscripts.CheckerReviewedExchangeCheckerOperations_TC4"));
*/		
		test.setXmlClasses(classes);
		
		List<XmlSuite> suites = new ArrayList<XmlSuite>();
		suites.add(suite);
		
		TestNG tng = new TestNG();
		tng.setXmlSuites(suites);
		tng.run();
		
		//int test  = Float.valueOf("8.0").intValue();
	}
}
