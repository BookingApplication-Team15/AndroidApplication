package com.example.bookingapplication.fragments.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.bookingapplication.R;
import com.example.bookingapplication.adapters.ApartmentCardsListAdapter;
import com.example.bookingapplication.clients.ClientUtils;
import com.example.bookingapplication.databinding.FragmentApartmentCardsListBinding;
import com.example.bookingapplication.model.ApartmentCard;
import com.example.bookingapplication.model.Card;
import com.example.bookingapplication.model.User;
import com.example.bookingapplication.model.enums.UserType;
import com.example.bookingapplication.util.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartmentCardsListFragment extends ListFragment {

    public static ArrayList<ApartmentCard> products = new ArrayList<ApartmentCard>();
    private ApartmentCardsListAdapter adapter;
    private static final String ARG_PARAM = "param";
    private ArrayList<ApartmentCard> mProducts;
    private FragmentApartmentCardsListBinding binding;
    private ArrayList<ApartmentCard> cards = new ArrayList<>();
    private static Map<String, String> map = new HashMap<>();
    private List<ApartmentCard> apartmentCards = new ArrayList<>();
    public static ApartmentCardsListFragment newInstance(ArrayList<ApartmentCard> products, Map<String, String> mapa){
        ApartmentCardsListFragment fragment = new ApartmentCardsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM, products);
        fragment.setArguments(args);
        map = mapa;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Log.i("ShopApp", "onCreateView Products List Fragment");
        binding = FragmentApartmentCardsListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        prepareApartmentCardsList();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.i("ShopApp", "onCreate Products List Fragment");
        if (getArguments() != null) {
            mProducts = getArguments().getParcelableArrayList(ARG_PARAM);
            adapter = new ApartmentCardsListAdapter(getActivity(), mProducts);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

    }

    private void prepareApartmentCardsList(){
        getAccommodations();
        Log.d("PrintKartica","alooo");
    }

    private void getAccommodations(){
        User user = SharedPreferencesManager.getUserInfo(getContext().getApplicationContext());
        if(user.getUserRole().equals(UserType.GUEST)){
            getAccommodationsForGuest(user.getId());
        }else{
//            getAccommodationsWithoutLike();
            filterAccommodation();
        }
    }
    private void getAccommodationsForGuest(Long id){
        ProgressBar loadingProgressBar = getActivity().findViewById(R.id.loadingPanel);
        loadingProgressBar.setVisibility(View.VISIBLE);

        Call<List<Card>> call = ClientUtils.apartmentService.getAllAccommodations(id);
        call.enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                loadingProgressBar.setVisibility(View.GONE);
                for (Card card : response.body()) {
                    String rate;
                    if(card.getAvgRate() == null){
                        rate = "";
                    } else {
                        rate = card.getAvgRate().toString();
                    }
                    ApartmentCard ac = new ApartmentCard(card.getId(), card.getTitle(), card.getAddress().toString(), rate, card.getImage(),card.getIsFavourite());
//                    markFavouriteAcc(ac);
                    Log.d("VrednostBul",ac.getTitle());
                    Log.d("VrednostBul",String.valueOf(card.getIsFavourite()));
//                    cards.add(ac);
                    apartmentCards.add(ac);
                }
                filterAccommodation();
//                addProducts(cards);

            }
            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }
    private void getAccommodationsWithoutLike(){
        ProgressBar loadingProgressBar = getActivity().findViewById(R.id.loadingPanel);
        loadingProgressBar.setVisibility(View.VISIBLE);

        Call<List<Card>> call = ClientUtils.apartmentService.getAccommodationsCards();
        call.enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                loadingProgressBar.setVisibility(View.GONE);

                for (Card card : response.body()) {
                    String rate;
                    if(card.getAvgRate() == null){
                        rate = "";
                    } else {
                        rate = card.getAvgRate().toString();
                    }
                    ApartmentCard ac = new ApartmentCard(card.getId(), card.getTitle(), card.getAddress().toString(), rate, card.getImage(),card.getIsFavourite());
                    Log.d("VrednostBul",ac.getTitle());
                    Log.d("VrednostBul",String.valueOf(card.getIsFavourite()));
                    cards.add(ac);
                }

                addProducts(cards);

            }
            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void filterAccommodation(){
        ProgressBar loadingProgressBar = getActivity().findViewById(R.id.loadingPanel);
        loadingProgressBar.setVisibility(View.VISIBLE);
//        Log.d("Mapa", map.toString());

        Call<List<Card>> call = ClientUtils.accommodationService.filterAccommodations(map);
        call.enqueue(new Callback<List<Card>>() {
            @Override
            public void onResponse(Call<List<Card>> call, Response<List<Card>> response) {
                loadingProgressBar.setVisibility(View.GONE);
                Log.d("filterRezultat", response.body().toString());
                User user = SharedPreferencesManager.getUserInfo(getContext().getApplicationContext());
                for (Card card : response.body()) {
                    String rate;
                    if(card.getAvgRate() == null){
                        rate = "";
                    } else {
                        rate = card.getAvgRate().toString();
                    }
                    ApartmentCard ac = new ApartmentCard(card.getId(), card.getTitle(), card.getAddress().toString(), rate, card.getImage(),card.getIsFavourite());

                    if(user.getUserRole().equals(UserType.GUEST)){
                        ApartmentCard appCard = apartmentCards.stream().filter(c -> c.getId().equals(card.getId())).findFirst().get();
                        if(appCard.getIsLiked()){
                            ac.setIsLiked(true);
                        } else {
                            ac.setIsLiked(false);
                        }
                    }

                    cards.add(ac);
                }

                addProducts(cards);

            }
            @Override
            public void onFailure(Call<List<Card>> call, Throwable t) {
                Log.d("FAIIIILLL", t.getMessage());
                loadingProgressBar.setVisibility(View.GONE);
            }
        });
    }
    private void markFavouriteAcc(ApartmentCard card){
        Long userId = SharedPreferencesManager.getUserInfo(getContext().getApplicationContext()).getId();
        Call<Boolean> call = ClientUtils.guestService.isFavourite(userId,card.getId());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d("ResponseIsLike",String.valueOf(response.code()));
                if(response.code() == 200){
                    card.setIsLiked(true);
                }else{
                    card.setIsLiked(false);
                }
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
            }
        });
    }

    private void addProducts(ArrayList<ApartmentCard> productss){
        this.adapter.clear();
        this.adapter.addAll(productss);
    }


}