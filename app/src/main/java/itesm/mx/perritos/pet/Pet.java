package itesm.mx.perritos.pet;

import java.io.Serializable;

/**
 * Created by jorgevazquez on 3/17/17.
 */


public class Pet implements Serializable {

    private String name;
    private String gender;
    private int age;
    private String description;
    private int requests;
    private int idImage;
    private String encodedImage;

    /**
     *  Default constructor.
     */
    public Pet() {
        
    }

    /**
     * Constructor.
     * @param name Name of the Pet.
     * @param gender Gender of the Pet.
     * @param age Age of the Pet.
     * @param description Description of the Pet.
     * @param requests Amout of the request this Pet hast.
     * @param idImage id of the Image of the Pet.
     */
    public Pet(String name, String gender, int age, String description, int requests, int idImage) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.description = description;
        this.requests = requests;
        this.idImage = idImage;
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
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Return the age of the pet.
     * @return age
     */
    public int getAge() {
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
     * Set the idImage of this Pet.
     * @param idImage
     */
    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }


    /**
     * Get the id of the image of this Pet.
     * @return
     */
    public int getIdImage() {
        return this.idImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public String getEncodedImage() {
        return this.encodedImage;
    }
}
