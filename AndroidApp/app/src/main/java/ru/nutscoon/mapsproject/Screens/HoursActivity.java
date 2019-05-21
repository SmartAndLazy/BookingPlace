package ru.nutscoon.mapsproject.Screens;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.nutscoon.mapsproject.Adapters.HourRecyclerViewAdapter;
import ru.nutscoon.mapsproject.Models.AvaliableHours;
import ru.nutscoon.mapsproject.Models.Table;
import ru.nutscoon.mapsproject.R;
import ru.nutscoon.mapsproject.ViewModels.HoursViewModel;

public class HoursActivity extends AppCompatActivity {

    RecyclerView hoursRecyclerView;
    TextView workHoursTextView;
    TextView allPlaces;
    HoursViewModel viewModel;
    LinearLayout tableContent;
    int id;
    String date;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hours_layout);
        setupViewModel();
        hoursRecyclerView = findViewById(R.id.recyclerViewHours);
        allPlaces = findViewById(R.id.allPlaces);
        workHoursTextView = findViewById(R.id.workHoursTextView);
        tableContent = findViewById(R.id.table_content);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        date= intent.getStringExtra("date");

    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getavaliableHoursData(date, id);
    }

    private void setupViewModel(){
       viewModel = ViewModelProviders.of(this).get(HoursViewModel.class);
       viewModel.getAvaliableHoursMutableLiveData().observe(this, new Observer<AvaliableHours>() {
           @Override
           public void onChanged(@Nullable AvaliableHours avaliableHours) {
               if(avaliableHours != null){
                   fillContent(avaliableHours);
               }else {
                   showEmptyScreen();
               }
           }
       });

    }

    private void showEmptyScreen(){
        tableContent.setVisibility(View.GONE);
        allPlaces.setVisibility(View.GONE);
        workHoursTextView.setText("Информации по данному дню нет");
    }

    private  void  fillContent(AvaliableHours data)
    {
        allPlaces.setText(allPlaces.getText().toString() + " " + data.getsummaryNumberOfPlace());
        workHoursTextView.setText(workHoursTextView.getText()+" от "+ data.getstartTime() + " до" + data.getendTime());
        hoursRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        final HourRecyclerViewAdapter adapter = new HourRecyclerViewAdapter(this, data.getbookingPlaceInfo());
        hoursRecyclerView.setAdapter(adapter);
        adapter.setClickListener(new HourRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            Table item= adapter.getItem(position);
             Intent intent = new Intent();
                intent.putExtra("time", item.getfromHours());
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }


}
