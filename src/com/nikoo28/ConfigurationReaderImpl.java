package com.nikoo28;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Created by nikoo28 on 2/16/17.
 */
public class ConfigurationReaderImpl implements ConfigurationReader {

  private Map<String, CandidateKey> configMap;
  private List<String> overrideList;

  public ConfigurationReaderImpl(Map<String, CandidateKey> propertiesMap) {
    this.configMap = propertiesMap;
  }

  public ConfigurationReaderImpl(Map<String, CandidateKey> propertiesMap, String... overrides) {

    this.configMap = propertiesMap;
    this.overrideList = new ArrayList<>();
    Collections.addAll(this.overrideList, overrides);
  }

  @Override
  public boolean hasKey(String key) {
    return configMap.containsKey(key);
  }

  @Override
  public Optional<Object> get(String key) {

    if (!isKeyValid(key))
      return Optional.empty();

    CandidateKey candidateKey = configMap.get(key.split("\\.")[0]);
    HashMap<String, Object> keyValueMap = candidateKey.getKeyValueMap();

    if (overrideList.isEmpty()) {
      Optional<Object> result = Optional.ofNullable(keyValueMap.get(key.split("\\.")[1]));
      return result;
    }

    for (String overrideString : overrideList) {
      String overrideKey = key.split("\\.")[1] + "<" + overrideString + ">";
      if (keyValueMap.get(overrideKey) != null) {
        Optional<Object> result = Optional.ofNullable(keyValueMap.get(overrideKey));
        return result;
      }
    }

    return Optional.empty();
  }

  @Override
  public List<Object> getList(String key) {

    if (!isKeyValid(key))
      return null;

    CandidateKey candidateKey = configMap.get(key.split("\\.")[0]);
    HashMap<String, Object> keyValueMap = candidateKey.getKeyValueMap();


    if (!(keyValueMap.get(key.split("\\.")[1]) instanceof List)) {
      System.out.println("Configuration file does not support a list for the given key:- " + key);
    }

    return (List<Object>) keyValueMap.get(key.split("\\.")[1]);
  }

  @Override
  public Optional<Boolean> getBoolean(String key) {

    Optional<Object> object = get(key);
    if (object.isPresent()) {
      return Optional.of((Boolean) object.get());
    }

    return Optional.empty();
  }

  @Override
  public List<Boolean> getBooleanList(String key) {

    List<Object> list = getList(key);

    List<Boolean> booleanList = new ArrayList<>();
    for (Object item : list) {
      booleanList.add((Boolean) item);
    }

    return booleanList;
  }

  @Override
  public Optional<Number> getNumber(String key) {

    Optional<Object> object = get(key);
    if (object.isPresent()) {
      try {
        return Optional.of(NumberFormat.getInstance().parse((String) object.get()));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    return Optional.empty();
  }

  @Override
  public List<Number> getNumberList(String key) {

    List<Object> list = getList(key);

    List<Number> numberList = new ArrayList<>();
    for (Object item : list) {
      numberList.add((Number) item);
    }

    return numberList;
  }

  @Override
  public Optional<String> getString(String key) {

    Optional<Object> object = get(key);
    if (object.isPresent()) {
      return Optional.of((String) object.get());
    }

    return Optional.empty();
  }

  @Override
  public List<String> getStringList(String key) {

    List<Object> list = getList(key);

    List<String> stringList = new ArrayList<>();
    for (Object item : list) {
      stringList.add((String) item);
    }

    return stringList;
  }

  private boolean isKeyValid(String key) {

    if (key == null || key.isEmpty() || key.split(".").length == 1)
      return false;

    String groupKey = key.split("\\.")[0];

    return hasKey(groupKey);
  }
}