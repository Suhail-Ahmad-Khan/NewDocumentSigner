// This is the controller section of the sign-up page.
// The methods defined here links the user with the input provided
// in the sign-up page of the browser and stores the data into the database.

package org.bridgelabz.documentsigner.controller;

import javax.servlet.http.HttpServletRequest;

import org.bridgelabz.documentsigner.model.Token;
import org.bridgelabz.documentsigner.model.User;
import org.bridgelabz.documentsigner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class SignupController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/signupPage", method = RequestMethod.GET)
	public String signup(Model model) {
		User user = new User();
		Token token = new Token();
		model.addAttribute("user", user);
		model.addAttribute("token", token);
		return "signup";
	}

	@RequestMapping(value = "/signupPage", method = RequestMethod.POST)
	public String addUser(@ModelAttribute("user") User user, BindingResult result, HttpServletRequest request) {

		userService.addUser(user);
		return "login";
	}

}