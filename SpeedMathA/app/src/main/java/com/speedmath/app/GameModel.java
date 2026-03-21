package com.speedmath.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {

    public enum Operation {
        ADD("+", "Add"),
        SUB("−", "Sub"),
        MUL("×", "Mul"),
        DIV("÷", "Div");

        public final String symbol;
        public final String name;

        Operation(String symbol, String name) {
            this.symbol = symbol;
            this.name = name;
        }
    }

    public enum Difficulty {
        EASY("Easy", "1–10", 10, 1),
        MEDIUM("Medium", "1–25", 25, 2),
        HARD("Hard", "1–99", 99, 3);

        public final String label;
        public final String rangeLabel;
        public final int max;
        public final int points;

        Difficulty(String label, String rangeLabel, int max, int points) {
            this.label = label;
            this.rangeLabel = rangeLabel;
            this.max = max;
            this.points = points;
        }
    }

    public static class Question {
        public final String text;
        public final int answer;

        public Question(String text, int answer) {
            this.text = text;
            this.answer = answer;
        }
    }

    // Settings
    public Operation operation = Operation.ADD;
    public Difficulty difficulty = Difficulty.EASY;
    public int timerSeconds = 60;

    // Game state
    public int score = 0;
    public int correctCount = 0;
    public int wrongCount = 0;
    public int questionNumber = 0;
    public Question currentQuestion;
    public List<Boolean> recentResults = new ArrayList<>();

    private final Random random = new Random();

    public void startSession() {
        score = 0;
        correctCount = 0;
        wrongCount = 0;
        questionNumber = 0;
        recentResults.clear();
        nextQuestion();
    }

    public void nextQuestion() {
        questionNumber++;
        currentQuestion = generateQuestion();
    }

    /** Returns true if correct, false if wrong */
    public boolean submitAnswer(int userAnswer) {
        boolean correct = (userAnswer == currentQuestion.answer);
        if (correct) {
            correctCount++;
            score += difficulty.points;
        } else {
            wrongCount++;
        }
        recentResults.add(correct);
        if (recentResults.size() > 5) recentResults.remove(0);
        return correct;
    }

    public int getAccuracy() {
        int total = correctCount + wrongCount;
        if (total == 0) return 0;
        return (int) ((double) correctCount / total * 100);
    }

    public String getGrade() {
        int acc = getAccuracy();
        if (acc == 100) return "🏆 Perfect Score!";
        if (acc >= 85)  return "🌟 Excellent!";
        if (acc >= 70)  return "⭐ Great Job!";
        if (acc >= 50)  return "👍 Good Effort!";
        return "💪 Keep Practicing!";
    }

    public String formatTime(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;
        return String.format("%d:%02d", m, s);
    }

    private int randInt(int max) {
        return random.nextInt(max) + 1;
    }

    private Question generateQuestion() {
        int max = difficulty.max;
        int a, b, ans;
        String text;

        switch (operation) {
            case ADD:
                a = randInt(max);
                b = randInt(max);
                ans = a + b;
                text = a + " + " + b;
                break;
            case SUB:
                a = randInt(max);
                b = randInt(max);
                if (b > a) { int tmp = a; a = b; b = tmp; }
                ans = a - b;
                text = a + " − " + b;
                break;
            case MUL:
                int mulMax = Math.min(max, 12);
                a = randInt(mulMax);
                b = randInt(mulMax);
                ans = a * b;
                text = a + " × " + b;
                break;
            case DIV:
            default:
                int divMax = Math.min(max, 12);
                b = randInt(divMax);
                ans = randInt(divMax);
                a = b * ans;
                text = a + " ÷ " + b;
                break;
        }

        return new Question(text + " = ?", ans);
    }
}
