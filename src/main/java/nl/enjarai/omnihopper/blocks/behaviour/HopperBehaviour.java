package nl.enjarai.omnihopper.blocks.behaviour;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import nl.enjarai.omnihopper.blocks.entity.HopperBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public abstract class HopperBehaviour<T> {
	private final Identifier hopperType;
	protected final HopperBlockEntity<?> blockEntity;
	private final BlockApiLookup<Storage<T>, Direction> blockApiLookup;

	protected HopperBehaviour(Identifier hopperType, BlockApiLookup<Storage<T>, Direction> blockApiLookup, HopperBlockEntity<?> blockEntity) {
		this.hopperType = hopperType;
		this.blockEntity = blockEntity;
		this.blockApiLookup = blockApiLookup;
	}

	public Identifier getHopperType() {
		return hopperType;
	}

	public abstract Storage<T> getStorage();

	public final BlockApiLookup<Storage<T>, Direction> getBlockApiLookup() {
		return blockApiLookup;
	}

	public abstract void writeNbt(NbtCompound tag);

	public abstract void readNbt(NbtCompound tag);

	public long getAmountPerActivation(BlockState targetState) {
		return 1;
	}

	public int getCooldown() {
		return 8;
	}

	public boolean pickupInWorldObjects(World world, BlockPos pos, Direction suckyDirection) {
		return false;
	}

	@Nullable
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return null;
	}
}
