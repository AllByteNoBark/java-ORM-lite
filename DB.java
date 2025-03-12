public abstract class DB {

	public DB() {}

	public static <T extends BaseBean> QuerySelect<T> get(Class<T> type) {
        return new QuerySelect<T>(type);
    }

    public static <T extends BaseBean> QueryInsert<T> insert(Class<T> type) {
        return new QueryInsert<T>(type);
    }

    public static <T extends BaseBean> QueryDelete<T> delete(Class<T> type) { return new QueryDelete<T>(type); }

    public static <T extends BaseBean> QueryUpdate<T> update(Class<T> type) { return new QueryUpdate<T>(type); }
}
