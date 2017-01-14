package org.bridgelabz.documentsigner.service;

import org.bridgelabz.documentsigner.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService {

	public void addUser(User user);

	public User authUser(String email, String password);

}