package com.example.codingexamkotlin

import android.R
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.codingexamkotlin.api.RetrofitInstance
import com.example.codingexamkotlin.databinding.ActivityMainBinding
import com.example.codingexamkotlin.model.UserInfo
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    var btSubmit: Button? = null
    var etFullname: EditText? = null
    var etEmail: EditText? = null
    var etMobileNumber: EditText? = null
    var tvAge: TextView? = null
    var etDateOfBirth: EditText? = null
    var age = 0
    var spinnerGender: Spinner? = null
    var gender: String? = null
    var datePicker: MaterialDatePicker<Long>? = null
    var scrollView: ScrollView? = null
    var currentDate: LocalDate? = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val genders = arrayOf("Male", "Female")
        binding.apply {
            this@MainActivity.btSubmit = btSubmit
            this@MainActivity.etFullname = etFullname
            this@MainActivity.etEmail = etEmailAddress
            this@MainActivity.etMobileNumber = etMobileNumber
            this@MainActivity.tvAge = tvAge
            this@MainActivity.etDateOfBirth = etDateOfBirth
            this@MainActivity.spinnerGender = spinnerGender
        }
        datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Enter Date of Birth")
                .build()
        val ad: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.simple_spinner_item, genders)
        spinnerGender!!.adapter = ad

        etDateOfBirth?.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                datePicker!!.show(supportFragmentManager, "DatePicker")
                etDateOfBirth!!.error = null
            }
        }

        datePicker!!.apply {
            addOnDismissListener {
                etDateOfBirth!!.clearFocus()
            }
            addOnPositiveButtonClickListener {
                var dpDate = datePicker!!.selection!!.toDatePicker()
                etDateOfBirth!!.setText("${dpDate.month}/${dpDate.dayOfMonth}/${dpDate.year}")



                if (currentDate!!.minus(
                        Period.of(
                            dpDate.year,
                            dpDate.month,
                            dpDate.dayOfMonth
                        )
                    ).year <= 0
                ) {
                    age = 0
                    tvAge!!.text = "Age: $age"
                } else {
                    age = currentDate!!.minus(
                        Period.of(
                            dpDate.year,
                            dpDate.month,
                            dpDate.dayOfMonth
                        )
                    ).year
                    tvAge!!.text = "Age: $age"
                }
            }
        }


        btSubmit?.setOnClickListener {


            isEmailValid()
            isAgeValid()
            isFullnameValid()
            isAgeValid()
            isNumberValid()
            val validationResult = (isEmailValid() && isAgeValid() && isFullnameValid() && isAgeValid() && isNumberValid())
            Log.i(TAG,"validationResults $validationResult")
            if (validationResult){
                var userInfo: UserInfo = UserInfo(
                    etFullname?.text.toString(),
                    etEmail?.text.toString(),
                    etMobileNumber?.text.toString(),
                    etDateOfBirth?.text.toString(),
                    spinnerGender!!.selectedItem.toString()
                )
                lifecycleScope.launch {
                    val response = try{
                        RetrofitInstance.api.submitDataOK(userInfo)
                    }catch (e: IOException){
                        Log.e(TAG, "IOException ${e}")
                        Toast.makeText(baseContext,e.localizedMessage,Toast.LENGTH_SHORT).show()
                        return@launch
                    }catch (e: HttpException){
                        Log.e(TAG,"HttpException ${e.toString()}")
                        Toast.makeText(baseContext,e.localizedMessage,Toast.LENGTH_SHORT).show()
                        return@launch
                    }
                    Log.i(TAG," success ${response.body()}")
                    Toast.makeText(baseContext,response.message().toString(),Toast.LENGTH_SHORT).show()
                }
            }


        }


    }

    private fun Long.toDatePicker(): DatePicker {
        var dp = DatePicker(baseContext)
        val date = java.util.Date(this)
        val formatdd = SimpleDateFormat("dd", Locale.US)
        val formatMM = SimpleDateFormat("MM", Locale.US)
        val formatyyyy = SimpleDateFormat("yyyy", Locale.US)
        dp.updateDate(
            formatyyyy.format(date).toInt(),
            formatMM.format(date).toInt(),
            formatdd.format(date).toInt()
        )
        return dp
    }

    private fun isEmailValid(): Boolean {
        val emailInput: String = etEmail?.text.toString()
        if (!emailInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            return true
        } else {
            etEmail?.error = "Enter a valid email"
            return false
        }
    }

    private fun isFullnameValid(): Boolean {
        val fullnameText: String = etFullname?.text.toString()
        if (fullnameText.isEmpty()) {
            etFullname?.error = "Fullname should not be empty"
            return false
        } else if (!fullnameText.matches("[a-zA-Z,. ]+".toRegex())) {
            etFullname?.error = "Only letters, (,) and (.) are accepted"
            return false
        } else {
            return true
        }
    }

    private fun isNumberValid(): Boolean {
        val numbers: String = etMobileNumber?.text.toString()
        if (!numbers.matches("[0-9]+".toRegex()) || numbers.length != 11 || numbers[0] != '0' || numbers[1] != '9' || etMobileNumber!!.text.isEmpty()) {
            etMobileNumber?.error = "Enter a valid number"
            return false
        } else {
            return true
        }
    }


    private fun isAgeValid(): Boolean {
        if (age <= 17) {
            etDateOfBirth?.error = "User must be 18 and above"
            tvAge!!.text = "Age: $age."
            return false
        } else {
            etDateOfBirth?.error = null
            return true
        }
    }



companion object{
    const val TAG = "MainActivity"
}
}