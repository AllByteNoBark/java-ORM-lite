# Java Lightweight ORM

This is a simple, lightweight Object-Relational Mapper (ORM) for Java, built using reflection and JDBC. It provides a structured way to interact with a MySQL database without writing raw SQL queries.

## Features
- **Automatic Query Building**: Supports `SELECT`, `INSERT`, `UPDATE`, and `DELETE` queries.
- **Reflection-Based Mapping**: Maps Java class fields to database columns automatically.
- **Fluent API**: Chainable query methods for easy query construction.
- **Relationship Handling**: Supports `belongsTo()` relationships.
- **No External Dependencies**: Uses plain JDBC for database interaction.

## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/AllByteNoBark/java-ORM-lite.git
   cd java-ORM-lite
   ```
2. Install MySQL connector:
   ```sh
   https://dev.mysql.com/downloads/connector/j/
   Extract it inside the lib folder
   Add the .jar file to the classpath
   ```
3. Ensure you have MySQL running and update database credentials in `Query.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/test";
   private static final String USER = "root";
   private static final String PASSWORD = "";
   ```

## Usage
### Selecting Data
```java
List<Product> products = DB.get(Product.class).where("id", 1).execute();
for (Product p : products) {
    System.out.println(p.user().toString());
}
```

### Inserting Data
```java
Product product = new Product("Juice", "Delicious Lemon Juice", 10.99, 100, "1999-01-01", 1);
DB.insert(Product.class).setValues(product).execute();
```

### Updating Data
```java
DB.update(Product.class).set("price", 11.99).where("name", "Juice").execute();
```

### Deleting Data
```java
DB.delete(Product.class).where("price", 11.99).execute();
```

## Classes Overview
- `BaseBean`: Abstract class for mapping objects to database tables.
- `QuerySelect<T>`: Handles `SELECT` queries.
- `QueryInsert<T>`: Handles `INSERT` queries.
- `QueryUpdate<T>`: Handles `UPDATE` queries.
- `QueryDelete<T>`: Handles `DELETE` queries.
- `DB`: Static helper class for easy database operations.

## License
This project is licensed under the MIT License.

---
Feel free to contribute or open issues if you find bugs or have feature suggestions!
