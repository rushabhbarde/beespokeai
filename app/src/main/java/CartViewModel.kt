import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CartViewModel: ViewModel() {
    private val _cartItems = MutableLiveData<List<Product>>()
    val cartItems: LiveData<List<Product>> = _cartItems

    fun addToCart(product: Product) {
        val currentList = _cartItems.value ?: emptyList()
        val newList = currentList.toMutableList()
        newList.add(product)
        _cartItems.value = newList
    }
}