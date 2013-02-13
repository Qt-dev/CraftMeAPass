/*
 * 
 */
package com.qdev.craftmeapass;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.ClipboardManager;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.qdev.craftmeapass.ResultFragment.ResultListener;
import com.qdev.craftmeapass.R;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultActivity.
 */
public class ResultActivity extends SherlockFragmentActivity implements ResultListener{
	
	ResultFragment result;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		String pass = intent.getStringExtra("pass");
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.result_fragment);
		
		if(fragment == null){
			FragmentTransaction ft = fm.beginTransaction();
			ft.add(R.id.result_fragment,ResultFragment.newInstance(pass));
			ft.commit();
		}
		
		result = (ResultFragment) fm.findFragmentById(R.id.result_fragment);
		setContentView(R.layout.result_fragment);
	}
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {               
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null; 
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		} else if (item.getItemId() == R.id.menu_clear_clipboard) {
			ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipBoard.setText("");
			return true;
		} else if (item.getItemId() == R.id.menu_settings) {
			intent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(intent);
			return true;
		} else if (item.getItemId() == R.id.menu_quit) {
			intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			startActivity(intent);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
		 
	
	//***** Copy to the clipboard
	/* (non-Javadoc)
	 * @see com.qdev.craftmeapass.ResultFragment.ResultListener#copy(java.lang.String)
	 */
	@Override
	public void copy(String pass) {
		ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipBoard.setText(pass);
		
		//Toast to confirm the copy to the user
		Resources res = getResources();		
		Toast toast = Toast.makeText(getApplicationContext(), res.getString(R.string.copy_toast), Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public void passUpdate(String txt){
		result.passUpdate(txt);
	}
	/* (non-Javadoc)
	 * @see com.qdev.craftmeapass.ResultFragment.ResultListener#getFont(java.lang.String)
	 */
	@Override
	public Typeface getFont(String path) {
		return Typeface.createFromAsset(getAssets(),path);
	}

}
