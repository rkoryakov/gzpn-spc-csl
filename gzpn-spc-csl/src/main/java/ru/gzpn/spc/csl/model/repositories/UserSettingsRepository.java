package ru.gzpn.spc.csl.model.repositories;

import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.UserSettings;

@Repository
public interface UserSettingsRepository extends BaseRepository<UserSettings> {
	public UserSettings findByUserId(String userId);
}
