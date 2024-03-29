package jemuillot.pkg.Utilities;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public class ExecuteAsRoot {
	@SuppressWarnings("deprecation")
	public static boolean canRunRootCommands() {
		boolean retval = false;
		Process suProcess;

		try {
			suProcess = Runtime.getRuntime().exec("su");

			DataOutputStream os = new DataOutputStream(
					suProcess.getOutputStream());
			DataInputStream osRes = new DataInputStream(
					suProcess.getInputStream());

			if (null != os && null != osRes) {
				// Getting the id of the current user to check if this is root
				os.writeBytes("id\n");
				os.flush();

				String currUid = osRes.readLine();
				boolean exitSu = false;
				if (null == currUid) {
					retval = false;
					exitSu = false;
					Log.d("ROOT", "Can't get root access or denied by user");
				} else if (true == currUid.contains("uid=0")) {
					retval = true;
					exitSu = true;
					Log.d("ROOT", "Root access granted");
				} else {
					retval = false;
					exitSu = true;
					Log.d("ROOT", "Root access rejected: " + currUid);
				}

				if (exitSu) {
					os.writeBytes("exit\n");
					os.flush();
				}
			}
		} catch (Exception e) {
			// Can't get root !
			// Probably broken pipe exception on trying to write to output
			// stream after su failed, meaning that the device is not rooted

			retval = false;
			Log.d("ROOT", "Root access rejected [" + e.getClass().getName()
					+ "] : " + e.getMessage());
		}

		return retval;
	}

	public final static boolean execute(ArrayList<String> commands) {
		boolean retval = false;

		try {

			if (null != commands && commands.size() > 0) {
				Process suProcess = Runtime.getRuntime().exec("su");

				DataOutputStream os = new DataOutputStream(
						suProcess.getOutputStream());

				// Execute commands that require root access
				for (String currCommand : commands) {
					os.writeBytes(currCommand + "\n");
					os.flush();
				}

				os.writeBytes("exit\n");
				os.flush();

				try {
					int suProcessRetval = suProcess.waitFor();
					if (255 != suProcessRetval) {
						// Root access granted
						retval = true;
					} else {
						// Root access denied
						retval = false;
					}
				} catch (Exception ex) {
					Log.e("Error executing root action", ex.toString());
				}
			}
		} catch (IOException ex) {
			Log.w("ROOT", "Can't get root access", ex);
		} catch (SecurityException ex) {
			Log.w("ROOT", "Can't get root access", ex);
		} catch (Exception ex) {
			Log.w("ROOT", "Error executing internal operation", ex);
		}

		return retval;
	}

}
