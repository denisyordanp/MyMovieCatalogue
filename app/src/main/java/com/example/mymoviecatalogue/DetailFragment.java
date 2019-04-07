package com.example.mymoviecatalogue;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleableRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;


public class DetailFragment extends Fragment implements View.OnClickListener {

    public static String EXTRA_TITLE = "extra_title", EXTRA_DESCRIPTION = "extra_description", EXTRA_RELEASE = "extra_release", EXTRA_DIRECTORS = "extra_directors", EXTRA_PHOTO = "extra_photo", EXTRA_CATEGORY = "extra_category";

    TextView txtDetailTitle, txtDetailRealese, txtDetailDirectors, txtDetailDescription;
    ImageView imgDetailPhoto;
    LinearLayout linearLayout;

    private String[] dataTitle, dataDescription, dataDirectors, dataRelease;
    private TypedArray dataPhoto;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtDetailDescription = view.findViewById(R.id.txt_detail_description);
        txtDetailDirectors = view.findViewById(R.id.txt_detail_directors);
        txtDetailRealese = view.findViewById(R.id.txt_detail_realese);
        txtDetailTitle = view.findViewById(R.id.txt_detail_title);
        imgDetailPhoto = view.findViewById(R.id.img_detail_photo);
        linearLayout = view.findViewById(R.id.liv_list);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            txtDetailDescription.setText(getArguments().getString(EXTRA_DESCRIPTION));
            txtDetailTitle.setText(getArguments().getString(EXTRA_TITLE));
            txtDetailDirectors.setText(getArguments().getString(EXTRA_DIRECTORS));
            txtDetailRealese.setText(getArguments().getString(EXTRA_RELEASE));
            Glide.with(this).load(getArguments().getInt(EXTRA_PHOTO)).into(imgDetailPhoto);
        }

        prepare();
        addItem();

    }

    private void addItem() {

        for (int i = 0; i < dataPhoto.length(); i++) {
            ImageView imageView = new ImageView(getContext());
            LinearLayout.LayoutParams size = new LinearLayout.LayoutParams(350, 350);
            imageView.setId(i);
            imageView.setLayoutParams(size);
            imageView.setPadding(10, 10, 10, 10);
            Glide.with(this).load(dataPhoto.getResourceId(i, -1)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setOnClickListener(this);
            linearLayout.addView(imageView);
        }
    }

    private void prepare() {

        dataTitle = getResources().getStringArray(R.array.data_movie_title);
        dataRelease = getResources().getStringArray(R.array.data_movie_release);
        dataDirectors = getResources().getStringArray(R.array.data_movie_directors);
        dataDescription = getResources().getStringArray(R.array.data_movie_description);
        dataPhoto = getResources().obtainTypedArray(R.array.data_movie_photo);

    }

    @Override
    public void onClick(View v) {

        @StyleableRes int i = v.getId();

        txtDetailDescription.setText(dataDescription[i]);
        txtDetailTitle.setText(dataTitle[i]);
        txtDetailDirectors.setText(dataDirectors[i]);
        txtDetailRealese.setText(dataRelease[i]);
        Glide.with(this).load(dataPhoto.getResourceId(i, -100)).into(imgDetailPhoto);

    }
}
