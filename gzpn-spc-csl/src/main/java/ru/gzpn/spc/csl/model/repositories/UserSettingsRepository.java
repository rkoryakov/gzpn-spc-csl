package ru.gzpn.spc.csl.model.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import ru.gzpn.spc.csl.model.UserSettings;

@Repository
public interface UserSettingsRepository extends BaseRepository<UserSettings> {
	public List<UserSettings> findByUserId(String userId);
}
