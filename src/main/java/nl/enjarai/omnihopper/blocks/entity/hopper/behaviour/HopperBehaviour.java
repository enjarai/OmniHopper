package nl.enjarai.omnihopper.blocks.entity.hopper.behaviour;

import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import nl.enjarai.omnihopper.blocks.entity.hopper.HopperBlockEntity;
import org.jetbrains.annotations.Nullable;

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

	public abstract void writeData(ValueOutput view);

	public abstract void readData(ValueInput view);

	public long getAmountPerActivation(BlockState targetState) {
		return 1;
	}

	public int getCooldown() {
		return 8;
	}

	public boolean pickupInWorldObjects(Level world, BlockPos pos, Direction suckyDirection) {
		return false;
	}

	@Nullable
	public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
		return null;
	}

	public InteractionResult onUseWithItem(Player player, InteractionHand hand, BlockHitResult hit) {
		return InteractionResult.TRY_WITH_EMPTY_HAND;
	}
}
