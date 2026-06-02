package nl.enjarai.omnihopper.blocks.entity.hopper.behaviour;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.hopper.HopperBlockEntity;
import nl.enjarai.omnihopper.screen.OneSlotHopperScreenHandler;
import org.jetbrains.annotations.Nullable;

public class WoodenHopperBehaviour extends ItemHopperBehaviour {
    public static final Identifier TYPE_ID = OmniHopper.id("wooden_hopper");

    public WoodenHopperBehaviour(HopperBlockEntity<?> blockEntity) {
        super(TYPE_ID, blockEntity);
    }

    @Override
    public int getInventorySize() {
        return 1;
    }

    @Override
    public int getCooldown() {
        return 16;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new OneSlotHopperScreenHandler(syncId, inv, inventory);
    }
}
