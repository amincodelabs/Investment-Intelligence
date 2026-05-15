package amin.codelabs.realestateinvestmentintelligence.designsystem.components

import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appColors
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appShapes
import amin.codelabs.realestateinvestmentintelligence.designsystem.theme.appTypography
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it) } },
            supportingText = supportingText?.let {
                {
                    Text(
                        text = it,
                        style = MaterialTheme.appTypography.caption,
                    )
                }
            },
            isError = isError,
            enabled = enabled,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            shape = MaterialTheme.appShapes.textField,
            textStyle = MaterialTheme.appTypography.body,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.appColors.primary,
                unfocusedBorderColor = MaterialTheme.appColors.outline,
                errorBorderColor = MaterialTheme.appColors.error,
                focusedLabelColor = MaterialTheme.appColors.primary,
                unfocusedLabelColor = MaterialTheme.appColors.mutedText,
                errorLabelColor = MaterialTheme.appColors.error,
                focusedTextColor = MaterialTheme.appColors.onSurface,
                unfocusedTextColor = MaterialTheme.appColors.onSurface,
                disabledTextColor = MaterialTheme.appColors.mutedText,
                focusedContainerColor = MaterialTheme.appColors.surface,
                unfocusedContainerColor = MaterialTheme.appColors.surface,
                disabledContainerColor = MaterialTheme.appColors.surfaceVariant,
                errorContainerColor = MaterialTheme.appColors.surface,
            ),
        )
    }
}

@Composable
fun AppPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = { Text(label) },
        supportingText = supportingText?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.appTypography.caption,
                )
            }
        },
        isError = isError,
        enabled = enabled,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            TextButton(onClick = { passwordVisible = !passwordVisible }) {
                Text(
                    text = if (passwordVisible) "Hide" else "Show",
                    style = MaterialTheme.appTypography.label,
                )
            }
        },
        shape = MaterialTheme.appShapes.textField,
        textStyle = MaterialTheme.appTypography.body,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.appColors.primary,
            unfocusedBorderColor = MaterialTheme.appColors.outline,
            errorBorderColor = MaterialTheme.appColors.error,
            focusedLabelColor = MaterialTheme.appColors.primary,
            unfocusedLabelColor = MaterialTheme.appColors.mutedText,
            errorLabelColor = MaterialTheme.appColors.error,
            focusedTextColor = MaterialTheme.appColors.onSurface,
            unfocusedTextColor = MaterialTheme.appColors.onSurface,
            disabledTextColor = MaterialTheme.appColors.mutedText,
            focusedContainerColor = MaterialTheme.appColors.surface,
            unfocusedContainerColor = MaterialTheme.appColors.surface,
            disabledContainerColor = MaterialTheme.appColors.surfaceVariant,
            errorContainerColor = MaterialTheme.appColors.surface,
        ),
    )
}
