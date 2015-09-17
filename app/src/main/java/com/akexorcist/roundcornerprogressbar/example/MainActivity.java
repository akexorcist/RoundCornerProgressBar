package com.akexorcist.roundcornerprogressbar.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private IconRoundCornerProgressBar progressOne;
    private RoundCornerProgressBar progressTwo;
    private TextRoundCornerProgressBar progressThree;
    private TextView tvProgressOne;
    private TextView tvProgressTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressOne = (IconRoundCornerProgressBar) findViewById(R.id.progress_one);
        progressOne.setProgressColor(getResources().getColor(R.color.custom_progress_blue_progress));
        progressOne.setSecondaryProgressColor(getResources().getColor(R.color.custom_progress_blue_progress_half));
        progressOne.setIconBackgroundColor(getResources().getColor(R.color.custom_progress_blue_header));
        progressOne.setProgressBackgroundColor(getResources().getColor(R.color.custom_progress_background));
        updateSecondaryProgressOne();

        tvProgressOne = (TextView) findViewById(R.id.tv_progress_one);
        updateTextProgressOne();

        progressTwo = (RoundCornerProgressBar) findViewById(R.id.progress_two);
        progressTwo.setProgressBackgroundColor(getResources().getColor(R.color.custom_progress_background));
        updateProgressTwoColor();

        tvProgressTwo = (TextView) findViewById(R.id.tv_progress_two);
        updateTextProgressTwo();

        progressThree = (TextRoundCornerProgressBar) findViewById(R.id.progress_three);
        updateTextProgressThree();

        findViewById(R.id.button_progress_one_decrease).setOnClickListener(this);
        findViewById(R.id.button_progress_one_increase).setOnClickListener(this);
        findViewById(R.id.button_progress_two_decrease).setOnClickListener(this);
        findViewById(R.id.button_progress_two_increase).setOnClickListener(this);
        findViewById(R.id.button_progress_three_decrease).setOnClickListener(this);
        findViewById(R.id.button_progress_three_increase).setOnClickListener(this);

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

    private void updateSecondaryProgressOne() {
        progressOne.setSecondaryProgress(progressOne.getProgress() + 2);
    }

    private void updateProgressTwoColor() {
        float progress = progressTwo.getProgress();
        if(progress <= 3) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_red_progress));
        } else if(progress > 3 && progress <= 6) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_orange_progress));
        } else if(progress > 6) {
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
}
