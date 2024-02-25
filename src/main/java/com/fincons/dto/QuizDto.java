package com.fincons.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {

    private long id;

    private String title;

    @JsonIgnoreProperties("quiz")
    private List<QuestionDto> questions;

    @JsonIgnoreProperties("quiz")
    private LessonDto lesson;
}