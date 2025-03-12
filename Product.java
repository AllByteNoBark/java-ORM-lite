public class Product extends BaseBean {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String description;
    private double price;
    private int quantity;
    private String created_at;
    private int user_id;

    public Product() {}

    public Product(String name, String description, double price,
                    int quantity, String created_at, int user_id) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.created_at = created_at;
        this.user_id = user_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public User user() {
        return belongsTo(User.class, "user_id");
    }
}
