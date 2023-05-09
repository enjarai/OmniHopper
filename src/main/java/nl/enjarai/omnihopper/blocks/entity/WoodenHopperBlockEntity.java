package nl.enjarai.omnihopper.blocks.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.blocks.behaviour.WoodenHopperBehaviour;

@SuppressWarnings("UnstableApiUsage")
public class WoodenHopperBlockEntity extends BasicHopperBlockEntity<ItemVariant> {
    public WoodenHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.WOODEN_HOPPER_BLOCK_ENTITY, pos, state);
        this.behaviour = new WoodenHopperBehaviour(this);
    }

    @Override
    public Text getName() {
        return Text.translatable("container.wooden_hopper");
    }
}
