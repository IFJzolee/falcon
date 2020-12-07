package com.falcon.assessment.service;

import java.time.OffsetDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "palindrome")
public class PalindromeTask {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String content;
    @Column
    private OffsetDateTime timestamp;

    public PalindromeTask(String content, OffsetDateTime timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

}
