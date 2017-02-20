import com.nikoo28.ConfigurationReader;
import com.nikoo28.parser.ConfigurationParser;
import com.nikoo28.parser.ParserFactory;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by nikoo28 on 2/20/17.
 */
public class IntegrationTest {

  private String configPath = "resources/test.config";

  // Parses string values
  @Test
  public void parsesStringValues() {
    ConfigurationReader config = getConfig(configPath);

    Optional<String> result = config.getString("user.email");

    assertThat(result.orElse(""), equalTo("alice.sykes@gmail.com"));
  }

  // Parses integer values
  @Test
  public void parsesIntegerValues() {
    ConfigurationReader config = getConfig(configPath);

    Optional<Number> result = config.getNumber("user.id");

    assertThat(result.map(Number::intValue).orElse(0), equalTo(641220));
  }

  // Parses decimal values
  @Test
  public void parsesDecimalValues() {
    ConfigurationReader config = getConfig(configPath);

    Optional<Number> result = config.getNumber("color.transparency");

    assertThat(result.map(Number::doubleValue).orElse(0d), equalTo(75.5));
  }

  // Parses (truthy) boolean values
  @Test
  public void parsesBooleanGivenTruthyValue() {
    ConfigurationReader config = getConfig(configPath);

    Optional<Boolean> result = config.getBoolean("color.ui");

    assertThat(result.orElse(false), is(true));
  }

  // Parses (false-y) boolean values
  @Test
  public void parsesBooleanGivenFalseyValue() {
    ConfigurationReader config = getConfig(configPath);

    Optional<Boolean> result = config.getBoolean("core.preloadindex");

    assertThat(result.orElse(true), is(false));
  }

  // Parses quoted string values
  @Test
  public void parsesQuotedStringValues() {
    ConfigurationReader config = getConfig(configPath);

    Optional<String> result = config.getString("user.name");

    assertThat(result.orElse(""), equalTo("Alice Sykes"));
  }

  // Parses list values
  @Test
  public void parsesListValues() {
    ConfigurationReader config = getConfig(configPath);

    List<String> resultList = config.getStringList("core.excludes");

    assertThat(resultList, hasItems(".ignore", "~/.ignore", "/etc/ignore"));
  }

  // Parse a boolean list with yes and no
  @Test
  public void parseBooleanList() {
    ConfigurationReader configurationReader = getConfig(configPath);

    List<Boolean> resultList = configurationReader.getBooleanList("core.setvalues");

    assertThat(resultList, hasItems(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE));
  }

  // Parse a number list
  @Test
  public void parseNumberList() {
    ConfigurationReader configurationReader = getConfig(configPath);

    List<Number> resultList = configurationReader.getNumberList("color.alphavalues");

    assertThat(resultList, hasItems(3L, 4L, 5.6));
  }

  // Parse a boolean list with overrides
  @Test
  public void parseBooleanListWithOverrides() {
    String override = "production";
    ConfigurationReader configurationReader = getConfig(configPath, override);

    List<Boolean> resultList = configurationReader.getBooleanList("core.setvalues");

    assertThat(resultList, hasItems(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE));
  }

  // Parses override keys, returning overridden values when requested
  @Test
  public void parsesOverrideKeys() {
    String override = "work";
    ConfigurationReader config = getConfig(configPath, override);

    Optional<String> result = config.getString("user.email");

    assertThat(result.orElse(""), equalTo("asykes@company.com"));
  }

  // Parses override keys, returning overridden values when requested
  @Test
  public void parsesMultipleOverrideKeys() {
    String override1 = "home";
    String override2 = "production";
    ConfigurationReader config = getConfig(configPath, override1, override2);

    Optional<String> result = config.getString("user.email");

    assertThat(result.orElse(""), equalTo("asykes@home.com"));
  }

  // Returns empty optional for non-existent key
  @Test
  public void handlesNonExistentKeys() {

    ConfigurationReader config = getConfig(configPath);

    Optional<String> result = config.getString("user.handle");

    assertThat(result.isPresent(), is(false));
  }

  private ConfigurationReader getConfig(String path, String... overrides) {
    ConfigurationParser parser = ParserFactory.createParser(path);
    parser.parse();

    return parser.getConfig(overrides);
  }

}