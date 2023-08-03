package com.example.stajtvplus.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.stajtvplus.databinding.FragmentNotificationsBinding
import com.example.stajtvplus.models.NotificationMessage

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var adapter: ArrayAdapter<NotificationMessage>
    lateinit var viewModel: NotificationsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, listOf())
        binding.listNotification.adapter = adapter

        return root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NotificationsViewModel::class.java)
        subscribe()
        viewModel.getNotifications()
    }

    private fun subscribe(){
        viewModel.notificationList.observe(viewLifecycleOwner) { nList ->
            adapter = ArrayAdapter(requireContext(),android.R.layout.simple_list_item_1, nList)
            binding.listNotification.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}