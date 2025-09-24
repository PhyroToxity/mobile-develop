package com.example.helloworldapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var rgGender: RadioGroup
    private lateinit var spCourse: Spinner
    private lateinit var sbDifficulty: SeekBar
    private lateinit var cvBirthdate: CalendarView
    private lateinit var btnSubmit: Button
    private lateinit var tvResult: TextView
    private lateinit var ivZodiac: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etFullName = findViewById(R.id.et_full_name) ?: return
        rgGender = findViewById(R.id.rg_gender) ?: return
        spCourse = findViewById(R.id.sp_course) ?: return
        sbDifficulty = findViewById(R.id.sb_difficulty) ?: return
        cvBirthdate = findViewById(R.id.cv_birthdate) ?: return
        btnSubmit = findViewById(R.id.btn_submit) ?: return
        tvResult = findViewById(R.id.tv_result) ?: return
        ivZodiac = findViewById(R.id.iv_zodiac) ?: return

        val adapter = ArrayAdapter.createFromResource(this, R.array.courses, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCourse.adapter = adapter

        // Добавляем слушатель для CalendarView
        cvBirthdate.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            cvBirthdate.date = calendar.timeInMillis // Устанавливаем выбранную дату
        }

        btnSubmit.setOnClickListener {
            val fullName = etFullName.text.toString()
            val genderId = rgGender.checkedRadioButtonId
            val gender = if (genderId == R.id.rb_male) "Мужской" else "Женский"
            val course = spCourse.selectedItem.toString()
            val difficulty = sbDifficulty.progress
            val birthDate = cvBirthdate.date // Берет выбранную дату

            val zodiac = calculateZodiac(birthDate)

            val player = Player(fullName, gender, course, difficulty, birthDate, zodiac)

            tvResult.text = "ФИО: ${player.fullName}\nПол: ${player.gender}\nКурс: ${player.course}\nСложность: ${player.difficulty}\nДата рождения: ${SimpleDateFormat("dd.MM.yyyy").format(Date(player.birthDate))}\nЗодиак: ${player.zodiac}"

            ivZodiac.setImageResource(getZodiacImage(zodiac))
            ivZodiac.visibility = View.VISIBLE
        }
    }

    private fun calculateZodiac(dateMillis: Long): String {
        val calendar = Calendar.getInstance().apply { timeInMillis = dateMillis }
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        return when {
            (month == 3 && day >= 21) || (month == 4 && day <= 19) -> "Овен"
            (month == 4 && day >= 20) || (month == 5 && day <= 20) -> "Телец"
            (month == 5 && day >= 21) || (month == 6 && day <= 20) -> "Близнецы"
            (month == 6 && day >= 21) || (month == 7 && day <= 22) -> "Рак"
            (month == 7 && day >= 23) || (month == 8 && day <= 22) -> "Лев"
            (month == 8 && day >= 23) || (month == 9 && day <= 22) -> "Дева"
            (month == 9 && day >= 23) || (month == 10 && day <= 22) -> "Весы"
            (month == 10 && day >= 23) || (month == 11 && day <= 21) -> "Скорпион"
            (month == 11 && day >= 22) || (month == 12 && day <= 21) -> "Стрелец"
            (month == 12 && day >= 22) || (month == 1 && day <= 19) -> "Козерог"
            (month == 1 && day >= 20) || (month == 2 && day <= 18) -> "Водолей"
            else -> "Рыбы"
        }
    }

    private fun getZodiacImage(zodiac: String): Int {
        return when (zodiac) {
            "Овен" -> R.drawable.aries
            "Телец" -> R.drawable.taurus
            "Близнецы" -> R.drawable.gemini
            "Рак" -> R.drawable.cancer
            "Лев" -> R.drawable.leo
            "Дева" -> R.drawable.virgo
            "Весы" -> R.drawable.libra
            "Скорпион" -> R.drawable.scorpio
            "Стрелец" -> R.drawable.sagittarius
            "Козерог" -> R.drawable.capricorn
            "Водолей" -> R.drawable.aquarius
            "Рыбы" -> R.drawable.pisces
            else -> R.drawable.ic_launcher_foreground
        }
    }
}

data class Player(
    val fullName: String,
    val gender: String,
    val course: String,
    val difficulty: Int,
    val birthDate: Long,
    val zodiac: String
)