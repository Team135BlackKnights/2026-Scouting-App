package com.frc135.match_collection.objective

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.frc135.match_collection.Constants
import com.frc135.match_collection.Constants.AllianceColor
import com.frc135.match_collection.R
import com.frc135.match_collection.allianceColor
import com.frc135.match_collection.autoIntakeList
import com.frc135.match_collection.buttonPressedTime
import com.frc135.match_collection.matchTimer
import com.frc135.match_collection.orientation
import com.frc135.match_collection.scoring

class AutoOuttakeFragment :
    Fragment(R.layout.collection_objective_auto_fragment) {

    /** Compose-observable score */
    private var numActionScorel1coral by mutableStateOf(0)

    private var mainView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainView = super.onCreateView(inflater, container, savedInstanceState)
        setContent()
        return mainView!!
    }

    private val collectionObjectiveActivity
        get() = activity as CollectionObjectiveActivity

    private fun setContent() {
        val composeView =
            mainView!!.findViewById<ComposeView>(R.id.auto_compose_view)

        composeView.setContent {
            BoxWithConstraints(
                contentAlignment =
                    if (allianceColor == AllianceColor.BLUE || allianceColor == AllianceColor.RED)
                        Alignment.TopStart
                    else Alignment.TopEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(if (orientation) 0f else 180f)
            ) {

                Image(
                    painter = painterResource(
                        id = if (allianceColor == AllianceColor.BLUE)
                            R.drawable.rebuilt_blue_map
                        else
                            R.drawable.rebuilt_red_map
                    ),
                    contentDescription = "Map with game pieces",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.matchParentSize()
                )

                // DEBUG BUTTON
                BoxWithConstraints(
                    modifier = Modifier
                        .size(40 * maxWidth / 200, 15 * maxHeight / 250)
                        .clickable {
                            scoring = !scoring
                            if (scoring) {
                                collectionObjectiveActivity.enableButtons()
                            }

                            allianceColor =
                                if (allianceColor == AllianceColor.BLUE)
                                    AllianceColor.RED
                                else
                                    AllianceColor.BLUE
                        }
                        .border(
                            4.dp,
                            Color(80, 80, 80).copy(alpha = 0.6f)
                        )
                        .rotate(if (orientation) 0f else 180f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "[ DEBUG ]",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            color =
                                if (!collectionObjectiveActivity.isTimerRunning)
                                    Color.Black
                                else
                                    Color.White
                        )
                    )
                }

                if (scoring) {

                    // L1 Coral Button
                    BoxWithConstraints(
                        modifier = Modifier
                            .size(10 * maxWidth / 200, 15 * maxHeight / 250)
                            .offset(maxWidth / 2, maxHeight / 2)
                            .clickable {
                                if (collectionObjectiveActivity.isTimerRunning) {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        if (matchTimer != null) {
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.L1_Tower
                                            )
                                            if (!collectionObjectiveActivity.failing) {
                                                numActionScorel1coral++
                                            }
                                            collectionObjectiveActivity.failing = false
                                            collectionObjectiveActivity.enableButtons()
                                        }
                                    }
                                }
                            }
                            .border(
                                4.dp,
                                if (!collectionObjectiveActivity.isTimerRunning)
                                    Color(142, 142, 142).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE)
                                    Color(30, 20, 125).copy(alpha = 0.6f)
                                else
                                    Color(125, 20, 20).copy(alpha = 0.6f)
                            )
                            .background(
                                if (!collectionObjectiveActivity.isTimerRunning)
                                    Color(239, 239, 239).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE)
                                    Color.Blue.copy(alpha = 0.6f)
                                else
                                    Color.Red.copy(alpha = 0.6f)
                            )
                            .rotate(if (orientation) 0f else 180f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "erm... this is being reworked!",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color =
                                    if (!collectionObjectiveActivity.isTimerRunning)
                                        Color.Black
                                    else
                                        Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Auto Intake Button (moved out of setContent)
 */
@Composable
fun AutoIntakeButton(
    intakeNum: Int,
    actionType: Constants.ActionType,
    modifier: Modifier,
    collectionObjectiveActivity: CollectionObjectiveActivity
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                if (!autoIntakeList[intakeNum] &&
                    collectionObjectiveActivity.isTimerRunning
                ) {
                    val newPressTime = System.currentTimeMillis()
                    if (buttonPressedTime + 250 < newPressTime) {
                        buttonPressedTime = newPressTime
                        if (matchTimer != null) {
                            autoIntakeList =
                                autoIntakeList.toMutableList().apply {
                                    set(intakeNum, true)
                                }
                            collectionObjectiveActivity.timelineAddWithStage(
                                action_type = actionType
                            )
                            scoring = true
                            collectionObjectiveActivity.enableButtons()
                        }
                    }
                }
            }
            .border(
                4.dp,
                if (autoIntakeList[intakeNum] ||
                    !collectionObjectiveActivity.isTimerRunning
                )
                    Color(142, 142, 142).copy(alpha = 0.6f)
                else
                    Color(255, 87, 34).copy(alpha = 0.6f)
            )
            .background(
                if (autoIntakeList[intakeNum] ||
                    !collectionObjectiveActivity.isTimerRunning
                )
                    Color(239, 239, 239).copy(alpha = 0.6f)
                else
                    Color(255, 152, 0).copy(alpha = 0.6f)
            )
            .rotate(if (orientation) 0f else 180f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text =
                if (autoIntakeList[intakeNum]) "TAKEN"
                else "${intakeNum + 1}",
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
    }
}
