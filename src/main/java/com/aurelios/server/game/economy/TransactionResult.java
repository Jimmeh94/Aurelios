package com.aurelios.server.game.economy;


import com.aurelios.server.util.text.Message;
import com.aurelios.server.util.text.Messager;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public enum TransactionResult {

    SUCCESS,
    NOT_ENOUGH_MONEY;

    public void display(Account player){display(player, 0.0);}

    public void display(Account player, double amount){
        if(this == SUCCESS){
            Messager.sendMessage(Message.builder().addReceiver(player.getOwner().getPlayer()).addMessage(Text.of(TextColors.GREEN, "Transaction was successful!"), Messager.Prefix.ECO).build());
        } else {
            Messager.sendMessage(Message.builder().addReceiver(player.getOwner().getPlayer()).addMessage(Text.of(TextColors.RED, "You don't have enough money! Needed: " + amount + " Balance: " + player.getBalance()), Messager.Prefix.ECO).build());
        }

        player.showBalance();
    }

}
