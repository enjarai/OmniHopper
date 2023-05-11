package nl.enjarai.omnihopper.blocks.entity.hopper.behaviour;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.hopper.HopperBlockEntity;
import nl.enjarai.omnihopper.screen.OneSlotHopperScreenHandler;

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
        return 32;
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new OneSlotHopperScreenHandler(syncId, inv, inventory);
    }
}
