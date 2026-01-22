package com.frc135.match_collection.objective

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.fragment.app.Fragment
import com.frc135.match_collection.Constants
import com.frc135.match_collection.Constants.AllianceColor
import com.frc135.match_collection.R
import com.frc135.match_collection.allianceColor
import com.frc135.match_collection.autoIntakeList
import com.frc135.match_collection.buttonPressedTime
import com.frc135.match_collection.matchTimer

// Scoring Variables

// Coral Total
//import com.frc135.match_collection.numActionScorel1coralTOTAL
//import com.frc135.match_collection.numActionScorel2coralTOTAL
//import com.frc135.match_collection.numActionScorel3coralTOTAL
//import com.frc135.match_collection.numActionScorel4coralTOTAL

//import com.frc135.match_collection.numActionScorel1coral
//import com.frc135.match_collection.numActionScorel2coral
//import com.frc135.match_collection.numActionScorel3coral
//import com.frc135.match_collection.numActionScorel4coral
// commented thses our for now these imports are not being used


import com.frc135.match_collection.numActionProcessor
import com.frc135.match_collection.numActionScoreNet

import com.frc135.match_collection.orientation
import com.frc135.match_collection.scoring
import kotlinx.android.synthetic.main.collection_objective_auto_fragment.view.auto_compose_view

/**
 * [Fragment] used for showing intake buttons in [AutoOuttakeFragment]
 */
class AutoOuttakeFragment : Fragment(R.layout.collection_objective_auto_fragment) {

    /**
     * The main view of this fragment.
     *
     */

    private var mainView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mainView = super.onCreateView(inflater, container, savedInstanceState)!!
        setContent()
        return mainView
    }

    /**
     * Parent activity of this fragment
     */
    private val collectionObjectiveActivity get() = activity as CollectionObjectiveActivity

    /**
     * This is the compose function that creates the layout for the compose view in collection_objective_auto_fragment.
     */
    private fun setContent() {
        println("Setting contents for Auto Fragment")

        mainView!!.auto_compose_view.setContent {
            /*
            This box contains all the elements that will be displayed, it is rotated based on your orientation.
            The elements within the box are aligned to the left or the right depending on the alliance color.
             */
            BoxWithConstraints(
                contentAlignment = if (allianceColor == AllianceColor.BLUE || allianceColor == AllianceColor.RED) Alignment.TopStart else Alignment.TopEnd,
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(if (orientation) 0f else 180f)
            ) {
                /*
                This image view is behind everything else in the box
                and displays one of two images based on your alliance.
                */
                Image(
                    painter = painterResource(
                        id = when {
                            (allianceColor == AllianceColor.BLUE) -> R.drawable.rebuilt_blue_map
                            else -> R.drawable.rebuilt_red_map
                        }
                    ),
                    contentDescription = "Map with game pieces",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.matchParentSize(),
                )

                //SCORING BUTTONS
                /*
                    When a scoring button is clicked, adds the action to the timeline,
                    adds to the count for that action if they are not failing the score,
                    then switches to intaking and enables buttons.
                    The background, border, and text colors are set depending on if they
                    are incap or not and the alliance. The contents of the buttons are
                    rotated depending on the orientation so that they are not upside
                    down for certain orientations.
                 */

                // DEBUG BUTTON
                BoxWithConstraints(
                    modifier = Modifier
                        .size(40 * maxWidth / 200, 15 * maxHeight / 250)
//                        .offset(maxWidth / 2, maxHeight / 2)
                        .clickable {
                            scoring = !scoring
                            if (scoring)
                                collectionObjectiveActivity.enableButtons()
                            println("Toggling Scoring: $scoring")

//                            if (allianceColor == AllianceColor.BLUE)
//                                allianceColor = AllianceColor.RED
//                            if (allianceColor == AllianceColor.RED)
//                                allianceColor = AllianceColor.BLUE
//
//
//
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
                            color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
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
                                                action_type = Constants.ActionType.L1_CORAL
                                            )
//                                            if (!collectionObjectiveActivity.failing) numActionScorel1coral++

                                            collectionObjectiveActivity.failing = false
                                            collectionObjectiveActivity.enableButtons()
                                        }
                                    }
                                }
                            }
                            .border(
                                4.dp,
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    142,
                                    142,
                                    142
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color(
                                    30,
                                    20,
                                    125
                                ).copy(alpha = 0.6f)
                                else Color(125, 20, 20).copy(alpha = 0.6f)
                            )
                            .background(
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    239,
                                    239,
                                    239
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color.Blue.copy(alpha = 0.6f)
                                else Color.Red.copy(alpha = 0.6f)
                            )
                            .rotate(if (orientation) 0f else 180f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "erm... this is being reworked!",
//                            text = "L1: $numActionScorel1coral",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                            )
                        )
                    }

                    //L2 button
                    BoxWithConstraints(
                        modifier = Modifier
                            .size(10 * maxWidth / 200, 15 * maxHeight / 250)
                            .offset(55 * maxWidth / 100, 53 * maxHeight / 100)
                            .clickable {
                                if (collectionObjectiveActivity.isTimerRunning) {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        if (matchTimer != null) {
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.L2_CORAL
                                            )
//                                            if (!collectionObjectiveActivity.failing) numActionScorel2coral++

                                            collectionObjectiveActivity.failing = false
                                            collectionObjectiveActivity.enableButtons()
                                        }
                                    }
                                }
                            }
                            .border(
                                4.dp,
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    142,
                                    142,
                                    142
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color(
                                    30,
                                    20,
                                    125
                                ).copy(alpha = 0.6f)
                                else Color(125, 20, 20).copy(alpha = 0.6f)
                            )
                            .background(
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    239,
                                    239,
                                    239
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color.Blue.copy(alpha = 0.6f)
                                else Color.Red.copy(alpha = 0.6f)
                            )
                            .rotate(if (orientation) 0f else 180f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "erm... this is being reworked!",
//                            text = "L2: $numActionScorel2coral",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                            )
                        )
                    }
                    // L3 Coral Button
                    BoxWithConstraints(
                        modifier = Modifier
                            .size(10 * maxWidth / 200, 15 * maxHeight / 250)
                            .offset(60 * maxWidth / 100, 56 * maxHeight / 100)
                            .clickable {
                                if (collectionObjectiveActivity.isTimerRunning) {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        if (matchTimer != null) {
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.L3_CORAL
                                            )
//                                            if (!collectionObjectiveActivity.failing) numActionScorel3coral++

                                            collectionObjectiveActivity.failing = false
                                            collectionObjectiveActivity.enableButtons()
                                        }
                                    }
                                }
                            }
                            .border(
                                4.dp,
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    142,
                                    142,
                                    142
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color(
                                    30,
                                    20,
                                    125
                                ).copy(alpha = 0.6f)
                                else Color(125, 20, 20).copy(alpha = 0.6f)
                            )
                            .background(
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    239,
                                    239,
                                    239
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color.Blue.copy(alpha = 0.6f)
                                else Color.Red.copy(alpha = 0.6f)
                            )
                            .rotate(if (orientation) 0f else 180f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "erm... this is being reworked!",
//                            text = "L3: $numActionScorel3coral",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                            )
                        )
                    }

                    // L4 Coral Button
                    BoxWithConstraints(
                        modifier = Modifier
                            .size(10 * maxWidth / 200, 15 * maxHeight / 250)
                            .offset(65 * maxWidth / 100, 59 * maxHeight / 100)
                            .clickable {
                                if (collectionObjectiveActivity.isTimerRunning) {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        if (matchTimer != null) {
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.L4_CORAL
                                            )
//                                            if (!collectionObjectiveActivity.failing) numActionScorel4coral++

                                            collectionObjectiveActivity.failing = false
                                            collectionObjectiveActivity.enableButtons()
                                        }
                                    }
                                }
                            }
                            .border(
                                4.dp,
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    142,
                                    142,
                                    142
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color(
                                    30,
                                    20,
                                    125
                                ).copy(alpha = 0.6f)
                                else Color(125, 20, 20).copy(alpha = 0.6f)
                            )
                            .background(
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    239,
                                    239,
                                    239
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color.Blue.copy(alpha = 0.6f)
                                else Color.Red.copy(alpha = 0.6f)
                            )
                            .rotate(if (orientation) 0f else 180f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "erm... this is being reworked!",
//                            text = "L4: $numActionScorel4coral",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                            )
                        )
                    }

                    //prossesor button
                    BoxWithConstraints(
                        modifier = Modifier
                            .size(50 * maxWidth / 200, 25 * maxHeight / 200)
                            .offset(0.55 * maxWidth, maxHeight / 200)

                            // .width(50 * maxWidth / 200)
                            .clickable {
                                if (collectionObjectiveActivity.isTimerRunning) {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        if (matchTimer != null) {
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.SCORE_ALGAE_PROCESSOR
                                            )
                                            if (!collectionObjectiveActivity.failing) numActionProcessor++

                                            collectionObjectiveActivity.failing = false
                                            collectionObjectiveActivity.enableButtons()
                                        }
                                    }
                                }
                            }
                            .border(
                                4.dp,
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    142,
                                    142,
                                    142
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color(
                                    30,
                                    20,
                                    125
                                ).copy(alpha = 0.6f)
                                else Color(125, 20, 20).copy(alpha = 0.6f)
                            )
                            .background(
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    239,
                                    239,
                                    239
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color.Blue.copy(alpha = 0.6f)
                                else Color.Red.copy(alpha = 0.6f)
                            )
                            .rotate(if (orientation) 0f else 180f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.offset(
                                0.dp,
                                if (orientation) maxHeight / 4 else maxHeight / -4
                            )
                        ) {
                            Text(
                                text = "Prossesser Button: $numActionProcessor",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                                )
                            )

                        }
                    }


                    BoxWithConstraints(
                        modifier = Modifier
                            .size(50 * maxWidth / 280, 25 * maxHeight / 200)
                            .offset(0.45 * maxWidth, 0.55 * maxHeight / 2)

                            // .width(50 * maxWidth / 200)
                            .clickable {
                                if (collectionObjectiveActivity.isTimerRunning) {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        if (matchTimer != null) {
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.SCORE_ALGAE_PROCESSOR
                                            )
                                            if (!collectionObjectiveActivity.failing) numActionProcessor++

                                            collectionObjectiveActivity.failing = false
                                            collectionObjectiveActivity.enableButtons()
                                        }
                                    }
                                }
                            }
                            .border(
                                4.dp,
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    142,
                                    142,
                                    142
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color(
                                    30,
                                    20,
                                    125
                                ).copy(alpha = 0.6f)
                                else Color(125, 20, 20).copy(alpha = 0.6f)
                            )
                            .background(
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    239,
                                    239,
                                    239
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color.Blue.copy(alpha = 0.6f)
                                else Color.Red.copy(alpha = 0.6f)
                            )
                            .rotate(if (orientation) 0f else 180f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.offset(
                                0.dp,
                                if (orientation) maxHeight / 4 else maxHeight / -4
                            )
                        ) {
                            Text(
                                text = "erm... this is being reworked!",
//                                text = "Algea: $numActionProcessor",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                                )
                            )

                        }
                    }
                    BoxWithConstraints(
                        modifier = Modifier
                            .size(50 * maxWidth / 280, 25 * maxHeight / 200)
                            .offset(0.7 * maxWidth, 0.5 * maxHeight)


                            // .width(50 * maxWidth / 200)
                            .clickable {
                                if (collectionObjectiveActivity.isTimerRunning) {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        if (matchTimer != null) {
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.SCORE_NET
                                            )
                                            if (!collectionObjectiveActivity.failing) numActionScoreNet++

                                            collectionObjectiveActivity.failing = false
                                            collectionObjectiveActivity.enableButtons()
                                        }
                                    }
                                }
                            }
                            .border(
                                4.dp,
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    142,
                                    142,
                                    142
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color(
                                    30,
                                    20,
                                    125
                                ).copy(alpha = 0.6f)
                                else Color(125, 20, 20).copy(alpha = 0.6f)
                            )
                            .background(
                                if (!collectionObjectiveActivity.isTimerRunning) Color(
                                    239,
                                    239,
                                    239
                                ).copy(alpha = 0.6f)
                                else if (allianceColor == AllianceColor.BLUE) Color.Blue.copy(alpha = 0.6f)
                                else Color.Red.copy(alpha = 0.6f)
                            )
                            .rotate(if (orientation) 0f else 180f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.offset(
                                0.dp,
                                if (orientation) maxHeight / 4 else maxHeight / -4
                            )
                        ) {
                            Text(
                                text = "Net Score: $numActionScoreNet",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                                )
                            )
                        }
                    }
                }


                // LEAVE BUTTON
//                    Box(
//                        modifier = Modifier
//                            .size(10 * maxWidth / 200, maxHeight / 150)
//                            .offset(maxWidth / 4, maxHeight / 4)
//                            .clickable {
//                                if (collectionObjectiveActivity.isTimerRunning) {
//                                    val newPressTime = System.currentTimeMillis()
//                                    if (buttonPressedTime + 250 < newPressTime) {
//                                        buttonPressedTime = newPressTime
//                                        if (matchTimer != null) {
//                                            collectionObjectiveActivity.timelineAddWithStage(
//                                                action_type = Constants.ActionType.LEFT_START
//                                            )
//                                            boolLeftStart = !boolLeftStart
//
//                                            scoring = false
//                                            collectionObjectiveActivity.failing = false
//                                            collectionObjectiveActivity.enableButtons()
//                                        }
//                                    }
//                                }
//                            }
//                            .border(
//                                4.dp,
//                                if (!collectionObjectiveActivity.isTimerRunning) Color(
//                                    142,
//                                    142,
//                                    142
//                                ).copy(alpha = 0.6f)
//                                else if (allianceColor == AllianceColor.BLUE) Color(
//                                    30,
//                                    20,
//                                    125
//                                ).copy(alpha = 0.6f)
//                                else Color(125, 20, 20).copy(alpha = 0.6f)
//                            )
//                            .background(
//                                if (!collectionObjectiveActivity.isTimerRunning) Color(
//                                    239,
//                                    239,
//                                    239
//                                ).copy(alpha = 0.6f)
//                                else if (allianceColor == AllianceColor.BLUE) Color.Blue.copy(alpha = 0.6f)
//                                else Color.Red.copy(alpha = 0.6f)
//                            )
//                            .rotate(if (orientation) 0f else 180f),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "LEAVE: $boolLeftStart",
//                            style = TextStyle(
//                                fontWeight = FontWeight.Bold,
//                                color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
//                            )
//                        )
//                    }
                // SPEAKER BUTTON
//                    if (scoring) {
//                        //L4 coral Button
//                        BoxWithConstraints(
//                            modifier = Modifier
//                                .size(20 * maxWidth / 100, 10 * maxHeight / 42)
//                                .offset(440.dp, maxHeight / 100)
//                                // .width(50 * maxWidth / 200)
//                                .clickable {
//                                    if (collectionObjectiveActivity.isTimerRunning) {
//                                        val newPressTime = System.currentTimeMillis()
//                                        if (buttonPressedTime + 250 < newPressTime) {
//                                            buttonPressedTime = newPressTime
//                                            if (matchTimer != null) {
//                                                collectionObjectiveActivity.timelineAddWithStage(
//                                                    action_type = Constants.ActionType.L4_CORAL
//                                                )
//                                                if (!collectionObjectiveActivity.failing) numActionProcessor++
//
//                                                collectionObjectiveActivity.failing = false
//                                                collectionObjectiveActivity.enableButtons()
//                                            }
//                                        }
//                                    }
//                                }
//                                .border(
//                                    4.dp,
//                                    if (collectionObjectiveActivity.isIncap || !collectionObjectiveActivity.isTimerRunning) Color(
//                                        142,
//                                        142,
//                                        142
//                                    ).copy(alpha = 0.6f)
//                                    else if (allianceColor == AllianceColor.BLUE) Color.Blue.copy(
//                                        alpha = 0.6f
//                                    )
//                                    else Color.Red.copy(alpha = 0.6f)
//                                )
//                                .background(
//                                    if (collectionObjectiveActivity.isIncap || !collectionObjectiveActivity.isTimerRunning) Color(
//                                        239,
//                                        239,
//                                        239
//                                    ).copy(alpha = 0.6f)
//                                    else if (allianceColor == AllianceColor.BLUE) Color(
//                                        33,
//                                        150,
//                                        243
//                                    ).copy(alpha = 0.6f)
//                                    else Color(243, 33, 33).copy(alpha = 0.6f)
//                                )
//                                .rotate(if (orientation) 0f else 180f),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Column(
//                                horizontalAlignment = Alignment.CenterHorizontally,
//                                modifier = Modifier.offset(
//                                    0.dp,
//                                    if (orientation) maxHeight / 4 else maxHeight / -4
//                                )
//                            ) {
//                                Text(
//                                    text = "Score Prosesser:",
//                                    style = TextStyle(fontWeight = FontWeight.Bold)
//                                )
//                                Text(
//                                    text = "$numActionProcessor",
//                                    style = TextStyle(fontWeight = FontWeight.Bold)
//                                )
//                            }
//                        }
//                    }
//                    // INTAKE BUTTONS
//                    // Refer to the comment for AutoIntakeButton() for details on the intake buttons
//                    else {
//                        // SPIKE NOTE INTAKE BUTTONS
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            modifier = Modifier
//                                .offset(
//                                    if (allianceColor == AllianceColor.BLUE) 20 * maxWidth / 77 else 20 * maxWidth / -77,
//                                    maxHeight / 17
//                                )
//                                .size(maxWidth / 11, 10 * maxHeight / 19)
//                        )
//                        {
////                        AutoIntakeButton(
////                            intakeNum = 0,
////                            actionType = Constants.ActionType.AUTO_INTAKE_SPIKE_1,
////                            modifier = Modifier.weight(1 / 3f)
////                        )
////                        AutoIntakeButton(
////                            intakeNum = 1,
////                            actionType = Constants.ActionType.AUTO_INTAKE_SPIKE_2,
////                            modifier = Modifier.weight(1 / 3f)
////                        )
////                        AutoIntakeButton(
////                            intakeNum = 2,
////                            actionType = Constants.ActionType.AUTO_INTAKE_SPIKE_3,
////                            modifier = Modifier.weight(1 / 3f)
////                        )
//                        }
//                        // CENTER NOTE INTAKE BUTTONS
//                        Column(
//                            horizontalAlignment = Alignment.CenterHorizontally,
//                            modifier = Modifier
//                                .offset(if (allianceColor == AllianceColor.BLUE) 46 * maxWidth / 55 else 46 * maxWidth / -55)
//                                .width(maxWidth / 11)
//                                .fillMaxHeight()
//                        )
//                        {
////                        AutoIntakeButton(
////                            intakeNum = 3,
////                            actionType = Constants.ActionType.AUTO_INTAKE_CENTER_1,
////                            modifier = Modifier.weight(1 / 5f)
////                        )
////                        AutoIntakeButton(
////                            intakeNum = 4,
////                            actionType = Constants.ActionType.AUTO_INTAKE_CENTER_2,
////                            modifier = Modifier.weight(1 / 5f)
////                        )
////                        AutoIntakeButton(
////                            intakeNum = 5,
////                            actionType = Constants.ActionType.AUTO_INTAKE_CENTER_3,
////                            modifier = Modifier.weight(1 / 5f)
////                        )
////                        AutoIntakeButton(
////                            intakeNum = 6,
////                            actionType = Constants.ActionType.AUTO_INTAKE_CENTER_4,
////                            modifier = Modifier.weight(1 / 5f)
////                        )
////                        AutoIntakeButton(
////                            intakeNum = 7,
////                            actionType = Constants.ActionType.AUTO_INTAKE_CENTER_5,
////                            modifier = Modifier.weight(1 / 5f)
////                        )
//                        }
//                    }
//                }
//            }
            }

            /*
    When a intake button is clicked, adds the action to the timeline,
    adds to the count for that action, then switches to scoring and
    enables buttons.
    The background, border, and text colors are set depending on if they
    are incap or not. The contents of the buttons are rotated depending
    on the orientation so that they are not upside down for certain
    orientations.
     */
            @Composable
            fun AutoIntakeButton(
                intakeNum: Int,
                actionType: Constants.ActionType,
                modifier: Modifier
            ) {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable {
                            if (!autoIntakeList[intakeNum] && collectionObjectiveActivity.isTimerRunning) {
                                val newPressTime = System.currentTimeMillis()
                                if (buttonPressedTime + 250 < newPressTime) {
                                    buttonPressedTime = newPressTime
                                    if (matchTimer != null) {
                                        autoIntakeList =
                                            autoIntakeList
                                                .toMutableList()
                                                .apply { set(intakeNum, true) }
                                        collectionObjectiveActivity.timelineAddWithStage(action_type = actionType)
                                        scoring = true
                                        collectionObjectiveActivity.enableButtons()
                                    }
                                }
                            }
                        }
                        .border(
                            4.dp,
                            if (autoIntakeList[intakeNum] || !collectionObjectiveActivity.isTimerRunning) Color(
                                142,
                                142,
                                142
                            ).copy(alpha = 0.6f)
                            else Color(255, 87, 34).copy(alpha = 0.6f)
                        )
                        .background(
                            if (autoIntakeList[intakeNum] || !collectionObjectiveActivity.isTimerRunning) Color(
                                239,
                                239,
                                239
                            ).copy(alpha = 0.6f)
                            else Color(255, 152, 0).copy(alpha = 0.6f)
                        )
                        .rotate(if (orientation) 0f else 180f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (autoIntakeList[intakeNum]) "TAKEN" else "${intakeNum + 1}",
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}