package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {
    //declares an audio manager object
    AudioManager mAudioManager;
    private  int result;

  private  MediaPlayer mMediaPlayer;
  private   MediaPlayer.OnCompletionListener onCompletionListener;

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

        //gets the audio service for our activity
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);


    //sets the onCompletion listener for the mediaPlayer
        onCompletionListener = new MediaPlayer.OnCompletionListener() {
    @Override
    public void onCompletion(MediaPlayer mp) {
  releaseMediaPlayer();
    }
};

//create array of word object to display in the list
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?","minto wuksus",R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?","tinnә oyaase'nә",R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...","oyaaset...",R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling?","michәksәs?",R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I’m feeling good.","kuchi achit",R.raw.phrase_are_you_coming));
        words.add(new Word("Are you coming?" ,"әәnәs'aa?",R.raw.phrase_yes_im_coming));
        words.add(new Word("Yes, I’m coming.","hәә’ әәnәm",R.raw.phrase_yes_im_coming));
        words.add(new Word("I’m coming.","әәnәm",R.raw.phrase_im_coming));
        words.add(new Word("Let’s go." ,"yoowutis",R.raw.phrase_lets_go));
        words.add(new Word("Come here." , "әnni'nem",R.raw.phrase_come_here));

        //finds the view of the listView
        ListView phrasesListView = (ListView) findViewById(R.id.word_list);

        //create new array adapter object
        final WordAdapter wordArrayAdapter = new WordAdapter(this,words,R.color.category_phrases);
        phrasesListView.setAdapter(wordArrayAdapter);

        phrasesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               releaseMediaPlayer();

                //request audio focus and assign it to an integer variable
                result = mAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //create new media player to play the selected list item word, the start the media player
                    mMediaPlayer = MediaPlayer.create(PhrasesActivity.this, words.get(position).getmAudioResourceId());
                    mMediaPlayer.start();

                    //set on completion listener for the media player
                    mMediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });

    }

    //methode to release media resources
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

    /**
     * on stop method to release the media resources when the activity is no more available to the user
     */
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
