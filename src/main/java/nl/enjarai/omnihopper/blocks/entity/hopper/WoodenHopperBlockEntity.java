package nl.enjarai.omnihopper.blocks.entity.hopper;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.WoodenHopperBehaviour;

public class WoodenHopperBlockEntity extends BasicHopperBlockEntity<ItemVariant> {
    public WoodenHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.WOODEN_HOPPER_BLOCK_ENTITY, pos, state);
        this.behaviour = new WoodenHopperBehaviour(this);
    }

    @Override
    public Component getName() {
        return Component.translatable("container.wooden_hopper");
    }
}
