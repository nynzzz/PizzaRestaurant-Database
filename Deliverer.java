public class Deliverer {

    String phoneNumber;
    String name;
    String postalCode;
    boolean available;

    public Deliverer(String phoneNumber, String name, String postalCode,boolean available){
        this.phoneNumber = phoneNumber;
        this.postalCode = postalCode;
        this.name = name;
        this.available = available;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getPostalCode(){
        return postalCode;
    }
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }
    public boolean getAvailable(){
        return available;
    }
    public void setAvailable(boolean available){
        this.available = available;
    }


}
