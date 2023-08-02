package com.videoyou.x.ui.guide.permission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.navOptions
import com.videoyou.x.R
import com.videoyou.x._utils.BaseFragment
import com.videoyou.x.databinding.FragmentGuidePermissionBinding

class GuidePermissionFragment :
    BaseFragment<GuidePermissionViewModel, FragmentGuidePermissionBinding>() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onViewCreate() {
        mViewModel.test = "1"
        val controller = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        binding.guidePermissionBack.setOnClickListener {
            controller.navigate(R.id.guideWelcomeFragment, bundleOf(), navOptions {
                anim {
                    enter = R.anim.guide_back_in
                    exit = R.anim.guide_back_out
                }
            })
        }
        binding.guidePermissionList.setContent {
            Column(modifier = Modifier.fillMaxWidth()) {
                Card(
                    modifier = Modifier.defaultMinSize(minHeight = 75.dp),
                    onClick = { },
                    shape = RoundedCornerShape(
                        topStart = 25.dp,
                        topEnd = 25.dp,
                        bottomStart = 5.dp,
                        bottomEnd = 5.dp
                    ),
                    colors = CardDefaults.cardColors(Color.White)
                ) {

                }
            }
        }
    }

    override fun getViewBinding(): FragmentGuidePermissionBinding {
        return FragmentGuidePermissionBinding.inflate(layoutInflater)
    }
}
