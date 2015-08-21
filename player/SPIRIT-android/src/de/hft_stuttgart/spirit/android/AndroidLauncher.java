package de.hft_stuttgart.spirit.android;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.qualcomm.vuforia.CameraDevice;
import com.qualcomm.vuforia.DataSet;
import com.qualcomm.vuforia.ImageTargetBuilder;
import com.qualcomm.vuforia.ImageTracker;
import com.qualcomm.vuforia.State;
import com.qualcomm.vuforia.Trackable;
import com.qualcomm.vuforia.TrackableSource;
import com.qualcomm.vuforia.Tracker;
import com.qualcomm.vuforia.TrackerManager;

import de.hft_stuttgart.spirit.SpiritMain;
import de.hft_stuttgart.spirit.SpiritWebviewHandler;
import de.hft_stuttgart.spirit.android.view.Main_Activity;
import de.hft_stuttgart.spirit.android.view.StoryDetails_Activity;

public class AndroidLauncher extends AndroidApplication implements
		VuforiaControl, SpiritWebviewHandler {
	private static final String MIME_TEXT_PLAIN = "text/plain";
	public static final String TAG = "NfcDemo";

	VuforiaSession vuforiaSession;
	DataSet dataSetUserDef = null;
	boolean userDefinedTargetsStarted = false;
	boolean vuforiaReady = false;
	boolean building = false;
	private int startBuildFails = 0;
	int targetBuilderCounter = -1;
	boolean retryStartBuild = false;

	View gameView;
	GeoToolsWrapper geoTools;
	WebView webView;
	RelativeLayout layout;
	RelativeLayout.LayoutParams params;
	
	ImageView imageView;
	RelativeLayout.LayoutParams imageParams;

	private NfcAdapter mNfcAdapter;
	private NfcLibgdxInterface nfcInterface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nfcInterface = new NfcLibgdxInterface();

		// geoTools starten
		geoTools = new GeoToolsWrapper(this);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		// App beenden wenn kein NFC vorhanden

		if (mNfcAdapter == null) {
			Toast.makeText(this, "This device doesn't support NFC.",
					Toast.LENGTH_LONG).show();
			// finish();
			// return;
		}
		// System.out.println("Status NFC: "+mNfcAdapter.isEnabled());

		// libgdx
		startlibgdx();

		// vuforia
		vuforiaSession = new VuforiaSession(this);
		vuforiaSession.initAR(this, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		// handleIntent(getIntent());
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Log.w("SPIRIT","resume auch im launcher! :E");
		geoTools.resume();
		if (mNfcAdapter != null) {
			setupForegroundDispatch(this, mNfcAdapter);
		}
	}

	@Override
	protected void onPause() {
		if (mNfcAdapter != null) {
			stopForegroundDispatch(this, mNfcAdapter);
		}
		geoTools.pause();
		super.onPause();
		// Log.w("SPIRIT","pause auch im launcher! :E");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Log.w("SPIRIT","destroy auch im launcher! :E");
	}

	@Override
	protected void onNewIntent(Intent intent) {
		System.out.println("onNewIntent...");
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		System.out.println("handleIntent...");
		String action = intent.getAction();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

			String type = intent.getType();
			if (MIME_TEXT_PLAIN.equals(type)) {
				Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				new NdefReaderTask().execute(tag);

			} else {
				Log.d(TAG, "Wrong mime type: " + type);
			}
		} else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

			// In case we would still use the Tech Discovered Intent
			Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
			String[] techList = tag.getTechList();
			String searchedTech = Ndef.class.getName();

			for (String tech : techList) {
				if (searchedTech.equals(tech)) {
					new NdefReaderTask().execute(tag);
					break;
				}
			}
		}
	}

	/**
	 * @param activity
	 *            The corresponding {@link Activity} requesting the foreground
	 *            dispatch.
	 * @param adapter
	 *            The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void setupForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(),
				activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(
				activity.getApplicationContext(), 0, intent, 0);

		IntentFilter[] filters = new IntentFilter[1];
		String[][] techList = new String[][] {};

		// Notice that this is the same filter as in our manifest.
		filters[0] = new IntentFilter();
		filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
		filters[0].addCategory(Intent.CATEGORY_DEFAULT);
		try {
			filters[0].addDataType(MIME_TEXT_PLAIN);
		} catch (MalformedMimeTypeException e) {
			throw new RuntimeException("Check your mime type.");
		}

		adapter.enableForegroundDispatch(activity, pendingIntent, filters,
				techList);
	}

	/**
	 * @param activity
	 *            The corresponding {@link BaseActivity} requesting to stop the
	 *            foreground dispatch.
	 * @param adapter
	 *            The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void stopForegroundDispatch(final Activity activity,
			NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

	void startlibgdx() {
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		// initialize(new SpiritMain(new VuforiaLibgdxInterface(this),new
		// SpiritFilmWrapper(this)), config);
		layout = new RelativeLayout(this);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

//		gameView = initializeForView(new SpiritMain(new VuforiaLibgdxInterface(
//				this), new SpiritFilmWrapper(this), geoTools, nfcInterface,
//				this, new ArmlLoader(this, new XmlPullParserHandler())), config);

		// Generate path for xml file
		int storyId = getIntent().getIntExtra(StoryDetails_Activity.EXTRA_STORYID, -1);
		String pathToAppDir = Environment.getExternalStorageDirectory()
				+ "/StorytellAR";
		String storyXMLPath = pathToAppDir + "/Content/" + String.valueOf(storyId) + "/arml.xml";
		
		gameView = initializeForView(new SpiritMain(new VuforiaLibgdxInterface(
				this), new SpiritFilmWrapper(this), geoTools, nfcInterface,
				this, new ArmlLoader(this, new XmlPullParserHandler()), storyXMLPath), config);
		gameView.setId(123);

		// RelativeLayout.LayoutParams params = new
		// RelativeLayout.LayoutParams(640, 480);
		// layout.addView(gameView,params);

		layout.addView(gameView);

		webView = new WebView(this);
		webView.setWebViewClient(new WebViewClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);
		params = new RelativeLayout.LayoutParams(900, 500);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		setWebviewVisible(false);
		layout.addView(webView, params);
		
		imageView = new ImageView(this);
		imageParams = new RelativeLayout.LayoutParams(900, 500);
		imageParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		imageView.setVisibility(View.INVISIBLE);
		layout.addView(imageView,imageParams);

		setContentView(layout);
	}

	@Override
	public boolean doInitTrackers() {
		// Indicate if the trackers were initialized correctly
		boolean result = true;

		// Initialize the image tracker:
		TrackerManager trackerManager = TrackerManager.getInstance();
		Tracker tracker = trackerManager.initTracker(ImageTracker
				.getClassType());
		if (tracker == null) {
			Log.d("SPIRIT", "Failed to initialize ImageTracker.");
			result = false;
		} else {
			Log.d("SPIRIT", "Successfully initialized ImageTracker.");
		}

		return result;

	}

	@Override
	public boolean doLoadTrackersData() {
		TrackerManager trackerManager = TrackerManager.getInstance();
		ImageTracker imageTracker = (ImageTracker) trackerManager
				.getTracker(ImageTracker.getClassType());
		if (imageTracker == null) {
			Log.d("SPIRIT",
					"Failed to load tracking data set because the ImageTracker has not been initialized.");
			return false;
		}

		// Create the data set:
		dataSetUserDef = imageTracker.createDataSet();
		if (dataSetUserDef == null) {
			Log.d("SPIRIT", "Failed to create a new tracking data.");
			return false;
		}

		if (!imageTracker.activateDataSet(dataSetUserDef)) {
			Log.d("SPIRIT", "Failed to activate data set.");
			return false;
		}

		Log.d("SPIRIT", "Successfully loaded and activated data set.");
		return true;
	}

	@Override
	public boolean doStartTrackers() {
		Tracker imageTracker = TrackerManager.getInstance().getTracker(
				ImageTracker.getClassType());
		if (imageTracker != null)
			imageTracker.start();
		return true;
	}

	@Override
	public boolean doStopTrackers() {
		Tracker imageTracker = TrackerManager.getInstance().getTracker(
				ImageTracker.getClassType());
		if (imageTracker != null)
			imageTracker.stop();

		return true;
	}

	@Override
	public boolean doUnloadTrackersData() {
		// Indicate if the trackers were unloaded correctly
		boolean result = true;

		// Get the image tracker:
		TrackerManager trackerManager = TrackerManager.getInstance();
		ImageTracker imageTracker = (ImageTracker) trackerManager
				.getTracker(ImageTracker.getClassType());
		if (imageTracker == null) {
			result = false;
			Log.d("SPIRIT",
					"Failed to destroy the tracking data set because the ImageTracker has not been initialized.");
		} else

		{
			if (dataSetUserDef != null) {
				if (imageTracker.getActiveDataSet() != null
						&& !imageTracker.deactivateDataSet(dataSetUserDef)) {
					Log.d("SPIRIT",
							"Failed to destroy the tracking data set because the data set could not be deactivated.");
					result = false;
				}

				if (!imageTracker.destroyDataSet(dataSetUserDef)) {
					Log.d("SPIRIT", "Failed to destroy the tracking data set.");
					result = false;
				}

				Log.d("SPIRIT", "Successfully destroyed the data set.");
				dataSetUserDef = null;
			}
		}
		return result;
	}

	@Override
	public boolean doDeinitTrackers() {
		TrackerManager tManager = TrackerManager.getInstance();
		tManager.deinitTracker(ImageTracker.getClassType());
		return true;
	}

	@Override
	public void onInitARDone(VuforiaException e) {
		if (e == null) {
			userDefinedTargetsStarted = startUserDefinedTargets();

			try {
				vuforiaSession.startAR(CameraDevice.CAMERA.CAMERA_DEFAULT);
			} catch (VuforiaException e1) {
				// konnte kamera nicht starten...
				Log.w("SPIRIT", "konnte Kamera nicht starten");
				e1.printStackTrace();
			}

			CameraDevice.getInstance().setFocusMode(
					CameraDevice.FOCUS_MODE.FOCUS_MODE_CONTINUOUSAUTO);
		} else {
			// starten nicht erfolgreich
			Log.w("SPIRIT", "Vuforia konnte nicht gestartet werden");
			e.printStackTrace();
			finish();
		}
		vuforiaReady = true;
	}

	boolean startUserDefinedTargets() {
		Log.d("SPIRIT", "startUserDefinedTargets");

		TrackerManager trackerManager = TrackerManager.getInstance();
		ImageTracker imageTracker = (ImageTracker) (trackerManager
				.getTracker(ImageTracker.getClassType()));
		if (imageTracker != null) {
			ImageTargetBuilder targetBuilder = imageTracker
					.getImageTargetBuilder();

			if (targetBuilder != null) {
				// if needed, stop the target builder
				if (targetBuilder.getFrameQuality() != ImageTargetBuilder.FRAME_QUALITY.FRAME_QUALITY_NONE)
					targetBuilder.stopScan();

				imageTracker.stop();

				targetBuilder.startScan();

			}
		} else
			return false;

		return true;
	}

	@Override
	public void onQCARUpdate(State state) {
		TrackerManager trackerManager = TrackerManager.getInstance();
		ImageTracker imageTracker = (ImageTracker) trackerManager
				.getTracker(ImageTracker.getClassType());
		ImageTargetBuilder targetBuilder = imageTracker.getImageTargetBuilder();

		if (building) {
			TrackableSource newTrackableSource = targetBuilder
					.getTrackableSource();

			if (newTrackableSource != null) {
				if (dataSetUserDef.getNumTrackables() > 0) {
					imageTracker.stop();
					imageTracker.resetExtendedTracking();
					imageTracker.start(); // Restart the tracker
				}

				Log.d("SPIRIT",
						"Attempting to transfer the trackable source to the dataset");

				// Deactivate current dataset
				imageTracker.deactivateDataSet(imageTracker.getActiveDataSet());

				// nur 1 ziel erwünscht, daher alle anderen löschen
				while (dataSetUserDef.getNumTrackables() > 0) {
					dataSetUserDef.getTrackable(0).stopExtendedTracking();
					dataSetUserDef.destroy(dataSetUserDef.getTrackable(0));
				}
				// Add new trackable source
				Trackable trackable = dataSetUserDef
						.createTrackable(newTrackableSource);

				// Reactivate current dataset
				imageTracker.activateDataSet(dataSetUserDef);

				trackable.startExtendedTracking();

			}
			building = false;
		} else if (retryStartBuild) {
			startBuild();
		}
	}

	protected void startBuild() {
		TrackerManager trackerManager = TrackerManager.getInstance();
		ImageTracker imageTracker = (ImageTracker) trackerManager
				.getTracker(ImageTracker.getClassType());

		if (imageTracker != null) {
			ImageTargetBuilder targetBuilder = imageTracker
					.getImageTargetBuilder();
			if (targetBuilder != null) {

				// if (targetBuilder.getFrameQuality() ==
				// ImageTargetBuilder.FRAME_QUALITY.FRAME_QUALITY_LOW)
				// {
				// Log.w("SPIRIT","Low Frame Quality");
				// }

				String name;
				// es wird so lange probiert bis es erfolgreich war, also z.B.
				// Frame Quality gut ist
				// do {
				// name = "UserTarget-" + targetBuilderCounter;
				// Log.d("SPIRIT", "TRYING " + name);
				// targetBuilderCounter++;
				// } while (!targetBuilder.build(name, 320.0f));
				// building = true;
				name = "UserTarget-" + targetBuilderCounter;
				Log.d("SPIRIT", "TRYING " + name);
				targetBuilderCounter++;
				if (targetBuilder.build(name, 320.0f)) {
					retryStartBuild = false;
					building = true;
					startBuildFails = 0;
				} else {
					retryStartBuild = true;
					building = false;
					startBuildFails++;
					if (startBuildFails > 25) {
						startUserDefinedTargets(); // UserDefinedTargets neu
													// starten probieren
						startBuildFails = 0;
					}
				}
			} else {
			}
		}
	}

	/**
	 * Background task for reading the data. Do not block the UI thread while
	 * reading.
	 * 
	 * @author Ralf Wondratschek
	 * 
	 */
	private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

		@Override
		protected String doInBackground(Tag... params) {
			Tag tag = params[0];

			Ndef ndef = Ndef.get(tag);
			if (ndef == null) {
				// NDEF is not supported by this Tag.
				return null;
			}

			NdefMessage ndefMessage = ndef.getCachedNdefMessage();

			NdefRecord[] records = ndefMessage.getRecords();
			System.out.println("records: " + records.length);

			for (NdefRecord ndefRecord : records) {
				System.out.println(ndefRecord.getTnf());
				System.out.println(ndefRecord.toString());
				if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN
						&& Arrays.equals(ndefRecord.getType(),
								NdefRecord.RTD_TEXT)) {
					try {
						System.out.println("try readtext");
						return readText(ndefRecord);
					} catch (UnsupportedEncodingException e) {
						System.out.println("unsupported encoding");
						Log.e(TAG, "Unsupported Encoding", e);
					}
				}
			}

			return null;
		}

		private String readText(NdefRecord record)
				throws UnsupportedEncodingException {
			/*
			 * See NFC forum specification for "Text Record Type Definition" at
			 * 3.2.1
			 * 
			 * http://www.nfc-forum.org/specs/
			 * 
			 * bit_7 defines encoding bit_6 reserved for future use, must be 0
			 * bit_5..0 length of IANA language code
			 */
			System.out.println("readText...");
			byte[] payload = record.getPayload();

			// Get the Text Encoding
			String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8"
					: "UTF-16";

			// Get the Language Code
			int languageCodeLength = payload[0] & 0063;

			// String languageCode = new String(payload, 1, languageCodeLength,
			// "US-ASCII");
			// e.g. "en"

			// Get the Text
			return new String(payload, languageCodeLength + 1, payload.length
					- languageCodeLength - 1, textEncoding);
		}

		@Override
		protected void onPostExecute(String result) {
			if (result != null) {
				System.out.println(result);
				nfcInterface.addNewTag(result);
			}
		}
	}

	@Override
	public void setAlpha(float alpha) {
		final float a = alpha;
		if (webView != null) {
			webView.post(new Runnable() {

				@Override
				public void run() {
					webView.setAlpha(a);

				}
			});
		}
	}

	@Override
	public void openUrl(String url) {
		if (webView != null) {
			final String postUrl = url;
			webView.post(new Runnable() {

				@Override
				public void run() {
					webView.loadUrl(postUrl);
				}
			});
		}
	}

	@Override
	public void setWebviewVisible(boolean visible) {
		final boolean status = visible;
		if (webView != null) {
			webView.post(new Runnable() {

				@Override
				public void run() {
					if (status) {
						webView.setVisibility(View.VISIBLE);
					} else {
						webView.setVisibility(View.INVISIBLE);
					}

				}
			});

		}
	}

	@Override
	public void setWebviewSize(int x, int y) {
		params.height = y;
		params.width = x;
		layout.post(new Runnable() {

			@Override
			public void run() {
				layout.updateViewLayout(webView, params);

			}
		});

	}

	@Override
	public int getWebviewWidth() {
		return params.width;
	}

	@Override
	public int getWebviewHeight() {
		return params.height;
	}

	@Override
	public boolean isWebviewVisible() {
		return (webView.getVisibility() == View.VISIBLE);
	}
	
	@Override
	public void showPicture(String picture) {
		final String pictureFinal = picture;
		imageView.post(new Runnable() {
			
			@Override
			public void run() {				
				imageView.setImageBitmap(BitmapFactory.decodeFile(pictureFinal));
				imageView.setVisibility(View.VISIBLE);
			}
		});
	}

	@Override
	public void hidePicture() {
		imageView.post(new Runnable() {
			
			@Override
			public void run() {
				imageView.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void endStory() {
		Intent i = new Intent(this, Main_Activity.class);
		startActivity(i);
	}
}
