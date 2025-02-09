package project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import project.models.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Integer> {
    public AppUser findByEmail(String email); // ✅ Now returns Optional
}
