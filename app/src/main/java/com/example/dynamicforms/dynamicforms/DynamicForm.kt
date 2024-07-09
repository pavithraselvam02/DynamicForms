
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dynamicforms.FormElement
import com.example.dynamicforms.FormType
import com.example.dynamicforms.JsonParser
import com.example.dynamicforms.Option
import java.util.*

@Composable
fun DynamicFormScreen(jsonString: String, formViewModel: FormViewModel = viewModel()) {
    val formType = remember { JsonParser.parseJson(jsonString) }

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
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
                        "SELECT" -> {
                            val selectedOption = formViewModel.selectedOptions[element.name ?: ""] ?: ""
                            DropdownField(
                                options = element.options,
                                selectedOption = selectedOption,
                                onOptionSelected = { formViewModel.updateSelectedOption(element.name ?: "", it) },
                                label = element.name ?: ""
                            )
                        }
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
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center // Center the TextField in the Box
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = label) },
            modifier = Modifier
                .width(400.dp) // Fixed width of 300.dp
                .padding(vertical = 8.dp)
                .padding(8.dp)
        )
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

    val datePickerDialog = android.app.DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            dateValue = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        },
        year,
        month,
        day
    )

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        OutlinedTextField(
            value = dateValue,
            onValueChange = { },
            label = { Text(text = element.name ?: "") },
            modifier = Modifier
                .width(400.dp) // Fixed width of 300.dp
                .padding(vertical = 8.dp)
                .padding(8.dp),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { datePickerDialog.show() }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
                }
            }
        )
    }
}

@Composable
fun DropdownField(
    options: List<Option>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(400.dp)
                .padding(8.dp)
                .padding(8.dp)
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { },
                label = { Text(text = label, style = TextStyle(color = Color.Gray)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                            contentDescription = "Select Option"
                        )
                    }
                }
            )
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                        .padding(vertical = 8.dp)
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.name, fontSize = 16.sp) },
                            onClick = {
                                onOptionSelected(option.name)
                                expanded = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(8.dp)
                        )
                        Divider(color = Color.Gray)
                    }
                }
            }
        }
    }
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
        "DATE" -> element.options.firstOrNull()?.name ?: ""
        "SELECT" -> element.options.firstOrNull()?.name ?: ""
        else -> ""
    }
}
