package com.aurelios.server.event.custom;

import com.aurelios.server.game.environment.nodes.Area;
import com.aurelios.server.game.environment.nodes.Node;
import com.aurelios.server.game.user.UserPlayer;
import org.spongepowered.api.event.cause.Cause;

public abstract class AreaEvents extends CustomEvent {

    private UserPlayer player;
    private Area area;

    public AreaEvents(Area area, UserPlayer player, Cause cause) {
        super(cause);

        this.area = area;
        this.player = player;
    }

    public static class PlayerEnterArea extends AreaEvents{

        public PlayerEnterArea(Area area, UserPlayer player, Cause cause) {
            super(area, player, cause);
        }
    }

    public static class PlayerLeaveArea extends AreaEvents{

        public PlayerLeaveArea(Area area, UserPlayer player, Cause cause) {
            super(area, player, cause);
        }
    }
}
