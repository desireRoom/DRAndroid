package com.room.desire.word.data;

/**
 * Created by desire on 14/12/2.
 */
public class Word {

    private String title;
    private String phonetic;
    private String mean;

    public Word(String title, String phonetic, String mean) {
        this.title = title;
        this.phonetic = phonetic;
        this.mean = mean;
    }

    public String getTitle() {
        return title;
    }
    public String getPhonetic() {
        return phonetic;
    }
    public String getMean() {
        return mean;
    }
}
