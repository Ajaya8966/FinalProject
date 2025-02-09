package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import project.models.AppUser;
import project.repositories.AppUserRepository;


@Service
public class AppUserService implements UserDetailsService {
    
    @Autowired
    private AppUserRepository repo;
	private PasswordEncoder passwordEncoder;
    
    @Autowired 
    public AppUserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser appUser = repo.findByEmail(email);
        
        if (appUser != null) {
        	throw new UsernameNotFoundException("User not found with email: " + email);
        }
        	var springUser = User.withUsername(appUser.getEmail())
                   .password(appUser.getPassword()) // Password must be encoded in DB
                   .roles(appUser.getRole()) // Assign roles dynamically if needed
                   .build();
			return springUser;
    }

    // Method to register a new user (ensures password is encoded)
    public AppUser registerUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password before saving
        return repo.save(user);
    }
}