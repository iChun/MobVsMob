package com.mobvsmob;

import ichun.core.LoggerHelper;
import ichun.core.iChunUtil;
import ichun.core.config.Config;
import ichun.core.config.ConfigHandler;
import ichun.core.config.IConfigUser;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Property;

import com.mobvsmob.client.core.PacketHandlerClient;
import com.mobvsmob.common.core.CommonProxy;
import com.mobvsmob.common.core.ConnectionHandler;
import com.mobvsmob.common.core.MapPacketHandler;
import com.mobvsmob.common.core.PacketHandlerServer;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkModHandler;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "MobVsMob", name = "MobVsMob",
			version = MobVsMob.version,
			dependencies = "required-after:iChunUtil@[2.3.0,)"
				)
@NetworkMod(clientSideRequired = true,
			serverSideRequired = false,
			versionBounds = "[" + iChunUtil.versionMC + "." + MobVsMob.versionMajor + ".0" + "," + iChunUtil.versionMC + "." + (MobVsMob.versionMajor + 1) + ".0" + ")",
			connectionHandler = ConnectionHandler.class,
			tinyPacketHandler = MapPacketHandler.class,
			clientPacketHandlerSpec = @SidedPacketHandler(channels = { "MobVsMob" }, packetHandler = PacketHandlerClient.class ),
			serverPacketHandlerSpec = @SidedPacketHandler(channels = { "MobVsMob" }, packetHandler = PacketHandlerServer.class )
				)
public class MobVsMob 
	implements IConfigUser
{
	public static final int versionMajor = 0;
	public static final int versionMinor = 0;
	public static final String version = iChunUtil.versionMC + "." + MobVsMob.versionMajor + "." + MobVsMob.versionMinor;

	@Instance("MobVsMob")
	public static MobVsMob instance;

	@SidedProxy(clientSide = "com.mobvsmob.client.core.ClientProxy", serverSide = "com.mobvsmob.common.core.CommonProxy")
	public static CommonProxy proxy;

	private static final Logger logger = LoggerHelper.createLogger("MobVsMob");

	public static Config config;

	@Override
	public boolean onConfigChange(Config cfg, Property prop) { return true; }

	@EventHandler
	public void preLoad(FMLPreInitializationEvent event)
	{
		config = ConfigHandler.createConfig(event.getSuggestedConfigurationFile(), "mobvsmob", "MobVsMob", logger, instance);
	}

	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		GameRegistry.registerPlayerTracker(new ConnectionHandler());
		
		proxy.initMod();
		
		MinecraftForge.EVENT_BUS.register(new com.mobvsmob.common.core.EventHandler());
	}

	public static int getNetId()
	{
		return ((NetworkModHandler)FMLNetworkHandler.instance().findNetworkModHandler(MobVsMob.instance)).getNetworkId(); 	//For Use of MapPackets
	}

	public static void console(String s, boolean warning)
	{
		StringBuilder sb = new StringBuilder();
		logger.log(warning ? Level.WARNING : Level.INFO, sb.append("[").append(version).append("] ").append(s).toString());
	}
}
