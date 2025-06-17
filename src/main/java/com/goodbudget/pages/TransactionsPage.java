package com.goodbudget.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class TransactionsPage
{
    Page page;

    public TransactionsPage(Page page)
    {
        this.page = page;
    }

    public void newIncome(String payer, String amount)
    {
        page.click("xpath = //li[@id = 'incomeTab']");
        page.fill("#income-payer", payer);
        page.fill("xpath = //input[@name = 'income-amount']", amount);
        page.click("text = Save");
    }

    public String newExpenditureValidation(String payee, String envelope, String amount)
    {
        String msg = "";
        try {
            page.click("xpath = //li[@id = 'expenseTab']");
            page.locator("#expense-receiver").fill(payee);
            Thread.sleep(1000);
            page.locator("#expense-amount").fill(amount);
            page.locator("#addTransactionSave").click();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());

        }
        msg = page.locator("xpath = //div[@class = 'controls envelope']//label[@class = 'error']").textContent();
        page.locator("xpath = //div[@class = 'controls envelope']//select").click();
        Locator dropDowns = page.locator("xpath = //div[@class = 'controls envelope']//select");
        Locator opt = page.locator("xpath = //div[@class = 'controls envelope']//select//option");
        for (int i = 0; i < opt.count(); i++) {
            if (opt.nth(i).textContent().contains(envelope)) {
                dropDowns.selectOption(opt.nth(i).textContent());
                break;
            }
        }
        page.locator("xpath = //textarea[@id = 'expense-notes']").click();
        page.locator("xpath = //button[@id = 'addTransactionSave']").click();
        return msg;
    }

    public void newExpenditure(String payee, String envelope, String amount)
    {
        addExpenditure(envelope, payee, amount);
        saveChanges();
    }

    public void newExpenditure(String payee, String amount, String multiEnv, String envelope1, String envelope2)
    {
        addExpenditure(multiEnv, payee, amount);
        addMultiExpenditure(payee, amount, multiEnv, envelope1, envelope2);
        saveChanges();
    }

    private void addExpenditure(String envelope, String payee, String amount)
    {
        try {
            page.click("xpath = //li[@id = 'expenseTab']");
            page.locator("#expense-receiver").fill(payee);
            Thread.sleep(1000);
            page.locator("#expense-amount").fill(amount);
            page.locator("xpath = //div[@class = 'controls envelope']//select").click();
            Locator dropDowns = page.locator("xpath = //div[@class = 'controls envelope']//select");
            Locator opt = page.locator("xpath = //div[@class = 'controls envelope']//select//option");
            for (int i = 0; i < opt.count(); i++) {
                if (opt.nth(i).textContent().contains(envelope)) {
                    dropDowns.selectOption(opt.nth(i).textContent());
                    break;
                }
            }
        }
        catch(Exception ignored){}
    }

    private void addMultiExpenditure(String payee, String amount, String multiEnv, String envelope1, String envelope2)
    {
        try {
            for (int i = 0; i < 2; i++) {
                page.click("xpath = //select[@name = 'splitEnvelope" + i + "']");
                var dropDowns = page.locator("xpath = //select[@name = 'splitEnvelope" + i + "']");
                var options = page.locator("xpath = //select[@name = 'splitEnvelope" + i + "']//option");
                for (int j = 0; j < options.count(); j++) {
                    if (i == 0 && options.nth(j).textContent().contains(envelope1)) {
                        dropDowns.selectOption(options.nth(j).textContent());
                        Thread.sleep(1000);
                        page.fill("xpath = //select[@name = 'splitEnvelope" + i + "']//parent :: div//following-sibling :: div//input", Double.toString(Double.parseDouble(amount) / 2));
                        break;
                    } else if (i == 1 && options.nth(j).textContent().contains(envelope2)) {
                        dropDowns.selectOption(options.nth(j).textContent());
                        Thread.sleep(1000);
                        page.fill("xpath = //select[@name = 'splitEnvelope" + i + "']//parent :: div//following-sibling :: div//input", Double.toString(Double.parseDouble(amount) / 2));
                        break;
                    }
                }
            }
        }catch(Exception ignored){}
    }

    public void transferFunds(String transactionName, String amount, String donor, String receiver)
    {
        page.click("xpath = //li[@id = 'transferTab']");
        page.locator("#transfer-name").fill(transactionName);
        page.locator("#transfer-amount").fill(amount);
        page.locator("xpath = //div[@class = 'controls account-from']//select").click();
        var dropDowns = page.locator("xpath = //div[@class = 'controls account-from']//select");
        var options = page.locator("xpath = //div[@class = 'controls account-from']//select//option");
        for(int i=0; i<options.count(); i++)
        {
            var a = options.nth(i).textContent();
            if(options.nth(i).textContent().contains(donor))
                dropDowns.selectOption(options.nth(i).textContent());
        }
        page.locator("xpath = //div[@class = 'controls account-to']//select").click();
        dropDowns = page.locator("xpath = //div[@class = 'controls account-to']//select");
        options = page.locator("xpath = //div[@class = 'controls account-to']//select//option");
        for(int i=0; i<options.count(); i++)
        {
            if(options.nth(i).textContent().contains(receiver))
                dropDowns.selectOption(options.nth(i).textContent());
        }
    }

    public void saveChanges()
    {
        page.locator("//button[@id= 'addTransactionSave']").click();
    }
}
