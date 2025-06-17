package com.goodbudget.base;

import com.goodbudget.pages.*;
import com.goodbudget.utils.Utilities;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Base
{
    protected String browserName;
    public static ThreadLocal<Playwright> playwright = new ThreadLocal<>();
    public static ThreadLocal<Browser> browser = new ThreadLocal<>();
    public static ThreadLocal<BrowserContext> context = new ThreadLocal<>();
    public static ThreadLocal<Page> page = new ThreadLocal<>();

    protected Utilities utils;
    protected LoginPage loginPage;
    protected HomePage homePage;
    protected AccountsPage accountsPage;
    protected EnvelopesPage envelopesPage;
    protected TransactionsPage transactionsPage;
    protected FillEnveleopesPage fillEnveleopesPage;
    protected ReportsPage reportsPage;

    public void setUp(String browserName) {
        this.browserName = browserName;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        playwright.set(Playwright.create());
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions().setHeadless(false);

        Browser launchedBrowser = switch (browserName.toLowerCase()) {
            case "firefox" -> getPlaywright().firefox().launch(options);
            case "webkit" -> getPlaywright().webkit().launch(options);
            default -> getPlaywright().chromium().launch(options);
        };
        browser.set(launchedBrowser);

        context.set(getBrowser().newContext(new Browser.NewContextOptions().setViewportSize(width, height)));
        page.set(getContext().newPage());
    }

    @AfterEach
    public void resetApplication()
    {
        try {
            if (getPage() != null && !getPage().isClosed()) {
                try {
                    resetAccount();
                    getHomePage().logOut();
                } catch (Exception e) {
                    System.err.println("Error during resetAccount/logOut: ");
                    e.printStackTrace();
                }
                getPage().close();
            }
        } catch (Exception e) {
            System.err.println("Error closing Page: ");
            e.printStackTrace();
        }

        try {
            if (getContext() != null) getContext().close();
        } catch (Exception e) {
            System.err.println("Error closing Context: ");
            e.printStackTrace();
        }

        try {
            if (getBrowser() != null) getBrowser().close();
        } catch (Exception e) {
            System.err.println("Error closing Browser: ");
            e.printStackTrace();
        }

        try {
            if (getPlaywright() != null) getPlaywright().close();
        } catch (Exception e) {
            System.err.println("Error closing Playwright: ");
            e.printStackTrace();
        }

        flushData();

        page.remove();
        context.remove();
        browser.remove();
        playwright.remove();
    }


    public Playwright getPlaywright() {
        return playwright.get();
    }

    public Browser getBrowser() {
        return browser.get();
    }

    public BrowserContext getContext() {
        return context.get();
    }

    public Page getPage() {
        return page.get();
    }

    public void launchApplication(String user)
    {
        page.get().navigate("https://goodbudget.com/");
        getLoginPage().login(getUtilities().getUserName(user), getUtilities().getPwd());
        assertEquals(getUtilities().getUserName(user).split("@")[0], getHomePage().verifyUser());
    }

    public Utilities getUtilities()
    {
        if(utils == null)
            utils = new Utilities(getPage());
        return utils;
    }

    public LoginPage getLoginPage()
    {
        if(loginPage == null)
            loginPage = new LoginPage(getPage());
        return loginPage;
    }

    public HomePage getHomePage()
    {
        if(homePage == null)
            homePage = new HomePage(getPage());
        return homePage;
    }

    public AccountsPage getAccountPage()
    {
        if(accountsPage == null)
            accountsPage = new AccountsPage(getPage());
        return accountsPage;
    }

    public EnvelopesPage getEnvelopesPage()
    {
        if(envelopesPage == null)
            envelopesPage = new EnvelopesPage(getPage());
        return envelopesPage;
    }

    public TransactionsPage getTransactionsPage()
    {
        if(transactionsPage == null)
            transactionsPage = new TransactionsPage(getPage());
        return transactionsPage;
    }

    public FillEnveleopesPage getFillEnvelopesPage()
    {
        if(fillEnveleopesPage == null)
            fillEnveleopesPage = new FillEnveleopesPage(getPage());
        return fillEnveleopesPage;
    }

    public ReportsPage getReportsPage()
    {
        if(reportsPage == null)
            reportsPage = new ReportsPage(getPage());
        return reportsPage;
    }

    public void resetAccount()
    {
        getHomePage().navigateToHome();
        getHomePage().deleteAllTransactions();
        getHomePage().editEnvelopes();
        getEnvelopesPage().removeEnvelope(getUtilities().getEnvelopeName());
        getEnvelopesPage().saveChanges();
        getHomePage().navigateToHome();
    }

    public void flushData()
    {
        getUtilities().flushData();
    }
}
