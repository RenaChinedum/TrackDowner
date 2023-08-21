package com.ntechinedum.TrackDowner.repository;

import com.ntechinedum.TrackDowner.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from users s where s.email = ?1 and s.password = ?2",
            nativeQuery = true)
    User findByEmailAndPassword(String userName, String password);

    @Query(value = "select * from users s where s.email = ?1",
            nativeQuery = true)
    User findByEmail(String email);

    User findUserBy();
    @Query(value = "select * from users s where s.password = ?1",
            nativeQuery = true)
    User findByPassword(String password);
}
