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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import kotlinx.android.synthetic.main.collection_objective_endgame_fragment.view.endgame_compose_view




/**
 * [Fragment] used for showing intake buttons in [TeleopOuttakeFragment]
 */
class EndgameFragment : Fragment(R.layout.collection_objective_endgame_fragment) {

    /**
     * The main view of this fragment.
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
     * This is the compose function that creates the layout for the compose view in collection_objective_endgame_fragment.
     */
    private fun setContent() {
        mainView!!.endgame_compose_view.setContent {
            /*
            This box contains all the elements that will be displayed, it is rotated based on your orientation.
            The elements within the box are aligned to the left or the right depending on the alliance color.
             */
            BoxWithConstraints(
                contentAlignment = if (allianceColor == AllianceColor.BLUE) Alignment.TopStart else Alignment.TopStart,
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(if (orientation) 0f else 180f)
            ) {
                val boxMaxWidth = maxWidth
                val boxMaxHeight = maxHeight
                /*
                This image view is behind everything else in the box
                and displays one of two images based on your alliance color
                */
                Image(
                    painter = painterResource(id = R.drawable.rebuilt_map),
                    contentDescription = "Stage Map",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.matchParentSize(),
                )
                Column(
                    modifier = Modifier
                        .size(maxWidth / 4, maxHeight / 4)
                        .align(Alignment.Center)
                        .offset(
                            maxWidth / 20,
                            0.dp
                        )
                )
                {
                    /*
                    Disabled if they are onstage on any chain.
                    When clicked, toggles parked between true and false. The border and
                    background colors and text are changed accordingly. The contents
                    of the buttons are rotated depending on the orientation so that
                    they are not upside down for certain orientations.
                     */
                    Box(
                        modifier = Modifier
                            .size(width=boxMaxWidth/7,height=boxMaxHeight/2)
                            .offset(                if (allianceColor == AllianceColor.BLUE) (boxMaxWidth / 500) else (boxMaxWidth /500),
                                if (allianceColor == AllianceColor.BLUE) .5 * boxMaxHeight / 16 else -1f * boxMaxHeight / 16
                            )
                            .weight(0.3f)
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
                                        if (matchTimer != null) parked = !parked
                                    }
                                }
                            }
                            .border(
                                3.dp,
                                if (
                                    cageTopLevel == Constants.StageLevel.D ||
                                    cageCenterLevel == Constants.StageLevel.D ||
                                    cageBottomLevel == Constants.StageLevel.D ||
                                    cageTopLevel == Constants.StageLevel.S ||
                                    cageCenterLevel == Constants.StageLevel.S ||
                                    cageBottomLevel == Constants.StageLevel.S
                                ) {
                                    Color(142, 142, 142).copy(alpha = 0.6f)
                                } else if (parked) Color(24, 125, 20).copy(alpha = 0.6f)
                                else Color(255, 87, 34).copy(alpha = 0.6f)
                            )
                            .background(
                                if (
                                    cageTopLevel == Constants.StageLevel.D ||
                                    cageCenterLevel == Constants.StageLevel.D ||
                                    cageBottomLevel == Constants.StageLevel.D ||
                                    cageTopLevel == Constants.StageLevel.S ||
                                    cageCenterLevel == Constants.StageLevel.S ||
                                    cageBottomLevel == Constants.StageLevel.S
                                ) {
                                    Color(239, 239, 239).copy(alpha = 0.6f)
                                } else if (parked) Color.Green.copy(alpha = 0.6f)
                                else Color(255, 152, 0).copy(alpha = 0.6f)
                            )
                            .rotate(if (orientation) 0f else 180f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (parked) "PARKED" else "NOT PARKED",
                                style = TextStyle(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
                // CHAIN TOGGLE BUTTONS
                /*
                Disabled if they are onstage on any other chain or if they are incap.
                When clicked, toggles the respective stage level variable from N
                (not attempted), to O (onstage), to F (failed), then back to N.
                The border and background colors and text are changed accordingly.
                Also sets parked to false if switched to onstage.
                The contents of the buttons are rotated depending on the orientation
                so that they are not upside down for certain orientations.
                 */
                CageFactory.createCage(
                    "TOP",
                    cageTopLevel,
                    { newLevel -> cageTopLevel = newLevel },
                    allianceColor,
                    orientation,
                    maxWidth/2,
                    maxHeight/1.125f,
                    Modifier.offset(
                        if (allianceColor == AllianceColor.BLUE) {(maxWidth / 2).div(1.15f)} else {(maxWidth /2).div(1.15f)},
                        if (allianceColor == AllianceColor.BLUE) maxHeight / 18 else 9 * maxHeight / 16
                    )
                )

                CageFactory.createCage(
                    "CENTER",
                    cageCenterLevel,
                    { newLevel -> cageCenterLevel = newLevel },
                    allianceColor,
                    orientation,
                    maxWidth/2,
                    maxHeight/1.125f,
                    Modifier.offset(
                        if (allianceColor == AllianceColor.BLUE) (maxWidth / 2).div(1.15f) else (maxWidth /2).div(1.15f),
                        if (allianceColor == AllianceColor.BLUE) 2.875 * maxHeight / 16 else 10.875 * maxHeight / 16
                    )
                )

                CageFactory.createCage(
                    "BOTTOM",
                    cageBottomLevel,
                    { newLevel -> cageBottomLevel = newLevel },
                    allianceColor,
                    orientation,
                    maxWidth/2,
                    maxHeight/1.125f,
                    Modifier.offset(
                        if (allianceColor == AllianceColor.BLUE) (maxWidth / 2).div(1.15f) else (maxWidth /2).div(1.15f),
                        if (allianceColor == AllianceColor.BLUE) 4.75 * maxHeight / 16 else 12.75 * maxHeight / 16
                    )
                )

            }
        }

    }
}