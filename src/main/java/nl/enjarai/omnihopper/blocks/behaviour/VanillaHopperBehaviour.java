package nl.enjarai.omnihopper.blocks.behaviour;

import net.minecraft.util.Identifier;
import nl.enjarai.omnihopper.blocks.entity.HopperBlockEntity;

public class VanillaHopperBehaviour extends ItemHopperBehaviour {
    public static final Identifier TYPE_ID = new Identifier("hopper");

    public VanillaHopperBehaviour(HopperBlockEntity<?> blockEntity) {
        super(TYPE_ID, blockEntity);
    }
}
