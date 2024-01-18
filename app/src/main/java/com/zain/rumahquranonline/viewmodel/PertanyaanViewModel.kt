package com.zain.rumahquranonline.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zain.rumahquranonline.model.modelAyat.AyatItem
import com.zain.rumahquranonline.model.modelAyat.AyatResponse
import com.zain.rumahquranonline.model.modelAyat.Data
import com.zain.rumahquranonline.model.modelAyat.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class PertanyaanViewModel : ViewModel() {
    private val _ayatResponse = MutableLiveData<AyatResponse>()
    val ayatResponse: LiveData<AyatResponse> = _ayatResponse
    val currentAyat = MutableLiveData<AyatItem>()
    val answerOptions = MutableLiveData<List<String>>()
    var correctAnswer: String? = null
    fun loadAyat(suratNumber: Int) {
        viewModelScope.launch {
            val response = try {
                RetrofitClient.api.getAyat(suratNumber).awaitResponse()
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }
            if (response.isSuccessful) {
                response.body()?.data?.let { data ->
                    _ayatResponse.value = response.body()
                    prepareQuestionAndAnswers(data)
                }
            } else {
                // TODO: Handle the case where the response is not successful
            }
        }
    }
    private fun prepareQuestionAndAnswers(data: Data) {
        val ayatList = data.ayat?.filterNotNull()
        if (!ayatList.isNullOrEmpty() && ayatList.size > 1) {
            val questionIndex = ayatList.indices.random()
            val questionAyat = ayatList[questionIndex]
            currentAyat.value = questionAyat
            correctAnswer = ayatList.getOrNull(questionIndex + 1)?.teksArab
            val options = (ayatList - questionAyat).shuffled().take(2).mapNotNull { it.teksArab }.toMutableList()
            correctAnswer?.let { options.add(it) }
            options.shuffle()
            answerOptions.value = options
        }
    }

    fun checkAnswer(selectedAnswer: String): Boolean {
        return selectedAnswer.trim() == correctAnswer?.trim()
    }
}
