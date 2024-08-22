import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TimerTask;
import java.util.Timer;

public class GUI extends JFrame {
    static int secondsPassed = 0;
    static Customer customer;
    static String postalCode = "";
    static boolean wasNull = false;
    static Order order1 = null;
    static Deliverer deliverer = null;
    static int quantity = 0;
    static String discount;
    static double total = 0;

    public static void main(String[] args) throws SQLException {
        Connection conn = ConnectionToDatabase.start(false);
        DelivererData delivererData = DelivererData.getInstance(conn);
        CustomerData customerData = CustomerData.getInstance(conn);
        OrderData orderData = OrderData.getInstance(conn);
        List<Pizza> pizzas = PizzaData.getInstance(conn).getAllPizzas();
        List<Drink> drinks = DrinkData.getInstance(conn).getAllDrinks();
        List<Desert> deserts = DesertData.getInstance(conn).getAllDeserts();
        Pizza_IngridientData pizza_IngridientData = Pizza_IngridientData.getInstance(conn);
        Order_DesertData order_DesertData = Order_DesertData.getInstance(conn);
        Order_DrinkData order_DrinkData = Order_DrinkData.getInstance(conn);
        Order_PizzaData order_PizzaData = Order_PizzaData.getInstance(conn);
        Customer_DiscountData customer_DiscountData = Customer_DiscountData.getInstance(conn);

        JFrame frame = new JFrame();
        // 1st panel register and start order
        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBounds(0, 0, 500, 500);
        panel_1.setBackground(Color.lightGray);

        Label welcome = new Label("Restaurant Name: Group 38");
        welcome.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        welcome.setBounds(25, 25, 500, 50);
        panel_1.add(welcome);

        Label CustomerLabel = new Label("Enter your informations");
        CustomerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        CustomerLabel.setBounds(200, 200, 350, 30);
        panel_1.add(CustomerLabel);

        // name and address
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(150, 230, 100, 30);
        JTextField custName = new JTextField(30);
        custName.setBounds(350, 230, 150, 30);

        JLabel addrLabel = new JLabel("Address(last 4 char is postal code):");
        addrLabel.setBounds(150, 260, 300, 30);
        JTextField custAddr = new JTextField(30);
        custAddr.setBounds(350, 260, 150, 30);

        JLabel phoneLabel = new JLabel("Phone number:");
        phoneLabel.setBounds(150, 290, 100, 30);
        JTextField phoneNumb = new JTextField(30);
        phoneNumb.setBounds(350, 290, 150, 30);

        JLabel remarkLabel = new JLabel("(Enter same credentials as last time, maybe you get discount)");
        remarkLabel.setBounds(200, 320, 350, 30);

        panel_1.add(nameLabel);
        panel_1.add(custName);
        panel_1.add(addrLabel);
        panel_1.add(custAddr);
        panel_1.add(phoneLabel);
        panel_1.add(phoneNumb);
        panel_1.add(remarkLabel);
        List<Deliverer> deliverers = delivererData.getAllDeliverers();
        for (int i = 0; i < deliverers.size(); i++) {
            JLabel postal = new JLabel("" + deliverers.get(i).getPostalCode());
            postal.setBounds(750, 100 + i * 30, 150, 30);
            panel_1.add(postal);
        }
        JLabel postal = new JLabel("Postal codes which we deliver to:");
        postal.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        postal.setForeground(Color.RED);
        postal.setBounds(550, 100, 200, 30);
        panel_1.add(postal);

        JLabel num = new JLabel("This restaurant has: " + deliverers.size() + " deliverers.");
        num.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        num.setBounds(25, 75, 400, 30);
        panel_1.add(num);

        // 2nd panel
        JPanel panel_2 = new JPanel();
        panel_2.setLayout(null);
        panel_2.setBounds(0, 0, 500, 500);
        panel_2.setBackground(Color.lightGray);

        Label menu = new Label("Menu");
        menu.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        menu.setBounds(25, 25, 350, 30);
        panel_2.add(menu);

        // pizzas menu
        JLabel pizzaLabel = new JLabel("Pizza ");
        pizzaLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        pizzaLabel.setForeground(Color.RED);
        pizzaLabel.setBounds(25, 80, 100, 30);
        panel_2.add(pizzaLabel);

        for (int i = 0; i < pizzas.size(); i++) {
            String vege = "";
            if (pizza_IngridientData.isVegeterian(pizzas.get(i).getPizzaId())) {
                vege = " (vege)";
            } else {
                vege = " (not vege)";
            }
            JLabel pizza1 = new JLabel("" + pizzas.get(i).getName() + vege);
            pizza1.setBounds(25, 110 + i * 30, 200, 30);
            panel_2.add(pizza1);
        }
        // pizza prices
        JLabel priceLabel = new JLabel("Price(€): ");
        priceLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        priceLabel.setForeground(Color.RED);
        priceLabel.setBounds(570, 80, 100, 30);
        panel_2.add(priceLabel);

        for (int i = 0; i < pizzas.size(); i++) {
            double pr = pizza_IngridientData.getPizzaPrice(pizzas.get(i).getPizzaId());
            pr = Math.round(pr * 100.0) / 100.0;
            JLabel price = new JLabel("" + pr);
            price.setBounds(570, 110 + i * 30, 150, 30);
            panel_2.add(price);
        }
        for (int i = 0; i < pizzas.size(); i++) {
            String allIngridients = "";
            List<Ingridient> ingridients = pizza_IngridientData.findAllIngridients(pizzas.get(i).getPizzaId());
            for (int j = 0; j < ingridients.size(); j++) {
                allIngridients += ingridients.get(j)+"("+ingridients.get(j).getPrice()+"€)";
                if (j != ingridients.size() - 1) {
                    allIngridients += ", ";
                }
            }
            JLabel price = new JLabel(allIngridients);
            price.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 8));
            price.setBounds(230, 110 + i * 30, 500, 30);
            panel_2.add(price);
        }
        JLabel error = new JLabel();
        error.setBounds(100, 500, 800, 30);
        panel_1.add(error);

        // buttons to continue/cancel
        JButton createOrder = new JButton("Start Order");
        createOrder.setBounds(250, 370, 150, 50);
        createOrder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean postalCodeExists = false;
                if (custAddr.getText().length() >= 4) {
                    postalCode = custAddr.getText().substring(custAddr.getText().length() - 4);
                    for (int i = 0; i < deliverers.size(); i++) {
                        if (deliverers.get(i).getPostalCode().equals(postalCode)) {
                            postalCodeExists = true;
                            break;
                        }
                    }
                }
                if (custAddr.getText().length() >= 4 && phoneNumb.getText().length() >= 7 && postalCodeExists) {
                    panel_1.setVisible(false);
                    frame.remove(panel_1);
                    frame.add(panel_2);
                    customer = customerData.find(phoneNumb.getText());
                    if (customer == null) {
                        wasNull = true;
                        customer = new Customer(custName.getText(), custAddr.getText(), phoneNumb.getText());
                        customerData.insert(customer);
                    }
                    else{
                        customer.setAddress(custAddr.getText());
                        customer.setName(custName.getText());
                        customerData.update(customer);
                    }
                } else if (custAddr.getText().length() >= 4 && phoneNumb.getText().length() >= 7) {
                    error.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
                    error.setForeground(Color.RED);
                    error.setText("Sorry but we don't deliver to you postal code.");
                } else {
                    error.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
                    error.setForeground(Color.RED);
                    error.setText("Address needs to have at least 4 chars and phone number at least 7 chars.");
                }
            }
        });

        JButton cancelOrder = new JButton("Exit");
        cancelOrder.setBounds(250, 430, 150, 50);
        cancelOrder.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                System.exit(0);

            }
        });

        panel_1.add(createOrder);
        panel_1.add(cancelOrder);
        // ingredients
        JLabel ingridientsLabel = new JLabel("Ingredients: ");
        ingridientsLabel.setBounds(230, 80, 100, 30);
        panel_2.add(ingridientsLabel);
        ingridientsLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        ingridientsLabel.setForeground(Color.RED);
        // amount to be bought
        JLabel amountLabel = new JLabel("Amount: ");
        amountLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        amountLabel.setForeground(Color.RED);
        amountLabel.setBounds(700, 80, 100, 30);
        panel_2.add(amountLabel);

        JLabel amounts[] = new JLabel[pizzas.size()];
        for (int i = 0; i < pizzas.size(); i++) {
            JLabel amount1 = new JLabel(0 + "");
            amount1.setBounds(725, 110 + 30 * i, 50, 30);
            panel_2.add(amount1);
            amounts[i] = amount1;

            JButton min1 = new JButton("-");
            min1.setBounds(650, 110 + 30 * i, 50, 30);
            panel_2.add(min1);
            min1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int counter = Integer.valueOf(amount1.getText());
                    if (counter - 1 >= 0)
                        amount1.setText(counter - 1 + "");
                }
            });

            JButton plus1 = new JButton("+");
            plus1.setBounds(750, 110 + 30 * i, 50, 30);
            panel_2.add(plus1);
            plus1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int counter = Integer.valueOf(amount1.getText());
                    if (counter + 1 <= 50)
                        amount1.setText(counter + 1 + "");
                }
            });
        }

        JLabel drinkLabel = new JLabel("Drink ");
        drinkLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        drinkLabel.setForeground(Color.RED);
        drinkLabel.setBounds(25, 420, 100, 30);
        panel_2.add(drinkLabel);

        JLabel priceLabel2 = new JLabel("Price(€): ");
        priceLabel2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        priceLabel2.setForeground(Color.RED);
        priceLabel2.setBounds(120, 420, 100, 30);
        panel_2.add(priceLabel2);

        JLabel amount2 = new JLabel("Amount: ");
        amount2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        amount2.setForeground(Color.RED);
        amount2.setBounds(250, 420, 100, 30);
        panel_2.add(amount2);

        for (int i = 0; i < drinks.size(); i++) {
            JLabel drink = new JLabel("" + drinks.get(i).getName());
            drink.setBounds(25, 450 + i * 30, 150, 30);
            panel_2.add(drink);
            JLabel price = new JLabel("" + drinks.get(i).getPrice());
            price.setBounds(120, 450 + i * 30, 150, 30);
            panel_2.add(price);
        }

        JLabel amounts2[] = new JLabel[drinks.size()];
        for (int i = 0; i < drinks.size(); i++) {
            JLabel amount1 = new JLabel(0 + "");
            amount1.setBounds(275, 450 + 30 * i, 50, 30);
            panel_2.add(amount1);
            amounts2[i] = amount1;

            JButton min1 = new JButton("-");
            min1.setBounds(200, 450 + 30 * i, 50, 30);
            panel_2.add(min1);
            min1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int counter = Integer.valueOf(amount1.getText());
                    if (counter - 1 >= 0)
                        amount1.setText(counter - 1 + "");
                }
            });

            JButton plus1 = new JButton("+");
            plus1.setBounds(300, 450 + 30 * i, 50, 30);
            panel_2.add(plus1);
            plus1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int counter = Integer.valueOf(amount1.getText());
                    if (counter + 1 <= 50)
                        amount1.setText(counter + 1 + "");
                }
            });
        }
        JLabel desertLabel = new JLabel("Desert ");
        desertLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        desertLabel.setForeground(Color.RED);
        desertLabel.setBounds(25, 570, 100, 30);
        panel_2.add(desertLabel);

        JLabel priceLabel3 = new JLabel("Price(€): ");
        priceLabel3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        priceLabel3.setForeground(Color.RED);
        priceLabel3.setBounds(120, 570, 100, 30);
        panel_2.add(priceLabel3);

        JLabel amount3 = new JLabel("Amount: ");
        amount3.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        amount3.setForeground(Color.RED);
        amount3.setBounds(250, 570, 100, 30);
        panel_2.add(amount3);

        for (int i = 0; i < deserts.size(); i++) {
            JLabel desert = new JLabel("" + deserts.get(i).getName());
            desert.setBounds(25, 600 + i * 30, 150, 30);
            panel_2.add(desert);
            JLabel price = new JLabel("" + deserts.get(i).getPrice());
            price.setBounds(120, 600 + i * 30, 150, 30);
            panel_2.add(price);
        }

        JLabel amounts3[] = new JLabel[deserts.size()];
        for (int i = 0; i < deserts.size(); i++) {
            JLabel amount1 = new JLabel(0 + "");
            amount1.setBounds(275, 600 + 30 * i, 50, 30);
            panel_2.add(amount1);
            amounts3[i] = amount1;

            JButton min1 = new JButton("-");
            min1.setBounds(200, 600 + 30 * i, 50, 30);
            panel_2.add(min1);
            min1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int counter = Integer.valueOf(amount1.getText());
                    if (counter - 1 >= 0)
                        amount1.setText(counter - 1 + "");
                }
            });

            JButton plus1 = new JButton("+");
            plus1.setBounds(300, 600 + 30 * i, 50, 30);
            panel_2.add(plus1);
            plus1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int counter = Integer.valueOf(amount1.getText());
                    if (counter + 1 <= 50)
                        amount1.setText(counter + 1 + "");
                }
            });
        }

        JLabel error2 = new JLabel();
        error2.setBounds(500, 640, 500, 30);
        panel_2.add(error2);

        JPanel panel_3 = new JPanel();
        panel_3.setLayout(null);
        panel_3.setBounds(0, 0, 500, 500);
        panel_3.setBackground(Color.lightGray);
        Label time = new Label("10:00");
        time.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 40));
        time.setForeground(Color.RED);
        time.setBounds(350, 350, 500, 50);
        JButton cancelOrder3 = new JButton("Cancel order");
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                int timing = (10 * 60 - secondsPassed);
                if (timing >= 0) {
                    secondsPassed += 1;
                    String minutes = String.format("%02d", timing / 60);
                    String seconds = String.format("%02d", timing % 60);
                    time.setText("" + minutes + ":" + seconds);
                } else {
                    frame.remove(panel_3);
                    panel_3.setVisible(false);
                    frame.dispose();
                    System.exit(0);
                }
            }

        };
        Timer timer = new Timer();

        JButton checkout = new JButton("Go to checkout");
        checkout.setBounds(550, 500, 150, 50);
        checkout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean canGoToCheckout = false;
                for (int i = 0; i < amounts.length; i++) {
                    if (Integer.valueOf(amounts[i].getText()) != 0) {
                        canGoToCheckout = true;
                        break;
                    }
                }
                if (canGoToCheckout) {
                    panel_2.setVisible(false);
                    panel_3.setVisible(true);
                    frame.remove(panel_2);
                    frame.add(panel_3);
                    int counter = 0;
                    for (int i = 0; i < amounts.length; i++) {
                        int am = Integer.valueOf(amounts[i].getText());
                        if (am > 0) {
                            double pr = pizza_IngridientData.getPizzaPrice(pizzas.get(i).getPizzaId()) * am;
                            pr = Math.round(pr * 100.0) / 100.0;
                            total += pr;
                            Label checkout1 = new Label("" + pizzas.get(i).getName() + " * " + am + " = " + pr);
                            checkout1.setBounds(25, 70 + 30 * counter, 250, 30);
                            panel_3.add(checkout1);
                            counter++;

                        }
                    }
                    for (int i = 0; i < amounts2.length; i++) {
                        int am = Integer.valueOf(amounts2[i].getText());
                        if (am > 0) {
                            double pr = drinks.get(i).getPrice() * am;
                            pr = Math.round(pr * 100.0) / 100.0;
                            total += pr;
                            Label checkout1 = new Label("" + drinks.get(i).getName() + " * " + am + " = " + pr);
                            checkout1.setBounds(25, 70 + 30 * counter, 250, 30);
                            panel_3.add(checkout1);
                            counter++;

                        }
                    }
                    for (int i = 0; i < amounts3.length; i++) {
                        int am = Integer.valueOf(amounts3[i].getText());
                        if (am > 0) {
                            double pr = deserts.get(i).getPrice() * am;
                            pr = Math.round(pr * 100.0) / 100.0;
                            total += pr;
                            Label checkout1 = new Label("" + deserts.get(i).getName() + " * " + am + " = " + pr);
                            checkout1.setBounds(25, 70 + 30 * counter, 250, 30);
                            panel_3.add(checkout1);
                            counter++;
                        }
                    }
                    total = Math.round(total * 100.0) / 100.0;
                    double vat = Math.round(total * 0.09 * 100.0) / 100.0;
                    Label checkout1 = new Label("VAT = " + vat + " euros");
                    checkout1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
                    checkout1.setBounds(25, 70 + 30 * (counter), 250, 30);
                    panel_3.add(checkout1);
                    total = Math.round((total + vat) * 100.0) / 100.0;
                    Label checkout2 = new Label("TOTAL = " + total + " euros");
                    checkout2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
                    checkout2.setBounds(25, 70 + 30 * (counter + 1), 300, 30);
                    panel_3.add(checkout2);
                    JButton discountOrder1 = new JButton("Use discount");
                    discountOrder1.setBounds(25, 70+30*(counter+2), 150, 20);
                    double discountTotal = Math.round((total*0.9) * 100.0) / 100.0;
                    discountOrder1.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            discount = customer_DiscountData.findDiscount(customer);
                            if(discount!=null){
                                checkout2.setText("TOTAL = " + total + " euros - 10% discount = "+ discountTotal);
                                total = discountTotal;
                                customer_DiscountData.delete(discount);

                            }
                            panel_3.remove(discountOrder1);
                        }
                    });
                    panel_3.add(discountOrder1);
                    JButton order = new JButton("Order");
                    order.setBounds(25, 70 + 30 * (counter + 3), 150, 50);
                    panel_3.add(order);
                    order.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            panel_3.remove(discountOrder1);
                            try {
                                deliverer = delivererData.getAvailableAndPostalCodeDeliverer(postalCode);
                            } catch (SQLException e2) {
                                e2.printStackTrace();
                            }
                            if (deliverer != null) {
                                order1 = new Order(customer.getPhoneNumber(), deliverer.getPhoneNumber(),
                                        "in progress",total,vat);
                                orderData.insert(order1);
                                deliverer.setAvailable(false);
                                delivererData.update(deliverer);
                                panel_3.remove(order);
                                List<Pizza> pizzasUsed = new ArrayList<>();
                                List<Integer> quantitiesOfPizzas = new ArrayList<>();
                                for (int i = 0; i < amounts.length; i++) {
                                    int am = Integer.valueOf(amounts[i].getText());
                                    if (am > 0) {
                                        quantity += am;
                                        pizzasUsed.add(pizzas.get(i));
                                        quantitiesOfPizzas.add(am);
                                    }
                                }
                                try {
                                    order_PizzaData.insert(order1, pizzasUsed, quantitiesOfPizzas);
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                                customer.setNumberOfPizzasOrdered(quantity);
                                customerData.update(customer);
                                try {
                                    if (customerData.checkIsNumberOfPizzas10(customer)) {
                                        byte[] array = new byte[7];
                                        new Random().nextBytes(array);
                                        discount = new String(array, Charset.forName("UTF-8"));
                                        customer_DiscountData.insert(customer, discount);
                                    }
                                } catch (SQLException e2) {
                                    e2.printStackTrace();
                                }

                                List<Drink> drinksUsed = new ArrayList<>();
                                List<Integer> quantitiesOfDrinks = new ArrayList<>();
                                for (int i = 0; i < amounts2.length; i++) {
                                    int am = Integer.valueOf(amounts2[i].getText());
                                    if (am > 0) {
                                        drinksUsed.add(drinks.get(i));
                                        quantitiesOfDrinks.add(am);
                                    }
                                }
                                try {
                                    order_DrinkData.insert(order1, drinksUsed, quantitiesOfDrinks);
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                                List<Desert> desertsUsed = new ArrayList<>();
                                List<Integer> quantitiesOfDeserts = new ArrayList<>();
                                for (int i = 0; i < amounts3.length; i++) {
                                    int am = Integer.valueOf(amounts3[i].getText());
                                    if (am > 0) {
                                        desertsUsed.add(deserts.get(i));
                                        quantitiesOfDeserts.add(am);
                                    }
                                }
                                try {
                                    order_DesertData.insert(order1, desertsUsed, quantitiesOfDeserts);
                                } catch (SQLException e1) {
                                    e1.printStackTrace();
                                }
                                Label desc2 = new Label("Estimated delivery time is 30 minutes");
                                desc2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                                Label desc = new Label("You can cancel order max. 5 min after placing it.");
                                desc.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                                Label desc1 = new Label("Your order will go out for delivery in:");
                                desc1.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                                desc2.setBounds(330, 150, 500, 50);
                                desc.setBounds(300, 200, 500, 50);
                                desc1.setBounds(330, 250, 500, 50);
                                panel_3.add(desc2);
                                panel_3.add(desc);
                                panel_3.add(desc1);
                                panel_3.add(time);
                                timer.scheduleAtFixedRate(timerTask, 1000, 1000);
                            } else {
                                Label desc = new Label("All deliverers are busy. Try again in 5 minutes");
                                desc.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
                                desc.setBounds(300, 200, 500, 50);
                                panel_3.add(desc);
                            }
                        }
                    });
                    cancelOrder3.setBounds(25, 70 + 30 * (counter + 5), 150, 50);
                    panel_3.add(cancelOrder3);
                    cancelOrder3.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            try {
                                if (order1!=null&&orderData.differenceInTime(order1) < 5) {
                                    frame.dispose();
                                    if (order1 != null) {
                                        order_DesertData.delete(order1);
                                        order_DrinkData.delete(order1);
                                        order_PizzaData.delete(order1);
                                        orderData.delete(order1);
                                        deliverer.setAvailable(true);
                                        delivererData.update(deliverer);
                                        panel_3.remove(cancelOrder3);
                                        customer.setNumberOfPizzasOrdered(-quantity);
                                        customer_DiscountData.delete(discount);
                                    }
                                    if (wasNull) {
                                        customerData.delete(customer.getPhoneNumber());
                                    }
                                    System.exit(0);
                                }
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                    });
                } else {
                    error2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
                    error2.setForeground(Color.RED);
                    error2.setText("Need to select at least one pizza");
                }
            }
        });

        JButton cancelOrder1 = new JButton("Exit");
        cancelOrder1.setBounds(550, 570, 150, 50);
        cancelOrder1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                if (wasNull) {
                    customerData.delete(customer.getPhoneNumber());
                }
                System.exit(0);
            }
        });

        panel_2.add(checkout);
        panel_2.add(cancelOrder1);

        Label Checkout = new Label("Checkout");
        Checkout.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 30));
        Checkout.setBounds(25, 25, 350, 30);
        panel_3.add(Checkout);

        // adding to frame
        frame.add(panel_1);
        frame.setSize(850, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
