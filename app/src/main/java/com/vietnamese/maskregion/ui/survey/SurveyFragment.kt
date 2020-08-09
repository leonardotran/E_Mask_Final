package com.vietnamese.maskregion.ui.survey

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.vietnamese.maskregion.databinding.FragmentSurveyBinding
import com.vietnamese.maskregion.model.Device
import com.vietnamese.maskregion.model.Survey
import com.vietnamese.maskregion.ui.home.DeviceAdapter

class SurveyFragment : Fragment() {

    private lateinit var binding: FragmentSurveyBinding
    var count: Int = 0

    private lateinit var surveyAdapter: SurveyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSurveyBinding.inflate(layoutInflater)
        surveyAdapter = SurveyAdapter()
        surveyAdapter.initRecyclerViews(binding.myRecyclerView)


        populateData(count)
        count++
        binding.submitButton.setOnClickListener {
            populateData(count)
            count++
        }



        return binding.root
    }

    private fun populateData(count: Int) {
        surveyAdapter.clear()
        val db = Firebase.firestore
        val data = db.collection("question").document(count.toString())
        data.get().addOnSuccessListener { documentSnapshot ->
            documentSnapshot.toObject<Survey>().let {
                val survey: Survey? = it
                var questions = ""
                survey?.answer?.forEach {
                    Log.d("answer",""+it)
                    surveyAdapter.insertItem(it)
                }
                survey?.content?.forEach {
                    Log.d("content",""+it)
                    questions += it + "\n"
                }
                binding.editTextQuestion.text = questions

            }
        }

    }
}
