package ru.gazprom_neft.example;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping(value = "greeting", produces = "application/json")
	public Greeting greeting(@RequestParam(value = "message", defaultValue = "World") String message) {
		return new Greeting(message, new String[] { "user1", "admin", "user2" });
	}
}

class Greeting {
	private String message;
	private List<String> users;

	public Greeting(String message, String[] users) {
		this.message = message;
		this.users = Arrays.asList(users);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}
}