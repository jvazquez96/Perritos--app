package itesm.mx.perritos.event;


public class Evento {

    private String title;
    private String description;
    private int idImage;

    /**
     * Default constructor
     */
    public Evento() {
        this.title = "";
        this.description = "";
        idImage = 0;
    }

    /**
     * Constructor
     * @param title Title of the news
     * @param desciption Description of the news
     */
    public Evento(String title, String desciption, int idImage) {
        this.title = title;
        this.description = desciption;
        this.idImage = idImage;
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
     * Set the idImage of the news
     * @param idImage
     */
    public void setIdImage(int idImage) {
        this.idImage = idImage;
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
     * Return the id of the image
     * @return idImage
     */
    public int getIdImage() {
        return this.idImage;
    }
}
