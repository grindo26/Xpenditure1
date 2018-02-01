package com.xpenditure.www.xpenditure;

import android.view.View;

/**
 * Created by Swaraj on 30-01-2018.
 */

public class RemoveRecycler {
    private String Title;

    public RemoveRecycler() {
    }

    public RemoveRecycler(View catgview) {
    }

    public RemoveRecycler(String title) {

        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

}
