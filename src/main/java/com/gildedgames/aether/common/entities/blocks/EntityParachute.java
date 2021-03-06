package com.gildedgames.aether.common.entities.blocks;

import com.gildedgames.aether.api.registrar.ItemsAether;
import com.gildedgames.aether.common.AetherCore;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.capabilities.entity.player.modules.PlayerParachuteModule;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityParachute extends Entity
{

	private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityParachute.class, DataSerializers.VARINT);

	private EntityPlayer parachutingPlayer;

	private int parachuteTimer;

	public EntityParachute(final World world)
	{
		super(world);

		this.setDead();
	}

	public EntityParachute(final World world, final EntityPlayer player, final Type type)
	{
		super(world);

		this.setType(type);
		this.parachutingPlayer = player;

		this.setPosition(player.posX, player.posY, player.posZ);
	}

	@Override
	public void entityInit()
	{
		this.dataManager.register(EntityParachute.TYPE, 0);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (this.getRidingEntity() == null)
		{
			this.startRiding(this.parachutingPlayer, true);
		}

		if (!this.isDead && (this.getType() == Type.BLUE || this.getType() == Type.PURPLE))
		{
			++this.parachuteTimer;
		}

		if (this.getRidingEntity() instanceof EntityPlayer)
		{
			final EntityPlayer player = (EntityPlayer) this.getRidingEntity();
			final Vec3d vec3 = player.getLookVec();

			final PlayerAether playerAether = PlayerAether.getPlayer(player);

			int blueParachuteAbilityTime = 20;
			int purpleParachuteAbilityTime = 90;

			if (this.getType() == Type.COLD)
			{
				if (!player.isSneaking())
				{
					player.motionY = -0.08;
				}
				else
				{
					player.motionY = -0.24;
				}
			}

			if (this.getType() == Type.BLUE)
			{
				if (this.parachuteTimer <= blueParachuteAbilityTime)
				{
					player.motionY = 1.08;
				}
				else
				{
					replaceParachute(player, playerAether);
				}
			}

			if (this.getType() == Type.PURPLE)
			{
				if (this.parachuteTimer <= purpleParachuteAbilityTime)
				{
					player.motionX = vec3.x * 0.36;
					player.motionY = 0.0;
					player.motionZ = vec3.z * 0.36;
				}
				else if (this.parachuteTimer > (purpleParachuteAbilityTime + 2))
				{
					replaceParachute(player, playerAether);
				}
			}

			player.isAirBorne = true;

			if (this.getType() == Type.BLUE && playerAether.getModule(PlayerParachuteModule.class).isUnderABlock((int) player.posY))
			{
				replaceParachute(player, playerAether);
			}

			if (player.onGround && this.getType() != Type.BLUE)
			{
				playerAether.getModule(PlayerParachuteModule.class).setParachuting(false, this.getType());
			}

			if (!playerAether.getModule(PlayerParachuteModule.class).isParachuting() || playerAether.getModule(PlayerParachuteModule.class).getParachuteEquipped())
			{
				this.destroyParachute(playerAether);
			}

			if (player.inventory.getStackInSlot(playerAether.getModule(PlayerParachuteModule.class).getParachuteItemSlot()).isEmpty())
			{
				playerAether.getModule(PlayerParachuteModule.class).setParachuting(false, this.getType());
				this.destroyParachute(playerAether);
			}
		}
	}

	public void replaceParachute(EntityPlayer player, PlayerAether playerAether)
	{
		playerAether.getModule(PlayerParachuteModule.class).setParachuting(true, Type.COLD);

		this.setType(Type.COLD);
	}

	public void destroyParachute(PlayerAether playerAether)
	{
		this.parachuteTimer = 0;
		this.setDead();

		playerAether.getModule(PlayerParachuteModule.class).parachuteEquipped(false);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canRenderOnFire()
	{
		return false;
	}

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	public Type getType()
	{
		return Type.fromOrdinal(this.dataManager.get(EntityParachute.TYPE));
	}

	public void setType(final Type type)
	{
		this.dataManager.set(EntityParachute.TYPE, type.ordinal());
	}

	public EntityPlayer getParachutingPlayer()
	{
		return this.parachutingPlayer;
	}

	@Override
	public void readEntityFromNBT(final NBTTagCompound tag)
	{
		this.setType(Type.fromOrdinal(tag.getInteger("type")));
	}

	@Override
	public void writeEntityToNBT(final NBTTagCompound tag)
	{
		tag.setInteger("type", this.getType().ordinal());
	}

	@Override
	public boolean attackEntityFrom(final DamageSource source, final float damage)
	{
		return source.getTrueSource() != this.getRidingEntity();
	}

	public enum Type
	{
		COLD("cold"), PURPLE("purple"), BLUE("blue");

		public final String name, desc;

		public final ResourceLocation texture;

		Type(final String name)
		{
			this.name = name;
			this.desc = "cloudParachute.ability." + this.name;

			this.texture = AetherCore.getResource("textures/entities/parachute/parachute_" + this.name + ".png");
		}

		public static Type fromOrdinal(final int ordinal)
		{
			final Type[] type = values();

			return type[ordinal >= type.length || ordinal < 0 ? 0 : ordinal];
		}
	}

}
