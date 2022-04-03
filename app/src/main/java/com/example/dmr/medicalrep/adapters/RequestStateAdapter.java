package com.example.dmr.medicalrep.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.dmr.medicalrep.fragments.AcceptedFragment;
import com.example.dmr.medicalrep.fragments.RejectedFragment;
import com.example.dmr.medicalrep.fragments.SentFragment;

public class RequestStateAdapter extends FragmentStateAdapter {
    public RequestStateAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position==0)
            return new SentFragment();
        else if (position==2)
            return new AcceptedFragment();
        else
            return new RejectedFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
