package naturegecko.jingjok.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import naturegecko.jingjok.models.entities.UserAccountModel;
import naturegecko.jingjok.repositories.UserAccountsRepository;

@Controller
public class UserController {
	
	@Autowired
	public UserAccountsRepository userAccountsRepository;
	
	public String createNewUser() {
		return null;
	}
	
}
