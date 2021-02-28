package com.guroh.jmascamargousuarios.Pantallas;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.FragmentActivity;

import com.guroh.jmascamargousuarios.OpenPay.DeviceIdFragment;
import com.guroh.jmascamargousuarios.OpenPay.MessageDialogFragment;
import com.guroh.jmascamargousuarios.OpenPay.OpenPayAppExample;
import com.guroh.jmascamargousuarios.OpenPay.ProgressDialogFragment;
import com.guroh.jmascamargousuarios.R;

import mx.openpay.android.Openpay;

import mx.openpay.android.OperationCallBack;
import mx.openpay.android.OperationResult;
import mx.openpay.android.exceptions.OpenpayServiceException;
import mx.openpay.android.exceptions.ServiceUnavailableException;
import mx.openpay.android.model.Card;
import mx.openpay.android.validation.CardValidator;

public class AddCardActivity extends FragmentActivity implements OperationCallBack {

    private ProgressDialogFragment progressFragment;

    private DeviceIdFragment deviceIdFragment;
    private Openpay openpay = null;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_add_card);

        this.progressFragment = ProgressDialogFragment.newInstance(R.string.progress_message);

        FragmentManager fm = this.getFragmentManager();
        this.deviceIdFragment = (DeviceIdFragment) fm.findFragmentByTag("DeviceCollector");
        // If not retained (or first time running), we need to create it.
        if (this.deviceIdFragment == null) {
            this.deviceIdFragment = new DeviceIdFragment();
            fm.beginTransaction().add(this.deviceIdFragment, "DeviceCollector").commit();
        }
    }

    public void saveCard(final View view) {
        this.addToken();
    }

    public String getDeviceId(final View view) {
        return this.deviceIdFragment.getDeviceId();
    }

    private void addToken() {

        this.openpay = new Openpay("mvdjqqvg8suo1lnrqnhu", "pk_0abb0b431d8e4ef38017e5345535362d", false);
        Card card = new Card();
        boolean isValid = true;

        final EditText holderNameEt = ((EditText) this.findViewById(R.id.holder_name));
        final String holderName = holderNameEt.getText().toString();
        card.holderName(holderName);
        if (!CardValidator.validateHolderName(holderName)) {
            holderNameEt.setError(this.getString(R.string.invalid_holder_name));
            isValid = false;
        }

        final EditText cardNumberEt = ((EditText) this.findViewById(R.id.card_number));
        final String cardNumber = cardNumberEt.getText().toString();
        card.cardNumber(cardNumber);
        if (!CardValidator.validateNumber(cardNumber)) {
            cardNumberEt.setError(this.getString(R.string.invalid_card_number));
            isValid = false;
        }

        EditText cvv2Et = ((EditText) this.findViewById(R.id.cvv2));
        String cvv = cvv2Et.getText().toString();
        card.cvv2(cvv);
        if (!CardValidator.validateCVV(cvv, cardNumber)) {
            cvv2Et.setError(this.getString(R.string.invalid_cvv));
            isValid = false;
        }

        Integer year = this.getInteger(((Spinner) this.findViewById(R.id.year_spinner)).getSelectedItem().toString());

        Integer month = this.getInteger(((Spinner) this.findViewById(R.id.month_spinner)).getSelectedItem().toString());

        if (!CardValidator.validateExpiryDate(month, year)) {
            DialogFragment fragment = MessageDialogFragment.newInstance(R.string.error,
                    this.getString(R.string.invalid_expire_date));
            fragment.show(this.getFragmentManager(), "Error");
            isValid = false;
        }

        card.expirationMonth(month);
        card.expirationYear(year);

        if (isValid) {
            this.progressFragment.show(this.getSupportFragmentManager(), "progress");
            openpay.createToken(card, this);
        }

    }

    @Override
    public void onError(final OpenpayServiceException error) {
        error.printStackTrace();
        this.progressFragment.dismiss();
        int desc = 0;
        String msg = null;
        switch (error.getErrorCode()) {
            case 3001:
                desc = R.string.declined;
                msg = this.getString(desc);
                break;
            case 3002:
                desc = R.string.expired;
                msg = this.getString(desc);
                break;
            case 3003:
                desc = R.string.insufficient_funds;
                msg = this.getString(desc);
                break;
            case 3004:
                desc = R.string.stolen_card;
                msg = this.getString(desc);
                break;
            case 3005:
                desc = R.string.suspected_fraud;
                msg = this.getString(desc);
                break;

            case 2002:
                desc = R.string.already_exists;
                msg = this.getString(desc);
                break;
            default:
                desc = R.string.error_creating_card;
                msg = error.getDescription();
        }

        DialogFragment fragment = MessageDialogFragment.newInstance(R.string.error, msg);
        fragment.show(this.getFragmentManager(), "Error");
    }

    @Override
    public void onCommunicationError(final ServiceUnavailableException error) {
        error.printStackTrace();
        this.progressFragment.dismiss();
        DialogFragment fragment = MessageDialogFragment.newInstance(R.string.error,
                this.getString(R.string.communication_error));
        fragment.show(this.getFragmentManager(), "Error");
    }

    @Override
    public void onSuccess(final OperationResult result) {
        this.progressFragment.dismiss();
        this.clearData();
        DialogFragment fragment = MessageDialogFragment.newInstance(R.string.card_added,
                this.getString(R.string.card_created));
        fragment.show(this.getFragmentManager(), this.getString(R.string.info));
    }

    private void clearData() {
        ((EditText) this.findViewById(R.id.holder_name)).setText("");
        ((EditText) this.findViewById(R.id.card_number)).setText("");
        ((EditText) this.findViewById(R.id.cvv2)).setText("");
        ((Spinner) this.findViewById(R.id.year_spinner)).setSelection(0);
        ((Spinner) this.findViewById(R.id.month_spinner)).setSelection(0);

    }

    private Integer getInteger(final String number) {
        try {
            return Integer.valueOf(number);
        } catch (NumberFormatException nfe) {
            return 0;
        }
    }

}