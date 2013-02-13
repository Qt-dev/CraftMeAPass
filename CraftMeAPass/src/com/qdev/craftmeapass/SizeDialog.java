/**
 * 
 */
package com.qdev.craftmeapass;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.qdev.craftmeapass.R;

/**
 * @author QT
 *
 */
public class SizeDialog extends SherlockDialogFragment {
	private ListView list;
	
	public interface OnSizeClickListener{
		public void setSize(int position);
		}
	
	public OnSizeClickListener mlistener = null;
	
	public void setOnSizeClickListener(OnSizeClickListener listener){
		mlistener = listener;
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		try {
			setOnSizeClickListener((OnSizeClickListener) activity);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnSizeListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.size_dialog, container,  false);
		String[] options = getResources().getStringArray(R.array.common_sizes);
		
		getDialog().setTitle(getResources().getString(R.string.common_length));
		
		list = (ListView) v.findViewById(R.id.length_dialog);
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,options);
		
		list.setAdapter(aa);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				mlistener.setSize(position);
				dismiss();
			}
		});
		return v;
	}
	
}
