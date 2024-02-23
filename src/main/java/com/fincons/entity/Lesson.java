package com.fincons.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "title",nullable = false)
    private String title;

    //1. CORSO - LEZIONE N:M
    @ManyToMany(mappedBy = "lessons")
    private List<Course> courses;

    //3. LEZIONE - QUIZ 1:1
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_quiz", referencedColumnName = "id")
    private Quiz quiz;

    //2. LEZIONE - CONTENUTO 1:1
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_content", referencedColumnName = "id")
    private Content content;



}
