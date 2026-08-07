package com.example.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import org.jetbrains.uast.UElement
import org.jetbrains.uast.UMethod

class ComposableModifierDetector : Detector(), Detector.UastScanner {
    override fun getApplicableUastTypes(): List<Class<out UElement>> = listOf(
        UMethod::class.java,
    )

    override fun createUastHandler(context: JavaContext): UElementHandler =
        object : UElementHandler() {
            override fun visitMethod(node: UMethod) {
                // Check if function is a Composable
                val isComposable = node.annotations.any { annotation ->
                    annotation.qualifiedName == "androidx.compose.runtime.Composable"
                }
                if (!isComposable) return

                // Skip private or preview composables
                if (node.isConstructor || node.annotations.any { it.qualifiedName?.contains("Preview") == true }) {
                    return
                }

                val parameters = node.uastParameters
                val modifierParamIndex = parameters.indexOfFirst { param ->
                    param.type.canonicalText == "androidx.compose.ui.Modifier"
                }

                // Rule 1: Modifier parameter must exist
                if (modifierParamIndex == -1) {
                    context.report(
                        issue = MISSING_MODIFIER_ISSUE,
                        location = context.getNameLocation(node),
                        message = "Composable function `${node.name}` should accept a `modifier: Modifier = Modifier` parameter.",
                    )
                    return
                }

                // Find index of the first parameter that has a default value (first optional parameter)
                val firstOptionalParamIndex = parameters.indexOfFirst { param ->
                    param.uastInitializer != null
                }

                // Rule 2: Modifier should be the first OPTIONAL parameter
                // (If no default values exist elsewhere, or if modifier appears after another optional param)
                if (firstOptionalParamIndex != -1 && modifierParamIndex > firstOptionalParamIndex) {
                    val modifierParam = parameters[modifierParamIndex] as UElement

                    context.report(
                        issue = MODIFIER_POSITION_ISSUE,
                        location = context.getLocation(modifierParam), // Disambiguated UElement call
                        message = "The `modifier` parameter in `${node.name}` should be the first optional parameter.",
                    )
                }
            }
        }
    companion object {
        @JvmField
        val MISSING_MODIFIER_ISSUE: Issue = Issue.create(
            id = "MissingComposableModifier",
            briefDescription = "Missing Modifier parameter in Composable",
            explanation = "Reusable Composable functions should accept a `modifier: Modifier = Modifier` parameter to allow call-site UI adjustments.",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposableModifierDetector::class.java,
                Scope.JAVA_FILE_SCOPE,
            ),
        )

        @JvmField
        val MODIFIER_POSITION_ISSUE: Issue = Issue.create(
            id = "ModifierPosition",
            briefDescription = "Modifier parameter is not the first parameter",
            explanation = "To maintain consistency across Compose APIs, `modifier` should be the first parameter of a Composable function.",
            category = Category.CORRECTNESS,
            priority = 5,
            severity = Severity.WARNING,
            implementation = Implementation(
                ComposableModifierDetector::class.java,
                Scope.JAVA_FILE_SCOPE,
            ),
        )
    }
}