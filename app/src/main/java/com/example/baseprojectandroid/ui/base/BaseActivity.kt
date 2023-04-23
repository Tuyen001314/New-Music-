package com.example.baseprojectandroid.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : BaseView, AppCompatActivity() {

    protected lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = layoutInflater.inflate(getContentViewId(), null)
        setContentView(view)
        init(view)
    }

    open fun init(view: View) {

    }

    fun hiddenKeyboard() {
        var viewFocus = view.findFocus()
        if (viewFocus == null) {
            viewFocus = findViewById(android.R.id.content) ?: return
        }

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(viewFocus.windowToken, 0)
        viewFocus.clearFocus()
    }

    fun showKeyboard(view: View? = null) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val v = view ?: this.currentFocus as? EditText
        if (v != null) {
            inputMethodManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT)
        } else {
            inputMethodManager.toggleSoftInput(
                InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY
            )
        }
    }

}