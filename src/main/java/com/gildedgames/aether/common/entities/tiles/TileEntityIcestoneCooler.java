package com.gildedgames.aether.common.entities.tiles;

import com.gildedgames.aether.api.registrar.BlocksAether;
import com.gildedgames.aether.api.registrar.ItemsAether;
import com.gildedgames.aether.common.blocks.containers.BlockIcestoneCooler;
import com.gildedgames.aether.common.containers.tiles.ContainerIcestoneCooler;
import com.gildedgames.aether.common.recipes.CoolerRecipes;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import java.util.Random;

public class TileEntityIcestoneCooler extends TileEntityLockable implements ITickable, ISidedInventory
{
	private static final int[] SLOTS_TOP = new int[] { 0 };

	private static final int[] SLOTS_BOTTOM = new int[] { 2, 4 };

	private static final int[] SLOTS_EAST = new int[] { 3 };

	private static final int[] SLOTS_WEST = new int[] { 1 };

	private final int totalCoolTime = 800;

	private final int itemCoolTime = 1600;

	private NonNullList<ItemStack> coolerItemStacks = NonNullList.withSize(5, ItemStack.EMPTY);

	private int coolerCoolTime;

	private int currentItemCoolTime;

	private int coolTime;

	private String coolerCustomName;

	private final IItemHandler handlerTop = new SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);

	private final IItemHandler handlerBottom = new SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);

	private final IItemHandler handlerEast = new SidedInvWrapper(this, net.minecraft.util.EnumFacing.EAST);

	private final IItemHandler handlerWest = new SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);

	@SideOnly(Side.CLIENT)
	public static boolean isCooling(IInventory inventory)
	{
		return inventory.getField(0) > 0;
	}

	public static boolean isItemCooling(ItemStack stack)
	{
		return stack.getItem() == ItemsAether.icestone;
	}

	public static boolean isItemIrradiated(ItemStack stack)
	{
		Item item = stack.getItem();
		return item == ItemsAether.irradiated_chunk
				|| item == ItemsAether.irradiated_armor
				|| item == ItemsAether.irradiated_charm
				|| item == ItemsAether.irradiated_neckwear
				|| item == ItemsAether.irradiated_ring
				|| item == ItemsAether.irradiated_sword
				|| item == ItemsAether.irradiated_tool;
	}

	@Override
	public int getSizeInventory()
	{
		return this.coolerItemStacks.size();
	}

	public EnumFacing getFacing()
	{
		IBlockState state = this.world.getBlockState(this.pos);

		if (state.getBlock() == BlocksAether.icestone_cooler)
		{
			return state.getValue(BlockIcestoneCooler.PROPERTY_FACING);
		}

		return EnumFacing.NORTH;
	}

	@Override
	public boolean isEmpty()
	{
		for (ItemStack itemstack : this.coolerItemStacks)
		{
			if (!itemstack.isEmpty())
			{
				return false;
			}
		}

		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.coolerItemStacks.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return ItemStackHelper.getAndSplit(this.coolerItemStacks, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return ItemStackHelper.getAndRemove(this.coolerItemStacks, index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		ItemStack itemstack = this.coolerItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.coolerItemStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit())
		{
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag)
		{
			this.coolTime = 0;
			this.markDirty();
		}
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return this.world.getTileEntity(this.pos) == this
				&& player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	public void openInventory(EntityPlayer player)
	{
		this.sendUpdatesToClients();

		if (this.world.isRemote)
		{
			this.sendUpdatesToClients();

			this.setCustomInventoryName(this.coolerCustomName);
		}
	}

	@Override
	public void closeInventory(EntityPlayer player)
	{

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		if (index == 2 || index == 4)
		{
			return false;
		}
		else
		{
			if (index == 0 || index == 3)
			{
				return true;
			}

			if (index == 1)
			{
				return isItemCooling(stack);
			}
		}

		return false;
	}

	@Override
	public int getField(int id)
	{
		switch (id)
		{
			case 0:
				return this.coolerCoolTime;
			case 1:
				return this.currentItemCoolTime;
			case 2:
				return this.coolTime;
			case 3:
				return this.totalCoolTime;
			default:
				return 0;
		}
	}

	@Override
	public void setField(int id, int value)
	{
		switch (id)
		{
			case 0:
				this.coolerCoolTime = value;
				break;
			case 1:
				this.currentItemCoolTime = value;
				break;
			case 2:
				this.coolTime = value;
		}
	}

	@Override
	public int getFieldCount()
	{
		return 4;
	}

	@Override
	public void clear()
	{
		this.coolerItemStacks.clear();
	}

	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public void update()
	{
		boolean flag = this.isCooling();
		boolean flag1 = false;

		this.sendUpdatesToClients();

		this.setCustomInventoryName(this.coolerCustomName);

		if (this.isCooling())
		{
			--this.coolerCoolTime;
		}

		if (!this.world.isRemote)
		{
			ItemStack itemstack = this.coolerItemStacks.get(1);

			if (this.isCooling() || !itemstack.isEmpty() && !this.coolerItemStacks.get(0).isEmpty())
			{
				if (!this.isCooling() && this.canCool())
				{
					this.coolerCoolTime = this.itemCoolTime;
					this.currentItemCoolTime = this.coolerCoolTime;

					if (this.isCooling())
					{
						flag1 = true;

						if (!itemstack.isEmpty())
						{
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty())
							{
								ItemStack item1 = item.getContainerItem(itemstack);
								this.coolerItemStacks.set(1, item1);
							}
						}
					}
				}

				if (this.isCooling() && this.canCool())
				{
					++this.coolTime;

					if (this.coolTime == this.totalCoolTime)
					{
						this.coolTime = 0;
						this.coolItem();
						flag1 = true;
					}
				}
				else
				{
					this.coolTime = 0;
				}
			}
			else if (!this.isCooling() && this.coolTime > 0)
			{
				this.coolTime = MathHelper.clamp(this.coolTime - 2, 0, this.totalCoolTime);
			}

			if (flag != this.isCooling())
			{
				flag1 = true;
			}
		}

		if (flag1)
		{
			this.markDirty();
		}
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
	{
		return new ContainerIcestoneCooler(playerInventory, this);
	}

	@Override
	public String getGuiID()
	{
		return "aether:coooler";
	}

	@Override
	public String getName()
	{
		return this.hasCustomName() ? this.coolerCustomName : "container.icestone_cooler";
	}

	@Override
	public boolean hasCustomName()
	{
		return this.coolerCustomName != null && !this.coolerCustomName.isEmpty();
	}

	public void setCustomInventoryName(String p_145951_1_)
	{
		this.coolerCustomName = p_145951_1_;
	}

	public void sendUpdatesToClients()
	{
		IBlockState state = this.world.getBlockState(this.pos);

		this.world.notifyBlockUpdate(this.pos, state, state, 3);

		this.markDirty();
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag = super.getUpdateTag();

		this.writeToNBT(tag);

		return tag;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound compound = this.getUpdateTag();

		return new SPacketUpdateTileEntity(this.pos, 1, compound);
	}

	@Override
	public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet)
	{
		this.readFromNBT(packet.getNbtCompound());
	}

	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);

		this.coolerItemStacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);

		ItemStackHelper.loadAllItems(compound, this.coolerItemStacks);

		this.coolerCoolTime = compound.getInteger("coolerCoolTime");
		this.coolTime = compound.getInteger("coolTime");

		if (compound.hasKey("CustomName", 8))
		{
			this.coolerCustomName = compound.getString("CustomName");
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		compound.setInteger("coolerCoolTime", (short) this.coolerCoolTime);
		compound.setInteger("coolTime", (short) this.coolTime);
		ItemStackHelper.saveAllItems(compound, this.coolerItemStacks);

		if (this.hasCustomName())
		{
			compound.setString("CustomName", this.coolerCustomName);
		}

		return compound;
	}

	public boolean isCooling()
	{
		return this.coolerCoolTime > 0;
	}

	private boolean canCool()
	{
		ItemStack stackSlot0 = this.coolerItemStacks.get(0);
		ItemStack stackSlot2 = this.coolerItemStacks.get(2);
		ItemStack stackSlot3 = this.coolerItemStacks.get(3);
		ItemStack stackSlot4 = this.coolerItemStacks.get(4);

		ItemStack primaryOutput = CoolerRecipes.instance().getCoolingResult(stackSlot0, stackSlot3);
		ItemStack secondaryOutput = CoolerRecipes.instance().getSecondaryOutput(stackSlot0);

		if (stackSlot0.isEmpty())
		{
			return false;
		}
		else
		{
			if (primaryOutput.isEmpty())
			{
				return false;
			}
			else
			{
				if (!stackSlot4.isEmpty() && stackSlot4.getItem() != secondaryOutput.getItem())
				{
					return false;
				}

				if (stackSlot2.isEmpty())
				{
					return true;
				}

				if (!stackSlot2.isItemEqual(primaryOutput))
				{
					return false;
				}
			}

			int result = stackSlot2.getCount() + primaryOutput.getCount();
			return result <= this.getInventoryStackLimit() && result <= stackSlot2.getMaxStackSize();
		}
	}

	public int getCoolTime(ItemStack stack)
	{
		return this.totalCoolTime;
	}

	public void coolItem()
	{
		if (this.canCool())
		{
			ItemStack stackSlot0 = this.coolerItemStacks.get(0);
			ItemStack stackSlot2 = this.coolerItemStacks.get(2);
			ItemStack stackSlot3 = this.coolerItemStacks.get(3);
			ItemStack stackSlot4 = this.coolerItemStacks.get(4);

			ItemStack secondaryInput = CoolerRecipes.instance().getSecondaryInput(stackSlot0);

			ItemStack primaryOutput = CoolerRecipes.instance().getCoolingResult(stackSlot0, stackSlot3);
			ItemStack secondaryOutput = CoolerRecipes.instance().getSecondaryOutput(stackSlot0);

			if (stackSlot2.isEmpty())
			{
				this.coolerItemStacks.set(2, primaryOutput.copy());
			}
			else if (stackSlot2.getItem() == primaryOutput.getItem())
			{
				stackSlot2.grow(primaryOutput.getCount());
			}

			if (secondaryOutput != null)
			{
				if (stackSlot4.isEmpty())
				{
					Random random = new Random();

					ItemStack copiedStack = secondaryOutput.copy();

					if (copiedStack.getItem() == ItemsAether.irradiated_dust) copiedStack.setCount(1 + random.nextInt(2));

					this.coolerItemStacks.set(4, copiedStack);
				}
				else
				{
					if (stackSlot4.getItem() == secondaryOutput.getItem())
					{
						stackSlot4.grow(1);
					}
				}
			}

			if (secondaryInput != ItemStack.EMPTY)
			{
				if (stackSlot3.getItem() == secondaryInput.getItem())
				{
					stackSlot3.shrink(1);
				}
			}

			stackSlot0.shrink(1);
		}
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return side == EnumFacing.DOWN ? SLOTS_BOTTOM : (side == EnumFacing.UP ? SLOTS_TOP : (side == EnumFacing.EAST || side == EnumFacing.SOUTH ? SLOTS_EAST : SLOTS_WEST));
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing)
	{
		if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
		{
			if (facing == EnumFacing.DOWN)
			{
				return (T) this.handlerBottom;
			}
			else if (facing == EnumFacing.UP)
			{
				return (T) this.handlerTop;
			}
			else if (facing == EnumFacing.EAST || facing == EnumFacing.SOUTH)
			{
				return (T) this.handlerEast;
			}
			else if (facing == EnumFacing.WEST || facing == EnumFacing.NORTH)
			{
				return (T) this.handlerWest;
			}
		}

		return super.getCapability(capability, facing);
	}
}
