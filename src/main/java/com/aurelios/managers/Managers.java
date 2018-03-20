package com.aurelios.managers;

public class Managers {

    public static final PollManager POLL = new PollManager();
    public static final AbilityManager ABILITY = new AbilityManager();
    public static final EconomyManager ECO = new EconomyManager();
    public static final UserManager USER = new UserManager();
    public static final NodeManager NODES = new NodeManager();
    public static final ChatChannelManager CHAT = new ChatChannelManager();
    public static final AbilityCollisionManager ABILITY_COLLISION = new AbilityCollisionManager();
    public static final BlockManager BLOCK = new BlockManager();
    public static final AIManager AI = new AIManager();

    /**
     * Anything that the managers need to initialize, post initialization
     */
    public static void init() {
        CHAT.init();
        NODES.load();
        AI.load();
    }
}
