package com.frc135.match_collection

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Activity for Playoff Match Collection for scouts to count various scores in playoffs matches.
 */
class PlayoffActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Sets the layout of the screen
            BoxWithConstraints {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(5.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /* The values of these variables are increased/decreased by 1 when their
                    corresponding counter addition/subtraction button is pressed.
                    Their default values are set to 0 */



                    var hub by remember {mutableStateOf(0) }
                    var tower by remember {mutableStateOf(0)}
                    var outpost by remember {mutableStateOf(0)}
                    // This row contains the reset button
                    Row(modifier = Modifier.weight(0.1f)) {
                        // Reset Button
                        Button(modifier = Modifier, colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.LightGray, contentColor = Color.Black
                        ), onClick = {
                            // When pressed, resets all the counter values to 0
                            hub = 0;
                            tower = 0;
                            outpost = 0;

                        }) {
                            Text("Reset")
                        }
                    }
                    // Top row of counter buttons
                    Row(
                        modifier = Modifier.weight(0.4f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        // Amp Counter Button
                        CounterButton(
                            color = Color(214, 212, 154), countItem = "Hub Score",
                            // When minus button is pressed, decreases count by 1 (but prevents negative counts)
                            onMinusPress = { if (hub > 0) hub-- },
                            // When main button is pressed, increases count by 1
                            onPlusPress = { hub++},
                            // Sets the displayed counter value to the amp count
                            countItemValue = hub
                        )

                        // Lob Ferry Counter Button
                        CounterButton(
                            color = Color(198, 171, 201), countItem = "Tower Score",
                            // When minus button is pressed, decreases count by 1 (but prevents negative counts)
                            onMinusPress = { if (tower > 0) tower--},
                            // When main button is pressed, increases count by 1
                            onPlusPress = {tower++},
                            // Sets the displayed counter value to the lob ferry count
                            countItemValue = tower
                        )
                    }

                    // Bottom row of counter buttons
                    Row(
                        modifier = Modifier.weight(0.4f),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        // Unamplified Speaker Counter Button
                        CounterButton(
                            color = Color(143, 226, 227), countItem = "outPost",
                            // When minus button is pressed, decreases count by 1 (but prevents negative counts)
                            onMinusPress = { if (outpost > 0) outpost--},
                            // When main button is pressed, increases count by 1
                            onPlusPress = { outpost++ },
                            // Sets the displayed counter value to the unamplified speaker count
                            countItemValue = outpost
                        )
                    }
                }
            }
        }
    }

    /**
     * Default counter button layout
     * @param color - button color
     * @param countItem - name of the action being counted
     * @param countItemValue - how many times the action being counted has occurred
     * @param onMinusPress - event that the minus button is pressed (decreases count value by 1)
     * @param onPlusPress - event that the plus button is pressed (increases count value by 1)
     */
    @Composable
    fun RowScope.CounterButton(
        color: Color,
        countItem: String,
        countItemValue: Int,
        onMinusPress: () -> Unit,
        onPlusPress: () -> Unit
    ) {
        // Default counter button layout
        Row(
            modifier = Modifier
                .padding(5.dp)
                .weight(0.5f),
        ) {
            // Minus button
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.1f)
                    .padding(0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xfff4c7c3), contentColor = Color.Black
                ),
                onClick = onMinusPress // when pressed, starts the onMinusPress event
            ) {
                Text("-")
            }
            // Main counter button (displays what is being counted and the counter value)
            Button(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.4f)
                    .padding(0.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = color, contentColor = Color.Black
                ),
                onClick = onPlusPress // when pressed, starts the onPlusPress event
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$countItem:")
                    Text(countItemValue.toString())
                }
            }
        }
    }

    /**
     * Returns app to Mode Collection Selection Activity when back button is long pressed
     */
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder(this).setMessage(R.string.error_back_reset)
                .setPositiveButton("Yes") { _, _ -> super.onBackPressed() }.show()
        }
        return super.onKeyLongPress(keyCode, event)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
    }
}
