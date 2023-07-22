package nl.enjarai.omnihopper.util;

import net.fabricmc.fabric.api.transfer.v1.storage.base.InsertionOnlyStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public abstract class ExecutingInsertionStorage<T> implements InsertionOnlyStorage<T> {
    final DroppedItems droppedItems = new DroppedItems();

    @Override
    public long insert(T resource, long maxAmount, TransactionContext transaction) {
        var amount = canInsert(resource, maxAmount);
        if (amount <= 0) return 0;

        droppedItems.addDrop(resource, amount, transaction);
        return amount;
    }

    protected long canInsert(T resource, long maxAmount) {
        return maxAmount;
    }

    protected abstract void handleEntry(T resource, long amount);

    private class DroppedItems extends SnapshotParticipant<Integer> {
        final List<Entry> entries = new ArrayList<>();

        void addDrop(T resource, long amount, TransactionContext transaction) {
            updateSnapshots(transaction);
            entries.add(new Entry(resource, amount));
        }

        @Override
        protected Integer createSnapshot() {
            return entries.size();
        }

        @Override
        protected void readSnapshot(Integer snapshot) {
            // effectively cancel processing the entries
            int previousSize = snapshot;

            while (entries.size() > previousSize) {
                entries.remove(entries.size() - 1);
            }
        }

        @Override
        protected void onFinalCommit() {
            // actually process the entries
            for (Entry entry : entries) {
                handleEntry(entry.resource, entry.amount);
            }

            entries.clear();
        }

        private class Entry {
            final T resource;
            final long amount;

            Entry(T resource, long amount) {
                this.resource = resource;
                this.amount = amount;
            }
        }
    }
}
