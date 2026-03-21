package com.speedmath.app;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameFragment extends Fragment {

    private GameModel model;
    private CountDownTimer countDownTimer;
    private int timeLeft;

    private TextView tvScore, tvQNum, tvTimerDisplay, tvQuestionLabel, tvQuestion;
    private EditText etAnswer;
    private TimerRingView timerRing;
    private View[] streakDots = new View[5];
    private View feedbackOverlay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = ((MainActivity) requireActivity()).getGameModel();

        tvScore         = view.findViewById(R.id.tv_score);
        tvQNum          = view.findViewById(R.id.tv_qnum);
        tvTimerDisplay  = view.findViewById(R.id.tv_timer_text);
        tvQuestionLabel = view.findViewById(R.id.tv_question_label);
        tvQuestion      = view.findViewById(R.id.tv_question);
        etAnswer        = view.findViewById(R.id.et_answer);
        timerRing       = view.findViewById(R.id.timer_ring);
        feedbackOverlay = view.findViewById(R.id.feedback_overlay);
        ImageButton btnSubmit = view.findViewById(R.id.btn_submit);

        streakDots[0] = view.findViewById(R.id.dot0);
        streakDots[1] = view.findViewById(R.id.dot1);
        streakDots[2] = view.findViewById(R.id.dot2);
        streakDots[3] = view.findViewById(R.id.dot3);
        streakDots[4] = view.findViewById(R.id.dot4);

        etAnswer.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        etAnswer.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etAnswer.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitAnswer();
                return true;
            }
            return false;
        });
        btnSubmit.setOnClickListener(v -> submitAnswer());

        displayQuestion();
        startTimer();
        focusInput();
    }

    private void startTimer() {
        timeLeft = model.timerSeconds;
        timerRing.setMaxTime(model.timerSeconds);
        updateTimerDisplay();

        countDownTimer = new CountDownTimer(model.timerSeconds * 1000L, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) (millisUntilFinished / 1000);
                updateTimerDisplay();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateTimerDisplay();
                if (isAdded()) {
                    ((MainActivity) requireActivity()).showResults();
                }
            }
        }.start();
    }

    private void updateTimerDisplay() {
        tvTimerDisplay.setText(model.formatTime(timeLeft));
        timerRing.setTimeLeft(timeLeft);

        // Color the ring
        float progress = (float) timeLeft / model.timerSeconds;
        if (progress < 0.25f) {
            timerRing.setRingColor(Color.parseColor("#ef4444"));
        } else if (progress < 0.5f) {
            timerRing.setRingColor(Color.parseColor("#f59e0b"));
        } else {
            timerRing.setRingColor(Color.parseColor("#7c3aed"));
        }
    }

    private void displayQuestion() {
        tvQuestionLabel.setText("QUESTION " + model.questionNumber);
        tvQNum.setText(String.valueOf(model.questionNumber));
        tvScore.setText(String.valueOf(model.score));
        tvQuestion.setText(model.currentQuestion.text);
        etAnswer.setText("");
        updateStreakDots();

        // Pop-in animation on question
        tvQuestion.setScaleX(0.85f);
        tvQuestion.setScaleY(0.85f);
        tvQuestion.setAlpha(0f);
        tvQuestion.animate().scaleX(1f).scaleY(1f).alpha(1f)
                .setDuration(200).start();
    }

    private void submitAnswer() {
        String raw = etAnswer.getText().toString().trim();
        if (raw.isEmpty()) return;

        int userAnswer;
        try {
            userAnswer = Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            return;
        }

        boolean correct = model.submitAnswer(userAnswer);
        tvScore.setText(String.valueOf(model.score));

        if (correct) {
            showFeedback(true);
            animateScoreBump();
        } else {
            showFeedback(false);
            shakeQuestion();
        }

        updateStreakDots();

        etAnswer.postDelayed(() -> {
            if (isAdded()) {
                model.nextQuestion();
                displayQuestion();
                focusInput();
            }
        }, 300);
    }

    private void showFeedback(boolean correct) {
        feedbackOverlay.setBackgroundColor(correct
                ? Color.parseColor("#1A10b981")
                : Color.parseColor("#1Aef4444"));
        feedbackOverlay.setAlpha(1f);
        feedbackOverlay.animate().alpha(0f).setStartDelay(280).setDuration(150).start();
    }

    private void animateScoreBump() {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(tvScore, "scaleX", 1f, 1.4f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(tvScore, "scaleY", 1f, 1.4f, 1f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(scaleX, scaleY);
        set.setDuration(350);
        set.start();
    }

    private void shakeQuestion() {
        ObjectAnimator shaker = ObjectAnimator.ofFloat(tvQuestion, "translationX",
                0f, -12f, 12f, -10f, 10f, -6f, 6f, 0f);
        shaker.setDuration(350);
        shaker.start();
    }

    private void updateStreakDots() {
        for (int i = 0; i < 5; i++) {
            if (i < model.recentResults.size()) {
                boolean res = model.recentResults.get(i);
                streakDots[i].setBackgroundResource(
                        res ? R.drawable.dot_correct : R.drawable.dot_wrong);
            } else {
                streakDots[i].setBackgroundResource(R.drawable.dot_neutral);
            }
        }
    }

    private void focusInput() {
        etAnswer.requestFocus();
        etAnswer.postDelayed(() -> {
            if (isAdded()) {
                InputMethodManager imm = (InputMethodManager)
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(etAnswer, InputMethodManager.SHOW_IMPLICIT);
                }
            }
        }, 100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
