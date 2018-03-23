package com.aurelios.common.game.dialogue;

import com.aurelios.common.game.user.User;
import com.aurelios.common.game.user.UserPlayer;
import com.aurelios.common.util.text.Message;
import com.aurelios.common.util.text.Messager;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A dialogue is simply an opening sentence/question followed by options/answers
 */
public class Dialogue {

    public static DialogueBuilder builder() {
        return new DialogueBuilder();
    }

    private Text openingSentence;
    private Choice[] choices;

    public Dialogue(DialogueBuilder builder){
        this.openingSentence = builder.openingSentence;
        this.choices = builder.choices.toArray(new Choice[builder.choices.size()]);
    }

    public void display(UserPlayer user){
        Message.Builder builder = Message.builder().addReceiver(user.getPlayer()).addMessage(openingSentence);

        for(Choice text: choices){
            if(text.meetsRequirement(user))
                builder.addAsChild(text.getText(), TextColors.GOLD);
        }

        Messager.sendMessage(builder.build());
    }

    public static class DialogueBuilder{

        private Text openingSentence;
        private List<Choice> choices = new ArrayList<>();

        public DialogueBuilder openingSentence(Text text){
            this.openingSentence = text;
            return this;
        }

        public DialogueBuilder choice(Choice choice){
            choices.add(choice);
            return this;
        }

        public Dialogue build(){
            return new Dialogue(this);
        }

    }

    public static class Choice{

        private Text text;
        private Optional<DialogueRequirement> requirement;

        public Choice(Optional<DialogueRequirement> requirement, Text text) {
            this.text = text;
            this.requirement = requirement;
        }

        public boolean meetsRequirement(User user){
            return !requirement.isPresent() || requirement.get().meetsRequirement(user);
        }

        public Text getText() {
            return text;
        }
    }

    public interface DialogueRequirement{

        boolean meetsRequirement(User user);
    }


}
