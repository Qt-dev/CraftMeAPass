/*
 * 
 */
package com.qdev.craftmeapass;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.qdev.craftmeapass.R;

// TODO: Auto-generated Javadoc
/**
 * The Class HowToFragment.
 */
public class HowToFragment extends Fragment{
	
	/** The how to_craft. */
	private TextView howTo_Title = null, howTo_who = null, howTo_where = null, howTo_key = null, howTo_text = null, howTo_text_top = null, howTo_text_bot = null, howTo_text_cen, howTo_craft;
	
	/** The how to_next. */
	private Button howTo_previous = null, howTo_next = null;
	
	/** The id. */
	private int id=0;
	
	/** The text position. */
	private int[] textPosition = new int[]{1,2,2,0,0,1};
	
	/** The how to_layout. */
	private RelativeLayout howTo_layout = null;
	
	
	
	/**
	 * The listener interface for receiving onClose events.
	 * The class that is interested in processing a onClose
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnCloseListener<code> method. When
	 * the onClose event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnCloseEvent
	 */
	public interface OnCloseListener{
		
		/**
		 * Close.
		 */
		public void closeHowTo();
		
		/**
		 * Sets the how to id.
		 *
		 * @param id the new how to id
		 */
		public void setHowToId(int id);
	}
	
	/** The m listener. */
	private OnCloseListener mListener = null;
	
	/**
	 * Sets the on close listener.
	 *
	 * @param listener the new on close listener
	 */
	public void setOnCloseListener(OnCloseListener listener){
		mListener = listener;
	}
	
	
	
	//Listener Initialization onAttach
	/* (non-Javadoc)
 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
 */
public void onAttach(Activity activity){
		super.onAttach(activity);		
		try {
			setOnCloseListener((OnCloseListener) activity);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnCloseListener");
		}
	}
	
	
	
	/**
	 * New instance.
	 *
	 * @param layout the layout
	 * @param id the id
	 * @return the how to fragment
	 */
	public static HowToFragment newInstance(int layout, int id){
		HowToFragment f = new HowToFragment();
		
		
		//FOR FUTURE USE
		Bundle args = new Bundle();
		args.putInt("layout",layout);
		args.putInt("id", id);
		f.setArguments(args);
		return f;
	}
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View viewer = inflater.inflate(R.layout.howto_activity, container, false);
		
		howTo_Title = (TextView) viewer.findViewById(R.id.howto_title);
		howTo_who = (TextView) viewer.findViewById(R.id.howto_who);
		howTo_where = (TextView) viewer.findViewById(R.id.howto_where);
		howTo_key = (TextView) viewer.findViewById(R.id.howto_key);
		howTo_text_top = (TextView) viewer.findViewById(R.id.howto_explanation_top);
		howTo_text_bot = (TextView) viewer.findViewById(R.id.howto_explanation_bot);
		howTo_text_cen = (TextView) viewer.findViewById(R.id.howto_explanation_cen);
		howTo_craft = (TextView) viewer.findViewById(R.id.howto_craft);
		
		howTo_text = howTo_text_cen;
		howTo_text_bot.setVisibility(View.INVISIBLE);
		howTo_text_top.setVisibility(View.INVISIBLE);
		
		howTo_previous = (Button) viewer.findViewById(R.id.howto_previous);
		howTo_next = (Button) viewer.findViewById(R.id.howto_next);

		
		howTo_layout = (RelativeLayout) viewer.findViewById(R.id.howto_rlayout);
		howTo_layout.setOnClickListener(closeButtonListener());
		
		howTo_previous.setOnClickListener(previousButtonListener());		
		howTo_next.setOnClickListener(nextButtonListener());
		
		if(getArguments()!= null){
			setLayout(getArguments().getInt("layout"));
			setId(getArguments().getInt("id"));
		}else{
			setLayout(0);
		}
		
		
		loadText(id);
		
		return viewer;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onPause()
	 */
	@Override
	public void onPause(){
		super.onPause();
		mListener.setHowToId(id);
	}
	
	/**
	 * Load text.
	 *
	 * @param id the id
	 */
	public void loadText(int id){
		Resources res = getResources();
		String[] exp = res.getStringArray(R.array.howto_array);
		setVisible();
		setTextLocation(textPosition[id]);
		switch (id) {
		case 0:
			howTo_previous.setVisibility(View.INVISIBLE);
			break;
		case 1:
			howTo_who.setText(res.getString(R.string.howto_who));
			break;
		case 2:
			howTo_where.setText(res.getString(R.string.howto_where));
			break;
		case 3:
			howTo_key.setText(res.getString(R.string.howto_key));
			break;
		case 4:
			howTo_craft.setVisibility(View.VISIBLE);
			break;
		case 5:
			setCloseButton(true);
			break;
		}
		howTo_text.setText(exp[id]);
		howTo_text.setMovementMethod(new ScrollingMovementMethod());
	}
	
	/**
	 * Sets the close button.
	 *
	 * @param b the new close button
	 */
	private void setCloseButton(boolean b){
		Resources res = getResources();
		if(b){
			howTo_next.setText(res.getString(R.string.close));
			howTo_next.setOnClickListener(closeButtonListener());
		}else{
			howTo_next.setText(res.getString(R.string.next));
			howTo_next.setOnClickListener(nextButtonListener());
		}
	}
	
	
	/**
	 * Sets the visible.
	 */
	private void setVisible(){
		howTo_who.setText("");
		howTo_where.setText("");
		howTo_key.setText("");
		howTo_craft.setVisibility(View.INVISIBLE);
		howTo_previous.setVisibility(View.VISIBLE);
		setCloseButton(false);
	}
	

	/**
	 * Sets the text location.
	 *
	 * @param i the new text location
	 */
	private void setTextLocation(int i){
		howTo_text.setVisibility(View.INVISIBLE);
		if(i==0){
			howTo_text = howTo_text_top;
		}else if(i==1){
			howTo_text = howTo_text_cen;
		}else{
			howTo_text = howTo_text_bot;
		}
		howTo_text.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Close button listener.
	 *
	 * @return the on click listener
	 */
	private OnClickListener closeButtonListener(){
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setId(0);
				mListener.closeHowTo();			
			}
		};
		return listener;
	}
	
	/**
	 * Previous button listener.
	 *
	 * @return the on click listener
	 */
	private OnClickListener previousButtonListener(){
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				id--;
				loadText(id);			
			}
		};
		return listener;
	}
	
	/**
	 * Next button listener.
	 *
	 * @return the on click listener
	 */
	private OnClickListener nextButtonListener(){
		OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				id++;
				loadText(id);			
			}
		};
		return listener;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	private void setId(int id){
		this.id = id;
	}
	
	/**
	 * Sets the layout.
	 *
	 * @param id the new layout
	 */
	private void setLayout(int id){
		String title = getResources().getString(R.string.howto_title);
		howTo_Title.setText(title);	
		switch (id) {
		case 0:/* portrait mobile */
			textPosition = new int[]{1,2,2,0,0,1};
			break;
		case 1:
			howTo_Title.setVisibility(View.INVISIBLE);
			getActivity().setTitle(title);
			textPosition = new int[]{1,1,1,1,1,1};
		default:
			break;
		}
	}
	
}
