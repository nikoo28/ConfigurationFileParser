package com.nikoo28.parser;

import com.nikoo28.ConfigurationReader;

/**
 * Created by nikoo28 on 2/16/17.
 */
public interface ConfigurationParser {

  void parse();

  ConfigurationReader getConfig();

  ConfigurationReader getConfig(String... overrides);
}
