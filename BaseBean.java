import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	public BaseBean() {}

	public void loadFromResultSet(ResultSet res) throws SQLException {
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String columnName = field.getName();
            if(columnName.equals("serialVersionUID")) continue;

            try {
                if (field.getType() == int.class || field.getType() == Integer.class) {
                    field.set(this, res.getInt(columnName));
                } else if (field.getType() == String.class) {
                    field.set(this, res.getString(columnName));
                } else if (field.getType() == boolean.class || field.getType() == Boolean.class) {
                    field.set(this, res.getBoolean(columnName));
                } else if (field.getType() == double.class || field.getType() == Double.class) {
                    field.set(this, res.getDouble(columnName));
                }
            } catch (IllegalAccessException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public <T extends BaseBean> T belongsTo(Class<T> relatedClass, String foreignKey) {
        try {
            Field field = this.getClass().getDeclaredField(foreignKey);
            field.setAccessible(true);
            Object foreignKeyValue = field.get(this);

            if (foreignKeyValue instanceof Integer) {
                return DB.get(relatedClass).where("id", foreignKeyValue).first();
            }
        } catch (NoSuchFieldException | IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName() + " [ ");

        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals("serialVersionUID")) continue;

            try {
                Object value = field.get(this);
                if(value == null) continue;
                sb.append(field.getName()).append(" = ").append(value).append(", ");
            } catch (IllegalAccessException e) {
                sb.append(field.getName()).append(" = <>, ");
            }
        }

        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        sb.append(" ]");
        return sb.toString();
    }
}
