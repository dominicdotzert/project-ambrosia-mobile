package com.projectambrosia.ambrosia.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.projectambrosia.ambrosia.databinding.PageUserAgeBinding
import com.projectambrosia.ambrosia.databinding.PageUserGoalBinding
import com.projectambrosia.ambrosia.databinding.PageUserMotivationBinding
import com.projectambrosia.ambrosia.databinding.PageUserNameBinding
import com.projectambrosia.ambrosia.login.viewmodels.CollectUserInfoViewModel
import com.projectambrosia.ambrosia.utilities.*

class CollectUserInfoViewPagerAdapter(
    private val collectUserInfoViewModel: CollectUserInfoViewModel
) : RecyclerView.Adapter<CollectUserViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectUserViewHolder {
        return when (viewType) {
            USER_NAME_PAGE -> UserNameViewHolder.from(parent)
            USER_AGE_PAGE -> UserAgeViewHolder.from(parent)
            USER_GOAL_PAGE -> UserGoalViewHolder.from(parent)
            USER_MOTIVATION_PAGE -> UserMotivationViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemCount(): Int {
        return LOGIN_USER_INFORMATION_PAGES
    }

    override fun onBindViewHolder(holder: CollectUserViewHolder, position: Int) {
        holder.bind(collectUserInfoViewModel)
    }

    class UserNameViewHolder private constructor (val binding: PageUserNameBinding) : CollectUserViewHolder(binding.root) {
        override fun bind(viewModel: CollectUserInfoViewModel) {
            binding.viewModel = viewModel
        }

        companion object {
            fun from(parent: ViewGroup) : UserNameViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PageUserNameBinding.inflate(inflater, parent, false)
                return UserNameViewHolder(binding)
            }
        }
    }

    class UserAgeViewHolder private constructor (val binding: PageUserAgeBinding) : CollectUserViewHolder(binding.root) {
        override fun bind(viewModel: CollectUserInfoViewModel) {
            binding.viewModel = viewModel
        }

        companion object {
            fun from(parent: ViewGroup) : UserAgeViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PageUserAgeBinding.inflate(inflater, parent, false)
                return UserAgeViewHolder(binding)
            }
        }
    }

    class UserGoalViewHolder private constructor (val binding: PageUserGoalBinding) : CollectUserViewHolder(binding.root) {
        override fun bind(viewModel: CollectUserInfoViewModel) {
            binding.viewModel = viewModel
        }

        companion object {
            fun from(parent: ViewGroup) : UserGoalViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PageUserGoalBinding.inflate(inflater, parent, false)
                return UserGoalViewHolder(binding)
            }
        }
    }

    class UserMotivationViewHolder private constructor (val binding: PageUserMotivationBinding) : CollectUserViewHolder(binding.root) {
        override fun bind(viewModel: CollectUserInfoViewModel) {
            binding.viewModel = viewModel
        }

        companion object {
            fun from(parent: ViewGroup) : UserMotivationViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = PageUserMotivationBinding.inflate(inflater, parent, false)
                return UserMotivationViewHolder(binding)
            }
        }
    }
}

abstract class CollectUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bind(viewModel: CollectUserInfoViewModel)
}