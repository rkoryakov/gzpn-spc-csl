package ru.gzpn.spc.csl.model.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.gzpn.spc.csl.model.UserSettings;

public interface IUserSettingsRepository extends JpaRepository<UserSettings, Long> {

}
