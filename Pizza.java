import java.io.Serializable;
import java.util.List;

public class Pizza extends Item implements Serializable {
    private int pizzaId;
    private List<Ingridient> ingridients;
    public Pizza(String name){
        super(name);
        
    }
    public List<Ingridient> getIngridients(){
        return ingridients;
    }
    public void setPizzaId(int pizzaId){
        this.pizzaId  = pizzaId;
    }
    public int getPizzaId(){
        return pizzaId;
    }

    @Override
    public String toString(){
        return "pizzaId = "+pizzaId+", name = "+name;
    }

    
}
