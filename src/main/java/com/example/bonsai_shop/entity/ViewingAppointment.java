package com.example.bonsai_shop.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "VIEWING_APPOINTMENT")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ViewingAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AppointmentID")
    private Integer appointmentId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;

    @Column(name = "AppointmentDate", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "Note", length = 500)
    private String note;

    @Column(name = "Status", length = 50)
    private String status = "PENDING";
}
