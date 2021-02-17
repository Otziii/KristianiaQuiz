package com.jorfald.kristianiaquestions

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import java.util.*
import kotlin.concurrent.schedule
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var pointsTextView: TextView
    private lateinit var questionTextView: TextView
    private lateinit var answerButton1: Button
    private lateinit var answerButton2: Button
    private lateinit var answerButton3: Button
    private lateinit var answerButton4: Button

    private lateinit var questionListObject: QuestionListObject
    private var points = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pointsTextView = findViewById(R.id.points_text_view)
        questionTextView = findViewById(R.id.question_text_view)
        answerButton1 = findViewById(R.id.answer_button_1)
        answerButton2 = findViewById(R.id.answer_button_2)
        answerButton3 = findViewById(R.id.answer_button_3)
        answerButton4 = findViewById(R.id.answer_button_4)

        getQuestionFromAPI()
    }

    private fun getQuestionFromAPI() {
        val url = "https://run.mocky.io/v3/42d70006-e005-4095-8aa1-a1c15704ddc7"
        val requestQueue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
                Request.Method.GET,
                url,
                { jsonResponse ->
                    questionListObject = Gson().fromJson(jsonResponse, QuestionListObject::class.java)

                    showRandomQuestion()
                },
                { errorObject ->
                    print(errorObject.networkResponse.statusCode)
                }
        )

        requestQueue.add(stringRequest)
    }

    private fun showRandomQuestion() {
        setButtonsEnabled(true)

        val questionIndex = Random.nextInt(0, questionListObject.questions.size - 1)
        val question = questionListObject.questions[questionIndex]

        questionTextView.text = question.question
        bindButtons(question)
    }

    private fun bindButtons(question: QuestionObject) {
        val answer1 = question.answers[0]
        answerButton1.text = answer1.answer
        answerButton1.setOnClickListener {
            checkIfAnswerIsCorrect(answer1, question.correctAnswerId)
        }

        val answer2 = question.answers[1]
        answerButton2.text = answer2.answer
        answerButton2.setOnClickListener {
            checkIfAnswerIsCorrect(answer2, question.correctAnswerId)
        }

        val answer3 = question.answers[2]
        answerButton3.text = answer3.answer
        answerButton3.setOnClickListener {
            checkIfAnswerIsCorrect(answer3, question.correctAnswerId)
        }

        val answer4 = question.answers[3]
        answerButton4.text = answer4.answer
        answerButton4.setOnClickListener {
            checkIfAnswerIsCorrect(answer4, question.correctAnswerId)
        }
    }

    private fun checkIfAnswerIsCorrect(answer: AnswerObject, correctAnswerId: Int) {
        if (answer.id == correctAnswerId) {
            points += 1000

            questionTextView.text = "Gratulerer, det var riktig svar!"
        } else {
            if (points >= 150) {
                points -= 150
            } else {
                points = 0
            }

            questionTextView.text = "Beklager, det var feil svar."
        }

        pointsTextView.text = "Points: $points"
        setButtonsEnabled(false)

        Timer("QuestionTimer", false).schedule(5000) {
            runOnUiThread {
                showRandomQuestion()
            }
        }
    }

    private fun setButtonsEnabled(isEnabled: Boolean) {
        answerButton1.isEnabled = isEnabled
        answerButton2.isEnabled = isEnabled
        answerButton3.isEnabled = isEnabled
        answerButton4.isEnabled = isEnabled
    }
}