import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class FormViewModel : ViewModel() {
    var selectedOptions by mutableStateOf(mutableMapOf<String, String>())

    fun updateSelectedOption(key: String, value: String) {
        selectedOptions[key] = value
    }
}
