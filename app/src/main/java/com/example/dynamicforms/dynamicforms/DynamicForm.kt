package com.example.dynamicforms

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun DynamicFormScreen(jsonString: String) {
    val formType = remember {
        JsonParser.parseJson(jsonString)
    }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(formType.pages) { page ->
            Text(
                text = page.name ?: "",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            page.sections.forEach { section ->
                section.elements.forEach { element ->
                    when (element.elementType) {
                        "TEXT" -> {
                            var textValue by remember { mutableStateOf(TextFieldValue()) }
                            CustomTextField(
                                value = textValue,
                                onValueChange = { textValue = it },
                                label = element.name ?: ""
                            )
                        }
                        "DATE" -> {
                            DateInputField(element)
                        }
                        // Handle other element types as needed
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    logFormData(formType)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Submit", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun CustomTextField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, label: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(8.dp)
    )
}

@Composable
fun DateInputField(element: FormElement) {
    val context = LocalContext.current
    var dateValue by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            dateValue = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        },
        year,
        month,
        day
    )

    OutlinedTextField(
        value = dateValue,
        onValueChange = { },
        label = { Text(text = element.name ?: "") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(8.dp),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
            }
        }
    )
}

private fun logFormData(formType: FormType) {
    println("Logging form data:")
    formType.pages.forEach { page ->
        println("Page: ${page.name}")
        page.sections.forEach { section ->
            section.elements.forEach { element ->
                println("${element.name}: ${getFormattedValue(element)}")
            }
        }
    }
}

private fun getFormattedValue(element: FormElement): String {
    return when (element.elementType) {
        "TEXT" -> element.options.firstOrNull()?.name ?: ""
        "DATE" -> element.options.firstOrNull()?.name ?: "" // Implement date formatting logic here
        // Handle other element types as needed
        else -> ""
    }
}