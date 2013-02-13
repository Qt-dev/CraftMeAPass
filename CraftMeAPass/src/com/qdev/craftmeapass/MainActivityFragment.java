/*
 * 
 */
package com.qdev.craftmeapass;

import java.util.Random;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qdev.craftmeapass.R;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivityFragment.
 */
public class MainActivityFragment extends Fragment{
	
	/** The TextInputs. */
	private EditText textInputWho = null, textInputWhere = null, textInputKey = null;
	
	/** The Buttons. */
	private Button templateButton = null, craftButton = null, sizeButton = null;
	
	/** The funny sentence. */
	private TextView funnySentence = null;

	/** The mobile options bar. */
	private LinearLayout mobileOptionsBar = null;

	
	/**
	 * The listener interface for receiving onButton events.
	 * The class that is interested in processing a onButton
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnButtonListener<code> method. When
	 * the onButton event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnButtonEvent
	 */
	public interface OnButtonListener{
		
		/**
		 * On craft click.
		 *
		 * @param Who, Where, Key, and Salt
		 */
		public void onCraftClick(String Who, String Where, String Key);
		
		/**
		 * On Settings Click
		 */
		public void onTemplateClick();
		
		/**
		 * On how to click.
		 */
		public void onLengthClick();
	}
	
	/** The m listener. */
	private OnButtonListener mListener = null;
	
	/**
	 * Sets the on button listener.
	 *
	 * @param listener the new on button listener
	 */
	public void setOnButtonListener(OnButtonListener listener){
		mListener = listener;
	}
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setRetainInstance(true);
	}
	
	
	//Listener Initialization onAttach
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		try {
			setOnButtonListener((OnButtonListener) activity);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnButtonListener");
		}
	}
	
	
	
	
	

	
	//Creation with the options as arguments
	/**
	 * New instance.
	 *
	 * @param basicOptions the basic options
	 * @param basicSize the basic size
	 * @param settingsViewOn the settings view on
	 * @return the main activity fragment
	 */
	public static MainActivityFragment newInstance(String template, String size){
		MainActivityFragment f = new MainActivityFragment();
		
		
		//FOR FUTURE USE
		Bundle args = new Bundle();
		args.putString("Template",template);
		args.putString("Size", size);
		f.setArguments(args);
		return f;
	}
	 
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		final View mainView = inflater.inflate(R.layout.main_activity, container, false);
		
		craftButton = (Button) mainView.findViewById(R.id.craft_button);
		templateButton = (Button) mainView.findViewById(R.id.template_button);
		sizeButton = (Button) mainView.findViewById(R.id.size_button);

		textInputWho = (EditText) mainView.findViewById(R.id.who);
		textInputWhere = (EditText) mainView.findViewById(R.id.where);
		textInputKey = (EditText) mainView.findViewById(R.id.key);
		
		mobileOptionsBar = (LinearLayout) mainView.findViewById(R.id.mobile_option_bar);
		setSettingsButtonOn();
		
		funnySentence = (TextView) mainView.findViewById(R.id.main_funny_sentence);
		Resources res = getResources();
		Random random = new Random();
		String[] sentencesList = res.getStringArray(R.array.funny_sentences_array_short);
		funnySentence.setText(sentencesList[random.nextInt(sentencesList.length)]);
		
		//use arguments 
		if(getArguments() != null){
			templateButton.setText(getArguments().getString("Template"));
			sizeButton.setText(getArguments().getString("Size"));
		}
		
		
		craftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.onCraftClick(textInputWho.getText().toString(), textInputWhere.getText().toString(), textInputKey.getText().toString());
			}
		});
		
		templateButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.onTemplateClick();
				
			}
		});
		
		sizeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.onLengthClick();
				
			}
		});
		
		return mainView;
	}
	
	
	
	
	
	/**
	 * Sets the settings button on.
	 */
	public void setSettingsButtonOn(){
		mobileOptionsBar.setVisibility(View.VISIBLE);
	}
	
	public void setLeftButtonText(String txt){
		templateButton.setText(txt);
		
	}
	
	public void setRightButtonText(String txt){
		sizeButton.setText(txt);
	}
	
	
	//***** Parameters Setters/Getters
	/**
	 * Sets the fields.
	 *
	 * @param Fields the new fields
	 */
	public void setFields(String[] Fields){
		textInputWho.setText(Fields[0]);
		textInputWhere.setText(Fields[1]);
		textInputKey.setText(Fields[2]);
	}


	/**
	 * Gets the fields.
	 *
	 * @return the fields
	 */
	public String[] getFields(){
		String[] fields = new String[3];
		fields[0] = textInputWho.getText().toString();
		fields[1] = textInputWhere.getText().toString();
		fields[2] = textInputKey.getText().toString();
		return fields;
	}
}