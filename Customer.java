public class Customer {
    private String address;
    private String name;
    private String phoneNumber;
    private int numberOfPizzas;

    public Customer(String name, String address, String phoneNumber){
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setNumberOfPizzasOrdered(int numberOfPizzas){
        this.numberOfPizzas += numberOfPizzas;
    }

    public int getNumberOfPizzasOrdered(){
        return numberOfPizzas;
    }


    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public String getPhoneNumber(){
        return phoneNumber;
    } 

    public void setAddress(String address){
        this.address = address;
    }
    public String getAddress(){
        return address;
    }

    @Override
    public String toString(){
        return " name = "+name+", address = "+address+", phone number = "+phoneNumber;
    }

    @Override
    public Customer clone(){
        return new Customer(name, address,phoneNumber);
    }
}
