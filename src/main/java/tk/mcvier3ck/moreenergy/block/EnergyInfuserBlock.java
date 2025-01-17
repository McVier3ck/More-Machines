package tk.mcvier3ck.moreenergy.block;

import java.util.Random;

import cofh.api.item.IToolHammer;
import cpw.mods.fml.common.network.internal.FMLNetworkHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import tk.mcvier3ck.moreenergy.MoreEnergy;
import tk.mcvier3ck.moreenergy.tileentity.TileEntityRegistry;
import tk.mcvier3ck.moreenergy.tileentity.machine.MachineEnergyInfuser;

public class EnergyInfuserBlock extends BlockContainer {

	public static IIcon frontIcon;
	public static IIcon frontIcon_active;

	public EnergyInfuserBlock(Material material) {

		super(material);
		setHardness(5.0F);
		setResistance(10.0F);
		setStepSound(soundTypeMetal);

		setHarvestLevel("pickaxe", 2);
		setHarvestLevel("pickaxe", 1, 0);
		setHarvestLevel("pickaxe", 1, 1);
		setHarvestLevel("pickaxe", 3, 6);
		setHarvestLevel("pickaxe", 3, 12);

	}

	@Override
	public Item getItemDropped(int i, Random random, int j) {

		return Item.getItemFromBlock(TileEntityRegistry.EnergyInfuser);
	}

	@Override
	public void registerBlockIcons(IIconRegister reg) {

		this.blockIcon = reg.registerIcon("MoreEnergy:MachineBlock_Side");
		this.frontIcon = reg.registerIcon("MoreEnergy:EnergyInfuser");
		this.frontIcon_active = reg.registerIcon("MoreEnergy:EnergyInfuser_Work");

	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta == 1)
			return frontIcon_active;
		if (side == 2 && meta == 6)
			return frontIcon_active;
		if (side == 3 && meta == 7)
			return frontIcon_active;
		if (side == 4 && meta == 8)
			return frontIcon_active;
		if (side == 5 && meta == 9)
			return frontIcon_active;

		return side == meta ? frontIcon : blockIcon;
	}

	public static void updateWork(boolean active, World world, int x, int y, int z) {

		int meta = world.getBlockMetadata(x, y, z);

		if (!active) {
			if (meta == 6) {
				world.setBlockMetadataWithNotify(x, y, z, 2, 2);
			} else if (meta == 7) {
				world.setBlockMetadataWithNotify(x, y, z, 3, 2);
			} else if (meta == 8) {
				world.setBlockMetadataWithNotify(x, y, z, 4, 2);
			} else if (meta == 9) {
				world.setBlockMetadataWithNotify(x, y, z, 5, 2);
			}
		} else {
			if (meta == 2) {
				world.setBlockMetadataWithNotify(x, y, z, 6, 2);
			} else if (meta == 3) {
				world.setBlockMetadataWithNotify(x, y, z, 7, 2);
			} else if (meta == 4) {
				world.setBlockMetadataWithNotify(x, y, z, 8, 2);
			} else if (meta == 5) {
				world.setBlockMetadataWithNotify(x, y, z, 9, 2);
			}
		}

	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		this.setDirection(world, x, y, z);
	}

	private void setDirection(World world, int x, int y, int z) {
		if (!world.isRemote) {
			Block block = world.getBlock(x, y, z - 1);
			Block block1 = world.getBlock(x, y, z + 1);
			Block block2 = world.getBlock(x - 1, y, z);
			Block block3 = world.getBlock(x + 1, y, z);
			byte b0 = 3;

			if (block.func_149730_j() && !block1.func_149730_j()) {
				b0 = 3;
			}

			if (block1.func_149730_j() && !block.func_149730_j()) {
				b0 = 2;
			}

			if (block2.func_149730_j() && !block3.func_149730_j()) {
				b0 = 5;
			}

			if (block3.func_149730_j() && !block2.func_149730_j()) {
				b0 = 4;
			}

			world.setBlockMetadataWithNotify(x, y, z, b0, 2);
		}
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitx,
			float gity, float hitz) {
		TileEntity entity = world.getTileEntity(x, y, z);
		if (world.isRemote) {
			return true;
		} else if (entity != null && !player.isSneaking()) {
			if (player.inventory.getCurrentItem() == null
					|| !(player.inventory.getCurrentItem().getItem() instanceof IToolHammer)) {
				FMLNetworkHandler.openGui(player, MoreEnergy.instance, MoreEnergy.EnergyInfuserID, world, x, y, z);
				return true;
			}

		}

		if (player.inventory.getCurrentItem() != null) {
			if (player.inventory.getCurrentItem().getItem() instanceof IToolHammer) {
				if (!player.isSneaking()) {
					int meta = world.getBlockMetadata(x, y, z);
					if (meta == 2)
						world.setBlockMetadataWithNotify(x, y, z, 5, 2);
					if (meta == 3)
						world.setBlockMetadataWithNotify(x, y, z, 4, 2);
					if (meta == 4)
						world.setBlockMetadataWithNotify(x, y, z, 2, 2);
					if (meta == 5)
						world.setBlockMetadataWithNotify(x, y, z, 3, 2);

					if (meta == 6)
						world.setBlockMetadataWithNotify(x, y, z, 9, 2);
					if (meta == 7)
						world.setBlockMetadataWithNotify(x, y, z, 8, 2);
					if (meta == 8)
						world.setBlockMetadataWithNotify(x, y, z, 6, 2);
					if (meta == 9)
						world.setBlockMetadataWithNotify(x, y, z, 7, 2);
				} else {
					MachineEnergyInfuser energyinfuser = (MachineEnergyInfuser) world.getTileEntity(x, y, z);

					for (int i = 0; i < energyinfuser.getSizeInventory(); ++i) {
						ItemStack itemstack = energyinfuser.getStackInSlot(i);
						if (itemstack != null) {
							dropBlockAsItem(world, x, y, z, itemstack);
						}
					}
					ItemStack infuser = new ItemStack(TileEntityRegistry.EnergyInfuser);

					infuser.stackTagCompound = new NBTTagCompound();
					infuser.stackTagCompound.setInteger("Energy", energyinfuser.getEnergyStored(ForgeDirection.SOUTH));

					dropBlockAsItem(world, x, y, z, infuser);
					world.setBlock(x, y, z, Blocks.air);
				}
			}

		}

		return true;

	}

	@Override
	public TileEntity createNewTileEntity(World world, int side) {

		return new MachineEnergyInfuser();
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack Itemstack) {
		MachineEnergyInfuser energyinfuser = (MachineEnergyInfuser) world.getTileEntity(x, y, z);

		if (Itemstack.hasTagCompound()) {
			energyinfuser.powerHandler.setEnergyStored(Itemstack.stackTagCompound.getInteger("Energy"));
		}

		int l = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

		if (l == 0) {
			world.setBlockMetadataWithNotify(x, y, z, 2, 2);
		}

		if (l == 1) {
			world.setBlockMetadataWithNotify(x, y, z, 5, 2);
		}

		if (l == 2) {
			world.setBlockMetadataWithNotify(x, y, z, 3, 2);
		}

		if (l == 3) {
			world.setBlockMetadataWithNotify(x, y, z, 4, 2);
		}

		
	}

	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {

		MachineEnergyInfuser energyinfuser = (MachineEnergyInfuser) world.getTileEntity(x, y, z);

		if (energyinfuser != null) {
			for (int i1 = 0; i1 < energyinfuser.getSizeInventory(); ++i1) {
				ItemStack itemstack = energyinfuser.getStackInSlot(i1);

				if (itemstack != null) {
					Random rand = new Random();

					float f = rand.nextFloat() * 0.8F + 0.1F;
					float f1 = rand.nextFloat() * 0.8F + 0.1F;
					float f2 = rand.nextFloat() * 0.8F + 0.1F;

					while (itemstack.stackSize > 0) {
						int j1 = rand.nextInt(21) + 10;

						if (j1 > itemstack.stackSize) {
							j1 = itemstack.stackSize;
						}

						itemstack.stackSize -= j1;
						EntityItem entityitem = new EntityItem(world, (double) ((float) x + f),
								(double) ((float) y + f1), (double) ((float) z + f2),
								new ItemStack(itemstack.getItem(), j1, itemstack.getItemDamage()));

						if (itemstack.hasTagCompound()) {
							entityitem.getEntityItem()
									.setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
						}

						float f3 = 0.05F;
						entityitem.motionX = (double) ((float) rand.nextGaussian() * f3);
						entityitem.motionY = (double) ((float) rand.nextGaussian() * f3 + 0.2F);
						entityitem.motionZ = (double) ((float) rand.nextGaussian() * f3);
						world.spawnEntityInWorld(entityitem);
					}
				}
			}

			world.func_147453_f(x, y, z, block);
		}
		super.breakBlock(world, x, y, z, block, meta);

	}

}
