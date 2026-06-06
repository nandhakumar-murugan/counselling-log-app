package com.example

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MyApplicationTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            LocalFileManager.bootstrap(this)
        } catch (e: Exception) {
            Log.e("MainActivity", "Error bootstrapping LocalFileManager on create", e)
        }

        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen()
            }
        }
    }
}

// Data state holding 27 headers information including College Name, Semester, Hostel, and Student Email
class CounselingFormState(lastSNo: Int) {
    var sNo by mutableStateOf((lastSNo + 1).toString())
    var date by mutableStateOf(getTodayString())
    var caseID by mutableStateOf("")
    var name by mutableStateOf("")
    var age by mutableStateOf("")
    var gender by mutableStateOf("Male")
    var department by mutableStateOf("")
    var year by mutableStateOf("1st Year")
    var contactNo by mutableStateOf("")
    var referralSource by mutableStateOf("Self")
    var presentingProblem by mutableStateOf("")
    var categoryOfIssue by mutableStateOf("Personal Issues")
    var severityLevel by mutableFloatStateOf(1f) // 1 to 5
    var visitType by mutableStateOf("First Visit") // Radio buttons: First Visit vs Follow-up
    var typeOfCounselling by mutableStateOf("In-person")
    var sessionsConducted by mutableIntStateOf(1)
    var interventionUsed by mutableStateOf("")
    var referredToSpecialist by mutableStateOf(false)
    var specialistDetails by mutableStateOf("")
    var assessment by mutableStateOf("")
    var followUpDate by mutableStateOf(getTodayString())
    var progressStatus by mutableStateOf("Improving")
    var outcome by mutableStateOf("")
    var nextPlan by mutableStateOf("")
    var collegeName by mutableStateOf("")
    var semester by mutableStateOf("")
    var hostel by mutableStateOf("")
    var studentEmail by mutableStateOf("")

    fun reset(lastSNo: Int) {
        sNo = (lastSNo + 1).toString()
        date = getTodayString()
        caseID = ""
        name = ""
        age = ""
        gender = "Male"
        department = ""
        year = "1st Year"
        contactNo = ""
        referralSource = "Self"
        presentingProblem = ""
        categoryOfIssue = "Personal Issues"
        severityLevel = 1f
        visitType = "First Visit"
        typeOfCounselling = "In-person"
        sessionsConducted = 1
        interventionUsed = ""
        referredToSpecialist = false
        specialistDetails = ""
        assessment = ""
        followUpDate = getTodayString()
        progressStatus = "Improving"
        outcome = ""
        nextPlan = ""
        collegeName = ""
        semester = ""
        hostel = ""
        studentEmail = ""
    }

    fun fromRowList(row: List<String>) {
        sNo = row.getOrNull(0) ?: ""
        date = row.getOrNull(1) ?: getTodayString()
        caseID = row.getOrNull(2) ?: ""
        name = row.getOrNull(3) ?: ""
        age = row.getOrNull(4) ?: ""
        gender = row.getOrNull(5) ?: "Male"
        department = row.getOrNull(6) ?: ""
        year = row.getOrNull(7) ?: "1st Year"
        contactNo = row.getOrNull(8) ?: ""
        referralSource = row.getOrNull(9) ?: "Self"
        presentingProblem = row.getOrNull(10) ?: ""
        categoryOfIssue = row.getOrNull(11) ?: "Personal Issues"
        val rawSev = row.getOrNull(12)?.toFloatOrNull() ?: 1f
        severityLevel = if (rawSev > 3f) 3f else if (rawSev < 1f) 1f else rawSev
        visitType = row.getOrNull(13) ?: "First Visit"
        typeOfCounselling = row.getOrNull(14) ?: "In-person"
        sessionsConducted = row.getOrNull(15)?.toIntOrNull() ?: 1
        interventionUsed = row.getOrNull(16) ?: ""
        val refCol = row.getOrNull(17) ?: ""
        if (refCol.trim().lowercase().startsWith("yes")) {
            referredToSpecialist = true
            specialistDetails = if (refCol.contains(" - ")) refCol.substringAfter(" - ") else ""
        } else {
            referredToSpecialist = false
            specialistDetails = ""
        }
        assessment = row.getOrNull(18) ?: ""
        followUpDate = row.getOrNull(19) ?: getTodayString()
        progressStatus = row.getOrNull(20) ?: "Improving"
        outcome = row.getOrNull(21) ?: ""
        nextPlan = row.getOrNull(22) ?: ""
        collegeName = row.getOrNull(23) ?: ""
        semester = row.getOrNull(24) ?: ""
        hostel = row.getOrNull(25) ?: ""
        studentEmail = row.getOrNull(26) ?: ""
    }

    fun toRowList(): List<String> {
        return listOf(
            sNo,
            date,
            caseID,
            name,
            age,
            gender,
            department,
            year,
            contactNo,
            referralSource,
            presentingProblem,
            categoryOfIssue,
            severityLevel.toInt().toString(),
            visitType,
            typeOfCounselling,
            sessionsConducted.toString(),
            interventionUsed,
            if (referredToSpecialist) {
                if (specialistDetails.isNotBlank()) "Yes - ${specialistDetails.trim()}" else "Yes"
            } else "No",
            assessment,
            followUpDate,
            progressStatus,
            outcome,
            nextPlan,
            collegeName,
            semester,
            hostel,
            studentEmail
        )
    }

    companion object {
        fun getTodayString(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            return sdf.format(Date())
        }
    }
}

// 2 Screen Tabs: Entry Form vs Admin Dashboard
enum class AppActiveTab {
    IntakeForm,
    AdminDashboard
}

@Composable
fun MainAppScreen() {
    val context = LocalContext.current
    var currentAppTab by remember { mutableStateOf(AppActiveTab.IntakeForm) }
    
    // Track row indices and count for S.No
    var lastSNo by remember { mutableIntStateOf(0) }
    
    fun refreshLastSNo() {
        val rows = LocalFileManager.getCachedRows()
        if (rows.size > 1) {
            val finalRow = rows.last()
            val sNoStr = finalRow.firstOrNull()?.trim()
            val parsed = sNoStr?.toIntOrNull()
            if (parsed != null) {
                lastSNo = parsed
            } else {
                lastSNo = rows.size - 1
            }
        } else {
            lastSNo = 0
        }
    }

    LaunchedEffect(Unit) {
        LocalFileManager.loadIntoCache(context)
        refreshLastSNo()
    }

    val formState = remember(lastSNo) { CounselingFormState(lastSNo) }
    
    // Sticky bottom bar helper hint communication channel
    var activeHint by remember { mutableStateOf("Fill in the fields above. Tap components for expert guidance.") }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                // Sticky bottom hint panel (Focused context)
                AnimatedVisibility(
                    visible = activeHint.isNotEmpty(),
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Guidance Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = activeHint,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                lineHeight = 20.sp
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                // Modern Navigation Bar complying with Android insets
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.testTag("app_navigation_bar"),
                    windowInsets = WindowInsets.navigationBars
                ) {
                    NavigationBarItem(
                        selected = currentAppTab == AppActiveTab.IntakeForm,
                        onClick = { currentAppTab = AppActiveTab.IntakeForm },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Form Entry"
                            )
                        },
                        label = { Text("Session Entry") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_intake_form")
                    )
                    NavigationBarItem(
                        selected = currentAppTab == AppActiveTab.AdminDashboard,
                        onClick = {
                            // Reload cache so calculations are fully fresh
                            LocalFileManager.loadIntoCache(context)
                            currentAppTab = AppActiveTab.AdminDashboard
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "Statistics Panel"
                            )
                        },
                        label = { Text("Insights Admin") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        modifier = Modifier.testTag("nav_admin_dashboard")
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedVisibility(
                visible = currentAppTab == AppActiveTab.IntakeForm,
                modifier = Modifier.weight(1f)
            ) {
                SwipeableIntakeFormScreen(
                    formState = formState,
                    onHintRequested = { hint -> activeHint = hint },
                    onSubmit = {
                        val rowList = formState.toRowList()
                        val result = LocalFileManager.appendRow(context, rowList)
                        if (result) {
                            Toast.makeText(context, "Record successfully stored locally!", Toast.LENGTH_SHORT).show()
                            refreshLastSNo()
                            formState.reset(lastSNo)
                            activeHint = "Record written. Form successfully reset for the next counselor entry."
                        } else {
                            Toast.makeText(context, "Error saving to offline storage sheet.", Toast.LENGTH_LONG).show()
                        }
                    }
                )
            }

            AnimatedVisibility(
                visible = currentAppTab == AppActiveTab.AdminDashboard,
                modifier = Modifier.weight(1f)
            ) {
                AdminDashboardScreen(
                    onHintRequested = { hint -> activeHint = hint }
                )
            }
        }
    }
}

// 3-tab Swipeable Form with local layouts
@Composable
fun SwipeableIntakeFormScreen(
    formState: CounselingFormState,
    onHintRequested: (String) -> Unit,
    onSubmit: () -> Unit
) {
    var activeFormTab by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("1. Student Profile", "2. Presenting Issue", "3. Therapy & Plan")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab Row selector
        TabRow(
            selectedTabIndex = activeFormTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = activeFormTab == index,
                    onClick = { activeFormTab = index },
                    modifier = Modifier.testTag("form_tab_$index")
                ) {
                    Box(modifier = Modifier.padding(vertical = 12.dp)) {
                        Text(
                            text = title,
                            fontWeight = if (activeFormTab == index) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (activeFormTab) {
                0 -> FormDemographicsTab(formState, onHintRequested)
                1 -> FormCategoryAndIssueTab(formState, onHintRequested)
                2 -> FormCounsellingPlanTab(formState, onHintRequested, onSubmit)
            }
        }
    }
}

@Composable
fun FormDemographicsTab(
    formState: CounselingFormState,
    onHintRequested: (String) -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Retrieve previous student logging database records from LocalFileManager
    val allRows = remember { LocalFileManager.getCachedRows() }
    val typedCaseID = formState.caseID.trim()

    // Locate the last logged profile instance of this Case ID to pre-populate current coordinates
    val matchedRow = remember(typedCaseID, allRows) {
        if (typedCaseID.isEmpty()) null
        else {
            allRows.drop(1).lastOrNull { r ->
                val rCaseId = r.getOrNull(2)?.trim() ?: ""
                rCaseId.equals(typedCaseID, ignoreCase = true)
            }
        }
    }

    // Interactive Case ID list suggestions matching characters as they enter it
    val suggestions = remember(typedCaseID, allRows) {
        if (typedCaseID.isEmpty()) {
            emptyList<List<String>>()
        } else {
            allRows.drop(1)
                .filter { r ->
                    val rCaseId = r.getOrNull(2)?.trim() ?: ""
                    rCaseId.isNotEmpty() && 
                    rCaseId.contains(typedCaseID, ignoreCase = true) && 
                    !rCaseId.equals(typedCaseID, ignoreCase = true)
                }
                .groupBy { r -> r.getOrNull(2)?.trim()?.lowercase() ?: "" }
                .map { entry -> entry.value.last() }
                .take(4)
        }
    }

    // Previous logged session details for the student
    val previousSessionsCount = remember(typedCaseID, allRows) {
        if (typedCaseID.isEmpty()) 0
        else {
            allRows.drop(1).count { r ->
                val rCaseId = r.getOrNull(2)?.trim() ?: ""
                rCaseId.equals(typedCaseID, ignoreCase = true)
            }
        }
    }

    // Historical records of this student's past sessions
    val studentHistoryRows = remember(typedCaseID, allRows) {
        if (typedCaseID.isEmpty()) {
            emptyList<List<String>>()
        } else {
            allRows.drop(1).filter { r ->
                val rCaseId = r.getOrNull(2)?.trim() ?: ""
                rCaseId.equals(typedCaseID, ignoreCase = true)
            }.sortedByDescending { r -> r.getOrNull(1) ?: "" }
        }
    }

    // Safe profile autofill execution helper block
    val performAutofill: (List<String>) -> Unit = { row ->
        val registeredName = row.getOrNull(3) ?: ""
        val registeredAge = row.getOrNull(4) ?: ""
        val registeredGender = row.getOrNull(5) ?: "Male"
        val registeredDept = row.getOrNull(6) ?: ""
        val registeredYear = row.getOrNull(7) ?: "1st Year"
        val registeredContact = row.getOrNull(8) ?: ""
        val registeredCollege = row.getOrNull(23) ?: ""
        val registeredSemester = row.getOrNull(24) ?: ""
        val registeredHostel = row.getOrNull(25) ?: ""
        val registeredEmail = row.getOrNull(26) ?: ""

        formState.name = registeredName
        formState.age = registeredAge
        formState.gender = registeredGender
        formState.department = registeredDept
        formState.year = registeredYear
        formState.contactNo = registeredContact
        formState.collegeName = registeredCollege
        formState.semester = registeredSemester
        formState.hostel = registeredHostel
        formState.studentEmail = registeredEmail

        // Automatically configure to a Follow-Up Visit Type for return students!
        formState.visitType = "Follow-up"

        // Calculate and pre-populate next sequence order of sessions conducted
        val targetCaseId = row.getOrNull(2)?.trim() ?: ""
        val loggedVisits = allRows.drop(1).count { r ->
            r.getOrNull(2)?.trim()?.equals(targetCaseId, ignoreCase = true) == true
        }
        formState.sessionsConducted = loggedVisits + 1

        Toast.makeText(context, "Student Profile pre-filled! Session set to Follow-up order #${loggedVisits + 1}.", Toast.LENGTH_LONG).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Highlight Headline Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Intake Info & Student Details",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
                Text(
                    text = "Step 1 of 3: Primary registration coordinates",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Read Only Auto Increment S.No
        Box(
            modifier = Modifier.onFocusChanged {
                if (it.isFocused) onHintRequested("Auto-assigned consecutive entry identifier inside CSV.")
            }
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = formState.sNo,
                onValueChange = {},
                readOnly = true,
                label = { Text("S.No (Auto Indexed)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_sno"),
                leadingIcon = { Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        }

        // Intake Log Date Picker Dialog wrap
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) onHintRequested("Defaults to today. Change if logging a past session.")
                }
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = formState.date,
                onValueChange = {},
                readOnly = true,
                label = { Text("Intake Date (Click to change)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_date"),
                leadingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Transparent overlay box to handle click ripple dynamically
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable {
                        showDatePicker(context, formState.date) { formState.date = it }
                    }
            )
        }

        // Case ID input
        CounsellorInputField(
            value = formState.caseID,
            onValueChange = { formState.caseID = it },
            label = "Case ID (Required)",
            placeholder = "e.g. CN-2026-0001",
            modifier = Modifier.testTag("input_case_id"),
            hintText = "Format: CN-2026-XXXX.",
            onHintRequested = onHintRequested
        )

        // Show Case ID selection Suggestions as user types!
        if (suggestions.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Suggested Matches (Click to pre-fill):",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    suggestions.forEach { r ->
                        val suggId = r.getOrNull(2) ?: ""
                        val suggName = r.getOrNull(3) ?: ""
                        val suggDept = r.getOrNull(6) ?: ""
                        val suggYear = r.getOrNull(7) ?: ""
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    formState.caseID = suggId
                                    performAutofill(r)
                                }
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Face,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = suggId,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "$suggName • $suggDept ($suggYear)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Prefill",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        if (r != suggestions.last()) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }

        // Active matched student profile Banner/Card once the exact Case ID is typed or matched
        if (matchedRow != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Registered student matched",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Existing Student Record Found!",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Case ID: $typedCaseID ($previousSessionsCount existing session(s) detected)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Name", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = matchedRow.getOrNull(3) ?: "Unknown", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Dept & Year", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = "${matchedRow.getOrNull(6) ?: ""} (${matchedRow.getOrNull(7) ?: ""})", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val emailMatch = matchedRow.getOrNull(26) ?: ""
                        if (emailMatch.isNotEmpty()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "Email Address", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text = emailMatch, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Contact Number", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = matchedRow.getOrNull(8) ?: "N/A", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { performAutofill(matchedRow) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Prefill student details",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Prefill Profile Details",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Chronological Session History & Follow-up Dates for this registered student
        if (studentHistoryRows.isNotEmpty()) {
            var isHistoryExpanded by remember { mutableStateOf(true) }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.12f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f),
                        RoundedCornerShape(12.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { isHistoryExpanded = !isHistoryExpanded },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = "History",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Chronological Session History (${studentHistoryRows.size})",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                                Text(
                                    text = "Click to view historical notes & follow-up dates",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        IconButton(onClick = { isHistoryExpanded = !isHistoryExpanded }) {
                            Icon(
                                imageVector = if (isHistoryExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                contentDescription = "Toggle History",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                    if (isHistoryExpanded) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            studentHistoryRows.forEach { row ->
                                val dateStr = row.getOrNull(1) ?: "No Date"
                                val problemStr = row.getOrNull(10) ?: "No Problem Notes"
                                val interventionStr = row.getOrNull(16) ?: ""
                                val followUpStr = row.getOrNull(19) ?: ""
                                val visitTypeStr = row.getOrNull(13) ?: "Unknown"
                                val sessionOrderStr = row.getOrNull(15) ?: "1"
                                val outcomeStr = row.getOrNull(21) ?: ""
                                val nextHomeworkStr = row.getOrNull(22) ?: ""
                                val progStatusStr = row.getOrNull(20) ?: "Improving"

                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        // Header Row with Date & Session No.
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Box(
                                                    modifier = Modifier
                                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), RoundedCornerShape(4.dp))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "Session #$sessionOrderStr ($visitTypeStr)",
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.primary
                                                    )
                                                }
                                            }
                                            Text(
                                                text = dateStr,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        // Presenting problem / notes of each member
                                        Text(
                                            text = "Session Intake Notes / Problem:",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )
                                        Text(
                                            text = if (problemStr.trim().isEmpty()) "No session notes recorded." else problemStr,
                                            fontSize = 12.sp,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.padding(bottom = 6.dp)
                                        )

                                        if (interventionStr.trim().isNotEmpty() || outcomeStr.trim().isNotEmpty()) {
                                            Text(
                                                text = "Interventions, Assessments & Outcomes:",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                            )
                                            Text(
                                                text = buildString {
                                                    if (interventionStr.trim().isNotEmpty()) append("• Intervention: $interventionStr\n")
                                                    if (outcomeStr.trim().isNotEmpty()) append("• Outcome: $outcomeStr")
                                                },
                                                fontSize = 11.sp,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(bottom = 6.dp)
                                            )
                                        }

                                        if (nextHomeworkStr.trim().isNotEmpty()) {
                                            Text(
                                                text = "Next Action Plan:",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                            )
                                            Text(
                                                text = nextHomeworkStr,
                                                fontSize = 11.sp,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.padding(bottom = 6.dp)
                                            )
                                        }

                                        // Progress and Follow-up Date row
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Refresh,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.secondary,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Next Follow-Up: ${if (followUpStr.trim().isEmpty()) "Not Scheduled" else followUpStr}",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                            }
                                            
                                            // Progress status label
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        color = when (progStatusStr) {
                                                            "Improving" -> Color(0xFFE8F5E9)
                                                            "No Change" -> Color(0xFFFFF3E0)
                                                            "Deteriorating" -> Color(0xFFFFEBEE)
                                                            else -> Color(0xFFEBF3FC)
                                                        },
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = progStatusStr,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = when (progStatusStr) {
                                                        "Improving" -> Color(0xFF2E7D32)
                                                        "No Change" -> Color(0xFFE65100)
                                                        "Deteriorating" -> Color(0xFFC62828)
                                                        else -> Color(0xFF1565C0)
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Full Name Field
        CounsellorInputField(
            value = formState.name,
            onValueChange = { formState.name = it },
            label = "Student Legal Name (Primary)",
            placeholder = "Enter full name",
            modifier = Modifier.testTag("input_name"),
            hintText = "Enter full legal name.",
            onHintRequested = onHintRequested
        )

        // Student Age Field
        CounsellorInputField(
            value = formState.age,
            onValueChange = { formState.age = it },
            label = "Age (in Years)",
            placeholder = "e.g. 21",
            modifier = Modifier.testTag("input_age"),
            hintText = "Enter age in years.",
            onHintRequested = onHintRequested,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        // Gender Selector Dropdown box
        CustomDropdownField(
            label = "Gender",
            selectedOption = formState.gender,
            options = listOf("Male", "Female", "Non-binary", "Prefer not to say"),
            onOptionSelected = { formState.gender = it },
            modifier = Modifier.testTag("input_gender"),
            onFocused = { onHintRequested("Select client gender category.") }
        )

        // Branch or Department
        CounsellorInputField(
            value = formState.department,
            onValueChange = { formState.department = it },
            label = "Department / Section",
            placeholder = "e.g. Computer Science, Admin",
            modifier = Modifier.testTag("input_department"),
            hintText = "Specify department or branch.",
            onHintRequested = onHintRequested
        )

        // Year Dropdown select
        CustomDropdownField(
            label = "Academic Year",
            selectedOption = formState.year,
            options = listOf("1st Year", "2nd Year", "3rd Year", "4th Year", "Staff"),
            onOptionSelected = { formState.year = it },
            modifier = Modifier.testTag("input_year"),
            onFocused = { onHintRequested("Choose active year standing.") }
        )

        // Phone connection number
        CounsellorInputField(
            value = formState.contactNo,
            onValueChange = { formState.contactNo = it },
            label = "Phone Contact No",
            placeholder = "10 digit entry",
            modifier = Modifier.testTag("input_contact"),
            hintText = "10-digit number.",
            onHintRequested = onHintRequested,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        )

        // Student Email Address
        CounsellorInputField(
            value = formState.studentEmail,
            onValueChange = { formState.studentEmail = it },
            label = "Student Email Address",
            placeholder = "e.g., student@email.com",
            modifier = Modifier.testTag("input_email"),
            hintText = "Official or primary email address.",
            onHintRequested = onHintRequested,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
        )

        // College Name Input (Default is manual entering + presets in dropdown)
        var collegeExpanded by remember { mutableStateOf(false) }
        val collegeOptions = listOf(
            "KGiSL Institute of Technology (KiTE)",
            "KG College of Arts and Science (KGCAS)",
            "KGiSL Institute of Information Management (KGiSL-IIM)",
            "KGCHS",
            "Thavathiru Santhalinga Adigalar Arts, Science and Tamil College"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) onHintRequested("Type the college name manually or select a predefined option using the dropdown arrow.")
                }
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = formState.collegeName,
                onValueChange = { formState.collegeName = it },
                label = { Text("College Name") },
                placeholder = { Text("Type custom name or select below") },
                trailingIcon = {
                    IconButton(onClick = { collegeExpanded = !collegeExpanded }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "College dropdown")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_college_name"),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(8.dp)
            )

            DropdownMenu(
                expanded = collegeExpanded,
                onDismissRequest = { collegeExpanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                collegeOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            formState.collegeName = option
                            collegeExpanded = false
                        },
                        modifier = Modifier.testTag("college_option_$option")
                    )
                }
            }
        }

        // Semester
        CounsellorInputField(
            value = formState.semester,
            onValueChange = { formState.semester = it },
            label = "Semester",
            placeholder = "e.g. 1st, 2nd, 3rd...",
            modifier = Modifier.testTag("input_semester"),
            hintText = "Specify the current semester of the student.",
            onHintRequested = onHintRequested
        )

        // Hostel / Commuter Stay Selection
        var hostelExpanded by remember { mutableStateOf(false) }
        val hostelOptions = listOf("Hosteller", "Day Scholar")
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) onHintRequested("Choose staying status or type the hostel/room name manually.")
                }
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = formState.hostel,
                onValueChange = { formState.hostel = it },
                label = { Text("Hostel / Residing Status") },
                placeholder = { Text("e.g. Day Scholar, Hostel block A, etc.") },
                trailingIcon = {
                    IconButton(onClick = { hostelExpanded = !hostelExpanded }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Hostel status dropdown")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_hostel"),
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(8.dp)
            )

            DropdownMenu(
                expanded = hostelExpanded,
                onDismissRequest = { hostelExpanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                hostelOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            formState.hostel = option
                            hostelExpanded = false
                        },
                        modifier = Modifier.testTag("hostel_option_$option")
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FormCategoryAndIssueTab(
    formState: CounselingFormState,
    onHintRequested: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Presenting Symptoms & Risk Categories",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
                Text(
                    text = "Step 2 of 3: Severity levels and incident mapping",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Referral Sources Droplist
        CustomDropdownField(
            label = "Referral Source",
            selectedOption = formState.referralSource,
            options = listOf("Self", "HOD", "Professor", "Friend", "Parent", "Website"),
            onOptionSelected = { formState.referralSource = it },
            modifier = Modifier.testTag("input_referral"),
            onFocused = { onHintRequested("Who suggested counseling?") }
        )


        // Issue primary category selection dropdown: "Referred for"
        val predefinedCategories = listOf(
            "Personal Issues",
            "Family Problems",
            "Academic related",
            "Emotional concerns",
            "Relationship issues",
            "Career confusion",
            "Addiction behaviour"
        )
        val selectedOption = if (formState.categoryOfIssue in predefinedCategories) {
            formState.categoryOfIssue
        } else {
            "Others"
        }

        CustomDropdownField(
            label = "Referred for",
            selectedOption = selectedOption,
            options = predefinedCategories + "Others",
            onOptionSelected = { selected ->
                if (selected == "Others") {
                    formState.categoryOfIssue = "Others"
                } else {
                    formState.categoryOfIssue = selected
                }
            },
            modifier = Modifier.testTag("input_issue_category"),
            onFocused = { onHintRequested("Referred for / primary area of concern.") }
        )

        if (selectedOption == "Others") {
            Spacer(modifier = Modifier.height(8.dp))
            CounsellorInputField(
                value = if (formState.categoryOfIssue == "Others") "" else formState.categoryOfIssue,
                onValueChange = { formState.categoryOfIssue = it },
                label = "Specify 'Others' Reason",
                placeholder = "Enter custom referred reason...",
                modifier = Modifier.testTag("input_issue_category_specify"),
                hintText = "Please write the custom referred for reason.",
                onHintRequested = onHintRequested
            )
        }

        // Interactive slider for severity level 1-3 (Mild, Moderate, Severe)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp)
                .onFocusChanged {
                    if (it.isFocused) onHintRequested("Rate 3 (Severe) if crisis management is required.")
                }
        ) {
            Text(
                text = "Severity: ${getSeverityText(formState.severityLevel)}",
                fontWeight = FontWeight.Bold,
                color = getSeverityColor(formState.severityLevel),
                style = MaterialTheme.typography.bodyMedium
            )
            
            Slider(
                value = formState.severityLevel,
                onValueChange = { formState.severityLevel = it },
                valueRange = 1f..3f,
                steps = 1,
                colors = SliderDefaults.colors(
                    thumbColor = getSeverityColor(formState.severityLevel),
                    activeTrackColor = getSeverityColor(formState.severityLevel)
                ),
                modifier = Modifier.testTag("input_severity")
            )

            // Dynamic emergency warning banner
            if (formState.severityLevel >= 3f) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(Color(0xFFFFF3CD), shape = RoundedCornerShape(4.dp))
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Risk Indicator",
                        tint = Color(0xFFD9822B),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SAFETY PROTOCOLS REQUIRED - Emergency crisis plan needed",
                        color = Color(0xFF856404),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun FormCounsellingPlanTab(
    formState: CounselingFormState,
    onHintRequested: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Counselling Log & Follow-up Plan",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 18.sp
                )
                Text(
                    text = "Step 3 of 3: Clinical notes, status, and local save",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Active radio button Visit Check (First vs Follow-Up)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp)
                .onFocusChanged {
                    if (it.isFocused) onHintRequested("Is this the initial intake or follow-up visit?")
                }
        ) {
            Text(
                text = "Visit Type Choice",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Row(
                modifier = Modifier.selectableGroup(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = formState.visitType == "First Visit",
                    onClick = { formState.visitType = "First Visit" },
                    modifier = Modifier.testTag("radio_first_visit")
                )
                Text(
                    text = "First Visit",
                    modifier = Modifier
                        .clickable { formState.visitType = "First Visit" }
                        .padding(end = 16.dp)
                )
                
                RadioButton(
                    selected = formState.visitType == "Follow-up",
                    onClick = { formState.visitType = "Follow-up" },
                    modifier = Modifier.testTag("radio_follow_up")
                )
                Text(
                    text = "Follow-up",
                    modifier = Modifier.clickable { formState.visitType = "Follow-up" }
                )
            }
        }

        // Dropdown Mode of Counselling
        CustomDropdownField(
            label = "Mode of Counselling",
            selectedOption = formState.typeOfCounselling,
            options = listOf("In-person", "Peer Group", "Virtual", "Mobile Call"),
            onOptionSelected = { formState.typeOfCounselling = it },
            modifier = Modifier.testTag("input_counselling_type"),
            onFocused = { onHintRequested("Format or mode of the therapy session.") }
        )

        // Increment Decrement sessions count
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(14.dp)
                .onFocusChanged {
                    if (it.isFocused) onHintRequested("Total counseling sessions completed to date.")
                }
        ) {
            Text(
                text = "Conducted Sessions Count",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { if (formState.sessionsConducted > 1) formState.sessionsConducted-- },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                        .testTag("btn_count_dec")
                ) {
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .height(2.dp)
                            .background(MaterialTheme.colorScheme.onPrimaryContainer)
                    )
                }

                Text(
                    text = formState.sessionsConducted.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .testTag("sessions_count_value")
                )

                IconButton(
                    onClick = { formState.sessionsConducted++ },
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                        .testTag("btn_count_inc")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }

        // Intervention String representation
        CounsellorInputField(
            value = formState.interventionUsed,
            onValueChange = { formState.interventionUsed = it },
            label = "Intervention Used",
            placeholder = "e.g. CBT, Mindfulness, Breathing",
            modifier = Modifier.testTag("input_intervention"),
            hintText = "e.g., CBT, Mindfulness, Grounding.",
            onHintRequested = onHintRequested
        )

        // Referred to external specialist Toggle Choice
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp)
                .clickable { formState.referredToSpecialist = !formState.referredToSpecialist }
                .onFocusChanged {
                    if (it.isFocused) onHintRequested("Indicate external helper referrals.")
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Ref to External Specialist",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = if (formState.referredToSpecialist) "Yes (Outside Counselor / Psychiatry)" else "No",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Switch(
                checked = formState.referredToSpecialist,
                onCheckedChange = { formState.referredToSpecialist = it },
                modifier = Modifier.testTag("toggle_specialist"),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }

        if (formState.referredToSpecialist) {
            Spacer(modifier = Modifier.height(8.dp))
            CounsellorInputField(
                value = formState.specialistDetails,
                onValueChange = { formState.specialistDetails = it },
                label = "Specialist Name & Referral Details",
                placeholder = "e.g. Dr. Jane Smith, Psychiatrist at Apex Clinic...",
                modifier = Modifier.testTag("input_specialist_details"),
                hintText = "Please enter the specialist's name and details.",
                onHintRequested = onHintRequested
            )
        }

        // Psychometric assessments summary notes
        CounsellorInputField(
            value = formState.assessment,
            onValueChange = { formState.assessment = it },
            label = "Psychometric assessments summary",
            placeholder = "Summarize psychometric findings...",
            minLines = 4,
            maxLines = 8,
            modifier = Modifier.testTag("input_assessment"),
            hintText = "Summarize psychometric assessments.",
            onHintRequested = onHintRequested
        )

        // Targeted follow up Datepicker
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused) onHintRequested("Target date for the next monitoring session.")
                }
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = formState.followUpDate,
                onValueChange = {},
                readOnly = true,
                label = { Text("Target Follow-Up Date") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_followup_date"),
                leadingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Dynamic transparent click ripple box to activate DatePicker dialog
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable {
                        showDatePicker(context, formState.followUpDate) { formState.followUpDate = it }
                    }
            )
        }

        // Droplist progress status
        CustomDropdownField(
            label = "Recovery Progress Status",
            selectedOption = formState.progressStatus,
            options = listOf("Improving", "No Change", "Deteriorating", "Case Closed"),
            onOptionSelected = { formState.progressStatus = it },
            modifier = Modifier.testTag("input_progress_status"),
            onFocused = { onHintRequested("Current trajectory of student recovery.") }
        )

        // Outcome summary details
        CounsellorInputField(
            value = formState.outcome,
            onValueChange = { formState.outcome = it },
            label = "Outcome Note",
            placeholder = "e.g. Continued weekly appointment details",
            modifier = Modifier.testTag("input_outcome"),
            hintText = "Short summary of results or session closure status.",
            onHintRequested = onHintRequested
        )

        // Tomorrow homework plan
        CounsellorInputField(
            value = formState.nextPlan,
            onValueChange = { formState.nextPlan = it },
            label = "Next Action Plan Homework",
            placeholder = "e.g. Diaphragmatic breathing loops, journals",
            minLines = 4,
            maxLines = 8,
            modifier = Modifier.testTag("input_next_plan"),
            hintText = "Homework or goals for next session.",
            onHintRequested = onHintRequested
        )

        // PRIMARY ACTION SUBMIT DATA BUTTON
        Button(
            onClick = {
                if (formState.caseID.trim().isEmpty() || formState.name.trim().isEmpty()) {
                    Toast.makeText(context, "ERROR: Case ID and Name parameters cannot be empty!", Toast.LENGTH_SHORT).show()
                } else {
                    onSubmit()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .height(54.dp)
                .testTag("submit_form_button"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Save Data Row Offline",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

// Dropdown component holding a reusable state list
@Composable
fun CustomDropdownField(
    label: String,
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    onFocused: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) onFocused()
            }
    ) {
        androidx.compose.material3.OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Dropdown Switch",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(8.dp)
        )

        // Overlay transparent Box that intercepts clicks to present drop selections cleanly with ripple effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { expanded = !expanded }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    modifier = Modifier.testTag("dropdown_item_$option")
                )
            }
        }
    }
}

fun getSeverityText(level: Float): String {
    return when (level.toInt()) {
        1 -> "1. Mild"
        2 -> "2. Moderate"
        3 -> "3. Severe"
        else -> "1. Mild"
    }
}

fun getSeverityColor(level: Float): Color {
    return when (level.toInt()) {
        1 -> Color(0xFF4CAF50) // Green
        2 -> Color(0xFFFF9800) // Orange
        3 -> Color(0xFFE53935) // Red
        else -> Color(0xFF4CAF50)
    }
}

// Android Calendar wrapped dialog helper
private fun showDatePicker(
    context: Context,
    currentDateStr: String,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = sdf.parse(currentDateStr)
        if (date != null) {
            calendar.time = date
        }
    } catch (ignored: Exception) {}

    val dpd = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val newCal = Calendar.getInstance()
            newCal.set(year, month, dayOfMonth)
            val sdfStr = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            onDateSelected(sdfStr.format(newCal.time))
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    dpd.show()
}

// Clean decoupled text input which does not recursively trigger itself
@Composable
fun CounsellorInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String = "",
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    leadingIcon: @Composable (() -> Unit)? = null,
    hintText: String = "",
    onHintRequested: (String) -> Unit = {}
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = if (placeholder.isNotEmpty()) { { Text(placeholder) } } else null,
        minLines = minLines,
        maxLines = maxLines,
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
            .onFocusChanged { focusState ->
                if (focusState.isFocused && hintText.isNotEmpty()) {
                    onHintRequested(hintText)
                }
            },
        keyboardOptions = keyboardOptions,
        leadingIcon = leadingIcon,
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(8.dp)
    )
}

// OFFLINE MONITORING & SHARING DASHBOARD
@Composable
fun AdminDashboardScreen(
    onHintRequested: (String) -> Unit
) {
    val context = LocalContext.current
    var activeFilter by remember { mutableStateOf("All") } // "All", "Weekly", "Monthly"
    var rowsList by remember { mutableStateOf<List<List<String>>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    var editingRowIndex by remember { mutableStateOf<Int?>(null) }
    var editingFormState by remember { mutableStateOf<CounselingFormState?>(null) }
    var showShareDialog by remember { mutableStateOf(false) }

    fun refreshDashboard() {
        LocalFileManager.loadIntoCache(context)
        rowsList = LocalFileManager.getCachedRows()
    }

    LaunchedEffect(Unit) {
        refreshDashboard()
    }

    // Filter elements with original index mapped
    val dataRows = remember(rowsList, activeFilter) {
        if (rowsList.size <= 1) {
            emptyList()
        } else {
            val dataWithIndices = rowsList.mapIndexed { idx, row -> idx to row }.subList(1, rowsList.size)
            when (activeFilter) {
                "Weekly" -> dataWithIndices.filter { (_, row) ->
                    val dateStr = row.getOrNull(1) ?: ""
                    LocalFileManager.isDateInCurrentWeek(dateStr)
                }
                "Monthly" -> dataWithIndices.filter { (_, row) ->
                    val dateStr = row.getOrNull(1) ?: ""
                    LocalFileManager.isDateInCurrentMonth(dateStr)
                }
                else -> dataWithIndices
            }
        }
    }

    val filteredDataRows = remember(dataRows, searchQuery) {
        if (searchQuery.trim().isEmpty()) {
            dataRows
        } else {
            val query = searchQuery.trim().lowercase()
            dataRows.filter { (_, row) ->
                val sNo = row.getOrNull(0) ?: ""
                val name = row.getOrNull(3) ?: ""
                val caseId = row.getOrNull(2) ?: ""
                val dept = row.getOrNull(6) ?: ""
                val college = row.getOrNull(23) ?: ""
                sNo.lowercase().contains(query) ||
                name.lowercase().contains(query) ||
                caseId.lowercase().contains(query) ||
                dept.lowercase().contains(query) ||
                college.lowercase().contains(query)
            }
        }
    }

    val onStartEdit = { originalIdx: Int, rowList: List<String> ->
        editingRowIndex = originalIdx
        val state = CounselingFormState(0)
        state.fromRowList(rowList)
        editingFormState = state
    }

    // Calculations based on dataRows
    val totalRecordsCount = dataRows.size

    val categoryBreakdown = remember(dataRows) {
        val breakdown = mutableMapOf<String, Int>()
        dataRows.forEach { (_, row) ->
            val cat = row.getOrNull(11)?.trim() ?: ""
            if (cat.isNotEmpty()) {
                breakdown[cat] = (breakdown[cat] ?: 0) + 1
            }
        }
        breakdown
    }

    val severityBreakdown = remember(dataRows) {
        val breakdown = mutableMapOf<Int, Int>()
        dataRows.forEach { (_, row) ->
            val sev = row.getOrNull(12)?.trim()?.toIntOrNull() ?: 1
            breakdown[sev] = (breakdown[sev] ?: 0) + 1
        }
        breakdown
    }

    // Modern resolved category list incorporating both preset options and actual data values
    val categoriesToDisplay = remember(categoryBreakdown) {
        val predefinedList = listOf(
            "Personal Issues",
            "Family Problems",
            "Academic related",
            "Emotional concerns",
            "Relationship issues",
            "Career confusion",
            "Addiction behaviour"
        )
        val fullList = predefinedList.toMutableList()
        categoryBreakdown.keys.forEach { key ->
            if (key !in predefinedList && key.isNotBlank() && key != "Others") {
                fullList.add(key)
            }
        }
        fullList.distinct()
    }

    if (editingRowIndex != null && editingFormState != null) {
        EditRecordDialog(
            formState = editingFormState!!,
            onDismiss = {
                editingRowIndex = null
                editingFormState = null
            },
            onSave = {
                val updatedRow = editingFormState!!.toRowList()
                val success = LocalFileManager.updateRow(context, editingRowIndex!!, updatedRow)
                if (success) {
                    Toast.makeText(context, "Record successfully updated!", Toast.LENGTH_SHORT).show()
                    refreshDashboard()
                    editingRowIndex = null
                    editingFormState = null
                } else {
                    Toast.makeText(context, "Error updating record in offline storage.", Toast.LENGTH_LONG).show()
                }
            }
        )
    }

    if (showShareDialog) {
        ShareDatabaseDialog(
            allRows = rowsList,
            filteredRows = filteredDataRows.map { it.second },
            activeFilterSegment = activeFilter,
            searchQuery = searchQuery,
            onDismiss = { showShareDialog = false },
            onShare = { selectedRows ->
                val shareUri = LocalFileManager.getShareableFileUri(context, selectedRows)
                if (shareUri != null) {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/csv"
                        putExtra(Intent.EXTRA_STREAM, shareUri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Master Counselling Spreadsheet"))
                } else {
                    Toast.makeText(context, "Empty list - nothing to share!", Toast.LENGTH_SHORT).show()
                }
                showShareDialog = false
            }
        )
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .onFocusChanged {
                if (it.isFocused) onHintRequested("Browse local database statistics. Sharing triggers manual document transfers.")
            },
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Redesigned Top Hero Banner with Sleek Outline & Dynamic Accents
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            ),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Spreadsheet Analytics Center",
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(6.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "OFFLINE CSV",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Reading live offline log entries from: ${LocalFileManager.FILE_NAME}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Side-by-Side Dual Button Control Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    if (rowsList.isEmpty()) {
                        Toast.makeText(context, "Local spreadsheet file is empty. Save data rows first!", Toast.LENGTH_SHORT).show()
                    } else {
                        showShareDialog = true
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1.2f)
                    .height(48.dp)
                    .testTag("btn_share_sheet")
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Share Spreadsheet",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Button(
                onClick = {
                    refreshDashboard()
                    Toast.makeText(context, "Offline data cache successfully synced with disk!", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("btn_refresh_dashboard")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Sync",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Sync Cache",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        // Segment Filter Tabpill
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Dynamic Data Segment Filter",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("All", "Weekly", "Monthly").forEach { segment ->
                    val isSelected = activeFilter == segment
                    val labelText = when (segment) {
                        "All" -> "All Records"
                        "Weekly" -> "Weekly View"
                        "Monthly" -> "Monthly View"
                        else -> segment
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            )
                            .clickable { activeFilter = segment }
                            .padding(vertical = 10.dp)
                            .testTag("filter_segment_$segment"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = labelText,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Two-Column Grid Summary KPI Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$totalRecordsCount entries",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Active Segment Records",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        )
                        Text(
                            text = "Healthy",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Database Sync Status",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Breakdown distribution visual cards
        Text(
            text = "Categories of Core Problems",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (totalRecordsCount == 0) {
                    Text(
                        text = "No records logged inside selected segment. Complete a session entry first!",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                } else {
                    categoriesToDisplay.forEach { category ->
                        val count = categoryBreakdown[category] ?: 0
                        val percentage = if (totalRecordsCount > 0) count.toFloat() / totalRecordsCount else 0f
                        Column(modifier = Modifier.padding(vertical = 6.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = category,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "$count cases (${(percentage * 100).toInt()}%)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            LinearProgressIndicator(
                                progress = { percentage },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }
        }

        // Severity Distribution
        Text(
            text = "Risk Severity Distribution",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = androidx.compose.foundation.BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
            ),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (totalRecordsCount == 0) {
                    Text(
                        text = "No records matching requested segment.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (1..3).forEach { severityLevel ->
                            val count = if (severityLevel == 3) {
                                (severityBreakdown[3] ?: 0) + (severityBreakdown[4] ?: 0) + (severityBreakdown[5] ?: 0)
                            } else {
                                severityBreakdown[severityLevel] ?: 0
                            }
                            val percentage = if (totalRecordsCount > 0) count.toFloat() / totalRecordsCount else 0f
                            val labelText = when (severityLevel) {
                                1 -> "Mild"
                                2 -> "Moderate"
                                3 -> "Severe"
                                else -> "Mild"
                            }
                            val color = getSeverityColor(severityLevel.toFloat())

                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(
                                    containerColor = color.copy(alpha = 0.08f)
                                ),
                                border = androidx.compose.foundation.BorderStroke(
                                    width = 1.dp,
                                    color = color.copy(alpha = 0.35f)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            text = labelText,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = color
                                        )
                                        if (severityLevel == 3) {
                                            Icon(
                                                imageVector = Icons.Default.Warning,
                                                contentDescription = null,
                                                tint = color,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "$count cases",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "(${(percentage * 100).toInt()}%)",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Logged Counselling Records",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp
        )

        // Clean Custom Styled Search Input
        androidx.compose.material3.OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Counselling Database") },
            placeholder = { Text("Search by student name, serial, category, college...") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_records_input"),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = if (searchQuery.isNotEmpty()) {
                {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear search")
                    }
                }
            } else null,
            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(12.dp)
        )

        if (filteredDataRows.isEmpty()) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "No matching counselling records found.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            filteredDataRows.forEach { (originalIdx, row) ->
                val sNo = row.getOrNull(0) ?: ""
                val dateVal = row.getOrNull(1) ?: ""
                val caseId = row.getOrNull(2) ?: ""
                val nameVal = row.getOrNull(3) ?: ""
                val dept = row.getOrNull(6) ?: ""
                val yr = row.getOrNull(7) ?: ""
                val problem = row.getOrNull(10) ?: ""
                val severityStr = row.getOrNull(12) ?: "1"
                val progress = row.getOrNull(20) ?: ""
                val college = row.getOrNull(23) ?: ""
                val emailVal = row.getOrNull(26) ?: ""

                // Redesigned Clinical Student Profile session card
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("record_card_$sNo")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // S.No + Date
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Ref ID: $sNo [Case $caseId]",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = dateVal,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Avatar & Student Name
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = nameVal,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Group: $dept ($yr)" + if (college.isNotEmpty()) " • $college" else "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (emailVal.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Email,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                    modifier = Modifier.size(13.dp)
                                )
                                Text(
                                    text = emailVal,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Presenting concern enclosed beautifully
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(3.dp)
                                        .height(38.dp)
                                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                                )
                                Column {
                                    Text(
                                        text = "Core Presenting Issue",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = problem,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Row metadata tags + edit triggers
                        // High contrast visual state indicators row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Severity Label
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = getSeverityColor(severityStr.toFloatOrNull() ?: 1f).copy(alpha = 0.12f),
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = getSeverityText(severityStr.toFloatOrNull() ?: 1f).substringAfter(". ").trim(),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = getSeverityColor(severityStr.toFloatOrNull() ?: 1f)
                                )
                            }

                            // Counselling Mode
                            val rowCounsellingType = row.getOrNull(14) ?: ""
                            if (rowCounsellingType.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = rowCounsellingType,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }

                            // Progress state
                            if (progress.isNotEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(6.dp)
                                        )
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = progress,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Clean interactive buttons row (Share and Open File)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            androidx.compose.material3.TextButton(
                                onClick = {
                                    val age = row.getOrNull(4) ?: ""
                                    val gender = row.getOrNull(5) ?: ""
                                    val phone = row.getOrNull(8) ?: ""
                                    val category = row.getOrNull(11) ?: ""
                                    val mode = row.getOrNull(14) ?: ""
                                    val assessment = row.getOrNull(18) ?: ""

                                    val severityText = when(severityStr.toFloatOrNull()?.toInt() ?: 1) {
                                        1 -> "Mild"
                                        2 -> "Moderate"
                                        3 -> "Severe"
                                        else -> "Mild"
                                    }

                                    val summaryBody = "COUNSELLING SESSION SUMMARY\n" +
                                            "--------------------------\n" +
                                            "Reference ID: $sNo\n" +
                                            "Case ID: $caseId\n" +
                                            "Date: $dateVal\n\n" +
                                            "STUDENT INFORMATION\n" +
                                            "Name: $nameVal\n" +
                                            "Age: $age\n" +
                                            "Gender: $gender\n" +
                                            "College: $college\n" +
                                            "Department: $dept ($yr Year)\n" +
                                            "Contact: $phone\n" +
                                            "Email: $emailVal\n\n" +
                                            "CLINICAL STATUS\n" +
                                            "Presenting Concern: $problem\n" +
                                            "Category: $category\n" +
                                            "Severity Level: $severityText\n" +
                                            "Counselling Mode: $mode\n" +
                                            "Progress Status: $progress\n\n" +
                                            "PSYCHOMETRIC FINDINGS\n" +
                                            "$assessment"

                                    val shareUri = LocalFileManager.getShareableFileUri(context, listOf(row))

                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/csv"
                                        putExtra(Intent.EXTRA_SUBJECT, "Counselling Student File - $nameVal")
                                        putExtra(Intent.EXTRA_TEXT, summaryBody)
                                        if (shareUri != null) {
                                            putExtra(Intent.EXTRA_STREAM, shareUri)
                                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                        }
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share record of $nameVal"))
                                },
                                modifier = Modifier.testTag("btn_share_record_$sNo"),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "Share student record",
                                    modifier = Modifier.size(13.dp),
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Share",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }

                            androidx.compose.material3.TextButton(
                                onClick = { onStartEdit(originalIdx, row) },
                                modifier = Modifier.testTag("btn_edit_record_$sNo"),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit record",
                                    modifier = Modifier.size(13.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Open File",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun EditRecordDialog(
    formState: CounselingFormState,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        androidx.compose.material3.Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Edit Log S.No: ${formState.sNo}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Modifying historical counselling record on disk",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("btn_close_edit_dialog")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close Edit System")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Scrollable content
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Demographic Information
                    Text(
                        text = "1. Student Profile & Intake Details",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Intake Log Date Picker Dialog wrap
                    Box(modifier = Modifier.fillMaxWidth()) {
                        androidx.compose.material3.OutlinedTextField(
                            value = formState.date,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Intake Date (Click to change)") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("edit_input_date"),
                            leadingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        // Transparent overlay box to intercept click events safely with ripple support
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable {
                                    showDatePicker(context, formState.date) { formState.date = it }
                                }
                        )
                    }

                    // Case ID input
                    CounsellorInputField(
                        value = formState.caseID,
                        onValueChange = { formState.caseID = it },
                        label = "Case ID (Required)",
                        placeholder = "e.g. CN-2026-0001",
                        modifier = Modifier.testTag("edit_input_case_id")
                    )

                    // Full Name Field
                    CounsellorInputField(
                        value = formState.name,
                        onValueChange = { formState.name = it },
                        label = "Student Legal Name (Primary)",
                        placeholder = "Enter full name",
                        modifier = Modifier.testTag("edit_input_name")
                    )

                    // Student Age Field
                    CounsellorInputField(
                        value = formState.age,
                        onValueChange = { formState.age = it },
                        label = "Age (in Years)",
                        placeholder = "e.g. 21",
                        modifier = Modifier.testTag("edit_input_age"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    // Gender Selector Dropdown box
                    CustomDropdownField(
                        label = "Gender",
                        selectedOption = formState.gender,
                        options = listOf("Male", "Female", "Non-binary", "Prefer not to say"),
                        onOptionSelected = { formState.gender = it },
                        modifier = Modifier.testTag("edit_input_gender")
                    )

                    // College Name dropdown + input
                    var collegeExpanded by remember { mutableStateOf(false) }
                    val collegeOptions = listOf(
                        "KGiSL Institute of Technology (KiTE)",
                        "KG College of Arts and Science (KGCAS)",
                        "KGiSL Institute of Information Management (KGiSL-IIM)",
                        "KGCHS",
                        "Thavathiru Santhalinga Adigalar Arts, Science and Tamil College"
                    )

                    Box(modifier = Modifier.fillMaxWidth()) {
                        androidx.compose.material3.OutlinedTextField(
                            value = formState.collegeName,
                            onValueChange = { formState.collegeName = it },
                            label = { Text("College Name") },
                            placeholder = { Text("Type custom name or select below") },
                            trailingIcon = {
                                IconButton(onClick = { collegeExpanded = !collegeExpanded }) {
                                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "College dropdown")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("edit_input_college_name"),
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        DropdownMenu(
                            expanded = collegeExpanded,
                            onDismissRequest = { collegeExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            collegeOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        formState.collegeName = option
                                        collegeExpanded = false
                                    },
                                    modifier = Modifier.testTag("edit_college_option_$option")
                                )
                            }
                        }
                    }

                    // Branch or Department
                    CounsellorInputField(
                        value = formState.department,
                        onValueChange = { formState.department = it },
                        label = "Department / Section",
                        placeholder = "e.g. Computer Science",
                        modifier = Modifier.testTag("edit_input_department")
                    )

                    // Semester
                    CounsellorInputField(
                        value = formState.semester,
                        onValueChange = { formState.semester = it },
                        label = "Semester",
                        placeholder = "e.g. 1st, 2nd, 3rd...",
                        modifier = Modifier.testTag("edit_input_semester")
                    )

                    // Year Dropdown select
                    CustomDropdownField(
                        label = "Academic Year",
                        selectedOption = formState.year,
                        options = listOf("1st Year", "2nd Year", "3rd Year", "4th Year", "Staff"),
                        onOptionSelected = { formState.year = it },
                        modifier = Modifier.testTag("edit_input_year")
                    )

                    // Hostel / Commuter Stay Selection
                    var hostelExpanded by remember { mutableStateOf(false) }
                    val hostelOptions = listOf("Hosteller", "Day Scholar")
                    
                    Box(modifier = Modifier.fillMaxWidth()) {
                        androidx.compose.material3.OutlinedTextField(
                            value = formState.hostel,
                            onValueChange = { formState.hostel = it },
                            label = { Text("Hostel / Residing Status") },
                            placeholder = { Text("e.g. Day Scholar, Hostel block A, etc.") },
                            trailingIcon = {
                                IconButton(onClick = { hostelExpanded = !hostelExpanded }) {
                                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Hostel status dropdown")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("edit_input_hostel"),
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        DropdownMenu(
                            expanded = hostelExpanded,
                            onDismissRequest = { hostelExpanded = false },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        ) {
                            hostelOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        formState.hostel = option
                                        hostelExpanded = false
                                    },
                                    modifier = Modifier.testTag("edit_hostel_option_$option")
                                )
                            }
                        }
                    }

                    // Contact No
                    CounsellorInputField(
                        value = formState.contactNo,
                        onValueChange = { formState.contactNo = it },
                        label = "Phone Contact No",
                        placeholder = "10 digit entry",
                        modifier = Modifier.testTag("edit_input_contact"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    )

                    // Student Email Address
                    CounsellorInputField(
                        value = formState.studentEmail,
                        onValueChange = { formState.studentEmail = it },
                        label = "Student Email Address",
                        placeholder = "e.g., student@email.com",
                        modifier = Modifier.testTag("edit_input_email"),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    // Symptoms & Risks
                    Text(
                        text = "2. Presenting Symptoms & Risk Level",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Referral Source
                    CustomDropdownField(
                        label = "Referral Source",
                        selectedOption = formState.referralSource,
                        options = listOf("Self", "HOD", "Professor", "Friend", "Parent", "Website"),
                        onOptionSelected = { formState.referralSource = it },
                        modifier = Modifier.testTag("edit_input_referral")
                    )

                    // Referred for
                    val predefinedCategoriesEdit = listOf(
                        "Personal Issues",
                        "Family Problems",
                        "Academic related",
                        "Emotional concerns",
                        "Relationship issues",
                        "Career confusion",
                        "Addiction behaviour"
                    )
                    val selectedOptionEdit = if (formState.categoryOfIssue in predefinedCategoriesEdit) {
                        formState.categoryOfIssue
                    } else {
                        "Others"
                    }

                    CustomDropdownField(
                        label = "Referred for",
                        selectedOption = selectedOptionEdit,
                        options = predefinedCategoriesEdit + "Others",
                        onOptionSelected = { selected ->
                            if (selected == "Others") {
                                formState.categoryOfIssue = "Others"
                            } else {
                                formState.categoryOfIssue = selected
                            }
                        },
                        modifier = Modifier.testTag("edit_input_issue_category")
                    )

                    if (selectedOptionEdit == "Others") {
                        Spacer(modifier = Modifier.height(8.dp))
                        CounsellorInputField(
                            value = if (formState.categoryOfIssue == "Others") "" else formState.categoryOfIssue,
                            onValueChange = { formState.categoryOfIssue = it },
                            label = "Specify 'Others' Reason",
                            placeholder = "Enter custom referred reason...",
                            modifier = Modifier.testTag("edit_input_issue_category_specify")
                        )
                    }

                    // Severity Level
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Severity: ${getSeverityText(formState.severityLevel)}",
                            fontWeight = FontWeight.Bold,
                            color = getSeverityColor(formState.severityLevel),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        
                        Slider(
                            value = formState.severityLevel,
                            onValueChange = { formState.severityLevel = it },
                            valueRange = 1f..3f,
                            steps = 1,
                            colors = SliderDefaults.colors(
                                thumbColor = getSeverityColor(formState.severityLevel),
                                activeTrackColor = getSeverityColor(formState.severityLevel)
                            ),
                            modifier = Modifier.testTag("edit_input_severity")
                        )

                        // Dynamic safety warning banner under edit dialog also 
                        if (formState.severityLevel >= 3f) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .background(Color(0xFFFFF3CD), shape = RoundedCornerShape(4.dp))
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Risk Indicator",
                                    tint = Color(0xFFD9822B),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "SAFETY PROTOCOLS REQUIRED - Emergency crisis plan needed",
                                    color = Color(0xFF856404),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    // Counselling & Plan
                    Text(
                        text = "3. Counselling Process & Future Plan",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Visit Type Check Box/Radio Group
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Visit Type Choice",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Row(
                            modifier = Modifier.selectableGroup(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = formState.visitType == "First Visit",
                                onClick = { formState.visitType = "First Visit" },
                                modifier = Modifier.testTag("edit_radio_first_visit")
                            )
                            Text(
                                text = "First Visit",
                                modifier = Modifier
                                    .clickable { formState.visitType = "First Visit" }
                                    .padding(end = 16.dp)
                            )
                            
                            RadioButton(
                                selected = formState.visitType == "Follow-up",
                                onClick = { formState.visitType = "Follow-up" },
                                modifier = Modifier.testTag("edit_radio_follow_up")
                            )
                            Text(
                                text = "Follow-up",
                                modifier = Modifier.clickable { formState.visitType = "Follow-up" }
                            )
                        }
                    }

                    // Mode of Counselling
                    CustomDropdownField(
                        label = "Mode of Counselling",
                        selectedOption = formState.typeOfCounselling,
                        options = listOf("In-person", "Peer Group", "Virtual", "Mobile Call"),
                        onOptionSelected = { formState.typeOfCounselling = it },
                        modifier = Modifier.testTag("edit_input_counselling_type")
                    )

                    // Sessions Conducted count
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "Conducted Sessions Count",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                onClick = { if (formState.sessionsConducted > 1) formState.sessionsConducted-- },
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                                    .testTag("edit_btn_count_dec")
                            ) {
                                Box(
                                    modifier = Modifier
                                        .width(12.dp)
                                        .height(2.dp)
                                        .background(MaterialTheme.colorScheme.onPrimaryContainer)
                                )
                            }

                            Text(
                                text = formState.sessionsConducted.toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .testTag("edit_sessions_count_value")
                            )

                            IconButton(
                                onClick = { formState.sessionsConducted++ },
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
                                    .testTag("edit_btn_count_inc")
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                            }
                        }
                    }

                    // Intervention Used
                    CounsellorInputField(
                        value = formState.interventionUsed,
                        onValueChange = { formState.interventionUsed = it },
                        label = "Intervention Used",
                        placeholder = "e.g. CBT, Mindfulness, Breathing",
                        modifier = Modifier.testTag("edit_input_intervention")
                    )

                    // Referred to Specialist Switch
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(12.dp)
                            .clickable { formState.referredToSpecialist = !formState.referredToSpecialist },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Ref to External Specialist",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (formState.referredToSpecialist) "Yes (Outside Counselor / Psychiatry)" else "No",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Switch(
                            checked = formState.referredToSpecialist,
                            onCheckedChange = { formState.referredToSpecialist = it },
                            modifier = Modifier.testTag("edit_toggle_specialist"),
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }

                    if (formState.referredToSpecialist) {
                        Spacer(modifier = Modifier.height(8.dp))
                        CounsellorInputField(
                            value = formState.specialistDetails,
                            onValueChange = { formState.specialistDetails = it },
                            label = "Specialist Name & Referral Details",
                            placeholder = "e.g. Dr. Jane Smith, Psychiatrist...",
                            modifier = Modifier.testTag("edit_input_specialist_details")
                        )
                    }

                    // Assessment notes
                    CounsellorInputField(
                        value = formState.assessment,
                        onValueChange = { formState.assessment = it },
                        label = "Psychometric assessments summary",
                        placeholder = "Summarize findings...",
                        minLines = 3,
                        maxLines = 6,
                        modifier = Modifier.testTag("edit_input_assessment")
                    )

                    // Follow up date
                    Box(modifier = Modifier.fillMaxWidth()) {
                        androidx.compose.material3.OutlinedTextField(
                            value = formState.followUpDate,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Target Follow-Up Date") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("edit_input_followup_date"),
                            leadingIcon = { Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )

                        // Transparent overlay box to intercept follow-up date click events nicely with ripple
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable {
                                    showDatePicker(context, formState.followUpDate) { formState.followUpDate = it }
                                }
                        )
                    }

                    // Progress Status
                    CustomDropdownField(
                        label = "Recovery Progress Status",
                        selectedOption = formState.progressStatus,
                        options = listOf("Improving", "No Change", "Deteriorating", "Case Closed"),
                        onOptionSelected = { formState.progressStatus = it },
                        modifier = Modifier.testTag("edit_input_progress_status")
                    )

                    // Outcome note
                    CounsellorInputField(
                        value = formState.outcome,
                        onValueChange = { formState.outcome = it },
                        label = "Outcome Note",
                        placeholder = "Enter results or closure details",
                        modifier = Modifier.testTag("edit_input_outcome")
                    )

                    // Next Homework Plan
                    CounsellorInputField(
                        value = formState.nextPlan,
                        onValueChange = { formState.nextPlan = it },
                        label = "Next Action Plan Homework",
                        placeholder = "e.g. mindfulness exercises",
                        minLines = 3,
                        maxLines = 6,
                        modifier = Modifier.testTag("edit_input_next_plan")
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    androidx.compose.material3.OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_cancel_edit"),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (formState.caseID.trim().isEmpty() || formState.name.trim().isEmpty()) {
                                Toast.makeText(context, "ERROR: Case ID and Name are required!", Toast.LENGTH_SHORT).show()
                            } else {
                                onSave()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("btn_save_edit"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Save Changes", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// OFFLINE ADVANCED EXPORT & FILTER SHARING DIALOG
@Composable
fun ShareDatabaseDialog(
    allRows: List<List<String>>,
    filteredRows: List<List<String>>,
    activeFilterSegment: String,
    searchQuery: String,
    onDismiss: () -> Unit,
    onShare: (List<List<String>>) -> Unit
) {
    val context = LocalContext.current
    var shareOption by remember { mutableStateOf("filtered") }
    
    var customCategory by remember { mutableStateOf("All") }
    var customSeverity by remember { mutableStateOf("All") }
    var customMode by remember { mutableStateOf("All") }

    val categoryOptions = listOf(
        "All",
        "Personal Issues",
        "Family Problems",
        "Academic related",
        "Emotional concerns",
        "Relationship issues",
        "Career confusion",
        "Addiction behaviour"
    )
    val severityOptions = listOf("All", "Mild", "Moderate", "Severe")
    val modeOptions = listOf("All", "In-person", "Peer Group", "Virtual", "Mobile Call")

    val rawDataRows = remember(allRows) {
        if (allRows.size > 1) allRows.subList(1, allRows.size) else emptyList()
    }

    val rowsToShare = remember(shareOption, filteredRows, rawDataRows, customCategory, customSeverity, customMode) {
        when (shareOption) {
            "filtered" -> filteredRows
            "all" -> rawDataRows
            "custom" -> {
                rawDataRows.filter { row ->
                    val catStr = row.getOrNull(11) ?: ""
                    val categoryMatch = customCategory == "All" || catStr.trim().equals(customCategory.trim(), ignoreCase = true)
                    
                    val sevStr = row.getOrNull(12)?.toFloatOrNull() ?: 1f
                    val sevText = when (sevStr.toInt()) {
                        1 -> "Mild"
                        2 -> "Moderate"
                        3 -> "Severe"
                        else -> "Mild"
                    }
                    val severityMatch = customSeverity == "All" || sevText.equals(customSeverity.trim(), ignoreCase = true)
                    
                    val modeStr = row.getOrNull(14) ?: ""
                    val modeMatch = customMode == "All" || modeStr.trim().equals(customMode.trim(), ignoreCase = true)
                    
                    categoryMatch && severityMatch && modeMatch
                }
            }
            else -> filteredRows
        }
    }

    androidx.compose.ui.window.Dialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false)
    ) {
        androidx.compose.material3.Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share, 
                            contentDescription = null, 
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Filter & Share Database",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close dialog")
                    }
                }

                Text(
                    text = "Select what segment of logs you want to extract and bundle as a temporary CSV spreadsheet attachment.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Card(
                        onClick = { shareOption = "filtered" },
                        colors = CardDefaults.cardColors(
                            containerColor = if (shareOption == "filtered") MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (shareOption == "filtered") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            RadioButton(
                                selected = (shareOption == "filtered"),
                                onClick = { shareOption = "filtered" }
                            )
                            Column {
                                Text(
                                    text = "Current Dashboard Filter Only",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Active: Segment '$activeFilterSegment'" + if (searchQuery.isNotEmpty()) " & Search query '$searchQuery'" else "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "(${rowsToShare.size} records matching)",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Card(
                        onClick = { shareOption = "all" },
                        colors = CardDefaults.cardColors(
                            containerColor = if (shareOption == "all") MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (shareOption == "all") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            RadioButton(
                                selected = (shareOption == "all"),
                                onClick = { shareOption = "all" }
                            )
                            Column {
                                Text(
                                    text = "Entire Spreadsheet Database",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Extracts all chronological logging records in raw CSV format.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "(${rawDataRows.size} total records)",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Card(
                        onClick = { shareOption = "custom" },
                        colors = CardDefaults.cardColors(
                            containerColor = if (shareOption == "custom") MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = if (shareOption == "custom") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                RadioButton(
                                    selected = (shareOption == "custom"),
                                    onClick = { shareOption = "custom" }
                                )
                                Column {
                                    Text(
                                        text = "Custom Spreadsheet Filter",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Specify criteria properties below to slice out specific entries.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (shareOption == "custom") {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outlineVariant)
                                
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    CustomDropdownField(
                                        label = "Core Issue Category",
                                        selectedOption = customCategory,
                                        options = categoryOptions,
                                        onOptionSelected = { customCategory = it }
                                    )

                                    CustomDropdownField(
                                        label = "Risk Severity Match",
                                        selectedOption = customSeverity,
                                        options = severityOptions,
                                        onOptionSelected = { customSeverity = it }
                                    )

                                    CustomDropdownField(
                                        label = "Counselling Mode Filter",
                                        selectedOption = customMode,
                                        options = modeOptions,
                                        onOptionSelected = { customMode = it }
                                    )
                                }
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Export Cargo Preview",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "${rowsToShare.size} records found matching filter constraints",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Info, 
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    androidx.compose.material3.OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            onShare(rowsToShare)
                        },
                        enabled = rowsToShare.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                        ),
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share via CSV", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
