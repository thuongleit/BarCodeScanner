package com.thuongleit.babr.view.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.thuongleit.babr.di.components.ActivityComponent;

import timber.log.Timber;

/**
 * Created by thuongle on 10/18/15.
 */
public abstract class BaseFragment extends Fragment {

    protected ActivityComponent getComponent() {
        return ((BaseActivity) getActivity()).getComponent();
    }

    public static BaseFragment create(Class<? extends BaseFragment> clazz) {
        return BaseFragment.create(clazz, null);
    }

    public static BaseFragment create(Class<? extends BaseFragment> clazz, Bundle args) {
        try {
            BaseFragment fragment = clazz.newInstance();
            if (args != null) {
                fragment.setArguments(args);
            }
            return fragment;
        } catch (Exception e) {
            Timber.e(e, "Cannot create new fragment");
        }
        return null;
    }

    public BaseActivity activity() {
        return ((BaseActivity) getActivity());
    }
}
