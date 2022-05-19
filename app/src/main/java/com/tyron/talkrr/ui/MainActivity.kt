package com.tyron.talkrr.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tyron.talkrr.ui.theme.TalkrrTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TalkrrTheme {
//                // System bars color
//                val systemUiController = rememberSystemUiController()
//                val useDarkIcons = MaterialTheme.colors.isLight
//                SideEffect {
//                    systemUiController.setSystemBarsColor(
//                        color = Color.Transparent,
//                        darkIcons = useDarkIcons
//                    )
//                }

                TalkrrApp()
            }
        }
    }
}

@Preview
@Composable
fun MainPreview() {
    TalkrrApp()
}