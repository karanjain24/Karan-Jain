package com.goodbudget.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class HomePage
{
    Page page;
    private String strEnvelopeNames = "//div[@id = 'wrapper-envelopes']//ul[@class = 'nodes buckets']//div[@class = 'wrapper-item category sub']//following-sibling :: ul//li//div[@class = 'left']//strong";
    private String strEnvelopeAmounts = "//ul[@class = 'nodes']//div[@class = 'right']//p//em";
    private String strEnvelopeAmountRemaining = "//div[@id = 'wrapper-envelopes']//ul[@class = 'nodes buckets']//div[@class = 'wrapper-item category sub']//following-sibling :: ul//li//div[@class = 'right']//strong";
    private String strAccountsBalance = "//strong[contains(text() , 'Available')]//ancestor :: div[1] // following-sibling :: div//strong";
    private String strTransactionRecords = "//tr[@id = 'see-all-scheduled']//following-sibling :: tr";

    public HomePage(Page page)
    {
        this.page = page;
    }

    public void logOut()
    {
        page.locator("xpath = //a[text() = 'Logout']").click();
    }

    public String verifyUser()
    {
        return page.locator("xpath = //span[@class = 'walkme-pii']").first().textContent();
    }

    public void navigateToAccountsTab()
    {
        try{Thread.sleep(2000);}catch (Exception ignored){}
        page.locator("xpath = //a[@data-target = 'accounts']").click();
    }

    public void navigateToEnvelopesTab()
    {
        page.locator("xpath = //a[@data-target = 'envelopes']").click();
    }

    public void navigateToHome()
    {
        page.locator("//a[text() = 'Home']").click();
    }

    public void navigateToReports(String report)
    {
        page.locator("xpath = //a[text() = 'Reports ']//span").click();
        page.locator("xpath = //a[text() = '"+report+"']").click();
    }

    public void editAccount()
    {
        navigateToAccountsTab();
        page.locator("xpath = //div[@id = 'wrapper-accounts']//a[text() = 'Add / Edit']").click();
    }

    public void editEnvelopes()
    {
        navigateToEnvelopesTab();
        page.locator("xpath = //div[@id = 'wrapper-envelopes']//a[text() = 'Add / Edit']").click();
    }

    public void addTransaction()
    {
        page.locator("xpath = //a[@class = 'btn addTransaction']").click();
    }

    public void fillEnvelopes()
    {
        page.locator("xpath = //a[@href = '/envelopes/fill']").click();
    }

    public boolean verifyAddedEnvelopes(String envelope, String amount)
    {
        page.locator("xpath = //a[@data-target = 'envelopes']").click();
        var envelopes = page.locator(strEnvelopeNames).allTextContents();
        for(int i=0; i<envelopes.size(); i++)
        {
            if (envelopes.get(i).equalsIgnoreCase(envelope))
            {
                if(page.locator(strEnvelopeAmounts).nth(i).textContent().equals(amount))
                    return true;
            }
        }
        return false;
    }

    public String verifyAccountBalance()
    {
        navigateToEnvelopesTab();
        try{Thread.sleep(1000);}catch(Exception ignored){}
        //page.locator(strAccountsBalance).first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        return page.locator(strAccountsBalance).nth(1).textContent().replace(",", "");
    }

    public boolean verifyFilledEnvelopes()
    {
        navigateToEnvelopesTab();
        var setAmount = page.locator(strEnvelopeAmounts).allTextContents();
        var availableAmount = page.locator(strEnvelopeAmountRemaining).allTextContents();
        for(int i =1; i<availableAmount.size()-1; i++)
        {
            if(!setAmount.get(i).equals(availableAmount.get(i)))
                return false;
        }
        return true;
    }

    public BigDecimal verifyEnvelopeTransaction(String envelope)
    {
        navigateToEnvelopesTab();
        //page.locator(strEnvelopeAmounts).first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        var setAmount = page.locator(strEnvelopeAmounts).allTextContents();
        var availableAmount = page.locator(strEnvelopeAmountRemaining).allTextContents();
        for(int i=1; i<availableAmount.size()-1; i++)
        {
            if(page.locator(strEnvelopeAmountRemaining + "//ancestor :: div[1]//preceding-sibling :: div//strong").nth(i).textContent().equalsIgnoreCase(envelope))
            {
                var expenditure = new BigDecimal(setAmount.get(i)).subtract(new BigDecimal(availableAmount.get(i)));
                return expenditure;//new BigDecimal(amount).equals(expenditure);
            }
        }
        return new BigDecimal("0.00");
    }

    public boolean verifyLatestTransaction(String payee, String entity, String amount)
    {
        try {
            Thread.sleep(2000);
        } catch (Exception ignored) {}

        if(entity == null) {
            entity = "[Available]";
            amount = "+"+amount;
        }
        if(payee == null)
            payee = "Updated My Account";

        String finalPayee = payee;
        String finalEntity = entity;

        String actualAmount = page.locator(strTransactionRecords + "//td//strong").first().textContent().replace(",","");

        String payeeText = page.locator(strTransactionRecords + "//th[@class='payee']").first().textContent();

        String[] details = payeeText.split("[\\n.]");

        boolean hasPayee = Arrays.stream(details).anyMatch(text -> text.contains(finalPayee));

        boolean hasEntity = Arrays.stream(details).anyMatch(text -> text.contains(finalEntity));

        boolean amountEqual = actualAmount.equals(amount);
        return hasPayee && hasEntity && amountEqual;
    }

    public boolean verifyScheduledTransaction(String payee, String entity, String amount)
    {
        boolean flag = verifyLatestTransaction(payee, entity, amount);
        boolean flag2 = page.locator("xpath = //tr[@id = 'see-all-scheduled']//following-sibling :: tr//th[@class='payee']//strong[text() = '"+payee+"']//preceding-sibling :: em").textContent().equalsIgnoreCase("[Scheduled]");
        return flag && flag2;
    }

    public boolean checkIfTransactionExists(String payee)
    {
        page.locator("xpath = //tbody//th[@class = 'payee']//strong").first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        List<String> payees = null;
        payees = page.locator("xpath = //tbody//th[@class = 'payee']//strong").allTextContents();
        return payees.stream().anyMatch(name -> name.equalsIgnoreCase(payee));
    }

    public String getActualBalance()
    {
        return page.locator("xpath = //ul[@class = 'totals']//div[@class = 'right']//strong").first().textContent().replace(",","");
    }

    public BigDecimal getActualTotalEnvelopeSpending()
    {
        BigDecimal envAmt = new BigDecimal("0.00");
        BigDecimal envAmtRemaining = new BigDecimal("0.00");
        try {
            navigateToEnvelopesTab();

            page.locator("xpath = //ul[@class = 'nodes buckets']//li//strong[text() = 'Available']").first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            Locator l1 = page.locator(strEnvelopeAmounts);
            Locator l2 = page.locator(strEnvelopeAmountRemaining);
            for (int i = 1; i < l1.allTextContents().size() - 1; i++)
                envAmt = envAmt.add(new BigDecimal(l1.allTextContents().get(i)));
            for (int j = 1; j < l1.allTextContents().size() - 1; j++) {
                l2.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
                envAmtRemaining = envAmtRemaining.add(new BigDecimal(l2.allTextContents().get(j)));
            }
        }
        catch(Exception e){}
        return envAmt.subtract(envAmtRemaining);
    }

    public String getEnvelopeAvailableFunds(String envelope)
    {
        navigateToEnvelopesTab();
        var v = page.locator("xpath = //strong[text() = '"+envelope+"']//ancestor :: div[@class = 'left']//following-sibling :: div[@class = 'right']//strong").textContent().replace(",","");
        var amount = Math.round(Double.parseDouble(v) * 100.0)/100.0;
        return v;
    }

    public void deleteAllTransactions()
    {
        try
        {
            Thread.sleep(1000);
            page.locator("xpath = //div[@id = 'transactions-scroll']").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
            var count = page.locator("xpath = //div[@id = 'transactions-scroll']//tbody//tr").count();
            while (count > 1) {
                System.out.println("while loop");
                Thread.sleep(1000);
                page.locator("xpath = //div[@id = 'transactions-scroll']//tbody//tr//th[@class = 'payee']").first().click();
                Thread.sleep(1000);
                Locator button1 = page.locator("xpath = //button[contains(@class , 'delete')]");

                if (button1.count() > 0) {
                    if (button1.first().isVisible())
                        button1.first().click();
                    else
                        button1.last().click();
                }
                Thread.sleep(1000);
                deleteDialog("Delete");
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void deleteDialog(String operation)
    {
        Locator modal = page.locator("#deleteConfirm");
        if(modal.isVisible())
        {
            System.out.println(modal.locator("xpath = //div[@class = 'modal-body']//p").textContent());
            modal.locator("xpath = //div[@class = 'modal-footer']//button[contains(text() , '"+operation+"')]").click();
        }
    }

    public String getEnvelopesHeaderMessage(String type)
    {
        Locator locator = page.locator("xpath = //strong[text() = 'Monthly']");
        if(locator.isVisible())
        {
            locator.click();
            Locator header = page.locator("xpath = //h1[text() = '"+type+"']//following-sibling :: p");
            if(header.isVisible())
                return header.textContent();
        }
        return null;
    }
}
