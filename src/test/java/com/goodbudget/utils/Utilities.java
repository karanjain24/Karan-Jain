package com.goodbudget.utils;

import com.google.gson.JsonObject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.*;

public class Utilities
{
    Page page;
    JsonObject data;
    private double accBalance = 0.00;
    private Map<String, Double> envelopeSpending = new HashMap<String, Double>();

    public Utilities(Page page)
    {
        this.page = page;
        data = JsonDataLoader.loadJson("testdata.json");
    }

    public void checkFillDialog()
    {
        try{Thread.sleep(3000);}catch (Exception ignored){}
        //page.locator(".fill-envelopes-modal").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));
        Locator modal = page.locator(".fill-envelopes-modal");

        if (modal.isVisible()) {
            System.out.println("Modal detected. Clicking 'No thanks'...");
            page.locator("#fillEnvelopesModalNo").click();
        } else {
            System.out.println("No modal appeared.");
        }
    }

    public void addToExpectedAccountBalance(String amount)
    {
        accBalance+= Double.parseDouble(amount);
    }

    public void removeFromExpectedAccountBalance(String amount)
    {
        accBalance-= Double.parseDouble(amount);
    }

    public String getExpectedAccountBalance()
    {
        return String.format("%.2f",accBalance);
    }

    public String getUserName(String user)
    {
        if(user.equalsIgnoreCase("user1"))
            return data.get("username1").getAsString();
        else if(user.equalsIgnoreCase("user2"))
            return data.get("username2").getAsString();
        else
            return data.get("username3").getAsString();
    }
    public String getUser()
    {
        return data.get("user").getAsString();
    }

    public String getPwd()
    {
        return data.get("password").getAsString();
    }

    public String getInitialAmount()
    {
        return data.get("initialBalance").getAsString();
    }

    public String getEnvelopeName()
    {
        return data.getAsJsonObject("envelope").get("name").getAsString();
    }

    public String getEnvelopeAmount()
    {
        return data.getAsJsonObject("envelope").get("amount").getAsString();
    }

    public String getIncomePayer()
    {
        return data.getAsJsonObject("income").get("payer").getAsString();
    }

    public String getIncomeAmount()
    {
        return data.getAsJsonObject("income").get("amount").getAsString();
    }

    public String getTransactionPayee(String type)
    {
        return getTransactionData(type, "payee");
    }

    public String getTransactionEnvelope(String type)
    {
        return getTransactionData(type, "envelopes");
    }

    public String getTransactionEnvelope(String type, int index)
    {
        return getTransactionData(type, "envelopes "+Integer.toString(index));
    }

    public String getTransactionAmount(String type)
    {
        return getTransactionData(type, "amount");
    }

    private String getTransactionData(String type, String entity)
    {
        if (type.equals("Single")) {
            return data.getAsJsonArray("transaction").get(0).getAsJsonObject().get(entity).getAsString();
        }
        if (entity.contains("envelopes")) {
            return data.getAsJsonArray("transaction").get(1).getAsJsonObject().get(entity.split(" ")[0]).getAsJsonObject().get(entity.split(" ")[1]).getAsString();
        }

        return data.getAsJsonArray("transaction").get(1).getAsJsonObject().get(entity).getAsString();
    }

    public void updateExpectedEnvelopeSpending(String envelope, String amount)
    {
        if(envelopeSpending.containsKey(envelope))
            envelopeSpending.put(envelope,envelopeSpending.get(envelope)+Double.parseDouble(amount));
        else
            envelopeSpending.put(envelope, Double.parseDouble(amount));
    }

    public String getExpectedEnvelopeSpending(String envelope)
    {
        for(Map.Entry<String, Double> entry : envelopeSpending.entrySet())
        {
            if(entry.getKey().equalsIgnoreCase(envelope))
                return Double.toString(entry.getValue());
        }
        return null;
    }

    public double getExpectedTotalEnvelopesSpending()
    {
        double spending = 0.00;
        for(Map.Entry<String, Double> entry : envelopeSpending.entrySet())
        {
            spending+= entry.getValue();
        }
        return spending;
    }

    public void flushData()
    {
        accBalance = 0.00;
        envelopeSpending = null;
    }
}
