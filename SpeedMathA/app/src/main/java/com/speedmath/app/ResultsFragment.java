package com.speedmath.app;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultsFragment extends Fragment {

    private GameModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = ((MainActivity) requireActivity()).getGameModel();

        TextView tvGrade      = view.findViewById(R.id.tv_grade);
        TextView tvFinalScore = view.findViewById(R.id.tv_final_score);
        TextView tvCorrect    = view.findViewById(R.id.tv_stat_correct);
        TextView tvWrong      = view.findViewById(R.id.tv_stat_wrong);
        TextView tvAccuracy   = view.findViewById(R.id.tv_stat_accuracy);
        View     accuracyBar  = view.findViewById(R.id.accuracy_bar_fill);
        Button   btnPlayAgain = view.findViewById(R.id.btn_play_again);

        tvGrade.setText(model.getGrade());
        tvCorrect.setText(String.valueOf(model.correctCount));
        tvWrong.setText(String.valueOf(model.wrongCount));
        tvAccuracy.setText(model.getAccuracy() + "%");

        // Animate score count-up
        ValueAnimator scoreAnim = ValueAnimator.ofInt(0, model.score);
        scoreAnim.setDuration(800);
        scoreAnim.setInterpolator(new DecelerateInterpolator());
        scoreAnim.addUpdateListener(a -> tvFinalScore.setText(String.valueOf(a.getAnimatedValue())));
        scoreAnim.start();

        // Animate accuracy bar
        view.post(() -> {
            View barContainer = view.findViewById(R.id.accuracy_bar_container);
            int maxWidth = barContainer.getWidth();
            int targetWidth = (int) (maxWidth * model.getAccuracy() / 100.0);
            ValueAnimator barAnim = ValueAnimator.ofInt(0, targetWidth);
            barAnim.setDuration(900);
            barAnim.setStartDelay(300);
            barAnim.setInterpolator(new DecelerateInterpolator());
            barAnim.addUpdateListener(a -> {
                ViewGroup.LayoutParams params = accuracyBar.getLayoutParams();
                params.width = (int) a.getAnimatedValue();
                accuracyBar.setLayoutParams(params);
            });
            barAnim.start();
        });

        // Entry animation
        view.setAlpha(0f);
        view.setScaleX(0.92f);
        view.setScaleY(0.92f);
        view.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(350).start();

        btnPlayAgain.setOnClickListener(v ->
                ((MainActivity) requireActivity()).showSetup());
    }
}
