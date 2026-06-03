package nl.enjarai.omnihopper.blocks.entity.hopper.behaviour;

import net.fabricmc.fabric.api.transfer.v1.item.ContainerStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HopperMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.shapes.VoxelShape;
import nl.enjarai.omnihopper.blocks.entity.hopper.HopperBlockEntity;
import nl.enjarai.omnihopper.blocks.hopper.ItemOmniHopperBlock;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ItemHopperBehaviour extends HopperBehaviour<ItemVariant> {
	public final SimpleContainer inventory = new SimpleContainer(getInventorySize()) {
		@Override
		public void setChanged() {
			blockEntity.setChanged();
		}
	};
	private final ContainerStorage inventoryWrapper = ContainerStorage.of(inventory, null);

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
	public void readData(ValueInput view) {
		inventory.items = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(view, inventory.items);
	}

	@Override
	public void writeData(ValueOutput view) {
		ContainerHelper.saveAllItems(view, inventory.items);
	}

	@Override
	public boolean pickupInWorldObjects(Level world, BlockPos pos, Direction suckyDirection) {
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
		ItemStack itemStack = itemEntity.getItem();

		try (Transaction transaction = Transaction.openOuter()) {
			long amountInserted = getStorage().insert(ItemVariant.of(itemStack), itemStack.getCount(), transaction);
			itemStack.shrink((int) amountInserted);
			transaction.commit();
		}

		if (itemStack.isEmpty()) {
			bl = true;
			itemEntity.discard();
		}

		return bl;
	}

	private static List<ItemEntity> getInputItemEntities(Level world, BlockPos pos, Direction suckyDirection) {
		return getInputAreaShape(suckyDirection).toAabbs().stream().flatMap((box) ->
				world.getEntitiesOfClass(ItemEntity.class, box.move(pos), EntitySelector.ENTITY_STILL_ALIVE).stream()
		).collect(Collectors.toList());
	}

	private static VoxelShape getInputAreaShape(Direction suckyDirection) {
		return ItemOmniHopperBlock.SUCKY_AREA[suckyDirection.ordinal()];
	}

	@Nullable
	@Override
	public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
		return new HopperMenu(syncId, inv, inventory);
	}
}
