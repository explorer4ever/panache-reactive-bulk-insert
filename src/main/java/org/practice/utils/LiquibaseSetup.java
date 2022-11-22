package org.practice.utils;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.util.ExceptionUtil;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseConnection;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
public class LiquibaseSetup {

  @ConfigProperty(name = "custom.liquibase.migrate")
  boolean runMigration;

  @ConfigProperty(name = "quarkus.datasource.jdbc.url")
  String datasourceUrl;

  @ConfigProperty(name = "quarkus.datasource.username")
  String datasourceUsername;

  @ConfigProperty(name = "quarkus.datasource.password")
  String datasourcePassword;

  @ConfigProperty(name = "quarkus.liquibase.change-log")
  String changeLogLocation;

  @ConfigProperty(name = "quarkus.liquibase.database-change-log-lock-table-name")
  String changeLogLockTableName;

  @ConfigProperty(name = "quarkus.liquibase.database-change-log-table-name")
  String changeLogTableName;

  @ConfigProperty(name = "quarkus.liquibase.default-schema-name")
  String defaultSchemaName;

  @ConfigProperty(name = "quarkus.liquibase.liquibase-schema-name")
  String liquibaseSchemaName;
  private static final Logger LOGGER = Logger.getLogger("ListenerBean");

  public void runLiquibaseMigration(@Observes StartupEvent event) throws LiquibaseException {
    LOGGER.info("Running the migration activities ");
    log.info("Running the migration activities {}", runMigration);
    if (runMigration) {
      Liquibase liquibase = null;
      try {
        ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(
            Thread.currentThread().getContextClassLoader());
        DatabaseConnection connection = DatabaseFactory.getInstance()
            .openConnection(datasourceUrl, datasourceUsername, datasourcePassword, null, resourceAccessor);
        Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(connection);

        database.setDatabaseChangeLogLockTableName(changeLogLockTableName);
        database.setDatabaseChangeLogTableName(changeLogTableName);
        database.setDefaultSchemaName(defaultSchemaName);
        database.setLiquibaseSchemaName(liquibaseSchemaName);

        liquibase = new Liquibase(changeLogLocation, resourceAccessor, database);
        liquibase.validate();
        liquibase.update(new Contexts(), new LabelExpression());
      } catch (Exception ex) {
        log.error("Liquibase Migration Exception Stack Trace: {}", ExceptionUtil.generateStackTrace(ex));
      } finally {
        if (liquibase != null) {
          liquibase.close();
        }
      }
    }
  }
}