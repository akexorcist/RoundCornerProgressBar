/*
Copyright 2015 Akexorcist
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.akexorcist.roundcornerprogressbar.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;

public class MainActivity extends Activity implements View.OnClickListener {

    private IconRoundCornerProgressBar progressOne;
    private RoundCornerProgressBar progressTwo;
    private TextRoundCornerProgressBar progressThree;
    private TextRoundCornerProgressBar progressFour;
    private TextView textProgressOne;
    private TextView textProgressTwo;
    private Button btnProgressOneDecrease;
    private Button btnProgressOneIncrease;
    private Button btnProgressTwoDecrease;
    private Button btnProgressTwoIncrease;

    private int progress1 = 5;
    private int progress2 = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressOne = (IconRoundCornerProgressBar) findViewById(R.id.progress_one);
        progressOne.setProgressColor(
                getResources().getColor(R.color.custom_progress_blue_progress),
                getResources().getColor(R.color.custom_progress_blue_progress_half)
        );
        progressOne.setHeaderColor(getResources().getColor(R.color.custom_progress_blue_header));
        progressOne.setBackgroundColor(getResources().getColor(R.color.custom_progress_background));
        textProgressOne = (TextView) findViewById(R.id.text_progress_one);
        btnProgressOneDecrease = (Button) findViewById(R.id.button_progress_one_decrease);
        btnProgressOneDecrease.setOnClickListener(this);
        btnProgressOneIncrease = (Button) findViewById(R.id.button_progress_one_increase);
        btnProgressOneIncrease.setOnClickListener(this);
        updateProgressOne();

        progressThree = (TextRoundCornerProgressBar) findViewById(R.id.progress_three);
        progressFour = (TextRoundCornerProgressBar) findViewById(R.id.progress_four);
        progressTwo = (RoundCornerProgressBar) findViewById(R.id.progress_two);
        progressTwo.setBackgroundColor(getResources().getColor(R.color.custom_progress_background));
        textProgressTwo = (TextView) findViewById(R.id.text_progress_two);
        btnProgressTwoDecrease = (Button) findViewById(R.id.button_progress_two_decrease);
        btnProgressTwoDecrease.setOnClickListener(this);
        btnProgressTwoIncrease = (Button) findViewById(R.id.button_progress_two_increase);
        btnProgressTwoIncrease.setOnClickListener(this);
        updateProgressTwo();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_progress_one_decrease:
                progress1 = (progress1 > 0) ? progress1 - 1 : progress1;
                updateProgressOne();
                break;
            case R.id.button_progress_one_increase:
                progress1 = (progress1 < progressOne.getMax()) ? progress1 + 1 : progress1;
                updateProgressOne();
                break;
            case R.id.button_progress_two_decrease:
                progress2 = (progress2 > 0) ? progress2 - 1 : progress2;
                updateProgressTwo();
                break;
            case R.id.button_progress_two_increase:
                progress2 = (progress2 < progressTwo.getMax()) ? progress2 + 1 : progress2;
                updateProgressTwo();
                break;
        }
    }

    private void updateProgressOne() {
        progressOne.setProgress(progress1);
        progressOne.setSecondaryProgress(progress1 + 1);
        textProgressOne.setText("" + progress1);
    }

    private void updateProgressTwo() {
        progressTwo.setProgress(progress2);
        progressTwo.setSecondaryProgress(progress2 + 2);
        progressThree.setProgress(progress2);
        progressFour.setProgress(progress2);
        progressFour.setSecondaryProgress(progress2 + 1);
        textProgressTwo.setText("" + progress2);
        updateProgressTwoColor();
    }

    private void updateProgressTwoColor() {
        if(progress2 <= 3) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_red_progress));
        } else if(progress2 > 3 && progress2 <= 6) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_orange_progress));
        } else if(progress2 > 6) {
            progressTwo.setProgressColor(getResources().getColor(R.color.custom_progress_green_progress));
        }
    }
}
