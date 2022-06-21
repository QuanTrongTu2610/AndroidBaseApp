package com.example.androidbaseapp.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.example.androidbaseapp.R
import com.example.androidbaseapp.common.views.SafetyClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_base.*
import kotlinx.android.synthetic.main.fragment_base.view.*

/**
 * BaseFragment used to defined general implementations for all fragments
 * */
@AndroidEntryPoint
abstract class BaseFragment : Fragment(), IBaseFragment {
    override val baseActivity: BaseActivity?
        get() = activity as? BaseActivity

    override val safetyClick: SafetyClickListener by lazy { SafetyClickListener() }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // inflate loading as default fragment screen
        val viewRoot = inflater.inflate(R.layout.fragment_base, container, false)
        inflater.inflate(getLayoutId(), viewRoot.contentContainer, true)
        return viewRoot
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData(arguments)
        initViews()
        initActions()
        initObservers()
    }

    override fun showLoading() {
        loadingView.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        loadingView.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        safetyClick.clearAllSafetyClickListeners()
        super.onDestroyView()
    }
}