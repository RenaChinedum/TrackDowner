package com.ntechinedum.TrackDowner.dto;

import com.ntechinedum.TrackDowner.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {
    private Long id;

    private String title;

    private String description;

//    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

}
