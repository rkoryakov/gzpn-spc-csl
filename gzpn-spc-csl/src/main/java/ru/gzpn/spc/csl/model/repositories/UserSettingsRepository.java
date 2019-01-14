package ru.gzpn.spc.csl.model.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.UserSettings;

@Repository
public interface UserSettingsRepository extends BaseRepository<UserSettings> {
	@Query(value= "SELECT u FROM UserSettings u WHERE u.userId = ?1")
	public UserSettings findByUserId(String userId);
}
