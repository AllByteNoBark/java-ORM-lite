import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryInsert<T extends BaseBean> extends Query{
    private StringBuilder query;
    private ArrayList<Object> args = new ArrayList<>();
    private Class<T> type;
    StringBuilder placeholders = null;

    public QueryInsert(Class<T> clazz) {
        super();
        this.type = clazz;

        StringBuilder columns = new StringBuilder();
        placeholders = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            if (!field.getName().equals("serialVersionUID") && !field.getName().equals("id")) {
                columns.append(field.getName()).append(", ");
                placeholders.append("?, ");
            }
        }

        if (columns.length() > 0) columns.setLength(columns.length() - 2);
        if (placeholders.length() > 0) placeholders.setLength(placeholders.length() - 2);

        query = new StringBuilder("INSERT INTO " + clazz.getSimpleName().toLowerCase() + " (" + columns + ") VALUES");
    }

    public QueryInsert<T> setValues(T object) {
        query.append(" (").append(placeholders).append("),");
        Field[] fields = type.getDeclaredFields();

        for (Field field : fields) {
            if (!field.getName().equals("serialVersionUID") && !field.getName().equals("id")) {
                field.setAccessible(true);
                try {
                    args.add(field.get(object));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    public QueryInsert<T> setValues(T[] objects) {
        for(T obj : objects) {
            setValues(obj);
        }
        return this;
    }

    public void execute() throws SQLException {
        setConnection(true);
        query.setCharAt(query.length() - 1, ';');

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < args.size(); i++) {
                Object arg = args.get(i);
                if (arg.getClass() == String.class) {
                    statement.setString(i + 1, (String) arg);
                } else if (arg.getClass() == Integer.class) {
                    statement.setInt(i + 1, (Integer) arg);
                } else if (arg.getClass() == Boolean.class) {
                    statement.setBoolean(i + 1, (Boolean) arg);
                } else if (arg.getClass() == Double.class) {
                    statement.setDouble(i + 1, (Double) arg);
                }
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setConnection(false);
    }
}
