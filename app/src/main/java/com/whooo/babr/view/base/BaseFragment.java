package com.whooo.babr.view.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import timber.log.Timber;

/**
 * Created by thuongle on 10/18/15.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    public BaseFragment create() {
        return create(null);
    }

    @Nullable
    public BaseFragment create(Bundle args) {
        try {
            BaseFragment fragment = this.getClass().newInstance();
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


    @Override
    public void onStart() {
        super.onStart();
        if (getPresenter() != null) {
            getPresenter().subscribe();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getPresenter() != null) {
            getPresenter().unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }
    }

    protected BasePresenter getPresenter() {
        return null;
    }
}
