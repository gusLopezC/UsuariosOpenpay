package com.guroh.jmascamargousuarios.OpenPay;

import android.app.Application;

import mx.openpay.android.Openpay;

public class OpenPayAppExample extends Application {

	private final Openpay openpay;
	
	public OpenPayAppExample() {

		this.openpay = new Openpay("mvdjqqvg8suo1lnrqnhu", "pk_0abb0b431d8e4ef38017e5345535362d", false);
		//this.openpay = new Openpay("mi93pk0cjumoraf08tqt", "pk_92e31f7c77424179b7cd451d21fbb771", false);
	}

	public Openpay getOpenpay() {
		return this.openpay;
	}

}
