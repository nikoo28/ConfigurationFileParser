package com.nikoo28;

/**
 * Created by nikoo28 on 2/16/17.
 */

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CandidateKey {

  private String key;
  private HashMap<String, Object> keyValueMap;

  public CandidateKey(String key) {

    this.key = key;
    keyValueMap = new HashMap<>();
  }


  public void addKeyValue(String line) {

    //Ignore the comments
    String relevantInfo = line.split(";")[0];

    String key = relevantInfo.split("=")[0].trim().toLowerCase();
    String value = relevantInfo.split("=")[1].trim();

    // Check for boolean values
    if (isValueBooleanTrue(value)) {
      keyValueMap.put(key, Boolean.TRUE);
      return;
    }

    if (isValueBooleanFalse(value)) {
      keyValueMap.put(key, Boolean.FALSE);
      return;
    }

    // Handle integer and double values
    if (isNumber(value)) {
      try {
        keyValueMap.put(key, NumberFormat.getInstance().parse(value));
      } catch (ParseException ignored) {
      }
    }

    // Handle lists
    if (value.contains(",")) {
      if (isNumber(value.split(",")[0])) {
        List<Number> numberList = new ArrayList<>();
        for (String item : value.split(",")) {
          try {
            numberList.add(NumberFormat.getInstance().parse(item));
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }
        keyValueMap.put(key, numberList);
        return;
      }

      if (isBoolean(value.split(",")[0])) {
        List<Boolean> booleanList = new ArrayList<>();
        for (String item : value.split(",")) {
          if (isValueBooleanTrue(item))
            booleanList.add(true);
          else
            booleanList.add(false);
        }
        keyValueMap.put(key, booleanList);
        return;
      }

      List<String> multipleItems = new ArrayList<>();
      Collections.addAll(multipleItems, value.split(","));
      keyValueMap.put(key, multipleItems);
      return;
    }

    // Handle quoted and unquoted strings
    if (value.contains("\"")) {
      value = value.replace('\"', ' ').trim();
    }
    keyValueMap.put(key, value);

  }

  private boolean isBoolean(String value) {
    return isValueBooleanFalse(value) || isValueBooleanTrue(value);
  }

  private boolean isValueBooleanTrue(String value) {
    return (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes"));
  }

  private boolean isValueBooleanFalse(String value) {
    return (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("no"));
  }

  private boolean isNumber(String value) {
    try {
      NumberFormat.getInstance().parse(value);
    } catch (ParseException ignored) {
      return false;
    }

    return true;
  }

  public HashMap<String, Object> getKeyValueMap() {
    return keyValueMap;
  }
}
