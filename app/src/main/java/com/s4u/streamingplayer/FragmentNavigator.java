package com.s4u.streamingplayer;

import android.os.Build;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


/**
 * Created by Saad on 6/24/2016.
 */

public class FragmentNavigator {

    private Fragment currentSection;
    private Fragment mainSection;
    private int container;
    private MainActivity activity;
    private ActionBar actionBar;

    private FragmentManager fragmentManager;
    public boolean isBack = false;


    public FragmentNavigator(MainActivity activity, Toolbar toolbar, Fragment mainSection, int container) {
        this.activity = activity;
        this.mainSection = mainSection;
        this.container = container;
        this.actionBar = activity.getSupportActionBar();
        this.fragmentManager = activity.getSupportFragmentManager();
        gotoMainSection();
        addStackListener();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideSoftwareKeyboard();
                goBack();

            }
        });

    }

    private void addStackListener()
    {
        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getCurrentFragment() == currentSection) {
                    displayHamburger();
                }
            }
        });
    }

    public void gotoSection(Fragment section)
    {
        try
        {
            displayHamburger();
            hideSoftwareKeyboard();

            if(currentSection != mainSection) fragmentManager.popBackStack(currentSection.getClass().getSimpleName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);

            fragmentManager.beginTransaction()
                    .replace(container,section,section.getClass().getSimpleName())
                    .addToBackStack(section.getClass().getSimpleName())
                    .commitAllowingStateLoss();

            currentSection = section;
        }
        catch (Exception e)
        {

        }

    }

    public void gotoMainSection()
    {
        try {
            currentSection = mainSection;
            displayHamburger();

            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            fragmentManager.beginTransaction()
                    .replace(container, mainSection,mainSection.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
        catch (Exception e)
        {

        }
    }

    public void goBackTo(String name)
    {
        try {
            fragmentManager.popBackStack(name,0);
        }
        catch (Exception e)
        {

        }
    }

    public Fragment getFragment(String name)
    {
        return fragmentManager.findFragmentByTag(name);
    }


    public void gotoSubSection(Fragment subSection)
    {
        hideSoftwareKeyboard();
        displayBack();


        fragmentManager.beginTransaction()
                .replace(container,subSection,subSection.getClass().getSimpleName())
                .addToBackStack(subSection.getClass().getSimpleName())
                .commitAllowingStateLoss();

        subSection.setHasOptionsMenu(true);
    }

    public void switchSubSection(Fragment subSection)
    {
        goBack();
        gotoSubSection(subSection);
    }

    public void restartMainSection(Handler handler)
    {
        fragmentManager.beginTransaction()
                .remove(mainSection)
                .commitAllowingStateLoss();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gotoMainSection();
            }
        },100);
    }

    public void goBack(int n)
    {
        try {
            for(int i = 0 ; i < n ; i++)
            {
                fragmentManager.popBackStack();
            }
        }
        catch (Exception e)
        {

        }
    }

    public void goBack()
    {
        goBack(1);
    }

    public void displayBack()
    {
        isBack = true;

        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    public void displayHamburger()
    {
        isBack = false;
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    public void hideToggleButton()
    {
//        activity.toggle.setDrawerIndicatorEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    public Fragment getCurrentFragment()
    {
        return fragmentManager.findFragmentById(container);
    }

    public Fragment getCurrentSection(){return currentSection;}

    public void hideSoftwareKeyboard()
    {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
