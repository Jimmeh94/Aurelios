package com.aurelios.common.game.user;

import com.aurelios.common.game.dialogue.test.OldMan;
import com.aurelios.common.game.economy.Account;
import com.aurelios.common.game.user.chat.ChatColorTemplate;
import com.aurelios.common.game.user.chat.Title;
import com.aurelios.common.game.user.chat.channel.ChatChannel;
import com.aurelios.common.game.user.chat.channel.ChatChannelReceiver;
import com.aurelios.common.game.user.modules.tools.ModuleAreaCreator;
import com.aurelios.common.game.user.modules.tools.ModulePOICreator;
import com.aurelios.common.game.user.modules.UserModule;
import com.aurelios.common.game.user.modules.UserModuleTypes;
import com.aurelios.common.game.user.modules.poll.Poll;
import com.aurelios.common.game.user.scoreboard.Scoreboard;
import com.aurelios.common.managers.Managers;
import org.spongepowered.api.entity.living.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserPlayer extends User {

    private Player player;
    private Account account;
    private Scoreboard scoreboard;
    private ChatChannel chatChannel;
    private ChatChannelReceiver receiveChannel;
    private ChatColorTemplate chatColorTemplate;
    private Title title;
    private PlayerKeys playerKeys;
    private List<UserModule> modules;

    /**
     * REMOVE THIS
     */
    private OldMan oldMan = new OldMan();

    public void display(){
        oldMan.displayDialogue(this);
    }
    /**
     * REMOVE ABOVE
     */

    public UserPlayer(Player player) {
        super(player);

        this.player = player;
        playerKeys = new PlayerKeys();
        account = new Account(this);

        setChatChannel(Managers.CHAT.GLOBAL);
        chatColorTemplate = ChatColorTemplate.GRAY;
        title = Title.TEST;

        modules = new CopyOnWriteArrayList<>();
        modules.add(new Poll.PollBuilder(this));

        //TODO REMOVE THIS
        if(player.getDisplayNameData().displayName().get().toPlain().contains("Jimmeh")){
            modules.add(new ModuleAreaCreator(this));
            modules.add(new ModulePOICreator(this));
        }

        scoreboard = new Scoreboard(this);

        updateCurrentArea();
    }

    public boolean hasModule(UserModuleTypes type){
        for(UserModule module: modules){
            if(module.getType() == type){
                return true;
            }
        }
        return false;
    }

    public Optional<UserModule> getModule(UserModuleTypes type){
        for(UserModule module: modules){
            if(module.getType() == type){
                return Optional.of(module);
            }
        }
        return Optional.empty();

    }

    @Override
    public void cleanUp(){
        super.cleanUp();

        scoreboard.unregisterScoreboard();
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerKeys getPlayerKeys() {
        return playerKeys;
    }

    public void setChatChannel(ChatChannel chatChannel) {
        if(this.chatChannel != null)
            this.chatChannel.removeMember(this);
        this.chatChannel = chatChannel;
        this.chatChannel.addMember(this);
    }

    public ChatChannel getChatChannel() {
        return chatChannel;
    }

    public ChatColorTemplate getChatColorTemplate() {
        return chatColorTemplate;
    }

    public Title getTitle() {
        return title;
    }

    public Account getAccount() {
        return account;
    }

    public void setReceiveChannel(ChatChannelReceiver receiveChannel) {
        this.receiveChannel = receiveChannel;
    }

    public boolean hasReceiverChannel(){return receiveChannel != null;}

    public ChatChannelReceiver getReceiveChannel() {
        return receiveChannel;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public List<UserModule> getModules() {
        return modules;
    }
}
