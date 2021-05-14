package com.oberoi.jsonsort;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 
 * @author Manjit Oberoi
 * 
 * A simple code to read JSON files listing Honda and Maruti cars and sort
 * (ascending or descending) it as per Cubic capacity (CC) or as per
 * Model.
 *
 */
public class ReadJSonFromFileAndSort {

	// JSon files to be read
	private static final String[] files = new String[] { "src/main/resources/hondacars.json",
			"src/main/resources/maruticars.json" };

	// Data type of attribute used for sorting
	private static enum DATA_TYPE {
		INTEGER, STRING
	};

	// Sort Order - ascending or descending
	private static enum SORT_ORDER {
		ASC, DESC
	};

	public static void main(String[] args) {
		parseFilesAndSortContent();
	}

	// Trigger point to read the files and sort the content of file
	public static void parseFilesAndSortContent() {
		try {
			String sortByCC = "cc";
			String sortByModel = "model";

			List list = readFiles(files);
			System.out.println("Before Sorting...: " + list);

			list = getSortedList(list, sortByCC, DATA_TYPE.INTEGER, SORT_ORDER.ASC);
			System.out.println("After sortByCC.ASC: " + list);

			list = getSortedList(list, sortByCC, DATA_TYPE.INTEGER, SORT_ORDER.DESC);
			System.out.println("After sortByCC.DESC: " + list);

			list = getSortedList(list, sortByModel, DATA_TYPE.STRING, SORT_ORDER.ASC);
			System.out.println("After sortByModel.ASC: " + list);

			list = getSortedList(list, sortByModel, DATA_TYPE.STRING, SORT_ORDER.DESC);
			System.out.println("After sortByModel.DESC: " + list);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Method to read files and return content as List
	public static List readFiles(String[] files) throws FileNotFoundException, IOException, ParseException {
		List carsList = new ArrayList();
		for (int counter = 0; counter < files.length; counter++) {
			carsList.addAll(readFileAsJSon(files[counter]));
		}
		return carsList;
	}

	// Method to read a file and return content as List
	public static List readFileAsList(String file) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		List list = new ArrayList();
		JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(file));
		for (int counter = 0; counter < jsonArray.size(); counter++) {
			list.add(jsonArray.get(counter));
		}
		return list;
	}

	// Method to read a file as JSONArray
	public static JSONArray readFileAsJSon(String file) throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		JSONArray jsonArray = (JSONArray) parser.parse(new FileReader(file));
		return jsonArray;
	}

	// Method to sort the list as per sort by attribute, data type and sort
	// order (ascending or descending)
	@SuppressWarnings("unchecked")
	public static List getSortedList(List list, final String sortBy, final DATA_TYPE dataType, final SORT_ORDER sortOrder) {
		Collections.sort(list, new Comparator<Object>() {
			public int compare(Object arg0, Object arg1) {

				int compareValue = 0;
				JSONObject a = (JSONObject) arg0;
				JSONObject b = (JSONObject) arg1;

				if (dataType == DATA_TYPE.INTEGER) {
					try {

						Integer value1 = new Integer(Integer.parseInt(a.get(sortBy).toString()));
						Integer value2 = new Integer(Integer.parseInt(b.get(sortBy).toString()));
						compareValue = value1.compareTo(value2);
					} catch (Exception e) {
						e.printStackTrace();

					}
				} else if (dataType == DATA_TYPE.STRING) {
					try {
						String str1 = ((String) a.get(sortBy)).toUpperCase();
						String str2 = ((String) b.get(sortBy)).toUpperCase();
						compareValue = str1.compareTo(str2);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return (sortOrder == SORT_ORDER.DESC ? (compareValue * -1) : compareValue);
			}
		});
		return list;
	}
}