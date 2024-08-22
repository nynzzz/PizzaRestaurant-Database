import java.io.Serializable;

public class Ingridient implements Serializable {

    private int ingridientId;
    private String name;
    private double price;
    private String vegeterian;

    public Ingridient(String name, double price, String vegeterian){
        this.name = name;
        this.price = price;
        this.vegeterian = vegeterian;
    }

    public void setIngridientId(int ingridientId){
        this.ingridientId = ingridientId;
    }
    public int getIngridientId(){
        return ingridientId;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setPrice(double price){
        this.price = price;
    }
    public double getPrice(){
        return price;
    }
    
    public void setVegeterian(String vegeterian){
        this.vegeterian = vegeterian;
    }
    public String getVegeterian(){
        return vegeterian;
    }

    @Override
    public String toString(){
        return name;
    }

    @Override
    public Ingridient clone(){
        return new Ingridient(name,price,vegeterian);
    }
}
