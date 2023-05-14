package nl.enjarai.omnihopper.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class OneSlotHopperScreenHandler extends ScreenHandler {
	public static final int SLOT_COUNT = 1;
	private final Inventory inventory;

	public OneSlotHopperScreenHandler(int syncId, PlayerInventory playerInventory) {
		this(syncId, playerInventory, new SimpleInventory(SLOT_COUNT));
	}

	public OneSlotHopperScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory) {
		super(ModScreenHandlers.ONE_SLOT_HOPPER, syncId);
		this.inventory = inventory;
		net.minecraft.screen.HopperScreenHandler.checkSize(inventory, SLOT_COUNT);
		inventory.onOpen(playerInventory.player);
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
	public boolean canUse(PlayerEntity player) {
		return inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack quickMove(PlayerEntity player, int slot) {
		ItemStack itemStack = ItemStack.EMPTY;
		Slot slot2 = slots.get(slot);
		if (slot2.hasStack()) {
			ItemStack itemStack2 = slot2.getStack();
			itemStack = itemStack2.copy();
			if (slot < inventory.size() ? !insertItem(itemStack2, inventory.size(), slots.size(), true) : !insertItem(itemStack2, 0, inventory.size(), false)) {
				return ItemStack.EMPTY;
			}
			if (itemStack2.isEmpty()) {
				slot2.setStack(ItemStack.EMPTY);
			} else {
				slot2.markDirty();
			}
		}
		return itemStack;
	}

	@Override
	public void onClosed(PlayerEntity player) {
		super.onClosed(player);
		inventory.onClose(player);
	}
}
