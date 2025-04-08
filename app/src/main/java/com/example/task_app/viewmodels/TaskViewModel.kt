import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.task_app.model.Task
import com.example.task_app.utils.KeyUtil
import com.example.task_app.utils.TaskPreferences

class TaskViewModel(private val taskPreferences: TaskPreferences) : ViewModel() {

    private val _filteredItems = MutableLiveData<List<Task>>()
    val filteredItems: LiveData<List<Task>> get() = _filteredItems

    private val _items: MutableLiveData<List<Task>> = taskPreferences.getTaskMutableList()
    val items: LiveData<List<Task>> get() = _items

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String> get() = _searchQuery

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
        updateFilteredItems()
    }

    fun updateFilteredItems() {
        val query = _searchQuery.value.orEmpty()
        _filteredItems.value =
            _items.value.orEmpty().filter { it.description.contains(query, ignoreCase = true) }
    }


    fun saveTaskDescription(description: String) {
        val currentList = _items.value.orEmpty().toMutableList()
        currentList.add(Task(key = KeyUtil.generateRandomKey(8), description = description))
        _items.value = currentList

        taskPreferences.saveTaskList(_items.value.orEmpty())
        updateFilteredItems()

    }

    fun deleteItem(position: Int) {
        val currentList = _items.value.orEmpty().toMutableList()
        if (position in currentList.indices) {
            currentList.removeAt(position)
            _items.value = currentList

            taskPreferences.saveTaskList(_items.value.orEmpty())
            updateFilteredItems()
        }
    }

    fun updateItem(position: Int, newDescription: String) {
        val currentList = _items.value.orEmpty().toMutableList()
        if (position in currentList.indices) {
            currentList[position] = currentList[position].copy(description = newDescription)
            _items.value = currentList

            taskPreferences.saveTaskList(_items.value.orEmpty())
            updateFilteredItems()
        }
    }

    fun markCompleted(position: Int) {
        val currentList = _items.value.orEmpty().toMutableList()
        if (position in currentList.indices) {
            currentList[position].isFinished = true
            _items.value = currentList

            taskPreferences.saveTaskList(_items.value.orEmpty())
            updateFilteredItems()
        }
    }

}