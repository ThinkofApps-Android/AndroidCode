package com.speedmath.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

// ── Enums ─────────────────────────────────────────────────────────────────────

enum class Operation(val symbol: String, val label: String) {
    ADD("+", "Add"),
    SUB("−", "Sub"),
    MUL("×", "Mul"),
    DIV("÷", "Div"),
}

enum class Difficulty(val label: String, val rangeLabel: String, val max: Int, val points: Int) {
    EASY("Easy", "1–10", 10, 1),
    MEDIUM("Medium", "1–25", 25, 2),
    HARD("Hard", "1–99", 99, 3),
}

enum class Screen { SETUP, PLAYING, RESULTS }

enum class FeedbackState { NONE, CORRECT, WRONG }

// ── State ─────────────────────────────────────────────────────────────────────

data class Question(val text: String, val answer: Int)

data class GameState(
    val screen: Screen = Screen.SETUP,

    // Setup
    val timerSeconds: Int = 60,
    val operation: Operation = Operation.ADD,
    val difficulty: Difficulty = Difficulty.EASY,

    // Game
    val score: Int = 0,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val questionNumber: Int = 0,
    val currentQuestion: Question = Question("", 0),
    val answerInput: String = "",
    val timeLeft: Int = 60,
    val recentResults: List<Boolean> = emptyList(),
    val feedbackState: FeedbackState = FeedbackState.NONE,
    val questionRevision: Int = 0,   // bumped to trigger animations
) {
    val timeProgress: Float get() = if (timerSeconds > 0) timeLeft.toFloat() / timerSeconds else 0f
    val timerDisplay: String get() = "${timeLeft / 60}:${(timeLeft % 60).toString().padStart(2, '0')}"
    val setupTimerDisplay: String get() = "${timerSeconds / 60}:${(timerSeconds % 60).toString().padStart(2, '0')}"
    val accuracy: Int get() {
        val total = correctCount + wrongCount
        return if (total > 0) (correctCount * 100 / total) else 0
    }
    val grade: String get() = when {
        accuracy == 100 -> "🏆 Perfect Score!"
        accuracy >= 85  -> "🌟 Excellent!"
        accuracy >= 70  -> "⭐ Great Job!"
        accuracy >= 50  -> "👍 Good Effort!"
        else            -> "💪 Keep Practicing!"
    }
}

// ── ViewModel ─────────────────────────────────────────────────────────────────

class GameViewModel : ViewModel() {

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    private var timerJob: Job? = null

    // ── Setup actions ──────────────────────────────────────────────────────────

    fun setTimerSeconds(seconds: Int) {
        _state.update { it.copy(timerSeconds = seconds) }
    }

    fun setOperation(op: Operation) {
        _state.update { it.copy(operation = op) }
    }

    fun setDifficulty(diff: Difficulty) {
        _state.update { it.copy(difficulty = diff) }
    }

    // ── Game flow ──────────────────────────────────────────────────────────────

    fun startSession() {
        val s = _state.value
        _state.update {
            it.copy(
                screen = Screen.PLAYING,
                score = 0,
                correctCount = 0,
                wrongCount = 0,
                questionNumber = 0,
                recentResults = emptyList(),
                timeLeft = s.timerSeconds,
                feedbackState = FeedbackState.NONE,
                answerInput = "",
            )
        }
        nextQuestion()
        startTimer()
    }

    fun setAnswerInput(value: String) {
        // Allow negative numbers and digits only
        if (value.matches(Regex("-?\\d*"))) {
            _state.update { it.copy(answerInput = value) }
        }
    }

    fun submitAnswer() {
        val s = _state.value
        val userAnswer = s.answerInput.toIntOrNull() ?: return
        val isCorrect = userAnswer == s.currentQuestion.answer

        val newRecent = (s.recentResults + isCorrect).takeLast(5)
        val newScore = if (isCorrect) s.score + s.difficulty.points else s.score
        val newCorrect = if (isCorrect) s.correctCount + 1 else s.correctCount
        val newWrong = if (!isCorrect) s.wrongCount + 1 else s.wrongCount

        _state.update {
            it.copy(
                score = newScore,
                correctCount = newCorrect,
                wrongCount = newWrong,
                recentResults = newRecent,
                feedbackState = if (isCorrect) FeedbackState.CORRECT else FeedbackState.WRONG,
            )
        }

        viewModelScope.launch {
            delay(300)
            nextQuestion()
        }
    }

    fun resetToSetup() {
        timerJob?.cancel()
        _state.update { it.copy(screen = Screen.SETUP) }
    }

    // ── Internals ──────────────────────────────────────────────────────────────

    private fun nextQuestion() {
        val s = _state.value
        val q = generateQuestion(s.operation, s.difficulty)
        _state.update {
            it.copy(
                questionNumber = it.questionNumber + 1,
                currentQuestion = q,
                answerInput = "",
                feedbackState = FeedbackState.NONE,
                questionRevision = it.questionRevision + 1,
            )
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val current = _state.value.timeLeft
                if (current <= 1) {
                    _state.update { it.copy(timeLeft = 0, screen = Screen.RESULTS) }
                    break
                } else {
                    _state.update { it.copy(timeLeft = current - 1) }
                }
            }
        }
    }

    private fun generateQuestion(op: Operation, diff: Difficulty): Question {
        val max = diff.max
        var a: Int; var b: Int; val ans: Int; val text: String
        when (op) {
            Operation.ADD -> {
                a = Random.nextInt(1, max + 1)
                b = Random.nextInt(1, max + 1)
                ans = a + b; text = "$a + $b"
            }
            Operation.SUB -> {
                a = Random.nextInt(1, max + 1)
                b = Random.nextInt(1, max + 1)
                if (b > a) { val tmp = a; a = b; b = tmp }
                ans = a - b; text = "$a − $b"
            }
            Operation.MUL -> {
                val mulMax = minOf(max, 12)
                a = Random.nextInt(1, mulMax + 1)
                b = Random.nextInt(1, mulMax + 1)
                ans = a * b; text = "$a × $b"
            }
            Operation.DIV -> {
                val divMax = minOf(max, 12)
                b = Random.nextInt(1, divMax + 1)
                ans = Random.nextInt(1, divMax + 1)
                a = b * ans; text = "$a ÷ $b"
            }
        }
        return Question("$text = ?", ans)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
