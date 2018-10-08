package com.ide.shota.viewintentcatalog

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Browser
import android.provider.MediaStore
import android.speech.RecognizerResultsIntent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.transition.ChangeImageTransform
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionSet
import android.view.Gravity
import android.view.View

import java.util.ArrayList
import java.util.Arrays

import com.ide.shota.viewintentcatalog.IntentDetailFragment.Companion.TRANSITION_NAME_INTENT_TITLE
import com.ide.shota.viewintentcatalog.IntentDetailFragment.Companion.VIEW_INTENT

class MainActivity : AppCompatActivity(), IntentListFragment.Callback, IntentDetailFragment.Callback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        var fragment = fragmentManager.findFragmentByTag(INTENT_LIST_FRAGMENT_TAG)
        if (fragment == null) {
            fragment = IntentListFragment()
            fragmentManager.beginTransaction()
                    .add(R.id.container, fragment, INTENT_LIST_FRAGMENT_TAG)
                    .commit()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val ts = TransitionSet()
            ts.addTransition(Fade())
            val slide = Slide()
            slide.slideEdge = Gravity.START
            ts.addTransition(slide)
            fragment.exitTransition = ts
            fragment.reenterTransition = ts
        }
    }

    override fun onSelectedIntent(view: View, viewIntent: ViewIntent) {
        var fragment = supportFragmentManager.findFragmentByTag(INTENT_DETAIL_FRAGMENT_TAG)
        val transaction = supportFragmentManager.beginTransaction()

        if (fragment == null) {
            fragment = IntentDetailFragment()

            val args = Bundle()
            args.putSerializable(VIEW_INTENT, viewIntent)
            fragment.arguments = args
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val ts1 = TransitionSet()
            ts1.addTransition(ChangeImageTransform())
            fragment.sharedElementEnterTransition = ts1

            val ts2 = TransitionSet()
            ts2.addTransition(Fade())
            val slide = Slide()
            slide.slideEdge = Gravity.END
            ts2.addTransition(slide)
            fragment.enterTransition = ts2
            fragment.exitTransition = ts2

            val targetView = view.findViewById<View>(R.id.intent_title)
            targetView.transitionName = TRANSITION_NAME_INTENT_TITLE
            transaction.addSharedElement(targetView, TRANSITION_NAME_INTENT_TITLE)
        }

        transaction.replace(R.id.container, fragment, INTENT_DETAIL_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit()
    }

    override fun onStartIntent(viewIntent: ViewIntent, data: String, app: String, zoom: Boolean) {
        val intent: Intent

        when (viewIntent.title) {
            "ACTION_VIEW" -> if (!data.isEmpty()) {
                intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(data)
                if (!app.isEmpty()) {
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, app)
                }
                if (zoom) {
                    intent.putExtra(Browser.INITIAL_ZOOM_LEVEL, ZOOM_LEVEL)
                }
                startActivity(intent)
            }
            "CUSTOM_TABS" -> if (!data.isEmpty()) {
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                if (!app.isEmpty()) {
                    customTabsIntent.intent.setPackage(app)
                }
                customTabsIntent.launchUrl(this, Uri.parse(data))
            }
            "ACTION_MEDIA_SEARCH" -> {
                intent = Intent(MediaStore.INTENT_ACTION_MEDIA_SEARCH)
                if (!data.isEmpty()) {
                    intent.putExtra(SearchManager.QUERY, data)
                }
                if (!app.isEmpty()) {
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, app)
                }
                if (zoom) {
                    intent.putExtra(Browser.INITIAL_ZOOM_LEVEL, ZOOM_LEVEL)
                }
                startActivity(intent)
            }
            "ACTION_WEB_SEARCH" -> {
                intent = Intent(Intent.ACTION_WEB_SEARCH)
                if (!data.isEmpty()) {
                    intent.putExtra(SearchManager.QUERY, data)
                }
                if (!app.isEmpty()) {
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, app)
                }
                if (zoom) {
                    intent.putExtra(Browser.INITIAL_ZOOM_LEVEL, ZOOM_LEVEL)
                }
                startActivity(intent)
            }
            "ACTION_VOICE_SEARCH" -> {
                intent = Intent(RecognizerResultsIntent.ACTION_VOICE_SEARCH_RESULTS)
                if (!data.isEmpty()) {
                    intent.putStringArrayListExtra(
                            RecognizerResultsIntent.EXTRA_VOICE_SEARCH_RESULT_STRINGS,
                            ArrayList(Arrays.asList(data)))
                }
                if (!app.isEmpty()) {
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, app)
                }
                if (zoom) {
                    intent.putExtra(Browser.INITIAL_ZOOM_LEVEL, ZOOM_LEVEL)
                }
                startActivity(intent)
            }
            else -> {
            }
        }
    }

    companion object {
        const val INTENT_LIST_FRAGMENT_TAG = "item_list"
        const val INTENT_DETAIL_FRAGMENT_TAG = "intent_detail"
        const val ZOOM_LEVEL = 400
    }
}
