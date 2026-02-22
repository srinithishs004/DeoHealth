package com.example.sristudio.domain

import com.example.sristudio.data.local.entity.WellnessScoreRecord
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Calculates the Wellness Score (0-100) by combining all health metrics.
 *
 * Weights:
 *  - Steps:    25%
 *  - Sleep:    25%
 *  - Water:    20%
 *  - Calories: 15%
 *  - Workout:  15%
 */
@Singleton
class WellnessScoreCalculator @Inject constructor() {

    data class MetricInput(
        val steps: Int = 0,
        val goalSteps: Int = 8000,
        val sleepMinutes: Int = 0,
        val goalSleepMinutes: Int = 480,
        val waterMl: Int = 0,
        val goalWaterMl: Int = 2500,
        val calories: Int = 0,
        val goalCalories: Int = 2000,
        val workoutMinutes: Int = 0,
        val goalWorkoutMinutes: Int = 30
    )

    fun calculate(input: MetricInput): WellnessScoreRecord {
        val stepsScore = calcComponentScore(input.steps, input.goalSteps)
        val sleepScore = calcComponentScore(input.sleepMinutes, input.goalSleepMinutes)
        val waterScore = calcComponentScore(input.waterMl, input.goalWaterMl)
        val caloriesScore = calcCalorieScore(input.calories, input.goalCalories)
        val workoutScore = calcComponentScore(input.workoutMinutes, input.goalWorkoutMinutes)

        val weightedScore = (
            stepsScore * 0.25 +
            sleepScore * 0.25 +
            waterScore * 0.20 +
            caloriesScore * 0.15 +
            workoutScore * 0.15
        ).toInt().coerceIn(0, 100)

        val tip = generateTip(input, stepsScore, sleepScore, waterScore, workoutScore)

        return WellnessScoreRecord(
            date = LocalDate.now().toString(),
            score = weightedScore,
            stepsScore = stepsScore,
            sleepScore = sleepScore,
            waterScore = waterScore,
            caloriesScore = caloriesScore,
            workoutScore = workoutScore,
            tip = tip
        )
    }

    private fun calcComponentScore(actual: Int, goal: Int): Int {
        if (goal <= 0) return 100
        val ratio = actual.toDouble() / goal
        return (ratio * 100).toInt().coerceIn(0, 100)
    }

    /**
     * For calories, being close to the goal is ideal.
     * Over or under is penalized symmetrically.
     */
    private fun calcCalorieScore(actual: Int, goal: Int): Int {
        if (goal <= 0) return 100
        val ratio = actual.toDouble() / goal
        val deviation = kotlin.math.abs(1.0 - ratio)
        return ((1.0 - deviation) * 100).toInt().coerceIn(0, 100)
    }

    private fun generateTip(
        input: MetricInput,
        stepsScore: Int,
        sleepScore: Int,
        waterScore: Int,
        workoutScore: Int
    ): String {
        // Find the weakest area and give a specific tip
        val scores = listOf(
            "steps" to stepsScore,
            "sleep" to sleepScore,
            "water" to waterScore,
            "workout" to workoutScore
        )
        val weakest = scores.minByOrNull { it.second } ?: return "Keep up the great work!"

        return when (weakest.first) {
            "steps" -> {
                val remaining = (input.goalSteps - input.steps).coerceAtLeast(0)
                if (remaining > 0) "Walk ${remaining} more steps to reach your goal!" else "Great step count today!"
            }
            "sleep" -> {
                val remainingHrs = ((input.goalSleepMinutes - input.sleepMinutes) / 60.0).coerceAtLeast(0.0)
                if (remainingHrs > 0) "Try to get ${String.format("%.1f", remainingHrs)} more hours of sleep tonight." else "Well rested!"
            }
            "water" -> {
                val remainingGlasses = ((input.goalWaterMl - input.waterMl) / 250).coerceAtLeast(0)
                if (remainingGlasses > 0) "Drink $remainingGlasses more glasses of water today." else "Hydration on point!"
            }
            "workout" -> {
                val remaining = (input.goalWorkoutMinutes - input.workoutMinutes).coerceAtLeast(0)
                if (remaining > 0) "Add $remaining minutes of exercise to boost your score!" else "Workout goal smashed!"
            }
            else -> "Keep up the great work!"
        }
    }
}
