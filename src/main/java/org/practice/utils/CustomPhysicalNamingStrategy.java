package org.practice.utils;

import java.util.Locale;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy {

  @Override
  public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    return name;
  }

  @Override
  public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    return name;
  }

  @Override
  public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    return name;
  }

  @Override
  public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    return toSnakeCase(name);
  }

  @Override
  public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
    return toSnakeCase(name);
  }

  private static Identifier toSnakeCase(Identifier name) {
    if (name == null)
      return null;
    String snakeCase = toSnakeCase(name.getText());
    return Identifier.toIdentifier(snakeCase, name.isQuoted());
  }

  private static String toSnakeCase(String text) {
    var buf = new StringBuilder(text);
    for (var counter = 1; counter < buf.length() - 1; counter++) {
      if (Character.isLowerCase(buf.charAt(counter - 1))
          && Character.isUpperCase(buf.charAt(counter))
          && Character.isLowerCase(buf.charAt(counter + 1))) {
        buf.insert(counter++, '_');
      }
    }
    return buf.toString().toLowerCase(Locale.ROOT);
  }
}
