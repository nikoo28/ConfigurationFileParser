# ConfigurationFileParser
An open source parser to read .ini configuration files.

The library reads structured configuration data, infer value types, and make this information available via the included ConfigurationParser and ConfigurationReader interfaces. 

## Configuration Format

The configuration file is a line-oriented format similar to INI files.

The following describes the format and expected behavior, while the included
`IntegrationTest.java` tests specify the same information more formally.

### Groups, Settings, and Keys

Groups are denoted by brackets.

Settings are name-value pairs separated by an equal sign (and optional
white-space) associated with the group preceding them.

Values are read via keys composed of the group and setting name, separated by a
period. Keys are not case sensitive.

The following defines the `core.size_limit` setting, with a numeric value of
512:

```
[core]
size_limit = 512
```

#### Overrides

Settings may include overrides, such that a given key (e.g.,
`core.size_limit`) may return different values, depending on the override
applied to the configuration reader.

The following defines the `core.size_limit` setting, such that it should return
a numeric value of 1024 when read using the `paid` override, 512 when read using
the `trial` override, and 256 when read without overrides.

```
[core]
size_limit = 256
size_limit<trial> = 512
size_limit<paid> = 1024
```

Multiple overrides may be specified when reading configuration information, and
the _last_ override takes priority.

The following defines the `user.id` and `user.email` settings, such that
`user.id` returns a numeric value of 123 and `user.email` returns
"asykes@company.com" when read using an override of `"school", "work"`:

```
[user]
email = alison.sykes@gmail.com
id<school> = 123
email<school> = asyk8912@school.edu
email<work> = asykes@company.com
```

### Comments

Semi-colons denote the start of a comment.

```
; Full line comment
[core]
size_limit = 256; Trailing comment
```

### Value Types

Value types are inferred when reading configuration data. Supported types
include booleans, numbers, strings, and lists.

```
[example]
; Booleans
truthy = true; Alternative: yes
falsey = false; Alternative: no
; Numbers
integer_value = 512
decimal_value = 52.75
; Strings
simple_value = example
quoted_value = "another example"
; Lists
multiple_values = first,second,third
```

These values are made accessible to clients via the `ConfigurationReader`
interface.

Continuing the above example configuration data:

```
config.getBoolean("example.truthy") // => Optional<Boolean>[true]
config.getNumber("example.decimal_value") // => Optional<Number>[52.75]
config.getString("example.quoted_value") // => Optional<String>[another example]
config.getStringList("example.multiple_values") // => List<String>[first, second, third]
```

# System Dependencies

To work with this project, you'll need to have JDK8 (or higher).

```
$ java -version
java version "1.8.0_72"
```

You can use the JAR file given directly to parse the INI file.
Refer to IntegrationTest.java for a sample usage of the same.