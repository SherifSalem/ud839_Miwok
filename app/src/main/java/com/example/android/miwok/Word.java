package com.example.android.miwok;

/**
 * {@link Word}
 *
 * class is class to provide the user with required miwok translation
 * in his default language
 * Created by sherif on 3/13/18.
 *
 */

public class Word {
    private String mMiwokTranslation, mEnglishTranslation;
    private int mImageResourceId = HAS_NO_IMAGE, mAudioResourceId;
    private static final int HAS_NO_IMAGE = -1;

    /**
     * public constructor to create a new Word object
     * with two parameters
     * @param miwokTranslation for thre miwok translation
     * @param englishTranslation for the english translation
     *
     */
    public Word (String miwokTranslation, String englishTranslation, int audioResource ){
        mMiwokTranslation = miwokTranslation;
        mEnglishTranslation = englishTranslation;
        mAudioResourceId = audioResource;
    }

    public Word(String miwokTranslation, String englishTranslation, int imageResourceId, int audioResource) {

        mMiwokTranslation = miwokTranslation;
        mEnglishTranslation = englishTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResource;
    }

    /**
     * getter method which
     * @return a String value of the miwok translation
     */

    public String getmMiwokTranslation() {
        return mMiwokTranslation;
    }

    /**
     * getter method which
     * @return a String value of the english translation of the word
     *
     */
    public String getmEnglishTranslation() {
        return mEnglishTranslation;
    }


    public int getmImageResourceId() {
        return mImageResourceId;
    }

    /**
     * check whether the object has an image or not
     * @return
     */
    public boolean hasImage(){

        return mImageResourceId != HAS_NO_IMAGE;
    }

    public int getmAudioResourceId() {
        return mAudioResourceId;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mEnglishTranslation='" + mEnglishTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mAudioResourceId=" + mAudioResourceId +
                ", mImageResourceId=" + mImageResourceId +
                '}';
    }
}
