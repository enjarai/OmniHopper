package nl.enjarai.omnihopper.blocks.behaviour;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import nl.enjarai.omnihopper.blocks.ItemOmniHopperBlock;
import nl.enjarai.omnihopper.blocks.entity.HopperBlockEntity;

@SuppressWarnings("UnstableApiUsage")
public abstract class ItemHopperBehaviour extends HopperBehaviour<ItemVariant> {
	public final SimpleInventory inventory = new SimpleInventory(getInventorySize()) {
		@Override
		public void markDirty() {
			blockEntity.markDirty();
		}
	};
	private final InventoryStorage inventoryWrapper = InventoryStorage.of(inventory, null);

	public ItemHopperBehaviour(Identifier typeId, HopperBlockEntity<?> blockEntity) {
		super(typeId, ItemStorage.SIDED, blockEntity);
	}

	public int getInventorySize() {
		return 5;
	}

	@Override
	public Storage<ItemVariant> getStorage() {
		return inventoryWrapper;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		inventory.stacks = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, inventory.stacks);
	}

	@Override
	public void writeNbt(NbtCompound nbt) {
		Inventories.writeNbt(nbt, inventory.stacks);
	}

	@Override
	public boolean pickupInWorldObjects(World world, BlockPos pos, Direction suckyDirection) {
		Iterator<ItemEntity> entities = getInputItemEntities(world, pos, suckyDirection).iterator();

		ItemEntity itemEntity;
		do {
			if (!entities.hasNext()) {
				return false;
			}

			itemEntity = entities.next();
		} while(!suckItem(itemEntity));

		return true;
	}

	private boolean suckItem(ItemEntity itemEntity) {
		boolean bl = false;
		ItemStack itemStack = itemEntity.getStack();

		try (Transaction transaction = Transaction.openOuter()) {
			long amountInserted = getStorage().insert(ItemVariant.of(itemStack), itemStack.getCount(), transaction);
			itemStack.decrement((int) amountInserted);
			transaction.commit();
		}

		if (itemStack.isEmpty()) {
			bl = true;
			itemEntity.discard();
		}

		return bl;
	}

	private static List<ItemEntity> getInputItemEntities(World world, BlockPos pos, Direction suckyDirection) {
		return getInputAreaShape(suckyDirection).getBoundingBoxes().stream().flatMap((box) ->
				world.getEntitiesByClass(ItemEntity.class, box.offset(pos), EntityPredicates.VALID_ENTITY).stream()
		).collect(Collectors.toList());
	}

	private static VoxelShape getInputAreaShape(Direction suckyDirection) {
		return ItemOmniHopperBlock.SUCKY_AREA[suckyDirection.ordinal()];
	}

	@Nullable
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new HopperScreenHandler(syncId, inv, inventory);
	}
}
