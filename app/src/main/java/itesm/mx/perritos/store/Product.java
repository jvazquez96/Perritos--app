package itesm.mx.perritos.store;

/**
 * Created by DELL1 on 28/03/2017.
 */

public class Product {
    private String sName;
    private double dPrice;
    private int iPicture;

    /**
     * Default Constructor
     */
    public Product(){
        sName = "";
        dPrice = 5;
        iPicture = 0;
    }

    /**
     * Constructor
     * @param sName Name of the product
     * @param dPrice Price of the product
     * @param iPicture Picture of the product
     */
    public Product(String sName, double dPrice, int iPicture){
        this.sName = sName;
        this.dPrice = dPrice;
        this.iPicture = iPicture;
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
     * Set the photo of the product
     * @param iPicture Photo to be set
     */
    public void setiPicture(int iPicture){
        this.iPicture = iPicture;
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
     * Return the photo of the product
     * @return iPicture
     */
    public int getiPicture() {
        return iPicture;
    }
}
