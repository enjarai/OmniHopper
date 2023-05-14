package nl.enjarai.omnihopper.blocks.entity.hopper;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.block.BlockState;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import nl.enjarai.omnihopper.blocks.ModBlocks;
import nl.enjarai.omnihopper.blocks.entity.hopper.behaviour.VanillaHopperBehaviour;

@SuppressWarnings("UnstableApiUsage")
public class ItemOmniHopperBlockEntity extends OmniHopperBlockEntity<ItemVariant> {
    public ItemOmniHopperBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.OMNIHOPPER_BLOCK_ENTITY, pos, state);
        this.behaviour = new VanillaHopperBehaviour(this);
    }

    @Override
    public Text getName() {
        return Text.translatable("container.omnihopper");
    }
}

