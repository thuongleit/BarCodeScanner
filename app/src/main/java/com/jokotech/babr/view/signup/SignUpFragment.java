package com.jokotech.babr.view.signup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jokotech.babr.R;
import com.jokotech.babr.databinding.FragmentSignUpBinding;
import com.jokotech.babr.view.base.BaseFragment;

/**
 * Created by thuongle on 6/8/16.
 */

public class SignUpFragment extends BaseFragment implements SignUpContract.View {

    public static SignUpFragment newInstance() {
        return new SignUpFragment();
    }

    public SignUpFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSignUpBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);

        return binding.getRoot();
    }

    @Override
    public void setPresenter(SignUpContract.Presenter presenter) {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onInAppError(Throwable e) {

    }

    @Override
    public void onCreateUserSuccess() {

    }
}
