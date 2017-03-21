package itesm.mx.perritos;

/**
 * Created by alextrujillo on 20/03/17.
 */

public class Evento {

    private String title;
    private  String descrip;
    private int idImage;


    /**
     * Constructor.
     * @param title Title of the news.
     * @param descrip Description of the news.
     */

    public Evento(String title, String descrip, int idImage){
        this.title = title;
        this.descrip = "Click para leer más información";
        this.idImage = idImage;

    }

    public void setTitle(String description) {
        this.title = description;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String description) {
        this.descrip = description;
    }

    public String getDescription() {
        return this.descrip;
    }

    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public int getIdImage() {
        return this.idImage;
    }
}
