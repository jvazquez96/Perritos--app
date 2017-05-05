package itesm.mx.perritos.product;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DELL1 on 28/03/2017.
 */

public class Product implements Serializable {
    private String sName;
    private double dPrice;
    private String photoUrl;
    private boolean isFavorite;
    private String description;
    private String key;
    private boolean isVisible;
    private ArrayList<String> listLikedUsers;


    /**
     * Default Constructor
     */
    public Product(){
        sName = "";
        dPrice = 5;
        photoUrl = null;
        description = "";
        this.listLikedUsers = new ArrayList<>();
    }

    /**
     * Constructor
     * @param sName Name of the product
     * @param dPrice Price of the product
     * @param photoUrl Picture of the product
     */
    public Product(String sName, double dPrice, String photoUrl, boolean isVisible){
        this.sName = sName;
        this.dPrice = dPrice;
        this.photoUrl = photoUrl;
        this.isFavorite = false;
        this.isVisible = isVisible;
        this.listLikedUsers = new ArrayList<>();
    }

    /**
     * Set the name of the product
     * @param sName Name to be set
     */
    public void setsName(String sName){
        this.sName = sName;
    }

    /**
     * Set the price of the product
     * @param  dPrice Price to be set
     */
    public void setdPrice(double dPrice){
        this.dPrice = dPrice;
    }

    /**
     * Return the name of the product
     * @return sName
     */
    public String getsName(){
        return sName;
    }

    /**
     * Return the price of the product
     * @return dPrice
     */
    public double getdPrice() {
        return dPrice;
    }

    /**
     *
     * @return photoURL
     */
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    /**
     * Set the description of the Product.
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the description of the product.
     * @return description
     */
    public String getDescription() {
        return this.description;
    }


    public void setKey(String key) {
        this.key = key;
    }

    public void setFav(boolean fav) {
        this.isFavorite = fav;
    }

    public boolean getFav() {
        return this.isFavorite;
    }

    public String getKey() {
        return key;
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
