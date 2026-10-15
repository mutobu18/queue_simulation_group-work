
package com.example.queue_simulation;

import android.os.Bundle;
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
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText customerCountEditText;
    private EditText durationEditText;
    private Button startSimulationButton;
    private TextView resultsTextView;

    private final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets bars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );
                    v.setPadding(
                            bars.left, bars.top, bars.right, bars.bottom
                    );
                    return insets;
                }
        );

        customerCountEditText =
                findViewById(R.id.customerCountEditText);
        durationEditText =
                findViewById(R.id.durationEditText);
        startSimulationButton =
                findViewById(R.id.startSimulationButton);
        resultsTextView =
                findViewById(R.id.resultsTextView);

        startSimulationButton.setOnClickListener(v -> runSimulation());
    }

    private void runSimulation() {
        String customerText =
                customerCountEditText.getText().toString().trim();
        String durationText =
                durationEditText.getText().toString().trim();

        if (customerText.isEmpty() || durationText.isEmpty()) {
            Toast.makeText(this, "Enter both values.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int numberOfCustomers = Integer.parseInt(customerText);
            double duration = Double.parseDouble(durationText);

            if (numberOfCustomers <= 0 || duration <= 0) {
                Toast.makeText(this,
                        "Values must be greater than zero.",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder output = new StringBuilder();

            double arrivalTime = 0.0;
            double previousCompletion = 0.0;

            double totalWaiting = 0.0;
            double totalService = 0.0;
            double totalTimeInSystem = 0.0;
            double totalIdle = 0.0;

            int served = 0;

            output.append("SINGLE-SERVER QUEUE\n\n");

            for (int i = 1; i <= numberOfCustomers; i++) {


                double interArrival = 1 + random.nextDouble() * 4;
                arrivalTime += interArrival;


                if (arrivalTime > duration) {
                    break;
                }


                double serviceTime = 1 + random.nextDouble() * 4;

                double idleTime = Math.max(
                        0.0, arrivalTime - previousCompletion
                );

                double serviceStart = Math.max(
                        arrivalTime, previousCompletion
                );

                double waitingTime = serviceStart - arrivalTime;

                double completionTime = serviceStart + serviceTime;

                double timeInSystem = completionTime - arrivalTime;


                previousCompletion = completionTime;

                totalIdle += idleTime;
                totalWaiting += waitingTime;
                totalService += serviceTime;
                totalTimeInSystem += timeInSystem;
                served++;

                output.append(String.format(
                        Locale.US,
                        "Customer %d\n"
                                + "Inter-arrival: %.2f min\n"
                                + "Arrival: %.2f min\n"
                                + "Service start: %.2f min\n"
                                + "Service time: %.2f min\n"
                                + "Completion: %.2f min\n"
                                + "Waiting: %.2f min\n"
                                + "Time in system: %.2f min\n"
                                + "Server idle: %.2f min\n\n",
                        i,
                        interArrival,
                        arrivalTime,
                        serviceStart,
                        serviceTime,
                        completionTime,
                        waitingTime,
                        timeInSystem,
                        idleTime
                ));
            }

            output.append("SUMMARY\n");
            output.append("Customers served: ").append(served)
                    .append("\n");

            if (served > 0) {
                output.append(String.format(
                        Locale.US,
                        "Average waiting time: %.2f min\n",
                        totalWaiting / served
                ));
                output.append(String.format(
                        Locale.US,
                        "Average service time: %.2f min\n",
                        totalService / served
                ));
                output.append(String.format(
                        Locale.US,
                        "Average time in system: %.2f min\n",
                        totalTimeInSystem / served
                ));
            }

            output.append(String.format(
                    Locale.US,
                    "Total server idle time: %.2f min\n",
                    totalIdle
            ));

            resultsTextView.setText(output.toString());

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Enter valid numeric values.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
