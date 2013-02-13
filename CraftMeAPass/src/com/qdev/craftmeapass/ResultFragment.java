/*
 * 
 */
package com.qdev.craftmeapass;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.qdev.craftmeapass.R;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultFragment.
 */
public class ResultFragment extends Fragment{
	
	/** The pass. */
	private String pass;
	
	/** The label. */
	private TextView printedPass = null, label = null;
	
	/** The copy button. */
	private Button copyButton = null;
	
	//Copying Listener
	/**
	 * The listener interface for receiving result events.
	 * The class that is interested in processing a result
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addResultListener<code> method. When
	 * the result event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ResultEvent
	 */
	public interface ResultListener{
		
		/**
		 * Gets the font.
		 *
		 * @param path the path
		 * @return the font
		 */
		public Typeface getFont(String path);
		
		/**
		 * Copy.
		 *
		 * @param pass the pass
		 */
		public void copy(String pass);
	}
	
	/** The m listener. */
	private ResultListener mListener = null;
	
	/**
	 * Sets the on result listener.
	 *
	 * @param listener the new on result listener
	 */
	public void setOnResultListener(ResultListener listener){
		mListener = listener;
	}
	
	//Listener Initialization
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		try{
			mListener = (ResultListener) activity;
		}catch (ClassCastException e){
			throw new ClassCastException(activity.toString() + " must implement OnCraftClickListener");
		}
	}
	

	
	
	//Initialization with a Pass as the argument
	/**
	 * New instance.
	 *
	 * @param Pass the pass
	 * @return the result fragment
	 */
	public static ResultFragment newInstance(String Pass){
		ResultFragment f = new ResultFragment();
		
		Bundle args = new Bundle();
		args.putString("pass", Pass);
		f.setArguments(args);
		
		return f;
	}
		
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewer = inflater.inflate(R.layout.result_activity, container, false);
		
		if(getArguments() != null){
			pass = getArguments().getString("pass");
		}
		

		copyButton = (Button) viewer.findViewById(R.id.pass_copy_button);
		printedPass = (TextView) viewer.findViewById(R.id.printed_pass);
		Typeface barkentina = mListener.getFont("fonts/barkentina.otf");
		printedPass.setTypeface(barkentina);
		label = (TextView) viewer.findViewById(R.id.pass_label);
		
		passUpdate(pass);
		
		copyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mListener.copy(pass);
				
			}
		});
		
		return viewer;
	}
	
	
	
	/**
	 * Pass update.
	 *
	 * @param pass the pass
	 */
	public void passUpdate(String pass){
		printedPass.setText(pass);
		if(pass != null){
			label.setVisibility(View.VISIBLE);
			copyButton.setVisibility(View.VISIBLE);
		}
	}
}
