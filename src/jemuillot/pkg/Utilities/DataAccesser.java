package jemuillot.pkg.Utilities;

public interface DataAccesser {

	public boolean open();

	public boolean close();

	public Object get(final String key, Class type, Object def);

	public boolean put(final String key, Class type, Object val);

}
