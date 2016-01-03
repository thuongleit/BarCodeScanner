package com.whooo.barscanner.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whooo.barscanner.activities.BaseActivity;
import com.whooo.barscanner.injectors.components.ApplicationComponent;
import com.whooo.barscanner.injectors.modules.ActivityModule;
import com.whooo.barscanner.utils.Log;

import butterknife.ButterKnife;

/**
 * Created by thuongle on 10/18/15.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);

        ButterKnife.bind(this, rootView);

        initializeInjectors();
        initializeVariables();
        setupViews();

        return rootView;
    }

    protected ApplicationComponent getApplicationComponent() {
        return ((BaseActivity) getActivity()).getApplicationComponent();
    }

    protected ActivityModule getActivityModule() {
        return new ActivityModule(getActivity());
    }

    protected abstract int getLayoutId();

    protected void initializeInjectors() {
    }

    protected void initializeVariables() {
    }

    protected void setupViews() {
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
            Log.e("Cannot create new fragment " + e.getMessage());
        }
        return null;
    }

    protected void showErrorDialog(String message, boolean isReload) {
        //check if error type is network connection
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message);
        if (isReload) {
            builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    reloadActivity();
                }
            });
        } else {
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.setCancelable(true);

        builder.create().show();
    }

    protected void reloadActivity() {
        getActivity().overridePendingTransition(0, 0);
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);
    }
}
