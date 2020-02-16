package com.sample.roomandpaginlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_first.view.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    lateinit var vm: FirstViewModel// by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        vm = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FirstViewModel(activity!!.application as Application) as T
            }
        }).get(FirstViewModel::class.java)

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindWordRecyclerView(view)
        bindLingovoRecyclerView(view)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment("From FirstFragment")
//            findNavController().navigate(action)
//        }
    }

    private fun bindWordRecyclerView(view: View) {
        val adapter = WordPagedAdapter()
        view.recycler_view.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }

        vm.words.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.submitList(it)
            }
        })
    }

    private fun bindLingovoRecyclerView(view: View) {
        val adapter = LingvoPagedAdapter()
        view.recycler_view2.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = adapter
        }

        vm.lingvoItems.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                adapter.submitList(it)
            }
        })
    }
}