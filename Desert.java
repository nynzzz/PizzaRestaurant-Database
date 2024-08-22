import java.io.Serializable;

public class Desert extends Item implements Serializable {

    private int desertId;
    private double price;

    public Desert(String name, double price) {
        super(name);
        this.price = price;
    }

    public void setDesertId(int desertId){
        this.desertId = desertId;
    }
    public int getDesertId(){
        return desertId;
    }

    public double getPrice(){
        return price;
    }

    public void setPrice(double price){
        this.price = price;
    }

    @Override
    public String toString(){
        return "desertId = "+desertId+", name = "+name+", price = "+price;
    }

    @Override
    public Drink clone(){
        return new Drink(name, price);
    }
    
}
