package com.gildedgames.aether.common;

import com.gildedgames.aether.api.AetherAPI;
import com.gildedgames.aether.api.IAetherServices;
import com.gildedgames.aether.api.net.IGildedGamesAccountApi;
import com.gildedgames.aether.common.dungeons.DungeonViewer;
import com.gildedgames.aether.common.events.PostAetherTravelEvent;
import com.gildedgames.aether.common.network.api.GildedGamesAccountApiImpl;
import com.gildedgames.aether.common.shop.*;
import com.gildedgames.aether.common.shop.filters.ShopFilterNewYearsEdisonSale;
import com.gildedgames.aether.common.world.biomes.irradiated_forests.IrradiatedForestsData;
import com.gildedgames.aether.common.world.biomes.magnetic_hills.MagneticHillPillar;
import com.gildedgames.aether.common.world.biomes.magnetic_hills.MagneticHillsData;
import com.gildedgames.aether.common.world.instances.necromancer_tower.NecromancerTowerInstance;
import com.gildedgames.aether.common.world.island.IslandVariables;
import com.gildedgames.orbis.lib.OrbisLib;
import com.gildedgames.orbis.lib.util.io.IClassSerializer;
import com.gildedgames.orbis.lib.util.io.Instantiator;
import com.gildedgames.orbis.lib.util.io.SimpleSerializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.*;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Random;
import java.util.function.Supplier;

public class CommonProxy implements IAetherServices
{
	private final ContentRegistry contentRegistry = new ContentRegistry();

	private final GildedGamesAccountApiImpl webAPI = new GildedGamesAccountApiImpl();

	private File configDir;

	public void preInit(final FMLPreInitializationEvent event)
	{
		CompatibilityAether.locateInstalledMods();

		this.configDir = new File(event.getModConfigurationDirectory(), "Aether/");

		if (!this.configDir.exists() && !this.configDir.mkdir())
		{
			throw new RuntimeException("Couldn't create configuration directory");
		}

		final IClassSerializer s = new SimpleSerializer(AetherCore.MOD_ID);

		s.register(0, NecromancerTowerInstance.class, new Instantiator<>(NecromancerTowerInstance.class));
		s.register(1, MagneticHillsData.class, new Instantiator<>(MagneticHillsData.class));
		s.register(2, MagneticHillPillar.class, new Instantiator<>(MagneticHillPillar.class));
		s.register(3, IrradiatedForestsData.class, new Instantiator<>(IrradiatedForestsData.class));
		s.register(4, IslandVariables.class, new Instantiator<>(IslandVariables.class));
		s.register(5, ShopInstance.class, new Instantiator<>(ShopInstance.class));
		s.register(6, ShopBuy.class, new Instantiator<>(ShopBuy.class));
		s.register(7, ShopInventory.class, new Instantiator<>(ShopInventory.class));
		s.register(8, ShopCurrencyGilt.class, new Instantiator<>(ShopCurrencyGilt.class));
		s.register(9, ShopCurrencyPlumproot.class, new Instantiator<>(ShopCurrencyPlumproot.class));
		s.register(10, ShopInstanceGroup.class, new Instantiator<>(ShopInstanceGroup.class));

		OrbisLib.services().io().register(s);

		this.contentRegistry.preInit();

		AetherAPI.content().shop().registerGlobalFilter(new ShopFilterNewYearsEdisonSale());
	}

	public void init(final FMLInitializationEvent event)
	{
		this.contentRegistry.init();

		MinecraftForge.EVENT_BUS.register(AetherCore.CONFIG);
}

	public void postInit(final FMLPostInitializationEvent event)
	{
		this.content().postInit();

//		DungeonViewer viewer = new DungeonViewer();
//		MinecraftForge.EVENT_BUS.register(viewer);
	}

	public void onServerAboutToStart(final FMLServerAboutToStartEvent event)
	{
		this.content().onServerAboutToStart();
	}

	public void onServerStarting(final FMLServerStartingEvent event)
	{
		this.content().onServerStarting();
	}

	public void spawnJumpParticles(final World world, final double x, final double y, final double z, final double radius, final int quantity)
	{
		final Random random = world.rand;

		for (int i = 0; i < quantity; i++)
		{
			final double x2 = x + (random.nextDouble() * radius) - (radius * 0.5D);
			final double y2 = y + (random.nextDouble() * 0.4D);
			final double z2 = z + (random.nextDouble() * radius) - (radius * 0.5D);

			world.spawnParticle(EnumParticleTypes.CLOUD, x2, y2, z2, 0.0D, random.nextDouble() * 0.03D, 0.0D);
		}
	}

	public void spawnCampfireStartParticles(final World world, final double x, final double y, final double z)
	{

	}

	public void spawnCampfireParticles(final World world, final double x, final double y, final double z)
	{

	}

	public void spawnSlashParticleFrom(
			final World world, final double x, final double y, final double z, final double offsetX, final double offsetY, final double offsetZ)
	{

	}

	public void spawnPierceParticleFrom(
			final World world, final double x, final double y, final double z, final double offsetX, final double offsetY, final double offsetZ)
	{

	}

	public void spawnImpactParticleFrom(
			final World world, final double x, final double y, final double z, final double offsetX, final double offsetY, final double offsetZ)
	{

	}

	public void spawnEffectParticles(final World world, double posXIn, double posYIn, double posZIn, double motionX, double motionY, double motionZ, float particleRed,
									 float particleGreen, float particleBlue)
	{

	}

	public File getConfigDir()
	{
		return this.configDir;
	}

	public void displayDismountMessage(final EntityPlayer player)
	{

	}

	public void turnOffScreen()
	{

	}

	public void modifyEntityQuicksoil(final EntityLivingBase entity)
	{
		entity.motionX *= 1.7D;
		entity.motionZ *= 1.7D;

		final double maxMotion = 0.7D;

		entity.motionX = MathHelper.clamp(entity.motionX, -maxMotion, maxMotion);
		entity.motionZ = MathHelper.clamp(entity.motionZ, -maxMotion, maxMotion);
	}

	/**
	 * Teleports any entity by duplicating it and . the old one. If {@param entity} is a player,
	 * the entity will be transferred instead of duplicated.
	 *
	 * @return A newsystem entity if {@param entity} wasn't a player, or the same entity if it was a player
	 */
	public Entity teleportEntity(final Entity entity, final WorldServer toWorld, final Teleporter teleporter, final int dimension,
			@Nullable final Supplier<BlockPos> optionalLoc)
	{
		if (entity == null)
		{
			return null;
		}

		if (entity instanceof EntityPlayer)
		{
			if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(entity, dimension))
			{
				return entity;
			}

			// Players require special magic to be teleported correctly, and are not duplicated
			if (!toWorld.isRemote)
			{
				final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
				final PlayerList playerList = server.getPlayerList();

				final EntityPlayerMP player = (EntityPlayerMP) entity;

				playerList.transferPlayerToDimension(player, dimension, teleporter);
				player.timeUntilPortal = player.getPortalCooldown();

				if (optionalLoc == null)
				{
					player.connection.setPlayerLocation(player.posX + 0.5, player.posY, player.posZ + 0.5, 0, 0);
				}
				else
				{
					final BlockPos loc = optionalLoc.get();

					player.connection.setPlayerLocation(loc.getX() + 0.5, loc.getY(), loc.getZ() + 0.5, 225, 0);
				}

				/** Strange flag that needs to be set to prevent the NetHandlerPlayServer instances from resetting your position **/
				player.invulnerableDimensionChange = true;

				final PostAetherTravelEvent event = new PostAetherTravelEvent(entity);
				MinecraftForge.EVENT_BUS.post(event);
			}

			return entity;
		}
		else
		{
			final Entity newEntity = entity.changeDimension(dimension);

			// Forces the entity to be sent to clients as early as possible
			newEntity.forceSpawn = true;
			newEntity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

			return newEntity;
		}
	}

	@Override
	public ContentRegistry content()
	{
		return this.contentRegistry;
	}

	@Override
	public IGildedGamesAccountApi gildedGamesAccountApi()
	{
		return this.webAPI;
	}

	public IThreadListener getMinecraftThread()
	{
		return null;
	}
}
