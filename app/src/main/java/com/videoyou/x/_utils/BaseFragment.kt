package com.videoyou.x._utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<VM : ViewModel, VB : ViewBinding> : Fragment(), BaseFragmentView<VB> {
    protected val TAG: String = javaClass.simpleName
    protected lateinit var binding: VB
    protected lateinit var mViewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = createViewModel()
        onViewCreate()
    }

    private fun <VM : ViewModel> createViewModel(): VM {
        return ViewModelProvider(this)[getVmClazz(this)]
    }

    private fun <VM : ViewModel> getVmClazz(obj: Any): Class<VM> {
        return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VM>
    }

}

interface BaseFragmentView<VB:ViewBinding> {
    fun onViewCreate()

    fun getViewBinding(): VB
}
