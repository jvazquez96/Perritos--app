/*
        Copyright (C) 2017  Jorge Armando Vazquez Ortiz, Valentin Alexandro Trujillo, Santiago Sandoval
        Treviño and Gerardo Suarez Martinez
        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>
*/
package itesm.mx.perritos.news;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jorgevazquez on 3/20/17.
 */

public class News implements Serializable {

    private String title;
    private String description;
    private String photoUrl;
    private String key;
    private boolean isFavorite;
    private boolean isVisible;
    private ArrayList<String> listLikedUsers;

    /**
     * Default constructor
     */
    public News() {
        this.title = "";
        this.description = "";
        photoUrl = null;
        isVisible = false;
        this.listLikedUsers = new ArrayList<>();
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
        this.isFavorite = false;
        this.isVisible = isVisible;
        this.listLikedUsers = new ArrayList<>();
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

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isFavorite() {
        return this.isFavorite;
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

    public void setListLikedUsers(ArrayList<String> listLikedUsers) {
        this.listLikedUsers = listLikedUsers;
    }

    public ArrayList<String> getListLikedUsers() {
        return this.listLikedUsers;
    }

    public void addLikedUser(String user) {
        this.listLikedUsers.add(user);
    }

    public boolean isUserInList(String user) {
        return this.listLikedUsers.contains(user);
    }

    public void removeUserFromList (String user) {
        this.listLikedUsers.remove(user);
    }
}
