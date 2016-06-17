package br.org.fit_tecnologia.fisio;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;


public class Fisio extends AppCompatActivity {
    private static final int EXERCISE_TIME = 30100;
    private static final int REST_TIME = 10100;
    private static final int NEXT_TIME = 20100;
    private boolean run = false;
    private int exercise = 0;
    private int iteration = 1;
    private int switchTime;
    private TextView textBox;
    private Chronometer cron;

    static final String[] exercises = {
            "Alongamento com fita", "Compressão joelho", "Extensão sentado",
            "Abertura com elástico", "Compressão coxa em travesseiro"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisio);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private void Reset() {
        exercise = 0;
        iteration = 1;
        run = false;
        textBox.setText("Exercício");
        cron.stop();
        cron.setText("00:00");
        cron.setBase(SystemClock.elapsedRealtime());
    }

    @Override
    protected void onPostCreate(final Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        textBox = (TextView) findViewById(R.id.textView);
        cron = (Chronometer) findViewById(R.id.chronometer);
        assert cron != null;
        cron.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (iteration == 5 & exercise == 4) {
                    Reset();
                    textBox.setText("Finalizado");
                } else if (SystemClock.elapsedRealtime() - chronometer.getBase() > switchTime)
                    if (switchTime == EXERCISE_TIME) {
                        cron.setBase(SystemClock.elapsedRealtime());
                        if (iteration == 5) {
                            exercise++;
                            iteration = 1;
                            switchTime = NEXT_TIME;
                            textBox.setText("Preparar " + exercises[exercise]);
                        } else {
                            iteration++;
                            switchTime = REST_TIME;
                            textBox.setText("Descanso...");
                        }
                    } else {
                        switchTime = EXERCISE_TIME;
                        cron.setBase(SystemClock.elapsedRealtime());
                        textBox.setText(exercises[exercise] + " #" + iteration);
                    }
            }
        });


        final Button resetButton = (Button) findViewById(R.id.resetB);
        if (resetButton != null) {
            resetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Reset();
                }
            });
        }

        final Button startButton = (Button) findViewById(R.id.button);
        if (startButton != null) {
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (run) {
                        cron.stop();
                        startButton.setText("Start");
                        resetButton.setEnabled(true);
                        run = false;
                    } else {
                        cron.setBase(SystemClock.elapsedRealtime());
                        cron.start();
                        resetButton.setEnabled(false);
                        switchTime = EXERCISE_TIME;
                        startButton.setText("Pause");
                        textBox.setText(exercises[exercise] + " #" + iteration);
                        run = true;
                    }
                }
            });
        }
    }
}
