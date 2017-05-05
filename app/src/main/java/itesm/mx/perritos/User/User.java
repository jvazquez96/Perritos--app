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

    public User() {

    }

    /**
     * Constructor
     * @param userName
     * @param userEmail
     */
    public User(String userName, String userEmail) {
        this.userName = userName;
        this.userEmail = userEmail;
    }

    /**
     * Setter for userName
     * @param userName
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * Setter for userEmail
     * @param userEmail
     */
    public void setUserEmail(String userEmail){this.userEmail = userEmail;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
