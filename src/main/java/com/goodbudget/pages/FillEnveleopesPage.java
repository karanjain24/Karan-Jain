package com.goodbudget.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class FillEnveleopesPage
{
    Page page;

    public FillEnveleopesPage(Page page)
    {
        this.page = page;
    }

    public void distributeToEnvelopes(String tab)
    {
        page.click("xpath = //a[text() = '"+tab+"']");
        Locator dropDowns = page.locator("xpath = //div[@class = 'fill-section']//div[@class = 'controls']//span[@class = 'caret']");
        for(int i=0; i< dropDowns.count(); i++)
        {
            dropDowns.nth(i).click();
            page.locator("xpath = //span[@class = 'startAmount']").nth(i).click();
        }
    }

    public void addIncomeAndFillEnvelopes(String payee, String amount, boolean schedule)
    {
        page.locator("xpath = //a[text() = 'New Income']").click();
        page.locator("xpath = //input[@id = 'specify-amount']").fill(amount);
        page.locator("xpath = //input[@placeholder = 'Income']").fill(payee);
        distributeToEnvelopes("New Income");
        if(schedule)
        {
            page.locator("xpath = (//input[@name = 'scheduleCheckbox'])[1]").click();
            Locator scheduleSelection = page.locator("xpath = (//select[@name = 'schedulethis'])[1]");
            scheduleSelection.selectOption("Every month");
        }
    }

    public void saveChanges()
    {
        if(page.locator("xpath = //div[@id = 'incomeSummary']//button[@class = 'btn btn-success set-submit-clicked']").isVisible())
            page.locator("xpath = //div[@id = 'incomeSummary']//button[@class = 'btn btn-success set-submit-clicked']").click();
        else
            page.click("xpath = //div[@id = 'unallocatedSummary']//button[@class = 'btn btn-success set-submit-clicked']");
    }
}
