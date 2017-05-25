package com.fondesa.recyclerviewdivider.sample;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fondesa.recyclerviewdivider.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int LIST_SIZE = 16;
    private static final int SPAN_COUNT = 3;
    private static final String SHOW = "ALL";
    private static final String REMOVE = "REMOVE";
    private boolean dividerShown;

    private RecyclerViewDivider firstDivider;
    private RecyclerViewDivider secondDivider;

    private RecyclerView mFirstRecyclerView;
    private RecyclerView mSecondRecyclerView;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirstRecyclerView = (RecyclerView) findViewById(R.id.first_recycler_view);
        GridLayoutManager mFirstManager = (GridLayoutManager) mFirstRecyclerView.getLayoutManager();
        mFirstManager.setSpanCount(SPAN_COUNT);
        mFirstManager.setSpanSizeLookup(new DummyLookup());

        mSecondRecyclerView = (RecyclerView) findViewById(R.id.second_recycler_view);
        GridLayoutManager mSecondManager = (GridLayoutManager) mSecondRecyclerView.getLayoutManager();
        mSecondManager.setSpanCount(SPAN_COUNT);
        mSecondManager.setSpanSizeLookup(new DummyLookup());

        firstDivider = RecyclerViewDivider.with(this)
                .color(Color.RED)
                .size(12)
                .build();

        firstDivider.addTo(mFirstRecyclerView);

        secondDivider = RecyclerViewDivider.with(this)
                .color(Color.BLACK)
                .size(6)
                .marginSize(30)
                .build();

        secondDivider.addTo(mSecondRecyclerView);

        dividerShown = true;

        final List<Integer> dummyList = new ArrayList<>(LIST_SIZE);
        for (int i = 0; i < LIST_SIZE; i++) {
            dummyList.add(i + 1);
        }
        final DummyAdapter firstDummyAdapter = new DummyAdapter(true);
        mFirstRecyclerView.setAdapter(firstDummyAdapter);
        final DummyAdapter secondDummyAdapter = new DummyAdapter(false);
        mSecondRecyclerView.setAdapter(secondDummyAdapter);

        firstDummyAdapter.updateList(dummyList);
        secondDummyAdapter.updateList(dummyList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_toggle_div).setTitle(dividerShown ? REMOVE : SHOW);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_toggle_div) {
            if (dividerShown) {
                firstDivider.removeFrom(mFirstRecyclerView);
                secondDivider.removeFrom(mSecondRecyclerView);
            } else {
                firstDivider.addTo(mFirstRecyclerView);
                secondDivider.addTo(mSecondRecyclerView);
            }
            dividerShown = !dividerShown;
            ActivityCompat.invalidateOptionsMenu(this);
        }
        return true;
    }

    private class DummyAdapter extends RecyclerView.Adapter {
        private List<Integer> list;
        private final boolean first;

        DummyAdapter(boolean first) {
            this.list = new ArrayList<>();
            this.first = first;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DummyViewHolder(LayoutInflater.from(MainActivity.this).inflate(first ? R.layout.dummy_cell_first : R.layout.dummy_cell_second, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((DummyViewHolder) holder).setItemText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        void updateList(List<Integer> list) {
            if (list != this.list) {
                this.list = list;
                notifyDataSetChanged();
            }
        }
    }

    private static class DummyViewHolder extends RecyclerView.ViewHolder {
        DummyViewHolder(View itemView) {
            super(itemView);
        }

        void setItemText(int item) {
            ((TextView) itemView).setText(String.valueOf(item));
        }
    }

    private static class DummyLookup extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            return position % 3 + 1;
        }
    }
}
