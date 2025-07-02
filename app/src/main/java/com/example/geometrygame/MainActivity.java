package com.example.geometrygame;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    /*
    Чтобы менять цвет по заданному количеству нажатий, нужно их считать

     */
    int count = 0;

    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.main);

        /*
        Чтобы повторялись дейтсвия, нужен цикл
         */



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * Метод, который будет вызываться при нажатии на кнопку
     * @param view сама кнопка
     */
    public void onClick(View view) {
        int id = view.getId();     // Получаем идентификатор нажатой кнопки
        Intent intent;
        if(id == R.id.btn_change_color) {  // Если нажади на сменить цвет, начинаем менять цвет
            startChanging();
        }
        else if(id == R.id.btn_character){   // Еще если нажали на кнопку Персонаж, выводим сообщеие о выборе персонажа
            Toast.makeText(this,"Нажали на персонажа", Toast.LENGTH_LONG ).show();
        }
        else if (id==R.id.btn_play) {
            Toast.makeText(this,"Нажали на играть", Toast.LENGTH_LONG ).show();
        }
        else if (id==R.id.btn_exit) {
            Toast.makeText(this,"Нажали на выход", Toast.LENGTH_LONG ).show();
        }
        else if (id==R.id.btn_settings) {
            Toast.makeText(this,"Нажали на настройки", Toast.LENGTH_LONG ).show();
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }

    }

    private Handler handler = new Handler(Looper.getMainLooper());

    public void startChanging() {
        handler.postDelayed(changeRunnable, 1000); // стартует через 1 секунду
    }



    private final Runnable changeRunnable = new Runnable() {
        @Override
        public void run() {
            int colorResId;

            switch (count) {
                case 0:
                    colorResId = R.color.seryi;
                    break;
                case 1:
                    colorResId = R.color.zelenyi;
                    break;
                case 2:
                    colorResId = R.color.goluboy;
                    break;
                case 3:
                    colorResId = R.color.krasnyi;
                    break;
                case 4:
                    colorResId = R.color.fioletovyi;
                    break;
                default:
                    colorResId = R.color.seryi;
            }

            constraintLayout.setBackgroundColor(
                    ContextCompat.getColor(constraintLayout.getContext(), colorResId)
            );

            count = (count + 1) % 5; // 0 -> 1 -> ... -> 4 -> 0

            handler.postDelayed(this, 1); // повтор через 1 секунду
        }
    };
}