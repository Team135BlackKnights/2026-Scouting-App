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
import com.frc135.match_collection.CageFactory
import com.frc135.match_collection.Constants
import com.frc135.match_collection.Constants.AllianceColor
import com.frc135.match_collection.R
import com.frc135.match_collection.allianceColor
import com.frc135.match_collection.buttonPressedTime
import com.frc135.match_collection.matchTimer
import com.frc135.match_collection.orientation
import com.frc135.match_collection.parked
import com.frc135.match_collection.cageTopLevel
import com.frc135.match_collection.cageCenterLevel
import com.frc135.match_collection.cageBottomLevel

/**
 * [Fragment] used for showing intake buttons in [TeleopOuttakeFragment]
 */
class EndgameFragment :
    Fragment(R.layout.collection_objective_endgame_fragment) {

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

    /**
     * Compose UI setup
     */
    private fun setContent() {
        val composeView =
            mainView!!.findViewById<ComposeView>(R.id.endgame_compose_view)

        composeView.setContent {
            BoxWithConstraints(
                contentAlignment = Alignment.TopStart,
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(if (orientation) 0f else 180f)
            ) {

                val boxMaxWidth = maxWidth
                val boxMaxHeight = maxHeight

                Image(
                    painter = painterResource(id = R.drawable.rebuilt_map),
                    contentDescription = "Stage Map",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.matchParentSize()
                )

                Column(
                    modifier = Modifier
                        .size(boxMaxWidth / 4, boxMaxHeight / 4)
                        .align(Alignment.Center)
                        .offset(boxMaxWidth / 20, 0.dp)
                ) {

                    // PARKED BUTTON
                    Box(
                        modifier = Modifier
                            .size(
                                width = boxMaxWidth / 7,
                                height = boxMaxHeight / 2
                            )
                            .offset(
                                boxMaxWidth / 500,
                                if (allianceColor == AllianceColor.BLUE)
                                    boxMaxHeight / 32
                                else
                                    -boxMaxHeight / 16
                            )
                            .clickable {
                                if (
                                    cageTopLevel != Constants.StageLevel.D &&
                                    cageCenterLevel != Constants.StageLevel.D &&
                                    cageBottomLevel != Constants.StageLevel.D &&
                                    cageTopLevel != Constants.StageLevel.S &&
                                    cageCenterLevel != Constants.StageLevel.S &&
                                    cageBottomLevel != Constants.StageLevel.S
                                ) {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        if (matchTimer != null) {
                                            parked = !parked
                                        }
                                    }
                                }
                            }
                            .border(
                                3.dp,
                                when {
                                    cageTopLevel in listOf(
                                        Constants.StageLevel.D,
                                        Constants.StageLevel.S
                                    ) ||
                                            cageCenterLevel in listOf(
                                        Constants.StageLevel.D,
                                        Constants.StageLevel.S
                                    ) ||
                                            cageBottomLevel in listOf(
                                        Constants.StageLevel.D,
                                        Constants.StageLevel.S
                                    ) ->
                                        Color(142, 142, 142).copy(alpha = 0.6f)

                                    parked ->
                                        Color(24, 125, 20).copy(alpha = 0.6f)

                                    else ->
                                        Color(255, 87, 34).copy(alpha = 0.6f)
                                }
                            )
                            .background(
                                when {
                                    cageTopLevel in listOf(
                                        Constants.StageLevel.D,
                                        Constants.StageLevel.S
                                    ) ||
                                            cageCenterLevel in listOf(
                                        Constants.StageLevel.D,
                                        Constants.StageLevel.S
                                    ) ||
                                            cageBottomLevel in listOf(
                                        Constants.StageLevel.D,
                                        Constants.StageLevel.S
                                    ) ->
                                        Color(239, 239, 239).copy(alpha = 0.6f)

                                    parked ->
                                        Color.Green.copy(alpha = 0.6f)

                                    else ->
                                        Color(255, 152, 0).copy(alpha = 0.6f)
                                }
                            )
                            .rotate(if (orientation) 0f else 180f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (parked) "PARKED" else "NOT PARKED",
                            style = TextStyle(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                // CAGE BUTTONS
                CageFactory.createCage(
                    "TOP",
                    cageTopLevel,
                    { cageTopLevel = it },
                    allianceColor,
                    orientation,
                    boxMaxWidth / 2,
                    boxMaxHeight / 1.125f,
                    Modifier.offset(
                        (boxMaxWidth / 2).div(1.15f),
                        if (allianceColor == AllianceColor.BLUE)
                            boxMaxHeight / 18
                        else
                            9 * boxMaxHeight / 16
                    )
                )

                CageFactory.createCage(
                    "CENTER",
                    cageCenterLevel,
                    { cageCenterLevel = it },
                    allianceColor,
                    orientation,
                    boxMaxWidth / 2,
                    boxMaxHeight / 1.125f,
                    Modifier.offset(
                        (boxMaxWidth / 2).div(1.15f),
                        if (allianceColor == AllianceColor.BLUE)
                            2.875 * boxMaxHeight / 16
                        else
                            10.875 * boxMaxHeight / 16
                    )
                )

                CageFactory.createCage(
                    "BOTTOM",
                    cageBottomLevel,
                    { cageBottomLevel = it },
                    allianceColor,
                    orientation,
                    boxMaxWidth / 2,
                    boxMaxHeight / 1.125f,
                    Modifier.offset(
                        (boxMaxWidth / 2).div(1.15f),
                        if (allianceColor == AllianceColor.BLUE)
                            4.75 * boxMaxHeight / 16
                        else
                            12.75 * boxMaxHeight / 16
                    )
                )
            }
        }
    }
}
