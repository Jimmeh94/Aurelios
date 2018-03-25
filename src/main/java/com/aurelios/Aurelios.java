package com.aurelios;

import com.aurelios.server.IProxy;
import com.aurelios.server.network.AureliosPacketHandler;
import com.aurelios.server.commands.*;
import com.aurelios.server.event.*;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.runnable.AbilityTimer;
import com.aurelios.server.runnable.GameTimer;
import com.aurelios.server.runnable.SlowTimer;
import com.aurelios.server.util.database.MongoUtils;
import com.aurelios.server.util.misc.Calendar;
import com.google.inject.Inject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
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

//@Mod(modid = "aurelios", name = "Aurelios", version = "1.0.0")
public class Aurelios {

    @Mod.Instance
    public static Aurelios INSTANCE;

    public PluginContainer PLUGIN_CONTAINER;

    @Inject
    private Logger logger;

    private GameTimer gameTimer;
    private SlowTimer slowTimer;
    private AbilityTimer abilityTimer;
    private MongoUtils mongoUtils;
    private Calendar calendar;

    @SidedProxy(clientSide = "com.aurelios.client.ClientProxy", serverSide = "com.aurelios.server.ServerProxy", modId = "aurelios")
    public static IProxy proxy;

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event){
        proxy.preInit();
    }

    @Listener
    public void onGamePreInit(GamePreInitializationEvent event){
       // AureliosPacketHandler.init();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event){
        proxy.init();
        AureliosPacketHandler.init();
    }

    @Listener
    public void onGameInit(GameInitializationEvent event){
        INSTANCE = this;
        PLUGIN_CONTAINER = Sponge.getPluginManager().fromInstance(this).get();

        mongoUtils = new MongoUtils("Admin", "admin", "@ds117749.mlab.com:17749/aurelios");
        mongoUtils.openConnection();
    }

    @Mod.EventHandler
    public void onFMLServerStarting(FMLServerStartingEvent event){
        //event.registerServerCommand(new com.aurelios.server.commands.forge.ToolCommands());
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
        proxy.stoppingServer();
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
