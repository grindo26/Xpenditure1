package com.xpenditure.www.xpenditure;

        import android.view.View;

/**
 * Created by Swaraj on 30-01-2018.
 */

public class CategoriesRecycler {
    private String Title;

    public CategoriesRecycler() {
    }

    public CategoriesRecycler(View catgview) {
    }

    public CategoriesRecycler(String title, String image) {

        Title = title;
        Image = image;
    }

    public String getImage() {

        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    private String Image;

}
