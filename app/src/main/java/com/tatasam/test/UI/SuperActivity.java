package com.tatasam.test.UI;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
public class SuperActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateToolbar(String toolbartext){
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(toolbartext);
        }

    }
    public void updateToolbarWithoutBackButton(String toolbartext){
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(toolbartext);
        }

    }
    public void hideToolbar(){
        if(getSupportActionBar()!=null) {
            getSupportActionBar().hide();
        }
    }

}
