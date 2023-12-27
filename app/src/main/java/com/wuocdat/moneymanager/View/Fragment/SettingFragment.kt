package com.wuocdat.moneymanager.View.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wuocdat.moneymanager.Utils.MNConstants
import com.wuocdat.moneymanager.Utils.StringUtils
import com.wuocdat.moneymanager.View.Activity.GoalActivity
import com.wuocdat.roomdatabase.R
import com.wuocdat.roomdatabase.databinding.FragmentSettingBinding

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
        // get goal value
        val sharedPreferences =
            requireActivity().getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
            )
        val savedGoalValue = sharedPreferences.getLong(
            resources.getString(R.string.goal_share_preference_key),
            MNConstants.DEFAULT_GOAL_AMOUNT
        )
        binding.settingFragmentGoalTv.text =
            StringUtils.convertToCurrencyFormat(savedGoalValue)

        // navigate to set goal activity
        binding.settingFragmentGoalLayout.setOnClickListener {
            val intent = Intent(requireContext(), GoalActivity::class.java)
            intent.putExtra(MNConstants.WITH_CANCEL_BTN_GOAL_ACTIVITY_KEY, true)
            startActivity(intent)
            requireActivity().finish()
        }

        // open about dialog
        binding.settingFragmentAboutLayout.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder
                .setMessage(getString(R.string.about_mess))
                .setTitle(getString(R.string.about))

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}