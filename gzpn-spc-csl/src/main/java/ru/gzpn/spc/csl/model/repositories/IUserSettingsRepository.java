package ru.gzpn.spc.csl.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.gzpn.spc.csl.model.UserSettings;

public interface IUserSettingsRepository extends JpaRepository<UserSettings, Long> {

}
