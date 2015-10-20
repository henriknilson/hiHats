package hihats.electricity.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hihats.electricity.fragment.ProfileFragment;
import hihats.electricity.fragment.DealsFragment;
import hihats.electricity.fragment.RideFragment;

/**
 * Created by filip on 28/09/15.
 */
public class TabsAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Ride", "Profile", "Deals" };
    private Context context;

    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
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
