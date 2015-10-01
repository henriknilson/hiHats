package hihats.electricity.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hihats.electricity.fragment.DashboardFragment;
import hihats.electricity.fragment.DealsFragment;
import hihats.electricity.fragment.RideFragment;

/**
 * Created by filip on 28/09/15.
 */
public class TabsAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Ride", "Dashboard", "Deals" };
    private Context context;

    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        switch(position) {
            case 0:
                return RideFragment.newInstance();
            case 1:
                return DashboardFragment.newInstance();
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
