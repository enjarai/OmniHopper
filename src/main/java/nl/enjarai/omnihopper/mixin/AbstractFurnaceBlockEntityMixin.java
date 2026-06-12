package nl.enjarai.omnihopper.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import nl.enjarai.omnihopper.OmniHopper;
import nl.enjarai.omnihopper.util.FurnaceFuelBucketStorage;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AbstractFurnaceBlockEntity.class, priority = 1200)
public abstract class AbstractFurnaceBlockEntityMixin extends BaseContainerBlockEntity implements SidedStorageBlockEntity {
    protected AbstractFurnaceBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Shadow public abstract void setItem(int slot, ItemStack stack);

    @Unique
    private final FurnaceFuelBucketStorage fluidStorage = new FurnaceFuelBucketStorage() {
        @Override
        protected ItemStack getFuelStack() {
            return AbstractFurnaceBlockEntityMixin.this.getItem(1);
        }

        @Override
        protected void setFuelStack(ItemStack fuelItem) {
            AbstractFurnaceBlockEntityMixin.this.setItem(1, fuelItem);
        }
    };

    @Override
    public @Nullable Storage<FluidVariant> getFluidStorage(Direction side) {
        if (side == null || side.getAxis().isHorizontal()) {
            return fluidStorage;
        }
        return null;
    }

    @ModifyExpressionValue(
            method = "canTakeItemThroughFace",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;is(Ljava/lang/Object;)Z"
            )
    )
    private boolean removeExtractionExceptions(boolean original) {
        if ((getLevel() instanceof ServerLevel serverWorld) && serverWorld.getGameRules().get(OmniHopper.REMOVE_FURNACE_EXCEPTIONS)) {
            return false;
        }
        return original;
    }
}
