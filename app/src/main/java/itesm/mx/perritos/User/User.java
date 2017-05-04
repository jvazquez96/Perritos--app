package itesm.mx.perritos.User;

import java.util.ArrayList;

import itesm.mx.perritos.news.News;
import itesm.mx.perritos.pet.Pet;
import itesm.mx.perritos.product.Product;

/**
 * Created by alextrujillo on 21/04/17.
 */

public class User {
    private String userName;
    private String userEmail;
    private ArrayList<Pet> userFavoritePets;
    private ArrayList<Product> userFavoriteProducts;
    private ArrayList<News> userFavoriteNews;

    public User() {

    }

    /**
     * @param userName
     * @param userEmail
     * @param userFavoritePets Array of Pets selected as Favotives
     * @param userFavoriteProducts Array of Products selected as Favotives
     * @param userFavoriteNews Array of News selected as Favotives
     */

    public User(String userName, String userEmail, ArrayList<Pet>  userFavoritePets, ArrayList<Product> userFavoriteProducts,ArrayList<News> userFavoriteNews) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userFavoritePets = userFavoritePets;
        this.userFavoriteProducts = userFavoriteProducts;
        this.userFavoriteNews = userFavoriteNews;
    }



    public void setUserName(String userName){
        this.userName = userName;
    }

    public void setUserEmail(String userEmail){this.userEmail = userEmail;
    }

    public void setUserFavoritePets(ArrayList<Pet> userFavoritePets){
        this.userFavoritePets = userFavoritePets;
    }

    public void setUserFavoriteProducts( ArrayList<Product>  userFavoriteProducts){
        this.userFavoriteProducts = userFavoriteProducts;
    }

    public void setUserFavoriteNews( ArrayList<News>  userFavoriteNews){
        this.userFavoriteNews = userFavoriteNews;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }
    
    public ArrayList<Pet> getUserFavoritePets() {
        return this.userFavoritePets;
    }

    public ArrayList<Product> getUserFavoriteProducts() {
        return this.userFavoriteProducts;
    }

    public ArrayList<News> getUserFavoriteNews() {
        return this.userFavoriteNews;
    }


}
