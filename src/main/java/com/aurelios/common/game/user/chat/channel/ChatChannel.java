package com.aurelios.common.game.user.chat.channel;

import com.aurelios.common.game.user.User;
import com.aurelios.common.game.user.UserPlayer;
import com.aurelios.common.managers.Managers;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ChatChannel{

    private String key;
    private Text prefix;
    protected List<UserPlayer> members = new CopyOnWriteArrayList<>();
    private boolean permanent = false;
    protected boolean passwordProtected = true;

    public ChatChannel(String key, boolean p, Text prefix){
        this.key = key;
        this.permanent = p;
        this.prefix = prefix;

        Managers.CHAT.add(this);
    }

    public ChatChannel(String key, boolean p, Text prefix, UserPlayer owner) {
        this(key, p, prefix, new UserPlayer[]{owner});
    }

    public ChatChannel(String key, boolean p, Text prefix, UserPlayer... members) {
        this(key, p, prefix, Arrays.asList(members));
    }
    
    public ChatChannel(String key,boolean p, Text prefix, List<UserPlayer> members){
        this.members.addAll(members);
        this.key = key;
        this.permanent = p;
        this.prefix = prefix.trim();

        Managers.CHAT.add(this);
        for(UserPlayer messageReceiver: members){
            Managers.CHAT.setChannel(messageReceiver, this.key);
        }
    }

    public void displayMessage(Text message){
        for(UserPlayer userPlayer: members){
            userPlayer.getPlayer().sendMessage(Text.builder().append(prefix).append(Text.of(prefix.getColor(), "| ")).append(message).build());
        }
    }

    public String getKey() {
        return key;
    }

    public boolean hasMember(User userPlayer){return members.contains(userPlayer);}

    public List<UserPlayer> getMembers() {
        return members;
    }

    public boolean addMember(UserPlayer member) {
        if(members.contains(member))
            return false;
        return this.members.add(member);
    }

    public void removeMember(User member) {
        this.members.remove(member);
        if(members.size() == 0 && !permanent){
            Managers.CHAT.remove(this);
        }
    }

    public boolean isPasswordProtected() {
        return passwordProtected;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public Text getPrefix() {
        return prefix;
    }
}
