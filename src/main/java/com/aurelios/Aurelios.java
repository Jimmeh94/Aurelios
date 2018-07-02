package com.aurelios;

import com.aurelios.server.IProxy;
import com.aurelios.server.network.AureliosPacketHandler;
import com.aurelios.server.commands.*;
import com.aurelios.server.event.*;
import com.aurelios.server.managers.Managers;
import com.aurelios.server.runnable.Timers;
import com.aurelios.server.util.database.MongoUtils;
import com.aurelios.server.util.misc.Calendar;
import com.google.inject.Inject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

@Plugin(
        id = Aurelios.ID,
        name = Aurelios.NAME,
        description = "A Sponge-Forge mod that takes inspiration from Fairy Tail and Ashes of Creation to create an MMORPG experience",
        authors = {
                "Jimmy",
                "Vectrix",
                "SwiftLee"
        }
)

public class Aurelios {

    @Mod.Instance
    public static Aurelios INSTANCE;

    public PluginContainer PLUGIN_CONTAINER;

    public static final String ID = "aurelios";
    public static final String NAME = "Aurelios";

    @Inject
    private Logger logger;
    private MongoUtils mongoUtils;
    private Calendar calendar;
    private Timers timers;

    @SidedProxy(clientSide = "com.aurelios.client.ClientProxy", serverSide = "com.aurelios.server.ServerProxy", modId = ID)
    public static IProxy proxy;

    /**
     * ************* FML EVENTS ******************************
     */
    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent event){
        proxy.preInit();
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event){
        proxy.init();
        AureliosPacketHandler.init();
    }

    @Mod.EventHandler
    public void onFMLServerStarting(FMLServerStartingEvent event){
        //event.registerServerCommand(new com.aurelios.server.commands.forge.ToolCommands());
    }

    @Mod.EventHandler
    public void onStop(FMLServerStoppingEvent event){
        proxy.stoppingServer();
    }

    /**
     * ********** SPONGE EVENTS *****************************
     */

    @Listener
    public void onServerStarting(GameStartingServerEvent event){
        INSTANCE = this;
        PLUGIN_CONTAINER = Sponge.getPluginManager().fromInstance(this).get();

        mongoUtils = new MongoUtils("Admin", "admin", "@ds117749.mlab.com:17749/aurelios");
        mongoUtils.openConnection();

        calendar = new Calendar();
        Managers.init();

        registerCommands();
        registerListeners();
        timers = new Timers();
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

    public Calendar getCalendar() {
        return calendar;
    }

    public Timers getTimers() {
        return timers;
    }
}
