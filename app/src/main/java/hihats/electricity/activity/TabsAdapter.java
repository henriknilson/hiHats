package hihats.electricity.activity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hihats.electricity.fragment.ProfileFragment;
import hihats.electricity.fragment.DealsFragment;
import hihats.electricity.fragment.RideFragment;

/**
 * This is the tab adapter that keeps the status and order of the
 * three fragment tabs that make up the entire main application.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private final String[] tabTitles = new String[] { "Ride", "Profile", "Deals" };

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return RideFragment.newInstance();
            case 1:
                return ProfileFragment.newInstance();
            case 2:
                return DealsFragment.newInstance();
            default:
                throw new NullPointerException();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
