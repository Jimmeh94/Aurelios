package com.aurelios.game.economy;


import com.aurelios.game.user.UserPlayer;
import com.aurelios.managers.Managers;
import com.aurelios.util.text.Message;
import com.aurelios.util.text.Messager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Account {

    private double amount;
    private boolean blockedFromOverdraft = false;
    private double overdraftedAmount = 0;
    private double overdraftCeiling = 100; //max amount a player can overdraft
    private UserPlayer owner;
    private List<MoneyRequest> moneyRequests;

    public Account(UserPlayer user){
        this.owner = user;
        moneyRequests = new CopyOnWriteArrayList<>();

        //TODO load amount
        amount = 100;
    }

    public void addMoneyRequest(UUID asker, double amount){
        for(MoneyRequest moneyRequest: moneyRequests){
            if(moneyRequest.getAsker().equals(asker) || moneyRequest.getAsker() == asker || moneyRequest.getAsker().toString().equals(asker.toString())){
                moneyRequest.setAmount(amount);
                return;
            }
        }

        moneyRequests.add(new MoneyRequest(asker, amount));
    }

    public void payMoneyRequest(MoneyRequest moneyRequest){
        if(canAfford(moneyRequest.getAmount())){

        } else {
            TransactionResult.NOT_ENOUGH_MONEY.display(this, moneyRequest.getAmount());
        }
    }

    public void setBlockedFromOverdraft(boolean blockedFromOverdraft) {
        this.blockedFromOverdraft = blockedFromOverdraft;
    }

    public boolean canAfford(double amount){
        return this.amount >= amount || (Managers.ECO.canOverdraft() && !blockedFromOverdraft && (overdraftCeiling - overdraftedAmount >= amount));
    }

    public void withdraw(double amount){
        if(canAfford(amount)){
            if(this.amount >= amount){
                this.amount -= amount;
            } else if(Managers.ECO.canOverdraft() && this.amount < amount && this.amount > 0){
                amount -= this.amount;
                if(overdraftCeiling - overdraftedAmount < amount){
                    TransactionResult.NOT_ENOUGH_MONEY.display(this, amount);
                    return;
                }

                overdraftedAmount += amount;
                this.amount = 0;
            }

            TransactionResult.SUCCESS.display(this);
        } else {
            TransactionResult.NOT_ENOUGH_MONEY.display(this, amount);
        }
    }

    public void deposit(double amount){
        if(!blockedFromOverdraft && overdraftedAmount > 0){
            if(amount > overdraftedAmount){
                amount -= overdraftedAmount;
                overdraftedAmount = 0;
            } else {
                overdraftedAmount -= amount;
            }
        }

        this.amount += amount;
        TransactionResult.SUCCESS.display(this);
    }

    public void showBalance(){
        Message.Builder builder = Message.builder();
        builder.addReceiver(owner.getPlayer()).addMessage(Text.of(TextColors.GOLD, "Your Account: "), Messager.Prefix.ECO)
                                .addAsChild(Text.of(TextColors.GREEN, " Balance: " + Managers.ECO.getCurrencySymbol() + amount), TextColors.GOLD);

        if(Managers.ECO.canOverdraft()){
            builder.addAsChild(Text.of(TextColors.RED, " Overdrafted: " + Managers.ECO.getCurrencySymbol() + overdraftedAmount), TextColors.GOLD);
        }

        Messager.sendMessage(builder.build());
    }

    public void showBalanceTo(Player player){
        Message.Builder builder = Message.builder();
        builder.addReceiver(player).addMessage(Text.of(TextColors.GOLD, owner.getPlayer().getDisplayNameData().displayName().get().toPlain() + "'s Account: "), Messager.Prefix.ECO)
                .addAsChild(Text.of(TextColors.GREEN, " Balance: " + Managers.ECO.getCurrencySymbol() + amount), TextColors.GOLD);

        if(Managers.ECO.canOverdraft()){
            builder.addAsChild(Text.of(TextColors.RED, " Overdrafted: " + Managers.ECO.getCurrencySymbol() + overdraftedAmount), TextColors.GOLD);
        }

        Messager.sendMessage(builder.build());
    }

    public UserPlayer getOwner() {
        return owner;
    }

    public double getBalance() {
        return amount;
    }
}
