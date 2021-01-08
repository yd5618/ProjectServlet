public class Product {
    // ordered the same as in DB for now
    private String brand;
    private String amount;
    private double sprice;
    private double pprice;
    private int fullstock;
    private boolean limitation;
    private String description;
    private String category;
    private int id;
    private int currentstock;

    public Product(String brand, String amount, double sprice, double pprice, int fullstock, boolean limitation, String description, String category, int id, int currentstock) {
        // super();?
        this.brand = brand;
        this.amount = amount;
        this.sprice = sprice;
        this.pprice = pprice;
        this.fullstock = fullstock;
        this.limitation = limitation;
        this.description = description;
        this.category = category;
        this.id = id;
        this.currentstock = currentstock;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public String getAmount() {
        return amount;
    }
}
