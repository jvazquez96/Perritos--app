package itesm.mx.perritos.product;

import java.io.Serializable;

/**
 * Created by DELL1 on 28/03/2017.
 */

public class Product implements Serializable {
    private String sName;
    private double dPrice;
    private String photoUrl;
    private String description;
    private String key;


    /**
     * Default Constructor
     */
    public Product(){
        sName = "";
        dPrice = 5;
        photoUrl = null;
        description = "";
    }

    /**
     * Constructor
     * @param sName Name of the product
     * @param dPrice Price of the product
     * @param iPicture Picture of the product
     */
    public Product(String sName, double dPrice, String photoUrl,String description){
        this.sName = sName;
        this.dPrice = dPrice;
        this.description = description;
        this.photoUrl = photoUrl;
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

    public String getKey() {
        return key;
    }
}
