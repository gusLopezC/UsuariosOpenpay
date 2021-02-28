package com.guroh.jmascamargousuarios.OpenPay;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.guroh.jmascamargousuarios.R;

import mx.openpay.android.Openpay;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class DeviceIdFragment extends Fragment {

  /**
   * Fragment initialization. We want to be retained.
   */
  @Override
  public void onCreate (final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    this.setRetainInstance(true);
  }


  /**
   * Handle the checkout
   *
   */
	public String getDeviceId() {
		Openpay openpay = ((OpenPayAppExample) this.getActivity().getApplication()).getOpenpay();
		String deviceIdString = openpay.getDeviceCollectorDefaultImpl().setup(this.getActivity());
		if (deviceIdString == null) {
			this.printMsg(openpay.getDeviceCollectorDefaultImpl().getErrorMessage());
		} else {
			this.printMsg(deviceIdString);
		}

		return deviceIdString;

	}


	/*
	 * Debug messages. Send to the view and to the logs.
	 * 
	 * @param message The message to pass to the view and logs
	 */
	private void printMsg(final String msg) {

		if (this.getActivity() != null) {
			this.getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Log.d("AddCardActivity", msg);
					TextView tv = (TextView) DeviceIdFragment.this.getActivity().findViewById(R.id.textView3);
					tv.setText(msg);
				}
			});
		}
	}
}
