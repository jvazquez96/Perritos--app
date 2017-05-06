package itesm.mx.perritos.event;


import java.io.Serializable;

public class Evento  implements Serializable{
    private String title;
    private String description;
    private int idImage;
    private String StartDate;
    private String EndDate;
    private String HoraInicio;
    private String HoraFinal;
    private boolean LugarVisible;
    private boolean isFavorite;
    private String Lugar;
    private String key;

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
    public Evento(String title, String desciption, int idImage, boolean LugarVisible) {
        this.title = title;
        this.description = desciption;
        this.idImage = idImage;
        this.LugarVisible = LugarVisible;
        this.isFavorite = false;
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
    public String getStartDate() {
        return this.StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public String getEndDate() {
        return this.EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public String getHoraInicio() {
        return this.HoraInicio;
    }

    public void setHoraInicio(String Hora) {
        this.HoraInicio = HoraInicio;
    }

    public String getHoraFinal(){
        return this.HoraFinal;
    }

    public void setHoraFinal(String HoraFinal) {
        this.HoraFinal = HoraFinal;
    }

    public void setLugarVisible(boolean LugarVisible){
        this.LugarVisible = LugarVisible;
    }

    public boolean getLugarVisible(){
        return LugarVisible;
    }

    public String getLugar(){
        return Lugar;
    }

    public void setLugar(String Lugar){
        this.Lugar = Lugar;
    }

    public String getKey(){
        return key;
    }

    public void setKey(String key){
        this.key = key;
    }

    public void setFav(boolean fav) {
        this.isFavorite = fav;
    }

    public boolean getFav() {
        return this.isFavorite;
    }

    //==========FALTA POR IMPLMEMENTAR EN EVENTO (EJEMPLO DESDE PET)==========
    //VARIABLE INICIAL:
    //private ArrayList<String> listLikedUsers;
    //======================
    //METODOS:
      /*
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

    @Override
    public boolean equals(Object obj) {
        Pet editedPet = (Pet) obj;
        return this.name.equals(editedPet.getName()) && this.gender.equals(editedPet.getGender()) &&
                this.age.equals(editedPet.getAge()) &&
                this.description.equals(editedPet.getDescription()) &&
                this.photoUrl.equals(editedPet.getPhotoUrl()) &&
                this.isVisible == editedPet.getIsVisible();
    }
     */
}
