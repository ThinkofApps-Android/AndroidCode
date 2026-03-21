package com.speedmath.app;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private GameModel gameModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameModel = new GameModel();

        if (savedInstanceState == null) {
            showSetup();
        }
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void showSetup() {
        switchFragment(new SetupFragment(), false);
    }

    public void showGame() {
        switchFragment(new GameFragment(), true);
    }

    public void showResults() {
        switchFragment(new ResultsFragment(), true);
    }

    private void switchFragment(Fragment fragment, boolean animate) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (animate) {
            ft.setCustomAnimations(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            );
        }
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }
}
