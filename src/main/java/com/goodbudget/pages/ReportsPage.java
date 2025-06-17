package com.goodbudget.pages;

import com.microsoft.playwright.Page;

public class ReportsPage
{
    Page page;

    public ReportsPage(Page page)
    {
        this.page = page;
    }

    public double calculateTotalSpending()
    {
        return Double.parseDouble(page.locator("xpath = //tfoot//th").nth(2).textContent());
    }
}
