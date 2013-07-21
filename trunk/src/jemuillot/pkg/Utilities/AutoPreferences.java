package jemuillot.pkg.Utilities;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AutoPreferences {
	protected String filename;
	protected Context context;

	public AutoPreferences(Context context, final String filename) {
		this.context = context;
		this.filename = filename;
	}

	final public void save() {
		SharedPreferences.Editor editor = context.getSharedPreferences(
				filename, Context.MODE_MULTI_PROCESS).edit();

		onSave(editor);

		editor.commit();
	}

	protected void onSave(Editor editor) {

		Field[] fields = getClass().getFields();

		try {

			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];

				if (Modifier.isFinal(f.getModifiers())) {
					continue;
				}

				Class<?> clz = f.getType();

				if (clz == boolean.class) {
					editor.putBoolean(f.getName(), f.getBoolean(this));
				} else if (clz == int.class) {
					editor.putInt(f.getName(), f.getInt(this));
				} else if (clz == float.class) {
					editor.putFloat(f.getName(), f.getFloat(this));
				} else if (clz == long.class) {
					editor.putLong(f.getName(), f.getLong(this));
				} else if (clz == String.class) {
					editor.putString(f.getName(), (String) f.get(this));
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	final public void load() {
		SharedPreferences prefs = context.getSharedPreferences(filename,
				Context.MODE_MULTI_PROCESS);

		onLoad(prefs);
	}

	protected void onLoad(SharedPreferences prefs) {

		Field[] fields = getClass().getFields();

		try {

			for (int i = 0; i < fields.length; i++) {
				Field f = fields[i];

				if (Modifier.isFinal(f.getModifiers())) {
					continue;
				}

				Class<?> clz = f.getType();

				if (clz == boolean.class) {
					f.setBoolean(this,
							prefs.getBoolean(f.getName(), f.getBoolean(this)));
				} else if (clz == int.class) {
					f.setInt(this, prefs.getInt(f.getName(), f.getInt(this)));
				} else if (clz == float.class) {
					f.setFloat(this,
							prefs.getFloat(f.getName(), f.getFloat(this)));
				} else if (clz == long.class) {
					f.setLong(this, prefs.getLong(f.getName(), f.getLong(this)));
				} else if (clz == String.class) {
					f.set(this,
							prefs.getString(f.getName(), (String) f.get(this)));
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

	}

}
