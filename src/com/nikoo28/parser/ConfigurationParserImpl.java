package com.nikoo28.parser;

import com.nikoo28.CandidateKey;
import com.nikoo28.ConfigurationReader;
import com.nikoo28.ConfigurationReaderImpl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by nikoo28 on 2/16/17.
 */
public class ConfigurationParserImpl implements ConfigurationParser {

  private Map<String, CandidateKey> propertiesMap;
  private String configFilePath;

  public ConfigurationParserImpl(String configPath) {
    this.configFilePath = configPath;
    propertiesMap = new HashMap<>();
  }

  @Override
  public void parse() {

    Scanner scanner = null;
    try {
      scanner = new Scanner(new FileReader(configFilePath));
    } catch (FileNotFoundException e) {
      System.out.println("File not found at path :- " + configFilePath);
      System.exit(-1);
    }

    CandidateKey candidateKey = null;
    while (scanner.hasNext()) {

      String line = scanner.nextLine();

      if (line == null)
        continue;

      // Handle the comments
      if (line.startsWith(";"))
        continue;

      if (line.startsWith("[")) {

        String key = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
        candidateKey = new CandidateKey(key);
        propertiesMap.put(key, candidateKey);
        continue;
      }

      if (line.isEmpty())
        continue;

      candidateKey.addKeyValue(line);
    }

  }

  @Override
  public ConfigurationReader getConfig() {

    ConfigurationReaderImpl INIConfigurationReader = new ConfigurationReaderImpl(propertiesMap);
    return INIConfigurationReader;
  }

  @Override
  public ConfigurationReader getConfig(String... overrides) {

    ConfigurationReaderImpl INIConfigurationReader = new ConfigurationReaderImpl(propertiesMap, overrides);
    return INIConfigurationReader;
  }
}