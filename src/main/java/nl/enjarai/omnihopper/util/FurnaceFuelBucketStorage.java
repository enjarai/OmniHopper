package nl.enjarai.omnihopper.util;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleSlotStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.fabricmc.fabric.mixin.transfer.BucketItemAccessor;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@SuppressWarnings("UnstableApiUsage")
public abstract class FurnaceFuelBucketStorage extends SnapshotParticipant<ItemStack> implements SingleSlotStorage<FluidVariant> {
    protected abstract ItemStack getFuelStack();

    protected abstract void setFuelStack(ItemStack fuelItem);

    @Override
    public long insert(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        ItemStack fuelStack = getFuelStack();
        if (fuelStack.isOf(Items.BUCKET) && fuelStack.getCount() == 1 && maxAmount >= FluidConstants.BUCKET) {
            ItemStack filledBucket = resource.getFluid().getBucketItem().getDefaultStack();
            updateSnapshots(transaction);
            setFuelStack(filledBucket);
            return FluidConstants.BUCKET;
        }

        return 0;
    }

    @Override
    public long extract(FluidVariant resource, long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notBlankNotNegative(resource, maxAmount);

        ItemStack fuelStack = getFuelStack();
        if (getResource().equals(resource) && fuelStack.getCount() == 1 && maxAmount >= FluidConstants.BUCKET) {
            ItemStack emptyBucket = Items.BUCKET.getDefaultStack();
            updateSnapshots(transaction);
            setFuelStack(emptyBucket);
            return FluidConstants.BUCKET;
        }

        return 0;
    }

    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    @Override
    public FluidVariant getResource() {
        ItemStack fuelStack = getFuelStack();
        if (fuelStack.getItem() instanceof BucketItem bucketItem) {
            return FluidVariant.of(((BucketItemAccessor) bucketItem).fabric_getFluid());
        }
        return FluidVariant.blank();
    }

    @Override
    public long getAmount() {
        return isResourceBlank() ? 0 : FluidConstants.BUCKET;
    }

    @Override
    public long getCapacity() {
        return FluidConstants.BUCKET;
    }

    @Override
    protected ItemStack createSnapshot() {
        return getFuelStack();
    }

    @Override
    protected void readSnapshot(ItemStack snapshot) {
        setFuelStack(snapshot);
    }
}
