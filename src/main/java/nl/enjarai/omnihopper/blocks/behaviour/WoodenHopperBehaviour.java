package nl.enjarai.omnihopper.blocks.behaviour;

import net.minecraft.util.Identifier;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.blocks.entity.HopperBlockEntity;

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
}
