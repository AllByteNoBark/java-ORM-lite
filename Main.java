import java.sql.SQLException;
import java.util.List;

class Main {
    public static void main(String[] args) throws SQLException {
        List<Product> products = DB.get(Product.class).where("id", 1).execute();
        for(Product p : products) {
            System.out.println(p.user().toString());
        }

        // Insert
        Product product = new Product("Juice", "Delicious Lemon Juice", 10.99,
                                        100, "1999-01-01", 1);
        DB.insert(Product.class).setValues(product).execute();

        // Update
        DB.update(Product.class).set("price", 11.99).where("name", "Juice");

        // Delete
        DB.delete(Product.class).where("price", 11.99);
    }
}