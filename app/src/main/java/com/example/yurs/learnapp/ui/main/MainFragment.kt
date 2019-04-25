package com.example.yurs.learnapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.yurs.learnapp.R
import android.widget.Toast
import com.example.yurs.learnapp.App
import com.example.yurs.learnapp.MainActivity
import com.example.yurs.learnapp.widget.BaseNiceDialog
import com.example.yurs.learnapp.widget.ViewHolder
import com.example.yurs.learnapp.widget.ViewConvertListener
import com.example.yurs.learnapp.widget.NiceDialog



class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun initView() {
        NiceDialog.init()
                .setLayoutId(R.layout.share_layout)
                .setConvertListener(object : ViewConvertListener() {
                    public override fun convertView(holder: ViewHolder, dialog: BaseNiceDialog) {
                        holder.setOnClickListener(R.id.wechat) {
                            Toast.makeText(App.getContext(), "分享成功", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                .setDimAmount(0.3f)
                .setShowBottom(true)
                .show(this.activity?.getSupportFragmentManager())
    }

}
