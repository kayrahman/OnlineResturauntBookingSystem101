package mahsa.com.onlineresturauntbookingsystem.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import mahsa.com.onlineresturauntbookingsystem.R;
import mahsa.com.onlineresturauntbookingsystem.model.Booking;

import static mahsa.com.onlineresturauntbookingsystem.fragments.ReviewFragment.KEY_POST_REVIEW_FRAGMENT;


/**
 * Created by  on 02/11/2016.
 */

public class ViewPagerFragment extends Fragment {

    public static final String  POST_KEY="post_key";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String post_key=getArguments().getString(POST_KEY);

        View view= inflater.inflate(R.layout.fragment_viewpager,container,false);


        Bundle bundle=new Bundle();
        final BookingFragment bookingFragment=new BookingFragment();

        bundle.putString(BookingFragment.RESTAURANT_KEY,post_key);
        bookingFragment.setArguments(bundle);

        final MenuFragment menuFragment=new MenuFragment();
        bundle.putString(MenuFragment.RESTAURANT_KEY,post_key);
        menuFragment.setArguments(bundle);

        final ReviewFragment reviewFragment=new ReviewFragment();

        bundle.putString( KEY_POST_REVIEW_FRAGMENT,post_key);
        reviewFragment.setArguments(bundle);

        final RateUSFragment rateUSFragment = new RateUSFragment();
        bundle.putString(RateUSFragment.RESTAURANT_KEY,post_key);
        rateUSFragment.setArguments(bundle);




        final ViewPager viewPager=(ViewPager)view.findViewById(R.id.viewPager);

        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
                                 @Override
                                 public Fragment getItem(int position) {

                                     Fragment fragment=null;


                                     switch (position) {

                                         case 0:
                                             fragment= bookingFragment;
                                         break;
                                         case 1:
                                             fragment= menuFragment;
                                         break;
                                         case 2:
                                             fragment= reviewFragment;
                                         break;
                                         case 3:
                                             fragment = rateUSFragment;
                                             break;
                                     }
                                     return fragment;

                                  /*if(position==0)
                                      return bookingFragment;
                                     else if (position==1)
                                      return menuFragment;
                                     else if(position==2)
                                      return reviewFragment;
                                     else
                                         return bookingFragment;*/

                                 }

                                 @Override
                                 public int getCount() {
                                     return 4;
                                 }

                                 @Override
                                 public CharSequence getPageTitle(int position) {

                                     String tabName = " ";

                                     switch (position) {

                                         case 0:
                                             tabName = "Book";
                                             break;
                                         case 1:
                                             tabName = "Menu";
                                             break;
                                         case 2:
                                             tabName = "Review";
                                             break;
                                         case 3:
                                             tabName ="Rate Us";


                                     }
                                     return tabName;
                                 }
                             });

        viewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout=(TabLayout)view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }
        });
        return view;
    }
}
