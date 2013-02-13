/**
 * 
 */
package com.qdev.craftmeapass;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * @author QT
 *
 */
public class EncryptionThread extends Thread implements Runnable {
	String text;
	SharedPreferences preferences;
	Resources res;
	int options;
	Handler mHandler;
	
	public EncryptionThread(String s, SharedPreferences sp, Resources r, int i, Handler h){
		text = s;
		preferences = sp;
		res = r;
		options = i;
		mHandler = h;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		if(preferences.getBoolean(res.getString(R.string.use_old_algorithm), false)){
			//Old algorithm
			try {
				text = Converter.encode(text, options, Integer.parseInt(preferences.getString(res.getString(R.string.pass_length), "8")));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}else{
			//New algorithm
			text = Converter.craftPass(text, preferences, res);
		}
		
		Message msg = mHandler.obtainMessage();
		Bundle b = new Bundle();
		b.putString("text", text);
		msg.setData(b);		
		mHandler.sendMessage(msg);


	}

}
