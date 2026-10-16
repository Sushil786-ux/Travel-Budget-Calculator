package com.example.travelbudgetcalculatorapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    EditText edtDestination;
    EditText edtDistance;
    EditText edtTravellers;
    EditText edtBudget;

    EditText edtTransport;
    EditText edtHotel;
    EditText edtFood;
    EditText edtActivities;
    EditText edtOther;

    Button btnCalculate;
    Button btnReset;

    TextView txtDestination;
    TextView txtTotalCost;
    TextView txtPerPerson;
    TextView txtRemaining;
    TextView txtDistance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        edtDestination = findViewById(R.id.edtDestination);
        edtDistance = findViewById(R.id.edtDistance);
        edtTravellers = findViewById(R.id.edtTravellers);
        edtBudget = findViewById(R.id.edtBudget);
        edtTransport = findViewById(R.id.edtTransport);
        edtHotel = findViewById(R.id.edtHotel);
        edtFood = findViewById(R.id.edtFood);
        edtActivities = findViewById(R.id.edtActivities);
        edtOther = findViewById(R.id.edtOther);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        txtDestination = findViewById(R.id.txtDestination);
        txtTotalCost = findViewById(R.id.txtTotalCost);
        txtPerPerson = findViewById(R.id.txtPerPerson);
        txtRemaining = findViewById(R.id.txtRemaining);
        txtDistance = findViewById(R.id.txtDistance);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateBudget();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCalculator();
            }
        });

    }

    private void resetCalculator() {
        edtDestination.setText("");
        edtDistance.setText("");
        edtTravellers.setText("1");
        edtBudget.setText("");

        edtTransport.setText("0");
        edtHotel.setText("0");
        edtFood.setText("0");
        edtActivities.setText("0");
        edtOther.setText("0");

        txtDestination.setText("Destination: -");
        txtTotalCost.setText("Total Trip Cost: ₹0");
        txtPerPerson.setText("Per Person Cost: ₹0");

        txtRemaining.setText("Budget Remaining: ₹0");
        txtRemaining.setTextColor(
                android.graphics.Color.BLACK
        );

        txtDistance.setText("Total Distance: 0 KM");

        edtDestination.requestFocus();
    }

    private void calculateBudget() {
        String destination =
                edtDestination.getText().toString().trim();

        String distanceText =
                edtDistance.getText().toString().trim();

        String travellersText =
                edtTravellers.getText().toString().trim();

        String budgetText =
                edtBudget.getText().toString().trim();

        // Validate required fields

        if (destination.isEmpty()) {
            edtDestination.setError("Enter destination");
            edtDestination.requestFocus();
            return;
        }

        if (distanceText.isEmpty()) {
            edtDistance.setError("Enter distance");
            edtDistance.requestFocus();
            return;
        }

        if (travellersText.isEmpty()) {
            edtTravellers.setError("Enter number of travellers");
            edtTravellers.requestFocus();
            return;
        }

        if (budgetText.isEmpty()) {
            edtBudget.setError("Enter your total budget");
            edtBudget.requestFocus();
            return;
        }

        double distance;
        int travellers;
        double budget;

        double transport;
        double hotel;
        double food;
        double activities;
        double other;

        try {

            distance = Double.parseDouble(distanceText);
            travellers = Integer.parseInt(travellersText);
            budget = Double.parseDouble(budgetText);

            transport = getAmount(edtTransport);
            hotel = getAmount(edtHotel);
            food = getAmount(edtFood);
            activities = getAmount(edtActivities);
            other = getAmount(edtOther);

        } catch (NumberFormatException e) {

            Toast.makeText(
                    this,
                    "Please enter valid numbers",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Validate numeric values

        if (distance <= 0) {
            edtDistance.setError("Distance must be greater than 0");
            return;
        }

        if (travellers <= 0) {
            edtTravellers.setError("Enter at least 1 traveller");
            return;
        }

        if (budget < 0) {
            edtBudget.setError("Budget cannot be negative");
            return;
        }

        if (transport < 0 || hotel < 0 || food < 0
                || activities < 0 || other < 0) {

            Toast.makeText(
                    this,
                    "Expenses cannot be negative",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Calculate total trip expenses

        double totalCost =
                transport + hotel + food + activities + other;

        // Calculate cost per person

        double perPersonCost = totalCost / travellers;

        // Calculate remaining budget

        double remainingBudget = budget - totalCost;

        // Display results

        txtDestination.setText("Destination: " + destination);

        txtTotalCost.setText(
                "Total Trip Cost: " + formatMoney(totalCost)
        );

        txtPerPerson.setText(
                "Per Person Cost: " + formatMoney(perPersonCost)
        );

        if (remainingBudget >= 0) {

            txtRemaining.setText(
                    "Budget Remaining: " + formatMoney(remainingBudget)
            );

            txtRemaining.setTextColor(
                    android.graphics.Color.rgb(0, 120, 0)
            );

        } else {

            txtRemaining.setText(
                    "Budget Exceeded By: " +
                            formatMoney(Math.abs(remainingBudget))
            );

            txtRemaining.setTextColor(
                    android.graphics.Color.RED
            );
        }

        txtDistance.setText(
                String.format(
                        Locale.US,
                        "Total Distance: %.2f KM",
                        distance
                )
        );

        Toast.makeText(
                this,
                "Travel budget calculated!",
                Toast.LENGTH_SHORT
        ).show();
    }

    private double getAmount(EditText editText) {

        String value = editText.getText().toString().trim();

        if (value.isEmpty()) {
            return 0;
        }

        return Double.parseDouble(value);
    }

    private String formatMoney(double amount) {

        return String.format(
                Locale.US,
                "₹%,.2f",
                amount
        );
    }
}