package com.frc135.match_collection

import android.app.Activity

// Contains constant values and enum classes.
class Constants {
    companion object {
        const val NONE_VALUE: String = "NONE"
        const val NUMBER_OF_ACTIVE_SCOUTS: Int = 6
        const val COMPRESSED_QR_TAG = "QR"
        const val PREVIOUS_SCREEN = "previous_screen"
        const val VERSION_NUMBER = "1.0.4"
        const val EVENT_KEY = "2026inmis"

        /**
         * The previous activity that was visited before this one. This is found by looking for the
         * intent extra with key [PREVIOUS_SCREEN].
         */
        val Activity.previousScreen
            get() = intent.getSerializableExtra(PREVIOUS_SCREEN) as? Screens
    }

    /**
     * Every screen in the app.
     */
    enum class Screens {
        COLLECTION_OBJECTIVE,
        COLLECTION_SUBJECTIVE,
        MATCH_INFORMATION_INPUT,
        MATCH_INFORMATION_EDIT,
        MODE_COLLECTION_SELECT,
        QR_GENERATE,
        STARTING_POSITION_OBJECTIVE
    }

    enum class ModeSelection {
        SUBJECTIVE,
        OBJECTIVE,
        NONE
    }

    enum class AllianceColor {
        RED,
        BLUE,
        NONE
    }

    enum class StageLevel {
        N, // NONE
        DF, // DEEP FAILED
        SF, // SHALLOW FAILED
        D, // DEEP
        S, // SHALLOW

    }

    enum class ActionType {
        SCORE_ALGAE_NET,
        SCORE_ALGAE_PROCESSOR,
        ALGAE,
        outpost,
        L1_Tower,
        L2_Tower,
        L3_Tower,
        FAIL,
        SCORE_Hub,



        INTAKE_CORAL_GROUND,
        INTAKE_ALGAE_GROUND,
        INTAKE_LEFT_CORAL,
        INTAKE_RIGHT_CORAL,
        AUTO_INTAKE_LEFT_CORAL,
        AUTO_INTAKE_RIGHT_CORAL,
        AUTO_INTAKE_TOP_TREE_CORAL,
        AUTO_INTAKE_MID_TREE_CORAL,
        AUTO_INTAKE_BOTTOM_TREE_CORAL,
        AUTO_INTAKE_TOP_TREE_ALGAE,
        AUTO_INTAKE_MID_TREE_ALGAE,
        AUTO_INTAKE_BOTTOM_TREE_ALGAE,

        START_INCAP,
        END_INCAP,
        TO_TELEOP,
        TO_ENDGAME,
//        SCORE_TRAP,
        LEFT_START

    }

    enum class Stage {
        AUTO,
        TELEOP,
        ENDGAME
    }

    enum class AssignmentMode {
        NONE,
        AUTOMATIC_ASSIGNMENT,
        OVERRIDE
    }
}
