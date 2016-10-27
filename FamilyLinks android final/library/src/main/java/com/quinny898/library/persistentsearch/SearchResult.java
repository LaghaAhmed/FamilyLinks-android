package com.quinny898.library.persistentsearch;

import android.graphics.drawable.Drawable;

public class SearchResult {
    public String title;
    public String userId;
    public Drawable icon;

    /**
     * Create a search result with text and an icon
     * @param title
     * @param icon
     * @param userId
     */
    public SearchResult(String title, Drawable icon,String userId) {
       this.title = title;
       this.userId = userId;
       this.icon = icon;
    }

    public int viewType = 0;

    public SearchResult(String title){
        this.title = title;
    }

    public SearchResult(int viewType, String title){
        this.viewType = viewType;
        this.title = title;
    }
    
    /**
     * Return the title of the result
     */
    @Override
    public String toString() {
        return title;
    }
    
}