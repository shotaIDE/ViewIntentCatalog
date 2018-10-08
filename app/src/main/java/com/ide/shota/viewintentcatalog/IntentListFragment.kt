package com.ide.shota.viewintentcatalog

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.SimpleAdapter

class IntentListFragment : Fragment() {

    private val intentList = listOf(
            ViewIntent("ACTION_VIEW", "ウェブページを他アプリで閲覧するときに利用するインテント", true, true),
            ViewIntent("CUSTOM_TABS", "ウェブページを他アプリのActivityを流用して閲覧するときに利用するインテント", true, false),
            ViewIntent("ACTION_MEDIA_SEARCH", "メディアファイルを他アプリで閲覧するときに利用するインテント", true, true),
            ViewIntent("ACTION_WEB_SEARCH", "ウェブの検索結果を他アプリで閲覧するときに利用するインテント", true, true),
            ViewIntent("ACTION_VOICE_SEARCH", "音声入力の結果を受け取るためのインテント", true, true)
    )

    interface Callback {
        fun onSelectedIntent(view: View, viewIntent: ViewIntent)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        val rootView = inflater.inflate(R.layout.intent_list, container, false)

        val intentLabelList = intentList.map { mapOf("title" to it.title, "description" to it.description) }
        val listView = rootView.findViewById<ListView>(R.id.intent_card_list)
        val simpleAdapter = SimpleAdapter(context,
                intentLabelList,
                R.layout.intent_list_item,
                arrayOf("title", "description"),
                intArrayOf(R.id.intent_title, R.id.intent_description))
        listView.adapter = simpleAdapter
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, view, pos, _ ->
            (activity as Callback).onSelectedIntent(view, intentList[pos])
        }

        return rootView
    }
}
