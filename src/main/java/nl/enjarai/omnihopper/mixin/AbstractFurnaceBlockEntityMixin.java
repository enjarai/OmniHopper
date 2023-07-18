package nl.enjarai.omnihopper.mixin;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import nl.enjarai.omnihopper.util.FurnaceFuelBucketStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings("UnstableApiUsage")
@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin implements SidedStorageBlockEntity {
    @Shadow public abstract ItemStack getStack(int slot);
    @Shadow public abstract void setStack(int slot, ItemStack stack);

    @Unique
    private final FurnaceFuelBucketStorage fluidStorage = new FurnaceFuelBucketStorage() {
        @Override
        protected ItemStack getFuelStack() {
            return AbstractFurnaceBlockEntityMixin.this.getStack(1);
        }

        @Override
        protected void setFuelStack(ItemStack fuelItem) {
            AbstractFurnaceBlockEntityMixin.this.setStack(1, fuelItem);
        }
    };

    @Override
    public @Nullable Storage<FluidVariant> getFluidStorage(Direction side) {
        if (side.getAxis().isHorizontal()) {
            return fluidStorage;
        }
        return null;
    }
}
