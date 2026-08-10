package com.example.lint

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
import com.android.tools.lint.detector.api.CURRENT_API

class IMDBIssueRegistry : IssueRegistry() {
    override val issues = listOf(
        ComposableModifierDetector.MISSING_MODIFIER_ISSUE,
        ComposableModifierDetector.MODIFIER_POSITION_ISSUE,
    )
    override val api: Int = CURRENT_API
    override val minApi: Int = 10
    override val vendor = Vendor(
        vendorName = "IMDB APP",
        feedbackUrl = "https://github.com/Mahnoosh92/IMDBApp/issues",
    )
}
