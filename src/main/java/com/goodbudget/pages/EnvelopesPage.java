package com.goodbudget.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class EnvelopesPage
{
    Page page;

    public EnvelopesPage(Page page)
    {
        this.page = page;
    }

    String strAllBuckets = "//div[@class = 'all-buckets']";
    String strMainEnvelopeContainer = strAllBuckets + "//envelope-list";
    String strMoreEnvelopesContainer = strAllBuckets + "//envelope-irr-list";

    String strAddEnvBtn = "//div//button[@class = 'btn btn-add']";  //Container+
    String strEnvRows = "//ul[contains(@class, 'envelope-list')]//li";  //Container+
    String strEnvNames = "//div[contains(@class , 'row-name')]//input"; //Container+Rows+
    String strEnvAmounts = "//div[contains(@class , 'row-amount')]//input"; //Container+Rows
    String strEnvRemoveButton = "//i[@class = 'icon-remove-sign']"; //Container+Rows

    public void AddMonthlyEnvelope(String name, String amount) {
        //Thread.sleep(3000);
        page.locator(strMainEnvelopeContainer+strEnvNames).first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(strMainEnvelopeContainer+strAddEnvBtn).click();
        page.locator(strMainEnvelopeContainer+strEnvNames).last().fill(name);
        page.locator(strMainEnvelopeContainer+strEnvRows+strEnvAmounts).last().fill(amount);
        page.locator(strMainEnvelopeContainer+strEnvRows+strEnvAmounts).last().press("Enter");
    }

    public String AddMonthlyEnvelope(String name1, String name2, String amount)
    {
        page.locator(strMainEnvelopeContainer+strEnvNames).first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        page.locator(strMainEnvelopeContainer+strAddEnvBtn).click();
        page.locator(strMainEnvelopeContainer+strEnvNames).last().fill(name1);
        page.locator(strMainEnvelopeContainer+strEnvRows+strEnvAmounts).last().fill(amount);
        page.locator(strMainEnvelopeContainer+strEnvRows+strEnvAmounts).last().press("Enter");
        saveChanges();
        String msg = page.locator("xpath = //div[@class = 'add-edit-errors']//label").first().textContent();
        page.locator(strMainEnvelopeContainer+strEnvNames).last().fill(name2);
        saveChanges();
        return msg;
    }

    public void removeEnvelope(String envelope)
    {
        page.locator("xpath = //div[@class = 'row-name']//input").first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        Locator inputs = page.locator("xpath = //div[@class = 'row-name']//input");
        for(int i=0; i<inputs.count(); i++)
        {
            if(inputs.nth(i).inputValue().equals(envelope)) {
                page.locator(strEnvRemoveButton).nth(i).click();
                checkDeleteModal();
                break;
            }
        }

    }

    public void checkDeleteModal()
    {
        Locator deleteModal = page.locator("xpath = //app-modal//div[@id = 'confirm-delete']").nth(2);
        if(deleteModal.isVisible())
        {
            String s = deleteModal.locator("xpath = //app-modal//div[@id = 'confirm-delete']//p").nth(2).textContent();
            System.out.println(s);
            deleteModal.locator("xpath = //app-modal//div[@id = 'confirm-delete']//div[@class = 'modal-footer']//button[text() = 'Delete']").nth(2).click();
        }
    }

    public void saveChanges()
    {
        try{Thread.sleep(2000);}catch (Exception ignored){}
        page.locator("xpath = //button[@id = 'save-envelopes-btn']").click();
    }
}
