package unsw.loopmania;

import java.util.HashMap;
import unsw.loopmania.item.Armours.Armour;
import unsw.loopmania.item.Armours.BasicArmour;
import unsw.loopmania.item.Helmets.BasicHelmet;
import unsw.loopmania.item.Helmets.Helmet;
import unsw.loopmania.item.Shields.BasicShield;
import unsw.loopmania.item.Shields.Shield;
import unsw.loopmania.item.Weapons.Staff;
import unsw.loopmania.item.Weapons.Stake;
import unsw.loopmania.item.Weapons.Sword;
import unsw.loopmania.item.Weapons.Weapon;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ShoppingCart {
    private boolean checkout;
    private HashMap<String, Integer> amtCart;
    private HashMap<String, Integer> priceCart;
    private int subtotal;

    public ShoppingCart () {
        amtCart = new HashMap<String, Integer>();
        priceCart = new HashMap<String, Integer>();
        amtCart = initializeCart(amtCart);
        priceCart = initializeCart(priceCart);
        checkout = false;
        subtotal = 0;
        subtotal = checkSubtotal();        
    }

    public HashMap<String, Integer> initializeCart(HashMap<String, Integer> cart) {
        cart.put("Sword", 0);
        cart.put("Stake", 0);
        cart.put("Staff", 0);
        cart.put("BasicHelmet", 0);
        cart.put("BasicShield", 0);
        cart.put("BasicArmour", 0);       
        return cart;
    }
    
    public void addintoCart(Entity item) {
        if (item instanceof Sword) {
            amtCart.put("Sword", (amtCart.get("Sword") + 1));
            Weapon w = (Weapon)item;
            priceCart.put("Sword", (amtCart.get("Sword") * (w.getPrice())));
        } else if (item instanceof Stake) {
            amtCart.put("Stake", (amtCart.get("Stake") + 1));
            Weapon w = (Weapon)item;
            priceCart.put("Stake", (amtCart.get("Stake") * (w.getPrice())));
        } else if (item instanceof Staff) {
            amtCart.put("Staff", (amtCart.get("Staff") + 1));
            Weapon w = (Weapon)item;
            priceCart.put("Staff", (amtCart.get("Staff") * (w.getPrice())));
        } else if (item instanceof BasicShield) {
            amtCart.put("BasicShield", (amtCart.get("BasicShield") + 1));
            Shield s = (Shield)item;
            priceCart.put("BasicShield", (amtCart.get("BasicShield") * (s.getPrice())));
        } else if (item instanceof BasicHelmet) {
            amtCart.put("BasicHelmet", (amtCart.get("BasicHelmet") + 1));
            Helmet h = (Helmet)item;
            priceCart.put("BasicHelmet", (amtCart.get("BasicHelmet") * (h.getPrice())));
        } else if (item instanceof BasicArmour) {
            amtCart.put("BasicArmour", (amtCart.get("BasicArmour") + 1));
            Armour a = (Armour)item;
            priceCart.put("BasicArmour", (amtCart.get("BasicArmour") * (a.getPrice())));
        }
        
    }

    public void checkOut() {
        checkout = true;
        initializeCart(amtCart);
        initializeCart(priceCart); 
        subtotal = 0;
    }

    public int checkSubtotal() {
        for (int p : priceCart.values()) {
            subtotal += p;
        }
        return subtotal;
    }



}