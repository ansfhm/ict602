package dev.lab.week3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private EditText etUnitsUsed, etRebate;
    private Button btnCalculate, btnReset;
    private TextView tvResult;
    private Switch switchRebate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        etUnitsUsed = findViewById(R.id.etUnitsUsed);
        etRebate = findViewById(R.id.etRebate);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnReset = findViewById(R.id.btnReset);
        tvResult = findViewById(R.id.tvResult);
        switchRebate = findViewById(R.id.switchRebate);

        switchRebate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            etRebate.setEnabled(isChecked);
            if (!isChecked) {
                etRebate.setText("");
            }
        });

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateBill();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUnitsUsed.setText("");
                etRebate.setText("");
                tvResult.setText("");
                switchRebate.setChecked(false);
            }
        });

        etRebate.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selected = item.getItemId();

        if (selected == R.id.menuInfo) {
            Intent intent = new Intent(MainActivity.this, Info.class);
            startActivity(intent);
            return true;
        } else if ( selected == R.id.menuAbout) {
            Intent intent = new Intent(MainActivity.this, About.class);
            startActivity(intent);
        return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void calculateBill() {
        if (etUnitsUsed.getText().toString().isEmpty() ||
                (switchRebate.isChecked() && etRebate.getText().toString().isEmpty())) {
            Toast.makeText(this, "Error: Please insert number in both forms", Toast.LENGTH_SHORT).show();
            return;
        }

        double unitsUsed = Double.parseDouble(etUnitsUsed.getText().toString());
        double rebate = 0;
        boolean isRebateApplied = false;
        if (switchRebate.isChecked()) {
            rebate = Double.parseDouble(etRebate.getText().toString());
            isRebateApplied = true;
        }

        double totalCost = calculateTotalCost(unitsUsed);
        double finalCost = totalCost;
        if (isRebateApplied) {
            finalCost = totalCost - (totalCost * (rebate / 100));
        }

        String result;
        if (isRebateApplied) {
            result = String.format("Units Used: %d kWh\nRebate: %.2f%%\nTotal Bill: RM %.2f\nTotal Bill after Rebate: RM %.2f",
                    (int) unitsUsed, rebate, totalCost, finalCost);
        } else {
            result = String.format("Units Used: %d kWh\nTotal Bill: RM %.2f", (int) unitsUsed, totalCost);
        }

        tvResult.setText(result);
    }

    private double calculateTotalCost(double unitsUsed) {
        double totalCost = 0;

        if (unitsUsed <= 200) {
            totalCost = unitsUsed * 0.218;
        } else if (unitsUsed <= 300) {
            totalCost = (200 * 0.218) + ((unitsUsed - 200) * 0.334);
        } else if (unitsUsed <= 600) {
            totalCost = (200 * 0.218) + (100 * 0.334) + ((unitsUsed - 300) * 0.516);
        } else if (unitsUsed > 600) {
            totalCost = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((unitsUsed - 600) * 0.546);
        }

        return totalCost;
    }
}
