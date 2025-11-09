package com.example.project.ui.activity

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.project.R
import com.example.project.ui.theme.ProjectTheme
import com.example.project.ui.theme.SurfaceWhite
import com.example.project.ui.theme.SwitchThumbDisabled
import com.example.project.ui.theme.SwitchThumbEnabled
import com.example.project.ui.theme.SwitchTrackDisabled
import com.example.project.ui.theme.SwitchTrackEnabled
import com.example.project.ui.theme.TextPrimary
import com.example.project.ui.theme.TextSecondary


@Composable
fun SettingsScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val darkThemeEnabled = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceWhite)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(SurfaceWhite),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.cd_back),
                        tint = TextPrimary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = stringResource(R.string.settings_title),
                    color = TextPrimary ,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(SurfaceWhite)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            SettingsItem(
                text = stringResource(R.string.dark_theme),
                showCustomSwitch = true,
                switchState = darkThemeEnabled.value,
                onSwitchChange = { }
            )

            SettingsItem(
                text = stringResource(R.string.share_app),
                showIcon = true,
                iconResId = R.drawable.ic_share,
                onClick = {
                    shareApp(context)
                }
            )

            SettingsItem(
                text = stringResource(R.string.write_to_developers),
                showIcon = true,
                iconResId = R.drawable.ic_support,
                onClick = {
                    writeToDevelopers(context)
                }
            )

            SettingsItem(
                text = stringResource(R.string.user_agreement),
                showArrow = true,
                onClick = {
                    openUserAgreement(context)
                }
            )
        }
    }
}

private fun shareApp(context: Context) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_app_text))
    }
    val chooserIntent = Intent.createChooser(shareIntent, context.getString(R.string.share_app))
    context.startActivity(chooserIntent)
}

private fun writeToDevelopers(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf(context.getString(R.string.developer_email)))
        putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject))
        putExtra(Intent.EXTRA_TEXT, context.getString(R.string.email_body))
    }

    try {
        context.startActivity(emailIntent)
    } catch (_: Exception) {
        Toast.makeText(
            context,
            R.string.no_email_app,
            Toast.LENGTH_SHORT
        ).show()
    }
}

private fun openUserAgreement(context: Context) {
    val agreementUri = context.getString(R.string.offer_link).toUri()
    val browserIntent = Intent(Intent.ACTION_VIEW, agreementUri)

    try {
        context.startActivity(browserIntent)
    } catch (_: Exception) {
        Toast.makeText(
            context,
            R.string.no_browser_app,
            Toast.LENGTH_SHORT
        ).show()
    }
}


@Composable
fun SettingsItem(
    text: String,
    showCustomSwitch: Boolean = false,
    showIcon: Boolean = false,
    showArrow: Boolean = false,
    iconResId: Int = 0,
    switchState: Boolean = false,
    onSwitchChange: ((Boolean) -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .background(SurfaceWhite)
            .clickable { onClick?.invoke() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                color = TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            when {
                showCustomSwitch -> {
                    CustomThumbSwitch(
                        checked = switchState,
                        onCheckedChange = onSwitchChange ?: {}
                    )
                }
                showIcon -> {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = TextSecondary
                    )
                }
                showArrow -> {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = stringResource(R.string.cd_arrow),
                        modifier = Modifier.size(24.dp),
                        tint = TextSecondary
                    )
                }
            }
        }
    }
}


@Composable
fun CustomThumbSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val thumbPosition by animateDpAsState(
        targetValue = if (checked) 21.dp else 0.dp,
        animationSpec = tween(durationMillis = 200)
    )

    val trackColor by animateColorAsState(
        targetValue = if (checked) SwitchTrackEnabled  else SwitchTrackDisabled,
        animationSpec = tween(durationMillis = 200)
    )

    val thumbColor by animateColorAsState(
        targetValue = if (checked) SwitchThumbEnabled else SwitchThumbDisabled,
        animationSpec = tween(durationMillis = 200)
    )

    Box(
        modifier = modifier
            .width(35.dp)
            .height(24.dp)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .width(35.dp)
                .height(14.dp)
                .background(trackColor, RoundedCornerShape(7.dp))
        )

        Box(
            modifier = Modifier
                .size(20.dp)
                .offset(x = thumbPosition)
                .background(thumbColor, CircleShape)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SettingsScreenPreview() {
    ProjectTheme {
        SettingsScreen(
            onBackClick = {}
        )
    }
}