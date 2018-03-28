package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NumbersActivity extends AppCompatActivity {

    //declares audio manager object to handle focus when playin audio file
    AudioManager mAudioManager;
    //declare mediaPlayer object and Oncompletion listener object
    private MediaPlayer mMediaPlayer;
    private MediaPlayer.OnCompletionListener onCompletionListener;
    private int result;

    //declare onAudioFocusChangeListener and implements its callback method
    private AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
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
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

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
                default:

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words_list);

        //gets the audio service for our activity
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);


        //sets the behavior for the onCompletion object
        onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //release mediaPlayer after complete playin

                releaseMediaPlayer();
            }
        };
//create array of word object to display in the list
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("one", "lutti", R.drawable.number_one, R.raw.number_one));
        words.add(new Word("two", "otiiko", R.drawable.number_two, R.raw.number_two));
        words.add(new Word("three", "tolookosu", R.drawable.number_three, R.raw.number_three));
        words.add(new Word("four", "oyyisa", R.drawable.number_four, R.raw.number_four));
        words.add(new Word("five", "massokka", R.drawable.number_five, R.raw.number_five));
        words.add(new Word("six", "temmokka", R.drawable.number_six, R.raw.number_six));
        words.add(new Word("seven", "kenekaku", R.drawable.number_seven, R.raw.number_seven));
        words.add(new Word("eight", "kawinta", R.drawable.number_eight, R.raw.number_eight));
        words.add(new Word("nine", "wo’e", R.drawable.number_nine, R.raw.number_nine));
        words.add(new Word("ten", "na’aacha", R.drawable.number_ten, R.raw.number_ten));

        //finds the view of the listView
        ListView numbersListView = (ListView) findViewById(R.id.word_list);

        //create new array adapter object
        WordAdapter wordArrayAdapter = new WordAdapter(this, words, R.color.category_numbers);
        numbersListView.setAdapter(wordArrayAdapter);
        numbersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();

                //request audio focus and assign it to an integer variable
                result = mAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);


                //checks if the audio focus request granted then plays the audio

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {


                    //creates a new mediaPlayer for the selected list item word to be played, then start the media player with the start method
                    mMediaPlayer = MediaPlayer.create(NumbersActivity.this, words.get(position).getmAudioResourceId());
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

