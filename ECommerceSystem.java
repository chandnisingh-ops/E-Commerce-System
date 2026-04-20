import java.util.*;

// ================= ABSTRACT PRODUCT =================
abstract class Product {
    protected String id, name, description;
    protected double price;
    protected int stock;

    public Product(String id, String name, double price, String description, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }

    public abstract double calculateDiscount();

    public double getFinalPrice() {
        return price - calculateDiscount();
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public void displayBase() {
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.printf("Price: ₹%.2f\n", price);
        System.out.println("Description: " + description);
        System.out.println("Stock: " + stock);
        System.out.printf("Discount: ₹%.2f\n", calculateDiscount());
        System.out.printf("Final Price: ₹%.2f\n", getFinalPrice());
    }

    public abstract void displayInfo();
}

// ================= ELECTRONICS =================
class ElectronicsProduct extends Product {
    private String brand;
    private int warranty;

    public ElectronicsProduct(String id, String name, double price, String desc, int stock,
                              String brand, int warranty) {
        super(id, name, price, desc, stock);
        this.brand = brand;
        this.warranty = warranty;
    }

    public double calculateDiscount() {
        return price * 0.10;
    }

    public void displayInfo() {
        displayBase();
        System.out.println("Type: Electronics");
        System.out.println("Brand: " + brand);
        System.out.println("Warranty: " + warranty + " months");
        System.out.println("----------------------------------------");
    }
}

// ================= CLOTHING =================
class ClothingProduct extends Product {
    private String size, color, material;

    public ClothingProduct(String id, String name, double price, String desc, int stock,
                           String size, String color, String material) {
        super(id, name, price, desc, stock);
        this.size = size;
        this.color = color;
        this.material = material;
    }

    public double calculateDiscount() {
        return price * 0.15;
    }

    public void displayInfo() {
        displayBase();
        System.out.println("Type: Clothing");
        System.out.println("Size: " + size);
        System.out.println("Color: " + color);
        System.out.println("Material: " + material);
        System.out.println("----------------------------------------");
    }
}

// ================= BOOK =================
class BookProduct extends Product {
    private String author, isbn;
    private int pages;

    public BookProduct(String id, String name, double price, String desc, int stock,
                       String author, String isbn, int pages) {
        super(id, name, price, desc, stock);
        this.author = author;
        this.isbn = isbn;
        this.pages = pages;
    }

    public double calculateDiscount() {
        return price * 0.10;
    }

    public void displayInfo() {
        displayBase();
        System.out.println("Type: Book");
        System.out.println("Author: " + author);
        System.out.println("ISBN: " + isbn);
        System.out.println("Pages: " + pages);
        System.out.println("----------------------------------------");
    }
}

// ================= CART =================
class CartItem {
    Product product;
    int qty;

    public CartItem(Product p, int q) {
        product = p;
        qty = q;
    }

    public double getTotal() {
        return product.getFinalPrice() * qty;
    }
}

class ShoppingCart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product p, int qty) {
        for (CartItem item : items) {
            if (item.product.getId().equals(p.getId())) {
                item.qty += qty;
                return;
            }
        }
        items.add(new CartItem(p, qty));
    }

    public void displayCart() {
        System.out.println("\n=== SHOPPING CART ===");

        if (items.isEmpty()) {
            System.out.println("Your cart is empty!");
            return;
        }

        System.out.printf("%-15s %-20s %-10s %-10s %-12s\n",
                "Product ID", "Name", "Price", "Qty", "Total");
        System.out.println("----------------------------------------------------------------------");

        double total = 0;

        for (CartItem item : items) {
            System.out.printf("%-15s %-20s ₹%-9.2f %-10d ₹%-11.2f\n",
                    item.product.getId(),
                    item.product.getName(),
                    item.product.getFinalPrice(),
                    item.qty,
                    item.getTotal());
            total += item.getTotal();
        }

        System.out.println("----------------------------------------------------------------------");
        System.out.printf("Total Amount: ₹%.2f\n", total);
    }

    public double getTotal() {
        double total = 0;
        for (CartItem item : items) total += item.getTotal();
        return total;
    }

    public List<CartItem> getItems() { return items; }
}

// ================= ORDER =================
class Order {
    private static int counter = 1000;
    private String id;
    private Date date;
    private ShoppingCart cart;

    public Order(ShoppingCart cart) {
        this.id = "ORD" + counter++;
        this.date = new Date();
        this.cart = cart;
    }

    public void displayOrder() {
        System.out.println("\n=== ORDER DETAILS ===");
        System.out.println("Order ID: " + id);
        System.out.println("Order Date: " + date);

        cart.displayCart();

        double subtotal = cart.getTotal();
        double gst = subtotal * 0.18;

        System.out.println("\nOrder Summary:");
        System.out.printf("Subtotal: ₹%.2f\n", subtotal);
        System.out.printf("GST (18%%): ₹%.2f\n", gst);
        System.out.printf("Final Amount: ₹%.2f\n", subtotal + gst);

        System.out.println("\n🎉 ORDER CONFIRMED!");
        System.out.println("Status: Processing");
        System.out.println("Estimated Delivery: 2024-01-22");
    }
}

// ================= MAIN =================
public class ECommerceSystem {

    private static List<Product> products = new ArrayList<>();
    private static ShoppingCart cart = new ShoppingCart();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        loadProducts();

        while (true) {
            System.out.println("\n=== E-COMMERCE SYSTEM ===");
            System.out.println("1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Update Cart");
            System.out.println("5. Checkout");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            switch (ch) {
                case 1: viewProducts(); break;
                case 2: addToCart(); break;
                case 3: cart.displayCart(); break;
                case 4: updateCart(); break;
                case 5: checkout(); break;
                case 6: return;
            }
        }
    }

    private static void loadProducts() {

        // Electronics
        products.add(new ElectronicsProduct("E001","Smartphone X",50000,"Latest smartphone with 5G",50,"TechBrand",24));
        products.add(new ElectronicsProduct("E002","Laptop Pro",90000,"High performance laptop",30,"CompTech",12));

        // Clothing
        products.add(new ClothingProduct("C001","Cotton T-Shirt",00,"100% Cotton T-Shirt",100,"M","Blue","Cotton"));
        products.add(new ClothingProduct("C002","Denim Jeans",2500,"Slim fit jeans",80,"L","Black","Denim"));

        // Books
        products.add(new BookProduct("B001","Java Programming",800,"Learn Java from scratch",75,"Chandni Singh","978-3-16-148410-0",400));
        products.add(new BookProduct("B002","Data Structures",650,"Master DS concepts",60,"Aman Rastogi","978-1-23-456789-0",350));
    }

    private static void viewProducts() {
        System.out.println("\n=== AVAILABLE PRODUCTS ===");

        System.out.println("\n📱 ELECTRONICS:");
        for(Product p: products) if(p instanceof ElectronicsProduct) p.displayInfo();

        System.out.println("\n👕 CLOTHING:");
        for(Product p: products) if(p instanceof ClothingProduct) p.displayInfo();

        System.out.println("\n📚 BOOKS:");
        for(Product p: products) if(p instanceof BookProduct) p.displayInfo();
    }

    private static void addToCart() {
        System.out.print("Enter Product ID to add: ");
        String id = sc.nextLine();

        for(Product p: products){
            if(p.getId().equals(id)){
                System.out.print("Enter Quantity: ");
                int qty = sc.nextInt();
                sc.nextLine();

                cart.addItem(p, qty);
                System.out.println("✅ " + p.getName() + " added to cart!");
                return;
            }
        }
        System.out.println("Product not found!");
    }

    private static void updateCart() {
        System.out.print("Enter Product ID: ");
        String id = sc.nextLine();

        System.out.print("Enter New Quantity: ");
        int qty = sc.nextInt();
        sc.nextLine();

        for(CartItem item: cart.getItems()){
            if(item.product.getId().equals(id)){
                item.qty = qty;
                System.out.println("Updated successfully!");
                return;
            }
        }
        System.out.println("Item not found!");
    }

    private static void checkout() {
        Order order = new Order(cart);
        order.displayOrder();
    }
}