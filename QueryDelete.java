import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryDelete<T extends BaseBean> extends Query{
    private StringBuilder query = null;
    private ArrayList<Object> args = new ArrayList<>();
    private final Class<T> type;

    public QueryDelete(Class<T> clazz) {
        super();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.type = clazz;
        this.query = new StringBuilder("DELETE FROM " + clazz.getSimpleName().toLowerCase());
    }

    private QueryDelete<T> baseWhere(String column, Object value, String logicalOperator, String mathOperator) {
        if (query.toString().contains("WHERE")) {
            query.append(" ").append(logicalOperator).append(" ");
        } else {
            query.append(" WHERE ");
        }
        query.append(column).append(" ").append(mathOperator).append(" ?");
        args.add(value);
        return this;
    }

    public QueryDelete<T> where(String column, Object value) {
        return baseWhere(column, value, "AND", "=");
    }

    public QueryDelete<T> orWhere(String column, Object value) {
        return baseWhere(column, value, "OR", "=");
    }

    public QueryDelete<T> notWhere(String column, Object value) {
        return baseWhere(column, value, "AND", "<>");
    }

    public QueryDelete<T> orNotWhere(String column, Object value) {
        return baseWhere(column, value, "OR", "<>");
    }

    public int execute() throws SQLException {
        setConnection(true);
        int ret = 0;

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < args.size(); i++) {
                Object arg = args.get(i);
                if (arg instanceof String) {
                    statement.setString(i + 1, (String) arg);
                } else if (arg instanceof Integer) {
                    statement.setInt(i + 1, (Integer) arg);
                } else if (arg instanceof Boolean) {
                    statement.setBoolean(i + 1, (Boolean) arg);
                } else if (arg instanceof Double) {
                    statement.setDouble(i + 1, (Double) arg);
                }
            }
            ret = statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setConnection(false);
        return ret;
    }
}
