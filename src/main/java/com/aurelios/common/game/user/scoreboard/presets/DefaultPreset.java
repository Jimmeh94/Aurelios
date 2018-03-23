package com.aurelios.common.game.user.scoreboard.presets;

import com.aurelios.Aurelios;
import com.aurelios.common.ServerProxy;
import com.aurelios.common.game.user.UserPlayer;
import com.aurelios.common.game.user.stats.Stat;
import com.aurelios.common.util.misc.StringUtils;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.List;

public class DefaultPreset extends ScoreboardPreset {

    public DefaultPreset(UserPlayer player){
        super(player);
        updateSAurelioss();
    }

    @Override
    public void updateSAurelioss() {
        UserPlayer owner = getOwner();
        List<Text> strings = new ArrayList<>();

        strings.add(Text.of(TextStyles.BOLD, "Ashes of Creation"));
        strings.add(Text.of("=============="));
        //strings.add(owner.getPresentArea().getDisplayName());
        strings.add(Text.of(TextColors.RED));
        strings.add(Text.of("Bounty: 0"));
        strings.add(Text.of(TextColors.RED, TextColors.AQUA));
        strings.add(Text.of("Gold: " + owner.getAccount().getBalance()));
        strings.add(Text.of(TextColors.RED, TextColors.BLACK));
        strings.add(Text.of("Magic: Fire Dragon Slayer"));
        strings.add(Text.of(TextColors.RED, TextColors.GREEN));
        strings.add(Text.of("Chat: " + owner.getChatChannel().getPrefix().toPlain()));
        strings.add(Text.of(TextColors.RED, TextColors.GOLD));
        strings.add(Text.of("Mana: " + owner.getStats().find(Stat.Type.MANA).get().getAmount()));
        strings.add(Text.of("Time: " + Aurelios.INSTANCE.getCalendar().getCurrentTime()
                .getFormattedTime(false)));
        strings.add(Text.of("Day: " + StringUtils.enumToString(Aurelios.INSTANCE.getCalendar().getCurrentDayOfWeek(), true)));

        if(owner.getCurrentArea() != null) {
            strings.add(Text.of(TextColors.RED, TextColors.DARK_AQUA));
            strings.add(Text.of("Node: " + owner.getCurrentArea().getDisplayName().toPlain()));
        }

        setSAurelioss(strings);
    }
}
