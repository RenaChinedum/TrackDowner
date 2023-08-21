package com.ntechinedum.TrackDowner.repository;

import com.ntechinedum.TrackDowner.entity.Tasks;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Tasks, Long> {
    @Query(value = "select * from tasks  where user_id = ?1",
            nativeQuery = true)
    List<Tasks> findAllByUserId(Long userId);

    @Query(value = "select * from tasks  where status = 0 and user_id = ?1",
            nativeQuery = true)
    List<Tasks> findAllByStatusPending(Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tasks set status = (status + 1) WHERE task_id = ?1",
            nativeQuery = true)
    void moveTask(Long taskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tasks set updated_at = ?1 WHERE task_id = ?2",
            nativeQuery = true)
    void updateUpdateAt(LocalDateTime dateTime, Long taskId);

    @Query(value = "select * from tasks where status = 1 and user_id = ?1",
            nativeQuery = true)
    List<Tasks> findAllInProgress(Long userId);

    @Query(value = "select * from tasks  where status = 2 and user_id = ?1",
            nativeQuery = true)
    List<Tasks> findAllDone(Long userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tasks set status = (status - 1) WHERE task_id = ?1",
            nativeQuery = true)
    void moveBack(Long taskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tasks  set title = ?1, description = ?2 WHERE task_id = ?3",
            nativeQuery = true)
    void updateTask(String title, String description, Long taskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tasks set completed_at = ?1 WHERE task_id = ?2",
            nativeQuery = true)
    void updateCompletedAt(LocalDateTime now, Long taskId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE tasks  set completed_at = null  WHERE task_id = ?1",
            nativeQuery = true)
    void removeCompletedAt(Long taskId);

    @Query(value = "select title from tasks  where user_id = ?1",
            nativeQuery = true)
    List<String> findAllTitle(Long userId);


}
