package ru.gzpn.spc.csl.services.bl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

	@Autowired
	ServerProperties serverProperties;

	@GetMapping("/login")
	public String login(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout, Model model) {

		if (error != null) {
			model.addAttribute("error", true);
		}

		if (logout != null) {
			model.addAttribute("logout", true);
		}

		model.addAttribute("contextPath", serverProperties.getServlet().getContextPath() + "/login");
		return "login";
	}

	@GetMapping("/sessionExpired")
	public String sessionExpired(@RequestParam(value = "session", required = false) String session, Model model) {
		if (session != null) {
			model.addAttribute("session", session);
		}

		model.addAttribute("contextPath", serverProperties.getServlet().getContextPath());

		return "sessionExpired";
	}
}
