package com.example.gamebacklog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

public class GameUpdate extends AppCompatActivity {

    private TextView title;
    private TextView platform;
    private Spinner status;

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_update);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.gameTitle);
        platform = findViewById(R.id.gamePlatform);
        status = findViewById(R.id.statusSpinner);

            game = getIntent().getExtras().getParcelable(MainActivity.UPDATE_GAME);
            title.setText(game.getGameTitle());
            platform.setText(game.getGamePlatform());
            status.setSelection(game.getGameStatus().getValue());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.setGameTitle(title.getText().toString());
                game.setGamePlatform(platform.getText().toString());
                game.setGameDate(Calendar.getInstance().getTimeInMillis());
                game.setGameStatus(GameStatusConverter.toGameStatus(status.getSelectedItemPosition()));

                Intent intent = new Intent();
                intent.putExtra(MainActivity.UPDATE_GAME, game);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setResult(Activity.RESULT_CANCELED, null);
        finish();

        return true;
    }
}
