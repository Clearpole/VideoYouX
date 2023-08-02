package com.videoyou.x.ui.guide.permission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            val infoList = arrayListOf(
                requireContext().getString(R.string.read_video_permission),
                "画中画权限",
                "其他权限"
            )
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(infoList) { index, item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth().padding(top = 1.5.dp, bottom = 1.5.dp),
                        onClick = { },
                        shape = when (index) {
                            0 -> {
                                RoundedCornerShape(
                                    topStart = 25.dp,
                                    topEnd = 25.dp,
                                    bottomStart = 3.dp,
                                    bottomEnd = 3.dp
                                )
                            }
                            infoList.size - 1 -> {
                                RoundedCornerShape(
                                    topStart = 3.dp,
                                    topEnd = 3.dp,
                                    bottomStart = 25.dp,
                                    bottomEnd = 25.dp
                                )
                            }
                            else -> {
                                RoundedCornerShape(3.dp)
                            }
                        },
                        colors = CardDefaults.cardColors(Color.White)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Rounded.CheckCircle,
                                contentDescription = null,
                                tint = colorResource(
                                    id = R.color.safe_green
                                ),
                                modifier = Modifier.padding(start = 20.dp)
                            )
                            Column(
                                modifier = Modifier
                                    .defaultMinSize(minHeight = 85.dp)
                                    .padding(start = 15.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = item, fontSize = 18.sp)
                                Text(text = "我是详细介绍", modifier = Modifier.padding(top = 3.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getViewBinding(): FragmentGuidePermissionBinding {
        return FragmentGuidePermissionBinding.inflate(layoutInflater)
    }
}
