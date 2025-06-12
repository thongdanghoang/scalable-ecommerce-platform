package vn.id.thongdanghoang;

import io.quarkus.liquibase.LiquibaseFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import liquibase.Liquibase;
import liquibase.exception.LiquibaseException;

@ApplicationScoped
public class MigrationService {

  @Inject
  LiquibaseFactory liquibaseFactory;

  public void checkMigration() throws LiquibaseException {
    // Get the list of liquibase change set statuses
    try (Liquibase liquibase = liquibaseFactory.createLiquibase()) {
      liquibase.validate();
      liquibase.update(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
      // DON'T REMOVE THIS LINE
      // For some unknown reason, liquibase will not run the all migrations if this is not called
      liquibase.getChangeSetStatuses(liquibaseFactory.createContexts(), liquibaseFactory.createLabels());
    }
  }
}
