package com.aurelios.server.managers;


import com.aurelios.server.game.economy.Account;
import com.aurelios.server.game.economy.TransactionResult;
import org.spongepowered.api.Sponge;

public class EconomyManager{

    private boolean overdraft = true;
    private String currencySymbol = "$";

    public void sendMoney(Account sender, Account receiver, double amount){
        if(sender.canAfford(amount)){
            sender.withdraw(amount);
            receiver.deposit(amount);
            TransactionResult.SUCCESS.display(sender);
            TransactionResult.SUCCESS.display(receiver);

        } else {
            TransactionResult.NOT_ENOUGH_MONEY.display(sender);
        }
    }

    public boolean canOverdraft() {
        return overdraft;
    }

    public void setOverdraft(boolean overdraft) {
        this.overdraft = overdraft;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
