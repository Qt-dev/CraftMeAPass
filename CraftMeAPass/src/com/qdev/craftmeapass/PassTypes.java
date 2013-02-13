package com.qdev.craftmeapass;

import java.util.HashMap;

import android.content.SharedPreferences;
import android.content.res.Resources;

public class PassTypes {
	Resources res;
	SharedPreferences preferences;
	
	private Integer[] pinParams = new Integer[]{4,4};
	private Integer[] alphanumericParams = new Integer[]{7};
	private Integer[] onlyLettersParams = new Integer[]{3};
	private Integer[] strongestParams = new Integer[]{31};
	
	private HashMap<String,Integer[]> types = new HashMap<String, Integer[]>();
	
	public PassTypes(Resources aRes, SharedPreferences aPref){
		res = aRes;
		preferences=aPref;
		setMap();
	}
	
	public void setType(String title){
		Integer[] params = types.get(title);
		boolean[] boo = convertToBoolean(params[0]);
		
		SharedPreferences.Editor pe = preferences.edit();
		
		for (int i = 0; i < boo.length; i++) {
			pe.putBoolean(res.getStringArray(R.array.settings_list)[i],boo[i]);
		}
		pe.commit();

	}
	
	public void setType(int id){
		Integer[] params = types.get(res.getStringArray(R.array.pass_type)[id]);
		boolean[] boo = convertToBoolean(params[0]);
		
		
		SharedPreferences.Editor pe = preferences.edit();
		
		for (int i = 0; i < boo.length; i++) {
			pe.putBoolean(res.getStringArray(R.array.settings_list)[i],boo[i]);
		}
		if(params.length>1){
			pe.putString(res.getStringArray(R.array.settings_list)[boo.length], Integer.toString(params[0]));
		}
		pe.commit();
	}
	
	public String getName(int[] params){
		switch (params[0]) {
			case 4:
				if(params[1] == pinParams[1]){
					return res.getStringArray(R.array.pass_type)[0];
				}else{
					return res.getStringArray(R.array.pass_type)[4];
				}
			case 7:
				return res.getStringArray(R.array.pass_type)[1];
			case 3:
				return res.getStringArray(R.array.pass_type)[2];
			case 31:
				return res.getStringArray(R.array.pass_type)[3];
		default:
			return res.getStringArray(R.array.pass_type)[4];
		}
	}

	private boolean[] convertToBoolean(int code){
		boolean[] boo = new boolean[5];
		boo[4] = (boolean) (code/16 == 1);
		code%=16;
		boo[3] = (boolean) (code/8 == 1);
		code%=8;
		boo[2] = (boolean) (code/4 == 1);
		code%=4;
		boo[1] = (boolean) (code/2 == 1);
		code%=2;
		boo[0] = (boolean) (code/1 == 1);
		return boo;
	}
	
	private void setMap(){
		types.put(res.getStringArray(R.array.pass_type)[0], pinParams);
		
		//Sets Alphanumeric template, need Alphanumeric to be the second in the list
		types.put(res.getStringArray(R.array.pass_type)[1], alphanumericParams);
		
		//Sets Only Letters template, need Only letters to be the third in the list
		types.put(res.getStringArray(R.array.pass_type)[2], onlyLettersParams);
		
		//Sets Strongest template, need Strongest to be the fourth in the list
		types.put(res.getStringArray(R.array.pass_type)[3], strongestParams);
	}
	
}
