package com.aurelios;

import com.aurelios.commands.*;
import com.aurelios.event.*;
import com.aurelios.managers.Managers;
import com.aurelios.runnable.AbilityTimer;
import com.aurelios.runnable.GameTimer;
import com.aurelios.runnable.SlowTimer;
import com.aurelios.util.database.MongoUtils;
import com.aurelios.util.misc.Calendar;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
        id = "aurelios",
        name = "Aurelios",
        description = "A Sponge-Forge mod that takes inspiration from Fairy Tail and Ashes of Creation to create an MMORPG experience",
        authors = {
                "Jimmy",
                "Vextrix",
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
    public void onGameInit(GameInitializationEvent event){
        INSTANCE = this;
        PLUGIN_CONTAINER = Sponge.getPluginManager().fromInstance(this).get();

        mongoUtils = new MongoUtils("Admin", "admin", "@ds241737.mlab.com:41737/com.aurelios-sponge");
        mongoUtils.openConnection();
    }

    @Listener
    public void onServerStarting(GameStartingServerEvent event){
        //register managers

        Managers.init();

        registerListeners();
        registerRunnables();
        registerCommands();

        calendar = new Calendar();
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

    @Listener
    public void onServerStopping(GameStoppingEvent event){
        mongoUtils.close();
        Managers.AI.despawn();
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

    public Logger getLogger() {
        return logger;
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
