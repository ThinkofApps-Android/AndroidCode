package com.speedmath.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SetupFragment extends Fragment {

    private GameModel model;
    private TextView tvTimerDisplay;

    // Op buttons
    private Button btnAdd, btnSub, btnMul, btnDiv;
    // Diff buttons
    private Button btnEasy, btnMedium, btnHard;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = ((MainActivity) requireActivity()).getGameModel();

        tvTimerDisplay = view.findViewById(R.id.tv_timer_display);
        SeekBar seekBar = view.findViewById(R.id.seekbar_timer);

        btnAdd    = view.findViewById(R.id.btn_op_add);
        btnSub    = view.findViewById(R.id.btn_op_sub);
        btnMul    = view.findViewById(R.id.btn_op_mul);
        btnDiv    = view.findViewById(R.id.btn_op_div);

        btnEasy   = view.findViewById(R.id.btn_diff_easy);
        btnMedium = view.findViewById(R.id.btn_diff_medium);
        btnHard   = view.findViewById(R.id.btn_diff_hard);

        Button btnStart = view.findViewById(R.id.btn_start);

        // Timer seekbar: steps of 15 from 15..120 → 7 steps (0..7)
        seekBar.setMax(7);
        int initialProgress = (model.timerSeconds / 15) - 1;
        seekBar.setProgress(initialProgress);
        tvTimerDisplay.setText(model.formatTime(model.timerSeconds));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                model.timerSeconds = (progress + 1) * 15;
                tvTimerDisplay.setText(model.formatTime(model.timerSeconds));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Operation buttons
        View.OnClickListener opListener = v -> {
            if (v == btnAdd)      model.operation = GameModel.Operation.ADD;
            else if (v == btnSub) model.operation = GameModel.Operation.SUB;
            else if (v == btnMul) model.operation = GameModel.Operation.MUL;
            else                  model.operation = GameModel.Operation.DIV;
            refreshOpButtons();
        };
        btnAdd.setOnClickListener(opListener);
        btnSub.setOnClickListener(opListener);
        btnMul.setOnClickListener(opListener);
        btnDiv.setOnClickListener(opListener);

        // Difficulty buttons
        View.OnClickListener diffListener = v -> {
            if (v == btnEasy)        model.difficulty = GameModel.Difficulty.EASY;
            else if (v == btnMedium) model.difficulty = GameModel.Difficulty.MEDIUM;
            else                     model.difficulty = GameModel.Difficulty.HARD;
            refreshDiffButtons();
        };
        btnEasy.setOnClickListener(diffListener);
        btnMedium.setOnClickListener(diffListener);
        btnHard.setOnClickListener(diffListener);

        refreshOpButtons();
        refreshDiffButtons();

        btnStart.setOnClickListener(v -> {
            model.startSession();
            ((MainActivity) requireActivity()).showGame();
        });
    }

    private void refreshOpButtons() {
        setOpSelected(btnAdd,  model.operation == GameModel.Operation.ADD);
        setOpSelected(btnSub,  model.operation == GameModel.Operation.SUB);
        setOpSelected(btnMul,  model.operation == GameModel.Operation.MUL);
        setOpSelected(btnDiv,  model.operation == GameModel.Operation.DIV);
    }

    private void refreshDiffButtons() {
        setDiffSelected(btnEasy,   model.difficulty == GameModel.Difficulty.EASY);
        setDiffSelected(btnMedium, model.difficulty == GameModel.Difficulty.MEDIUM);
        setDiffSelected(btnHard,   model.difficulty == GameModel.Difficulty.HARD);
    }

    private void setOpSelected(Button btn, boolean selected) {
        if (selected) {
            btn.setBackgroundResource(R.drawable.bg_btn_selected);
            btn.setTextColor(Color.parseColor("#a78bfa"));
        } else {
            btn.setBackgroundResource(R.drawable.bg_btn_normal);
            btn.setTextColor(Color.parseColor("#64748b"));
        }
    }

    private void setDiffSelected(Button btn, boolean selected) {
        setOpSelected(btn, selected);
    }
}
