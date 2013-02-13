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
public class PassTypeDialog extends SherlockDialogFragment {
	private ListView list;
	
	public interface OnPassTypeClickListener{
		public void setType(int position);
		}
	
	
	public OnPassTypeClickListener mlistener = null;
	
	public void setOnPassTypeClickListener(OnPassTypeClickListener listener){
		mlistener = listener;
	}
	
	public void onAttach(Activity activity){
		super.onAttach(activity);
		
		try {
			setOnPassTypeClickListener((OnPassTypeClickListener) activity);
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnButtonListener");
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View v = inflater.inflate(R.layout.pass_type_dialog, container,  false);
		String[] options = getResources().getStringArray(R.array.pass_type);
		
		getDialog().setTitle(getResources().getString(R.string.password_type));
		
		list = (ListView) v.findViewById(R.id.pass_list);
		
		ArrayAdapter<String> aa = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,options);
		
		list.setAdapter(aa);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
				mlistener.setType(position);
				dismiss();
			}
		});
		return v;
	}
	
}
