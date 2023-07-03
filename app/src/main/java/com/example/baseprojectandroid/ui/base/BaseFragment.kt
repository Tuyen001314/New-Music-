package com.example.baseprojectandroid.ui.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.example.baseprojectandroid.R

abstract class BaseFragment : BaseView, Fragment() {

    var mView: View? = null
    private lateinit var mHandler: Handler
    protected var durationDelayed: Long = 800

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler = Handler()
        Log.d(javaClass.name, "onCreate()...")
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(javaClass.name, "onCreateView()...")
        val view = inflater.inflate(getContentViewId(), null)
        mView = view
        view.isClickable = true
        view.isFocusable = true
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.name, "onViewCreated()...")
        initializeViews()
        registerObservers()
        initializeData()
        registerListeners()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!onBackPressed()) {
                isEnabled = false
                requireActivity().onBackPressed()
            }
        }
    }

    fun openFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.main_container, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    fun openFragmentNotFullScreen(idLayout: Int, fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(idLayout, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    fun removeFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
            .commitAllowingStateLoss()
    }


    fun showToast(str: String) {
        val con = activity ?: return
        Toast.makeText(con, str, Toast.LENGTH_SHORT).show()
    }

    open fun showToast(@StringRes id: Int) {
        val con = activity ?: return
        Toast.makeText(con, id, Toast.LENGTH_SHORT).show()
    }

    fun showLongToast(str: String) {
        val con = activity ?: return
        Toast.makeText(con, str, Toast.LENGTH_LONG).show()
    }

    open fun showLongToast(@StringRes id: Int) {
        val con = activity ?: return
        Toast.makeText(con, id, Toast.LENGTH_LONG).show()
    }

    fun postDelayed(run: Runnable, delayMillis: Long) {
        mHandler.postDelayed(run, delayMillis)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroyView()
    }

    open fun onBackPressed() = false

    open fun hiddenKeyboard() {
        (activity as? BaseActivity)?.hiddenKeyboard()
    }

    fun showKeyboard(view: View? = null) {
        (activity as? BaseActivity)?.showKeyboard(view)
    }

}
