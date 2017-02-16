package com.nikoo28;

/**
 * Created by nikoo28 on 2/16/17.
 */
import java.util.List;
import java.util.Optional;

public interface ConfigurationReader {

  boolean hasKey(String key);

  Optional<Object> get(String key);

  List<Object> getList(String key);

  Optional<Boolean> getBoolean(String key);

  List<Boolean> getBooleanList(String key);

  Optional<Number> getNumber(String key);

  List<Number> getNumberList(String key);

  Optional<String> getString(String key);

  List<String> getStringList(String key);
}
