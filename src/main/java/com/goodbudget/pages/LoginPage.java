package com.goodbudget.pages;

import com.microsoft.playwright.Page;

public class LoginPage
{
    private Page page;

    public LoginPage(Page page)
    {
        this.page = page;
    }

    public void login(String userName, String password)
    {
        page.click("text = Log in");
        page.fill("#username", userName);
        page.fill("#password", password);
        page.locator("xpath = //span[@class = 'elementor-button-text']").click();
    }
}
