import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuerySelect<T extends BaseBean> extends Query{
    private StringBuilder query = null;
    private ArrayList<Object> args = new ArrayList<>();
    private final Class<T> type;

    public QuerySelect(Class<T> clazz) {
    	this.type = clazz;
        this.query = new StringBuilder("SELECT * FROM " + clazz.getSimpleName().toLowerCase());
    }

    private QuerySelect<T> baseWhere(String column, Object value, String logicalOperator, String mathOperator) {
        if (query.toString().contains("WHERE")) {
            query.append(" ").append(logicalOperator).append(" ");
        } else {
            query.append(" WHERE ");
        }
        query.append(column).append(" ").append(mathOperator).append(" ?");
        args.add(value);
        return this;
    }

    public QuerySelect<T> where(String column, Object value) {
        return baseWhere(column, value, "AND", "=");
    }

    public QuerySelect<T> orWhere(String column, Object value) {
        return baseWhere(column, value, "OR", "=");
    }

    public QuerySelect<T> notWhere(String column, Object value) {
        return baseWhere(column, value, "AND", "<>");
    }

    public QuerySelect<T> orNotWhere(String column, Object value) {
        return baseWhere(column, value, "OR", "<>");
    }

    public QuerySelect<T> andLike(String column, Object value) { return baseWhere(column, value, "AND", "LIKE"); }

    public QuerySelect<T> andNotLike(String column, Object value) { return baseWhere(column, value, "AND", "NOT LIKE"); }

    public QuerySelect<T> orLike(String column, Object value) { return baseWhere(column, value, "OR", "LIKE"); }

    public QuerySelect<T> orNotLike(String column, Object value) { return baseWhere(column, value, "OR", "NOT LIKE"); }

    public T first() throws SQLException {
        List<T> res = limit(1).execute();
        T ret = null;
        try {
            ret = (res.isEmpty()) ? type.getDeclaredConstructor().newInstance() : res.get(0);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        res.clear();

        return ret;
    }

    public List<T> execute() throws SQLException {
        setConnection(true);
        List<T> ret;

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

            try (ResultSet res = statement.executeQuery()) {
                ret = convert(res);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        setConnection(false);
        return ret;
    }

    private QuerySelect<T> limit(int limit) {
        query.append(" LIMIT ").append(limit);
        return this;
    }

    private List<T> convert(ResultSet res) {
        List<T> resultList = new ArrayList<>();
        try {
            while (res.next()) {
                T instance = type.getDeclaredConstructor().newInstance();
                instance.loadFromResultSet(res);
                resultList.add(instance);
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException | SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
}
