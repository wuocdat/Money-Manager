package com.wuocdat.moneymanager.View.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wuocdat.moneymanager.Store.GoalStore
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.View.Activity.GoalActivity
import com.wuocdat.roomdatabase.databinding.FragmentSettingBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        CoroutineScope(Dispatchers.IO).launch {
            val goalValue = GoalStore.read(requireContext())
            withContext(Dispatchers.Main) {
                binding.settingFragmentGoalTv.text =
                    StringUtils.convertToCurrencyFormat(goalValue.toLong())
            }
        }
        binding.settingFragmentGoalLayout.setOnClickListener {
            val intent = Intent(requireContext(), GoalActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}