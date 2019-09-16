package com.anand.swipelylib

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView

class CustomView(context: Context) {

    private var mContext: Context = context
    private var view: View
    private var progressBar: ProgressBar
    private var textView: TextView
    private var titleText: TextView
    private var title: String? = null

    init {
        val layout =
            (context as Activity).findViewById<View>(android.R.id.content).rootView as ViewGroup
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater.inflate(R.layout.custom_view, null)
        view.background.alpha = 100
        progressBar = view.findViewById(R.id.progressBar) as ProgressBar
        progressBar.setBackgroundColor(0x00000000)
        textView = view.findViewById(R.id.textView2) as TextView
        val type = Typeface.createFromAsset(context.getAssets(), "DroidSans-Bold.ttf")
        textView.typeface = type
        titleText = view.findViewById(R.id.textView) as TextView
        val type1 = Typeface.createFromAsset(context.getAssets(), "DroidSans-Bold.ttf")
        titleText.typeface = type1
        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        params.leftMargin = 100
        params.bottomMargin = 10
        val relativeLayout = RelativeLayout(context)
        relativeLayout.gravity = Gravity.CENTER
        relativeLayout.addView(view)
        layout.addView(relativeLayout, params)
        view.requestLayout()
        view.visibility = View.INVISIBLE
    }

    fun setProgress(n: Int) {
        progressBar.progress = n
    }

    fun show() {
        view.visibility = View.VISIBLE
        titleText.text = title
    }

    fun hide() {
        view.visibility = View.INVISIBLE
    }

    fun isVisible(): Boolean {
        return view.visibility == View.VISIBLE
    }

    fun setTitle(s: String) {
        title = s
    }

    fun setProgressText(s: String) {
        textView.text = s
    }
}

