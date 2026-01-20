package com.frc135.match_collection

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.WindowManager
import android.widget.Button
import androidx.core.content.ContextCompat
import com.frc135.match_collection.Constants.Companion.PREVIOUS_SCREEN
import com.frc135.match_collection.objective.CollectionObjectiveActivity
import com.frc135.match_collection.objective.StartingPositionObjectiveActivity
import com.frc135.match_collection.databinding.EditMatchInformationActivityBinding
import java.lang.Integer.parseInt

// Class to edit previously inputted match information.
class MatchInformationEditActivity : MatchInformationActivity() {
    private lateinit var binding: EditMatchInformationActivityBinding

    private lateinit var teamNumberOne: String
    private lateinit var teamNumberTwo: String
    private lateinit var teamNumberThree: String

    private var blueToggleButtonColor: Int = 0
    private var redToggleButtonColor: Int = 0
    private var blueToggleButtonColorDark: Int = 0
    private var redToggleButtonColorDark: Int = 0

    private lateinit var blueToggleButton: Button
    private lateinit var redToggleButton: Button

    // Get subjective team numbers through intent extras from CollectionSubjectiveActivity.kt.
    private fun getExtras() {
        teamNumberOne = intent.extras?.getString("team_one").toString()
        teamNumberTwo = intent.extras?.getString("team_two").toString()
        teamNumberThree = intent.extras?.getString("team_three").toString()
    }

    // Populate edit texts with previously inputted match information data.
    private fun populateData() {
        binding.etMatchNumber.setText(matchNumber.toString())
        if (collectionMode == Constants.ModeSelection.OBJECTIVE) {
            binding.etTeamTwo.setText(teamNumber)
        } else {
            getExtras()

            makeViewVisible(
                binding.etTeamOne,
                binding.etTeamThree,
                binding.tvHintTeamTwo,
                binding.tvHintTeamThree,
                binding.separatorTeamTwo,
                binding.separatorTeamOne
            )

            binding.etTeamOne.setText(teamNumberOne)
            binding.etTeamTwo.setText(teamNumberTwo)
            binding.etTeamThree.setText(teamNumberThree)
        }
    }

    private fun initMatchNumberTextChangeListener() {
        binding.etMatchNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                if (checkInputNotEmpty(binding.etMatchNumber)) {
                    if (binding.etMatchNumber.text.toString() != "") {
                        if (MatchInformationInputActivity.MatchSchedule.fileExists) {
                            if (parseInt(binding.etMatchNumber.text.toString()) > MatchInformationInputActivity.MatchSchedule.contents!!.keySet()!!.size) {
                                binding.etMatchNumber.setText(matchNumber.toString())
                            } else {
                                matchNumber = parseInt(binding.etMatchNumber.text.toString())
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    // Update match information based on newly inputted information.
    private fun updateMatchInformation() {
        matchNumber = parseInt(binding.etMatchNumber.text.toString())
        if (collectionMode == Constants.ModeSelection.OBJECTIVE) {
            teamNumber = binding.etTeamTwo.text.toString()
        } else {
            for (ranking in listOf(defenseRating, quicknessScore, fieldAwarenessScore)) {

                // Sets the teams for quickness, field awareness, and timeLeftToClimb based on the team
                // numbers being displayed
                ranking.teamOne?.teamNumber = binding.etTeamOne.text.toString()
                ranking.teamTwo?.teamNumber = binding.etTeamTwo.text.toString()
                ranking.teamThree?.teamNumber = binding.etTeamThree.text.toString()
            }
        }
    }

    // Call updateMatchInformation() and proceed to QRGenerateActivity.kt.
    private fun generateQR() {
        updateMatchInformation()

        val intent = Intent(this, QRGenerateActivity::class.java).putExtras(intent)
            .putExtra(PREVIOUS_SCREEN, Constants.Screens.MATCH_INFORMATION_EDIT)
        startActivity(
            intent, ActivityOptions.makeSceneTransitionAnimation(
                this, binding.btnProceedQrGenerate, "proceed_button"
            ).toBundle()
        )
    }

    // Initialize proceed button to set the updated values and start QRGenerateActivity.kt.
    private fun initProceedButton() {
        binding.btnProceedQrGenerate.setOnClickListener { view ->
            val newPressTime = System.currentTimeMillis()
            if (buttonPressedTime + 250 < newPressTime) {
                buttonPressedTime = newPressTime
                if (safetyCheck(
                        view = view,
                        currentScreen = "match_edit_activity_screen",
                        etMatchNumber = binding.etMatchNumber,
                        etTeamOne = binding.etTeamOne,
                        etTeamTwo = binding.etTeamTwo,
                        etTeamThree = binding.etTeamThree
                    )) {
                    generateQR()
                }

            }
        }
    }

    // Goes back to collectionObjectiveActivity if in objective mode
    // Restart app from MatchInformationInputActivity.kt when back button is long pressed if in Subjective
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val newPressTime = System.currentTimeMillis()
            if (buttonPressedTime + 250 < newPressTime) {
                buttonPressedTime = newPressTime
                updateMatchInformation()
                if (collectionMode == Constants.ModeSelection.OBJECTIVE) {
                    if (startingPosition.toString() != "0") {
                        teamNumber = binding.etTeamTwo.text.toString()
                        startActivity(
                            Intent(this, CollectionObjectiveActivity::class.java).putExtra(
                                PREVIOUS_SCREEN,
                                Constants.Screens.MATCH_INFORMATION_EDIT
                            )
                        )
                    } else {
                        startActivity(
                            Intent(this, StartingPositionObjectiveActivity::class.java).putExtra(
                                PREVIOUS_SCREEN,
                                Constants.Screens.MATCH_INFORMATION_EDIT
                            )
                        )
                    }
                } else if (collectionMode == Constants.ModeSelection.SUBJECTIVE) {
                    startActivity(
                        Intent(this, CollectionSubjectiveActivity::class.java).putExtras(intent)
                            .putExtra("team_one", binding.etTeamOne.text.toString())
                            .putExtra("team_two", binding.etTeamTwo.text.toString())
                            .putExtra("team_three", binding.etTeamThree.text.toString())
                            .putExtra(PREVIOUS_SCREEN, Constants.Screens.MATCH_INFORMATION_EDIT)
                    )
                }
            }
        }
        return super.onKeyLongPress(keyCode, event)
    }

    // Only lets the user type in numbers and uppercase letters
    private fun initTeamNumberTextChangeListeners() {
        val regex = "[^A-Z0-9]".toRegex()
        binding.etTeamTwo.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (checkInputNotEmpty(binding.etTeamTwo)) {
                    if (s.toString().contains(regex)) {
                        val tempString: String = binding.etTeamTwo.text.toString()
                        binding.etTeamTwo.setText(regex.replace(tempString, ""))
                    }
                }
            }
        })
        if (collectionMode == Constants.ModeSelection.SUBJECTIVE) {
            binding.etTeamOne.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (checkInputNotEmpty(binding.etTeamOne)) {
                        if (s.toString().contains(regex)) {
                            val tempString: String = binding.etTeamOne.text.toString()
                            binding.etTeamOne.setText(regex.replace(tempString, ""))
                        }
                    }
                }
            })
            binding.etTeamThree.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int, after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (checkInputNotEmpty(binding.etTeamThree)) {
                        if (s.toString().contains(regex)) {
                            val tempString: String = binding.etTeamThree.text.toString()
                            binding.etTeamThree.setText(regex.replace(tempString, ""))
                        }
                    }
                }
            })
        }
    }

    // Create an alliance color toggle button given its specifications.
    private fun createToggleButton(
        isBordered: Boolean,
        toggleButton: Button,
        toggleButtonColor: Int,
        toggleButtonColorDark: Int
    ) {
        val backgroundDrawable = GradientDrawable()

        if (isBordered) {
            backgroundDrawable.setStroke(10, toggleButtonColorDark)
        }

        backgroundDrawable.setColor(toggleButtonColor)
        backgroundDrawable.cornerRadius = 10f
        toggleButton.background = backgroundDrawable
    }

    private fun resetBackground() {
        // Create the unselected alliance color left toggle.
        createToggleButton(
            isBordered = false,
            toggleButton = blueToggleButton,
            toggleButtonColor = blueToggleButtonColor,
            toggleButtonColorDark = blueToggleButtonColorDark
        )

        // Create the unselected alliance color right toggle.
        createToggleButton(
            isBordered = false,
            toggleButton = redToggleButton,
            toggleButtonColor = redToggleButtonColor,
            toggleButtonColorDark = redToggleButtonColorDark
        )
    }

    private fun initToggleButtons() {
        redToggleButtonColor = ContextCompat.getColor(this, R.color.alliance_red_light)
        blueToggleButtonColor = ContextCompat.getColor(this, R.color.alliance_blue_light)
        redToggleButtonColorDark = ContextCompat.getColor(this, R.color.alliance_red_dark)
        blueToggleButtonColorDark = ContextCompat.getColor(this, R.color.alliance_blue_dark)
        redToggleButton = binding.redToggleButton
        blueToggleButton = binding.blueToggleButton

        resetBackground()

        when (allianceColor) {
            Constants.AllianceColor.BLUE -> {
                switchBorderToBlueToggle()
            }

            else -> {
                switchBorderToRedToggle()
            }
        }

        blueToggleButton.setOnLongClickListener {
            val newPressTime = System.currentTimeMillis()
            if (buttonPressedTime + 250 < newPressTime) {
                buttonPressedTime = newPressTime
                allianceColor = Constants.AllianceColor.BLUE
                switchBorderToBlueToggle()
            }
            return@setOnLongClickListener true
        }
        redToggleButton.setOnLongClickListener {
            val newPressTime = System.currentTimeMillis()
            if (buttonPressedTime + 250 < newPressTime) {
                buttonPressedTime = newPressTime
                allianceColor = Constants.AllianceColor.RED
                switchBorderToRedToggle()
            }
            return@setOnLongClickListener true
        }
    }

    // Apply border to red alliance toggle when red alliance selected.
    private fun switchBorderToRedToggle() {
        resetBackground()

        // Create selected red toggle.
        createToggleButton(
            isBordered = true,
            toggleButton = redToggleButton,
            toggleButtonColor = redToggleButtonColor,
            toggleButtonColorDark = redToggleButtonColorDark
        )
    }

    // Apply border to blue alliance toggle when blue alliance is selected.
    private fun switchBorderToBlueToggle() {
        resetBackground()

        // Create selected blue toggle.
        createToggleButton(
            isBordered = true,
            toggleButton = blueToggleButton,
            toggleButtonColor = blueToggleButtonColor,
            toggleButtonColorDark = blueToggleButtonColorDark
        )
    }

    // Sets the edit match information activity screen the populates it with previously inputted data
    // Initiates the scout name dropdown so that users can change their scout name if they need
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditMatchInformationActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        populateData()
        initToggleButtons()
        initMatchNumberTextChangeListener()
        initTeamNumberTextChangeListeners()
        initScoutNameSpinner(context = this, spinner = binding.spinnerScoutName)
        initProceedButton()
        if (collectionMode == Constants.ModeSelection.OBJECTIVE) {
            binding.tvTeamOne.text = "Team Number"
        }
    }
}