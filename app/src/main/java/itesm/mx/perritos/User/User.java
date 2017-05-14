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
