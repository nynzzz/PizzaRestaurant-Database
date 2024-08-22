import java.io.Serializable;

public class Drink extends Item implements Serializable {

    private double price;
    private int drinkId;

    public Drink(String name,double price){
        super(name);
        this.price = price;
    }
    public void setDrinkId(int drinkId){
        this.drinkId = drinkId;
    }
    public int getDrinkId(){
        return drinkId;
    }

    public void setPrice(double price){
        this.price = price;
    }
    public double getPrice(){
        return price;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public Drink clone(){
        return new Drink(name, price);
    }
    
}
