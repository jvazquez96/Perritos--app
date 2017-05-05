package itesm.mx.perritos.pet;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by jorgevazquez on 3/17/17.
 */


public class Pet implements Serializable {

    private String name;
    private String gender;
    private String age;
    private String description;
    private String key;
    private int requests;
    private String photoUrl;
    private boolean isVisible;
    private boolean isFavorite;
    private ArrayList<String> listLikedUsers;

    /**
     *  Default constructor.
     */
    public Pet() {
        this.listLikedUsers = new ArrayList<>();
    }

    /**
     * Constructor.
     * @param name Name of the Pet.
     * @param gender Gender of the Pet.
     * @param age Age of the Pet.
     * @param description Description of the Pet.
     * @param requests Amout of the request this Pet hast.
     * @param photoUrl id of the Image of the Pet.
     * @param isVisible Visibility of the app
     */
    public Pet(String name, String gender, String age, String description, int requests, String photoUrl, boolean isVisible) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.description = description;
        this.requests = requests;
        this.photoUrl = photoUrl;
        this.isVisible = isVisible;
        this.isFavorite = false;
        this.listLikedUsers = new ArrayList<>();
    }


    /**
     *  Set the name.
     * @param name Name of the Pet.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *  Return the name of the Pet
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the gender of the Pet.
     * @param gender Gender of the pet.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Set the gender of the Pet.
     * @return gender
     */
    public String getGender() {
        return this.gender;
    }

    /**
     * Set the age of the Pet.
     * @param age Age of the pet
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * Return the age of the pet.
     * @return age
     */
    public String getAge() {
        return this.age;
    }

    /**
     * Set the description of the Pet.
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the description of the Pet.
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Set the amount of request for this Pet.
     * @param requests
     */
    public void setRequests(int requests) {
        this.requests = requests;
    }

    /**
     * Return the number of request for this Pet.
     * @return requests
     */
    public int getRequest() {
        return this.requests;
    }

    /**
     * Set the photoUrl of this Pet.
     * @param photoUrl
     */
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


    /**
     * Get the photuURL of the image of this Pet.
     * @return
     */
    public String getPhotoUrl() {
        return this.photoUrl;
    }


    /**
     * Set the visibility of the pet.
     * @param isVisible
     */
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    /**
     * Get the visibility status of the pet
     * @return
     */
    public boolean getIsVisible() {
        return this.isVisible;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public void setFav(boolean fav) {
        this.isFavorite = fav;
    }

    public boolean getFav() {
        return this.isFavorite;
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

    public void removeLikedUser(String user) {
        this.listLikedUsers.remove(user);
    }

    public boolean isUserInList(String user) {
        return this.listLikedUsers.contains(user);
    }

    @Override
    public boolean equals(Object obj) {
        Pet editedPet = (Pet) obj;
        return this.name.equals(editedPet.getName()) && this.gender.equals(editedPet.getGender()) &&
                this.age.equals(editedPet.getAge()) &&
                this.description.equals(editedPet.getDescription()) &&
                this.photoUrl.equals(editedPet.getPhotoUrl()) &&
                this.isVisible == editedPet.getIsVisible();
    }
}
