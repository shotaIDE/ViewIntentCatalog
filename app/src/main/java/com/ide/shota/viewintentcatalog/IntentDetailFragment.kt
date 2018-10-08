package com.ide.shota.viewintentcatalog

import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView

class IntentDetailFragment : Fragment() {

    private lateinit var viewIntent: ViewIntent

    interface Callback {
        fun onStartIntent(viewIntent: ViewIntent, data: String, app: String, zoom: Boolean)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.intent_detail, container, false)

        viewIntent = arguments!!.getSerializable(VIEW_INTENT) as ViewIntent
        val tvTitle = rootView.findViewById<TextView>(R.id.intent_detail_title)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tvTitle.transitionName = TRANSITION_NAME_INTENT_TITLE
        }
        tvTitle.text = viewIntent.title

        val dataInput = rootView.findViewById<EditText>(R.id.intent_detail_data_input)
        var button = rootView.findViewById<Button>(R.id.intent_detail_data_input_button1)
        button.setOnClickListener { dataInput.setText(R.string.intent_detail_data_sample1) }
        button = rootView.findViewById(R.id.intent_detail_data_input_button2)
        button.setOnClickListener { dataInput.setText(R.string.intent_detail_data_sample2) }
        button = rootView.findViewById(R.id.intent_detail_data_input_button3)
        button.setOnClickListener { dataInput.setText(R.string.intent_detail_data_sample3) }
        button = rootView.findViewById(R.id.intent_detail_data_input_button4)
        button.setOnClickListener { dataInput.setText(R.string.intent_detail_data_sample4) }
        button = rootView.findViewById(R.id.intent_detail_data_clear)
        button.setOnClickListener { dataInput.setText("") }

        val appInput = rootView.findViewById<EditText>(R.id.intent_detail_app_input)
        if (viewIntent.hasAppName) {
            button = rootView.findViewById(R.id.intent_detail_app_sample1_input)
            button.setOnClickListener { appInput.setText(R.string.intent_detail_app_sample1) }
            button = rootView.findViewById(R.id.intent_detail_app_sample2_input)
            button.setOnClickListener { appInput.setText(R.string.intent_detail_app_sample2) }
            button = rootView.findViewById(R.id.intent_detail_app_clear)
            button.setOnClickListener { appInput.setText("") }
        } else {
            val appField = rootView.findViewById<View>(R.id.intent_content_app)
            appField.visibility = GONE
        }

        val zoomSwitch = rootView.findViewById<Switch>(R.id.intent_detail_zoom_input)
        if (!viewIntent.hasZoomLevel) {
            val zoomField = rootView.findViewById<View>(R.id.intent_content_zoom)
            zoomField.visibility = GONE
        }

        button = rootView.findViewById(R.id.intent_detail_start_intent)
        button.setOnClickListener {
            val data = dataInput.text.toString()
            val app = appInput.text.toString()
            val zoom = zoomSwitch.isChecked
            (activity as Callback).onStartIntent(viewIntent, data, app, zoom)
        }

        return rootView
    }

    companion object {
        const val VIEW_INTENT = "view_intent"
        const val TRANSITION_NAME_INTENT_TITLE = "intent_title"
    }
}
