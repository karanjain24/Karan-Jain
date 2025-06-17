package com.goodbudget.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class AccountsPage
{
    Page page;

    public AccountsPage(Page page)
    {
        this.page = page;
    }

    public void setCurrentBalance(String balance)
    {
        page.fill("xpath = //div[@class = 'row-amount']//input", balance);
        page.locator("xpath = //div[@class = 'row-amount']//input").press("Enter");
    }

    public void deleteAccountActivity()
    {
        page.locator("#addTransactionDelete").click();
    }

    public String verifyOverdraft()
    {
        Locator locator = page.locator("xpath = //div[@class = 'row-amount']");
        locator.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        if(locator.isVisible())
            return page.locator("xpath = //div[@class = 'row-amount']//span").textContent();
        return "No Overdraft";
    }

    public void saveChanges()
    {
        page.click("#save-accounts-btn");
    }

}
