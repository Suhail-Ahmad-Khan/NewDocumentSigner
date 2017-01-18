// This is an interface which declares the methods 
// and provides the services to the User 

package org.bridgelabz.documentsigner.service;

import org.bridgelabz.documentsigner.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService {

	public void addUser(User user);

	public User authUser(String email, String password);

}