package sas.infrastructure.repository.auth;

import sas.model.entity.auth.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
    Optional<Profile> findByName(String Name);

    @Query("SELECT DISTINCT p.name FROM Profile p")
    List<String> findAllNames();
}
