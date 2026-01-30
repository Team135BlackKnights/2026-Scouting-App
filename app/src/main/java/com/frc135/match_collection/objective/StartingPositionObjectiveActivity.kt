package com.frc135.match_collection.objective


import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import com.frc135.match_collection.*
import com.frc135.match_collection.Constants.Companion.PREVIOUS_SCREEN
import com.frc135.match_collection.databinding.StartingPositionActivityBinding
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import com.frc135.match_collection.Constants
import com.frc135.match_collection.R
import com.frc135.match_collection.allianceColor
import com.frc135.match_collection.buttonPressedTime
import com.frc135.match_collection.Constants.Companion.previousScreen
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.frc135.match_collection.CollectionActivity
import com.frc135.match_collection.MatchInformationEditActivity
import com.frc135.match_collection.MatchInformationInputActivity
import com.frc135.match_collection.orientation
import com.frc135.match_collection.preloaded
import com.frc135.match_collection.resetCollectionReferences
import com.frc135.match_collection.scoring
import com.frc135.match_collection.startPosAOffsetX
import com.frc135.match_collection.startPosDOffsetY
import com.frc135.match_collection.startingPosition
import com.frc135.match_collection.teamNumber


import  androidx.compose.ui.graphics.Color


class StartingPositionObjectiveActivity : CollectionActivity() {


    private lateinit var binding: StartingPositionActivityBinding

    @SuppressLint("ResourceAsColor")
    private fun initOnClicks() {


        binding.btnSwitchOrientation.setOnClickListener {
            orientation = !orientation
        }


        binding.togglePreload.setOnClickListener {
            preloaded = !preloaded
            scoring = !scoring
            binding.togglePreload.setBackgroundColor(
                getColor(
                    if (binding.togglePreload.isChecked)
                        R.color.action_orange
                    else
                        R.color.light_gray
                )
            )
        }


        binding.btnNoShow.setOnClickListener {
            val newPressTime = System.currentTimeMillis()
            if (buttonPressedTime + 150 < newPressTime) {
                buttonPressedTime = newPressTime
                startingPosition = 0
                preloaded = false
                scoring = false
                binding.togglePreload.apply {
                    isEnabled = false
                    isChecked = false
                    setBackgroundColor(getColor(R.color.light_gray))
                }
                binding.btnNoShow.setBackgroundColor(
                    getColor(R.color.starting_position_selected)
                )
            }
        }


        binding.btnProceedStartingPosition.setOnClickListener { view ->
            val newPressTime = System.currentTimeMillis()
            if (buttonPressedTime + 150 < newPressTime) {
                buttonPressedTime = newPressTime


                if (startingPosition != null) {
                    val intent = if (startingPosition == 0) {
                        Intent(this, MatchInformationEditActivity::class.java)
                    } else {
                        Intent(this, CollectionObjectiveActivity::class.java)
                    }.putExtra(
                        PREVIOUS_SCREEN,
                        Constants.Screens.STARTING_POSITION_OBJECTIVE
                    )


                    startActivity(
                        intent,
                        ActivityOptions.makeSceneTransitionAnimation(
                            this,
                            binding.btnProceedStartingPosition,
                            "proceed_button"
                        ).toBundle()
                    )
                } else {
                    createErrorMessage(
                        getString(R.string.error_missing_information),
                        view
                    )
                }
            }
        }
    }


    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val newPressTime = System.currentTimeMillis()
            if (buttonPressedTime + 250 < newPressTime) {
                buttonPressedTime = newPressTime
                AlertDialog.Builder(this)
                    .setMessage(R.string.error_back_reset)
                    .setPositiveButton("Yes") { _, _ ->
                        startActivity(
                            Intent(this, MatchInformationInputActivity::class.java)
                                .putExtra("team_one", teamNumber)
                                .putExtra(
                                    PREVIOUS_SCREEN,
                                    Constants.Screens.STARTING_POSITION_OBJECTIVE
                                ),
                            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
                        )
                    }
                    .show()
            }
        }
        return super.onKeyLongPress(keyCode, event)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = StartingPositionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //binding.composeMap.setContent { MapContent() }


        if (previousScreen == Constants.Screens.MATCH_INFORMATION_INPUT) {
            preloaded = true
            scoring = true
            binding.togglePreload.isChecked = true
            binding.togglePreload.setBackgroundColor(getColor(R.color.action_orange))
        } else {
            binding.togglePreload.isChecked = preloaded
            scoring = preloaded
            binding.togglePreload.setBackgroundColor(
                getColor(
                    if (preloaded)
                        R.color.action_orange
                    else
                        R.color.light_gray
                )
            )
        }


        binding.tvPosTeamNumber.text = teamNumber
        binding.tvPosTeamNumber.setTextColor(
            getColor(
                if (allianceColor == Constants.AllianceColor.RED)
                    R.color.alliance_red_light
                else
                    R.color.alliance_blue_light
            )
        )


        resetCollectionReferences()
        initOnClicks()
    }
}

    @Composable
    fun MapContent() {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Column {
                BoxWithConstraints(
                    contentAlignment =
                        if (allianceColor == Constants.AllianceColor.BLUE)
                            Alignment.TopStart
                        else
                            Alignment.TopEnd,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                       .rotate(if (orientation) 0f else 180f)
                ) {
                    Image(
                        painter = painterResource(
                            id = if (allianceColor == Constants.AllianceColor.BLUE)
                                R.drawable.rebuilt_blue_map
                            else
                                R.drawable.rebuilt_red_map
                        ),
                        contentDescription = "FIELD MAP",
                        modifier = Modifier.fillMaxSize()
                    )


                    Column(
                        modifier = Modifier
                            .size(160 * maxWidth / 1152, 46 * maxHeight / 63)
                            .offset( // random ahh numbers my beloved
                                if (allianceColor == Constants.AllianceColor.BLUE) 245 * maxWidth / startPosAOffsetX else 245 * maxWidth / -startPosAOffsetX,
                                maxHeight / startPosDOffsetY
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1 / 4.toFloat())
                                .clickable {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                       buttonPressedTime = newPressTime
                                        startingPosition = 4
                                        toggle_preload.isEnabled = true
                                        btn_no_show.setBackgroundColor(getColor(R.color.light_gray))
                                    }
                                }
                                .border(
                                    4.dp,
                                    if (startingPosition != 4) Color(
                                        142,
                                        142,
                                        142
                                    ).copy(alpha = 0.6f)
                                    else Color(255, 87, 34).copy(alpha = 0.6f)
                                )
                                .background(
                                    if (startingPosition != 4) Color(
                                        239,
                                        239,
                                        239
                                    ).copy(alpha = 0.6f)
                                    else Color(255, 152, 0).copy(alpha = 0.6f)
                                )
                                .rotate(if (orientation) 0f else 180f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "4", style = TextStyle(fontWeight = FontWeight.Bold))
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1 / 4.toFloat())
                                .clickable {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        startingPosition = 3
                                        toggle_preload.isEnabled = true
                                        btn_no_show.setBackgroundColor(getColor(R.color.light_gray))
                                    }
                                }
                                .border(
                                    4.dp,
                                    if (startingPosition != 3) Color(
                                        142,
                                        142,
                                        142
                                    ).copy(alpha = 0.6f)
                                    else Color(255, 87, 34).copy(alpha = 0.6f)
                                )
                                .background(
                                    if (startingPosition != 3) Color(
                                        239,
                                        239,
                                        239
                                    ).copy(alpha = 0.6f)
                                    else Color(255, 152, 0).copy(alpha = 0.6f)
                                )
                                .rotate(if (orientation) 0f else 180f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "3", style = TextStyle(fontWeight = FontWeight.Bold))
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1 / 4.toFloat())
                                .clickable {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        startingPosition = 2
                                        toggle_preload.isEnabled = true
                                        btn_no_show.setBackgroundColor(getColor(R.color.light_gray))
                                    }
                                }
                                .border(
                                    4.dp,
                                    if (startingPosition != 2) Color(
                                        142,
                                        142,
                                        142
                                    ).copy(alpha = 0.6f)
                                    else Color(255, 87, 34).copy(alpha = 0.6f)
                                )
                                .background(
                                    if (startingPosition != 2) Color(
                                        239,
                                        239,
                                        239
                                    ).copy(alpha = 0.6f)
                                    else Color(255, 152, 0).copy(alpha = 0.6f)
                                )
                                .rotate(if (orientation) 0f else 180f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "2", style = TextStyle(fontWeight = FontWeight.Bold))
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1 / 4.toFloat())
                                .clickable {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        startingPosition = 1
                                        toggle_preload.isEnabled = true
                                        btn_no_show.setBackgroundColor(getColor(R.color.light_gray))
                                    }
                                }
                                .border(
                                    4.dp,
                                    if (startingPosition != 1) Color(
                                        142,
                                        142,
                                        142
                                    ).copy(alpha = 0.6f)
                                    else Color(255, 87, 34).copy(alpha = 0.6f)
                                )
                                .background(
                                    if (startingPosition != 1) Color(
                                        239,
                                        239,
                                        239
                                    ).copy(alpha = 0.6f)
                                    else Color(255, 152, 0).copy(alpha = 0.6f)
                                )
                                .rotate(if (orientation) 0f else 180f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "1", style = TextStyle(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            }
        }
   }

