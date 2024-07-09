import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

class FormViewModel : ViewModel() {
    var selectedOptions = mutableStateMapOf<String, String>()

    fun updateSelectedOption(elementName: String, option: String) {
        selectedOptions[elementName] = option
    }
}
