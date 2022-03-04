package com.example.MoneyManager.mainactivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.MoneyManager.R;
import com.example.MoneyManager.budget_categories.BudgetCategoryManager;
import com.example.MoneyManager.budgetplanner.PlansManager;
import com.example.MoneyManager.dbmanagment.DatabaseController;
import com.example.MoneyManager.monthestracker.MonthsTracker;
import com.example.MoneyManager.transactions.TransactionManager;
import com.example.MoneyManager.usermoney.UserWallet;
import com.example.MoneyManager.usermoney.ui.AddingIncomeFragment;
import com.example.MoneyManager.usermoney.ui.UserWalletFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    DatabaseController databaseController;

    BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        BiometricManager biometricManager = BiometricManager.from(this);

        switch(biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(MainActivity.this, "Device Doesn't Have Fingerprint" +
                        "!", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(MainActivity.this, "Not Working" +
                        "!", Toast.LENGTH_SHORT).show();
                break;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(MainActivity.this, "No Fingerprint Assigned" +
                        "!", Toast.LENGTH_SHORT).show();
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(MainActivity.this, "You Don't Want To Open App!", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(MainActivity.this, "Login Success!", Toast.LENGTH_SHORT).show();
                drawerLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Login")
                .setDescription("Use Fingerprint To Login").setDeviceCredentialAllowed(true).build();
        biometricPrompt.authenticate(promptInfo);

        try {

            databaseController = new DatabaseController(this);
            BudgetCategoryManager.LoadCategoryEnvelop();
            MonthsTracker.checkUpdates(this);
            TransactionManager.loadTransactions();
            PlansManager.loadPlans();
            UserWallet.loadUserMoney(this);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);

            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_budgetManager , R.id.nav_budget_analysis ,R.id.nav_budgetPlanner
                    ,R.id.nav_monthly_expenses_analysis)
                    .setDrawerLayout(drawer)
                    .build();

            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.add_income:
                addIncome();
                return true;
            case R.id.user_wallet:
                showUserWallet();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void addIncome() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        AddingIncomeFragment addingIncomeFragment = new AddingIncomeFragment();

        addingIncomeFragment.show(fragmentManager,"add income");
    }

    private void showUserWallet() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        UserWalletFragment userWalletFragment = new UserWalletFragment();

        userWalletFragment.show(fragmentManager,"add income");
    }

    // TODO // Database contain(plans , transactions , budget categories)
}