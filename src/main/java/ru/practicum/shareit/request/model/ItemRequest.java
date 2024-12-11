package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private User requestor;

    @Column(name = "created")
    private LocalDateTime created;
}
