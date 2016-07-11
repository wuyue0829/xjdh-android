package com.chinatelecom.xjdh.ui;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import com.chinatelecom.xjdh.R;
import com.chinatelecom.xjdh.utils.T;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.widget.TextView;

@EActivity(R.layout.activity_readnfc)
public class ReadNfcNumber extends BaseActivity {

	@ViewById(R.id.tvNumber)
	TextView tvNumber;

	private NfcAdapter mNfcAdapter;

	@AfterViews
	public void ShowView() {

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

		if (mNfcAdapter == null) {
			// Stop here, we definitely need NFC
			T.showLong(this, "您的手机不支持NFC功能");
			return;

		}

		if (!mNfcAdapter.isEnabled()) {
			T.showLong(this, "您手机的NFC功能被禁用,请手工启用后重试");
		}
		handleIntent(getIntent());

	}

	@Override
	protected void onResume() {
		super.onResume();

		/*
		 * It's important, that the activity is in the foreground (resumed).
		 * Otherwise an IllegalStateException is thrown.
		 */
		if(mNfcAdapter != null)
		setupForegroundDispatch(this, mNfcAdapter);
	}

	@Override
	protected void onPause() {
		/*
		 * Call this before onPause, otherwise an IllegalArgumentException is
		 * thrown as well.
		 */
		if(mNfcAdapter != null)
		stopForegroundDispatch(this, mNfcAdapter);

		super.onPause();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		/*
		 * This method gets called, when a new Intent gets associated with the
		 * current activity instance. Instead of creating a new activity,
		 * onNewIntent will be called. For more information have a look at the
		 * documentation.
		 *
		 * In our case this method gets called, when the user attaches a Tag to
		 * the device.
		 */
		handleIntent(intent);
	}


	private void handleIntent(Intent intent) {
		String action = intent.getAction();

		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			byte[] idArr = tag.getId();
			if(idArr.length == 8)
			{
				int number = idArr[5] * 16 * 16 + idArr[6] * 16 + idArr[7];
				ShowNumber(number);
			}
		}
	}

	@UiThread
	public void ShowNumber(int number)
	{
		tvNumber.setText(Integer.valueOf(number).toString());
	}
	/**
	 * @param activity
	 *            The corresponding {@link Activity} requesting the foreground
	 *            dispatch.
	 * @param adapter
	 *            The {@link NfcAdapter} used for the foreground dispatch.
	 */
	public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

		adapter.enableForegroundDispatch(activity, pendingIntent, null, null);// Capture
																				// All
																				// Tag
	}

	public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
		adapter.disableForegroundDispatch(activity);
	}

}
