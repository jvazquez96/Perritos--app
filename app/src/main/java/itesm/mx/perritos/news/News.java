package itesm.mx.perritos.news;

import java.io.Serializable;

/**
 * Created by jorgevazquez on 3/20/17.
 */

public class News implements Serializable {

    private String title;
    private String description;
    private String photoUrl;
    private String key;
    private boolean isVisible;

    /**
     * Default constructor
     */
    public News() {
        this.title = "";
        this.description = "";
        photoUrl = null;
        isVisible = false;
    }

    /**
     * Constructor
     * @param title Title of the news
     * @param desciption Description of the news
     */
    public News(String title, String desciption, String photoUrl, boolean isVisible) {
        this.title = title;
        this.description = desciption;
        this.photoUrl = photoUrl;
        this.isVisible = isVisible;
    }

    /**
     * Set the title of the news
     * @param title News to be set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Set the description of the news
     * @param description Description to be set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * Return the title of the news
     * @return title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Return the description of the news
     * @return description
     */
    public String getDescription() {
        return this.description;
    }



    /**
     * Set the idImage of the news
     * @param photoUrl
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
       return this.photoUrl;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    @Override
    public boolean equals(Object obj) {
        News edit = (News) obj;
        return this.title.equals(edit.getTitle()) &&
                this.description.equals(edit.getDescription()) &&
                this.photoUrl.equals(edit.getPhotoUrl());
    }

    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public boolean getIsVisible() {
        return this.isVisible;
    }
}
