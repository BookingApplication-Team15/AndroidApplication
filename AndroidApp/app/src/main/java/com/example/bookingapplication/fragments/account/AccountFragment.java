package com.example.bookingapplication.fragments.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bookingapplication.clients.ClientUtils;
import com.example.bookingapplication.databinding.FragmentAccountBinding;
import com.example.bookingapplication.model.User;
import com.example.bookingapplication.util.SharedPreferencesManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private User userUpdate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        userUpdate = new User();

        getUserInfo();


//        accountViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    private void getUserInfo(){
        Long id = SharedPreferencesManager.getUserInfo(this.getContext()).getId();
        Call<User> call = ClientUtils.updateUserService.getUser(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200){
                    Log.d("Booking","GET BY ID");

                    userUpdate = response.body();
                    binding.nameInputAccount.setText(userUpdate.getName());
                    binding.lastNameInputAccount.setText(userUpdate.getLastname());
                    binding.phoneInputAccount.setText(userUpdate.getPhoneNumber());
                    binding.streetInput.setText(userUpdate.getAddress().getStreet());
                    binding.cityInput.setText(userUpdate.getAddress().getCity());
                    binding.stateInput.setText(userUpdate.getAddress().getState());
                    binding.postalCodeInput.setText(String.valueOf(userUpdate.getAddress().getPostalCode()));
                    binding.emailInputAccount.setText(userUpdate.getEmail());
                    binding.roleInputAcc.setText(String.valueOf(userUpdate.getUserRole()));

                }else{
                    Log.d("ShopApp","Meesage recieved: "+response.code());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("ShopApp", t.getMessage() != null?t.getMessage():"error");
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}