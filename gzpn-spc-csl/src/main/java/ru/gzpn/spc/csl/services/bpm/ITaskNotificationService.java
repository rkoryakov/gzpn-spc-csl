package ru.gzpn.spc.csl.services.bpm;

import javax.mail.internet.InternetAddress;

import ru.gzpn.spc.csl.model.enums.MessageType;

public interface ITaskNotificationService {
	void sendMessage(MessageType type, InternetAddress to, String attributes);
}
