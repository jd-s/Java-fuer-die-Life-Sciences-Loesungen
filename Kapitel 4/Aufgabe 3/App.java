package de.bit.pl2.ex10;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class App {
	/* These parameters were selected to result in a fair comparison of the two algorithms. By using these parameters the number of
	   possible hashes will be the same in both cases. */
	private static final int NUMBER_OF_VALUES = 30;
	private static final int MAX_RANDOM_VALUE = Integer.MAX_VALUE;
	private static final int MID_SQUARE_SHIFT_AMOUNT = 27;
	private static final boolean DISPLAY = false;
	
	public static void main(String args[]) {
		List<Integer> list = getRandomIntegers(NUMBER_OF_VALUES);
		if(DISPLAY)
			showList(list);
		HashMap<Integer, Integer> modulo = hashModulo(list);
		HashMap<Integer, Integer> midSquare = hashMidSquare(list);
		System.out.println("Modulo:");
		evaluateHashes(modulo);
		System.out.println("MidSquare:");
		evaluateHashes(midSquare);
	}

	private static HashMap<Integer, Integer> hashModulo(List<Integer> list) {
		HashMap<Integer, Integer> map = new HashMap<>();
		for(Integer value: list) {
			Integer hash = value % 32;
			map.put(value, hash);
		}
		return map;
	}
	
	private static HashMap<Integer, Integer> hashMidSquare(List<Integer> list) {
		HashMap<Integer, Integer> map = new HashMap<>();
		for(Integer value: list) {
		    Integer hash = (value * value) >>> MID_SQUARE_SHIFT_AMOUNT;
			map.put(value, hash);
		}
		return map;
	}
     
	private static List<Integer> getRandomIntegers(int n) {
		List<Integer> list = new ArrayList<Integer>(n);
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());

		while (list.size() < n) {
			Integer r = rand.nextInt() % MAX_RANDOM_VALUE;
			if(!list.contains(r))
				list.add(r);
		}

		return list;
	}

	private static void showList(List<Integer> list) {
		System.out.println(list);
	}
	
	private static void evaluateHashes(Map<Integer, Integer> map) {
		if(DISPLAY)
		{
			for(Integer key: map.keySet())
			{
				System.out.println("Value " + key + " -> " + map.get(key));
			}
		}
		// Add all hashes to a set to remove duplicates
		Set<Integer> hashSet = new HashSet<Integer>();
		hashSet.addAll(map.values());
		System.out.println("" + map.size() + " values were mapped to " + hashSet.size() + " hashes (" + (map.size() - hashSet.size()) + " duplicates)");
		System.out.println();
	}
}

/* Which hashing method works best? 
 * Please see the PDF file for an explanation */