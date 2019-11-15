package com.guestlogix.traveleruikit.utils;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class FragmentTransactionQueue {
    private List<FragmentTransaction> transactions;
    private boolean isSuspended = false;
    private FragmentManager manager;

    public FragmentTransactionQueue(FragmentManager manager) {
        transactions = new ArrayList<>();
        this.manager = manager;
    }

    public void setSuspended(boolean suspended) {
        this.isSuspended = suspended;

        if (isSuspended) {
            return;
        }

        for (FragmentTransaction transaction : transactions) {
            transaction.commit();
        }

        transactions.clear();
    }

    public void addTransaction(FragmentTransaction transaction) {
        if (isSuspended) {
            transactions.add(transaction);
            return;
        }

        transaction.commit();
    }

    public FragmentTransaction newTransaction() {
        return manager.beginTransaction();
    }

    public void setNewFragmentManager(FragmentManager fragmentManager) {
        manager = fragmentManager;
    }
}
