import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class Query {
    private static final String URL = "jdbc:mysql://localhost:3306/test";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private Connection connection = null;

    public Query() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver non trovato", e);
        }
    }

    public Connection getConnection() { return connection; }

    public void setConnection(boolean status) throws SQLException {
        if (!status && connection != null) {
            connection.close();
            return;
        }
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
