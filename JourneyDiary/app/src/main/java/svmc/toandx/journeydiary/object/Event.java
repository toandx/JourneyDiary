package svmc.toandx.journeydiary.object;

public class Event {
    public String name,time,image_uri,address;
    public Boolean location,favorite;
    public double longitude,latitude;
    public Event(String name,String time,String image_uri,Boolean favorite)
    {
        this.name=name;
        this.time=time;
        this.image_uri=image_uri;
        this.favorite=favorite;
        this.location=false;
    }
    public Event(String name,String time,String image_uri, Boolean favorite,double latitude,double longitude, String address)
    {
        this.name=name;
        this.time=time;
        this.image_uri=image_uri;
        this.favorite=favorite;
        this.location=true;
        this.latitude=latitude;
        this.longitude=longitude;
        this.address=address;
    }

}
