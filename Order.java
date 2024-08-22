public class Order {

    private int orderId;
    private String customerId;
    private String delivererId;
    private String status;
    private double VAT;
    private double total;

    public Order(String customerId, String delivererId, String status, double total, double vat){
        this.customerId = customerId;
        this.delivererId = delivererId;
        this.status = status;
        this.total = total;
        this.VAT = vat;
    }

    public void setOrderId(int orderId){
        this.orderId = orderId;
    }

    public int getOrderId(){
        return orderId;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    public String getCustomerId(){
        return customerId;
    }

    public String getDelivererId(){
        return delivererId;
    }
    public void setTotal(double total){
        this.total = total;
    }
    public void setVAT(double VAT){
        this.VAT = VAT;
    }
    public double getTotal(){
        return total;
    }
    public double getVAT(){
        return VAT;
    }

}
