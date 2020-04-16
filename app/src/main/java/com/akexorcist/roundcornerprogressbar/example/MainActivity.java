package com.akexorcist.roundcornerprogressbar.example;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.akexorcist.roundcornerprogressbar.CenteredRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private IconRoundCornerProgressBar progressOne;
    private RoundCornerProgressBar progressTwo;
    private TextRoundCornerProgressBar progressThree;
    private CenteredRoundCornerProgressBar progressFour;
    private TextView tvProgressOne;
    private TextView tvProgressTwo;
    private TextView tvProgressFour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressOne = findViewById(R.id.progress_one);
        progressOne.setProgressColor(getResources().getColor(R.color.custom_progress_blue_progress));
        progressOne.setSecondaryProgressColor(getResources().getColor(R.color.custom_progress_blue_progress_half));
        progressOne.setIconBackgroundColor(getResources().getColor(R.color.custom_progress_blue_header));
        progressOne.setProgressBackgroundColor(getResources().getColor(R.color.custom_progress_background));
        updateSecondaryProgressOne();

        tvProgressOne = findViewById(R.id.tv_progress_one);
        updateTextProgressOne();

        progressTwo = findViewById(R.id.progress_two);
        progressTwo.setProgressBackgroundColor(getResources().getColor(R.color.custom_progress_background));
        updateProgressTwoColor();

        tvProgressTwo = findViewById(R.id.tv_progress_two);
        updateTextProgressTwo();

        progressThree = findViewById(R.id.progress_three);
        updateTextProgressThree();

        progressFour = findViewById(R.id.progress_four);

        tvProgressFour  = findViewById(R.id.tv_progress_four);
        updateTextProgressTwo();

        findViewById(R.id.button_progress_one_decrease).setOnClickListener(this);
        findViewById(R.id.button_progress_one_increase).setOnClickListener(this);
        findViewById(R.id.button_progress_two_decrease).setOnClickListener(this);
        findViewById(R.id.button_progress_two_increase).setOnClickListener(this);
        findViewById(R.id.button_progress_three_decrease).setOnClickListener(this);
        findViewById(R.id.button_progress_three_increase).setOnClickListener(this);
        findViewById(R.id.button_progress_four_decrease).setOnClickListener(this);
        findViewById(R.id.button_progress_four_increase).setOnClickListener(this);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateTextProgressOne();
        updateTextProgressTwo();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_progress_one_decrease) {
            decreaseProgressOne();
        } else if (id == R.id.button_progress_one_increase) {
            increaseProgressOne();
        } else if (id == R.id.button_progress_two_decrease) {
            decreaseProgressTwo();
        } else if (id == R.id.button_progress_two_increase) {
            increaseProgressTwo();
        } else if (id == R.id.button_progress_three_decrease) {
            decreaseProgressThree();
        } else if (id == R.id.button_progress_three_increase) {
            increaseProgressThree();
        } else if (id == R.id.button_progress_four_decrease) {
            decreaseProgressFour();
        } else if (id == R.id.button_progress_four_increase) {
            increaseProgressFour();
        }
    }

    private void increaseProgressOne() {
        progressOne.setProgress(progressOne.getProgress() + 1);
        updateSecondaryProgressOne();
        updateTextProgressOne();
    }

    private void decreaseProgressOne() {
        progressOne.setProgress(progressOne.getProgress() - 1);
        updateSecondaryProgressOne();
        updateTextProgressOne();
    }

    private void increaseProgressTwo() {
        progressTwo.setProgress(progressTwo.getProgress() + 1);
        updateProgressTwoColor();
        updateTextProgressTwo();
    }

    private void decreaseProgressTwo() {
        progressTwo.setProgress(progressTwo.getProgress() - 1);
        updateProgressTwoColor();
        updateTextProgressTwo();
    }

    private void increaseProgressThree() {
        progressThree.setProgress(progressThree.getProgress() + 1);
        updateTextProgressThree();
    }

    private void decreaseProgressThree() {
        progressThree.setProgress(progressThree.getProgress() - 1);
        updateTextProgressThree();
    }

    private void increaseProgressFour() {
        progressFour.setProgress(progressFour.getProgress() + 1);
        updateTextProgressFour();
    }

    private void decreaseProgressFour() {
        progressFour.setProgress(progressFour.getProgress() - 1);
        updateTextProgressFour();
    }

    private void updateSecondaryProgressOne() {
        progressOne.setSecondaryProgress(progressOne.getProgress() + 2);
    }

    private void updateProgressTwoColor() {
        float progress = progressTwo.getProgress();
        if (progress <= 3) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_red_progress));
        } else if (progress > 3 && progress <= 6) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_orange_progress));
        } else if (progress > 6) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_green_progress));
        }
    }

    private void updateTextProgressOne() {
        tvProgressOne.setText(String.valueOf((int) progressOne.getProgress()));
    }

    private void updateTextProgressTwo() {
        tvProgressTwo.setText(String.valueOf((int) progressTwo.getProgress()));
    }

    private void updateTextProgressThree() {
        progressThree.setProgressText(String.valueOf((int) progressThree.getProgress()));
    }

    private void updateTextProgressFour() {
        tvProgressFour.setText(String.valueOf((int) progressFour.getProgress()));
    }
}
