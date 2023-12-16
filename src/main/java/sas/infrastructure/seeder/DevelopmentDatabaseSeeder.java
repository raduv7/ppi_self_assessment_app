package sas.infrastructure.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@org.springframework.context.annotation.Profile({"dev"})
public class DevelopmentDatabaseSeeder
        extends AbstractDatabaseSeeder
        implements CommandLineRunner {

    @Override
    public void run(String... args) {
        this.insertSystemEntriesIfNotPresent();

        this.insertProfilesIfNotPresent();
        this.insertUsersIfNotPresent();
    }
}
