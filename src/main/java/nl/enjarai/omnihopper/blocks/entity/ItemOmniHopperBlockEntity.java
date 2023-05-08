package nl.enjarai.omnihopper.blocks.entity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.blocks.behaviour.ItemHopperBehaviour;

@SuppressWarnings("UnstableApiUsage")
public class ItemOmniHopperBlockEntity extends OmniHopperBlockEntity<ItemVariant> {
    public ItemOmniHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.OMNIHOPPER_BLOCK_ENTITY, pos, state);
        this.behaviour = new ItemHopperBehaviour(this);
    }

    @Override
    public Text getName() {
        return Text.translatable("container.omnihopper");
    }
}

