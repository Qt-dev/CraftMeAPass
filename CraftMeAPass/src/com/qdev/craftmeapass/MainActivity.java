/*
 * 
 */
package com.qdev.craftmeapass;


import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.qdev.craftmeapass.HowToFragment.OnCloseListener;
import com.qdev.craftmeapass.MainActivityFragment.OnButtonListener;
import com.qdev.craftmeapass.PassTypeDialog.OnPassTypeClickListener;
import com.qdev.craftmeapass.ResultFragment.ResultListener;
import com.qdev.craftmeapass.SizeDialog.OnSizeClickListener;
import com.qdev.craftmeapass.R;


/**
 * The Class MainActivity.
 *
 * @author QT
 */
@SuppressLint("NewApi")
public class MainActivity extends SherlockFragmentActivity implements ResultListener, OnButtonListener, OnCloseListener, OnPassTypeClickListener, OnSizeClickListener{
	

	/** The preferences 	*/
	private SharedPreferences preferences;
	
	/** The Fields. */
	private String[] Fields = null;
	
	/** Salt for the conversion	 */
	private String salt = "yupyup";
	
	/** The Main f. */
	private MainActivityFragment MainF = null;
	
	/** The how to layer. */
	private RelativeLayout howToLayer = null;
	
	/** The how to button. */
	private MenuItem howToButton = null;
	
	/** The layout. */
	private int Layout;
	
	/** The Fragment Manager. */
	private FragmentManager fm;
	
	/** The how to id. */
	private int howToId = 0;
	private boolean howTo = false;
	
	private PassTypes passTemplate;
	
	private String templateName;
	
	
	ProgressBar progressBar;
	ProgressDialog progDialog;
	private Handler progressBarHandler = new Handler();
	
	//Match preferences on changed
	public OnSharedPreferenceChangeListener prefChanceListener = new OnSharedPreferenceChangeListener() {
		
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
				String key) {
			setTemplateName();

		}
	};
	

	
	/**
	 * OnButton Listener for the Main Activity Fragment.
	 *
	 * @param Who, Where, Key
	 */
	@Override
	public void onCraftClick(String who, String where, String key) {
		String text = (String) who+where+key+salt;
		
		//Chances the case according to preferences
		if(preferences.getBoolean("Input to lowercase", true)){
			text = text.toLowerCase();
		}
			
		//TODO : To switch to that String text = salt+who+key+where;
		
		EncryptionThread thread = new EncryptionThread(text, preferences, getResources(), getOptions(), handler);
		progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setMessage(getResources().getString(R.string.crafting));
		progDialog.setCancelable(false);
		progDialog.show();
		thread.start();
		
		
	
		
		
	}
	
	/**
	 * onSettings Listener
	 */
	public void onTemplateClick(){
		PassTypeDialog passType = new PassTypeDialog();
		passType.show(fm, "Pass Type Dialog");
	}
	
	
	//HowTo Button Listener
	/* (non-Javadoc)
	 * @see com.qdev.craftmeapass.MainActivityFragment.OnButtonListener#onHowToClick()
	 */
	@Override
	public void onLengthClick() {
		SizeDialog sizeDialog = new SizeDialog();
		sizeDialog.show(fm, "Size Dialog");
	}
	
	//CopyListener for the ResultFragment (only in Tablets)
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
	
	/* (non-Javadoc)
	 * @see com.qdev.craftmeapass.HowToFragment.OnCloseListener#Close()
	 */
	@Override
	public void closeHowTo() {
		howToLayer.setVisibility(View.INVISIBLE);
		FragmentTransaction ft = fm.beginTransaction();
		HowToFragment f = (HowToFragment) fm.findFragmentById(R.id.howto_fragment_layer);
		ft.remove(f);
		ft.commit();
		this.setTitle(getResources().getString(R.string.app_name));
		howTo = true;
	}
	
	/* (non-Javadoc)
	 * @see com.qdev.craftmeapass.HowToFragment.OnCloseListener#setHowToId(int)
	 */
	public void setHowToId(int id){
			howToId = id;
	}
	
	public void setSize(int position){
		SharedPreferences.Editor pe = preferences.edit();
		pe.putString(getResources().getString(R.string.pass_length), getResources().getStringArray(R.array.common_sizes)[position]);
		pe.commit();
	}
	/* (non-Javadoc)
	 * @see com.qdev.craftmeapass.ResultFragment.ResultListener#getFont(java.lang.String)
	 */
	@Override
	public Typeface getFont(String path) {
		return Typeface.createFromAsset(getAssets(),path);
	}
	
	@Override
	public void setType(int position) {
		passTemplate.setType(position);
	}
	
	
	
	
	//Overrides of the original Methods
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		howToId=savedInstanceState.getInt("id");
		Fields=savedInstanceState.getStringArray("fields");
	}
		
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.w("Pouet","Start");
		super.onCreate(savedInstanceState);
		// 	Closes the Activity if EXIT is set as an argument
		if (getIntent().getBooleanExtra("EXIT", false)) {
		    finish();
		}
		int a = 0;
		
		// Sets Preference, layout, ContentView, FragmentManager and the Main Fragment
		PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.settings, false);
		setLayout();
		setContentView(R.layout.main_fragment);
		fm = getSupportFragmentManager();
		MainF = (MainActivityFragment) fm.findFragmentById(R.id.main_fragment);
		howToLayer = (RelativeLayout) findViewById(R.id.howto_fragment_layer);
		
		//sets the preferences
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor pe = preferences.edit();
		pe.clear();
		preferences.registerOnSharedPreferenceChangeListener(prefChanceListener);
		
		//Sets pass types
		passTemplate = new PassTypes(getResources(), preferences);
		
		// Starts the onCreate things 
		FragmentTransaction ft = fm.beginTransaction();
		
		//if MainFrame is null, make a fragment and add it		
		if(MainF == null){
			String length = getResources().getString(R.string.main_length_button, "")+preferences.getString(getResources().getStringArray(R.array.settings_list)[5], "8");
			MainF = MainActivityFragment.newInstance(templateName,length);
			ft.add(R.id.main_fragment,MainF);
		}
		
		
		ft.commit();
		
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	public void onResume(){
		super.onResume();
		FragmentManager fm = getSupportFragmentManager();
		if( Fields != null){
			MainF.setFields(Fields);
		}
		setTemplateName();
		if(howToId != 0){
			showHowTo();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
	 */
	@Override
	public void onPause(){
		Fields=getFields();
		super.onPause();
	}
	
	public void onStop(){
		super.onStop();
	}
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState){
		savedInstanceState.putInt("id", howToId);
		savedInstanceState.putStringArray("fields", Fields);
		super.onSaveInstanceState(savedInstanceState);
	}
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {               
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        howToButton = menu.findItem(R.id.menu_howto_button);
        howToButton.setVisible(true);

        return super.onCreateOptionsMenu(menu);
    }
	
	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		 if (item.getItemId() == R.id.menu_howto_button) {
			showHowTo();
			return true;
		} else if (item.getItemId() == R.id.menu_clear_clipboard) {
			ClipboardManager clipBoard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipBoard.setText("");
			return true;
		} else if (item.getItemId() == R.id.menu_settings) {
			launchSettings();
			return true;
		} else if (item.getItemId() == R.id.menu_quit) {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("EXIT", true);
			startActivity(intent);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed(){
		if(howTo != false){
			closeHowTo();
		}else{
			finish();
		}
	}
	
	
	
	//***** Setters & Getters from MainActivityFragment
	/**
	 * Sets the options.
	 *
	 * @param options the new options
	 */
	public void setOptions(int options) {
		SharedPreferences.Editor pe = preferences.edit();
		switch(options){
		case 0 : pe.putBoolean("space", true) ; pe.putBoolean("punctuation", true);break;
		case 1 : pe.putBoolean("space", false) ; pe.putBoolean("punctuation", true);break;
		case 2 : pe.putBoolean("space", true) ; pe.putBoolean("punctuation", false);break;
		case 3 : pe.putBoolean("space", false) ; pe.putBoolean("punctuation", false);break;
		}
		pe.commit();
	}
	
	/**
	 * Gets the options.
	 *
	 * @return the options
	 */
	public int getOptions(){
		boolean punctuation = preferences.getBoolean("Punctuation", true);
		boolean space = preferences.getBoolean("Space", true);
		
		if(space && punctuation){
			return 0;
		}else if(space){
			return 2;
		}else if(punctuation){
			return 1;
		}else{
			return 3;
		}
	}
	
	/**
	 * Gets the fields.
	 *
	 * @return the fields
	 */
	private String[] getFields(){
		return MainF.getFields();
	}
	

	
	/**
	 * Sets the Layout as a number (0 = portrait mobile, 1 = landscape mobile, 2 = tablet)
	 */
	private void setLayout(){
		if(android.os.Build.VERSION.SDK_INT < 13){
			if(getResources().getConfiguration().orientation == 1 ){
				this.Layout=0;
			}else{
				this.Layout=1;
			}
			
		}else{
			if(getResources().getConfiguration().orientation == 1 && getResources().getConfiguration().smallestScreenWidthDp < 700){
				//portrait mobile
				this.Layout =  0;
			}else if(getResources().getConfiguration().orientation == 2 && getResources().getConfiguration().smallestScreenWidthDp < 700){
				//landscape mobile
				this.Layout = 1;
			}else{
				//tablets
				this.Layout = 2;
			}
		}
		
	}
	
		/**
	 * Launch settings.
	 */
	private void launchSettings(){
		Intent showSettings = new Intent(getApplicationContext(), SettingsActivity.class);
		startActivity(showSettings);
	}

	/**
	 * Show how to.
	 */
	private void showHowTo(){
		FragmentTransaction ft = fm.beginTransaction();
		
		//Enables the dark layout
		howToLayer.setVisibility(View.VISIBLE);
		if(fm.findFragmentById(R.id.howto_fragment_layer)!=null){
			//remove if already present
			ft.remove(fm.findFragmentById(R.id.howto_fragment_layer));
		}
		ft.add(R.id.howto_fragment_layer, HowToFragment.newInstance(Layout, howToId));
		ft.commit();
		howTo = true;
	}

	
	private void setTemplateName(){
		Resources res = getResources();
		String[] settingsList = res.getStringArray(R.array.settings_list);
		int[] paramsInInt = new int[2];
		int paramPower = 1;
		
		// added the -1 to not count the length
		for (int i = 0; i < settingsList.length-1; i++, paramPower = paramPower*2) {
			try {
				System.out.println(settingsList[i]+" = "+preferences.getString(settingsList[i], "pouet"));
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (preferences.getBoolean(settingsList[i], true)){
				paramsInInt[0] = paramsInInt[0] + paramPower;
			}
		}
		
		String length = preferences.getString(res.getStringArray(R.array.settings_list)[5], "8");
		paramsInInt[1] = Integer.parseInt(length);
		
		
		templateName = passTemplate.getName(paramsInInt);
		if(templateName.length()<10 || Layout != 0){
			templateName = "Type: "+templateName;
		}
		
		String size= "Length: "+paramsInInt[1];
		
		if(MainF != null){
			MainF.setLeftButtonText(templateName);
			MainF.setRightButtonText(size);
		}
	}


	final Handler handler = new Handler() {
		public void handleMessage(Message msg){
			String pass = msg.getData().getString("text");
			progDialog.dismiss();
			
			if(Layout!=2){
				Intent showPass = new Intent(getApplicationContext(), ResultActivity.class);
				showPass.putExtra("pass", pass);
				startActivity(showPass);
			}else {
				FragmentTransaction ft = fm.beginTransaction();
				if(fm.findFragmentById(R.id.result_fragment) != null){
					ft.replace(R.id.result_fragment, ResultFragment.newInstance(pass)).addToBackStack("main");
				}else{
					ft.add(R.id.result_fragment, ResultFragment.newInstance(pass));
				}
				ft.commit();
			}
		}
	};


}