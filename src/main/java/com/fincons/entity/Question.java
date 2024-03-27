package com.fincons.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "text", length = 20971520)
    private String textQuestion;

    //5. DOMANDA - RISPOSTE (Question.class - Answer.class) 1:N   la domanda a più rispostesbagliate
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL) //
    private List<Answer> answers;

    //4. QUIZ - DOMANDE(question.class) 1:N Un quiz a molte domande
    @ManyToOne(fetch = FetchType.LAZY)
    private Quiz quiz;

    @Column(name = "valueOfQuestion")
    private int valueOfQuestion;

    @Column(name  = "deleted")
    private boolean deleted;

}
