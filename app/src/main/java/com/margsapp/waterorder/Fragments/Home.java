package com.margsapp.waterorder.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.margsapp.waterorder.R;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator2;


public class Home extends Fragment {


    ImageCarousel imageCarousel;

    CircleIndicator2 indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imageCarousel = view.findViewById(R.id.imageCarousel);

        imageCarousel.registerLifecycle(getViewLifecycleOwner());


        indicator = view.findViewById(R.id.indicator);

        imageCarousel.setIndicator(indicator);

        List<CarouselItem> list = new ArrayList<>();

        list.add(
                new CarouselItem(
                        R.drawable.img_1,
                        "left"
                )
        );

        list.add(
                new CarouselItem(
                        R.drawable.img_2,
                        "centre"
                )
        );

        list.add(
                new CarouselItem(
                        R.drawable.img_3,
                        "right"
                )
        );

        imageCarousel.setData(list);







        return view;
    }
}