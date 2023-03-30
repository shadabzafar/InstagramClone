package com.example.instagramclone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class TabAdapter extends FragmentStateAdapter {

    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int tabPosition) {
        switch (tabPosition){
            case 0:
                return new ProfileTab();

            case 1:
                return new UsersTab();

            case 2:
                return new SharePictureTab();

            default:
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
