package com.gildedgames.aether.common.items.weapons.crossbow;

import com.gildedgames.aether.api.entity.damage.DamageTypeAttributes;
import com.gildedgames.aether.api.player.inventory.IInventoryEquipment;
import com.gildedgames.aether.common.capabilities.DamageSystem;
import com.gildedgames.aether.common.capabilities.entity.player.PlayerAether;
import com.gildedgames.aether.common.capabilities.entity.player.modules.PlayerEquipmentModule;
import com.gildedgames.aether.common.entities.projectiles.EntityBolt;
import com.gildedgames.aether.common.init.CreativeTabsAether;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCrossbow extends Item
{
	private float durationInTicks;

	private float knockBackValue;

	private crossBowTypes cBType;

	private boolean isSpecialLoaded;

	private boolean finishedLoading = false;

	public ItemCrossbow()
	{
		this.maxStackSize = 1;

		this.knockBackValue = 0;
		this.durationInTicks = 20.0F;

		this.isSpecialLoaded = false;

		this.setCreativeTab(CreativeTabsAether.TAB_WEAPONS);

		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
		{
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn)
			{
				//"pull": The amount that the crossbow has been pulled.

				if (entityIn != null)
				{
					final ItemStack activeItemStack = entityIn.getActiveItemStack();
					final ItemStack heldItemStack = entityIn.getHeldItemMainhand();
					float duration;

					if (ItemCrossbow.this.isSpecialLoaded)
					{
						duration = getDurationInTicks() * 2;
					}
					else
					{
						duration = getDurationInTicks();
					}

					if (heldItemStack == stack && heldItemStack.getItem() == ItemCrossbow.this)
					{
						if (entityIn.isHandActive() && activeItemStack == stack && activeItemStack.getItem() == ItemCrossbow.this)
						{
							return ((float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / duration);
						}
						else if (((ItemCrossbow) heldItemStack.getItem()).finishedLoading)
						{
							return 1.0F;
						}
					}
				}

				return 0.0F;
			}
		});

		this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter()
		{
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn)
			{
				//"pulling": Whether the crossbow is being pulled or not. Basically a boolean in 1.0F and 0.0F form.

				if (entityIn != null)
				{
					final ItemStack activeItemStack = entityIn.getActiveItemStack();
					final ItemStack heldItemStack = entityIn.getHeldItemMainhand();

					if (heldItemStack == stack && heldItemStack.getItem() == ItemCrossbow.this)
					{
						if (entityIn.isHandActive() && activeItemStack == stack && activeItemStack.getItem() == ItemCrossbow.this)
						{
							return 1.0F;
						}
					}
				}

				return 0.0F;
			}
		});

		this.addPropertyOverride(new ResourceLocation("ready"), new IItemPropertyGetter()
		{
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn)
			{
				return entityIn != null && ItemCrossbow.this.finishedLoading ? 1.0F : 0.0F;
			}
		});

		this.addPropertyOverride(new ResourceLocation("charged"), new IItemPropertyGetter()
		{
			@Override
			@SideOnly(Side.CLIENT)
			public float apply(final ItemStack stack, @Nullable final World worldIn, @Nullable final EntityLivingBase entityIn)
			{
				return entityIn != null && ItemCrossbow.isLoaded(stack) ? 1.0F : 0.0F;
			}
		});
	}

	private static void checkTag(final ItemStack stack)
	{
		if (stack.getTagCompound() == null)
		{
			stack.setTagCompound(new NBTTagCompound());
		}
	}

	public static boolean isLoaded(final ItemStack stack)
	{
		if (stack == null)
		{
			return false;
		}

		checkTag(stack);

		return stack.getTagCompound().getBoolean("loaded");
	}

	public static void setLoaded(final ItemStack stack, final boolean loaded)
	{
		if (stack == null)
		{
			return;
		}

		checkTag(stack);

		stack.getTagCompound().setBoolean("loaded", loaded);
	}

	private ItemStack findAmmo(EntityPlayer player)
	{
		if (this.isBolt(player.getHeldItem(EnumHand.OFF_HAND)))
		{
			return player.getHeldItem(EnumHand.OFF_HAND);
		}
		else if (this.isBolt(player.getHeldItem(EnumHand.MAIN_HAND)))
		{
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
			{
				ItemStack itemStack = player.inventory.getStackInSlot(i);

				if (this.isBolt(itemStack))
				{
					return itemStack;
				}
			}

			return ItemStack.EMPTY;
		}
	}

	private boolean hasAmmo(final EntityPlayer player)
	{
		final ItemStack boltStack = this.findAmmo(player);

		boolean hasBolts = boltStack.getCount() >= 1;

		if (this.cBType == crossBowTypes.SKYROOT && player.isSneaking())
		{
			hasBolts = boltStack.getCount() >= 2;
		}
		else if (this.cBType == crossBowTypes.HOLYSTONE && player.isSneaking())
		{
			hasBolts = boltStack.getCount() >= 3;
		}

		return this.isBolt(boltStack) && hasBolts;
	}

	private boolean isBolt(ItemStack stack)
	{
		return stack.getItem() instanceof ItemBolt;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand hand)
	{
		final ItemStack stack = playerIn.getHeldItem(hand);

		if (!ItemCrossbow.isLoaded(stack))
		{
			if (this.hasAmmo(playerIn))
			{
				playerIn.setActiveHand(EnumHand.MAIN_HAND);
				return new ActionResult<>(EnumActionResult.SUCCESS, stack);
			}
		}
		else
		{
			if (playerIn.world.isRemote)
			{
				final ItemRenderer renderer = Minecraft.getMinecraft().getItemRenderer();
				renderer.equippedProgressMainHand = 1.5F;
			}

			this.shootBolt(playerIn, stack);

			if (!playerIn.world.isRemote)
			{
				ItemCrossbow.setLoaded(stack, false);
			}

			playerIn.activeItemStackUseCount = 0;

			return new ActionResult<>(EnumActionResult.SUCCESS, stack);
		}

		return new ActionResult<>(EnumActionResult.PASS, stack);
	}

	private void shootBolt(final EntityLivingBase entityLiving, final ItemStack stack)
	{
		if (!entityLiving.world.isRemote)
		{
			final World world = entityLiving.world;

			world.playSound(null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F,
					1.0F / (itemRand.nextFloat() * 0.4F + 1.2F));

			final float speed = 1.0f;

			EntityBolt bolt0 = null,
					bolt1 = null,
					bolt2 = null;

			float slashDamageLevel = this.cBType.slashDamageLevel;
			float pierceDamageLevel = this.cBType.pierceDamageLevel;
			float impactDamageLevel = this.cBType.impactDamageLevel;
			float vanillaDamage = this.cBType.damage;

			IInventoryEquipment inventory = null;

			if (entityLiving instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer) entityLiving;

				inventory = PlayerAether.getPlayer(player).getModule(PlayerEquipmentModule.class).getInventory();
			}

			if (slashDamageLevel > 0)
			{
				slashDamageLevel += DamageSystem.getBonusDamageFromAccessories("slash", inventory);
			}
			if (pierceDamageLevel > 0)
			{
				pierceDamageLevel += DamageSystem.getBonusDamageFromAccessories("pierce", inventory);
			}
			if (impactDamageLevel > 0)
			{
				impactDamageLevel += DamageSystem.getBonusDamageFromAccessories("impact", inventory);
			}

			// calculate bolts that are special is being loaded.
			if (this.isSpecialLoaded && this.cBType != crossBowTypes.ZANITE && this.cBType != crossBowTypes.GRAVETITE)
			{
				if (this.cBType == crossBowTypes.SKYROOT)
				{
					bolt0 = this.createBolt(entityLiving, speed, 0, 0, 0, slashDamageLevel, pierceDamageLevel, impactDamageLevel, vanillaDamage);
					bolt1 = this.createBolt(entityLiving, speed, 0, 0, 0, slashDamageLevel, pierceDamageLevel, impactDamageLevel, vanillaDamage);
				}
				else if (this.cBType == crossBowTypes.HOLYSTONE)
				{
					bolt0 = this.createBolt(entityLiving, speed / 2, 0, 0, 0, slashDamageLevel, pierceDamageLevel, impactDamageLevel, vanillaDamage);
					bolt1 = this.createBolt(entityLiving, speed / 2, 10, 0, 0, slashDamageLevel, pierceDamageLevel, impactDamageLevel, vanillaDamage);
					bolt2 = this.createBolt(entityLiving, speed / 2, -10, 0, 0, slashDamageLevel, pierceDamageLevel, impactDamageLevel, vanillaDamage);
				}
				else if (this.cBType == crossBowTypes.ARKENIUM)
				{
					bolt0 = this.createBolt(entityLiving, speed * 2.5f, 0, -1, 0, slashDamageLevel, pierceDamageLevel, impactDamageLevel, vanillaDamage);
				}
			}
			// standard bolts
			else
			{
				if (this.cBType == crossBowTypes.ZANITE)
				{
					float bonusPierce = 0;

					if (entityLiving.getHeldItemMainhand() == stack)
					{
						vanillaDamage = 6f + ((float) (stack.getItemDamage() * 4) / stack.getItem().getMaxDamage());

						bonusPierce = ((float) (stack.getItemDamage() * 4) / stack.getItem().getMaxDamage());
					}

					bolt0 = this.createBolt(entityLiving, speed, 0, 0, 0, slashDamageLevel, pierceDamageLevel + bonusPierce, impactDamageLevel,
							vanillaDamage);
				}
				else if (this.cBType == crossBowTypes.GRAVETITE)
				{
					bolt0 = this.createBolt(entityLiving, speed, 0, 0, 0, slashDamageLevel, pierceDamageLevel, impactDamageLevel, vanillaDamage);

					if (bolt0 != null)
					{
						bolt0.setNoGravity(true);
					}
				}
				else
				{
					bolt0 = this.createBolt(entityLiving, speed, 0, 0, 0, slashDamageLevel, pierceDamageLevel, impactDamageLevel, vanillaDamage);
				}
			}

			if (entityLiving instanceof EntityPlayer)
			{
				final EntityPlayer player = (EntityPlayer) entityLiving;

				if (player.capabilities.isCreativeMode)
				{
					if (bolt0 != null)
					{
						bolt0.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
					}
					if (bolt1 != null)
					{
						bolt1.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
					}
					if (bolt2 != null)
					{
						bolt2.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
					}
				}
				else
				{
					if (this.getDamage(stack) >= this.getMaxDamage(stack))
					{
						player.inventory.deleteStack(stack);
						world.playSound(null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.ENTITY_ITEM_BREAK, SoundCategory.PLAYERS,
								1.0F,
								1.0F / (itemRand.nextFloat() * 0.4F + 1.2F));
					}

					// damage crossbow
					if (this.isSpecialLoaded)
					{
						this.setDamage(stack, this.getDamage(stack) + 2);
					}
					else
					{
						this.setDamage(stack, this.getDamage(stack) + 1);
					}
				}
			}

			if (bolt0 != null)
			{
				entityLiving.getEntityWorld().spawnEntity(bolt0);
			}

			if (bolt1 != null)
			{
				entityLiving.getEntityWorld().spawnEntity(bolt1);
			}

			if (bolt2 != null)
			{
				entityLiving.getEntityWorld().spawnEntity(bolt2);
			}

			this.isSpecialLoaded = false;
			this.finishedLoading = false;
		}
	}

	private EntityBolt createBolt(EntityLivingBase entityLiving, float speed, float addRotationYaw, float addRotationPitch, float addInaccuracy,
			float slashDamageLevel, float pierceDamageLevel, float impactDamageLevel, float damage)
	{
		if (!entityLiving.world.isRemote)
		{
			final EntityBolt bolt = new EntityBolt(entityLiving.getEntityWorld(), entityLiving);

			bolt.shoot(entityLiving, entityLiving.rotationPitch + addRotationPitch, entityLiving.rotationYaw + addRotationYaw,
					0.0F, speed * 2.0F, 1.0F + addInaccuracy);

			bolt.setSlashDamageLevel(slashDamageLevel);
			bolt.setPierceDamageLevel(pierceDamageLevel);
			bolt.setImpactDamageLevel(impactDamageLevel);
			bolt.setDamage(damage / 2);
			bolt.setIsCritical(false);

			bolt.pickupStatus = EntityArrow.PickupStatus.ALLOWED;

			return bolt;
		}

		return null;
	}

	@Override
	public void onUsingTick(final ItemStack stack, final EntityLivingBase living, final int count)
	{
		if (living instanceof EntityPlayer)
		{
			final EntityPlayer player = (EntityPlayer) living;

			if (this.hasAmmo(player))
			{
				if (!living.getEntityWorld().isRemote)
				{
					final float use = (float) (this.getMaxItemUseDuration(stack) - living.getItemInUseCount()) / 20.0F;
					float duration;

					if (use == 0)
					{
						if (this.cBType != crossBowTypes.ZANITE && this.cBType != crossBowTypes.GRAVETITE)
						{
							this.isSpecialLoaded = player.isSneaking();
						}

						this.finishedLoading = false;
					}

					if (this.isSpecialLoaded)
					{
						duration = getDurationInTicks() * 2;
					}
					else
					{
						duration = getDurationInTicks();
					}

					if (use >= (duration / 20.0F))
					{
						this.finishedLoading = true;
					}
				}
			}
		}
	}

	@Override
	public void onPlayerStoppedUsing(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving, final int timeLeft)
	{
		if (entityLiving instanceof EntityPlayer)
		{
			final EntityPlayer player = (EntityPlayer) entityLiving;
			final ItemStack boltStack = findAmmo(player);

			this.finishedLoading = false;

			if (this.hasAmmo(player))
			{
				if (!entityLiving.getEntityWorld().isRemote)
				{
					final float use = (float) (this.getMaxItemUseDuration(stack) - entityLiving.getItemInUseCount()) / 20.0F;
					float duration;

					if (!player.isSneaking())
					{
						this.isSpecialLoaded = false;
					}

					if (this.isSpecialLoaded)
					{
						duration = getDurationInTicks() * 2;
					}
					else
					{
						duration = getDurationInTicks();
					}

					if (use >= (duration / 20.0F))
					{
						ItemCrossbow.setLoaded(stack, true);

						final World world = entityLiving.world;

						world.playSound(null, entityLiving.posX, entityLiving.posY, entityLiving.posZ, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF, SoundCategory.NEUTRAL, 1.0F,
								1.0F / (itemRand.nextFloat() * 0.4F + 1.2F));

						if (!player.capabilities.isCreativeMode)
						{
							int shrinkQuantity = 1;
							if (this.isSpecialLoaded)
							{
								if (this.cBType == crossBowTypes.HOLYSTONE)
								{
									shrinkQuantity = 3;
								}
								if (this.cBType == crossBowTypes.SKYROOT)
								{
									shrinkQuantity = 2;
								}
							}
							boltStack.shrink(shrinkQuantity);

							if (boltStack.getCount() == 0)
							{
								player.inventory.deleteStack(boltStack);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack)
	{
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(final ItemStack stack)
	{
		return EnumAction.BOW;
	}

	@Override
	public boolean getShareTag()
	{
		return true;
	}

	public float getKnockBackValue()
	{
		return this.knockBackValue;
	}

	public ItemCrossbow setKnockBackValue(final float x)
	{
		this.knockBackValue = x;

		return this;
	}

	public float getDurationInTicks()
	{
		return this.durationInTicks;
	}

	public ItemCrossbow setDurationInTicks(final int x)
	{
		this.durationInTicks = x;

		return this;
	}

	public boolean getIsSpecialLoaded()
	{
		return this.isSpecialLoaded;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final World world, final List<String> tooltip, final ITooltipFlag flag)
	{
		final float seconds = this.durationInTicks / 20.0F;

		tooltip.add(String.format("%s: %s",
				TextFormatting.BLUE + I18n.format("item.aether.crossbow.desc4"),
				TextFormatting.WHITE + I18n.format("item.aether." + this.cBType.name + ".ability")));

		if (this.cBType != crossBowTypes.ZANITE && this.cBType != crossBowTypes.GRAVETITE)
		{
			tooltip.add(String.format("%s: %s",
					TextFormatting.DARK_AQUA + I18n.format("item.aether.tooltip.use"),
					TextFormatting.WHITE + I18n.format("item.aether.crossbow.use.desc")));
		}

		tooltip.add("");

		tooltip.add(TextFormatting.GRAY + I18n.format("item.modifiers.mainhand"));

		if (seconds == Math.floor(seconds))
		{
			tooltip.add(TextFormatting.GRAY + " " + (int) Math.floor(seconds) + " " + I18n.format(
					"item.aether.crossbow.desc3") + I18n.format("item.aether.crossbow.desc1"));
		}
		else
		{
			tooltip.add(TextFormatting.GRAY + " " + seconds + " " + I18n.format(
					"item.aether.crossbow.desc3") + I18n.format("item.aether.crossbow.desc1"));
		}

		if (this.cBType.slashDamageLevel > 0)
		{
			String slashValue;

			if (this.cBType.slashDamageLevel % 1 == 0)
			{
				int n = Math.round(this.cBType.slashDamageLevel);
				slashValue = String.valueOf(n);
			}
			else
			{
				float n = this.cBType.slashDamageLevel;
				slashValue = String.valueOf(n);
			}

			tooltip.add(String.format(" %s %s",
					slashValue,
					String.format("%s %s",
							TextFormatting.BLUE + I18n.format("attribute.name.aether.slash"),
							TextFormatting.GRAY + I18n.format("attribute.name.aether.damageLevel"))));
		}

		if (this.cBType.pierceDamageLevel > 0)
		{
			String pierceValue;

			if (this.cBType.pierceDamageLevel % 1 == 0)
			{
				int n = Math.round(this.cBType.pierceDamageLevel);
				pierceValue = String.valueOf(n);
			}
			else
			{
				float n = this.cBType.pierceDamageLevel;
				pierceValue = String.valueOf(n);
			}

			tooltip.add(String.format(" %s %s",
					pierceValue,
					String.format("%s %s",
							TextFormatting.RED + I18n.format("attribute.name.aether.pierce"),
							TextFormatting.GRAY + I18n.format("attribute.name.aether.damageLevel"))));
		}

		if (this.cBType.impactDamageLevel > 0)
		{
			String impactValue;

			if (this.cBType.impactDamageLevel % 1 == 0)
			{
				int n = Math.round(this.cBType.impactDamageLevel);
				impactValue = String.valueOf(n);
			}
			else
			{
				float n = this.cBType.impactDamageLevel;
				impactValue = String.valueOf(n);
			}

			tooltip.add(String.format(" %s %s",
					impactValue,
					String.format("%s %s",
							TextFormatting.YELLOW + I18n.format("attribute.name.aether.impact"),
							TextFormatting.GRAY + I18n.format("attribute.name.aether.damageLevel"))));
		}
	}

	@Override
	public int getItemBurnTime(ItemStack itemStack)
	{
		if (this.cBType == crossBowTypes.SKYROOT)
		{
			return 300;
		}
		else
		{
			return 0;
		}
	}

	public crossBowTypes getType()
	{
		return this.cBType;
	}

	public ItemCrossbow setType(crossBowTypes type)
	{
		this.cBType = type;
		this.setMaxDamage(this.cBType.maxDurability);
		return this;
	}

	public enum crossBowTypes
	{
		SKYROOT(0, 4, 0, 4, 82, "skyroot_crossbow"),
		HOLYSTONE(0, 5, 0, 5, 181, "holystone_crossbow"),
		ZANITE(0, 6, 0, 6, 346, "zanite_crossbow"),
		ARKENIUM(0, 8, 0, 8, 4418, "arkenium_crossbow"),
		GRAVETITE(0, 7, 0, 7, 2160, "gravitite_crossbow");

		final float slashDamageLevel, pierceDamageLevel, impactDamageLevel, damage;

		final int maxDurability;

		final String name;

		crossBowTypes(float slashDamageLevel, float pierceDamageLevel, float impactDamageLevel, float damage, int maxDurability, String name)
		{
			this.slashDamageLevel = slashDamageLevel;
			this.pierceDamageLevel = pierceDamageLevel;
			this.impactDamageLevel = impactDamageLevel;
			this.damage = damage;
			this.maxDurability = maxDurability;
			this.name = name;
		}
	}
}
