package com.nikoo28.parser;

/**
 * Created by nikoo28 on 2/16/17.
 */
public class ParserFactory {

  public static ConfigurationParser createParser(String configPath) {

    // TODO: Implement!
    ConfigurationParserImpl INIconfigurationParser = new ConfigurationParserImpl(configPath);
    return INIconfigurationParser;
  }
}
