package com.aurelios;

import com.aurelios.client.ClientProxy;
import com.aurelios.common.ServerProxy;
import com.aurelios.common.network.AureliosPacketHandler;
import com.aurelios.common.commands.*;
import com.aurelios.common.event.*;
import com.aurelios.common.managers.Managers;
import com.aurelios.common.runnable.AbilityTimer;
import com.aurelios.common.runnable.GameTimer;
import com.aurelios.common.runnable.SlowTimer;
import com.aurelios.common.util.database.MongoUtils;
import com.aurelios.common.util.misc.Calendar;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
        id = "aurelios",
        name = "Aurelios",
        description = "A Sponge-Forge mod that takes inspiration from Fairy Tail and Ashes of Creation to create an MMORPG experience",
        authors = {
                "Jimmy",
                "Vectrix",
                "SwiftLee"
        }
)
public class Aurelios {

    public static Aurelios INSTANCE;
    public PluginContainer PLUGIN_CONTAINER;

    @Inject
    private Logger logger;

    private GameTimer gameTimer;
    private SlowTimer slowTimer;
    private AbilityTimer abilityTimer;
    private MongoUtils mongoUtils;
    private Calendar calendar;

    @Listener
    public void onGamePreInit(GamePreInitializationEvent event){
        AureliosPacketHandler.init();

        ServerProxy.INSTANCE.preInit();
        ClientProxy.INSTANCE.preInit();
    }

    @Listener
    public void onGameInit(GameInitializationEvent event){
        INSTANCE = this;
        PLUGIN_CONTAINER = Sponge.getPluginManager().fromInstance(this).get();

        mongoUtils = new MongoUtils("Admin", "admin", "@ds117749.mlab.com:17749/aurelios");
        mongoUtils.openConnection();

        ServerProxy.INSTANCE.init();
        ClientProxy.INSTANCE.init();
    }

    @Listener
    public void onServerStarting(GameStartingServerEvent event){
        //register managers

        registerListeners();
        registerRunnables();
        registerCommands();

        calendar = new Calendar();

        Managers.init();
    }

    @Listener
    public void onServerStopping(GameStoppingEvent event){
        getMongoUtils().close();
        Managers.AI.despawn();
    }

    public Logger getLogger() {
        return logger;
    }

    private void registerCommands() {
        new EcoCommands();
        new ParticleEffectCommands();
        new AbilityCommands();
        new PollCommands();
        new DialogueCommands();
        new TestCommands();
        new ToolCommands();
        new AICommands();
    }

    private void registerRunnables(){
        gameTimer = new GameTimer(5L);
        slowTimer = new SlowTimer(20L);
        abilityTimer = new AbilityTimer(1L);
    }

    private void registerListeners(){
        Sponge.getEventManager().registerListeners(this, new PlayerConnectionEvents());
        Sponge.getEventManager().registerListeners(this, new PlayerInteractEvents());
        Sponge.getEventManager().registerListeners(this, new ChatEvents());
        Sponge.getEventManager().registerListeners(this, new InventoryEvents());
        Sponge.getEventManager().registerListeners(this, new DataEvents());
    }

    public MongoUtils getMongoUtils() {
        return mongoUtils;
    }

    public GameTimer getGameTimer() {
        return gameTimer;
    }

    public AbilityTimer getAbilityTimer() {
        return abilityTimer;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
