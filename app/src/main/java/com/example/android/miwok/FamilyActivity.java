package com.example.android.miwok;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class FamilyActivity extends AppCompatActivity {

    //declares an audio manager object
    AudioManager mAudioManager;
    private  int result;

    //declare media player and onCompletion objects
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

        //gets the audio service for our activity
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        //create an OnCompletion listener and sets the behavior for media player completion
        onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
            releaseMediaPlayer();
            }
        };
//create array of word object to display in the list
       final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("father","әpә",R.drawable.family_father,R.raw.family_father));
        words.add(new Word("mother","әṭa",R.drawable.family_mother,R.raw.family_mother));
        words.add(new Word("son","angsi",R.drawable.family_son,R.raw.family_son));
        words.add(new Word("daughter","tune",R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new Word("older brother","taachi",R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("younger brother","chalitti",R.drawable.family_younger_brother,R.raw.family_older_sister));
        words.add(new Word("older sister","teṭe",R.drawable.family_older_sister,R.raw.family_younger_sister));
        words.add(new Word("younger sister","kolliti",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        words.add(new Word("grand mother","ama",R.drawable.family_grandmother,R.raw.family_grandmother));
        words.add(new Word("grand father", "paapa",R.drawable.family_grandfather,R.raw.family_grandfather));

        //finds the view of the listView
        ListView familyListView = (ListView) findViewById(R.id.word_list);

        //create new array adapter object
        WordAdapter wordArrayAdapter = new WordAdapter(this,words,R.color.category_family);
        familyListView.setAdapter(wordArrayAdapter);


        //set an item click listener on the selected item of the list
        familyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                releaseMediaPlayer();

                //request audio focus and assign it to an integer variable
                result = mAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

                    //create a mediaplayer to play the choosen audio associated with the word, then start the mesia player
                    mMediaPlayer = MediaPlayer.create(FamilyActivity.this, words.get(position).getmAudioResourceId());
                    mMediaPlayer.start();

                    //set a completion listener for the media player,
                    mMediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });
    }

    //method to release media resources
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

    //sets the appropriate behavior for the activity in the onStop state
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
