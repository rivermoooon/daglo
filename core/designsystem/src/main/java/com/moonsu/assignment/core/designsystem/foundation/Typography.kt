package com.moonsu.assignment.core.designsystem.foundation

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.moonsu.assignment.core.designsystem.R

val Pretendard = FontFamily(
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),
    Font(R.font.pretendard_black, FontWeight.Black),
)

private val Base = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.Normal,
)

internal val Typography = DagloTypography(
    displayLargeR = Base.copy(
        fontSize = 45.sp,
        lineHeight = 54.sp, // 120%~135%
        letterSpacing = (-0.9).sp,
    ),
    displayMediumR = Base.copy(
        fontSize = 36.sp,
        lineHeight = 46.8.sp, // 130%
        letterSpacing = (-0.72).sp, // -0.02em
    ),
    displaySmallR = Base.copy(
        fontSize = 32.sp,
        lineHeight = 41.6.sp, // 130%
        letterSpacing = (-0.64).sp,
    ),

    // Headline (32/28/24)
    headlineLargeEB = Base.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.ExtraBold,
        letterSpacing = (-0.64).sp,
    ),
    headlineLargeSB = Base.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = (-0.64).sp,
    ),
    headlineLargeR = Base.copy(
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.64).sp,
    ),
    headlineMediumB = Base.copy(
        fontSize = 28.sp,
        lineHeight = 36.4.sp, // 130%
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.56).sp,
    ),
    headlineMediumM = Base.copy(
        fontSize = 28.sp,
        lineHeight = 36.4.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = (-0.56).sp,
    ),
    headlineMediumR = Base.copy(
        fontSize = 28.sp,
        lineHeight = 36.4.sp,
        letterSpacing = (-0.56).sp,
    ),
    headlineSmallBL = Base.copy(
        fontSize = 24.sp,
        lineHeight = 32.4.sp, // 135%
        fontWeight = FontWeight.Black,
        letterSpacing = (-0.48).sp,
    ),
    headlineSmallM = Base.copy(
        fontSize = 24.sp,
        lineHeight = 32.4.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = (-0.48).sp,
    ),
    headlineSmallR = Base.copy(
        fontSize = 24.sp,
        lineHeight = 32.4.sp,
        letterSpacing = (-0.48).sp,
    ),

    // Title (22 / 16 / 14)
    titleLargeBL = Base.copy(
        fontSize = 22.sp,
        lineHeight = 29.7.sp, // 135%
        fontWeight = FontWeight.Black,
        letterSpacing = (-0.44).sp,
    ),
    titleLargeB = Base.copy(
        fontSize = 22.sp,
        lineHeight = 29.7.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.44).sp,
    ),
    titleLargeM = Base.copy(
        fontSize = 22.sp,
        lineHeight = 29.7.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = (-0.44).sp,
    ),
    titleLargeR = Base.copy(
        fontSize = 22.sp,
        lineHeight = 29.7.sp,
        letterSpacing = (-0.44).sp,
    ),

    titleMediumBL = Base.copy(
        fontSize = 16.sp,
        lineHeight = 21.6.sp, // 135%
        fontWeight = FontWeight.Black,
        letterSpacing = (-0.32).sp, // -0.02em
    ),
    titleMediumB = Base.copy(
        fontSize = 16.sp,
        lineHeight = 21.6.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.32).sp,
    ),
    titleMediumR = Base.copy(
        fontSize = 16.sp,
        lineHeight = 21.6.sp,
        letterSpacing = (-0.32).sp,
    ),

    titleSmallB = Base.copy(
        fontSize = 14.sp,
        lineHeight = 18.9.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = (-0.28).sp,
    ),
    titleSmallM = Base.copy(
        fontSize = 14.sp,
        lineHeight = 18.9.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = (-0.28).sp,
    ),
    titleSmallM140 = Base.copy(
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = (-0.28).sp,
    ),
    titleSmallR = Base.copy(
        fontSize = 14.sp,
        lineHeight = 18.9.sp,
        letterSpacing = (-0.28).sp,
    ),
    titleSmallR140 = Base.copy(
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        letterSpacing = (-0.28).sp,
    ),

    // Label / Body
    labelLargeM = Base.copy(
        fontSize = 12.sp,
        lineHeight = 16.2.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = (-0.24).sp,
    ),
    labelMediumR = Base.copy(
        fontSize = 12.sp,
        lineHeight = 16.2.sp,
        letterSpacing = (-0.24).sp,
    ),
    labelSmallM = Base.copy(
        fontSize = 11.sp,
        lineHeight = 14.85.sp, // 135%
        fontWeight = FontWeight.Medium,
        letterSpacing = (-0.22).sp,
    ),

    bodyLargeR = Base.copy(
        fontSize = 16.sp,
        lineHeight = 22.4.sp,
        letterSpacing = (-0.32).sp,
    ),
    bodyMediumR = Base.copy(
        fontSize = 14.sp,
        lineHeight = 19.6.sp, // 140%
        letterSpacing = (-0.28).sp,
    ),
    bodySmallR = Base.copy(
        fontSize = 12.sp,
        lineHeight = 16.8.sp, // 140%
        letterSpacing = (-0.24).sp,
    ),
)

@Immutable
data class DagloTypography(
    val displayLargeR: TextStyle,
    val displayMediumR: TextStyle,
    val displaySmallR: TextStyle,

    val headlineLargeEB: TextStyle,
    val headlineLargeSB: TextStyle,
    val headlineLargeR: TextStyle,
    val headlineMediumB: TextStyle,
    val headlineMediumM: TextStyle,
    val headlineMediumR: TextStyle,
    val headlineSmallBL: TextStyle,
    val headlineSmallM: TextStyle,
    val headlineSmallR: TextStyle,

    val titleLargeBL: TextStyle,
    val titleLargeB: TextStyle,
    val titleLargeM: TextStyle,
    val titleLargeR: TextStyle,
    val titleMediumBL: TextStyle,
    val titleMediumB: TextStyle,
    val titleMediumR: TextStyle,
    val titleSmallB: TextStyle,
    val titleSmallM: TextStyle,
    val titleSmallM140: TextStyle,
    val titleSmallR: TextStyle,
    val titleSmallR140: TextStyle,

    val labelLargeM: TextStyle,
    val labelMediumR: TextStyle,
    val labelSmallM: TextStyle,

    val bodyLargeR: TextStyle,
    val bodyMediumR: TextStyle,
    val bodySmallR: TextStyle,
)
