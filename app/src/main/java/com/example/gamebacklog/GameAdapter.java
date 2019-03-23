package com.example.gamebacklog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {
    private List<Game> mGames;

    public GameAdapter(List<Game> mGames) {
        this.mGames = mGames;
    }

    @NonNull
    @Override
    public GameAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_game_cardview, viewGroup, false);

        // Return a new holder instance
        GameAdapter.ViewHolder viewHolder = new GameAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.ViewHolder viewHolder, int i) {
        Game game = mGames.get(i);
        viewHolder.updateUI(game);
    }

    public void swapLIst(List<Game> gameList) {
        mGames = gameList;
        if (gameList != null) {
            this.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView gameTitle;
        TextView gamePlatform;
        TextView gameDate;
        TextView gameStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gamePlatform = itemView.findViewById(R.id.gamePlatform);
            gameDate = itemView.findViewById(R.id.gameDate);
            gameStatus = itemView.findViewById(R.id.gameStatus);
        }

        public void updateUI(Game game) {
            gameTitle.setText(game.getGameTitle());
            gamePlatform.setText(game.getGamePlatform());
            gameStatus.setText(game.getGameStatus().toString());

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(game.getGameDate());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY", new Locale("NL_nl"));
            gameDate.setText(dateFormat.format(calendar.getTime()));
        }
    }
}