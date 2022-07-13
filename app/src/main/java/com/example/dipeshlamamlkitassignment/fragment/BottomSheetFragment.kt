package com.example.dipeshlamamlkitassignment.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.dipeshlamamlkitassignment.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.concurrent.Executors

class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var txvScanText: TextView
    private var fetchUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txvScanText = view.findViewById(R.id.txvScanLink)
        txvScanText.text = fetchUrl.toString()

    }

    fun fetchUrl(url: String) {
        val executorService = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executorService.execute {
            fetchUrl = url
        }
    }
}