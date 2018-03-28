package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

    //declares an audio manager object
    AudioManager mAudioManager;
    private  int result;


  //declare mediaPlayer object and Oncompletion listener object
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener;

    //declare onAudioFocusChangeListener and implements its callback method
    private AudioManager.OnAudioFocusChangeListener  afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
      switch (focusChange){
          /**
           * on losing audio focus we release the media player
           */
          case AudioManager.AUDIOFOCUS_LOSS:
              releaseMediaPlayer();
              break;

          /**
           * when we lose audio focus for a short period of time we pause mediaplayer
           * and set the media player to the start position
           */
          case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                  mMediaPlayer.pause();
                  mMediaPlayer.seekTo(0);
                  break;
          /**
           * we we lose audio focus for short period of time and the other app that
           * requested audio focus can lower its volume we pause mediaPlayer also
           * and sets its position to the start again
           */
          case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                      mMediaPlayer.pause();
                      mMediaPlayer.seekTo(0);
                      break;
          /**
           * we our app regains audio focus the media player will start over again
           */
          case AudioManager.AUDIOFOCUS_GAIN:
              mMediaPlayer.start();
      }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list);

        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        //sets the behavior for the onCompletion object
        onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
             releaseMediaPlayer();
            }
        };

//create array of word object to display in the list
       final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("red","weṭeṭṭi",R.drawable.color_red,R.raw.color_red));
        words.add(new Word("green","chokokki",R.drawable.color_green,R.raw.color_green));
        words.add(new Word("brown","ṭakaakki",R.drawable.color_brown,R.raw.color_brown));
        words.add(new Word("gray","ṭopoppi",R.drawable.color_gray,R.raw.color_gray));
        words.add(new Word("black","kululli",R.drawable.color_black,R.raw.color_black));
        words.add(new Word("white","kelelli",R.drawable.color_white,R.raw.color_white));
        words.add(new Word("dusty yellow","ṭopiisә",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new Word("mustard yellow","chiwiiṭә",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));

        //finds the view of the listView
        ListView colorListView = (ListView) findViewById(R.id.word_list);

        //create new array adapter object
        WordAdapter wordArrayAdapter = new WordAdapter(this,words,R.color.category_colors);
        colorListView.setAdapter(wordArrayAdapter);

        colorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               releaseMediaPlayer();

                //request audio focus and assign it to an integer variable
                result = mAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //creates a new mediaPlayer for the selected list item word to be played, then start the media player with the start method
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, words.get(position).getmAudioResourceId());
                    mMediaPlayer.start();

                    //sets the oncompletion lestener on the media player object
                    mMediaPlayer.setOnCompletionListener(onCompletionListener);

                }
            }
        });


    }
    //methode to release media resources when needed to
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            //abandon audio focus whether audio focus was ranted or not
            mAudioManager.abandonAudioFocus(afChangeListener);
        }
    }
    //sets the behavior for the onStop state for the activity
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
