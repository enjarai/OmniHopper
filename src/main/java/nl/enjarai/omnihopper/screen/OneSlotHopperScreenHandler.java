package nl.enjarai.omnihopper.screen;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class OneSlotHopperScreenHandler extends AbstractContainerMenu {
	public static final int SLOT_COUNT = 1;
	private final Container inventory;

	public OneSlotHopperScreenHandler(int syncId, Inventory playerInventory) {
		this(syncId, playerInventory, new SimpleContainer(SLOT_COUNT));
	}

	public OneSlotHopperScreenHandler(int syncId, Inventory playerInventory, Container inventory) {
		super(ModScreenHandlers.ONE_SLOT_HOPPER, syncId);
		this.inventory = inventory;
		net.minecraft.world.inventory.HopperMenu.checkContainerSize(inventory, SLOT_COUNT);
		inventory.startOpen(playerInventory.player);
		for (int i = 0; i < SLOT_COUNT; ++i) {
			addSlot(new Slot(inventory, i, 44 + (i + 2) * 18, 20));
		}
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, i * 18 + 51));
			}
		}
		for (int i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 109));
		}
	}

	@Override
	public boolean stillValid(Player player) {
		return inventory.stillValid(player);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int slot) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot2 = slots.get(slot);
		if (slot2.hasItem()) {
			ItemStack itemStack2 = slot2.getItem();
			itemStack = itemStack2.copy();
			if (slot < inventory.getContainerSize() ? !moveItemStackTo(itemStack2, inventory.getContainerSize(), slots.size(), true) : !moveItemStackTo(itemStack2, 0, inventory.getContainerSize(), false)) {
				return ItemStack.EMPTY;
			}
			if (itemStack2.isEmpty()) {
				slot2.setByPlayer(ItemStack.EMPTY);
			} else {
				slot2.setChanged();
			}
		}
		return itemStack;
	}

	@Override
	public void removed(Player player) {
		super.removed(player);
		inventory.stopOpen(player);
	}
}
