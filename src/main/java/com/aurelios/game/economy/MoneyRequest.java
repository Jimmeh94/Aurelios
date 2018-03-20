package com.aurelios.game.economy;

import java.util.UUID;

public class MoneyRequest {

    private UUID asker;
    private double amount;

    public MoneyRequest(UUID asker, double amount) {
        this.asker = asker;
        this.amount = amount;
    }

    public UUID getAsker() {
        return asker;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
