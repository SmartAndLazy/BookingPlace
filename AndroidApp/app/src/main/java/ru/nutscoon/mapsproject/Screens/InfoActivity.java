package ru.nutscoon.mapsproject.Screens;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.GeocodeRequest;
import com.here.android.mpa.search.GeocodeResult;
import com.here.android.mpa.search.ResultListener;

import java.util.List;

import ru.nutscoon.mapsproject.CommentDialog;
import ru.nutscoon.mapsproject.Models.OrganizationData;
import ru.nutscoon.mapsproject.Models.UserRating;
import ru.nutscoon.mapsproject.R;
import ru.nutscoon.mapsproject.ViewModels.InfoViewModel;

public class InfoActivity extends AppCompatActivity {

    private InfoViewModel viewModel;
    private String mOrgName;
    private String orgLogoUrl;
    private double lat;
    private double lon;

    private ImageView vk;
    private ImageView facebook;
    private ImageView inst;

    private ImageView logo;
    private RatingBar ratingBar;
    private TextView username;
    private TextView login;
    private TextView logout;
    private TextView name;
    private TextView type;
    private TextView address;
    private TextView workingTimes;
    private TextView phone;
    private TextView description;
    private TextView space;
    private TextView addCommetn;
    private Button bookBtn;
    LinearLayout commentContainer;
    LinearLayout loginContainer;

    private int orgId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info);
        setupViewModel();
        Intent intent = getIntent();
        mOrgName = intent.getStringExtra("orgName");
        orgLogoUrl = intent.getStringExtra("url");
        lat = intent.getDoubleExtra("latitude", 0);
        lon = intent.getDoubleExtra("longitude", 0);

        vk = findViewById(R.id.org_vk);
        facebook = findViewById(R.id.org_facebook);
        inst = findViewById(R.id.org_inst);

        ratingBar = findViewById(R.id.org_rating);
        logo = findViewById(R.id.org_logo);
        name = findViewById(R.id.org_name);
        type = findViewById(R.id.org_type);
        address = findViewById(R.id.org_address);
        workingTimes = findViewById(R.id.org_working_times);
        phone = findViewById(R.id.org_phone);
        description = findViewById(R.id.org_description);
        space = findViewById(R.id.org_space);
        commentContainer = findViewById(R.id.org_comment_container);
        loginContainer = findViewById(R.id.org_login_container);
        username = findViewById(R.id.org_username);
        login = findViewById(R.id.org_login);
        logout = findViewById(R.id.org_logout);
        bookBtn = findViewById(R.id.org_book_btn);
        addCommetn = findViewById(R.id.org_add_comment);
        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, BookPlaceActivity.class);
                intent.putExtra("orgId", orgId);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InfoActivity.this, LoginActivity.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.logout();
                loginContainer.setVisibility(View.GONE);
                login.setVisibility(View.VISIBLE);
            }
        });
        addCommetn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(viewModel.isUserLoggedIn()){
                    CommentDialog dialog = new CommentDialog();
                    dialog.setPostCommentCallback(new CommentDialog.PostCommentCallback() {
                        @Override
                        public void onPost(double rate, String comment) {
                            viewModel.postComment(comment, rate);
                        }
                    });
                    dialog.show(getSupportFragmentManager(), "");
                }else {
                    startActivity(new Intent(InfoActivity.this, LoginActivity.class));
                }
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall(phone.getText().toString());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.getOrganizationInfo(lat, lon, mOrgName);
        viewModel.loadUserName();
    }

    private void setupViewModel(){
        viewModel = ViewModelProviders.of(this).get(InfoViewModel.class);
        viewModel.getOrganizationDataLiveData().observe(this, new Observer<OrganizationData>() {
            @Override
            public void onChanged(@Nullable OrganizationData organizationsOnMapData) {
                if(organizationsOnMapData != null){
                    fillScreen(organizationsOnMapData, false);
                }
            }
        });
        viewModel.getInfoResultLiveData().observe(this, new Observer<InfoViewModel.GetInfoResult>() {
            @Override
            public void onChanged(@Nullable InfoViewModel.GetInfoResult getInfoResult) {
                if(getInfoResult == InfoViewModel.GetInfoResult.ERROR){
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewModel.getLoginStatus().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String userName) {
                if(userName != null){
                    login.setVisibility(View.GONE);
                    loginContainer.setVisibility(View.VISIBLE);
                    username.setVisibility(View.VISIBLE);
                    username.setText(userName);
                }else {
                    login.setVisibility(View.VISIBLE);
                    username.setVisibility(View.GONE);
                    loginContainer.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getCommentSendResult().observe(this, new Observer<OrganizationData>() {
            @Override
            public void onChanged(@Nullable OrganizationData organizationData) {
                if(organizationData != null){
                    fillScreen(organizationData, true);
                }else {
                    Toast.makeText(getApplicationContext(), "Не удалось отправить комментарий", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void fillScreen(final OrganizationData organizationData, boolean isRecreate){
        orgId = organizationData.getId();
        name.setText(organizationData.getName());
        phone.setText(organizationData.getPhone());
        address.setText(organizationData.getAddress());
        type.setText(organizationData.getCategories());
        workingTimes.setText(organizationData.getWorkingTimeStr());
        description.setText(organizationData.getDescription());
        String spaceText = "Свободных мест: " + organizationData.getCountOfAvailablePlacement() + " из " + organizationData.getTotalSpaceCount();
        space.setText(spaceText);
        ratingBar.setRating((float)organizationData.getRating());
        if(organizationData.getUrl() != null){
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openUrl(name.getText().toString());
                }
            });
        }
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openUrl(organizationData.getUrl());
            }
        });

        UserRating[] comments = organizationData.getUserRatings();

        if (comments != null && comments.length > 0){
            if(isRecreate){
                commentContainer.removeAllViews();
                for(UserRating c : comments){
                    commentContainer.addView(getCommentView(c.getName() + " " + c.getSurname(), c.getText()));
                }
            }
        }

        OrganizationData.OrganizationSocialNetwork[] urls = organizationData.getOrganizationSocialNetworks();
        if(urls != null && urls.length > 0){
            for(final OrganizationData.OrganizationSocialNetwork url : urls){
                if(url.getType() == 1){
                    vk.setVisibility(View.VISIBLE);
                    vk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openUrl(url.getUrl());
                        }
                    });
                }

                if(url.getType() == 2){
                    inst.setVisibility(View.VISIBLE);
                    inst.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openUrl(url.getUrl());
                        }
                    });
                }

                if(url.getType() == 3){
                    facebook.setVisibility(View.VISIBLE);
                    facebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openUrl(url.getUrl());
                        }
                    });
                }
            }
        }
    }

    private void openUrl(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private View getCommentView(String username, String text){
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View v = li.inflate(R.layout.comment_view, null, false);
        TextView usernameTv = v.findViewById(R.id.comment_username);
        TextView textTv = v.findViewById(R.id.comment_text);
        usernameTv.setText(username);
        textTv.setText(text);
        return v;
    }

    private void makeCall(String phone){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }
}
