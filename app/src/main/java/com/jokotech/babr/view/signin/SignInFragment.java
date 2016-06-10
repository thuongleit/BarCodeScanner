package com.jokotech.babr.view.signin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jokotech.babr.view.base.BaseFragment;

/**
 * Created by thuongle on 6/8/16.
 */

public class SignInFragment extends BaseFragment implements SignInContract.View {

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    public SignInFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setPresenter(SignInContract.Presenter presenter) {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onInAppError(Throwable e) {

    }
}
