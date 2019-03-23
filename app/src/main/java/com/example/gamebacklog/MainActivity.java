package com.example.gamebacklog;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {
    //Constants used when calling the update activity
    public static final String EXTRA_GAME = "Game";
    public static final int CREATE_REQUESTCODE = 1234;
    public static final int UPDATE_REQUESTCODE = 42;
    public static final String UPDATE_GAME = "Update_game";


    private List<Game> Games;
    private GameAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private GestureDetector mGestureDetector;
    private MainViewModel mMainViewModel;
    private Executor executor = Executors.newSingleThreadExecutor();
    private GameRoomDatabase db;

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback;
    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize the instance variables
        Games = new ArrayList<>();
        mAdapter = new GameAdapter(Games);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addOnItemTouchListener(this);
        db = GameRoomDatabase.getDatabase(this);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mMainViewModel.getGames().observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                Games = games;
                updateUI();
            }
        });

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int adapterPosition = viewHolder.getAdapterPosition();

                final Game game = Games.get(adapterPosition);
                Snackbar.make(MainActivity.this.findViewById(android.R.id.content),
                        getString(R.string.delete) + " " + game.getGameTitle(),
                        Snackbar.LENGTH_LONG).setAction(R.string.undo,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mMainViewModel.insert(game);
                            }
                        }).show();

                mMainViewModel.delete(Games.get(adapterPosition));
            }
        };

        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameCreate.class);

                startActivityForResult(intent, CREATE_REQUESTCODE);
            }
        });

    }

    public void updateUI() {
        mAdapter.swapLIst(Games);
    }

    private void deleteAll() {
        final List<Game> deletedGames = Games;

        Snackbar.make(MainActivity.this.findViewById(android.R.id.content), R.string.deleted,
                Snackbar.LENGTH_LONG).setAction(R.string.undo,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMainViewModel.insert(deletedGames);
                    }
                }).show();

        mMainViewModel.delete(Games);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_item) {
            deleteAll();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK) {
            if(data != null) {
                final Game game = data.getParcelableExtra(EXTRA_GAME);
                if(game == null) {
                    return;
                }

                if(requestCode == CREATE_REQUESTCODE) {
                    executor.execute(new Runnable() {

                        @Override

                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mMainViewModel.insert(game);
                                }
                            });
                        }
                    });
                } else if (requestCode ==  UPDATE_REQUESTCODE) {
                    mMainViewModel.update(game);
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        int gameLocation = recyclerView.getChildAdapterPosition(child);

        if(child != null && mGestureDetector.onTouchEvent(motionEvent)) {
            Intent intent = new Intent(MainActivity.this, GameUpdate.class);

            intent.putExtra(UPDATE_GAME, Games.get(gameLocation));
            startActivityForResult(intent, UPDATE_REQUESTCODE);
        }

        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }
}
