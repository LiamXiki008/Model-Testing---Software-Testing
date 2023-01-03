public class AlertStruct {
    // Properties
    protected int alertType;
    protected String heading;
    protected String description;
    protected String url;
    protected String imageUrl;
    protected String postedBy = "aba2df1c-5441-4581-9dc2-5413c9691825";
    protected int priceInCents;

    // Constructor
    public AlertStruct(int alertType, String heading, String description, String url, String imageUrl,String postedBy, int priceInCents) {
        this.alertType = alertType;
        this.heading = heading;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.postedBy = postedBy;
        this.priceInCents = priceInCents;

    }

    public AlertStruct(){}
}
