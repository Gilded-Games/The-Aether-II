package com.gildedgames.aether.common.blocks.containers;

import com.gildedgames.aether.api.AetherAPI;
import com.gildedgames.aether.api.util.BlockPosDimension;
import com.gildedgames.aether.api.world.instances.IInstance;
import com.gildedgames.aether.api.world.instances.IPlayerInstances;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.network.NetworkingAether;
import com.gildedgames.aether.common.network.packets.instances.PacketRegisterDimension;
import com.gildedgames.aether.common.registry.content.DimensionsAether;
import com.gildedgames.aether.common.registry.content.InstancesAether;
import com.gildedgames.aether.common.world.necromancer_tower.NecromancerTowerInstance;
import com.gildedgames.aether.common.world.necromancer_tower.NecromancerTowerInstanceHandler;
import com.gildedgames.aether.common.world.util.TeleporterGeneric;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class BlockAetherTeleporter extends Block
{
	public BlockAetherTeleporter()
	{
		super(Material.ROCK);

		this.setHardness(2.5f);
		this.setSoundType(SoundType.WOOD);
	}

	@Override
	public boolean onBlockActivated(
			final World world, final BlockPos pos, final IBlockState state, final EntityPlayer player, final EnumHand hand, final EnumFacing facing,
			final float hitX, final float hitY, final float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			final NecromancerTowerInstanceHandler handler = InstancesAether.NECROMANCER_TOWER_HANDLER;

			final PlayerAether playerAether = PlayerAether.getPlayer(player);
			final IPlayerInstances hook = AetherAPI.instances().getPlayer(player);

			if (playerAether.getTeleportingModule().getAetherPos() != null)
			{
				if (hook.getInstance() != null)
				{
					final IInstance instance = hook.getInstance();

					instance.onLeave(player);
				}

				final EntityPlayerMP playerMP = (EntityPlayerMP) player;
				final BlockPosDimension p = playerAether.getTeleportingModule().getAetherPos();

				final MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

				final Teleporter teleporter = new TeleporterGeneric(server.getWorld(player.dimension));
				final PlayerList playerList = server.getPlayerList();
				playerList.transferPlayerToDimension(playerMP, p.getDim(), teleporter);
				player.timeUntilPortal = player.getPortalCooldown();

				playerMP.connection.setPlayerLocation(p.getX(), p.getY(), p.getZ(), 0, 0);
			}
			else if (hook.getInstance() != null)
			{
				final IInstance instance = hook.getInstance();

				if (player.dimension == instance.getDimIdInside())
				{
					handler.teleportBack((EntityPlayerMP) player);

					hook.setInstance(null);
				}
				else
				{
					final NecromancerTowerInstance inst = handler.get(new BlockPosDimension(pos, world.provider.getDimension()));

					if (player instanceof EntityPlayerMP)
					{
						NetworkingAether.sendPacketToPlayer(new PacketRegisterDimension(DimensionsAether.NECROMANCER_TOWER, inst.getDimIdInside()),
								(EntityPlayerMP) player);
					}

					handler.teleportToInst((EntityPlayerMP) player, inst);
				}
			}
			else
			{
				final NecromancerTowerInstance inst = handler.get(new BlockPosDimension(pos, world.provider.getDimension()));

				if (player instanceof EntityPlayerMP)
				{
					NetworkingAether.sendPacketToPlayer(new PacketRegisterDimension(DimensionsAether.NECROMANCER_TOWER, inst.getDimIdInside()),
							(EntityPlayerMP) player);
				}

				playerAether.getTeleportingModule()
						.setNonAetherPos(new BlockPosDimension((int) player.posX, (int) player.posY, (int) player.posZ, player.dimension));

				handler.teleportToInst((EntityPlayerMP) player, inst);
			}

			return true;
		}
	}
}
