package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AuthMode
import com.example.ui.theme.AiGlowGradient
import com.example.ui.theme.BrandGradient
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonViolet
import com.example.ui.theme.RadiantPink
import com.example.ui.viewmodel.SocialViewModel

@Composable
fun AuthScreen(
    viewModel: SocialViewModel,
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("auth_screen")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item { Spacer(modifier = Modifier.height(20.dp)) }

            // Logo & Title
            item {
                Box(
                    modifier = Modifier.size(76.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_icon),
                        contentDescription = "Aether Logo",
                        modifier = Modifier
                            .size(72.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(2.5.dp, NeonViolet, RoundedCornerShape(18.dp))
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "AETHER SOCIAL",
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = 1.5.sp
                )

                Text(
                    text = "Next-Gen AI Social Network & Live Studio",
                    fontSize = 12.sp,
                    color = NeonCyan,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(20.dp))
            }

            // Error / Status Message Banners
            item {
                authState.errorMessage?.let { error ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = RadiantPink.copy(alpha = 0.15f)),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = error,
                            color = RadiantPink,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                authState.statusMessage?.let { status ->
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MintEmerald.copy(alpha = 0.15f)),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = status,
                            color = MintEmerald,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            // Mode Selector (Login vs Sign Up)
            if (authState.currentAuthMode == AuthMode.LOGIN || authState.currentAuthMode == AuthMode.SIGNUP) {
                item {
                    TabRow(
                        selectedTabIndex = if (authState.currentAuthMode == AuthMode.LOGIN) 0 else 1,
                        containerColor = MaterialTheme.colorScheme.surface,
                        indicator = { tabPositions ->
                            val index = if (authState.currentAuthMode == AuthMode.LOGIN) 0 else 1
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[index]),
                                color = NeonViolet
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Tab(
                            selected = authState.currentAuthMode == AuthMode.LOGIN,
                            onClick = { viewModel.setAuthMode(AuthMode.LOGIN) },
                            text = { Text("Log In", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("tab_login")
                        )
                        Tab(
                            selected = authState.currentAuthMode == AuthMode.SIGNUP,
                            onClick = { viewModel.setAuthMode(AuthMode.SIGNUP) },
                            text = { Text("Create Account", fontWeight = FontWeight.Bold) },
                            modifier = Modifier.testTag("tab_signup")
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                }
            }

            // Auth Forms Content
            item {
                Crossfade(targetState = authState.currentAuthMode, label = "AuthModeCrossfade") { mode ->
                    when (mode) {
                        AuthMode.LOGIN -> {
                            LoginForm(
                                initialEmailOrUsername = authState.currentEmailOrUsername,
                                rememberMeInitial = authState.rememberMe,
                                onLogin = { email, pass, remember ->
                                    val ok = viewModel.loginUser(email, pass, remember)
                                    if (ok) onAuthSuccess()
                                },
                                onForgotPassword = { viewModel.setAuthMode(AuthMode.FORGOT_PASSWORD) },
                                onDemoFastLogin = {
                                    viewModel.loginUser("vasudev7490@gmail.com", "password123", true)
                                    onAuthSuccess()
                                }
                            )
                        }
                        AuthMode.SIGNUP -> {
                            SignupForm(
                                onSignup = { name, username, email, pass ->
                                    val ok = viewModel.signupUser(name, username, email, pass)
                                    if (ok) onAuthSuccess()
                                },
                                onSwitchToLogin = { viewModel.setAuthMode(AuthMode.LOGIN) }
                            )
                        }
                        AuthMode.FORGOT_PASSWORD -> {
                            ForgotPasswordForm(
                                onRequestOtp = { emailOrPhone ->
                                    viewModel.requestPasswordResetOtp(emailOrPhone)
                                },
                                onBackToLogin = { viewModel.setAuthMode(AuthMode.LOGIN) }
                            )
                        }
                        AuthMode.VERIFY_OTP -> {
                            VerifyOtpForm(
                                target = authState.resetEmailTarget,
                                timeRemainingSeconds = authState.otpTimeRemainingSeconds,
                                onVerify = { otp ->
                                    viewModel.verifyOtp(otp)
                                },
                                onResend = {
                                    viewModel.requestPasswordResetOtp(authState.resetEmailTarget)
                                },
                                onBackToLogin = { viewModel.setAuthMode(AuthMode.LOGIN) }
                            )
                        }
                        AuthMode.RESET_NEW_PASSWORD -> {
                            ResetNewPasswordForm(
                                onReset = { newPass, confirmPass ->
                                    viewModel.resetNewPassword(newPass, confirmPass)
                                },
                                onBackToLogin = { viewModel.setAuthMode(AuthMode.LOGIN) }
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// ==========================================
// 1. LOGIN FORM
// ==========================================

@Composable
private fun LoginForm(
    initialEmailOrUsername: String,
    rememberMeInitial: Boolean,
    onLogin: (email: String, pass: String, remember: Boolean) -> Unit,
    onForgotPassword: () -> Unit,
    onDemoFastLogin: () -> Unit
) {
    var emailOrUsername by remember { mutableStateOf(initialEmailOrUsername) }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(rememberMeInitial) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        OutlinedTextField(
            value = emailOrUsername,
            onValueChange = { emailOrUsername = it },
            label = { Text("Email or @Username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NeonViolet) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("login_email_input"),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NeonCyan) },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }, modifier = Modifier.testTag("toggle_password_visibility")) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Show/Hide Password",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("login_password_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it },
                    colors = CheckboxDefaults.colors(checkedColor = NeonViolet),
                    modifier = Modifier.testTag("remember_me_checkbox")
                )
                Text("Remember me", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
            }

            TextButton(onClick = onForgotPassword, modifier = Modifier.testTag("forgot_password_button")) {
                Text("Forgot Password?", fontSize = 13.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
            }
        }

        Button(
            onClick = { onLogin(emailOrUsername, password, rememberMe) },
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("login_submit_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
        ) {
            Text("Log In", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Text("  OR QUICK ACCESS  ", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold)
            HorizontalDivider(modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Demo 1-Tap Biometric Login
        OutlinedButton(
            onClick = onDemoFastLogin,
            modifier = Modifier.fillMaxWidth().height(46.dp).testTag("demo_fast_login_button"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = MintEmerald)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Instant Sign-In with Biometrics / Demo", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}

// ==========================================
// 2. SIGNUP / CREATE ACCOUNT FORM
// ==========================================

@Composable
private fun SignupForm(
    onSignup: (name: String, username: String, email: String, pass: String) -> Unit,
    onSwitchToLogin: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var agreedToTerms by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NeonViolet) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("signup_name_input"),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Desired Username") },
            prefix = { Text("@", color = NeonCyan) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("signup_username_input"),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NeonCyan) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("signup_email_input"),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password (min 8 chars)") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = RadiantPink) },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("signup_password_input"),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("signup_confirm_password_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = agreedToTerms,
                onCheckedChange = { agreedToTerms = it },
                colors = CheckboxDefaults.colors(checkedColor = NeonViolet)
            )
            Text(
                "I agree to the Terms of Service & Privacy Policy",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Button(
            onClick = {
                if (password == confirmPassword && agreedToTerms) {
                    onSignup(fullName, username, email, password)
                }
            },
            enabled = agreedToTerms && password.isNotBlank() && password == confirmPassword,
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("signup_submit_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
        ) {
            Text("Create Aether Account", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
        }

        TextButton(
            onClick = onSwitchToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? Log In", color = NeonCyan, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

// ==========================================
// 3. FORGOT PASSWORD FORM
// ==========================================

@Composable
private fun ForgotPasswordForm(
    onRequestOtp: (emailOrPhone: String) -> Unit,
    onBackToLogin: () -> Unit
) {
    var emailOrPhone by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "Reset Your Password",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            "Enter your registered email address or phone number. We'll send you a 6-digit verification OTP to securely reset your password.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = emailOrPhone,
            onValueChange = { emailOrPhone = it },
            label = { Text("Email or Phone Number") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NeonViolet) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("forgot_pass_email_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = { onRequestOtp(emailOrPhone) },
            enabled = emailOrPhone.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("send_otp_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
        ) {
            Icon(Icons.Default.Security, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Send Verification Code (OTP)", fontWeight = FontWeight.Bold, color = Color.White)
        }

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Back to Log In", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        }
    }
}

// ==========================================
// 4. VERIFY OTP FORM
// ==========================================

@Composable
private fun VerifyOtpForm(
    target: String,
    timeRemainingSeconds: Int,
    onVerify: (otp: String) -> Unit,
    onResend: () -> Unit,
    onBackToLogin: () -> Unit
) {
    var otpInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "Enter 6-Digit OTP",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            "Verification code sent to $target. Enter the code below to proceed.",
            textAlign = TextAlign.Center,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = otpInput,
            onValueChange = { if (it.length <= 6) otpInput = it },
            label = { Text("6-Digit Code") },
            placeholder = { Text("849201") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("otp_code_input"),
            shape = RoundedCornerShape(12.dp)
        )

        if (timeRemainingSeconds > 0) {
            Text("Code expires in: ${timeRemainingSeconds}s", fontSize = 12.sp, color = NeonCyan, fontWeight = FontWeight.Bold)
        } else {
            TextButton(onClick = onResend) {
                Text("Didn't receive code? Resend OTP", color = RadiantPink, fontWeight = FontWeight.Bold)
            }
        }

        Button(
            onClick = { onVerify(otpInput) },
            enabled = otpInput.length == 6,
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("verify_otp_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MintEmerald)
        ) {
            Text("Verify & Continue", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
        }

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel & Return to Login", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        }
    }
}

// ==========================================
// 5. RESET NEW PASSWORD FORM
// ==========================================

@Composable
private fun ResetNewPasswordForm(
    onReset: (newPass: String, confirmPass: String) -> Unit,
    onBackToLogin: () -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPassVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            "Create New Password",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            "Your identity has been verified! Create a strong, new password with at least 8 characters.",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password (min 8 characters)") },
            visualTransformation = if (isPassVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPassVisible = !isPassVisible }) {
                    Icon(if (isPassVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null)
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("reset_new_password_input"),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm New Password") },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("reset_confirm_password_input"),
            shape = RoundedCornerShape(12.dp)
        )

        Button(
            onClick = { onReset(newPassword, confirmPassword) },
            enabled = newPassword.length >= 8 && newPassword == confirmPassword,
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("submit_reset_password_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonViolet)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save New Password & Log In", fontWeight = FontWeight.Bold, color = Color.White)
        }

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("← Back to Log In", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp)
        }
    }
}
