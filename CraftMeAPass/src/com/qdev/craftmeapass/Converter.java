/*
 * 
 */
package com.qdev.craftmeapass;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;
import com.qdev.craftmeapass.R;

// TODO: Auto-generated Javadoc
/**
 * The Class Converter.
 */
public class Converter {
	
	/** The available characters. */
	private static List<Character> availableCharacters = new ArrayList<Character>(); 
	
	/**
	 * Basic sha conversion.
	 *
	 * @param message the message
	 * @return the byte[]
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 */
	private static byte[] basicShaConversion(String message) throws NoSuchAlgorithmException{
		MessageDigest mDigest = MessageDigest.getInstance("SHA-512");
		byte[] convertedMessage = mDigest.digest(message.getBytes());
		return convertedMessage;
//		***** TO RETURN STRING *******
//		StringBuffer sb = new StringBuffer();
//		for(int i = 0; i < convertedMessage.length ; i++){
//			sb.append(Integer.toString((convertedMessage[i] & 0xff) + 0x100, 16).substring(1));
//		}
//		return sb.toString();
	}

	/**
	 * Byte tab to n base.
	 *
	 * @param byteTab the byte tab
	 * @param base the base
	 * @return the list
	 */
	private static List<Integer> byteTabToNBase(byte[] byteTab, int base){
		int remainder, temp = 0;
		List<Integer> result = new ArrayList<Integer>();

		for (int i = 0; i < byteTab.length; i++) {
			//initialise temp a : valeur de la case + retenue*256
			temp = Math.abs(byteTab[i]) + (temp*256);			
			
			//Calcule l'indice max de byteTab[i] en base base
			int maxPower = 0;
			do{	maxPower++;
				remainder = (int) (temp/Math.pow(base, maxPower));
			}while(remainder > 0);
			maxPower--;
			
			//Itere avec de maxPower à 0
			while (maxPower != 0) {
				result.add((int) (temp/Math.pow(base, maxPower)));
				if (temp%(int)Math.pow(base, maxPower) == 0) {
					temp = (int)Math.pow(base, maxPower-1);
				} else {
					temp%=(int)Math.pow(base, maxPower);
				}
				maxPower--;
			}			
		}
		result.add(temp);

		return result;
	}
	
	
	
	
	
	
	/**
	 * ************OLD BETA ALGORITHM, DO NOT TOUCH**********.
	 *
	 * @param toBeConverted the to be converted
	 * @param options the options
	 * @param maxSize the max size
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	/**
	 * Encode.
	 *
	 * @param toBeConverted the to be converted
	 * @param options the options
	 * @param maxSize the max size
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static String encode(String toBeConverted, int options, int maxSize) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		/* Options :
		 * 0 -> Low + High + Numbers + Punctuation + Space
		 * 1 -> Low + High + Numbers + Punctuation
		 * 2 -> Low + High + Numbers + Space
		 * 3 -> Low + High + Numbers */
		switch (options) {
		case 0:
			return toStringLHNPS(toBeConverted, maxSize);
		
		case 1:
			return toStringLHNP(toBeConverted, maxSize);
			
		case 2:
			return toStringLHNS(toBeConverted, maxSize);
			
		case 3:
			return toStringLHN(toBeConverted, maxSize);
		default:
			break;
		}
		
		return toBeConverted;
		
	} 
	
	/**
	 * To string lhnps.
	 *
	 * @param toBeConverted the to be converted
	 * @param maxSize the max size
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private static String toStringLHNPS(String toBeConverted, int maxSize) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		byte[] msg = basicShaConversion(toBeConverted);
		List<Integer> charList = byteTabToNBase(msg, 92);
		
		String pass = encodeLHNPS(charList);
		
		//encode the last version of the pass again and again until it goes over the pass's maxSize
		while(pass.length() < maxSize){
			msg = basicShaConversion(pass);
			charList = byteTabToNBase(msg, 92);
			pass = pass.concat(encodeLHNPS(charList));
		}
		
		//truncates the final pass to the maxsize
		return pass.substring(0, maxSize);
	}
	
	/**
	 * Encode lhnps.
	 *
	 * @param charList the char list
	 * @return the string
	 */
	private static String encodeLHNPS(List<Integer> charList) {
		//Encode using ASCII Full Options
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i<charList.size(); i++){
			char temp = (char)(charList.get(i)+32);
			sb.append(temp);
		}
		return sb.toString();
	}
	
	
	//NO SPACE METHODS
	/**
	 * To string lhnp.
	 *
	 * @param toBeConverted the to be converted
	 * @param maxSize the max size
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private static String toStringLHNP(String toBeConverted, int maxSize) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		byte[] msg = basicShaConversion(toBeConverted);
		List<Integer> charList = byteTabToNBase(msg, 91);
		String pass = encodeLHNP(charList);
		
		//encode the last version of the pass again and again until it goes over the pass's maxSize
		while(pass.length() < maxSize){
			msg = basicShaConversion(pass);
			charList = byteTabToNBase(msg, 91);
			pass = pass.concat(encodeLHNP(charList));
		}
		
		//truncates the final pass to the maxsize
		return pass.substring(0, maxSize);
	}
	
	/**
	 * Encode lhnp.
	 *
	 * @param charList the char list
	 * @return the string
	 */
	private static String encodeLHNP(List<Integer> charList) {
		//Encode using ASCII Full Options
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i<charList.size(); i++){
			char temp = (char)(charList.get(i)+33);
			sb.append(temp);
		}
		return sb.toString();
	}

	
	//LOW HIGH AND NUMBERS METHODS
	/**
	 * To string lhn.
	 *
	 * @param toBeConverted the to be converted
	 * @param maxSize the max size
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private static String toStringLHN(String toBeConverted, int maxSize) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		byte[] msg = basicShaConversion(toBeConverted);
		List<Integer> charList = byteTabToNBase(msg, 60);
		String pass = encodeLHN(charList);
		
		//encode the last version of the pass again and again until it goes over the pass's maxSize
		while(pass.length() < maxSize){
			msg = basicShaConversion(pass);
			charList = byteTabToNBase(msg, 60);
			pass = pass.concat(encodeLHN(charList));
		}
		
		//truncates the final pass to the maxsize
		return pass.substring(0, maxSize);
	}
	
	/**
	 * Encode lhn.
	 *
	 * @param charlist the charlist
	 * @return the string
	 */
	private static String encodeLHN(List<Integer> charlist) {
		//Encode using ASCII LowCaps, HighCaps and Numbers
		StringBuffer sb = new StringBuffer();
		for (Integer value : charlist) {
			char temp;
			if(value<10){
				temp = (char)(value+48);
			}else if(value >=10 && value < 35){
				temp = (char)(value+55);
			}else{
				temp = (char)(value+62);
			}
			sb.append(temp);
		}
		
		return sb.toString();
		
	}
		
	
	//LOW HIGH NUMBERS AND SPACE METHODS
	/**
	 * To string lhns.
	 *
	 * @param toBeConverted the to be converted
	 * @param maxSize the max size
	 * @return the string
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	private static String toStringLHNS(String toBeConverted, int maxSize) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		byte[] msg = basicShaConversion(toBeConverted);
		List<Integer> charList = byteTabToNBase(msg, 61);
		String pass = encodeLHNS(charList);
		
		//encode the last version of the pass again and again until it goes over the pass's maxSize
		while(pass.length() < maxSize){
			msg = basicShaConversion(pass);
			charList = byteTabToNBase(msg, 61);
			pass = pass.concat(encodeLHNS(charList));
		}
		
		//truncates the final pass to the maxsize
		return pass.substring(0, maxSize);
	}
	
	/**
	 * Encode lhns.
	 *
	 * @param charlist the charlist
	 * @return the string
	 */
	private static String encodeLHNS(List<Integer> charlist) {
		//Encode using ASCII LowCaps, HighCaps and Numbers
		StringBuffer sb = new StringBuffer();
		for (Integer value : charlist) {
			char temp;
			if(value == 1){
				temp = (char)(value+31);
			}else if(value < 12){
				temp = (char)(value+47);
			}else if(value < 37){
				temp = (char)(value+53);
			}else{
				temp = (char)(value+60);
			}
			sb.append(temp);
		}
		
		return sb.toString();
	
}
	
	
	
	
	
	/**
	 * **********NEW ALGORITHM, USE ONLY THAT**********.
	 *
	 * @param toBeConverted the to be converted
	 * @param preferences the preferences
	 * @param res the res
	 * @return the string
	 */
	public static String craftPass(String toBeConverted, SharedPreferences preferences, Resources res){
		fillList(preferences,res);
		byte[] msg = null;
		int maxLength = Integer.parseInt(preferences.getString(res.getStringArray(R.array.settings_list)[5], "8"));
		int length = 0;
		
		//converts with Sha256
		try {
			msg = basicShaConversion(toBeConverted);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		List<Integer> charList = new ArrayList<Integer>();
		
		do{
			//converts to baseN (N = the number of available characters), with a length of maxLength
			charList = radixConversion(msg, availableCharacters.size(), maxLength);
			
			//the conversion didnt reach maxLength
			length += charList.size();
		}while(length < maxLength);
		
		
		//encodes to the matching char in the list
		String pass = encode(charList);
//		
//		//iterates until the length is higher than the one required
//		while(pass.length() < maxLength){
//			try {
//				msg = basicShaConversion(pass);
//			} catch (NoSuchAlgorithmException e) {
//				e.printStackTrace();
//			}
//			charList = radixConversion(msg, availableCharacters.size());
//			pass = pass+encode(charList);
//		}
		
		//empties list and pass a String the right size
		emptyList();
		return pass.substring(0,maxLength);	
	}
	
	/**
	 * Encode.
	 *
	 * @param charlist the charlist
	 * @return the string
	 */
	private static String encode(List<Integer> charlist){
		String txt = "";
		for (Integer value : charlist) {
			txt = txt+availableCharacters.get(value);
		}
		return txt;
	}
		
	/**
	 * Fill availableCharacter list.
	 *
	 * @param preferences the preferences
	 * @param res the res
	 */
	private static void fillList(SharedPreferences preferences, Resources res){
		if(preferences.getBoolean(res.getString(R.string.uppercase_letters), true)){
			addUpperCase();
		}
		
		if(preferences.getBoolean(res.getString(R.string.lowercase_letters), true)){
			addLowerCase();
		}
		
		if(preferences.getBoolean(res.getString(R.string.numbers), true)){
			addNumbers();
		}
		
		if(preferences.getBoolean(res.getString(R.string.punctuation), true)){
			addPunctuation();
		}
		
		if(preferences.getBoolean(res.getString(R.string.space), true)){
			addSpace();
		}

	}
	
	/**
	 * Empty availableCharacter list.
	 */
	private static void emptyList(){
		availableCharacters.clear();
	}
	
	/**
	 * Adds the upper case to availableCharacter.
	 */
	private static void addUpperCase(){
		for(int i = 65; i<90 ; i++){
			availableCharacters.add((char)i);
		}
	}
	
	/**
	 * Adds the lower case to availableCharacter.
	 */
	private static void addLowerCase(){
		for(int i = 97; i<122 ; i++){
			availableCharacters.add((char)i);
		}
	}
	
	/**
	 * Adds the numbers to availableCharacter.
	 */
	private static void addNumbers(){
		for(int i = 48; i<57 ; i++){
			availableCharacters.add((char)i);
		}
	}
	
	/**
	 * Adds the punctuation to availableCharacter.
	 */
	private static void addPunctuation(){
		for(int i = 33; i<47 ; i++){
			availableCharacters.add((char)i);
		}
		for(int i = 58; i<64 ; i++){
			availableCharacters.add((char)i);
		}
		availableCharacters.add((char)91);
		for(int i = 93; i<95 ; i++){
			availableCharacters.add((char)i);
		}
		availableCharacters.add((char)123);
		for(int i = 125; i<126 ; i++){
			availableCharacters.add((char)i);
		}
	}
	
	/**
	 * Adds the space to availableCharacter.
	 */
	private static void addSpace(){
		availableCharacters.add((char) 32);
	}

	/**
	 * Radix conversion.
	 *
	 * @param byteTab the byte tab
	 * @param base the base
	 * @return the list
	 */
	private static List<Integer> radixConversion(byte[] byteTab, int base, int size){
			List<Integer> result = new ArrayList<Integer>();
			List<Integer> resultBackwards = new ArrayList<Integer>();
			int temp = 0;
			

				for(int i = 0; i < byteTab.length ; i++){
					temp = temp*256 + Math.abs(byteTab[i] & 0xFF);
					
					while(temp > base && resultBackwards.size() < size){
						resultBackwards.add((int)temp%base);
						temp = temp/base;
					}
					
					if(resultBackwards.size() >= size){
						break;
					}
				}

//			
			
			
			
			
//			for(int i = 0 ; i < byteTab.length ; i+=2){
//				for (int j = 0; j < 2; j++) {
//					//converts to Decimal
//					temp = temp + Math.abs(byteTab[j+i] & 0xFF) * (int) Math.pow(256, 2-(j+1));
////					System.out.println("On a :"+Math.abs(byteTab[i+j] & 0xFF)+" * "+(int)Math.pow(256,4-(j+1))+" = "+temp);
//				}
//				//converts from Decimal to base N
//				while (temp > base ) {
////					System.out.println("added :"+(int)temp%base);
//					resultBackwards.add((int)temp%base);
//					temp = temp/base;
//				}
//						
	//
//				//does it again 2 by 2
	//
//			}
			
			
			//puts the list on the right order
			for (int k = 0; k < resultBackwards.size(); k++) {
				result.add(resultBackwards.get(resultBackwards.size()-(k+1)));
			}

			return result;
		
	}
	
}
	

	