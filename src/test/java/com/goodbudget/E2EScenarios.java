package com.goodbudget;

import com.goodbudget.base.Base;
import jdk.jfr.Description;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class E2EScenarios extends Base
{
    @ParameterizedTest
    @ValueSource(strings = {"firefox"}) //"chromium", "firefox", "webkit"
    @Description("End To End scenario to test Adding Initial Funds, New Income, Adding Envelopes, Filling Envelopes,Adding Transactions, Verifying All Activities and Resetting")
    public void e2eScenario1(String browserName)
    {
        setUp(browserName);
        launchApplication("user1");

        //Add Initial Amount to Account
        getHomePage().navigateToAccountsTab();
        getHomePage().editAccount();
        getAccountPage().setCurrentBalance(getUtilities().getInitialAmount());
        getAccountPage().saveChanges();

        getUtilities().addToExpectedAccountBalance(getUtilities().getInitialAmount());

        assertEquals(getUtilities().getExpectedAccountBalance(), getHomePage().verifyAccountBalance());
        assertTrue(getHomePage().verifyLatestTransaction(null, null, getUtilities().getInitialAmount()));

        //Add Envelope
        getHomePage().editEnvelopes();
        getEnvelopesPage().AddMonthlyEnvelope(getUtilities().getEnvelopeName(), getUtilities().getEnvelopeAmount());    //dynamic
        getEnvelopesPage().saveChanges();
        getUtilities().checkFillDialog();
        assertTrue(getHomePage().verifyAddedEnvelopes(getUtilities().getEnvelopeName(), getUtilities().getEnvelopeAmount()));  //dynamic

        //Add Income
        getHomePage().addTransaction();
        getTransactionsPage().newIncome(getUtilities().getIncomePayer(), getUtilities().getIncomeAmount());   //add to existing amount

        getUtilities().addToExpectedAccountBalance(getUtilities().getIncomeAmount());

        assertEquals(getUtilities().getExpectedAccountBalance(), getHomePage().verifyAccountBalance());
        assertTrue(getHomePage().verifyLatestTransaction(getUtilities().getIncomePayer(), null, getUtilities().getIncomeAmount()));

        //Fill Envelopes
        getHomePage().fillEnvelopes();
        getFillEnvelopesPage().distributeToEnvelopes("Available");
        getFillEnvelopesPage().saveChanges();

        assertTrue(getHomePage().verifyFilledEnvelopes());

        //Add Single Expenditure
        getHomePage().addTransaction();
        getTransactionsPage().newExpenditure(getUtilities().getTransactionPayee("Single")
                , getUtilities().getTransactionEnvelope("Single")
                , getUtilities().getTransactionAmount("Single"));
        assertTrue(getHomePage().verifyLatestTransaction(getUtilities().getTransactionPayee("Single")
                , getUtilities().getTransactionEnvelope("Single")
                , getUtilities().getTransactionAmount("Single")));

        getUtilities().removeFromExpectedAccountBalance(getUtilities().getTransactionAmount("Single"));
        getUtilities().updateExpectedEnvelopeSpending(getUtilities().getTransactionEnvelope("Single"), getUtilities().getTransactionAmount("Single"));

        //Add Multi Expenditure
        getHomePage().addTransaction();
        getTransactionsPage().newExpenditure(getUtilities().getTransactionPayee("Multiple")
                , getUtilities().getTransactionAmount("Multiple")
                , "Multiple"
                , getUtilities().getTransactionEnvelope("Multiple", 1)
                , getUtilities().getTransactionEnvelope("Multiple", 2));
        assertTrue(getHomePage().verifyLatestTransaction(getUtilities().getTransactionPayee("Multiple")
                , "(Multiple)", getUtilities().getTransactionAmount("Multiple")));

        getUtilities().removeFromExpectedAccountBalance(getUtilities().getTransactionAmount("Multiple"));
        getUtilities().updateExpectedEnvelopeSpending(getUtilities().getTransactionEnvelope("Multiple", 1), Double.toString(Double.parseDouble(getUtilities().getTransactionAmount("Multiple")) / 2));
        getUtilities().updateExpectedEnvelopeSpending(getUtilities().getTransactionEnvelope("Multiple", 2), Double.toString(Double.parseDouble(getUtilities().getTransactionAmount("Multiple")) / 2));

        //Verify Expected and Actual Balance
        assertEquals(getUtilities().getExpectedAccountBalance(), getHomePage().getActualBalance());

        //Verify Total Envelope Spending from Reports
        getHomePage().navigateToReports("Spending by Envelope");
        assertEquals(getUtilities().getExpectedTotalEnvelopesSpending(), getReportsPage().calculateTotalSpending());

    }

    @ParameterizedTest
    @ValueSource(strings = {"firefox"})    //"chromium", "firefox", "webkit"
    @Description("End To End Scenario to Test Over-Spending from Envelopes, Transferring Funds among Envelopes, Verifying All Activities and Resetting")
    public void e2eScenario2(String browserName)
    {
        setUp(browserName);
        launchApplication("user2");

        //Add Income
        getHomePage().addTransaction();
        getTransactionsPage().newIncome(getUtilities().getIncomePayer(), getUtilities().getIncomeAmount());   //add to existing amount

        getUtilities().addToExpectedAccountBalance(getUtilities().getIncomeAmount());

        assertEquals(getUtilities().getExpectedAccountBalance(), getHomePage().verifyAccountBalance());
        assertTrue(getHomePage().verifyLatestTransaction(getUtilities().getIncomePayer(), null, getUtilities().getIncomeAmount()));

        //Add Envelope
        getHomePage().editEnvelopes();
        getEnvelopesPage().AddMonthlyEnvelope(getUtilities().getEnvelopeName(), getUtilities().getEnvelopeAmount());    //dynamic
        getEnvelopesPage().saveChanges();
        getUtilities().checkFillDialog();

        assertTrue(getHomePage().verifyAddedEnvelopes(getUtilities().getEnvelopeName(), getUtilities().getEnvelopeAmount()));

        //Fill Envelopes
        getHomePage().fillEnvelopes();
        getFillEnvelopesPage().distributeToEnvelopes("Available");
        getFillEnvelopesPage().saveChanges();

        assertTrue(getHomePage().verifyFilledEnvelopes());

        //Add Single Expenditure More Than The Envelope Capacity
        getHomePage().addTransaction();
        getTransactionsPage().newExpenditure(getUtilities().getTransactionPayee("Single")
                , getUtilities().getTransactionEnvelope("Single")
                , new BigDecimal(getUtilities().getTransactionAmount("Single")).add(new BigDecimal("100.00")).toString());
        assertTrue(getHomePage().verifyLatestTransaction(getUtilities().getTransactionPayee("Single")
                , getUtilities().getTransactionEnvelope("Single")
                , new BigDecimal(getUtilities().getTransactionAmount("Single")).add(new BigDecimal("100.00")).toString()));

        getUtilities().removeFromExpectedAccountBalance(getUtilities().getTransactionAmount("Single"));
        getUtilities().updateExpectedEnvelopeSpending(getUtilities().getTransactionEnvelope("Single"), getUtilities().getTransactionAmount("Single"));

        //Transfer Funds Among Envelopes
        String initialFundsGas = getHomePage().getEnvelopeAvailableFunds("Gas");
        String initialFundsGroceries = getHomePage().getEnvelopeAvailableFunds("Groceries");
        getHomePage().addTransaction();
        getTransactionsPage().transferFunds("Envelope Transfer", "50.00", "Groceries", "Gas");
        getTransactionsPage().saveChanges();
        var finalFundsGas = getHomePage().getEnvelopeAvailableFunds("Gas");
        var finalFundsGroceries = getHomePage().getEnvelopeAvailableFunds("Groceries");

        assertEquals(new BigDecimal("50.00"), new BigDecimal(finalFundsGas).subtract(new BigDecimal(initialFundsGas)));
        assertEquals(new BigDecimal("50.00"), new BigDecimal(initialFundsGroceries).subtract(new BigDecimal(finalFundsGroceries)));

        //Remove Filled Envelope
        var initialAvailableFunds = getHomePage().getEnvelopeAvailableFunds("Available");
        getHomePage().editEnvelopes();
        getEnvelopesPage().removeEnvelope("Rent");
        getEnvelopesPage().saveChanges();

        var finalAvailableFunds = getHomePage().getEnvelopeAvailableFunds("Available");
        assertEquals(new BigDecimal("800.00"), new BigDecimal(finalAvailableFunds).subtract(new BigDecimal(initialAvailableFunds)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"firefox"})    //"chromium", "firefox", "webkit"
    @Description("Validation Checks for Adding Envelopes, Overdraft, Scheduling of Transaction, Overfilling Envelopes")
    public void validationChecks(String browserName)
    {
        setUp(browserName);
        launchApplication("user3");

        //Envelope Name Validation
        getHomePage().editEnvelopes();
        assertEquals("Envelope names must be unique and not blank.", getEnvelopesPage().AddMonthlyEnvelope("Gas", "Rent", "800.00"));
        getUtilities().checkFillDialog();

        getHomePage().addTransaction();
        assertEquals("This field is required.", getTransactionsPage().newExpenditureValidation("Mall", "Gas", "10.00"));

        //Overdraft Validation
        getHomePage().navigateToAccountsTab();
        getHomePage().editAccount();
        assertEquals("Overdraft", getAccountPage().verifyOverdraft());
        getAccountPage().saveChanges();

        getHomePage().navigateToEnvelopesTab();
        assertEquals("Hmm, negative money. Interesting...", getHomePage().getEnvelopesHeaderMessage("Monthly"));

        //Fill Envelopes -> Add New Income, Schedule It and Fill Envelopes
        getHomePage().fillEnvelopes();
        getFillEnvelopesPage().addIncomeAndFillEnvelopes("Salary", "2000.00", true);
        getFillEnvelopesPage().saveChanges();
        assertTrue(getHomePage().verifyScheduledTransaction("Salary", "(Multiple)", "+2000.00"));
        assertFalse(getHomePage().checkIfTransactionExists("Fill Envelopes"));

        //Overfill Envelopes
        getHomePage().fillEnvelopes();
        getFillEnvelopesPage().distributeToEnvelopes("Available");
        getFillEnvelopesPage().saveChanges();

        assertFalse(getHomePage().verifyFilledEnvelopes());
        assertTrue(getHomePage().checkIfTransactionExists("Fill Envelopes"));
    }
}
