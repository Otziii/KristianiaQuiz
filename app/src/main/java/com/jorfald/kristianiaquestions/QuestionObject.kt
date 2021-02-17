package com.jorfald.kristianiaquestions

class QuestionObject(
    val question: String,
    val answers: List<AnswerObject>,
    val correctAnswerId: Int
)