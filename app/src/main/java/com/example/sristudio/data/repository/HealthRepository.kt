package com.example.sristudio.data.repository

import com.example.sristudio.data.local.PreferencesDataStore
import com.example.sristudio.data.local.dao.*
import com.example.sristudio.data.local.entity.*
import kotlinx.coroutines.flow.*
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthRepository @Inject constructor(
    private val stepDao: StepDao,
    private val sleepDao: SleepDao,
    private val waterDao: WaterDao,
    private val calorieDao: CalorieDao,
    private val workoutDao: WorkoutDao,
    private val heartRateDao: HeartRateDao,
    private val wellnessScoreDao: WellnessScoreDao,
    private val userAccountDao: UserAccountDao,
    val preferences: PreferencesDataStore
) {
    // --- Auth ---
    suspend fun register(emailOrPhone: String, password: String, displayName: String): Result<Long> {
        return try {
            if (userAccountDao.isRegistered(emailOrPhone)) {
                Result.failure(Exception("Account already exists"))
            } else {
                val hash = hashPassword(password)
                val account = if (emailOrPhone.contains("@")) {
                    UserAccount(email = emailOrPhone, passwordHash = hash, displayName = displayName)
                } else {
                    UserAccount(phone = emailOrPhone, passwordHash = hash, displayName = displayName)
                }
                val id = userAccountDao.insert(account)
                preferences.setLoggedIn(id)
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(emailOrPhone: String, password: String): Result<UserAccount> {
        return try {
            val hash = hashPassword(password)
            val account = userAccountDao.authenticate(emailOrPhone, hash)
            if (account != null) {
                preferences.setLoggedIn(account.id)
                Result.success(account)
            } else {
                Result.failure(Exception("Invalid credentials"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        preferences.setLoggedOut()
    }

    suspend fun getCurrentUser(): UserAccount? {
        val userId = preferences.loggedInUserId.firstOrNull() ?: -1L
        return if (userId > 0) userAccountDao.getById(userId) else null
    }

    fun observeCurrentUser(): Flow<UserAccount?> {
        return preferences.loggedInUserId.flatMapLatest { id ->
            if (id > 0) userAccountDao.observeById(id) else flowOf(null)
        }
    }

    suspend fun updateProfile(userId: Long, name: String, age: Int, weight: Double, height: Double, gender: String) {
        userAccountDao.updateProfile(userId, name, age, weight, height, gender)
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // --- Steps ---
    suspend fun insertSteps(record: StepRecord) = stepDao.insert(record)
    suspend fun getStepsByDate(date: String) = stepDao.getByDate(date)
    fun observeStepsByDate(date: String) = stepDao.observeByDate(date)
    fun getRecentSteps(limit: Int = 7) = stepDao.getRecent(limit)
    fun getStepsRange(start: String, end: String) = stepDao.getRange(start, end)

    // --- Sleep ---
    suspend fun insertSleep(record: SleepRecord) = sleepDao.insert(record)
    fun observeSleepByDate(date: String) = sleepDao.observeByDate(date)
    fun getRecentSleep(limit: Int = 7) = sleepDao.getRecent(limit)

    // --- Water ---
    suspend fun insertWater(record: WaterRecord) = waterDao.insert(record)
    fun getWaterByDate(date: String) = waterDao.getByDate(date)
    fun getWaterTotalForDate(date: String) = waterDao.getTotalForDate(date)
    suspend fun deleteWater(id: Long) = waterDao.delete(id)

    // --- Calories ---
    suspend fun insertCalorie(record: CalorieRecord) = calorieDao.insert(record)
    fun getCaloriesByDate(date: String) = calorieDao.getByDate(date)
    fun getCalorieTotalForDate(date: String) = calorieDao.getTotalForDate(date)
    suspend fun deleteCalorie(id: Long) = calorieDao.delete(id)

    // --- Workouts ---
    suspend fun insertWorkout(record: WorkoutRecord) = workoutDao.insert(record)
    fun getWorkoutsByDate(date: String) = workoutDao.getByDate(date)
    fun getWorkoutMinutesForDate(date: String) = workoutDao.getTotalMinutesForDate(date)
    suspend fun deleteWorkout(id: Long) = workoutDao.delete(id)

    // --- Heart Rate ---
    suspend fun insertHeartRate(record: HeartRateRecord) = heartRateDao.insert(record)
    fun getHeartRateByDate(date: String) = heartRateDao.getByDate(date)
    fun getHeartRateAvgForDate(date: String) = heartRateDao.getAverageForDate(date)

    // --- Wellness Score ---
    suspend fun insertWellnessScore(record: WellnessScoreRecord) = wellnessScoreDao.insert(record)
    fun observeWellnessScore(date: String) = wellnessScoreDao.observeByDate(date)
    fun getRecentScores(limit: Int = 7) = wellnessScoreDao.getRecent(limit)

    // --- Goals (delegated to preferences) ---
    val goalSteps: Flow<Int> = preferences.goalSteps
    val goalWaterMl: Flow<Int> = preferences.goalWaterMl
    val goalSleepMinutes: Flow<Int> = preferences.goalSleepMinutes
    val goalCalories: Flow<Int> = preferences.goalCalories
    val goalWorkoutMinutes: Flow<Int> = preferences.goalWorkoutMinutes
}
