package com.example.bookingapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookingapplication.R;
import com.example.bookingapplication.model.ApartmentCard;
import com.google.android.material.card.MaterialCardView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ApartmentCardsListAdapter extends ArrayAdapter<ApartmentCard> {
    private ArrayList<ApartmentCard> aProducts;

    public ApartmentCardsListAdapter(Context context, ArrayList<ApartmentCard> products){
        super(context, R.layout.apartment_card, products);
        aProducts = products;

    }

    @Override
    public int getCount() {
        return aProducts.size();
    }


    @Nullable
    @Override
    public ApartmentCard getItem(int position) {
        return aProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ApartmentCard card = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.apartment_card,
                    parent, false);
        }
        MaterialCardView apartment_card = convertView.findViewById(R.id.apartment_card_item);
        ImageView imageView = convertView.findViewById(R.id.apartment_card_image);
        TextView productTitle = convertView.findViewById(R.id.apartment_card_title);
        TextView product_desc11 = convertView.findViewById(R.id.apartment_card_desc11);
        TextView product_desc12 = convertView.findViewById(R.id.apartment_card_desc12);

        if(card != null){

//            // Dekodiranje Base64 Stringa u byte[]
//            byte[] imageBytes = Base64.decode(card.getImage(), Base64.DEFAULT);
//
//// Pretvaranje byte[] u Bitmap
//            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
//
//// Postavljanje Bitmapa u ImageView koristeći Glide
//            Glide.with(BookingApp.getContext())
//                    .load(Base64.decode(card.getImage(), Base64.DEFAULT))
//                    .into(imageView);

            String dusan = "data:image/jpeg;base64,";
//            imageView.setImageBitmap(convertBase64ToBitmap(card.getImage()));
            productTitle.setText(card.getTitle());
            product_desc11.setText(card.getDescriptionInfo());
            product_desc12.setText(card.getDescriptionRating());
            apartment_card.setOnClickListener(v -> {
                // Handle click on the item at 'position'
                Log.i("Booking", "Clicked: " + card.getTitle() + ", id: " +
                        card.getId().toString());
                Toast.makeText(getContext(), "Clicked: " + card.getTitle()  +
                        ", id: " + card.getId().toString(), Toast.LENGTH_SHORT).show();
            });
        }

        return convertView;
    }

    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    private Bitmap convertBase64ToBitmap(String b64) {
        byte[] imageAsBytes = Base64.decode(b64, 0);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }
}
