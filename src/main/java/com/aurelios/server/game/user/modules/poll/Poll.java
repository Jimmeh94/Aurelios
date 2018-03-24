package com.aurelios.server.game.user.modules.poll;

import com.aurelios.server.game.user.User;
import com.aurelios.server.game.user.UserPlayer;
import com.aurelios.server.game.user.modules.UserModule;
import com.aurelios.server.game.user.modules.UserModuleTypes;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.util.misc.Pair;
import com.aurelios.server.util.text.Message;
import com.aurelios.server.util.text.Messager;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Poll{

    private final String title;
    private int timeLimit; //in minutes
    private List<Pair<String, Integer>> choices;
    private long whenStarted;
    private List<User> voted;
    private UserPlayer owner;

    public Poll(PollBuilder pollBuilder) {
        this.choices = pollBuilder.choices;
        this.timeLimit = pollBuilder.timeLimit;
        this.title = pollBuilder.title;
        whenStarted = System.currentTimeMillis();
        voted = new ArrayList<>();

        owner = pollBuilder.getOwner();
        Managers.POLL.add(this);

        displayChat(Optional.<UserPlayer>empty());
    }

    public ItemStack getItemRepresentation(){
        ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.PAPER).build();
        itemStack.offer(Keys.DISPLAY_NAME, Text.of(TextColors.GOLD, title));

        List<Text> lore = new ArrayList<>();
        lore.add(Text.of(" "));

        for(Pair<String, Integer> pair: choices){
            int percentage = 0;
            if(getTotalVotes() > 0)
                percentage =  (pair.getSecond() / getTotalVotes()) * 100;

            lore.add(Text.of(TextColors.GRAY, pair.getFirst() + ": " + pair.getSecond() + " votes, " + percentage + "%"));
        }

        itemStack.offer(Keys.ITEM_LORE, lore);
        return itemStack;
    }

    public void displayChat(Optional<UserPlayer> receiver){
        Message.Builder builder = Message.builder();
        builder.addMessage(Text.of(" "));
        builder.addMessage(Text.of("==============================="));
        builder.addMessage(Text.of(TextColors.GRAY, title), Messager.Prefix.POLL);

        for(Pair<String, Integer> pair: choices){
            builder.addAsChild(Text.builder(pair.getFirst()).color(TextColors.GRAY).onClick(TextActions.executeCallback(new Consumer<CommandSource>() {
                @Override
                public void accept(CommandSource commandSource) {
                    if (commandSource instanceof Player) {
                        Player player = (Player) commandSource;
                        UserPlayer user = Managers.USER.findPlayer(player.getUniqueId()).get();

                        if (!Managers.POLL.isRunningPoll(owner)) {
                            Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "This poll is finished!"), Messager.Prefix.ERROR).build());
                            return;
                        }

                        if (!voted.contains(user)) {
                            pair.setSecond(pair.getSecond() + 1);
                            voted.add(user);
                            Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.GREEN, "Vote submitted!"), Messager.Prefix.SUCCESS).build());
                            displayResultChat(user);
                        } else {
                            Messager.sendMessage(Message.builder().addReceiver(player).addMessage(Text.of(TextColors.RED, "You already voted in this poll!"), Messager.Prefix.ERROR).build());
                        }
                    }
                }
            })).build(), TextColors.GOLD);
        }

        if(receiver.isPresent()){
            builder.addReceiver(receiver.get().getPlayer());
        } else {
            for(User user: Managers.USER.getObjects()){
                if(user == owner || user.equals(owner) || !user.isPlayer() || !((UserPlayer)user).getPlayerKeys().MISC_SHOW_POLLS_IN_CHAT.getValue())
                    continue;

                builder.addReceiver(((UserPlayer)user).getPlayer());
            }
        }

        builder.addMessage(Text.of("==============================="));
        builder.addMessage(Text.of(" "));

        Messager.sendMessage(builder.build());

    }

    public void displayResultChat(UserPlayer user){
        Message.Builder builder = Message.builder();
        builder.addMessage(Text.of(" "));
        builder.addMessage(Text.of("==============================="));
        builder.addMessage(Text.of(TextColors.GRAY, title), Messager.Prefix.POLL);

        for(Pair<String, Integer> pair: choices){
            int percentage = 0;
            if(voted.size() > 0)
                percentage = (pair.getSecond() / voted.size()) * 100;

            builder.addAsChild(Text.of(TextColors.GRAY, pair.getFirst() + " (" + pair.getSecond() + ", " + percentage + "%)"), TextColors.GOLD);
        }

        builder.addReceiver(user.getPlayer());

        builder.addMessage(Text.of("==============================="));
        builder.addMessage(Text.of(" "));

        Messager.sendMessage(builder.build());
    }

    public static PollBuilder builder(UserPlayer owner) {
        return new PollBuilder(owner);
    }

    public long getWhenStarted() {
        return whenStarted;
    }

    public int getLength() {
        return timeLimit;
    }

    public void stop() {
        Managers.POLL.remove(this);
        Messager.sendMessage(Message.builder().addReceiver(owner.getPlayer()).addMessage(Text.of(TextColors.RED, "Your poll has ended!"), Messager.Prefix.INFO).build());
        displayResultChat(owner);
    }

    public String getTitle() {
        return title;
    }

    public List<Pair<String, Integer>> getChoices() {
        return choices;
    }

    public Integer getTotalVotes() {
        return voted.size();
    }

    public UserPlayer getOwner() {
        return owner;
    }

    public static class PollBuilder extends UserModule{

        private List<Pair<String, Integer>> choices;
        private int timeLimit = 3; //in minutes
        private String title;

        @Override
        public String getDisabledMessage() {
            return "You can't create polls right now";
        }

        @Override
        public String getEnabledMessage() {
            return "You can now create polls again";
        }

        public PollBuilder(UserPlayer owner){
            super(owner, UserModuleTypes.POLL_BUILDER);
        }

        public PollBuilder title(String title){
            this.title = title;
            return this;
        }

        public PollBuilder choice(String choice){
            if(choices == null){
                choices = new ArrayList<>();
            }

            choices.add(new Pair<>(choice, 0));
            return this;
        }

        public PollBuilder timeLength(int minutes){
            this.timeLimit = minutes;
            return this;
        }

        public Poll build(){
            Poll poll = new Poll(this);
            clear();
            return poll;
        }

        public void clear(){
            title = "";
            if(choices != null) {
                choices.clear();
            }
        }

        public void end() {
            Messager.sendMessage(Message.builder().addReceiver(getOwner().getPlayer()).addMessage(Text.of("Poll canceled!")).build());
        }

        public String getTitle() {
            return title;
        }

        public List<Pair<String, Integer>> getChoices() {
            return choices;
        }
    }

}
