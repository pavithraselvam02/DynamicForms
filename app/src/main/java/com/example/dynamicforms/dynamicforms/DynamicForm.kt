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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
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
                            TextField(
                                value = textValue,
                                onValueChange = { textValue = it },
                                label = { Text(text = element.name ?: "") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
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
                    // Log the form data
                    logFormData(formType)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Submit", fontSize = 18.sp)
            }
        }
    }
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

    TextField(
        value = dateValue,
        onValueChange = { },
        label = { Text(text = element.name ?: "") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { datePickerDialog.show() }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
            }
        }
    )
}

private fun logFormData(formType: FormType) {
    // Here, you can log the form data to the console or any other logging mechanism
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
