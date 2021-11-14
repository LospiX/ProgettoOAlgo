package xpKernelSearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;


public class UTILITY {

	public static String delimiterToString(String txt, String leftDelim, String rightDelim) {
		String result = null;
		try {
			result = UTILITY.split(UTILITY.split(txt, leftDelim)[1], rightDelim)[0].trim();
			if (result.length()<1)
				return null;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return result;		
	}
	public static String delimiterToString(String txt, String leftDelim) {
		String result = null;
		try {
			result = txt.split(leftDelim)[1].trim();
			if (result.length()<1)
				return null;
		} catch (ArrayIndexOutOfBoundsException e) {
		}
		return result;
	}
	
	public static ArrayList<String> tokenizeOp (String s, String [] divisori) {
		TreeSet<String> divs = new TreeSet<String> (new Comparator<String> (){
			public int compare(String s1, String s2) {
				if (s1.length()<s2.length())
					return 1;
				return -1;
			}
		}); 
			// ORDINA i divisori dal più lungo al più piccolo //
		for (String d: divisori) 
			divs.add(d);
		int lastIndex;
		ArrayList<String> result = new ArrayList<String> ();
		result.add(s);
		String parte;
		boolean flgAdded = false;
		for (String div: divs) {
			for (int ind=0; ind<result.size(); ind++) {
				parte = result.get(ind).trim();
				if (UTILITY.contains(divs, parte))
					continue;
				lastIndex = 0;
				while (parte.contains(div)) {
					if(!flgAdded)
						result.remove(ind);
					if(parte.substring(lastIndex, parte.indexOf(div)).length()>0) {
						result.add(ind, parte.substring(lastIndex, parte.indexOf(div)));
						ind+=1;
					}
					result.add(ind, div);
					ind+=1;
					lastIndex = parte.indexOf(div);
					parte = UTILITY.replaceTimes(parte, div, "",1);
					parte = parte.substring(lastIndex, parte.length());
					lastIndex= 0;
					flgAdded = true;
				}
				if (flgAdded) {
					if (parte.length()>0) 
						result.add(ind, parte); //Aggiungi la parte restante //
					flgAdded = false;
				}
			}
		}
		return result;
	}
	public static<E> boolean contains(Collection<E> collection, E elem){
		for (E e: collection)
			if (e.equals(elem))
				return true;
		return false;
	}
		
	public static String replace (String original, String toFind, String replace) {
		String conc="";
		String result="";
		int indexComp = 0;
		for (char c: original.toCharArray()) {
			conc+=c;
			if (toFind.charAt(indexComp)==conc.charAt(indexComp)) {
				if(conc.length()==toFind.length()) {
					result+=replace;
					conc="";
					indexComp = 0;
				} else {
					indexComp += 1;
				}
			} else {
				result+=conc;
				conc="";
				indexComp = 0;
			}
		}
		return result;
	}
	public static String replaceTimes (String original, String toFind, String replace, int times) {
		String conc="";
		String result="";
		String remaining=original.toString();
		if (times<=0)
			return null;
		int indexComp = 0;
		for (char c: original.toCharArray()) {
			remaining = remaining.substring(1, remaining.length());
			conc+=c;
			if (toFind.charAt(indexComp)==conc.charAt(indexComp)) {
				if(conc.length()==toFind.length()) {
					result+=replace;
					times-=1;
					if(times==0)
						return result + remaining;
					conc="";
					indexComp = 0;
				} else {
					indexComp += 1;
				}
			} else {
				result+=conc;
				conc="";
				indexComp = 0;
			}
		}
		return result;
	}
	public static String[] split(String original, String sequenza ) {
		if (original==null || sequenza==null)
			return null;
		ArrayList<String> result = new ArrayList<String> ();
		int lastIndex=0;
		int beginIndex=original.indexOf(sequenza);
		while(beginIndex>=lastIndex) {
			result.add(original.substring(lastIndex, beginIndex));
			lastIndex=beginIndex+sequenza.length();
			beginIndex=original.indexOf(sequenza, lastIndex);
		}
		//if (lastIndex!=original.length()) // per eliminare elemento vuoto in coda se sequenza è alla fine // 
		result.add(original.substring(lastIndex, original.length()));
		String [] res = new String [result.size()];
		for (int i=0; i<result.size(); i++)
			res[i]=result.get(i);
		return res;
	}
	/*
	public static <T extends Manageable> String fromCollectionToList(Collection<T> insieme) {
		String list="";
		for (T elem: insieme)
			list+=elem.getName()+", ";
		if(list.length()>2)
			list=list.substring(0, list.length()-2);
		return list;
	}*/
}
