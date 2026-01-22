package com.frc135.match_collection.objective

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.fragment.app.Fragment
import com.frc135.match_collection.Constants
import com.frc135.match_collection.Constants.AllianceColor
import com.frc135.match_collection.R
import com.frc135.match_collection.allianceColor
import com.frc135.match_collection.buttonPressedTime
import com.frc135.match_collection.matchTimer
import com.frc135.match_collection.numActionDrop
import com.frc135.match_collection.numActionIntakePoach
import com.frc135.match_collection.numActionProcessor
import com.frc135.match_collection.numActionScoreAlgae
import com.frc135.match_collection.numActionScoreAmp
import com.frc135.match_collection.numActionScoreSpeaker
import com.frc135.match_collection.numActionScorel1coral
import com.frc135.match_collection.numActionScorel2coral
import com.frc135.match_collection.numActionScorel3coral
import com.frc135.match_collection.numActionScorel4coral
import com.frc135.match_collection.orientation
import com.frc135.match_collection.scoring
import kotlinx.android.synthetic.main.collection_objective_teleop_fragment.view.teleop_compose_view

/**
 * [Fragment] used for showing intake buttons in [TeleopOuttakeFragment]
 */
class TeleopOuttakeFragment : Fragment(R.layout.collection_objective_teleop_fragment) {

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
     * This is the compose function that creates the layout for the compose view in collection_objective_teleop_fragment.
     */
    private fun setContent() {
        mainView!!.teleop_compose_view.setContent {
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
                Image(
                    painter = painterResource(
                        id = when {
                            (allianceColor == AllianceColor.BLUE) -> R.drawable.rebuilt_blue_map
                            else -> R.drawable.rebuilt_red_map
                        }
                    ),
                    contentDescription = "Field Map",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.matchParentSize(),
                )

                // SCORING BUTTONS
                /*
                    Refer to the comment on scoringButtonPress() for details on pressing the
                    buttons.
                    The background, border, and text colors are set depending on if they
                    are incap and the alliance color. The contents of the buttons are
                    rotated depending on the orientation so that they are not upside
                    down for certain orientations.
                 */


                if (scoring) {
                    //L1 coral Button
                    BoxWithConstraints(

                        modifier = Modifier
                            .size(10 * maxWidth / 200, 15 * maxHeight / 200)
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
                                            if (!collectionObjectiveActivity.failing) numActionScorel1coral++

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
                                text = "L1: $numActionScorel1coral",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                                )
                            )

                        }
                    }

                    //L2 button
                    BoxWithConstraints(
                        modifier = Modifier
                            .size(10 * maxWidth / 200, 15 * maxHeight / 200)
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
                                            if (!collectionObjectiveActivity.failing) numActionScorel2coral++

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
                            text = "L2: $numActionScorel2coral",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                            )
                        )
                    }
                    //L3 coral Button
                    BoxWithConstraints(
                        modifier = Modifier
                            .size(10 * maxWidth / 200, 15 * maxHeight / 200)
                            .offset(60 * maxWidth / 100, 56 * maxHeight / 100)
                            // .width(50 * maxWidth / 200)
                            .clickable {
                                if (collectionObjectiveActivity.isTimerRunning) {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        if (matchTimer != null) {
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.L3_CORAL
                                            )
                                            if (!collectionObjectiveActivity.failing) numActionScorel3coral++

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
                                text = "L3: $numActionScorel3coral",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                                )
                            )

                        }
                    }
                    //L4 coral Button
                    BoxWithConstraints(
                        modifier = Modifier
                            .size(10 * maxWidth / 200, 15 * maxHeight / 200)
                            .offset(65 * maxWidth / 100, 59 * maxHeight / 100)
                            // .width(50 * maxWidth / 200)
                            .clickable {
                                if (collectionObjectiveActivity.isTimerRunning) {
                                    val newPressTime = System.currentTimeMillis()
                                    if (buttonPressedTime + 250 < newPressTime) {
                                        buttonPressedTime = newPressTime
                                        if (matchTimer != null) {
                                            collectionObjectiveActivity.timelineAddWithStage(
                                                action_type = Constants.ActionType.L4_CORAL
                                            )
                                            if (!collectionObjectiveActivity.failing) numActionScorel4coral++

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
                                text = "L4: $numActionScorel4coral",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                                )
                            )

                        }
                    }
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
                            text = "Algea: $numActionProcessor",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                            )
                        )

                    }
                }
                BoxWithConstraints(
                    modifier = Modifier
                        .size(50 * maxWidth / 280 ,  25 * maxHeight / 200)
                        .offset( 0.7 * maxWidth ,  0.5 * maxHeight  )


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
                                        if (!collectionObjectiveActivity.failing) numActionScoreAlgae++

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
                            text = "Net Score: $numActionProcessor",
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                color = if (!collectionObjectiveActivity.isTimerRunning) Color.Black else Color.White
                            )
                        )

                    }
                }






           //     BoxWithConstraints(
             //       modifier = Modifier
               //         .fillMaxHeight()
                 //       .width(10 * maxWidth / 50)
                   //     .offset(
                     //       if (allianceColor == AllianceColor.BLUE) 17 * maxWidth / 20 else 10 * maxWidth / -20,
                       //     0.dp
                        //)
                       // .clickable { scoringButtonPress(actionType = Constants.ActionType.L3_CORAL) }
                       // .border(
                         //   4.dp,
                           // if (collectionObjectiveActivity.isIncap || collectionObjectiveActivity.failing) Color(
                             //   142,
                               // 142,
                                //142
                            //).copy(alpha = 0.6f)
                           // else if (allianceColor == AllianceColor.RED) Color.Blue.copy(alpha = 0.6f)
                           // else Color.Red.copy(alpha = 0.6f)
                        //)
                        //.background(
                          //  if (collectionObjectiveActivity.isIncap || collectionObjectiveActivity.failing) Color(
                            //    239,
                              //  239,
                                //239
                            //).copy(alpha = 0.6f)
                            //else if (allianceColor == AllianceColor.RED) Color(
                              //  33,
                                //150,
                                //243
                            //).copy(alpha = 0.6f)
                            //else Color(243, 33, 33).copy(alpha = 0.6f)
                        //)
                        //.rotate(if (orientation) 0f else 180f),
                    //contentAlignment = Alignment.Center
                //) {
                  //  Column(
                    //    horizontalAlignment = Alignment.CenterHorizontally,
                      //  modifier = Modifier.offset(
                        //    if (
                          //      (allianceColor == Constants.AllianceColor.BLUE && orientation) ||
                            //    (allianceColor == Constants.AllianceColor.RED && !orientation)
                           // ) {
                             //   maxWidth / -11
                            //} else maxWidth / 11,
                            //0.dp
                        //)
                   // ) {
                     //   Text(text = "DROP:", style = TextStyle(fontWeight = FontWeight.Bold))
                       // Text(
                         //   text = "$numActionDrop",
                           // style = TextStyle(fontWeight = FontWeight.Bold)
                        //)
                    //}
                //}
            }




        }
            }


    @SuppressLint("ModifierFactoryUnreferencedReceiver")
    private fun Modifier.intakeButtonModifier(
        actionType: Constants.ActionType,
        borderColorBlue: Color,
        borderColorRed: Color,
        backgroundColorBlue: Color,
        backgroundColorRed: Color,
        width: Dp,
        height: Dp,
        offsetX: Dp,
        offsetY: Dp
    ): Modifier {
        return size(width, height)
            .offset(
                if (allianceColor == Constants.AllianceColor.BLUE) offsetX else -offsetX,
                offsetY
            )
            .clickable { intakeButtonPress(actionType = actionType) }
            .border(
                4.dp,
                if (collectionObjectiveActivity.isIncap) Color(142, 142, 142).copy(alpha = 0.6f)
                else if (allianceColor == AllianceColor.BLUE) borderColorBlue.copy(alpha = 0.6f)
                else borderColorRed.copy(alpha = 0.6f)
            )
            .background(
                if (collectionObjectiveActivity.isIncap) Color(239, 239, 239).copy(alpha = 0.6f)
                else if (allianceColor == AllianceColor.BLUE) backgroundColorBlue.copy(alpha = 0.6f)
                else backgroundColorRed.copy(alpha = 0.6f)
            )
            .rotate(if (orientation) 0f else 180f)
    }


    private fun intakeButtonPress(actionType: Constants.ActionType) {
        if (!collectionObjectiveActivity.isIncap) {
            val newPressTime = System.currentTimeMillis()
            if (buttonPressedTime + 250 < newPressTime) {
                buttonPressedTime = newPressTime
                if (matchTimer != null) {
                    collectionObjectiveActivity.timelineAddWithStage(action_type = actionType)
                    when (actionType) {
//                        Constants.ActionType.INTAKE_AMP -> numActionIntakeAmp++
//                        Constants.ActionType.INTAKE_FAR -> numActionIntakeFar++
//                        Constants.ActionType.INTAKE_CENTER -> numActionIntakeCenter++
                        else -> numActionIntakePoach++
                    }
                    scoring = true
                    collectionObjectiveActivity.enableButtons()
                }
            }
        }
    }

    /*
    When a scoring button is clicked, it calls this function.
    If they are not incap and if either they aren't failing or they are
    pressing the speaker, or amp button, then:
    Adds the given action to the timeline, adds to the count for that action
    if they are not failing the score, then switches to intaking and
    enables buttons.
     */
    private fun scoringButtonPress(actionType: Constants.ActionType) {
        if (!collectionObjectiveActivity.isIncap) {
            if (
                !collectionObjectiveActivity.failing ||
                actionType == Constants.ActionType.L1_CORAL ||
                actionType == Constants.ActionType.L2_CORAL ||
                actionType == Constants.ActionType.L3_CORAL ||
                actionType == Constants.ActionType.L4_CORAL


//                actionType == Constants.ActionType.SCORE_CORAL ||
//                actionType == Constants.ActionType.SCORE_ALGAE_PROCESSOR // ||
//                actionType == Constants.ActionType.FERRY_SHOOT
            ) {
                val newPressTime = System.currentTimeMillis()
                if (buttonPressedTime + 250 < newPressTime) {
                    buttonPressedTime = newPressTime
                    if (matchTimer != null) {
                        collectionObjectiveActivity.timelineAddWithStage(action_type = actionType)
                        if (!collectionObjectiveActivity.failing) {
                            when (actionType) {
                                Constants.ActionType.L1_CORAL -> numActionScorel1coral++
                                Constants.ActionType.L2_CORAL -> numActionScorel2coral++
                                Constants.ActionType.L3_CORAL -> numActionScorel3coral++
                                Constants.ActionType.L4_CORAL -> numActionScorel4coral++
                                Constants.ActionType.SCORE_ALGAE_PROCESSOR -> numActionScoreAmp++
                                else -> numActionScoreSpeaker++
                            }
                        }
                        collectionObjectiveActivity.failing = false
                        scoring = false
                        collectionObjectiveActivity.enableButtons()
                    }
                }
            }
        }
    }
}