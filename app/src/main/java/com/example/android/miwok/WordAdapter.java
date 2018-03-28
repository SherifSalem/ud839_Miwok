package com.example.android.miwok;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sherif on 3/13/18.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int mColor;


    public WordAdapter(@NonNull Context context, ArrayList<Word> wordArrayList,int color) {
        super(context,0, wordArrayList);
        mColor = color;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //create a view object for the list view item
    View wordItemView = convertView;

    if (wordItemView== null){
        //inflating the view from out cutom layout
        wordItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
    }

    //get the current word object located  at this position in the list
    Word currentWord = getItem(position);




    //find the view for the miwok word and the english word in the list item
        TextView miwokText = (TextView) wordItemView.findViewById(R.id.miwok_word_textview);
        TextView englishText = (TextView) wordItemView.findViewById(R.id.english_word_textview);


        //find the view of the play button
        ImageView playImageView = (ImageView) wordItemView.findViewById(R.id.play_button);


        //set the text for each textview
        miwokText.setText(currentWord.getmMiwokTranslation());
        englishText.setText(currentWord.getmEnglishTranslation());


        //find the view of the linearlayout of the text views
        View textContainer = wordItemView.findViewById(R.id.list_item_linearlayout);

        //gets the color from the context
        int color = ContextCompat.getColor(getContext(),mColor);

        //sets the back ground of the text container to
        // the assigned current passed to the word adapter constructor
        textContainer.setBackgroundColor(color);


       //find the image view for the list item
        ImageView imageResource = (ImageView) wordItemView.findViewById(R.id.list_item_imageView);
       //check if the current object has an image or not
        if (currentWord.hasImage()) {

            //set the image resource for the list item
            imageResource.setImageResource(currentWord.getmImageResourceId());
           //make sure the image is visible
            imageResource.setVisibility(View.VISIBLE);

        //if doesn't has an image resource
        }else {
            imageResource.setVisibility(View.GONE);
        }


       // wordItemView.setBackgroundColor();
        return wordItemView;
    }


}
